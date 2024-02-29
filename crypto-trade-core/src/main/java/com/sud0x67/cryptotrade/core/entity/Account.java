/* (C)2024  Sud0x67@github.com */
package com.sud0x67.cryptotrade.core.entity;

import java.util.HashMap;
import lombok.Data;
import lombok.ToString;

@ToString
@Data
public class Account {
  HashMap<String, Double> assets = new HashMap<>();

  public Double save(String currency, Double amount) {
    Double all = assets.getOrDefault(currency, 0d) + amount;
    assets.put(currency, all);
    return all;
  }

  public Double withdraw(String currency, Double amount) {
    Double remained = assets.getOrDefault(currency, 0d) - amount;
    // 避免double 计算的误差
    if (remained < -1E-9) {
      throw new IllegalArgumentException("not enough amount of " + currency);
    }
    assets.put(currency, remained);
    return remained;
  }

  public Double queryBalance(String currency) {
    return assets.getOrDefault(currency, 0d);
  }
}
