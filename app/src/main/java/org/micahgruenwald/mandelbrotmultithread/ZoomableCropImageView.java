package org.micahgruenwald.mandelbrotmultithread;

import io.qt.core.QRectF;
import io.qt.core.QTimer;
import io.qt.core.Qt;
import io.qt.gui.QPixmap;
import io.qt.gui.QWheelEvent;
import io.qt.widgets.QGraphicsPixmapItem;
import io.qt.widgets.QGraphicsScene;
import io.qt.widgets.QGraphicsView;
import io.qt.widgets.QGraphicsView.ViewportAnchor;

class ZoomableCropImageView extends QGraphicsView {
  private QPixmap image;
  private String errorText = "No image loaded";
  private final QTimer rerenderTimer = new QTimer(this);
  private final Manager manager;
  private double zoomFactor = 1.0;
  private static final double ZOOM_STEP = 1.15;
  private static final double MAX_ZOOM = 20.0;
  private QGraphicsPixmapItem item;
  private QGraphicsScene scene;

  ZoomableCropImageView(Manager manager) {
    this.manager = manager;
    setFocusPolicy(Qt.FocusPolicy.StrongFocus);
    setTransformationAnchor(ViewportAnchor.AnchorUnderMouse);
    setResizeAnchor(ViewportAnchor.AnchorUnderMouse);

    this.scene = new QGraphicsScene(this);

    // Set transformations to be anchored under the mouse
    setTransformationAnchor(ViewportAnchor.AnchorUnderMouse);
    setResizeAnchor(ViewportAnchor.AnchorUnderMouse);

    // Optional: Dragging to pan
    setDragMode(DragMode.ScrollHandDrag);

    rerenderTimer.setSingleShot(true);
    rerenderTimer.setInterval(500);
    rerenderTimer.timeout.connect(this::rerenderVisible);
  }

  void setImage(QPixmap pixmap) {
    // image = pixmap;
    item = new QGraphicsPixmapItem(pixmap);
    scene.addItem(item);
    setScene(scene);
    errorText = null;
    zoomFactor = 1.0;
    update();
  }

  void setErrorText(String text) {
    image = null;
    errorText = text;
    update();
  }

  void zoomIn() {
    scale(zoomFactor * ZOOM_STEP, zoomFactor * ZOOM_STEP);
    scheduleRerender();
  }

  void zoomOut() {
    scale(zoomFactor / ZOOM_STEP, zoomFactor / ZOOM_STEP);
  }

  void resetZoom() {
    setZoom(1.0);
  }

  private void setZoom(double zoom) {
    zoomFactor = Math.max(1.0, Math.min(zoom, MAX_ZOOM));
    update();
  }

  private void scheduleRerender() {
    rerenderTimer.start();
  }

  public void rerenderVisible() {
    RenderArea current = manager.getRenderArea();

    QRectF visibleScene = mapToScene(viewport().rect()).boundingRect();
    QRectF visibleImage = visibleScene.intersected(item.sceneBoundingRect());

    if (visibleImage.isEmpty()) {
      return; // No visible area to render
    }

    double imageWidth = item.pixmap().width();
    double imageHeight = item.pixmap().height();

    double x0 = current.x0() + (visibleImage.left() / imageWidth) * current.xWidth();
    double x1 = current.x0() + (visibleImage.right() / imageWidth) * current.xWidth();
    double y0 = current.y0() + (visibleImage.top() / imageHeight) * current.yWidth();
    double y1 = current.y0() + (visibleImage.bottom() / imageHeight) * current.yWidth();

    manager.setRenderArea(new RenderArea(
      (x0 + x1) / 2.0,
      (y0 + y1) / 2.0,
      x1 - x0,
      y1 - y0
    ));

    manager.render();
    this.setImage(manager.getQPixmap()); // might be unneeded

    resetTransform();
    zoomFactor = 1.0;
  }

  // @Override
  // protected void paintEvent(QPaintEvent event) {
  //   super.paintEvent(event);

  //   QPainter painter = new QPainter(this);
  //   try {
  //     if (image == null || image.isNull()) {
  //       painter.drawText(rect(), Qt.AlignmentFlag.AlignCenter.value(), errorText);
  //       return;
  //     }

  //     int viewWidth = Math.max(1, width());
  //     int viewHeight = Math.max(1, height());
  //     int imageWidth = image.width();
  //     int imageHeight = image.height();

  //     double viewAspect = (double) viewWidth / viewHeight;
  //     double imageAspect = (double) imageWidth / imageHeight;

  //     double baseSourceWidth;
  //     double baseSourceHeight;
  //     if (imageAspect > viewAspect) {
  //       baseSourceHeight = imageHeight;
  //       baseSourceWidth = imageHeight * viewAspect;
  //     } else {
  //       baseSourceWidth = imageWidth;
  //       baseSourceHeight = imageWidth / viewAspect;
  //     }

  //     int sourceWidth = Math.max(1, (int) Math.round(baseSourceWidth / zoomFactor));
  //     int sourceHeight = Math.max(1, (int) Math.round(baseSourceHeight / zoomFactor));
  //     int sourceX = (imageWidth - sourceWidth) / 2;
  //     int sourceY = (imageHeight - sourceHeight) / 2;

  //     painter.drawPixmap(0, 0, viewWidth, viewHeight, image, sourceX, sourceY, sourceWidth,
  // sourceHeight);
  //   } finally {
  //     painter.end();
  //   }
  // }

  @Override
  protected void wheelEvent(QWheelEvent event) {

    if (event.angleDelta().y() > 0) {
      zoomIn();
    } else if (event.angleDelta().y() < 0) {
      zoomOut();
    } else {
      super.wheelEvent(event);
      return;
    }

    event.accept();
  }
}
