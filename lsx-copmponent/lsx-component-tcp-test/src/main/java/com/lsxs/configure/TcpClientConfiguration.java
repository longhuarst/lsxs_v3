//package com.lsxs.configure;
//
//
//import org.springframework.context.ApplicationEvent;
//import org.springframework.context.ApplicationListener;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.integration.annotation.MessagingGateway;
//import org.springframework.integration.annotation.ServiceActivator;
//import org.springframework.integration.annotation.Transformer;
//import org.springframework.integration.channel.DirectChannel;
//import org.springframework.integration.config.EnableIntegration;
//import org.springframework.integration.ip.tcp.TcpInboundGateway;
//import org.springframework.integration.ip.tcp.TcpOutboundGateway;
//import org.springframework.integration.ip.tcp.TcpReceivingChannelAdapter;
//import org.springframework.integration.ip.tcp.connection.AbstractClientConnectionFactory;
//import org.springframework.integration.ip.tcp.connection.TcpConnection;
//import org.springframework.integration.ip.tcp.connection.TcpConnectionOpenEvent;
//import org.springframework.integration.ip.tcp.connection.TcpNetClientConnectionFactory;
//import org.springframework.integration.ip.tcp.serializer.ByteArrayLfSerializer;
//import org.springframework.messaging.Message;
//import org.springframework.messaging.MessageChannel;
//import org.springframework.messaging.MessageHandler;
//import org.springframework.scheduling.annotation.EnableScheduling;
//import org.springframework.scheduling.annotation.Scheduled;
//
//import java.text.SimpleDateFormat;
//
//
//@EnableIntegration
//@Configuration
//@EnableScheduling
//public class TcpClientConfiguration implements ApplicationListener<ApplicationEvent> {
//
////    private Gateway gateway;
//
////    @Scheduled(fixedDelay = 1000L)
////    public void sendMessageJob() {
////        String currentTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(System.currentTimeMillis());
////        String msg = "{\"currentTime\":\"" + currentTime + "\"}";
////        System.out.println("客户端发送消息=" + msg);
////        gateway.sendMessage(msg);
////    }
//
//    @Bean
//    public AbstractClientConnectionFactory clientCF(){
//        TcpNetClientConnectionFactory factory = new TcpNetClientConnectionFactory("127.0.0.1", 22000);
////        factory.setSingleUse(false);
////        factory.setSoTimeout(3000);
//        factory.setConnectTimeout(3);
////        factory.setSerializer(new ByteArrayLfSerializer());
////        factory.setDeserializer(new ByteArrayLfSerializer());
//        return factory;
//    }
//
//
//
//    @Bean
//    public TcpInboundGateway tcpInGate(AbstractClientConnectionFactory connectionFactory) {
//        TcpInboundGateway inGate = new TcpInboundGateway();
//        inGate.setConnectionFactory(connectionFactory);
//        inGate.setRequestChannel(fromTcp());
//        return inGate;
//    }
//
//    @Bean
//    public MessageChannel fromTcp() {
//        return new DirectChannel();
//    }
//
//
////    @MessagingGateway(defaultRequestChannel = "sendMessageChannel")
////    public interface Gateway{
////        void sendMessage(String msg);
////    }
////
////
////    @Bean
////    @ServiceActivator(inputChannel = "sendMessageChannel")
////    public MessageHandler tcpOutboundGatewayClient(){
////        TcpOutboundGateway tcpOutboundGateway = new TcpOutboundGateway();
////        tcpOutboundGateway.setConnectionFactory(clientCF());
////        tcpOutboundGateway.setOutputChannelName("outputchannelClient");
////        return tcpOutboundGateway;
////    }
////
////
////    @Transformer(inputChannel = "outputchannelClient", outputChannel = "outputchannelClient2")
////    public String clientConvert(byte[] bytes){
////        return new String(bytes);
////    }
////
////    @ServiceActivator(inputChannel = "outputchannelClient2")
////    public String handleResponse(String msg) throws Exception{
////        System.out.println("msg = "+msg);
////        return null;
////    }
////
////
////
////
//////
//////    @Bean
//////    public TcpInboundGateway tcpInboundGateway(AbstractClientConnectionFactory connectionFactory){
//////        TcpInboundGateway tcpInboundGateway = new TcpInboundGateway();
//////        tcpInboundGateway.setClientMode(true);
//////        tcpInboundGateway.setConnectionFactory(connectionFactory);
////////        tcpInboundGateway.setRequestChannelName("clientIn");
//////        tcpInboundGateway.setRequestChannel(clientIn());
//////        tcpInboundGateway.setReplyChannelName("replyTcp");
//////
//////        return tcpInboundGateway;
//////    }
////////
//////    @Bean
//////    public MessageChannel clientIn(){
//////        return new DirectChannel();
//////    }
//////
//////    @ServiceActivator(inputChannel = "clientIn")
//////    public String clientInput(String in){
//////        System.out.println("in: "+ in);
//////        return in.toUpperCase();
//////    }
////
//////
//////    @Bean
//////    public TcpReceivingChannelAdapter tcpReceivingChannelAdapterClient(){
//////        TcpReceivingChannelAdapter adapter = new TcpReceivingChannelAdapter();
//////        adapter.setConnectionFactory(clientCF());
//////        adapter.setOutputChannelName("tcpClient");
//////
//////        return adapter;
//////    }
//////
//////
//////    @ServiceActivator(inputChannel = "tcpClient")
//////    public void messageHandleClient(Message<?> message){
//////        System.out.println(new String((byte[]) message.getPayload()));
//////    }
//////
////
//////    @Bean
//////    public MessageChannel outboundChannel88() {
//////        return new DirectChannel();
//////    }
//////
//////    @Bean
//////    @ServiceActivator(inputChannel = "outboundChannel88")
//////    public MessageHandler outboundGateway(AbstractClientConnectionFactory clientConnectionFactory) {
//////        TcpOutboundGateway tcpOutboundGateway = new TcpOutboundGateway();
//////        tcpOutboundGateway.setConnectionFactory(clientConnectionFactory);
//////        return tcpOutboundGateway;
//////    }
//////
//////
//////
//////    @Bean
//////    public MessageChannel replyTcp(){
//////        return new DirectChannel();
//////    }
//////
//////
//////
//////
////
//////    @Bean
//////    public TcpReceivingChannelAdapter tcpReceivingChannelAdapter8(AbstractClientConnectionFactory connectionFactory){
//////        TcpReceivingChannelAdapter adapter = new TcpReceivingChannelAdapter();
//////        adapter.setConnectionFactory(connectionFactory);
//////        adapter.setOutputChannelName("tcpClientOut");
//////        return adapter;
//////    }
//////
//////
//////    @ServiceActivator(inputChannel = "tcpClientOut")
//////    public void messageHandleClient(Message<?> message){
//////        System.out.println(new String((byte[]) message.getPayload()));
//////    }
////
////
////
//    @Override
//    public void onApplicationEvent(ApplicationEvent applicationEvent) {
//        if (applicationEvent instanceof TcpConnectionOpenEvent){
//            TcpConnection connection = (TcpConnection) applicationEvent.getSource();
//            System.out.println("connection: "+ connection.getConnectionId());
//        }
//    }
//
//
//}
