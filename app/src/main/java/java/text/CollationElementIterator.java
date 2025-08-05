package java.text;

import com.sun.org.apache.xml.internal.dtm.DTMManager;
import java.util.Vector;
import sun.text.CollatorUtilities;
import sun.text.normalizer.NormalizerBase;
import sun.text.normalizer.NormalizerImpl;

/* loaded from: rt.jar:java/text/CollationElementIterator.class */
public final class CollationElementIterator {
    public static final int NULLORDER = -1;
    static final int UNMAPPEDCHARVALUE = 2147418112;
    private NormalizerBase text;
    private int[] buffer = null;
    private int expIndex = 0;
    private StringBuffer key = new StringBuffer(5);
    private int swapOrder = 0;
    private RBCollationTables ordering;
    private RuleBasedCollator owner;

    CollationElementIterator(String str, RuleBasedCollator ruleBasedCollator) {
        this.text = null;
        this.owner = ruleBasedCollator;
        this.ordering = ruleBasedCollator.getTables();
        if (str.length() != 0) {
            this.text = new NormalizerBase(str, CollatorUtilities.toNormalizerMode(ruleBasedCollator.getDecomposition()));
        }
    }

    CollationElementIterator(CharacterIterator characterIterator, RuleBasedCollator ruleBasedCollator) {
        this.text = null;
        this.owner = ruleBasedCollator;
        this.ordering = ruleBasedCollator.getTables();
        this.text = new NormalizerBase(characterIterator, CollatorUtilities.toNormalizerMode(ruleBasedCollator.getDecomposition()));
    }

    public void reset() {
        if (this.text != null) {
            this.text.reset();
            this.text.setMode(CollatorUtilities.toNormalizerMode(this.owner.getDecomposition()));
        }
        this.buffer = null;
        this.expIndex = 0;
        this.swapOrder = 0;
    }

    public int next() {
        if (this.text == null) {
            return -1;
        }
        NormalizerBase.Mode mode = this.text.getMode();
        NormalizerBase.Mode normalizerMode = CollatorUtilities.toNormalizerMode(this.owner.getDecomposition());
        if (mode != normalizerMode) {
            this.text.setMode(normalizerMode);
        }
        if (this.buffer != null) {
            if (this.expIndex < this.buffer.length) {
                int[] iArr = this.buffer;
                int i2 = this.expIndex;
                this.expIndex = i2 + 1;
                return strengthOrder(iArr[i2]);
            }
            this.buffer = null;
            this.expIndex = 0;
        } else if (this.swapOrder != 0) {
            if (Character.isSupplementaryCodePoint(this.swapOrder)) {
                char[] chars = Character.toChars(this.swapOrder);
                this.swapOrder = chars[1];
                return chars[0] << 16;
            }
            int i3 = this.swapOrder << 16;
            this.swapOrder = 0;
            return i3;
        }
        int next = this.text.next();
        if (next == -1) {
            return -1;
        }
        int unicodeOrder = this.ordering.getUnicodeOrder(next);
        if (unicodeOrder == -1) {
            this.swapOrder = next;
            return UNMAPPEDCHARVALUE;
        }
        if (unicodeOrder >= 2130706432) {
            unicodeOrder = nextContractChar(next);
        }
        if (unicodeOrder >= 2113929216) {
            this.buffer = this.ordering.getExpandValueList(unicodeOrder);
            this.expIndex = 0;
            int[] iArr2 = this.buffer;
            int i4 = this.expIndex;
            this.expIndex = i4 + 1;
            unicodeOrder = iArr2[i4];
        }
        if (this.ordering.isSEAsianSwapping()) {
            if (isThaiPreVowel(next)) {
                int next2 = this.text.next();
                if (isThaiBaseConsonant(next2)) {
                    this.buffer = makeReorderedBuffer(next2, unicodeOrder, this.buffer, true);
                    unicodeOrder = this.buffer[0];
                    this.expIndex = 1;
                } else if (next2 != -1) {
                    this.text.previous();
                }
            }
            if (isLaoPreVowel(next)) {
                int next3 = this.text.next();
                if (isLaoBaseConsonant(next3)) {
                    this.buffer = makeReorderedBuffer(next3, unicodeOrder, this.buffer, true);
                    unicodeOrder = this.buffer[0];
                    this.expIndex = 1;
                } else if (next3 != -1) {
                    this.text.previous();
                }
            }
        }
        return strengthOrder(unicodeOrder);
    }

