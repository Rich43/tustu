package com.efiAnalytics.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;
import javax.imageio.stream.FileImageOutputStream;

/* renamed from: com.efiAnalytics.ui.cz, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:com/efiAnalytics/ui/cz.class */
public class C1615cz {
    public boolean a(Component component, String str, String str2, String str3) {
        String strA = a(str2);
        BufferedImage bufferedImageA = (str3 == null || str3.isEmpty()) ? a(component) : a(component, str3);
        if (strA.toLowerCase().indexOf("jpg") > 0 || strA.toLowerCase().indexOf("jpeg") > 0) {
            return a(bufferedImageA, str, strA);
        }
        if (strA.toLowerCase().indexOf("png") > 0) {
            return b(bufferedImageA, str, strA);
        }
        return false;
    }

    public BufferedImage a(Component component) {
        BufferedImage bufferedImage = new BufferedImage(component.getWidth(), component.getHeight(), 5);
        component.paint(bufferedImage.getGraphics());
        return bufferedImage;
    }

    public BufferedImage a(Component component, String str) {
        BufferedImage bufferedImage = new BufferedImage(component.getWidth(), component.getHeight() + component.getFont().getSize(), 5);
        Graphics graphics = bufferedImage.getGraphics();
        component.paint(graphics);
        graphics.setColor(Color.darkGray);
        graphics.fillRect(0, component.getHeight(), bufferedImage.getWidth(), bufferedImage.getHeight() - component.getHeight());
        graphics.setColor(Color.white);
        graphics.drawString(str, 5, bufferedImage.getHeight() - 2);
        return bufferedImage;
    }

    public File a(Component component, int i2, int i3) throws IOException {
        File fileCreateTempFile = File.createTempFile(component.getName() + Math.random(), "png");
        fileCreateTempFile.deleteOnExit();
        return a(component, fileCreateTempFile, i2, i3);
    }

    public File a(Component component, File file, int i2, int i3) throws V.a {
        if (b(cN.a(a(component), i2, i3, RenderingHints.VALUE_INTERPOLATION_BICUBIC), file.getParentFile().getAbsolutePath(), file.getName())) {
            return file;
        }
        throw new V.a("Unable to resize Image");
    }

    public boolean a(BufferedImage bufferedImage, String str, String str2) {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(new File(str, str2));
            ImageIO.write(bufferedImage, "png", fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
            return true;
        } catch (Exception e2) {
            System.out.println("ERROR encoding " + str2);
            e2.printStackTrace();
            return false;
        }
    }

    public boolean b(BufferedImage bufferedImage, String str, String str2) {
        try {
            ImageWriter next = ImageIO.getImageWritersBySuffix("png").next();
            FileImageOutputStream fileImageOutputStream = new FileImageOutputStream(new File(str, str2));
            next.setOutput(fileImageOutputStream);
            next.write(bufferedImage);
            fileImageOutputStream.flush();
            fileImageOutputStream.close();
            return true;
        } catch (Exception e2) {
            System.out.println("ERROR encoding " + str2);
            e2.printStackTrace();
            return false;
        }
    }

    public static String a(String str) {
        if (str.toLowerCase().endsWith("jpg") || str.toLowerCase().endsWith("jpeg")) {
            return str;
        }
        double d2 = 1.1d;
        try {
            d2 = Double.parseDouble(System.getProperty("java.specification.version"));
        } catch (Exception e2) {
        }
        if (d2 < 1.4d) {
            str = str.substring(0, str.length() - 3) + "jpg";
            System.out.println("png unsupported on this JVM, changing to jpg");
        }
        return str;
    }
}
