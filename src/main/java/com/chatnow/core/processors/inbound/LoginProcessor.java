package com.chatnow.core.processors.inbound;

import com.chatnow.core.dao.UserDao;
import com.chatnow.core.domain.protobuf.command.Login;
import com.chatnow.core.processors.InboundProcessor;
import com.chatnow.core.processors.ResultCode;
import com.chatnow.core.route.RouteManager;
import com.chatnow.core.session.Session;
import com.chatnow.core.session.SessionManager;
import com.chatnow.core.utils.UUIDUtil;
import io.netty.channel.ChannelHandlerContext;

public class LoginProcessor implements InboundProcessor<Login.LoginCommand> {

    @Override
    public void process(ChannelHandlerContext ctx, Login.LoginCommand command) {

        String userName = command.getUserName();
        String pwd = UserDao.getInstance().queryPwdByUserName(userName);
        Login.LoginCommandResp resp = null;
        if (null == pwd || "".equals(pwd)) {
            resp = Login.LoginCommandResp.newBuilder().setResultCode(ResultCode.USER_NOT_EXISTS.ordinal()).setAuthToken("").build();
            ctx.channel().writeAndFlush(resp);
            return;
        }
        if (!pwd.equals(command.getPwd())) {
            resp = Login.LoginCommandResp.newBuilder().setResultCode(ResultCode.PWD_ERROR.ordinal()).setAuthToken("").build();
            ctx.channel().writeAndFlush(resp);
            return;
        }

        String authToken = UUIDUtil.getUUID();
        Session session = SessionManager.getInstance().newSession(authToken);

        session.put("userName", userName);

        RouteManager.getInstance().removeChannel(userName);
        RouteManager.getInstance().addToChannelRoute(userName, ctx.channel());


        resp = Login.LoginCommandResp.newBuilder().setResultCode(ResultCode.SUCCESS.ordinal()).setAuthToken(authToken).build();
        ctx.channel().writeAndFlush(resp);

    }
}
