package ru.nkargin.coffeeshopmanager.model;

import com.orm.SugarRecord;

import java.util.Date;

public class ShopOrder extends SugarRecord<ShopOrder> {

    public ShopOrder() {}

    private Date executionTime;
    private long sessionId;

    public long getSessionId() {
        return sessionId;
    }

    public void setSessionId(long sessionId) {
        this.sessionId = sessionId;
    }

    public Date getExecutionTime() {
        return executionTime;
    }

    public void setExecutionTime(Date executionTime) {
        this.executionTime = executionTime;
    }
}
