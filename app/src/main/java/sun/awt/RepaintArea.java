package sun.awt;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Rectangle;

/* loaded from: rt.jar:sun/awt/RepaintArea.class */
public class RepaintArea {
    private static final int MAX_BENEFIT_RATIO = 4;
    private static final int HORIZONTAL = 0;
    private static final int VERTICAL = 1;
    private static final int UPDATE = 2;
    private static final int RECT_COUNT = 3;
    private Rectangle[] paintRects;

    public RepaintArea() {
        this.paintRects = new Rectangle[3];
    }

    private RepaintArea(RepaintArea repaintArea) {
        this.paintRects = new Rectangle[3];
        for (int i2 = 0; i2 < 3; i2++) {
            this.paintRects[i2] = repaintArea.paintRects[i2];
        }
    }

    public synchronized void add(Rectangle rectangle, int i2) {
        if (rectangle.isEmpty()) {
            return;
        }
        boolean z2 = 2;
        if (i2 == 800) {
            z2 = rectangle.width <= rectangle.height;
        }
        if (this.paintRects[z2 ? 1 : 0] != null) {
            this.paintRects[z2 ? 1 : 0].add(rectangle);
        } else {
            this.paintRects[z2 ? 1 : 0] = new Rectangle(rectangle);
        }
    }

    private synchronized RepaintArea cloneAndReset() {
        RepaintArea repaintArea = new RepaintArea(this);
        for (int i2 = 0; i2 < 3; i2++) {
            this.paintRects[i2] = null;
        }
        return repaintArea;
    }

    public boolean isEmpty() {
        for (int i2 = 0; i2 < 3; i2++) {
            if (this.paintRects[i2] != null) {
                return false;
            }
        }
        return true;
    }

    public synchronized void constrain(int i2, int i3, int i4, int i5) {
        for (int i6 = 0; i6 < 3; i6++) {
            Rectangle rectangle = this.paintRects[i6];
            if (rectangle != null) {
                if (rectangle.f12372x < i2) {
                    rectangle.width -= i2 - rectangle.f12372x;
                    rectangle.f12372x = i2;
                }
                if (rectangle.f12373y < i3) {
                    rectangle.height -= i3 - rectangle.f12373y;
                    rectangle.f12373y = i3;
                }
                int i7 = ((rectangle.f12372x + rectangle.width) - i2) - i4;
                if (i7 > 0) {
                    rectangle.width -= i7;
                }
                int i8 = ((rectangle.f12373y + rectangle.height) - i3) - i5;
                if (i8 > 0) {
                    rectangle.height -= i8;
                }
                if (rectangle.width <= 0 || rectangle.height <= 0) {
                    this.paintRects[i6] = null;
                }
            }
        }
    }

    public synchronized void subtract(int i2, int i3, int i4, int i5) {
        Rectangle rectangle = new Rectangle(i2, i3, i4, i5);
        for (int i6 = 0; i6 < 3; i6++) {
            if (subtract(this.paintRects[i6], rectangle) && this.paintRects[i6] != null && this.paintRects[i6].isEmpty()) {
                this.paintRects[i6] = null;
            }
        }
    }

    public void paint(Object obj, boolean z2) {
        Graphics graphics;
        Component component = (Component) obj;
        if (isEmpty() || !component.isVisible()) {
            return;
        }
        RepaintArea repaintAreaCloneAndReset = cloneAndReset();
        if (!subtract(repaintAreaCloneAndReset.paintRects[1], repaintAreaCloneAndReset.paintRects[0])) {
            subtract(repaintAreaCloneAndReset.paintRects[0], repaintAreaCloneAndReset.paintRects[1]);
        }
        if (repaintAreaCloneAndReset.paintRects[0] != null && repaintAreaCloneAndReset.paintRects[1] != null) {
            Rectangle rectangleUnion = repaintAreaCloneAndReset.paintRects[0].union(repaintAreaCloneAndReset.paintRects[1]);
            int i2 = rectangleUnion.width * rectangleUnion.height;
            if (4 * ((i2 - (repaintAreaCloneAndReset.paintRects[0].width * repaintAreaCloneAndReset.paintRects[0].height)) - (repaintAreaCloneAndReset.paintRects[1].width * repaintAreaCloneAndReset.paintRects[1].height)) < i2) {
                repaintAreaCloneAndReset.paintRects[0] = rectangleUnion;
                repaintAreaCloneAndReset.paintRects[1] = null;
            }
        }
        for (int i3 = 0; i3 < this.paintRects.length; i3++) {
            if (repaintAreaCloneAndReset.paintRects[i3] != null && !repaintAreaCloneAndReset.paintRects[i3].isEmpty() && (graphics = component.getGraphics()) != null) {
                try {
                    graphics.setClip(repaintAreaCloneAndReset.paintRects[i3]);
                    if (i3 == 2) {
                        updateComponent(component, graphics);
                    } else {
                        if (z2) {
                            graphics.clearRect(repaintAreaCloneAndReset.paintRects[i3].f12372x, repaintAreaCloneAndReset.paintRects[i3].f12373y, repaintAreaCloneAndReset.paintRects[i3].width, repaintAreaCloneAndReset.paintRects[i3].height);
                        }
                        paintComponent(component, graphics);
                    }
                } finally {
                    graphics.dispose();
                }
            }
        }
    }

    protected void updateComponent(Component component, Graphics graphics) {
        if (component != null) {
            component.update(graphics);
        }
    }

    protected void paintComponent(Component component, Graphics graphics) {
        if (component != null) {
            component.paint(graphics);
        }
    }

    static boolean subtract(Rectangle rectangle, Rectangle rectangle2) {
        if (rectangle == null || rectangle2 == null) {
            return true;
        }
        Rectangle rectangleIntersection = rectangle.intersection(rectangle2);
        if (rectangleIntersection.isEmpty()) {
            return true;
        }
        if (rectangle.f12372x == rectangleIntersection.f12372x && rectangle.f12373y == rectangleIntersection.f12373y) {
            if (rectangle.width == rectangleIntersection.width) {
                rectangle.f12373y += rectangleIntersection.height;
                rectangle.height -= rectangleIntersection.height;
                return true;
            }
            if (rectangle.height == rectangleIntersection.height) {
                rectangle.f12372x += rectangleIntersection.width;
                rectangle.width -= rectangleIntersection.width;
                return true;
            }
            return false;
        }
        if (rectangle.f12372x + rectangle.width == rectangleIntersection.f12372x + rectangleIntersection.width && rectangle.f12373y + rectangle.height == rectangleIntersection.f12373y + rectangleIntersection.height) {
            if (rectangle.width == rectangleIntersection.width) {
                rectangle.height -= rectangleIntersection.height;
                return true;
            }
            if (rectangle.height == rectangleIntersection.height) {
                rectangle.width -= rectangleIntersection.width;
                return true;
            }
            return false;
        }
        return false;
    }

    public String toString() {
        return super.toString() + "[ horizontal=" + ((Object) this.paintRects[0]) + " vertical=" + ((Object) this.paintRects[1]) + " update=" + ((Object) this.paintRects[2]) + "]";
    }
}
