package javax.swing.text.html;

import com.sun.media.jfxmedia.MetadataParser;
import com.sun.org.apache.xalan.internal.xsltc.compiler.Constants;
import com.sun.org.apache.xerces.internal.impl.xs.SchemaSymbols;
import java.awt.Color;
import java.awt.Font;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import javafx.fxml.FXMLLoader;
import javax.swing.AbstractButton;
import javax.swing.ImageIcon;
import javax.swing.JSplitPane;
import javax.swing.SizeRequirements;
import javax.swing.plaf.nimbus.NimbusStyle;
import javax.swing.text.AttributeSet;
import javax.swing.text.Element;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.View;
import javax.swing.text.html.HTML;
import org.icepdf.core.pobjects.graphics.Separation;
import org.icepdf.core.util.PdfOps;
import org.slf4j.Marker;
import sun.font.Font2D;
import sun.util.locale.LanguageTag;

/* loaded from: rt.jar:javax/swing/text/html/CSS.class */
public class CSS implements Serializable {
    private static final Hashtable<String, Attribute> attributeMap = new Hashtable<>();
    private static final Hashtable<String, Value> valueMap = new Hashtable<>();
    private static final Hashtable<HTML.Attribute, Attribute[]> htmlAttrToCssAttrMap = new Hashtable<>(20);
    private static final Hashtable<Object, Attribute> styleConstantToCssMap = new Hashtable<>(17);
    private static final Hashtable<String, Value> htmlValueToCssValueMap = new Hashtable<>(8);
    private static final Hashtable<String, Value> cssValueToInternalValueMap = new Hashtable<>(13);
    static int baseFontSizeIndex;
    private transient StyleSheet styleSheet = null;
    private int baseFontSize = baseFontSizeIndex + 1;
    private transient Hashtable<Object, Object> valueConvertor = new Hashtable<>();

    /* loaded from: rt.jar:javax/swing/text/html/CSS$LayoutIterator.class */
    interface LayoutIterator {
        public static final int WorstAdjustmentWeight = 2;

        void setOffset(int i2);

        int getOffset();

        void setSpan(int i2);

        int getSpan();

        int getCount();

        void setIndex(int i2);

        float getMinimumSpan(float f2);

        float getPreferredSpan(float f2);

        float getMaximumSpan(float f2);

        int getAdjustmentWeight();

        float getBorderWidth();

        float getLeadingCollapseSpan();

        float getTrailingCollapseSpan();
    }

    /* loaded from: rt.jar:javax/swing/text/html/CSS$Attribute.class */
    public static final class Attribute {
        private String name;
        private String defaultValue;
        private boolean inherited;
        public static final Attribute BACKGROUND = new Attribute("background", null, false);
        public static final Attribute BACKGROUND_ATTACHMENT = new Attribute("background-attachment", "scroll", false);
        public static final Attribute BACKGROUND_COLOR = new Attribute("background-color", "transparent", false);
        public static final Attribute BACKGROUND_IMAGE = new Attribute("background-image", Separation.COLORANT_NONE, false);
        public static final Attribute BACKGROUND_POSITION = new Attribute("background-position", null, false);
        public static final Attribute BACKGROUND_REPEAT = new Attribute("background-repeat", "repeat", false);
        public static final Attribute BORDER = new Attribute("border", null, false);
        public static final Attribute BORDER_BOTTOM = new Attribute("border-bottom", null, false);
        public static final Attribute BORDER_BOTTOM_COLOR = new Attribute("border-bottom-color", null, false);
        public static final Attribute BORDER_BOTTOM_STYLE = new Attribute("border-bottom-style", Separation.COLORANT_NONE, false);
        public static final Attribute BORDER_BOTTOM_WIDTH = new Attribute("border-bottom-width", "medium", false);
        public static final Attribute BORDER_COLOR = new Attribute("border-color", null, false);
        public static final Attribute BORDER_LEFT = new Attribute("border-left", null, false);
        public static final Attribute BORDER_LEFT_COLOR = new Attribute("border-left-color", null, false);
        public static final Attribute BORDER_LEFT_STYLE = new Attribute("border-left-style", Separation.COLORANT_NONE, false);
        public static final Attribute BORDER_LEFT_WIDTH = new Attribute("border-left-width", "medium", false);
        public static final Attribute BORDER_RIGHT = new Attribute("border-right", null, false);
        public static final Attribute BORDER_RIGHT_COLOR = new Attribute("border-right-color", null, false);
        public static final Attribute BORDER_RIGHT_STYLE = new Attribute("border-right-style", Separation.COLORANT_NONE, false);
        public static final Attribute BORDER_RIGHT_WIDTH = new Attribute("border-right-width", "medium", false);
        public static final Attribute BORDER_STYLE = new Attribute("border-style", Separation.COLORANT_NONE, false);
        public static final Attribute BORDER_TOP = new Attribute("border-top", null, false);
        public static final Attribute BORDER_TOP_COLOR = new Attribute("border-top-color", null, false);
        public static final Attribute BORDER_TOP_STYLE = new Attribute("border-top-style", Separation.COLORANT_NONE, false);
        public static final Attribute BORDER_TOP_WIDTH = new Attribute("border-top-width", "medium", false);
        public static final Attribute BORDER_WIDTH = new Attribute("border-width", "medium", false);
        public static final Attribute CLEAR = new Attribute(Constants.CLEAR_ATTRIBUTES, Separation.COLORANT_NONE, false);
        public static final Attribute COLOR = new Attribute("color", "black", true);
        public static final Attribute DISPLAY = new Attribute("display", "block", false);
        public static final Attribute FLOAT = new Attribute(SchemaSymbols.ATTVAL_FLOAT, Separation.COLORANT_NONE, false);
        public static final Attribute FONT = new Attribute("font", null, true);
        public static final Attribute FONT_FAMILY = new Attribute("font-family", null, true);
        public static final Attribute FONT_SIZE = new Attribute("font-size", "medium", true);
        public static final Attribute FONT_STYLE = new Attribute("font-style", "normal", true);
        public static final Attribute FONT_VARIANT = new Attribute("font-variant", "normal", true);
        public static final Attribute FONT_WEIGHT = new Attribute("font-weight", "normal", true);
        public static final Attribute HEIGHT = new Attribute(MetadataParser.HEIGHT_TAG_NAME, "auto", false);
        public static final Attribute LETTER_SPACING = new Attribute("letter-spacing", "normal", true);
        public static final Attribute LINE_HEIGHT = new Attribute("line-height", "normal", true);
        public static final Attribute LIST_STYLE = new Attribute("list-style", null, true);
        public static final Attribute LIST_STYLE_IMAGE = new Attribute("list-style-image", Separation.COLORANT_NONE, true);
        public static final Attribute LIST_STYLE_POSITION = new Attribute("list-style-position", "outside", true);
        public static final Attribute LIST_STYLE_TYPE = new Attribute("list-style-type", "disc", true);
        public static final Attribute MARGIN = new Attribute(AbstractButton.MARGIN_CHANGED_PROPERTY, null, false);
        public static final Attribute MARGIN_BOTTOM = new Attribute("margin-bottom", "0", false);
        public static final Attribute MARGIN_LEFT = new Attribute("margin-left", "0", false);
        public static final Attribute MARGIN_RIGHT = new Attribute("margin-right", "0", false);
        static final Attribute MARGIN_LEFT_LTR = new Attribute("margin-left-ltr", Integer.toString(Integer.MIN_VALUE), false);
        static final Attribute MARGIN_LEFT_RTL = new Attribute("margin-left-rtl", Integer.toString(Integer.MIN_VALUE), false);
        static final Attribute MARGIN_RIGHT_LTR = new Attribute("margin-right-ltr", Integer.toString(Integer.MIN_VALUE), false);
        static final Attribute MARGIN_RIGHT_RTL = new Attribute("margin-right-rtl", Integer.toString(Integer.MIN_VALUE), false);
        public static final Attribute MARGIN_TOP = new Attribute("margin-top", "0", false);
        public static final Attribute PADDING = new Attribute("padding", null, false);
        public static final Attribute PADDING_BOTTOM = new Attribute("padding-bottom", "0", false);
        public static final Attribute PADDING_LEFT = new Attribute("padding-left", "0", false);
        public static final Attribute PADDING_RIGHT = new Attribute("padding-right", "0", false);
        public static final Attribute PADDING_TOP = new Attribute("padding-top", "0", false);
        public static final Attribute TEXT_ALIGN = new Attribute("text-align", null, true);
        public static final Attribute TEXT_DECORATION = new Attribute("text-decoration", Separation.COLORANT_NONE, true);
        public static final Attribute TEXT_INDENT = new Attribute("text-indent", "0", true);
        public static final Attribute TEXT_TRANSFORM = new Attribute("text-transform", Separation.COLORANT_NONE, true);
        public static final Attribute VERTICAL_ALIGN = new Attribute("vertical-align", "baseline", false);
        public static final Attribute WORD_SPACING = new Attribute("word-spacing", "normal", true);
        public static final Attribute WHITE_SPACE = new Attribute("white-space", "normal", true);
        public static final Attribute WIDTH = new Attribute(MetadataParser.WIDTH_TAG_NAME, "auto", false);
        static final Attribute BORDER_SPACING = new Attribute("border-spacing", "0", true);
        static final Attribute CAPTION_SIDE = new Attribute("caption-side", JSplitPane.LEFT, true);
        static final Attribute[] allAttributes = {BACKGROUND, BACKGROUND_ATTACHMENT, BACKGROUND_COLOR, BACKGROUND_IMAGE, BACKGROUND_POSITION, BACKGROUND_REPEAT, BORDER, BORDER_BOTTOM, BORDER_BOTTOM_WIDTH, BORDER_COLOR, BORDER_LEFT, BORDER_LEFT_WIDTH, BORDER_RIGHT, BORDER_RIGHT_WIDTH, BORDER_STYLE, BORDER_TOP, BORDER_TOP_WIDTH, BORDER_WIDTH, BORDER_TOP_STYLE, BORDER_RIGHT_STYLE, BORDER_BOTTOM_STYLE, BORDER_LEFT_STYLE, BORDER_TOP_COLOR, BORDER_RIGHT_COLOR, BORDER_BOTTOM_COLOR, BORDER_LEFT_COLOR, CLEAR, COLOR, DISPLAY, FLOAT, FONT, FONT_FAMILY, FONT_SIZE, FONT_STYLE, FONT_VARIANT, FONT_WEIGHT, HEIGHT, LETTER_SPACING, LINE_HEIGHT, LIST_STYLE, LIST_STYLE_IMAGE, LIST_STYLE_POSITION, LIST_STYLE_TYPE, MARGIN, MARGIN_BOTTOM, MARGIN_LEFT, MARGIN_RIGHT, MARGIN_TOP, PADDING, PADDING_BOTTOM, PADDING_LEFT, PADDING_RIGHT, PADDING_TOP, TEXT_ALIGN, TEXT_DECORATION, TEXT_INDENT, TEXT_TRANSFORM, VERTICAL_ALIGN, WORD_SPACING, WHITE_SPACE, WIDTH, BORDER_SPACING, CAPTION_SIDE, MARGIN_LEFT_LTR, MARGIN_LEFT_RTL, MARGIN_RIGHT_LTR, MARGIN_RIGHT_RTL};
        private static final Attribute[] ALL_MARGINS = {MARGIN_TOP, MARGIN_RIGHT, MARGIN_BOTTOM, MARGIN_LEFT};
        private static final Attribute[] ALL_PADDING = {PADDING_TOP, PADDING_RIGHT, PADDING_BOTTOM, PADDING_LEFT};
        private static final Attribute[] ALL_BORDER_WIDTHS = {BORDER_TOP_WIDTH, BORDER_RIGHT_WIDTH, BORDER_BOTTOM_WIDTH, BORDER_LEFT_WIDTH};
        private static final Attribute[] ALL_BORDER_STYLES = {BORDER_TOP_STYLE, BORDER_RIGHT_STYLE, BORDER_BOTTOM_STYLE, BORDER_LEFT_STYLE};
        private static final Attribute[] ALL_BORDER_COLORS = {BORDER_TOP_COLOR, BORDER_RIGHT_COLOR, BORDER_BOTTOM_COLOR, BORDER_LEFT_COLOR};

