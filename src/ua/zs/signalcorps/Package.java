package ua.zs.signalcorps;

public class Package {

    private int id;
    private CourierContact delivery;
    private int classified;

    public Package(int id, CourierContact delivery, int classified) {
        this.id = id;
        this.delivery = delivery;
        if(classified >=0 && classified < 5) {
            this.classified = classified;
        } else {
            throw new IllegalArgumentException("Classified code must be from 0 to 4. " +
                    "Use <Classified> class for legal constants.");
        }
    }

    public int getId() {
        return id;
    }

    public CourierContact getDelivery() {
        return delivery;
    }

    public int getClassified() {
        return classified;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setDelivery(CourierContact delivery) {
        this.delivery = delivery;
    }

    public void setClassified(int classified) {
        if(classified >=0 && classified < 5) {
            this.classified = classified;
        } else {
            throw new IllegalArgumentException("Classified code must be from 0 to 4. " +
                    "Use <Classified> class for legal constants.");
        }
    }

    @Override
    public String toString() {
        return "Пакет " + Classified.toString(classified) +
               " №" + String.valueOf(id);
    }

}
