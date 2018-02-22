package ru.nkargin.coffeeshopmanager.model;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import ru.nkargin.coffeeshopmanager.service.SessionService;
import ru.nkargin.coffeeshopmanager.service.StatisticsService;
import rx.Observable;
import rx.subjects.BehaviorSubject;

import static ru.nkargin.coffeeshopmanager.feature.checkout.CheckoutRxUtils.getItemsTotalAccumulator;
import static ru.nkargin.coffeeshopmanager.feature.checkout.CheckoutRxUtils.getMapToEntrySet;


public class IncompleteShopOrder {

    private ShopOrder shopOrder;
    private HashMap<Good, Integer> unsavedSoldItems = new LinkedHashMap<>();
    private BehaviorSubject<Map<Good, Integer>> soldItemsSubject = BehaviorSubject.create();

    public void addItem(Good good) {
        Integer integer = unsavedSoldItems.get(good);
        if (integer == null) {
            integer = 0;
        }

        unsavedSoldItems.put(good, ++integer);

        pushSoldItems();
    }

    public void removeItem(Good good) {
        Integer integer = unsavedSoldItems.get(good);
        if (integer != null && integer > 0) {
            unsavedSoldItems.put(good, --integer);
            removeIfNoMoreItems(good, integer);
        }

        pushSoldItems();
    }

    private void removeIfNoMoreItems(Good good, Integer integer) {
        if (integer == 0) {
            unsavedSoldItems.remove(good);
        }
    }

    private void pushSoldItems() {
        soldItemsSubject.onNext(unsavedSoldItems);
    }

    public Observable<Map<Good, Integer>> observeGoods() {
        return soldItemsSubject.asObservable();
    }

    public Observable<Integer> observeSummary() {
        return soldItemsSubject
                .map(getMapToEntrySet())
                .map(getItemsTotalAccumulator());
    }

    public ShopOrder persist() {
        if (!unsavedSoldItems.isEmpty()) {

            shopOrder = new ShopOrder();
            shopOrder.setExecutionTime(new Date());
            shopOrder.setSessionId(SessionService.getInstance().getCurrentSession().getId());
            shopOrder.save();

            int orderSummary = 0;

            for (Map.Entry<Good, Integer> goodIntegerEntry : unsavedSoldItems.entrySet()) {
                SoldItem soldItem = new SoldItem();
                soldItem.setGood(goodIntegerEntry.getKey());
                soldItem.setShopOrder(shopOrder.getId());
                soldItem.setCount(goodIntegerEntry.getValue());

                soldItem.save();

                orderSummary += goodIntegerEntry.getKey().getPrice() * goodIntegerEntry.getValue();
            }

            shopOrder.setSummary(orderSummary);
        }

        shopOrder.save();
        soldItemsSubject.onCompleted();

        StatisticsService.INSTANCE.updateStatistics();

        return shopOrder;
    }
}
