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


import com.ztianzeng.agouti.core.AgoutiException;
import com.ztianzeng.agouti.core.Task;

/**
 * 执行器工厂
 *
 * @author zhaotianzeng
 * @version V1.0
 * @date 2019-01-28 21:10
 */
public final class ActuatorFactory {

    private ActuatorFactory() {

    }

    /**
     * 构造执行器
     *
     * @param taskType
     * @return
     */
    public static BaseActuator build(Task.TaskType taskType) {
        if (taskType.equals(Task.TaskType.FEIGN)) {
            return new FeignActuator();
        }
        if (taskType.equals(Task.TaskType.URL)) {
            return new FeignActuator();
        }
        throw new AgoutiException("json error ,not support taskType");
    }
}