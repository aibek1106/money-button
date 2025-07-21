package com.example.moneybutton.rules;

public class TokenInfo {
    private double fdv;
    private double liquidity;
    private double volume24h;
    private boolean authorityBurned;
    private boolean lpLocked;

    public double getFdv() {
        return fdv;
    }

    public void setFdv(double fdv) {
        this.fdv = fdv;
    }

    public double getLiquidity() {
        return liquidity;
    }

    public void setLiquidity(double liquidity) {
        this.liquidity = liquidity;
    }

    public double getVolume24h() {
        return volume24h;
    }

    public void setVolume24h(double volume24h) {
        this.volume24h = volume24h;
    }

    public boolean isAuthorityBurned() {
        return authorityBurned;
    }

    public void setAuthorityBurned(boolean authorityBurned) {
        this.authorityBurned = authorityBurned;
    }

    public boolean isLpLocked() {
        return lpLocked;
    }

    public void setLpLocked(boolean lpLocked) {
        this.lpLocked = lpLocked;
    }
}
