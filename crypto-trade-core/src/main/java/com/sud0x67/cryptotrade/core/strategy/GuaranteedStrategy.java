/* (C)2024  Sud0x67@github.com */
package com.sud0x67.cryptotrade.core.strategy;

import com.sud0x67.cryptotrade.core.entity.Order;
import com.sud0x67.cryptotrade.core.entity.TradeContext;
import com.sud0x67.cryptotrade.core.enums.OrderSide;
import com.sud0x67.cryptotrade.core.enums.OrderStatus;
import com.sud0x67.cryptotrade.core.enums.OrderSymbol;
import com.sud0x67.cryptotrade.core.service.SpotService;
import com.sud0x67.cryptotrade.core.utils.Constant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class GuaranteedStrategy extends BaseStrategy implements Constant {

  private final SpotService spotService;

  public GuaranteedStrategy(SpotService spotService) {
    this.spotService = spotService;
  }

  @Override
  public String getName() {
    return "GuaranteedStrategyV2";
  }

  /**
   * 启动交易策略
   *
   * @param symbol 交易对
   */
  public void execute(OrderSymbol symbol) {
    TradeContext tradeContext = loadContext(symbol);
    if (tradeContext == null) {
      tradeContext = initContext(symbol);
    }
    log.info("TradeContext: {}", TradeContext.serialize(tradeContext));
    boolean buyFlag = true; // 这轮调度是否需要新购
    if (tradeContext.getOrder() != null) {
      Long orderId = tradeContext.getOrder().getOrderId();
      // 查询订单状态
      Order newOrder = spotService.queryOrder(symbol, orderId);
      if (OrderSide.BUY.equals(newOrder.getSide())) {
        if (OrderStatus.NEW.equals(newOrder.getStatus())) {
          // 如果未成交，则撤销订单
          Boolean res = spotService.cancelOrder(symbol, orderId);
          log.info("订单Id: {}， 是否取消成功：{}", orderId, res);
          if (res) {
            clearContext(symbol.name());
          }
        } else if (OrderStatus.FILLED.equals(newOrder.getStatus())) {
          // 如果完全成交，查询价格
          Double currentPrice = spotService.queryPrice(symbol);
          if (currentPrice >= tradeContext.getOrder().getPrice()) {
            sellOut(tradeContext, symbol);
          } else {
            pendingSell(tradeContext, symbol);
            buyFlag = false;
          }
        } else {
          return;
        }
      } else if (OrderSide.SELL.equals(newOrder.getSide())) {
        if (OrderStatus.NEW.equals(newOrder.getStatus())) {
          if (tradeContext.getPendingTimes() >= 2) {
            spotService.cancelOrder(symbol, tradeContext.getOrder().getOrderId());
            sellOut(tradeContext, symbol);
          } else {
            tradeContext.setPendingTimes(tradeContext.getPendingTimes() + 1);
            saveContext(tradeContext, symbol);
            buyFlag = false;
          }
        } else if (OrderStatus.FILLED.equals(newOrder.getStatus())) {
          // 卖单成交
          clearContext(symbol.name());
        } else {
          return;
        }
      }
    }
    // todo sleep only when living trading.
    try {
      Thread.sleep(1_000);
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
    if (buyFlag) { // buyIn; cashOut;
      pendingBuy(tradeContext, symbol);
    }
  }

  private void sellOut(TradeContext tradeContext, OrderSymbol symbol) {
    Order sellRes =
        spotService.marketSell(symbol, spotService.getBalance(symbol.getBaseCurrency()));
    log.info("sell all coins, response: {} ", sellRes);
    clearContext(symbol.name());
  }

  private void pendingSell(TradeContext tradeContext, OrderSymbol symbol) {
    // 挂单 价格为原来价格 + 0.001
    double price = tradeContext.getOrder().getPrice() * 1.001;
    Order limitSellOrder =
        spotService.limitSell(symbol, price, spotService.getBalance(symbol.getBaseCurrency()));
    tradeContext.setPendingTimes(0);
    tradeContext.setOrder(limitSellOrder);
    saveContext(tradeContext, symbol);
  }

  private void pendingBuy(TradeContext tradeContext, OrderSymbol symbol) {
    Double price = spotService.queryPrice(symbol);
    Double targetPrice = price * 0.997;
    log.info("price = " + price + "  targetPrice = " + targetPrice);
    // 挂单并写入数据
    double quoteAmount = spotService.getBalance(symbol.getQuoteCurrency());
    Order newOrder = spotService.limitBuyByQuota(symbol, targetPrice, quoteAmount);
    tradeContext.setOrder(newOrder);
    tradeContext.setAccount(tradeContext.getAccount());
    saveContext(tradeContext, symbol);
  }

  private TradeContext initContext(OrderSymbol symbol) {
    TradeContext context = new TradeContext();
    context.setPendingTimes(0);
    return context;
  }
}
