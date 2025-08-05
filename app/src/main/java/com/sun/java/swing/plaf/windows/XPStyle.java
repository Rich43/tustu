package com.sun.java.swing.plaf.windows;

import com.sun.java.swing.plaf.windows.TMSchema;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.security.AccessController;
import java.util.HashMap;
import javax.swing.AbstractButton;
import javax.swing.CellRendererPane;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JRadioButton;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.AbstractBorder;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.InsetsUIResource;
import javax.swing.plaf.UIResource;
import javax.swing.text.JTextComponent;
import sun.awt.image.SunWritableRaster;
import sun.awt.windows.ThemeReader;
import sun.awt.windows.WToolkit;
import sun.security.action.GetPropertyAction;
import sun.swing.CachedPainter;

/* loaded from: rt.jar:com/sun/java/swing/plaf/windows/XPStyle.class */
class XPStyle {
    private static XPStyle xp;
    private static SkinPainter skinPainter;
    private static Boolean themeActive;
    static final /* synthetic */ boolean $assertionsDisabled;
    private boolean flatMenus = getSysBoolean(TMSchema.Prop.FLATMENUS);
    private HashMap<String, Color> colorMap = new HashMap<>();
    private HashMap<String, Border> borderMap = new HashMap<>();

    static {
        $assertionsDisabled = !XPStyle.class.desiredAssertionStatus();
        skinPainter = new SkinPainter();
        themeActive = null;
        invalidateStyle();
    }

    static synchronized void invalidateStyle() {
        xp = null;
        themeActive = null;
        skinPainter.flush();
    }

    static synchronized XPStyle getXP() {
        if (themeActive == null) {
            themeActive = (Boolean) Toolkit.getDefaultToolkit().getDesktopProperty(WToolkit.XPSTYLE_THEME_ACTIVE);
            if (themeActive == null) {
                themeActive = Boolean.FALSE;
            }
            if (themeActive.booleanValue() && AccessController.doPrivileged(new GetPropertyAction("swing.noxp")) == null && ThemeReader.isThemed() && !(UIManager.getLookAndFeel() instanceof WindowsClassicLookAndFeel)) {
                xp = new XPStyle();
            }
        }
        if (ThemeReader.isXPStyleEnabled()) {
            return xp;
        }
        return null;
    }

    static boolean isVista() {
        XPStyle xp2 = getXP();
        return xp2 != null && xp2.isSkinDefined(null, TMSchema.Part.CP_DROPDOWNBUTTONRIGHT);
    }

    String getString(Component component, TMSchema.Part part, TMSchema.State state, TMSchema.Prop prop) {
        return getTypeEnumName(component, part, state, prop);
    }

