package com.bedatasolutions.leaseDrop.constants.db;

import lombok.Getter;

@Getter
public enum ActionType {
    CREATE("CREATE"),
    UPDATE("UPDATE"),
    DELETE("DELETE");

    private final String action;

    ActionType(String action) {
        this.action = action;
    }

    @Override
    public String toString() {
        return action;
    }
}
