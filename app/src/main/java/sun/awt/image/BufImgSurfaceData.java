package sun.awt.image;

import java.awt.GraphicsConfiguration;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.DirectColorModel;
import java.awt.image.IndexColorModel;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import sun.java2d.SunGraphics2D;
import sun.java2d.SurfaceData;
import sun.java2d.loops.CompositeType;
import sun.java2d.loops.RenderLoops;
import sun.java2d.loops.SurfaceType;

/* loaded from: rt.jar:sun/awt/image/BufImgSurfaceData.class */
public class BufImgSurfaceData extends SurfaceData {
    BufferedImage bufImg;
    private BufferedImageGraphicsConfig graphicsConfig;
    RenderLoops solidloops;
    private static final int DCM_RGBX_RED_MASK = -16777216;
    private static final int DCM_RGBX_GREEN_MASK = 16711680;
    private static final int DCM_RGBX_BLUE_MASK = 65280;
    private static final int DCM_555X_RED_MASK = 63488;
    private static final int DCM_555X_GREEN_MASK = 1984;
    private static final int DCM_555X_BLUE_MASK = 62;
    private static final int DCM_4444_RED_MASK = 3840;
    private static final int DCM_4444_GREEN_MASK = 240;
    private static final int DCM_4444_BLUE_MASK = 15;
    private static final int DCM_4444_ALPHA_MASK = 61440;
    private static final int DCM_ARGBBM_ALPHA_MASK = 16777216;
    private static final int DCM_ARGBBM_RED_MASK = 16711680;
    private static final int DCM_ARGBBM_GREEN_MASK = 65280;
    private static final int DCM_ARGBBM_BLUE_MASK = 255;
    private static final int CACHE_SIZE = 5;
    private static RenderLoops[] loopcache;
    private static SurfaceType[] typecache;

    private static native void initIDs(Class cls, Class cls2);

    protected native void initRaster(Object obj, int i2, int i3, int i4, int i5, int i6, int i7, IndexColorModel indexColorModel);

