package com.sun.javafx.font;

import com.sun.javafx.tk.FontLoader;
import com.sun.javafx.tk.FontMetrics;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;

/* loaded from: jfxrt.jar:com/sun/javafx/font/PrismFontLoader.class */
public class PrismFontLoader extends FontLoader {
    private static PrismFontLoader theInstance = new PrismFontLoader();
    private boolean embeddedFontsLoaded = false;
    FontFactory installedFontFactory = null;

    public static PrismFontLoader getInstance() {
        return theInstance;
    }

    Properties loadEmbeddedFontDefinitions() {
        URL u2;
        Properties map = new Properties();
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        if (loader != null && (u2 = loader.getResource("META-INF/fonts.mf")) != null) {
            try {
                InputStream in = u2.openStream();
                Throwable th = null;
                try {
                    map.load(in);
                    if (in != null) {
                        if (0 != 0) {
                            try {
                                in.close();
                            } catch (Throwable th2) {
                                th.addSuppressed(th2);
                            }
                        } else {
                            in.close();
                        }
                    }
                } finally {
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
            return map;
        }
        return map;
    }

    private void loadEmbeddedFonts() {
        if (!this.embeddedFontsLoaded) {
            FontFactory fontFactory = getFontFactoryFromPipeline();
            if (!fontFactory.hasPermission()) {
                this.embeddedFontsLoaded = true;
                return;
            }
            Properties map = loadEmbeddedFontDefinitions();
            Enumeration<?> names = map.keys();
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            while (names.hasMoreElements()) {
                String n2 = (String) names.nextElement2();
                String p2 = map.getProperty(n2);
                if (p2.startsWith("/")) {
                    try {
                        InputStream in = loader.getResourceAsStream(p2.substring(1));
                        Throwable th = null;
                        try {
                            try {
                                fontFactory.loadEmbeddedFont(n2, in, 0.0f, true);
                                if (in != null) {
                                    if (0 != 0) {
                                        try {
                                            in.close();
                                        } catch (Throwable th2) {
                                            th.addSuppressed(th2);
                                        }
                                    } else {
                                        in.close();
                                    }
                                }
                            } finally {
                            }
                        } catch (Throwable th3) {
                            th = th3;
                            throw th3;
                        }
                    } catch (Exception e2) {
                    }
                }
            }
            this.embeddedFontsLoaded = true;
        }
    }

    @Override // com.sun.javafx.tk.FontLoader
    public Font loadFont(InputStream in, double size) {
        FontFactory factory = getFontFactoryFromPipeline();
        PGFont font = factory.loadEmbeddedFont((String) null, in, (float) size, true);
        if (font != null) {
            return createFont(font);
        }
        return null;
    }

    @Override // com.sun.javafx.tk.FontLoader
    public Font loadFont(String path, double size) {
        FontFactory factory = getFontFactoryFromPipeline();
        PGFont font = factory.loadEmbeddedFont((String) null, path, (float) size, true);
        if (font != null) {
            return createFont(font);
        }
        return null;
    }

    private Font createFont(PGFont font) {
        return Font.impl_NativeFont(font, font.getName(), font.getFamilyName(), font.getStyleName(), font.getSize());
    }

    @Override // com.sun.javafx.tk.FontLoader
    public List<String> getFamilies() {
        loadEmbeddedFonts();
        return Arrays.asList(getFontFactoryFromPipeline().getFontFamilyNames());
    }

    @Override // com.sun.javafx.tk.FontLoader
    public List<String> getFontNames() {
        loadEmbeddedFonts();
        return Arrays.asList(getFontFactoryFromPipeline().getFontFullNames());
    }

    @Override // com.sun.javafx.tk.FontLoader
    public List<String> getFontNames(String family) {
        loadEmbeddedFonts();
        return Arrays.asList(getFontFactoryFromPipeline().getFontFullNames(family));
    }

    @Override // com.sun.javafx.tk.FontLoader
    public Font font(String family, FontWeight weight, FontPosture posture, float size) {
        FontFactory fontFactory = getFontFactoryFromPipeline();
        if (!this.embeddedFontsLoaded && !fontFactory.isPlatformFont(family)) {
            loadEmbeddedFonts();
        }
        boolean bold = weight != null && weight.ordinal() >= FontWeight.BOLD.ordinal();
        boolean italic = posture == FontPosture.ITALIC;
        PGFont prismFont = fontFactory.createFont(family, bold, italic, size);
        Font fxFont = Font.impl_NativeFont(prismFont, prismFont.getName(), prismFont.getFamilyName(), prismFont.getStyleName(), size);
        return fxFont;
    }

    @Override // com.sun.javafx.tk.FontLoader
    public void loadFont(Font font) {
        FontFactory fontFactory = getFontFactoryFromPipeline();
        String fullName = font.getName();
        if (!this.embeddedFontsLoaded && !fontFactory.isPlatformFont(fullName)) {
            loadEmbeddedFonts();
        }
        PGFont prismFont = fontFactory.createFont(fullName, (float) font.getSize());
        String name = prismFont.getName();
        String family = prismFont.getFamilyName();
        String style = prismFont.getStyleName();
        font.impl_setNativeFont(prismFont, name, family, style);
    }

    @Override // com.sun.javafx.tk.FontLoader
    public FontMetrics getFontMetrics(Font font) {
        if (font != null) {
            PGFont prismFont = (PGFont) font.impl_getNativeFont();
            Metrics metrics = PrismFontUtils.getFontMetrics(prismFont);
            float maxAscent = -metrics.getAscent();
            float ascent = -metrics.getAscent();
            float xheight = metrics.getXHeight();
            float descent = metrics.getDescent();
            float maxDescent = metrics.getDescent();
            float leading = metrics.getLineGap();
            return FontMetrics.impl_createFontMetrics(maxAscent, ascent, xheight, descent, maxDescent, leading, font);
        }
        return null;
    }

    @Override // com.sun.javafx.tk.FontLoader
    public float computeStringWidth(String string, Font font) {
        PGFont prismFont = (PGFont) font.impl_getNativeFont();
        return (float) PrismFontUtils.computeStringWidth(prismFont, string);
    }

    @Override // com.sun.javafx.tk.FontLoader
    public float getSystemFontSize() {
        return PrismFontFactory.getSystemFontSize();
    }

    private FontFactory getFontFactoryFromPipeline() {
        if (this.installedFontFactory != null) {
            return this.installedFontFactory;
        }
        try {
            Class plc = Class.forName("com.sun.prism.GraphicsPipeline");
            Method gpm = plc.getMethod("getPipeline", (Class[]) null);
            Object plo = gpm.invoke(null, new Object[0]);
            Method gfm = plc.getMethod("getFontFactory", (Class[]) null);
            Object ffo = gfm.invoke(plo, new Object[0]);
            this.installedFontFactory = (FontFactory) ffo;
        } catch (Exception e2) {
        }
        return this.installedFontFactory;
    }
}
