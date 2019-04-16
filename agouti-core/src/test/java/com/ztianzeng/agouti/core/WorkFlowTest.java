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

package com.ztianzeng.agouti.core;

import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * @author zhaotianzeng
 * @version V1.0
 * @date 2019-04-16 11:19
 */
public class WorkFlowTest {

    @Test
    public void getOutputs() {
        WorkFlow workFlow = new WorkFlow();

        Map<String, Object> d1Res = new HashMap<>();
        d1Res.put("key1", "key1111Val");
        d1Res.put("key2", "key2222Val");
        workFlow.getInputs().put("d1", d1Res);

        Map<String, Object> outputParameters = new HashMap<>();
        outputParameters.put("name","${d1.key1}");
        outputParameters.put("key1","${d1}");
        Map<String, Object> outputs = workFlow.getOutputs(outputParameters);
        Assert.assertEquals(outputs.get("name"),"key1111Val");
    }
}