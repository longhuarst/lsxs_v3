package com.lsxs.configure;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.bootstrap.encrypt.KeyProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.security.KeyPair;
import java.util.Arrays;

@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {


    //数据源，用于从数据库中获取数据进行认证操作，测试可以从内存中获取
    @Autowired
    private DataSource dataSource;

    //jwt令牌转换器
    @Autowired
    private JwtAccessTokenConverter jwtAccessTokenConverter;

    @Autowired
    private UserDetailsService userDetailsService;

    //授权认证管理器 【密码模式】
    @Autowired
    private AuthenticationManager authenticationManager;

    //授权认证管理器【授权码模式模式】
//    @Autowired
//    private AuthorizationCodeServices authorizationCodeServices;


    @Autowired
    private RedisConnectionFactory redisConnectionFactory;

    //令牌持久化存储接口
    @Autowired
    TokenStore tokenStore;

    @Autowired
    private CustomUserAuthenticationConverter customUserAuthenticationConverter;




    //读取密钥的配置
    @Bean("keyProp")
    public KeyProperties keyProperties(){
        return new KeyProperties();
    }

    @Resource(name = "keyProp")
    private KeyProperties keyProperties;


    //客户端配置
    @Bean
    public ClientDetailsService clientDetails(){
        return new JdbcClientDetailsService(dataSource);
    }


    //使用 jwt 令牌
    @Bean
    public TokenStore tokenStore(){//JwtAccessTokenConverter jwtAccessTokenConverter){
        return new JwtTokenStore(jwtAccessTokenConverter);
    }



    /*
     * jwt令牌转换器
     * */
    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter(){//CustomUserAuthenticationConverter customUserAuthenticationConverter){
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        KeyPair keyPair = new KeyStoreKeyFactory(
                keyProperties.getKeyStore().getLocation(),              //证书路径 lsx.jks
                keyProperties.getKeyStore().getSecret().toCharArray())  //证书密钥 ??????这里没有填写
                .getKeyPair(
                        keyProperties.getKeyStore().getAlias(),            //证书别名 lsx
                        keyProperties.getKeyStore().getPassword().toCharArray());   //证书密码 ？？？？这里没有填写
        converter.setKeyPair(keyPair);

//        converter.setVerifier(new RsaVerifier());

        //配置自定义的CustomUserAuthenticationConverter        ------- 这个不能加    结论就  server_error Internal Server Error
//        DefaultAccessTokenConverter accessTokenConverter = (DefaultAccessTokenConverter) converter.getAccessTokenConverter();
//        accessTokenConverter.setUserTokenConverter(customUserAuthenticationConverter);

        return converter;
    }







    /*
     * 客户端信息配置
     * 用来配置客户端详情服务（ClientDetailsService），客户端详情信息在这里进行初始化，你能够把客户端详情信息写死在这里或者通过数据库才存取详情信息
     * @param clients
     * @throws Exception
     * */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
//        super.configure(clients);

        //在数据库中查询
        clients.jdbc(dataSource).clients(clientDetails());

//        //使用 in-memory存储
//            String secret = new BCryptPasswordEncoder().encode("lsxlsx");
//            System.out.println("secret = " + secret);
//            clients.inMemory()//使用in-memory存储
//                    .withClient("lsx")//client_id
//                    .secret(secret)//客户端秘钥
////                .resourceIds("res1")//客户端可以访问的资源列表
//                    .authorizedGrantTypes("authorization_code","password","client_credentials","implicit","refresh_token")//该cleint允许的5种授权类型
//                    .scopes("all","app")//允许的授权范围  -- 客户端的权限
//                    .autoApprove(false) // -- false --> 如果是授权码模式，会跳转到授权页面，  true-->直接授权
////                .refreshTokenValiditySeconds(60)  //刷新令牌有效期
////                .accessTokenValiditySeconds(60)   //访问令牌有效期
//                    //加上验证回调地址
//                    .redirectUris("https://www.baidu.com");//



    }

    /*
     * 授权服务器端点配置
     * 用于配置令牌（token）的访问端点和令牌服务（token services）
     * @param endpoints
     * @throws Exception
     * */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        //super.configure(endpoints);

        //令牌增强
        TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
        tokenEnhancerChain.setTokenEnhancers(Arrays.asList(jwtAccessTokenConverter));//采用jwt令牌方式

        endpoints.accessTokenConverter(jwtAccessTokenConverter)//jwtAccessTokenConverter
                .authenticationManager(authenticationManager)//认证管理器 【密码模式需要】
//                .authorizationCodeServices(authorizationCodeServices)//授权码模式需要      这个 需要自己构建 authorizationCodeServices @bean   获取授权吗 没有这个也能获取到
//                .tokenServices(tokenService())//令牌管理服务 【不管是 密码 还是 授权码 模式，这个管理服务都必须要】
                .tokenStore(tokenStore) //令牌存储
//                .tokenEnhancer(tokenEnhancerChain) //令牌增强
//                .allowedTokenEndpointRequestMethods(HttpMethod.POST)//允许post提交
                .userDetailsService(userDetailsService); //用户信息service

    }

    /*
     * 授权服务器的安全配置
     * 用于配置令牌端点的安全约束【安全策略】
     * @param security
     * @throws Exception
     * */

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security
                //.allowFormAuthenticationForClients()
                .passwordEncoder(new BCryptPasswordEncoder())
                .tokenKeyAccess("permitAll()")// oauth/token_key
                .allowFormAuthenticationForClients()//允许表单认证
                .checkTokenAccess("permitAll()");//检测令牌  oauth/check_token
        //super.configure(security);
    }
}
