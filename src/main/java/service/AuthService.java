package service;

import DTO.AuthenticationTokenResponse;
import DTO.LoginRequest;
import DTO.RefreshTokenRequest;
import DTO.RegisterRequest;
import exception.SpringRedittException;
import lombok.AllArgsConstructor;
import model.NotificationEmail;
import model.User;
import model.VerificationToken;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import repository.UserRepository;
import repository.VerificationTokenRepository;
import security.JwtProvider;

import javax.transaction.Transactional;
import javax.validation.constraints.NotBlank;
import java.time.Clock;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final VerificationTokenRepository verificationTokenRepository;
    private final MailService mailService;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final RefreshTokenService refreshTokenService;

    @Transactional
    //signup an user with given data and save them to repo which is db
    public void signUp(RegisterRequest registerRequest) {
        model.User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setPassword(passwordEncoder.encode((registerRequest.getPassword())));
        user.setEmail(registerRequest.getEmail());
        user.setCreated(Instant.now());
        user.setEnabled(false);
        userRepository.save(user);
        String token = generateActivationToken(user);
        mailService.sendEmail(new NotificationEmail(
                "Please Activate your account",
                user.getEmail(),
                "Thankyou for signing up to Spring Reditt \n" + "Please click on the below link to activate your account : \n" + "http://localhost:8080/api/auth/accountVerification : " + token));
    }

    //Getting random token and storing it in databse for future reference asn say for 1 day and return the token from database when user requested.
    private String generateActivationToken(User user) {
        String token = UUID.randomUUID().toString(); //128bit value
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUser(user);
        verificationToken.setExpiryDate(Instant.now(Clock.systemUTC()));
        verificationTokenRepository.save(verificationToken);
        return token;
    }

    public void verifiationToken(String token) {
        Optional<VerificationToken> verificationToken = verificationTokenRepository.findByToken(token);
        verificationToken.orElseThrow(() -> new SpringRedittException("Invalid Token"));
        fetchUserAndEnable(verificationToken.get());
    }

    @Transactional
    void fetchUserAndEnable(VerificationToken verificationToken) {
        @NotBlank(message = "username is required")
        String userName = verificationToken.getUser().getUsername();
        User user = userRepository.findByUsername(userName).orElseThrow(() -> new SpringRedittException("User Not Found"));
        user.setEnabled(true);
        userRepository.save(user);
    }


    public AuthenticationTokenResponse login(LoginRequest loginRequest) {
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginRequest.getUsername(),
                loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authenticate);
        String token = jwtProvider.getJwtToken(authenticate);
        return AuthenticationTokenResponse.builder()
                .authenticationToken(token)
                .refreshToken(refreshTokenService.generateRefreshToken().getToken())
                .expiresAt(Instant.now().plusMillis(jwtProvider.getJwtExpirationInMillis()))
                .username(loginRequest.getUsername())
                .build();
    }

    public AuthenticationTokenResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {
        refreshTokenService.validateRefreshToken(refreshTokenRequest.getRefreshToken());
        String token = jwtProvider.generateTokenWithUserName(refreshTokenRequest.getUsername());
        return AuthenticationTokenResponse.builder()
                .authenticationToken(token)
                .refreshToken(refreshTokenRequest.getRefreshToken())
                .expiresAt(Instant.now().plusMillis(jwtProvider.getJwtExpirationInMillis()))
                .username(refreshTokenRequest.getUsername())
                .build();
    }

    public boolean isLoggedIn() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return !(authentication instanceof AnonymousAuthenticationToken) && authentication.isAuthenticated();
    }
}
