package sun.java2d.pipe;

/* loaded from: rt.jar:sun/java2d/pipe/AATileGenerator.class */
public interface AATileGenerator {
    int getTileWidth();

    int getTileHeight();

    int getTypicalAlpha();

    void nextTile();

    void getAlpha(byte[] bArr, int i2, int i3);

    void dispose();
}
