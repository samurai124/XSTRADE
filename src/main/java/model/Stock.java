package model;

import java.util.ArrayList;

public class Stock extends Asset {
    private String companyName;

    public Stock(String name, float unitPrice,int quantite, ArrayList<Integer> priceInterval,String companyName){
        super(name,unitPrice,quantite,priceInterval);
        this.companyName = companyName;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
}