    TMSchema.TypeEnum getTypeEnum(Component component, TMSchema.Part part, TMSchema.State state, TMSchema.Prop prop) {
        return TMSchema.TypeEnum.getTypeEnum(prop, ThemeReader.getEnum(part.getControlName(component), part.getValue(), TMSchema.State.getValue(part, state), prop.getValue()));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String getTypeEnumName(Component component, TMSchema.Part part, TMSchema.State state, TMSchema.Prop prop) {
        int i2 = ThemeReader.getEnum(part.getControlName(component), part.getValue(), TMSchema.State.getValue(part, state), prop.getValue());
        if (i2 == -1) {
            return null;
        }
        return TMSchema.TypeEnum.getTypeEnum(prop, i2).getName();
    }

    int getInt(Component component, TMSchema.Part part, TMSchema.State state, TMSchema.Prop prop, int i2) {
        return ThemeReader.getInt(part.getControlName(component), part.getValue(), TMSchema.State.getValue(part, state), prop.getValue());
    }

    Dimension getDimension(Component component, TMSchema.Part part, TMSchema.State state, TMSchema.Prop prop) {
        Dimension position = ThemeReader.getPosition(part.getControlName(component), part.getValue(), TMSchema.State.getValue(part, state), prop.getValue());
        return position != null ? position : new Dimension();
    }

    Point getPoint(Component component, TMSchema.Part part, TMSchema.State state, TMSchema.Prop prop) {
        Dimension position = ThemeReader.getPosition(part.getControlName(component), part.getValue(), TMSchema.State.getValue(part, state), prop.getValue());
        return position != null ? new Point(position.width, position.height) : new Point();
    }

    Insets getMargin(Component component, TMSchema.Part part, TMSchema.State state, TMSchema.Prop prop) {
        Insets themeMargins = ThemeReader.getThemeMargins(part.getControlName(component), part.getValue(), TMSchema.State.getValue(part, state), prop.getValue());
        return themeMargins != null ? themeMargins : new Insets(0, 0, 0, 0);
    }

    synchronized Color getColor(Skin skin, TMSchema.Prop prop, Color color) {
        String str = skin.toString() + "." + prop.name();
        TMSchema.Part part = skin.part;
        Color color2 = this.colorMap.get(str);
        if (color2 == null) {
            color2 = ThemeReader.getColor(part.getControlName(null), part.getValue(), TMSchema.State.getValue(part, skin.state), prop.getValue());
            if (color2 != null) {
                color2 = new ColorUIResource(color2);
                this.colorMap.put(str, color2);
            }
        }
        return color2 != null ? color2 : color;
    }

    Color getColor(Component component, TMSchema.Part part, TMSchema.State state, TMSchema.Prop prop, Color color) {
        return getColor(new Skin(component, part, state), prop, color);
    }

    synchronized Border getBorder(Component component, TMSchema.Part part) {
        Insets margin;
        if (part == TMSchema.Part.MENU) {
            if (this.flatMenus) {
                return new XPFillBorder(UIManager.getColor("InternalFrame.borderShadow"), 1);
            }
            return null;
        }
        Skin skin = new Skin(component, part, null);
        Border xPImageBorder = this.borderMap.get(skin.string);
        if (xPImageBorder == null) {
            String typeEnumName = getTypeEnumName(component, part, null, TMSchema.Prop.BGTYPE);
            if ("borderfill".equalsIgnoreCase(typeEnumName)) {
                int i2 = getInt(component, part, null, TMSchema.Prop.BORDERSIZE, 1);
                Color color = getColor(skin, TMSchema.Prop.BORDERCOLOR, Color.black);
                xPImageBorder = new XPFillBorder(color, i2);
                if (part == TMSchema.Part.CP_COMBOBOX) {
                    xPImageBorder = new XPStatefulFillBorder(color, i2, part, TMSchema.Prop.BORDERCOLOR);
                }
            } else if ("imagefile".equalsIgnoreCase(typeEnumName) && (margin = getMargin(component, part, null, TMSchema.Prop.SIZINGMARGINS)) != null) {
                xPImageBorder = getBoolean(component, part, null, TMSchema.Prop.BORDERONLY) ? new XPImageBorder(component, part) : part == TMSchema.Part.CP_COMBOBOX ? new EmptyBorder(1, 1, 1, 1) : part == TMSchema.Part.TP_BUTTON ? new XPEmptyBorder(new Insets(3, 3, 3, 3)) : new XPEmptyBorder(margin);
            }
            if (xPImageBorder != null) {
                this.borderMap.put(skin.string, xPImageBorder);
            }
        }
        return xPImageBorder;
    }

    /* loaded from: rt.jar:com/sun/java/swing/plaf/windows/XPStyle$XPFillBorder.class */
    private class XPFillBorder extends LineBorder implements UIResource {
        XPFillBorder(Color color, int i2) {
            super(color, i2);
        }

        @Override // javax.swing.border.LineBorder, javax.swing.border.AbstractBorder
        public Insets getBorderInsets(Component component, Insets insets) {
            Insets margin = null;
            if (component instanceof AbstractButton) {
                margin = ((AbstractButton) component).getMargin();
            } else if (component instanceof JToolBar) {
                margin = ((JToolBar) component).getMargin();
            } else if (component instanceof JTextComponent) {
                margin = ((JTextComponent) component).getMargin();
            }
            insets.top = (margin != null ? margin.top : 0) + this.thickness;
            insets.left = (margin != null ? margin.left : 0) + this.thickness;
            insets.bottom = (margin != null ? margin.bottom : 0) + this.thickness;
            insets.right = (margin != null ? margin.right : 0) + this.thickness;
            return insets;
        }
    }

    /* loaded from: rt.jar:com/sun/java/swing/plaf/windows/XPStyle$XPStatefulFillBorder.class */
    private class XPStatefulFillBorder extends XPFillBorder {
        private final TMSchema.Part part;
        private final TMSchema.Prop prop;

        XPStatefulFillBorder(Color color, int i2, TMSchema.Part part, TMSchema.Prop prop) {
            super(color, i2);
            this.part = part;
            this.prop = prop;
        }

        @Override // javax.swing.border.LineBorder, javax.swing.border.AbstractBorder, javax.swing.border.Border
        public void paintBorder(Component component, Graphics graphics, int i2, int i3, int i4, int i5) {
            TMSchema.State xPComboBoxState = TMSchema.State.NORMAL;
            if (component instanceof JComboBox) {
                JComboBox jComboBox = (JComboBox) component;
                if (jComboBox.getUI() instanceof WindowsComboBoxUI) {
                    xPComboBoxState = ((WindowsComboBoxUI) jComboBox.getUI()).getXPComboBoxState(jComboBox);
                }
            }
            this.lineColor = XPStyle.this.getColor(component, this.part, xPComboBoxState, this.prop, Color.black);
            super.paintBorder(component, graphics, i2, i3, i4, i5);
        }
    }

    /* loaded from: rt.jar:com/sun/java/swing/plaf/windows/XPStyle$XPImageBorder.class */
    private class XPImageBorder extends AbstractBorder implements UIResource {
        Skin skin;

        XPImageBorder(Component component, TMSchema.Part part) {
            this.skin = XPStyle.this.getSkin(component, part);
        }

        @Override // javax.swing.border.AbstractBorder, javax.swing.border.Border
        public void paintBorder(Component component, Graphics graphics, int i2, int i3, int i4, int i5) {
            this.skin.paintSkin(graphics, i2, i3, i4, i5, null);
        }

        @Override // javax.swing.border.AbstractBorder
        public Insets getBorderInsets(Component component, Insets insets) {
            Insets margin = null;
            Insets contentMargin = this.skin.getContentMargin();
            if (contentMargin == null) {
                contentMargin = new Insets(0, 0, 0, 0);
            }
            if (component instanceof AbstractButton) {
                margin = ((AbstractButton) component).getMargin();
            } else if (component instanceof JToolBar) {
                margin = ((JToolBar) component).getMargin();
            } else if (component instanceof JTextComponent) {
                margin = ((JTextComponent) component).getMargin();
            }
            insets.top = (margin != null ? margin.top : 0) + contentMargin.top;
            insets.left = (margin != null ? margin.left : 0) + contentMargin.left;
            insets.bottom = (margin != null ? margin.bottom : 0) + contentMargin.bottom;
            insets.right = (margin != null ? margin.right : 0) + contentMargin.right;
            return insets;
        }
    }

    /* loaded from: rt.jar:com/sun/java/swing/plaf/windows/XPStyle$XPEmptyBorder.class */
    private class XPEmptyBorder extends EmptyBorder implements UIResource {
        XPEmptyBorder(Insets insets) {
            super(insets.top + 2, insets.left + 2, insets.bottom + 2, insets.right + 2);
        }

        @Override // javax.swing.border.EmptyBorder, javax.swing.border.AbstractBorder
        public Insets getBorderInsets(Component component, Insets insets) {
            Insets borderInsets = super.getBorderInsets(component, insets);
            Insets margin = null;
            if (component instanceof AbstractButton) {
                Insets margin2 = ((AbstractButton) component).getMargin();
                if ((component.getParent() instanceof JToolBar) && !(component instanceof JRadioButton) && !(component instanceof JCheckBox) && (margin2 instanceof InsetsUIResource)) {
                    borderInsets.top -= 2;
                    borderInsets.left -= 2;
                    borderInsets.bottom -= 2;
                    borderInsets.right -= 2;
                } else {
                    margin = margin2;
                }
            } else if (component instanceof JToolBar) {
                margin = ((JToolBar) component).getMargin();
            } else if (component instanceof JTextComponent) {
                margin = ((JTextComponent) component).getMargin();
            }
            if (margin != null) {
                borderInsets.top = margin.top + 2;
                borderInsets.left = margin.left + 2;
                borderInsets.bottom = margin.bottom + 2;
                borderInsets.right = margin.right + 2;
            }
            return borderInsets;
        }
    }

    boolean isSkinDefined(Component component, TMSchema.Part part) {
        return part.getValue() == 0 || ThemeReader.isThemePartDefined(part.getControlName(component), part.getValue(), 0);
    }

    synchronized Skin getSkin(Component component, TMSchema.Part part) {
        if ($assertionsDisabled || isSkinDefined(component, part)) {
            return new Skin(component, part, null);
        }
        throw new AssertionError((Object) ("part " + ((Object) part) + " is not defined"));
    }

    long getThemeTransitionDuration(Component component, TMSchema.Part part, TMSchema.State state, TMSchema.State state2, TMSchema.Prop prop) {
        return ThemeReader.getThemeTransitionDuration(part.getControlName(component), part.getValue(), TMSchema.State.getValue(part, state), TMSchema.State.getValue(part, state2), prop != null ? prop.getValue() : 0);
    }

    /* loaded from: rt.jar:com/sun/java/swing/plaf/windows/XPStyle$Skin.class */
    static class Skin {
        final Component component;
        final TMSchema.Part part;
        final TMSchema.State state;
        private final String string;
        private Dimension size;

        Skin(Component component, TMSchema.Part part) {
            this(component, part, null);
        }

        Skin(TMSchema.Part part, TMSchema.State state) {
            this(null, part, state);
        }

        Skin(Component component, TMSchema.Part part, TMSchema.State state) {
            this.size = null;
            this.component = component;
            this.part = part;
            this.state = state;
            String str = part.getControlName(component) + "." + part.name();
            this.string = state != null ? str + "(" + state.name() + ")" : str;
        }

        Insets getContentMargin() {
            Insets themeBackgroundContentMargins = ThemeReader.getThemeBackgroundContentMargins(this.part.getControlName(null), this.part.getValue(), 0, 100, 100);
            return themeBackgroundContentMargins != null ? themeBackgroundContentMargins : new Insets(0, 0, 0, 0);
        }

        private int getWidth(TMSchema.State state) {
            if (this.size == null) {
                this.size = XPStyle.getPartSize(this.part, state);
            }
            if (this.size != null) {
                return this.size.width;
            }
            return 0;
        }

        int getWidth() {
            return getWidth(this.state != null ? this.state : TMSchema.State.NORMAL);
        }

        private int getHeight(TMSchema.State state) {
            if (this.size == null) {
                this.size = XPStyle.getPartSize(this.part, state);
            }
            if (this.size != null) {
                return this.size.height;
            }
            return 0;
        }

        int getHeight() {
            return getHeight(this.state != null ? this.state : TMSchema.State.NORMAL);
        }

        public String toString() {
            return this.string;
        }

        public boolean equals(Object obj) {
            return (obj instanceof Skin) && ((Skin) obj).string.equals(this.string);
        }

        public int hashCode() {
            return this.string.hashCode();
        }

        void paintSkin(Graphics graphics, int i2, int i3, TMSchema.State state) {
            if (state == null) {
                state = this.state;
            }
            paintSkin(graphics, i2, i3, getWidth(state), getHeight(state), state);
        }

        void paintSkin(Graphics graphics, Rectangle rectangle, TMSchema.State state) {
            paintSkin(graphics, rectangle.f12372x, rectangle.f12373y, rectangle.width, rectangle.height, state);
        }

        void paintSkin(Graphics graphics, int i2, int i3, int i4, int i5, TMSchema.State state) {
            if (XPStyle.getXP() == null) {
                return;
            }
            if (ThemeReader.isGetThemeTransitionDurationDefined() && (this.component instanceof JComponent) && SwingUtilities.getAncestorOfClass(CellRendererPane.class, this.component) == null) {
                AnimationController.paintSkin((JComponent) this.component, this, graphics, i2, i3, i4, i5, state);
            } else {
                paintSkinRaw(graphics, i2, i3, i4, i5, state);
            }
        }

        void paintSkinRaw(Graphics graphics, int i2, int i3, int i4, int i5, TMSchema.State state) {
            if (XPStyle.getXP() != null) {
                XPStyle.skinPainter.paint(null, graphics, i2, i3, i4, i5, this, state);
            }
        }

        void paintSkin(Graphics graphics, int i2, int i3, int i4, int i5, TMSchema.State state, boolean z2) {
            if (XPStyle.getXP() == null) {
                return;
            }
            if (!z2 || !"borderfill".equals(XPStyle.getTypeEnumName(this.component, this.part, state, TMSchema.Prop.BGTYPE))) {
                XPStyle.skinPainter.paint(null, graphics, i2, i3, i4, i5, this, state);
            }
        }
    }

    /* loaded from: rt.jar:com/sun/java/swing/plaf/windows/XPStyle$SkinPainter.class */
    private static class SkinPainter extends CachedPainter {
        SkinPainter() {
            super(30);
            flush();
        }

        @Override // sun.swing.CachedPainter
        public void flush() {
            super.flush();
        }

        @Override // sun.swing.CachedPainter
        protected void paintToImage(Component component, Image image, Graphics graphics, int i2, int i3, Object[] objArr) {
            Skin skin = (Skin) objArr[0];
            TMSchema.Part part = skin.part;
            TMSchema.State state = (TMSchema.State) objArr[1];
            if (state == null) {
                state = skin.state;
            }
            if (component == null) {
                component = skin.component;
            }
            DataBufferInt dataBufferInt = (DataBufferInt) ((BufferedImage) image).getRaster().getDataBuffer();
            ThemeReader.paintBackground(SunWritableRaster.stealData(dataBufferInt, 0), part.getControlName(component), part.getValue(), TMSchema.State.getValue(part, state), 0, 0, i2, i3, i2);
            SunWritableRaster.markDirty(dataBufferInt);
        }

        @Override // sun.swing.CachedPainter
        protected Image createImage(Component component, int i2, int i3, GraphicsConfiguration graphicsConfiguration, Object[] objArr) {
            return new BufferedImage(i2, i3, 2);
        }
    }

    /* loaded from: rt.jar:com/sun/java/swing/plaf/windows/XPStyle$GlyphButton.class */
    static class GlyphButton extends JButton {
        private Skin skin;

        public GlyphButton(Component component, TMSchema.Part part) {
            XPStyle xp = XPStyle.getXP();
            this.skin = xp != null ? xp.getSkin(component, part) : null;
            setBorder(null);
            setContentAreaFilled(false);
            setMinimumSize(new Dimension(5, 5));
            setPreferredSize(new Dimension(16, 16));
            setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
        }

        @Override // java.awt.Component
        public boolean isFocusTraversable() {
            return false;
        }

        protected TMSchema.State getState() {
            TMSchema.State state = TMSchema.State.NORMAL;
            if (!isEnabled()) {
                state = TMSchema.State.DISABLED;
            } else if (getModel().isPressed()) {
                state = TMSchema.State.PRESSED;
            } else if (getModel().isRollover()) {
                state = TMSchema.State.HOT;
            }
            return state;
        }

        @Override // javax.swing.JComponent
        public void paintComponent(Graphics graphics) {
            if (XPStyle.getXP() == null || this.skin == null) {
                return;
            }
            Dimension size = getSize();
            this.skin.paintSkin(graphics, 0, 0, size.width, size.height, getState());
        }

        public void setPart(Component component, TMSchema.Part part) {
            XPStyle xp = XPStyle.getXP();
            this.skin = xp != null ? xp.getSkin(component, part) : null;
            revalidate();
            repaint();
        }

        @Override // javax.swing.AbstractButton, javax.swing.JComponent
        protected void paintBorder(Graphics graphics) {
        }
    }

    private XPStyle() {
    }

    private boolean getBoolean(Component component, TMSchema.Part part, TMSchema.State state, TMSchema.Prop prop) {
        return ThemeReader.getBoolean(part.getControlName(component), part.getValue(), TMSchema.State.getValue(part, state), prop.getValue());
    }

    static Dimension getPartSize(TMSchema.Part part, TMSchema.State state) {
        return ThemeReader.getPartSize(part.getControlName(null), part.getValue(), TMSchema.State.getValue(part, state));
    }

    private static boolean getSysBoolean(TMSchema.Prop prop) {
        return ThemeReader.getSysBoolean("window", prop.getValue());
    }
}
