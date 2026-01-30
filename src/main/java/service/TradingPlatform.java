package service;

import model.*;

import java.util.ArrayList;
import util.Validating;

public class TradingPlatform {

    // the list of the traders registered in the platform
    private ArrayList<Trader> traders = new ArrayList<>();

    // the list of the assets available in the platform
    private  ArrayList<Asset> actifs = new ArrayList<>();

    // the list of all the transactions performed in the platform
    private ArrayList<Transaction> transactions = new ArrayList<>();


    // function to register a new trader and creat his portfolio
    public void addTrader(){
        System.out.println("______________________________ Trader registration _____________________________");

        // getting the trader name
        String name = Validating.validateString("name");

        // getting the balance
        float balance = Validating.validateFloats("balance");

        // creating a new portfolio
        Portfolio<Asset> portfolio = new Portfolio<>(balance);

        // creating the trader's object
        Trader trader = new Trader(name,portfolio);

        // assigning the trader to the portfolio
        portfolio.setTrader(trader);

        // register the new trader
        traders.add(trader);

        // displaying a message of succession
        System.out.println("✅ your trader account has been registered successfully");

        // displaying the portfolio id for easy access
        System.out.println("your portfolio id is : "+portfolio.getId());

    }

    // function to display all the traders
    public void diplayTraders(){
        System.out.println("_______________________________ Traders display ________________________________");

        // in case of no trader is registered yet
        if (traders.isEmpty()){
            System.out.println("no no trader is registered yet");
            return;
        }

        // display the traders registered in the platform
        traders.stream().forEach(t-> System.out.printf(
                "--------------------------------------------------------------------------------\n"+
                        "| Trader id : %s" +
                        "| name : %s" +
                        "| portfolio id : %d\n",t.getId(),t.getName(),t.getPortfolio().getId()
        ));
    }


    // function to add asset
    public void addasset(){
        System.out.println("__________________________________ Add asset ___________________________________");

        // clarifying if the asset is a stock or a crypto coin
        String assetType = Validating.validateTwoChoice("stock","crypto");

        // getting the name of the asset
        String name = Validating.validateString("Asset name");

        // getting the asset unitPrice

        float unitPrice = Validating.validateFloats("unitPrice");

        // getting the price intervale
        System.out.println("Enter the price intervale start and end : ");
        int a = Validating.validateInts("start");
        int b = Validating.validateInts("end");

        // creating the asset based on the type chosen : stock
        if (assetType.equals("stock")){

            // getting the company name for the stock
            String companyName = Validating.validateString("company name");

            // creating the stock object
            Stock stock = new Stock(name, unitPrice, new ArrayList<>(java.util.Arrays.asList(a, b)),companyName);

            // register the stock
            actifs.add(stock);

            // message of success
            System.out.println("✅ the stock registered with success");
        }

        // creating the asset based on the type chosen : crypto
        if (assetType.equals("crypto")){

            // getting the crypto coin name
            String cryptoName = Validating.validateString("crypto coin name");

            // creating the crypto object
            CryptoCurrency cryptoCurrency = new CryptoCurrency(name,unitPrice,new ArrayList<>(java.util.Arrays.asList(a, b)),cryptoName);

            // register the crypto
            actifs.add(cryptoCurrency);

            // message of success
            System.out.println("✅ the crypto coin registered with success");
        }
    }

    // function to display assets
    public void displayAssets() {
        System.out.println("________________________________ Assets Display ________________________________");

        if (actifs.isEmpty()) {
            System.out.println("No assets available.");
            return;
        }

        actifs.stream().forEach(a -> {
            System.out.print("--------------------------------------------------------------------------------\n");
            if (a instanceof Stock) {
                Stock s = (Stock) a; // Casting to Stock
                System.out.printf("| [STOCK] ID: %d | Name: %s | Price: %.2f | Company: %s\n",
                        s.getId(), s.getName(), s.getUnitPrice(), s.getCompanyName());
            } else if (a instanceof CryptoCurrency) {
                CryptoCurrency c = (CryptoCurrency) a; // Casting to Crypto
                System.out.printf("| [CRYPTO] ID: %d | Name: %s | Price: %.2f | Coin: %s\n",
                        c.getId(), c.getName(), c.getUnitPrice(), c.getCoineName());
            }
        });
    }

    // function to register a new transaction in the platform
    public void registerTransaction(String type, float price, Trader trader, Asset asset) {
        System.out.println("___________________________ Transaction Registration ___________________________");

        // creating the transaction object based on your model constructor
        Transaction t = new Transaction(type, price, trader, asset);

        // adding to the platform list
        transactions.add(t);

        // message of success
        System.out.println("✅ Transaction (ID: " + t.getId() + ") registered successfully");
    }

    // function to display all the transactions performed in the platform
    public void displayTransactions() {
        System.out.println("_____________________________ Transactions History _____________________________");

        // handling empty list
        if (transactions.isEmpty()) {
            System.out.println("No transactions have been performed yet.");
            return;
        }

        // using stream to display each transaction record
        transactions.stream().forEach(t -> {
            System.out.printf(
                    "--------------------------------------------------------------------------------\n" +
                            "| ID: %-4d  | Type: %-10s | Asset: %-10s |\n" +
                            "| Trader: %-12s | Unit Price: %-10.2f |\n",
                    t.getId(),
                    t.getTransactionType().toUpperCase(),
                    t.getAsset().getName(),
                    t.getTrader().getName(),
                    t.getUnitPrice()
            );
        });
        System.out.println("--------------------------------------------------------------------------------");
    }
    // export Transactions function
    public void exportTransactions(){}

    // function to purchase an asset
    public void purchaseAsset(Trader trader){
        System.out.println("_____________________________ Purchase Asset _____________________________");

        // display available assets
        displayAssets();

        // getting the user choice
        int assetId = Validating.validateInts("Asset id");

        // getting the asset with the same assetId
        Asset asset = Validating.findById(actifs , assetId);

        // verifying if the asset exist
        if (asset == null){
            System.out.println("❌ this asset doesn't exist");
            return;
        }

        // call the holding function of the trader
        trader.getPortfolio().holdAsset(asset);
    }





    // the app interface
    public void appInerface(){}
}