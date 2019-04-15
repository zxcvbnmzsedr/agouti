package com.ztianzeng.common.workflow;

import javax.validation.constraints.NotEmpty;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author zhaotianzeng
 * @version V1.0
 * @date 2019-04-15 11:24
 */
public class WorkflowTask {

    @NotEmpty(message = "WorkflowTask name cannot be empty or null")
    private String name;

    /**
     * input parameters
     */
    private Map<String, Object> inputParameters = new LinkedHashMap<>();
    
}