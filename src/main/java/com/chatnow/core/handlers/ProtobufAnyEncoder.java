package com.chatnow.core.handlers;

import com.google.protobuf.Any;
import com.google.protobuf.Message;
import com.google.protobuf.MessageOrBuilder;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;

/**
 * Created by Enzo Cotter on 2020/2/14.
 */
public class ProtobufAnyEncoder extends ChannelOutboundHandlerAdapter {
    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        if (Message.class.isAssignableFrom(msg.getClass())) {
            ByteBuf byteBuf = ctx.alloc().buffer();
            byte[] byteArray = Any.pack((Message) msg).toByteArray();
            byteBuf.writeBytes(byteArray);
            ctx.writeAndFlush(byteBuf);
            byteBuf.release();
        }

    }
}
