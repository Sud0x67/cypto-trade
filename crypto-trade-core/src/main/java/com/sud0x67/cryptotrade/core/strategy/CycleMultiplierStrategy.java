/* (C)2024  Sud0x67@github.com */
package com.sud0x67.cryptotrade.core.strategy;

import com.sud0x67.cryptotrade.core.entity.Order;
import com.sud0x67.cryptotrade.core.entity.TradeContext;
import com.sud0x67.cryptotrade.core.enums.OrderStatus;
import com.sud0x67.cryptotrade.core.enums.OrderSymbol;
import com.sud0x67.cryptotrade.core.service.SpotService;
import com.sud0x67.cryptotrade.core.utils.Constant;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CycleMultiplierStrategy extends BaseStrategy implements Constant {

  private final SpotService spotService;

  public CycleMultiplierStrategy(SpotService spotService) {
    this.spotService = spotService;
  }

  /**
   * 启动交易策略
   *
   * @param symbol 交易对
   */
  public void execute(OrderSymbol symbol) {
    // 查询是否有成交订单
    TradeContext tradeContext = loadContext(symbol);
    if (tradeContext == null) {
      tradeContext = new TradeContext();
    }
    // 查询订单状态
    if (tradeContext.getOrder() != null) {
      Order oldOrder = tradeContext.getOrder();
      Long orderId = oldOrder.getOrderId();
      Order orderDetail = spotService.queryOrder(symbol, orderId);
      if (orderDetail.getStatus().equals(OrderStatus.NEW)) {
        // 如果未成交，则撤销订单
        Boolean res = spotService.cancelOrder(symbol, orderId);
        log.info("订单Id: {}， 是否取消成功：{}", orderId, res);
        clearContext(symbol.name());
      } else if (orderDetail.getStatus().equals(OrderStatus.FILLED)) {
        // 如果完全成交，则卖出
        Order sellRes =
            spotService.marketSell(symbol, spotService.getBalance(symbol.getBaseCurrency()));
        log.info("sell all coins, response: {} ", sellRes);
        clearContext(symbol.name());
      } else {
        // TODO 如果部分成交，先不处理
        return;
      }
    }

    // 查询价格
    Double price = spotService.queryPrice(symbol);
    Double targetPrice = price * 0.997;
    log.info("price = " + price + "  targetPrice = " + targetPrice);
    //        try {
    //            Thread.sleep(3_000);
    //        } catch (InterruptedException e) {
    //            throw new RuntimeException(e);
    //        }
    Order newOrder =
        spotService.limitBuyByQuota(
            symbol, targetPrice, spotService.getBalance(symbol.getQuoteCurrency()));
    tradeContext.setOrder(newOrder);
    saveContext(tradeContext, symbol);
  }

  /**
   * 终止交易策略
   *
   * @param name 策略名称
   */
  public void stop(String name) {}

  public String getName() {
    return "CycleMultiplierStrategyV2";
  }
}
