package java.text;

import java.util.Vector;
import sun.text.IntHashtable;
import sun.text.UCompactIntArray;

/* loaded from: rt.jar:java/text/RBCollationTables.class */
final class RBCollationTables {
    static final int EXPANDCHARINDEX = 2113929216;
    static final int CONTRACTCHARINDEX = 2130706432;
    static final int UNMAPPED = -1;
    static final int PRIMARYORDERMASK = -65536;
    static final int SECONDARYORDERMASK = 65280;
    static final int TERTIARYORDERMASK = 255;
    static final int PRIMARYDIFFERENCEONLY = -65536;
    static final int SECONDARYDIFFERENCEONLY = -256;
    static final int PRIMARYORDERSHIFT = 16;
    static final int SECONDARYORDERSHIFT = 8;
    private String rules;
    private boolean frenchSec = false;
    private boolean seAsianSwapping = false;
    private UCompactIntArray mapping = null;
    private Vector<Vector<EntryPair>> contractTable = null;
    private Vector<int[]> expandTable = null;
    private IntHashtable contractFlags = null;
    private short maxSecOrder = 0;
    private short maxTerOrder = 0;

    public RBCollationTables(String str, int i2) throws ParseException {
        this.rules = null;
        this.rules = str;
        new RBTableBuilder(new BuildAPI()).build(str, i2);
    }

    /* loaded from: rt.jar:java/text/RBCollationTables$BuildAPI.class */
    final class BuildAPI {
        private BuildAPI() {
        }

        void fillInTables(boolean z2, boolean z3, UCompactIntArray uCompactIntArray, Vector<Vector<EntryPair>> vector, Vector<int[]> vector2, IntHashtable intHashtable, short s2, short s3) {
            RBCollationTables.this.frenchSec = z2;
            RBCollationTables.this.seAsianSwapping = z3;
            RBCollationTables.this.mapping = uCompactIntArray;
            RBCollationTables.this.contractTable = vector;
            RBCollationTables.this.expandTable = vector2;
            RBCollationTables.this.contractFlags = intHashtable;
            RBCollationTables.this.maxSecOrder = s2;
            RBCollationTables.this.maxTerOrder = s3;
        }
    }

    public String getRules() {
        return this.rules;
    }

    public boolean isFrenchSec() {
        return this.frenchSec;
    }

    public boolean isSEAsianSwapping() {
        return this.seAsianSwapping;
    }

    Vector<EntryPair> getContractValues(int i2) {
        return getContractValuesImpl(this.mapping.elementAt(i2) - CONTRACTCHARINDEX);
    }

    private Vector<EntryPair> getContractValuesImpl(int i2) {
        if (i2 >= 0) {
            return this.contractTable.elementAt(i2);
        }
        return null;
    }

    boolean usedInContractSeq(int i2) {
        return this.contractFlags.get(i2) == 1;
    }

    int getMaxExpansion(int i2) {
        int i3 = 1;
        if (this.expandTable != null) {
            for (int i4 = 0; i4 < this.expandTable.size(); i4++) {
                int[] iArrElementAt = this.expandTable.elementAt(i4);
                int length = iArrElementAt.length;
                if (length > i3 && iArrElementAt[length - 1] == i2) {
                    i3 = length;
                }
            }
        }
        return i3;
    }

    final int[] getExpandValueList(int i2) {
        return this.expandTable.elementAt(i2 - EXPANDCHARINDEX);
    }

    int getUnicodeOrder(int i2) {
        return this.mapping.elementAt(i2);
    }

    short getMaxSecOrder() {
        return this.maxSecOrder;
    }

    short getMaxTerOrder() {
        return this.maxTerOrder;
    }

    static void reverse(StringBuffer stringBuffer, int i2, int i3) {
        int i4 = i2;
        for (int i5 = i3 - 1; i4 < i5; i5--) {
            char cCharAt = stringBuffer.charAt(i4);
            stringBuffer.setCharAt(i4, stringBuffer.charAt(i5));
            stringBuffer.setCharAt(i5, cCharAt);
            i4++;
        }
    }

    static final int getEntry(Vector<EntryPair> vector, String str, boolean z2) {
        for (int i2 = 0; i2 < vector.size(); i2++) {
            EntryPair entryPairElementAt = vector.elementAt(i2);
            if (entryPairElementAt.fwd == z2 && entryPairElementAt.entryName.equals(str)) {
                return i2;
            }
        }
        return -1;
    }
}
