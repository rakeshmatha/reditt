package DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthenticationTokenResponse {
    private String authenticationToken;
    private String username;
    private Instant expiresAt;
    private String refreshToken;
}
