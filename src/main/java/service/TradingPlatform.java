package service;

import model.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import util.Validating;

import static util.Validating.input;
import static util.Validating.validateInts;

public class TradingPlatform {

    // the list of the traders registered in the platform
    private ArrayList<Trader> traders = new ArrayList<>();

    // the list of the assets available in the platform
    private ArrayList<Asset> actifs = new ArrayList<>(
            Arrays.asList(
                    new CryptoCurrency("BTC", 65000, 10, new ArrayList<>(Arrays.asList(-500, 500)), "Bitcoin"),
                    new CryptoCurrency("ETH", 3500, 50, new ArrayList<>(Arrays.asList(-50, 50)), "Ethereum"),
                    new CryptoCurrency("SOL", 145, 200, new ArrayList<>(Arrays.asList(-5, 5)), "Solana"),

                    new Stock("AAPL", 185, 1000, new ArrayList<>(Arrays.asList(-2, 2)), "Apple Inc."),
                    new Stock("NVDA", 820, 500, new ArrayList<>(Arrays.asList(-10, 10)), "Nvidia Corporation"),
                    new Stock("TSLA", 175, 800, new ArrayList<>(Arrays.asList(-4, 4)), "Tesla, Inc."),
                    new Stock("GOOGL", 150, 1200, new ArrayList<>(Arrays.asList(-1, 1)), "Alphabet Inc.")
            )
    );
    // the list of all the transactions performed in the platform
    private ArrayList<Transaction> transactions = new ArrayList<>();

    // current trader in session
    private Trader currentTrader = null;

    // function to register a new trader and creat his portfolio
    public void addTrader() {
        System.out.println("______________________________ Trader registration _____________________________");

        // getting the trader name
        String name = Validating.validateString("name");

        // getting the balance
        float balance = Validating.validateFloats("balance");

        // creating a new portfolio
        Portfolio<Asset> portfolio = new Portfolio<>(balance);

        // creating the trader's object
        Trader trader = new Trader(name, portfolio);

        // assigning the trader to the portfolio
        portfolio.setTrader(trader);

        // register the new trader
        traders.add(trader);

        // displaying a message of succession
        System.out.println("✅ your trader account has been registered successfully");

        // displaying the portfolio id for easy access
        System.out.println("your portfolio id is : " + portfolio.getId());

    }

    // function to display all the traders
    public void displayTraders() {
        System.out.println("_______________________________ Traders display ________________________________");

        // in case of no trader is registered yet
        if (traders.isEmpty()) {
            System.out.println("no no trader is registered yet");
            return;
        }

        // display the traders registered in the platform
        traders.stream().forEach(t -> System.out.printf(
                "--------------------------------------------------------------------------------\n" +
                        "| Trader id : %s" +
                        "| name : %s" +
                        "| portfolio id : %d\n",
                t.getId(), t.getName(), t.getPortfolio().getId()));
    }

    // function to add asset
    public void addasset() {
        System.out.println("__________________________________ Add asset ___________________________________");

        // clarifying if the asset is a stock or a crypto coin
        String assetType = Validating.validateTwoChoice("stock", "crypto");

        // getting the name of the asset
        String name = Validating.validateString("Asset name");

        // getting the asset unitPrice

        float unitPrice = Validating.validateFloats("unitPrice");

        // getting the quantity
        int quantity = Validating.validateInts("quantity");

        // getting the price intervale
        System.out.println("Enter the price intervale start and end : ");
        int a = Validating.validateInts("start");
        int b = Validating.validateInts("end");

        // creating the asset based on the type chosen : stock
        if (assetType.equals("stock")) {

            // getting the company name for the stock
            String companyName = Validating.validateString("company name");

            // creating the stock object
            Stock stock = new Stock(name, unitPrice,quantity, new ArrayList<>(java.util.Arrays.asList(a, b)), companyName);

            // register the stock
            actifs.add(stock);

            // message of success
            System.out.println("✅ the stock registered with success");
        }

        // creating the asset based on the type chosen : crypto
        if (assetType.equals("crypto")) {

            // getting the crypto coin name
            String cryptoName = Validating.validateString("crypto coin name");

            // creating the crypto object
            CryptoCurrency cryptoCurrency = new CryptoCurrency(name, unitPrice, quantity , new ArrayList<>(java.util.Arrays.asList(a, b)), cryptoName);

            // register the crypto
            actifs.add(cryptoCurrency);

            // message of success
            System.out.println("✅ the crypto coin registered with success");
        }
    }

