package java.awt.font;

import java.awt.Font;
import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.im.InputMethodHighlight;
import java.text.Annotation;
import java.text.AttributedCharacterIterator;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import sun.font.Decoration;
import sun.font.FontResolver;
import sun.text.CodePointIterator;

/* loaded from: rt.jar:java/awt/font/StyledParagraph.class */
final class StyledParagraph {
    private int length;
    private Decoration decoration;
    private Object font;
    private Vector<Decoration> decorations;
    int[] decorationStarts;
    private Vector<Object> fonts;
    int[] fontStarts;
    private static int INITIAL_SIZE = 8;

    public StyledParagraph(AttributedCharacterIterator attributedCharacterIterator, char[] cArr) throws HeadlessException {
        int beginIndex = attributedCharacterIterator.getBeginIndex();
        int endIndex = attributedCharacterIterator.getEndIndex();
        this.length = endIndex - beginIndex;
        int i2 = beginIndex;
        attributedCharacterIterator.first();
        do {
            int runLimit = attributedCharacterIterator.getRunLimit();
            int i3 = i2 - beginIndex;
            Map<? extends AttributedCharacterIterator.Attribute, ?> mapAddInputMethodAttrs = addInputMethodAttrs(attributedCharacterIterator.getAttributes());
            addDecoration(Decoration.getDecoration(mapAddInputMethodAttrs), i3);
            Object graphicOrFont = getGraphicOrFont(mapAddInputMethodAttrs);
            if (graphicOrFont == null) {
                addFonts(cArr, mapAddInputMethodAttrs, i3, runLimit - beginIndex);
            } else {
                addFont(graphicOrFont, i3);
            }
            attributedCharacterIterator.setIndex(runLimit);
            i2 = runLimit;
        } while (i2 < endIndex);
        if (this.decorations != null) {
            this.decorationStarts = addToVector(this, this.length, this.decorations, this.decorationStarts);
        }
        if (this.fonts != null) {
            this.fontStarts = addToVector(this, this.length, this.fonts, this.fontStarts);
        }
    }

    private static void insertInto(int i2, int[] iArr, int i3) {
        while (true) {
            i3--;
            if (iArr[i3] > i2) {
                iArr[i3] = iArr[i3] + 1;
            } else {
                return;
            }
        }
    }

    public static StyledParagraph insertChar(AttributedCharacterIterator attributedCharacterIterator, char[] cArr, int i2, StyledParagraph styledParagraph) throws HeadlessException {
        char index = attributedCharacterIterator.setIndex(i2);
        int iMax = Math.max((i2 - attributedCharacterIterator.getBeginIndex()) - 1, 0);
        Map<? extends AttributedCharacterIterator.Attribute, ?> mapAddInputMethodAttrs = addInputMethodAttrs(attributedCharacterIterator.getAttributes());
        if (!styledParagraph.getDecorationAt(iMax).equals(Decoration.getDecoration(mapAddInputMethodAttrs))) {
            return new StyledParagraph(attributedCharacterIterator, cArr);
        }
        Object graphicOrFont = getGraphicOrFont(mapAddInputMethodAttrs);
        if (graphicOrFont == null) {
            FontResolver fontResolver = FontResolver.getInstance();
            graphicOrFont = fontResolver.getFont(fontResolver.getFontIndex(index), mapAddInputMethodAttrs);
        }
        if (!styledParagraph.getFontOrGraphicAt(iMax).equals(graphicOrFont)) {
            return new StyledParagraph(attributedCharacterIterator, cArr);
        }
        styledParagraph.length++;
        if (styledParagraph.decorations != null) {
            insertInto(iMax, styledParagraph.decorationStarts, styledParagraph.decorations.size());
        }
        if (styledParagraph.fonts != null) {
            insertInto(iMax, styledParagraph.fontStarts, styledParagraph.fonts.size());
        }
        return styledParagraph;
    }

    private static void deleteFrom(int i2, int[] iArr, int i3) {
        while (true) {
            i3--;
            if (iArr[i3] > i2) {
                iArr[i3] = iArr[i3] - 1;
            } else {
                return;
            }
        }
    }

    public static StyledParagraph deleteChar(AttributedCharacterIterator attributedCharacterIterator, char[] cArr, int i2, StyledParagraph styledParagraph) {
        int beginIndex = i2 - attributedCharacterIterator.getBeginIndex();
        if (styledParagraph.decorations == null && styledParagraph.fonts == null) {
            styledParagraph.length--;
            return styledParagraph;
        }
        if (styledParagraph.getRunLimit(beginIndex) == beginIndex + 1 && (beginIndex == 0 || styledParagraph.getRunLimit(beginIndex - 1) == beginIndex)) {
            return new StyledParagraph(attributedCharacterIterator, cArr);
        }
        styledParagraph.length--;
        if (styledParagraph.decorations != null) {
            deleteFrom(beginIndex, styledParagraph.decorationStarts, styledParagraph.decorations.size());
        }
        if (styledParagraph.fonts != null) {
            deleteFrom(beginIndex, styledParagraph.fontStarts, styledParagraph.fonts.size());
        }
        return styledParagraph;
    }

