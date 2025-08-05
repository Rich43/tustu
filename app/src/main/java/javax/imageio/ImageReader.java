package javax.imageio;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.awt.image.RenderedImage;
import java.io.IOException;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.Set;
import javax.imageio.event.IIOReadProgressListener;
import javax.imageio.event.IIOReadUpdateListener;
import javax.imageio.event.IIOReadWarningListener;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.metadata.IIOMetadataFormatImpl;
import javax.imageio.spi.ImageReaderSpi;
import javax.imageio.stream.ImageInputStream;

/* loaded from: rt.jar:javax/imageio/ImageReader.class */
public abstract class ImageReader {
    protected ImageReaderSpi originatingProvider;
    protected Object input = null;
    protected boolean seekForwardOnly = false;
    protected boolean ignoreMetadata = false;
    protected int minIndex = 0;
    protected Locale[] availableLocales = null;
    protected Locale locale = null;
    protected List<IIOReadWarningListener> warningListeners = null;
    protected List<Locale> warningLocales = null;
    protected List<IIOReadProgressListener> progressListeners = null;
    protected List<IIOReadUpdateListener> updateListeners = null;
    private boolean abortFlag = false;

    public abstract int getNumImages(boolean z2) throws IOException;

    public abstract int getWidth(int i2) throws IOException;

    public abstract int getHeight(int i2) throws IOException;

    public abstract Iterator<ImageTypeSpecifier> getImageTypes(int i2) throws IOException;

    public abstract IIOMetadata getStreamMetadata() throws IOException;

    public abstract IIOMetadata getImageMetadata(int i2) throws IOException;

    public abstract BufferedImage read(int i2, ImageReadParam imageReadParam) throws IOException;

    protected ImageReader(ImageReaderSpi imageReaderSpi) {
        this.originatingProvider = imageReaderSpi;
    }

    public String getFormatName() throws IOException {
        return this.originatingProvider.getFormatNames()[0];
    }

    public ImageReaderSpi getOriginatingProvider() {
        return this.originatingProvider;
    }

    public void setInput(Object obj, boolean z2, boolean z3) {
        if (obj != null) {
            boolean z4 = false;
            if (this.originatingProvider != null) {
                Class[] inputTypes = this.originatingProvider.getInputTypes();
                int i2 = 0;
                while (true) {
                    if (i2 >= inputTypes.length) {
                        break;
                    }
                    if (!inputTypes[i2].isInstance(obj)) {
                        i2++;
                    } else {
                        z4 = true;
                        break;
                    }
                }
            } else if (obj instanceof ImageInputStream) {
                z4 = true;
            }
            if (!z4) {
                throw new IllegalArgumentException("Incorrect input type!");
            }
            this.seekForwardOnly = z2;
            this.ignoreMetadata = z3;
            this.minIndex = 0;
        }
        this.input = obj;
    }

    public void setInput(Object obj, boolean z2) {
        setInput(obj, z2, false);
    }

    public void setInput(Object obj) {
        setInput(obj, false, false);
    }

    public Object getInput() {
        return this.input;
    }

    public boolean isSeekForwardOnly() {
        return this.seekForwardOnly;
    }

    public boolean isIgnoringMetadata() {
        return this.ignoreMetadata;
    }

    public int getMinIndex() {
        return this.minIndex;
    }

    public Locale[] getAvailableLocales() {
        if (this.availableLocales == null) {
            return null;
        }
        return (Locale[]) this.availableLocales.clone();
    }

    public void setLocale(Locale locale) {
        if (locale != null) {
            Locale[] availableLocales = getAvailableLocales();
            boolean z2 = false;
            if (availableLocales != null) {
                int i2 = 0;
                while (true) {
                    if (i2 >= availableLocales.length) {
                        break;
                    }
                    if (!locale.equals(availableLocales[i2])) {
                        i2++;
                    } else {
                        z2 = true;
                        break;
                    }
                }
            }
            if (!z2) {
                throw new IllegalArgumentException("Invalid locale!");
            }
        }
        this.locale = locale;
    }

    public Locale getLocale() {
        return this.locale;
    }

    public boolean isRandomAccessEasy(int i2) throws IOException {
        return false;
    }

    public float getAspectRatio(int i2) throws IOException {
        return getWidth(i2) / getHeight(i2);
    }

    public ImageTypeSpecifier getRawImageType(int i2) throws IOException {
        return getImageTypes(i2).next();
    }

