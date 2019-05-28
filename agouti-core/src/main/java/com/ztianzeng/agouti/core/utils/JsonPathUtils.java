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

package com.ztianzeng.agouti.core.utils;

import com.alibaba.fastjson.JSONObject;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;

import java.util.*;

/**
 * @author zhaotianzeng
 * @version V1.0
 * @date 2019-04-16 17:51
 */
public class JsonPathUtils {
    private JsonPathUtils() {
    }

    /**
     * out put
     *
     * @param outputParameters
     * @return
     */
    public static Map<String, Object> extractResult(Map<String, Object> inputs,
                                                    Map<String, Object> outputParameters) {
        if (outputParameters == null) {
            return Collections.emptyMap();
        }
        Configuration option = Configuration.defaultConfiguration().addOptions(Option.SUPPRESS_EXCEPTIONS);
        Map<String, Object> outMapper = JacksonUtils.convertValue(outputParameters,JacksonUtils.mapOfObj);

        Map<String, Object> resultMap = new HashMap<>(20);
        DocumentContext documentContext = JsonPath.parse(JSONObject.toJSONString(inputs), option);

        outMapper.forEach((k, v) -> {
            Object value = v;
            if (v instanceof String) {
                value = replaceVariables(value.toString(), documentContext);
            } else if (v instanceof Map) {
                //recursive call
                replace((Map<String, Object>) value, documentContext);
            } else if (v instanceof List) {
                value = replaceList((List<?>) value, documentContext);
            }
            resultMap.put(k, value);
        });

        return resultMap;
    }

    public static Object replace(Map<String, Object> input, Object json) {
        if (json == null || input == null) {
            return Collections.emptyMap();
        }
        Configuration option = Configuration.defaultConfiguration().addOptions(Option.SUPPRESS_EXCEPTIONS);
        DocumentContext documentContext = JsonPath.parse(input, option);

        Object value = json;
        if (json instanceof String) {
            value = replaceVariables(value.toString(), documentContext);
        } else if (json instanceof Map) {
            //recursive call
            replace((Map<String, Object>) value, documentContext);
        } else if (json instanceof List) {
            value = replaceList((List<?>) value, documentContext);
        }

        return value;
    }


    private static Map<String, Object> replace(Map<String, Object> input, DocumentContext documentContext) {
        for (Map.Entry<String, Object> e : input.entrySet()) {
            Object value = e.getValue();
            if (value instanceof String) {
                Object replaced = replaceVariables(value.toString(), documentContext);
                e.setValue(replaced);
            } else if (value instanceof Map) {
                //recursive call
                Object replaced = replace((Map<String, Object>) value, documentContext);
                e.setValue(replaced);
            } else if (value instanceof List) {
                Object replaced = replaceList((List<?>) value, documentContext);
                e.setValue(replaced);
            } else {
                e.setValue(value);
            }
        }
        return input;
    }

    private static Object replaceVariables(String paramString, DocumentContext documentContext) {
        String[] values = paramString.split("(?=\\$\\{)|(?<=\\})");
        Object[] convertedValues = new Object[values.length];
        for (int i = 0; i < values.length; i++) {
            convertedValues[i] = values[i];
            if (values[i].startsWith("${") && values[i].endsWith("}")) {
                String paramPath = values[i].substring(2, values[i].length() - 1);
                convertedValues[i] = documentContext.read(paramPath);
            }
        }

        Object retObj = convertedValues[0];
        // If the parameter String was "v1 v2 v3" then make sure to stitch it back
        if (convertedValues.length > 1) {
            for (int i = 0; i < convertedValues.length; i++) {
                Object val = convertedValues[i];
                if (val == null) {
                    val = "";
                }
                if (i == 0) {
                    retObj = val;
                } else {
                    retObj = retObj + "" + val.toString();
                }
            }

        }
        return retObj;
    }

    private static Object replaceList(List<?> values, DocumentContext io) {
        List<Object> replacedList = new LinkedList<>();
        for (Object listVal : values) {
            if (listVal instanceof String) {
                Object replaced = replaceVariables(listVal.toString(), io);
                replacedList.add(replaced);
            } else if (listVal instanceof Map) {
                Object replaced = replace((Map<String, Object>) listVal, io);
                replacedList.add(replaced);
            } else if (listVal instanceof List) {
                Object replaced = replaceList((List<?>) listVal, io);
                replacedList.add(replaced);
            } else {
                replacedList.add(listVal);
            }
        }
        return replacedList;
    }
}