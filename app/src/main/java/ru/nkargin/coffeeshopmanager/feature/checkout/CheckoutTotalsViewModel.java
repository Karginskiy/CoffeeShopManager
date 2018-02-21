package ru.nkargin.coffeeshopmanager.feature.checkout;

import android.databinding.BaseObservable;

/**
 * Created by hei on 21.02.2018.
 */

public class CheckoutTotalsViewModel extends BaseObservable {

    private int summary;

    public void setSummary(int summary) {
        this.summary = summary;
        notifyChange();
    }

    public String getSummary() {
        return String.format("Всего:  %s руб. ", String.valueOf(summary));
    }
}
