package ru.nkargin.coffeeshopmanager.service;

import android.support.annotation.NonNull;

import com.orm.query.Condition;
import com.orm.query.Select;

import java.util.ArrayList;
import java.util.List;

import ru.nkargin.coffeeshopmanager.model.ShopOrder;
import ru.nkargin.coffeeshopmanager.model.SoldItem;
import rx.Observable;
import rx.functions.Func1;
import rx.subjects.BehaviorSubject;

public class StatisticsService {

    public static final StatisticsService INSTANCE = new StatisticsService();

    private BehaviorSubject<List<ShopOrder>> currentSessionOrdersSubject = BehaviorSubject.create();

    private List<ShopOrder> getOrdersForCurrentSession() {
        return Select.from(ShopOrder.class)
                .where(Condition.prop("session_id").eq(SessionService
                        .getInstance()
                        .getCurrentSession()
                        .getId()))
                .list();
    }

    public void dispose() {
        currentSessionOrdersSubject.onCompleted();
    }

    public void updateStatistics() {
        currentSessionOrdersSubject.onNext(getOrdersForCurrentSession());
    }

    public Observable<Integer> observeOrdersSummaryForCurrentSession() {
        return currentSessionOrdersSubject.asObservable().map(getSoldItemsForOrders()).map(getSummaryForOrders());
    }

    public Observable<Integer> observeSpendingForCurrentSession() {
        return observeOrdersSummaryForCurrentSession().map(new Func1<Integer, Integer>() {
            @Override
            public Integer call(Integer summary) {
                return (summary / 100) * 40;
            }
        });
    }

    public Observable<Integer> observePaymentForCurrentSession() {
        return observeOrdersSummaryForCurrentSession().map(new Func1<Integer, Integer>() {
            @Override
            public Integer call(Integer summary) {
                return (summary - ((summary / 100) * 40)) / 2;
            }
        });
    }

    @NonNull
    private Func1<List<ShopOrder>, List<SoldItem>> getSoldItemsForOrders() {
        return new Func1<List<ShopOrder>, List<SoldItem>>() {
            @Override
            public List<SoldItem> call(List<ShopOrder> shopOrders) {
                List<SoldItem> soldItems = new ArrayList<>();
                for (ShopOrder shopOrder : shopOrders) {
                    soldItems.addAll(SoldItem.find(SoldItem.class, "shop_order_id = ?", String.valueOf(shopOrder.getId())));
                }

                return soldItems;
            }
        };
    }

    @NonNull
    private Func1<List<SoldItem>, Integer> getSummaryForOrders() {
        return new Func1<List<SoldItem>, Integer>() {
            @Override
            public Integer call(List<SoldItem> soldItems) {
                int sum = 0;
                for (SoldItem soldItem : soldItems) {
                    sum += soldItem.getCount() * soldItem.getGood().getPrice();
                }

                return sum;
            }
        };
    }

}
