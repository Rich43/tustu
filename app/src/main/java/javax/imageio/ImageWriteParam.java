package javax.imageio;

import java.awt.Dimension;
import java.util.Locale;

/* loaded from: rt.jar:javax/imageio/ImageWriteParam.class */
public class ImageWriteParam extends IIOParam {
    public static final int MODE_DISABLED = 0;
    public static final int MODE_DEFAULT = 1;
    public static final int MODE_EXPLICIT = 2;
    public static final int MODE_COPY_FROM_METADATA = 3;
    private static final int MAX_MODE = 3;
    protected boolean canWriteTiles;
    protected int tilingMode;
    protected Dimension[] preferredTileSizes;
    protected boolean tilingSet;
    protected int tileWidth;
    protected int tileHeight;
    protected boolean canOffsetTiles;
    protected int tileGridXOffset;
    protected int tileGridYOffset;
    protected boolean canWriteProgressive;
    protected int progressiveMode;
    protected boolean canWriteCompressed;
    protected int compressionMode;
    protected String[] compressionTypes;
    protected String compressionType;
    protected float compressionQuality;
    protected Locale locale;

    protected ImageWriteParam() {
        this.canWriteTiles = false;
        this.tilingMode = 3;
        this.preferredTileSizes = null;
        this.tilingSet = false;
        this.tileWidth = 0;
        this.tileHeight = 0;
        this.canOffsetTiles = false;
        this.tileGridXOffset = 0;
        this.tileGridYOffset = 0;
        this.canWriteProgressive = false;
        this.progressiveMode = 3;
        this.canWriteCompressed = false;
        this.compressionMode = 3;
        this.compressionTypes = null;
        this.compressionType = null;
        this.compressionQuality = 1.0f;
        this.locale = null;
    }

    public ImageWriteParam(Locale locale) {
        this.canWriteTiles = false;
        this.tilingMode = 3;
        this.preferredTileSizes = null;
        this.tilingSet = false;
        this.tileWidth = 0;
        this.tileHeight = 0;
        this.canOffsetTiles = false;
        this.tileGridXOffset = 0;
        this.tileGridYOffset = 0;
        this.canWriteProgressive = false;
        this.progressiveMode = 3;
        this.canWriteCompressed = false;
        this.compressionMode = 3;
        this.compressionTypes = null;
        this.compressionType = null;
        this.compressionQuality = 1.0f;
        this.locale = null;
        this.locale = locale;
    }

    private static Dimension[] clonePreferredTileSizes(Dimension[] dimensionArr) {
        if (dimensionArr == null) {
            return null;
        }
        Dimension[] dimensionArr2 = new Dimension[dimensionArr.length];
        for (int i2 = 0; i2 < dimensionArr.length; i2++) {
            dimensionArr2[i2] = new Dimension(dimensionArr[i2]);
        }
        return dimensionArr2;
    }

    public Locale getLocale() {
        return this.locale;
    }

    public boolean canWriteTiles() {
        return this.canWriteTiles;
    }

    public boolean canOffsetTiles() {
        return this.canOffsetTiles;
    }

    public void setTilingMode(int i2) {
        if (!canWriteTiles()) {
            throw new UnsupportedOperationException("Tiling not supported!");
        }
        if (i2 < 0 || i2 > 3) {
            throw new IllegalArgumentException("Illegal value for mode!");
        }
        this.tilingMode = i2;
        if (i2 == 2) {
            unsetTiling();
        }
    }

    public int getTilingMode() {
        if (!canWriteTiles()) {
            throw new UnsupportedOperationException("Tiling not supported");
        }
        return this.tilingMode;
    }

    public Dimension[] getPreferredTileSizes() {
        if (!canWriteTiles()) {
            throw new UnsupportedOperationException("Tiling not supported");
        }
        return clonePreferredTileSizes(this.preferredTileSizes);
    }

    public void setTiling(int i2, int i3, int i4, int i5) {
        if (!canWriteTiles()) {
            throw new UnsupportedOperationException("Tiling not supported!");
        }
        if (getTilingMode() != 2) {
            throw new IllegalStateException("Tiling mode not MODE_EXPLICIT!");
        }
        if (i2 <= 0 || i3 <= 0) {
            throw new IllegalArgumentException("tile dimensions are non-positive!");
        }
        boolean z2 = (i4 == 0 && i5 == 0) ? false : true;
        if (!canOffsetTiles() && z2) {
            throw new UnsupportedOperationException("Can't offset tiles!");
        }
        if (this.preferredTileSizes != null) {
            boolean z3 = true;
            for (int i6 = 0; i6 < this.preferredTileSizes.length; i6 += 2) {
                Dimension dimension = this.preferredTileSizes[i6];
                Dimension dimension2 = this.preferredTileSizes[i6 + 1];
                if (i2 < dimension.width || i2 > dimension2.width || i3 < dimension.height || i3 > dimension2.height) {
                    z3 = false;
                    break;
                }
            }
            if (!z3) {
                throw new IllegalArgumentException("Illegal tile size!");
            }
        }
        this.tilingSet = true;
        this.tileWidth = i2;
        this.tileHeight = i3;
        this.tileGridXOffset = i4;
        this.tileGridYOffset = i5;
    }

