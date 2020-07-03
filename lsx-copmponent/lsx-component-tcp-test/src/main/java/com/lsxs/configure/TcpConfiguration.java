//package com.lsxs.configure;
//
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.integration.annotation.ServiceActivator;
//import org.springframework.integration.ip.tcp.TcpReceivingChannelAdapter;
//import org.springframework.integration.ip.tcp.connection.TcpNetServerConnectionFactory;
//import org.springframework.integration.ip.tcp.serializer.ByteArrayElasticRawDeserializer;
//import org.springframework.integration.ip.tcp.serializer.ByteArrayRawSerializer;
//import org.springframework.messaging.Message;
//
//
//@Configuration
//public class TcpConfiguration {
//
//    @Bean
//    public TcpNetServerConnectionFactory serverConnectionFactory(){
//        TcpNetServerConnectionFactory tcpNetServerConnectionFactory = new TcpNetServerConnectionFactory(1234);
//        tcpNetServerConnectionFactory.setSerializer(new ByteArrayRawSerializer());
//        tcpNetServerConnectionFactory.setDeserializer(new ByteArrayElasticRawDeserializer());
//        tcpNetServerConnectionFactory.setLookupHost(false);
//        return tcpNetServerConnectionFactory;
//    }
//
//    @Bean
//    public TcpReceivingChannelAdapter tcpReceivingChannelAdapter(){
//        TcpReceivingChannelAdapter adapter = new TcpReceivingChannelAdapter();
//        adapter.setConnectionFactory(serverConnectionFactory());
//        adapter.setOutputChannelName("tcp");
//        return adapter;
//    }
//
//    @ServiceActivator(inputChannel = "tcp")
//    public void messageHandle(Message<?> message){
//        System.out.println(new String((byte[]) message.getPayload()));
//    }
//
//}
