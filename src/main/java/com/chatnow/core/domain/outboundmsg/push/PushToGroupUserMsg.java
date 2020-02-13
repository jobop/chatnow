package com.chatnow.core.domain.outboundmsg.push;

import com.chatnow.core.domain.enums.OutboundMsgTypeEnums;
import com.chatnow.core.domain.outboundmsg.OutboundMsg;
import com.chatnow.core.utils.ByteBufUtils;
import io.netty.buffer.ByteBuf;

/**
 * Created by Enzo Cotter on 2020/2/11.
 */
public class PushToGroupUserMsg extends OutboundMsg {
    private String groupName;

    private String senderName;
    private String content;

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


    public int getType() {
        return OutboundMsgTypeEnums.PUSH_GROUP_MSG_TO_USER.ordinal();
    }

    public void writeToMessage(ByteBuf buf) {
        buf.writeInt(getVersion());
        buf.writeInt(getType());
        ByteBufUtils.writeString(buf, groupName);
        ByteBufUtils.writeString(buf, senderName);
        ByteBufUtils.writeString(buf, content);
    }
}
