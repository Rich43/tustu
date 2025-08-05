package sun.awt.windows;

import java.awt.AWTEvent;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ComponentEvent;
import java.awt.event.InputMethodEvent;
import java.awt.event.InvocationEvent;
import java.awt.font.TextAttribute;
import java.awt.font.TextHitInfo;
import java.awt.im.InputMethodHighlight;
import java.awt.im.InputSubset;
import java.awt.im.spi.InputMethodContext;
import java.awt.peer.ComponentPeer;
import java.awt.peer.LightweightPeer;
import java.lang.Character;
import java.text.Annotation;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import sun.awt.im.InputMethodAdapter;

/* loaded from: rt.jar:sun/awt/windows/WInputMethod.class */
final class WInputMethod extends InputMethodAdapter {
    private InputMethodContext inputContext;
    private Component awtFocussedComponent;
    private boolean isActive;
    private Locale currentLocale;
    public static final byte ATTR_INPUT = 0;
    public static final byte ATTR_TARGET_CONVERTED = 1;
    public static final byte ATTR_CONVERTED = 2;
    public static final byte ATTR_TARGET_NOTCONVERTED = 3;
    public static final byte ATTR_INPUT_ERROR = 4;
    public static final int IME_CMODE_ALPHANUMERIC = 0;
    public static final int IME_CMODE_NATIVE = 1;
    public static final int IME_CMODE_KATAKANA = 2;
    public static final int IME_CMODE_LANGUAGE = 3;
    public static final int IME_CMODE_FULLSHAPE = 8;
    public static final int IME_CMODE_HANJACONVERT = 64;
    public static final int IME_CMODE_ROMAN = 16;
    private static final boolean COMMIT_INPUT = true;
    private static final boolean DISCARD_INPUT = false;
    private static Map<TextAttribute, Object>[] highlightStyles;
    private WComponentPeer awtFocussedComponentPeer = null;
    private WComponentPeer lastFocussedComponentPeer = null;
    private boolean isLastFocussedActiveClient = false;
    private boolean statusWindowHidden = false;
    private int context = createNativeContext();
    private int cmode = getConversionStatus(this.context);
    private boolean open = getOpenStatus(this.context);

    private native int createNativeContext();

    private native void destroyNativeContext(int i2);

    private native void enableNativeIME(WComponentPeer wComponentPeer, int i2, boolean z2);

    private native void disableNativeIME(WComponentPeer wComponentPeer);

    private native void handleNativeIMEEvent(WComponentPeer wComponentPeer, AWTEvent aWTEvent);

    private native void endCompositionNative(int i2, boolean z2);

    private native void setConversionStatus(int i2, int i3);

    private native int getConversionStatus(int i2);

    private native void setOpenStatus(int i2, boolean z2);

    private native boolean getOpenStatus(int i2);

    private native void setStatusWindowVisible(WComponentPeer wComponentPeer, boolean z2);

    private native String getNativeIMMDescription();

    static native Locale getNativeLocale();

    static native boolean setNativeLocale(String str, boolean z2);

    /* JADX INFO: Access modifiers changed from: private */
    public native void openCandidateWindow(WComponentPeer wComponentPeer, int i2, int i3);

    static {
        HashMap map = new HashMap(1);
        map.put(TextAttribute.INPUT_METHOD_UNDERLINE, TextAttribute.UNDERLINE_LOW_DOTTED);
        HashMap map2 = new HashMap(1);
        map2.put(TextAttribute.INPUT_METHOD_UNDERLINE, TextAttribute.UNDERLINE_LOW_GRAY);
        HashMap map3 = new HashMap(1);
        map3.put(TextAttribute.INPUT_METHOD_UNDERLINE, TextAttribute.UNDERLINE_LOW_DOTTED);
        HashMap map4 = new HashMap(4);
        map4.put(TextAttribute.FOREGROUND, new Color(0, 0, 128));
        map4.put(TextAttribute.BACKGROUND, Color.white);
        map4.put(TextAttribute.SWAP_COLORS, TextAttribute.SWAP_COLORS_ON);
        map4.put(TextAttribute.INPUT_METHOD_UNDERLINE, TextAttribute.UNDERLINE_LOW_ONE_PIXEL);
        highlightStyles = new Map[]{Collections.unmodifiableMap(map), Collections.unmodifiableMap(map2), Collections.unmodifiableMap(map3), Collections.unmodifiableMap(map4)};
    }

    public WInputMethod() {
        this.currentLocale = getNativeLocale();
        if (this.currentLocale == null) {
            this.currentLocale = Locale.getDefault();
        }
    }

    protected void finalize() throws Throwable {
        if (this.context != 0) {
            destroyNativeContext(this.context);
            this.context = 0;
        }
        super.finalize();
    }

