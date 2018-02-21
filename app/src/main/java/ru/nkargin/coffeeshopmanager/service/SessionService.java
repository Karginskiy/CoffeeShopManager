package ru.nkargin.coffeeshopmanager.service;

import com.orm.query.Condition;
import com.orm.query.Select;

import java.util.Date;

import ru.nkargin.coffeeshopmanager.model.Session;
import ru.nkargin.coffeeshopmanager.model.User;


public class SessionService {

    private static SessionService INSTANCE;
    private static Session currentSession;
    private static User currentUser;

    public static SessionService getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new SessionService();
        }
        return INSTANCE;
    }

    public Session getCurrentSession() {
        return currentSession;
    }

    public void startSessionOrRetrieve(User user) {
        if (currentSession == null) {
            currentSession = getStoredSession(user);
            if (currentSession == null) {
                currentSession = createNewSession(user);
                currentUser = user;
            }
        }
    }

    private Session getStoredSession(User user) {
        Select<Session> openedSessions = Select.from(Session.class).where(Condition.prop("user_id").eq(user.getId())).and(Condition.prop("is_closed").eq(false));
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
        StatisticsService.INSTANCE.dispose();
    }

}
