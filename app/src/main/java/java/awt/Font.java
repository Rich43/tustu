package java.awt;

import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.font.LineMetrics;
import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.peer.FontPeer;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FilePermission;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.lang.ref.SoftReference;
import java.nio.file.Files;
import java.nio.file.attribute.FileAttribute;
import java.security.AccessController;
import java.security.PrivilegedExceptionAction;
import java.text.AttributedCharacterIterator;
import java.text.CharacterIterator;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Map;
import javax.swing.Action;
import sun.font.AttributeMap;
import sun.font.AttributeValues;
import sun.font.CompositeFont;
import sun.font.CreatedFontTracker;
import sun.font.EAttribute;
import sun.font.Font2D;
import sun.font.Font2DHandle;
import sun.font.FontAccess;
import sun.font.FontLineMetrics;
import sun.font.FontManager;
import sun.font.FontManagerFactory;
import sun.font.FontUtilities;
import sun.font.GlyphLayout;
import sun.font.StandardGlyphVector;

/* loaded from: rt.jar:java/awt/Font.class */
public class Font implements Serializable {
    private Hashtable<Object, Object> fRequestedAttributes;
    public static final String DIALOG = "Dialog";
    public static final String DIALOG_INPUT = "DialogInput";
    public static final String SANS_SERIF = "SansSerif";
    public static final String SERIF = "Serif";
    public static final String MONOSPACED = "Monospaced";
    public static final int PLAIN = 0;
    public static final int BOLD = 1;
    public static final int ITALIC = 2;
    public static final int ROMAN_BASELINE = 0;
    public static final int CENTER_BASELINE = 1;
    public static final int HANGING_BASELINE = 2;
    public static final int TRUETYPE_FONT = 0;
    public static final int TYPE1_FONT = 1;
    protected String name;
    protected int style;
    protected int size;
    protected float pointSize;
    private transient FontPeer peer;
    private transient long pData;
    private transient Font2DHandle font2DHandle;
    private transient AttributeValues values;
    private transient boolean hasLayoutAttributes;
    private transient boolean createdFont;
    private transient boolean nonIdentityTx;
    private static final AffineTransform identityTx;
    private static final long serialVersionUID = -4206021311591459213L;
    private static final int RECOGNIZED_MASK;
    private static final int PRIMARY_MASK;
    private static final int SECONDARY_MASK;
    private static final int LAYOUT_MASK;
    private static final int EXTRA_MASK;
    private static final float[] ssinfo;
    transient int hash;
    private int fontSerializedDataVersion;
    private transient SoftReference<FontLineMetrics> flmref;
    public static final int LAYOUT_LEFT_TO_RIGHT = 0;
    public static final int LAYOUT_RIGHT_TO_LEFT = 1;
    public static final int LAYOUT_NO_START_CONTEXT = 2;
    public static final int LAYOUT_NO_LIMIT_CONTEXT = 4;

    private static native void initIDs();

    /* loaded from: rt.jar:java/awt/Font$FontAccessImpl.class */
    private static class FontAccessImpl extends FontAccess {
        private FontAccessImpl() {
        }

        @Override // sun.font.FontAccess
        public Font2D getFont2D(Font font) {
            return font.getFont2D();
        }

        @Override // sun.font.FontAccess
        public void setFont2D(Font font, Font2DHandle font2DHandle) {
            font.font2DHandle = font2DHandle;
        }

        @Override // sun.font.FontAccess
        public void setCreatedFont(Font font) {
            font.createdFont = true;
        }

        @Override // sun.font.FontAccess
        public boolean isCreatedFont(Font font) {
            return font.createdFont;
        }
    }

    static {
        Toolkit.loadLibraries();
        initIDs();
        FontAccess.setFontAccess(new FontAccessImpl());
        identityTx = new AffineTransform();
        RECOGNIZED_MASK = AttributeValues.MASK_ALL & (AttributeValues.getMask(EAttribute.EFONT) ^ (-1));
        PRIMARY_MASK = AttributeValues.getMask(EAttribute.EFAMILY, EAttribute.EWEIGHT, EAttribute.EWIDTH, EAttribute.EPOSTURE, EAttribute.ESIZE, EAttribute.ETRANSFORM, EAttribute.ESUPERSCRIPT, EAttribute.ETRACKING);
        SECONDARY_MASK = RECOGNIZED_MASK & (PRIMARY_MASK ^ (-1));
        LAYOUT_MASK = AttributeValues.getMask(EAttribute.ECHAR_REPLACEMENT, EAttribute.EFOREGROUND, EAttribute.EBACKGROUND, EAttribute.EUNDERLINE, EAttribute.ESTRIKETHROUGH, EAttribute.ERUN_DIRECTION, EAttribute.EBIDI_EMBEDDING, EAttribute.EJUSTIFICATION, EAttribute.EINPUT_METHOD_HIGHLIGHT, EAttribute.EINPUT_METHOD_UNDERLINE, EAttribute.ESWAP_COLORS, EAttribute.ENUMERIC_SHAPING, EAttribute.EKERNING, EAttribute.ELIGATURES, EAttribute.ETRACKING, EAttribute.ESUPERSCRIPT);
        EXTRA_MASK = AttributeValues.getMask(EAttribute.ETRANSFORM, EAttribute.ESUPERSCRIPT, EAttribute.EWIDTH);
        ssinfo = new float[]{0.0f, 0.375f, 0.625f, 0.7916667f, 0.9027778f, 0.9768519f, 1.0262346f, 1.0591564f};
    }

