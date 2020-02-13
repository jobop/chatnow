package com.chatnow.core.domain.inboundmsg.resp;

import com.chatnow.core.domain.inboundmsg.InboundMsg;
import com.chatnow.core.utils.ByteBufUtils;
import io.netty.buffer.ByteBuf;

/**
 * Created by Enzo Cotter on 2020/2/8.
 */
public class ResultFromUserReceivedMsg extends InboundMsg {
    private int resultCode;

    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

    public void writeToMessage(ByteBuf buf) {
        buf.writeInt(getVersion());
        buf.writeInt(getType());
        buf.writeInt(resultCode);
    }
}