    public int getRunLimit(int i2) {
        if (i2 < 0 || i2 >= this.length) {
            throw new IllegalArgumentException("index out of range");
        }
        int i3 = this.length;
        if (this.decorations != null) {
            i3 = this.decorationStarts[findRunContaining(i2, this.decorationStarts) + 1];
        }
        int i4 = this.length;
        if (this.fonts != null) {
            i4 = this.fontStarts[findRunContaining(i2, this.fontStarts) + 1];
        }
        return Math.min(i3, i4);
    }

    public Decoration getDecorationAt(int i2) {
        if (i2 < 0 || i2 >= this.length) {
            throw new IllegalArgumentException("index out of range");
        }
        if (this.decorations == null) {
            return this.decoration;
        }
        return this.decorations.elementAt(findRunContaining(i2, this.decorationStarts));
    }

    public Object getFontOrGraphicAt(int i2) {
        if (i2 < 0 || i2 >= this.length) {
            throw new IllegalArgumentException("index out of range");
        }
        if (this.fonts == null) {
            return this.font;
        }
        return this.fonts.elementAt(findRunContaining(i2, this.fontStarts));
    }

    private static int findRunContaining(int i2, int[] iArr) {
        int i3 = 1;
        while (iArr[i3] <= i2) {
            i3++;
        }
        return i3 - 1;
    }

    private static int[] addToVector(Object obj, int i2, Vector vector, int[] iArr) {
        if (!vector.lastElement().equals(obj)) {
            vector.addElement(obj);
            int size = vector.size();
            if (iArr.length == size) {
                int[] iArr2 = new int[iArr.length * 2];
                System.arraycopy(iArr, 0, iArr2, 0, iArr.length);
                iArr = iArr2;
            }
            iArr[size - 1] = i2;
        }
        return iArr;
    }

    private void addDecoration(Decoration decoration, int i2) {
        if (this.decorations != null) {
            this.decorationStarts = addToVector(decoration, i2, this.decorations, this.decorationStarts);
            return;
        }
        if (this.decoration == null) {
            this.decoration = decoration;
            return;
        }
        if (!this.decoration.equals(decoration)) {
            this.decorations = new Vector<>(INITIAL_SIZE);
            this.decorations.addElement(this.decoration);
            this.decorations.addElement(decoration);
            this.decorationStarts = new int[INITIAL_SIZE];
            this.decorationStarts[0] = 0;
            this.decorationStarts[1] = i2;
        }
    }

    private void addFont(Object obj, int i2) {
        if (this.fonts != null) {
            this.fontStarts = addToVector(obj, i2, this.fonts, this.fontStarts);
            return;
        }
        if (this.font == null) {
            this.font = obj;
            return;
        }
        if (!this.font.equals(obj)) {
            this.fonts = new Vector<>(INITIAL_SIZE);
            this.fonts.addElement(this.font);
            this.fonts.addElement(obj);
            this.fontStarts = new int[INITIAL_SIZE];
            this.fontStarts[0] = 0;
            this.fontStarts[1] = i2;
        }
    }

    private void addFonts(char[] cArr, Map<? extends AttributedCharacterIterator.Attribute, ?> map, int i2, int i3) {
        FontResolver fontResolver = FontResolver.getInstance();
        CodePointIterator codePointIteratorCreate = CodePointIterator.create(cArr, i2, i3);
        int iCharIndex = codePointIteratorCreate.charIndex();
        while (true) {
            int i4 = iCharIndex;
            if (i4 < i3) {
                addFont(fontResolver.getFont(fontResolver.nextFontRunIndex(codePointIteratorCreate), map), i4);
                iCharIndex = codePointIteratorCreate.charIndex();
            } else {
                return;
            }
        }
    }

    static Map<? extends AttributedCharacterIterator.Attribute, ?> addInputMethodAttrs(Map<? extends AttributedCharacterIterator.Attribute, ?> map) throws HeadlessException {
        Object value = map.get(TextAttribute.INPUT_METHOD_HIGHLIGHT);
        if (value != null) {
            try {
                if (value instanceof Annotation) {
                    value = ((Annotation) value).getValue();
                }
                InputMethodHighlight inputMethodHighlight = (InputMethodHighlight) value;
                Map<TextAttribute, ?> mapMapInputMethodHighlight = null;
                try {
                    mapMapInputMethodHighlight = inputMethodHighlight.getStyle();
                } catch (NoSuchMethodError e2) {
                }
                if (mapMapInputMethodHighlight == null) {
                    mapMapInputMethodHighlight = Toolkit.getDefaultToolkit().mapInputMethodHighlight(inputMethodHighlight);
                }
                if (mapMapInputMethodHighlight != null) {
                    HashMap map2 = new HashMap(5, 0.9f);
                    map2.putAll(map);
                    map2.putAll(mapMapInputMethodHighlight);
                    return map2;
                }
            } catch (ClassCastException e3) {
            }
        }
        return map;
    }

    private static Object getGraphicOrFont(Map<? extends AttributedCharacterIterator.Attribute, ?> map) {
        Object obj = map.get(TextAttribute.CHAR_REPLACEMENT);
        if (obj != null) {
            return obj;
        }
        Object obj2 = map.get(TextAttribute.FONT);
        if (obj2 != null) {
            return obj2;
        }
        if (map.get(TextAttribute.FAMILY) != null) {
            return Font.getFont(map);
        }
        return null;
    }
}
