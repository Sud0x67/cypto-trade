/* (C)2024  Sud0x67@github.com */
package com.sud0x67.cryptotrade.core.entity;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

@Data
public class TradeContext {
  Order order;
  Account account;
  int pendingTimes;

  public static String serialize(TradeContext tradeContext) {
    return JSONObject.toJSONString(tradeContext);
  }

  public static TradeContext deserialize(String jsonString) {
    return JSONObject.parseObject(jsonString, TradeContext.class);
  }
}