        private Attribute(String str, String str2, boolean z2) {
            this.name = str;
            this.defaultValue = str2;
            this.inherited = z2;
        }

        public String toString() {
            return this.name;
        }

        public String getDefaultValue() {
            return this.defaultValue;
        }

        public boolean isInherited() {
            return this.inherited;
        }
    }

    /* loaded from: rt.jar:javax/swing/text/html/CSS$Value.class */
    static final class Value {
        private String name;
        static final Value INHERITED = new Value("inherited");
        static final Value NONE = new Value(Separation.COLORANT_NONE);
        static final Value HIDDEN = new Value("hidden");
        static final Value DOTTED = new Value("dotted");
        static final Value DASHED = new Value("dashed");
        static final Value SOLID = new Value("solid");
        static final Value DOUBLE = new Value(SchemaSymbols.ATTVAL_DOUBLE);
        static final Value GROOVE = new Value("groove");
        static final Value RIDGE = new Value("ridge");
        static final Value INSET = new Value("inset");
        static final Value OUTSET = new Value("outset");
        static final Value DISC = new Value("disc");
        static final Value CIRCLE = new Value("circle");
        static final Value SQUARE = new Value("square");
        static final Value DECIMAL = new Value(SchemaSymbols.ATTVAL_DECIMAL);
        static final Value LOWER_ROMAN = new Value("lower-roman");
        static final Value UPPER_ROMAN = new Value("upper-roman");
        static final Value LOWER_ALPHA = new Value("lower-alpha");
        static final Value UPPER_ALPHA = new Value("upper-alpha");
        static final Value BACKGROUND_NO_REPEAT = new Value("no-repeat");
        static final Value BACKGROUND_REPEAT = new Value("repeat");
        static final Value BACKGROUND_REPEAT_X = new Value("repeat-x");
        static final Value BACKGROUND_REPEAT_Y = new Value("repeat-y");
        static final Value BACKGROUND_SCROLL = new Value("scroll");
        static final Value BACKGROUND_FIXED = new Value("fixed");
        static final Value[] allValues = {INHERITED, NONE, DOTTED, DASHED, SOLID, DOUBLE, GROOVE, RIDGE, INSET, OUTSET, DISC, CIRCLE, SQUARE, DECIMAL, LOWER_ROMAN, UPPER_ROMAN, LOWER_ALPHA, UPPER_ALPHA, BACKGROUND_NO_REPEAT, BACKGROUND_REPEAT, BACKGROUND_REPEAT_X, BACKGROUND_REPEAT_Y, BACKGROUND_FIXED, BACKGROUND_FIXED};

        private Value(String str) {
            this.name = str;
        }

        public String toString() {
            return this.name;
        }
    }

    public CSS() {
        this.valueConvertor.put(Attribute.FONT_SIZE, new FontSize());
        this.valueConvertor.put(Attribute.FONT_FAMILY, new FontFamily());
        this.valueConvertor.put(Attribute.FONT_WEIGHT, new FontWeight());
        BorderStyle borderStyle = new BorderStyle();
        this.valueConvertor.put(Attribute.BORDER_TOP_STYLE, borderStyle);
        this.valueConvertor.put(Attribute.BORDER_RIGHT_STYLE, borderStyle);
        this.valueConvertor.put(Attribute.BORDER_BOTTOM_STYLE, borderStyle);
        this.valueConvertor.put(Attribute.BORDER_LEFT_STYLE, borderStyle);
        ColorValue colorValue = new ColorValue();
        this.valueConvertor.put(Attribute.COLOR, colorValue);
        this.valueConvertor.put(Attribute.BACKGROUND_COLOR, colorValue);
        this.valueConvertor.put(Attribute.BORDER_TOP_COLOR, colorValue);
        this.valueConvertor.put(Attribute.BORDER_RIGHT_COLOR, colorValue);
        this.valueConvertor.put(Attribute.BORDER_BOTTOM_COLOR, colorValue);
        this.valueConvertor.put(Attribute.BORDER_LEFT_COLOR, colorValue);
        LengthValue lengthValue = new LengthValue();
        this.valueConvertor.put(Attribute.MARGIN_TOP, lengthValue);
        this.valueConvertor.put(Attribute.MARGIN_BOTTOM, lengthValue);
        this.valueConvertor.put(Attribute.MARGIN_LEFT, lengthValue);
        this.valueConvertor.put(Attribute.MARGIN_LEFT_LTR, lengthValue);
        this.valueConvertor.put(Attribute.MARGIN_LEFT_RTL, lengthValue);
        this.valueConvertor.put(Attribute.MARGIN_RIGHT, lengthValue);
        this.valueConvertor.put(Attribute.MARGIN_RIGHT_LTR, lengthValue);
        this.valueConvertor.put(Attribute.MARGIN_RIGHT_RTL, lengthValue);
        this.valueConvertor.put(Attribute.PADDING_TOP, lengthValue);
        this.valueConvertor.put(Attribute.PADDING_BOTTOM, lengthValue);
        this.valueConvertor.put(Attribute.PADDING_LEFT, lengthValue);
        this.valueConvertor.put(Attribute.PADDING_RIGHT, lengthValue);
        BorderWidthValue borderWidthValue = new BorderWidthValue(null, 0);
        this.valueConvertor.put(Attribute.BORDER_TOP_WIDTH, borderWidthValue);
        this.valueConvertor.put(Attribute.BORDER_BOTTOM_WIDTH, borderWidthValue);
        this.valueConvertor.put(Attribute.BORDER_LEFT_WIDTH, borderWidthValue);
        this.valueConvertor.put(Attribute.BORDER_RIGHT_WIDTH, borderWidthValue);
        this.valueConvertor.put(Attribute.TEXT_INDENT, new LengthValue(true));
        this.valueConvertor.put(Attribute.WIDTH, lengthValue);
        this.valueConvertor.put(Attribute.HEIGHT, lengthValue);
        this.valueConvertor.put(Attribute.BORDER_SPACING, lengthValue);
        StringValue stringValue = new StringValue();
        this.valueConvertor.put(Attribute.FONT_STYLE, stringValue);
        this.valueConvertor.put(Attribute.TEXT_DECORATION, stringValue);
        this.valueConvertor.put(Attribute.TEXT_ALIGN, stringValue);
        this.valueConvertor.put(Attribute.VERTICAL_ALIGN, stringValue);
        CssValueMapper cssValueMapper = new CssValueMapper();
        this.valueConvertor.put(Attribute.LIST_STYLE_TYPE, cssValueMapper);
        this.valueConvertor.put(Attribute.BACKGROUND_IMAGE, new BackgroundImage());
        this.valueConvertor.put(Attribute.BACKGROUND_POSITION, new BackgroundPosition());
        this.valueConvertor.put(Attribute.BACKGROUND_REPEAT, cssValueMapper);
        this.valueConvertor.put(Attribute.BACKGROUND_ATTACHMENT, cssValueMapper);
        CssValue cssValue = new CssValue();
        int length = Attribute.allAttributes.length;
        for (int i2 = 0; i2 < length; i2++) {
            Attribute attribute = Attribute.allAttributes[i2];
            if (this.valueConvertor.get(attribute) == null) {
                this.valueConvertor.put(attribute, cssValue);
            }
        }
    }

    void setBaseFontSize(int i2) {
        if (i2 < 1) {
            this.baseFontSize = 0;
        } else if (i2 > 7) {
            this.baseFontSize = 7;
        } else {
            this.baseFontSize = i2;
        }
    }

    void setBaseFontSize(String str) {
        if (str != null) {
            if (str.startsWith(Marker.ANY_NON_NULL_MARKER)) {
                setBaseFontSize(this.baseFontSize + Integer.valueOf(str.substring(1)).intValue());
            } else if (str.startsWith(LanguageTag.SEP)) {
                setBaseFontSize(this.baseFontSize + (-Integer.valueOf(str.substring(1)).intValue()));
            } else {
                setBaseFontSize(Integer.valueOf(str).intValue());
            }
        }
    }

    int getBaseFontSize() {
        return this.baseFontSize;
    }

    void addInternalCSSValue(MutableAttributeSet mutableAttributeSet, Attribute attribute, String str) {
        if (attribute == Attribute.FONT) {
            ShorthandFontParser.parseShorthandFont(this, str, mutableAttributeSet);
            return;
        }
        if (attribute == Attribute.BACKGROUND) {
            ShorthandBackgroundParser.parseShorthandBackground(this, str, mutableAttributeSet);
            return;
        }
        if (attribute == Attribute.MARGIN) {
            ShorthandMarginParser.parseShorthandMargin(this, str, mutableAttributeSet, Attribute.ALL_MARGINS);
            return;
        }
        if (attribute == Attribute.PADDING) {
            ShorthandMarginParser.parseShorthandMargin(this, str, mutableAttributeSet, Attribute.ALL_PADDING);
            return;
        }
        if (attribute == Attribute.BORDER_WIDTH) {
            ShorthandMarginParser.parseShorthandMargin(this, str, mutableAttributeSet, Attribute.ALL_BORDER_WIDTHS);
            return;
        }
        if (attribute == Attribute.BORDER_COLOR) {
            ShorthandMarginParser.parseShorthandMargin(this, str, mutableAttributeSet, Attribute.ALL_BORDER_COLORS);
            return;
        }
        if (attribute == Attribute.BORDER_STYLE) {
            ShorthandMarginParser.parseShorthandMargin(this, str, mutableAttributeSet, Attribute.ALL_BORDER_STYLES);
            return;
        }
        if (attribute == Attribute.BORDER || attribute == Attribute.BORDER_TOP || attribute == Attribute.BORDER_RIGHT || attribute == Attribute.BORDER_BOTTOM || attribute == Attribute.BORDER_LEFT) {
            ShorthandBorderParser.parseShorthandBorder(mutableAttributeSet, attribute, str);
            return;
        }
        Object internalCSSValue = getInternalCSSValue(attribute, str);
        if (internalCSSValue != null) {
            mutableAttributeSet.addAttribute(attribute, internalCSSValue);
        }
    }

    Object getInternalCSSValue(Attribute attribute, String str) {
        CssValue cssValue = (CssValue) this.valueConvertor.get(attribute);
        Object cssValue2 = cssValue.parseCssValue(str);
        return cssValue2 != null ? cssValue2 : cssValue.parseCssValue(attribute.getDefaultValue());
    }

    Attribute styleConstantsKeyToCSSKey(StyleConstants styleConstants) {
        return styleConstantToCssMap.get(styleConstants);
    }

    Object styleConstantsValueToCSSValue(StyleConstants styleConstants, Object obj) {
        Attribute attributeStyleConstantsKeyToCSSKey = styleConstantsKeyToCSSKey(styleConstants);
        if (attributeStyleConstantsKeyToCSSKey != null) {
            return ((CssValue) this.valueConvertor.get(attributeStyleConstantsKeyToCSSKey)).fromStyleConstants(styleConstants, obj);
        }
        return null;
    }

    Object cssValueToStyleConstantsValue(StyleConstants styleConstants, Object obj) {
        if (obj instanceof CssValue) {
            return ((CssValue) obj).toStyleConstants(styleConstants, null);
        }
        return null;
    }

    Font getFont(StyleContext styleContext, AttributeSet attributeSet, int i2, StyleSheet styleSheet) {
        int fontSize = getFontSize(attributeSet, i2, getStyleSheet(styleSheet));
        StringValue stringValue = (StringValue) attributeSet.getAttribute(Attribute.VERTICAL_ALIGN);
        if (stringValue != null) {
            String string = stringValue.toString();
            if (string.indexOf("sup") >= 0 || string.indexOf("sub") >= 0) {
                fontSize -= 2;
            }
        }
        FontFamily fontFamily = (FontFamily) attributeSet.getAttribute(Attribute.FONT_FAMILY);
        String value = fontFamily != null ? fontFamily.getValue() : "SansSerif";
        int i3 = 0;
        FontWeight fontWeight = (FontWeight) attributeSet.getAttribute(Attribute.FONT_WEIGHT);
        if (fontWeight != null && fontWeight.getValue() > 400) {
            i3 = 0 | 1;
        }
        Object attribute = attributeSet.getAttribute(Attribute.FONT_STYLE);
        if (attribute != null && attribute.toString().indexOf("italic") >= 0) {
            i3 |= 2;
        }
        if (value.equalsIgnoreCase("monospace")) {
            value = "Monospaced";
        }
        Font font = styleContext.getFont(value, i3, fontSize);
        if (font == null || (font.getFamily().equals(Font.DIALOG) && !value.equalsIgnoreCase(Font.DIALOG))) {
            font = styleContext.getFont("SansSerif", i3, fontSize);
        }
        return font;
    }

