package com.chatnow.core.test.client.handler;

import com.chatnow.core.domain.protobuf.command.Login;
import com.google.protobuf.Any;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * Created by Enzo Cotter on 2020/2/14.
 */
public class ClientProcessHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf byteBuf = (ByteBuf) msg;


        try {
            byte[] bytes = new byte[byteBuf.readableBytes()];
            byteBuf.readBytes(bytes);
            Any any = Any.parseFrom(bytes);
            if (any.is(Login.LoginCommandResp.class)) {
                Login.LoginCommandResp resp = any.unpack(Login.LoginCommandResp.class);
                System.out.println(resp.getResultCode());
                System.out.println(resp.getAuthToken());
            } else if (any.is(Login.LoginCommandResp.class)) {

            }

        } finally {
            byteBuf.release();
        }

    }

}