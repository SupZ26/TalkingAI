package com.example.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;

@Configuration
public class GiteeOAuth2LoginConfig {

    @Value("${spring.security.oauth2.client.registration.gitee.client-id}")
    String gitee_client_id;

    @Value("${spring.security.oauth2.client.registration.gitee.client-secret}")
    String gitee_client_secret;

    @Value("${spring.security.oauth2.client.registration.gitee.redirect-uri}")
    String redirectUri;

    @Value("${spring.security.oauth2.client.provider.gitee.authorization-uri}")
    String authorization_uri;

    @Value("${spring.security.oauth2.client.provider.gitee.token-uri}")
    String token_uri;

    @Value("${spring.security.oauth2.client.provider.gitee.user-info-uri}")
    String user_info_uri;
    @Bean
    public ClientRegistrationRepository clientRegistrationRepository() {
        return new InMemoryClientRegistrationRepository(this.giteeClientRegistration());
    }

    private ClientRegistration giteeClientRegistration() {
        return ClientRegistration.withRegistrationId("gitee")
                .clientId(gitee_client_id)
                .clientSecret(gitee_client_secret)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .redirectUri(redirectUri)
                .authorizationUri(authorization_uri)
                .tokenUri(token_uri)
                .userInfoUri(user_info_uri)
                .userNameAttributeName("name")
                .clientName("Gitee")
                .build();
    }
}
