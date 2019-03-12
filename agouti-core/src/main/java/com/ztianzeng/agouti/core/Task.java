package com.ztianzeng.agouti.core;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * 具体任务
 *
 * @author zhaotianzeng
 * @version V1.0
 * @date 2019-01-28 17:39
 */
@NoArgsConstructor
@Data
public class Task {
    /**
     * 调用类型
     */
    private TaskType taskType;
    /**
     * 调用的目标
     */
    private String target;

    private String alias;

    private String method;

    private JSONObject headers;

    /**
     * 入参情况，多个入参通过key:value的结构书写
     */
    private Map<String, Object> originInputs;

    private Map<String, Object> inputs;


    public enum TaskType {
        /**
         * FEIGN 调用
         */
        METHOD,
        /**
         * URL调用
         */
        URL;
    }
}