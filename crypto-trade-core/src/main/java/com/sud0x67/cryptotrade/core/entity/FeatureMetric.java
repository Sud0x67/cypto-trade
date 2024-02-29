/* (C)2024  Sud0x67@github.com */
package com.sud0x67.cryptotrade.core.entity;

import java.util.Date;
import lombok.Data;

/**
 * 字段信息 字段名称 描述 create_time 创建时间 symbol 符号 sum_open_interest 总持仓量（基础币） sum_open_interest_value 持仓总价值
 * count_toptrader_long_short_ratio 大户账户数多空比 sum_toptrader_long_short_ratio 大户持仓量多空比
 * count_long_short_ratio 所有交易者账户数多空比 sum_taker_long_short_vol_ratio 吃单方主动买入总量/吃单方主动卖出总量
 */
@Data
public class FeatureMetric {

  /** 创建时间 */
  private Date createTime;

  /** 符号 */
  private String symbol;

  /** 总持仓量（基础币） */
  private double sumOpenInterest;

  /** 持仓总价值 */
  private double sumOpenInterestValue;

  /** 大户账户数多空比 */
  private double countToptraderLongShortRatio;

  /** 大户持仓量多空比 */
  private double sumToptraderLongShortRatio;

  /** 所有交易者账户数多空比 */
  private double countLongShortRatio;

  /** 吃单方主动买入总量/吃单方主动卖出总量 */
  private double sumTakerLongShortVolRatio;
}
