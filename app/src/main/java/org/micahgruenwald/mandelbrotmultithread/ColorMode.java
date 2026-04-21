package org.micahgruenwald.mandelbrotmultithread;

import java.awt.Color;
import java.util.Random;

public interface ColorMode{
    public abstract int calcColor(double lightness);

    public static final ColorMode RANDOM = new ColorMode(){
      private int randomOffset = new Random().nextInt();

      public int calcColor(double lightness){
        if (lightness == 1) {
          return 0;
        }
        Random random = new Random((long) (lightness* Calculator.getMaxIterations() + randomOffset));
        return Color.HSBtoRGB(360 * random.nextFloat(), 1f, random.nextFloat());
      }
    };

    public static final ColorMode HSV = (double lightness) -> Color.HSBtoRGB((float) lightness * 10, 1, .9f);

    public static final ColorMode HSV_WITH_BLACK = (double lightness) ->{
        if (lightness == 1) {
          return 0;
        }
        return  Color.HSBtoRGB((float) lightness * 10, 1, .9f);
    };

    public static final ColorMode BLACK_AND_WHITE = (double lightness) -> {
        int gray = (int)(lightness * 255.0);
        return (gray << 16) | (gray << 8) | gray;
    };
    public record SimpleGradient(float r, float g, float b) implements ColorMode{
        @Override
        public int calcColor(double lightness){
            return (((int) (r * lightness) << 16) | ((int) (g * lightness) << 8) | ((int) (b * lightness)));
        }
    }
  }