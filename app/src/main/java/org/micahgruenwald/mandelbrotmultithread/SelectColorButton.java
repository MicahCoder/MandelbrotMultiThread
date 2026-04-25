package org.micahgruenwald.mandelbrotmultithread;

import io.qt.gui.QColor;
import io.qt.widgets.QColorDialog;
import io.qt.widgets.QPushButton;
import io.qt.widgets.QWidget;

/*
Inspiration found @link{https://stackoverflow.com/questions/18257281/qt-color-picker-widget}
*/
public class SelectColorButton extends QPushButton{
    private QColor color;
    public SelectColorButton(QWidget parent, QColor color){
        super(parent);
        this.color= color;
        setFixedSize(25,25);
        updateColor();
        clicked.connect(this::changeColor);
    }

    public void updateColor(){
        setStyleSheet("background-color: " + color.name());
    }

    public void changeColor(){
    QColor newColor = QColorDialog.getColor(color, parentWidget());
        if ( newColor != color )
        {
            setColor(newColor);
        }
    }
    public void setColor(QColor color){
        this.color=color;
        updateColor();
    }

    public QColor getColor(){
        return color;
    }

}