    public int previous() {
        if (this.text == null) {
            return -1;
        }
        NormalizerBase.Mode mode = this.text.getMode();
        NormalizerBase.Mode normalizerMode = CollatorUtilities.toNormalizerMode(this.owner.getDecomposition());
        if (mode != normalizerMode) {
            this.text.setMode(normalizerMode);
        }
        if (this.buffer != null) {
            if (this.expIndex > 0) {
                int[] iArr = this.buffer;
                int i2 = this.expIndex - 1;
                this.expIndex = i2;
                return strengthOrder(iArr[i2]);
            }
            this.buffer = null;
            this.expIndex = 0;
        } else if (this.swapOrder != 0) {
            if (Character.isSupplementaryCodePoint(this.swapOrder)) {
                char[] chars = Character.toChars(this.swapOrder);
                this.swapOrder = chars[1];
                return chars[0] << 16;
            }
            int i3 = this.swapOrder << 16;
            this.swapOrder = 0;
            return i3;
        }
        int iPrevious = this.text.previous();
        if (iPrevious == -1) {
            return -1;
        }
        int unicodeOrder = this.ordering.getUnicodeOrder(iPrevious);
        if (unicodeOrder == -1) {
            this.swapOrder = UNMAPPEDCHARVALUE;
            return iPrevious;
        }
        if (unicodeOrder >= 2130706432) {
            unicodeOrder = prevContractChar(iPrevious);
        }
        if (unicodeOrder >= 2113929216) {
            this.buffer = this.ordering.getExpandValueList(unicodeOrder);
            this.expIndex = this.buffer.length;
            int[] iArr2 = this.buffer;
            int i4 = this.expIndex - 1;
            this.expIndex = i4;
            unicodeOrder = iArr2[i4];
        }
        if (this.ordering.isSEAsianSwapping()) {
            if (isThaiBaseConsonant(iPrevious)) {
                int iPrevious2 = this.text.previous();
                if (isThaiPreVowel(iPrevious2)) {
                    this.buffer = makeReorderedBuffer(iPrevious2, unicodeOrder, this.buffer, false);
                    this.expIndex = this.buffer.length - 1;
                    unicodeOrder = this.buffer[this.expIndex];
                } else {
                    this.text.next();
                }
            }
            if (isLaoBaseConsonant(iPrevious)) {
                int iPrevious3 = this.text.previous();
                if (isLaoPreVowel(iPrevious3)) {
                    this.buffer = makeReorderedBuffer(iPrevious3, unicodeOrder, this.buffer, false);
                    this.expIndex = this.buffer.length - 1;
                    unicodeOrder = this.buffer[this.expIndex];
                } else {
                    this.text.next();
                }
            }
        }
        return strengthOrder(unicodeOrder);
    }

    public static final int primaryOrder(int i2) {
        return (i2 & DTMManager.IDENT_DTM_DEFAULT) >>> 16;
    }

    public static final short secondaryOrder(int i2) {
        return (short) ((i2 & NormalizerImpl.CC_MASK) >> 8);
    }

    public static final short tertiaryOrder(int i2) {
        return (short) (i2 & 255);
    }

    final int strengthOrder(int i2) {
        int strength = this.owner.getStrength();
        if (strength == 0) {
            i2 &= DTMManager.IDENT_DTM_DEFAULT;
        } else if (strength == 1) {
            i2 &= -256;
        }
        return i2;
    }

    public void setOffset(int i2) {
        if (this.text != null) {
            if (i2 < this.text.getBeginIndex() || i2 >= this.text.getEndIndex()) {
                this.text.setIndexOnly(i2);
            } else {
                int index = this.text.setIndex(i2);
                if (this.ordering.usedInContractSeq(index)) {
                    while (this.ordering.usedInContractSeq(index)) {
                        index = this.text.previous();
                    }
                    int index2 = this.text.getIndex();
                    while (this.text.getIndex() <= i2) {
                        index2 = this.text.getIndex();
                        next();
                    }
                    this.text.setIndexOnly(index2);
                }
            }
        }
        this.buffer = null;
        this.expIndex = 0;
        this.swapOrder = 0;
    }

    public int getOffset() {
        if (this.text != null) {
            return this.text.getIndex();
        }
        return 0;
    }

    public int getMaxExpansion(int i2) {
        return this.ordering.getMaxExpansion(i2);
    }

    public void setText(String str) {
        this.buffer = null;
        this.swapOrder = 0;
        this.expIndex = 0;
        NormalizerBase.Mode normalizerMode = CollatorUtilities.toNormalizerMode(this.owner.getDecomposition());
        if (this.text == null) {
            this.text = new NormalizerBase(str, normalizerMode);
        } else {
            this.text.setMode(normalizerMode);
            this.text.setText(str);
        }
    }

    public void setText(CharacterIterator characterIterator) {
        this.buffer = null;
        this.swapOrder = 0;
        this.expIndex = 0;
        NormalizerBase.Mode normalizerMode = CollatorUtilities.toNormalizerMode(this.owner.getDecomposition());
        if (this.text == null) {
            this.text = new NormalizerBase(characterIterator, normalizerMode);
        } else {
            this.text.setMode(normalizerMode);
            this.text.setText(characterIterator);
        }
    }

