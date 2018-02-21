package ru.nkargin.coffeeshopmanager.feature.checkout;

import android.databinding.BaseObservable;

import ru.nkargin.coffeeshopmanager.model.Good;

/**
 * Created by hei on 21.02.2018.
 */

public class CheckoutSoldListItemViewModel extends BaseObservable {
    private Good good;
    private int count;

    public Good getGood() {
        return good;
    }

    public void setGood(Good good) {
        this.good = good;
        notifyChange();
    }

    public String getPrice() {
        return String.valueOf(good.getPrice() * count) + " руб.";
    }

    public String getCount() {
        return String.valueOf(count);
    }

    public void setCount(int count) {
        this.count = count;
        notifyChange();
    }

    @Override
    public String toString() {
        return good.getTitle();
    }
}
