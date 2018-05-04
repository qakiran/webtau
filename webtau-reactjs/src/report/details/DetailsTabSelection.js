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

import React from 'react'

import './DetailsTabSelection.css'

const DetailsTabSelection = ({tabs, selectedTabName, onTabSelection}) => {
    return (
        <div className="details-tab-selection">
            <div className="tab-names">
                {tabs.map(t => {
                    const className = "tab-name" + (selectedTabName === t ? " selected" : "")
                    return <div key={t} className={className} onClick={() => onTabSelection(t)}>{t}</div>
                })}
            </div>
        </div>
    )
}

export default DetailsTabSelection
