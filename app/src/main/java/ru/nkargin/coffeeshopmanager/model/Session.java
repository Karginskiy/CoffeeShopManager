package ru.nkargin.coffeeshopmanager.model;

import com.orm.SugarRecord;

import java.util.Date;


public class Session extends SugarRecord<Session> {

    public Session() {
    }

    private long userId;
    private Date startDate;
    private boolean isClosed;
    private Date endDate;

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public boolean isClosed() {
        return isClosed;
    }

    public void setClosed(boolean closed) {
        isClosed = closed;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}
