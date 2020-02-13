package com.chatnow.core.dao;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Enzo Cotter on 2020/2/10.
 */
public class GroupDao {
    private static Map<String, Set<String>> groupMap = new ConcurrentHashMap<String, Set<String>>();

    private static volatile GroupDao dao = new GroupDao();

    public static GroupDao getInstance() {

        if (null == dao) {
            synchronized (GroupDao.class) {
                if (null == dao) {
                    dao = new GroupDao();
                }
            }
        }
        return dao;
    }


    public Set<String> queryGroupByName(String groupName) {
        return groupMap.get(groupName);
    }

    public void joinGroup(String userName, String groupName) {
        queryGroupByName(groupName).add(userName);
    }

    public boolean hasJoinGroup(String groupName, String userName) {
        Set<String> users = queryGroupByName(groupName);
        return users.contains(userName);
    }

    public void createGroup(String groupName, String masterName) {
        if (groupMap.get(groupName) == null) {
            Set<String> userSet = new HashSet<String>();
            userSet.add(masterName);
            groupMap.put(groupName, userSet);

        }
    }
}