    @Override // java.awt.im.spi.InputMethod
    public synchronized void setInputMethodContext(InputMethodContext inputMethodContext) {
        this.inputContext = inputMethodContext;
    }

    @Override // java.awt.im.spi.InputMethod
    public final void dispose() {
    }

    @Override // java.awt.im.spi.InputMethod
    public Object getControlObject() {
        return null;
    }

    @Override // java.awt.im.spi.InputMethod
    public boolean setLocale(Locale locale) {
        return setLocale(locale, false);
    }

    private boolean setLocale(Locale locale, boolean z2) {
        for (Locale locale2 : WInputMethodDescriptor.getAvailableLocalesInternal()) {
            if (locale.equals(locale2) || ((locale2.equals(Locale.JAPAN) && locale.equals(Locale.JAPANESE)) || (locale2.equals(Locale.KOREA) && locale.equals(Locale.KOREAN)))) {
                if (this.isActive) {
                    setNativeLocale(locale2.toLanguageTag(), z2);
                }
                this.currentLocale = locale2;
                return true;
            }
        }
        return false;
    }

    @Override // java.awt.im.spi.InputMethod
    public Locale getLocale() {
        if (this.isActive) {
            this.currentLocale = getNativeLocale();
            if (this.currentLocale == null) {
                this.currentLocale = Locale.getDefault();
            }
        }
        return this.currentLocale;
    }

    @Override // java.awt.im.spi.InputMethod
    public void setCharacterSubsets(Character.Subset[] subsetArr) {
        int i2;
        int i3;
        int i4;
        if (subsetArr == null) {
            setConversionStatus(this.context, this.cmode);
            setOpenStatus(this.context, this.open);
            return;
        }
        Character.Subset subset = subsetArr[0];
        Locale nativeLocale = getNativeLocale();
        if (nativeLocale == null) {
            return;
        }
        if (nativeLocale.getLanguage().equals(Locale.JAPANESE.getLanguage())) {
            if (subset == Character.UnicodeBlock.BASIC_LATIN || subset == InputSubset.LATIN_DIGITS) {
                setOpenStatus(this.context, false);
                return;
            }
            if (subset == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS || subset == InputSubset.KANJI || subset == Character.UnicodeBlock.HIRAGANA) {
                i4 = 9;
            } else if (subset == Character.UnicodeBlock.KATAKANA) {
                i4 = 11;
            } else if (subset == InputSubset.HALFWIDTH_KATAKANA) {
                i4 = 3;
            } else if (subset == InputSubset.FULLWIDTH_LATIN) {
                i4 = 8;
            } else {
                return;
            }
            setOpenStatus(this.context, true);
            setConversionStatus(this.context, i4 | (getConversionStatus(this.context) & 16));
            return;
        }
        if (nativeLocale.getLanguage().equals(Locale.KOREAN.getLanguage())) {
            if (subset == Character.UnicodeBlock.BASIC_LATIN || subset == InputSubset.LATIN_DIGITS) {
                setOpenStatus(this.context, false);
                return;
            }
            if (subset == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS || subset == InputSubset.HANJA || subset == Character.UnicodeBlock.HANGUL_SYLLABLES || subset == Character.UnicodeBlock.HANGUL_JAMO || subset == Character.UnicodeBlock.HANGUL_COMPATIBILITY_JAMO) {
                i3 = 1;
            } else if (subset == InputSubset.FULLWIDTH_LATIN) {
                i3 = 8;
            } else {
                return;
            }
            setOpenStatus(this.context, true);
            setConversionStatus(this.context, i3);
            return;
        }
        if (nativeLocale.getLanguage().equals(Locale.CHINESE.getLanguage())) {
            if (subset == Character.UnicodeBlock.BASIC_LATIN || subset == InputSubset.LATIN_DIGITS) {
                setOpenStatus(this.context, false);
                return;
            }
            if (subset == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS || subset == InputSubset.TRADITIONAL_HANZI || subset == InputSubset.SIMPLIFIED_HANZI) {
                i2 = 1;
            } else if (subset == InputSubset.FULLWIDTH_LATIN) {
                i2 = 8;
            } else {
                return;
            }
            setOpenStatus(this.context, true);
            setConversionStatus(this.context, i2);
        }
    }

    @Override // java.awt.im.spi.InputMethod
    public void dispatchEvent(AWTEvent aWTEvent) {
        Component component;
        if ((aWTEvent instanceof ComponentEvent) && (component = ((ComponentEvent) aWTEvent).getComponent()) == this.awtFocussedComponent) {
            if (this.awtFocussedComponentPeer == null || this.awtFocussedComponentPeer.isDisposed()) {
                this.awtFocussedComponentPeer = getNearestNativePeer(component);
            }
            if (this.awtFocussedComponentPeer != null) {
                handleNativeIMEEvent(this.awtFocussedComponentPeer, aWTEvent);
            }
        }
    }

