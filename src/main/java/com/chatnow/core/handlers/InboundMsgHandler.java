package com.chatnow.core.handlers;

import com.chatnow.core.domain.inboundmsg.InboundMsg;
import com.chatnow.core.processors.InboundProcessorFactory;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * Created by Enzo Cotter on 2020/2/10.
 */
public class InboundMsgHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//        InboundMsg inboundMsg = (InboundMsg) msg;
//        InboundProcessorFactory.process(ctx, inboundMsg);
    }
}
