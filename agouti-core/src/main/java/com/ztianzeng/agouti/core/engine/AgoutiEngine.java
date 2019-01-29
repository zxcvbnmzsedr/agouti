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
import com.ztianzeng.agouti.core.AgoutiException;
import com.ztianzeng.agouti.core.Task;
import com.ztianzeng.agouti.core.WorkFlow;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

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
    public Object invoke(WorkFlow workFlow, JSONObject params) {
        if (workFlow == null) {
            throw new AgoutiException("workFlow is null");
        }
        log.info("invoke work flow name {} , desc {} ", workFlow.getName(), workFlow.getDescription());
        Map<String, String> invokeResult = invoke(workFlow.getTasks().iterator());

        log.debug("all task invoke result {}", invokeResult);

        Map<String, Object> result = new HashMap<>(10);


        JSONObject outputs = workFlow.getOutputs();
        outputs.forEach((k, v) -> result.put(k, handleValue(invokeResult, v)));

        log.debug("output {} ", result);
        return result;
    }

    private Object handleValue(Map<String, String> invokeResult, Object v) {
        if (v instanceof String) {
            String v1 = (String) v;
            return invokeResult.get(v1);
        }
        return invokeResult;
    }

    /**
     * 具体的执行
     *
     * @param tasks 任务列表
     */
    private Map<String, String> invoke(Iterator<Task> tasks) {
        Map<String, String> invokeResult = new HashMap<>(10);
        while (tasks.hasNext()) {
            Task next = tasks.next();
            BaseExecutor baseExecutor = ActuatorFactory.build(next.getTaskType());
            baseExecutor.invoke(invokeResult, next);
        }
        return invokeResult;
    }
}