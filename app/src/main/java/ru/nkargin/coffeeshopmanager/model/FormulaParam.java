package ru.nkargin.coffeeshopmanager.model;

import com.orm.SugarRecord;

public class FormulaParam extends SugarRecord<FormulaParam> {
    private String paramName;
    private Integer value;

    public FormulaParam() {}

    public String getParamName() {
        return paramName;
    }

    public void setParamName(String paramName) {
        this.paramName = paramName;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }
}