package com.questapp.QuestApp.services;

import com.questapp.QuestApp.entities.RefreshToken;
import com.questapp.QuestApp.entities.User;
import com.questapp.QuestApp.repos.RefreshTokenRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@Service
public class RefreshTokenService {

    @Value("${refreshToken.expires.in}")
    private Long refreshTokenExpiresIn;

    private RefreshTokenRepository refreshTokenRepository;

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
    }

    public boolean isRefreshExpired(RefreshToken refreshToken) {
        return refreshToken.getExpireDate().before(new Date());
    }

    public String createRefreshToken(User user) {
        RefreshToken token = refreshTokenRepository.findByUserId(user.getId());
        if(token == null) {
            token =	new RefreshToken();
            token.setUser(user);
        }
        token.setToken(UUID.randomUUID().toString());
        token.setExpireDate(Date.from(Instant.now().plusSeconds(refreshTokenExpiresIn)));
        refreshTokenRepository.save(token);
        return token.getToken();
    }

    public RefreshToken getByUser(Long id) {
        return refreshTokenRepository.findByUserId(id);
    }
}
