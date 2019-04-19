/*
 * Copyright 2018-2019 zTianzeng Group Holding Ltd.
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

package com.ztianzeng.agouti.test.controller;

import com.ztianzeng.agouti.core.WorkFlow;
import com.ztianzeng.agouti.core.executor.BaseExecutor;
import com.ztianzeng.agouti.core.parse.WorkFlowParse;
import com.ztianzeng.common.workflow.WorkFlowDef;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.ribbon.SpringClientFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zhaotianzeng
 * @version V1.0
 * @date 2019-04-17 20:35
 */
@RestController
@RequestMapping("/test")
public class TestController {

    @GetMapping("/getTest1")
    public Object getTest1() {
        BaseExecutor baseExecutor = new BaseExecutor();
        WorkFlowDef workFlowDef = WorkFlowParse.fromResource("getTest1.json");

        WorkFlow workFlow = baseExecutor.startWorkFlow(workFlowDef, null);
        return workFlow.getOutputs();
    }

    @GetMapping("/ribbonGetTest1")
    public Object ribbonGetTest1() {
        BaseExecutor baseExecutor = new BaseExecutor();
        WorkFlowDef workFlowDef = WorkFlowParse.fromResource("ribbonGetTest1.json");

        WorkFlow workFlow = baseExecutor.startWorkFlow(workFlowDef, null);
        return workFlow.getOutputs();
    }
}