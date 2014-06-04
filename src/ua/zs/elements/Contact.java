package ua.zs.elements;

import java.util.Calendar;
import java.util.Date;

public class Contact {

    private int id;
    private Equipage equipage;
    private Date startTime;
    private Date endTime;

    public Contact(int id, Equipage equipage, Date startTime) {
        this.id = id;
        this.equipage = equipage;
        this.startTime = startTime;
    }

    public int getId() {
        return id;
    }

    public Equipage getEquipage() {
        return equipage;
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

    public void changeEquipage(Equipage equipage) {
        this.equipage = equipage;
    }

    public void changeStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public void finishOn(Date endTime) {
        this.endTime = endTime;
    }

    public void finish() {
        this.endTime = Calendar.getInstance().getTime();
    }

    public String getReceiver() {
        return "";
    }

    public int getFrequency() {
        return -1;
    }

    public double getAzimuth() {
        return -1.0;
    }

    public String getSatellite() {
        return "";
    }

    public int getNode() {
        return -1;
    }

    @Override
    public String toString() {
        return "№" + String.valueOf(id);
    }

}
