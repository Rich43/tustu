package com.sun.xml.internal.messaging.saaj.soap;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.datatransfer.DataFlavor;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.activation.ActivationDataFlavor;
import javax.activation.DataContentHandler;
import javax.activation.DataSource;
import javax.imageio.ImageIO;

/* loaded from: rt.jar:com/sun/xml/internal/messaging/saaj/soap/JpegDataContentHandler.class */
public class JpegDataContentHandler extends Component implements DataContentHandler {
    public static final String STR_SRC = "java.awt.Image";

    @Override // javax.activation.DataContentHandler
    public DataFlavor[] getTransferDataFlavors() {
        DataFlavor[] flavors = new DataFlavor[1];
        try {
            flavors[0] = new ActivationDataFlavor(Class.forName(STR_SRC), "image/jpeg", "JPEG");
        } catch (Exception e2) {
            System.out.println(e2);
        }
        return flavors;
    }

    @Override // javax.activation.DataContentHandler
    public Object getTransferData(DataFlavor df, DataSource ds) {
        if (df.getMimeType().startsWith("image/jpeg") && df.getRepresentationClass().getName().equals(STR_SRC)) {
            BufferedImage jpegLoadImage = null;
            try {
                InputStream inputStream = ds.getInputStream();
                jpegLoadImage = ImageIO.read(inputStream);
            } catch (Exception e2) {
                System.out.println(e2);
            }
            return jpegLoadImage;
        }
        return null;
    }

    @Override // javax.activation.DataContentHandler
    public Object getContent(DataSource ds) {
        BufferedImage jpegLoadImage = null;
        try {
            InputStream inputStream = ds.getInputStream();
            jpegLoadImage = ImageIO.read(inputStream);
        } catch (Exception e2) {
        }
        return jpegLoadImage;
    }

    @Override // javax.activation.DataContentHandler
    public void writeTo(Object obj, String mimeType, OutputStream os) throws IOException {
        BufferedImage bufImage;
        if (!mimeType.equals("image/jpeg")) {
            throw new IOException("Invalid content type \"" + mimeType + "\" for ImageContentHandler");
        }
        if (obj == null) {
            throw new IOException("Null object for ImageContentHandler");
        }
        try {
            if (obj instanceof BufferedImage) {
                bufImage = (BufferedImage) obj;
            } else {
                Image img = (Image) obj;
                MediaTracker tracker = new MediaTracker(this);
                tracker.addImage(img, 0);
                tracker.waitForAll();
                if (tracker.isErrorAny()) {
                    throw new IOException("Error while loading image");
                }
                bufImage = new BufferedImage(img.getWidth(null), img.getHeight(null), 1);
                Graphics g2 = bufImage.createGraphics();
                g2.drawImage(img, 0, 0, null);
            }
            ImageIO.write(bufImage, "jpeg", os);
        } catch (Exception ex) {
            throw new IOException("Unable to run the JPEG Encoder on a stream " + ex.getMessage());
        }
    }
}
