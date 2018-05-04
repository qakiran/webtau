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

package com.twosigma.webtau.expectation.equality.handlers

import com.twosigma.webtau.data.DummyLiveValue
import org.junit.Test

import static com.twosigma.webtau.Ddjt.*

class LiveValueEqualHandlerTest {
    @Test
    void "handles instances of live value as actual and any other value as expected"() {
        def handler = new LiveValueEqualHandler()

        def liveValue = new DummyLiveValue([])

        assert handler.handle(liveValue, "hello")
        assert handler.handle(liveValue, 100)

        assert ! handler.handle(100, 100)
    }

    @Test
    void "calculates new value for comparison"() {
        def liveValue = new DummyLiveValue([1, 10, 100])
        actual(liveValue).should(equal(1))
        actual(liveValue).should(equal(10))
        actual(liveValue).should(equal(100))
    }
}
