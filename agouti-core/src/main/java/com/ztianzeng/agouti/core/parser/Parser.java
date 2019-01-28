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

import com.alibaba.fastjson.JSONObject;
import com.ztianzeng.agouti.core.AgoutiException;
import com.ztianzeng.agouti.core.WorkFlow;
import com.ztianzeng.agouti.core.resource.AbstractResource;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

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
        WorkFlow workFlow;
        try {
            workFlow = JSONObject.parseObject(resource.read(), WorkFlow.class);
        } catch (IOException e) {
            throw new AgoutiException(e);
        }
        return workFlow;
    }
}