package ru.nkargin.coffeeshopmanager.service;

import com.orm.query.Condition;
import com.orm.query.Select;

import java.util.Date;

import ru.nkargin.coffeeshopmanager.model.Session;
import ru.nkargin.coffeeshopmanager.model.User;
import rx.Subscription;
import rx.functions.Action1;


public class SessionService {

    private static SessionService INSTANCE;
    private static Session currentSession;
    private static User currentUser;
    private boolean hasChanges;
    private static Subscription orderChangingSubscription;

    public static SessionService getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new SessionService();
            getSubscriptionOnOrderActivity();
        }
        return INSTANCE;
    }

    private static void getSubscriptionOnOrderActivity() {
        orderChangingSubscription = StatisticsService.INSTANCE.observeOrdersSummaryForCurrentSession().first().subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                INSTANCE.hasChanges = integer > 0;
            }
        });
    }

    public Session getCurrentSession() {
        return currentSession;
    }

    public void startSessionOrRetrieve(User user) {
        if (currentSession == null) {
            currentSession = getStoredSession(user);
            if (currentSession == null) {
                currentSession = createNewSession(user);
            }
        }
        currentUser = user;
        StatisticsService.INSTANCE.updateStatistics();
    }

    private Session getStoredSession(User user) {
        Select<Session> openedSessions = Select.from(Session.class).where(Condition.prop("user_id").eq(user.getId())).and(Condition.prop("is_closed").eq(0));
        return openedSessions.first();
    }

    private Session createNewSession(User user) {
        Session session = new Session();
        session.setStartDate(new Date());
        session.setEndDate(new Date());
        session.setUserId(user.getId());

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

        removeSessionIfNoOperability();

        currentSession = null;
    }

    private void removeSessionIfNoOperability() {
        if (!hasChanges) {
            currentSession.delete();
            currentSession.save();
        }
    }

}
