package java.awt.image.renderable;

import java.awt.image.ColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.ImageConsumer;
import java.awt.image.ImageProducer;
import java.awt.image.Raster;
import java.awt.image.RenderedImage;
import java.awt.image.SampleModel;
import java.util.Enumeration;
import java.util.Vector;

/* loaded from: rt.jar:java/awt/image/renderable/RenderableImageProducer.class */
public class RenderableImageProducer implements ImageProducer, Runnable {
    RenderableImage rdblImage;
    RenderContext rc;
    Vector ics = new Vector();

    public RenderableImageProducer(RenderableImage renderableImage, RenderContext renderContext) {
        this.rdblImage = renderableImage;
        this.rc = renderContext;
    }

    public synchronized void setRenderContext(RenderContext renderContext) {
        this.rc = renderContext;
    }

    @Override // java.awt.image.ImageProducer
    public synchronized void addConsumer(ImageConsumer imageConsumer) {
        if (!this.ics.contains(imageConsumer)) {
            this.ics.addElement(imageConsumer);
        }
    }

    @Override // java.awt.image.ImageProducer
    public synchronized boolean isConsumer(ImageConsumer imageConsumer) {
        return this.ics.contains(imageConsumer);
    }

    @Override // java.awt.image.ImageProducer
    public synchronized void removeConsumer(ImageConsumer imageConsumer) {
        this.ics.removeElement(imageConsumer);
    }

    @Override // java.awt.image.ImageProducer
    public synchronized void startProduction(ImageConsumer imageConsumer) {
        addConsumer(imageConsumer);
        new Thread(this, "RenderableImageProducer Thread").start();
    }

    @Override // java.awt.image.ImageProducer
    public void requestTopDownLeftRightResend(ImageConsumer imageConsumer) {
    }

    @Override // java.lang.Runnable
    public void run() {
        RenderedImage renderedImageCreateDefaultRendering;
        if (this.rc != null) {
            renderedImageCreateDefaultRendering = this.rdblImage.createRendering(this.rc);
        } else {
            renderedImageCreateDefaultRendering = this.rdblImage.createDefaultRendering();
        }
        ColorModel colorModel = renderedImageCreateDefaultRendering.getColorModel();
        Raster data = renderedImageCreateDefaultRendering.getData();
        SampleModel sampleModel = data.getSampleModel();
        DataBuffer dataBuffer = data.getDataBuffer();
        if (colorModel == null) {
            colorModel = ColorModel.getRGBdefault();
        }
        data.getMinX();
        data.getMinY();
        int width = data.getWidth();
        int height = data.getHeight();
        Enumeration enumerationElements = this.ics.elements();
        while (enumerationElements.hasMoreElements()) {
            ImageConsumer imageConsumer = (ImageConsumer) enumerationElements.nextElement2();
            imageConsumer.setDimensions(width, height);
            imageConsumer.setHints(30);
        }
        int[] iArr = new int[width];
        int[] iArr2 = new int[sampleModel.getNumBands()];
        for (int i2 = 0; i2 < height; i2++) {
            for (int i3 = 0; i3 < width; i3++) {
                sampleModel.getPixel(i3, i2, iArr2, dataBuffer);
                iArr[i3] = colorModel.getDataElement(iArr2, 0);
            }
            Enumeration enumerationElements2 = this.ics.elements();
            while (enumerationElements2.hasMoreElements()) {
                ((ImageConsumer) enumerationElements2.nextElement2()).setPixels(0, i2, width, 1, colorModel, iArr, 0, width);
            }
        }
        Enumeration enumerationElements3 = this.ics.elements();
        while (enumerationElements3.hasMoreElements()) {
            ((ImageConsumer) enumerationElements3.nextElement2()).imageComplete(3);
        }
    }
}
