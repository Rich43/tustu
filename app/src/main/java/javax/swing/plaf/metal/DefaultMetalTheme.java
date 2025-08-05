package javax.swing.plaf.metal;

import java.awt.Font;
import java.security.AccessController;
import java.security.PrivilegedAction;
import javax.swing.UIManager;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.FontUIResource;
import sun.awt.AppContext;
import sun.security.action.GetPropertyAction;
import sun.swing.SwingUtilities2;

/* loaded from: rt.jar:javax/swing/plaf/metal/DefaultMetalTheme.class */
public class DefaultMetalTheme extends MetalTheme {
    private static final boolean PLAIN_FONTS;
    private static final String[] fontNames = {Font.DIALOG, Font.DIALOG, Font.DIALOG, Font.DIALOG, Font.DIALOG, Font.DIALOG};
    private static final int[] fontStyles = {1, 0, 0, 1, 1, 0};
    private static final int[] fontSizes = {12, 12, 12, 12, 12, 10};
    private static final String[] defaultNames = {"swing.plaf.metal.controlFont", "swing.plaf.metal.systemFont", "swing.plaf.metal.userFont", "swing.plaf.metal.controlFont", "swing.plaf.metal.controlFont", "swing.plaf.metal.smallFont"};
    private static final ColorUIResource primary1;
    private static final ColorUIResource primary2;
    private static final ColorUIResource primary3;
    private static final ColorUIResource secondary1;
    private static final ColorUIResource secondary2;
    private static final ColorUIResource secondary3;
    private FontDelegate fontDelegate;

    static {
        Object objDoPrivileged = AccessController.doPrivileged(new GetPropertyAction("swing.boldMetal"));
        if (objDoPrivileged == null || !"false".equals(objDoPrivileged)) {
            PLAIN_FONTS = false;
        } else {
            PLAIN_FONTS = true;
        }
        primary1 = new ColorUIResource(102, 102, 153);
        primary2 = new ColorUIResource(153, 153, 204);
        primary3 = new ColorUIResource(204, 204, 255);
        secondary1 = new ColorUIResource(102, 102, 102);
        secondary2 = new ColorUIResource(153, 153, 153);
        secondary3 = new ColorUIResource(204, 204, 204);
    }

    static String getDefaultFontName(int i2) {
        return fontNames[i2];
    }

    static int getDefaultFontSize(int i2) {
        return fontSizes[i2];
    }

    static int getDefaultFontStyle(int i2) {
        if (i2 != 4) {
            Object obj = null;
            if (AppContext.getAppContext().get(SwingUtilities2.LAF_STATE_KEY) != null) {
                obj = UIManager.get("swing.boldMetal");
            }
            if (obj != null) {
                if (Boolean.FALSE.equals(obj)) {
                    return 0;
                }
            } else if (PLAIN_FONTS) {
                return 0;
            }
        }
        return fontStyles[i2];
    }

    static String getDefaultPropertyName(int i2) {
        return defaultNames[i2];
    }

    @Override // javax.swing.plaf.metal.MetalTheme
    public String getName() {
        return "Steel";
    }

    public DefaultMetalTheme() {
        install();
    }

    @Override // javax.swing.plaf.metal.MetalTheme
    protected ColorUIResource getPrimary1() {
        return primary1;
    }

    @Override // javax.swing.plaf.metal.MetalTheme
    protected ColorUIResource getPrimary2() {
        return primary2;
    }

    @Override // javax.swing.plaf.metal.MetalTheme
    protected ColorUIResource getPrimary3() {
        return primary3;
    }

    @Override // javax.swing.plaf.metal.MetalTheme
    protected ColorUIResource getSecondary1() {
        return secondary1;
    }

    @Override // javax.swing.plaf.metal.MetalTheme
    protected ColorUIResource getSecondary2() {
        return secondary2;
    }

    @Override // javax.swing.plaf.metal.MetalTheme
    protected ColorUIResource getSecondary3() {
        return secondary3;
    }

    @Override // javax.swing.plaf.metal.MetalTheme
    public FontUIResource getControlTextFont() {
        return getFont(0);
    }

    @Override // javax.swing.plaf.metal.MetalTheme
    public FontUIResource getSystemTextFont() {
        return getFont(1);
    }

    @Override // javax.swing.plaf.metal.MetalTheme
    public FontUIResource getUserTextFont() {
        return getFont(2);
    }

    @Override // javax.swing.plaf.metal.MetalTheme
    public FontUIResource getMenuTextFont() {
        return getFont(3);
    }

    @Override // javax.swing.plaf.metal.MetalTheme
    public FontUIResource getWindowTitleFont() {
        return getFont(4);
    }

    @Override // javax.swing.plaf.metal.MetalTheme
    public FontUIResource getSubTextFont() {
        return getFont(5);
    }

    private FontUIResource getFont(int i2) {
        return this.fontDelegate.getFont(i2);
    }

    @Override // javax.swing.plaf.metal.MetalTheme
    void install() {
        if (MetalLookAndFeel.isWindows() && MetalLookAndFeel.useSystemFonts()) {
            this.fontDelegate = new WindowsFontDelegate();
        } else {
            this.fontDelegate = new FontDelegate();
        }
    }

    @Override // javax.swing.plaf.metal.MetalTheme
    boolean isSystemTheme() {
        return getClass() == DefaultMetalTheme.class;
    }

    /* loaded from: rt.jar:javax/swing/plaf/metal/DefaultMetalTheme$FontDelegate.class */
    private static class FontDelegate {
        private static int[] defaultMapping = {0, 1, 2, 0, 0, 5};
        FontUIResource[] fonts = new FontUIResource[6];

        public FontUIResource getFont(int i2) {
            int i3 = defaultMapping[i2];
            if (this.fonts[i2] == null) {
                Font privilegedFont = getPrivilegedFont(i3);
                if (privilegedFont == null) {
                    privilegedFont = new Font(DefaultMetalTheme.getDefaultFontName(i2), DefaultMetalTheme.getDefaultFontStyle(i2), DefaultMetalTheme.getDefaultFontSize(i2));
                }
                this.fonts[i2] = new FontUIResource(privilegedFont);
            }
            return this.fonts[i2];
        }

        protected Font getPrivilegedFont(final int i2) {
            return (Font) AccessController.doPrivileged(new PrivilegedAction<Font>() { // from class: javax.swing.plaf.metal.DefaultMetalTheme.FontDelegate.1
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.security.PrivilegedAction
                /* renamed from: run */
                public Font run2() {
                    return Font.getFont(DefaultMetalTheme.getDefaultPropertyName(i2));
                }
            });
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/metal/DefaultMetalTheme$WindowsFontDelegate.class */
    private static class WindowsFontDelegate extends FontDelegate {
        private MetalFontDesktopProperty[] props = new MetalFontDesktopProperty[6];
        private boolean[] checkedPriviledged = new boolean[6];

        @Override // javax.swing.plaf.metal.DefaultMetalTheme.FontDelegate
        public FontUIResource getFont(int i2) {
            if (this.fonts[i2] != null) {
                return this.fonts[i2];
            }
            if (!this.checkedPriviledged[i2]) {
                Font privilegedFont = getPrivilegedFont(i2);
                this.checkedPriviledged[i2] = true;
                if (privilegedFont != null) {
                    this.fonts[i2] = new FontUIResource(privilegedFont);
                    return this.fonts[i2];
                }
            }
            if (this.props[i2] == null) {
                this.props[i2] = new MetalFontDesktopProperty(i2);
            }
            return (FontUIResource) this.props[i2].createValue(null);
        }
    }
}
