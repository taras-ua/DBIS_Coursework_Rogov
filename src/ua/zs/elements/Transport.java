package ua.zs.elements;

import java.util.Date;

public class Transport {

    private int id;
    private String model;
    private Date lastTechwork;
    private Equipage owner;

    public Transport(int id, String model, Date lastTechwork, Equipage owner) {
        this.id = id;
        this.model = model;
        this.lastTechwork = lastTechwork;
        this.owner = owner;
    }

    public int getId() {
        return id;
    }

    public String getModel() {
        return model;
    }

    public Date getLastTechwork() {
        return lastTechwork;
    }

    public Equipage getOwner() {
        return owner;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public void setLastTechwork(Date lastTechwork) {
        this.lastTechwork = lastTechwork;
    }

    public void setOwner(Equipage owner) {
        this.owner = owner;
    }

    @Override
    public String toString() {
        return model + " №" + String.valueOf(id);
    }

}
