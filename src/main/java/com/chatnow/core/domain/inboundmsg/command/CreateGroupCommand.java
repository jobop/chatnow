package com.chatnow.core.domain.inboundmsg.command;

import com.chatnow.core.domain.inboundmsg.InboundMsg;
import com.chatnow.core.utils.ByteBufUtils;
import io.netty.buffer.ByteBuf;

/**
 * Created by Enzo Cotter on 2020/2/8.
 */
public class CreateGroupCommand extends InboundMsg {
    private String groupName;
    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public void writeToMessage(ByteBuf buf) {
        buf.writeInt(getVersion());
        buf.writeInt(getType());
        ByteBufUtils.writeString(buf, groupName);
        ByteBufUtils.writeString(buf, token);
    }
}
