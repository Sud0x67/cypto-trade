/* (C)2024  Sud0x67@github.com */
package com.sud0x67.cryptotrade.core.factory;

import com.alibaba.fastjson.JSONObject;
import com.sud0x67.cryptotrade.core.entity.Order;
import com.sud0x67.cryptotrade.core.enums.*;

public class OrderFactory {
  public static Order fromJsonString(String result) {
    JSONObject orderDetailJson = JSONObject.parseObject(result);
    Order order = new Order();
    order.setSymbol(OrderSymbol.valueOf(orderDetailJson.getString("symbol")));
    order.setOrderId(orderDetailJson.getLong("orderId"));
    order.setPrice(orderDetailJson.getDouble("price"));
    order.setOriginalQuantity(orderDetailJson.getDouble("origQty"));
    order.setExecutedQuantity(orderDetailJson.getDouble("executedQty"));
    order.setStatus(OrderStatus.valueOf(orderDetailJson.getString("status")));
    order.setTimeInForce(TimeInForce.valueOf(orderDetailJson.getString("timeInForce"))); //
    order.setType(OrderType.valueOf(orderDetailJson.getString("type")));
    order.setSide(OrderSide.valueOf(orderDetailJson.getString("side")));
    return order;
  }
}
