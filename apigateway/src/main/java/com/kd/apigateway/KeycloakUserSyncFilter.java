package com.kd.apigateway;

import com.kd.apigateway.user.RegisterRequest;
import com.kd.apigateway.user.UserService;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Component
@Slf4j
@RequiredArgsConstructor
public class KeycloakUserSyncFilter implements WebFilter {

    private final UserService userService;

    @Override
    public Mono<Void> filter (ServerWebExchange exchange, WebFilterChain chain) {
        String userId = exchange.getRequest().getHeaders().getFirst("X-User-ID");
        String token = exchange.getRequest().getHeaders().getFirst("Authorization");

        RegisterRequest registerRequest;
        if (token != null && token.startsWith("Bearer ")) {
            registerRequest = getUserDetails(token);
        } else {
            registerRequest = null;
        }

        if (userId == null && registerRequest != null) {
            userId = registerRequest.getKeyCloakId();
        }

        if (userId != null && token != null) {
            String finalUserId = userId;
            String finalUserId1 = userId;
            return userService.validateUser(userId)
                    .onErrorResume(ex -> {
                        // If validation failed for any reason, treat as "not found" so registration can be attempted.
                        log.warn("User validation call failed for {}: {} — attempting registration", finalUserId1, ex.getMessage());
                        return Mono.just(Boolean.FALSE);
                    })
                    .flatMap(exist -> {
                        if (!exist) {
                            // Register User (only if we have details)
                            if (registerRequest != null) {
                                return userService.registerUser(registerRequest).then(Mono.just(Boolean.TRUE));
                            }
                            return Mono.just(Boolean.FALSE);
                        } else {
                            log.info("User {} already exist", finalUserId);
                            return Mono.just(Boolean.TRUE);
                        }
                    })
                    .then(Mono.defer(() -> {
                        ServerHttpRequest mutatedRequest = exchange.getRequest().mutate()
                                .header("X-User-ID", finalUserId).build();
                        return chain.filter(exchange.mutate().request(mutatedRequest).build());
                    }));
        }
        return chain.filter(exchange);
    }

    private RegisterRequest getUserDetails(String token) {
        if (token == null) return null;
        try{
            String tokenWithoutBearer = token.replace("Bearer ", "").trim();
            SignedJWT signedJWT = SignedJWT.parse(tokenWithoutBearer);
            JWTClaimsSet claims = signedJWT.getJWTClaimsSet();

            RegisterRequest registerRequest = new RegisterRequest();
            registerRequest.setEmail(claims.getStringClaim("email"));
            registerRequest.setKeyCloakId(claims.getStringClaim("sub"));
            registerRequest.setPassword("password123");
            registerRequest.setFirstName(claims.getStringClaim("given_name"));
            registerRequest.setLastName(claims.getStringClaim("family_name"));

            return registerRequest;

        }catch (Exception ex){
            log.warn("Failed to parse JWT for user sync: {}", ex.getMessage());
            return null;
        }
    }
}
