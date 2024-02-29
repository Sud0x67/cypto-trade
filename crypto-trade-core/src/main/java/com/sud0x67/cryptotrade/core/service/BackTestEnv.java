/* (C)2024  Sud0x67@github.com */
package com.sud0x67.cryptotrade.core.service;

import com.sud0x67.cryptotrade.core.entity.KLine;
import com.sud0x67.cryptotrade.core.enums.OrderSymbol;
import java.util.List;

public interface BackTestEnv extends SpotService {
  boolean stop();

  void init(List<KLine> history, OrderSymbol symbol, Double initBalance);

  void update();

  void printProfit();

  void reset();
}
