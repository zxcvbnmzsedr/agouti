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
import lombok.extern.slf4j.Slf4j;

import java.util.Iterator;
import java.util.Map;

/**
 * 基础执行器
 *
 * @author zhaotianzeng
 * @version V1.0
 * @date 2019-01-28 20:57
 */
@Slf4j
public abstract class BaseActuator {

    /**
     * 执行任务
     *
     * @param all  已执行过的task的结果
     * @param task
     * @return
     */
    public void invoke(Map<String, String> all, Task task) {
        Object invokeResult = invoke(all, task.getAlias(), task.getMethod(), task.getTarget(), task.getInputs());
        log.info("task {} invoke result {} ", invokeResult);
        if (invokeResult instanceof Iterator) {
            log.debug("iterable result");
            Iterator iterableResult = (Iterator) invokeResult;

        }

        if (invokeResult instanceof Map) {
            log.debug("map result");
            Map<String, Object> mapResult = (Map) invokeResult;
            mapResult.forEach((k, v) -> {
                if (v instanceof String
                        || v instanceof Number) {
                    all.put(k, String.valueOf(v));
                } else if (v instanceof Iterator) {
                    Iterator iterator = (Iterator) v;
                    int p = 0;
                    while (iterator.hasNext()) {
                        all.put(k + iterator.toString() + (p++), iterator.toString());
                    }
                } else if (v instanceof Map) {

                }
            });

        }

    }

    /**
     * 执行器
     *
     * @param all    已执行过的task的结果
     * @param alias  别名
     * @param method 方法
     * @param target 目标
     * @param inputs 入参
     * @return 执行结果
     */
    protected abstract Object invoke(Map<String, String> all, String alias, String method, String target, Map<String, Object> inputs);


}