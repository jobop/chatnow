package com.chatnow.core.processors.inbound;

import com.chatnow.core.dao.UserDao;
import com.chatnow.core.domain.inboundmsg.InboundMsg;
import com.chatnow.core.domain.inboundmsg.command.RegiestCommand;
import com.chatnow.core.domain.outboundmsg.resp.RegiestCommandResp;
import com.chatnow.core.domain.protobuf.command.Regiest;
import com.chatnow.core.processors.InboundProcessor;
import com.chatnow.core.processors.ResultCode;
import io.netty.channel.ChannelHandlerContext;

public class RegiestProcessor implements InboundProcessor<Regiest.RegiestCommand> {


    @Override
    public void process(ChannelHandlerContext ctx, Regiest.RegiestCommand command) {
        UserDao.getInstance().addUser(command.getUserName(), command.getPwd());

        Regiest.RegiestCommandResp resp = Regiest.RegiestCommandResp.newBuilder().setResultCode(ResultCode.SUCCESS.ordinal()).build();
        ctx.channel().writeAndFlush(resp);
    }
}
