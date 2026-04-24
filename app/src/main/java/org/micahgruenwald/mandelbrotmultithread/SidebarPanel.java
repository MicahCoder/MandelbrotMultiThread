package org.micahgruenwald.mandelbrotmultithread;

import io.qt.widgets.QComboBox;
import io.qt.widgets.QHBoxLayout;
import io.qt.widgets.QLabel;
import io.qt.widgets.QPushButton;
import io.qt.widgets.QVBoxLayout;
import io.qt.widgets.QWidget;

class SidebarPanel extends QWidget {
  SidebarPanel(ZoomableCropImageView imageView) {
    QVBoxLayout sidebarLayout = new QVBoxLayout();
    QLabel titleLabel = new QLabel("Mandelbrot Explorer");
    titleLabel.font().setPixelSize(25);
    titleLabel.font().setBold(true);
    sidebarLayout.addWidget(titleLabel);

    sidebarLayout.addWidget(new QLabel("Zoom Controls"));
    QHBoxLayout zoomButtonsLayout = new QHBoxLayout();
    zoomButtonsLayout.setSpacing(5);
    QPushButton zoomInButton = new QPushButton("+");
    QPushButton zoomOutButton = new QPushButton("-");
    QPushButton resetZoomButton = new QPushButton("Reset");
    zoomButtonsLayout.addWidget(zoomInButton);
    zoomButtonsLayout.addWidget(zoomOutButton);
    zoomButtonsLayout.addWidget(resetZoomButton);
    sidebarLayout.addLayout(zoomButtonsLayout); 

    QComboBox colorSchemeComboBox = new QComboBox();
    colorSchemeComboBox.addItem("Default");
    colorSchemeComboBox.addItem("Inverted");
    sidebarLayout.addWidget(colorSchemeComboBox);
    sidebarLayout.addStretch(1);

    zoomInButton.clicked.connect(imageView::zoomIn);
    zoomOutButton.clicked.connect(imageView::zoomOut);
    resetZoomButton.clicked.connect(imageView::resetZoom);

    setLayout(sidebarLayout);
    setMinimumWidth(160);
  }
}
