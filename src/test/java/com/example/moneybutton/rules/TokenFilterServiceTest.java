package com.example.moneybutton.rules;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TokenFilterServiceTest {
    private final TokenFilterService service = new TokenFilterService();

    @Test
    void positiveFixturePasses() {
        TokenInfo token = createToken(TokenFilterService.MAX_FDV, TokenFilterService.MIN_LIQUIDITY,
                TokenFilterService.MIN_VOLUME, true, true);
        assertTrue(service.isPass(token));
    }

    @Test
    void failsFdvRule() {
        TokenInfo token = createToken(TokenFilterService.MAX_FDV + 1, TokenFilterService.MIN_LIQUIDITY,
                TokenFilterService.MIN_VOLUME, true, true);
        assertFalse(service.isPass(token));
    }

    @Test
    void failsLiquidityRule() {
        TokenInfo token = createToken(TokenFilterService.MAX_FDV, TokenFilterService.MIN_LIQUIDITY - 1,
                TokenFilterService.MIN_VOLUME, true, true);
        assertFalse(service.isPass(token));
    }

    @Test
    void failsVolumeRule() {
        TokenInfo token = createToken(TokenFilterService.MAX_FDV, TokenFilterService.MIN_LIQUIDITY,
                TokenFilterService.MIN_VOLUME - 1, true, true);
        assertFalse(service.isPass(token));
    }

    @Test
    void failsAuthorityRule() {
        TokenInfo token = createToken(TokenFilterService.MAX_FDV, TokenFilterService.MIN_LIQUIDITY,
                TokenFilterService.MIN_VOLUME, false, true);
        assertFalse(service.isPass(token));
    }

    @Test
    void failsLpRule() {
        TokenInfo token = createToken(TokenFilterService.MAX_FDV, TokenFilterService.MIN_LIQUIDITY,
                TokenFilterService.MIN_VOLUME, true, false);
        assertFalse(service.isPass(token));
    }

    private TokenInfo createToken(double fdv, double liquidity, double volume, boolean authority, boolean lp) {
        TokenInfo t = new TokenInfo();
        t.setFdv(fdv);
        t.setLiquidity(liquidity);
        t.setVolume24h(volume);
        t.setAuthorityBurned(authority);
        t.setLpLocked(lp);
        return t;
    }
}
