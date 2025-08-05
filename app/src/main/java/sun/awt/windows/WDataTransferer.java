package sun.awt.windows;

import com.sun.istack.internal.localization.Localizable;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.color.ColorSpace;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.FlavorTable;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.ComponentColorModel;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferInt;
import java.awt.image.DirectColorModel;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.SortedMap;
import sun.awt.datatransfer.DataTransferer;
import sun.awt.datatransfer.ToolkitThreadBlockedHandler;
import sun.awt.image.ImageRepresentation;
import sun.awt.image.ToolkitImage;
import sun.text.normalizer.NormalizerImpl;

/* loaded from: rt.jar:sun/awt/windows/WDataTransferer.class */
final class WDataTransferer extends DataTransferer {
    private static final String[] predefinedClipboardNames = {"", "TEXT", "BITMAP", "METAFILEPICT", "SYLK", "DIF", "TIFF", "OEM TEXT", "DIB", "PALETTE", "PENDATA", "RIFF", "WAVE", "UNICODE TEXT", "ENHMETAFILE", "HDROP", "LOCALE", "DIBV5"};
    private static final Map<String, Long> predefinedClipboardNameMap;
    public static final int CF_TEXT = 1;
    public static final int CF_METAFILEPICT = 3;
    public static final int CF_DIB = 8;
    public static final int CF_ENHMETAFILE = 14;
    public static final int CF_HDROP = 15;
    public static final int CF_LOCALE = 16;
    public static final long CF_HTML;
    public static final long CFSTR_INETURL;
    public static final long CF_PNG;
    public static final long CF_JFIF;
    public static final long CF_FILEGROUPDESCRIPTORW;
    public static final long CF_FILEGROUPDESCRIPTORA;
    private static final Long L_CF_LOCALE;
    private static final DirectColorModel directColorModel;
    private static final int[] bandmasks;
    private static WDataTransferer transferer;
    private final ToolkitThreadBlockedHandler handler = new WToolkitThreadBlockedHandler();
    private static final byte[] UNICODE_NULL_TERMINATOR;

    private static native long registerClipboardFormat(String str);

    private static native String getClipboardFormatName(long j2);

    private native byte[] imageDataToPlatformImageBytes(byte[] bArr, int i2, int i3, long j2);

    private native int[] platformImageBytesToImageData(byte[] bArr, long j2) throws IOException;

    @Override // sun.awt.datatransfer.DataTransferer
    protected native String[] dragQueryFile(byte[] bArr);

    static {
        HashMap map = new HashMap(predefinedClipboardNames.length, 1.0f);
        for (int i2 = 1; i2 < predefinedClipboardNames.length; i2++) {
            map.put(predefinedClipboardNames[i2], Long.valueOf(i2));
        }
        predefinedClipboardNameMap = Collections.synchronizedMap(map);
        CF_HTML = registerClipboardFormat("HTML Format");
        CFSTR_INETURL = registerClipboardFormat("UniformResourceLocator");
        CF_PNG = registerClipboardFormat("PNG");
        CF_JFIF = registerClipboardFormat("JFIF");
        CF_FILEGROUPDESCRIPTORW = registerClipboardFormat("FileGroupDescriptorW");
        CF_FILEGROUPDESCRIPTORA = registerClipboardFormat("FileGroupDescriptor");
        L_CF_LOCALE = predefinedClipboardNameMap.get(predefinedClipboardNames[16]);
        directColorModel = new DirectColorModel(24, 16711680, NormalizerImpl.CC_MASK, 255);
        bandmasks = new int[]{directColorModel.getRedMask(), directColorModel.getGreenMask(), directColorModel.getBlueMask()};
        UNICODE_NULL_TERMINATOR = new byte[]{0, 0};
    }

    private WDataTransferer() {
    }

    static synchronized WDataTransferer getInstanceImpl() {
        if (transferer == null) {
            transferer = new WDataTransferer();
        }
        return transferer;
    }

    @Override // sun.awt.datatransfer.DataTransferer
    public SortedMap<Long, DataFlavor> getFormatsForFlavors(DataFlavor[] dataFlavorArr, FlavorTable flavorTable) {
        SortedMap<Long, DataFlavor> formatsForFlavors = super.getFormatsForFlavors(dataFlavorArr, flavorTable);
        formatsForFlavors.remove(L_CF_LOCALE);
        return formatsForFlavors;
    }

