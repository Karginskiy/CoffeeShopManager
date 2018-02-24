package ru.nkargin.coffeeshopmanager.model;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.functions.Function;
import io.reactivex.subjects.BehaviorSubject;
import ru.nkargin.coffeeshopmanager.service.GoodService;
import ru.nkargin.coffeeshopmanager.service.OrderService;
import ru.nkargin.coffeeshopmanager.service.SessionService;
import ru.nkargin.coffeeshopmanager.service.StatisticsService;

import static ru.nkargin.coffeeshopmanager.feature.checkout.CheckoutRxUtils.getItemsTotalAccumulator;
import static ru.nkargin.coffeeshopmanager.feature.checkout.CheckoutRxUtils.getMapToEntrySet;


public class IncompleteShopOrder {

    private ShopOrder shopOrder;
    private HashMap<Good, Integer> goodsOnSale = new LinkedHashMap<>();
    private BehaviorSubject<HashMap<Good, Integer>> soldItemsSubject = BehaviorSubject.createDefault(new HashMap<Good, Integer>());

    public void addItem(Good good) {
        Integer integer = goodsOnSale.get(good);
        if (integer == null) {
            integer = 0;
        }

        goodsOnSale.put(good, ++integer);

        pushSoldItems();
    }

    public Observable<Boolean> observeIsValidOrder() {
        return soldItemsSubject.map(new Function<HashMap<Good,Integer>, Boolean>() {
            @Override
            public Boolean apply(HashMap<Good, Integer> goodIntegerHashMap) throws Exception {
                return !goodIntegerHashMap.isEmpty();
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
        return soldItemsSubject;
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
        soldItemsSubject.onComplete();

        OrderService.INSTANCE.updateOrders();
        StatisticsService.INSTANCE.updateStatistics();
    }
}