    @Deprecated
    public FontPeer getPeer() {
        return getPeer_NoClientCode();
    }

    final FontPeer getPeer_NoClientCode() {
        if (this.peer == null) {
            this.peer = Toolkit.getDefaultToolkit().getFontPeer(this.name, this.style);
        }
        return this.peer;
    }

    private AttributeValues getAttributeValues() {
        if (this.values == null) {
            AttributeValues attributeValues = new AttributeValues();
            attributeValues.setFamily(this.name);
            attributeValues.setSize(this.pointSize);
            if ((this.style & 1) != 0) {
                attributeValues.setWeight(2.0f);
            }
            if ((this.style & 2) != 0) {
                attributeValues.setPosture(0.2f);
            }
            attributeValues.defineAll(PRIMARY_MASK);
            this.values = attributeValues;
        }
        return this.values;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Font2D getFont2D() {
        FontManager fontManagerFactory = FontManagerFactory.getInstance();
        if (fontManagerFactory.usingPerAppContextComposites() && this.font2DHandle != null && (this.font2DHandle.font2D instanceof CompositeFont) && ((CompositeFont) this.font2DHandle.font2D).isStdComposite()) {
            return fontManagerFactory.findFont2D(this.name, this.style, 2);
        }
        if (this.font2DHandle == null) {
            this.font2DHandle = fontManagerFactory.findFont2D(this.name, this.style, 2).handle;
        }
        return this.font2DHandle.font2D;
    }

    public Font(String str, int i2, int i3) {
        this.createdFont = false;
        this.fontSerializedDataVersion = 1;
        this.name = str != null ? str : Action.DEFAULT;
        this.style = (i2 & (-4)) == 0 ? i2 : 0;
        this.size = i3;
        this.pointSize = i3;
    }

    private Font(String str, int i2, float f2) {
        this.createdFont = false;
        this.fontSerializedDataVersion = 1;
        this.name = str != null ? str : Action.DEFAULT;
        this.style = (i2 & (-4)) == 0 ? i2 : 0;
        this.size = (int) (f2 + 0.5d);
        this.pointSize = f2;
    }

    private Font(String str, int i2, float f2, boolean z2, Font2DHandle font2DHandle) {
        this(str, i2, f2);
        this.createdFont = z2;
        if (z2) {
            if ((font2DHandle.font2D instanceof CompositeFont) && font2DHandle.font2D.getStyle() != i2) {
                this.font2DHandle = FontManagerFactory.getInstance().getNewComposite(null, i2, font2DHandle);
            } else {
                this.font2DHandle = font2DHandle;
            }
        }
    }

    private Font(File file, int i2, boolean z2, CreatedFontTracker createdFontTracker) throws FontFormatException {
        this.createdFont = false;
        this.fontSerializedDataVersion = 1;
        this.createdFont = true;
        this.font2DHandle = FontManagerFactory.getInstance().createFont2D(file, i2, z2, createdFontTracker).handle;
        this.name = this.font2DHandle.font2D.getFontName(Locale.getDefault());
        this.style = 0;
        this.size = 1;
        this.pointSize = 1.0f;
    }

    private Font(AttributeValues attributeValues, String str, int i2, boolean z2, Font2DHandle font2DHandle) {
        this.createdFont = false;
        this.fontSerializedDataVersion = 1;
        this.createdFont = z2;
        if (z2) {
            this.font2DHandle = font2DHandle;
            String family = null;
            if (str != null) {
                family = attributeValues.getFamily();
                if (str.equals(family)) {
                    family = null;
                }
            }
            int i3 = 0;
            if (i2 == -1) {
                i3 = -1;
            } else {
                i3 = attributeValues.getWeight() >= 2.0f ? 1 : i3;
                i3 = attributeValues.getPosture() >= 0.2f ? i3 | 2 : i3;
                if (i2 == i3) {
                    i3 = -1;
                }
            }
            if (font2DHandle.font2D instanceof CompositeFont) {
                if (i3 != -1 || family != null) {
                    this.font2DHandle = FontManagerFactory.getInstance().getNewComposite(family, i3, font2DHandle);
                }
            } else if (family != null) {
                this.createdFont = false;
                this.font2DHandle = null;
            }
        }
        initFromValues(attributeValues);
    }

    public Font(Map<? extends AttributedCharacterIterator.Attribute, ?> map) {
        this.createdFont = false;
        this.fontSerializedDataVersion = 1;
        initFromValues(AttributeValues.fromMap(map, RECOGNIZED_MASK));
    }

    protected Font(Font font) {
        this.createdFont = false;
        this.fontSerializedDataVersion = 1;
        if (font.values != null) {
            initFromValues(font.getAttributeValues().m5731clone());
        } else {
            this.name = font.name;
            this.style = font.style;
            this.size = font.size;
            this.pointSize = font.pointSize;
        }
        this.font2DHandle = font.font2DHandle;
        this.createdFont = font.createdFont;
    }

    private void initFromValues(AttributeValues attributeValues) {
        this.values = attributeValues;
        attributeValues.defineAll(PRIMARY_MASK);
        this.name = attributeValues.getFamily();
        this.pointSize = attributeValues.getSize();
        this.size = (int) (attributeValues.getSize() + 0.5d);
        if (attributeValues.getWeight() >= 2.0f) {
            this.style |= 1;
        }
        if (attributeValues.getPosture() >= 0.2f) {
            this.style |= 2;
        }
        this.nonIdentityTx = attributeValues.anyNonDefault(EXTRA_MASK);
        this.hasLayoutAttributes = attributeValues.anyNonDefault(LAYOUT_MASK);
    }

    public static Font getFont(Map<? extends AttributedCharacterIterator.Attribute, ?> map) {
        if ((map instanceof AttributeMap) && ((AttributeMap) map).getValues() != null) {
            AttributeValues values = ((AttributeMap) map).getValues();
            if (values.isNonDefault(EAttribute.EFONT)) {
                Font font = values.getFont();
                if (!values.anyDefined(SECONDARY_MASK)) {
                    return font;
                }
                AttributeValues attributeValuesClone = font.getAttributeValues().m5731clone();
                attributeValuesClone.merge(map, SECONDARY_MASK);
                return new Font(attributeValuesClone, font.name, font.style, font.createdFont, font.font2DHandle);
            }
            return new Font(map);
        }
        Font font2 = (Font) map.get(TextAttribute.FONT);
        if (font2 != null) {
            if (map.size() > 1) {
                AttributeValues attributeValuesClone2 = font2.getAttributeValues().m5731clone();
                attributeValuesClone2.merge(map, SECONDARY_MASK);
                return new Font(attributeValuesClone2, font2.name, font2.style, font2.createdFont, font2.font2DHandle);
            }
            return font2;
        }
        return new Font(map);
    }

    private static boolean hasTempPermission() {
        if (System.getSecurityManager() == null) {
            return true;
        }
        boolean z2 = false;
        try {
            Files.createTempFile("+~JT", ".tmp", new FileAttribute[0]).toFile().delete();
            z2 = true;
        } catch (Throwable th) {
        }
        return z2;
    }

    public static Font createFont(int i2, InputStream inputStream) throws IOException, FontFormatException {
        if (hasTempPermission()) {
            return createFont0(i2, inputStream, null);
        }
        CreatedFontTracker tracker = CreatedFontTracker.getTracker();
        try {
            try {
                boolean zAcquirePermit = tracker.acquirePermit();
                if (!zAcquirePermit) {
                    throw new IOException("Timed out waiting for resources.");
                }
                Font fontCreateFont0 = createFont0(i2, inputStream, tracker);
                if (zAcquirePermit) {
                    tracker.releasePermit();
                }
                return fontCreateFont0;
            } catch (InterruptedException e2) {
                throw new IOException("Problem reading font data.");
            }
        } catch (Throwable th) {
            if (0 != 0) {
                tracker.releasePermit();
            }
            throw th;
        }
    }

    /* JADX WARN: Finally extract failed */
    private static Font createFont0(int i2, InputStream inputStream, CreatedFontTracker createdFontTracker) throws IOException, FontFormatException {
        if (i2 != 0 && i2 != 1) {
            throw new IllegalArgumentException("font format not recognized");
        }
        try {
            final File file = (File) AccessController.doPrivileged(new PrivilegedExceptionAction<File>() { // from class: java.awt.Font.1
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.security.PrivilegedExceptionAction
                public File run() throws IOException {
                    return Files.createTempFile("+~JF", ".tmp", new FileAttribute[0]).toFile();
                }
            });
            if (createdFontTracker != null) {
                createdFontTracker.add(file);
            }
            int i3 = 0;
            try {
                OutputStream outputStream = (OutputStream) AccessController.doPrivileged(new PrivilegedExceptionAction<OutputStream>() { // from class: java.awt.Font.2
                    /* JADX WARN: Can't rename method to resolve collision */
                    @Override // java.security.PrivilegedExceptionAction
                    public OutputStream run() throws IOException {
                        return new FileOutputStream(file);
                    }
                });
                if (createdFontTracker != null) {
                    createdFontTracker.set(file, outputStream);
                }
                try {
                    byte[] bArr = new byte[8192];
                    while (true) {
                        int i4 = inputStream.read(bArr);
                        if (i4 >= 0) {
                            if (createdFontTracker != null) {
                                if (i3 + i4 > 33554432) {
                                    throw new IOException("File too big.");
                                }
                                if (i3 + createdFontTracker.getNumBytes() > 335544320) {
                                    throw new IOException("Total files too big.");
                                }
                                i3 += i4;
                                createdFontTracker.addBytes(i4);
                            }
                            outputStream.write(bArr, 0, i4);
                        } else {
                            outputStream.close();
                            Font font = new Font(file, i2, true, createdFontTracker);
                            if (createdFontTracker != null) {
                                createdFontTracker.remove(file);
                            }
                            if (1 == 0) {
                                if (createdFontTracker != null) {
                                    createdFontTracker.subBytes(i3);
                                }
                                AccessController.doPrivileged(new PrivilegedExceptionAction<Void>() { // from class: java.awt.Font.3
                                    /* JADX WARN: Can't rename method to resolve collision */
                                    @Override // java.security.PrivilegedExceptionAction
                                    public Void run() {
                                        file.delete();
                                        return null;
                                    }
                                });
                            }
                            return font;
                        }
                    }
                } catch (Throwable th) {
                    outputStream.close();
                    throw th;
                }
            } catch (Throwable th2) {
                if (createdFontTracker != null) {
                    createdFontTracker.remove(file);
                }
                if (0 == 0) {
                    if (createdFontTracker != null) {
                        createdFontTracker.subBytes(0);
                    }
                    AccessController.doPrivileged(new PrivilegedExceptionAction<Void>() { // from class: java.awt.Font.3
                        /* JADX WARN: Can't rename method to resolve collision */
                        @Override // java.security.PrivilegedExceptionAction
                        public Void run() {
                            file.delete();
                            return null;
                        }
                    });
                }
                throw th2;
            }
        } catch (Throwable th3) {
            if (th3 instanceof FontFormatException) {
                throw ((FontFormatException) th3);
            }
            if (th3 instanceof IOException) {
                throw ((IOException) th3);
            }
            Throwable cause = th3.getCause();
            if (cause instanceof FontFormatException) {
                throw ((FontFormatException) cause);
            }
            throw new IOException("Problem reading font data.");
        }
    }

    public static Font createFont(int i2, File file) throws IOException, FontFormatException {
        File file2 = new File(file.getPath());
        if (i2 != 0 && i2 != 1) {
            throw new IllegalArgumentException("font format not recognized");
        }
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkPermission(new FilePermission(file2.getPath(), "read"));
        }
        if (!file2.canRead()) {
            throw new IOException("Can't read " + ((Object) file2));
        }
        return new Font(file2, i2, false, null);
    }

