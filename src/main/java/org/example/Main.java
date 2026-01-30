package org.example;

import service.TradingPlatform;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    static void main() {
        TradingPlatform t = new TradingPlatform();
        t.diplayTraders();

        t.addTrader();
        t.addTrader();

        t.diplayTraders();

        t.displayAssets();

        t.addasset();
        t.addasset();

        t.displayAssets();

    }
}
