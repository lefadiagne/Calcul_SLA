package com.lbp.sla.niveau.de.service.model;

public class jira {


    private long gtiDays;
    private long gtiHour;
    private long gtiMin;

    private long gtrDays;
    private long gtrHour;
    private long gtrMin;

    @Override
    public String toString() {
        return "jira{" +
                "gtiDays=" + gtiDays +
                ", gtiHour=" + gtiHour +
                ", gtiMin=" + gtiMin +
                ", gtrDays=" + gtrDays +
                ", gtrHour=" + gtrHour +
                ", gtrMin=" + gtrMin +
                '}';
    }

    public void setGtiDays(long gtiDays) {
        this.gtiDays = gtiDays;
    }

    public void setGtiHour(long gtiHour) {
        this.gtiHour = gtiHour;
    }

    public void setGtiMin(long gtiMin) {
        this.gtiMin = gtiMin;
    }

    public void setGtrDays(long gtrDays) {
        this.gtrDays = gtrDays;
    }

    public void setGtrMin(long gtrMin) {
        this.gtrMin = gtrMin;
    }

    public void setGtrHour(long gtrHour) {
        this.gtrHour = gtrHour;
    }

    public long getGtiDays() {
        return gtiDays;
    }

    public long getGtiHour() {
        return gtiHour;
    }

    public long getGtiMin() {
        return gtiMin;
    }

    public long getGtrDays() {
        return gtrDays;
    }

    public long getGtrMin() {
        return gtrMin;
    }

    public long getGtrHour() {
        return gtrHour;
    }

    public jira(long gtiDays, long gtiHour, long gtiMin, long gtrDays, long gtrHour,long gtrMin) {
        this.gtiDays = gtiDays;
        this.gtiHour = gtiHour;
        this.gtiMin = gtiMin;
        this.gtrDays = gtrDays;
        this.gtrHour = gtrHour;
        this.gtrMin = gtrMin;
    }

    public jira() {
    }
}