package java.awt.image;

import java.awt.Point;

/* loaded from: rt.jar:java/awt/image/WritableRenderedImage.class */
public interface WritableRenderedImage extends RenderedImage {
    void addTileObserver(TileObserver tileObserver);

    void removeTileObserver(TileObserver tileObserver);

    WritableRaster getWritableTile(int i2, int i3);

    void releaseWritableTile(int i2, int i3);

    boolean isTileWritable(int i2, int i3);

    Point[] getWritableTileIndices();

    boolean hasTileWriters();

    void setData(Raster raster);
}
