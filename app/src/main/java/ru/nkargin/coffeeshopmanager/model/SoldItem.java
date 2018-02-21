package ru.nkargin.coffeeshopmanager.model;

import com.orm.SugarRecord;

import java.util.Objects;


public class SoldItem extends SugarRecord<SoldItem> {

    private Good good;
    private int count;
    private ShopOrder shopOrder;

    public static SoldItem forGood(Good good) {
        SoldItem soldItem = new SoldItem();
        soldItem.setGood(good);

        return soldItem;
    }

    public SoldItem() {
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Good getGood() {
        return good;
    }

    public void setGood(Good good) {
        this.good = good;
    }

    public ShopOrder getShopOrder() {
        return shopOrder;
    }

    public void setShopOrder(ShopOrder shopOrder) {
        this.shopOrder = shopOrder;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SoldItem soldItem = (SoldItem) o;
        return Objects.equals(good, soldItem.good);
    }

    @Override
    public int hashCode() {
        return Objects.hash(good);
    }
}
