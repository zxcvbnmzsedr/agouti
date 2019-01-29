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
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * URL执行器
 *
 * @author zhaotianzeng
 * @version V1.0
 * @date 2019-01-28 20:53
 */
@Slf4j
public class UrlActuator extends BaseActuator {
    /**
     * http method
     */
    private static final Pattern REQUEST_LINE_PATTERN = Pattern.compile("^([A-Z]+)[ ]*(.*)$");

    /**
     * path value replace
     */
    private static final Pattern VARIABLE_PATTERN = Pattern.compile("\\$\\{[^/]+?}");

    @Override
    protected Object invoke(Map<String, String> all,
                            String alias,
                            String method,
                            String target,
                            Map<String, Object> inputs) {

        String url = target + method;
        HttpMethod httpMethod;


        Matcher pathValueMatcher = VARIABLE_PATTERN.matcher(url);
        while (pathValueMatcher.find()) {
            String group = pathValueMatcher.group();
            String tempString = group.substring(2, group.length() - 1);
            String s = all.get("$" + tempString);
            if (s != null) {
                url = pathValueMatcher.replaceFirst(s);
            }

        }

        Matcher requestLineMatcher = REQUEST_LINE_PATTERN.matcher(url);
        Request.Builder builder = new Request.Builder();
        if (!requestLineMatcher.find()) {
            throw new IllegalStateException(String.format(
                    "RequestLine annotation didn't start with an HTTP verb on method %s",
                    target));
        } else {
            httpMethod = HttpMethod.valueOf(requestLineMatcher.group(1));
        }
        if (httpMethod == HttpMethod.GET) {
            StringBuilder param = new StringBuilder();

            inputs.forEach((k, v) -> param.append(k).append("=").append(v).append("&"));
            if (!param.toString().isEmpty()){
                param.insert(0, "?");
            }


            builder.url(requestLineMatcher.group(2) + param.toString());


        } else {
            builder.url(requestLineMatcher.group(2));
        }

        builder.method(httpMethod.name(), null);
        OkHttpClient client = new OkHttpClient();

        builder.addHeader("user-agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3578.80 Safari/537.36");
        Request request = builder.build();

        Response response = null;
        try {
            response = client.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            return JSONObject.parse(response.body().string());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}