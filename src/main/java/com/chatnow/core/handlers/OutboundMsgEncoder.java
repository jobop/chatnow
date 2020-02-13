package com.chatnow.core.handlers;

import com.chatnow.core.domain.outboundmsg.OutboundMsg;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;

/**
 * Created by Enzo Cotter on 2020/2/11.
 */
public class OutboundMsgEncoder extends ChannelOutboundHandlerAdapter {
    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {

        ByteBuf byteBuf = ctx.alloc().buffer();
        OutboundMsg outboundMsg = (OutboundMsg) msg;

        outboundMsg.writeToMessage(byteBuf);

        ctx.writeAndFlush(byteBuf);

        byteBuf.release();
    }
}
