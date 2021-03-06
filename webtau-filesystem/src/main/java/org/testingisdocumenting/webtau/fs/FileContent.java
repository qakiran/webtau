/*
 * Copyright 2020 webtau maintainers
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

package org.testingisdocumenting.webtau.fs;

import org.testingisdocumenting.webtau.expectation.ActualPath;
import org.testingisdocumenting.webtau.expectation.ActualPathAware;
import org.testingisdocumenting.webtau.expectation.ActualValueExpectations;
import org.testingisdocumenting.webtau.expectation.ValueMatcher;
import org.testingisdocumenting.webtau.expectation.timer.ExpectationTimer;
import org.testingisdocumenting.webtau.reporter.IntegrationTestsMessageBuilder;
import org.testingisdocumenting.webtau.reporter.StepReportOptions;
import org.testingisdocumenting.webtau.reporter.ValueMatcherExpectationSteps;
import org.testingisdocumenting.webtau.utils.FileUtils;

import java.nio.file.Path;

import static org.testingisdocumenting.webtau.reporter.TokenizedMessage.tokenizedMessage;

public class FileContent implements ActualValueExpectations, ActualPathAware {
    private final ActualPath actualPath;
    private final Path path;

    public FileContent(Path path) {
        this.actualPath = new ActualPath("file <" + path.getFileName().toString() + ">");
        this.path = path;
    }

    public String getContent() {
        return FileUtils.fileTextContent(path);
    }

    @Override
    public ActualPath actualPath() {
        return actualPath;
    }

    @Override
    public void should(ValueMatcher valueMatcher) {
        ValueMatcherExpectationSteps.shouldStep(null, this, StepReportOptions.REPORT_ALL,
                tokenizedMessage(IntegrationTestsMessageBuilder.id(actualPath().getPath())), valueMatcher);
    }

    @Override
    public void shouldNot(ValueMatcher valueMatcher) {
        ValueMatcherExpectationSteps.shouldNotStep(null, this, StepReportOptions.REPORT_ALL,
                tokenizedMessage(IntegrationTestsMessageBuilder.id(actualPath().getPath())), valueMatcher);
    }

    @Override
    public void waitTo(ValueMatcher valueMatcher, ExpectationTimer expectationTimer, long tickMillis, long timeOutMillis) {
        ValueMatcherExpectationSteps.waitStep(null, this, StepReportOptions.REPORT_ALL,
                tokenizedMessage(IntegrationTestsMessageBuilder.id(actualPath().getPath())), valueMatcher,
                expectationTimer, tickMillis, timeOutMillis);
    }

    @Override
    public void waitToNot(ValueMatcher valueMatcher, ExpectationTimer expectationTimer, long tickMillis, long timeOutMillis) {
        ValueMatcherExpectationSteps.waitNotStep(null, this, StepReportOptions.REPORT_ALL,
                tokenizedMessage(IntegrationTestsMessageBuilder.id(actualPath().getPath())), valueMatcher,
                expectationTimer, tickMillis, timeOutMillis);
    }
}
