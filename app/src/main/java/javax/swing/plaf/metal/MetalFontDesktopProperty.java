package javax.swing.plaf.metal;

import com.sun.java.swing.plaf.windows.DesktopProperty;
import java.awt.Font;

/* loaded from: rt.jar:javax/swing/plaf/metal/MetalFontDesktopProperty.class */
class MetalFontDesktopProperty extends DesktopProperty {
    private static final String[] propertyMapping = {"win.ansiVar.font.height", "win.tooltip.font.height", "win.ansiVar.font.height", "win.menu.font.height", "win.frame.captionFont.height", "win.menu.font.height"};
    private int type;

    MetalFontDesktopProperty(int i2) {
        this(propertyMapping[i2], i2);
    }

    MetalFontDesktopProperty(String str, int i2) {
        super(str, null);
        this.type = i2;
    }

    @Override // com.sun.java.swing.plaf.windows.DesktopProperty
    protected Object configureValue(Object obj) {
        if (obj instanceof Integer) {
            obj = new Font(DefaultMetalTheme.getDefaultFontName(this.type), DefaultMetalTheme.getDefaultFontStyle(this.type), ((Integer) obj).intValue());
        }
        return super.configureValue(obj);
    }

    @Override // com.sun.java.swing.plaf.windows.DesktopProperty
    protected Object getDefaultValue() {
        return new Font(DefaultMetalTheme.getDefaultFontName(this.type), DefaultMetalTheme.getDefaultFontStyle(this.type), DefaultMetalTheme.getDefaultFontSize(this.type));
    }
}
