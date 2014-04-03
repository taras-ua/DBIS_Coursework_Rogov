package ua.zs.signalcorps;

import java.util.Date;

public class RadioContact extends Contact {

    private int frequency;

    public RadioContact(int id, Equipage executant, Date startTime, int frequency) {
        super(id, executant, startTime);
        this.frequency = frequency;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    @Override
    public String toString() {
        return "Радіоконтакт " + super.toString();
    }

}
