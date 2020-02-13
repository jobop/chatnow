package com.chatnow.core.dao;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Enzo Cotter on 2020/2/10.
 */
public class UserDao {
    private static Map<String, String> userMap = new ConcurrentHashMap<String, String>();

    private static volatile UserDao dao = new UserDao();

    public static UserDao getInstance() {

        if (null == dao) {
            synchronized (UserDao.class) {
                if (null == dao) {
                    dao = new UserDao();
                }
            }
        }
        return dao;
    }

    public void addUser(String userName, String pwd) {
        userMap.put(userName, pwd);
    }

    public String queryPwdByUserName(String userName) {
        return userMap.get(userName);
    }
}
