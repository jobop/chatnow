package com.chatnow.core.processors;

import com.chatnow.core.domain.inboundmsg.InboundMsg;
import io.netty.channel.ChannelHandlerContext;

/**
 * Created by Enzo Cotter on 2020/2/10.
 */
public interface InboundProcessor {

    public void process(ChannelHandlerContext ctx,InboundMsg msg);
}
