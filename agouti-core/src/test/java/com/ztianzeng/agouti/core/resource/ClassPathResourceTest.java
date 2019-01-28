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

package com.ztianzeng.agouti.core.resource;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author zhaotianzeng
 * @version V1.0
 * @date 2019-01-28 18:43
 */
public class ClassPathResourceTest {
    @Test
    public void read() {
        String path = "agouti/path.json";

        AbstractResource resource = new ClassPathResource(
                path, ClassLoader.getSystemClassLoader());

        Assert.assertEquals(resource.getFilename(), "path.json");

    }
}