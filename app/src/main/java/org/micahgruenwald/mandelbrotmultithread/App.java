package org.micahgruenwald.mandelbrotmultithread;

import io.qt.core.Qt;
import io.qt.gui.QPixmap;
import io.qt.widgets.*;
import java.nio.file.Files;
import java.nio.file.Path;

public class App {
  public static void main(String[] args) {
    QApplication.initialize(args);

    QWidget window = new QWidget();
    window.setWindowTitle("Test Window");
    window.resize(900, 600);

    QHBoxLayout mainLayout = new QHBoxLayout(window);
    String imagePath = findImagePath();
    ZoomableCropImageView imageView = new ZoomableCropImageView();
    QPixmap pixmap = new QPixmap(imagePath);

    if (pixmap.isNull()) {
      imageView.setErrorText("Could not load image. Tried: " + imagePath);
    } else {
      imageView.setImage(pixmap);
    }
    imageView.setSizePolicy(QSizePolicy.Policy.Expanding, QSizePolicy.Policy.Expanding);
    imageView.setMinimumSize(1, 1);

    SidebarPanel sidebar = new SidebarPanel(imageView);

    QSplitter splitter = new QSplitter();
    splitter.setOrientation(Qt.Orientation.Horizontal);
    splitter.addWidget(sidebar);
    splitter.addWidget(imageView);
    splitter.setOpaqueResize(true);
    splitter.setChildrenCollapsible(false);
    splitter.setHandleWidth(8);
    splitter.setStretchFactor(0, 0);
    splitter.setStretchFactor(1, 1);
    splitter.setCollapsible(0, false);
    splitter.setCollapsible(1, false);
    splitter.setSizes(java.util.List.of(180, 320));

    mainLayout.addWidget(splitter);

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
