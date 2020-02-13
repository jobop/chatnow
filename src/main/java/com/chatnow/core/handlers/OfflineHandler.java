package com.chatnow.core.handlers;

import com.chatnow.core.route.RouteManager;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * Created by Enzo Cotter on 2020/2/14.
 */
public class OfflineHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        RouteManager.getInstance().removeByChannelId(ctx.channel().id().asLongText());

    }
}
