package com.chatnow.core.session;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Enzo Cotter on 2020/2/10.
 */
public class InMemorySession implements Session {
    private Map<String, Object> map = new ConcurrentHashMap<String, Object>();

    public Object getByKey(String key) {
        return map.get(key);
    }

    public void put(String key, Object obj) {
        map.put(key, obj);
    }

    public void clear() {
        map.clear();
    }
}
