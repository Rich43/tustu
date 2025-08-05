package java.awt.image;

import java.awt.RenderingHints;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

/* loaded from: rt.jar:java/awt/image/RasterOp.class */
public interface RasterOp {
    WritableRaster filter(Raster raster, WritableRaster writableRaster);

    Rectangle2D getBounds2D(Raster raster);

    WritableRaster createCompatibleDestRaster(Raster raster);

    Point2D getPoint2D(Point2D point2D, Point2D point2D2);

    RenderingHints getRenderingHints();
}
