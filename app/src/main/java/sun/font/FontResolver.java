package sun.font;

import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.text.AttributedCharacterIterator;
import java.util.ArrayList;
import java.util.Map;
import sun.text.CodePointIterator;

/* loaded from: rt.jar:sun/font/FontResolver.class */
public final class FontResolver {
    private Font[] allFonts;
    private Font[] supplementaryFonts;
    private int[] supplementaryIndices;
    private static final int DEFAULT_SIZE = 12;
    private static final int SHIFT = 9;
    private static final int BLOCKSIZE = 128;
    private static final int MASK = 127;
    private static FontResolver INSTANCE;
    private Font defaultFont = new Font(Font.DIALOG, 0, 12);
    private int[][] blocks = new int[512];

    /* JADX WARN: Type inference failed for: r1v2, types: [int[], int[][]] */
    private FontResolver() {
    }

    private Font[] getAllFonts() {
        if (this.allFonts == null) {
            this.allFonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAllFonts();
            for (int i2 = 0; i2 < this.allFonts.length; i2++) {
                this.allFonts[i2] = this.allFonts[i2].deriveFont(12.0f);
            }
        }
        return this.allFonts;
    }

    private int getIndexFor(char c2) {
        if (this.defaultFont.canDisplay(c2)) {
            return 1;
        }
        for (int i2 = 0; i2 < getAllFonts().length; i2++) {
            if (this.allFonts[i2].canDisplay(c2)) {
                return i2 + 2;
            }
        }
        return 1;
    }

    private Font[] getAllSCFonts() {
        if (this.supplementaryFonts == null) {
            ArrayList arrayList = new ArrayList();
            ArrayList arrayList2 = new ArrayList();
            for (int i2 = 0; i2 < getAllFonts().length; i2++) {
                Font font = this.allFonts[i2];
                if (FontUtilities.getFont2D(font).hasSupplementaryChars()) {
                    arrayList.add(font);
                    arrayList2.add(Integer.valueOf(i2));
                }
            }
            int size = arrayList.size();
            this.supplementaryIndices = new int[size];
            for (int i3 = 0; i3 < size; i3++) {
                this.supplementaryIndices[i3] = ((Integer) arrayList2.get(i3)).intValue();
            }
            this.supplementaryFonts = (Font[]) arrayList.toArray(new Font[size]);
        }
        return this.supplementaryFonts;
    }

    private int getIndexFor(int i2) {
        if (this.defaultFont.canDisplay(i2)) {
            return 1;
        }
        for (int i3 = 0; i3 < getAllSCFonts().length; i3++) {
            if (this.supplementaryFonts[i3].canDisplay(i2)) {
                return this.supplementaryIndices[i3] + 2;
            }
        }
        return 1;
    }

    public int getFontIndex(char c2) {
        int i2 = c2 >> '\t';
        int[] iArr = this.blocks[i2];
        if (iArr == null) {
            iArr = new int[128];
            this.blocks[i2] = iArr;
        }
        int i3 = c2 & 127;
        if (iArr[i3] == 0) {
            iArr[i3] = getIndexFor(c2);
        }
        return iArr[i3];
    }

    public int getFontIndex(int i2) {
        if (i2 < 65536) {
            return getFontIndex((char) i2);
        }
        return getIndexFor(i2);
    }

    public int nextFontRunIndex(CodePointIterator codePointIterator) {
        int next = codePointIterator.next();
        int fontIndex = 1;
        if (next != -1) {
            fontIndex = getFontIndex(next);
            while (true) {
                int next2 = codePointIterator.next();
                if (next2 == -1) {
                    break;
                }
                if (getFontIndex(next2) != fontIndex) {
                    codePointIterator.prev();
                    break;
                }
            }
        }
        return fontIndex;
    }

    public Font getFont(int i2, Map map) {
        Font font = this.defaultFont;
        if (i2 >= 2) {
            font = this.allFonts[i2 - 2];
        }
        return font.deriveFont((Map<? extends AttributedCharacterIterator.Attribute, ?>) map);
    }

    public static FontResolver getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new FontResolver();
        }
        return INSTANCE;
    }
}
