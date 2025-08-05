package com.sun.prism.j2d;

import com.sun.javafx.PlatformUtil;
import com.sun.javafx.font.FontFactory;
import com.sun.javafx.font.FontResource;
import com.sun.javafx.font.PGFont;
import java.awt.Font;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedAction;

/* loaded from: jfxrt.jar:com/sun/prism/j2d/J2DFontFactory.class */
final class J2DFontFactory implements FontFactory {
    FontFactory prismFontFactory;
    private static boolean compositeFontMethodsInitialized = false;
    private static Method getCompositeFontUIResource = null;

    J2DFontFactory(FontFactory fontFactory) {
        this.prismFontFactory = fontFactory;
    }

    @Override // com.sun.javafx.font.FontFactory
    public PGFont createFont(String name, float size) {
        return this.prismFontFactory.createFont(name, size);
    }

    @Override // com.sun.javafx.font.FontFactory
    public PGFont createFont(String family, boolean bold, boolean italic, float size) {
        return this.prismFontFactory.createFont(family, bold, italic, size);
    }

    @Override // com.sun.javafx.font.FontFactory
    public synchronized PGFont deriveFont(PGFont font, boolean bold, boolean italic, float size) {
        return this.prismFontFactory.deriveFont(font, bold, italic, size);
    }

    @Override // com.sun.javafx.font.FontFactory
    public String[] getFontFamilyNames() {
        return this.prismFontFactory.getFontFamilyNames();
    }

    @Override // com.sun.javafx.font.FontFactory
    public String[] getFontFullNames() {
        return this.prismFontFactory.getFontFullNames();
    }

    @Override // com.sun.javafx.font.FontFactory
    public String[] getFontFullNames(String family) {
        return this.prismFontFactory.getFontFullNames(family);
    }

    @Override // com.sun.javafx.font.FontFactory
    public boolean isPlatformFont(String name) {
        return this.prismFontFactory.isPlatformFont(name);
    }

    @Override // com.sun.javafx.font.FontFactory
    public final boolean hasPermission() {
        return this.prismFontFactory.hasPermission();
    }

    @Override // com.sun.javafx.font.FontFactory
    public PGFont loadEmbeddedFont(String name, InputStream fontStream, float size, boolean register) {
        if (!hasPermission()) {
            return createFont(FontFactory.DEFAULT_FULLNAME, size);
        }
        PGFont font = this.prismFontFactory.loadEmbeddedFont(name, fontStream, size, register);
        if (font == null) {
            return null;
        }
        font.getFontResource();
        registerFont(font.getFontResource());
        return font;
    }

    public static void registerFont(FontResource fr) {
        AccessController.doPrivileged(() -> {
            InputStream stream = null;
            try {
                try {
                    File file = new File(fr.getFileName());
                    stream = new FileInputStream(file);
                    Font font = Font.createFont(0, stream);
                    fr.setPeer(font);
                    if (stream == null) {
                        return null;
                    }
                    try {
                        stream.close();
                        return null;
                    } catch (Exception e2) {
                        return null;
                    }
                } catch (Exception e3) {
                    e3.printStackTrace();
                    if (stream == null) {
                        return null;
                    }
                    try {
                        stream.close();
                        return null;
                    } catch (Exception e4) {
                        return null;
                    }
                }
            } catch (Throwable th) {
                if (stream != null) {
                    try {
                        stream.close();
                    } catch (Exception e5) {
                    }
                }
                throw th;
            }
        });
    }

    @Override // com.sun.javafx.font.FontFactory
    public PGFont loadEmbeddedFont(String name, String path, float size, boolean register) {
        if (!hasPermission()) {
            return createFont(FontFactory.DEFAULT_FULLNAME, size);
        }
        PGFont font = this.prismFontFactory.loadEmbeddedFont(name, path, size, register);
        if (font == null) {
            return null;
        }
        final FontResource fr = font.getFontResource();
        AccessController.doPrivileged(new PrivilegedAction<Object>() { // from class: com.sun.prism.j2d.J2DFontFactory.1
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public Object run2() {
                try {
                    File file = new File(fr.getFileName());
                    Font font2 = Font.createFont(0, file);
                    fr.setPeer(font2);
                    return null;
                } catch (Exception e2) {
                    e2.printStackTrace();
                    return null;
                }
            }
        });
        return font;
    }

    static Font getCompositeFont(Font srcFont) {
        if (PlatformUtil.isMac()) {
            return srcFont;
        }
        synchronized (J2DFontFactory.class) {
            if (!compositeFontMethodsInitialized) {
                AccessController.doPrivileged(() -> {
                    Class<?> fontMgrCls;
                    compositeFontMethodsInitialized = true;
                    try {
                        fontMgrCls = Class.forName("sun.font.FontUtilities", true, null);
                    } catch (ClassNotFoundException e2) {
                        try {
                            fontMgrCls = Class.forName("sun.font.FontManager", true, null);
                        } catch (ClassNotFoundException e3) {
                            return null;
                        }
                    }
                    try {
                        getCompositeFontUIResource = fontMgrCls.getMethod("getCompositeFontUIResource", Font.class);
                        return null;
                    } catch (NoSuchMethodException e4) {
                        return null;
                    }
                });
            }
        }
        if (getCompositeFontUIResource != null) {
            try {
                return (Font) getCompositeFontUIResource.invoke(null, srcFont);
            } catch (IllegalAccessException e2) {
            } catch (InvocationTargetException e3) {
            }
        }
        return srcFont;
    }
}
