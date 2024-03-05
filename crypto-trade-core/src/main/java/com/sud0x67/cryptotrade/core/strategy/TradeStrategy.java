/* (C)2024  Sud0x67@github.com */
package com.sud0x67.cryptotrade.core.strategy;

import com.sud0x67.cryptotrade.core.enums.OrderSymbol;
import java.util.Set;

public interface TradeStrategy {
  /**
   * Implements this method to tell which order symbol currency pair this strategy trade.
   *
   * @return the set of order symbols you want to trade in this strategy.
   */
  Set<OrderSymbol> getRequestOrderSymbols();

  default String getName() {
    return "DefaultStrategy";
  }

  /**
   * invoke the strategy, a strategy will be invoked by the strategy executor according to the cron
   * expression in this framework.
   */
  void invoke();
}
