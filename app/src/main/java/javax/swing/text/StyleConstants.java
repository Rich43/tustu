package javax.swing.text;

import java.awt.Color;
import java.awt.Component;
import javax.swing.Icon;
import javax.swing.text.AttributeSet;

/* loaded from: rt.jar:javax/swing/text/StyleConstants.class */
public class StyleConstants {
    public static final String ComponentElementName = "component";
    public static final String IconElementName = "icon";
    public static final int ALIGN_LEFT = 0;
    public static final int ALIGN_CENTER = 1;
    public static final int ALIGN_RIGHT = 2;
    public static final int ALIGN_JUSTIFIED = 3;
    private String representation;
    public static final Object NameAttribute = new StyleConstants("name");
    public static final Object ResolveAttribute = new StyleConstants("resolver");
    public static final Object ModelAttribute = new StyleConstants("model");
    public static final Object BidiLevel = new CharacterConstants("bidiLevel");
    public static final Object FontFamily = new FontConstants("family");
    public static final Object Family = FontFamily;
    public static final Object FontSize = new FontConstants("size");
    public static final Object Size = FontSize;
    public static final Object Bold = new FontConstants("bold");
    public static final Object Italic = new FontConstants("italic");
    public static final Object Underline = new CharacterConstants("underline");
    public static final Object StrikeThrough = new CharacterConstants("strikethrough");
    public static final Object Superscript = new CharacterConstants("superscript");
    public static final Object Subscript = new CharacterConstants("subscript");
    public static final Object Foreground = new ColorConstants("foreground");
    public static final Object Background = new ColorConstants("background");
    public static final Object ComponentAttribute = new CharacterConstants("component");
    public static final Object IconAttribute = new CharacterConstants("icon");
    public static final Object ComposedTextAttribute = new StyleConstants("composed text");
    public static final Object FirstLineIndent = new ParagraphConstants("FirstLineIndent");
    public static final Object LeftIndent = new ParagraphConstants("LeftIndent");
    public static final Object RightIndent = new ParagraphConstants("RightIndent");
    public static final Object LineSpacing = new ParagraphConstants("LineSpacing");
    public static final Object SpaceAbove = new ParagraphConstants("SpaceAbove");
    public static final Object SpaceBelow = new ParagraphConstants("SpaceBelow");
    public static final Object Alignment = new ParagraphConstants("Alignment");
    public static final Object TabSet = new ParagraphConstants("TabSet");
    public static final Object Orientation = new ParagraphConstants("Orientation");
    static Object[] keys = {NameAttribute, ResolveAttribute, BidiLevel, FontFamily, FontSize, Bold, Italic, Underline, StrikeThrough, Superscript, Subscript, Foreground, Background, ComponentAttribute, IconAttribute, FirstLineIndent, LeftIndent, RightIndent, LineSpacing, SpaceAbove, SpaceBelow, Alignment, TabSet, Orientation, ModelAttribute, ComposedTextAttribute};

    public String toString() {
        return this.representation;
    }

    public static int getBidiLevel(AttributeSet attributeSet) {
        Integer num = (Integer) attributeSet.getAttribute(BidiLevel);
        if (num != null) {
            return num.intValue();
        }
        return 0;
    }

    public static void setBidiLevel(MutableAttributeSet mutableAttributeSet, int i2) {
        mutableAttributeSet.addAttribute(BidiLevel, Integer.valueOf(i2));
    }

    public static Component getComponent(AttributeSet attributeSet) {
        return (Component) attributeSet.getAttribute(ComponentAttribute);
    }

    public static void setComponent(MutableAttributeSet mutableAttributeSet, Component component) {
        mutableAttributeSet.addAttribute(AbstractDocument.ElementNameAttribute, "component");
        mutableAttributeSet.addAttribute(ComponentAttribute, component);
    }

    public static Icon getIcon(AttributeSet attributeSet) {
        return (Icon) attributeSet.getAttribute(IconAttribute);
    }

    public static void setIcon(MutableAttributeSet mutableAttributeSet, Icon icon) {
        mutableAttributeSet.addAttribute(AbstractDocument.ElementNameAttribute, "icon");
        mutableAttributeSet.addAttribute(IconAttribute, icon);
    }

