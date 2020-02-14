package com.chatnow.core.processors.inbound;

import com.chatnow.core.processors.InboundProcessor;
import io.netty.channel.ChannelHandlerContext;

/**
 * Created by Enzo Cotter on 2020/2/10.
 */
public class ResultFromUserReceivedMsgProcessor implements InboundProcessor<Object> {

    @Override
    public void process(ChannelHandlerContext ctx, Object msg) {

    }
}
