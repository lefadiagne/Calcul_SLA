package com.lbp.sla.niveau.de.service.model;

public class JiraTicket {

    private String ticket;
    private long gtiDays;
    private long gtiHour;
    private long gtiMin;

    private long gtrDays;
    private long gtrMin;
    private long gtrHour;
    String Newligne=System.getProperty("line.separator");
    @Override
    public String toString() {
        return "jira"+ticket+"{\n\r" +
                "gti : " + gtiDays + " jour(s) " + gtiHour + " heure(s)" + gtiMin +" minutes(s) "+ Newligne+
                "gtr : " + gtrDays + " jour(s) " + gtrHour + " heure(s)" + gtrMin +" minutes(s) "+ Newligne+
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
    public void setTicket(String ticket) {
        this.ticket = ticket;
    }

    public String getTicket() {
        return ticket;
    }

    public JiraTicket(String ticket, long gtiDays, long gtiHour, long gtiMin, long gtrDays, long gtrHour, long gtrMin) {
        this.ticket = ticket;
        this.gtiDays = gtiDays;
        this.gtiHour = gtiHour;
        this.gtiMin = gtiMin;
        this.gtrDays = gtrDays;
        this.gtrHour = gtrHour;
        this.gtrMin = gtrMin;
    }

    public JiraTicket() {
    }
}
