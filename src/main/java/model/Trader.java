package model;

public class Trader extends Person{
    private Portfolio portfolio;

    public Trader(String name,Portfolio portfolio){
        super(name);
        this.portfolio = portfolio;
    }

    public Portfolio getPortfolio() {
        return portfolio;
    }

    public void setPortfolio(Portfolio portfolio) {
        this.portfolio = portfolio;
    }

    // function to consulter the portfolio
    public void consulterPortfolio(){
        System.out.println("__________________________________ Consult portfolio ___________________________________");

        // displaying the portfolio data
        System.out.println("Your portfolio id : "+portfolio.getId());
        System.out.println("Balance : "+ portfolio.getBalance());
        System.out.println("Total actifs : "+portfolio.getTotalActifs());
        System.out.println("Actifs held :");
        portfolio.listActifs();
    }
}
