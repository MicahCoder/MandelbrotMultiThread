package org.micahgruenwald.mandelbrotmultithread;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Manager extends Thread{
    private final BufferedImage image;
    private final int threadCount;
    private final ArrayList<MandelbrotThread> threads;
    private final RenderArea area;
    public Manager(int threadCount, RenderArea area, BufferedImage image){
        this.threadCount = threadCount;
        this.image = image;
        this.area = area;
        threads = new ArrayList<>(threadCount);
    }

    private static ArrayList<Integer> rowLengths(int dividend, int divisor){
        ArrayList<Integer> rowLengths = new ArrayList<>(divisor);
        int base = dividend / divisor;
        int remainder = dividend % divisor;
        for(int i = 0; i<divisor; i++){
            if(i<remainder){
                rowLengths.add(base + 1);
            }else{
                rowLengths.add(base);
            }
        }
        return rowLengths;
    }

    @Override
    public void run(){
        ArrayList<Integer> rowLengths = rowLengths(image.getWidth(), threadCount);
        
        double x0 = area.x0();
        double y0 = area.y0();
        double dx = area.xWidth() / (image.getWidth());
        double dy = area.yWidth() / (image.getHeight());
        double x1;
        double y1 = area.y1();

        int row = 0;
        for(int i = 0; i<threadCount; i++){
            int length = rowLengths.get(i);
            x1 = x0 + length*dx;
            threads.add(new MandelbrotThread(x0, y0, x1, y1, dx, dy, row,0,image));
            row += length;
            x0 = x1;
        }
        for(Thread thread:threads){
            thread.start();
        }
        for(Thread thread:threads){
            try {
                thread.join();
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}
