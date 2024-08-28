package com.example.config;

import com.example.entity.RestBean;
import com.example.entity.dto.Account;
import com.example.entity.vo.response.AuthorizeVO;
import com.example.filter.JwtAuthenticationFilter;
import com.example.filter.RequestLogFilter;
import com.example.service.AccountService;
import com.example.service.GithubAuthService;
import com.example.utils.Const;
import com.example.utils.JwtUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.endpoint.DefaultAuthorizationCodeTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AccessTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

/**
 * SpringSecurity相关配置
 */
@Configuration
@Slf4j
public class SecurityConfiguration {

    @Resource
    JwtAuthenticationFilter jwtAuthenticationFilter;

    @Resource
    RequestLogFilter requestLogFilter;

    @Resource
    JwtUtils utils;

    @Resource
    AccountService service;

    @Resource
    PasswordEncoder passwordEncoder;

    @Resource
    GithubAuthService githubAuthService;

    @Resource
    private OAuth2AuthorizedClientService authorizedClientService;

    /**
     * 针对于 SpringSecurity 6 的新版配置方法
     * @param http 配置器
     * @return 自动构建的内置过滤器链
     * @throws Exception 可能的异常
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(conf -> conf
                        .requestMatchers("/oauth2/**").permitAll()
                        .requestMatchers("/api/auth/**", "/error","/api/accounts/**").permitAll()
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                        .requestMatchers("/alipay/**").permitAll()
                        .requestMatchers("/api/token/**").permitAll()
                        .requestMatchers("/api/VX/**").permitAll()
                        .requestMatchers("/api/chatAI/**").permitAll()
                        .anyRequest().hasAnyRole(Const.ROLE_DEFAULT)
                )
                .oauth2Login(conf -> conf
                        .successHandler(this::handleOAuth2LoginSuccess)
                        .failureHandler(this::handleOAuth2LoginFailure)
                        .defaultSuccessUrl("http://127.0.0.1:5173/a",true)
                        .permitAll()
                )
                .formLogin(conf -> conf
                        .loginProcessingUrl("/api/auth/login")
                        .failureHandler(this::handleProcess)
                        .successHandler(this::handleProcess)
                        .permitAll()
                )
                .logout(conf -> conf
                        .logoutUrl("/api/auth/logout")
                        .logoutSuccessHandler(this::onLogoutSuccess)
                )
                .exceptionHandling(conf -> conf
                        .accessDeniedHandler(this::handleProcess)
                        .authenticationEntryPoint(this::handleProcess)
                )
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(conf -> conf
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(requestLogFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtAuthenticationFilter, RequestLogFilter.class)
                .build();
    }

    /**
     * OAuth2登录失败处理
     * @param request
     * @param response
     * @param exceptionOrAuthentication
     * @throws IOException
     */
    private void handleOAuth2LoginFailure(HttpServletRequest request, HttpServletResponse response, Object exceptionOrAuthentication) throws IOException {
        PrintWriter writer = response.getWriter();
        if(exceptionOrAuthentication instanceof AccessDeniedException exception) {
            writer.write(RestBean
                    .forbidden(exception.getMessage()).asJsonString());
        } else if(exceptionOrAuthentication instanceof Exception exception){
            writer.write(RestBean
                    .unauthorized(exception.getMessage()).asJsonString());
        }else {
            writer.write(RestBean.failure(500,"未知的OAuth2登录错误").asJsonString());
        }
    }

    /**
     * OAuth2登陆成功处理
     * @param request
     * @param response
     * @param authentication
     * @throws IOException
     */
    private void handleOAuth2LoginSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        OAuth2AuthenticationToken oauth2Token = (OAuth2AuthenticationToken) authentication;
        OAuth2User oauth2User = oauth2Token.getPrincipal();
        String registrationId = oauth2Token.getAuthorizedClientRegistrationId();
        OAuth2AuthorizedClient authorizedClient = authorizedClientService.loadAuthorizedClient(registrationId, oauth2Token.getName());
        // 从 OAuth2User 中获取信息，处理并保存用户信息
        log.info(String.valueOf(oauth2User));
        String username = oauth2User.getAttribute("login");
        Integer githubId = oauth2User.getAttribute("id");
        if (authorizedClient != null && authorizedClient.getAccessToken() != null && username!=null) {
            String accessToken = authorizedClient.getAccessToken().getTokenValue();
            githubAuthService.githubLogin(githubId,username,accessToken);
            Account account = service.findAccountByNameOrEmail(username);

            String jwt = utils.createJwt(new User(username,passwordEncoder.encode("123456") , oauth2User.getAuthorities()), username, account.getId());

            PrintWriter writer = response.getWriter();
            if(jwt == null){
                writer.write(RestBean.forbidden("登录验证频繁，请稍后再试").asJsonString());
            }
            // 构建返回对象
            AuthorizeVO vo = account.asViewObject(AuthorizeVO.class, o -> o.setToken(jwt));
            vo.setExpire(utils.expireTime());
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().write(RestBean.success(vo).asJsonString());
        }

    }


    /**
     * 将多种类型的Handler整合到同一个方法中，包含：
     * - 登录成功
     * - 登录失败
     * - 未登录拦截/无权限拦截
     * @param request 请求
     * @param response 响应
     * @param exceptionOrAuthentication 异常或是验证实体
     * @throws IOException 可能的异常
     */
    private void handleProcess(HttpServletRequest request,
                               HttpServletResponse response,
                               Object exceptionOrAuthentication) throws IOException {
        response.setContentType("application/json;charset=utf-8");
        PrintWriter writer = response.getWriter();
        if(exceptionOrAuthentication instanceof AccessDeniedException exception) {
            writer.write(RestBean
                    .forbidden(exception.getMessage()).asJsonString());
        } else if(exceptionOrAuthentication instanceof Exception exception) {
            writer.write(RestBean
                    .unauthorized(exception.getMessage()).asJsonString());
        } else if(exceptionOrAuthentication instanceof Authentication authentication){
            User user = (User) authentication.getPrincipal();
            Account account = service.findAccountByNameOrEmail(user.getUsername());
            String jwt = utils.createJwt(user, account.getUsername(), account.getId());
            if(jwt == null) {
                writer.write(RestBean.forbidden("登录验证频繁，请稍后再试").asJsonString());
            } else {
                AuthorizeVO vo = account.asViewObject(AuthorizeVO.class, o -> o.setToken(jwt));
                vo.setExpire(utils.expireTime());
                writer.write(RestBean.success(vo).asJsonString());


            }
        }

    }

    /**
     * 退出登录处理，将对应的Jwt令牌列入黑名单不再使用
     * @param request 请求
     * @param response 响应
     * @param authentication 验证实体
     * @throws IOException 可能的异常
     */
    private void onLogoutSuccess(HttpServletRequest request,
                                 HttpServletResponse response,
                                 Authentication authentication) throws IOException {
        response.setContentType("application/json;charset=utf-8");
        PrintWriter writer = response.getWriter();
        String authorization = request.getHeader("Authorization");
        if(utils.invalidateJwt(authorization)) {
            writer.write(RestBean.success("退出登录成功").asJsonString());
            return;
        }
        writer.write(RestBean.failure(400, "退出登录失败").asJsonString());
    }
}
