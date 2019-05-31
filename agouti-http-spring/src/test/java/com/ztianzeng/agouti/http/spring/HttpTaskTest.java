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

package com.ztianzeng.agouti.http.spring;

import com.ztianzeng.agouti.core.WorkFlow;
import com.ztianzeng.agouti.core.executor.DefaultExecutor;
import com.ztianzeng.agouti.core.parse.WorkFlowParse;
import com.ztianzeng.agouti.core.resource.AbstractResource;
import com.ztianzeng.agouti.core.resource.ClassPathResource;
import com.ztianzeng.common.workflow.WorkFlowDef;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author zhaotianzeng
 * @version V1.0
 * @date 2019-05-31 10:19
 */
public class HttpTaskTest {

    @Test
    public void startLbWorkFlow() {
        DefaultExecutor defaultExecutor = new DefaultExecutor();
        String path = "lb.json";
        AbstractResource resource = new ClassPathResource(
                path, ClassLoader.getSystemClassLoader());

        WorkFlowDef workFlowDef = WorkFlowParse.parse(resource);




        WorkFlow workFlow = defaultExecutor.startWorkFlow(workFlowDef, null);
        Object d1Key = workFlow.getOutputs().get("d1Key");
        Assert.assertEquals(d1Key, "input_key1");
    }
}