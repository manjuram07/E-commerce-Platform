import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;

import java.util.UUID;

@Bean
public RegisteredClient registeredClient() {
    return RegisteredClient.withId(UUID.randomUUID().toString())
            .clientId("ekart-client")
            .clientSecret("{noop}secret")
            .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
            .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
            .scope("payment.read")
            .scope("payment.write")
            .build();
}

public void main() {
}