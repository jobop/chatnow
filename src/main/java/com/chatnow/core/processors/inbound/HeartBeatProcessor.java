package com.chatnow.core.processors.inbound;

import com.chatnow.core.domain.protobuf.command.HeartBeat;
import com.chatnow.core.processors.InboundProcessor;
import io.netty.channel.ChannelHandlerContext;

/**
 * Created by Enzo Cotter on 2020/2/10.
 */
public class HeartBeatProcessor implements InboundProcessor<HeartBeat.HeartBeatCommand> {

    @Override
    public void process(ChannelHandlerContext ctx, HeartBeat.HeartBeatCommand msg) {

    }
}
