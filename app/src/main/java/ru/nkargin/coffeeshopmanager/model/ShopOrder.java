package ru.nkargin.coffeeshopmanager.model;

import com.orm.SugarRecord;

import java.util.Date;

public class ShopOrder extends SugarRecord<ShopOrder> {
    public ShopOrder() {}
    private Date executionTime;
}
