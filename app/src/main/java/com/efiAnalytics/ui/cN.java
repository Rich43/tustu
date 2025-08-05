package com.efiAnalytics.ui;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.awt.image.ConvolveOp;
import java.awt.image.ImageObserver;
import java.awt.image.Kernel;
import java.awt.image.RescaleOp;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/ui/cN.class */
public class cN {

    /* renamed from: a, reason: collision with root package name */
    public static final boolean f11077a = Boolean.getBoolean("imgscalr.debug");

    /* renamed from: b, reason: collision with root package name */
    public static final String f11078b = System.getProperty("imgscalr.logPrefix", "[imgscalr] ");

    /* renamed from: c, reason: collision with root package name */
    public static final ConvolveOp f11079c = new ConvolveOp(new Kernel(3, 3, new float[]{0.0f, 0.08f, 0.0f, 0.08f, 0.68f, 0.08f, 0.0f, 0.08f, 0.0f}), 1, null);

    /* renamed from: d, reason: collision with root package name */
    public static final RescaleOp f11080d = new RescaleOp(0.9f, 0.0f, (RenderingHints) null);

    /* renamed from: e, reason: collision with root package name */
    public static final RescaleOp f11081e = new RescaleOp(1.1f, 0.0f, (RenderingHints) null);

    /* renamed from: f, reason: collision with root package name */
    public static final ColorConvertOp f11082f = new ColorConvertOp(ColorSpace.getInstance(1003), (RenderingHints) null);

    protected static void a(int i2, String str, Object... objArr) {
        if (f11077a) {
            System.out.print(f11078b);
            for (int i3 = 0; i3 < i2; i3++) {
                System.out.print("\t");
            }
            System.out.printf(str, objArr);
            System.out.println();
        }
    }

    protected static BufferedImage a(BufferedImage bufferedImage, int i2, int i3) {
        if (i2 < 0 || i3 < 0) {
            throw new IllegalArgumentException("width [" + i2 + "] and height [" + i3 + "] must be >= 0");
        }
        return new BufferedImage(i2, i3, bufferedImage.getTransparency() == 1 ? 1 : 2);
    }

    public static BufferedImage a(BufferedImage bufferedImage, int i2, int i3, Object obj) {
        BufferedImage bufferedImageA = a(bufferedImage, i2, i3);
        Graphics2D graphics2DCreateGraphics = bufferedImageA.createGraphics();
        graphics2DCreateGraphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, obj);
        graphics2DCreateGraphics.drawImage(bufferedImage, 0, 0, i2, i3, null);
        graphics2DCreateGraphics.dispose();
        return bufferedImageA;
    }

    public static BufferedImage a(Image image) {
        if (image instanceof BufferedImage) {
            return (BufferedImage) image;
        }
        BufferedImage bufferedImage = new BufferedImage(image.getWidth(null), image.getHeight(null), 2);
        Graphics2D graphics2DCreateGraphics = bufferedImage.createGraphics();
        graphics2DCreateGraphics.drawImage(image, 0, 0, (ImageObserver) null);
        graphics2DCreateGraphics.dispose();
        return bufferedImage;
    }

    static {
        a(0, "Debug output ENABLED", new Object[0]);
    }
}
