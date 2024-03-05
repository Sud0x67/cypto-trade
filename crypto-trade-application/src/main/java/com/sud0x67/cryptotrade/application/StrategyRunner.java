/* (C)2024  Sud0x67@github.com */
package com.sud0x67.cryptotrade.application;

import com.sud0x67.cryptotrade.application.controller.config.ApplicationConfigProperties;
import com.sud0x67.cryptotrade.core.service.SpotService;
import com.sud0x67.cryptotrade.core.service.impl.BinanceSpotServiceImpl;
import com.sud0x67.cryptotrade.core.strategy.CycleMultiplierStrategy;
import com.sud0x67.cryptotrade.core.strategyexcecutor.DefaultStrategyExecutor;
import com.sud0x67.cryptotrade.core.utils.TriggerUtil;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

@Component
public class StrategyRunner implements ApplicationRunner {
  public static final String cronOption = "cron";
  public static final String freqOption = "freq";
  private final ApplicationConfigProperties applicationConfigProperties;

  public StrategyRunner(ApplicationConfigProperties applicationConfigProperties) {
    this.applicationConfigProperties = applicationConfigProperties;
  }

  @Override
  public void run(ApplicationArguments args) throws Exception {
    // --freq second, minute, hour, day --cron 0 0 "* * * *"
    // default trigger frequency
    Trigger trigger = TriggerUtil.TRIGGER_BY_MINUTE;
    if (args.containsOption(cronOption)) {
      trigger = new CronTrigger(args.getOptionValues(cronOption).get(0));
    } else if (args.containsOption(freqOption)) {
      String frequency = args.getOptionValues(freqOption).get(0);
      switch (frequency) {
        case "second":
        case "s":
          trigger = TriggerUtil.TRIGGER_BY_SECOND;
          break;
        case "minute":
        case "m":
          trigger = TriggerUtil.TRIGGER_BY_MINUTE;
          break;
        case "hour":
        case "h":
          trigger = TriggerUtil.TRIGGER_BY_HOUR;
          break;
        case "day":
        case "d":
          trigger = TriggerUtil.TRIGGER_BY_DAY;
      }
    }

    SpotService spotService =
        new BinanceSpotServiceImpl(applicationConfigProperties.getExchangeConfig());
    new DefaultStrategyExecutor().execute(trigger, new CycleMultiplierStrategy(spotService));
  }
}
