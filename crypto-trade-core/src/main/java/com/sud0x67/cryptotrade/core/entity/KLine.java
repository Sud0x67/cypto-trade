/* (C)2024  Sud0x67@github.com */
package com.sud0x67.cryptotrade.core.entity;

import java.text.SimpleDateFormat;
import java.util.Date;
import lombok.Data;

@Data
public class KLine {

  private Date date;
  private double openPrice;
  private double highPrice;
  private double lowPrice;
  private double closePrice;

  public KLine(
      long timestamp, double openPrice, double highPrice, double lowPrice, double closePrice) {
    this.date = new Date(timestamp);
    this.openPrice = openPrice;
    this.highPrice = highPrice;
    this.lowPrice = lowPrice;
    this.closePrice = closePrice;
  }

  // Getters and setters (if needed)

  @Override
  public String toString() {
    return "PriceData{"
        + "date="
        + date
        + ", openPrice="
        + openPrice
        + ", highPrice="
        + highPrice
        + ", lowPrice="
        + lowPrice
        + ", closePrice="
        + closePrice
        + " }";
  }

  /**
   * 时间表达式 2022-08-11 22:22:22
   *
   * @return
   */
  public String fetchDate() {

    try {
      // 创建SimpleDateFormat对象，指定日期时间格式
      SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

      // 使用format方法将时间戳转换为时间表达式
      return sdf.format(date);
    } catch (Exception e) {
      e.printStackTrace();
      return "";
    }
  }
}
