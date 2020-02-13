package com.chatnow.core.domain.inboundmsg.command;

import com.chatnow.core.domain.inboundmsg.InboundMsg;
import com.chatnow.core.utils.ByteBufUtils;
import io.netty.buffer.ByteBuf;

/**
 * Created by Enzo Cotter on 2020/2/8.
 */
public class SendToUserCommand extends InboundMsg {
    private String receiveUserName;
    private String content;
    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
    public String getReceiveUserName() {
        return receiveUserName;
    }

    public void setReceiveUserName(String receiveUserName) {
        this.receiveUserName = receiveUserName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void writeToMessage(ByteBuf buf) {
        buf.writeInt(getVersion());
        buf.writeInt(getType());
        ByteBufUtils.writeString(buf, receiveUserName);
        ByteBufUtils.writeString(buf, content);
        ByteBufUtils.writeString(buf, token);
    }
}
