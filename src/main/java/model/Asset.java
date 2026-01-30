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

        int minStep = priceInterval.getFirst();
        int maxStep = priceInterval.getLast();
        int change = r.nextInt(minStep, maxStep);

        float newPrice = this.unitPrice + change;

        if (newPrice > this.unitPrice) {
            System.out.println("📈 The price is up now: " + newPrice);
        } else if (newPrice < this.unitPrice) {
            System.out.println("📉 The price is down now: " + newPrice);
        } else {
            System.out.println("➖ No change: " + newPrice);
        }
        this.unitPrice = newPrice;
    }

}