    public ImageReadParam getDefaultReadParam() {
        return new ImageReadParam();
    }

    public IIOMetadata getStreamMetadata(String str, Set<String> set) throws IOException {
        return getMetadata(str, set, true, 0);
    }

    private IIOMetadata getMetadata(String str, Set set, boolean z2, int i2) throws IOException {
        IIOMetadata imageMetadata;
        if (str == null) {
            throw new IllegalArgumentException("formatName == null!");
        }
        if (set == null) {
            throw new IllegalArgumentException("nodeNames == null!");
        }
        if (z2) {
            imageMetadata = getStreamMetadata();
        } else {
            imageMetadata = getImageMetadata(i2);
        }
        IIOMetadata iIOMetadata = imageMetadata;
        if (iIOMetadata != null) {
            if (iIOMetadata.isStandardMetadataFormatSupported() && str.equals(IIOMetadataFormatImpl.standardMetadataFormatName)) {
                return iIOMetadata;
            }
            String nativeMetadataFormatName = iIOMetadata.getNativeMetadataFormatName();
            if (nativeMetadataFormatName != null && str.equals(nativeMetadataFormatName)) {
                return iIOMetadata;
            }
            String[] extraMetadataFormatNames = iIOMetadata.getExtraMetadataFormatNames();
            if (extraMetadataFormatNames != null) {
                for (String str2 : extraMetadataFormatNames) {
                    if (str.equals(str2)) {
                        return iIOMetadata;
                    }
                }
                return null;
            }
            return null;
        }
        return null;
    }

    public IIOMetadata getImageMetadata(int i2, String str, Set<String> set) throws IOException {
        return getMetadata(str, set, false, i2);
    }

    public BufferedImage read(int i2) throws IOException {
        return read(i2, null);
    }

    public IIOImage readAll(int i2, ImageReadParam imageReadParam) throws IOException {
        if (i2 < getMinIndex()) {
            throw new IndexOutOfBoundsException("imageIndex < getMinIndex()!");
        }
        BufferedImage bufferedImage = read(i2, imageReadParam);
        ArrayList arrayList = null;
        int numThumbnails = getNumThumbnails(i2);
        if (numThumbnails > 0) {
            arrayList = new ArrayList();
            for (int i3 = 0; i3 < numThumbnails; i3++) {
                arrayList.add(readThumbnail(i2, i3));
            }
        }
        return new IIOImage(bufferedImage, arrayList, getImageMetadata(i2));
    }

    public Iterator<IIOImage> readAll(Iterator<? extends ImageReadParam> it) throws IOException {
        ImageReadParam next;
        ArrayList arrayList = new ArrayList();
        int minIndex = getMinIndex();
        processSequenceStarted(minIndex);
        while (true) {
            ImageReadParam imageReadParam = null;
            if (it != null && it.hasNext() && (next = it.next()) != null) {
                if (next instanceof ImageReadParam) {
                    imageReadParam = next;
                } else {
                    throw new IllegalArgumentException("Non-ImageReadParam supplied as part of params!");
                }
            }
            try {
                BufferedImage bufferedImage = read(minIndex, imageReadParam);
                ArrayList arrayList2 = null;
                int numThumbnails = getNumThumbnails(minIndex);
                if (numThumbnails > 0) {
                    arrayList2 = new ArrayList();
                    for (int i2 = 0; i2 < numThumbnails; i2++) {
                        arrayList2.add(readThumbnail(minIndex, i2));
                    }
                }
                arrayList.add(new IIOImage(bufferedImage, arrayList2, getImageMetadata(minIndex)));
                minIndex++;
            } catch (IndexOutOfBoundsException e2) {
                processSequenceComplete();
                return arrayList.iterator();
            }
        }
    }

    public boolean canReadRaster() {
        return false;
    }

    public Raster readRaster(int i2, ImageReadParam imageReadParam) throws IOException {
        throw new UnsupportedOperationException("readRaster not supported!");
    }

    public boolean isImageTiled(int i2) throws IOException {
        return false;
    }

    public int getTileWidth(int i2) throws IOException {
        return getWidth(i2);
    }

    public int getTileHeight(int i2) throws IOException {
        return getHeight(i2);
    }

    public int getTileGridXOffset(int i2) throws IOException {
        return 0;
    }

    public int getTileGridYOffset(int i2) throws IOException {
        return 0;
    }

