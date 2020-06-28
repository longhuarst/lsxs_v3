package com.lsxs.configure;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


@Configuration
@EnableWebSecurity          // 开启websecurity
@Order(-1)
public class WebSecuityConfig extends WebSecurityConfigurerAdapter {

    /*
     * 忽略安全拦截的URL
     * @param web
     * @throws Exception
     * */

    @Override
    public void configure(WebSecurity web) throws Exception {
        //super.configure(web);
        System.out.println("void configure(WebSecurity web) throws Exception");
        web.ignoring().antMatchers("/oauth/login",
                "/oauth/logout",
                "/oauth/toLogin",
                "/user/login",
                "/login.html",
                "/css/**",
                "/data/**",
                "/fonts/**",
                "/img/**",
                "/js/**");
    }

    /*
     * 创建授权管理认证对象
     * @return
     * @throws Exception
     * */
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        //return super.authenticationManagerBean();
        AuthenticationManager manager = super.authenticationManagerBean();
        return manager;
    }

    /*
     * 采用BCryptPasswordEncoder对密码进行编码
     * @return
     * */
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    /*
     * @param http
     * @throws Exception
     * */

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //super.configure(http);

//        http.httpBasic();//设置basic登陆

        http.csrf().disable()
                .httpBasic()    //启用Http基本身份认证
                .and()
                .formLogin()    //启动表单身份认证
                .and()
                .authorizeRequests()
                .antMatchers("/login").permitAll()
                .and()
                .authorizeRequests()
                .antMatchers("/toLogin").permitAll()
                .and()
                .authorizeRequests()    //限制基于request的请求访问
                .anyRequest()
                .authenticated();//其他都需要经过认证才可以访问
//                    .hasRole("USER");   //其他请求都需要经过认证   这样会出现问题

        //开启表单登陆
        http.formLogin().loginPage("/oauth/toLogin")//设置访问登陆页面的路径
                .loginProcessingUrl("oauth/login");//设置执行登陆操作的路径

    }




}
