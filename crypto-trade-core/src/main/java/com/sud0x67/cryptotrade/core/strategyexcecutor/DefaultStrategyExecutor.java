/* (C)2024  Sud0x67@github.com */
package com.sud0x67.cryptotrade.core.strategyexcecutor;

import com.sud0x67.cryptotrade.core.enums.OrderSymbol;
import com.sud0x67.cryptotrade.core.strategy.CycleMultiplierStrategy;

public class DefaultStrategyExecutor {

  private CycleMultiplierStrategy cycleMultiplierStrategyV2;

  public void execute() {
    cycleMultiplierStrategyV2.execute(OrderSymbol.BTCFDUSD);
  }
}
