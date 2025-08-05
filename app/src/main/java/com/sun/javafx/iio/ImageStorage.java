package com.sun.javafx.iio;

import com.sun.javafx.PlatformUtil;
import com.sun.javafx.iio.ImageFormatDescription;
import com.sun.javafx.iio.bmp.BMPImageLoaderFactory;
import com.sun.javafx.iio.common.ImageTools;
import com.sun.javafx.iio.gif.GIFImageLoaderFactory;
import com.sun.javafx.iio.ios.IosImageLoaderFactory;
import com.sun.javafx.iio.jpeg.JPEGImageLoaderFactory;
import com.sun.javafx.iio.png.PNGImageLoaderFactory;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.SequenceInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/* loaded from: jfxrt.jar:com/sun/javafx/iio/ImageStorage.class */
public class ImageStorage {
    private static final HashMap<ImageFormatDescription.Signature, ImageLoaderFactory> loaderFactoriesBySignature;
    private static final ImageLoaderFactory[] loaderFactories;
    private static final boolean isIOS = PlatformUtil.isIOS();
    private static int maxSignatureLength;

    /* loaded from: jfxrt.jar:com/sun/javafx/iio/ImageStorage$ImageType.class */
    public enum ImageType {
        GRAY,
        GRAY_ALPHA,
        GRAY_ALPHA_PRE,
        PALETTE,
        PALETTE_ALPHA,
        PALETTE_ALPHA_PRE,
        PALETTE_TRANS,
        RGB,
        RGBA,
        RGBA_PRE
    }

    static {
        if (isIOS) {
            loaderFactories = new ImageLoaderFactory[]{IosImageLoaderFactory.getInstance()};
        } else {
            loaderFactories = new ImageLoaderFactory[]{GIFImageLoaderFactory.getInstance(), JPEGImageLoaderFactory.getInstance(), PNGImageLoaderFactory.getInstance(), BMPImageLoaderFactory.getInstance()};
        }
        loaderFactoriesBySignature = new HashMap<>(loaderFactories.length);
        for (int i2 = 0; i2 < loaderFactories.length; i2++) {
            addImageLoaderFactory(loaderFactories[i2]);
        }
    }

    public static ImageFormatDescription[] getSupportedDescriptions() {
        ImageFormatDescription[] formats = new ImageFormatDescription[loaderFactories.length];
        for (int i2 = 0; i2 < loaderFactories.length; i2++) {
            formats[i2] = loaderFactories[i2].getFormatDescription();
        }
        return formats;
    }

    public static int getNumBands(ImageType type) {
        int numBands;
        switch (type) {
            case GRAY:
            case PALETTE:
            case PALETTE_ALPHA:
            case PALETTE_ALPHA_PRE:
            case PALETTE_TRANS:
                numBands = 1;
                break;
            case GRAY_ALPHA:
            case GRAY_ALPHA_PRE:
                numBands = 2;
                break;
            case RGB:
                numBands = 3;
                break;
            case RGBA:
            case RGBA_PRE:
                numBands = 4;
                break;
            default:
                throw new IllegalArgumentException("Unknown ImageType " + ((Object) type));
        }
        return numBands;
    }

    public static void addImageLoaderFactory(ImageLoaderFactory factory) {
        ImageFormatDescription desc = factory.getFormatDescription();
        for (ImageFormatDescription.Signature signature : desc.getSignatures()) {
            loaderFactoriesBySignature.put(signature, factory);
        }
        synchronized (ImageStorage.class) {
            maxSignatureLength = -1;
        }
    }

    public static ImageFrame[] loadAll(InputStream input, ImageLoadListener listener, int width, int height, boolean preserveAspectRatio, float pixelScale, boolean smooth) throws ImageStorageException {
        ImageLoader loader;
        try {
            if (isIOS) {
                loader = IosImageLoaderFactory.getInstance().createImageLoader(input);
            } else {
                loader = getLoaderBySignature(input, listener);
            }
            if (loader != null) {
                ImageFrame[] images = loadAll(loader, width, height, preserveAspectRatio, pixelScale, smooth);
                return images;
            }
            throw new ImageStorageException("No loader for image data");
        } catch (IOException e2) {
            throw new ImageStorageException(e2.getMessage(), e2);
        }
    }

