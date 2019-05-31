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
import com.ztianzeng.agouti.core.utils.JsonPathUtils;
import com.ztianzeng.common.tasks.Task;
import com.ztianzeng.common.workflow.TaskType;
import com.ztianzeng.common.workflow.WorkFlowDef;
import com.ztianzeng.common.workflow.WorkflowTaskDef;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import reactor.core.publisher.Flux;

import java.util.HashMap;
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
public class DefaultExecutor {

    /**
     * start work flow
     *
     * @param workFlowDefinition work flow definition
     * @param workflowInput      work flow input
     * @return
     */
    public WorkFlow startWorkFlow(WorkFlowDef workFlowDefinition,
                                  Map<String, Object> workflowInput) {
        WorkFlow workFlow = convertWorkFlow(workFlowDefinition, workflowInput);

        Flux.fromIterable(workFlow.getTasks())
                .doOnComplete(() -> completeWorkFlow(workFlow, workFlowDefinition.getOutputParameters()))
                .subscribe(task ->
                        {
                            WorkFlowTask workFlowTask = WorkFlowTask.get(task.getTaskType().name());
                            workFlowTask.start(workFlow, task);
                            completeTask(workFlow, task);
                        }
                );


        return workFlow;
    }

    private void completeWorkFlow(WorkFlow workFlow, Map<String, Object> outputParameters) {
        workFlow.setStatus(WorkFlow.WorkFlowStatus.COMPLETED);
        workFlow.setOutputs(JsonPathUtils.extractResult(workFlow.getRuntimeParam(), outputParameters));
    }

    /**
     * handle task output
     *
     * @param workFlow
     */
    private void completeTask(WorkFlow workFlow, Task task) {
        if (StringUtils.isNotEmpty(task.getAlias())) {
            workFlow.getRuntimeParam().put(task.getAlias(), task.getOutputData());
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

        // handle input
        Map<String, Object> taskInput = getTaskInput(workFlow);
        workFlow.getRuntimeParam().putAll(taskInput);

        if (workflowInput != null) {
            workFlow.getInputs().putAll(workflowInput);
        }

        List<Task> tasks = new LinkedList<>();
        for (WorkflowTaskDef task : workFlowDef.getTasks()) {
            Task t = new Task();
            t.setName(task.getName());
            t.setTaskType(TaskType.valueOf(task.getType()));
            t.setAlias(task.getAlias());

            t.setInputData(task.getInputParameters());
            tasks.add(t);
        }
        workFlow.setTasks(tasks);

        return workFlow;
    }

    private Map<String, Object> getTaskInput(WorkFlow workFlow) {
        Map<String, Object> inputMap = new HashMap<>(5);

        Map<String, Object> workflowParams = new HashMap<>(5);
        workflowParams.put("input", workFlow.getInputs());
        workflowParams.put("status", workFlow.getStatus());
        inputMap.put("workflow", workflowParams);

        return inputMap;
    }

}