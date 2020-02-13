package com.chatnow.core.domain.outboundmsg.resp;

import com.chatnow.core.domain.enums.OutboundMsgTypeEnums;
import com.chatnow.core.domain.outboundmsg.OutboundMsg;
import io.netty.buffer.ByteBuf;

/**
 * Created by Enzo Cotter on 2020/2/11.
 */
public class HeartBeatCommandResp extends OutboundMsg {
    int resultCode;

    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }


    public int getType() {
        return OutboundMsgTypeEnums.HEARTBEAT.ordinal();
    }
    public void writeToMessage(ByteBuf buf) {
        buf.writeInt(getVersion());
        buf.writeInt(getType());
        buf.writeInt(resultCode);
    }
}
