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

import java.io.InputStream;

/**
 * 资源基类
 *
 * @author zhaotianzeng
 * @version V1.0
 * @date 2019-01-28 18:02
 */
public abstract class AbstractResource implements Resource {
    /**
     * 读取
     *
     * @return 输入流
     */
    public abstract InputStream read();
}