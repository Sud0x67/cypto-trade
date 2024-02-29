/* (C)2024  Sud0x67@github.com */
package com.sud0x67.cryptotrade.core.entity;

import com.sud0x67.cryptotrade.core.enums.*;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class Order {
  /**
   * "symbol": "BTCUSDT", "orderId": 28, "orderListId": -1, //Unless OCO, value will be -1
   * "clientOrderId": "6gCrw2kRUAF9CvJDGP16IP", "transactTime": 1507725176595, "price":
   * "0.00000000", "origQty": "10.00000000", "executedQty": "10.00000000", "cummulativeQuoteQty":
   * "10.00000000", "status": "FILLED", "timeInForce": "GTC", "type": "MARKET", "side": "SELL",
   * "workingTime": 1507725176595, "selfTradePreventionMode": "NONE",
   */
  private OrderSymbol symbol;

  private Long orderId;
  Double price;
  Double originalQuantity;
  Double executedQuantity;
  OrderStatus status;
  TimeInForce timeInForce;
  OrderType type;
  OrderSide side;
}
