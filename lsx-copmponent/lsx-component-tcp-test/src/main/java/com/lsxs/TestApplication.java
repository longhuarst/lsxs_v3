//package com.lsxs;
//
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.SpringApplication;
//import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.context.annotation.Bean;
//import org.springframework.integration.annotation.MessagingGateway;
//import org.springframework.integration.annotation.ServiceActivator;
//import org.springframework.integration.annotation.Transformer;
//import org.springframework.integration.channel.DirectChannel;
//import org.springframework.integration.channel.FixedSubscriberChannel;
//import org.springframework.integration.event.inbound.ApplicationEventListeningMessageProducer;
//import org.springframework.integration.ip.tcp.TcpInboundGateway;
//import org.springframework.integration.ip.tcp.TcpReceivingChannelAdapter;
//import org.springframework.integration.ip.tcp.TcpSendingMessageHandler;
//import org.springframework.integration.ip.tcp.connection.AbstractClientConnectionFactory;
//import org.springframework.integration.ip.tcp.connection.AbstractServerConnectionFactory;
//import org.springframework.integration.ip.tcp.connection.TcpNioClientConnectionFactory;
//import org.springframework.integration.ip.tcp.connection.TcpNioServerConnectionFactory;
//import org.springframework.integration.ip.tcp.serializer.ByteArrayLfSerializer;
//import org.springframework.integration.ip.tcp.serializer.ByteArrayRawSerializer;
//import org.springframework.messaging.MessageChannel;
//import org.springframework.messaging.MessageHandler;
//import org.springframework.scheduling.annotation.EnableScheduling;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//
//import java.text.SimpleDateFormat;
//import java.util.Map;
//
//@SpringBootApplication
//@EnableScheduling
//public class TestApplication {
//
//
//    public static void main(String[] args) {
//        SpringApplication.run(TestApplication.class, args);
//    }
//
//    @Autowired
//    private Gateway gateway;
//
//    @Autowired
//    private GatewaySend gatewaySend;
//
//
//    @Scheduled(fixedDelay = 1000L)
//    public void sendMessageJob() {
//        String currentTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(System.currentTimeMillis());
//        String msg = "{\"currentTime\":\"" + currentTime + "\"}";
//        System.out.println("客户端发送消息" + msg);
////            gateway.sendMessage("");
//        gatewaySend.sendMessage(msg);
//
//    }
//
//    // server
//    //服务器
//
//    @Component
//    @MessagingGateway(defaultRequestChannel = "sendMessageChannelServer")
//    public interface GatewaySend {
//        void sendMessage(String message);
//    }
//
//    @Bean
//    @ServiceActivator(inputChannel = "sendMessageChannelServer")
//    public TcpSendingMessageHandler serverTcpSendingMessageHandler() {
//        TcpSendingMessageHandler tcpSendingMessageHandler = new TcpSendingMessageHandler();
//        tcpSendingMessageHandler.setConnectionFactory(serverConnectionFactory());
//        return tcpSendingMessageHandler;
//    }
//
//
//    @Bean
//    public AbstractServerConnectionFactory serverConnectionFactory() {
//        AbstractServerConnectionFactory factory = new TcpNioServerConnectionFactory(20001);
//        factory.setSerializer(new ByteArrayLfSerializer());
//        factory.setDeserializer(new ByteArrayLfSerializer());
//        return factory;
//    }
//
//    @Bean
//    public MessageChannel requestChannel() {
//        return new DirectChannel();
//    }
//
////        @Bean
////        public TcpInboundGateway tcpInboundGateway() {
////            TcpInboundGateway tcpInboundGateway = new TcpInboundGateway();
////            tcpInboundGateway.setConnectionFactory(serverConnectionFactory());
////            tcpInboundGateway.setRequestChannel(requestChannel());
////            return tcpInboundGateway;
////        }
//
//    @Bean
//    public TcpReceivingChannelAdapter serverTcpReceivingChannelAdapter() {
//        TcpReceivingChannelAdapter tcpReceivingChannelAdapter = new TcpReceivingChannelAdapter();
//        tcpReceivingChannelAdapter.setConnectionFactory(serverConnectionFactory());
//        tcpReceivingChannelAdapter.setOutputChannelName("serverOutputChannel");
//        return tcpReceivingChannelAdapter;
//    }
//
//    @Transformer(inputChannel = "serverOutputChannel", outputChannel = "serverOutputChannel2")
//    public String serverConvert(byte[] bytes) {
//        return new String(bytes);
//    }
//
//
//    @Bean
//    public MessageChannel serverOutputChannel2() {
//        return new FixedSubscriberChannel(msg -> {
//            System.out.println("server处理响应消息=" + msg.getPayload());// client handle
//        });
//    }
//
//    @Bean
//    @ServiceActivator(inputChannel = "serverOutputChannel2")
//    public MessageHandler serverhandleResponse() {
//        return msg -> {
//            System.out.println("server处理响应消息=" + msg.getPayload());// client handle
//        };
//    }
//
//    @ServiceActivator(inputChannel = "serverOutputChannel2")
//    public String serverhandleResponse(String msg) throws Exception {
//        System.out.println("server处理响应消息=" + msg);// client handle
//        return null;
//    }
//
////    @Transformer(inputChannel = "requestChannel", outputChannel = "requestChannel2")
////        public String serverConvert(byte[] bytes) {
////            return new String(bytes);
////        }
//
//
//    @ServiceActivator(inputChannel = "requestChannel2")
//    public String handleRequest(String msg) throws Exception {
//        System.out.println("服务端处理请求消息=" + msg);// server handle
////            ObjectMapper objectMapper = new ObjectMapper();
////            Map map = objectMapper.readValue(msg, Map.class);
////            map.put("result", "is processed");
//        return "123";//objectMapper.writeValueAsString(map);
//    }
//
//    // client
//    @Bean
//    public AbstractClientConnectionFactory clientConnectionFactory() {
//        AbstractClientConnectionFactory factory = new TcpNioClientConnectionFactory("localhost", 20000);
//        factory.setConnectTimeout(1);
//        factory.setSerializer(new ByteArrayRawSerializer());
//        factory.setDeserializer(new ByteArrayLfSerializer());
//
//        return factory;
//    }
//
//    @Component
//    @MessagingGateway(defaultRequestChannel = "sendMessageChannel")
//    public interface Gateway {
//        void sendMessage(String message);
//    }
//
//    @Bean
//    @ServiceActivator(inputChannel = "sendMessageChannel")
//    public TcpSendingMessageHandler tcpSendingMessageHandler() {
//        TcpSendingMessageHandler tcpSendingMessageHandler = new TcpSendingMessageHandler();
//        tcpSendingMessageHandler.setConnectionFactory(clientConnectionFactory());
//        return tcpSendingMessageHandler;
//    }
//
//    @Bean
//    public TcpReceivingChannelAdapter tcpReceivingChannelAdapter() {
//        TcpReceivingChannelAdapter tcpReceivingChannelAdapter = new TcpReceivingChannelAdapter();
//        tcpReceivingChannelAdapter.setConnectionFactory(clientConnectionFactory());
//        tcpReceivingChannelAdapter.setOutputChannelName("outputChannel");
//        return tcpReceivingChannelAdapter;
//    }
//
//    @Transformer(inputChannel = "outputChannel", outputChannel = "outputChannel2")
//    public String clientConvert(byte[] bytes) {
//        return new String(bytes);
//    }
//
//    @Bean
//    public MessageChannel outputChannel2() {
//        return new FixedSubscriberChannel(msg -> {
//            System.out.println("客户端处理响应消息=" + msg.getPayload());// client handle
//        });
//    }
//
//    @Bean
//    @ServiceActivator(inputChannel = "outputChannel2")
//    public MessageHandler handleResponse() {
//        return msg -> {
//            System.out.println("客户端处理响应消息=" + msg.getPayload());// client handle
//        };
//    }
//
//    @ServiceActivator(inputChannel = "outputChannel2")
//    public String handleResponse(String msg) throws Exception {
//        System.out.println("客户端处理响应消息=" + msg);// client handle
//        return null;
//    }
//
//
//
//
//
//
////
////    @Bean
////    public ApplicationEventListeningMessageProducer eventsAdapter(MessageChannel eventChannel, MessageChannel eventErrorChannel){
////        ApplicationEventListeningMessageProducer producer = new ApplicationEventListeningMessageProducer();
////        producer.setEventTypes();
////    }
//
//}
