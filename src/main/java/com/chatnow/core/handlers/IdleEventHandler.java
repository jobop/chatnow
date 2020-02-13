package com.chatnow.core.handlers;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;

/**
 * Created by Enzo Cotter on 2020/2/11.
 */
public class IdleEventHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {

        if (evt == IdleStateEvent.FIRST_READER_IDLE_STATE_EVENT) {
            System.out.println("第一次空闲");
        } else if (evt == IdleStateEvent.READER_IDLE_STATE_EVENT) {
            //非第一次空闲
            System.out.println("第N次空闲");
            ctx.channel().close();
        } else {
            super.userEventTriggered(ctx, evt);
        }

    }
}
