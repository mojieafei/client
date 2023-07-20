package com.example.client.server;

import com.example.client.controller.param.ChatSendMsgParam;
import com.example.client.controller.param.VersatileMessageParam;
import com.example.server.gen.proto.*;
import io.micrometer.common.util.StringUtils;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

import java.util.Iterator;


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
        SendMessageReply sendMessageReply = chatServiceBlockingStub.sendMessageToSomeone(request);
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

    public Boolean getConnection(Long userId) {
        ConnectionRequest.Builder request = ConnectionRequest.newBuilder().setUserId(userId);
        Iterator<ConnectionReply> connection = chatServiceBlockingStub.getConnection(request.build());
        if(connection.hasNext() && Boolean.TRUE.toString().equals(connection.next().getText())){
            mockClientReceiveMessage(userId, connection);
            return true;
        }
        return false;
    }

    private void mockClientReceiveMessage(Long userId, Iterator<ConnectionReply> connection) {
        // 开启新线程 模拟随时接收用户发来的消息
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (connection.hasNext()){
                    ConnectionReply next = connection.next();
                    System.out.println(Thread.currentThread().getName() + "收到了用户" + next.getFormUserId() + "发送的消息" + next.getText() + "消息id ：" + next.getMessageId());
                    System.out.println("消息体为：" + next.getVersatileMessage());
                }
            }
        });
        thread.setName("用户" + userId + "的链接" );
        thread.start();
    }

    public Boolean giveUpConnection(Long userId) {
        ConnectionRequest.Builder request = ConnectionRequest.newBuilder().setUserId(userId);
        GiveUpConnectionReply giveUpConnectionReply = chatServiceBlockingStub.giveUpConnection(request.build());
        return giveUpConnectionReply.getResult();
    }

    public Boolean sendVersatileMessageToSomeone(VersatileMessageParam versatileMessageParam) {
        VersatileMessage.Builder versatileMessage = VersatileMessage.newBuilder().setMessageId(versatileMessageParam.getMessageId())
                .setType(OperationType.valueOf(versatileMessageParam.getOperationType()))
                .setExt(versatileMessageParam.getExt());
        VersatileMessageRequest.Builder builder = VersatileMessageRequest.newBuilder().setFormUserId(versatileMessageParam.getFromUserId())
                .setToUserId(versatileMessageParam.getToUserId()).setVersatileMessage(versatileMessage);
        VersatileMessageReply versatileMessageReply = chatServiceBlockingStub.sendVersatileMessageToSomeone(builder.build());
        return versatileMessageReply.getResult();
    }
}