    @Override // java.awt.im.spi.InputMethod
    public void activate() {
        boolean zHaveActiveClient = haveActiveClient();
        if (this.lastFocussedComponentPeer != this.awtFocussedComponentPeer || this.isLastFocussedActiveClient != zHaveActiveClient) {
            if (this.lastFocussedComponentPeer != null) {
                disableNativeIME(this.lastFocussedComponentPeer);
            }
            if (this.awtFocussedComponentPeer != null) {
                enableNativeIME(this.awtFocussedComponentPeer, this.context, !zHaveActiveClient);
            }
            this.lastFocussedComponentPeer = this.awtFocussedComponentPeer;
            this.isLastFocussedActiveClient = zHaveActiveClient;
        }
        this.isActive = true;
        if (this.currentLocale != null) {
            setLocale(this.currentLocale, true);
        }
        if (this.statusWindowHidden) {
            setStatusWindowVisible(this.awtFocussedComponentPeer, true);
            this.statusWindowHidden = false;
        }
    }

    @Override // java.awt.im.spi.InputMethod
    public void deactivate(boolean z2) {
        getLocale();
        if (this.awtFocussedComponentPeer != null) {
            this.lastFocussedComponentPeer = this.awtFocussedComponentPeer;
            this.isLastFocussedActiveClient = haveActiveClient();
        }
        this.isActive = false;
    }

    @Override // sun.awt.im.InputMethodAdapter
    public void disableInputMethod() {
        if (this.lastFocussedComponentPeer != null) {
            disableNativeIME(this.lastFocussedComponentPeer);
            this.lastFocussedComponentPeer = null;
            this.isLastFocussedActiveClient = false;
        }
    }

    @Override // sun.awt.im.InputMethodAdapter
    public String getNativeInputMethodInfo() {
        return getNativeIMMDescription();
    }

    @Override // sun.awt.im.InputMethodAdapter
    protected void stopListening() {
        disableInputMethod();
    }

    @Override // sun.awt.im.InputMethodAdapter
    protected void setAWTFocussedComponent(Component component) {
        if (component == null) {
            return;
        }
        WComponentPeer nearestNativePeer = getNearestNativePeer(component);
        if (this.isActive) {
            if (this.awtFocussedComponentPeer != null) {
                disableNativeIME(this.awtFocussedComponentPeer);
            }
            if (nearestNativePeer != null) {
                enableNativeIME(nearestNativePeer, this.context, !haveActiveClient());
            }
        }
        this.awtFocussedComponent = component;
        this.awtFocussedComponentPeer = nearestNativePeer;
    }

    @Override // java.awt.im.spi.InputMethod
    public void hideWindows() {
        if (this.awtFocussedComponentPeer != null) {
            setStatusWindowVisible(this.awtFocussedComponentPeer, false);
            this.statusWindowHidden = true;
        }
    }

    @Override // java.awt.im.spi.InputMethod
    public void removeNotify() {
        endCompositionNative(this.context, false);
        this.awtFocussedComponent = null;
        this.awtFocussedComponentPeer = null;
    }

    static Map<TextAttribute, ?> mapInputMethodHighlight(InputMethodHighlight inputMethodHighlight) {
        int i2;
        int state = inputMethodHighlight.getState();
        if (state == 0) {
            i2 = 0;
        } else if (state == 1) {
            i2 = 2;
        } else {
            return null;
        }
        if (inputMethodHighlight.isSelected()) {
            i2++;
        }
        return highlightStyles[i2];
    }

    @Override // sun.awt.im.InputMethodAdapter
    protected boolean supportsBelowTheSpot() {
        return true;
    }

    @Override // java.awt.im.spi.InputMethod
    public void endComposition() {
        endCompositionNative(this.context, haveActiveClient());
    }

    @Override // java.awt.im.spi.InputMethod
    public void setCompositionEnabled(boolean z2) {
        setOpenStatus(this.context, z2);
    }

    @Override // java.awt.im.spi.InputMethod
    public boolean isCompositionEnabled() {
        return getOpenStatus(this.context);
    }