    static int getFontSize(AttributeSet attributeSet, int i2, StyleSheet styleSheet) {
        FontSize fontSize = (FontSize) attributeSet.getAttribute(Attribute.FONT_SIZE);
        return fontSize != null ? fontSize.getValue(attributeSet, styleSheet) : i2;
    }

    Color getColor(AttributeSet attributeSet, Attribute attribute) {
        ColorValue colorValue = (ColorValue) attributeSet.getAttribute(attribute);
        if (colorValue != null) {
            return colorValue.getValue();
        }
        return null;
    }

    float getPointSize(String str, StyleSheet styleSheet) {
        StyleSheet styleSheet2 = getStyleSheet(styleSheet);
        if (str != null) {
            if (str.startsWith(Marker.ANY_NON_NULL_MARKER)) {
                return getPointSize(this.baseFontSize + Integer.valueOf(str.substring(1)).intValue(), styleSheet2);
            }
            if (str.startsWith(LanguageTag.SEP)) {
                return getPointSize(this.baseFontSize + (-Integer.valueOf(str.substring(1)).intValue()), styleSheet2);
            }
            return getPointSize(Integer.valueOf(str).intValue(), styleSheet2);
        }
        return 0.0f;
    }

    float getLength(AttributeSet attributeSet, Attribute attribute, StyleSheet styleSheet) {
        StyleSheet styleSheet2 = getStyleSheet(styleSheet);
        LengthValue lengthValue = (LengthValue) attributeSet.getAttribute(attribute);
        return lengthValue != null ? lengthValue.getValue(styleSheet2 == null ? false : styleSheet2.isW3CLengthUnits()) : 0.0f;
    }

    AttributeSet translateHTMLToCSS(AttributeSet attributeSet) {
        MutableAttributeSet simpleAttributeSet = new SimpleAttributeSet();
        Element element = (Element) attributeSet;
        HTML.Tag hTMLTag = getHTMLTag(attributeSet);
        if (hTMLTag == HTML.Tag.TD || hTMLTag == HTML.Tag.TH) {
            AttributeSet attributes = element.getParentElement().getParentElement().getAttributes();
            if (getTableBorder(attributes) > 0) {
                translateAttribute(HTML.Attribute.BORDER, "1", simpleAttributeSet);
            }
            String str = (String) attributes.getAttribute(HTML.Attribute.CELLPADDING);
            if (str != null) {
                LengthValue lengthValue = (LengthValue) getInternalCSSValue(Attribute.PADDING_TOP, str);
                lengthValue.span = lengthValue.span < 0.0f ? 0.0f : lengthValue.span;
                simpleAttributeSet.addAttribute(Attribute.PADDING_TOP, lengthValue);
                simpleAttributeSet.addAttribute(Attribute.PADDING_BOTTOM, lengthValue);
                simpleAttributeSet.addAttribute(Attribute.PADDING_LEFT, lengthValue);
                simpleAttributeSet.addAttribute(Attribute.PADDING_RIGHT, lengthValue);
            }
        }
        if (element.isLeaf()) {
            translateEmbeddedAttributes(attributeSet, simpleAttributeSet);
        } else {
            translateAttributes(hTMLTag, attributeSet, simpleAttributeSet);
        }
        if (hTMLTag == HTML.Tag.CAPTION) {
            Object attribute = attributeSet.getAttribute(HTML.Attribute.ALIGN);
            if (attribute != null && (attribute.equals(JSplitPane.TOP) || attribute.equals(JSplitPane.BOTTOM))) {
                simpleAttributeSet.addAttribute(Attribute.CAPTION_SIDE, attribute);
                simpleAttributeSet.removeAttribute(Attribute.TEXT_ALIGN);
            } else {
                Object attribute2 = attributeSet.getAttribute(HTML.Attribute.VALIGN);
                if (attribute2 != null) {
                    simpleAttributeSet.addAttribute(Attribute.CAPTION_SIDE, attribute2);
                }
            }
        }
        return simpleAttributeSet;
    }

