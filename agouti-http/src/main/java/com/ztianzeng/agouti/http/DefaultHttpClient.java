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

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ztianzeng.agouti.core.AgoutiException;
import com.ztianzeng.agouti.http.common.AgoutiHttpInput;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Objects;

import static com.ztianzeng.agouti.core.utils.JacksonUtils.*;

/**
 * @author zhaotianzeng
 * @version V1.0
 * @date 2019-04-18 14:55
 */
@Slf4j
public class DefaultHttpClient implements HttpClient {

    private ObjectMapper om = defaultMapper();


    /**
     * request implementation
     *
     * @param input input param
     * @return okhttp Response wrap to inner response
     */
    @Override
    public AgoutiHttpResponse httpCall(AgoutiHttpInput input) {
        OkHttpClient client = new OkHttpClient();


        Request.Builder builder = new Request.Builder();
        try {
            builder.url(input.getUri().toURL());
        } catch (MalformedURLException e) {
            throw new AgoutiException(e);
        }
        input.getHeaders().forEach(builder::header);
        Response response;

        try {
            RequestBody requestBody = RequestBody.create(
                    MediaType.parse(input.getAccept()),
                    om.writeValueAsString(input.getBody())
            );
            if (!Objects.equals(input.getMethod(), "GET")) {
                builder.method(input.getMethod(), requestBody);
            }

            response = client.newCall(builder.build()).execute();
            AgoutiHttpResponse responseWrapper = new AgoutiHttpResponse();
            responseWrapper.status = response.code();
            responseWrapper.body = extractBody(response.body());
            responseWrapper.headers = response.headers().toMultimap();

            return responseWrapper;

        } catch (IOException e) {
            throw new AgoutiException(e);
        }
    }

    private Object extractBody(ResponseBody responseBody) {
        if (responseBody == null) {
            return null;
        }
        String json = null;
        try {
            json = responseBody.string();
            log.info("HTTP response {}", json);
            JsonNode node = om.readTree(json);
            if (node == null) {
                return null;
            }
            if (node.isArray()) {
                return om.convertValue(node, listOfObj);
            } else if (node.isObject()) {
                return om.convertValue(node, mapOfObj);
            } else if (node.isNumber()) {
                return om.convertValue(node, Double.class);
            } else {
                return node.asText();
            }

        } catch (IOException jpe) {
            return json;
        }
    }


}