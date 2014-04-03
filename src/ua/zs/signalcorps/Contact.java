package ua.zs.signalcorps;

import java.util.Date;

public class Contact {

    private int id;
    private Equipage executant;
    private Date startTime;
    private Date endTime;

    public Contact(int id, Equipage executant, Date startTime) {
        this.id = id;
        this.executant = executant;
        this.startTime = startTime;
    }

    public int getId() {
        return id;
    }

    public Equipage getExecutant() {
        return executant;
    }

    public Date getStartTime() {
        return startTime;
    }

    public Date getEndTime() {
        if(endTime != null) {
            return endTime;
        } else {
            throw new NullPointerException("Contact not finished. <id=" + String.valueOf(this.id) +
                                           ";started at " + this.startTime.toString() + ">");
        }
    }

    public void changeId(int id) {
        this.id = id;
    }

    public void changeExecutant(Equipage executant) {
        this.executant = executant;
    }

    public void changeStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public void finishOn(Date endTime) {
        this.endTime = endTime;
    }

    public void finish() {
        this.endTime = new Date();
    }

    @Override
    public String toString() {
        return "№" + String.valueOf(id);
    }

}