    private static int getTableBorder(AttributeSet attributeSet) {
        String str = (String) attributeSet.getAttribute(HTML.Attribute.BORDER);
        if (str == HTML.NULL_ATTRIBUTE_VALUE || "".equals(str)) {
            return 1;
        }
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException e2) {
            return 0;
        }
    }

    static {
        for (int i2 = 0; i2 < Attribute.allAttributes.length; i2++) {
            attributeMap.put(Attribute.allAttributes[i2].toString(), Attribute.allAttributes[i2]);
        }
        for (int i3 = 0; i3 < Value.allValues.length; i3++) {
            valueMap.put(Value.allValues[i3].toString(), Value.allValues[i3]);
        }
        htmlAttrToCssAttrMap.put(HTML.Attribute.COLOR, new Attribute[]{Attribute.COLOR});
        htmlAttrToCssAttrMap.put(HTML.Attribute.TEXT, new Attribute[]{Attribute.COLOR});
        htmlAttrToCssAttrMap.put(HTML.Attribute.CLEAR, new Attribute[]{Attribute.CLEAR});
        htmlAttrToCssAttrMap.put(HTML.Attribute.BACKGROUND, new Attribute[]{Attribute.BACKGROUND_IMAGE});
        htmlAttrToCssAttrMap.put(HTML.Attribute.BGCOLOR, new Attribute[]{Attribute.BACKGROUND_COLOR});
        htmlAttrToCssAttrMap.put(HTML.Attribute.WIDTH, new Attribute[]{Attribute.WIDTH});
        htmlAttrToCssAttrMap.put(HTML.Attribute.HEIGHT, new Attribute[]{Attribute.HEIGHT});
        htmlAttrToCssAttrMap.put(HTML.Attribute.BORDER, new Attribute[]{Attribute.BORDER_TOP_WIDTH, Attribute.BORDER_RIGHT_WIDTH, Attribute.BORDER_BOTTOM_WIDTH, Attribute.BORDER_LEFT_WIDTH});
        htmlAttrToCssAttrMap.put(HTML.Attribute.CELLPADDING, new Attribute[]{Attribute.PADDING});
        htmlAttrToCssAttrMap.put(HTML.Attribute.CELLSPACING, new Attribute[]{Attribute.BORDER_SPACING});
        htmlAttrToCssAttrMap.put(HTML.Attribute.MARGINWIDTH, new Attribute[]{Attribute.MARGIN_LEFT, Attribute.MARGIN_RIGHT});
        htmlAttrToCssAttrMap.put(HTML.Attribute.MARGINHEIGHT, new Attribute[]{Attribute.MARGIN_TOP, Attribute.MARGIN_BOTTOM});
        htmlAttrToCssAttrMap.put(HTML.Attribute.HSPACE, new Attribute[]{Attribute.PADDING_LEFT, Attribute.PADDING_RIGHT});
        htmlAttrToCssAttrMap.put(HTML.Attribute.VSPACE, new Attribute[]{Attribute.PADDING_BOTTOM, Attribute.PADDING_TOP});
        htmlAttrToCssAttrMap.put(HTML.Attribute.FACE, new Attribute[]{Attribute.FONT_FAMILY});
        htmlAttrToCssAttrMap.put(HTML.Attribute.SIZE, new Attribute[]{Attribute.FONT_SIZE});
        htmlAttrToCssAttrMap.put(HTML.Attribute.VALIGN, new Attribute[]{Attribute.VERTICAL_ALIGN});
        htmlAttrToCssAttrMap.put(HTML.Attribute.ALIGN, new Attribute[]{Attribute.VERTICAL_ALIGN, Attribute.TEXT_ALIGN, Attribute.FLOAT});
        htmlAttrToCssAttrMap.put(HTML.Attribute.TYPE, new Attribute[]{Attribute.LIST_STYLE_TYPE});
        htmlAttrToCssAttrMap.put(HTML.Attribute.NOWRAP, new Attribute[]{Attribute.WHITE_SPACE});
        styleConstantToCssMap.put(StyleConstants.FontFamily, Attribute.FONT_FAMILY);
        styleConstantToCssMap.put(StyleConstants.FontSize, Attribute.FONT_SIZE);
        styleConstantToCssMap.put(StyleConstants.Bold, Attribute.FONT_WEIGHT);
        styleConstantToCssMap.put(StyleConstants.Italic, Attribute.FONT_STYLE);
        styleConstantToCssMap.put(StyleConstants.Underline, Attribute.TEXT_DECORATION);
        styleConstantToCssMap.put(StyleConstants.StrikeThrough, Attribute.TEXT_DECORATION);
        styleConstantToCssMap.put(StyleConstants.Superscript, Attribute.VERTICAL_ALIGN);
        styleConstantToCssMap.put(StyleConstants.Subscript, Attribute.VERTICAL_ALIGN);
        styleConstantToCssMap.put(StyleConstants.Foreground, Attribute.COLOR);
        styleConstantToCssMap.put(StyleConstants.Background, Attribute.BACKGROUND_COLOR);
        styleConstantToCssMap.put(StyleConstants.FirstLineIndent, Attribute.TEXT_INDENT);
        styleConstantToCssMap.put(StyleConstants.LeftIndent, Attribute.MARGIN_LEFT);
        styleConstantToCssMap.put(StyleConstants.RightIndent, Attribute.MARGIN_RIGHT);
        styleConstantToCssMap.put(StyleConstants.SpaceAbove, Attribute.MARGIN_TOP);
        styleConstantToCssMap.put(StyleConstants.SpaceBelow, Attribute.MARGIN_BOTTOM);
        styleConstantToCssMap.put(StyleConstants.Alignment, Attribute.TEXT_ALIGN);
        htmlValueToCssValueMap.put("disc", Value.DISC);
        htmlValueToCssValueMap.put("square", Value.SQUARE);
        htmlValueToCssValueMap.put("circle", Value.CIRCLE);
        htmlValueToCssValueMap.put("1", Value.DECIMAL);
        htmlValueToCssValueMap.put("a", Value.LOWER_ALPHA);
        htmlValueToCssValueMap.put("A", Value.UPPER_ALPHA);
        htmlValueToCssValueMap.put(PdfOps.i_TOKEN, Value.LOWER_ROMAN);
        htmlValueToCssValueMap.put("I", Value.UPPER_ROMAN);
        cssValueToInternalValueMap.put(Separation.COLORANT_NONE, Value.NONE);
        cssValueToInternalValueMap.put("disc", Value.DISC);
        cssValueToInternalValueMap.put("square", Value.SQUARE);
        cssValueToInternalValueMap.put("circle", Value.CIRCLE);
        cssValueToInternalValueMap.put(SchemaSymbols.ATTVAL_DECIMAL, Value.DECIMAL);
        cssValueToInternalValueMap.put("lower-roman", Value.LOWER_ROMAN);
        cssValueToInternalValueMap.put("upper-roman", Value.UPPER_ROMAN);
        cssValueToInternalValueMap.put("lower-alpha", Value.LOWER_ALPHA);
        cssValueToInternalValueMap.put("upper-alpha", Value.UPPER_ALPHA);
        cssValueToInternalValueMap.put("repeat", Value.BACKGROUND_REPEAT);
        cssValueToInternalValueMap.put("no-repeat", Value.BACKGROUND_NO_REPEAT);
        cssValueToInternalValueMap.put("repeat-x", Value.BACKGROUND_REPEAT_X);
        cssValueToInternalValueMap.put("repeat-y", Value.BACKGROUND_REPEAT_Y);
        cssValueToInternalValueMap.put("scroll", Value.BACKGROUND_SCROLL);
        cssValueToInternalValueMap.put("fixed", Value.BACKGROUND_FIXED);
        try {
            for (Attribute attribute : Attribute.allAttributes) {
                StyleContext.registerStaticAttributeKey(attribute);
            }
        } catch (Throwable th) {
            th.printStackTrace();
        }
        try {
            for (Value value : Value.allValues) {
                StyleContext.registerStaticAttributeKey(value);
            }
        } catch (Throwable th2) {
            th2.printStackTrace();
        }
        baseFontSizeIndex = 3;
    }

    public static Attribute[] getAllAttributeKeys() {
        Attribute[] attributeArr = new Attribute[Attribute.allAttributes.length];
        System.arraycopy(Attribute.allAttributes, 0, attributeArr, 0, Attribute.allAttributes.length);
        return attributeArr;
    }

    public static final Attribute getAttribute(String str) {
        return attributeMap.get(str);
    }

    static final Value getValue(String str) {
        return valueMap.get(str);
    }

    static URL getURL(URL url, String str) {
        if (str == null) {
            return null;
        }
        if (str.startsWith("url(") && str.endsWith(")")) {
            str = str.substring(4, str.length() - 1);
        }
        try {
            URL url2 = new URL(str);
            if (url2 != null) {
                return url2;
            }
        } catch (MalformedURLException e2) {
        }
        if (url != null) {
            try {
                return new URL(url, str);
            } catch (MalformedURLException e3) {
                return null;
            }
        }
        return null;
    }

    static String colorToHex(Color color) {
        String str = FXMLLoader.CONTROLLER_METHOD_PREFIX;
        String hexString = Integer.toHexString(color.getRed());
        if (hexString.length() > 2) {
            hexString.substring(0, 2);
        } else if (hexString.length() < 2) {
            str = str + "0" + hexString;
        } else {
            str = str + hexString;
        }
        String hexString2 = Integer.toHexString(color.getGreen());
        if (hexString2.length() > 2) {
            hexString2.substring(0, 2);
        } else if (hexString2.length() < 2) {
            str = str + "0" + hexString2;
        } else {
            str = str + hexString2;
        }
        String hexString3 = Integer.toHexString(color.getBlue());
        if (hexString3.length() > 2) {
            hexString3.substring(0, 2);
        } else if (hexString3.length() < 2) {
            str = str + "0" + hexString3;
        } else {
            str = str + hexString3;
        }
        return str;
    }

    static final Color hexToColor(String str) {
        String strSubstring;
        Color colorDecode;
        str.length();
        if (str.startsWith(FXMLLoader.CONTROLLER_METHOD_PREFIX)) {
            strSubstring = str.substring(1, Math.min(str.length(), 7));
        } else {
            strSubstring = str;
        }
        try {
            colorDecode = Color.decode("0x" + strSubstring);
        } catch (NumberFormatException e2) {
            colorDecode = null;
        }
        return colorDecode;
    }

    static Color stringToColor(String str) {
        Color colorHexToColor;
        if (str == null) {
            return null;
        }
        if (str.length() == 0) {
            colorHexToColor = Color.black;
        } else if (str.startsWith("rgb(")) {
            colorHexToColor = parseRGB(str);
        } else if (str.charAt(0) == '#') {
            colorHexToColor = hexToColor(str);
        } else if (str.equalsIgnoreCase("Black")) {
            colorHexToColor = hexToColor("#000000");
        } else if (str.equalsIgnoreCase("Silver")) {
            colorHexToColor = hexToColor("#C0C0C0");
        } else if (str.equalsIgnoreCase("Gray")) {
            colorHexToColor = hexToColor("#808080");
        } else if (str.equalsIgnoreCase("White")) {
            colorHexToColor = hexToColor("#FFFFFF");
        } else if (str.equalsIgnoreCase("Maroon")) {
            colorHexToColor = hexToColor("#800000");
        } else if (str.equalsIgnoreCase("Red")) {
            colorHexToColor = hexToColor("#FF0000");
        } else if (str.equalsIgnoreCase("Purple")) {
            colorHexToColor = hexToColor("#800080");
        } else if (str.equalsIgnoreCase("Fuchsia")) {
            colorHexToColor = hexToColor("#FF00FF");
        } else if (str.equalsIgnoreCase("Green")) {
            colorHexToColor = hexToColor("#008000");
        } else if (str.equalsIgnoreCase("Lime")) {
            colorHexToColor = hexToColor("#00FF00");
        } else if (str.equalsIgnoreCase("Olive")) {
            colorHexToColor = hexToColor("#808000");
        } else if (str.equalsIgnoreCase("Yellow")) {
            colorHexToColor = hexToColor("#FFFF00");
        } else if (str.equalsIgnoreCase("Navy")) {
            colorHexToColor = hexToColor("#000080");
        } else if (str.equalsIgnoreCase("Blue")) {
            colorHexToColor = hexToColor("#0000FF");
        } else if (str.equalsIgnoreCase("Teal")) {
            colorHexToColor = hexToColor("#008080");
        } else if (str.equalsIgnoreCase("Aqua")) {
            colorHexToColor = hexToColor("#00FFFF");
        } else if (str.equalsIgnoreCase("Orange")) {
            colorHexToColor = hexToColor("#FF8000");
        } else {
            colorHexToColor = hexToColor(str);
        }
        return colorHexToColor;
    }

    private static Color parseRGB(String str) {
        int[] iArr = {4};
        return new Color(getColorComponent(str, iArr), getColorComponent(str, iArr), getColorComponent(str, iArr));
    }

    private static int getColorComponent(String str, int[] iArr) {
        char cCharAt;
        int length = str.length();
        while (iArr[0] < length && (cCharAt = str.charAt(iArr[0])) != '-' && !Character.isDigit(cCharAt) && cCharAt != '.') {
            iArr[0] = iArr[0] + 1;
        }
        int i2 = iArr[0];
        if (i2 < length && str.charAt(iArr[0]) == '-') {
            iArr[0] = iArr[0] + 1;
        }
        while (iArr[0] < length && Character.isDigit(str.charAt(iArr[0]))) {
            iArr[0] = iArr[0] + 1;
        }
        if (iArr[0] < length && str.charAt(iArr[0]) == '.') {
            iArr[0] = iArr[0] + 1;
            while (iArr[0] < length && Character.isDigit(str.charAt(iArr[0]))) {
                iArr[0] = iArr[0] + 1;
            }
        }
        if (i2 != iArr[0]) {
            try {
                float f2 = Float.parseFloat(str.substring(i2, iArr[0]));
                if (iArr[0] < length && str.charAt(iArr[0]) == '%') {
                    iArr[0] = iArr[0] + 1;
                    f2 = (f2 * 255.0f) / 100.0f;
                }
                return Math.min(255, Math.max(0, (int) f2));
            } catch (NumberFormatException e2) {
                return 0;
            }
        }
        return 0;
    }

    static int getIndexOfSize(float f2, int[] iArr) {
        for (int i2 = 0; i2 < iArr.length; i2++) {
            if (f2 <= iArr[i2]) {
                return i2 + 1;
            }
        }
        return iArr.length;
    }

    static int getIndexOfSize(float f2, StyleSheet styleSheet) {
        return getIndexOfSize(f2, styleSheet != null ? styleSheet.getSizeMap() : StyleSheet.sizeMapDefault);
    }

    static String[] parseStrings(String str) {
        int length = str == null ? 0 : str.length();
        Vector vector = new Vector(4);
        int i2 = 0;
        while (i2 < length) {
            while (i2 < length && Character.isWhitespace(str.charAt(i2))) {
                i2++;
            }
            int i3 = i2;
            while (i2 < length && !Character.isWhitespace(str.charAt(i2))) {
                i2++;
            }
            if (i3 != i2) {
                vector.addElement(str.substring(i3, i2));
            }
            i2++;
        }
        String[] strArr = new String[vector.size()];
        vector.copyInto(strArr);
        return strArr;
    }

    float getPointSize(int i2, StyleSheet styleSheet) {
        StyleSheet styleSheet2 = getStyleSheet(styleSheet);
        int[] sizeMap = styleSheet2 != null ? styleSheet2.getSizeMap() : StyleSheet.sizeMapDefault;
        int i3 = i2 - 1;
        if (i3 < 0) {
            return sizeMap[0];
        }
        if (i3 > sizeMap.length - 1) {
            return sizeMap[sizeMap.length - 1];
        }
        return sizeMap[i3];
    }

    private void translateEmbeddedAttributes(AttributeSet attributeSet, MutableAttributeSet mutableAttributeSet) {
        Enumeration<?> attributeNames = attributeSet.getAttributeNames();
        if (attributeSet.getAttribute(StyleConstants.NameAttribute) == HTML.Tag.HR) {
            translateAttributes(HTML.Tag.HR, attributeSet, mutableAttributeSet);
        }
        while (attributeNames.hasMoreElements()) {
            Object objNextElement = attributeNames.nextElement2();
            if (objNextElement instanceof HTML.Tag) {
                HTML.Tag tag = (HTML.Tag) objNextElement;
                Object attribute = attributeSet.getAttribute(tag);
                if (attribute != null && (attribute instanceof AttributeSet)) {
                    translateAttributes(tag, (AttributeSet) attribute, mutableAttributeSet);
                }
            } else if (objNextElement instanceof Attribute) {
                mutableAttributeSet.addAttribute(objNextElement, attributeSet.getAttribute(objNextElement));
            }
        }
    }

    private void translateAttributes(HTML.Tag tag, AttributeSet attributeSet, MutableAttributeSet mutableAttributeSet) {
        Attribute cssAlignAttribute;
        Object cssValue;
        Enumeration<?> attributeNames = attributeSet.getAttributeNames();
        while (attributeNames.hasMoreElements()) {
            Object objNextElement = attributeNames.nextElement2();
            if (objNextElement instanceof HTML.Attribute) {
                HTML.Attribute attribute = (HTML.Attribute) objNextElement;
                if (attribute == HTML.Attribute.ALIGN) {
                    String str = (String) attributeSet.getAttribute(HTML.Attribute.ALIGN);
                    if (str != null && (cssAlignAttribute = getCssAlignAttribute(tag, attributeSet)) != null && (cssValue = getCssValue(cssAlignAttribute, str)) != null) {
                        mutableAttributeSet.addAttribute(cssAlignAttribute, cssValue);
                    }
                } else if (attribute != HTML.Attribute.SIZE || isHTMLFontTag(tag)) {
                    if (tag == HTML.Tag.TABLE && attribute == HTML.Attribute.BORDER) {
                        int tableBorder = getTableBorder(attributeSet);
                        if (tableBorder > 0) {
                            translateAttribute(HTML.Attribute.BORDER, Integer.toString(tableBorder), mutableAttributeSet);
                        }
                    } else {
                        translateAttribute(attribute, (String) attributeSet.getAttribute(attribute), mutableAttributeSet);
                    }
                }
            } else if (objNextElement instanceof Attribute) {
                mutableAttributeSet.addAttribute(objNextElement, attributeSet.getAttribute(objNextElement));
            }
        }
    }

    private void translateAttribute(HTML.Attribute attribute, String str, MutableAttributeSet mutableAttributeSet) {
        Attribute[] cssAttribute = getCssAttribute(attribute);
        if (cssAttribute == null || str == null) {
            return;
        }
        for (Attribute attribute2 : cssAttribute) {
            Object cssValue = getCssValue(attribute2, str);
            if (cssValue != null) {
                mutableAttributeSet.addAttribute(attribute2, cssValue);
            }
        }
    }

    Object getCssValue(Attribute attribute, String str) {
        return ((CssValue) this.valueConvertor.get(attribute)).parseHtmlValue(str);
    }

    private Attribute[] getCssAttribute(HTML.Attribute attribute) {
        return htmlAttrToCssAttrMap.get(attribute);
    }

    private Attribute getCssAlignAttribute(HTML.Tag tag, AttributeSet attributeSet) {
        return Attribute.TEXT_ALIGN;
    }

    private HTML.Tag getHTMLTag(AttributeSet attributeSet) {
        Object attribute = attributeSet.getAttribute(StyleConstants.NameAttribute);
        if (attribute instanceof HTML.Tag) {
            return (HTML.Tag) attribute;
        }
        return null;
    }

    private boolean isHTMLFontTag(HTML.Tag tag) {
        return tag != null && (tag == HTML.Tag.FONT || tag == HTML.Tag.BASEFONT);
    }

    private boolean isFloater(String str) {
        return str.equals(JSplitPane.LEFT) || str.equals(JSplitPane.RIGHT);
    }

    private boolean validTextAlignValue(String str) {
        return isFloater(str) || str.equals("center");
    }

    /* loaded from: rt.jar:javax/swing/text/html/CSS$CssValue.class */
    static class CssValue implements Serializable {
        String svalue;

        CssValue() {
        }

        Object parseCssValue(String str) {
            return str;
        }

        Object parseHtmlValue(String str) {
            return parseCssValue(str);
        }

        Object fromStyleConstants(StyleConstants styleConstants, Object obj) {
            return null;
        }

        Object toStyleConstants(StyleConstants styleConstants, View view) {
            return null;
        }

        public String toString() {
            return this.svalue;
        }
    }

    /* loaded from: rt.jar:javax/swing/text/html/CSS$StringValue.class */
    static class StringValue extends CssValue {
        StringValue() {
        }

        @Override // javax.swing.text.html.CSS.CssValue
        Object parseCssValue(String str) {
            StringValue stringValue = new StringValue();
            stringValue.svalue = str;
            return stringValue;
        }

        @Override // javax.swing.text.html.CSS.CssValue
        Object fromStyleConstants(StyleConstants styleConstants, Object obj) {
            String str;
            if (styleConstants == StyleConstants.Italic) {
                if (obj.equals(Boolean.TRUE)) {
                    return parseCssValue("italic");
                }
                return parseCssValue("");
            }
            if (styleConstants == StyleConstants.Underline) {
                if (obj.equals(Boolean.TRUE)) {
                    return parseCssValue("underline");
                }
                return parseCssValue("");
            }
            if (styleConstants == StyleConstants.Alignment) {
                switch (((Integer) obj).intValue()) {
                    case 0:
                        str = JSplitPane.LEFT;
                        break;
                    case 1:
                        str = "center";
                        break;
                    case 2:
                        str = JSplitPane.RIGHT;
                        break;
                    case 3:
                        str = "justify";
                        break;
                    default:
                        str = JSplitPane.LEFT;
                        break;
                }
                return parseCssValue(str);
            }
            if (styleConstants == StyleConstants.StrikeThrough) {
                if (obj.equals(Boolean.TRUE)) {
                    return parseCssValue("line-through");
                }
                return parseCssValue("");
            }
            if (styleConstants == StyleConstants.Superscript) {
                if (obj.equals(Boolean.TRUE)) {
                    return parseCssValue("super");
                }
                return parseCssValue("");
            }
            if (styleConstants == StyleConstants.Subscript) {
                if (obj.equals(Boolean.TRUE)) {
                    return parseCssValue("sub");
                }
                return parseCssValue("");
            }
            return null;
        }

        @Override // javax.swing.text.html.CSS.CssValue
        Object toStyleConstants(StyleConstants styleConstants, View view) {
            if (styleConstants == StyleConstants.Italic) {
                if (this.svalue.indexOf("italic") >= 0) {
                    return Boolean.TRUE;
                }
                return Boolean.FALSE;
            }
            if (styleConstants == StyleConstants.Underline) {
                if (this.svalue.indexOf("underline") >= 0) {
                    return Boolean.TRUE;
                }
                return Boolean.FALSE;
            }
            if (styleConstants == StyleConstants.Alignment) {
                if (this.svalue.equals(JSplitPane.RIGHT)) {
                    return new Integer(2);
                }
                if (this.svalue.equals("center")) {
                    return new Integer(1);
                }
                if (this.svalue.equals("justify")) {
                    return new Integer(3);
                }
                return new Integer(0);
            }
            if (styleConstants == StyleConstants.StrikeThrough) {
                if (this.svalue.indexOf("line-through") >= 0) {
                    return Boolean.TRUE;
                }
                return Boolean.FALSE;
            }
            if (styleConstants == StyleConstants.Superscript) {
                if (this.svalue.indexOf("super") >= 0) {
                    return Boolean.TRUE;
                }
                return Boolean.FALSE;
            }
            if (styleConstants == StyleConstants.Subscript) {
                if (this.svalue.indexOf("sub") >= 0) {
                    return Boolean.TRUE;
                }
                return Boolean.FALSE;
            }
            return null;
        }

        boolean isItalic() {
            return this.svalue.indexOf("italic") != -1;
        }

        boolean isStrike() {
            return this.svalue.indexOf("line-through") != -1;
        }

        boolean isUnderline() {
            return this.svalue.indexOf("underline") != -1;
        }

        boolean isSub() {
            return this.svalue.indexOf("sub") != -1;
        }

        boolean isSup() {
            return this.svalue.indexOf("sup") != -1;
        }
    }

    /* loaded from: rt.jar:javax/swing/text/html/CSS$FontSize.class */
    class FontSize extends CssValue {
        float value;
        boolean index;
        LengthUnit lu;

        FontSize() {
        }

        int getValue(AttributeSet attributeSet, StyleSheet styleSheet) {
            AttributeSet resolveParent;
            float f2;
            StyleSheet styleSheet2 = CSS.this.getStyleSheet(styleSheet);
            if (this.index) {
                return Math.round(CSS.this.getPointSize((int) this.value, styleSheet2));
            }
            if (this.lu == null) {
                return Math.round(this.value);
            }
            if (this.lu.type == 0) {
                return Math.round(this.lu.getValue(styleSheet2 == null ? false : styleSheet2.isW3CLengthUnits()));
            }
            if (attributeSet != null && (resolveParent = attributeSet.getResolveParent()) != null) {
                int fontSize = StyleConstants.getFontSize(resolveParent);
                if (this.lu.type == 1 || this.lu.type == 3) {
                    f2 = this.lu.value * fontSize;
                } else {
                    f2 = this.lu.value + fontSize;
                }
                return Math.round(f2);
            }
            return 12;
        }

        @Override // javax.swing.text.html.CSS.CssValue
        Object parseCssValue(String str) {
            FontSize fontSize = CSS.this.new FontSize();
            fontSize.svalue = str;
            try {
                if (str.equals("xx-small")) {
                    fontSize.value = 1.0f;
                    fontSize.index = true;
                } else if (str.equals("x-small")) {
                    fontSize.value = 2.0f;
                    fontSize.index = true;
                } else if (str.equals(NimbusStyle.SMALL_KEY)) {
                    fontSize.value = 3.0f;
                    fontSize.index = true;
                } else if (str.equals("medium")) {
                    fontSize.value = 4.0f;
                    fontSize.index = true;
                } else if (str.equals(NimbusStyle.LARGE_KEY)) {
                    fontSize.value = 5.0f;
                    fontSize.index = true;
                } else if (str.equals("x-large")) {
                    fontSize.value = 6.0f;
                    fontSize.index = true;
                } else if (str.equals("xx-large")) {
                    fontSize.value = 7.0f;
                    fontSize.index = true;
                } else {
                    fontSize.lu = new LengthUnit(str, (short) 1, 1.0f);
                }
            } catch (NumberFormatException e2) {
                fontSize = null;
            }
            return fontSize;
        }

        @Override // javax.swing.text.html.CSS.CssValue
        Object parseHtmlValue(String str) {
            if (str == null || str.length() == 0) {
                return null;
            }
            FontSize fontSize = CSS.this.new FontSize();
            fontSize.svalue = str;
            try {
                int baseFontSize = CSS.this.getBaseFontSize();
                if (str.charAt(0) == '+') {
                    fontSize.value = baseFontSize + Integer.valueOf(str.substring(1)).intValue();
                    fontSize.index = true;
                } else if (str.charAt(0) == '-') {
                    fontSize.value = baseFontSize + (-Integer.valueOf(str.substring(1)).intValue());
                    fontSize.index = true;
                } else {
                    fontSize.value = Integer.parseInt(str);
                    if (fontSize.value > 7.0f) {
                        fontSize.value = 7.0f;
                    } else if (fontSize.value < 0.0f) {
                        fontSize.value = 0.0f;
                    }
                    fontSize.index = true;
                }
            } catch (NumberFormatException e2) {
                fontSize = null;
            }
            return fontSize;
        }

        @Override // javax.swing.text.html.CSS.CssValue
        Object fromStyleConstants(StyleConstants styleConstants, Object obj) {
            if (obj instanceof Number) {
                FontSize fontSize = CSS.this.new FontSize();
                fontSize.value = CSS.getIndexOfSize(((Number) obj).floatValue(), StyleSheet.sizeMapDefault);
                fontSize.svalue = Integer.toString((int) fontSize.value);
                fontSize.index = true;
                return fontSize;
            }
            return parseCssValue(obj.toString());
        }

        @Override // javax.swing.text.html.CSS.CssValue
        Object toStyleConstants(StyleConstants styleConstants, View view) {
            if (view != null) {
                return Integer.valueOf(getValue(view.getAttributes(), null));
            }
            return Integer.valueOf(getValue(null, null));
        }
    }

    /* loaded from: rt.jar:javax/swing/text/html/CSS$FontFamily.class */
    static class FontFamily extends CssValue {
        String family;

        FontFamily() {
        }

        String getValue() {
            return this.family;
        }

        @Override // javax.swing.text.html.CSS.CssValue
        Object parseCssValue(String str) {
            int iIndexOf = str.indexOf(44);
            FontFamily fontFamily = new FontFamily();
            fontFamily.svalue = str;
            fontFamily.family = null;
            if (iIndexOf == -1) {
                setFontName(fontFamily, str);
            } else {
                boolean z2 = false;
                int length = str.length();
                int iIndexOf2 = 0;
                while (!z2) {
                    while (iIndexOf2 < length && Character.isWhitespace(str.charAt(iIndexOf2))) {
                        iIndexOf2++;
                    }
                    int i2 = iIndexOf2;
                    iIndexOf2 = str.indexOf(44, iIndexOf2);
                    if (iIndexOf2 == -1) {
                        iIndexOf2 = length;
                    }
                    if (i2 < length) {
                        if (i2 != iIndexOf2) {
                            int i3 = iIndexOf2;
                            if (iIndexOf2 > 0 && str.charAt(iIndexOf2 - 1) == ' ') {
                                i3--;
                            }
                            setFontName(fontFamily, str.substring(i2, i3));
                            z2 = fontFamily.family != null;
                        }
                        iIndexOf2++;
                    } else {
                        z2 = true;
                    }
                }
            }
            if (fontFamily.family == null) {
                fontFamily.family = "SansSerif";
            }
            return fontFamily;
        }

        private void setFontName(FontFamily fontFamily, String str) {
            fontFamily.family = str;
        }

        @Override // javax.swing.text.html.CSS.CssValue
        Object parseHtmlValue(String str) {
            return parseCssValue(str);
        }

        @Override // javax.swing.text.html.CSS.CssValue
        Object fromStyleConstants(StyleConstants styleConstants, Object obj) {
            return parseCssValue(obj.toString());
        }

        @Override // javax.swing.text.html.CSS.CssValue
        Object toStyleConstants(StyleConstants styleConstants, View view) {
            return this.family;
        }
    }

    /* loaded from: rt.jar:javax/swing/text/html/CSS$FontWeight.class */
    static class FontWeight extends CssValue {
        int weight;

        FontWeight() {
        }

        int getValue() {
            return this.weight;
        }

        @Override // javax.swing.text.html.CSS.CssValue
        Object parseCssValue(String str) {
            FontWeight fontWeight = new FontWeight();
            fontWeight.svalue = str;
            if (str.equals("bold")) {
                fontWeight.weight = Font2D.FWEIGHT_BOLD;
            } else if (str.equals("normal")) {
                fontWeight.weight = 400;
            } else {
                try {
                    fontWeight.weight = Integer.parseInt(str);
                } catch (NumberFormatException e2) {
                    fontWeight = null;
                }
            }
            return fontWeight;
        }

        @Override // javax.swing.text.html.CSS.CssValue
        Object fromStyleConstants(StyleConstants styleConstants, Object obj) {
            if (obj.equals(Boolean.TRUE)) {
                return parseCssValue("bold");
            }
            return parseCssValue("normal");
        }

        @Override // javax.swing.text.html.CSS.CssValue
        Object toStyleConstants(StyleConstants styleConstants, View view) {
            return this.weight > 500 ? Boolean.TRUE : Boolean.FALSE;
        }

        boolean isBold() {
            return this.weight > 500;
        }
    }

    /* loaded from: rt.jar:javax/swing/text/html/CSS$ColorValue.class */
    static class ColorValue extends CssValue {

        /* renamed from: c, reason: collision with root package name */
        Color f12844c;

        ColorValue() {
        }

        Color getValue() {
            return this.f12844c;
        }

        @Override // javax.swing.text.html.CSS.CssValue
        Object parseCssValue(String str) {
            Color colorStringToColor = CSS.stringToColor(str);
            if (colorStringToColor != null) {
                ColorValue colorValue = new ColorValue();
                colorValue.svalue = str;
                colorValue.f12844c = colorStringToColor;
                return colorValue;
            }
            return null;
        }

        @Override // javax.swing.text.html.CSS.CssValue
        Object parseHtmlValue(String str) {
            return parseCssValue(str);
        }

        @Override // javax.swing.text.html.CSS.CssValue
        Object fromStyleConstants(StyleConstants styleConstants, Object obj) {
            ColorValue colorValue = new ColorValue();
            colorValue.f12844c = (Color) obj;
            colorValue.svalue = CSS.colorToHex(colorValue.f12844c);
            return colorValue;
        }

        @Override // javax.swing.text.html.CSS.CssValue
        Object toStyleConstants(StyleConstants styleConstants, View view) {
            return this.f12844c;
        }
    }

    /* loaded from: rt.jar:javax/swing/text/html/CSS$BorderStyle.class */
    static class BorderStyle extends CssValue {
        private transient Value style;

        BorderStyle() {
        }

        Value getValue() {
            return this.style;
        }

        @Override // javax.swing.text.html.CSS.CssValue
        Object parseCssValue(String str) {
            Value value = CSS.getValue(str);
            if (value != null) {
                if (value == Value.INSET || value == Value.OUTSET || value == Value.NONE || value == Value.DOTTED || value == Value.DASHED || value == Value.SOLID || value == Value.DOUBLE || value == Value.GROOVE || value == Value.RIDGE) {
                    BorderStyle borderStyle = new BorderStyle();
                    borderStyle.svalue = str;
                    borderStyle.style = value;
                    return borderStyle;
                }
                return null;
            }
            return null;
        }

        private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
            objectOutputStream.defaultWriteObject();
            if (this.style == null) {
                objectOutputStream.writeObject(null);
            } else {
                objectOutputStream.writeObject(this.style.toString());
            }
        }

        private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
            objectInputStream.defaultReadObject();
            Object object = objectInputStream.readObject();
            if (object != null) {
                this.style = CSS.getValue((String) object);
            }
        }
    }

    /* loaded from: rt.jar:javax/swing/text/html/CSS$LengthValue.class */
    static class LengthValue extends CssValue {
        boolean mayBeNegative;
        boolean percentage;
        float span;
        String units;

        LengthValue() {
            this(false);
        }

        LengthValue(boolean z2) {
            this.units = null;
            this.mayBeNegative = z2;
        }

        float getValue() {
            return getValue(false);
        }

        float getValue(boolean z2) {
            return getValue(0.0f, z2);
        }

        float getValue(float f2) {
            return getValue(f2, false);
        }

        float getValue(float f2, boolean z2) {
            if (this.percentage) {
                return this.span * f2;
            }
            return LengthUnit.getValue(this.span, this.units, Boolean.valueOf(z2));
        }

        boolean isPercentage() {
            return this.percentage;
        }

        @Override // javax.swing.text.html.CSS.CssValue
        Object parseCssValue(String str) {
            LengthValue lengthValue;
            try {
                float fFloatValue = Float.valueOf(str).floatValue();
                lengthValue = new LengthValue();
                lengthValue.span = fFloatValue;
            } catch (NumberFormatException e2) {
                LengthUnit lengthUnit = new LengthUnit(str, (short) 10, 0.0f);
                switch (lengthUnit.type) {
                    case 0:
                        lengthValue = new LengthValue();
                        lengthValue.span = this.mayBeNegative ? lengthUnit.value : Math.max(0.0f, lengthUnit.value);
                        lengthValue.units = lengthUnit.units;
                        break;
                    case 1:
                        lengthValue = new LengthValue();
                        lengthValue.span = Math.max(0.0f, Math.min(1.0f, lengthUnit.value));
                        lengthValue.percentage = true;
                        break;
                    default:
                        return null;
                }
            }
            lengthValue.svalue = str;
            return lengthValue;
        }

        @Override // javax.swing.text.html.CSS.CssValue
        Object parseHtmlValue(String str) {
            if (str.equals(HTML.NULL_ATTRIBUTE_VALUE)) {
                str = "1";
            }
            return parseCssValue(str);
        }

        @Override // javax.swing.text.html.CSS.CssValue
        Object fromStyleConstants(StyleConstants styleConstants, Object obj) {
            LengthValue lengthValue = new LengthValue();
            lengthValue.svalue = obj.toString();
            lengthValue.span = ((Float) obj).floatValue();
            return lengthValue;
        }

        @Override // javax.swing.text.html.CSS.CssValue
        Object toStyleConstants(StyleConstants styleConstants, View view) {
            return new Float(getValue(false));
        }
    }

    /* loaded from: rt.jar:javax/swing/text/html/CSS$BorderWidthValue.class */
    static class BorderWidthValue extends LengthValue {
        private static final float[] values = {1.0f, 2.0f, 4.0f};

        BorderWidthValue(String str, int i2) {
            this.svalue = str;
            this.span = values[i2];
            this.percentage = false;
        }

        @Override // javax.swing.text.html.CSS.LengthValue, javax.swing.text.html.CSS.CssValue
        Object parseCssValue(String str) {
            if (str != null) {
                if (str.equals("thick")) {
                    return new BorderWidthValue(str, 2);
                }
                if (str.equals("medium")) {
                    return new BorderWidthValue(str, 1);
                }
                if (str.equals("thin")) {
                    return new BorderWidthValue(str, 0);
                }
            }
            return super.parseCssValue(str);
        }

        @Override // javax.swing.text.html.CSS.LengthValue, javax.swing.text.html.CSS.CssValue
        Object parseHtmlValue(String str) {
            if (str == HTML.NULL_ATTRIBUTE_VALUE) {
                return parseCssValue("medium");
            }
            return parseCssValue(str);
        }
    }

    /* loaded from: rt.jar:javax/swing/text/html/CSS$CssValueMapper.class */
    static class CssValueMapper extends CssValue {
        CssValueMapper() {
        }

        @Override // javax.swing.text.html.CSS.CssValue
        Object parseCssValue(String str) {
            Object obj = CSS.cssValueToInternalValueMap.get(str);
            if (obj == null) {
                obj = CSS.cssValueToInternalValueMap.get(str.toLowerCase());
            }
            return obj;
        }

        @Override // javax.swing.text.html.CSS.CssValue
        Object parseHtmlValue(String str) {
            Object obj = CSS.htmlValueToCssValueMap.get(str);
            if (obj == null) {
                obj = CSS.htmlValueToCssValueMap.get(str.toLowerCase());
            }
            return obj;
        }
    }

    /* loaded from: rt.jar:javax/swing/text/html/CSS$BackgroundPosition.class */
    static class BackgroundPosition extends CssValue {
        float horizontalPosition;
        float verticalPosition;
        short relative;

        BackgroundPosition() {
        }

        @Override // javax.swing.text.html.CSS.CssValue
        Object parseCssValue(String str) {
            String[] strings = CSS.parseStrings(str);
            int length = strings.length;
            BackgroundPosition backgroundPosition = new BackgroundPosition();
            backgroundPosition.relative = (short) 5;
            backgroundPosition.svalue = str;
            if (length > 0) {
                short s2 = 0;
                int i2 = 0;
                while (i2 < length) {
                    int i3 = i2;
                    i2++;
                    String str2 = strings[i3];
                    if (str2.equals("center")) {
                        s2 = (short) (s2 | 4);
                    } else {
                        if ((s2 & 1) == 0) {
                            if (str2.equals(JSplitPane.TOP)) {
                                s2 = (short) (s2 | 1);
                            } else if (str2.equals(JSplitPane.BOTTOM)) {
                                s2 = (short) (s2 | 1);
                                backgroundPosition.verticalPosition = 1.0f;
                            }
                        }
                        if ((s2 & 2) == 0) {
                            if (str2.equals(JSplitPane.LEFT)) {
                                s2 = (short) (s2 | 2);
                                backgroundPosition.horizontalPosition = 0.0f;
                            } else if (str2.equals(JSplitPane.RIGHT)) {
                                s2 = (short) (s2 | 2);
                                backgroundPosition.horizontalPosition = 1.0f;
                            }
                        }
                    }
                }
                if (s2 != 0) {
                    if ((s2 & 1) == 1) {
                        if ((s2 & 2) == 0) {
                            backgroundPosition.horizontalPosition = 0.5f;
                        }
                    } else if ((s2 & 2) == 2) {
                        backgroundPosition.verticalPosition = 0.5f;
                    } else {
                        backgroundPosition.verticalPosition = 0.5f;
                        backgroundPosition.horizontalPosition = 0.5f;
                    }
                } else {
                    LengthUnit lengthUnit = new LengthUnit(strings[0], (short) 0, 0.0f);
                    if (lengthUnit.type == 0) {
                        backgroundPosition.horizontalPosition = lengthUnit.value;
                        backgroundPosition.relative = (short) (1 ^ backgroundPosition.relative);
                    } else if (lengthUnit.type == 1) {
                        backgroundPosition.horizontalPosition = lengthUnit.value;
                    } else if (lengthUnit.type == 3) {
                        backgroundPosition.horizontalPosition = lengthUnit.value;
                        backgroundPosition.relative = (short) ((1 ^ backgroundPosition.relative) | 2);
                    }
                    if (length > 1) {
                        LengthUnit lengthUnit2 = new LengthUnit(strings[1], (short) 0, 0.0f);
                        if (lengthUnit2.type == 0) {
                            backgroundPosition.verticalPosition = lengthUnit2.value;
                            backgroundPosition.relative = (short) (4 ^ backgroundPosition.relative);
                        } else if (lengthUnit2.type == 1) {
                            backgroundPosition.verticalPosition = lengthUnit2.value;
                        } else if (lengthUnit2.type == 3) {
                            backgroundPosition.verticalPosition = lengthUnit2.value;
                            backgroundPosition.relative = (short) ((4 ^ backgroundPosition.relative) | 8);
                        }
                    } else {
                        backgroundPosition.verticalPosition = 0.5f;
                    }
                }
            }
            return backgroundPosition;
        }

        boolean isHorizontalPositionRelativeToSize() {
            return (this.relative & 1) == 1;
        }

        boolean isHorizontalPositionRelativeToFontSize() {
            return (this.relative & 2) == 2;
        }

        float getHorizontalPosition() {
            return this.horizontalPosition;
        }

        boolean isVerticalPositionRelativeToSize() {
            return (this.relative & 4) == 4;
        }

        boolean isVerticalPositionRelativeToFontSize() {
            return (this.relative & 8) == 8;
        }

        float getVerticalPosition() {
            return this.verticalPosition;
        }
    }

    /* loaded from: rt.jar:javax/swing/text/html/CSS$BackgroundImage.class */
    static class BackgroundImage extends CssValue {
        private boolean loadedImage;
        private ImageIcon image;

        BackgroundImage() {
        }

        @Override // javax.swing.text.html.CSS.CssValue
        Object parseCssValue(String str) {
            BackgroundImage backgroundImage = new BackgroundImage();
            backgroundImage.svalue = str;
            return backgroundImage;
        }

        @Override // javax.swing.text.html.CSS.CssValue
        Object parseHtmlValue(String str) {
            return parseCssValue(str);
        }

        ImageIcon getImage(URL url) {
            if (!this.loadedImage) {
                synchronized (this) {
                    if (!this.loadedImage) {
                        URL url2 = CSS.getURL(url, this.svalue);
                        this.loadedImage = true;
                        if (url2 != null) {
                            this.image = new ImageIcon();
                            Image imageCreateImage = Toolkit.getDefaultToolkit().createImage(url2);
                            if (imageCreateImage != null) {
                                this.image.setImage(imageCreateImage);
                            }
                        }
                    }
                }
            }
            return this.image;
        }
    }

    /* loaded from: rt.jar:javax/swing/text/html/CSS$LengthUnit.class */
    static class LengthUnit implements Serializable {
        static Hashtable<String, Float> lengthMapping = new Hashtable<>(6);
        static Hashtable<String, Float> w3cLengthMapping = new Hashtable<>(6);
        short type;
        float value;
        String units = null;
        static final short UNINITALIZED_LENGTH = 10;

        static {
            lengthMapping.put("pt", new Float(1.0f));
            lengthMapping.put("px", new Float(1.3f));
            lengthMapping.put("mm", new Float(2.83464f));
            lengthMapping.put(PdfOps.cm_TOKEN, new Float(28.3464f));
            lengthMapping.put("pc", new Float(12.0f));
            lengthMapping.put("in", new Float(72.0f));
            int screenResolution = 72;
            try {
                screenResolution = Toolkit.getDefaultToolkit().getScreenResolution();
            } catch (HeadlessException e2) {
            }
            w3cLengthMapping.put("pt", new Float(screenResolution / 72.0f));
            w3cLengthMapping.put("px", new Float(1.0f));
            w3cLengthMapping.put("mm", new Float(screenResolution / 25.4f));
            w3cLengthMapping.put(PdfOps.cm_TOKEN, new Float(screenResolution / 2.54f));
            w3cLengthMapping.put("pc", new Float(screenResolution / 6.0f));
            w3cLengthMapping.put("in", new Float(screenResolution));
        }

        LengthUnit(String str, short s2, float f2) {
            parse(str, s2, f2);
        }

        void parse(String str, short s2, float f2) {
            this.type = s2;
            this.value = f2;
            int length = str.length();
            if (length > 0 && str.charAt(length - 1) == '%') {
                try {
                    this.value = Float.valueOf(str.substring(0, length - 1)).floatValue() / 100.0f;
                    this.type = (short) 1;
                } catch (NumberFormatException e2) {
                }
            }
            if (length >= 2) {
                this.units = str.substring(length - 2, length);
                if (lengthMapping.get(this.units) != null) {
                    try {
                        this.value = Float.valueOf(str.substring(0, length - 2)).floatValue();
                        this.type = (short) 0;
                        return;
                    } catch (NumberFormatException e3) {
                        return;
                    }
                }
                if (this.units.equals("em") || this.units.equals("ex")) {
                    try {
                        this.value = Float.valueOf(str.substring(0, length - 2)).floatValue();
                        this.type = (short) 3;
                        return;
                    } catch (NumberFormatException e4) {
                        return;
                    }
                } else if (str.equals("larger")) {
                    this.value = 2.0f;
                    this.type = (short) 2;
                    return;
                } else if (str.equals("smaller")) {
                    this.value = -2.0f;
                    this.type = (short) 2;
                    return;
                } else {
                    try {
                        this.value = Float.valueOf(str).floatValue();
                        this.type = (short) 0;
                        return;
                    } catch (NumberFormatException e5) {
                        return;
                    }
                }
            }
            if (length > 0) {
                try {
                    this.value = Float.valueOf(str).floatValue();
                    this.type = (short) 0;
                } catch (NumberFormatException e6) {
                }
            }
        }

        float getValue(boolean z2) {
            Float f2;
            Hashtable<String, Float> hashtable = z2 ? w3cLengthMapping : lengthMapping;
            float fFloatValue = 1.0f;
            if (this.units != null && (f2 = hashtable.get(this.units)) != null) {
                fFloatValue = f2.floatValue();
            }
            return this.value * fFloatValue;
        }

        static float getValue(float f2, String str, Boolean bool) {
            Float f3;
            Hashtable<String, Float> hashtable = bool.booleanValue() ? w3cLengthMapping : lengthMapping;
            float fFloatValue = 1.0f;
            if (str != null && (f3 = hashtable.get(str)) != null) {
                fFloatValue = f3.floatValue();
            }
            return f2 * fFloatValue;
        }

        public String toString() {
            return ((int) this.type) + " " + this.value;
        }
    }

    /* loaded from: rt.jar:javax/swing/text/html/CSS$ShorthandFontParser.class */
    static class ShorthandFontParser {
        ShorthandFontParser() {
        }

        static void parseShorthandFont(CSS css, String str, MutableAttributeSet mutableAttributeSet) {
            String[] strings = CSS.parseStrings(str);
            int length = strings.length;
            int i2 = 0;
            short s2 = 0;
            int iMin = Math.min(3, length);
            while (i2 < iMin) {
                if ((s2 & 1) == 0 && isFontStyle(strings[i2])) {
                    int i3 = i2;
                    i2++;
                    css.addInternalCSSValue(mutableAttributeSet, Attribute.FONT_STYLE, strings[i3]);
                    s2 = (short) (s2 | 1);
                } else if ((s2 & 2) == 0 && isFontVariant(strings[i2])) {
                    int i4 = i2;
                    i2++;
                    css.addInternalCSSValue(mutableAttributeSet, Attribute.FONT_VARIANT, strings[i4]);
                    s2 = (short) (s2 | 2);
                } else if ((s2 & 4) == 0 && isFontWeight(strings[i2])) {
                    int i5 = i2;
                    i2++;
                    css.addInternalCSSValue(mutableAttributeSet, Attribute.FONT_WEIGHT, strings[i5]);
                    s2 = (short) (s2 | 4);
                } else if (!strings[i2].equals("normal")) {
                    break;
                } else {
                    i2++;
                }
            }
            if ((s2 & 1) == 0) {
                css.addInternalCSSValue(mutableAttributeSet, Attribute.FONT_STYLE, "normal");
            }
            if ((s2 & 2) == 0) {
                css.addInternalCSSValue(mutableAttributeSet, Attribute.FONT_VARIANT, "normal");
            }
            if ((s2 & 4) == 0) {
                css.addInternalCSSValue(mutableAttributeSet, Attribute.FONT_WEIGHT, "normal");
            }
            if (i2 < length) {
                String strSubstring = strings[i2];
                int iIndexOf = strSubstring.indexOf(47);
                if (iIndexOf != -1) {
                    strSubstring = strSubstring.substring(0, iIndexOf);
                    strings[i2] = strings[i2].substring(iIndexOf);
                } else {
                    i2++;
                }
                css.addInternalCSSValue(mutableAttributeSet, Attribute.FONT_SIZE, strSubstring);
            } else {
                css.addInternalCSSValue(mutableAttributeSet, Attribute.FONT_SIZE, "medium");
            }
            if (i2 < length && strings[i2].startsWith("/")) {
                String strSubstring2 = null;
                if (strings[i2].equals("/")) {
                    i2++;
                    if (i2 < length) {
                        i2++;
                        strSubstring2 = strings[i2];
                    }
                } else {
                    int i6 = i2;
                    i2++;
                    strSubstring2 = strings[i6].substring(1);
                }
                if (strSubstring2 != null) {
                    css.addInternalCSSValue(mutableAttributeSet, Attribute.LINE_HEIGHT, strSubstring2);
                } else {
                    css.addInternalCSSValue(mutableAttributeSet, Attribute.LINE_HEIGHT, "normal");
                }
            } else {
                css.addInternalCSSValue(mutableAttributeSet, Attribute.LINE_HEIGHT, "normal");
            }
            if (i2 < length) {
                int i7 = i2;
                int i8 = i2 + 1;
                String str2 = strings[i7];
                while (true) {
                    String str3 = str2;
                    if (i8 < length) {
                        int i9 = i8;
                        i8++;
                        str2 = str3 + " " + strings[i9];
                    } else {
                        css.addInternalCSSValue(mutableAttributeSet, Attribute.FONT_FAMILY, str3);
                        return;
                    }
                }
            } else {
                css.addInternalCSSValue(mutableAttributeSet, Attribute.FONT_FAMILY, "SansSerif");
            }
        }

        private static boolean isFontStyle(String str) {
            return str.equals("italic") || str.equals("oblique");
        }

        private static boolean isFontVariant(String str) {
            return str.equals("small-caps");
        }

        private static boolean isFontWeight(String str) {
            if (str.equals("bold") || str.equals("bolder") || str.equals("italic") || str.equals("lighter")) {
                return true;
            }
            return str.length() == 3 && str.charAt(0) >= '1' && str.charAt(0) <= '9' && str.charAt(1) == '0' && str.charAt(2) == '0';
        }
    }

    /* loaded from: rt.jar:javax/swing/text/html/CSS$ShorthandBackgroundParser.class */
    static class ShorthandBackgroundParser {
        ShorthandBackgroundParser() {
        }

        static void parseShorthandBackground(CSS css, String str, MutableAttributeSet mutableAttributeSet) {
            String[] strings = CSS.parseStrings(str);
            int length = strings.length;
            int i2 = 0;
            short s2 = 0;
            while (i2 < length) {
                int i3 = i2;
                i2++;
                String str2 = strings[i3];
                if ((s2 & 1) == 0 && isImage(str2)) {
                    css.addInternalCSSValue(mutableAttributeSet, Attribute.BACKGROUND_IMAGE, str2);
                    s2 = (short) (s2 | 1);
                } else if ((s2 & 2) == 0 && isRepeat(str2)) {
                    css.addInternalCSSValue(mutableAttributeSet, Attribute.BACKGROUND_REPEAT, str2);
                    s2 = (short) (s2 | 2);
                } else if ((s2 & 4) == 0 && isAttachment(str2)) {
                    css.addInternalCSSValue(mutableAttributeSet, Attribute.BACKGROUND_ATTACHMENT, str2);
                    s2 = (short) (s2 | 4);
                } else if ((s2 & 8) == 0 && isPosition(str2)) {
                    if (i2 < length && isPosition(strings[i2])) {
                        i2++;
                        css.addInternalCSSValue(mutableAttributeSet, Attribute.BACKGROUND_POSITION, str2 + " " + strings[i2]);
                    } else {
                        css.addInternalCSSValue(mutableAttributeSet, Attribute.BACKGROUND_POSITION, str2);
                    }
                    s2 = (short) (s2 | 8);
                } else if ((s2 & 16) == 0 && isColor(str2)) {
                    css.addInternalCSSValue(mutableAttributeSet, Attribute.BACKGROUND_COLOR, str2);
                    s2 = (short) (s2 | 16);
                }
            }
            if ((s2 & 1) == 0) {
                css.addInternalCSSValue(mutableAttributeSet, Attribute.BACKGROUND_IMAGE, null);
            }
            if ((s2 & 2) == 0) {
                css.addInternalCSSValue(mutableAttributeSet, Attribute.BACKGROUND_REPEAT, "repeat");
            }
            if ((s2 & 4) == 0) {
                css.addInternalCSSValue(mutableAttributeSet, Attribute.BACKGROUND_ATTACHMENT, "scroll");
            }
            if ((s2 & 8) == 0) {
                css.addInternalCSSValue(mutableAttributeSet, Attribute.BACKGROUND_POSITION, null);
            }
        }

        static boolean isImage(String str) {
            return str.startsWith("url(") && str.endsWith(")");
        }

        static boolean isRepeat(String str) {
            return str.equals("repeat-x") || str.equals("repeat-y") || str.equals("repeat") || str.equals("no-repeat");
        }

        static boolean isAttachment(String str) {
            return str.equals("fixed") || str.equals("scroll");
        }

        static boolean isPosition(String str) {
            return str.equals(JSplitPane.TOP) || str.equals(JSplitPane.BOTTOM) || str.equals(JSplitPane.LEFT) || str.equals(JSplitPane.RIGHT) || str.equals("center") || (str.length() > 0 && Character.isDigit(str.charAt(0)));
        }

        static boolean isColor(String str) {
            return CSS.stringToColor(str) != null;
        }
    }

    /* loaded from: rt.jar:javax/swing/text/html/CSS$ShorthandMarginParser.class */
    static class ShorthandMarginParser {
        ShorthandMarginParser() {
        }

        static void parseShorthandMargin(CSS css, String str, MutableAttributeSet mutableAttributeSet, Attribute[] attributeArr) {
            String[] strings = CSS.parseStrings(str);
            switch (strings.length) {
                case 0:
                    break;
                case 1:
                    for (int i2 = 0; i2 < 4; i2++) {
                        css.addInternalCSSValue(mutableAttributeSet, attributeArr[i2], strings[0]);
                    }
                    break;
                case 2:
                    css.addInternalCSSValue(mutableAttributeSet, attributeArr[0], strings[0]);
                    css.addInternalCSSValue(mutableAttributeSet, attributeArr[2], strings[0]);
                    css.addInternalCSSValue(mutableAttributeSet, attributeArr[1], strings[1]);
                    css.addInternalCSSValue(mutableAttributeSet, attributeArr[3], strings[1]);
                    break;
                case 3:
                    css.addInternalCSSValue(mutableAttributeSet, attributeArr[0], strings[0]);
                    css.addInternalCSSValue(mutableAttributeSet, attributeArr[1], strings[1]);
                    css.addInternalCSSValue(mutableAttributeSet, attributeArr[2], strings[2]);
                    css.addInternalCSSValue(mutableAttributeSet, attributeArr[3], strings[1]);
                    break;
                default:
                    for (int i3 = 0; i3 < 4; i3++) {
                        css.addInternalCSSValue(mutableAttributeSet, attributeArr[i3], strings[i3]);
                    }
                    break;
            }
        }
    }

    /* loaded from: rt.jar:javax/swing/text/html/CSS$ShorthandBorderParser.class */
    static class ShorthandBorderParser {
        static Attribute[] keys = {Attribute.BORDER_TOP, Attribute.BORDER_RIGHT, Attribute.BORDER_BOTTOM, Attribute.BORDER_LEFT};

        ShorthandBorderParser() {
        }

        static void parseShorthandBorder(MutableAttributeSet mutableAttributeSet, Attribute attribute, String str) {
            Object[] objArr = new Object[CSSBorder.PARSERS.length];
            for (String str2 : CSS.parseStrings(str)) {
                boolean z2 = false;
                int i2 = 0;
                while (true) {
                    if (i2 >= objArr.length) {
                        break;
                    }
                    Object cssValue = CSSBorder.PARSERS[i2].parseCssValue(str2);
                    if (cssValue == null) {
                        i2++;
                    } else if (objArr[i2] == null) {
                        objArr[i2] = cssValue;
                        z2 = true;
                    }
                }
                if (!z2) {
                    return;
                }
            }
            for (int i3 = 0; i3 < objArr.length; i3++) {
                if (objArr[i3] == null) {
                    objArr[i3] = CSSBorder.DEFAULTS[i3];
                }
            }
            for (int i4 = 0; i4 < keys.length; i4++) {
                if (attribute == Attribute.BORDER || attribute == keys[i4]) {
                    for (int i5 = 0; i5 < objArr.length; i5++) {
                        mutableAttributeSet.addAttribute(CSSBorder.ATTRIBUTES[i5][i4], objArr[i5]);
                    }
                }
            }
        }
    }

    static SizeRequirements calculateTiledRequirements(LayoutIterator layoutIterator, SizeRequirements sizeRequirements) {
        long minimumSpan = 0;
        long maximumSpan = 0;
        long preferredSpan = 0;
        int trailingCollapseSpan = 0;
        int iMax = 0;
        int count = layoutIterator.getCount();
        for (int i2 = 0; i2 < count; i2++) {
            layoutIterator.setIndex(i2);
            iMax += Math.max(trailingCollapseSpan, (int) layoutIterator.getLeadingCollapseSpan());
            preferredSpan += (int) layoutIterator.getPreferredSpan(0.0f);
            minimumSpan = (long) (minimumSpan + layoutIterator.getMinimumSpan(0.0f));
            maximumSpan = (long) (maximumSpan + layoutIterator.getMaximumSpan(0.0f));
            trailingCollapseSpan = (int) layoutIterator.getTrailingCollapseSpan();
        }
        int borderWidth = (int) (iMax + trailingCollapseSpan + (2.0f * layoutIterator.getBorderWidth()));
        long j2 = minimumSpan + borderWidth;
        long j3 = preferredSpan + borderWidth;
        long j4 = maximumSpan + borderWidth;
        if (sizeRequirements == null) {
            sizeRequirements = new SizeRequirements();
        }
        sizeRequirements.minimum = j2 > 2147483647L ? Integer.MAX_VALUE : (int) j2;
        sizeRequirements.preferred = j3 > 2147483647L ? Integer.MAX_VALUE : (int) j3;
        sizeRequirements.maximum = j4 > 2147483647L ? Integer.MAX_VALUE : (int) j4;
        return sizeRequirements;
    }

    static void calculateTiledLayout(LayoutIterator layoutIterator, int i2) {
        int iCeil;
        int span;
        double dCeil;
        long j2 = 0;
        int trailingCollapseSpan = 0;
        int offset = 0;
        int count = layoutIterator.getCount();
        long[] jArr = new long[3];
        long[] jArr2 = new long[3];
        for (int i3 = 0; i3 < 3; i3++) {
            jArr2[i3] = 0;
            jArr[i3] = 0;
        }
        for (int i4 = 0; i4 < count; i4++) {
            layoutIterator.setIndex(i4);
            layoutIterator.setOffset(Math.max(trailingCollapseSpan, (int) layoutIterator.getLeadingCollapseSpan()));
            offset += layoutIterator.getOffset();
            long preferredSpan = (long) layoutIterator.getPreferredSpan(i2);
            layoutIterator.setSpan((int) preferredSpan);
            j2 += preferredSpan;
            int adjustmentWeight = layoutIterator.getAdjustmentWeight();
            jArr[adjustmentWeight] = jArr[adjustmentWeight] + (((long) layoutIterator.getMaximumSpan(i2)) - preferredSpan);
            int adjustmentWeight2 = layoutIterator.getAdjustmentWeight();
            jArr2[adjustmentWeight2] = jArr2[adjustmentWeight2] + (preferredSpan - ((long) layoutIterator.getMinimumSpan(i2)));
            trailingCollapseSpan = (int) layoutIterator.getTrailingCollapseSpan();
        }
        int borderWidth = (int) (offset + trailingCollapseSpan + (2.0f * layoutIterator.getBorderWidth()));
        for (int i5 = 1; i5 < 3; i5++) {
            int i6 = i5;
            jArr[i6] = jArr[i6] + jArr[i5 - 1];
            int i7 = i5;
            jArr2[i7] = jArr2[i7] + jArr2[i5 - 1];
        }
        int i8 = i2 - borderWidth;
        long j3 = i8 - j2;
        long[] jArr3 = j3 > 0 ? jArr : jArr2;
        long jAbs = Math.abs(j3);
        int i9 = 0;
        while (i9 <= 2 && jArr3[i9] < jAbs) {
            i9++;
        }
        float f2 = 0.0f;
        if (i9 <= 2) {
            long j4 = jAbs - (i9 > 0 ? jArr3[i9 - 1] : 0L);
            if (j4 != 0) {
                f2 = j4 / (jArr3[i9] - (i9 > 0 ? jArr3[i9 - 1] : 0L));
            }
        }
        int borderWidth2 = (int) layoutIterator.getBorderWidth();
        for (int i10 = 0; i10 < count; i10++) {
            layoutIterator.setIndex(i10);
            layoutIterator.setOffset(layoutIterator.getOffset() + borderWidth2);
            if (layoutIterator.getAdjustmentWeight() < i9) {
                if (i8 > j2) {
                    dCeil = Math.floor(layoutIterator.getMaximumSpan(i2));
                } else {
                    dCeil = Math.ceil(layoutIterator.getMinimumSpan(i2));
                }
                layoutIterator.setSpan((int) dCeil);
            } else if (layoutIterator.getAdjustmentWeight() == i9) {
                if (i8 > j2) {
                    span = ((int) layoutIterator.getMaximumSpan(i2)) - layoutIterator.getSpan();
                } else {
                    span = layoutIterator.getSpan() - ((int) layoutIterator.getMinimumSpan(i2));
                }
                int iFloor = (int) Math.floor(f2 * span);
                layoutIterator.setSpan(layoutIterator.getSpan() + (((long) i8) > j2 ? iFloor : -iFloor));
            }
            borderWidth2 = (int) Math.min(layoutIterator.getOffset() + layoutIterator.getSpan(), 2147483647L);
        }
        int trailingCollapseSpan2 = ((i2 - borderWidth2) - ((int) layoutIterator.getTrailingCollapseSpan())) - ((int) layoutIterator.getBorderWidth());
        int i11 = trailingCollapseSpan2 > 0 ? 1 : -1;
        int i12 = trailingCollapseSpan2 * i11;
        boolean z2 = true;
        while (i12 > 0 && z2) {
            z2 = false;
            int i13 = 0;
            for (int i14 = 0; i14 < count; i14++) {
                layoutIterator.setIndex(i14);
                layoutIterator.setOffset(layoutIterator.getOffset() + i13);
                int span2 = layoutIterator.getSpan();
                if (i12 > 0) {
                    if (i11 > 0) {
                        iCeil = ((int) Math.floor(layoutIterator.getMaximumSpan(i2))) - span2;
                    } else {
                        iCeil = span2 - ((int) Math.ceil(layoutIterator.getMinimumSpan(i2)));
                    }
                    if (iCeil >= 1) {
                        z2 = true;
                        layoutIterator.setSpan(span2 + i11);
                        i13 += i11;
                        i12--;
                    }
                }
            }
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:11:0x004d A[PHI: r6
  0x004d: PHI (r6v1 java.lang.Object) = (r6v0 java.lang.Object), (r6v4 java.lang.Object) binds: [B:7:0x0039, B:9:0x0042] A[DONT_GENERATE, DONT_INLINE]] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private void writeObject(java.io.ObjectOutputStream r4) throws java.io.IOException {
        /*
            r3 = this;
            r0 = r4
            r0.defaultWriteObject()
            r0 = r3
            java.util.Hashtable<java.lang.Object, java.lang.Object> r0 = r0.valueConvertor
            java.util.Enumeration r0 = r0.keys()
            r5 = r0
            r0 = r4
            r1 = r3
            java.util.Hashtable<java.lang.Object, java.lang.Object> r1 = r1.valueConvertor
            int r1 = r1.size()
            r0.writeInt(r1)
            r0 = r5
            if (r0 == 0) goto L73
        L1b:
            r0 = r5
            boolean r0 = r0.hasMoreElements()
            if (r0 == 0) goto L73
            r0 = r5
            java.lang.Object r0 = r0.nextElement2()
            r6 = r0
            r0 = r3
            java.util.Hashtable<java.lang.Object, java.lang.Object> r0 = r0.valueConvertor
            r1 = r6
            java.lang.Object r0 = r0.get(r1)
            r7 = r0
            r0 = r6
            boolean r0 = r0 instanceof java.io.Serializable
            if (r0 != 0) goto L4d
            r0 = r6
            java.lang.Object r0 = javax.swing.text.StyleContext.getStaticAttributeKey(r0)
            r1 = r0
            r6 = r1
            if (r0 != 0) goto L4d
            r0 = 0
            r6 = r0
            r0 = 0
            r7 = r0
            goto L65
        L4d:
            r0 = r7
            boolean r0 = r0 instanceof java.io.Serializable
            if (r0 != 0) goto L65
            r0 = r7
            java.lang.Object r0 = javax.swing.text.StyleContext.getStaticAttributeKey(r0)
            r1 = r0
            r7 = r1
            if (r0 != 0) goto L65
            r0 = 0
            r6 = r0
            r0 = 0
            r7 = r0
        L65:
            r0 = r4
            r1 = r6
            r0.writeObject(r1)
            r0 = r4
            r1 = r7
            r0.writeObject(r1)
            goto L1b
        L73:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: javax.swing.text.html.CSS.writeObject(java.io.ObjectOutputStream):void");
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        int i2 = objectInputStream.readInt();
        this.valueConvertor = new Hashtable<>();
        while (true) {
            int i3 = i2;
            i2--;
            if (i3 > 0) {
                Object object = objectInputStream.readObject();
                Object object2 = objectInputStream.readObject();
                Object staticAttribute = StyleContext.getStaticAttribute(object);
                if (staticAttribute != null) {
                    object = staticAttribute;
                }
                Object staticAttribute2 = StyleContext.getStaticAttribute(object2);
                if (staticAttribute2 != null) {
                    object2 = staticAttribute2;
                }
                if (object != null && object2 != null) {
                    this.valueConvertor.put(object, object2);
                }
            } else {
                return;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public StyleSheet getStyleSheet(StyleSheet styleSheet) {
        if (styleSheet != null) {
            this.styleSheet = styleSheet;
        }
        return this.styleSheet;
    }
}
