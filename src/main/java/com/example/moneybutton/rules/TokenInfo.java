package com.example.moneybutton.rules;

public class TokenInfo {
    private double fdv;
    private double liquidity;
    private double volume1hChange;
    private int holders;
    private boolean authorityBurned;
    private double lpLockMonths;
    private double volume24h;

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

    public double getVolume1hChange() {
        return volume1hChange;
    }

    public void setVolume1hChange(double volume1hChange) {
        this.volume1hChange = volume1hChange;
    }

    public int getHolders() {
        return holders;
    }

    public void setHolders(int holders) {
        this.holders = holders;
    }

    public boolean isAuthorityBurned() {
        return authorityBurned;
    }

    public void setAuthorityBurned(boolean authorityBurned) {
        this.authorityBurned = authorityBurned;
    }

    public double getLpLockMonths() {
        return lpLockMonths;
    }

    public void setLpLockMonths(double lpLockMonths) {
        this.lpLockMonths = lpLockMonths;
    }

    public double getVolume24h() {
        return volume24h;
    }

    public void setVolume24h(double volume24h) {
        this.volume24h = volume24h;
    }
}
