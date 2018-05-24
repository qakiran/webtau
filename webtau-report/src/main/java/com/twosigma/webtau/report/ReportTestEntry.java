/*
 * Copyright 2018 TWO SIGMA OPEN SOURCE, LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.twosigma.webtau.report;

import com.twosigma.webtau.reporter.TestResultPayload;
import com.twosigma.webtau.reporter.TestStatus;
import com.twosigma.webtau.reporter.TestStep;
import com.twosigma.webtau.reporter.stacktrace.StackTraceCodeEntry;
import com.twosigma.webtau.reporter.stacktrace.StackTraceUtils;
import com.twosigma.webtau.utils.FileUtils;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.twosigma.webtau.reporter.TestStatus.Errored;
import static com.twosigma.webtau.reporter.TestStatus.Failed;
import static com.twosigma.webtau.reporter.TestStatus.Passed;
import static com.twosigma.webtau.reporter.TestStatus.Skipped;


public class ReportTestEntry {
    private String id;
    private String scenario;

    private Path filePath;

    private Throwable exception;

    private List<TestResultPayload> payloads;
    private List<TestStep<?>> steps;

    private boolean isRan;
    private Path workingDir;

    public ReportTestEntry(Path workingDir) {
        this.workingDir = workingDir;
        payloads = new ArrayList<>();
        steps = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getScenario() {
        return scenario;
    }

    public void setScenario(String scenario) {
        this.scenario = scenario;
    }

    public Path getFilePath() {
        return filePath;
    }

    public void setFilePath(Path filePath) {
        this.filePath = filePath;
    }

    public Throwable getException() {
        return exception;
    }

    public void setException(Throwable exception) {
        this.exception = exception;
    }

    public void setPayloads(List<TestResultPayload> payloads) {
        this.payloads = payloads;
    }

    public void setRan(boolean ran) {
        isRan = ran;
    }

    public String getAssertionMessage() {
        if (!(exception instanceof AssertionError)) {
            return null;
        }

        return exception.getMessage();
    }

    public boolean hasError() {
        return exception != null && !isFailed();
    }

    public boolean isSkipped() {
        return ! isRan;
    }

    public boolean isFailed() {
        return exception instanceof AssertionError;
    }

    public TestStatus getTestStatus() {
        if (isFailed()) {
            return Failed;
        }

        if (hasError()) {
            return Errored;
        }

        if (isSkipped()) {
            return Skipped;
        }

        return Passed;
    }

    public List<TestResultPayload> getPayloads() {
        return payloads;
    }

    public List<TestStep<?>> getSteps() {
        return steps;
    }

    public void addStep(TestStep<?> step) {
        steps.add(step);
    }

    public void addTestResultPayload(TestResultPayload testResultPayload) {
        payloads.add(testResultPayload);
    }

    public void setSteps(List<TestStep<?>> steps) {
        this.steps = steps;
    }

    public Map<String, ?> toMap() {
        Map<String, Object> result = new LinkedHashMap<>();

        result.put("id", id);
        result.put("scenario", scenario);
        result.put("status", getTestStatus().toString());

        if (filePath !=null) {
            result.put("fileName", filePath.toString());
        }

        if (exception != null) {
            result.put("assertion", getAssertionMessage());
            result.put("exceptionMessage", StackTraceUtils.fullCauseMessage(exception));
            result.put("failedCodeSnippets", extractFailedCodeSnippet(exception));
            result.put("fullStackTrace", StackTraceUtils.renderStackTrace(exception));
            result.put("shortStackTrace", StackTraceUtils.renderStackTraceWithoutLibCalls(exception));
        }

        payloads.forEach(p -> result.putAll(p.toMap()));

        return result;
    }

    private List<Map<String, ?>> extractFailedCodeSnippet(Throwable throwable) {
        List<StackTraceCodeEntry> entries = StackTraceUtils.extractLocalCodeEntries(throwable);
        return entries.stream()
                .filter(e -> Files.exists(workingDir.resolve(e.getFilePath())))
                .map(e -> {
                    Map<String, Object> entry = new LinkedHashMap<>();
                    entry.put("filePath", e.getFilePath());
                    entry.put("lineNumbers", e.getLineNumbers());
                    entry.put("snippet", FileUtils.fileTextContent(workingDir.resolve(e.getFilePath())));

                    return entry;
                }).collect(Collectors.toList());
    }
}