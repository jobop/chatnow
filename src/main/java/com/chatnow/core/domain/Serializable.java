package com.chatnow.core.domain;

import io.netty.buffer.ByteBuf;

/**
 * Created by Enzo Cotter on 2020/2/12.
 */
public interface Serializable {
    public void writeToMessage(ByteBuf buf);
}
