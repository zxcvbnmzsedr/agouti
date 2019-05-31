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
import com.ztianzeng.agouti.core.AgoutiException;
import com.ztianzeng.agouti.core.WorkFlow;
import com.ztianzeng.agouti.core.WorkFlowTask;
import com.ztianzeng.agouti.core.utils.JsonPathUtils;
import com.ztianzeng.agouti.http.common.AgoutiHttpInput;
import com.ztianzeng.agouti.http.common.AgoutiServiceInstance;
import com.ztianzeng.common.tasks.Task;
import com.ztianzeng.common.workflow.TaskType;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import static com.ztianzeng.agouti.core.utils.JacksonUtils.defaultMapper;

/**
 * @author zhaotianzeng
 * @version V1.0
 * @date 2019-04-15 11:51
 */
@Slf4j
public class HttpTask extends WorkFlowTask {
    static final String REQUEST_PARAMETER_NAME = "http_request";

    private static final String LOAD_STR = "lb";

    private static final String MISSING_REQUEST = "Missing HTTP request. Task input MUST have a '" + REQUEST_PARAMETER_NAME + "' key with HttpTask.Input as value. See documentation for HttpTask for required input parameters";

    private ObjectMapper om = defaultMapper();


    private HttpClient httpClient;

    private AgoutiServiceInstanceChooser chooser;


    /**
     * http task
     */
    public HttpTask(HttpClient httpClient, AgoutiServiceInstanceChooser chooser) {
        super(TaskType.HTTP.name());
        this.httpClient = httpClient;
        this.chooser = chooser;
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
            task.setStatus(Task.Status.FAILED);
            task.setRuntimeResultData(MISSING_REQUEST);
            throw new AgoutiException(MISSING_REQUEST);
        }

        Input input = om.convertValue(request, Input.class);
        if (input.url == null) {
            String reason = "Missing HTTP URI.  See documentation for HttpTask for required input parameters";
            task.setRuntimeResultData(reason);
            task.setStatus(Task.Status.FAILED);
            throw new AgoutiException(reason);
        }

        URI uri = buildUri(workflow, input);

        if (uri.getScheme().equals(LOAD_STR)) {
            AgoutiServiceInstance choose = chooser.choose(uri.getHost());
            uri = URI.create(choose.getUri() + uri.getPath() + "?" + uri.getQuery());
        }


        if (input.method == null) {
            String reason = "No HTTP method specified";
            task.setStatus(Task.Status.FAILED);
            throw new AgoutiException(reason);
        }

        AgoutiHttpInput build = AgoutiHttpInput.builder()
                .accept(input.accept)
                .body(JsonPathUtils.replace(workflow.getRuntimeParam(), input.body))
                .headers(input.headers)
                .contentType(input.contentType)
                .uri(uri)
                .method(input.method)
                .build();

        AgoutiHttpResponse response = httpClient.httpCall(build);

        if (response.status > 199 && response.status < 300) {
            task.setStatus(Task.Status.COMPLETED);
            if (response.body != null) {
                task.setRuntimeResultData(response.body.toString());
            } else {
                task.setRuntimeResultData("No response from the remote service");
            }
        } else {
            task.setStatus(Task.Status.FAILED);
        }


        task.getOutputData().put("response", response.asMap());

    }

    private URI buildUri(WorkFlow workFlow, Input input) {

        StringBuilder urlSb = new StringBuilder(input.url);

        Map<String, Object> param = JsonPathUtils.extractResult(workFlow.getRuntimeParam(), input.param);
        if (param != null) {
            urlSb.append("?");
            param.forEach((k, v) -> {
                if (v != null) {
                    urlSb.append(k);
                    urlSb.append("=");
                    urlSb.append(v.toString());
                    urlSb.append("&");
                }
            });
        }


        return URI.create(urlSb.toString());
    }

    /**
     * @author zhaotianzeng
     * @version V1.0
     * @date 2019-04-18 14:57
     */
    @Data
    public static class Input {
        String method;

        String url;

        Object body;

        Map<String, Object> param;

        String accept = "application/json";

        String contentType = "application/json";

        Map<String, String> headers = new HashMap<>();
    }
}