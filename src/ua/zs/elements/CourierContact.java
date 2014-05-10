package ua.zs.elements;

import java.util.Date;

public class CourierContact extends Contact {

    private String receiver;

    public CourierContact(int id, Equipage executant, Date startTime, String receiver) {
        super(id, executant, startTime);
        this.receiver = receiver;
    }

    @Override
    public String getReceiver() {
        return receiver;
    }

    public void changeReciever(String receiver) {
        this.receiver = receiver;
    }

}