    public void unsetTiling() {
        if (!canWriteTiles()) {
            throw new UnsupportedOperationException("Tiling not supported!");
        }
        if (getTilingMode() != 2) {
            throw new IllegalStateException("Tiling mode not MODE_EXPLICIT!");
        }
        this.tilingSet = false;
        this.tileWidth = 0;
        this.tileHeight = 0;
        this.tileGridXOffset = 0;
        this.tileGridYOffset = 0;
    }

    public int getTileWidth() {
        if (!canWriteTiles()) {
            throw new UnsupportedOperationException("Tiling not supported!");
        }
        if (getTilingMode() != 2) {
            throw new IllegalStateException("Tiling mode not MODE_EXPLICIT!");
        }
        if (!this.tilingSet) {
            throw new IllegalStateException("Tiling parameters not set!");
        }
        return this.tileWidth;
    }

    public int getTileHeight() {
        if (!canWriteTiles()) {
            throw new UnsupportedOperationException("Tiling not supported!");
        }
        if (getTilingMode() != 2) {
            throw new IllegalStateException("Tiling mode not MODE_EXPLICIT!");
        }
        if (!this.tilingSet) {
            throw new IllegalStateException("Tiling parameters not set!");
        }
        return this.tileHeight;
    }

    public int getTileGridXOffset() {
        if (!canWriteTiles()) {
            throw new UnsupportedOperationException("Tiling not supported!");
        }
        if (getTilingMode() != 2) {
            throw new IllegalStateException("Tiling mode not MODE_EXPLICIT!");
        }
        if (!this.tilingSet) {
            throw new IllegalStateException("Tiling parameters not set!");
        }
        return this.tileGridXOffset;
    }

    public int getTileGridYOffset() {
        if (!canWriteTiles()) {
            throw new UnsupportedOperationException("Tiling not supported!");
        }
        if (getTilingMode() != 2) {
            throw new IllegalStateException("Tiling mode not MODE_EXPLICIT!");
        }
        if (!this.tilingSet) {
            throw new IllegalStateException("Tiling parameters not set!");
        }
        return this.tileGridYOffset;
    }

    public boolean canWriteProgressive() {
        return this.canWriteProgressive;
    }

    public void setProgressiveMode(int i2) {
        if (!canWriteProgressive()) {
            throw new UnsupportedOperationException("Progressive output not supported");
        }
        if (i2 < 0 || i2 > 3) {
            throw new IllegalArgumentException("Illegal value for mode!");
        }
        if (i2 == 2) {
            throw new IllegalArgumentException("MODE_EXPLICIT not supported for progressive output");
        }
        this.progressiveMode = i2;
    }

    public int getProgressiveMode() {
        if (!canWriteProgressive()) {
            throw new UnsupportedOperationException("Progressive output not supported");
        }
        return this.progressiveMode;
    }

    public boolean canWriteCompressed() {
        return this.canWriteCompressed;
    }

    public void setCompressionMode(int i2) {
        if (!canWriteCompressed()) {
            throw new UnsupportedOperationException("Compression not supported.");
        }
        if (i2 < 0 || i2 > 3) {
            throw new IllegalArgumentException("Illegal value for mode!");
        }
        this.compressionMode = i2;
        if (i2 == 2) {
            unsetCompression();
        }
    }

    public int getCompressionMode() {
        if (!canWriteCompressed()) {
            throw new UnsupportedOperationException("Compression not supported.");
        }
        return this.compressionMode;
    }

    public String[] getCompressionTypes() {
        if (!canWriteCompressed()) {
            throw new UnsupportedOperationException("Compression not supported");
        }
        if (this.compressionTypes == null) {
            return null;
        }
        return (String[]) this.compressionTypes.clone();
    }