    public void sendInputMethodEvent(int i2, long j2, String str, int[] iArr, String[] strArr, int[] iArr2, byte[] bArr, int i3, int i4, int i5) {
        InputMethodHighlight inputMethodHighlight;
        AttributedCharacterIterator iterator = null;
        if (str != null) {
            AttributedString attributedString = new AttributedString(str);
            attributedString.addAttribute(AttributedCharacterIterator.Attribute.LANGUAGE, Locale.getDefault(), 0, str.length());
            if (iArr != null && strArr != null && strArr.length != 0 && iArr.length == strArr.length + 1 && iArr[0] == 0 && iArr[strArr.length] <= str.length()) {
                for (int i6 = 0; i6 < iArr.length - 1; i6++) {
                    attributedString.addAttribute(AttributedCharacterIterator.Attribute.INPUT_METHOD_SEGMENT, new Annotation(null), iArr[i6], iArr[i6 + 1]);
                    attributedString.addAttribute(AttributedCharacterIterator.Attribute.READING, new Annotation(strArr[i6]), iArr[i6], iArr[i6 + 1]);
                }
            } else {
                attributedString.addAttribute(AttributedCharacterIterator.Attribute.INPUT_METHOD_SEGMENT, new Annotation(null), 0, str.length());
                attributedString.addAttribute(AttributedCharacterIterator.Attribute.READING, new Annotation(""), 0, str.length());
            }
            if (iArr2 != null && bArr != null && bArr.length != 0 && iArr2.length == bArr.length + 1 && iArr2[0] == 0 && iArr2[bArr.length] == str.length()) {
                for (int i7 = 0; i7 < iArr2.length - 1; i7++) {
                    switch (bArr[i7]) {
                        case 0:
                        case 4:
                        default:
                            inputMethodHighlight = InputMethodHighlight.UNSELECTED_RAW_TEXT_HIGHLIGHT;
                            break;
                        case 1:
                            inputMethodHighlight = InputMethodHighlight.SELECTED_CONVERTED_TEXT_HIGHLIGHT;
                            break;
                        case 2:
                            inputMethodHighlight = InputMethodHighlight.UNSELECTED_CONVERTED_TEXT_HIGHLIGHT;
                            break;
                        case 3:
                            inputMethodHighlight = InputMethodHighlight.SELECTED_RAW_TEXT_HIGHLIGHT;
                            break;
                    }
                    attributedString.addAttribute(TextAttribute.INPUT_METHOD_HIGHLIGHT, inputMethodHighlight, iArr2[i7], iArr2[i7 + 1]);
                }
            } else {
                attributedString.addAttribute(TextAttribute.INPUT_METHOD_HIGHLIGHT, InputMethodHighlight.UNSELECTED_CONVERTED_TEXT_HIGHLIGHT, 0, str.length());
            }
            iterator = attributedString.getIterator();
        }
        Component clientComponent = getClientComponent();
        if (clientComponent == null) {
            return;
        }
        WToolkit.postEvent(WToolkit.targetToAppContext(clientComponent), new InputMethodEvent(clientComponent, i2, j2, iterator, i3, TextHitInfo.leading(i4), TextHitInfo.leading(i5)));
    }

    public void inquireCandidatePosition() {
        Component clientComponent = getClientComponent();
        if (clientComponent == null) {
            return;
        }
        WToolkit.postEvent(WToolkit.targetToAppContext(clientComponent), new InvocationEvent(clientComponent, new Runnable() { // from class: sun.awt.windows.WInputMethod.1
            @Override // java.lang.Runnable
            public void run() {
                int i2 = 0;
                int i3 = 0;
                Component clientComponent2 = WInputMethod.this.getClientComponent();
                if (clientComponent2 != null) {
                    if (clientComponent2.isShowing()) {
                        if (WInputMethod.this.haveActiveClient()) {
                            Rectangle textLocation = WInputMethod.this.inputContext.getTextLocation(TextHitInfo.leading(0));
                            i2 = textLocation.f12372x;
                            i3 = textLocation.f12373y + textLocation.height;
                        } else {
                            Point locationOnScreen = clientComponent2.getLocationOnScreen();
                            Dimension size = clientComponent2.getSize();
                            i2 = locationOnScreen.f12370x;
                            i3 = locationOnScreen.f12371y + size.height;
                        }
                    } else {
                        return;
                    }
                }
                WInputMethod.this.openCandidateWindow(WInputMethod.this.awtFocussedComponentPeer, i2, i3);
            }
        }));
    }

    private WComponentPeer getNearestNativePeer(Component component) {
        if (component == null) {
            return null;
        }
        ComponentPeer peer = component.getPeer();
        if (peer == null) {
            return null;
        }
        while (peer instanceof LightweightPeer) {
            component = component.getParent();
            if (component == null) {
                return null;
            }
            peer = component.getPeer();
            if (peer == null) {
                return null;
            }
        }
        if (peer instanceof WComponentPeer) {
            return (WComponentPeer) peer;
        }
        return null;
    }
}
