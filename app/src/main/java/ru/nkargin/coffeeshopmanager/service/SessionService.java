package ru.nkargin.coffeeshopmanager.service;

import android.support.annotation.NonNull;
import android.util.Pair;

import com.orm.query.Condition;
import com.orm.query.Select;

import java.util.Calendar;
import java.util.Date;

import io.reactivex.functions.Consumer;
import ru.nkargin.coffeeshopmanager.model.Session;
import ru.nkargin.coffeeshopmanager.model.StatisticTO;
import ru.nkargin.coffeeshopmanager.model.User;


public class SessionService {

    private static SessionService INSTANCE;
    private static User currentUser;

    private Session currentSession;
    private boolean hasChanges;

    public static SessionService getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new SessionService();
        }
        return INSTANCE;
    }

    private static void getSubscriptionOnOrderActivity() {
        StatisticsService.INSTANCE
                .observeStatisticsForDatesBetween(Pair.create(Calendar.getInstance(), Calendar.getInstance()))
                .firstElement()
                .subscribe(onChangesInCurrentSession());
    }

    @NonNull
    private static Consumer<StatisticTO> onChangesInCurrentSession() {
        return new Consumer<StatisticTO>() {
            @Override
            public void accept(StatisticTO statisticTO) {
                INSTANCE.hasChanges = true;
            }
        };
    }

    public void setCurrentUserAndRestoreSession(User currentUser) {
        SessionService.currentUser = currentUser;
        currentSession = getStoredSession(currentUser);
    }

    public Session getCurrentSession() {
        return currentSession;
    }

    public Session startSessionOrRetrieve(User user) {
        if (currentUser == null || (currentSession != null && currentSession.isClosed())) {
            return null;
        }

        currentSession = getStoredSession(user);
        if (currentSession == null) {
            currentSession = createNewSession(user);
        }

        getSubscriptionOnOrderActivity();
        StatisticsService.INSTANCE.updateStatistics();

        return currentSession;
    }

    private Session getStoredSession(User user) {
        Calendar todayStart = Calendar.getInstance();
        Calendar todayEnd = Calendar.getInstance();

        ServiceUtils.setDayToMinimum(todayStart);
        ServiceUtils.setDayToMaximum(todayEnd);

        Select<Session> openedSessions = Select.from(Session.class)
                .where(Condition.prop("user_id").eq(user.getId()))
                .and(Condition.prop("start_date").gt(todayStart.getTime().getTime()))
                .and(Condition.prop("start_date").lt(todayEnd.getTime().getTime()));

        return openedSessions.first();
    }

    private Session createNewSession(User user) {
        Session session = new Session();
        session.setStartDate(new Date());
        session.setEndDate(new Date());
        session.setUserId(user.getId());

        session.setPayment(FormulaService.getInstance().getPayment());
        session.setTax(FormulaService.getInstance().getTax());

        session.save();
        return session;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void closeCurrentSession() {
        currentSession.setClosed(true);
        currentSession.setEndDate(new Date());
        currentSession.save();

        OrderService.INSTANCE.dispose();
        removeSessionIfNoOperability();
    }

    private void removeSessionIfNoOperability() {
        if (!hasChanges) {
            currentSession.delete();
            currentSession.save();
        }
    }

}
