/* (C)2024  Sud0x67@github.com */
package com.sud0x67.cryptotrade.core.strategyexcecutor;

import com.sud0x67.cryptotrade.core.enums.OrderSymbol;
import com.sud0x67.cryptotrade.core.strategy.CycleMultiplierStrategy;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

public class DefaultStrategyExecutor {

  ThreadPoolTaskScheduler threadPoolTaskScheduler;
  private CycleMultiplierStrategy cycleMultiplierStrategyV2;

  public DefaultStrategyExecutor() {
    this.threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
    threadPoolTaskScheduler.setPoolSize(4);
  }

  public void execute(Trigger trigger) {
    this.threadPoolTaskScheduler.schedule(()->cycleMultiplierStrategyV2.execute(OrderSymbol.BTCFDUSD)
        , trigger);
  }
}
