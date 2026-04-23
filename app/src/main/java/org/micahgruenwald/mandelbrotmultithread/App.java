package org.micahgruenwald.mandelbrotmultithread;

import io.qt.gui.QPixmap;
import io.qt.widgets.*;
import java.nio.file.Files;
import java.nio.file.Path;

public class App {
  public static void main(String[] args) {
    QApplication.initialize(args);

    QWidget window = new QWidget();
    window.setWindowTitle("Test Window");
    window.resize(500, 300);

    QHBoxLayout mainLayout = new QHBoxLayout(window);
    String imagePath = findImagePath();
    QLabel imageLabel = new QLabel();
    QPixmap pixmap = new QPixmap(imagePath);

    if (pixmap.isNull()) {
      imageLabel.setText("Could not load image. Tried: " + imagePath);
    } else {
      imageLabel.setPixmap(pixmap);
      imageLabel.setScaledContents(true);
    }
    imageLabel.setSizePolicy(QSizePolicy.Policy.Expanding, QSizePolicy.Policy.Expanding);

    QVBoxLayout sidebarLayout = new QVBoxLayout();
    sidebarLayout.addWidget(new QPushButton("Button 1"));
    sidebarLayout.addWidget(new QPushButton("Button 2"));
    sidebarLayout.addStretch(1);

    QWidget sidebar = new QWidget();
    sidebar.setLayout(sidebarLayout);
    sidebar.setMinimumWidth(160);

    mainLayout.addWidget(sidebar);
    mainLayout.addWidget(imageLabel);
    mainLayout.setStretch(0, 0);
    mainLayout.setStretch(1, 1);

    window.show();

    QApplication.exec();
    QApplication.shutdown();
  }

  private static String findImagePath() {
    Path cwd = Path.of("").toAbsolutePath().normalize();

    Path[] candidates =
        new Path[] {
          cwd.resolve(
              "app/src/test/java/org/micahgruenwald/mandelbrotmultithread/testOutput/saved.png"),
          cwd.resolve(
              "src/test/java/org/micahgruenwald/mandelbrotmultithread/testOutput/saved.png"),
          cwd.resolve(
              "../app/src/test/java/org/micahgruenwald/mandelbrotmultithread/testOutput/saved.png")
        };

    for (Path candidate : candidates) {
      Path normalized = candidate.normalize();
      if (Files.exists(normalized)) {
        return normalized.toString();
      }
    }

    return candidates[0].normalize().toString();
  }
}
