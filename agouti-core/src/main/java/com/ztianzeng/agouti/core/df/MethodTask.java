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

package com.ztianzeng.agouti.core.df;

import com.ztianzeng.agouti.core.AgoutiException;
import com.ztianzeng.agouti.core.WorkFlow;
import com.ztianzeng.agouti.core.WorkFlowTask;
import com.ztianzeng.common.tasks.Task;
import com.ztianzeng.common.workflow.TaskType;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author zhaotianzeng
 * @version V1.0
 * @date 2019-04-19 16:41
 */
public class MethodTask extends WorkFlowTask {

    /**
     * method work flow
     */
    public MethodTask() {
        super(TaskType.METHOD.name());
    }

    @Override
    public void start(WorkFlow workflow, Task task) {
        Map<String, Object> inputData = task.getInputData();
        String classS = (String) inputData.get("class");
        String methodS = (String) inputData.get("method");
        List param = inputData.get("param") == null ? new ArrayList() : (List) inputData.get("param");
        List<String> paramType = inputData.get("paramType") == null ? new ArrayList<>() : (List<String>) inputData.get("paramType");

        if (param.size() != paramType.size()) {
            throw new AgoutiException("param size != paramType size");
        }


        Class<?> aClass;
        try {
            aClass = Class.forName(classS);
            Class[] paramTypeClass = new Class[paramType.size()];
            for (int i = 0; i < paramType.size(); i++) {
                paramTypeClass[i] = Class.forName(paramType.get(i));
            }

            Method method = aClass.getMethod(methodS, paramTypeClass);
            Object o = aClass.getConstructor().newInstance();
            Object invokeResult = method.invoke(o, param.toArray());

            if (invokeResult != null) {
                workflow.getRuntimeParam().put("response", invokeResult);
            }
        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
            throw new AgoutiException(e);
        }

    }
}