package com.ztianzeng.common.workflow;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * the work flow initial define
 * <p>
 * as a parameter to receive json file
 *
 * @author zhaotianzeng
 * @version V1.0
 * @date 2019-04-15 11:16
 */
@Data
public class WorkFlowDef {

    @NotEmpty(message = "WorkflowDef name cannot be null or empty")
    private String name;

    private String description;

    @NotEmpty(message = "WorkflowTask list cannot be empty")
    private List<@Valid WorkflowTask> tasks = new LinkedList<>();

    /**
     * the out put parameter
     */
    private Map<String, Object> outputParameters = new HashMap<>();

}