package model;

import java.util.ArrayList;
import java.util.Random;

public abstract class Asset implements Identifiable{
    private static int nextId = 1;
    private int id;
    private String name;
    private float unitPrice;
    private ArrayList<Integer> priceInterval ;

    public Asset(String name,float unitPrice,ArrayList<Integer> priceInterval){
        this.id = nextId++;
        this.name = name;
        this.unitPrice = unitPrice;
        this.priceInterval = priceInterval;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(float unitPrice) {
        this.unitPrice = unitPrice;
    }

    public ArrayList<Integer> getPriceInterval() {
        return priceInterval;
    }

    public void setPriceInterval(ArrayList<Integer> priceInterval) {
        this.priceInterval = priceInterval;
    }

    // function to update price randomly
    public void updatePriceRandomly() {
        Random r = new Random();
        int minStep = priceInterval.get(0);
        int maxStep = priceInterval.get(priceInterval.size() - 1);

        // Calculate change
        int change = r.nextInt(minStep, maxStep + 1);
        this.unitPrice += change;

        // Keep price from dropping below 0
        if (this.unitPrice < 0) this.unitPrice = 0;
    }

}
