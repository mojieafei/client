package com.example.client.server;

import com.example.client.controller.param.ChatSendMsgParam;
import com.example.server.gen.proto.ChatServiceGrpc;
import com.example.server.gen.proto.Code;
import com.example.server.gen.proto.SendMessageReply;
import com.example.server.gen.proto.SendMessageRequest;
import io.micrometer.common.util.StringUtils;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;


@Service
public class ChatService {


    @GrpcClient("server-grpc")
    private ChatServiceGrpc.ChatServiceBlockingStub chatServiceBlockingStub;


    public String sendMsgToSomeOne(ChatSendMsgParam chatSendMsgParam) {

        if(checkParam(chatSendMsgParam)){
            return "发送消息失败，请检查参数";
        }
        // 此处可以查询需要发送消息的必要信息，比如用户名等;
        String fromUserName = "用户id" +  chatSendMsgParam.getFromUserId();
        String toUserName = "用户id" +  chatSendMsgParam.getToUserId();

        // 调用消息服务
        SendMessageRequest request = SendMessageRequest.newBuilder()
                .setFormUserId(chatSendMsgParam.getFromUserId())
                .setFromUserName(fromUserName)
                .setToUserId(chatSendMsgParam.getToUserId())
                .setToUserName(toUserName)
                .setText(chatSendMsgParam.getText()).build();
        SendMessageReply sendMessageReply = chatServiceBlockingStub.sendMessageToSomeOne(request);
        if(Code.SUCCESS.getNumber() == sendMessageReply.getCodeValue()){
            return fromUserName + "向" + toUserName + "成功发送了消息，消息为：" + chatSendMsgParam.getText() ;
        }
         return "发送消息失败";


    }

    private boolean checkParam(ChatSendMsgParam chatSendMsgParam){
        // 校验参数   用id代替代替登陆信息
        if(chatSendMsgParam.getFromUserId() <= 0 || chatSendMsgParam.getToUserId() <= 0
                || StringUtils.isEmpty(chatSendMsgParam.getText())){
            return true;
        }
        return false;
    }
}
