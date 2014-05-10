package ua.zs.elements;

import java.util.Date;

public class WiredContact extends Contact {

    private int node;

    public WiredContact(int id, Equipage executant, Date startTime, int node) {
        super(id, executant, startTime);
        this.node = node;
    }

    @Override
    public int getNode() {
        return node;
    }

    public void setNode(int node) {
        this.node = node;
    }

}
