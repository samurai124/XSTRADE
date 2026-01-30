package model;

public class Person implements Identifiable{
    private static int nextId = 1;
    private int id;
    private String name;
    public Person(String name){
        this.id = nextId++;
        this.name = name;
    }

    @Override
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
