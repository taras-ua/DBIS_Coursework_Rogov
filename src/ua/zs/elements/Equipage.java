package ua.zs.elements;

public class Equipage {

    private int id;
    private Person commander;

    public Equipage(int id, Person commander) {
        this.id = id;
        this.commander = commander;
    }

    public int getId() {
        return id;
    }

    public Person getCommander() {
        return commander;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCommander(Person commander) {
        this.commander = commander;
    }

    @Override
    public String toString() {
        return "№" + String.valueOf(id);
    }

}
