package javax.imageio;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.awt.image.RenderedImage;
import java.io.IOException;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.List;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import javax.imageio.event.IIOWriteProgressListener;
import javax.imageio.event.IIOWriteWarningListener;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.spi.ImageWriterSpi;

/* loaded from: rt.jar:javax/imageio/ImageWriter.class */
public abstract class ImageWriter implements ImageTranscoder {
    protected ImageWriterSpi originatingProvider;
    protected Object output = null;
    protected Locale[] availableLocales = null;
    protected Locale locale = null;
    protected List<IIOWriteWarningListener> warningListeners = null;
    protected List<Locale> warningLocales = null;
    protected List<IIOWriteProgressListener> progressListeners = null;
    private boolean abortFlag = false;

    public abstract IIOMetadata getDefaultStreamMetadata(ImageWriteParam imageWriteParam);

    public abstract IIOMetadata getDefaultImageMetadata(ImageTypeSpecifier imageTypeSpecifier, ImageWriteParam imageWriteParam);

    @Override // javax.imageio.ImageTranscoder
    public abstract IIOMetadata convertStreamMetadata(IIOMetadata iIOMetadata, ImageWriteParam imageWriteParam);

    @Override // javax.imageio.ImageTranscoder
    public abstract IIOMetadata convertImageMetadata(IIOMetadata iIOMetadata, ImageTypeSpecifier imageTypeSpecifier, ImageWriteParam imageWriteParam);

    public abstract void write(IIOMetadata iIOMetadata, IIOImage iIOImage, ImageWriteParam imageWriteParam) throws IOException;

    protected ImageWriter(ImageWriterSpi imageWriterSpi) {
        this.originatingProvider = null;
        this.originatingProvider = imageWriterSpi;
    }

    public ImageWriterSpi getOriginatingProvider() {
        return this.originatingProvider;
    }

    public void setOutput(Object obj) {
        ImageWriterSpi originatingProvider;
        if (obj != null && (originatingProvider = getOriginatingProvider()) != null) {
            Class[] outputTypes = originatingProvider.getOutputTypes();
            boolean z2 = false;
            int i2 = 0;
            while (true) {
                if (i2 >= outputTypes.length) {
                    break;
                }
                if (!outputTypes[i2].isInstance(obj)) {
                    i2++;
                } else {
                    z2 = true;
                    break;
                }
            }
            if (!z2) {
                throw new IllegalArgumentException("Illegal output type!");
            }
        }
        this.output = obj;
    }

