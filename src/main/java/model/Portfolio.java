package model;

import java.util.ArrayList;

public class Portfolio<T> implements Identifiable{
    private static int nextId = 1;
    private int id ;
    private ArrayList<T> actifs = new ArrayList<>();
    private float balance;
    private int totalActifs;
    private Trader trader;

    public Portfolio(float balance){
        this.id = nextId++;
        this.balance = balance;
        this.totalActifs = 0;
    }
    @Override
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ArrayList<T> getActifs() {
        return actifs;
    }

    public void setActifs(ArrayList<T> actifs) {
        this.actifs = actifs;
    }

    public float getBalance() {
        return balance;
    }

    public void setBalance(float balance) {
        this.balance = balance;
    }

    public int getTotalActifs() {
        return totalActifs;
    }

    public void setTotalActifs(int totalActifs) {
        this.totalActifs = totalActifs;
    }

    public Trader getTrader() {
        return trader;
    }

    public void setTrader(Trader trader) {
        this.trader = trader;
    }
    // deposit function

    public void deposit(float amount){
        this.balance += amount;
    }

    // withdraw function

    public void withdraw(float amount){
        this.balance -= amount;
    }

    // hold Asset/buy asset function
    public void holdAsset(Asset asset){
        if (asset.getUnitPrice() > this.balance){
            System.out.println("❌ Not enough balance to by this asset");
            return;
        }
        this.balance -= asset.getUnitPrice();
        this.actifs.add((T) asset);
        this.totalActifs += 1;
        System.out.println("✅ asset has been purchased successfully");
    }

    // inhold Asset/sell asset function
    public void inholdAsset(Asset asset){
        if (!actifs.contains(asset)){
            System.out.println("❌ you never bought this asset");
            return;
        }
        this.balance += asset.getUnitPrice();
        this.actifs.remove(asset);
        this.totalActifs -=1;
        System.out.println("✅ asset has been sold successfully");
    }

    // list Actifs function
    public void listActifs() {

        if (actifs.isEmpty()){
            System.out.println("_________________________________________________________________________________\n" +
                    "No assets are purchased yet ");
        }

        for (T actif : actifs) {
            if (actif instanceof Stock) {
                System.out.printf("_________________________________________________________________________________\n| Name : %s | Price : %0.2f | Company : %s |\n", ((Stock) actif).getName(), ((Stock) actif).getUnitPrice(), ((Stock) actif).getCompanyName());
            }
            if (actif instanceof CryptoCurrency) {
                System.out.printf("_________________________________________________________________________________\n| Name : %s | Price : %0.2f | Company : %s |\n", ((CryptoCurrency) actif).getName(), ((CryptoCurrency) actif).getUnitPrice(), ((CryptoCurrency) actif).getCoineName());
            }
        }
    }


    // calculate Profit/Loss function

}
