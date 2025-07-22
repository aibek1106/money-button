package com.example.moneybutton.rules;

import org.springframework.stereotype.Service;

@Service
public class TokenFilterService {
    static final double MAX_FDV = 100_000d;
    static final double MIN_LIQUIDITY = 3_000d;
    static final double MIN_VOL_CHANGE = 200d;
    static final int MIN_HOLDERS = 100;
    static final double MIN_LP_MONTHS = 6d;

    public boolean isPass(TokenInfo token) {
        if (token == null) {
            return false;
        }
        boolean fdvOk = token.getFdv() < MAX_FDV;
        boolean liquidityOk = token.getLiquidity() >= MIN_LIQUIDITY;
        boolean volumeOk = token.getVolume1hChange() >= MIN_VOL_CHANGE;
        boolean holdersOk = token.getHolders() >= MIN_HOLDERS;
        boolean authorityOk = token.isAuthorityBurned();
        boolean lpOk = token.getLpLockMonths() >= MIN_LP_MONTHS;
        return fdvOk && liquidityOk && volumeOk && holdersOk && authorityOk && lpOk;
    }
}
