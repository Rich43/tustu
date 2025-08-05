package javafx.scene.image;

import com.sun.javafx.tk.ImageLoader;
import com.sun.javafx.tk.PlatformImage;
import com.sun.javafx.tk.Toolkit;
import com.sun.media.jfxmedia.MetadataParser;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import javafx.beans.NamedArg;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.scene.paint.Color;
import org.icepdf.core.util.PdfOps;
import sun.util.locale.LanguageTag;

/* loaded from: jfxrt.jar:javafx/scene/image/WritableImage.class */
public class WritableImage extends Image {
    private ImageLoader tkImageLoader;
    private PixelWriter writer;

    static {
        Toolkit.setWritableImageAccessor(new Toolkit.WritableImageAccessor() { // from class: javafx.scene.image.WritableImage.1
            @Override // com.sun.javafx.tk.Toolkit.WritableImageAccessor
            public void loadTkImage(WritableImage wimg, Object loader) {
                wimg.loadTkImage(loader);
            }

            @Override // com.sun.javafx.tk.Toolkit.WritableImageAccessor
            public Object getTkImageLoader(WritableImage wimg) {
                return wimg.getTkImageLoader();
            }
        });
    }

    public WritableImage(@NamedArg(MetadataParser.WIDTH_TAG_NAME) int width, @NamedArg(MetadataParser.HEIGHT_TAG_NAME) int height) {
        super(width, height);
    }

    public WritableImage(@NamedArg("reader") PixelReader reader, @NamedArg(MetadataParser.WIDTH_TAG_NAME) int width, @NamedArg(MetadataParser.HEIGHT_TAG_NAME) int height) {
        super(width, height);
        getPixelWriter().setPixels(0, 0, width, height, reader, 0, 0);
    }

    public WritableImage(@NamedArg("reader") PixelReader reader, @NamedArg(LanguageTag.PRIVATEUSE) int x2, @NamedArg(PdfOps.y_TOKEN) int y2, @NamedArg(MetadataParser.WIDTH_TAG_NAME) int width, @NamedArg(MetadataParser.HEIGHT_TAG_NAME) int height) {
        super(width, height);
        getPixelWriter().setPixels(0, 0, width, height, reader, x2, y2);
    }

    @Override // javafx.scene.image.Image
    boolean isAnimation() {
        return true;
    }

    @Override // javafx.scene.image.Image
    boolean pixelsReadable() {
        return true;
    }

    public final PixelWriter getPixelWriter() {
        if (getProgress() < 1.0d || isError()) {
            return null;
        }
        if (this.writer == null) {
            this.writer = new PixelWriter() { // from class: javafx.scene.image.WritableImage.2
                ReadOnlyObjectProperty<PlatformImage> pimgprop;

                {
                    this.pimgprop = WritableImage.this.acc_platformImageProperty();
                }

                @Override // javafx.scene.image.PixelWriter
                public PixelFormat getPixelFormat() {
                    PlatformImage pimg = WritableImage.this.getWritablePlatformImage();
                    return pimg.getPlatformPixelFormat();
                }

                @Override // javafx.scene.image.PixelWriter
                public void setArgb(int x2, int y2, int argb) {
                    WritableImage.this.getWritablePlatformImage().setArgb(x2, y2, argb);
                    WritableImage.this.pixelsDirty();
                }

                @Override // javafx.scene.image.PixelWriter
                public void setColor(int x2, int y2, Color c2) {
                    if (c2 == null) {
                        throw new NullPointerException("Color cannot be null");
                    }
                    int a2 = (int) Math.round(c2.getOpacity() * 255.0d);
                    int r2 = (int) Math.round(c2.getRed() * 255.0d);
                    int g2 = (int) Math.round(c2.getGreen() * 255.0d);
                    int b2 = (int) Math.round(c2.getBlue() * 255.0d);
                    setArgb(x2, y2, (a2 << 24) | (r2 << 16) | (g2 << 8) | b2);
                }

                @Override // javafx.scene.image.PixelWriter
                public <T extends Buffer> void setPixels(int x2, int y2, int w2, int h2, PixelFormat<T> pixelformat, T buffer, int scanlineStride) {
                    if (pixelformat == null) {
                        throw new NullPointerException("PixelFormat cannot be null");
                    }
                    if (buffer == null) {
                        throw new NullPointerException("Buffer cannot be null");
                    }
                    PlatformImage pimg = WritableImage.this.getWritablePlatformImage();
                    pimg.setPixels(x2, y2, w2, h2, (PixelFormat<PixelFormat<T>>) pixelformat, (PixelFormat<T>) buffer, scanlineStride);
                    WritableImage.this.pixelsDirty();
                }

                @Override // javafx.scene.image.PixelWriter
                public void setPixels(int x2, int y2, int w2, int h2, PixelFormat<ByteBuffer> pixelformat, byte[] buffer, int offset, int scanlineStride) {
                    if (pixelformat == null) {
                        throw new NullPointerException("PixelFormat cannot be null");
                    }
                    if (buffer == null) {
                        throw new NullPointerException("Buffer cannot be null");
                    }
                    PlatformImage pimg = WritableImage.this.getWritablePlatformImage();
                    pimg.setPixels(x2, y2, w2, h2, pixelformat, buffer, offset, scanlineStride);
                    WritableImage.this.pixelsDirty();
                }

                @Override // javafx.scene.image.PixelWriter
                public void setPixels(int x2, int y2, int w2, int h2, PixelFormat<IntBuffer> pixelformat, int[] buffer, int offset, int scanlineStride) {
                    if (pixelformat == null) {
                        throw new NullPointerException("PixelFormat cannot be null");
                    }
                    if (buffer == null) {
                        throw new NullPointerException("Buffer cannot be null");
                    }
                    PlatformImage pimg = WritableImage.this.getWritablePlatformImage();
                    pimg.setPixels(x2, y2, w2, h2, pixelformat, buffer, offset, scanlineStride);
                    WritableImage.this.pixelsDirty();
                }

                @Override // javafx.scene.image.PixelWriter
                public void setPixels(int writex, int writey, int w2, int h2, PixelReader reader, int readx, int ready) {
                    if (reader == null) {
                        throw new NullPointerException("Reader cannot be null");
                    }
                    PlatformImage pimg = WritableImage.this.getWritablePlatformImage();
                    pimg.setPixels(writex, writey, w2, h2, reader, readx, ready);
                    WritableImage.this.pixelsDirty();
                }
            };
        }
        return this.writer;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void loadTkImage(Object loader) {
        if (!(loader instanceof ImageLoader)) {
            throw new IllegalArgumentException("Unrecognized image loader: " + loader);
        }
        ImageLoader tkLoader = (ImageLoader) loader;
        if (tkLoader.getWidth() != ((int) getWidth()) || tkLoader.getHeight() != ((int) getHeight())) {
            throw new IllegalArgumentException("Size of loader does not match size of image");
        }
        super.setPlatformImage(tkLoader.getFrame(0));
        this.tkImageLoader = tkLoader;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Object getTkImageLoader() {
        return this.tkImageLoader;
    }
}
