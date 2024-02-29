/* (C)2024  Sud0x67@github.com */
package com.sud0x67.cryptotrade.core.strategy;

import com.sud0x67.cryptotrade.core.entity.KLine;
import com.sud0x67.cryptotrade.core.enums.OrderSymbol;
import com.sud0x67.cryptotrade.core.service.SpotService;
import java.util.List;
import lombok.Data;

public class AtrGridStrategy {
  private final SpotService spotService;
  private OrderSymbol symbol = OrderSymbol.BTCFDUSD; // default value
  TradeContext tradeContext;

  public void init() {
    this.tradeContext = new TradeContext();
    Double marketPrice = spotService.queryPrice(symbol);
    updateTradeContext(marketPrice, 0);
  }

  private double calAtr() {
    int k = 20;
    List<KLine> kLines = spotService.queryLastKLines(symbol, k, "1h");
    double totalPercent = 0;
    for (KLine line : kLines) {
      totalPercent += (line.getHighPrice() - line.getLowPrice()) / line.getClosePrice();
    }
    if (kLines.isEmpty()) {
      return 0.001;
    }
    return totalPercent / kLines.size();
  }

  public void execute() {
    Double price = spotService.queryPrice(symbol);
    if (price < tradeContext.gridBuyPrice && tradeContext.getStep() <= 0) {
      spotService.marketBuyByQuota(symbol, spotService.getBalance(symbol.getQuoteCurrency()));
      updateTradeContext(price, this.tradeContext.getStep() + 1);
    } else if (price > tradeContext.gridSellPrice && tradeContext.getStep() > 0) {
      spotService.marketSell(symbol, spotService.getBalance(symbol.getBaseCurrency()));
      updateTradeContext(price, this.tradeContext.getStep() - 1);
    } else {
      System.out.println("Skip");
    }
  }

  public void updateTradeContext(Double price, int step) {
    double coe = 0.00001;
    double atrInPercent = calAtr();
    tradeContext.setGridBuyPrice(price * (1 - atrInPercent * coe));
    tradeContext.setGridSellPrice(price * (1 + atrInPercent * coe));
    tradeContext.setStep(step);
  }

  public AtrGridStrategy(SpotService spotService) {
    this.spotService = spotService;
  }

  public String getName() {
    return "AtrGridStrategyV2";
  }

  @Data
  public static class TradeContext {
    Double gridBuyPrice;
    Double gridSellPrice;
    int step;
  }
}
