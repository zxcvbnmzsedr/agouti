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

package com.ztianzeng.agouti.core.parser;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ztianzeng.agouti.core.AgoutiException;
import com.ztianzeng.agouti.core.Task;
import com.ztianzeng.agouti.core.WorkFlow;
import com.ztianzeng.agouti.core.resource.AbstractResource;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zhaotianzeng
 * @version V1.0
 * @date 2019-01-28 18:16
 */
@Slf4j
public class Parser {
    /**
     * 解析
     *
     * @param resource
     * @return
     */
    public WorkFlow parse(AbstractResource resource) {
        JSONObject workFlowJSON;
        try {
            workFlowJSON = JSONObject.parseObject(resource.read(), JSONObject.class);

        } catch (IOException e) {
            throw new AgoutiException(e);
        }
        JSONArray taskJSONs = workFlowJSON.getJSONArray("tasks");
        List<Task> tasks = new ArrayList<>();

        for (Object t : taskJSONs) {
            JSONObject t1 = (JSONObject) t;
            Task task = new Task();
            task.setTarget(t1.getString("target"));
            task.setAlias(t1.getString("alias"));
            task.setMethod(t1.getString("method"));
            task.setInputs(handleInput(t1.getJSONObject("inputs"), t1.getJSONObject("inputsExtra")));
            tasks.add(task);
        }

        return new WorkFlow(workFlowJSON.getString("name"),
                workFlowJSON.getString("description"),
                workFlowJSON.get("outputs"),
                tasks);
    }

    /**
     * 处理输入参数
     *
     * @param inputs      输入参数
     * @param inputsExtra 输入参数类型
     */
    private Map<String, Object> handleInput(JSONObject inputs, JSONObject inputsExtra) {
        Map<String, Object> stringObjectMap = new HashMap<>(1);
        inputsExtra.forEach((k, v) -> {
            try {
                Class<?> aClass = Class.forName(String.valueOf(v));
                Object o = inputs.get(k);
                if (aClass.equals(o.getClass())) {
                    stringObjectMap.put(k, o);
                } else {
                    stringObjectMap.put(k, JSONObject.parseObject(o.toString(), aClass));
                }

            } catch (ClassNotFoundException e) {
                throw new AgoutiException("class not found " + v);
            }
        });
        return stringObjectMap;
    }

}