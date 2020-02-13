package com.chatnow.core.domain.outboundmsg.resp;

import com.chatnow.core.domain.enums.OutboundMsgTypeEnums;
import com.chatnow.core.domain.outboundmsg.OutboundMsg;
import com.chatnow.core.utils.ByteBufUtils;
import io.netty.buffer.ByteBuf;

/**
 * Created by Enzo Cotter on 2020/2/11.
 */
public class LoginCommandResp extends OutboundMsg {
    int resultCode;

    String authToken;

    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }


    public int getType() {
        return OutboundMsgTypeEnums.RESULT_TO_LOGIN.ordinal();
    }

    public void writeToMessage(ByteBuf buf) {
        buf.writeInt(getVersion());
        buf.writeInt(getType());
        buf.writeInt(resultCode);
        ByteBufUtils.writeString(buf,authToken);

    }
}
