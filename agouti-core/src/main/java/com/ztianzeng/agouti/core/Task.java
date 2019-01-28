package com.ztianzeng.agouti.core;

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
     * 调用的目标
     */
    private String target;

    private String alias;

    private String method;

    /**
     * 入参情况，多个入参通过key:value的结构书写，key的类别通过下面的inputsExtra定义。
     */
    private Map<String, Object> inputs;

    /**
     * 入参key的类别定义
     */
    private Map<String, String> inputsExtra;


}