    public BufferedImage readTile(int i2, int i3, int i4) throws IOException {
        if (i3 != 0 || i4 != 0) {
            throw new IllegalArgumentException("Invalid tile indices");
        }
        return read(i2);
    }

    public Raster readTileRaster(int i2, int i3, int i4) throws IOException {
        if (!canReadRaster()) {
            throw new UnsupportedOperationException("readTileRaster not supported!");
        }
        if (i3 != 0 || i4 != 0) {
            throw new IllegalArgumentException("Invalid tile indices");
        }
        return readRaster(i2, null);
    }

    public RenderedImage readAsRenderedImage(int i2, ImageReadParam imageReadParam) throws IOException {
        return read(i2, imageReadParam);
    }

    public boolean readerSupportsThumbnails() {
        return false;
    }

    public boolean hasThumbnails(int i2) throws IOException {
        return getNumThumbnails(i2) > 0;
    }

    public int getNumThumbnails(int i2) throws IOException {
        return 0;
    }

    public int getThumbnailWidth(int i2, int i3) throws IOException {
        return readThumbnail(i2, i3).getWidth();
    }

    public int getThumbnailHeight(int i2, int i3) throws IOException {
        return readThumbnail(i2, i3).getHeight();
    }

    public BufferedImage readThumbnail(int i2, int i3) throws IOException {
        throw new UnsupportedOperationException("Thumbnails not supported!");
    }

    public synchronized void abort() {
        this.abortFlag = true;
    }

    protected synchronized boolean abortRequested() {
        return this.abortFlag;
    }

    protected synchronized void clearAbortRequest() {
        this.abortFlag = false;
    }

    static List addToList(List list, Object obj) {
        if (list == null) {
            list = new ArrayList();
        }
        list.add(obj);
        return list;
    }

    static List removeFromList(List list, Object obj) {
        if (list == null) {
            return list;
        }
        list.remove(obj);
        if (list.size() == 0) {
            list = null;
        }
        return list;
    }

    public void addIIOReadWarningListener(IIOReadWarningListener iIOReadWarningListener) {
        if (iIOReadWarningListener == null) {
            return;
        }
        this.warningListeners = addToList(this.warningListeners, iIOReadWarningListener);
        this.warningLocales = addToList(this.warningLocales, getLocale());
    }

    public void removeIIOReadWarningListener(IIOReadWarningListener iIOReadWarningListener) {
        int iIndexOf;
        if (iIOReadWarningListener != null && this.warningListeners != null && (iIndexOf = this.warningListeners.indexOf(iIOReadWarningListener)) != -1) {
            this.warningListeners.remove(iIndexOf);
            this.warningLocales.remove(iIndexOf);
            if (this.warningListeners.size() == 0) {
                this.warningListeners = null;
                this.warningLocales = null;
            }
        }
    }

    public void removeAllIIOReadWarningListeners() {
        this.warningListeners = null;
        this.warningLocales = null;
    }

    public void addIIOReadProgressListener(IIOReadProgressListener iIOReadProgressListener) {
        if (iIOReadProgressListener == null) {
            return;
        }
        this.progressListeners = addToList(this.progressListeners, iIOReadProgressListener);
    }

    public void removeIIOReadProgressListener(IIOReadProgressListener iIOReadProgressListener) {
        if (iIOReadProgressListener == null || this.progressListeners == null) {
            return;
        }
        this.progressListeners = removeFromList(this.progressListeners, iIOReadProgressListener);
    }

    public void removeAllIIOReadProgressListeners() {
        this.progressListeners = null;
    }

    public void addIIOReadUpdateListener(IIOReadUpdateListener iIOReadUpdateListener) {
        if (iIOReadUpdateListener == null) {
            return;
        }
        this.updateListeners = addToList(this.updateListeners, iIOReadUpdateListener);
    }

    public void removeIIOReadUpdateListener(IIOReadUpdateListener iIOReadUpdateListener) {
        if (iIOReadUpdateListener == null || this.updateListeners == null) {
            return;
        }
        this.updateListeners = removeFromList(this.updateListeners, iIOReadUpdateListener);
    }

    public void removeAllIIOReadUpdateListeners() {
        this.updateListeners = null;
    }

    protected void processSequenceStarted(int i2) {
        if (this.progressListeners == null) {
            return;
        }
        int size = this.progressListeners.size();
        for (int i3 = 0; i3 < size; i3++) {
            this.progressListeners.get(i3).sequenceStarted(this, i2);
        }
    }

