package com.chatnow.core.domain.enums;

/**
 * Created by Enzo Cotter on 2020/2/8.
 */
public enum InboundMsgTypeEnums {
    REGISTER, LOGIN, SEND_TO_USER, RESULT_FROM_USER_RECEIVED_MSG, CREATE_GROUP, JOIN_GROUP, SEND_TO_GROUP, HEARTBEAT;


    public static InboundMsgTypeEnums valueOf(int index) {
        for (InboundMsgTypeEnums e : InboundMsgTypeEnums.values()) {
            if (e.ordinal() == index) {
                return e;
            }
        }
        return null;
    }
}
