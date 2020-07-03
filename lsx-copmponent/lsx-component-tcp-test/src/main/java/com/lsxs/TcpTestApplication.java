//package com.lsxs;
//
//
//import org.springframework.boot.ApplicationRunner;
//import org.springframework.boot.SpringApplication;
//import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.DependsOn;
//import org.springframework.integration.dsl.IntegrationFlow;
//import org.springframework.integration.dsl.IntegrationFlows;
//import org.springframework.integration.dsl.Transformers;
//import org.springframework.integration.ip.dsl.Tcp;
//import org.springframework.integration.ip.tcp.serializer.AbstractByteArraySerializer;
//import org.springframework.integration.ip.tcp.serializer.TcpCodecs;
//
//@SpringBootApplication
//public class TcpTestApplication {
//
//    public static void main(String[] args) {
//        SpringApplication.run(TcpTestApplication.class, args);
//    }
//
//
//
//
//    @Bean
//    public IntegrationFlow client() {
//        return IntegrationFlows.from(MyGateway.class)
//                .handle(Tcp.outboundGateway(
//                        Tcp.netClient("localhost", 20000).connectTimeout(1000)
////                                .serializer(codec()) // default is CRLF
////                                .deserializer(codec()))) // default is CRLF
//                ))
//                .transform(Transformers.objectToString()) // byte[] -> String
//                .get();
//    }
//
//    @Bean
//    public AbstractByteArraySerializer codec() {
//        return TcpCodecs.lf();
//    }
//
//    @Bean
//    @DependsOn("client")
//    ApplicationRunner runner(MyGateway gateway) {
//        return args -> {
//            try {
//                System.out.println(gateway.exchange("foo"));
//                System.out.println(gateway.exchange("bar"));
//            }catch (Exception e){
//                e.printStackTrace();
//            }
//        };
//    }
//
//    public interface MyGateway {
//
//        String exchange(String out);
//
//    }
//}
