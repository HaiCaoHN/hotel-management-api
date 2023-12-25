package com.example.karmashopbe.sevices;

import com.example.karmashopbe.models.RefreshToken;
import com.example.karmashopbe.models.request.AuthenticationRequest;
import com.example.karmashopbe.models.request.RefreshTokenRequest;
import com.example.karmashopbe.models.response.AuthenticationResponse;
import com.example.karmashopbe.models.Role;
import com.example.karmashopbe.models.User;
import com.example.karmashopbe.models.response.RefreshTokenResponse;
import com.example.karmashopbe.repositories.RefreshTokenRepository;
import com.example.karmashopbe.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final RefreshTokenRepository tokenRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(AuthenticationRequest request) {
        //check if user existed
        if (userRepository.findByEmail(request.email()).isPresent()) {
            return AuthenticationResponse.builder().token("").message("user existed!").build();
        }
        //insert new user
        var user = User.builder()
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .role(Role.USER)
                .build();
        //generate token
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        //save refresh token
        tokenRepository.save(RefreshToken.builder()
                .userEmail(request.email())
                .refreshToken(refreshToken).build());
        //save user
        userRepository.save(user);

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .refreshToken(refreshToken)
                .message("register success!")
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.email(),
                request.password()
        ));
        var user = userRepository.findByEmail(request.email()).orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        String refreshToken;
        var refreshTokenOpt = tokenRepository.findRefreshTokenByUserEmail(user.getEmail());
        if (jwtService.isTokenExpired(refreshTokenOpt.getRefreshToken())) {
            refreshToken = jwtService.generateToken(user);
            refreshTokenOpt.setRefreshToken(refreshToken);
            tokenRepository.save(refreshTokenOpt);
        } else refreshToken = refreshTokenOpt.getRefreshToken();
        return AuthenticationResponse
                .builder()
                .token(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    public RefreshTokenResponse getRefreshToken(RefreshTokenRequest request) {
        String newToken;
        RefreshToken refreshToken;
        var refreshTokenOpt = tokenRepository.findRefreshTokenByRefreshToken(request.refreshToken());
        //check if refresh token not exist
        if (refreshTokenOpt.isEmpty()) return new RefreshTokenResponse("refresh token not existed!", null);

        refreshToken = refreshTokenOpt.get();
        var user = userRepository.findByEmail(jwtService.extractUsername(request.refreshToken())).orElseThrow();
        //check if refresh token expired
        if (jwtService.isTokenExpired(request.refreshToken())) {
            newToken = jwtService.generateRefreshToken(user);
            refreshToken.setRefreshToken(newToken);
            tokenRepository.save(refreshToken);
        } else newToken = jwtService.generateRefreshToken(user);

        return new RefreshTokenResponse("", newToken);
    }
}
