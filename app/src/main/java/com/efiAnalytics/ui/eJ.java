package com.efiAnalytics.ui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.Insets;
import java.awt.MediaTracker;
import java.awt.Toolkit;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/ui/eJ.class */
public class eJ {

    /* renamed from: a, reason: collision with root package name */
    private static int f11484a = 12;

    /* renamed from: b, reason: collision with root package name */
    private static float f11485b = -1.0f;

    /* renamed from: c, reason: collision with root package name */
    private static boolean f11486c = true;

    public static int a() throws HeadlessException {
        int iC = c();
        if (!bH.I.a()) {
            return iC;
        }
        return Math.round(iC * (Toolkit.getDefaultToolkit().getScreenResolution() / 96.0f));
    }

    public static float a(float f2) {
        return bH.I.a() ? f2 * (d() / 96.0f) : f2;
    }

    public static int a(int i2) {
        return bH.I.a() ? Math.round(i2 * (d() / 96.0f)) : i2;
    }

    public static int b(int i2) {
        return bH.I.a() ? Math.round(i2 * (96.0f / d())) : i2;
    }

    private static float d() {
        if (f11485b < 0.0f) {
            try {
                f11485b = Toolkit.getDefaultToolkit().getScreenResolution();
            } catch (Error e2) {
                bH.C.c("Could not get JavaFX screen resolution, using AWT");
                f11485b = Toolkit.getDefaultToolkit().getScreenResolution();
            }
        }
        return f11485b;
    }

    public static Image a(Image image) {
        return a(image, -1);
    }

    public static Image a(Image image, int i2) {
        if ((b() || i2 > 0) && image != null) {
            int iA = a(i2 > 0 ? i2 : image.getHeight(null));
            int iRound = Math.round((image.getWidth(null) * iA) / image.getHeight(null));
            try {
                image = f11486c ? image.getScaledInstance(iRound, iA, 4) : image.getScaledInstance(iRound, iA, 1);
            } catch (Exception e2) {
                f11486c = false;
                bH.C.c("Smooth Image Scaling failed, going to Default");
                image = image.getScaledInstance(iRound, iA, 1);
            }
        }
        return image;
    }

    public static boolean b() {
        return bH.I.a() && Toolkit.getDefaultToolkit().getScreenResolution() != 96;
    }

    public static int c() {
        return f11484a;
    }

    public static Insets a(Insets insets) {
        if (b()) {
            insets = new Insets(Math.round(a(insets.top)), Math.round(a(insets.left)), Math.round(a(insets.bottom)), Math.round(a(insets.right)));
        }
        return insets;
    }

    public static Image a(Image image, Component component) {
        return a(image, component, -1);
    }

    public static Image a(Image image, Component component, int i2) {
        if (image.getWidth(null) <= 0) {
            MediaTracker mediaTracker = new MediaTracker(component);
            mediaTracker.addImage(image, 1);
            try {
                mediaTracker.waitForAll(300L);
            } catch (InterruptedException e2) {
                e2.printStackTrace();
            }
            mediaTracker.removeImage(image, 1);
        }
        Image imageA = a(image, i2);
        if (imageA.getWidth(null) <= 0) {
            MediaTracker mediaTracker2 = new MediaTracker(component);
            mediaTracker2.addImage(imageA, 1);
            try {
                mediaTracker2.waitForAll(250L);
            } catch (InterruptedException e3) {
                e3.printStackTrace();
            }
            mediaTracker2.removeImage(imageA, 1);
        }
        return imageA;
    }

    public static Dimension a(int i2, int i3) {
        return new Dimension(a(i2), a(i3));
    }

    public static Insets a(int i2, int i3, int i4, int i5) {
        return new Insets(Math.round(a(i2)), Math.round(a(i3)), Math.round(a(i4)), Math.round(a(i5)));
    }

    public static void c(int i2) {
        f11484a = i2;
    }
}
