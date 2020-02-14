package com.chatnow.core.processors.inbound;

import com.chatnow.core.dao.GroupDao;
import com.chatnow.core.domain.protobuf.command.JoinGroup;
import com.chatnow.core.processors.InboundProcessor;
import com.chatnow.core.processors.ResultCode;
import com.chatnow.core.session.Session;
import com.chatnow.core.session.SessionManager;
import io.netty.channel.ChannelHandlerContext;

/**
 * Created by Enzo Cotter on 2020/2/10.
 */
public class JoinGroupProcessor implements InboundProcessor<JoinGroup.JoinGroupCommand> {

    @Override
    public void process(ChannelHandlerContext ctx, JoinGroup.JoinGroupCommand command) {

        String authToken = command.getToken();
        Session session = SessionManager.getInstance().getSession(authToken);
        JoinGroup.JoinGroupCommandResp resp = null;
        if (null == session) {
            resp = JoinGroup.JoinGroupCommandResp.newBuilder().setResultCode(ResultCode.NO_LOGIN.ordinal()).build();
            ctx.channel().writeAndFlush(resp);
            return;

        }

        GroupDao.getInstance().joinGroup((String) session.getByKey("userName"), command.getGroupName());
        //成功
        resp = JoinGroup.JoinGroupCommandResp.newBuilder().setResultCode(ResultCode.SUCCESS.ordinal()).build();
        ctx.channel().writeAndFlush(resp);

    }
}
