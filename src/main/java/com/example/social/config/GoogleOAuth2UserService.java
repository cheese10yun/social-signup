package com.example.social.config;

import com.example.social.user.UserRegistrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GoogleOAuth2UserService implements OAuth2UserService<OidcUserRequest, OidcUser> {

    private final UserRegistrationService userRegistrationService;

    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
        final OidcUserService oidcUserService = new OidcUserService();
        final OidcUser oidcUser = oidcUserService.loadUser(userRequest);
        final OAuth2AccessToken accessToken = userRequest.getAccessToken();

        final String name = oidcUser.getAttributes().get("name").toString();
        final String email = oidcUser.getAttributes().get("email").toString();
        userRegistrationService.requestRegistration(name, email);

        return new DefaultOidcUser(
            oidcUser.getAuthorities(),
            oidcUser.getIdToken(),
            oidcUser.getUserInfo()
        );
    }
}
