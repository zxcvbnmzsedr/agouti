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

package com.ztianzeng.agouti.core.executor.feign;

import com.ztianzeng.agouti.core.AgoutiException;
import com.ztianzeng.agouti.core.Task;
import com.ztianzeng.agouti.core.executor.BaseExecutor;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;

/**
 * feign executor
 *
 * @author zhaotianzeng
 * @version V1.0
 * @date 2019-01-29 17:43
 */
public class FeignExecutor extends BaseExecutor {

    @Override
    protected Object invoke(Task task,
                            Map<String, String> all,
                            String alias,
                            String method,
                            String target,
                            Map<String, Object> inputs) {
        try {
            Class<?> aClass = Class.forName(target);


            InvocationHandler handler = new DefaultInvocationHandler(aClass);

            Object o = Proxy.newProxyInstance(aClass.getClassLoader(), new Class<?>[]{aClass}, handler);

            Method me = o.getClass().getDeclaredMethod(method);
            me.invoke(o);
        } catch (ClassNotFoundException | NoSuchMethodException e) {
            throw new AgoutiException(e);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new AgoutiException("invoke method error " + target + " " + method);
        }


        return null;
    }

    static class DefaultInvocationHandler implements InvocationHandler {

        private final Class target;

        DefaultInvocationHandler(Class target) {
            this.target = target;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if ("equals".equals(method.getName())) {
                try {
                    Object otherHandler =
                            args.length > 0 && args[0] != null ? Proxy.getInvocationHandler(args[0]) : null;
                    return equals(otherHandler);
                } catch (IllegalArgumentException e) {
                    return false;
                }
            } else if ("hashCode".equals(method.getName())) {
                return hashCode();
            } else if ("toString".equals(method.getName())) {
                return toString();
            }

            return method.invoke(proxy, args);
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof DefaultInvocationHandler) {
                DefaultInvocationHandler other = (DefaultInvocationHandler) obj;
                return target.equals(other.target);
            }
            return false;
        }

        @Override
        public int hashCode() {
            return target.hashCode();
        }

        @Override
        public String toString() {
            return target.toString();
        }
    }
}