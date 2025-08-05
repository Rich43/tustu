package java.text;

import com.sun.org.apache.xml.internal.dtm.DTMManager;
import java.text.RBCollationTables;
import java.util.Vector;
import sun.text.ComposedCharIter;
import sun.text.IntHashtable;
import sun.text.UCompactIntArray;
import sun.text.normalizer.NormalizerImpl;

/* loaded from: rt.jar:java/text/RBTableBuilder.class */
final class RBTableBuilder {
    static final int CHARINDEX = 1879048192;
    private static final int IGNORABLEMASK = 65535;
    private static final int PRIMARYORDERINCREMENT = 65536;
    private static final int SECONDARYORDERINCREMENT = 256;
    private static final int TERTIARYORDERINCREMENT = 1;
    private static final int INITIALTABLESIZE = 20;
    private static final int MAXKEYSIZE = 5;
    private RBCollationTables.BuildAPI tables;
    private MergeCollation mPattern = null;
    private boolean isOverIgnore = false;
    private char[] keyBuf = new char[5];
    private IntHashtable contractFlags = new IntHashtable(100);
    private boolean frenchSec = false;
    private boolean seAsianSwapping = false;
    private UCompactIntArray mapping = null;
    private Vector<Vector<EntryPair>> contractTable = null;
    private Vector<int[]> expandTable = null;
    private short maxSecOrder = 0;
    private short maxTerOrder = 0;

    public RBTableBuilder(RBCollationTables.BuildAPI buildAPI) {
        this.tables = null;
        this.tables = buildAPI;
    }

    public void build(String str, int i2) throws ParseException {
        if (str.length() == 0) {
            throw new ParseException("Build rules empty.", 0);
        }
        this.mapping = new UCompactIntArray(-1);
        this.mPattern = new MergeCollation(NormalizerImpl.canonicalDecomposeWithSingleQuotation(str));
        int iIncrement = 0;
        for (int i3 = 0; i3 < this.mPattern.getCount(); i3++) {
            PatternEntry itemAt = this.mPattern.getItemAt(i3);
            if (itemAt != null) {
                String chars = itemAt.getChars();
                if (chars.length() > 1) {
                    switch (chars.charAt(chars.length() - 1)) {
                        case '!':
                            this.seAsianSwapping = true;
                            chars = chars.substring(0, chars.length() - 1);
                            break;
                        case '@':
                            this.frenchSec = true;
                            chars = chars.substring(0, chars.length() - 1);
                            break;
                    }
                }
                iIncrement = increment(itemAt.getStrength(), iIncrement);
                String extension = itemAt.getExtension();
                if (extension.length() != 0) {
                    addExpandOrder(chars, extension, iIncrement);
                } else if (chars.length() > 1) {
                    char cCharAt = chars.charAt(0);
                    if (Character.isHighSurrogate(cCharAt) && chars.length() == 2) {
                        addOrder(Character.toCodePoint(cCharAt, chars.charAt(1)), iIncrement);
                    } else {
                        addContractOrder(chars, iIncrement);
                    }
                } else {
                    addOrder(chars.charAt(0), iIncrement);
                }
            }
        }
        addComposedChars();
        commit();
        this.mapping.compact();
        this.tables.fillInTables(this.frenchSec, this.seAsianSwapping, this.mapping, this.contractTable, this.expandTable, this.contractFlags, this.maxSecOrder, this.maxTerOrder);
    }

