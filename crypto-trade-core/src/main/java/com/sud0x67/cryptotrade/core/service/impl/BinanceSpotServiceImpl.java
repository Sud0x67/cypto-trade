/* (C)2024  Sud0x67@github.com */
package com.sud0x67.cryptotrade.core.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.binance.connector.client.SpotClient;
import com.binance.connector.client.impl.SpotClientImpl;
import com.sud0x67.cryptotrade.core.config.SpotClientConfigProperties;
import com.sud0x67.cryptotrade.core.entity.KLine;
import com.sud0x67.cryptotrade.core.entity.Order;
import com.sud0x67.cryptotrade.core.enums.*;
import com.sud0x67.cryptotrade.core.factory.OrderFactory;
import com.sud0x67.cryptotrade.core.service.SpotService;
import java.util.*;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BinanceSpotServiceImpl implements SpotService {

  private final SpotClientConfigProperties spotClientConfigProperties;
  private SpotClient spotClient;

  public BinanceSpotServiceImpl(SpotClientConfigProperties spotClientConfigProperties) {
    this.spotClientConfigProperties =
        Objects.requireNonNull(spotClientConfigProperties, "privateConfig");
    this.spotClient =
        new SpotClientImpl(spotClientConfigProperties.getAk(), spotClientConfigProperties.getSk());
  }

  @Override
  public Double queryAvgPrice(OrderSymbol symbol) {
    Map<String, Object> parameters = new LinkedHashMap<String, Object>();
    parameters.put("symbol", symbol.name());
    String result = spotClient.createMarket().averagePrice(parameters);
    JSONObject json = JSONObject.parseObject(result);
    return json.getDouble("price");
  }

  @Override
  public Double queryPrice(OrderSymbol symbol) {
    Map<String, Object> parameters = new LinkedHashMap<String, Object>();
    parameters.put("symbol", symbol.name());
    String result = spotClient.createMarket().tickerSymbol(parameters);
    JSONObject json = JSONObject.parseObject(result);
    return json.getDouble("price");
  }

  @Override
  public Order queryOrder(OrderSymbol symbol, Long orderId) {
    Map<String, Object> parameters = new LinkedHashMap<String, Object>();
    parameters.put("symbol", symbol.name());
    parameters.put("orderId", orderId);
    String result = spotClient.createTrade().getOrder(parameters);
    return OrderFactory.fromJsonString(result);
  }

  @Override
  public Order limitBuy(OrderSymbol symbol, Double price, Double quantity) {
    // use Math.floor 避免四舍五入
    Double requestPrice = Math.floor(price * 100) / 100;
    Double requestQuantity = Math.floor(quantity * 10_000) / 10_000;
    Map<String, Object> parameters = new LinkedHashMap<>();
    parameters.put("symbol", symbol.name());
    parameters.put("side", OrderSide.BUY.name());
    parameters.put("type", OrderType.LIMIT.name());
    parameters.put("timeInForce", TimeInForce.GTC.name());
    parameters.put("price", requestPrice); // 使用格式化后的 price
    parameters.put("quantity", requestQuantity);
    parameters.put("timestamp", System.currentTimeMillis());

    String result = spotClient.createTrade().newOrder(parameters);
    return OrderFactory.fromJsonString(result);
  }

  @Override
  public Order limitBuyByQuota(OrderSymbol symbol, Double price, Double quotaQuantity) {
    Double baseQuantity = quotaQuantity / price;
    return limitBuy(symbol, price, baseQuantity);
  }

  @Override
  public Order limitSell(OrderSymbol symbol, Double price, Double quantity) {
    // 格式化 price 为两位小数
    Double requestPrice = Double.parseDouble(String.format("%.2f", price));
    Double requestQuantity = Math.floor(quantity * 10_000) / 10_000;
    Map<String, Object> parameters = new LinkedHashMap<>();
    parameters.put("symbol", symbol.name());
    parameters.put("side", OrderSide.SELL.name());
    parameters.put("type", OrderType.LIMIT.name());
    parameters.put("timeInForce", TimeInForce.GTC.name());
    parameters.put("price", requestPrice); // 使用格式化后的 price
    parameters.put("quantity", requestQuantity);
    parameters.put("timestamp", System.currentTimeMillis());
    String res = spotClient.createTrade().newOrder(parameters);
    return OrderFactory.fromJsonString(res);
  }

  @Override
  public Order marketSell(OrderSymbol symbol, Double quantity) {
    Map<String, Object> parameters = new LinkedHashMap<>();
    parameters.put("symbol", symbol.name());
    parameters.put("side", OrderSide.SELL.name());
    parameters.put("type", OrderType.MARKET.name());
    parameters.put("quantity", quantity);
    parameters.put("timestamp", System.currentTimeMillis());
    String res = spotClient.createTrade().newOrder(parameters);
    return OrderFactory.fromJsonString(res);
  }

  @Override
  public Order marketBuy(OrderSymbol symbol, Double quantity) {
    Map<String, Object> parameters = new LinkedHashMap<>();
    parameters.put("symbol", symbol.name());
    parameters.put("side", OrderSide.BUY.name());
    parameters.put("type", OrderType.MARKET.name());
    parameters.put("quantity", quantity);
    parameters.put("timestamp", System.currentTimeMillis());
    String res = spotClient.createTrade().newOrder(parameters);
    return OrderFactory.fromJsonString(res);
  }

  @Override
  public Order marketBuyByQuota(OrderSymbol symbol, Double quantity) {
    Double price = queryPrice(symbol);
    Double baseQuantity = Math.floor(quantity / price * 10_000) / 10_000;
    return marketBuy(symbol, baseQuantity);
  }

  @Override
  public Boolean cancelOrder(OrderSymbol symbol, Long orderId) {
    Map<String, Object> parameters = new LinkedHashMap<>();
    parameters.put("symbol", symbol.name());
    parameters.put("orderId", orderId);
    parameters.put("timestamp", System.currentTimeMillis());
    String cancelRes = spotClient.createTrade().cancelOrder(parameters);
    JSONObject cancelJson = JSONObject.parseObject(cancelRes);
    return OrderStatus.CANCELED.name().equals(cancelJson.getString("status"));
  }

  @Override
  public Double getBalance(String currency) {
    Map<String, Object> params = new HashMap<>();
    params.put("asset", currency);
    String assets = spotClient.createWallet().getUserAsset(params);
    // 使用 FastJSON 解析 JSON 字符串为 JSONArray
    JSONArray jsonArray = JSONArray.parseArray(assets);
    for (int i = 0; i < jsonArray.size(); i++) {
      JSONObject jsonObject = jsonArray.getJSONObject(i);
      if (currency.equals(jsonObject.getString("asset"))) {
        String freeValue = jsonObject.getString("free");
        return Double.valueOf(freeValue);
      }
    }
    log.info("Can't get balance from server, and return zero. ");
    return 0d;
  }

  @Override
  public List<KLine> queryLastKLines(OrderSymbol symbol, int k, String interval) {
    List<KLine> res = new ArrayList<>();

    Map<String, Object> parameters = new LinkedHashMap<>();
    parameters.put("symbol", symbol.name());
    parameters.put("interval", interval);
    parameters.put("endTime", System.currentTimeMillis());
    parameters.put("limit", k);
    String klines = spotClient.createMarket().klines(parameters);
    JSONArray array = JSONArray.parseArray(klines);
    for (int i = 0; i < array.size(); i++) {
      JSONArray array1 = array.getJSONArray(i);
      Long timeStamp = array1.getLong(0);
      double openPrice = array1.getDouble(1);
      double highPrice = array1.getDouble(2);
      double lowPrice = array1.getDouble(3);
      double closePrice = array1.getDouble(4);
      KLine kLine = new KLine(timeStamp, openPrice, highPrice, lowPrice, closePrice);
      res.add(kLine);
    }
    return res;
  }
}
