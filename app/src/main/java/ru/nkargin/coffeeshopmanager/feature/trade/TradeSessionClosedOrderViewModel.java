package ru.nkargin.coffeeshopmanager.feature.trade;

import android.databinding.BaseObservable;

import java.text.SimpleDateFormat;
import java.util.Locale;

import ru.nkargin.coffeeshopmanager.model.ShopOrder;


public class TradeSessionClosedOrderViewModel extends BaseObservable {
    private ShopOrder shopOrder;
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("Заказ от dd MMM yyyy - hh:mm", Locale.getDefault());

    public ShopOrder getShopOrder() {
        return shopOrder;
    }

    public void setShopOrder(ShopOrder shopOrder) {
        this.shopOrder = shopOrder;
        notifyChange();
    }

    public String getExecutionTime() {
        return simpleDateFormat.format(shopOrder.getExecutionTime());
    }

    public String getSummary() {
        return String.valueOf(shopOrder.getSummary()) + " руб.";
    }
}
