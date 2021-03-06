package ru.nkargin.coffeeshopmanager.model;

import com.orm.SugarRecord;

import java.io.Serializable;
import java.util.Objects;

/**
 * Created by hei on 20.02.2018.
 */

public class Good extends SugarRecord<Good> implements Serializable {

    private String title;
    private int price;

    private long soldTimes;

    public Good() {}

    public long getSoldTimes() {
        return soldTimes;
    }

    public void setSoldTimes(long soldTimes) {
        this.soldTimes = soldTimes;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Good good = (Good) o;
        return price == good.price &&
                Objects.equals(title, good.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, price);
    }
}
