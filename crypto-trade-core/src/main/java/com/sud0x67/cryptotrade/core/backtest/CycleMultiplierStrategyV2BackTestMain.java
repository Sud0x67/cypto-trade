package com.sud0x67.cryptotrade.core.backtest;

import com.sud0x67.cryptotrade.core.dataloader.DefaultDataloader;
import com.sud0x67.cryptotrade.core.entity.KLine;
import com.sud0x67.cryptotrade.core.enums.OrderSymbol;
import com.sud0x67.cryptotrade.core.service.BackTestEnv;
import com.sud0x67.cryptotrade.core.service.impl.LocalHistoryBackTestEnv;
import com.sud0x67.cryptotrade.core.strategy.CycleMultiplierStrategy;

import java.util.List;

public class CycleMultiplierStrategyV2BackTestMain {

    public static void main(String[] args) {
        // TODO ATTENTION GuaranteedStrategyV2 will sleep for 1 second when executing please check it when back test.
        String path = GuaranteedStrategyV2BackTestMain.class.getResource("/data/spot/btc/2023/1h").getPath();
        List<KLine> history = DefaultDataloader.load(path);
        BackTestEnv backTestEnv = new LocalHistoryBackTestEnv();
        backTestEnv.init(history, OrderSymbol.BTCFDUSD, 10000d);
        CycleMultiplierStrategy multiplierStrategy = new CycleMultiplierStrategy(backTestEnv);
        while (!backTestEnv.stop()){
            multiplierStrategy.execute(OrderSymbol.BTCFDUSD);
            backTestEnv.update();
        }

        backTestEnv.printProfit();
    }
}
