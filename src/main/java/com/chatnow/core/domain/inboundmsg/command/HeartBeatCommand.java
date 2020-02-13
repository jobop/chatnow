package com.chatnow.core.domain.inboundmsg.command;

import com.chatnow.core.domain.inboundmsg.InboundMsg;
import com.chatnow.core.utils.ByteBufUtils;
import io.netty.buffer.ByteBuf;

/**
 * Created by Enzo Cotter on 2020/2/8.
 */
public class HeartBeatCommand extends InboundMsg {
    public void writeToMessage(ByteBuf buf) {
        buf.writeInt(getVersion());
        buf.writeInt(getType());
    }
}
