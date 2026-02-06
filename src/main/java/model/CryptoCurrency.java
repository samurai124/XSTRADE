package model;

import java.util.ArrayList;

public class CryptoCurrency extends Asset{
    private String coineName;
    public CryptoCurrency(String name, float unitPrice, ArrayList<Integer> priceInterval, String coineName){
        super(name,unitPrice,priceInterval);
        this.coineName = coineName;
    }

    public String getCoineName() {
        return coineName;
    }

    public void setCoineName(String coineName) {
        this.coineName = coineName;
    }
}
