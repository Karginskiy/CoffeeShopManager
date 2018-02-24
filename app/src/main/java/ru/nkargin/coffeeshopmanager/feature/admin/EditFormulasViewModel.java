package ru.nkargin.coffeeshopmanager.feature.admin;

import android.databinding.BaseObservable;

/**
 * Created by hei on 24.02.2018.
 */

public class EditFormulasViewModel extends BaseObservable {

    private int tax;
    private int payment;

    public String getTax() {
        return String.valueOf(tax);
    }

    public void setTax(int tax) {
        this.tax = tax;
        notifyChange();
    }

    public String getPayment() {
        return String.valueOf(payment);
    }

    public void setPayment(int payment) {
        this.payment = payment;
        notifyChange();
    }
}
