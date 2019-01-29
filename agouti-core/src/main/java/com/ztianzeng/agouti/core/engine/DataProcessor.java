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

package com.ztianzeng.agouti.core.engine;

import com.alibaba.fastjson.JSONObject;
import com.ztianzeng.agouti.core.AgoutiException;
import com.ztianzeng.agouti.core.WorkFlow;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 * data handle
 *
 * @author zhaotianzeng
 * @version V1.0
 * @date 2019-01-29 17:14
 */
@Slf4j
public class DataProcessor {
    public final Map<String, String> INVOKE_RESULT = new HashMap<>(10);

    private static DataProcessor dataProcessor = null;

    private static Class<? extends DataProcessor> contextClass = DataProcessor.class;

    private static final ThreadLocal<? extends DataProcessor> THREAD_LOCAL = ThreadLocal.withInitial(() -> {

        try {
            return contextClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new AgoutiException(e);
        }
    });

    public static DataProcessor getCurrentContext() {
        if (dataProcessor != null) {
            return dataProcessor;
        }
        return THREAD_LOCAL.get();
    }


    public Object getResult(WorkFlow workFlow) {
        Map<String, Object> result = new HashMap<>(10);

        JSONObject outputs = workFlow.getOutputs();
        outputs.forEach((k, v) -> result.put(k, handleValue(INVOKE_RESULT, v)));
        log.debug("output {} ", result);
        return result;
    }

    private Object handleValue(Map<String, String> invokeResult, Object v) {
        if (v instanceof String) {
            String v1 = (String) v;
            return invokeResult.get(v1);
        }
        return invokeResult;
    }
}