package com.example.moneybutton.rules;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TokenFilterServiceTest {
    private final TokenFilterService service = new TokenFilterService();

    @Test
    void positiveFixturePasses() {
        TokenInfo token = createToken(TokenFilterService.MAX_FDV - 1, TokenFilterService.MIN_LIQUIDITY,
                TokenFilterService.MIN_VOL_CHANGE, TokenFilterService.MIN_HOLDERS,
                true, TokenFilterService.MIN_LP_MONTHS);
        assertTrue(service.isPass(token));
    }

    @Test
    void failsFdvRule() {
        TokenInfo token = createToken(TokenFilterService.MAX_FDV + 1, TokenFilterService.MIN_LIQUIDITY,
                TokenFilterService.MIN_VOL_CHANGE, TokenFilterService.MIN_HOLDERS,
                true, TokenFilterService.MIN_LP_MONTHS);
        assertFalse(service.isPass(token));
    }

    @Test
    void failsLiquidityRule() {
        TokenInfo token = createToken(TokenFilterService.MAX_FDV - 1, TokenFilterService.MIN_LIQUIDITY - 1,
                TokenFilterService.MIN_VOL_CHANGE, TokenFilterService.MIN_HOLDERS,
                true, TokenFilterService.MIN_LP_MONTHS);
        assertFalse(service.isPass(token));
    }

    @Test
    void failsVolumeRule() {
        TokenInfo token = createToken(TokenFilterService.MAX_FDV - 1, TokenFilterService.MIN_LIQUIDITY,
                TokenFilterService.MIN_VOL_CHANGE - 1, TokenFilterService.MIN_HOLDERS,
                true, TokenFilterService.MIN_LP_MONTHS);
        assertFalse(service.isPass(token));
    }

    @Test
    void failsAuthorityRule() {
        TokenInfo token = createToken(TokenFilterService.MAX_FDV - 1, TokenFilterService.MIN_LIQUIDITY,
                TokenFilterService.MIN_VOL_CHANGE, TokenFilterService.MIN_HOLDERS,
                false, TokenFilterService.MIN_LP_MONTHS);
        assertFalse(service.isPass(token));
    }

    @Test
    void failsLpRule() {
        TokenInfo token = createToken(TokenFilterService.MAX_FDV - 1, TokenFilterService.MIN_LIQUIDITY,
                TokenFilterService.MIN_VOL_CHANGE, TokenFilterService.MIN_HOLDERS,
                true, TokenFilterService.MIN_LP_MONTHS - 0.1);
        assertFalse(service.isPass(token));
    }

    private TokenInfo createToken(double fdv, double liquidity, double volumeChange, int holders,
                                  boolean authority, double lpMonths) {
        TokenInfo t = new TokenInfo();
        t.setFdv(fdv);
        t.setLiquidity(liquidity);
        t.setVolume1hChange(volumeChange);
        t.setHolders(holders);
        t.setAuthorityBurned(authority);
        t.setLpLockMonths(lpMonths);
        return t;
    }
}
