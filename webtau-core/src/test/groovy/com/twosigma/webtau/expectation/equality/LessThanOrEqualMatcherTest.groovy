/*
 * Copyright 2019 TWO SIGMA OPEN SOURCE, LLC
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

package com.twosigma.webtau.expectation.equality

import com.twosigma.webtau.expectation.ActualPath
import org.junit.Test

import static com.twosigma.webtau.expectation.equality.ActualExpectedTestReportExpectations.simpleActualExpectedWithIntegers

class LessThanOrEqualMatcherTest {
    private final int expected = 8
    private final ActualPath actualPath = new ActualPath("value")
    private final LessThanOrEqualMatcher matcher = new LessThanOrEqualMatcher(expected)

    @Test
    void "positive match less than"() {
        assertPositiveMatch(expected - 1)
    }

    @Test
    void "positive match equal"() {
        assertPositiveMatch(expected)
    }

    private void assertPositiveMatch(int actual) {
        assert matcher.matches(actualPath, actual)
        assert matcher.matchedMessage(actualPath, actual) == "less than or equal $expected\n" +
            'matches:\n\n' +
            simpleActualExpectedWithIntegers(actual, "less than or equal to", expected)
    }

    @Test
    void "positive mismatch"() {
        def actual = expected + 1
        assert !matcher.matches(actualPath, actual)
        assert matcher.mismatchedMessage(actualPath, actual) ==  "greater then $expected\n" +
            'mismatches:\n\n' +
            simpleActualExpectedWithIntegers(actual, "less than or equal to", expected)
    }

    @Test
    void "negative match"() {
        def actual = expected + 1
        assert matcher.negativeMatches(actualPath, actual)
        assert matcher.negativeMatchedMessage(actualPath, actual) == "greater than $expected\n" +
            'matches:\n\n' +
            simpleActualExpectedWithIntegers(actual, "greater than", expected)
    }

    @Test
    void "negative mismatch equal"() {
        assertNegativeMismatch(expected)
    }

    @Test
    void "negative mismatch less"() {
        assertNegativeMismatch(expected - 1)
    }

    private void assertNegativeMismatch(int actual) {
        assert !matcher.negativeMatches(actualPath, actual)
        assert matcher.negativeMismatchedMessage(actualPath, actual) == "value is less than or equal to $expected, but should be greater\n" +
            'mismatches:\n\n' +
            simpleActualExpectedWithIntegers(actual, "greater than", expected)
    }

    @Test
    void "matching message"() {
        assert matcher.matchingMessage() == "to be less than or equal to $expected"
    }

    @Test
    void "negative matching message"() {
        assert matcher.negativeMatchingMessage() == "to be greater than $expected"
    }
}