    public static ImageFrame[] loadAll(String input, ImageLoadListener listener, int width, int height, boolean preserveAspectRatio, float devPixelScale, boolean smooth) throws ImageStorageException {
        ImageLoader loader;
        if (input == null || input.isEmpty()) {
            throw new ImageStorageException("URL can't be null or empty");
        }
        InputStream theStream = null;
        float imgPixelScale = 1.0f;
        try {
            if (devPixelScale >= 1.5f) {
                try {
                    try {
                        String name2x = ImageTools.getScaledImageName(input);
                        theStream = ImageTools.createInputStream(name2x);
                        imgPixelScale = 2.0f;
                    } catch (IOException e2) {
                    }
                } catch (IOException e3) {
                    throw new ImageStorageException(e3.getMessage(), e3);
                }
            }
            if (theStream == null) {
                theStream = ImageTools.createInputStream(input);
            }
            if (isIOS) {
                loader = IosImageLoaderFactory.getInstance().createImageLoader(theStream);
            } else {
                loader = getLoaderBySignature(theStream, listener);
            }
            if (loader != null) {
                ImageFrame[] images = loadAll(loader, width, height, preserveAspectRatio, imgPixelScale, smooth);
                if (theStream != null) {
                    try {
                        theStream.close();
                    } catch (IOException e4) {
                    }
                }
                return images;
            }
            throw new ImageStorageException("No loader for image data");
        } catch (Throwable th) {
            if (0 != 0) {
                try {
                    theStream.close();
                } catch (IOException e5) {
                    throw th;
                }
            }
            throw th;
        }
    }

    private static synchronized int getMaxSignatureLength() {
        if (maxSignatureLength < 0) {
            maxSignatureLength = 0;
            for (ImageFormatDescription.Signature signature : loaderFactoriesBySignature.keySet()) {
                int signatureLength = signature.getLength();
                if (maxSignatureLength < signatureLength) {
                    maxSignatureLength = signatureLength;
                }
            }
        }
        return maxSignatureLength;
    }

    private static ImageFrame[] loadAll(ImageLoader loader, int width, int height, boolean preserveAspectRatio, float pixelScale, boolean smooth) throws ImageStorageException {
        ImageFrame[] images = null;
        ArrayList<ImageFrame> list = new ArrayList<>();
        int imageIndex = 0;
        while (true) {
            try {
                int i2 = imageIndex;
                imageIndex++;
                ImageFrame image = loader.load(i2, width, height, preserveAspectRatio, smooth);
                if (image == null) {
                    break;
                }
                image.setPixelScale(pixelScale);
                list.add(image);
            } catch (Exception e2) {
                if (imageIndex <= 1) {
                    throw new ImageStorageException(e2.getMessage(), e2);
                }
            }
        }
        int numImages = list.size();
        if (numImages > 0) {
            images = new ImageFrame[numImages];
            list.toArray(images);
        }
        return images;
    }

    private static ImageLoader getLoaderBySignature(InputStream stream, ImageLoadListener listener) throws IOException {
        byte[] header = new byte[getMaxSignatureLength()];
        ImageTools.readFully(stream, header);
        for (Map.Entry<ImageFormatDescription.Signature, ImageLoaderFactory> factoryRegistration : loaderFactoriesBySignature.entrySet()) {
            if (factoryRegistration.getKey().matches(header)) {
                InputStream headerStream = new ByteArrayInputStream(header);
                InputStream seqStream = new SequenceInputStream(headerStream, stream);
                ImageLoader loader = factoryRegistration.getValue().createImageLoader(seqStream);
                if (listener != null) {
                    loader.addListener(listener);
                }
                return loader;
            }
        }
        return null;
    }

    private ImageStorage() {
    }
}
