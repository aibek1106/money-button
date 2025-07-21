package com.example.moneybutton.rules;

import org.springframework.stereotype.Service;

@Service
public class TokenFilterService {
    static final double MAX_FDV = 10_000_000d;
    static final double MIN_LIQUIDITY = 100_000d;
    static final double MIN_VOLUME = 1_000_000d;

    public boolean isPass(TokenInfo token) {
        if (token == null) {
            return false;
        }
        boolean fdvOk = token.getFdv() <= MAX_FDV;
        boolean liquidityOk = token.getLiquidity() >= MIN_LIQUIDITY;
        boolean volumeOk = token.getVolume24h() >= MIN_VOLUME;
        boolean authorityOk = token.isAuthorityBurned();
        boolean lpOk = token.isLpLocked();
        return fdvOk && liquidityOk && volumeOk && authorityOk && lpOk;
    }
}
