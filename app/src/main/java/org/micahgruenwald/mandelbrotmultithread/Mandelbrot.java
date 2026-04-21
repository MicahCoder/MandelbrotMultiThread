package org.micahgruenwald.mandelbrotmultithread;

public class Mandelbrot implements Runnable {

  @Override
  public void run() {
    for (int i = 0; i < 10; i++) {
      System.out.println("Running" + i);
    }
  }
}
