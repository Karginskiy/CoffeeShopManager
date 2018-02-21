package ru.nkargin.coffeeshopmanager.feature.admin;

import android.annotation.SuppressLint;
import android.databinding.BaseObservable;

import ru.nkargin.coffeeshopmanager.model.Good;

/**
 * Created by hei on 21.02.2018.
 */

public class AdminViewModel extends BaseObservable {
    private Good good;

    public Good getGood() {
        return good;
    }

    public void setGood(Good good) {
        this.good = good;
        notifyChange();
    }

    @SuppressLint("DefaultLocale")
    @Override
    public String toString() {
        return String.format("%s, %d руб.", good.getTitle(), good.getPrice());
    }
}
