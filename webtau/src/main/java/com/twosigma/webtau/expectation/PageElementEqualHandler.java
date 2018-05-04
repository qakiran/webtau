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

package com.twosigma.webtau.expectation;

import com.twosigma.webtau.expectation.equality.EqualComparator;
import com.twosigma.webtau.expectation.equality.EqualComparatorHandler;
import com.twosigma.webtau.page.ElementValue;
import com.twosigma.webtau.page.PageElement;

import static com.twosigma.webtau.Ddjt.createActualPath;

public class PageElementEqualHandler implements EqualComparatorHandler {
    @Override
    public boolean handle(Object actual, Object expected) {
        return actual instanceof PageElement;
    }

    @Override
    public void compare(EqualComparator equalComparator, ActualPath actualPath, Object actual, Object expected) {
        PageElement actualPageElement = (PageElement) actual;
        ElementValue elementValue = actualPageElement.elementValue();
        Object actualValue = elementValue.get();

        equalComparator.compare(createActualPath(elementValue.getName()), actualValue, expected);
    }
}
