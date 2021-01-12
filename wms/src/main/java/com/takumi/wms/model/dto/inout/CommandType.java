package com.takumi.wms.model.dto.inout;

public interface CommandType {

    String CREATE = "Create";

    String MERGE_PATCH = "MergePatch";

    String DELETE = "Delete";

    String REMOVE = "Remove";

}