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

import static java.util.Calendar.HOUR_OF_DAY;
import static java.util.Calendar.MINUTE;
import static java.util.Calendar.SECOND;

public class StatisticsService {

    public static final StatisticsService INSTANCE = new StatisticsService();

    private BehaviorSubject<Boolean> updateSubject = BehaviorSubject.create(true);

    public Observable<StatisticTO> observeStatisticsForDatesBetween(final Pair<Calendar, Calendar> dates) {
        return updateSubject.asObservable().map(mapUpdateTickToSession(dates))
                .map(mapToOrders())
                .map(mapOrdersOnSummary())
                .map(mapSummaryOnStatistics());
    }

    @NonNull
    private Func1<Boolean, List<Session>> mapUpdateTickToSession(final Pair<Calendar, Calendar> dates) {
        return new Func1<Boolean, List<Session>>() {
            @Override
            public List<Session> call(Boolean aBoolean) {
                setFromToDayMinimum(dates);
                setToToDayMaximum(dates);
                return Select.from(Session.class)
                        .where(Condition.prop("start_date")
                                .gt(dates.first.getTime().getTime()))
                        .and(Condition.prop("start_date")
                                .lt(dates.second.getTime().getTime()))
                        .list();
            }
        };
    }

    private void setToToDayMaximum(Pair<Calendar, Calendar> dates) {
        dates.second.set(HOUR_OF_DAY, dates.first.getActualMaximum(HOUR_OF_DAY));
        dates.second.set(MINUTE, dates.first.getActualMaximum(MINUTE));
        dates.second.set(SECOND, dates.first.getActualMaximum(SECOND));
        dates.second.set(Calendar.MILLISECOND, dates.first.getActualMaximum(SECOND));
    }

    private void setFromToDayMinimum(Pair<Calendar, Calendar> dates) {
        dates.first.set(HOUR_OF_DAY, dates.first.getActualMinimum(HOUR_OF_DAY));
        dates.first.set(MINUTE, dates.first.getActualMinimum(MINUTE));
        dates.first.set(SECOND, dates.first.getActualMinimum(SECOND));
        dates.first.set(Calendar.MILLISECOND, dates.first.getActualMinimum(SECOND));
    }

    @NonNull
    private Func1<List<Session>, List<ShopOrder>> mapToOrders() {
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
        updateSubject.onNext(true);
    }

    public Observable<StatisticTO> observeStatisticsForCurrentSession() {
        return updateSubject.asObservable().map(mapUpdateTickOnOrdersForCurrentSession())
                .map(mapOrdersOnSummary())
                .map(mapSummaryOnStatistics());
    }

    @NonNull
    private Func1<Boolean, List<ShopOrder>> mapUpdateTickOnOrdersForCurrentSession() {
        return new Func1<Boolean, List<ShopOrder>>() {
            @Override
            public List<ShopOrder> call(Boolean aBoolean) {
                return Select.from(ShopOrder.class)
                        .where(Condition.prop("session_id").eq(SessionService
                                .getInstance()
                                .getCurrentSession()
                                .getId()))
                        .list();
            }
        };
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
