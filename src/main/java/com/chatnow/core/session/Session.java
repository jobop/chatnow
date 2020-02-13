package com.chatnow.core.session;

import io.netty.channel.Channel;

/**
 * Created by Enzo Cotter on 2020/2/10.
 */
public interface Session {
    public <T> T getByKey(String key);

    public void put(String key, Object obj);

    public void clear();
}
