package ua.zs.elements;

public class Weapon {

    private int id;
    private String model;
    private Person owner;

    public Weapon(int id, String model, Person owner) {
        this.id = id;
        this.model = model;
        this.owner = owner;
    }

    public int getId() {
        return id;
    }

    public String getModel() {
        return model;
    }

    public Person getOwner() {
        return owner;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public void setOwner(Person owner) {
        this.owner = owner;
    }

    @Override
    public String toString() {
        return model + " №" + String.valueOf(id);
    }

}
