package model;

public class Transaction implements Identifiable{

    private static int nextId = 1;
    private int id;
    private String transactionType;
    private float unitPrice;

    private Trader trader;
    private Asset asset;

    public Transaction(String transactionType,float unitPrice,Trader trader,Asset asset){
        this.id = nextId++;
        this.transactionType = transactionType;
        this.unitPrice = unitPrice;
        this.trader = trader;
        this.asset = asset;
    }

    @Override
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public float getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(float unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Trader getTrader() {
        return trader;
    }

    public void setTrader(Trader trader) {
        this.trader = trader;
    }

    public Asset getAsset() {
        return asset;
    }

    public void setAsset(Asset asset) {
        this.asset = asset;
    }
}
