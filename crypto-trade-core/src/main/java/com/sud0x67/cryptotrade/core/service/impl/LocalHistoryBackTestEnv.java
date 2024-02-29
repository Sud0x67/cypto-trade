/* (C)2024  Sud0x67@github.com */
package com.sud0x67.cryptotrade.core.service.impl;

import com.sud0x67.cryptotrade.core.entity.*;
import com.sud0x67.cryptotrade.core.entity.Account;
import com.sud0x67.cryptotrade.core.entity.KLine;
import com.sud0x67.cryptotrade.core.enums.*;
import com.sud0x67.cryptotrade.core.service.BackTestEnv;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class LocalHistoryBackTestEnv implements BackTestEnv {

  private HashMap<Long, Order> orders = new HashMap<>();

  private List<Order> limitedOrders = new ArrayList<>();
  private List<Double> profitList = new ArrayList<>();
  private long orderIdIndex = 0;
  private int current = 0;
  private List<KLine> history;
  private Account account;

  private double initBalance;
  private OrderSymbol symbol;

  public void init(List<KLine> history, OrderSymbol symbol, Double initBalance) {
    this.history = history;
    this.initBalance = initBalance;
    this.account = new Account();
    account.save(symbol.getQuoteCurrency(), initBalance);
    this.symbol = symbol;
  }

  public void reset() {
    this.orders.clear();
    this.limitedOrders.clear();
    this.account = new Account();
    account.save(symbol.getQuoteCurrency(), initBalance);
    this.profitList.clear();
    this.orderIdIndex = 0;
    this.current = 0;
  }

  public void update() {
    Iterator<Order> iterator = limitedOrders.iterator();
    while (iterator.hasNext()) {
      Order order = iterator.next();
      boolean needRemoved = updateLimitOrder(order);
      if (needRemoved) {
        iterator.remove();
      }
    }

    double profit =
        account.queryBalance(symbol.getQuoteCurrency())
            + account.queryBalance(symbol.getBaseCurrency()) * history.get(current).getClosePrice()
            - initBalance;
    current++;
    this.profitList.add(profit);
    if (current % 10 == 0) {
      System.out.println("current: " + current + "  profit = " + profit);
      System.out.println(this.account);
    }
  }

  public boolean stop() {
    return current >= history.size();
  }

  @Override
  public void printProfit() {
    System.out.println("================== Result Report ======================");
    System.out.println("=================== Max Profit =====================");
    System.out.println(profitList.stream().max(Double::compareTo).get() / initBalance * 100 + "%");
    System.out.println("=================== MIN Profit =====================");
    System.out.println(profitList.stream().min(Double::compareTo).get() / initBalance * 100 + "%");
    System.out.println("=================== BASE COIN inc =====================");
    System.out.println(
        (history.get(history.size() - 1).getClosePrice() - history.get(0).getOpenPrice())
                / history.get(0).getOpenPrice()
                * 100
            + "% ");
    System.out.println("=================== Account details =====================");
    System.out.println(account);
    System.out.println("=================== profit =====================");
    System.out.println(profitList.get(profitList.size() - 1) / initBalance * 100 + "%");
    System.out.println("========================================");
  }

  @Override
  public Double queryAvgPrice(OrderSymbol symbol) {
    double price = 0;
    int count = 0;
    for (int i = 0; i < 5; i++) {
      if (current - i >= 0) {
        price += history.get(current - i).getOpenPrice();
        count++;
      }
    }
    return price / count;
  }

  @Override
  public Double queryPrice(OrderSymbol symbol) {
    return history.get(current).getOpenPrice();
  }

  @Override
  public Order queryOrder(OrderSymbol symbol, Long orderId) {
    return orders.get(orderId);
  }

  @Override
  public Order limitBuy(OrderSymbol symbol, Double price, Double quantity) {
    return limitOrder(OrderSide.BUY, symbol, price, quantity);
  }

  @Override
  public Order limitBuyByQuota(OrderSymbol symbol, Double price, Double quotaQuantity) {
    Double quantity = quotaQuantity / price;
    return limitBuy(symbol, price, quantity);
  }

  @Override
  public Order limitSell(OrderSymbol symbol, Double price, Double quantity) {
    return limitOrder(OrderSide.SELL, symbol, price, quantity);
  }

  @Override
  public Order marketSell(OrderSymbol symbol, Double quantity) {
    return marketOrder(OrderSide.SELL, symbol, quantity);
  }

  @Override
  public Order marketBuyByQuota(OrderSymbol symbol, Double quantity) {
    return marketBuy(symbol, quantity / queryPrice(symbol));
  }

  @Override
  public Order marketBuy(OrderSymbol symbol, Double quantity) {
    return marketOrder(OrderSide.BUY, symbol, quantity);
  }

  @Override
  public Boolean cancelOrder(OrderSymbol symbol, Long orderId) {
    Order order = orders.get(orderId);
    order.setStatus(OrderStatus.CANCELED);
    limitedOrders.remove(order);
    return true;
  }

  @Override
  public Double getBalance(String currency) {
    return account.queryBalance(currency);
  }

  @Override
  public List<KLine> queryLastKLines(OrderSymbol symbol, int k, String interval) {
    int from = Math.max(current - 1 - k, 0);
    int to = Math.max(current - 1, 0);
    return new ArrayList<>(history.subList(from, to));
  }

  // 返回 是否已经成交
  public boolean updateLimitOrder(Order order) {
    if (order.getStatus() != OrderStatus.NEW) {
      return true;
    }
    KLine kLineData = history.get(current);
    if (order.getSide().equals(OrderSide.BUY) && kLineData.getLowPrice() <= order.getPrice()) {
      double tradePrice = Math.min(kLineData.getOpenPrice(), order.getPrice());
      account.withdraw(
          order.getSymbol().getQuoteCurrency(), tradePrice * order.getOriginalQuantity());
      account.save(order.getSymbol().getBaseCurrency(), order.getOriginalQuantity());
      order.setStatus(OrderStatus.FILLED);
      return true;
    }
    if (order.getSide().equals(OrderSide.SELL) && kLineData.getHighPrice() >= order.getPrice()) {
      double tradePrice = Math.max(kLineData.getOpenPrice(), order.getPrice());
      account.withdraw(order.getSymbol().getBaseCurrency(), order.getOriginalQuantity());
      account.save(order.getSymbol().getQuoteCurrency(), order.getOriginalQuantity() * tradePrice);
      order.setStatus(OrderStatus.FILLED);
      return true;
    }
    return false;
  }

  private Order marketOrder(OrderSide orderSide, OrderSymbol symbol, Double quantity) {
    Double price = queryPrice(symbol);
    Order order = new Order();
    order.setSide(orderSide);
    order.setOrderId(++orderIdIndex);
    order.setPrice(price);
    order.setOriginalQuantity(quantity);
    order.setExecutedQuantity(0d);
    order.setType(OrderType.MARKET);
    order.setStatus(OrderStatus.NEW);
    order.setSymbol(symbol);
    order.setTimeInForce(TimeInForce.GTC);
    orders.put(order.getOrderId(), order);
    // change OrderStatus to FILLED when updating;
    updateMarketOrder(order);
    return order;
  }

  public void updateMarketOrder(Order order) {
    switch (order.getSide()) {
      case SELL:
        account.save(
            order.getSymbol().getQuoteCurrency(), order.getPrice() * order.getOriginalQuantity());
        account.withdraw(order.getSymbol().getBaseCurrency(), order.getOriginalQuantity());
        break;
      case BUY:
        account.save(order.getSymbol().getBaseCurrency(), order.getOriginalQuantity());
        account.withdraw(
            order.getSymbol().getQuoteCurrency(), order.getPrice() * order.getOriginalQuantity());
        break;
      default:
    }
    order.setStatus(OrderStatus.FILLED);
  }

  private Order limitOrder(OrderSide orderSide, OrderSymbol symbol, Double price, Double quantity) {
    Order order = new Order();
    order.setSide(orderSide);
    order.setOrderId(++orderIdIndex);
    order.setPrice(price);
    order.setOriginalQuantity(quantity);
    order.setExecutedQuantity(0d);
    order.setType(OrderType.LIMIT);
    order.setStatus(OrderStatus.NEW);
    order.setSymbol(symbol);
    order.setTimeInForce(TimeInForce.GTC);
    orders.put(order.getOrderId(), order);
    limitedOrders.add(order);
    return order;
  }
}
