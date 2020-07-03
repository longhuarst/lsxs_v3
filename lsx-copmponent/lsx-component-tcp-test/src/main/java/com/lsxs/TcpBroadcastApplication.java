package com.lsxs;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.event.EventListener;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.annotation.Transformer;
import org.springframework.integration.channel.FixedSubscriberChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.ip.IpHeaders;
import org.springframework.integration.ip.dsl.Tcp;
import org.springframework.integration.ip.tcp.TcpReceivingChannelAdapter;
import org.springframework.integration.ip.tcp.connection.AbstractClientConnectionFactory;
import org.springframework.integration.ip.tcp.connection.AbstractServerConnectionFactory;
import org.springframework.integration.ip.tcp.connection.TcpConnectionServerListeningEvent;
import org.springframework.integration.ip.tcp.serializer.ByteArrayLfSerializer;
import org.springframework.integration.ip.tcp.serializer.ByteArrayRawSerializer;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.concurrent.CountDownLatch;

@SpringBootApplication
@EnableScheduling
public class TcpBroadcastApplication {


    public static void main(String[] args) {
        SpringApplication.run(TcpBroadcastApplication.class, args);
    }

    @Autowired
    private Broadcaster broadcaster;


    @Scheduled(fixedDelay = 1000L)
    public void sendMessageJob() {
        String currentTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(System.currentTimeMillis());
        String msg = "{\"currentTime\":\"" + currentTime + "\"}";
        System.out.println("客户端发送消息" + msg);
//            gateway.sendMessage("");
//        gatewaySend.sendMessage(msg);
        this.broadcaster.send(msg);
    }


    // 发送 gateway 设置连接id头
    public interface Sender {
        void send(String payload, @Header(IpHeaders.CONNECTION_ID) String connectioId);
    }


    //广播
    @Component
    @DependsOn("gateway")
    public static class Broadcaster {
        @Autowired
        private Sender sender;

        @Autowired
        private AbstractServerConnectionFactory server;

        public void send(String what) {
            this.server.getOpenConnectionIds().forEach(cid -> sender.send(what, cid));
        }
    }


    //服务器配置
    @Configuration
    public static class ServerConfig {

        //同步计数器
        private final CountDownLatch listenLatch = new CountDownLatch(1);

        //服务器连接池
        @Bean
        public AbstractServerConnectionFactory serverFactory() {
            return Tcp.netServer(20000)
                    .serializer(new ByteArrayRawSerializer())
                    .deserializer(new ByteArrayRawSerializer())
                    .get();
        }


        //Inbound adapter 发送 connected！
        @Bean
        public IntegrationFlow tcpServer(AbstractServerConnectionFactory serverFactory) {
            return IntegrationFlows.from(Tcp.inboundAdapter(serverFactory))
                    .transform(p -> "connected!")
                    .channel("toTcp.input")
                    .get();
        }



        //gateway flow for controller
        @Bean
        public IntegrationFlow toTcp(AbstractServerConnectionFactory serverFactory) {
            return f -> f.handle(Tcp.outboundAdapter(serverFactory));
        }

        //gateway flow for controller
        @Bean
        public IntegrationFlow gateway() {
            return IntegrationFlows.from(Sender.class)
                    .channel("toTcp.input")
                    .get();
        }

        //excutor for clients
        @Bean
        public ThreadPoolTaskExecutor exec() {
            ThreadPoolTaskExecutor exec = new ThreadPoolTaskExecutor();
            exec.setCorePoolSize(5);
            return exec;
        }


//        /*
//         * Wait for server to start listenng and start 5 clients.
//         */
//        @Bean
//        public ApplicationRunner runner(TaskExecutor exec, Broadcaster caster) {
//            return args -> {
//                if (!this.listenLatch.await(10, TimeUnit.SECONDS)) {
//                    throw new IllegalStateException("Failed to start listening");
//                }
//                IntStream.range(1, 6).forEach(i -> exec.execute(new Client()));
//            };
//        }

        @EventListener
        public void serverStarted(TcpConnectionServerListeningEvent event) {
            this.listenLatch.countDown();
        }


//        //接收
//        @Bean
//        public TcpReceivingChannelAdapter serverTcpReceivingChannelAdapter() {
//            TcpReceivingChannelAdapter tcpReceivingChannelAdapter = new TcpReceivingChannelAdapter();
//            tcpReceivingChannelAdapter.setConnectionFactory(serverFactory());
//            tcpReceivingChannelAdapter.setOutputChannelName("serverOutputChannel");
//            return tcpReceivingChannelAdapter;
//        }
//
//        @Transformer(inputChannel = "serverOutputChannel", outputChannel = "serverOutputChannel2")
//        public String serverConvert(byte[] bytes) {
//            return new String(bytes);
//        }
//
//
//        @Bean
//        public MessageChannel serverOutputChannel2() {
//            return new FixedSubscriberChannel(msg -> {
//                System.out.println("1.server处理响应消息=" + msg.getPayload());// client handle
//            });
//        }
//
//        @Bean
//        @ServiceActivator(inputChannel = "serverOutputChannel2")
//        public MessageHandler serverhandleResponse() {
//            return msg -> {
//                System.out.println("2.server处理响应消息=" + msg.getPayload());// client handle
//            };
//        }
//
//        @ServiceActivator(inputChannel = "serverOutputChannel2")
//        public String serverhandleResponse(String msg) throws Exception {
//            System.out.println("3.server处理响应消息=" + msg);// client handle
//            return null;
//        }


    }


}
