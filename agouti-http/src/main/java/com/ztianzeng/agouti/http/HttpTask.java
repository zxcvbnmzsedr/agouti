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
package com.ztianzeng.agouti.http;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ztianzeng.agouti.core.Task;
import com.ztianzeng.agouti.core.WorkFlow;
import com.ztianzeng.agouti.core.WorkFlowTask;
import com.ztianzeng.agouti.core.executor.http.HttpMethod;
import lombok.Data;

/**
 * @author zhaotianzeng
 * @version V1.0
 * @date 2019-04-15 11:51
 */
public class HttpTask extends WorkFlowTask {
    public static final String REQUEST_PARAMETER_NAME = "http_request";

    protected ObjectMapper om = objectMapper();

    @Override
    public void start(WorkFlow workflow, Task task) {
        // request info
        Object request = task.getInputData().get(REQUEST_PARAMETER_NAME);

        if (request == null){
            task.setReasonForFail("");
            task.setStatus(Task.Status.FAILED);
            return;
        }
        Input input = om.convertValue(request, Input.class);


    }

    @Data
    public static class Input {

        private HttpMethod method;

        private String uri;

        private Object body;
    }


    private static ObjectMapper objectMapper() {
        final ObjectMapper om = new ObjectMapper();
        om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        om.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false);
        om.configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, false);
        om.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        om.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        return om;
    }
}