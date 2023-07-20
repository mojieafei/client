package com.example.client.controller.param;

import com.example.server.gen.proto.OperationType;
import lombok.Data;

@Data
public class VersatileMessageParam  extends ChatSendMsgParam{
    private String messageId;
    private String operationType;
    private String ext;
}
