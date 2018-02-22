package ru.nkargin.coffeeshopmanager.service;

import android.support.annotation.NonNull;
import android.util.Pair;

import com.orm.query.Condition;
import com.orm.query.Select;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import ru.nkargin.coffeeshopmanager.model.Session;
import ru.nkargin.coffeeshopmanager.model.ShopOrder;
import ru.nkargin.coffeeshopmanager.model.StatisticTO;
import rx.Observable;
import rx.functions.Func1;
import rx.subjects.BehaviorSubject;

public class StatisticsService {

    public static final StatisticsService INSTANCE = new StatisticsService();
    private static final String EXECUTION_TIME = "execution_time";

    private BehaviorSubject<List<ShopOrder>> currentSessionOrdersSubject = BehaviorSubject.create();
    private BehaviorSubject<Pair<Calendar, Calendar>> betweenDatesSubject = BehaviorSubject.create();

    private List<ShopOrder> getOrdersForCurrentSession() {
        return Select.from(ShopOrder.class)
                .where(Condition.prop("session_id").eq(SessionService
                        .getInstance()
                        .getCurrentSession()
                        .getId()))
                .list();
    }

    public void changeDatesForStatistics(final Pair<Calendar, Calendar> dates) {
        betweenDatesSubject.onNext(dates);
    }

    public Observable<StatisticTO> observeStatisticsForDatesBetween() {
        return betweenDatesSubject.asObservable()
                .map(mapPeriodOnSessions())
                .map(mapSessionsOnOrders())
                .map(mapOrdersOnSummary())
                .map(mapSummaryOnStatistics());
    }

    @NonNull
    private Func1<Pair<Calendar, Calendar>, List<Session>> mapPeriodOnSessions() {
        return new Func1<Pair<Calendar, Calendar>, List<Session>>() {
            @Override
            public List<Session> call(Pair<Calendar, Calendar> fromToPeriod) {
                return Select.from(Session.class)
                        .where(Condition.prop("start_date")
                                .gt(fromToPeriod.first.getTime().getTime()))
                        .and(Condition.prop("start_date")
                                .lt(fromToPeriod.second.getTime().getTime()))
                        .list();
            }
        };
    }

    @NonNull
    private Func1<List<Session>, List<ShopOrder>> mapSessionsOnOrders() {
        return new Func1<List<Session>, List<ShopOrder>>() {
            @Override
            public List<ShopOrder> call(List<Session> sessions) {
                List<ShopOrder> shopOrders = new ArrayList<>();
                for (Session session : sessions) {
                    shopOrders.addAll(
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
    private Func1<Integer, StatisticTO> mapSummaryOnStatistics() {
        return new Func1<Integer, StatisticTO>() {
            @Override
            public StatisticTO call(Integer summary) {
                return StatisticTO.getFor(summary, getSpendingOfSummary(summary), getPaymentOfSummary(summary));
            }
        };
    }

    public void updateStatistics() {
        currentSessionOrdersSubject.onNext(getOrdersForCurrentSession());
    }

    public Observable<StatisticTO> observeStatisticsForCurrentSession() {
        return currentSessionOrdersSubject.asObservable()
                .map(mapOrdersOnSummary())
                .map(mapSummaryOnStatistics());
    }

    @NonNull
    private Func1<List<ShopOrder>, Integer> mapOrdersOnSummary() {
        return new Func1<List<ShopOrder>, Integer>() {
            @Override
            public Integer call(List<ShopOrder> shopOrders) {
                int sum = 0;
                for (ShopOrder shopOrder : shopOrders) {
                    sum += shopOrder.getSummary();
                }

                return sum;
            }
        };
    }

    public int getSpendingOfSummary(int summary) {
        return (summary / 100) * 40;
    }

    public int getPaymentOfSummary(int summary) {
        return (summary - ((summary / 100) * 40)) / 2;
    }
}
