package com.sooft_sales.model;

import java.io.Serializable;
import java.util.List;

public class DepartmentDataModel extends StatusResponse implements Serializable {
    private List<DepartmentModel> data;

    public List<DepartmentModel> getData() {
        return data;
    }

}
