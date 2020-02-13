package com.chatnow.core.test.client.handler;

import com.chatnow.core.domain.inboundmsg.InboundMsg;
import com.chatnow.core.domain.outboundmsg.OutboundMsg;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;

/**
 * Created by Enzo Cotter on 2020/2/11.
 */
public class ClientOutboundMsgEncoder extends ChannelOutboundHandlerAdapter {
    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {

        ByteBuf byteBuf = ctx.alloc().buffer();
        InboundMsg inboundMsg = (InboundMsg) msg;

        inboundMsg.writeToMessage(byteBuf);

        ctx.writeAndFlush(byteBuf);

        byteBuf.release();
    }
}
