package com.sun.xml.internal.ws.encoding;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.datatransfer.DataFlavor;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Iterator;
import java.util.logging.Logger;
import javax.activation.ActivationDataFlavor;
import javax.activation.DataContentHandler;
import javax.activation.DataSource;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;

/* loaded from: rt.jar:com/sun/xml/internal/ws/encoding/ImageDataContentHandler.class */
public class ImageDataContentHandler extends Component implements DataContentHandler {
    private static final Logger log = Logger.getLogger(ImageDataContentHandler.class.getName());
    private final DataFlavor[] flavor;

    public ImageDataContentHandler() {
        String[] mimeTypes = ImageIO.getReaderMIMETypes();
        this.flavor = new DataFlavor[mimeTypes.length];
        for (int i2 = 0; i2 < mimeTypes.length; i2++) {
            this.flavor[i2] = new ActivationDataFlavor(Image.class, mimeTypes[i2], "Image");
        }
    }

    @Override // javax.activation.DataContentHandler
    public DataFlavor[] getTransferDataFlavors() {
        return (DataFlavor[]) Arrays.copyOf(this.flavor, this.flavor.length);
    }

    @Override // javax.activation.DataContentHandler
    public Object getTransferData(DataFlavor df, DataSource ds) throws IOException {
        for (DataFlavor aFlavor : this.flavor) {
            if (aFlavor.equals(df)) {
                return getContent(ds);
            }
        }
        return null;
    }

    @Override // javax.activation.DataContentHandler
    public Object getContent(DataSource ds) throws IOException {
        return ImageIO.read(new BufferedInputStream(ds.getInputStream()));
    }

    @Override // javax.activation.DataContentHandler
    public void writeTo(Object obj, String type, OutputStream os) throws IOException {
        BufferedImage bufImage;
        try {
            if (obj instanceof BufferedImage) {
                bufImage = (BufferedImage) obj;
            } else if (obj instanceof Image) {
                bufImage = render((Image) obj);
            } else {
                throw new IOException("ImageDataContentHandler requires Image object, was given object of type " + obj.getClass().toString());
            }
            ImageWriter writer = null;
            Iterator<ImageWriter> i2 = ImageIO.getImageWritersByMIMEType(type);
            if (i2.hasNext()) {
                writer = i2.next();
            }
            if (writer != null) {
                ImageOutputStream stream = ImageIO.createImageOutputStream(os);
                writer.setOutput(stream);
                writer.write(bufImage);
                writer.dispose();
                stream.close();
                return;
            }
            throw new IOException("Unsupported mime type:" + type);
        } catch (Exception e2) {
            throw new IOException("Unable to encode the image to a stream " + e2.getMessage());
        }
    }

    private BufferedImage render(Image img) throws InterruptedException {
        MediaTracker tracker = new MediaTracker(this);
        tracker.addImage(img, 0);
        tracker.waitForAll();
        BufferedImage bufImage = new BufferedImage(img.getWidth(null), img.getHeight(null), 1);
        Graphics g2 = bufImage.createGraphics();
        g2.drawImage(img, 0, 0, null);
        g2.dispose();
        return bufImage;
    }
}
