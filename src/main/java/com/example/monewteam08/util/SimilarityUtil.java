package com.example.monewteam08.util;

public class SimilarityUtil {

  public static boolean isSimilar(String a, String b) {
    String[] tokensA = a.replaceAll("\\s+", "").split(" ");
    String[] tokensB = b.replaceAll("\\s+", "").split(" ");

    int intersection = 0;
    for (String tokenA : tokensA) {
      for (String tokenB : tokensB) {
        if (tokenA.equals(tokenB)) {
          intersection++;
          break;
        }
      }
    }

    int union = tokensA.length + tokensB.length - intersection;
    double similarity = intersection / (double) union;

    return similarity >= 0.8;
  }

}
