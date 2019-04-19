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

import com.ztianzeng.agouti.core.WorkFlowTask;
import com.ztianzeng.agouti.http.DefaultHttpClient;
import com.ztianzeng.agouti.http.HttpTask;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author zhaotianzeng
 * @version V1.0
 * @date 2019-04-18 20:02
 */
@Configuration
public class Config {



    @Bean
    public DefaultChooser loadBalancerClientFilter(LoadBalancerClient client) {
        return new DefaultChooser(client);
    }

    @Bean
    public WorkFlowTask workFlowTask(DefaultChooser defaultChooser) {
        return new HttpTask(new DefaultHttpClient(), defaultChooser);
    }

}