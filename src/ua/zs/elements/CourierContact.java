package ua.zs.elements;

import java.util.Date;

public class CourierContact extends Contact {

    private String receiver;

    public CourierContact(int id, Equipage executant, Date startTime, String receiver) {
        super(id, executant, startTime);
        this.receiver = receiver;
    }

    public String getReceiver() {
        return receiver;
    }

    public void changeReciever(String receiver) {
        this.receiver = receiver;
    }

    @Override
    public String toString() {
        return "Фельд’єгерська доставка " + super.toString();
    }

}
