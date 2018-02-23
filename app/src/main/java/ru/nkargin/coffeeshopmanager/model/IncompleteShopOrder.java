package ru.nkargin.coffeeshopmanager.model;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import ru.nkargin.coffeeshopmanager.service.GoodService;
import ru.nkargin.coffeeshopmanager.service.SessionService;
import ru.nkargin.coffeeshopmanager.service.StatisticsService;
import rx.Observable;
import rx.functions.Func1;
import rx.subjects.BehaviorSubject;

import static ru.nkargin.coffeeshopmanager.feature.checkout.CheckoutRxUtils.getItemsTotalAccumulator;
import static ru.nkargin.coffeeshopmanager.feature.checkout.CheckoutRxUtils.getMapToEntrySet;


public class IncompleteShopOrder {

    private ShopOrder shopOrder;
    private HashMap<Good, Integer> goodsOnSale = new LinkedHashMap<>();
    private BehaviorSubject<HashMap<Good, Integer>> soldItemsSubject = BehaviorSubject.create(new HashMap<Good, Integer>());

    public void addItem(Good good) {
        Integer integer = goodsOnSale.get(good);
        if (integer == null) {
            integer = 0;
        }

        goodsOnSale.put(good, ++integer);

        pushSoldItems();
    }

    public Observable<Boolean> observeIsValidOrder() {
        return soldItemsSubject.map(new Func1<Map<Good, Integer>, Boolean>() {
            @Override
            public Boolean call(Map<Good, Integer> goodIntegerMap) {
                return !goodIntegerMap.isEmpty();
            }
        });
    }

    public void removeItem(Good good) {
        Integer integer = goodsOnSale.get(good);
        if (integer != null && integer > 0) {
            goodsOnSale.put(good, --integer);
            removeIfNoMoreItems(good, integer);
        }

        pushSoldItems();
    }

    private void removeIfNoMoreItems(Good good, Integer integer) {
        if (integer == 0) {
            goodsOnSale.remove(good);
        }
    }

    private void pushSoldItems() {
        soldItemsSubject.onNext(goodsOnSale);
    }

    public Observable<HashMap<Good, Integer>> observeGoods() {
        return soldItemsSubject.asObservable();
    }

    public Observable<Integer> observeSummary() {
        return soldItemsSubject
                .map(getMapToEntrySet())
                .map(getItemsTotalAccumulator());
    }

    public void persist() {
        if (goodsOnSale.isEmpty()) {
            return;
        }

        shopOrder = new ShopOrder();
        shopOrder.setExecutionTime(new Date());
        shopOrder.setSessionId(SessionService.getInstance().getCurrentSession().getId());
        shopOrder.save();

        int orderSummary = 0;

        for (Map.Entry<Good, Integer> goodIntegerEntry : goodsOnSale.entrySet()) {
            Good good = goodIntegerEntry.getKey();
            Integer count = goodIntegerEntry.getValue();
            good.setSoldTimes(good.getSoldTimes() + count);
            orderSummary += good.getPrice() * count;

            GoodService.INSTANCE.save(good);
        }

        shopOrder.setSummary(orderSummary);

        shopOrder.save();
        soldItemsSubject.onCompleted();

        StatisticsService.INSTANCE.updateStatistics();
    }
}