    protected void processSequenceComplete() {
        if (this.progressListeners == null) {
            return;
        }
        int size = this.progressListeners.size();
        for (int i2 = 0; i2 < size; i2++) {
            this.progressListeners.get(i2).sequenceComplete(this);
        }
    }

    protected void processImageStarted(int i2) {
        if (this.progressListeners == null) {
            return;
        }
        int size = this.progressListeners.size();
        for (int i3 = 0; i3 < size; i3++) {
            this.progressListeners.get(i3).imageStarted(this, i2);
        }
    }

    protected void processImageProgress(float f2) {
        if (this.progressListeners == null) {
            return;
        }
        int size = this.progressListeners.size();
        for (int i2 = 0; i2 < size; i2++) {
            this.progressListeners.get(i2).imageProgress(this, f2);
        }
    }

    protected void processImageComplete() {
        if (this.progressListeners == null) {
            return;
        }
        int size = this.progressListeners.size();
        for (int i2 = 0; i2 < size; i2++) {
            this.progressListeners.get(i2).imageComplete(this);
        }
    }

    protected void processThumbnailStarted(int i2, int i3) {
        if (this.progressListeners == null) {
            return;
        }
        int size = this.progressListeners.size();
        for (int i4 = 0; i4 < size; i4++) {
            this.progressListeners.get(i4).thumbnailStarted(this, i2, i3);
        }
    }

    protected void processThumbnailProgress(float f2) {
        if (this.progressListeners == null) {
            return;
        }
        int size = this.progressListeners.size();
        for (int i2 = 0; i2 < size; i2++) {
            this.progressListeners.get(i2).thumbnailProgress(this, f2);
        }
    }

    protected void processThumbnailComplete() {
        if (this.progressListeners == null) {
            return;
        }
        int size = this.progressListeners.size();
        for (int i2 = 0; i2 < size; i2++) {
            this.progressListeners.get(i2).thumbnailComplete(this);
        }
    }

    protected void processReadAborted() {
        if (this.progressListeners == null) {
            return;
        }
        int size = this.progressListeners.size();
        for (int i2 = 0; i2 < size; i2++) {
            this.progressListeners.get(i2).readAborted(this);
        }
    }

    protected void processPassStarted(BufferedImage bufferedImage, int i2, int i3, int i4, int i5, int i6, int i7, int i8, int[] iArr) {
        if (this.updateListeners == null) {
            return;
        }
        int size = this.updateListeners.size();
        for (int i9 = 0; i9 < size; i9++) {
            this.updateListeners.get(i9).passStarted(this, bufferedImage, i2, i3, i4, i5, i6, i7, i8, iArr);
        }
    }

    protected void processImageUpdate(BufferedImage bufferedImage, int i2, int i3, int i4, int i5, int i6, int i7, int[] iArr) {
        if (this.updateListeners == null) {
            return;
        }
        int size = this.updateListeners.size();
        for (int i8 = 0; i8 < size; i8++) {
            this.updateListeners.get(i8).imageUpdate(this, bufferedImage, i2, i3, i4, i5, i6, i7, iArr);
        }
    }

    protected void processPassComplete(BufferedImage bufferedImage) {
        if (this.updateListeners == null) {
            return;
        }
        int size = this.updateListeners.size();
        for (int i2 = 0; i2 < size; i2++) {
            this.updateListeners.get(i2).passComplete(this, bufferedImage);
        }
    }

    protected void processThumbnailPassStarted(BufferedImage bufferedImage, int i2, int i3, int i4, int i5, int i6, int i7, int i8, int[] iArr) {
        if (this.updateListeners == null) {
            return;
        }
        int size = this.updateListeners.size();
        for (int i9 = 0; i9 < size; i9++) {
            this.updateListeners.get(i9).thumbnailPassStarted(this, bufferedImage, i2, i3, i4, i5, i6, i7, i8, iArr);
        }
    }

    protected void processThumbnailUpdate(BufferedImage bufferedImage, int i2, int i3, int i4, int i5, int i6, int i7, int[] iArr) {
        if (this.updateListeners == null) {
            return;
        }
        int size = this.updateListeners.size();
        for (int i8 = 0; i8 < size; i8++) {
            this.updateListeners.get(i8).thumbnailUpdate(this, bufferedImage, i2, i3, i4, i5, i6, i7, iArr);
        }
    }

