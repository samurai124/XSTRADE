package service;

import model.*;

import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.time.*;
import java.util.stream.Collectors;

import util.Validating;

import static util.Validating.*;

public class TradingPlatform {

    // the list of the traders registered in the platform
    private ArrayList<Trader> traders = new ArrayList<>();

    // the list of the assets available in the platform
    private ArrayList<Asset> actifs = new ArrayList<>(
            Arrays.asList(
                    new CryptoCurrency("BTC", 65000, new ArrayList<>(Arrays.asList(-500, 500)), "Bitcoin"),
                    new CryptoCurrency("ETH", 3500,  new ArrayList<>(Arrays.asList(-50, 50)), "Ethereum"),
                    new CryptoCurrency("SOL", 145,  new ArrayList<>(Arrays.asList(-5, 5)), "Solana"),

                    new Stock("AAPL", 185,  new ArrayList<>(Arrays.asList(-2, 2)), "Apple Inc."),
                    new Stock("NVDA", 820,  new ArrayList<>(Arrays.asList(-10, 10)), "Nvidia Corporation"),
                    new Stock("TSLA", 175,  new ArrayList<>(Arrays.asList(-4, 4)), "Tesla, Inc."),
                    new Stock("GOOGL", 150,  new ArrayList<>(Arrays.asList(-1, 1)), "Alphabet Inc.")
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


        // getting the price intervale
        System.out.println("Enter the price intervale start and end : ");
        int a = Validating.validateInts("start");
        int b = Validating.validateInts("end");

        // creating the asset based on the type chosen : stock
        if (assetType.equals("stock")) {

            // getting the company name for the stock
            String companyName = Validating.validateString("company name");

            // creating the stock object
            Stock stock = new Stock(name, unitPrice, new ArrayList<>(java.util.Arrays.asList(a, b)), companyName);

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
            CryptoCurrency cryptoCurrency = new CryptoCurrency(name, unitPrice, new ArrayList<>(java.util.Arrays.asList(a, b)), cryptoName);

            // register the crypto
            actifs.add(cryptoCurrency);

            // message of success
            System.out.println("✅ the crypto coin registered with success");
        }
    }

    public void displayAssets() {
        String separator = "--------------------------------------------------------------------------------";
        System.out.println(separator);

        // Header: 5 columns
        System.out.printf("%-10s | %-5s | %-15s | %-10s | %-20s%n", "TYPE", "ID", "NAME", "PRICE", "SOURCE");
        System.out.println(separator);

        if (actifs.isEmpty()) {
            System.out.println("No assets available.");
        } else {
            for (Asset a : actifs) {
                String type = (a instanceof Stock) ? "STOCK" : "CRYPTO";
                String source = (a instanceof Stock) ? ((Stock) a).getCompanyName() : ((CryptoCurrency) a).getCoineName();

                // Fixed: Matched the 5 columns exactly
                System.out.printf("%-10s | %-5d | %-15s | %-10.2f | %-20s%n",
                        type, a.getId(), a.getName(), a.getUnitPrice(), source);
            }
        }
        System.out.println(separator);
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
        Transaction t = new Transaction("BOUGHT", asset.getUnitPrice(), trader, asset);
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
        Transaction t = new Transaction("SELL", asset.getUnitPrice(), trader, asset);
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
    // Afficher toutes les transactions d’un trader donné
    public void filtrerTransction(Trader trader){
        List<Transaction> traderTransactions = new ArrayList<>();
        traderTransactions = transactions.stream().filter(t->t.getTrader().equals(trader)).toList();
        System.out.printf("_____________________________ %s transactions _____________________________\n",trader.getName());
        if (traderTransactions.isEmpty()){
            System.out.println("No transaction available !!!");
            return;
        }
        System.out.printf(
                "%-6s | %-10s | %-15s | %-12s | %12s | %-20s%n",
                "ID", "TYPE", "ASSET", "TRADER", "UNIT_PRICE", "DATE"
        );
        System.out.println("_________________________________________________________________________");

        traderTransactions.forEach(t -> {
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
        System.out.println("_________________________________________________________________________");
    }

    // Filtrer les transactions par : type (BUY / SELL), actif financier (ex : AAPL, BTC, EUR/USD), intervalle de dates
    public void typeFilteringTransaction(){
        System.out.println("_____________________________ filter transactions _____________________________");
        System.out.println("1. Par Type (BUY/SELL)");
        System.out.println("2. Par Actif ");
        System.out.println("3. Par Intervalle de Dates");
        int choice = Validating.validateInts("filtering choice");
        List<Transaction> filteredTransactions = new ArrayList<>();
        if (choice == 1){
            System.out.println("1 ==> SELL");
            System.out.println("2 ==> BUY");
            int type = validateInts("type");
            if (type == 1){
                filteredTransactions = transactions.stream().filter(t -> t.getTransactionType().toLowerCase().equals("sell")).toList();
            }
            if (type == 2) {
                filteredTransactions = transactions.stream().filter(t -> t.getTransactionType().toLowerCase().equals("bought")).toList();
            }
            if (type != 1 && type != 2 ){
                System.out.println("Invalid type");
            }
        }
        if (choice == 2 ){
            String actif = validateString("Asset name");
            filteredTransactions = transactions.stream().filter(t -> t.getAsset().getName().toLowerCase().equals(actif.toLowerCase())).toList();
        }
        if (choice == 3){
            System.out.println("year-months-day");
            LocalDate start = LocalDate.parse(validateString("Starting date"));
            LocalDate end = LocalDate.parse(validateString("Ending date"));
            try {
                filteredTransactions = transactions.stream().filter(t -> {
                    LocalDate d = t.getDate().toLocalDate();
                    return !d.isBefore(start) && !d.isAfter(end);
                }).toList();
            }catch (DateTimeParseException e) {
            System.out.println("Somthign went wrong");
            return;
        }
        }
        if (choice != 1 && choice != 2 && choice != 3){
            System.out.println("invalid choice");
        }

        if (filteredTransactions.isEmpty()){
            System.out.println("No transaction available !!!");
            return;
        }
        System.out.printf(
                "%-6s | %-10s | %-15s | %-12s | %12s | %-20s%n",
                "ID", "TYPE", "ASSET", "TRADER", "UNIT_PRICE", "DATE"
        );
        System.out.println("_________________________________________________________________________");

        filteredTransactions.forEach(t -> {
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
        System.out.println("_________________________________________________________________________");
    }

    // Trier les transactions par : date, montant

    public void trierTransaction(){
        System.out.println("_____________________________ sort transactions _____________________________");
        System.out.println("1 ==> to sort it by date");
        System.out.println("2 ==> to sort it by price");
        int choice = validateInts("sorting option");
        if (choice == 1) {
            transactions = transactions.stream()
                    .sorted(Comparator.comparing(Transaction::getDate))
                    .collect(Collectors.toCollection(ArrayList::new));
        }
        if (choice == 2){
            transactions = transactions.stream()
                    .sorted(Comparator.comparing(Transaction::getUnitPrice))
                    .collect(Collectors.toCollection(ArrayList::new));
        }
        if (choice != 1 && choice != 2){
            System.out.println("invalid choice !!!!");
        }
        displayTransactions();
    }

    // Calculer : le volume total échangé par actif, le montant total des achats et des ventes
    public void totalChane(){
        displayAssets();
        String assetName = validateString("Asset name").toLowerCase();
        List<Transaction> transactionsForAsset = new ArrayList<>();
        transactionsForAsset = transactions.stream().filter(t->t.getAsset().getName().toLowerCase().equals(assetName)).toList();
        double sellChange = transactionsForAsset.stream()
                .filter(t -> t.getTransactionType().equalsIgnoreCase("sell"))
                .map(Transaction::getUnitPrice)
                .reduce(0.0f, Float::sum);

       double buyChange = transactionsForAsset.stream()
            .filter(t -> t.getTransactionType().equalsIgnoreCase("bought"))
            .map(Transaction::getUnitPrice)
            .reduce(0.0f, Float::sum);

        System.out.println("total change for  : "+assetName);
        System.out.println("Sells : "+sellChange);
        System.out.println("Bought : "+buyChange);
    }
    // Calcul du volume total échangé par trader
    public void totalChangeBytradedr(Trader trader){
        System.out.println("_____________________________ sort transactions _____________________________");
        List<Transaction> traderTransactions = transactions.stream().filter(t-> t.getTrader().equals(trader)).toList();
        if(traderTransactions.isEmpty()){
            System.out.println("no transaction for this trader");
            return;
        }
        double totalBuy = traderTransactions.stream().filter(t -> t.getTransactionType().toLowerCase().equals("bought")).map(t->t.getUnitPrice()).reduce(0.0f, Float::sum);
        double totalSell = traderTransactions.stream().filter(t -> t.getTransactionType().toLowerCase().equals("sell")).map(t->t.getUnitPrice()).reduce(0.0f, Float::sum);

        System.out.println("total change for  : "+trader.getName());
        System.out.println("Sells : "+totalSell);
        System.out.println("Bought : "+totalBuy);
    }
    // Calcul du nombre total d’ordres passés
    public void totalOrders(Trader trader){
        System.out.printf("_____________________________ Total transactions for %s_____________________________\n",trader.getName());
        List<Transaction> traderTransactions = transactions.stream().filter(t-> t.getTrader().equals(trader)).toList();
        if(traderTransactions.isEmpty()){
            System.out.println("no transaction for this trader");
            return;
        }
        System.out.printf(
                "%-6s | %-10s | %-15s | %-12s | %12s | %-20s%n",
                "ID", "TYPE", "ASSET", "TRADER", "UNIT_PRICE", "DATE"
        );
        System.out.println("_________________________________________________________________________");

        traderTransactions.forEach(t -> {
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
        System.out.println("_________________________________________________________________________");
    }
    //Classement des traders par volume (top N traders)
    public void classementTrader() {
        System.out.println("_____________________________ Top Trader (Le plus actif) _____________________________");

        if (transactions.isEmpty()) {
            System.out.println("Aucune transaction enregistrée.");
            return;
        }

        Map<Trader, Integer> traderActivity = new HashMap<>();
        transactions.forEach(t -> {
            traderActivity.put(
                    t.getTrader(),
                    traderActivity.getOrDefault(t.getTrader(), 0) + 1
            );
        });


        Trader topTrader = traderActivity.keySet().stream().findFirst().orElse(null);
        int maxOrders = traderActivity.get(topTrader);

        for (Map.Entry<Trader, Integer> entry : traderActivity.entrySet()) {
            if (entry.getValue() > maxOrders) {
                maxOrders = entry.getValue();
                topTrader = entry.getKey();
            }
        }

        System.out.println("The trader the moste actif :  " + topTrader.getName() + " : " + maxOrders );
    }




    // Calcul du volume total échangé par instrument financier

    public void volumeTotalinstrument(){
        displayAssets();
        String assetName = validateString("Asset name").toLowerCase();
        List<Transaction> transactionsForAsset = new ArrayList<>();
        transactionsForAsset = transactions.stream().filter(t-> t.getAsset().getName().toLowerCase().equals(assetName)).toList();
        int totaleVolume = transactionsForAsset.size();
        System.out.println("Total volume : "+totaleVolume);
    }


    //Identification de l’instrument le plus échangé
    public void instrumentPlusEchange(){
        Map<Asset, Integer> assetActivity = new HashMap<>();
        transactions.forEach(t -> {
            assetActivity.put(
                    t.getAsset(),
                    assetActivity.getOrDefault(t.getAsset(), 0) + 1
            );
        });
        Asset assetMax = assetActivity.keySet().stream().findFirst().orElse(null);
        int assetMaxVolue = assetActivity.get(assetMax);
        for (Map.Entry<Asset, Integer> entry : assetActivity.entrySet()) {
            if (entry.getValue() > assetMaxVolue) {
                assetMaxVolue = entry.getValue();
                assetMax = entry.getKey();
            }
        }

        System.out.println("__________________________________________________________________");
        System.out.println("Most changed asset: " + assetMax.getName() + ":" + assetMaxVolue );

    }







    //Calcul du montant total des BUY et des SELL séparément
    public void calculerTotaalAchatVente() {
        System.out.println("_________________________  Total BUY / SELL _________________________");

        if (transactions.isEmpty()) {
            System.out.println("Aucune transaction enregistrée.");
            return;
        }

        float totalBuy = 0.0f;
        float totalSell = 0.0f;

        for (Transaction t : transactions) {
            if (t.getTransactionType().equalsIgnoreCase("BOUGHT")) {
                totalBuy += t.getUnitPrice();
            } else if (t.getTransactionType().equalsIgnoreCase("SELL")) {
                totalSell += t.getUnitPrice();
            }
        }

        System.out.printf("Total  Achats (BUY)  : %.2f $\n", totalBuy);
        System.out.printf("Total  Ventes (SELL) : %.2f $\n", totalSell);
        System.out.println("_____________________________________________________________________________");
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
            System.out.println("6 ==> display all your transaction");
            System.out.println("7 ==> filter transactions");
            System.out.println("7 ==> log out");
            choice = validateInts("choice");


            // call the right function for ech choice
            switch (choice) {
                case 1 -> currentTrader.consulterPortfolio();
                case 2 -> displayAssets();
                case 3 -> purchaseAsset(currentTrader);
                case 4 -> sellAsst(currentTrader);
                case 5 -> currentTrader.getPortfolio().watchHeldAssets();
                case 6 -> filtrerTransction(currentTrader);
                case 7 -> typeFilteringTransaction();
                case 8 -> logOut();
                default -> System.out.println("Invalid choice");
            }
        } while (choice != 8 );

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
            System.out.println("_________________________________________________ Parte 2 _________________________________________________");
            System.out.println("8 ==> sort transactios");
            System.out.println("9 ==> totalChane");
            System.out.println("10 ==>  classement Trader");
            System.out.println("11 ==>  volumeTotalinstrument");
            System.out.println("12 ==> instrumentPlusEchange");
            System.out.println("13 ==>  priceTotalinstrument");
            System.out.println("14 ==> Exit Admin Mode");

            choice = validateInts("Admin Choice");

            switch (choice) {
                case 1 -> displayTraders();
                case 2 -> addasset();
                case 3 -> displayTransactions();
                case 4 -> exportTransactions();
                case 5 -> displayAssets();
                case 6 -> watchAllAssets();
                case 8 -> trierTransaction();
                case 9 -> totalChane();
                case 10 -> classementTrader();
                case 11 -> volumeTotalinstrument();
                case 12 -> instrumentPlusEchange();
                case 13 -> calculerTotaalAchatVente();
                case 14 -> System.out.println("ok");
                default -> System.out.println("❌ Invalid choice.");
            }
        } while (choice != 14);

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