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

package com.ztianzeng.agouti.core.executor.method;

import com.ztianzeng.agouti.core.executor.BaseExecutor;
import com.ztianzeng.common.tasks.Task;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zhaotianzeng
 * @version V1.0
 * @date 2019-01-29 20:04
 */
public class MethodExecutorTest {

    @Test
    public void invoke() {
        BaseExecutor baseExecutor = new MethodExecutor();
        Map<String, String> all = new HashMap<>();

        Task task = new Task();
        task.setTaskType(Task.TaskType.METHOD);
        task.setTarget("com.ztianzeng.agouti.core.executor.method.TestFeignInterfaceClass");
        task.setMethod("print");


        Task task2 = new Task();
        task2.setTaskType(Task.TaskType.METHOD);
        task2.setTarget("com.ztianzeng.agouti.core.executor.method.TestFeignClass");
        task2.setMethod("print");

        baseExecutor.invoke(all, task);

        baseExecutor.invoke(all, task2);
    }

}