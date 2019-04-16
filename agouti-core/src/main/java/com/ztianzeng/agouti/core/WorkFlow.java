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


import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;
import com.ztianzeng.common.tasks.Task;
import com.ztianzeng.common.workflow.WorkFlowDef;
import lombok.Data;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 任务编排流程
 *
 * @author zhaotianzeng
 * @version V1.0
 * @date 2019-01-28 17:52
 */
@Data
public class WorkFlow {

    public enum WorkFlowStatus {
        /**
         * RUNNING
         */
        RUNNING,
        /**
         *
         */
        COMPLETED;

    }

    private String name;

    private String description;


    private List<Task> tasks;

    private WorkFlowStatus status;

    private WorkFlowDef workflowDefinition;

    private Map<String, Object> inputs = new ConcurrentHashMap<>();

    private Map<String, Object> runtimeParam = new ConcurrentHashMap<>();

    private Map<String, Object> outputs = new HashMap<>();



}