    protected void processThumbnailPassComplete(BufferedImage bufferedImage) {
        if (this.updateListeners == null) {
            return;
        }
        int size = this.updateListeners.size();
        for (int i2 = 0; i2 < size; i2++) {
            this.updateListeners.get(i2).thumbnailPassComplete(this, bufferedImage);
        }
    }

    protected void processWarningOccurred(String str) {
        if (this.warningListeners == null) {
            return;
        }
        if (str == null) {
            throw new IllegalArgumentException("warning == null!");
        }
        int size = this.warningListeners.size();
        for (int i2 = 0; i2 < size; i2++) {
            this.warningListeners.get(i2).warningOccurred(this, str);
        }
    }

    protected void processWarningOccurred(String str, String str2) {
        ResourceBundle bundle;
        if (this.warningListeners == null) {
            return;
        }
        if (str == null) {
            throw new IllegalArgumentException("baseName == null!");
        }
        if (str2 == null) {
            throw new IllegalArgumentException("keyword == null!");
        }
        int size = this.warningListeners.size();
        for (int i2 = 0; i2 < size; i2++) {
            IIOReadWarningListener iIOReadWarningListener = this.warningListeners.get(i2);
            Locale locale = this.warningLocales.get(i2);
            if (locale == null) {
                locale = Locale.getDefault();
            }
            try {
                bundle = ResourceBundle.getBundle(str, locale, (ClassLoader) AccessController.doPrivileged(new PrivilegedAction() { // from class: javax.imageio.ImageReader.1
                    @Override // java.security.PrivilegedAction
                    public Object run() {
                        return Thread.currentThread().getContextClassLoader();
                    }
                }));
            } catch (MissingResourceException e2) {
                try {
                    bundle = ResourceBundle.getBundle(str, locale);
                } catch (MissingResourceException e3) {
                    throw new IllegalArgumentException("Bundle not found!");
                }
            }
            try {
                iIOReadWarningListener.warningOccurred(this, bundle.getString(str2));
            } catch (ClassCastException e4) {
                throw new IllegalArgumentException("Resource is not a String!");
            } catch (MissingResourceException e5) {
                throw new IllegalArgumentException("Resource is missing!");
            }
        }
    }

    public void reset() {
        setInput(null, false, false);
        setLocale(null);
        removeAllIIOReadUpdateListeners();
        removeAllIIOReadProgressListeners();
        removeAllIIOReadWarningListeners();
        clearAbortRequest();
    }

    public void dispose() {
    }

    protected static Rectangle getSourceRegion(ImageReadParam imageReadParam, int i2, int i3) {
        Rectangle rectangle = new Rectangle(0, 0, i2, i3);
        if (imageReadParam != null) {
            Rectangle sourceRegion = imageReadParam.getSourceRegion();
            if (sourceRegion != null) {
                rectangle = rectangle.intersection(sourceRegion);
            }
            int subsamplingXOffset = imageReadParam.getSubsamplingXOffset();
            int subsamplingYOffset = imageReadParam.getSubsamplingYOffset();
            rectangle.f12372x += subsamplingXOffset;
            rectangle.f12373y += subsamplingYOffset;
            rectangle.width -= subsamplingXOffset;
            rectangle.height -= subsamplingYOffset;
        }
        return rectangle;
    }

