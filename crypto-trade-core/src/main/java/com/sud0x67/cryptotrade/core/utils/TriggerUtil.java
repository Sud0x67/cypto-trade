package com.sud0x67.cryptotrade.core.utils;

import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.support.CronTrigger;

public class TriggerUtil {

  public static final Trigger TRIGGER_BY_SECOND = new CronTrigger("0/1 * * * * *");
  public static final Trigger TRIGGER_BY_MINUTE = new CronTrigger("0 * * * * *");

  public static final Trigger TRIGGER_BY_HOUR = new CronTrigger("0 0 * * * *");
  // Trigger on 9 clock every day
  public static final Trigger TRIGGER_BY_DAY = new CronTrigger("0 0 9 * * *");
}
