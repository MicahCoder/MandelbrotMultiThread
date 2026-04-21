package org.micahgruenwald.mandelbrotmultithread;

public record RenderArea(double xCenter, double yCenter, double xWidth, double yWidth) {
    public double x0(){
        return xCenter - xWidth/2;
    }
    public double y0(){
        return yCenter - yWidth/2;
    }
    public double x1(){
        return xCenter + xWidth/2;
    }
    public double y1(){
        return yCenter + yWidth/2;
    }
}
