package com.chatnow.core.processors.inbound;

import com.chatnow.core.dao.UserDao;
import com.chatnow.core.domain.inboundmsg.InboundMsg;
import com.chatnow.core.domain.inboundmsg.command.RegiestCommand;
import com.chatnow.core.domain.outboundmsg.resp.RegiestCommandResp;
import com.chatnow.core.processors.InboundProcessor;
import com.chatnow.core.processors.ResultCode;
import io.netty.channel.ChannelHandlerContext;

public class RegiestProcessor implements InboundProcessor {

    public void process(ChannelHandlerContext ctx, InboundMsg msg) {

        RegiestCommand command = (RegiestCommand) msg;
        UserDao.getInstance().addUser(command.getUserName(), command.getPwd());

        RegiestCommandResp resp = new RegiestCommandResp();
        resp.setResultCode(ResultCode.SUCCESS.ordinal());
        ctx.channel().writeAndFlush(resp);

    }
}
