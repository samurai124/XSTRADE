package model;

import java.util.ArrayList;

import static util.Validating.input;

public class Portfolio<T extends Asset> implements Identifiable {
    private static int nextId = 1;
    private int id;
    private ArrayList<T> actifs = new ArrayList<>();
    private float balance;
    private int totalActifs;
    private Trader trader;

    public Portfolio(float balance) {
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

    public void deposit(float amount) {
        this.balance += amount;
    }

    // withdraw function

    public void withdraw(float amount) {
        this.balance -= amount;
    }

    // hold Asset/buy asset function
    public void holdAsset(Asset asset) {
        if (asset.getUnitPrice() > this.balance) {
            System.out.println("❌ Not enough balance to by this asset");
            return;
        }
        this.balance -= asset.getUnitPrice();
        this.actifs.add((T) asset);
        this.totalActifs += 1;
        System.out.println("✅ asset has been purchased successfully");
    }

    // inhold Asset/sell asset function
    public void inholdAsset(Asset asset) {
        if (!actifs.contains(asset)) {
            System.out.println("❌ you never bought this asset");
            return;
        }
        this.balance += asset.getUnitPrice();
        this.actifs.remove(asset);
        this.totalActifs -= 1;
        System.out.println("✅ asset has been sold successfully");
    }

    // list Actifs function
    public void listActifs() {

        if (actifs.isEmpty()) {
            System.out.println("_________________________________________________________________________________\n" +
                    "No assets are purchased yet ");
        }

        for (T actif : actifs) {
            if (actif instanceof Stock) {
                System.out.printf(
                        "_________________________________________________________________________________\n|Id : %d | Name : %s | Price : %.2f | Company : %s |\n",
                        ((Stock) actif).getId(),((Stock) actif).getName(), ((Stock) actif).getUnitPrice(), ((Stock) actif).getCompanyName());
            }
            if (actif instanceof CryptoCurrency) {
                System.out.printf(
                        "_________________________________________________________________________________\n|Id : %d | Name : %s | Price : %.2f | Company : %s |\n",
                        ((CryptoCurrency) actif).getId(),((CryptoCurrency) actif).getName(), ((CryptoCurrency) actif).getUnitPrice(),
                        ((CryptoCurrency) actif).getCoineName());
            }
        }
        System.out.println("_________________________________________________________________________________\n");

    }

    public void watchHeldAssets() {

        Thread watchThread = new Thread(() -> {
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    System.out.print("\033[H\033[2J");
                    System.out.flush();

                    System.out.println("--- PORTFOLIO LIVE VIEW ---");
                    System.out.println("NAME            | PRICE");
                    System.out.println("---------------------------");

                    for (int i = 0; i < actifs.size(); i++) {
                        T a = actifs.get(i);
                        System.out.printf("%-15s : %.2f $\n", a.getName(), a.getUnitPrice());
                    }

                    System.out.println("\n[Press Enter to go back]");
                    Thread.sleep(2000);
                }
            } catch (InterruptedException e) {
            }
        });

        watchThread.start();


        input.nextLine();

        watchThread.interrupt();


    }
    // calculate Profit/Loss function

}
