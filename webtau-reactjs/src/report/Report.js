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

import TestSteps from './details/TestSteps'
import TestHttpCalls from './details/http/TestHttpCalls'
import ShortStackTrace from './details/ShortStackTrace'
import Screenshot from './details/Screenshot'
import FullStackTrace from './details/FullStackTrace'
import Summary from './details/Summary'
import StatusEnum from './StatusEnum'

class Report {
    static overallHttpCallTimeForTest(test) {
        const httpCalls = test.httpCalls || []
        const times = httpCalls.map(c => c.elapsedTime)
        return times.reduce((a, r) => a + r, 0)
    }

    static averageHttpCallTimeForTest(test) {
        if (!test.httpCalls) {
            return 0
        }

        const overallTime = Report.overallHttpCallTimeForTest(test)
        return overallTime / test.httpCalls.length
    }

    constructor(report) {
        this.report = report
        this.tests = enrichTestsData(report.tests)
        this.httpCalls = extractHttpCalls(this.tests)
        this.testsSummary = report.summary
        this.httpCallsSummary = buildHttpCallsSummary(this.httpCalls)
    }

    findTestById(id) {
        const found = this.tests.filter(t => t.id === id)
        return found.length ? found[0] : null
    }

    findHttpCallByIdx(idx) {
        return (idx >= 0 && idx < this.httpCalls.length) ? this.httpCalls[idx] : null
    }

    numberOfHttpCalls() {
        return this.httpCalls.length
    }

    overallHttpCallTime() {
        return this.httpCalls
            .map(c => c.elapsedTime)
            .reduce((prev, curr) => prev + curr, 0)
    }

    averageHttpCallTime() {
        const n = this.numberOfHttpCalls()
        if (!n) {
            return 0
        }

        return this.overallHttpCallTime() / n
    }

    testsWithStatusAndFilteredByText(status, text) {
        return this.tests.filter(t => statusFilterPredicate(t.status, status) &&
            textFilterPredicate(t.scenario, text))
    }

    httpCallsWithStatusAndFilteredByText(status, text) {
        return this.httpCalls.filter(c => statusFilterPredicate(c.status, status) &&
            (textFilterPredicate(c.shortUrl, text) || textStartOnlyFilterPredicate(c.method, text)))
    }

    hasTestWithId(id) {
        return this.findTestById(id) !== null
    }

    hasDetailWithTabName(testId, tabName) {
        const test = this.findTestById(testId)
        if (!test) {
            return false
        }

        return test.details.filter(d => d.tabName === tabName).length !== 0
    }

    firstDetailTabName(testId) {
        const test = this.findTestById(testId)
        if (!test) {
            return ''
        }

        return test.details[0].tabName
    }
}

function extractHttpCalls(tests) {
    return tests
        .filter(t => t.httpCalls)
        .map(t => enrichHttpCallsData(t, t.httpCalls)).reduce((acc, r) => acc.concat(r), [])
}

function statusFilterPredicate(actualStatus, status) {
    if (!status || status === 'Total') {
        return true
    }

    return actualStatus === status
}

function textFilterPredicate(actualText, text) {
    return lowerCaseIndexOf(actualText, text) !== -1
}

function textStartOnlyFilterPredicate(actualText, text) {
    return lowerCaseIndexOf(actualText, text) === 0
}

function lowerCaseIndexOf(text, part) {
    if (! text) {
        return -1
    }

    return text.toLowerCase().indexOf(part.toLowerCase())
}

function enrichTestsData(tests) {
    return tests.map(test => ({
            ...test,
            details: additionalDetails(test)
        }))
}

function enrichHttpCallsData(test, httpCalls) {
    return httpCalls.map(httpCall => enrichHttpCallData(test, httpCall))
}

function buildHttpCallsSummary(httpCalls) {
    return {
        total: httpCalls.length,
        passed: httpCalls.filter(c => c.status === StatusEnum.PASSED).length,
        failed: httpCalls.filter(c => c.status === StatusEnum.FAILED).length,
        skipped: 0,
        errored: 0
    }
}

function deriveHttpCallStatus(httpCall) {
    if (httpCall.mismatches.length > 0) {
        return StatusEnum.FAILED
    }

    return StatusEnum.PASSED
}

function enrichHttpCallData(test, httpCall) {
    const shortUrl = removeHostFromUrl(httpCall.url)

    return {
        ...httpCall,
        shortUrl,
        test,
        status: deriveHttpCallStatus(httpCall),
        label: httpCall.method + ' ' + shortUrl
    }
}

function removeHostFromUrl(url) {
    const doubleSlashPattern = '://'
    const doubleSlashStartIdx = url.indexOf(doubleSlashPattern)

    if (doubleSlashStartIdx === -1) {
        return url
    }

    const firstUrlSlashIdx = url.indexOf('/', doubleSlashStartIdx + doubleSlashPattern.length)
    return url.substr(firstUrlSlashIdx)
}

function additionalDetails(test) {
    const details = []
    details.push({tabName: 'Summary', component: Summary})

    if (test.hasOwnProperty('screenshot')) {
        details.push({tabName: 'Screenshot', component: Screenshot})
    }

    if (test.hasOwnProperty('httpCalls')) {
        details.push({tabName: 'HTTP calls', component: TestHttpCalls})
    }

    if (test.hasOwnProperty('steps')) {
        details.push({tabName: 'Steps', component: TestSteps})
    }

    if (test.hasOwnProperty('shortStackTrace')) {
        details.push({tabName: 'StackTrace', component: ShortStackTrace})
    }

    if (test.hasOwnProperty('fullStackTrace')) {
        details.push({tabName: 'Full StackTrace', component: FullStackTrace})
    }

    return details
}

export default Report