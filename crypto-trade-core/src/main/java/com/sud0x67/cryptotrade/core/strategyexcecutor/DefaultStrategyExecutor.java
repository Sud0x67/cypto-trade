/* (C)2024  Sud0x67@github.com */
package com.sud0x67.cryptotrade.core.strategyexcecutor;

import com.sud0x67.cryptotrade.core.strategy.CycleMultiplierStrategy;
import com.sud0x67.cryptotrade.core.strategy.TradeStrategy;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

public class DefaultStrategyExecutor {

  ThreadPoolTaskScheduler threadPoolTaskScheduler;
  private CycleMultiplierStrategy cycleMultiplierStrategyV2;

  public DefaultStrategyExecutor() {
    this.threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
    threadPoolTaskScheduler.setPoolSize(4);
  }

  public void execute(Trigger trigger, TradeStrategy tradeStrategy) {
    this.threadPoolTaskScheduler.schedule(() -> tradeStrategy.invoke(), trigger);
  }
}
