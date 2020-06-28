package com.lsxs.configure;

import com.lsxs.repository.UserRepository;
import com.lsxs.util.UserJwt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Optional;

@Service
public class CustomUserService implements UserDetailsService {

    @Autowired
    private ClientDetailsService clientDetailsService;

    @Autowired
    private UserRepository userRepository;


//    @Autowired
//    private UserFeign userFeign;

    /*
     * 自定义授权认证
     * @param username
     * @return
     * @throws UsernameNotFoundException
     * */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //==============================  客户端信息认证  start ==============================


        System.out.println("------------------ loadUserByUsername ---------------");
        System.out.println("username = "+ username);

        //取出身份，如果身份为空则说明没有认证
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        //没有认证统一采用httpbasic认证，httpbasic中存储了client_id 和 client_secret, 开始认证 client_id 和 client_secret
        if (authentication == null){
            System.out.println("authentication null");
            System.out.println("-->");
            ClientDetails clientDetails = clientDetailsService.loadClientByClientId(username);
            if (clientDetails!=null){
                //密钥
                String clientSecret = clientDetails.getClientSecret();
                //静态方式
                //return new User(username, new BCryptPasswordEncoder.encode(clientSecret), AuthorityUtils.commaSeparatedStringToAuthorityList(""));
                //数据库查找方式
                return new User(username,   //客户端ID
                        clientSecret,   //密钥   数据库中已经加过密  不需要
                        AuthorityUtils.commaSeparatedStringToAuthorityList(""));//权限

            }
        }

        //==============================  客户端信息认证  end ==============================




        //==============================  用户账号密码信息认证  start ==============================

        System.out.println("user");
        if (StringUtils.isEmpty(username)){
            return null;
        }


        /*
         *   从数据库中查询用户信息
         *   1。 没有令牌， Feign 调用之前 生成令牌
         *   2。 Feign 调用之前， 令牌需要携带过去
         *   3。 Feign 调用之前， 令牌需要存放到Header文件中
         *   4。 请求 -> Feign 调用 -> 拦截器 RequestInterceptor  -> Feign 调用之前拦截
         **/



        //根据用户名查询用户信息
        //String pwd = new BCryptPasswordEncoder().encode("lsxlsx");

        Optional<com.lsxs.entity.User> user = null;
        try{

            com.lsxs.entity.User exampleUser = new com.lsxs.entity.User();
            exampleUser.setUsername(username);




            user = userRepository.findOne(Example.of(exampleUser));//userFeign.findByUsername(username).getData();

//            System.out.println("user = "+ user.toString());

        }catch (Exception e){
            e.printStackTrace();
            System.out.println(e.getMessage());
        }


        System.out.println("userFeign 查询完毕");



        if (!user.isPresent()){
            System.out.println("用户不存在");
            throw new UsernameNotFoundException("用户不存在");
        }else{
            System.out.println(user.toString());
        }



        //

        //创建user对象
        String permission = user.get().getRole();
        UserJwt userDetails = new UserJwt(username, user.get().getPassword(), AuthorityUtils.commaSeparatedStringToAuthorityList(permission));

        //==============================  用户账号密码信息认证  end ==============================


        return userDetails;
    }

}
