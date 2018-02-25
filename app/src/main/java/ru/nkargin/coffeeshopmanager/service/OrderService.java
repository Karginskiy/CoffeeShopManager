package ru.nkargin.coffeeshopmanager.service;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.functions.Function;
import io.reactivex.subjects.BehaviorSubject;
import ru.nkargin.coffeeshopmanager.model.IncompleteShopOrder;
import ru.nkargin.coffeeshopmanager.model.ShopOrder;

public class OrderService {
    public static final OrderService INSTANCE = new OrderService();

    private final BehaviorSubject<Boolean> updateSubject = BehaviorSubject.createDefault(true);
    private IncompleteShopOrder incompleteShopOrder;

    private OrderService() {}

    public Observable<List<ShopOrder>> observeShopOrdersForCurrentSession() {
        return updateSubject.map(new Function<Boolean, List<ShopOrder>>() {
            @Override
            public List<ShopOrder> apply(Boolean aBoolean) throws Exception {
                return ShopOrder.find(
                        ShopOrder.class,
                        "session_id = ?",
                        String.valueOf(SessionService.getInstance().getCurrentSession().getId())
                );
            }
        });
    }

    public void dispose() {
        updateSubject.onComplete();
    }

    public void updateOrders() {
        this.updateSubject.onNext(true);
    }

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
