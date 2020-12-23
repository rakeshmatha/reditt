package controller;

import DTO.AuthenticationTokenResponse;
import DTO.LoginRequest;
import DTO.RegisterRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import service.AuthService;

@AllArgsConstructor
@RestController
@RequestMapping("/api/auth/")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<String> signUp(@RequestBody RegisterRequest registerRequest) {
        authService.signUp(registerRequest);
        return new ResponseEntity<>("User Registration Successfull", HttpStatus.OK);
    }

    @GetMapping("accountVerification/{token}")
    public ResponseEntity<String> verficationToken(@PathVariable String token) {
        authService.verifiationToken(token);
        return new ResponseEntity<>("Activation Successful", HttpStatus.OK);
    }

    @PostMapping("/login")
    public AuthenticationTokenResponse login(@RequestBody LoginRequest loginRequest) {
        return authService.login(loginRequest);
    }
}
