//package com.lsxs.configure;
//
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.integration.annotation.Filter;
//import org.springframework.integration.annotation.Router;
//import org.springframework.integration.annotation.ServiceActivator;
//import org.springframework.integration.annotation.Transformer;
//import org.springframework.integration.ip.udp.UnicastReceivingChannelAdapter;
//import org.springframework.messaging.Message;
//
//
//@Configuration
//public class UdpConfiguration {
//
//
//    @Bean
//    public UnicastReceivingChannelAdapter unicastReceivingChannelAdapter(){
//        UnicastReceivingChannelAdapter adapter = new UnicastReceivingChannelAdapter(4567);
//        adapter.setOutputChannelName("udp");
//        return adapter;
//    }
//
//    @Transformer(inputChannel = "udp", outputChannel = "udpString")
//    public String transformer(Message<?> message){
//        return new String((byte[]) message.getPayload());//把接收的数据转化为字符串
//    }
//
//    @Filter(inputChannel = "udpString", outputChannel = "udpFilter")
//    public boolean filter(String message){
//        return message.startsWith("abc");//如果接收数据开头不是abc直接过滤掉
//    }
//
//
//    @Router(inputChannel = "udpFilter")
//    public String routing(String message){
//        if (message.contains("1")){//当接收数据包含数字1时
//            return "udpRoute1";
//        }else{
//            return "udpRoute2";
//        }
//    }
//
//
//    @ServiceActivator(inputChannel = "udpRoute1")
//    public void udpMessageHandle1(String message){
//        System.out.println("udp1: "+message);
//    }
//
//
//    @ServiceActivator(inputChannel = "udpRoute2")
//    public void udpMessageHandle2(String message){
//        System.out.println("udp2: "+message);
//    }
//
//
//
//
//}