    @Override // sun.awt.datatransfer.DataTransferer
    public String getDefaultUnicodeEncoding() {
        return "utf-16le";
    }

    @Override // sun.awt.datatransfer.DataTransferer
    public byte[] translateTransferable(Transferable transferable, DataFlavor dataFlavor, long j2) throws IOException {
        byte[] bArrTranslateTransferable;
        if (j2 == CF_HTML) {
            if (transferable.isDataFlavorSupported(DataFlavor.selectionHtmlFlavor)) {
                bArrTranslateTransferable = super.translateTransferable(transferable, DataFlavor.selectionHtmlFlavor, j2);
            } else if (transferable.isDataFlavorSupported(DataFlavor.allHtmlFlavor)) {
                bArrTranslateTransferable = super.translateTransferable(transferable, DataFlavor.allHtmlFlavor, j2);
            } else {
                bArrTranslateTransferable = HTMLCodec.convertToHTMLFormat(super.translateTransferable(transferable, dataFlavor, j2));
            }
        } else {
            bArrTranslateTransferable = super.translateTransferable(transferable, dataFlavor, j2);
        }
        return bArrTranslateTransferable;
    }

    @Override // sun.awt.datatransfer.DataTransferer
    public Object translateStream(InputStream inputStream, DataFlavor dataFlavor, long j2, Transferable transferable) throws IOException {
        if (j2 == CF_HTML && dataFlavor.isFlavorTextType()) {
            inputStream = new HTMLCodec(inputStream, EHTMLReadMode.getEHTMLReadMode(dataFlavor));
        }
        return super.translateStream(inputStream, dataFlavor, j2, transferable);
    }

    @Override // sun.awt.datatransfer.DataTransferer
    public Object translateBytes(byte[] bArr, DataFlavor dataFlavor, long j2, Transferable transferable) throws IOException {
        if (j2 == CF_FILEGROUPDESCRIPTORA || j2 == CF_FILEGROUPDESCRIPTORW) {
            if (bArr == null || !DataFlavor.javaFileListFlavor.equals(dataFlavor)) {
                throw new IOException("data translation failed");
            }
            String[] strArrSplit = new String(bArr, 0, bArr.length, "UTF-16LE").split(Localizable.NOT_LOCALIZABLE);
            if (0 == strArrSplit.length) {
                return null;
            }
            File[] fileArr = new File[strArrSplit.length];
            for (int i2 = 0; i2 < strArrSplit.length; i2++) {
                fileArr[i2] = new File(strArrSplit[i2]);
                fileArr[i2].deleteOnExit();
            }
            return Arrays.asList(fileArr);
        }
        if (j2 == CFSTR_INETURL && URL.class.equals(dataFlavor.getRepresentationClass())) {
            String defaultTextCharset = getDefaultTextCharset();
            if (transferable != null && transferable.isDataFlavorSupported(javaTextEncodingFlavor)) {
                try {
                    defaultTextCharset = new String((byte[]) transferable.getTransferData(javaTextEncodingFlavor), "UTF-8");
                } catch (UnsupportedFlavorException e2) {
                }
            }
            return new URL(new String(bArr, defaultTextCharset));
        }
        return super.translateBytes(bArr, dataFlavor, j2, transferable);
    }

    @Override // sun.awt.datatransfer.DataTransferer
    public boolean isLocaleDependentTextFormat(long j2) {
        return j2 == 1 || j2 == CFSTR_INETURL;
    }

    @Override // sun.awt.datatransfer.DataTransferer
    public boolean isFileFormat(long j2) {
        return j2 == 15 || j2 == CF_FILEGROUPDESCRIPTORA || j2 == CF_FILEGROUPDESCRIPTORW;
    }

    @Override // sun.awt.datatransfer.DataTransferer
    protected Long getFormatForNativeAsLong(String str) {
        Long lValueOf = predefinedClipboardNameMap.get(str);
        if (lValueOf == null) {
            lValueOf = Long.valueOf(registerClipboardFormat(str));
        }
        return lValueOf;
    }

    @Override // sun.awt.datatransfer.DataTransferer
    protected String getNativeForFormat(long j2) {
        return j2 < ((long) predefinedClipboardNames.length) ? predefinedClipboardNames[(int) j2] : getClipboardFormatName(j2);
    }