    public AffineTransform getTransform() {
        if (this.nonIdentityTx) {
            AttributeValues attributeValues = getAttributeValues();
            AffineTransform affineTransform = attributeValues.isNonDefault(EAttribute.ETRANSFORM) ? new AffineTransform(attributeValues.getTransform()) : new AffineTransform();
            if (attributeValues.getSuperscript() != 0) {
                int superscript = attributeValues.getSuperscript();
                double d2 = 0.0d;
                int i2 = 0;
                boolean z2 = superscript > 0;
                int i3 = z2 ? -1 : 1;
                int i4 = z2 ? superscript : -superscript;
                while ((i4 & 7) > i2) {
                    int i5 = i4 & 7;
                    d2 += i3 * (ssinfo[i5] - ssinfo[i2]);
                    i4 >>= 3;
                    i3 = -i3;
                    i2 = i5;
                }
                double dPow = Math.pow(0.6666666666666666d, i2);
                affineTransform.preConcatenate(AffineTransform.getTranslateInstance(0.0d, d2 * this.pointSize));
                affineTransform.scale(dPow, dPow);
            }
            if (attributeValues.isNonDefault(EAttribute.EWIDTH)) {
                affineTransform.scale(attributeValues.getWidth(), 1.0d);
            }
            return affineTransform;
        }
        return new AffineTransform();
    }

