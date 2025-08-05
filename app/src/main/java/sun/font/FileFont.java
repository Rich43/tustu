package sun.font;

import java.awt.FontFormatException;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.IOException;
import java.lang.ref.Reference;
import java.nio.ByteBuffer;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import sun.java2d.Disposer;
import sun.java2d.DisposerRecord;

/* loaded from: rt.jar:sun/font/FileFont.class */
public abstract class FileFont extends PhysicalFont {
    protected boolean useJavaRasterizer;
    protected int fileSize;
    protected FontScaler scaler;
    protected boolean checkedNatives;
    protected boolean useNatives;
    protected NativeFont[] nativeFonts;
    protected char[] glyphToCharMap;

    protected abstract void close();

    abstract ByteBuffer readBlock(int i2, int i3);

    protected abstract FontScaler getScaler();

    FileFont(String str, Object obj) throws FontFormatException {
        super(str, obj);
        this.useJavaRasterizer = true;
    }

    @Override // sun.font.Font2D
    FontStrike createStrike(FontStrikeDesc fontStrikeDesc) {
        if (!this.checkedNatives) {
            checkUseNatives();
        }
        return new FileFontStrike(this, fontStrikeDesc);
    }

    protected boolean checkUseNatives() {
        this.checkedNatives = true;
        return this.useNatives;
    }

    @Override // sun.font.Font2D
    public boolean canDoStyle(int i2) {
        return true;
    }

    void setFileToRemove(File file, CreatedFontTracker createdFontTracker) {
        Disposer.addObjectRecord(this, new CreatedFontFileDisposerRecord(file, createdFontTracker));
    }

    static void setFileToRemove(Object obj, File file, CreatedFontTracker createdFontTracker) {
        Disposer.addObjectRecord(obj, new CreatedFontFileDisposerRecord(file, createdFontTracker));
    }

    synchronized void deregisterFontAndClearStrikeCache() {
        FileFontStrike fileFontStrike;
        SunFontManager.getInstance().deRegisterBadFont(this);
        for (Reference reference : this.strikeCache.values()) {
            if (reference != null && (fileFontStrike = (FileFontStrike) reference.get()) != null && fileFontStrike.pScalerContext != 0) {
                this.scaler.invalidateScalerContext(fileFontStrike.pScalerContext);
            }
        }
        if (this.scaler != null) {
            this.scaler.disposeScaler();
        }
        this.scaler = FontScaler.getNullScaler();
    }

    @Override // sun.font.PhysicalFont
    StrikeMetrics getFontMetrics(long j2) {
        try {
            return getScaler().getFontMetrics(j2);
        } catch (FontScalerException e2) {
            this.scaler = FontScaler.getNullScaler();
            return getFontMetrics(j2);
        }
    }

    @Override // sun.font.PhysicalFont
    float getGlyphAdvance(long j2, int i2) {
        try {
            return getScaler().getGlyphAdvance(j2, i2);
        } catch (FontScalerException e2) {
            this.scaler = FontScaler.getNullScaler();
            return getGlyphAdvance(j2, i2);
        }
    }

    @Override // sun.font.PhysicalFont
    void getGlyphMetrics(long j2, int i2, Point2D.Float r10) {
        try {
            getScaler().getGlyphMetrics(j2, i2, r10);
        } catch (FontScalerException e2) {
            this.scaler = FontScaler.getNullScaler();
            getGlyphMetrics(j2, i2, r10);
        }
    }

    @Override // sun.font.PhysicalFont
    long getGlyphImage(long j2, int i2) {
        try {
            return getScaler().getGlyphImage(j2, i2);
        } catch (FontScalerException e2) {
            this.scaler = FontScaler.getNullScaler();
            return getGlyphImage(j2, i2);
        }
    }

    @Override // sun.font.PhysicalFont
    Rectangle2D.Float getGlyphOutlineBounds(long j2, int i2) {
        try {
            return getScaler().getGlyphOutlineBounds(j2, i2);
        } catch (FontScalerException e2) {
            this.scaler = FontScaler.getNullScaler();
            return getGlyphOutlineBounds(j2, i2);
        }
    }

    @Override // sun.font.PhysicalFont
    GeneralPath getGlyphOutline(long j2, int i2, float f2, float f3) {
        try {
            return getScaler().getGlyphOutline(j2, i2, f2, f3);
        } catch (FontScalerException e2) {
            this.scaler = FontScaler.getNullScaler();
            return getGlyphOutline(j2, i2, f2, f3);
        }
    }

    @Override // sun.font.PhysicalFont
    GeneralPath getGlyphVectorOutline(long j2, int[] iArr, int i2, float f2, float f3) {
        try {
            return getScaler().getGlyphVectorOutline(j2, iArr, i2, f2, f3);
        } catch (FontScalerException e2) {
            this.scaler = FontScaler.getNullScaler();
            return getGlyphVectorOutline(j2, iArr, i2, f2, f3);
        }
    }

    @Override // sun.font.Font2D
    protected long getUnitsPerEm() {
        return getScaler().getUnitsPerEm();
    }

    /* loaded from: rt.jar:sun/font/FileFont$CreatedFontFileDisposerRecord.class */
    private static class CreatedFontFileDisposerRecord implements DisposerRecord {
        File fontFile;
        CreatedFontTracker tracker;

        private CreatedFontFileDisposerRecord(File file, CreatedFontTracker createdFontTracker) {
            this.fontFile = null;
            this.fontFile = file;
            this.tracker = createdFontTracker;
        }

        @Override // sun.java2d.DisposerRecord
        public void dispose() {
            AccessController.doPrivileged(new PrivilegedAction() { // from class: sun.font.FileFont.CreatedFontFileDisposerRecord.1
                @Override // java.security.PrivilegedAction
                /* renamed from: run */
                public Object run2() {
                    if (CreatedFontFileDisposerRecord.this.fontFile != null) {
                        try {
                            if (CreatedFontFileDisposerRecord.this.tracker != null) {
                                CreatedFontFileDisposerRecord.this.tracker.subBytes((int) CreatedFontFileDisposerRecord.this.fontFile.length());
                            }
                            CreatedFontFileDisposerRecord.this.fontFile.delete();
                            SunFontManager.getInstance().tmpFontFiles.remove(CreatedFontFileDisposerRecord.this.fontFile);
                            return null;
                        } catch (Exception e2) {
                            return null;
                        }
                    }
                    return null;
                }
            });
        }
    }

    protected String getPublicFileName() {
        Boolean bool;
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager == null) {
            return this.platName;
        }
        boolean z2 = true;
        try {
            securityManager.checkPropertyAccess("java.io.tmpdir");
        } catch (SecurityException e2) {
            z2 = false;
        }
        if (z2) {
            return this.platName;
        }
        final File file = new File(this.platName);
        Boolean bool2 = Boolean.FALSE;
        try {
            bool = (Boolean) AccessController.doPrivileged(new PrivilegedExceptionAction<Boolean>() { // from class: sun.font.FileFont.1
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.security.PrivilegedExceptionAction
                public Boolean run() {
                    try {
                        String canonicalPath = new File(System.getProperty("java.io.tmpdir")).getCanonicalPath();
                        String canonicalPath2 = file.getCanonicalPath();
                        return Boolean.valueOf(canonicalPath2 == null || canonicalPath2.startsWith(canonicalPath));
                    } catch (IOException e3) {
                        return Boolean.TRUE;
                    }
                }
            });
        } catch (PrivilegedActionException e3) {
            bool = Boolean.TRUE;
        }
        return bool.booleanValue() ? "temp file" : this.platName;
    }
}
