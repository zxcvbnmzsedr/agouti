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
 *
 */

package com.ztianzeng.agouti.core.engine;

import com.alibaba.fastjson.JSONObject;
import com.ztianzeng.agouti.core.Task;
import com.ztianzeng.agouti.core.WorkFlow;
import lombok.extern.slf4j.Slf4j;

import java.util.Iterator;
import java.util.Objects;

/**
 * 核心引擎
 *
 * @author zhaotianzeng
 * @version V1.0
 * @date 2019-01-28 20:20
 */
@Slf4j
public class AgoutiEngine {
    /**
     * 执行器
     *
     * @param workFlow 服务流程
     * @param params   接口参数
     */
    public void invoke(WorkFlow workFlow, JSONObject params) {
        log.info("invoke workFlow name {} , desc {} ", workFlow.getName(), workFlow.getDescription());
        invoke(workFlow.getTasks().iterator());
    }

    /**
     * 具体的执行
     *
     * @param tasks 任务列表
     */
    private void invoke(Iterator<Task> tasks) {
        Objects invoke = null;
        while (tasks.hasNext()) {
            Task next = tasks.next();
            BaseActuator baseActuator = ActuatorFactory.build(next.getTaskType());
            invoke = baseActuator.invoke(invoke, next);
        }
    }
}