    public String getFamily() {
        return getFamily_NoClientCode();
    }

    final String getFamily_NoClientCode() {
        return getFamily(Locale.getDefault());
    }

    public String getFamily(Locale locale) {
        if (locale == null) {
            throw new NullPointerException("null locale doesn't mean default");
        }
        return getFont2D().getFamilyName(locale);
    }

    public String getPSName() {
        return getFont2D().getPostscriptName();
    }

    public String getName() {
        return this.name;
    }

    public String getFontName() {
        return getFontName(Locale.getDefault());
    }

    public String getFontName(Locale locale) {
        if (locale == null) {
            throw new NullPointerException("null locale doesn't mean default");
        }
        return getFont2D().getFontName(locale);
    }

    public int getStyle() {
        return this.style;
    }

    public int getSize() {
        return this.size;
    }

    public float getSize2D() {
        return this.pointSize;
    }

    public boolean isPlain() {
        return this.style == 0;
    }

    public boolean isBold() {
        return (this.style & 1) != 0;
    }

    public boolean isItalic() {
        return (this.style & 2) != 0;
    }

    public boolean isTransformed() {
        return this.nonIdentityTx;
    }

    public boolean hasLayoutAttributes() {
        return this.hasLayoutAttributes;
    }

    public static Font getFont(String str) {
        return getFont(str, null);
    }

