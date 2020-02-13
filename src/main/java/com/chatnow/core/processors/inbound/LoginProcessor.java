package com.chatnow.core.processors.inbound;

import com.chatnow.core.dao.UserDao;
import com.chatnow.core.domain.inboundmsg.InboundMsg;
import com.chatnow.core.domain.inboundmsg.command.LoginCommand;
import com.chatnow.core.domain.outboundmsg.resp.LoginCommandResp;
import com.chatnow.core.processors.InboundProcessor;
import com.chatnow.core.processors.ResultCode;
import com.chatnow.core.route.RouteManager;
import com.chatnow.core.session.Session;
import com.chatnow.core.session.SessionManager;
import com.chatnow.core.utils.UUIDUtil;
import io.netty.channel.ChannelHandlerContext;

public class LoginProcessor implements InboundProcessor {
    public void process(ChannelHandlerContext ctx, InboundMsg msg) {
        LoginCommand command = (LoginCommand) msg;
        String userName = command.getUserName();
        String pwd = UserDao.getInstance().queryPwdByUserName(userName);
        LoginCommandResp resp = new LoginCommandResp();
        if (null == pwd || "".equals(pwd)) {
            resp.setResultCode(ResultCode.USER_NOT_EXISTS.ordinal());
            resp.setAuthToken("");
            ctx.channel().writeAndFlush(resp);
            return;
        }
        if (!pwd.equals(command.getPwd())) {
            resp.setResultCode(ResultCode.PWD_ERROR.ordinal());
            resp.setAuthToken("");
            ctx.channel().writeAndFlush(resp);
            return;
        }

        String authToken = UUIDUtil.getUUID();
        Session session = SessionManager.getInstance().newSession(authToken);

        session.put("userName", userName);

        RouteManager.getInstance().removeChannel(userName);
        RouteManager.getInstance().addToChannelRoute(userName, ctx.channel());

        resp.setResultCode(ResultCode.SUCCESS.ordinal());
        resp.setAuthToken(authToken);
        ctx.channel().writeAndFlush(resp);
    }
}
