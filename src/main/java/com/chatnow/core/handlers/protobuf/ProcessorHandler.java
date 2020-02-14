package com.chatnow.core.handlers.protobuf;

import com.chatnow.core.domain.inboundmsg.InboundMsg;
import com.chatnow.core.processors.InboundProcessorFactory;
import com.google.protobuf.Any;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * Created by Enzo Cotter on 2020/2/14.
 */
public class ProcessorHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf byteBuf = (ByteBuf) msg;


        try {
            byte[] bytes = new byte[byteBuf.readableBytes()];
            byteBuf.readBytes(bytes);
            Any any = Any.parseFrom(bytes);
            InboundProcessorFactory.process(ctx, any);

        } finally {
            byteBuf.release();
        }

    }

}
