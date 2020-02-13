package com.chatnow.core.domain.inboundmsg;

import com.chatnow.core.domain.Message;
import com.chatnow.core.domain.Serializable;
import com.chatnow.core.domain.enums.InboundMsgTypeEnums;
import io.netty.buffer.ByteBuf;

/**
 * Created by Enzo Cotter on 2020/2/8.
 */
public abstract class InboundMsg implements Message , Serializable {
    private int version;
    private InboundMsgTypeEnums type;

    public int getVersion() {
        return version;
    }

    public int getType() {
        return type.ordinal();
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public void setType(InboundMsgTypeEnums type) {
        this.type = type;
    }
}
