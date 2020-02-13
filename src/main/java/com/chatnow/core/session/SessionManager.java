package com.chatnow.core.session;

import com.chatnow.core.dao.UserDao;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SessionManager {
    private String sessionClass = "com.chatnow.core.session.InMemorySession";
    private static Map<String, Session> sessionMap = new ConcurrentHashMap<String, Session>();

    private static volatile SessionManager sessionManager = new SessionManager();

    public static SessionManager getInstance(){

        if (null == sessionManager) {
            synchronized (SessionManager.class) {
                if (null == sessionManager) {
                    sessionManager = new SessionManager();
                }
            }
        }
        return sessionManager;
    }

    public Session getSession(String key) {
        return sessionMap.get(key);
    }

    public void clear(String key) {
        getSession(key).clear();
        sessionMap.remove(key);
    }

    public Session newSession(String key) {
        Session session = null;
        try {
            session = (Session) Class.forName(sessionClass).newInstance();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        if (null != session) {
            sessionMap.put(key, session);
        }
        return session;
    }

}
