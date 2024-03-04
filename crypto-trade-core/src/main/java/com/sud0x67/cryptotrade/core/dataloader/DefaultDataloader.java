package com.sud0x67.cryptotrade.core.dataloader;


import com.sud0x67.cryptotrade.core.entity.KLine;
import com.sud0x67.cryptotrade.core.utils.SpotUtil;

import java.util.List;

public class DefaultDataloader {
    public static List<KLine> load(String fileDir) {
        return SpotUtil.readDatas(fileDir);
    }
}