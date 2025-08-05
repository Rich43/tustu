package javax.swing.text.html;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.Shape;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Serializable;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.EmptyStackException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Stack;
import java.util.StringTokenizer;
import java.util.Vector;
import javafx.fxml.FXMLLoader;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.UIManager;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.UIResource;
import javax.swing.text.AttributeSet;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;
import javax.swing.text.View;
import javax.swing.text.html.CSS;
import javax.swing.text.html.CSSParser;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import org.icepdf.core.pobjects.graphics.Separation;
import sun.swing.SwingUtilities2;

/* loaded from: rt.jar:javax/swing/text/html/StyleSheet.class */
public class StyleSheet extends StyleContext {
    static final int DEFAULT_FONT_SIZE = 3;
    private CSS css;
    private Vector<StyleSheet> linkedStyleSheets;
    private URL base;
    static final Border noBorder = new EmptyBorder(0, 0, 0, 0);
    static final int[] sizeMapDefault = {8, 10, 12, 14, 18, 24, 36};
    private int[] sizeMap = sizeMapDefault;
    private boolean w3cLengthUnits = false;
    private SelectorMapping selectorMapping = new SelectorMapping(0);
    private Hashtable<String, ResolvedStyle> resolvedStyles = new Hashtable<>();

    public StyleSheet() {
        if (this.css == null) {
            this.css = new CSS();
        }
    }

    public Style getRule(HTML.Tag tag, Element element) {
        SearchBuffer searchBufferObtainSearchBuffer = SearchBuffer.obtainSearchBuffer();
        try {
            Vector vector = searchBufferObtainSearchBuffer.getVector();
            for (Element parentElement = element; parentElement != null; parentElement = parentElement.getParentElement()) {
                vector.addElement(parentElement);
            }
            int size = vector.size();
            StringBuffer stringBuffer = searchBufferObtainSearchBuffer.getStringBuffer();
            for (int i2 = size - 1; i2 >= 1; i2--) {
                AttributeSet attributes = ((Element) vector.elementAt(i2)).getAttributes();
                stringBuffer.append(attributes.getAttribute(StyleConstants.NameAttribute).toString());
                if (attributes != null) {
                    if (attributes.isDefined(HTML.Attribute.ID)) {
                        stringBuffer.append('#');
                        stringBuffer.append(attributes.getAttribute(HTML.Attribute.ID));
                    } else if (attributes.isDefined(HTML.Attribute.CLASS)) {
                        stringBuffer.append('.');
                        stringBuffer.append(attributes.getAttribute(HTML.Attribute.CLASS));
                    }
                }
                stringBuffer.append(' ');
            }
            stringBuffer.append(tag.toString());
            Element element2 = (Element) vector.elementAt(0);
            AttributeSet attributes2 = element2.getAttributes();
            if (element2.isLeaf()) {
                Object attribute = attributes2.getAttribute(tag);
                if (attribute instanceof AttributeSet) {
                    attributes2 = (AttributeSet) attribute;
                } else {
                    attributes2 = null;
                }
            }
            if (attributes2 != null) {
                if (attributes2.isDefined(HTML.Attribute.ID)) {
                    stringBuffer.append('#');
                    stringBuffer.append(attributes2.getAttribute(HTML.Attribute.ID));
                } else if (attributes2.isDefined(HTML.Attribute.CLASS)) {
                    stringBuffer.append('.');
                    stringBuffer.append(attributes2.getAttribute(HTML.Attribute.CLASS));
                }
            }
            Style resolvedStyle = getResolvedStyle(stringBuffer.toString(), vector, tag);
            SearchBuffer.releaseSearchBuffer(searchBufferObtainSearchBuffer);
            return resolvedStyle;
        } catch (Throwable th) {
            SearchBuffer.releaseSearchBuffer(searchBufferObtainSearchBuffer);
            throw th;
        }
    }

    public Style getRule(String str) {
        String strCleanSelectorString = cleanSelectorString(str);
        if (strCleanSelectorString != null) {
            return getResolvedStyle(strCleanSelectorString);
        }
        return null;
    }

    public void addRule(String str) {
        if (str != null) {
            if (str == "BASE_SIZE_DISABLE") {
                this.sizeMap = sizeMapDefault;
                return;
            }
            if (str.startsWith("BASE_SIZE ")) {
                rebaseSizeMap(Integer.parseInt(str.substring("BASE_SIZE ".length())));
                return;
            }
            if (str == "W3C_LENGTH_UNITS_ENABLE") {
                this.w3cLengthUnits = true;
            } else {
                if (str == "W3C_LENGTH_UNITS_DISABLE") {
                    this.w3cLengthUnits = false;
                    return;
                }
                try {
                    new CssParser().parse(getBase(), new StringReader(str), false, false);
                } catch (IOException e2) {
                }
            }
        }
    }

    public AttributeSet getDeclaration(String str) {
        if (str == null) {
            return SimpleAttributeSet.EMPTY;
        }
        return new CssParser().parseDeclaration(str);
    }

    public void loadRules(Reader reader, URL url) throws IOException {
        new CssParser().parse(url, reader, false, false);
    }

    public AttributeSet getViewAttributes(View view) {
        return new ViewAttributeSet(view);
    }

    @Override // javax.swing.text.StyleContext
    public void removeStyle(String str) {
        if (getStyle(str) != null) {
            String[] simpleSelectors = getSimpleSelectors(cleanSelectorString(str));
            synchronized (this) {
                SelectorMapping rootSelectorMapping = getRootSelectorMapping();
                for (int length = simpleSelectors.length - 1; length >= 0; length--) {
                    rootSelectorMapping = rootSelectorMapping.getChildSelectorMapping(simpleSelectors[length], true);
                }
                Style style = rootSelectorMapping.getStyle();
                if (style != null) {
                    rootSelectorMapping.setStyle(null);
                    if (this.resolvedStyles.size() > 0) {
                        Enumeration<ResolvedStyle> enumerationElements = this.resolvedStyles.elements();
                        while (enumerationElements.hasMoreElements()) {
                            enumerationElements.nextElement2().removeStyle(style);
                        }
                    }
                }
            }
        }
        super.removeStyle(str);
    }

    public void addStyleSheet(StyleSheet styleSheet) {
        synchronized (this) {
            if (this.linkedStyleSheets == null) {
                this.linkedStyleSheets = new Vector<>();
            }
            if (!this.linkedStyleSheets.contains(styleSheet)) {
                int size = 0;
                if ((styleSheet instanceof UIResource) && this.linkedStyleSheets.size() > 1) {
                    size = this.linkedStyleSheets.size() - 1;
                }
                this.linkedStyleSheets.insertElementAt(styleSheet, size);
                linkStyleSheetAt(styleSheet, size);
            }
        }
    }

    public void removeStyleSheet(StyleSheet styleSheet) {
        int iIndexOf;
        synchronized (this) {
            if (this.linkedStyleSheets != null && (iIndexOf = this.linkedStyleSheets.indexOf(styleSheet)) != -1) {
                this.linkedStyleSheets.removeElementAt(iIndexOf);
                unlinkStyleSheet(styleSheet, iIndexOf);
                if (iIndexOf == 0 && this.linkedStyleSheets.size() == 0) {
                    this.linkedStyleSheets = null;
                }
            }
        }
    }

    public StyleSheet[] getStyleSheets() {
        StyleSheet[] styleSheetArr;
        synchronized (this) {
            if (this.linkedStyleSheets != null) {
                styleSheetArr = new StyleSheet[this.linkedStyleSheets.size()];
                this.linkedStyleSheets.copyInto(styleSheetArr);
            } else {
                styleSheetArr = null;
            }
        }
        return styleSheetArr;
    }