    public void setCompressionType(String str) {
        if (!canWriteCompressed()) {
            throw new UnsupportedOperationException("Compression not supported");
        }
        if (getCompressionMode() != 2) {
            throw new IllegalStateException("Compression mode not MODE_EXPLICIT!");
        }
        String[] compressionTypes = getCompressionTypes();
        if (compressionTypes == null) {
            throw new UnsupportedOperationException("No settable compression types");
        }
        if (str != null) {
            boolean z2 = false;
            if (compressionTypes != null) {
                int i2 = 0;
                while (true) {
                    if (i2 >= compressionTypes.length) {
                        break;
                    }
                    if (!str.equals(compressionTypes[i2])) {
                        i2++;
                    } else {
                        z2 = true;
                        break;
                    }
                }
            }
            if (!z2) {
                throw new IllegalArgumentException("Unknown compression type!");
            }
        }
        this.compressionType = str;
    }

    public String getCompressionType() {
        if (!canWriteCompressed()) {
            throw new UnsupportedOperationException("Compression not supported.");
        }
        if (getCompressionMode() != 2) {
            throw new IllegalStateException("Compression mode not MODE_EXPLICIT!");
        }
        return this.compressionType;
    }

    public void unsetCompression() {
        if (!canWriteCompressed()) {
            throw new UnsupportedOperationException("Compression not supported");
        }
        if (getCompressionMode() != 2) {
            throw new IllegalStateException("Compression mode not MODE_EXPLICIT!");
        }
        this.compressionType = null;
        this.compressionQuality = 1.0f;
    }

    public String getLocalizedCompressionTypeName() {
        if (!canWriteCompressed()) {
            throw new UnsupportedOperationException("Compression not supported.");
        }
        if (getCompressionMode() != 2) {
            throw new IllegalStateException("Compression mode not MODE_EXPLICIT!");
        }
        if (getCompressionType() == null) {
            throw new IllegalStateException("No compression type set!");
        }
        return getCompressionType();
    }

    public boolean isCompressionLossless() {
        if (!canWriteCompressed()) {
            throw new UnsupportedOperationException("Compression not supported");
        }
        if (getCompressionMode() != 2) {
            throw new IllegalStateException("Compression mode not MODE_EXPLICIT!");
        }
        if (getCompressionTypes() != null && getCompressionType() == null) {
            throw new IllegalStateException("No compression type set!");
        }
        return true;
    }

    public void setCompressionQuality(float f2) {
        if (!canWriteCompressed()) {
            throw new UnsupportedOperationException("Compression not supported");
        }
        if (getCompressionMode() != 2) {
            throw new IllegalStateException("Compression mode not MODE_EXPLICIT!");
        }
        if (getCompressionTypes() != null && getCompressionType() == null) {
            throw new IllegalStateException("No compression type set!");
        }
        if (f2 < 0.0f || f2 > 1.0f) {
            throw new IllegalArgumentException("Quality out-of-bounds!");
        }
        this.compressionQuality = f2;
    }

    public float getCompressionQuality() {
        if (!canWriteCompressed()) {
            throw new UnsupportedOperationException("Compression not supported.");
        }
        if (getCompressionMode() != 2) {
            throw new IllegalStateException("Compression mode not MODE_EXPLICIT!");
        }
        if (getCompressionTypes() != null && getCompressionType() == null) {
            throw new IllegalStateException("No compression type set!");
        }
        return this.compressionQuality;
    }

    public float getBitRate(float f2) {
        if (!canWriteCompressed()) {
            throw new UnsupportedOperationException("Compression not supported.");
        }
        if (getCompressionMode() != 2) {
            throw new IllegalStateException("Compression mode not MODE_EXPLICIT!");
        }
        if (getCompressionTypes() != null && getCompressionType() == null) {
            throw new IllegalStateException("No compression type set!");
        }
        if (f2 < 0.0f || f2 > 1.0f) {
            throw new IllegalArgumentException("Quality out-of-bounds!");
        }
        return -1.0f;
    }

    public String[] getCompressionQualityDescriptions() {
        if (!canWriteCompressed()) {
            throw new UnsupportedOperationException("Compression not supported.");
        }
        if (getCompressionMode() != 2) {
            throw new IllegalStateException("Compression mode not MODE_EXPLICIT!");
        }
        if (getCompressionTypes() != null && getCompressionType() == null) {
            throw new IllegalStateException("No compression type set!");
        }
        return null;
    }

    public float[] getCompressionQualityValues() {
        if (!canWriteCompressed()) {
            throw new UnsupportedOperationException("Compression not supported.");
        }
        if (getCompressionMode() != 2) {
            throw new IllegalStateException("Compression mode not MODE_EXPLICIT!");
        }
        if (getCompressionTypes() != null && getCompressionType() == null) {
            throw new IllegalStateException("No compression type set!");
        }
        return null;
    }
}
