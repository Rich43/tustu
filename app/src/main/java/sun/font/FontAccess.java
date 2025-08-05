package sun.font;

import java.awt.Font;

/* loaded from: rt.jar:sun/font/FontAccess.class */
public abstract class FontAccess {
    private static FontAccess access;

    public abstract Font2D getFont2D(Font font);

    public abstract void setFont2D(Font font, Font2DHandle font2DHandle);

    public abstract void setCreatedFont(Font font);

    public abstract boolean isCreatedFont(Font font);

    public static synchronized void setFontAccess(FontAccess fontAccess) {
        if (access != null) {
            throw new InternalError("Attempt to set FontAccessor twice");
        }
        access = fontAccess;
    }

    public static synchronized FontAccess getFontAccess() {
        return access;
    }
}
