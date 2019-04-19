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
package com.ztianzeng.agouti.core;

import com.ztianzeng.common.tasks.Task;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author zhaotianzeng
 * @version V1.0
 * @date 2019-04-15 11:47
 */
public abstract class WorkFlowTask {
    private static ConcurrentHashMap<String, WorkFlowTask> registry = new ConcurrentHashMap<>();
    /**
     * task name
     */
    private String name;

    /**
     * base work flow
     *
     * @param name
     */
    public WorkFlowTask(String name) {
        this.name = name;
        registry.put(name, this);

    }

    /**
     * start work flow
     *
     * @param workflow wf
     * @param task     task
     */
    public abstract void start(WorkFlow workflow, Task task);

    public static WorkFlowTask get(String type) {
        return registry.get(type);
    }

}