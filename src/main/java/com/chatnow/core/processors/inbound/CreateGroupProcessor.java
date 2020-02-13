package com.chatnow.core.processors.inbound;

import com.chatnow.core.dao.GroupDao;
import com.chatnow.core.domain.inboundmsg.InboundMsg;
import com.chatnow.core.domain.inboundmsg.command.CreateGroupCommand;
import com.chatnow.core.domain.outboundmsg.resp.CreateGroupCommandResp;
import com.chatnow.core.processors.InboundProcessor;
import com.chatnow.core.processors.ResultCode;
import com.chatnow.core.session.Session;
import com.chatnow.core.session.SessionManager;
import io.netty.channel.ChannelHandlerContext;

/**
 * Created by Enzo Cotter on 2020/2/10.
 */
public class CreateGroupProcessor implements InboundProcessor {
    public void process(ChannelHandlerContext ctx, InboundMsg msg) {

        CreateGroupCommand command = (CreateGroupCommand) msg;
        String authToken = command.getToken();
        Session session = SessionManager.getInstance().getSession(authToken);

        CreateGroupCommandResp resp = new CreateGroupCommandResp();
        if (null == session) {
            resp.setResultCode(ResultCode.NO_LOGIN.ordinal());
            ctx.channel().writeAndFlush(resp);
            return;
        }

        String userName = (String) session.getByKey("userName");
        System.out.println(userName);

        GroupDao.getInstance().createGroup(command.getGroupName(), userName);
        //成功
        resp.setResultCode(ResultCode.SUCCESS.ordinal());
        ctx.channel().writeAndFlush(resp);

    }
}