    public static String getFontFamily(AttributeSet attributeSet) {
        String str = (String) attributeSet.getAttribute(FontFamily);
        if (str == null) {
            str = "Monospaced";
        }
        return str;
    }

    public static void setFontFamily(MutableAttributeSet mutableAttributeSet, String str) {
        mutableAttributeSet.addAttribute(FontFamily, str);
    }

    public static int getFontSize(AttributeSet attributeSet) {
        Integer num = (Integer) attributeSet.getAttribute(FontSize);
        if (num != null) {
            return num.intValue();
        }
        return 12;
    }

    public static void setFontSize(MutableAttributeSet mutableAttributeSet, int i2) {
        mutableAttributeSet.addAttribute(FontSize, Integer.valueOf(i2));
    }

    public static boolean isBold(AttributeSet attributeSet) {
        Boolean bool = (Boolean) attributeSet.getAttribute(Bold);
        if (bool != null) {
            return bool.booleanValue();
        }
        return false;
    }

    public static void setBold(MutableAttributeSet mutableAttributeSet, boolean z2) {
        mutableAttributeSet.addAttribute(Bold, Boolean.valueOf(z2));
    }

    public static boolean isItalic(AttributeSet attributeSet) {
        Boolean bool = (Boolean) attributeSet.getAttribute(Italic);
        if (bool != null) {
            return bool.booleanValue();
        }
        return false;
    }

    public static void setItalic(MutableAttributeSet mutableAttributeSet, boolean z2) {
        mutableAttributeSet.addAttribute(Italic, Boolean.valueOf(z2));
    }

    public static boolean isUnderline(AttributeSet attributeSet) {
        Boolean bool = (Boolean) attributeSet.getAttribute(Underline);
        if (bool != null) {
            return bool.booleanValue();
        }
        return false;
    }

    public static boolean isStrikeThrough(AttributeSet attributeSet) {
        Boolean bool = (Boolean) attributeSet.getAttribute(StrikeThrough);
        if (bool != null) {
            return bool.booleanValue();
        }
        return false;
    }

    public static boolean isSuperscript(AttributeSet attributeSet) {
        Boolean bool = (Boolean) attributeSet.getAttribute(Superscript);
        if (bool != null) {
            return bool.booleanValue();
        }
        return false;
    }

    public static boolean isSubscript(AttributeSet attributeSet) {
        Boolean bool = (Boolean) attributeSet.getAttribute(Subscript);
        if (bool != null) {
            return bool.booleanValue();
        }
        return false;
    }

    public static void setUnderline(MutableAttributeSet mutableAttributeSet, boolean z2) {
        mutableAttributeSet.addAttribute(Underline, Boolean.valueOf(z2));
    }

    public static void setStrikeThrough(MutableAttributeSet mutableAttributeSet, boolean z2) {
        mutableAttributeSet.addAttribute(StrikeThrough, Boolean.valueOf(z2));
    }

    public static void setSuperscript(MutableAttributeSet mutableAttributeSet, boolean z2) {
        mutableAttributeSet.addAttribute(Superscript, Boolean.valueOf(z2));
    }

    public static void setSubscript(MutableAttributeSet mutableAttributeSet, boolean z2) {
        mutableAttributeSet.addAttribute(Subscript, Boolean.valueOf(z2));
    }

    public static Color getForeground(AttributeSet attributeSet) {
        Color color = (Color) attributeSet.getAttribute(Foreground);
        if (color == null) {
            color = Color.black;
        }
        return color;
    }

    public static void setForeground(MutableAttributeSet mutableAttributeSet, Color color) {
        mutableAttributeSet.addAttribute(Foreground, color);
    }

    public static Color getBackground(AttributeSet attributeSet) {
        Color color = (Color) attributeSet.getAttribute(Background);
        if (color == null) {
            color = Color.black;
        }
        return color;
    }

    public static void setBackground(MutableAttributeSet mutableAttributeSet, Color color) {
        mutableAttributeSet.addAttribute(Background, color);
    }

    public static float getFirstLineIndent(AttributeSet attributeSet) {
        Float f2 = (Float) attributeSet.getAttribute(FirstLineIndent);
        if (f2 != null) {
            return f2.floatValue();
        }
        return 0.0f;
    }

