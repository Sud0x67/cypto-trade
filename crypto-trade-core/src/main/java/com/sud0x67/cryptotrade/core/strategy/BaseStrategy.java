/* (C)2024  Sud0x67@github.com */
package com.sud0x67.cryptotrade.core.strategy;

import com.sud0x67.cryptotrade.core.entity.TradeContext;
import com.sud0x67.cryptotrade.core.enums.OrderSymbol;
import java.util.HashMap;

public abstract class BaseStrategy {

  HashMap<OrderSymbol, TradeContext> contextHashMap = new HashMap<>();

  public abstract String getName();

  public TradeContext loadContext(OrderSymbol symbol) {
    return contextHashMap.getOrDefault(symbol, null);
  }

  public void saveContext(TradeContext context, OrderSymbol symbol) {
    contextHashMap.put(symbol, context);
  }

  public void clearContext(String symbol) {
    contextHashMap.remove(symbol);
  }
}
