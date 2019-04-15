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

package com.ztianzeng.agouti.core.executor;

import com.alibaba.fastjson.JSONObject;
import com.ztianzeng.agouti.core.WorkFlow;
import com.ztianzeng.common.tasks.Task;
import com.ztianzeng.common.workflow.WorkFlowDef;
import com.ztianzeng.common.workflow.WorkflowTask;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 基础执行器
 *
 * @author zhaotianzeng
 * @version V1.0
 * @date 2019-01-28 20:57
 */
@Slf4j
public abstract class BaseExecutor {

    /**
     * start work flow
     *
     * @param workFlowDefinition work flow definition
     * @param workflowInput      work flow input
     * @return
     */
    public void startWorkFlow(WorkFlowDef workFlowDefinition,
                              Map<String, Object> workflowInput) {
        WorkFlow workFlow = convertWorkFlow(workFlowDefinition, workflowInput);

        for (WorkflowTask task : workFlowDefinition.getTasks()) {
            Task t = new Task();
        }

    }


    /**
     * start work flow
     *
     * @param workFlow
     * @param tasks
     */
    public void startWorkFlow(WorkFlow workFlow, List<Task> tasks) {


    }

    /**
     * workFlowDef to WorkFlow
     */
    private WorkFlow convertWorkFlow(WorkFlowDef workFlowDef, Map<String, Object> workflowInput) {
        WorkFlow workFlow = new WorkFlow();
        workFlow.setName(workFlowDef.getName());
        workFlow.setDescription(workFlowDef.getDescription());
        workFlow.setStatus(WorkFlow.WorkFlowStatus.RUNNING);
        workFlow.setInputs(workflowInput);
        return workFlow;
    }

    private void handleResult(String prefix, Object invokeResult, Map<String, String> all) {
        if (invokeResult == null) {
            return;
        }
        if (invokeResult instanceof Collection) {
            Collection collection = (Collection) invokeResult;
            Iterator iterator = collection.iterator();
            int i = 0;
            while (iterator.hasNext()) {
                handleResult(prefix + "[" + (i++) + "]", iterator.next(), all);
            }
        } else if (invokeResult instanceof Map) {
            Map<String, Object> mapResult = (Map) invokeResult;
            handleMapResult(prefix + ".", mapResult, all);
        } else if (invokeResult instanceof String) {
            String k = (String) invokeResult;
            all.put(prefix, k);
        } else {
            all.put(prefix, JSONObject.toJSONString(invokeResult));
            Field[] fields = invokeResult.getClass().getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                try {
                    all.put(prefix + "." + field.getName(), field.get(invokeResult).toString());
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    /**
     * 处理MAP结果集
     *
     * @param mapResult
     * @param all
     */
    private void handleMapResult(String prefix, Map<String, Object> mapResult, Map<String, String> all) {

        mapResult.forEach((k, v) -> {
            if (v instanceof String
                    || v instanceof Number) {
                all.put(prefix + k, String.valueOf(v));
            } else if (v instanceof Collection) {
                Collection collection = (Collection) v;
                Iterator iterator = collection.iterator();
                int i = 0;
                while (iterator.hasNext()) {
                    handleResult(prefix + k + "[" + (i++) + "].", iterator.next(), all);
                }
            } else if (v instanceof Map) {
                handleMapResult(prefix + k + ".", (Map<String, Object>) v, all);
            }
        });
    }

    /**
     * 执行器
     *
     * @param task   当前执行的任务
     * @param all    已执行过的task的结果
     * @param alias  别名
     * @param method 方法
     * @param target 目标
     * @param inputs 入参
     * @return 执行结果
     */
    protected abstract Object invoke(Task task,
                                     Map<String, String> all,
                                     String alias,
                                     String method,
                                     String target,
                                     Map<String, Object> inputs);


}