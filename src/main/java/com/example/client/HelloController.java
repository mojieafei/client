package com.example.client;


import com.example.server.gen.proto.HelloReply;
import com.example.server.gen.proto.HelloRequest;
import com.example.server.gen.proto.TestGrpc;
import io.grpc.Channel;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Scanner;

@RestController
public class HelloController {

    @GrpcClient("server-grpc")
    private TestGrpc.TestStub testBlockingStub;

    @GrpcClient("server-grpc")
    private TestGrpc.TestStub testStub;

    @GetMapping("hello")
    public String hello(){

        StreamObserver<HelloReply> responseObserver = new StreamObserver<HelloReply>() {            // 用stringBuilder保存所有来自服务端的响应

            @Override
            public void onNext(HelloReply helloReply) {
                System.out.println("用户B：");
                System.out.println(helloReply.getText());
            }

            @Override
            public void onError(Throwable t) {
            }

            /**
             * 服务端确认响应完成后，这里的onCompleted方法会被调用
             */
            @Override
            public void onCompleted() {
                System.out.println("系统消息：聊天聊不下去了");
            }
        };

        StreamObserver<HelloRequest> helloRequestStreamObserver = testStub.chatHello(responseObserver);
        HelloRequest build = HelloRequest.newBuilder().setText("我想找你聊天啊").build();
        helloRequestStreamObserver.onNext(build);
        for (;;){
            Scanner str=new Scanner(System.in);
            String next = str.next();
            if("baibai".equals(next)){
                break;
            }
            HelloRequest build1 = HelloRequest.newBuilder().setText(next).build();
            helloRequestStreamObserver.onNext(build1);
        }



        helloRequestStreamObserver.onCompleted();

        return "hello , this is client~";
    }

}