    protected static void computeRegions(ImageReadParam imageReadParam, int i2, int i3, BufferedImage bufferedImage, Rectangle rectangle, Rectangle rectangle2) {
        if (rectangle == null) {
            throw new IllegalArgumentException("srcRegion == null!");
        }
        if (rectangle2 == null) {
            throw new IllegalArgumentException("destRegion == null!");
        }
        rectangle.setBounds(0, 0, i2, i3);
        rectangle2.setBounds(0, 0, i2, i3);
        int sourceXSubsampling = 1;
        int sourceYSubsampling = 1;
        if (imageReadParam != null) {
            Rectangle sourceRegion = imageReadParam.getSourceRegion();
            if (sourceRegion != null) {
                rectangle.setBounds(rectangle.intersection(sourceRegion));
            }
            sourceXSubsampling = imageReadParam.getSourceXSubsampling();
            sourceYSubsampling = imageReadParam.getSourceYSubsampling();
            int subsamplingXOffset = imageReadParam.getSubsamplingXOffset();
            int subsamplingYOffset = imageReadParam.getSubsamplingYOffset();
            rectangle.translate(subsamplingXOffset, subsamplingYOffset);
            rectangle.width -= subsamplingXOffset;
            rectangle.height -= subsamplingYOffset;
            rectangle2.setLocation(imageReadParam.getDestinationOffset());
        }
        if (rectangle2.f12372x < 0) {
            int i4 = (-rectangle2.f12372x) * sourceXSubsampling;
            rectangle.f12372x += i4;
            rectangle.width -= i4;
            rectangle2.f12372x = 0;
        }
        if (rectangle2.f12373y < 0) {
            int i5 = (-rectangle2.f12373y) * sourceYSubsampling;
            rectangle.f12373y += i5;
            rectangle.height -= i5;
            rectangle2.f12373y = 0;
        }
        int i6 = ((rectangle.width + sourceXSubsampling) - 1) / sourceXSubsampling;
        int i7 = ((rectangle.height + sourceYSubsampling) - 1) / sourceYSubsampling;
        rectangle2.width = i6;
        rectangle2.height = i7;
        if (bufferedImage != null) {
            rectangle2.setBounds(rectangle2.intersection(new Rectangle(0, 0, bufferedImage.getWidth(), bufferedImage.getHeight())));
            if (rectangle2.isEmpty()) {
                throw new IllegalArgumentException("Empty destination region!");
            }
            int width = (rectangle2.f12372x + i6) - bufferedImage.getWidth();
            if (width > 0) {
                rectangle.width -= width * sourceXSubsampling;
            }
            int height = (rectangle2.f12373y + i7) - bufferedImage.getHeight();
            if (height > 0) {
                rectangle.height -= height * sourceYSubsampling;
            }
        }
        if (rectangle.isEmpty() || rectangle2.isEmpty()) {
            throw new IllegalArgumentException("Empty region!");
        }
    }

    protected static void checkReadParamBandSettings(ImageReadParam imageReadParam, int i2, int i3) {
        int[] sourceBands = null;
        int[] destinationBands = null;
        if (imageReadParam != null) {
            sourceBands = imageReadParam.getSourceBands();
            destinationBands = imageReadParam.getDestinationBands();
        }
        if ((sourceBands == null ? i2 : sourceBands.length) != (destinationBands == null ? i3 : destinationBands.length)) {
            throw new IllegalArgumentException("ImageReadParam num source & dest bands differ!");
        }
        if (sourceBands != null) {
            for (int i4 : sourceBands) {
                if (i4 >= i2) {
                    throw new IllegalArgumentException("ImageReadParam source bands contains a value >= the number of source bands!");
                }
            }
        }
        if (destinationBands != null) {
            for (int i5 : destinationBands) {
                if (i5 >= i3) {
                    throw new IllegalArgumentException("ImageReadParam dest bands contains a value >= the number of dest bands!");
                }
            }
        }
    }

    protected static BufferedImage getDestination(ImageReadParam imageReadParam, Iterator<ImageTypeSpecifier> it, int i2, int i3) throws IIOException {
        if (it == null || !it.hasNext()) {
            throw new IllegalArgumentException("imageTypes null or empty!");
        }
        if (i2 * i3 > 2147483647L) {
            throw new IllegalArgumentException("width*height > Integer.MAX_VALUE!");
        }
        ImageTypeSpecifier destinationType = null;
        if (imageReadParam != null) {
            BufferedImage destination = imageReadParam.getDestination();
            if (destination != null) {
                return destination;
            }
            destinationType = imageReadParam.getDestinationType();
        }
        if (destinationType == null) {
            ImageTypeSpecifier next = it.next();
            if (!(next instanceof ImageTypeSpecifier)) {
                throw new IllegalArgumentException("Non-ImageTypeSpecifier retrieved from imageTypes!");
            }
            destinationType = next;
        } else {
            boolean z2 = false;
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                if (it.next().equals(destinationType)) {
                    z2 = true;
                    break;
                }
            }
            if (!z2) {
                throw new IIOException("Destination type from ImageReadParam does not match!");
            }
        }
        Rectangle rectangle = new Rectangle(0, 0, 0, 0);
        Rectangle rectangle2 = new Rectangle(0, 0, 0, 0);
        computeRegions(imageReadParam, i2, i3, null, rectangle, rectangle2);
        return destinationType.createBufferedImage(rectangle2.f12372x + rectangle2.width, rectangle2.f12373y + rectangle2.height);
    }
}
