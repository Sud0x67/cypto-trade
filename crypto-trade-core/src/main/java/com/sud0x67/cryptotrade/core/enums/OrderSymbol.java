/* (C)2024  Sud0x67@github.com */
package com.sud0x67.cryptotrade.core.enums;

import com.sud0x67.cryptotrade.core.utils.CurrencyUtil;
import lombok.Getter;

@Getter
public enum OrderSymbol {
  BTCFDUSD(CurrencyUtil.BTC, CurrencyUtil.FDUSD);
  private String baseCurrency;
  private String quoteCurrency;

  OrderSymbol(String baseCurrency, String quoteCurrency) {
    this.baseCurrency = baseCurrency;
    this.quoteCurrency = quoteCurrency;
  }
}
