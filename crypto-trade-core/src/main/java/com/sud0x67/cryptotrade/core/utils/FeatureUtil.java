/* (C)2024  Sud0x67@github.com */
package com.sud0x67.cryptotrade.core.utils;

import com.sud0x67.cryptotrade.core.entity.FeatureMetric;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

public class FeatureUtil {

  public static List<FeatureMetric> readDatas(String folderPath) {
    ArrayList<FeatureMetric> res = new ArrayList<>();
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
    df.setTimeZone(TimeZone.getTimeZone("UTC"));
    for (File file : files) {
      try (BufferedReader br = new BufferedReader(new FileReader(file))) {
        String line;
        int i = 0;
        while ((line = br.readLine()) != null) {
          if (i == 0) {
            i++;
            continue;
          }
          // 以逗号分隔行数据
          String[] columns = line.split(",");

          try {
            // 创建 FeatureMetric 对象
            FeatureMetric featureMetric = new FeatureMetric();

            // 设置字段值
            featureMetric.setCreateTime(df.parse(columns[0]));
            featureMetric.setSymbol(columns[1]);
            featureMetric.setSumOpenInterest(new Double(columns[2]));
            featureMetric.setSumOpenInterestValue(new Double(columns[3]));
            featureMetric.setCountToptraderLongShortRatio(new Double(columns[4]));
            featureMetric.setSumToptraderLongShortRatio(new Double(columns[5]));
            featureMetric.setCountLongShortRatio(new Double(columns[6]));
            featureMetric.setSumTakerLongShortVolRatio(new Double(columns[7]));
            // 创建PriceData对象并进行处理（可以根据需求进行其他操作）
            res.add(featureMetric);
          } catch (Exception e) {

          }
        }
      } catch (IOException | NumberFormatException e) {
        e.printStackTrace();
      }
    }

    return res;
  }
}
