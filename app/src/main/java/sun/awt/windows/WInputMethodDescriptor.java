package sun.awt.windows;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.im.spi.InputMethod;
import java.awt.im.spi.InputMethodDescriptor;
import java.util.Locale;

/* loaded from: rt.jar:sun/awt/windows/WInputMethodDescriptor.class */
final class WInputMethodDescriptor implements InputMethodDescriptor {
    private static native Locale[] getNativeAvailableLocales();

    WInputMethodDescriptor() {
    }

    @Override // java.awt.im.spi.InputMethodDescriptor
    public Locale[] getAvailableLocales() {
        Locale[] availableLocalesInternal = getAvailableLocalesInternal();
        Locale[] localeArr = new Locale[availableLocalesInternal.length];
        System.arraycopy(availableLocalesInternal, 0, localeArr, 0, availableLocalesInternal.length);
        return localeArr;
    }

    static Locale[] getAvailableLocalesInternal() {
        return getNativeAvailableLocales();
    }

    @Override // java.awt.im.spi.InputMethodDescriptor
    public boolean hasDynamicLocaleList() {
        return true;
    }

    @Override // java.awt.im.spi.InputMethodDescriptor
    public synchronized String getInputMethodDisplayName(Locale locale, Locale locale2) {
        String property = "System Input Methods";
        if (Locale.getDefault().equals(locale2)) {
            property = Toolkit.getProperty("AWT.HostInputMethodDisplayName", property);
        }
        return property;
    }

    @Override // java.awt.im.spi.InputMethodDescriptor
    public Image getInputMethodIcon(Locale locale) {
        return null;
    }

    @Override // java.awt.im.spi.InputMethodDescriptor
    public InputMethod createInputMethod() throws Exception {
        return new WInputMethod();
    }
}
