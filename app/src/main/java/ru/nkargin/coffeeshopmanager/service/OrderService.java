package ru.nkargin.coffeeshopmanager.service;

import ru.nkargin.coffeeshopmanager.model.IncompleteShopOrder;

public class OrderService {
    public static final OrderService INSTANCE = new OrderService();
    private IncompleteShopOrder incompleteShopOrder;

    private OrderService() {}

    public IncompleteShopOrder startOrder() {
        if (incompleteShopOrder == null) {
            return new IncompleteShopOrder();
        }
        return incompleteShopOrder;
    }

    public void cancelOrder() {
        incompleteShopOrder = null;
    }

    public IncompleteShopOrder getCurrentOrder() {
        return incompleteShopOrder;
    }
}
