package org.micahgruenwald.mandelbrotmultithread;

import java.awt.Color;


public class Calculator {
  private static int maxIterations = 100;
  private static ColorMode COLOR_CALC= ColorMode.BLACK_AND_WHITE;

  public static double mandelbrotValue(double x, double y) {
    double x2 = 0.0;
    double y2 = 0.0;
    double w = 0.0;
    double x0 = x;
    double y0 = y;
    int iteration = 0;
    while (x2 + y2 <= 4 && iteration < maxIterations) {
      x = x2 - y2 + x0;
      y = w - x2 - y2 + y0;
      x2 = x * x;
      y2 = y * y;
      w = (x + y) * (x + y);
      iteration++;
    }
    return (double) iteration / maxIterations;
  }


  public static void setMaxIterations(int maxIts) {
    maxIterations = maxIts;
  }

  public static int getMaxIterations(){
    return maxIterations;
  }

  public static void setColorMode(ColorMode colorMode){
    COLOR_CALC = colorMode;
  }

  public static ColorMode getColorCalc(){
    return COLOR_CALC;
  }

  private Color complexGradient(Color[] colors, float[] positions, float position) {
    Color out = colors[0];
    for (int i = 1; i < positions.length; i++) {
      if (position > positions[i - 1] && position <= positions[i]) {
        out = gradient(colors[i - 1], colors[i], positions[i], positions[i - 1], position);
      }
    }
    return out;
  }

  private Color gradient(Color start, Color end, float startX, float endX, float position) {
    float length = endX - startX;
    float startWeight = (position - startX) / length;
    float endWeight = 1f - startWeight;
    float r = (start.getRed() * startWeight + end.getRed() * endWeight) / 255f;
    float g = (start.getGreen() * startWeight + end.getGreen() * endWeight) / 255f;
    float b = (start.getBlue() * startWeight + end.getBlue() * endWeight) / 255f;
    float a = (start.getAlpha() * startWeight + end.getAlpha() * endWeight) / 255f;
    return new Color(r, g, b, a);
  }
}
