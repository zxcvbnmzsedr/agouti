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

import com.ztianzeng.agouti.core.WorkFlow;
import com.ztianzeng.agouti.core.WorkFlowTask;
import com.ztianzeng.common.tasks.Task;
import com.ztianzeng.common.workflow.TaskType;
import com.ztianzeng.common.workflow.WorkFlowDef;
import com.ztianzeng.common.workflow.WorkflowTask;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.LinkedList;
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
public class BaseExecutor {

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
        for (Task task : workFlow.getTasks()) {
            WorkFlowTask workFlowTask = WorkFlowTask.get(task.getTaskType().name());
            workFlowTask.start(workFlow, task);
            completeTask(workFlow, task);
        }
    }

    /**
     * handle task output
     *
     * @param workFlow
     */
    private void completeTask(WorkFlow workFlow, Task task) {
        if (StringUtils.isNotEmpty(task.getAlias())) {
            workFlow.getTampTaskResult().put(task.getAlias(), task.getOutputData());
        }
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

        List<Task> tasks = new LinkedList<>();
        for (WorkflowTask task : workFlowDef.getTasks()) {
            Task t = new Task();
            t.setName(task.getName());
            t.setTaskType(TaskType.valueOf(task.getType()));

            t.setInputData(task.getInputParameters());
            tasks.add(t);
        }
        workFlow.setTasks(tasks);

        return workFlow;
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
    protected Object invoke(Task task,
                            Map<String, String> all,
                            String alias,
                            String method,
                            String target,
                            Map<String, Object> inputs) {
        return null;
    }


    public void invoke(Map<String, String> invokeResult, Task next) {

    }
}