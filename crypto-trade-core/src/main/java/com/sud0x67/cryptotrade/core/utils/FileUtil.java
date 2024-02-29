/* (C)2024  Sud0x67@github.com */
package com.sud0x67.cryptotrade.core.utils;

import java.io.*;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileUtil {

  /**
   * 使用 BufferedReader 读取文件内容
   *
   * @param filePath 文件路径
   * @return 文件内容字符串
   * @throws IOException 读取文件时可能抛出的异常
   */
  public static String readFile(String filePath) throws IOException {
    try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
      StringBuilder content = new StringBuilder();
      String line;

      while ((line = reader.readLine()) != null) {
        content.append(line).append("\n");
      }

      return content.toString();
    }
  }

  /**
   * 使用 BufferedWriter 写入文件内容
   *
   * @param filePath 文件路径
   * @param fileContent 文件内容字符串
   * @throws IOException 写入文件时可能抛出的异常
   */
  public static void writeFile(String filePath, String fileContent) throws IOException {
    // 获取文件夹路径
    Path folderPath = FileSystems.getDefault().getPath(filePath).getParent();

    // 检查文件夹是否存在，如果不存在则创建
    if (!Files.exists(folderPath)) {
      Files.createDirectories(folderPath);
    }

    // 使用 BufferedWriter 写入文件内容
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
      writer.write(fileContent);
    }
  }

  public static void removeFile(String filePath) {
    // 将字符串路径转换为 Path 对象
    Path path = Paths.get(filePath);
    try {
      // 使用 Files.delete 方法删除文件
      Files.delete(path);
      System.out.println("File deleted successfully.");
    } catch (IOException e) {
      System.err.println("Unable to delete the file: " + e.getMessage());
    }
  }
}
