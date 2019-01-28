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

import com.ztianzeng.agouti.core.Task;

import java.util.Map;
import java.util.Objects;

/**
 * 基础执行器
 *
 * @author zhaotianzeng
 * @version V1.0
 * @date 2019-01-28 20:57
 */
public abstract class BaseActuator {

    /**
     * 执行任务
     *
     * @param before
     * @param task
     * @return
     */
    public Objects invoke(Object before, Task task) {
        return invoke(before, task.getAlias(), task.getMethod(), task.getTarget(), task.getInputs());
    }

    /**
     * 执行器
     *
     * @param before 上一个task执行的结果
     * @param alias  别名
     * @param method 方法
     * @param target 目标
     * @param inputs 入参
     * @return 执行结果
     */
    protected abstract Objects invoke(Object before, String alias, String method, String target, Map<String, Object> inputs);


}