package com.example.admin.config;

import com.example.admin.filter.JwtAuthenticationTokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * @Description:Security配置类
 * @Author:Rainbow
 * @CreateTime:2023/12/1814:35
 */
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    //虽然我们注入的不是自己写的认证失败处理器，但是最终用的实际上就是我们写的，Security会自己去找我们写的
    AuthenticationEntryPoint authenticationEntryPoint;

    @Autowired
    //虽然我们注入的不是自己写的授权失败处理器，但是最终用的实际上就是我们写的，Security会自己去找我们写的
    AccessDeniedHandler accessDeniedHandler;

    @Autowired
    //注入我们在blog工程写的JwtAuthenticationTokenFilter过滤器
    private JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable() // 关闭 CSRF
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 不使用 Session
                .and()
                .authorizeRequests()
                // 对于登录接口允许匿名访问
                .antMatchers("user/login").anonymous()


                //需要token的才能访问的页面地址
                //退出登录的配置。如果'没登录'就调用'退出登录'，就会报错，报的错设置为'401 需要登录后操作'，也就是authenticated
                .antMatchers("user/logout").authenticated()

                //为方便测试认证过滤器，我们把查询友链的接口设置为需要token才能访问。然后我们去访问的时候就能测试登录认证功能了
//                .antMatchers("/link/getAllLink").authenticated()


                // 其他请求不需要认证即可访问，这部分可能需要进一步细化
                .anyRequest().permitAll();

        http.logout().disable(); // 禁用注销功能

        //第一个参数是你要添加的过滤器，第二个参数是你想把你的过滤器添加到哪个security官方过滤器之前
        http.addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);

        //把我们写的自定义异常处理器配置给Security
        http.exceptionHandling()
                .authenticationEntryPoint(authenticationEntryPoint)
                .accessDeniedHandler(accessDeniedHandler);

        http.cors(); // 允许跨域访问

        // 可能的其他配置，比如添加登录、异常处理等
        // http.formLogin().loginProcessingUrl("/perform_login");
        // http.exceptionHandling().accessDeniedPage("/access_denied");
    }
}