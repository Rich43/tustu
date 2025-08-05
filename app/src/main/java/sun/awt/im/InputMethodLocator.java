package sun.awt.im;

import java.awt.AWTException;
import java.awt.im.spi.InputMethodDescriptor;
import java.util.Locale;

/* loaded from: rt.jar:sun/awt/im/InputMethodLocator.class */
final class InputMethodLocator {
    private InputMethodDescriptor descriptor;
    private ClassLoader loader;
    private Locale locale;

    InputMethodLocator(InputMethodDescriptor inputMethodDescriptor, ClassLoader classLoader, Locale locale) {
        if (inputMethodDescriptor == null) {
            throw new NullPointerException("descriptor can't be null");
        }
        this.descriptor = inputMethodDescriptor;
        this.loader = classLoader;
        this.locale = locale;
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        InputMethodLocator inputMethodLocator = (InputMethodLocator) obj;
        if (!this.descriptor.getClass().equals(inputMethodLocator.descriptor.getClass())) {
            return false;
        }
        if (this.loader != null || inputMethodLocator.loader == null) {
            if (this.loader != null && !this.loader.equals(inputMethodLocator.loader)) {
                return false;
            }
            if (this.locale != null || inputMethodLocator.locale == null) {
                if (this.locale != null && !this.locale.equals(inputMethodLocator.locale)) {
                    return false;
                }
                return true;
            }
            return false;
        }
        return false;
    }

    public int hashCode() {
        int iHashCode = this.descriptor.hashCode();
        if (this.loader != null) {
            iHashCode |= this.loader.hashCode() << 10;
        }
        if (this.locale != null) {
            iHashCode |= this.locale.hashCode() << 20;
        }
        return iHashCode;
    }

    InputMethodDescriptor getDescriptor() {
        return this.descriptor;
    }

    ClassLoader getClassLoader() {
        return this.loader;
    }

    Locale getLocale() {
        return this.locale;
    }

    boolean isLocaleAvailable(Locale locale) {
        try {
            for (Locale locale2 : this.descriptor.getAvailableLocales()) {
                if (locale2.equals(locale)) {
                    return true;
                }
            }
            return false;
        } catch (AWTException e2) {
            return false;
        }
    }

    InputMethodLocator deriveLocator(Locale locale) {
        if (locale == this.locale) {
            return this;
        }
        return new InputMethodLocator(this.descriptor, this.loader, locale);
    }

    boolean sameInputMethod(InputMethodLocator inputMethodLocator) {
        if (inputMethodLocator == this) {
            return true;
        }
        if (inputMethodLocator == null || !this.descriptor.getClass().equals(inputMethodLocator.descriptor.getClass())) {
            return false;
        }
        if (this.loader != null || inputMethodLocator.loader == null) {
            if (this.loader != null && !this.loader.equals(inputMethodLocator.loader)) {
                return false;
            }
            return true;
        }
        return false;
    }

    String getActionCommandString() {
        String name = this.descriptor.getClass().getName();
        if (this.locale == null) {
            return name;
        }
        return name + "\n" + this.locale.toString();
    }
}
