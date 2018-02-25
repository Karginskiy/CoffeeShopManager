package ru.nkargin.coffeeshopmanager.service;

import android.support.annotation.NonNull;
import android.util.Pair;

import com.orm.query.Condition;
import com.orm.query.Select;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;
import ru.nkargin.coffeeshopmanager.model.Session;
import ru.nkargin.coffeeshopmanager.model.ShopOrder;
import ru.nkargin.coffeeshopmanager.model.StatisticTO;


import static java.util.Calendar.HOUR_OF_DAY;
import static java.util.Calendar.MINUTE;
import static java.util.Calendar.SECOND;

public class StatisticsService {

    public static final StatisticsService INSTANCE = new StatisticsService();

    private BehaviorSubject<Boolean> updateSubject = BehaviorSubject.createDefault(true);

    public Observable<StatisticTO> observeStatisticsForDatesBetween(final Pair<Calendar, Calendar> dates) {
        return updateSubject.map(mapUpdateTickToSession(dates))
                .observeOn(Schedulers.computation())
                .map(mapToOrders())
                .map(mapOrdersOnSummary())
                .map(mapSummaryOnStatistics());
    }

    @NonNull
    private Function<Boolean, List<Session>> mapUpdateTickToSession(final Pair<Calendar, Calendar> dates) {
        return new Function<Boolean, List<Session>>() {
            @Override
            public List<Session> apply(Boolean aBoolean) {
                ServiceUtils.setDayToMaximum(dates.first);
                ServiceUtils.setDayToMaximum(dates.second);
                return Select.from(Session.class)
                        .where(Condition.prop("start_date")
                                .gt(dates.first.getTime().getTime()))
                        .and(Condition.prop("start_date")
                                .lt(dates.second.getTime().getTime()))
                        .list();
            }
        };
    }

    @NonNull
    private Function<List<Session>, Map<Session, List<ShopOrder>>> mapToOrders() {
        return new Function<List<Session>, Map<Session, List<ShopOrder>>>() {
            @Override
            public Map<Session, List<ShopOrder>> apply(List<Session> sessions) {
                Map<Session, List<ShopOrder>> shopOrders = new HashMap<>();
                for (Session session : sessions) {
                    shopOrders.put(session,
                            ShopOrder.find(
                                    ShopOrder.class,
                                    "session_id = ?",
                                    String.valueOf(session.getId())
                            )
                    );
                }

                return shopOrders;
            }
        };
    }

    @NonNull
    private Function<Integer, StatisticTO> mapSummaryOnStatistics() {
        return new Function<Integer, StatisticTO>() {
            @Override
            public StatisticTO apply(Integer summary) {
                return StatisticTO.getFor(summary, getSpendingOfSummary(summary), getPaymentOfSummary(summary));
            }
        };
    }

    public void updateStatistics() {
        updateSubject.onNext(true);
    }

    @NonNull
    private Function<Map<Session, List<ShopOrder>>, Integer> mapOrdersOnSummary() {
        return new Function<Map<Session, List<ShopOrder>>, Integer>() {
            @Override
            public Integer apply(Map<Session, List<ShopOrder>> sessionToOrders) {
                int resultSummary = 0;
                for (Map.Entry<Session, List<ShopOrder>> entry : sessionToOrders.entrySet()) {
                    Session session = entry.getKey();
                    int ordersSum = 0;
                    for (ShopOrder shopOrder : entry.getValue()) {
                        ordersSum += shopOrder.getSummary();
                    }

                    resultSummary += ordersSum - (session.getPayment() + ((ordersSum / 100) * session.getTax()));
                }

                return resultSummary;
            }
        };
    }

    private int getSpendingOfSummary(int summary) {
        return (summary / 100) * 40;
    }

    private int getPaymentOfSummary(int summary) {
        return (summary - ((summary / 100) * 40)) / 2;
    }

}
