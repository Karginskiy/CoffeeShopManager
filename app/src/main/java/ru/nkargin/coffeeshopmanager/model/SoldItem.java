package ru.nkargin.coffeeshopmanager.model;

import com.orm.SugarRecord;

import java.util.Objects;


public class SoldItem extends SugarRecord<SoldItem> {

    private Good good;
    private int count;
    private long shopOrderId;

    public SoldItem() {}

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

    public long getShopOrder() {
        return shopOrderId;
    }

    public void setShopOrder(long shopOrderId) {
        this.shopOrderId = shopOrderId;
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