    public static Font decode(String str) {
        String strSubstring;
        int iIntValue = 12;
        int i2 = 0;
        if (str == null) {
            return new Font(DIALOG, 0, 12);
        }
        char c2 = str.lastIndexOf(45) > str.lastIndexOf(32) ? '-' : ' ';
        int iLastIndexOf = str.lastIndexOf(c2);
        int iLastIndexOf2 = str.lastIndexOf(c2, iLastIndexOf - 1);
        int length = str.length();
        if (iLastIndexOf > 0 && iLastIndexOf + 1 < length) {
            try {
                iIntValue = Integer.valueOf(str.substring(iLastIndexOf + 1)).intValue();
                if (iIntValue <= 0) {
                    iIntValue = 12;
                }
            } catch (NumberFormatException e2) {
                iLastIndexOf2 = iLastIndexOf;
                iLastIndexOf = length;
                if (str.charAt(iLastIndexOf - 1) == c2) {
                    iLastIndexOf--;
                }
            }
        }
        if (iLastIndexOf2 >= 0 && iLastIndexOf2 + 1 < length) {
            String lowerCase = str.substring(iLastIndexOf2 + 1, iLastIndexOf).toLowerCase(Locale.ENGLISH);
            if (lowerCase.equals("bolditalic")) {
                i2 = 3;
            } else if (lowerCase.equals("italic")) {
                i2 = 2;
            } else if (lowerCase.equals("bold")) {
                i2 = 1;
            } else if (lowerCase.equals("plain")) {
                i2 = 0;
            } else {
                iLastIndexOf2 = iLastIndexOf;
                if (str.charAt(iLastIndexOf2 - 1) == c2) {
                    iLastIndexOf2--;
                }
            }
            strSubstring = str.substring(0, iLastIndexOf2);
        } else {
            int i3 = length;
            if (iLastIndexOf2 > 0) {
                i3 = iLastIndexOf2;
            } else if (iLastIndexOf > 0) {
                i3 = iLastIndexOf;
            }
            if (i3 > 0 && str.charAt(i3 - 1) == c2) {
                i3--;
            }
            strSubstring = str.substring(0, i3);
        }
        return new Font(strSubstring, i2, iIntValue);
    }

    public static Font getFont(String str, Font font) {
        String property = null;
        try {
            property = System.getProperty(str);
        } catch (SecurityException e2) {
        }
        if (property == null) {
            return font;
        }
        return decode(property);
    }

