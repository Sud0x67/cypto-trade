/* (C)2024  Sud0x67@github.com */
package com.sud0x67.cryptotrade.core.utils;

import com.sud0x67.cryptotrade.core.entity.KLine;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class SpotUtil {

  public static List<KLine> readDatas(String folderPath) {
    ArrayList<KLine> res = new ArrayList<>();
    File folder = new File(folderPath);
    File[] files = folder.listFiles();
    Arrays.sort(
        files,
        new Comparator<File>() {
          @Override
          public int compare(File o1, File o2) {
            return o1.getName().compareTo(o2.getName());
          }
        });
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    for (File file : files) {
      try (BufferedReader br = new BufferedReader(new FileReader(file))) {
        String line;
        int i = 0;
        while ((line = br.readLine()) != null) {
          // 使用逗号分割CSV行中的字段
          String[] data = line.split(",");

          // 解析数据并创建PriceData对象
          long timestamp = Long.parseLong(data[0]);
          double openPrice = Double.parseDouble(data[1]);
          double highPrice = Double.parseDouble(data[2]);
          double lowPrice = Double.parseDouble(data[3]);
          double closePrice = Double.parseDouble(data[4]);

          // 创建PriceData对象并进行处理（可以根据需求进行其他操作）
          KLine KLine = new KLine(timestamp, openPrice, highPrice, lowPrice, closePrice);
          res.add(KLine);
        }
      } catch (IOException | NumberFormatException e) {
        e.printStackTrace();
      }
    }

    return res;
  }
}
