package org.icepdf.core.pobjects;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

/* loaded from: icepdf-core.jar:org/icepdf/core/pobjects/PRectangle.class */
public class PRectangle extends Rectangle2D.Float {
    private float p1x;
    private float p1y;
    private float p2x;
    private float p2y;

    public PRectangle(Point2D.Float p1, Point2D.Float p2) {
        normalizeCoordinates(p1.f12396x, p1.f12397y, p2.f12396x, p2.f12397y);
        this.p1x = p1.f12396x;
        this.p1y = p1.f12397y;
        this.p2x = p2.f12396x;
        this.p2y = p2.f12397y;
    }

    private PRectangle(float x2, float y2, float width, float height) {
        super(x2, y2, width, height);
    }

    public PRectangle(List coordinates) throws IllegalArgumentException {
        if (coordinates == null || coordinates.size() < 4) {
            throw new IllegalArgumentException();
        }
        float x1 = ((Number) coordinates.get(0)).floatValue();
        float y1 = ((Number) coordinates.get(1)).floatValue();
        float x2 = ((Number) coordinates.get(2)).floatValue();
        float y2 = ((Number) coordinates.get(3)).floatValue();
        this.p1x = x1;
        this.p1y = y1;
        this.p2x = x2;
        this.p2y = y2;
        normalizeCoordinates(x1, y1, x2, y2);
    }

    public PRectangle createCartesianIntersection(PRectangle src2) {
        PRectangle rec = new PRectangle(src2.f12404x, src2.f12405y, src2.width, src2.height);
        float xLeft = this.f12404x > rec.f12404x ? this.f12404x : rec.f12404x;
        float xRight = this.f12404x + this.width > rec.f12404x + rec.width ? rec.f12404x + rec.width : this.f12404x + this.width;
        float yBottom = this.f12405y - this.height < rec.f12405y - rec.height ? rec.f12405y - rec.height : this.f12405y - this.height;
        float yTop = this.f12405y > rec.f12405y ? rec.f12405y : this.f12405y;
        rec.f12404x = xLeft;
        rec.f12405y = yTop;
        rec.width = xRight - xLeft;
        rec.height = yTop - yBottom;
        if (rec.width < 0.0f || rec.height < 0.0f) {
            rec.height = 0.0f;
            rec.width = 0.0f;
            rec.f12405y = 0.0f;
            rec.f12404x = 0.0f;
        }
        return rec;
    }

    public Rectangle2D.Float getOriginalPoints() {
        return new Rectangle2D.Float(this.p1x, this.p1y, this.p2x, this.p2y);
    }

    public Rectangle2D.Float toJava2dCoordinates() {
        return new Rectangle2D.Float(this.f12404x, this.f12405y - this.height, this.width, this.height);
    }

    public static List getPRectangleVector(Rectangle2D rect) {
        Rectangle2D rect2 = new Rectangle2D.Double(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());
        ArrayList<Number> coords = new ArrayList<>(4);
        coords.add(Double.valueOf(rect2.getMinX()));
        coords.add(Double.valueOf(rect2.getMinY()));
        coords.add(Double.valueOf(rect2.getMaxX()));
        coords.add(Double.valueOf(rect2.getMaxY()));
        return coords;
    }

    private void normalizeCoordinates(float x1, float y1, float x2, float y2) {
        float x3 = x1;
        float y3 = y1;
        float w2 = Math.abs(x2 - x1);
        float h2 = Math.abs(y2 - y1);
        if (x1 > x2) {
            x3 = x2;
        }
        if (y1 < y2) {
            y3 = y2;
        }
        setRect(x3, y3, w2, h2);
    }
}