    public void displayAssets() {
        final String SEPARATOR =
                "----------------------------------------------------------------------------------------------------";

        System.out.println(SEPARATOR);

        // 🔹 HEADER (LABELS) - Alignement identique aux transactions
        System.out.printf(
                "%-8s | %-6s | %-15s | %12s | %8s | %-20s%n",
                "TYPE", "ID", "NAME", "PRICE", "QTY", "SOURCE"
        );

        System.out.println(SEPARATOR);

        if (actifs.isEmpty()) {
            System.out.println("No assets available.");
            System.out.println(SEPARATOR);
            return;
        }

        // 🔹 DATA - Affichage en liste continue
        actifs.forEach(a -> {
            String type = "";
            String source = "";

            if (a instanceof Stock) {
                type = "STOCK";
                source = ((Stock) a).getCompanyName();
            } else if (a instanceof CryptoCurrency) {
                type = "CRYPTO";
                source = ((CryptoCurrency) a).getCoineName();
            }

            System.out.printf(
                    "%-8s | %-6d | %-15s | %12.2f | %8d | %-20s%n",
                    type,
                    a.getId(),
                    a.getName(),
                    a.getUnitPrice(),
                    a.getQuantite(),
                    source
            );
        });

        System.out.println(SEPARATOR);
    }
    // function to register a new transaction in the platform
    public void registerTransaction(Transaction t) {
        System.out.println("___________________________ Transaction Registration ___________________________");

        // adding to the platform list
        transactions.add(t);

        // message of success
        System.out.println("✅ Transaction (ID: " + t.getId() + ") registered successfully");
    }

    // display history of transaction
    public void displayTransactions() {

        final String SEPARATOR =
                "----------------------------------------------------------------------------------------------------";

        System.out.println(SEPARATOR);


        System.out.printf(
                "%-6s | %-10s | %-15s | %-12s | %12s | %-20s%n",
                "ID", "TYPE", "ASSET", "TRADER", "UNIT_PRICE", "DATE"
        );

        System.out.println(SEPARATOR);


        if (transactions.isEmpty()) {
            System.out.println("No transactions have been performed yet.");
            System.out.println(SEPARATOR);
            return;
        }


        transactions.forEach(t -> {
            System.out.printf(
                    "%-6d | %-10s | %-15s | %-12s | %12.2f | %-20s%n",
                    t.getId(),
                    t.getTransactionType().toUpperCase(),
                    t.getAsset().getName(),
                    t.getTrader().getName(),
                    t.getUnitPrice(),
                    t.getDate()
            );
        });

        System.out.println(SEPARATOR);
    }


    // export Transactions function
    public void exportTransactions() {
        System.out.println("ok");
    }

    // function to purchase an asset
    public void purchaseAsset(Trader trader) {
        System.out.println("_____________________________ Purchase Asset _____________________________");

        // display available assets
        displayAssets();

        // getting the user choice
        int assetId = Validating.validateInts("Asset id");

        // getting the asset with the same assetId
        Asset asset = Validating.findById(actifs, assetId);

        // verifying if the asset exist
        if (asset == null) {
            System.out.println("❌ this asset doesn't exist");
            return;
        }

        // call the holding function of the trader
        trader.getPortfolio().holdAsset(asset);
        Transaction t = new Transaction("bought", asset.getUnitPrice(), trader, asset);
        registerTransaction(t);
    }

    // function to sell an asset
    public void sellAsst(Trader trader) {
        System.out.println("_____________________________ Sell Asset _____________________________");

        // the trader's portfolio for easy access
        Portfolio<Asset> portfolio = trader.getPortfolio();

        // display assets held
        portfolio.listActifs();

        // getting the user choice
        int assetId = Validating.validateInts("Asset id");

        // getting the asset with the same assetId
        Asset asset = Validating.findById(portfolio.getActifs(), assetId);


        if (asset == null) {
            System.out.println("❌ this asset doesn't exist");
            return;
        }
        // call the inholding function of the trader
        portfolio.inholdAsset(asset);
        Transaction t = new Transaction("sell", asset.getUnitPrice(), trader, asset);
        registerTransaction(t);
    }

    // function to update the all the asset over time
    private ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public void updateAssetsPrice() {
        scheduler.scheduleAtFixedRate(() -> {
            for (Asset a : actifs) {
                // call the updatePrice function for each asset
                a.updatePriceRandomly();
            }
        }, 0, 5, TimeUnit.SECONDS);
    }

    // function to stop updateAssetsPrice
    public void stopUpdateAssetsPrice() {
        // 1. Tell the scheduler: "Don't start the next 10-second update"
        scheduler.shutdown();

        try {
            // 2. Give it a few seconds to finish if it's currently in the middle of a loop
            if (!scheduler.awaitTermination(3, TimeUnit.SECONDS)) {
                // 3. If it's still running after 3s, force it to stop
                scheduler.shutdownNow();
            }
        } catch (InterruptedException e) {
            scheduler.shutdownNow();
        }
    }