    private void addComposedChars() throws ParseException {
        ComposedCharIter composedCharIter = new ComposedCharIter();
        while (true) {
            int next = composedCharIter.next();
            if (next != -1) {
                if (getCharOrder(next) == -1) {
                    String strDecomposition = composedCharIter.decomposition();
                    if (strDecomposition.length() == 1) {
                        int charOrder = getCharOrder(strDecomposition.charAt(0));
                        if (charOrder != -1) {
                            addOrder(next, charOrder);
                        }
                    } else if (strDecomposition.length() == 2 && Character.isHighSurrogate(strDecomposition.charAt(0))) {
                        int charOrder2 = getCharOrder(strDecomposition.codePointAt(0));
                        if (charOrder2 != -1) {
                            addOrder(next, charOrder2);
                        }
                    } else {
                        int contractOrder = getContractOrder(strDecomposition);
                        if (contractOrder != -1) {
                            addOrder(next, contractOrder);
                        } else {
                            boolean z2 = true;
                            int i2 = 0;
                            while (true) {
                                if (i2 >= strDecomposition.length()) {
                                    break;
                                }
                                if (getCharOrder(strDecomposition.charAt(i2)) != -1) {
                                    i2++;
                                } else {
                                    z2 = false;
                                    break;
                                }
                            }
                            if (z2) {
                                addExpandOrder(next, strDecomposition, -1);
                            }
                        }
                    }
                }
            } else {
                return;
            }
        }
    }

    private final void commit() {
        if (this.expandTable != null) {
            for (int i2 = 0; i2 < this.expandTable.size(); i2++) {
                int[] iArrElementAt = this.expandTable.elementAt(i2);
                for (int i3 = 0; i3 < iArrElementAt.length; i3++) {
                    int i4 = iArrElementAt[i3];
                    if (i4 < 2113929216 && i4 > CHARINDEX) {
                        int i5 = i4 - CHARINDEX;
                        int charOrder = getCharOrder(i5);
                        if (charOrder == -1) {
                            iArrElementAt[i3] = 65535 & i5;
                        } else {
                            iArrElementAt[i3] = charOrder;
                        }
                    }
                }
            }
        }
    }

    private final int increment(int i2, int i3) {
        switch (i2) {
            case 0:
                i3 = (i3 + 65536) & DTMManager.IDENT_DTM_DEFAULT;
                this.isOverIgnore = true;
                break;
            case 1:
                i3 = (i3 + 256) & (-256);
                if (!this.isOverIgnore) {
                    this.maxSecOrder = (short) (this.maxSecOrder + 1);
                    break;
                }
                break;
            case 2:
                i3++;
                if (!this.isOverIgnore) {
                    this.maxTerOrder = (short) (this.maxTerOrder + 1);
                    break;
                }
                break;
        }
        return i3;
    }

    private final void addOrder(int i2, int i3) {
        if (this.mapping.elementAt(i2) >= 2130706432) {
            int chars = 1;
            if (Character.isSupplementaryCodePoint(i2)) {
                chars = Character.toChars(i2, this.keyBuf, 0);
            } else {
                this.keyBuf[0] = (char) i2;
            }
            addContractOrder(new String(this.keyBuf, 0, chars), i3);
            return;
        }
        this.mapping.setElementAt(i2, i3);
    }

    private final void addContractOrder(String str, int i2) {
        addContractOrder(str, i2, true);
    }

    private final void addContractOrder(String str, int i2, boolean z2) {
        if (this.contractTable == null) {
            this.contractTable = new Vector<>(20);
        }
        int iCodePointAt = str.codePointAt(0);
        int iElementAt = this.mapping.elementAt(iCodePointAt);
        Vector<EntryPair> contractValuesImpl = getContractValuesImpl(iElementAt - 2130706432);
        if (contractValuesImpl == null) {
            int size = 2130706432 + this.contractTable.size();
            contractValuesImpl = new Vector<>(20);
            this.contractTable.addElement(contractValuesImpl);
            contractValuesImpl.addElement(new EntryPair(str.substring(0, Character.charCount(iCodePointAt)), iElementAt));
            this.mapping.setElementAt(iCodePointAt, size);
        }
        int entry = RBCollationTables.getEntry(contractValuesImpl, str, z2);
        if (entry != -1) {
            contractValuesImpl.elementAt(entry).value = i2;
        } else {
            if (str.length() > contractValuesImpl.lastElement().entryName.length()) {
                contractValuesImpl.addElement(new EntryPair(str, i2, z2));
            } else {
                contractValuesImpl.insertElementAt(new EntryPair(str, i2, z2), contractValuesImpl.size() - 1);
            }
        }
        if (z2 && str.length() > 1) {
            addContractFlags(str);
            addContractOrder(new StringBuffer(str).reverse().toString(), i2, false);
        }
    }

