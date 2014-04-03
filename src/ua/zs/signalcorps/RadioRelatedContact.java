package ua.zs.signalcorps;


import java.util.Date;

public class RadioRelatedContact extends Contact {

    private double azimuth;

    public RadioRelatedContact(int id, Equipage executant, Date startTime, double azimuth) {
        super(id, executant, startTime);
        if(azimuth >=0 && azimuth < 360) {
            this.azimuth = azimuth;
        } else {
            throw new IllegalArgumentException("Azimuth must be from 0 (inc) to 360.");
        }
    }

    public double getAzimuth() {
        return azimuth;
    }

    public void setAzimuth(double azimuth) {
        if(azimuth >=0 && azimuth < 360) {
            this.azimuth = azimuth;
        } else {
            throw new IllegalArgumentException("Azimuth must be from 0 (inc) to 360.");
        }
    }

    @Override
    public String toString() {
        return "Радіорелейний контакт " + super.toString();
    }

}