    public Object getOutput() {
        return this.output;
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

    public ImageWriteParam getDefaultWriteParam() {
        return new ImageWriteParam(getLocale());
    }

    public int getNumThumbnailsSupported(ImageTypeSpecifier imageTypeSpecifier, ImageWriteParam imageWriteParam, IIOMetadata iIOMetadata, IIOMetadata iIOMetadata2) {
        return 0;
    }

    public Dimension[] getPreferredThumbnailSizes(ImageTypeSpecifier imageTypeSpecifier, ImageWriteParam imageWriteParam, IIOMetadata iIOMetadata, IIOMetadata iIOMetadata2) {
        return null;
    }

    public boolean canWriteRasters() {
        return false;
    }

    public void write(IIOImage iIOImage) throws IOException {
        write(null, iIOImage, null);
    }

    public void write(RenderedImage renderedImage) throws IOException {
        write(null, new IIOImage(renderedImage, (List<? extends BufferedImage>) null, (IIOMetadata) null), null);
    }

    private void unsupported() {
        if (getOutput() == null) {
            throw new IllegalStateException("getOutput() == null!");
        }
        throw new UnsupportedOperationException("Unsupported write variant!");
    }

    public boolean canWriteSequence() {
        return false;
    }

    public void prepareWriteSequence(IIOMetadata iIOMetadata) throws IOException {
        unsupported();
    }

    public void writeToSequence(IIOImage iIOImage, ImageWriteParam imageWriteParam) throws IOException {
        unsupported();
    }

    public void endWriteSequence() throws IOException {
        unsupported();
    }

    public boolean canReplaceStreamMetadata() throws IOException {
        if (getOutput() == null) {
            throw new IllegalStateException("getOutput() == null!");
        }
        return false;
    }

    public void replaceStreamMetadata(IIOMetadata iIOMetadata) throws IOException {
        unsupported();
    }

    public boolean canReplaceImageMetadata(int i2) throws IOException {
        if (getOutput() == null) {
            throw new IllegalStateException("getOutput() == null!");
        }
        return false;
    }

    public void replaceImageMetadata(int i2, IIOMetadata iIOMetadata) throws IOException {
        unsupported();
    }

    public boolean canInsertImage(int i2) throws IOException {
        if (getOutput() == null) {
            throw new IllegalStateException("getOutput() == null!");
        }
        return false;
    }

    public void writeInsert(int i2, IIOImage iIOImage, ImageWriteParam imageWriteParam) throws IOException {
        unsupported();
    }

    public boolean canRemoveImage(int i2) throws IOException {
        if (getOutput() == null) {
            throw new IllegalStateException("getOutput() == null!");
        }
        return false;
    }

    public void removeImage(int i2) throws IOException {
        unsupported();
    }

    public boolean canWriteEmpty() throws IOException {
        if (getOutput() == null) {
            throw new IllegalStateException("getOutput() == null!");
        }
        return false;
    }

    public void prepareWriteEmpty(IIOMetadata iIOMetadata, ImageTypeSpecifier imageTypeSpecifier, int i2, int i3, IIOMetadata iIOMetadata2, List<? extends BufferedImage> list, ImageWriteParam imageWriteParam) throws IOException {
        unsupported();
    }

    public void endWriteEmpty() throws IOException {
        if (getOutput() == null) {
            throw new IllegalStateException("getOutput() == null!");
        }
        throw new IllegalStateException("No call to prepareWriteEmpty!");
    }

    public boolean canInsertEmpty(int i2) throws IOException {
        if (getOutput() == null) {
            throw new IllegalStateException("getOutput() == null!");
        }
        return false;
    }

    public void prepareInsertEmpty(int i2, ImageTypeSpecifier imageTypeSpecifier, int i3, int i4, IIOMetadata iIOMetadata, List<? extends BufferedImage> list, ImageWriteParam imageWriteParam) throws IOException {
        unsupported();
    }

    public void endInsertEmpty() throws IOException {
        unsupported();
    }

    public boolean canReplacePixels(int i2) throws IOException {
        if (getOutput() == null) {
            throw new IllegalStateException("getOutput() == null!");
        }
        return false;
    }

    public void prepareReplacePixels(int i2, Rectangle rectangle) throws IOException {
        unsupported();
    }

    public void replacePixels(RenderedImage renderedImage, ImageWriteParam imageWriteParam) throws IOException {
        unsupported();
    }

    public void replacePixels(Raster raster, ImageWriteParam imageWriteParam) throws IOException {
        unsupported();
    }

    public void endReplacePixels() throws IOException {
        unsupported();
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

    public void addIIOWriteWarningListener(IIOWriteWarningListener iIOWriteWarningListener) {
        if (iIOWriteWarningListener == null) {
            return;
        }
        this.warningListeners = ImageReader.addToList(this.warningListeners, iIOWriteWarningListener);
        this.warningLocales = ImageReader.addToList(this.warningLocales, getLocale());
    }

    public void removeIIOWriteWarningListener(IIOWriteWarningListener iIOWriteWarningListener) {
        int iIndexOf;
        if (iIOWriteWarningListener != null && this.warningListeners != null && (iIndexOf = this.warningListeners.indexOf(iIOWriteWarningListener)) != -1) {
            this.warningListeners.remove(iIndexOf);
            this.warningLocales.remove(iIndexOf);
            if (this.warningListeners.size() == 0) {
                this.warningListeners = null;
                this.warningLocales = null;
            }
        }
    }

    public void removeAllIIOWriteWarningListeners() {
        this.warningListeners = null;
        this.warningLocales = null;
    }

    public void addIIOWriteProgressListener(IIOWriteProgressListener iIOWriteProgressListener) {
        if (iIOWriteProgressListener == null) {
            return;
        }
        this.progressListeners = ImageReader.addToList(this.progressListeners, iIOWriteProgressListener);
    }

    public void removeIIOWriteProgressListener(IIOWriteProgressListener iIOWriteProgressListener) {
        if (iIOWriteProgressListener == null || this.progressListeners == null) {
            return;
        }
        this.progressListeners = ImageReader.removeFromList(this.progressListeners, iIOWriteProgressListener);
    }

    public void removeAllIIOWriteProgressListeners() {
        this.progressListeners = null;
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

    protected void processWriteAborted() {
        if (this.progressListeners == null) {
            return;
        }
        int size = this.progressListeners.size();
        for (int i2 = 0; i2 < size; i2++) {
            this.progressListeners.get(i2).writeAborted(this);
        }
    }

    protected void processWarningOccurred(int i2, String str) {
        if (this.warningListeners == null) {
            return;
        }
        if (str == null) {
            throw new IllegalArgumentException("warning == null!");
        }
        int size = this.warningListeners.size();
        for (int i3 = 0; i3 < size; i3++) {
            this.warningListeners.get(i3).warningOccurred(this, i2, str);
        }
    }

    protected void processWarningOccurred(int i2, String str, String str2) {
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
        for (int i3 = 0; i3 < size; i3++) {
            IIOWriteWarningListener iIOWriteWarningListener = this.warningListeners.get(i3);
            Locale locale = this.warningLocales.get(i3);
            if (locale == null) {
                locale = Locale.getDefault();
            }
            try {
                bundle = ResourceBundle.getBundle(str, locale, (ClassLoader) AccessController.doPrivileged(new PrivilegedAction() { // from class: javax.imageio.ImageWriter.1
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
                iIOWriteWarningListener.warningOccurred(this, i2, bundle.getString(str2));
            } catch (ClassCastException e4) {
                throw new IllegalArgumentException("Resource is not a String!");
            } catch (MissingResourceException e5) {
                throw new IllegalArgumentException("Resource is missing!");
            }
        }
    }

    public void reset() {
        setOutput(null);
        setLocale(null);
        removeAllIIOWriteWarningListeners();
        removeAllIIOWriteProgressListeners();
        clearAbortRequest();
    }

    public void dispose() {
    }
}
