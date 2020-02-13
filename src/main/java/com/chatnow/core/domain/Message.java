package com.chatnow.core.domain;

import io.netty.buffer.ByteBuf;

/**
 * Created by Enzo Cotter on 2020/2/8.
 */
public interface Message {
    public int getVersion();

    public int getType();

}
