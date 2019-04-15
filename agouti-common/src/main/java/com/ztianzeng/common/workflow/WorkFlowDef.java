package com.ztianzeng.common.workflow;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.LinkedList;
import java.util.List;

/**
 * @author zhaotianzeng
 * @version V1.0
 * @date 2019-04-15 11:16
 */
public class WorkFlowDef {

    @NotEmpty(message = "WorkflowDef name cannot be null or empty")
    private String name;

    private String description;

    @NotEmpty(message = "WorkflowTask list cannot be empty")
    private List<@Valid WorkflowTask> tasks = new LinkedList<>();

}