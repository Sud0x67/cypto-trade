/* (C)2024  Sud0x67@github.com */
package com.sud0x67.cryptotrade.core.service;

import com.sud0x67.cryptotrade.core.entity.KLine;
import com.sud0x67.cryptotrade.core.entity.Order;
import com.sud0x67.cryptotrade.core.enums.OrderSymbol;
import java.util.List;

public interface SpotService {
  /**
   * 查询交易对5min价格
   *
   * @param symbol
   * @return
   */
  Double queryAvgPrice(OrderSymbol symbol);

  /**
   * 查询最近价格
   *
   * @param symbol
   * @return
   */
  Double queryPrice(OrderSymbol symbol);

  /**
   * 查询订单详情
   *
   * @param symbol
   * @param orderId
   * @return
   */
  Order queryOrder(OrderSymbol symbol, Long orderId);

  /**
   * 发起限价单
   *
   * @param symbol
   * @param price
   * @param quantity
   * @return
   */
  Order limitBuy(OrderSymbol symbol, Double price, Double quantity);

  Order limitBuyByQuota(OrderSymbol symbol, Double price, Double quotaQuantity);

  /**
   * 卖出限价单
   *
   * @param symbol
   * @param price
   * @param quantity
   * @return
   */
  Order limitSell(OrderSymbol symbol, Double price, Double quantity);

  /**
   * 发起市价卖单
   *
   * @param symbol
   * @param quantity
   * @return
   */
  Order marketSell(OrderSymbol symbol, Double quantity);

  /**
   * 发起市价卖单
   *
   * @param symbol
   * @param quantity
   * @return
   */
  Order marketBuy(OrderSymbol symbol, Double quantity);

  /**
   * 发起市价卖单
   *
   * @param symbol
   * @param quantity in quota coin
   * @return
   */
  Order marketBuyByQuota(OrderSymbol symbol, Double quantity);

  /**
   * 撤销挂单
   *
   * @param symbol
   * @param orderId
   * @return
   */
  Boolean cancelOrder(OrderSymbol symbol, Long orderId);

  Double getBalance(String currency);

  /** 查询最近n根k线 1s 1m 3m 5m 15m 30m 1h 2h 4h 6h 8h 12h 1d 3d 1w 1M */
  List<KLine> queryLastKLines(OrderSymbol symbol, int k, String interval);
}
