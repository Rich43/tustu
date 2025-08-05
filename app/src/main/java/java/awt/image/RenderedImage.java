package java.awt.image;

import java.awt.Rectangle;
import java.util.Vector;

/* loaded from: rt.jar:java/awt/image/RenderedImage.class */
public interface RenderedImage {
    Vector<RenderedImage> getSources();

    Object getProperty(String str);

    String[] getPropertyNames();

    ColorModel getColorModel();

    SampleModel getSampleModel();

    int getWidth();

    int getHeight();

    int getMinX();

    int getMinY();

    int getNumXTiles();

    int getNumYTiles();

    int getMinTileX();

    int getMinTileY();

    int getTileWidth();

    int getTileHeight();

    int getTileGridXOffset();

    int getTileGridYOffset();

    Raster getTile(int i2, int i3);

    Raster getData();

    Raster getData(Rectangle rectangle);

    WritableRaster copyData(WritableRaster writableRaster);
}
