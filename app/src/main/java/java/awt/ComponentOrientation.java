package java.awt;

import java.io.Serializable;
import java.util.Locale;
import java.util.ResourceBundle;

/* loaded from: rt.jar:java/awt/ComponentOrientation.class */
public final class ComponentOrientation implements Serializable {
    private static final long serialVersionUID = -4113291392143563828L;
    private static final int UNK_BIT = 1;
    private static final int HORIZ_BIT = 2;
    private static final int LTR_BIT = 4;
    public static final ComponentOrientation LEFT_TO_RIGHT = new ComponentOrientation(6);
    public static final ComponentOrientation RIGHT_TO_LEFT = new ComponentOrientation(2);
    public static final ComponentOrientation UNKNOWN = new ComponentOrientation(7);
    private int orientation;

    public boolean isHorizontal() {
        return (this.orientation & 2) != 0;
    }

    public boolean isLeftToRight() {
        return (this.orientation & 4) != 0;
    }

    public static ComponentOrientation getOrientation(Locale locale) {
        String language = locale.getLanguage();
        if ("iw".equals(language) || "ar".equals(language) || "fa".equals(language) || "ur".equals(language)) {
            return RIGHT_TO_LEFT;
        }
        return LEFT_TO_RIGHT;
    }

    @Deprecated
    public static ComponentOrientation getOrientation(ResourceBundle resourceBundle) {
        ComponentOrientation orientation = null;
        try {
            orientation = (ComponentOrientation) resourceBundle.getObject("Orientation");
        } catch (Exception e2) {
        }
        if (orientation == null) {
            orientation = getOrientation(resourceBundle.getLocale());
        }
        if (orientation == null) {
            orientation = getOrientation(Locale.getDefault());
        }
        return orientation;
    }

    private ComponentOrientation(int i2) {
        this.orientation = i2;
    }
}