    static {
        initIDs(IndexColorModel.class, ICMColorData.class);
        loopcache = new RenderLoops[5];
        typecache = new SurfaceType[5];
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v101, types: [sun.java2d.SurfaceData] */
    /* JADX WARN: Type inference failed for: r0v104, types: [sun.java2d.SurfaceData] */
    /* JADX WARN: Type inference failed for: r0v106, types: [sun.java2d.SurfaceData] */
    /* JADX WARN: Type inference failed for: r0v113, types: [sun.java2d.SurfaceData] */
    /* JADX WARN: Type inference failed for: r0v120, types: [sun.java2d.SurfaceData] */
    /* JADX WARN: Type inference failed for: r0v122, types: [sun.java2d.SurfaceData] */
    /* JADX WARN: Type inference failed for: r0v124, types: [sun.java2d.SurfaceData] */
    /* JADX WARN: Type inference failed for: r0v126, types: [sun.java2d.SurfaceData] */
    /* JADX WARN: Type inference failed for: r0v128, types: [sun.java2d.SurfaceData] */
    /* JADX WARN: Type inference failed for: r0v130, types: [sun.java2d.SurfaceData] */
    /* JADX WARN: Type inference failed for: r0v132, types: [sun.java2d.SurfaceData] */
    /* JADX WARN: Type inference failed for: r0v134, types: [sun.java2d.SurfaceData] */
    /* JADX WARN: Type inference failed for: r0v139, types: [sun.java2d.SurfaceData] */
    /* JADX WARN: Type inference failed for: r0v36, types: [sun.java2d.SurfaceData] */
    /* JADX WARN: Type inference failed for: r0v68, types: [sun.java2d.SurfaceData] */
    public static SurfaceData createData(BufferedImage bufferedImage) {
        BufImgSurfaceData bufImgSurfaceData;
        SurfaceType surfaceType;
        SurfaceType surfaceType2;
        if (bufferedImage == null) {
            throw new NullPointerException("BufferedImage cannot be null");
        }
        ColorModel colorModel = bufferedImage.getColorModel();
        switch (bufferedImage.getType()) {
            case 0:
            default:
                WritableRaster raster = bufferedImage.getRaster();
                int numBands = raster.getNumBands();
                if ((raster instanceof IntegerComponentRaster) && raster.getNumDataElements() == 1 && ((IntegerComponentRaster) raster).getPixelStride() == 1) {
                    SurfaceType surfaceType3 = SurfaceType.AnyInt;
                    if (colorModel instanceof DirectColorModel) {
                        DirectColorModel directColorModel = (DirectColorModel) colorModel;
                        int alphaMask = directColorModel.getAlphaMask();
                        int redMask = directColorModel.getRedMask();
                        int greenMask = directColorModel.getGreenMask();
                        int blueMask = directColorModel.getBlueMask();
                        if (numBands == 3 && alphaMask == 0 && redMask == -16777216 && greenMask == 16711680 && blueMask == 65280) {
                            surfaceType3 = SurfaceType.IntRgbx;
                        } else if (numBands == 4 && alphaMask == 16777216 && redMask == 16711680 && greenMask == 65280 && blueMask == 255) {
                            surfaceType3 = SurfaceType.IntArgbBm;
                        } else {
                            surfaceType3 = SurfaceType.AnyDcm;
                        }
                    }
                    bufImgSurfaceData = createDataIC(bufferedImage, surfaceType3);
                    break;
                } else if ((raster instanceof ShortComponentRaster) && raster.getNumDataElements() == 1 && ((ShortComponentRaster) raster).getPixelStride() == 1) {
                    SurfaceType surfaceType4 = SurfaceType.AnyShort;
                    IndexColorModel indexColorModel = null;
                    if (colorModel instanceof DirectColorModel) {
                        DirectColorModel directColorModel2 = (DirectColorModel) colorModel;
                        int alphaMask2 = directColorModel2.getAlphaMask();
                        int redMask2 = directColorModel2.getRedMask();
                        int greenMask2 = directColorModel2.getGreenMask();
                        int blueMask2 = directColorModel2.getBlueMask();
                        if (numBands == 3 && alphaMask2 == 0 && redMask2 == DCM_555X_RED_MASK && greenMask2 == DCM_555X_GREEN_MASK && blueMask2 == 62) {
                            surfaceType4 = SurfaceType.Ushort555Rgbx;
                        } else if (numBands == 4 && alphaMask2 == 61440 && redMask2 == 3840 && greenMask2 == 240 && blueMask2 == 15) {
                            surfaceType4 = SurfaceType.Ushort4444Argb;
                        }
                    } else if (colorModel instanceof IndexColorModel) {
                        indexColorModel = (IndexColorModel) colorModel;
                        if (indexColorModel.getPixelSize() == 12) {
                            surfaceType4 = isOpaqueGray(indexColorModel) ? SurfaceType.Index12Gray : SurfaceType.UshortIndexed;
                        } else {
                            indexColorModel = null;
                        }
                    }
                    bufImgSurfaceData = createDataSC(bufferedImage, surfaceType4, indexColorModel);
                    break;
                } else {
                    bufImgSurfaceData = new BufImgSurfaceData(raster.getDataBuffer(), bufferedImage, SurfaceType.Custom);
                    break;
                }
                break;
            case 1:
                bufImgSurfaceData = createDataIC(bufferedImage, SurfaceType.IntRgb);
                break;
            case 2:
                bufImgSurfaceData = createDataIC(bufferedImage, SurfaceType.IntArgb);
                break;
            case 3:
                bufImgSurfaceData = createDataIC(bufferedImage, SurfaceType.IntArgbPre);
                break;
            case 4:
                bufImgSurfaceData = createDataIC(bufferedImage, SurfaceType.IntBgr);
                break;
            case 5:
                bufImgSurfaceData = createDataBC(bufferedImage, SurfaceType.ThreeByteBgr, 2);
                break;
            case 6:
                bufImgSurfaceData = createDataBC(bufferedImage, SurfaceType.FourByteAbgr, 3);
                break;
            case 7:
                bufImgSurfaceData = createDataBC(bufferedImage, SurfaceType.FourByteAbgrPre, 3);
                break;
            case 8:
                bufImgSurfaceData = createDataSC(bufferedImage, SurfaceType.Ushort565Rgb, null);
                break;
            case 9:
                bufImgSurfaceData = createDataSC(bufferedImage, SurfaceType.Ushort555Rgb, null);
                break;
            case 10:
                bufImgSurfaceData = createDataBC(bufferedImage, SurfaceType.ByteGray, 0);
                break;
            case 11:
                bufImgSurfaceData = createDataSC(bufferedImage, SurfaceType.UshortGray, null);
                break;
            case 12:
                switch (bufferedImage.getRaster().getSampleModel().getSampleSize(0)) {
                    case 1:
                        surfaceType = SurfaceType.ByteBinary1Bit;
                        break;
                    case 2:
                        surfaceType = SurfaceType.ByteBinary2Bit;
                        break;
                    case 3:
                    default:
                        throw new InternalError("Unrecognized pixel size");
                    case 4:
                        surfaceType = SurfaceType.ByteBinary4Bit;
                        break;
                }
                bufImgSurfaceData = createDataBP(bufferedImage, surfaceType);
                break;
            case 13:
                switch (colorModel.getTransparency()) {
                    case 1:
                        if (isOpaqueGray((IndexColorModel) colorModel)) {
                            surfaceType2 = SurfaceType.Index8Gray;
                            break;
                        } else {
                            surfaceType2 = SurfaceType.ByteIndexedOpaque;
                            break;
                        }
                    case 2:
                        surfaceType2 = SurfaceType.ByteIndexedBm;
                        break;
                    case 3:
                        surfaceType2 = SurfaceType.ByteIndexed;
                        break;
                    default:
                        throw new InternalError("Unrecognized transparency");
                }
                bufImgSurfaceData = createDataBC(bufferedImage, surfaceType2, 0);
                break;
        }
        bufImgSurfaceData.initSolidLoops();
        return bufImgSurfaceData;
    }

    public static SurfaceData createData(Raster raster, ColorModel colorModel) {
        throw new InternalError("SurfaceData not implemented for Raster/CM");
    }

    public static SurfaceData createDataIC(BufferedImage bufferedImage, SurfaceType surfaceType) {
        IntegerComponentRaster integerComponentRaster = (IntegerComponentRaster) bufferedImage.getRaster();
        BufImgSurfaceData bufImgSurfaceData = new BufImgSurfaceData(integerComponentRaster.getDataBuffer(), bufferedImage, surfaceType);
        bufImgSurfaceData.initRaster(integerComponentRaster.getDataStorage(), integerComponentRaster.getDataOffset(0) * 4, 0, integerComponentRaster.getWidth(), integerComponentRaster.getHeight(), integerComponentRaster.getPixelStride() * 4, integerComponentRaster.getScanlineStride() * 4, null);
        return bufImgSurfaceData;
    }

    public static SurfaceData createDataSC(BufferedImage bufferedImage, SurfaceType surfaceType, IndexColorModel indexColorModel) {
        ShortComponentRaster shortComponentRaster = (ShortComponentRaster) bufferedImage.getRaster();
        BufImgSurfaceData bufImgSurfaceData = new BufImgSurfaceData(shortComponentRaster.getDataBuffer(), bufferedImage, surfaceType);
        bufImgSurfaceData.initRaster(shortComponentRaster.getDataStorage(), shortComponentRaster.getDataOffset(0) * 2, 0, shortComponentRaster.getWidth(), shortComponentRaster.getHeight(), shortComponentRaster.getPixelStride() * 2, shortComponentRaster.getScanlineStride() * 2, indexColorModel);
        return bufImgSurfaceData;
    }

    public static SurfaceData createDataBC(BufferedImage bufferedImage, SurfaceType surfaceType, int i2) {
        ByteComponentRaster byteComponentRaster = (ByteComponentRaster) bufferedImage.getRaster();
        BufImgSurfaceData bufImgSurfaceData = new BufImgSurfaceData(byteComponentRaster.getDataBuffer(), bufferedImage, surfaceType);
        ColorModel colorModel = bufferedImage.getColorModel();
        bufImgSurfaceData.initRaster(byteComponentRaster.getDataStorage(), byteComponentRaster.getDataOffset(i2), 0, byteComponentRaster.getWidth(), byteComponentRaster.getHeight(), byteComponentRaster.getPixelStride(), byteComponentRaster.getScanlineStride(), colorModel instanceof IndexColorModel ? (IndexColorModel) colorModel : null);
        return bufImgSurfaceData;
    }

    public static SurfaceData createDataBP(BufferedImage bufferedImage, SurfaceType surfaceType) {
        BytePackedRaster bytePackedRaster = (BytePackedRaster) bufferedImage.getRaster();
        BufImgSurfaceData bufImgSurfaceData = new BufImgSurfaceData(bytePackedRaster.getDataBuffer(), bufferedImage, surfaceType);
        ColorModel colorModel = bufferedImage.getColorModel();
        bufImgSurfaceData.initRaster(bytePackedRaster.getDataStorage(), bytePackedRaster.getDataBitOffset() / 8, bytePackedRaster.getDataBitOffset() & 7, bytePackedRaster.getWidth(), bytePackedRaster.getHeight(), 0, bytePackedRaster.getScanlineStride(), colorModel instanceof IndexColorModel ? (IndexColorModel) colorModel : null);
        return bufImgSurfaceData;
    }

    @Override // sun.java2d.SurfaceData
    public RenderLoops getRenderLoops(SunGraphics2D sunGraphics2D) {
        if (sunGraphics2D.paintState <= 1 && sunGraphics2D.compositeState <= 0) {
            return this.solidloops;
        }
        return super.getRenderLoops(sunGraphics2D);
    }

    @Override // sun.java2d.SurfaceData
    public Raster getRaster(int i2, int i3, int i4, int i5) {
        return this.bufImg.getRaster();
    }

    public BufImgSurfaceData(DataBuffer dataBuffer, BufferedImage bufferedImage, SurfaceType surfaceType) {
        super(SunWritableRaster.stealTrackable(dataBuffer), surfaceType, bufferedImage.getColorModel());
        this.bufImg = bufferedImage;
    }

    protected BufImgSurfaceData(SurfaceType surfaceType, ColorModel colorModel) {
        super(surfaceType, colorModel);
    }

    public void initSolidLoops() {
        this.solidloops = getSolidLoops(getSurfaceType());
    }

    public static synchronized RenderLoops getSolidLoops(SurfaceType surfaceType) {
        for (int i2 = 4; i2 >= 0; i2--) {
            SurfaceType surfaceType2 = typecache[i2];
            if (surfaceType2 == surfaceType) {
                return loopcache[i2];
            }
            if (surfaceType2 == null) {
                break;
            }
        }
        RenderLoops renderLoopsMakeRenderLoops = makeRenderLoops(SurfaceType.OpaqueColor, CompositeType.SrcNoEa, surfaceType);
        System.arraycopy(loopcache, 1, loopcache, 0, 4);
        System.arraycopy(typecache, 1, typecache, 0, 4);
        loopcache[4] = renderLoopsMakeRenderLoops;
        typecache[4] = surfaceType;
        return renderLoopsMakeRenderLoops;
    }

    @Override // sun.java2d.SurfaceData
    public SurfaceData getReplacement() {
        return restoreContents(this.bufImg);
    }

    @Override // sun.java2d.SurfaceData
    public synchronized GraphicsConfiguration getDeviceConfiguration() {
        if (this.graphicsConfig == null) {
            this.graphicsConfig = BufferedImageGraphicsConfig.getConfig(this.bufImg);
        }
        return this.graphicsConfig;
    }

    @Override // sun.java2d.SurfaceData
    public Rectangle getBounds() {
        return new Rectangle(this.bufImg.getWidth(), this.bufImg.getHeight());
    }

    @Override // sun.java2d.SurfaceData
    protected void checkCustomComposite() {
    }

    @Override // sun.java2d.SurfaceData
    public Object getDestination() {
        return this.bufImg;
    }

    /* loaded from: rt.jar:sun/awt/image/BufImgSurfaceData$ICMColorData.class */
    public static final class ICMColorData {
        private long pData;

        private ICMColorData(long j2) {
            this.pData = 0L;
            this.pData = j2;
        }
    }
}
