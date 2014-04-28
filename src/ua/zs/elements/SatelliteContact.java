package ua.zs.elements;

import java.util.Date;

public class SatelliteContact extends Contact {

    private String satellite;

    public SatelliteContact(int id, Equipage executant, Date startTime, String satellite) {
        super(id, executant, startTime);
        this.satellite = satellite;
    }

    public String getSatellite() {
        return satellite;
    }

    public void setSatellite(String satellite) {
        this.satellite = satellite;
    }

    @Override
    public String toString() {
        return "Супутниковий контакт " + super.toString();
    }

}