    @Override // sun.awt.datatransfer.DataTransferer
    public ToolkitThreadBlockedHandler getToolkitThreadBlockedHandler() {
        return this.handler;
    }

    @Override // sun.awt.datatransfer.DataTransferer
    public boolean isImageFormat(long j2) {
        return j2 == 8 || j2 == 14 || j2 == 3 || j2 == CF_PNG || j2 == CF_JFIF;
    }

    @Override // sun.awt.datatransfer.DataTransferer
    protected byte[] imageToPlatformBytes(Image image, long j2) throws IOException {
        int width;
        int height;
        String str = null;
        if (j2 == CF_PNG) {
            str = "image/png";
        } else if (j2 == CF_JFIF) {
            str = "image/jpeg";
        }
        if (str != null) {
            return imageToStandardBytes(image, str);
        }
        if (image instanceof ToolkitImage) {
            ImageRepresentation imageRep = ((ToolkitImage) image).getImageRep();
            imageRep.reconstruct(32);
            width = imageRep.getWidth();
            height = imageRep.getHeight();
        } else {
            width = image.getWidth(null);
            height = image.getHeight(null);
        }
        int i2 = (width * 3) % 4;
        int i3 = i2 > 0 ? 4 - i2 : 0;
        ComponentColorModel componentColorModel = new ComponentColorModel(ColorSpace.getInstance(1000), new int[]{8, 8, 8}, false, false, 1, 0);
        WritableRaster writableRasterCreateInterleavedRaster = Raster.createInterleavedRaster(0, width, height, (width * 3) + i3, 3, new int[]{2, 1, 0}, (Point) null);
        BufferedImage bufferedImage = new BufferedImage((ColorModel) componentColorModel, writableRasterCreateInterleavedRaster, false, (Hashtable<?, ?>) null);
        AffineTransform affineTransform = new AffineTransform(1.0f, 0.0f, 0.0f, -1.0f, 0.0f, height);
        Graphics2D graphics2DCreateGraphics = bufferedImage.createGraphics();
        try {
            graphics2DCreateGraphics.drawImage(image, affineTransform, null);
            graphics2DCreateGraphics.dispose();
            return imageDataToPlatformImageBytes(((DataBufferByte) writableRasterCreateInterleavedRaster.getDataBuffer()).getData(), width, height, j2);
        } catch (Throwable th) {
            graphics2DCreateGraphics.dispose();
            throw th;
        }
    }

    @Override // sun.awt.datatransfer.DataTransferer
    protected ByteArrayOutputStream convertFileListToBytes(ArrayList<String> arrayList) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        if (arrayList.isEmpty()) {
            byteArrayOutputStream.write(UNICODE_NULL_TERMINATOR);
        } else {
            for (int i2 = 0; i2 < arrayList.size(); i2++) {
                byte[] bytes = arrayList.get(i2).getBytes(getDefaultUnicodeEncoding());
                byteArrayOutputStream.write(bytes, 0, bytes.length);
                byteArrayOutputStream.write(UNICODE_NULL_TERMINATOR);
            }
        }
        byteArrayOutputStream.write(UNICODE_NULL_TERMINATOR);
        return byteArrayOutputStream;
    }

    @Override // sun.awt.datatransfer.DataTransferer
    protected Image platformImageBytesToImage(byte[] bArr, long j2) throws IOException {
        String str = null;
        if (j2 == CF_PNG) {
            str = "image/png";
        } else if (j2 == CF_JFIF) {
            str = "image/jpeg";
        }
        if (str != null) {
            return standardImageBytesToImage(bArr, str);
        }
        int[] iArrPlatformImageBytesToImageData = platformImageBytesToImageData(bArr, j2);
        if (iArrPlatformImageBytesToImageData == null) {
            throw new IOException("data translation failed");
        }
        int length = iArrPlatformImageBytesToImageData.length - 2;
        int i2 = iArrPlatformImageBytesToImageData[length];
        return new BufferedImage((ColorModel) directColorModel, Raster.createPackedRaster(new DataBufferInt(iArrPlatformImageBytesToImageData, length), i2, iArrPlatformImageBytesToImageData[length + 1], i2, bandmasks, (Point) null), false, (Hashtable<?, ?>) null);
    }
}