    public int hashCode() {
        if (this.hash == 0) {
            this.hash = (this.name.hashCode() ^ this.style) ^ this.size;
            if (this.nonIdentityTx && this.values != null && this.values.getTransform() != null) {
                this.hash ^= this.values.getTransform().hashCode();
            }
        }
        return this.hash;
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj != null) {
            try {
                Font font = (Font) obj;
                if (this.size == font.size && this.style == font.style && this.nonIdentityTx == font.nonIdentityTx && this.hasLayoutAttributes == font.hasLayoutAttributes && this.pointSize == font.pointSize && this.name.equals(font.name)) {
                    if (this.values == null) {
                        if (font.values == null) {
                            return true;
                        }
                        return getAttributeValues().equals(font.values);
                    }
                    return this.values.equals(font.getAttributeValues());
                }
                return false;
            } catch (ClassCastException e2) {
                return false;
            }
        }
        return false;
    }

    public String toString() {
        String str;
        if (isBold()) {
            str = isItalic() ? "bolditalic" : "bold";
        } else {
            str = isItalic() ? "italic" : "plain";
        }
        return getClass().getName() + "[family=" + getFamily() + ",name=" + this.name + ",style=" + str + ",size=" + this.size + "]";
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException, ClassNotFoundException {
        if (this.values != null) {
            synchronized (this.values) {
                this.fRequestedAttributes = this.values.toSerializableHashtable();
                objectOutputStream.defaultWriteObject();
                this.fRequestedAttributes = null;
            }
            return;
        }
        objectOutputStream.defaultWriteObject();
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        if (this.pointSize == 0.0f) {
            this.pointSize = this.size;
        }
        if (this.fRequestedAttributes != null) {
            try {
                try {
                    this.values = getAttributeValues();
                    AttributeValues attributeValuesFromSerializableHashtable = AttributeValues.fromSerializableHashtable(this.fRequestedAttributes);
                    if (!AttributeValues.is16Hashtable(this.fRequestedAttributes)) {
                        attributeValuesFromSerializableHashtable.unsetDefault();
                    }
                    this.values = getAttributeValues().merge(attributeValuesFromSerializableHashtable);
                    this.nonIdentityTx = this.values.anyNonDefault(EXTRA_MASK);
                    this.hasLayoutAttributes = this.values.anyNonDefault(LAYOUT_MASK);
                    this.fRequestedAttributes = null;
                } catch (Throwable th) {
                    throw new IOException(th);
                }
            } catch (Throwable th2) {
                this.fRequestedAttributes = null;
                throw th2;
            }
        }
    }

    public int getNumGlyphs() {
        return getFont2D().getNumGlyphs();
    }

    public int getMissingGlyphCode() {
        return getFont2D().getMissingGlyphCode();
    }

    public byte getBaselineFor(char c2) {
        return getFont2D().getBaselineFor(c2);
    }

    public Map<TextAttribute, ?> getAttributes() {
        return new AttributeMap(getAttributeValues());
    }

    public AttributedCharacterIterator.Attribute[] getAvailableAttributes() {
        return new AttributedCharacterIterator.Attribute[]{TextAttribute.FAMILY, TextAttribute.WEIGHT, TextAttribute.WIDTH, TextAttribute.POSTURE, TextAttribute.SIZE, TextAttribute.TRANSFORM, TextAttribute.SUPERSCRIPT, TextAttribute.CHAR_REPLACEMENT, TextAttribute.FOREGROUND, TextAttribute.BACKGROUND, TextAttribute.UNDERLINE, TextAttribute.STRIKETHROUGH, TextAttribute.RUN_DIRECTION, TextAttribute.BIDI_EMBEDDING, TextAttribute.JUSTIFICATION, TextAttribute.INPUT_METHOD_HIGHLIGHT, TextAttribute.INPUT_METHOD_UNDERLINE, TextAttribute.SWAP_COLORS, TextAttribute.NUMERIC_SHAPING, TextAttribute.KERNING, TextAttribute.LIGATURES, TextAttribute.TRACKING};
    }

    public Font deriveFont(int i2, float f2) {
        if (this.values == null) {
            return new Font(this.name, i2, f2, this.createdFont, this.font2DHandle);
        }
        AttributeValues attributeValuesClone = getAttributeValues().m5731clone();
        int i3 = this.style != i2 ? this.style : -1;
        applyStyle(i2, attributeValuesClone);
        attributeValuesClone.setSize(f2);
        return new Font(attributeValuesClone, (String) null, i3, this.createdFont, this.font2DHandle);
    }

    public Font deriveFont(int i2, AffineTransform affineTransform) {
        AttributeValues attributeValuesClone = getAttributeValues().m5731clone();
        int i3 = this.style != i2 ? this.style : -1;
        applyStyle(i2, attributeValuesClone);
        applyTransform(affineTransform, attributeValuesClone);
        return new Font(attributeValuesClone, (String) null, i3, this.createdFont, this.font2DHandle);
    }

    public Font deriveFont(float f2) {
        if (this.values == null) {
            return new Font(this.name, this.style, f2, this.createdFont, this.font2DHandle);
        }
        AttributeValues attributeValuesClone = getAttributeValues().m5731clone();
        attributeValuesClone.setSize(f2);
        return new Font(attributeValuesClone, (String) null, -1, this.createdFont, this.font2DHandle);
    }

    public Font deriveFont(AffineTransform affineTransform) {
        AttributeValues attributeValuesClone = getAttributeValues().m5731clone();
        applyTransform(affineTransform, attributeValuesClone);
        return new Font(attributeValuesClone, (String) null, -1, this.createdFont, this.font2DHandle);
    }

    public Font deriveFont(int i2) {
        if (this.values == null) {
            return new Font(this.name, i2, this.size, this.createdFont, this.font2DHandle);
        }
        AttributeValues attributeValuesClone = getAttributeValues().m5731clone();
        int i3 = this.style != i2 ? this.style : -1;
        applyStyle(i2, attributeValuesClone);
        return new Font(attributeValuesClone, (String) null, i3, this.createdFont, this.font2DHandle);
    }

    public Font deriveFont(Map<? extends AttributedCharacterIterator.Attribute, ?> map) {
        if (map == null) {
            return this;
        }
        AttributeValues attributeValuesClone = getAttributeValues().m5731clone();
        attributeValuesClone.merge(map, RECOGNIZED_MASK);
        return new Font(attributeValuesClone, this.name, this.style, this.createdFont, this.font2DHandle);
    }

    public boolean canDisplay(char c2) {
        return getFont2D().canDisplay(c2);
    }

    public boolean canDisplay(int i2) {
        if (!Character.isValidCodePoint(i2)) {
            throw new IllegalArgumentException("invalid code point: " + Integer.toHexString(i2));
        }
        return getFont2D().canDisplay(i2);
    }

    public int canDisplayUpTo(String str) {
        Font2D font2D = getFont2D();
        int length = str.length();
        int i2 = 0;
        while (i2 < length) {
            char cCharAt = str.charAt(i2);
            if (!font2D.canDisplay(cCharAt)) {
                if (!Character.isHighSurrogate(cCharAt)) {
                    return i2;
                }
                if (!font2D.canDisplay(str.codePointAt(i2))) {
                    return i2;
                }
                i2++;
            }
            i2++;
        }
        return -1;
    }

    public int canDisplayUpTo(char[] cArr, int i2, int i3) {
        Font2D font2D = getFont2D();
        int i4 = i2;
        while (i4 < i3) {
            char c2 = cArr[i4];
            if (!font2D.canDisplay(c2)) {
                if (!Character.isHighSurrogate(c2)) {
                    return i4;
                }
                if (!font2D.canDisplay(Character.codePointAt(cArr, i4, i3))) {
                    return i4;
                }
                i4++;
            }
            i4++;
        }
        return -1;
    }

    public int canDisplayUpTo(CharacterIterator characterIterator, int i2, int i3) {
        Font2D font2D = getFont2D();
        char index = characterIterator.setIndex(i2);
        int i4 = i2;
        while (i4 < i3) {
            if (!font2D.canDisplay(index)) {
                if (!Character.isHighSurrogate(index)) {
                    return i4;
                }
                char next = characterIterator.next();
                if (!Character.isLowSurrogate(next)) {
                    return i4;
                }
                if (!font2D.canDisplay(Character.toCodePoint(index, next))) {
                    return i4;
                }
                i4++;
            }
            i4++;
            index = characterIterator.next();
        }
        return -1;
    }

    public float getItalicAngle() {
        return getItalicAngle(null);
    }

    private float getItalicAngle(FontRenderContext fontRenderContext) {
        Object antiAliasingHint;
        Object fractionalMetricsHint;
        if (fontRenderContext == null) {
            antiAliasingHint = RenderingHints.VALUE_TEXT_ANTIALIAS_OFF;
            fractionalMetricsHint = RenderingHints.VALUE_FRACTIONALMETRICS_OFF;
        } else {
            antiAliasingHint = fontRenderContext.getAntiAliasingHint();
            fractionalMetricsHint = fontRenderContext.getFractionalMetricsHint();
        }
        return getFont2D().getItalicAngle(this, identityTx, antiAliasingHint, fractionalMetricsHint);
    }

    public boolean hasUniformLineMetrics() {
        return false;
    }

    /* JADX WARN: Removed duplicated region for block: B:8:0x0023  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private sun.font.FontLineMetrics defaultLineMetrics(java.awt.font.FontRenderContext r16) {
        /*
            Method dump skipped, instructions count: 397
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: java.awt.Font.defaultLineMetrics(java.awt.font.FontRenderContext):sun.font.FontLineMetrics");
    }

    public LineMetrics getLineMetrics(String str, FontRenderContext fontRenderContext) {
        FontLineMetrics fontLineMetricsDefaultLineMetrics = defaultLineMetrics(fontRenderContext);
        fontLineMetricsDefaultLineMetrics.numchars = str.length();
        return fontLineMetricsDefaultLineMetrics;
    }

    public LineMetrics getLineMetrics(String str, int i2, int i3, FontRenderContext fontRenderContext) {
        FontLineMetrics fontLineMetricsDefaultLineMetrics = defaultLineMetrics(fontRenderContext);
        int i4 = i3 - i2;
        fontLineMetricsDefaultLineMetrics.numchars = i4 < 0 ? 0 : i4;
        return fontLineMetricsDefaultLineMetrics;
    }

    public LineMetrics getLineMetrics(char[] cArr, int i2, int i3, FontRenderContext fontRenderContext) {
        FontLineMetrics fontLineMetricsDefaultLineMetrics = defaultLineMetrics(fontRenderContext);
        int i4 = i3 - i2;
        fontLineMetricsDefaultLineMetrics.numchars = i4 < 0 ? 0 : i4;
        return fontLineMetricsDefaultLineMetrics;
    }

    public LineMetrics getLineMetrics(CharacterIterator characterIterator, int i2, int i3, FontRenderContext fontRenderContext) {
        FontLineMetrics fontLineMetricsDefaultLineMetrics = defaultLineMetrics(fontRenderContext);
        int i4 = i3 - i2;
        fontLineMetricsDefaultLineMetrics.numchars = i4 < 0 ? 0 : i4;
        return fontLineMetricsDefaultLineMetrics;
    }

    public Rectangle2D getStringBounds(String str, FontRenderContext fontRenderContext) {
        char[] charArray = str.toCharArray();
        return getStringBounds(charArray, 0, charArray.length, fontRenderContext);
    }

    public Rectangle2D getStringBounds(String str, int i2, int i3, FontRenderContext fontRenderContext) {
        return getStringBounds(str.substring(i2, i3), fontRenderContext);
    }

    public Rectangle2D getStringBounds(char[] cArr, int i2, int i3, FontRenderContext fontRenderContext) {
        if (i2 < 0) {
            throw new IndexOutOfBoundsException("beginIndex: " + i2);
        }
        if (i3 > cArr.length) {
            throw new IndexOutOfBoundsException("limit: " + i3);
        }
        if (i2 > i3) {
            throw new IndexOutOfBoundsException("range length: " + (i3 - i2));
        }
        boolean z2 = this.values == null || (this.values.getKerning() == 0 && this.values.getLigatures() == 0 && this.values.getBaselineTransform() == null);
        if (z2) {
            z2 = !FontUtilities.isComplexText(cArr, i2, i3);
        }
        if (z2) {
            return new StandardGlyphVector(this, cArr, i2, i3 - i2, fontRenderContext).getLogicalBounds();
        }
        TextLayout textLayout = new TextLayout(new String(cArr, i2, i3 - i2), this, fontRenderContext);
        return new Rectangle2D.Float(0.0f, -textLayout.getAscent(), textLayout.getAdvance(), textLayout.getAscent() + textLayout.getDescent() + textLayout.getLeading());
    }

    public Rectangle2D getStringBounds(CharacterIterator characterIterator, int i2, int i3, FontRenderContext fontRenderContext) {
        int beginIndex = characterIterator.getBeginIndex();
        int endIndex = characterIterator.getEndIndex();
        if (i2 < beginIndex) {
            throw new IndexOutOfBoundsException("beginIndex: " + i2);
        }
        if (i3 > endIndex) {
            throw new IndexOutOfBoundsException("limit: " + i3);
        }
        if (i2 > i3) {
            throw new IndexOutOfBoundsException("range length: " + (i3 - i2));
        }
        char[] cArr = new char[i3 - i2];
        characterIterator.setIndex(i2);
        for (int i4 = 0; i4 < cArr.length; i4++) {
            cArr[i4] = characterIterator.current();
            characterIterator.next();
        }
        return getStringBounds(cArr, 0, cArr.length, fontRenderContext);
    }

    public Rectangle2D getMaxCharBounds(FontRenderContext fontRenderContext) {
        float[] fArr = new float[4];
        getFont2D().getFontMetrics(this, fontRenderContext, fArr);
        return new Rectangle2D.Float(0.0f, -fArr[0], fArr[3], fArr[0] + fArr[1] + fArr[2]);
    }

    public GlyphVector createGlyphVector(FontRenderContext fontRenderContext, String str) {
        return new StandardGlyphVector(this, str, fontRenderContext);
    }

    public GlyphVector createGlyphVector(FontRenderContext fontRenderContext, char[] cArr) {
        return new StandardGlyphVector(this, cArr, fontRenderContext);
    }

    public GlyphVector createGlyphVector(FontRenderContext fontRenderContext, CharacterIterator characterIterator) {
        return new StandardGlyphVector(this, characterIterator, fontRenderContext);
    }

    public GlyphVector createGlyphVector(FontRenderContext fontRenderContext, int[] iArr) {
        return new StandardGlyphVector(this, iArr, fontRenderContext);
    }

    public GlyphVector layoutGlyphVector(FontRenderContext fontRenderContext, char[] cArr, int i2, int i3, int i4) {
        GlyphLayout glyphLayout = GlyphLayout.get(null);
        StandardGlyphVector standardGlyphVectorLayout = glyphLayout.layout(this, fontRenderContext, cArr, i2, i3 - i2, i4, null);
        GlyphLayout.done(glyphLayout);
        return standardGlyphVectorLayout;
    }

    private static void applyTransform(AffineTransform affineTransform, AttributeValues attributeValues) {
        if (affineTransform == null) {
            throw new IllegalArgumentException("transform must not be null");
        }
        attributeValues.setTransform(affineTransform);
    }

    private static void applyStyle(int i2, AttributeValues attributeValues) {
        attributeValues.setWeight((i2 & 1) != 0 ? 2.0f : 1.0f);
        attributeValues.setPosture((i2 & 2) != 0 ? 0.2f : 0.0f);
    }
}