    private int getContractOrder(String str) {
        Vector<EntryPair> contractValues;
        int entry;
        int i2 = -1;
        if (this.contractTable != null && (contractValues = getContractValues(str.codePointAt(0))) != null && (entry = RBCollationTables.getEntry(contractValues, str, true)) != -1) {
            i2 = contractValues.elementAt(entry).value;
        }
        return i2;
    }

    private final int getCharOrder(int i2) {
        int iElementAt = this.mapping.elementAt(i2);
        if (iElementAt >= 2130706432) {
            iElementAt = getContractValuesImpl(iElementAt - 2130706432).firstElement().value;
        }
        return iElementAt;
    }

    private Vector<EntryPair> getContractValues(int i2) {
        return getContractValuesImpl(this.mapping.elementAt(i2) - 2130706432);
    }

    private Vector<EntryPair> getContractValuesImpl(int i2) {
        if (i2 >= 0) {
            return this.contractTable.elementAt(i2);
        }
        return null;
    }

    private final void addExpandOrder(String str, String str2, int i2) throws ParseException {
        int iAddExpansion = addExpansion(i2, str2);
        if (str.length() > 1) {
            char cCharAt = str.charAt(0);
            if (Character.isHighSurrogate(cCharAt) && str.length() == 2) {
                char cCharAt2 = str.charAt(1);
                if (Character.isLowSurrogate(cCharAt2)) {
                    addOrder(Character.toCodePoint(cCharAt, cCharAt2), iAddExpansion);
                    return;
                }
                return;
            }
            addContractOrder(str, iAddExpansion);
            return;
        }
        addOrder(str.charAt(0), iAddExpansion);
    }

    private final void addExpandOrder(int i2, String str, int i3) throws ParseException {
        addOrder(i2, addExpansion(i3, str));
    }

    private int addExpansion(int i2, String str) {
        int codePoint;
        if (this.expandTable == null) {
            this.expandTable = new Vector<>(20);
        }
        int i3 = i2 == -1 ? 0 : 1;
        int[] iArr = new int[str.length() + i3];
        if (i3 == 1) {
            iArr[0] = i2;
        }
        int i4 = i3;
        int i5 = 0;
        while (i5 < str.length()) {
            char cCharAt = str.charAt(i5);
            if (Character.isHighSurrogate(cCharAt)) {
                i5++;
                if (i5 == str.length()) {
                    break;
                }
                char cCharAt2 = str.charAt(i5);
                if (!Character.isLowSurrogate(cCharAt2)) {
                    break;
                }
                codePoint = Character.toCodePoint(cCharAt, cCharAt2);
            } else {
                codePoint = cCharAt;
            }
            int charOrder = getCharOrder(codePoint);
            if (charOrder != -1) {
                int i6 = i4;
                i4++;
                iArr[i6] = charOrder;
            } else {
                int i7 = i4;
                i4++;
                iArr[i7] = CHARINDEX + codePoint;
            }
            i5++;
        }
        if (i4 < iArr.length) {
            int[] iArr2 = new int[i4];
            while (true) {
                i4--;
                if (i4 < 0) {
                    break;
                }
                iArr2[i4] = iArr[i4];
            }
            iArr = iArr2;
        }
        int size = 2113929216 + this.expandTable.size();
        this.expandTable.addElement(iArr);
        return size;
    }

    private void addContractFlags(String str) {
        int codePoint;
        int length = str.length();
        int i2 = 0;
        while (i2 < length) {
            char cCharAt = str.charAt(i2);
            if (Character.isHighSurrogate(cCharAt)) {
                i2++;
                codePoint = Character.toCodePoint(cCharAt, str.charAt(i2));
            } else {
                codePoint = cCharAt;
            }
            this.contractFlags.put(codePoint, 1);
            i2++;
        }
    }
}