    private static final boolean isThaiPreVowel(int i2) {
        return i2 >= 3648 && i2 <= 3652;
    }

    private static final boolean isThaiBaseConsonant(int i2) {
        return i2 >= 3585 && i2 <= 3630;
    }

    private static final boolean isLaoPreVowel(int i2) {
        return i2 >= 3776 && i2 <= 3780;
    }

    private static final boolean isLaoBaseConsonant(int i2) {
        return i2 >= 3713 && i2 <= 3758;
    }

    private int[] makeReorderedBuffer(int i2, int i3, int[] iArr, boolean z2) {
        int[] iArr2;
        int unicodeOrder = this.ordering.getUnicodeOrder(i2);
        if (unicodeOrder >= 2130706432) {
            unicodeOrder = z2 ? nextContractChar(i2) : prevContractChar(i2);
        }
        int[] expandValueList = null;
        if (unicodeOrder >= 2113929216) {
            expandValueList = this.ordering.getExpandValueList(unicodeOrder);
        }
        if (!z2) {
            int i4 = unicodeOrder;
            unicodeOrder = i3;
            i3 = i4;
            int[] iArr3 = expandValueList;
            expandValueList = iArr;
            iArr = iArr3;
        }
        if (expandValueList == null && iArr == null) {
            iArr2 = new int[]{unicodeOrder, i3};
        } else {
            int length = expandValueList == null ? 1 : expandValueList.length;
            int length2 = iArr == null ? 1 : iArr.length;
            iArr2 = new int[length + length2];
            if (expandValueList == null) {
                iArr2[0] = unicodeOrder;
            } else {
                System.arraycopy(expandValueList, 0, iArr2, 0, length);
            }
            if (iArr == null) {
                iArr2[length] = i3;
            } else {
                System.arraycopy(iArr, 0, iArr2, length, length2);
            }
        }
        return iArr2;
    }

    static final boolean isIgnorable(int i2) {
        return primaryOrder(i2) == 0;
    }

    private int nextContractChar(int i2) {
        Vector<EntryPair> contractValues = this.ordering.getContractValues(i2);
        int i3 = contractValues.firstElement().value;
        int length = contractValues.lastElement().entryName.length();
        NormalizerBase normalizerBase = (NormalizerBase) this.text.clone();
        normalizerBase.previous();
        this.key.setLength(0);
        int next = normalizerBase.next();
        while (true) {
            int i4 = next;
            if (length <= 0 || i4 == -1) {
                break;
            }
            if (Character.isSupplementaryCodePoint(i4)) {
                this.key.append(Character.toChars(i4));
                length -= 2;
            } else {
                this.key.append((char) i4);
                length--;
            }
            next = normalizerBase.next();
        }
        String string = this.key.toString();
        int iCharCount = 1;
        for (int size = contractValues.size() - 1; size > 0; size--) {
            EntryPair entryPairElementAt = contractValues.elementAt(size);
            if (entryPairElementAt.fwd && string.startsWith(entryPairElementAt.entryName) && entryPairElementAt.entryName.length() > iCharCount) {
                iCharCount = entryPairElementAt.entryName.length();
                i3 = entryPairElementAt.value;
            }
        }
        while (iCharCount > 1) {
            iCharCount -= Character.charCount(this.text.next());
        }
        return i3;
    }

    private int prevContractChar(int i2) {
        Vector<EntryPair> contractValues = this.ordering.getContractValues(i2);
        int i3 = contractValues.firstElement().value;
        int length = contractValues.lastElement().entryName.length();
        NormalizerBase normalizerBase = (NormalizerBase) this.text.clone();
        normalizerBase.next();
        this.key.setLength(0);
        int iPrevious = normalizerBase.previous();
        while (true) {
            int i4 = iPrevious;
            if (length <= 0 || i4 == -1) {
                break;
            }
            if (Character.isSupplementaryCodePoint(i4)) {
                this.key.append(Character.toChars(i4));
                length -= 2;
            } else {
                this.key.append((char) i4);
                length--;
            }
            iPrevious = normalizerBase.previous();
        }
        String string = this.key.toString();
        int iCharCount = 1;
        for (int size = contractValues.size() - 1; size > 0; size--) {
            EntryPair entryPairElementAt = contractValues.elementAt(size);
            if (!entryPairElementAt.fwd && string.startsWith(entryPairElementAt.entryName) && entryPairElementAt.entryName.length() > iCharCount) {
                iCharCount = entryPairElementAt.entryName.length();
                i3 = entryPairElementAt.value;
            }
        }
        while (iCharCount > 1) {
            iCharCount -= Character.charCount(this.text.previous());
        }
        return i3;
    }
}
