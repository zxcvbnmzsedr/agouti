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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ztianzeng.agouti.core.WorkFlow;
import com.ztianzeng.agouti.core.WorkFlowTask;
import com.ztianzeng.common.tasks.Task;
import com.ztianzeng.common.workflow.TaskType;
import lombok.extern.slf4j.Slf4j;

import static com.ztianzeng.agouti.utils.JacksonUtils.defaultMapper;

/**
 * @author zhaotianzeng
 * @version V1.0
 * @date 2019-04-15 11:51
 */
@Slf4j
public class HttpTask extends WorkFlowTask {
    static final String REQUEST_PARAMETER_NAME = "http_request";

    private static final String MISSING_REQUEST = "Missing HTTP request. Task input MUST have a '" + REQUEST_PARAMETER_NAME + "' key with HttpTask.Input as value. See documentation for HttpTask for required input parameters";

    private ObjectMapper om = defaultMapper();


    private HttpClient httpClient;


    /**
     * http task
     */
    public HttpTask(HttpClient httpClient) {
        super(TaskType.HTTP.name());
        this.httpClient = httpClient;
    }

    /**
     * http task
     */
    public HttpTask() {
        super(TaskType.HTTP.name());
        this.httpClient = new DefaultHttpClient();
    }

    @Override
    public void start(WorkFlow workflow, Task task) {
        // request info
        Object request = task.getInputData().get(REQUEST_PARAMETER_NAME);

        if (request == null) {
            task.setReasonForFail(MISSING_REQUEST);
            task.setStatus(Task.Status.FAILED);
            return;
        }

        AgoutiHttpInput input = httpClient.handleInput(workflow, om.convertValue(request, AgoutiHttpInput.class));

        if (input.getUrl() == null) {
            String reason = "Missing HTTP URI.  See documentation for HttpTask for required input parameters";
            task.setReasonForFail(reason);
            task.setStatus(Task.Status.FAILED);
            return;
        }

        if (input.getMethod() == null) {
            String reason = "No HTTP method specified";
            task.setReasonForFail(reason);
            task.setStatus(Task.Status.FAILED);
            return;
        }


        AgoutiHttpResponse response = httpClient.httpCall(input);

        if (response.status > 199 && response.status < 300) {
            task.setStatus(Task.Status.COMPLETED);
            if (response.body != null) {
                task.setReasonForFail(response.body.toString());
            } else {
                task.setReasonForFail("No response from the remote service");
            }
        } else {
            task.setStatus(Task.Status.FAILED);
        }


        task.getOutputData().put("response", response.asMap());


    }

}