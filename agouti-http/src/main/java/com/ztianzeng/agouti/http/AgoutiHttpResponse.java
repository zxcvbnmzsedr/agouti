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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zhaotianzeng
 * @version V1.0
 * @date 2019-04-18 14:57
 */
public class AgoutiHttpResponse {

    int status;

    Map<String, List<String>> headers;

    Object body;

    public Map<String, Object> asMap() {

        Map<String, Object> map = new HashMap<>();
        map.put("body", body);
        map.put("headers", headers);
        map.put("status", status);

        return map;
    }
}