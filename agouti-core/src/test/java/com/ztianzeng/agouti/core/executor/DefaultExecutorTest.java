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

package com.ztianzeng.agouti.core.executor;

import com.ztianzeng.agouti.core.WorkFlow;
import com.ztianzeng.agouti.core.WorkFlowTask;
import com.ztianzeng.common.tasks.Task;
import com.ztianzeng.common.workflow.WorkFlowDef;
import com.ztianzeng.common.workflow.WorkflowTask;
import org.junit.Test;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author zhaotianzeng
 * @version V1.0
 * @date 2019-04-16 09:46
 */
public class DefaultExecutorTest {

    @Test
    public void startWorkFlow() {
        DefaultExecutor defaultExecutor = new DefaultExecutor();
        WorkFlowDef workFlowDef = new WorkFlowDef();
        workFlowDef.setName("name");
        workFlowDef.setDescription("desc");

        WorkflowTask workflowTask = new WorkflowTask();

        workflowTask.setName("name");
        workflowTask.setType("SIMPLE");

        new SimpleTask();

        workFlowDef.setTasks(Collections.singletonList(workflowTask));


        Map<String, Object> workflowInput = new ConcurrentHashMap<>();

        workflowInput.put("input_1", "1");

        WorkFlow workFlow = defaultExecutor.startWorkFlow(workFlowDef, workflowInput);


    }

    class SimpleTask extends WorkFlowTask {

        SimpleTask() {
            super("SIMPLE");
        }

        @Override
        public void start(WorkFlow workflow, Task task) {
            System.out.println("task");
        }
    }
}