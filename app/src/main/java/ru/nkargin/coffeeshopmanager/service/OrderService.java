package ru.nkargin.coffeeshopmanager.service;

import ru.nkargin.coffeeshopmanager.model.IncompleteShopOrder;

public class OrderService {
    public static final OrderService INSTANCE = new OrderService();
    private IncompleteShopOrder incompleteShopOrder;

    private OrderService() {}

    public IncompleteShopOrder startOrderOrGetOpened() {
        if (incompleteShopOrder == null) {
            incompleteShopOrder = new IncompleteShopOrder();
        }
        return incompleteShopOrder;
    }

    public void approveAndCloseOrder() {
        incompleteShopOrder.persist();
        incompleteShopOrder = null;
    }

    public void cancelOrder() {
        incompleteShopOrder = null;
    }

    public IncompleteShopOrder getCurrentOrder() {
        return incompleteShopOrder;
    }
}
