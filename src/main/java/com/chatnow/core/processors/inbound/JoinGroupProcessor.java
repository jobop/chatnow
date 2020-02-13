package com.chatnow.core.processors.inbound;

import com.chatnow.core.dao.GroupDao;
import com.chatnow.core.domain.inboundmsg.InboundMsg;
import com.chatnow.core.domain.inboundmsg.command.CreateGroupCommand;
import com.chatnow.core.domain.inboundmsg.command.JoinGroupCommand;
import com.chatnow.core.domain.outboundmsg.resp.JoinGroupCommandResp;
import com.chatnow.core.processors.InboundProcessor;
import com.chatnow.core.processors.ResultCode;
import com.chatnow.core.session.Session;
import com.chatnow.core.session.SessionManager;
import io.netty.channel.ChannelHandlerContext;

/**
 * Created by Enzo Cotter on 2020/2/10.
 */
public class JoinGroupProcessor implements InboundProcessor {
    public void process(ChannelHandlerContext ctx, InboundMsg msg) {
        JoinGroupCommand command = (JoinGroupCommand) msg;
        String authToken = command.getToken();
        Session session = SessionManager.getInstance().getSession(authToken);
        JoinGroupCommandResp resp = new JoinGroupCommandResp();
        if (null == session) {
            resp.setResultCode(ResultCode.NO_LOGIN.ordinal());
            ctx.channel().writeAndFlush(resp);
            return;

        }

        GroupDao.getInstance().joinGroup((String) session.getByKey("userName"), command.getGroupName());
        //成功
        resp.setResultCode(ResultCode.SUCCESS.ordinal());
        ctx.channel().writeAndFlush(resp);
    }
}
