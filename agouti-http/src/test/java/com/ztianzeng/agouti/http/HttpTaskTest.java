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

package com.ztianzeng.agouti.http;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ztianzeng.agouti.core.executor.BaseExecutor;
import com.ztianzeng.agouti.core.executor.http.HttpMethod;
import com.ztianzeng.common.tasks.Task;
import com.ztianzeng.common.workflow.WorkFlowDef;
import com.ztianzeng.common.workflow.WorkflowTask;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author zhaotianzeng
 * @version V1.0
 * @date 2019-04-15 14:26
 */
public class HttpTaskTest {
    private static final String ERROR_RESPONSE = "Something went wrong!";

    private static final String TEXT_RESPONSE = "Text Response";

    private static final double NUM_RESPONSE = 42.42d;

    private static String JSON_RESPONSE;

    private HttpTask httpTask = new HttpTask();


    private static Server server;

    private static ObjectMapper objectMapper = new ObjectMapper();

    @BeforeClass
    public static void init() throws Exception {

        Map<String, Object> map = new HashMap<>();
        map.put("key", "value1");
        map.put("num", 42);
        JSON_RESPONSE = objectMapper.writeValueAsString(map);

        server = new Server(7009);
        ServletContextHandler servletContextHandler = new ServletContextHandler(server, "/", ServletContextHandler.SESSIONS);
        servletContextHandler.setHandler(new EchoHandler());
        server.start();
    }

    @Test
    public void startWorkFlow() {
        BaseExecutor baseExecutor = new BaseExecutor();
        WorkFlowDef workFlowDef = new WorkFlowDef();
        workFlowDef.setName("name");
        workFlowDef.setDescription("desc");

        WorkflowTask workflowTask = new WorkflowTask();

        workflowTask.setName("name");
        workflowTask.setType("HTTP");
        HttpTask.Input input = new HttpTask.Input();
        input.setUri("http://localhost:7009/post");

        Map<String, Object> body = new HashMap<>();
        body.put("input_key1", "value1");
        body.put("input_key2", 45.3d);
        input.setBody(body);
        input.setMethod(HttpMethod.POST);
        workflowTask.getInputParameters().put(HttpTask.REQUEST_PARAMETER_NAME, input);
        workFlowDef.setTasks(Collections.singletonList(workflowTask));
        baseExecutor.startWorkFlow(workFlowDef, null);
    }

    @Test
    public void testPost() {
        Task task = new Task();
        HttpTask.Input input = new HttpTask.Input();
        input.setUri("http://localhost:7009/post");

        Map<String, Object> body = new HashMap<>();
        body.put("input_key1", "value1");
        body.put("input_key2", 45.3d);
        input.setBody(body);

        input.setMethod(HttpMethod.POST);

        task.getInputData().put(HttpTask.REQUEST_PARAMETER_NAME, input);

        httpTask.start(null, task);

        assertEquals(task.getReasonForFail(), Task.Status.COMPLETED, task.getStatus());

        Map<String, Object> hr = (Map<String, Object>) task.getOutputData().get("response");
        Object response = hr.get("body");
        assertEquals(Task.Status.COMPLETED, task.getStatus());
        assertTrue("response is: " + response, response instanceof Map);
    }


    @AfterClass
    public static void cleanup() {
        if (server != null) {
            try {
                server.stop();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static class EchoHandler extends AbstractHandler {

        private TypeReference<Map<String, Object>> mapOfObj = new TypeReference<Map<String, Object>>() {
        };

        @Override
        public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response)
                throws IOException {
            if (request.getMethod().equals("GET") && request.getRequestURI().equals("/text")) {
                PrintWriter writer = response.getWriter();
                writer.print(TEXT_RESPONSE);
                writer.flush();
                writer.close();
            } else if (request.getMethod().equals("GET") && request.getRequestURI().equals("/json")) {
                response.addHeader("Content-Type", "application/json");
                PrintWriter writer = response.getWriter();
                writer.print(JSON_RESPONSE);
                writer.flush();
                writer.close();
            } else if (request.getMethod().equals("GET") && request.getRequestURI().equals("/failure")) {
                response.addHeader("Content-Type", "text/plain");
                response.setStatus(500);
                PrintWriter writer = response.getWriter();
                writer.print(ERROR_RESPONSE);
                writer.flush();
                writer.close();
            } else if (request.getMethod().equals("POST") && request.getRequestURI().equals("/post")) {
                response.addHeader("Content-Type", "application/json");
                BufferedReader reader = request.getReader();
                Map<String, Object> input = objectMapper.readValue(reader, mapOfObj);
                Set<String> keys = input.keySet();
                for (String key : keys) {
                    input.put(key, key);
                }
                PrintWriter writer = response.getWriter();
                writer.print(objectMapper.writeValueAsString(input));
                writer.flush();
                writer.close();
            } else if (request.getMethod().equals("POST") && request.getRequestURI().equals("/post2")) {
                response.addHeader("Content-Type", "application/json");
                response.setStatus(204);
                BufferedReader reader = request.getReader();
                Map<String, Object> input = objectMapper.readValue(reader, mapOfObj);
                Set<String> keys = input.keySet();
                System.out.println(keys);
                response.getWriter().close();

            } else if (request.getMethod().equals("GET") && request.getRequestURI().equals("/numeric")) {
                PrintWriter writer = response.getWriter();
                writer.print(NUM_RESPONSE);
                writer.flush();
                writer.close();
            } else if (request.getMethod().equals("POST") && request.getRequestURI().equals("/oauth")) {
                //echo back oauth parameters generated in the Authorization header in the response
                response.addHeader("Content-Type", "application/json");
                PrintWriter writer = response.getWriter();
                writer.flush();
                writer.close();
            }
        }

    }
}