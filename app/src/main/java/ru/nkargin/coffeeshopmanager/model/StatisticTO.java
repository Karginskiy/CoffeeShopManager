package ru.nkargin.coffeeshopmanager.model;

/**
 * Created by hei on 22.02.2018.
 */

public class StatisticTO {
    private int orderSummary;
    private int spending;
    private int profit;

    public int getOrderSummary() {
        return orderSummary;
    }

    public int getSpending() {
        return spending;
    }

    public int getProfit() {
        return profit;
    }

    public static StatisticTO getFor(int orderSummary, int spending, int profit) {
        StatisticTO statisticTO = new StatisticTO();
        statisticTO.orderSummary  = orderSummary;
        statisticTO.spending = spending;
        statisticTO.profit = profit;

        return statisticTO;
    }
}