    public void importStyleSheet(URL url) {
        try {
            InputStream inputStreamOpenStream = url.openStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStreamOpenStream));
            new CssParser().parse(url, bufferedReader, false, true);
            bufferedReader.close();
            inputStreamOpenStream.close();
        } catch (Throwable th) {
        }
    }

    public void setBase(URL url) {
        this.base = url;
    }

    public URL getBase() {
        return this.base;
    }

    public void addCSSAttribute(MutableAttributeSet mutableAttributeSet, CSS.Attribute attribute, String str) {
        this.css.addInternalCSSValue(mutableAttributeSet, attribute, str);
    }

    public boolean addCSSAttributeFromHTML(MutableAttributeSet mutableAttributeSet, CSS.Attribute attribute, String str) {
        Object cssValue = this.css.getCssValue(attribute, str);
        if (cssValue != null) {
            mutableAttributeSet.addAttribute(attribute, cssValue);
            return true;
        }
        return false;
    }

    public AttributeSet translateHTMLToCSS(AttributeSet attributeSet) {
        AttributeSet attributeSetTranslateHTMLToCSS = this.css.translateHTMLToCSS(attributeSet);
        Style styleAddStyle = addStyle(null, null);
        styleAddStyle.addAttributes(attributeSetTranslateHTMLToCSS);
        return styleAddStyle;
    }

    @Override // javax.swing.text.StyleContext, javax.swing.text.AbstractDocument.AttributeContext
    public AttributeSet addAttribute(AttributeSet attributeSet, Object obj, Object obj2) {
        CSS.Attribute attributeStyleConstantsKeyToCSSKey;
        if (this.css == null) {
            this.css = new CSS();
        }
        if (obj instanceof StyleConstants) {
            HTML.Tag tagForStyleConstantsKey = HTML.getTagForStyleConstantsKey((StyleConstants) obj);
            if (tagForStyleConstantsKey != null && attributeSet.isDefined(tagForStyleConstantsKey)) {
                attributeSet = removeAttribute(attributeSet, tagForStyleConstantsKey);
            }
            Object objStyleConstantsValueToCSSValue = this.css.styleConstantsValueToCSSValue((StyleConstants) obj, obj2);
            if (objStyleConstantsValueToCSSValue != null && (attributeStyleConstantsKeyToCSSKey = this.css.styleConstantsKeyToCSSKey((StyleConstants) obj)) != null) {
                return super.addAttribute(attributeSet, attributeStyleConstantsKeyToCSSKey, objStyleConstantsValueToCSSValue);
            }
        }
        return super.addAttribute(attributeSet, obj, obj2);
    }

    @Override // javax.swing.text.StyleContext, javax.swing.text.AbstractDocument.AttributeContext
    public AttributeSet addAttributes(AttributeSet attributeSet, AttributeSet attributeSet2) {
        if (!(attributeSet2 instanceof HTMLDocument.TaggedAttributeSet)) {
            attributeSet = removeHTMLTags(attributeSet, attributeSet2);
        }
        return super.addAttributes(attributeSet, convertAttributeSet(attributeSet2));
    }

    @Override // javax.swing.text.StyleContext, javax.swing.text.AbstractDocument.AttributeContext
    public AttributeSet removeAttribute(AttributeSet attributeSet, Object obj) {
        if (obj instanceof StyleConstants) {
            HTML.Tag tagForStyleConstantsKey = HTML.getTagForStyleConstantsKey((StyleConstants) obj);
            if (tagForStyleConstantsKey != null) {
                attributeSet = super.removeAttribute(attributeSet, tagForStyleConstantsKey);
            }
            CSS.Attribute attributeStyleConstantsKeyToCSSKey = this.css.styleConstantsKeyToCSSKey((StyleConstants) obj);
            if (attributeStyleConstantsKeyToCSSKey != null) {
                return super.removeAttribute(attributeSet, attributeStyleConstantsKeyToCSSKey);
            }
        }
        return super.removeAttribute(attributeSet, obj);
    }

    @Override // javax.swing.text.StyleContext, javax.swing.text.AbstractDocument.AttributeContext
    public AttributeSet removeAttributes(AttributeSet attributeSet, Enumeration<?> enumeration) {
        return super.removeAttributes(attributeSet, enumeration);
    }

    @Override // javax.swing.text.StyleContext, javax.swing.text.AbstractDocument.AttributeContext
    public AttributeSet removeAttributes(AttributeSet attributeSet, AttributeSet attributeSet2) {
        if (attributeSet != attributeSet2) {
            attributeSet = removeHTMLTags(attributeSet, attributeSet2);
        }
        return super.removeAttributes(attributeSet, convertAttributeSet(attributeSet2));
    }

    @Override // javax.swing.text.StyleContext
    protected StyleContext.SmallAttributeSet createSmallAttributeSet(AttributeSet attributeSet) {
        return new SmallConversionSet(attributeSet);
    }

    @Override // javax.swing.text.StyleContext
    protected MutableAttributeSet createLargeAttributeSet(AttributeSet attributeSet) {
        return new LargeConversionSet(attributeSet);
    }

    private AttributeSet removeHTMLTags(AttributeSet attributeSet, AttributeSet attributeSet2) {
        HTML.Tag tagForStyleConstantsKey;
        if (!(attributeSet2 instanceof LargeConversionSet) && !(attributeSet2 instanceof SmallConversionSet)) {
            Enumeration<?> attributeNames = attributeSet2.getAttributeNames();
            while (attributeNames.hasMoreElements()) {
                Object objNextElement = attributeNames.nextElement2();
                if ((objNextElement instanceof StyleConstants) && (tagForStyleConstantsKey = HTML.getTagForStyleConstantsKey((StyleConstants) objNextElement)) != null && attributeSet.isDefined(tagForStyleConstantsKey)) {
                    attributeSet = super.removeAttribute(attributeSet, tagForStyleConstantsKey);
                }
            }
        }
        return attributeSet;
    }

    AttributeSet convertAttributeSet(AttributeSet attributeSet) {
        CSS.Attribute attributeStyleConstantsKeyToCSSKey;
        if ((attributeSet instanceof LargeConversionSet) || (attributeSet instanceof SmallConversionSet)) {
            return attributeSet;
        }
        Enumeration<?> attributeNames = attributeSet.getAttributeNames();
        while (attributeNames.hasMoreElements()) {
            if (attributeNames.nextElement2() instanceof StyleConstants) {
                LargeConversionSet largeConversionSet = new LargeConversionSet();
                Enumeration<?> attributeNames2 = attributeSet.getAttributeNames();
                while (attributeNames2.hasMoreElements()) {
                    Object objNextElement = attributeNames2.nextElement2();
                    Object objStyleConstantsValueToCSSValue = null;
                    if ((objNextElement instanceof StyleConstants) && (attributeStyleConstantsKeyToCSSKey = this.css.styleConstantsKeyToCSSKey((StyleConstants) objNextElement)) != null) {
                        objStyleConstantsValueToCSSValue = this.css.styleConstantsValueToCSSValue((StyleConstants) objNextElement, attributeSet.getAttribute(objNextElement));
                        if (objStyleConstantsValueToCSSValue != null) {
                            largeConversionSet.addAttribute(attributeStyleConstantsKeyToCSSKey, objStyleConstantsValueToCSSValue);
                        }
                    }
                    if (objStyleConstantsValueToCSSValue == null) {
                        largeConversionSet.addAttribute(objNextElement, attributeSet.getAttribute(objNextElement));
                    }
                }
                return largeConversionSet;
            }
        }
        return attributeSet;
    }

    /* loaded from: rt.jar:javax/swing/text/html/StyleSheet$LargeConversionSet.class */
    class LargeConversionSet extends SimpleAttributeSet {
        public LargeConversionSet(AttributeSet attributeSet) {
            super(attributeSet);
        }

        public LargeConversionSet() {
        }

        @Override // javax.swing.text.SimpleAttributeSet, javax.swing.text.AttributeSet
        public boolean isDefined(Object obj) {
            CSS.Attribute attributeStyleConstantsKeyToCSSKey;
            if ((obj instanceof StyleConstants) && (attributeStyleConstantsKeyToCSSKey = StyleSheet.this.css.styleConstantsKeyToCSSKey((StyleConstants) obj)) != null) {
                return super.isDefined(attributeStyleConstantsKeyToCSSKey);
            }
            return super.isDefined(obj);
        }

        @Override // javax.swing.text.SimpleAttributeSet, javax.swing.text.AttributeSet
        public Object getAttribute(Object obj) {
            CSS.Attribute attributeStyleConstantsKeyToCSSKey;
            Object attribute;
            if ((obj instanceof StyleConstants) && (attributeStyleConstantsKeyToCSSKey = StyleSheet.this.css.styleConstantsKeyToCSSKey((StyleConstants) obj)) != null && (attribute = super.getAttribute(attributeStyleConstantsKeyToCSSKey)) != null) {
                return StyleSheet.this.css.cssValueToStyleConstantsValue((StyleConstants) obj, attribute);
            }
            return super.getAttribute(obj);
        }
    }

    /* loaded from: rt.jar:javax/swing/text/html/StyleSheet$SmallConversionSet.class */
    class SmallConversionSet extends StyleContext.SmallAttributeSet {
        public SmallConversionSet(AttributeSet attributeSet) {
            super(attributeSet);
        }

        @Override // javax.swing.text.StyleContext.SmallAttributeSet, javax.swing.text.AttributeSet
        public boolean isDefined(Object obj) {
            CSS.Attribute attributeStyleConstantsKeyToCSSKey;
            if ((obj instanceof StyleConstants) && (attributeStyleConstantsKeyToCSSKey = StyleSheet.this.css.styleConstantsKeyToCSSKey((StyleConstants) obj)) != null) {
                return super.isDefined(attributeStyleConstantsKeyToCSSKey);
            }
            return super.isDefined(obj);
        }

        @Override // javax.swing.text.StyleContext.SmallAttributeSet, javax.swing.text.AttributeSet
        public Object getAttribute(Object obj) {
            CSS.Attribute attributeStyleConstantsKeyToCSSKey;
            Object attribute;
            if ((obj instanceof StyleConstants) && (attributeStyleConstantsKeyToCSSKey = StyleSheet.this.css.styleConstantsKeyToCSSKey((StyleConstants) obj)) != null && (attribute = super.getAttribute(attributeStyleConstantsKeyToCSSKey)) != null) {
                return StyleSheet.this.css.cssValueToStyleConstantsValue((StyleConstants) obj, attribute);
            }
            return super.getAttribute(obj);
        }
    }

    @Override // javax.swing.text.StyleContext
    public Font getFont(AttributeSet attributeSet) {
        return this.css.getFont(this, attributeSet, 12, this);
    }

    @Override // javax.swing.text.StyleContext
    public Color getForeground(AttributeSet attributeSet) {
        Color color = this.css.getColor(attributeSet, CSS.Attribute.COLOR);
        if (color == null) {
            return Color.black;
        }
        return color;
    }

    @Override // javax.swing.text.StyleContext
    public Color getBackground(AttributeSet attributeSet) {
        return this.css.getColor(attributeSet, CSS.Attribute.BACKGROUND_COLOR);
    }

    public BoxPainter getBoxPainter(AttributeSet attributeSet) {
        return new BoxPainter(attributeSet, this.css, this);
    }

    public ListPainter getListPainter(AttributeSet attributeSet) {
        return new ListPainter(attributeSet, this);
    }

    public void setBaseFontSize(int i2) {
        this.css.setBaseFontSize(i2);
    }

    public void setBaseFontSize(String str) {
        this.css.setBaseFontSize(str);
    }

    public static int getIndexOfSize(float f2) {
        return CSS.getIndexOfSize(f2, sizeMapDefault);
    }

    public float getPointSize(int i2) {
        return this.css.getPointSize(i2, this);
    }

    public float getPointSize(String str) {
        return this.css.getPointSize(str, this);
    }

    public Color stringToColor(String str) {
        return CSS.stringToColor(str);
    }

    ImageIcon getBackgroundImage(AttributeSet attributeSet) {
        Object attribute = attributeSet.getAttribute(CSS.Attribute.BACKGROUND_IMAGE);
        if (attribute != null) {
            return ((CSS.BackgroundImage) attribute).getImage(getBase());
        }
        return null;
    }

    void addRule(String[] strArr, AttributeSet attributeSet, boolean z2) {
        int length = strArr.length;
        StringBuilder sb = new StringBuilder();
        sb.append(strArr[0]);
        for (int i2 = 1; i2 < length; i2++) {
            sb.append(' ');
            sb.append(strArr[i2]);
        }
        String string = sb.toString();
        Style style = getStyle(string);
        if (style == null) {
            Style styleAddStyle = addStyle(string, null);
            synchronized (this) {
                SelectorMapping rootSelectorMapping = getRootSelectorMapping();
                for (int i3 = length - 1; i3 >= 0; i3--) {
                    rootSelectorMapping = rootSelectorMapping.getChildSelectorMapping(strArr[i3], true);
                }
                style = rootSelectorMapping.getStyle();
                if (style == null) {
                    style = styleAddStyle;
                    rootSelectorMapping.setStyle(style);
                    refreshResolvedRules(string, strArr, style, rootSelectorMapping.getSpecificity());
                }
            }
        }
        if (z2) {
            style = getLinkedStyle(style);
        }
        style.addAttributes(attributeSet);
    }

    private synchronized void linkStyleSheetAt(StyleSheet styleSheet, int i2) {
        if (this.resolvedStyles.size() > 0) {
            Enumeration<ResolvedStyle> enumerationElements = this.resolvedStyles.elements();
            while (enumerationElements.hasMoreElements()) {
                ResolvedStyle resolvedStyleNextElement = enumerationElements.nextElement2();
                resolvedStyleNextElement.insertExtendedStyleAt(styleSheet.getRule(resolvedStyleNextElement.getName()), i2);
            }
        }
    }

    private synchronized void unlinkStyleSheet(StyleSheet styleSheet, int i2) {
        if (this.resolvedStyles.size() > 0) {
            Enumeration<ResolvedStyle> enumerationElements = this.resolvedStyles.elements();
            while (enumerationElements.hasMoreElements()) {
                enumerationElements.nextElement2().removeExtendedStyleAt(i2);
            }
        }
    }

    String[] getSimpleSelectors(String str) {
        String strCleanSelectorString = cleanSelectorString(str);
        SearchBuffer searchBufferObtainSearchBuffer = SearchBuffer.obtainSearchBuffer();
        Vector vector = searchBufferObtainSearchBuffer.getVector();
        int i2 = 0;
        int length = strCleanSelectorString.length();
        while (i2 != -1) {
            int iIndexOf = strCleanSelectorString.indexOf(32, i2);
            if (iIndexOf != -1) {
                vector.addElement(strCleanSelectorString.substring(i2, iIndexOf));
                int i3 = iIndexOf + 1;
                if (i3 == length) {
                    i2 = -1;
                } else {
                    i2 = i3;
                }
            } else {
                vector.addElement(strCleanSelectorString.substring(i2));
                i2 = -1;
            }
        }
        String[] strArr = new String[vector.size()];
        vector.copyInto(strArr);
        SearchBuffer.releaseSearchBuffer(searchBufferObtainSearchBuffer);
        return strArr;
    }

    String cleanSelectorString(String str) {
        boolean z2;
        boolean z3 = true;
        int length = str.length();
        for (int i2 = 0; i2 < length; i2++) {
            switch (str.charAt(i2)) {
                case '\t':
                case '\n':
                case '\r':
                    return _cleanSelectorString(str);
                case ' ':
                    if (z3) {
                        return _cleanSelectorString(str);
                    }
                    z2 = true;
                    break;
                default:
                    z2 = false;
                    break;
            }
            z3 = z2;
        }
        if (z3) {
            return _cleanSelectorString(str);
        }
        return str;
    }

    private String _cleanSelectorString(String str) {
        SearchBuffer searchBufferObtainSearchBuffer = SearchBuffer.obtainSearchBuffer();
        StringBuffer stringBuffer = searchBufferObtainSearchBuffer.getStringBuffer();
        boolean z2 = true;
        int i2 = 0;
        char[] charArray = str.toCharArray();
        int length = charArray.length;
        for (int i3 = 0; i3 < length; i3++) {
            try {
                switch (charArray[i3]) {
                    case '\t':
                    case '\n':
                    case '\r':
                        if (!z2) {
                            z2 = true;
                            if (i2 < i3) {
                                stringBuffer.append(charArray, i2, i3 - i2);
                                stringBuffer.append(' ');
                            }
                        }
                        i2 = i3 + 1;
                        break;
                    case ' ':
                        if (!z2) {
                            z2 = true;
                            if (i2 < i3) {
                                stringBuffer.append(charArray, i2, (1 + i3) - i2);
                            }
                        }
                        i2 = i3 + 1;
                        break;
                    default:
                        z2 = false;
                        break;
                }
            } catch (Throwable th) {
                SearchBuffer.releaseSearchBuffer(searchBufferObtainSearchBuffer);
                throw th;
            }
        }
        if (z2 && stringBuffer.length() > 0) {
            stringBuffer.setLength(stringBuffer.length() - 1);
        } else if (i2 < length) {
            stringBuffer.append(charArray, i2, length - i2);
        }
        String string = stringBuffer.toString();
        SearchBuffer.releaseSearchBuffer(searchBufferObtainSearchBuffer);
        return string;
    }

    private SelectorMapping getRootSelectorMapping() {
        return this.selectorMapping;
    }

    static int getSpecificity(String str) {
        int i2 = 0;
        boolean z2 = true;
        int length = str.length();
        for (int i3 = 0; i3 < length; i3++) {
            switch (str.charAt(i3)) {
                case ' ':
                    z2 = true;
                    break;
                case '#':
                    i2 += 10000;
                    break;
                case '.':
                    i2 += 100;
                    break;
                default:
                    if (z2) {
                        z2 = false;
                        i2++;
                        break;
                    } else {
                        break;
                    }
            }
        }
        return i2;
    }

    private Style getLinkedStyle(Style style) {
        Style styleAddStyle = (Style) style.getResolveParent();
        if (styleAddStyle == null) {
            styleAddStyle = addStyle(null, null);
            style.setResolveParent(styleAddStyle);
        }
        return styleAddStyle;
    }

    private synchronized Style getResolvedStyle(String str, Vector vector, HTML.Tag tag) {
        Style styleCreateResolvedStyle = this.resolvedStyles.get(str);
        if (styleCreateResolvedStyle == null) {
            styleCreateResolvedStyle = createResolvedStyle(str, vector, tag);
        }
        return styleCreateResolvedStyle;
    }

    private synchronized Style getResolvedStyle(String str) {
        Style styleCreateResolvedStyle = this.resolvedStyles.get(str);
        if (styleCreateResolvedStyle == null) {
            styleCreateResolvedStyle = createResolvedStyle(str);
        }
        return styleCreateResolvedStyle;
    }

    private void addSortedStyle(SelectorMapping selectorMapping, Vector<SelectorMapping> vector) {
        int size = vector.size();
        if (size > 0) {
            int specificity = selectorMapping.getSpecificity();
            for (int i2 = 0; i2 < size; i2++) {
                if (specificity >= vector.elementAt(i2).getSpecificity()) {
                    vector.insertElementAt(selectorMapping, i2);
                    return;
                }
            }
        }
        vector.addElement(selectorMapping);
    }

    private synchronized void getStyles(SelectorMapping selectorMapping, Vector<SelectorMapping> vector, String[] strArr, String[] strArr2, String[] strArr3, int i2, int i3, Hashtable<SelectorMapping, SelectorMapping> hashtable) {
        if (hashtable.contains(selectorMapping)) {
            return;
        }
        hashtable.put(selectorMapping, selectorMapping);
        if (selectorMapping.getStyle() != null) {
            addSortedStyle(selectorMapping, vector);
        }
        for (int i4 = i2; i4 < i3; i4++) {
            String str = strArr[i4];
            if (str != null) {
                SelectorMapping childSelectorMapping = selectorMapping.getChildSelectorMapping(str, false);
                if (childSelectorMapping != null) {
                    getStyles(childSelectorMapping, vector, strArr, strArr2, strArr3, i4 + 1, i3, hashtable);
                }
                if (strArr3[i4] != null) {
                    String str2 = strArr3[i4];
                    SelectorMapping childSelectorMapping2 = selectorMapping.getChildSelectorMapping(str + "." + str2, false);
                    if (childSelectorMapping2 != null) {
                        getStyles(childSelectorMapping2, vector, strArr, strArr2, strArr3, i4 + 1, i3, hashtable);
                    }
                    SelectorMapping childSelectorMapping3 = selectorMapping.getChildSelectorMapping("." + str2, false);
                    if (childSelectorMapping3 != null) {
                        getStyles(childSelectorMapping3, vector, strArr, strArr2, strArr3, i4 + 1, i3, hashtable);
                    }
                }
                if (strArr2[i4] != null) {
                    String str3 = strArr2[i4];
                    SelectorMapping childSelectorMapping4 = selectorMapping.getChildSelectorMapping(str + FXMLLoader.CONTROLLER_METHOD_PREFIX + str3, false);
                    if (childSelectorMapping4 != null) {
                        getStyles(childSelectorMapping4, vector, strArr, strArr2, strArr3, i4 + 1, i3, hashtable);
                    }
                    SelectorMapping childSelectorMapping5 = selectorMapping.getChildSelectorMapping(FXMLLoader.CONTROLLER_METHOD_PREFIX + str3, false);
                    if (childSelectorMapping5 != null) {
                        getStyles(childSelectorMapping5, vector, strArr, strArr2, strArr3, i4 + 1, i3, hashtable);
                    }
                }
            }
        }
    }

    private synchronized Style createResolvedStyle(String str, String[] strArr, String[] strArr2, String[] strArr3) {
        SearchBuffer searchBufferObtainSearchBuffer = SearchBuffer.obtainSearchBuffer();
        Vector vector = searchBufferObtainSearchBuffer.getVector();
        Hashtable hashtable = searchBufferObtainSearchBuffer.getHashtable();
        try {
            SelectorMapping rootSelectorMapping = getRootSelectorMapping();
            int length = strArr.length;
            String str2 = strArr[0];
            SelectorMapping childSelectorMapping = rootSelectorMapping.getChildSelectorMapping(str2, false);
            if (childSelectorMapping != null) {
                getStyles(childSelectorMapping, vector, strArr, strArr2, strArr3, 1, length, hashtable);
            }
            if (strArr3[0] != null) {
                String str3 = strArr3[0];
                SelectorMapping childSelectorMapping2 = rootSelectorMapping.getChildSelectorMapping(str2 + "." + str3, false);
                if (childSelectorMapping2 != null) {
                    getStyles(childSelectorMapping2, vector, strArr, strArr2, strArr3, 1, length, hashtable);
                }
                SelectorMapping childSelectorMapping3 = rootSelectorMapping.getChildSelectorMapping("." + str3, false);
                if (childSelectorMapping3 != null) {
                    getStyles(childSelectorMapping3, vector, strArr, strArr2, strArr3, 1, length, hashtable);
                }
            }
            if (strArr2[0] != null) {
                String str4 = strArr2[0];
                SelectorMapping childSelectorMapping4 = rootSelectorMapping.getChildSelectorMapping(str2 + FXMLLoader.CONTROLLER_METHOD_PREFIX + str4, false);
                if (childSelectorMapping4 != null) {
                    getStyles(childSelectorMapping4, vector, strArr, strArr2, strArr3, 1, length, hashtable);
                }
                SelectorMapping childSelectorMapping5 = rootSelectorMapping.getChildSelectorMapping(FXMLLoader.CONTROLLER_METHOD_PREFIX + str4, false);
                if (childSelectorMapping5 != null) {
                    getStyles(childSelectorMapping5, vector, strArr, strArr2, strArr3, 1, length, hashtable);
                }
            }
            int size = this.linkedStyleSheets != null ? this.linkedStyleSheets.size() : 0;
            int size2 = vector.size();
            AttributeSet[] attributeSetArr = new AttributeSet[size2 + size];
            for (int i2 = 0; i2 < size2; i2++) {
                attributeSetArr[i2] = vector.elementAt(i2).getStyle();
            }
            for (int i3 = 0; i3 < size; i3++) {
                Style rule = this.linkedStyleSheets.elementAt(i3).getRule(str);
                if (rule == null) {
                    attributeSetArr[i3 + size2] = SimpleAttributeSet.EMPTY;
                } else {
                    attributeSetArr[i3 + size2] = rule;
                }
            }
            ResolvedStyle resolvedStyle = new ResolvedStyle(str, attributeSetArr, size2);
            this.resolvedStyles.put(str, resolvedStyle);
            SearchBuffer.releaseSearchBuffer(searchBufferObtainSearchBuffer);
            return resolvedStyle;
        } catch (Throwable th) {
            SearchBuffer.releaseSearchBuffer(searchBufferObtainSearchBuffer);
            throw th;
        }
    }

    private Style createResolvedStyle(String str, Vector vector, HTML.Tag tag) {
        int size = vector.size();
        String[] strArr = new String[size];
        String[] strArr2 = new String[size];
        String[] strArr3 = new String[size];
        for (int i2 = 0; i2 < size; i2++) {
            Element element = (Element) vector.elementAt(i2);
            AttributeSet attributes = element.getAttributes();
            if (i2 == 0 && element.isLeaf()) {
                Object attribute = attributes.getAttribute(tag);
                attributes = attribute instanceof AttributeSet ? (AttributeSet) attribute : null;
            }
            if (attributes != null) {
                HTML.Tag tag2 = (HTML.Tag) attributes.getAttribute(StyleConstants.NameAttribute);
                if (tag2 != null) {
                    strArr[i2] = tag2.toString();
                } else {
                    strArr[i2] = null;
                }
                if (attributes.isDefined(HTML.Attribute.CLASS)) {
                    strArr3[i2] = attributes.getAttribute(HTML.Attribute.CLASS).toString();
                } else {
                    strArr3[i2] = null;
                }
                if (attributes.isDefined(HTML.Attribute.ID)) {
                    strArr2[i2] = attributes.getAttribute(HTML.Attribute.ID).toString();
                } else {
                    strArr2[i2] = null;
                }
            } else {
                strArr3[i2] = null;
                strArr2[i2] = null;
                strArr[i2] = null;
            }
        }
        strArr[0] = tag.toString();
        return createResolvedStyle(str, strArr, strArr2, strArr3);
    }

    private Style createResolvedStyle(String str) {
        int iIndexOf;
        SearchBuffer searchBufferObtainSearchBuffer = SearchBuffer.obtainSearchBuffer();
        Vector vector = searchBufferObtainSearchBuffer.getVector();
        try {
            int iIndexOf2 = 0;
            int iIndexOf3 = 0;
            int length = str.length();
            for (int i2 = 0; i2 < length; i2 = iIndexOf + 1) {
                if (iIndexOf2 == i2) {
                    iIndexOf2 = str.indexOf(46, i2);
                }
                if (iIndexOf3 == i2) {
                    iIndexOf3 = str.indexOf(35, i2);
                }
                iIndexOf = str.indexOf(32, i2);
                if (iIndexOf == -1) {
                    iIndexOf = length;
                }
                if (iIndexOf2 != -1 && iIndexOf3 != -1 && iIndexOf2 < iIndexOf && iIndexOf3 < iIndexOf) {
                    if (iIndexOf3 < iIndexOf2) {
                        if (i2 == iIndexOf3) {
                            vector.addElement("");
                        } else {
                            vector.addElement(str.substring(i2, iIndexOf3));
                        }
                        if (iIndexOf2 + 1 < iIndexOf) {
                            vector.addElement(str.substring(iIndexOf2 + 1, iIndexOf));
                        } else {
                            vector.addElement(null);
                        }
                        if (iIndexOf3 + 1 == iIndexOf2) {
                            vector.addElement(null);
                        } else {
                            vector.addElement(str.substring(iIndexOf3 + 1, iIndexOf2));
                        }
                    } else if (iIndexOf3 < iIndexOf) {
                        if (i2 == iIndexOf2) {
                            vector.addElement("");
                        } else {
                            vector.addElement(str.substring(i2, iIndexOf2));
                        }
                        if (iIndexOf2 + 1 < iIndexOf3) {
                            vector.addElement(str.substring(iIndexOf2 + 1, iIndexOf3));
                        } else {
                            vector.addElement(null);
                        }
                        if (iIndexOf3 + 1 == iIndexOf) {
                            vector.addElement(null);
                        } else {
                            vector.addElement(str.substring(iIndexOf3 + 1, iIndexOf));
                        }
                    }
                    int i3 = iIndexOf + 1;
                    iIndexOf3 = i3;
                    iIndexOf2 = i3;
                } else if (iIndexOf2 != -1 && iIndexOf2 < iIndexOf) {
                    if (iIndexOf2 == i2) {
                        vector.addElement("");
                    } else {
                        vector.addElement(str.substring(i2, iIndexOf2));
                    }
                    if (iIndexOf2 + 1 == iIndexOf) {
                        vector.addElement(null);
                    } else {
                        vector.addElement(str.substring(iIndexOf2 + 1, iIndexOf));
                    }
                    vector.addElement(null);
                    iIndexOf2 = iIndexOf + 1;
                } else if (iIndexOf3 != -1 && iIndexOf3 < iIndexOf) {
                    if (iIndexOf3 == i2) {
                        vector.addElement("");
                    } else {
                        vector.addElement(str.substring(i2, iIndexOf3));
                    }
                    vector.addElement(null);
                    if (iIndexOf3 + 1 == iIndexOf) {
                        vector.addElement(null);
                    } else {
                        vector.addElement(str.substring(iIndexOf3 + 1, iIndexOf));
                    }
                    iIndexOf3 = iIndexOf + 1;
                } else {
                    vector.addElement(str.substring(i2, iIndexOf));
                    vector.addElement(null);
                    vector.addElement(null);
                }
            }
            int size = vector.size();
            int i4 = size / 3;
            String[] strArr = new String[i4];
            String[] strArr2 = new String[i4];
            String[] strArr3 = new String[i4];
            int i5 = 0;
            int i6 = size - 3;
            while (i5 < i4) {
                strArr[i5] = (String) vector.elementAt(i6);
                strArr3[i5] = (String) vector.elementAt(i6 + 1);
                strArr2[i5] = (String) vector.elementAt(i6 + 2);
                i5++;
                i6 -= 3;
            }
            Style styleCreateResolvedStyle = createResolvedStyle(str, strArr, strArr2, strArr3);
            SearchBuffer.releaseSearchBuffer(searchBufferObtainSearchBuffer);
            return styleCreateResolvedStyle;
        } catch (Throwable th) {
            SearchBuffer.releaseSearchBuffer(searchBufferObtainSearchBuffer);
            throw th;
        }
    }

    private synchronized void refreshResolvedRules(String str, String[] strArr, Style style, int i2) {
        if (this.resolvedStyles.size() > 0) {
            Enumeration<ResolvedStyle> enumerationElements = this.resolvedStyles.elements();
            while (enumerationElements.hasMoreElements()) {
                ResolvedStyle resolvedStyleNextElement = enumerationElements.nextElement2();
                if (resolvedStyleNextElement.matches(str)) {
                    resolvedStyleNextElement.insertStyle(style, i2);
                }
            }
        }
    }

    /* loaded from: rt.jar:javax/swing/text/html/StyleSheet$SearchBuffer.class */
    private static class SearchBuffer {
        static Stack<SearchBuffer> searchBuffers = new Stack<>();
        Vector vector = null;
        StringBuffer stringBuffer = null;
        Hashtable hashtable = null;

        private SearchBuffer() {
        }

        static SearchBuffer obtainSearchBuffer() {
            SearchBuffer searchBuffer;
            try {
                if (!searchBuffers.empty()) {
                    searchBuffer = searchBuffers.pop();
                } else {
                    searchBuffer = new SearchBuffer();
                }
            } catch (EmptyStackException e2) {
                searchBuffer = new SearchBuffer();
            }
            return searchBuffer;
        }

        static void releaseSearchBuffer(SearchBuffer searchBuffer) {
            searchBuffer.empty();
            searchBuffers.push(searchBuffer);
        }

        StringBuffer getStringBuffer() {
            if (this.stringBuffer == null) {
                this.stringBuffer = new StringBuffer();
            }
            return this.stringBuffer;
        }

        Vector getVector() {
            if (this.vector == null) {
                this.vector = new Vector();
            }
            return this.vector;
        }

        Hashtable getHashtable() {
            if (this.hashtable == null) {
                this.hashtable = new Hashtable();
            }
            return this.hashtable;
        }

        void empty() {
            if (this.stringBuffer != null) {
                this.stringBuffer.setLength(0);
            }
            if (this.vector != null) {
                this.vector.removeAllElements();
            }
            if (this.hashtable != null) {
                this.hashtable.clear();
            }
        }
    }

    /* loaded from: rt.jar:javax/swing/text/html/StyleSheet$BoxPainter.class */
    public static class BoxPainter implements Serializable {
        float topMargin;
        float bottomMargin;
        float leftMargin;
        float rightMargin;
        short marginFlags;
        Border border;
        Insets binsets;
        CSS css;
        StyleSheet ss;

        /* renamed from: bg, reason: collision with root package name */
        Color f12854bg;
        BackgroundImagePainter bgPainter;

        /* loaded from: rt.jar:javax/swing/text/html/StyleSheet$BoxPainter$HorizontalMargin.class */
        enum HorizontalMargin {
            LEFT,
            RIGHT
        }

        BoxPainter(AttributeSet attributeSet, CSS css, StyleSheet styleSheet) {
            this.ss = styleSheet;
            this.css = css;
            this.border = getBorder(attributeSet);
            this.binsets = this.border.getBorderInsets(null);
            this.topMargin = getLength(CSS.Attribute.MARGIN_TOP, attributeSet);
            this.bottomMargin = getLength(CSS.Attribute.MARGIN_BOTTOM, attributeSet);
            this.leftMargin = getLength(CSS.Attribute.MARGIN_LEFT, attributeSet);
            this.rightMargin = getLength(CSS.Attribute.MARGIN_RIGHT, attributeSet);
            this.f12854bg = styleSheet.getBackground(attributeSet);
            if (styleSheet.getBackgroundImage(attributeSet) != null) {
                this.bgPainter = new BackgroundImagePainter(attributeSet, css, styleSheet);
            }
        }

        Border getBorder(AttributeSet attributeSet) {
            return new CSSBorder(attributeSet);
        }

        Color getBorderColor(AttributeSet attributeSet) {
            Color color = this.css.getColor(attributeSet, CSS.Attribute.BORDER_COLOR);
            if (color == null) {
                color = this.css.getColor(attributeSet, CSS.Attribute.COLOR);
                if (color == null) {
                    return Color.black;
                }
            }
            return color;
        }

        public float getInset(int i2, View view) {
            float length;
            AttributeSet attributes = view.getAttributes();
            switch (i2) {
                case 1:
                    length = 0.0f + this.topMargin + this.binsets.top + getLength(CSS.Attribute.PADDING_TOP, attributes);
                    break;
                case 2:
                    length = 0.0f + getOrientationMargin(HorizontalMargin.LEFT, this.leftMargin, attributes, isLeftToRight(view)) + this.binsets.left + getLength(CSS.Attribute.PADDING_LEFT, attributes);
                    break;
                case 3:
                    length = 0.0f + this.bottomMargin + this.binsets.bottom + getLength(CSS.Attribute.PADDING_BOTTOM, attributes);
                    break;
                case 4:
                    length = 0.0f + getOrientationMargin(HorizontalMargin.RIGHT, this.rightMargin, attributes, isLeftToRight(view)) + this.binsets.right + getLength(CSS.Attribute.PADDING_RIGHT, attributes);
                    break;
                default:
                    throw new IllegalArgumentException("Invalid side: " + i2);
            }
            return length;
        }

        public void paint(Graphics graphics, float f2, float f3, float f4, float f5, View view) {
            float f6 = 0.0f;
            float f7 = 0.0f;
            float f8 = 0.0f;
            float f9 = 0.0f;
            AttributeSet attributes = view.getAttributes();
            boolean zIsLeftToRight = isLeftToRight(view);
            float orientationMargin = getOrientationMargin(HorizontalMargin.LEFT, this.leftMargin, attributes, zIsLeftToRight);
            float orientationMargin2 = getOrientationMargin(HorizontalMargin.RIGHT, this.rightMargin, attributes, zIsLeftToRight);
            if (!(view instanceof HTMLEditorKit.HTMLFactory.BodyBlockView)) {
                f6 = orientationMargin;
                f7 = this.topMargin;
                f8 = -(orientationMargin + orientationMargin2);
                f9 = -(this.topMargin + this.bottomMargin);
            }
            if (this.f12854bg != null) {
                graphics.setColor(this.f12854bg);
                graphics.fillRect((int) (f2 + f6), (int) (f3 + f7), (int) (f4 + f8), (int) (f5 + f9));
            }
            if (this.bgPainter != null) {
                this.bgPainter.paint(graphics, f2 + f6, f3 + f7, f4 + f8, f5 + f9, view);
            }
            float f10 = f2 + orientationMargin;
            float f11 = f3 + this.topMargin;
            float f12 = f4 - (orientationMargin + orientationMargin2);
            float f13 = f5 - (this.topMargin + this.bottomMargin);
            if (this.border instanceof BevelBorder) {
                for (int length = ((int) getLength(CSS.Attribute.BORDER_TOP_WIDTH, attributes)) - 1; length >= 0; length--) {
                    this.border.paintBorder(null, graphics, ((int) f10) + length, ((int) f11) + length, ((int) f12) - (2 * length), ((int) f13) - (2 * length));
                }
                return;
            }
            this.border.paintBorder(null, graphics, (int) f10, (int) f11, (int) f12, (int) f13);
        }

        float getLength(CSS.Attribute attribute, AttributeSet attributeSet) {
            return this.css.getLength(attributeSet, attribute, this.ss);
        }

        static boolean isLeftToRight(View view) {
            Container container;
            boolean zIsLeftToRight = true;
            if (isOrientationAware(view) && view != null && (container = view.getContainer()) != null) {
                zIsLeftToRight = container.getComponentOrientation().isLeftToRight();
            }
            return zIsLeftToRight;
        }

        static boolean isOrientationAware(View view) {
            AttributeSet attributes;
            boolean z2 = false;
            if (view != null && (attributes = view.getElement().getAttributes()) != null) {
                Object attribute = attributes.getAttribute(StyleConstants.NameAttribute);
                if ((attribute instanceof HTML.Tag) && (attribute == HTML.Tag.DIR || attribute == HTML.Tag.MENU || attribute == HTML.Tag.UL || attribute == HTML.Tag.OL)) {
                    z2 = true;
                }
            }
            return z2;
        }

        float getOrientationMargin(HorizontalMargin horizontalMargin, float f2, AttributeSet attributeSet, boolean z2) {
            float length;
            float length2;
            float f3 = f2;
            float f4 = f2;
            Object attribute = null;
            switch (horizontalMargin) {
                case RIGHT:
                    if (z2) {
                        length2 = getLength(CSS.Attribute.MARGIN_RIGHT_LTR, attributeSet);
                    } else {
                        length2 = getLength(CSS.Attribute.MARGIN_RIGHT_RTL, attributeSet);
                    }
                    f4 = length2;
                    attribute = attributeSet.getAttribute(CSS.Attribute.MARGIN_RIGHT);
                    break;
                case LEFT:
                    if (z2) {
                        length = getLength(CSS.Attribute.MARGIN_LEFT_LTR, attributeSet);
                    } else {
                        length = getLength(CSS.Attribute.MARGIN_LEFT_RTL, attributeSet);
                    }
                    f4 = length;
                    attribute = attributeSet.getAttribute(CSS.Attribute.MARGIN_LEFT);
                    break;
            }
            if (attribute == null && f4 != -2.1474836E9f) {
                f3 = f4;
            }
            return f3;
        }
    }

    /* loaded from: rt.jar:javax/swing/text/html/StyleSheet$ListPainter.class */
    public static class ListPainter implements Serializable {
        static final char[][] romanChars = {new char[]{'i', 'v'}, new char[]{'x', 'l'}, new char[]{'c', 'd'}, new char[]{'m', '?'}};
        private Rectangle paintRect;
        private boolean checkedForStart;
        private int start;
        private CSS.Value type;
        URL imageurl;
        private StyleSheet ss;
        Icon img;
        private int bulletgap = 5;
        private boolean isLeftToRight;

        ListPainter(AttributeSet attributeSet, StyleSheet styleSheet) {
            this.ss = null;
            this.img = null;
            this.ss = styleSheet;
            String str = (String) attributeSet.getAttribute(CSS.Attribute.LIST_STYLE_IMAGE);
            this.type = null;
            if (str != null && !str.equals(Separation.COLORANT_NONE)) {
                String strNextToken = null;
                try {
                    StringTokenizer stringTokenizer = new StringTokenizer(str, "()");
                    strNextToken = stringTokenizer.hasMoreTokens() ? stringTokenizer.nextToken() : stringTokenizer.hasMoreTokens() ? stringTokenizer.nextToken() : strNextToken;
                    this.img = new ImageIcon(new URL(strNextToken));
                } catch (MalformedURLException e2) {
                    if (strNextToken != null && styleSheet != null && styleSheet.getBase() != null) {
                        try {
                            this.img = new ImageIcon(new URL(styleSheet.getBase(), strNextToken));
                        } catch (MalformedURLException e3) {
                            this.img = null;
                        }
                    } else {
                        this.img = null;
                    }
                }
            }
            if (this.img == null) {
                this.type = (CSS.Value) attributeSet.getAttribute(CSS.Attribute.LIST_STYLE_TYPE);
            }
            this.start = 1;
            this.paintRect = new Rectangle();
        }

        private CSS.Value getChildType(View view) {
            CSS.Value value = (CSS.Value) view.getAttributes().getAttribute(CSS.Attribute.LIST_STYLE_TYPE);
            if (value == null) {
                if (this.type == null) {
                    View parent = view.getParent();
                    if (HTMLDocument.matchNameAttribute(parent.getElement().getAttributes(), HTML.Tag.OL)) {
                        value = CSS.Value.DECIMAL;
                    } else {
                        value = CSS.Value.DISC;
                    }
                } else {
                    value = this.type;
                }
            }
            return value;
        }

        private void getStart(View view) {
            AttributeSet attributes;
            Object attribute;
            this.checkedForStart = true;
            Element element = view.getElement();
            if (element != null && (attributes = element.getAttributes()) != null && attributes.isDefined(HTML.Attribute.START) && (attribute = attributes.getAttribute(HTML.Attribute.START)) != null && (attribute instanceof String)) {
                try {
                    this.start = Integer.parseInt((String) attribute);
                } catch (NumberFormatException e2) {
                }
            }
        }

        private int getRenderIndex(View view, int i2) {
            Object attribute;
            if (!this.checkedForStart) {
                getStart(view);
            }
            int i3 = i2;
            for (int i4 = i2; i4 >= 0; i4--) {
                AttributeSet attributes = view.getElement().getElement(i4).getAttributes();
                if (attributes.getAttribute(StyleConstants.NameAttribute) != HTML.Tag.LI) {
                    i3--;
                } else if (attributes.isDefined(HTML.Attribute.VALUE) && (attribute = attributes.getAttribute(HTML.Attribute.VALUE)) != null && (attribute instanceof String)) {
                    try {
                        return (i3 - i4) + Integer.parseInt((String) attribute);
                    } catch (NumberFormatException e2) {
                    }
                }
            }
            return i3 + this.start;
        }

        public void paint(Graphics graphics, float f2, float f3, float f4, float f5, View view, int i2) {
            Color color;
            Shape childAllocation;
            View view2 = view.getView(i2);
            Container container = view.getContainer();
            Object attribute = view2.getElement().getAttributes().getAttribute(StyleConstants.NameAttribute);
            if (!(attribute instanceof HTML.Tag) || attribute != HTML.Tag.LI) {
                return;
            }
            this.isLeftToRight = container.getComponentOrientation().isLeftToRight();
            float alignment = 0.0f;
            if (view2.getViewCount() > 0) {
                View view3 = view2.getView(0);
                Object attribute2 = view3.getElement().getAttributes().getAttribute(StyleConstants.NameAttribute);
                if ((attribute2 == HTML.Tag.f12849P || attribute2 == HTML.Tag.IMPLIED) && view3.getViewCount() > 0) {
                    this.paintRect.setBounds((int) f2, (int) f3, (int) f4, (int) f5);
                    Shape childAllocation2 = view2.getChildAllocation(0, this.paintRect);
                    if (childAllocation2 != null && (childAllocation = view3.getView(0).getChildAllocation(0, childAllocation2)) != null) {
                        Rectangle bounds = childAllocation instanceof Rectangle ? (Rectangle) childAllocation : childAllocation.getBounds();
                        alignment = view3.getView(0).getAlignment(1);
                        f3 = bounds.f12373y;
                        f5 = bounds.height;
                    }
                }
            }
            if (!container.isEnabled()) {
                color = UIManager.getColor("textInactiveText");
            } else if (this.ss != null) {
                color = this.ss.getForeground(view2.getAttributes());
            } else {
                color = container.getForeground();
            }
            graphics.setColor(color);
            if (this.img != null) {
                drawIcon(graphics, (int) f2, (int) f3, (int) f4, (int) f5, alignment, container);
                return;
            }
            CSS.Value childType = getChildType(view2);
            Font font = ((StyledDocument) view2.getDocument()).getFont(view2.getAttributes());
            if (font != null) {
                graphics.setFont(font);
            }
            if (childType == CSS.Value.SQUARE || childType == CSS.Value.CIRCLE || childType == CSS.Value.DISC) {
                drawShape(graphics, childType, (int) f2, (int) f3, (int) f4, (int) f5, alignment);
                return;
            }
            if (childType == CSS.Value.DECIMAL) {
                drawLetter(graphics, '1', (int) f2, (int) f3, (int) f4, (int) f5, alignment, getRenderIndex(view, i2));
                return;
            }
            if (childType == CSS.Value.LOWER_ALPHA) {
                drawLetter(graphics, 'a', (int) f2, (int) f3, (int) f4, (int) f5, alignment, getRenderIndex(view, i2));
                return;
            }
            if (childType == CSS.Value.UPPER_ALPHA) {
                drawLetter(graphics, 'A', (int) f2, (int) f3, (int) f4, (int) f5, alignment, getRenderIndex(view, i2));
            } else if (childType == CSS.Value.LOWER_ROMAN) {
                drawLetter(graphics, 'i', (int) f2, (int) f3, (int) f4, (int) f5, alignment, getRenderIndex(view, i2));
            } else if (childType == CSS.Value.UPPER_ROMAN) {
                drawLetter(graphics, 'I', (int) f2, (int) f3, (int) f4, (int) f5, alignment, getRenderIndex(view, i2));
            }
        }

        void drawIcon(Graphics graphics, int i2, int i3, int i4, int i5, float f2, Component component) {
            this.img.paintIcon(component, graphics, i2 + (this.isLeftToRight ? -(this.img.getIconWidth() + this.bulletgap) : i4 + this.bulletgap), Math.max(i3, (i3 + ((int) (f2 * i5))) - this.img.getIconHeight()));
        }

        void drawShape(Graphics graphics, CSS.Value value, int i2, int i3, int i4, int i5, float f2) {
            int i6 = i2 + (this.isLeftToRight ? -(this.bulletgap + 8) : i4 + this.bulletgap);
            int iMax = Math.max(i3, (i3 + ((int) (f2 * i5))) - 8);
            if (value == CSS.Value.SQUARE) {
                graphics.drawRect(i6, iMax, 8, 8);
            } else if (value == CSS.Value.CIRCLE) {
                graphics.drawOval(i6, iMax, 8, 8);
            } else {
                graphics.fillOval(i6, iMax, 8, 8);
            }
        }

        void drawLetter(Graphics graphics, char c2, int i2, int i3, int i4, int i5, float f2, int i6) {
            String itemNum = formatItemNum(i6, c2);
            String str = this.isLeftToRight ? itemNum + "." : "." + itemNum;
            FontMetrics fontMetrics = SwingUtilities2.getFontMetrics((JComponent) null, graphics);
            SwingUtilities2.drawString((JComponent) null, graphics, str, i2 + (this.isLeftToRight ? -(SwingUtilities2.stringWidth(null, fontMetrics, str) + this.bulletgap) : i4 + this.bulletgap), Math.max(i3 + fontMetrics.getAscent(), i3 + ((int) (i5 * f2))));
        }

        /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
        String formatItemNum(int i2, char c2) {
            String romanNumerals;
            boolean z2 = false;
            switch (c2) {
                case '1':
                default:
                    romanNumerals = String.valueOf(i2);
                    break;
                case 'A':
                    z2 = true;
                    romanNumerals = formatAlphaNumerals(i2);
                    break;
                case 'I':
                    z2 = true;
                    romanNumerals = formatRomanNumerals(i2);
                    break;
                case 'a':
                    romanNumerals = formatAlphaNumerals(i2);
                    break;
                case 'i':
                    romanNumerals = formatRomanNumerals(i2);
                    break;
            }
            if (z2) {
                romanNumerals = romanNumerals.toUpperCase();
            }
            return romanNumerals;
        }

        String formatAlphaNumerals(int i2) {
            String strValueOf;
            if (i2 > 26) {
                strValueOf = formatAlphaNumerals(i2 / 26) + formatAlphaNumerals(i2 % 26);
            } else {
                strValueOf = String.valueOf((char) ((97 + i2) - 1));
            }
            return strValueOf;
        }

        String formatRomanNumerals(int i2) {
            return formatRomanNumerals(0, i2);
        }

        String formatRomanNumerals(int i2, int i3) {
            if (i3 < 10) {
                return formatRomanDigit(i2, i3);
            }
            return formatRomanNumerals(i2 + 1, i3 / 10) + formatRomanDigit(i2, i3 % 10);
        }

        String formatRomanDigit(int i2, int i3) {
            String str = "";
            if (i3 == 9) {
                return (str + romanChars[i2][0]) + romanChars[i2 + 1][0];
            }
            if (i3 == 4) {
                return (str + romanChars[i2][0]) + romanChars[i2][1];
            }
            if (i3 >= 5) {
                str = str + romanChars[i2][1];
                i3 -= 5;
            }
            for (int i4 = 0; i4 < i3; i4++) {
                str = str + romanChars[i2][0];
            }
            return str;
        }
    }

    /* loaded from: rt.jar:javax/swing/text/html/StyleSheet$BackgroundImagePainter.class */
    static class BackgroundImagePainter implements Serializable {
        ImageIcon backgroundImage;
        float hPosition;
        float vPosition;
        short flags;
        private int paintX;
        private int paintY;
        private int paintMaxX;
        private int paintMaxY;

        BackgroundImagePainter(AttributeSet attributeSet, CSS css, StyleSheet styleSheet) {
            this.backgroundImage = styleSheet.getBackgroundImage(attributeSet);
            CSS.BackgroundPosition backgroundPosition = (CSS.BackgroundPosition) attributeSet.getAttribute(CSS.Attribute.BACKGROUND_POSITION);
            if (backgroundPosition != null) {
                this.hPosition = backgroundPosition.getHorizontalPosition();
                this.vPosition = backgroundPosition.getVerticalPosition();
                if (backgroundPosition.isHorizontalPositionRelativeToSize()) {
                    this.flags = (short) (this.flags | 4);
                } else if (backgroundPosition.isHorizontalPositionRelativeToSize()) {
                    this.hPosition *= CSS.getFontSize(attributeSet, 12, styleSheet);
                }
                if (backgroundPosition.isVerticalPositionRelativeToSize()) {
                    this.flags = (short) (this.flags | 8);
                } else if (backgroundPosition.isVerticalPositionRelativeToFontSize()) {
                    this.vPosition *= CSS.getFontSize(attributeSet, 12, styleSheet);
                }
            }
            CSS.Value value = (CSS.Value) attributeSet.getAttribute(CSS.Attribute.BACKGROUND_REPEAT);
            if (value == null || value == CSS.Value.BACKGROUND_REPEAT) {
                this.flags = (short) (this.flags | 3);
            } else if (value == CSS.Value.BACKGROUND_REPEAT_X) {
                this.flags = (short) (this.flags | 1);
            } else if (value == CSS.Value.BACKGROUND_REPEAT_Y) {
                this.flags = (short) (this.flags | 2);
            }
        }

        void paint(Graphics graphics, float f2, float f3, float f4, float f5, View view) {
            Rectangle clipRect = graphics.getClipRect();
            if (clipRect != null) {
                graphics.clipRect((int) f2, (int) f3, (int) f4, (int) f5);
            }
            if ((this.flags & 3) == 0) {
                int iconWidth = this.backgroundImage.getIconWidth();
                int iconWidth2 = this.backgroundImage.getIconWidth();
                if ((this.flags & 4) == 4) {
                    this.paintX = (int) ((f2 + (f4 * this.hPosition)) - (iconWidth * this.hPosition));
                } else {
                    this.paintX = ((int) f2) + ((int) this.hPosition);
                }
                if ((this.flags & 8) == 8) {
                    this.paintY = (int) ((f3 + (f5 * this.vPosition)) - (iconWidth2 * this.vPosition));
                } else {
                    this.paintY = ((int) f3) + ((int) this.vPosition);
                }
                if (clipRect == null || (this.paintX + iconWidth > clipRect.f12372x && this.paintY + iconWidth2 > clipRect.f12373y && this.paintX < clipRect.f12372x + clipRect.width && this.paintY < clipRect.f12373y + clipRect.height)) {
                    this.backgroundImage.paintIcon(null, graphics, this.paintX, this.paintY);
                }
            } else {
                int iconWidth3 = this.backgroundImage.getIconWidth();
                int iconHeight = this.backgroundImage.getIconHeight();
                if (iconWidth3 > 0 && iconHeight > 0) {
                    this.paintX = (int) f2;
                    this.paintY = (int) f3;
                    this.paintMaxX = (int) (f2 + f4);
                    this.paintMaxY = (int) (f3 + f5);
                    if (updatePaintCoordinates(clipRect, iconWidth3, iconHeight)) {
                        while (this.paintX < this.paintMaxX) {
                            int i2 = this.paintY;
                            while (true) {
                                int i3 = i2;
                                if (i3 < this.paintMaxY) {
                                    this.backgroundImage.paintIcon(null, graphics, this.paintX, i3);
                                    i2 = i3 + iconHeight;
                                }
                            }
                            this.paintX += iconWidth3;
                        }
                    }
                }
            }
            if (clipRect != null) {
                graphics.setClip(clipRect.f12372x, clipRect.f12373y, clipRect.width, clipRect.height);
            }
        }

        private boolean updatePaintCoordinates(Rectangle rectangle, int i2, int i3) {
            if ((this.flags & 3) == 1) {
                this.paintMaxY = this.paintY + 1;
            } else if ((this.flags & 3) == 2) {
                this.paintMaxX = this.paintX + 1;
            }
            if (rectangle != null) {
                if ((this.flags & 3) == 1 && (this.paintY + i3 <= rectangle.f12373y || this.paintY > rectangle.f12373y + rectangle.height)) {
                    return false;
                }
                if ((this.flags & 3) == 2 && (this.paintX + i2 <= rectangle.f12372x || this.paintX > rectangle.f12372x + rectangle.width)) {
                    return false;
                }
                if ((this.flags & 1) == 1) {
                    if (rectangle.f12372x + rectangle.width < this.paintMaxX) {
                        if (((rectangle.f12372x + rectangle.width) - this.paintX) % i2 == 0) {
                            this.paintMaxX = rectangle.f12372x + rectangle.width;
                        } else {
                            this.paintMaxX = (((((rectangle.f12372x + rectangle.width) - this.paintX) / i2) + 1) * i2) + this.paintX;
                        }
                    }
                    if (rectangle.f12372x > this.paintX) {
                        this.paintX = (((rectangle.f12372x - this.paintX) / i2) * i2) + this.paintX;
                    }
                }
                if ((this.flags & 2) == 2) {
                    if (rectangle.f12373y + rectangle.height < this.paintMaxY) {
                        if (((rectangle.f12373y + rectangle.height) - this.paintY) % i3 == 0) {
                            this.paintMaxY = rectangle.f12373y + rectangle.height;
                        } else {
                            this.paintMaxY = (((((rectangle.f12373y + rectangle.height) - this.paintY) / i3) + 1) * i3) + this.paintY;
                        }
                    }
                    if (rectangle.f12373y > this.paintY) {
                        this.paintY = (((rectangle.f12373y - this.paintY) / i3) * i3) + this.paintY;
                        return true;
                    }
                    return true;
                }
                return true;
            }
            return true;
        }
    }

    /* loaded from: rt.jar:javax/swing/text/html/StyleSheet$ViewAttributeSet.class */
    class ViewAttributeSet extends MuxingAttributeSet {
        View host;

        ViewAttributeSet(View view) {
            Object attribute;
            this.host = view;
            Document document = view.getDocument();
            SearchBuffer searchBufferObtainSearchBuffer = SearchBuffer.obtainSearchBuffer();
            Vector vector = searchBufferObtainSearchBuffer.getVector();
            try {
                if (document instanceof HTMLDocument) {
                    Element element = view.getElement();
                    AttributeSet attributes = element.getAttributes();
                    AttributeSet attributeSetTranslateHTMLToCSS = StyleSheet.this.translateHTMLToCSS(attributes);
                    if (attributeSetTranslateHTMLToCSS.getAttributeCount() != 0) {
                        vector.addElement(attributeSetTranslateHTMLToCSS);
                    }
                    if (element.isLeaf()) {
                        Enumeration<?> attributeNames = attributes.getAttributeNames();
                        while (attributeNames.hasMoreElements()) {
                            Object objNextElement = attributeNames.nextElement2();
                            if (objNextElement instanceof HTML.Tag) {
                                if (objNextElement != HTML.Tag.f12846A || (attribute = attributes.getAttribute(objNextElement)) == null || !(attribute instanceof AttributeSet) || ((AttributeSet) attribute).getAttribute(HTML.Attribute.HREF) != null) {
                                    Style rule = StyleSheet.this.getRule((HTML.Tag) objNextElement, element);
                                    if (rule != null) {
                                        vector.addElement(rule);
                                    }
                                }
                            }
                        }
                    } else {
                        Style rule2 = StyleSheet.this.getRule((HTML.Tag) attributes.getAttribute(StyleConstants.NameAttribute), element);
                        if (rule2 != null) {
                            vector.addElement(rule2);
                        }
                    }
                }
                AttributeSet[] attributeSetArr = new AttributeSet[vector.size()];
                vector.copyInto(attributeSetArr);
                setAttributes(attributeSetArr);
                SearchBuffer.releaseSearchBuffer(searchBufferObtainSearchBuffer);
            } catch (Throwable th) {
                SearchBuffer.releaseSearchBuffer(searchBufferObtainSearchBuffer);
                throw th;
            }
        }

        @Override // javax.swing.text.html.MuxingAttributeSet, javax.swing.text.AttributeSet
        public boolean isDefined(Object obj) {
            CSS.Attribute attributeStyleConstantsKeyToCSSKey;
            if ((obj instanceof StyleConstants) && (attributeStyleConstantsKeyToCSSKey = StyleSheet.this.css.styleConstantsKeyToCSSKey((StyleConstants) obj)) != null) {
                obj = attributeStyleConstantsKeyToCSSKey;
            }
            return super.isDefined(obj);
        }

        @Override // javax.swing.text.html.MuxingAttributeSet, javax.swing.text.AttributeSet
        public Object getAttribute(Object obj) {
            CSS.Attribute attributeStyleConstantsKeyToCSSKey;
            if ((obj instanceof StyleConstants) && (attributeStyleConstantsKeyToCSSKey = StyleSheet.this.css.styleConstantsKeyToCSSKey((StyleConstants) obj)) != null) {
                Object objDoGetAttribute = doGetAttribute(attributeStyleConstantsKeyToCSSKey);
                if (objDoGetAttribute instanceof CSS.CssValue) {
                    return ((CSS.CssValue) objDoGetAttribute).toStyleConstants((StyleConstants) obj, this.host);
                }
            }
            return doGetAttribute(obj);
        }

        Object doGetAttribute(Object obj) {
            AttributeSet resolveParent;
            Object attribute = super.getAttribute(obj);
            if (attribute != null) {
                return attribute;
            }
            if ((obj instanceof CSS.Attribute) && ((CSS.Attribute) obj).isInherited() && (resolveParent = getResolveParent()) != null) {
                return resolveParent.getAttribute(obj);
            }
            return null;
        }

        @Override // javax.swing.text.html.MuxingAttributeSet, javax.swing.text.AttributeSet
        public AttributeSet getResolveParent() {
            View parent;
            if (this.host == null || (parent = this.host.getParent()) == null) {
                return null;
            }
            return parent.getAttributes();
        }
    }

    /* loaded from: rt.jar:javax/swing/text/html/StyleSheet$ResolvedStyle.class */
    static class ResolvedStyle extends MuxingAttributeSet implements Serializable, Style {
        String name;
        private int extendedIndex;

        ResolvedStyle(String str, AttributeSet[] attributeSetArr, int i2) {
            super(attributeSetArr);
            this.name = str;
            this.extendedIndex = i2;
        }

        synchronized void insertStyle(Style style, int i2) {
            AttributeSet[] attributes = getAttributes();
            int length = attributes.length;
            int i3 = 0;
            while (i3 < this.extendedIndex && i2 <= StyleSheet.getSpecificity(((Style) attributes[i3]).getName())) {
                i3++;
            }
            insertAttributeSetAt(style, i3);
            this.extendedIndex++;
        }

        synchronized void removeStyle(Style style) {
            AttributeSet[] attributes = getAttributes();
            for (int length = attributes.length - 1; length >= 0; length--) {
                if (attributes[length] == style) {
                    removeAttributeSetAt(length);
                    if (length < this.extendedIndex) {
                        this.extendedIndex--;
                        return;
                    }
                    return;
                }
            }
        }

        synchronized void insertExtendedStyleAt(Style style, int i2) {
            insertAttributeSetAt(style, this.extendedIndex + i2);
        }

        synchronized void addExtendedStyle(Style style) {
            insertAttributeSetAt(style, getAttributes().length);
        }

        synchronized void removeExtendedStyleAt(int i2) {
            removeAttributeSetAt(this.extendedIndex + i2);
        }

        protected boolean matches(String str) {
            boolean z2;
            int length = str.length();
            if (length == 0) {
                return false;
            }
            int length2 = this.name.length();
            int iLastIndexOf = str.lastIndexOf(32);
            int iLastIndexOf2 = this.name.lastIndexOf(32);
            if (iLastIndexOf >= 0) {
                iLastIndexOf++;
            }
            if (iLastIndexOf2 >= 0) {
                iLastIndexOf2++;
            }
            if (!matches(str, iLastIndexOf, length, iLastIndexOf2, length2)) {
                return false;
            }
            while (iLastIndexOf != -1) {
                int i2 = iLastIndexOf - 1;
                iLastIndexOf = str.lastIndexOf(32, i2 - 1);
                if (iLastIndexOf >= 0) {
                    iLastIndexOf++;
                }
                boolean zMatches = false;
                while (true) {
                    z2 = zMatches;
                    if (z2 || iLastIndexOf2 == -1) {
                        break;
                    }
                    int i3 = iLastIndexOf2 - 1;
                    iLastIndexOf2 = this.name.lastIndexOf(32, i3 - 1);
                    if (iLastIndexOf2 >= 0) {
                        iLastIndexOf2++;
                    }
                    zMatches = matches(str, iLastIndexOf, i2, iLastIndexOf2, i3);
                }
                if (!z2) {
                    return false;
                }
            }
            return true;
        }

        boolean matches(String str, int i2, int i3, int i4, int i5) {
            int iMax = Math.max(i2, 0);
            int iMax2 = Math.max(i4, 0);
            int iBoundedIndexOf = boundedIndexOf(this.name, '.', iMax2, i5);
            int iBoundedIndexOf2 = boundedIndexOf(this.name, '#', iMax2, i5);
            int iBoundedIndexOf3 = boundedIndexOf(str, '.', iMax, i3);
            int iBoundedIndexOf4 = boundedIndexOf(str, '#', iMax, i3);
            if (iBoundedIndexOf3 != -1) {
                if (iBoundedIndexOf == -1) {
                    return false;
                }
                if (iMax == iBoundedIndexOf3) {
                    if (i5 - iBoundedIndexOf != i3 - iBoundedIndexOf3 || !str.regionMatches(iMax, this.name, iBoundedIndexOf, i5 - iBoundedIndexOf)) {
                        return false;
                    }
                    return true;
                }
                if (i3 - iMax != i5 - iMax2 || !str.regionMatches(iMax, this.name, iMax2, i5 - iMax2)) {
                    return false;
                }
                return true;
            }
            if (iBoundedIndexOf4 == -1) {
                return iBoundedIndexOf != -1 ? iBoundedIndexOf - iMax2 == i3 - iMax && str.regionMatches(iMax, this.name, iMax2, iBoundedIndexOf - iMax2) : iBoundedIndexOf2 != -1 ? iBoundedIndexOf2 - iMax2 == i3 - iMax && str.regionMatches(iMax, this.name, iMax2, iBoundedIndexOf2 - iMax2) : i5 - iMax2 == i3 - iMax && str.regionMatches(iMax, this.name, iMax2, i5 - iMax2);
            }
            if (iBoundedIndexOf2 == -1) {
                return false;
            }
            if (iMax == iBoundedIndexOf4) {
                if (i5 - iBoundedIndexOf2 != i3 - iBoundedIndexOf4 || !str.regionMatches(iMax, this.name, iBoundedIndexOf2, i5 - iBoundedIndexOf2)) {
                    return false;
                }
                return true;
            }
            if (i3 - iMax != i5 - iMax2 || !str.regionMatches(iMax, this.name, iMax2, i5 - iMax2)) {
                return false;
            }
            return true;
        }

        int boundedIndexOf(String str, char c2, int i2, int i3) {
            int iIndexOf = str.indexOf(c2, i2);
            if (iIndexOf >= i3) {
                return -1;
            }
            return iIndexOf;
        }

        @Override // javax.swing.text.MutableAttributeSet
        public void addAttribute(Object obj, Object obj2) {
        }

        @Override // javax.swing.text.MutableAttributeSet
        public void addAttributes(AttributeSet attributeSet) {
        }

        @Override // javax.swing.text.MutableAttributeSet
        public void removeAttribute(Object obj) {
        }

        @Override // javax.swing.text.MutableAttributeSet
        public void removeAttributes(Enumeration<?> enumeration) {
        }

        @Override // javax.swing.text.MutableAttributeSet
        public void removeAttributes(AttributeSet attributeSet) {
        }

        @Override // javax.swing.text.MutableAttributeSet
        public void setResolveParent(AttributeSet attributeSet) {
        }

        @Override // javax.swing.text.Style
        public String getName() {
            return this.name;
        }

        @Override // javax.swing.text.Style
        public void addChangeListener(ChangeListener changeListener) {
        }

        @Override // javax.swing.text.Style
        public void removeChangeListener(ChangeListener changeListener) {
        }

        public ChangeListener[] getChangeListeners() {
            return new ChangeListener[0];
        }
    }

    /* loaded from: rt.jar:javax/swing/text/html/StyleSheet$SelectorMapping.class */
    static class SelectorMapping implements Serializable {
        private int specificity;
        private Style style;
        private HashMap<String, SelectorMapping> children;

        public SelectorMapping(int i2) {
            this.specificity = i2;
        }

        public int getSpecificity() {
            return this.specificity;
        }

        public void setStyle(Style style) {
            this.style = style;
        }

        public Style getStyle() {
            return this.style;
        }

        public SelectorMapping getChildSelectorMapping(String str, boolean z2) {
            SelectorMapping selectorMappingCreateChildSelectorMapping = null;
            if (this.children != null) {
                selectorMappingCreateChildSelectorMapping = this.children.get(str);
            } else if (z2) {
                this.children = new HashMap<>(7);
            }
            if (selectorMappingCreateChildSelectorMapping == null && z2) {
                selectorMappingCreateChildSelectorMapping = createChildSelectorMapping(getChildSpecificity(str));
                this.children.put(str, selectorMappingCreateChildSelectorMapping);
            }
            return selectorMappingCreateChildSelectorMapping;
        }

        protected SelectorMapping createChildSelectorMapping(int i2) {
            return new SelectorMapping(i2);
        }

        protected int getChildSpecificity(String str) {
            int i2;
            char cCharAt = str.charAt(0);
            int specificity = getSpecificity();
            if (cCharAt == '.') {
                i2 = specificity + 100;
            } else if (cCharAt == '#') {
                i2 = specificity + 10000;
            } else {
                i2 = specificity + 1;
                if (str.indexOf(46) != -1) {
                    i2 += 100;
                }
                if (str.indexOf(35) != -1) {
                    i2 += 10000;
                }
            }
            return i2;
        }
    }

    /* loaded from: rt.jar:javax/swing/text/html/StyleSheet$CssParser.class */
    class CssParser implements CSSParser.CSSParserCallback {
        String propertyName;
        boolean parsingDeclaration;
        boolean isLink;
        URL base;
        Vector<String[]> selectors = new Vector<>();
        Vector<String> selectorTokens = new Vector<>();
        MutableAttributeSet declaration = new SimpleAttributeSet();
        CSSParser parser = new CSSParser();

        CssParser() {
        }

        public AttributeSet parseDeclaration(String str) {
            try {
                return parseDeclaration(new StringReader(str));
            } catch (IOException e2) {
                return null;
            }
        }

        public AttributeSet parseDeclaration(Reader reader) throws IOException {
            parse(this.base, reader, true, false);
            return this.declaration.copyAttributes();
        }

        public void parse(URL url, Reader reader, boolean z2, boolean z3) throws IOException {
            this.base = url;
            this.isLink = z3;
            this.parsingDeclaration = z2;
            this.declaration.removeAttributes(this.declaration);
            this.selectorTokens.removeAllElements();
            this.selectors.removeAllElements();
            this.propertyName = null;
            this.parser.parse(reader, this, z2);
        }

        @Override // javax.swing.text.html.CSSParser.CSSParserCallback
        public void handleImport(String str) {
            URL url = CSS.getURL(this.base, str);
            if (url != null) {
                StyleSheet.this.importStyleSheet(url);
            }
        }

        @Override // javax.swing.text.html.CSSParser.CSSParserCallback
        public void handleSelector(String str) {
            if (!str.startsWith(".") && !str.startsWith(FXMLLoader.CONTROLLER_METHOD_PREFIX)) {
                str = str.toLowerCase();
            }
            int length = str.length();
            if (str.endsWith(",")) {
                if (length > 1) {
                    this.selectorTokens.addElement(str.substring(0, length - 1));
                }
                addSelector();
                return;
            }
            if (length > 0) {
                this.selectorTokens.addElement(str);
            }
        }

        @Override // javax.swing.text.html.CSSParser.CSSParserCallback
        public void startRule() {
            if (this.selectorTokens.size() > 0) {
                addSelector();
            }
            this.propertyName = null;
        }

        @Override // javax.swing.text.html.CSSParser.CSSParserCallback
        public void handleProperty(String str) {
            this.propertyName = str;
        }

        @Override // javax.swing.text.html.CSSParser.CSSParserCallback
        public void handleValue(String str) {
            URL url;
            if (this.propertyName != null && str != null && str.length() > 0) {
                CSS.Attribute attribute = CSS.getAttribute(this.propertyName);
                if (attribute != null) {
                    if (attribute == CSS.Attribute.LIST_STYLE_IMAGE && str != null && !str.equals(Separation.COLORANT_NONE) && (url = CSS.getURL(this.base, str)) != null) {
                        str = url.toString();
                    }
                    StyleSheet.this.addCSSAttribute(this.declaration, attribute, str);
                }
                this.propertyName = null;
            }
        }

        @Override // javax.swing.text.html.CSSParser.CSSParserCallback
        public void endRule() {
            int size = this.selectors.size();
            for (int i2 = 0; i2 < size; i2++) {
                String[] strArrElementAt = this.selectors.elementAt(i2);
                if (strArrElementAt.length > 0) {
                    StyleSheet.this.addRule(strArrElementAt, this.declaration, this.isLink);
                }
            }
            this.declaration.removeAttributes(this.declaration);
            this.selectors.removeAllElements();
        }

        private void addSelector() {
            String[] strArr = new String[this.selectorTokens.size()];
            this.selectorTokens.copyInto(strArr);
            this.selectors.addElement(strArr);
            this.selectorTokens.removeAllElements();
        }
    }

    void rebaseSizeMap(int i2) {
        this.sizeMap = new int[sizeMapDefault.length];
        for (int i3 = 0; i3 < sizeMapDefault.length; i3++) {
            this.sizeMap[i3] = Math.max((i2 * sizeMapDefault[i3]) / sizeMapDefault[CSS.baseFontSizeIndex], 4);
        }
    }

    int[] getSizeMap() {
        return this.sizeMap;
    }

    boolean isW3CLengthUnits() {
        return this.w3cLengthUnits;
    }
}