    // function to watch the market
    public void watchAllAssets() {
        System.out.println("_____________________________ LIVE MARKET MODE _____________________________");

        System.out.println("Press ENTER to return to menu");

        Thread watchThread = new Thread(() -> {
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    // clear the console
                    System.out.print("\033[H\033[2J");
                    System.out.flush();
                    System.out.println("-------------------------");
                    System.out.println("NAME            | PRICE");
                    for (Asset a : actifs) {
                        System.out.printf("%-15s : %.2f $\n", a.getName(), a.getUnitPrice());
                    }

                    Thread.sleep(10000);
                }
            } catch (InterruptedException e) {
            }
        });

        watchThread.start();
        input.nextLine();
        watchThread.interrupt();
    }

    // log in function
    public void logIn() {
        System.out.println("_____________________________ Log In _____________________________");

        // getting the trader portfolio id
        int traderPrfolioId = Validating.validateInts("Trader portfolio id ");

        // finding the trader with same id
        Trader trader = traders.stream().filter(t -> t.getPortfolio().getId() == traderPrfolioId).findAny()
                .orElse(null);

        // verifying if the user trader exist
        if (trader == null) {
            System.out.println("❌ No trader with this portfolio id exists");
            return;
        }

        // assigning the trader to the currentTrader
        currentTrader = trader;


        // calling the trader menu
        traderMenu();

    }

    // log out function
    public void logOut() {
        currentTrader = null;
        System.out.println("good by");
    }

    // trader menu

    public void traderMenu() {
        System.out.println("_____________________________ Trader menu _____________________________");
        System.out.printf("                                    %s                                 \n",
                currentTrader.getName());
        System.out.println("_______________________________________________________________________");
        int choice = 0;
        do {

            // available operations fo a trader
            System.out.println("1 ==> consult portfolio ");
            System.out.println("2 ==> consult available assets");
            System.out.println("3 ==> purchase an asset");
            System.out.println("4 ==> sell an asset");
            System.out.println("5 ==> watch your held assets");
            System.out.println("6 ==> log out");

            // getting the trader choice
            choice = validateInts("choice");

            // call the right function for ech choice
            switch (choice) {
                case 1 -> currentTrader.consulterPortfolio();
                case 2 -> displayAssets();
                case 3 -> purchaseAsset(currentTrader);
                case 4 -> sellAsst(currentTrader);
                case 5 -> currentTrader.getPortfolio().watchHeldAssets();
                case 6 -> logOut();
                default -> System.out.println("Invalid choice");
            }
        } while (choice != 6 );

    }
    // admin
    public void appManagment() {
        System.out.println("_____________________________ Admin Authentication _____________________________");
        int adminId = validateInts("admin id");

        if (adminId != 2004) {
            System.out.println("❌ Incorrect admin id. Access Denied.");
            return;
        }

        int choice = 0;
        do {
            System.out.println("\n____________________________________________ Admin menu ____________________________________________");
            System.out.println("1 ==> View All Registered Traders");
            System.out.println("2 ==> Add New Asset (Stock/Crypto)");
            System.out.println("3 ==> View Full Transaction History");
            System.out.println("4 ==> Export Transactions ");
            System.out.println("5 ==> View all available assets ");
            System.out.println("6 ==> Global Market Watch");
            System.out.println("7 ==> Exit Admin Mode");

            choice = validateInts("Admin Choice");

            switch (choice) {
                case 1 -> displayTraders();
                case 2 -> addasset();
                case 3 -> displayTransactions();
                case 4 -> exportTransactions();
                case 5 -> displayAssets();
                case 6 -> watchAllAssets();
                case 7 -> System.out.println("ok");
                default -> System.out.println("❌ Invalid choice.");
            }
        } while (choice != 7);

    }
    // the app interface
    public void appInerface() {
        System.out.println("""
        \u001B[36m
          ██╗  ██╗███████╗████████╗██████╗  █████╗ ██████╗ ███████╗
          ╚██╗██╔╝██╔════╝╚══██╔══╝██╔══██╗██╔══██╗██╔══██╗██╔════╝
           ╚███╔╝ ███████╗   ██║   ██████╔╝███████║██║  ██║█████╗  
           ██╔██╗ ╚════██║   ██║   ██╔══██╗██╔══██║██║  ██║██╔══╝  
          ██╔╝ ██╗███████║   ██║   ██║  ██║██║  ██║██████╔╝███████╗
          ╚═╝  ╚═╝╚══════╝   ╚═╝   ╚═╝  ╚═╝╚═╝  ╚═╝╚═════╝ ╚══════╝
                                                                   
                  -- THE FUTURE OF DIGITAL TRADING --
        \u001B[0m""");
        // calling the price updating function
        updateAssetsPrice();


        int choice = 0;
        do{
            System.out.println("_______________________________________ Xstrade menu __________________________________________");
            System.out.println("1 ==> register as a trader");
            System.out.println("2 ==> log in to your trader account");
            System.out.println("3 ==> Admin: Manage Platform");
            System.out.println("4 ==> exit the app");

            choice = validateInts("choice");
            switch (choice){
                case 1 -> addTrader();
                case 2 -> logIn();
                case 3 -> appManagment();
                case 4 -> stopUpdateAssetsPrice();
                default -> System.out.println("invalid choice");
            }
        }while (choice != 4);
    }
}