    public static void setFirstLineIndent(MutableAttributeSet mutableAttributeSet, float f2) {
        mutableAttributeSet.addAttribute(FirstLineIndent, new Float(f2));
    }

    public static float getRightIndent(AttributeSet attributeSet) {
        Float f2 = (Float) attributeSet.getAttribute(RightIndent);
        if (f2 != null) {
            return f2.floatValue();
        }
        return 0.0f;
    }

    public static void setRightIndent(MutableAttributeSet mutableAttributeSet, float f2) {
        mutableAttributeSet.addAttribute(RightIndent, new Float(f2));
    }

    public static float getLeftIndent(AttributeSet attributeSet) {
        Float f2 = (Float) attributeSet.getAttribute(LeftIndent);
        if (f2 != null) {
            return f2.floatValue();
        }
        return 0.0f;
    }

    public static void setLeftIndent(MutableAttributeSet mutableAttributeSet, float f2) {
        mutableAttributeSet.addAttribute(LeftIndent, new Float(f2));
    }

    public static float getLineSpacing(AttributeSet attributeSet) {
        Float f2 = (Float) attributeSet.getAttribute(LineSpacing);
        if (f2 != null) {
            return f2.floatValue();
        }
        return 0.0f;
    }

    public static void setLineSpacing(MutableAttributeSet mutableAttributeSet, float f2) {
        mutableAttributeSet.addAttribute(LineSpacing, new Float(f2));
    }

    public static float getSpaceAbove(AttributeSet attributeSet) {
        Float f2 = (Float) attributeSet.getAttribute(SpaceAbove);
        if (f2 != null) {
            return f2.floatValue();
        }
        return 0.0f;
    }

    public static void setSpaceAbove(MutableAttributeSet mutableAttributeSet, float f2) {
        mutableAttributeSet.addAttribute(SpaceAbove, new Float(f2));
    }

    public static float getSpaceBelow(AttributeSet attributeSet) {
        Float f2 = (Float) attributeSet.getAttribute(SpaceBelow);
        if (f2 != null) {
            return f2.floatValue();
        }
        return 0.0f;
    }

    public static void setSpaceBelow(MutableAttributeSet mutableAttributeSet, float f2) {
        mutableAttributeSet.addAttribute(SpaceBelow, new Float(f2));
    }

    public static int getAlignment(AttributeSet attributeSet) {
        Integer num = (Integer) attributeSet.getAttribute(Alignment);
        if (num != null) {
            return num.intValue();
        }
        return 0;
    }

    public static void setAlignment(MutableAttributeSet mutableAttributeSet, int i2) {
        mutableAttributeSet.addAttribute(Alignment, Integer.valueOf(i2));
    }

    public static TabSet getTabSet(AttributeSet attributeSet) {
        return (TabSet) attributeSet.getAttribute(TabSet);
    }

    public static void setTabSet(MutableAttributeSet mutableAttributeSet, TabSet tabSet) {
        mutableAttributeSet.addAttribute(TabSet, tabSet);
    }

    StyleConstants(String str) {
        this.representation = str;
    }

    /* loaded from: rt.jar:javax/swing/text/StyleConstants$ParagraphConstants.class */
    public static class ParagraphConstants extends StyleConstants implements AttributeSet.ParagraphAttribute {
        private ParagraphConstants(String str) {
            super(str);
        }
    }

    /* loaded from: rt.jar:javax/swing/text/StyleConstants$CharacterConstants.class */
    public static class CharacterConstants extends StyleConstants implements AttributeSet.CharacterAttribute {
        private CharacterConstants(String str) {
            super(str);
        }
    }

    /* loaded from: rt.jar:javax/swing/text/StyleConstants$ColorConstants.class */
    public static class ColorConstants extends StyleConstants implements AttributeSet.ColorAttribute, AttributeSet.CharacterAttribute {
        private ColorConstants(String str) {
            super(str);
        }
    }

    /* loaded from: rt.jar:javax/swing/text/StyleConstants$FontConstants.class */
    public static class FontConstants extends StyleConstants implements AttributeSet.FontAttribute, AttributeSet.CharacterAttribute {
        private FontConstants(String str) {
            super(str);
        }
    }
}
