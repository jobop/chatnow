package com.chatnow.core.processors.inbound;

import com.chatnow.core.domain.inboundmsg.InboundMsg;
import com.chatnow.core.domain.protobuf.command.SendToUser;
import com.chatnow.core.processors.InboundProcessor;
import io.netty.channel.ChannelHandlerContext;

/**
 * Created by Enzo Cotter on 2020/2/10.
 */
public class ResultFromUserReceivedMsgProcessor implements InboundProcessor<Object> {
    public void process(ChannelHandlerContext ctx, InboundMsg msg) {
        
    }

    @Override
    public void process(ChannelHandlerContext ctx, Object msg) {

    }
}