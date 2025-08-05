package java.text;

import java.text.PatternEntry;
import java.util.ArrayList;

/* loaded from: rt.jar:java/text/MergeCollation.class */
final class MergeCollation {
    ArrayList<PatternEntry> patterns = new ArrayList<>();
    private transient PatternEntry saveEntry = null;
    private transient PatternEntry lastEntry = null;
    private transient StringBuffer excess = new StringBuffer();
    private transient byte[] statusArray = new byte[8192];
    private final byte BITARRAYMASK = 1;
    private final int BYTEPOWER = 3;
    private final int BYTEMASK = 7;

    public MergeCollation(String str) throws ParseException {
        for (int i2 = 0; i2 < this.statusArray.length; i2++) {
            this.statusArray[i2] = 0;
        }
        setPattern(str);
    }

    public String getPattern() {
        return getPattern(true);
    }

    public String getPattern(boolean z2) {
        StringBuffer stringBuffer = new StringBuffer();
        ArrayList arrayList = null;
        int i2 = 0;
        while (i2 < this.patterns.size()) {
            PatternEntry patternEntry = this.patterns.get(i2);
            if (patternEntry.extension.length() != 0) {
                if (arrayList == null) {
                    arrayList = new ArrayList();
                }
                arrayList.add(patternEntry);
            } else {
                if (arrayList != null) {
                    PatternEntry patternEntryFindLastWithNoExtension = findLastWithNoExtension(i2 - 1);
                    for (int size = arrayList.size() - 1; size >= 0; size--) {
                        ((PatternEntry) arrayList.get(size)).addToBuffer(stringBuffer, false, z2, patternEntryFindLastWithNoExtension);
                    }
                    arrayList = null;
                }
                patternEntry.addToBuffer(stringBuffer, false, z2, null);
            }
            i2++;
        }
        if (arrayList != null) {
            PatternEntry patternEntryFindLastWithNoExtension2 = findLastWithNoExtension(i2 - 1);
            for (int size2 = arrayList.size() - 1; size2 >= 0; size2--) {
                ((PatternEntry) arrayList.get(size2)).addToBuffer(stringBuffer, false, z2, patternEntryFindLastWithNoExtension2);
            }
        }
        return stringBuffer.toString();
    }

    private final PatternEntry findLastWithNoExtension(int i2) {
        PatternEntry patternEntry;
        do {
            i2--;
            if (i2 >= 0) {
                patternEntry = this.patterns.get(i2);
            } else {
                return null;
            }
        } while (patternEntry.extension.length() != 0);
        return patternEntry;
    }

    public String emitPattern() {
        return emitPattern(true);
    }

    public String emitPattern(boolean z2) {
        StringBuffer stringBuffer = new StringBuffer();
        for (int i2 = 0; i2 < this.patterns.size(); i2++) {
            PatternEntry patternEntry = this.patterns.get(i2);
            if (patternEntry != null) {
                patternEntry.addToBuffer(stringBuffer, true, z2, null);
            }
        }
        return stringBuffer.toString();
    }

    public void setPattern(String str) throws ParseException {
        this.patterns.clear();
        addPattern(str);
    }

    public void addPattern(String str) throws ParseException {
        if (str == null) {
            return;
        }
        PatternEntry.Parser parser = new PatternEntry.Parser(str);
        PatternEntry next = parser.next();
        while (true) {
            PatternEntry patternEntry = next;
            if (patternEntry != null) {
                fixEntry(patternEntry);
                next = parser.next();
            } else {
                return;
            }
        }
    }

    public int getCount() {
        return this.patterns.size();
    }

    public PatternEntry getItemAt(int i2) {
        return this.patterns.get(i2);
    }

    private final void fixEntry(PatternEntry patternEntry) throws ParseException {
        if (this.lastEntry != null && patternEntry.chars.equals(this.lastEntry.chars) && patternEntry.extension.equals(this.lastEntry.extension)) {
            if (patternEntry.strength != 3 && patternEntry.strength != -2) {
                throw new ParseException("The entries " + ((Object) this.lastEntry) + " and " + ((Object) patternEntry) + " are adjacent in the rules, but have conflicting strengths: A character can't be unequal to itself.", -1);
            }
            return;
        }
        boolean z2 = true;
        if (patternEntry.strength != -2) {
            int iLastIndexOf = -1;
            if (patternEntry.chars.length() == 1) {
                char cCharAt = patternEntry.chars.charAt(0);
                int i2 = cCharAt >> 3;
                byte b2 = this.statusArray[i2];
                byte b3 = (byte) (1 << (cCharAt & 7));
                if (b2 != 0 && (b2 & b3) != 0) {
                    iLastIndexOf = this.patterns.lastIndexOf(patternEntry);
                } else {
                    this.statusArray[i2] = (byte) (b2 | b3);
                }
            } else {
                iLastIndexOf = this.patterns.lastIndexOf(patternEntry);
            }
            if (iLastIndexOf != -1) {
                this.patterns.remove(iLastIndexOf);
            }
            this.excess.setLength(0);
            int iFindLastEntry = findLastEntry(this.lastEntry, this.excess);
            if (this.excess.length() != 0) {
                patternEntry.extension = ((Object) this.excess) + patternEntry.extension;
                if (iFindLastEntry != this.patterns.size()) {
                    this.lastEntry = this.saveEntry;
                    z2 = false;
                }
            }
            if (iFindLastEntry == this.patterns.size()) {
                this.patterns.add(patternEntry);
                this.saveEntry = patternEntry;
            } else {
                this.patterns.add(iFindLastEntry, patternEntry);
            }
        }
        if (z2) {
            this.lastEntry = patternEntry;
        }
    }

    private final int findLastEntry(PatternEntry patternEntry, StringBuffer stringBuffer) throws ParseException {
        if (patternEntry == null) {
            return 0;
        }
        if (patternEntry.strength != -2) {
            int iLastIndexOf = -1;
            if (patternEntry.chars.length() == 1) {
                if ((this.statusArray[patternEntry.chars.charAt(0) >> 3] & (1 << (patternEntry.chars.charAt(0) & 7))) != 0) {
                    iLastIndexOf = this.patterns.lastIndexOf(patternEntry);
                }
            } else {
                iLastIndexOf = this.patterns.lastIndexOf(patternEntry);
            }
            if (iLastIndexOf == -1) {
                throw new ParseException("couldn't find last entry: " + ((Object) patternEntry), iLastIndexOf);
            }
            return iLastIndexOf + 1;
        }
        int size = this.patterns.size() - 1;
        while (true) {
            if (size < 0) {
                break;
            }
            PatternEntry patternEntry2 = this.patterns.get(size);
            if (!patternEntry2.chars.regionMatches(0, patternEntry.chars, 0, patternEntry2.chars.length())) {
                size--;
            } else {
                stringBuffer.append(patternEntry.chars.substring(patternEntry2.chars.length(), patternEntry.chars.length()));
                break;
            }
        }
        if (size == -1) {
            throw new ParseException("couldn't find: " + ((Object) patternEntry), size);
        }
        return size + 1;
    }
}
