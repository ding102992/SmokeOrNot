package com.jingcai.apps.smokeornot.entity;

import java.util.Date;

/**
 * Created by JasonDing on 15/9/22.
 */
public class SmokeRecord {
    private int smokeId;
    private Date smokeDate;
    private boolean isSmoke;

    public SmokeRecord(){}

    public SmokeRecord(int smokeId, Date smokeDate, boolean isSmoke) {
        this.smokeId = smokeId;
        this.smokeDate = smokeDate;
        this.isSmoke = isSmoke;
    }

    public int getSmokeId() {
        return smokeId;
    }

    public void setSmokeId(int smokeId) {
        this.smokeId = smokeId;
    }

    public Date getSmokeDate() {
        return smokeDate;
    }

    public void setSmokeDate(Date smokeDate) {
        this.smokeDate = smokeDate;
    }

    public boolean isSmoke() {
        return isSmoke;
    }

    public void setIsSmoke(boolean isSmoke) {
        this.isSmoke = isSmoke;
    }
}
