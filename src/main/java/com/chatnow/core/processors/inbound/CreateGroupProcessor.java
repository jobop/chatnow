package com.chatnow.core.processors.inbound;

import com.chatnow.core.dao.GroupDao;
import com.chatnow.core.domain.inboundmsg.InboundMsg;
import com.chatnow.core.domain.inboundmsg.command.CreateGroupCommand;
import com.chatnow.core.domain.outboundmsg.resp.CreateGroupCommandResp;
import com.chatnow.core.domain.protobuf.command.CreateGroup;
import com.chatnow.core.processors.InboundProcessor;
import com.chatnow.core.processors.ResultCode;
import com.chatnow.core.session.Session;
import com.chatnow.core.session.SessionManager;
import io.netty.channel.ChannelHandlerContext;

/**
 * Created by Enzo Cotter on 2020/2/10.
 */
public class CreateGroupProcessor implements InboundProcessor<CreateGroup.CreateGroupCommand> {

    @Override
    public void process(ChannelHandlerContext ctx, CreateGroup.CreateGroupCommand command) {


        String authToken = command.getToken();
        Session session = SessionManager.getInstance().getSession(authToken);

        CreateGroup.CreateGroupCommandResp resp;

        if (null == session) {
            resp = CreateGroup.CreateGroupCommandResp.newBuilder().setResultCode(ResultCode.NO_LOGIN.ordinal()).build();
            ctx.channel().writeAndFlush(resp);
            return;
        }

        String userName = (String) session.getByKey("userName");
        System.out.println(userName);

        GroupDao.getInstance().createGroup(command.getGroupName(), userName);
        //成功
        resp = CreateGroup.CreateGroupCommandResp.newBuilder().setResultCode(ResultCode.SUCCESS.ordinal()).build();
        ctx.channel().writeAndFlush(resp);


    }

}
