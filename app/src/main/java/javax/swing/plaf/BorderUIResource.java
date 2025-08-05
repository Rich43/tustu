package javax.swing.plaf;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Insets;
import java.beans.ConstructorProperties;
import java.io.Serializable;
import javax.swing.Icon;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import javax.swing.border.TitledBorder;

/* loaded from: rt.jar:javax/swing/plaf/BorderUIResource.class */
public class BorderUIResource implements Border, UIResource, Serializable {
    static Border etched;
    static Border loweredBevel;
    static Border raisedBevel;
    static Border blackLine;
    private Border delegate;

    public static Border getEtchedBorderUIResource() {
        if (etched == null) {
            etched = new EtchedBorderUIResource();
        }
        return etched;
    }

    public static Border getLoweredBevelBorderUIResource() {
        if (loweredBevel == null) {
            loweredBevel = new BevelBorderUIResource(1);
        }
        return loweredBevel;
    }

    public static Border getRaisedBevelBorderUIResource() {
        if (raisedBevel == null) {
            raisedBevel = new BevelBorderUIResource(0);
        }
        return raisedBevel;
    }

    public static Border getBlackLineBorderUIResource() {
        if (blackLine == null) {
            blackLine = new LineBorderUIResource(Color.black);
        }
        return blackLine;
    }

    public BorderUIResource(Border border) {
        if (border == null) {
            throw new IllegalArgumentException("null border delegate argument");
        }
        this.delegate = border;
    }

    @Override // javax.swing.border.Border
    public void paintBorder(Component component, Graphics graphics, int i2, int i3, int i4, int i5) {
        this.delegate.paintBorder(component, graphics, i2, i3, i4, i5);
    }

    @Override // javax.swing.border.Border
    public Insets getBorderInsets(Component component) {
        return this.delegate.getBorderInsets(component);
    }

    @Override // javax.swing.border.Border
    public boolean isBorderOpaque() {
        return this.delegate.isBorderOpaque();
    }

    /* loaded from: rt.jar:javax/swing/plaf/BorderUIResource$CompoundBorderUIResource.class */
    public static class CompoundBorderUIResource extends CompoundBorder implements UIResource {
        @ConstructorProperties({"outsideBorder", "insideBorder"})
        public CompoundBorderUIResource(Border border, Border border2) {
            super(border, border2);
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/BorderUIResource$EmptyBorderUIResource.class */
    public static class EmptyBorderUIResource extends EmptyBorder implements UIResource {
        public EmptyBorderUIResource(int i2, int i3, int i4, int i5) {
            super(i2, i3, i4, i5);
        }

        @ConstructorProperties({"borderInsets"})
        public EmptyBorderUIResource(Insets insets) {
            super(insets);
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/BorderUIResource$LineBorderUIResource.class */
    public static class LineBorderUIResource extends LineBorder implements UIResource {
        public LineBorderUIResource(Color color) {
            super(color);
        }

        @ConstructorProperties({"lineColor", "thickness"})
        public LineBorderUIResource(Color color, int i2) {
            super(color, i2);
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/BorderUIResource$BevelBorderUIResource.class */
    public static class BevelBorderUIResource extends BevelBorder implements UIResource {
        public BevelBorderUIResource(int i2) {
            super(i2);
        }

        public BevelBorderUIResource(int i2, Color color, Color color2) {
            super(i2, color, color2);
        }

        @ConstructorProperties({"bevelType", "highlightOuterColor", "highlightInnerColor", "shadowOuterColor", "shadowInnerColor"})
        public BevelBorderUIResource(int i2, Color color, Color color2, Color color3, Color color4) {
            super(i2, color, color2, color3, color4);
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/BorderUIResource$EtchedBorderUIResource.class */
    public static class EtchedBorderUIResource extends EtchedBorder implements UIResource {
        public EtchedBorderUIResource() {
        }

        public EtchedBorderUIResource(int i2) {
            super(i2);
        }

        public EtchedBorderUIResource(Color color, Color color2) {
            super(color, color2);
        }

        @ConstructorProperties({"etchType", "highlightColor", "shadowColor"})
        public EtchedBorderUIResource(int i2, Color color, Color color2) {
            super(i2, color, color2);
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/BorderUIResource$MatteBorderUIResource.class */
    public static class MatteBorderUIResource extends MatteBorder implements UIResource {
        public MatteBorderUIResource(int i2, int i3, int i4, int i5, Color color) {
            super(i2, i3, i4, i5, color);
        }

        public MatteBorderUIResource(int i2, int i3, int i4, int i5, Icon icon) {
            super(i2, i3, i4, i5, icon);
        }

        public MatteBorderUIResource(Icon icon) {
            super(icon);
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/BorderUIResource$TitledBorderUIResource.class */
    public static class TitledBorderUIResource extends TitledBorder implements UIResource {
        public TitledBorderUIResource(String str) {
            super(str);
        }

        public TitledBorderUIResource(Border border) {
            super(border);
        }

        public TitledBorderUIResource(Border border, String str) {
            super(border, str);
        }

        public TitledBorderUIResource(Border border, String str, int i2, int i3) {
            super(border, str, i2, i3);
        }

        public TitledBorderUIResource(Border border, String str, int i2, int i3, Font font) {
            super(border, str, i2, i3, font);
        }

        @ConstructorProperties({"border", "title", "titleJustification", "titlePosition", "titleFont", "titleColor"})
        public TitledBorderUIResource(Border border, String str, int i2, int i3, Font font, Color color) {
            super(border, str, i2, i3, font, color);
        }
    }
}
