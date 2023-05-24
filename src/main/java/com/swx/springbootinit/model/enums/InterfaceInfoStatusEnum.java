package com.swx.springbootinit.model.enums;

public enum InterfaceInfoStatusEnum {
    ONLINE(1),
    OFFLINE(0);
    private Integer value;

    InterfaceInfoStatusEnum(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }
}
