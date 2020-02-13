package com.chatnow.core.domain.inboundmsg.command;

import com.chatnow.core.domain.inboundmsg.InboundMsg;
import com.chatnow.core.utils.ByteBufUtils;
import io.netty.buffer.ByteBuf;

/**
 * Created by Enzo Cotter on 2020/2/8.
 */
public class LoginCommand extends InboundMsg {
    private String userName;
    private String pwd;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public void writeToMessage(ByteBuf buf) {
        buf.writeInt(getVersion());
        buf.writeInt(getType());
        ByteBufUtils.writeString(buf, userName);
        ByteBufUtils.writeString(buf, pwd);
    }
}
