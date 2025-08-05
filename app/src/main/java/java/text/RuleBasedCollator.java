package java.text;

import java.text.Normalizer;

/* loaded from: rt.jar:java/text/RuleBasedCollator.class */
public class RuleBasedCollator extends Collator {
    static final int CHARINDEX = 1879048192;
    static final int EXPANDCHARINDEX = 2113929216;
    static final int CONTRACTCHARINDEX = 2130706432;
    static final int UNMAPPED = -1;
    private static final int COLLATIONKEYOFFSET = 1;
    private RBCollationTables tables;
    private StringBuffer primResult;
    private StringBuffer secResult;
    private StringBuffer terResult;
    private CollationElementIterator sourceCursor;
    private CollationElementIterator targetCursor;

    public RuleBasedCollator(String str) throws ParseException {
        this(str, 1);
    }

    RuleBasedCollator(String str, int i2) throws ParseException {
        this.tables = null;
        this.primResult = null;
        this.secResult = null;
        this.terResult = null;
        this.sourceCursor = null;
        this.targetCursor = null;
        setStrength(2);
        setDecomposition(i2);
        this.tables = new RBCollationTables(str, i2);
    }

    private RuleBasedCollator(RuleBasedCollator ruleBasedCollator) {
        this.tables = null;
        this.primResult = null;
        this.secResult = null;
        this.terResult = null;
        this.sourceCursor = null;
        this.targetCursor = null;
        setStrength(ruleBasedCollator.getStrength());
        setDecomposition(ruleBasedCollator.getDecomposition());
        this.tables = ruleBasedCollator.tables;
    }

    public String getRules() {
        return this.tables.getRules();
    }

    public CollationElementIterator getCollationElementIterator(String str) {
        return new CollationElementIterator(str, this);
    }

    public CollationElementIterator getCollationElementIterator(CharacterIterator characterIterator) {
        return new CollationElementIterator(characterIterator, this);
    }

    /* JADX WARN: Code restructure failed: missing block: B:101:0x01a6, code lost:
    
        if (r11 == false) goto L103;
     */
    /* JADX WARN: Code restructure failed: missing block: B:102:0x01a9, code lost:
    
        r7 = 1;
        r11 = false;
     */
    /* JADX WARN: Code restructure failed: missing block: B:103:0x01ae, code lost:
    
        r0 = r4.sourceCursor.next();
        r8 = r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:104:0x01b9, code lost:
    
        if (r0 != (-1)) goto L172;
     */
    /* JADX WARN: Code restructure failed: missing block: B:107:0x01c2, code lost:
    
        if (r9 == (-1)) goto L119;
     */
    /* JADX WARN: Code restructure failed: missing block: B:109:0x01ca, code lost:
    
        if (java.text.CollationElementIterator.primaryOrder(r9) == 0) goto L112;
     */
    /* JADX WARN: Code restructure failed: missing block: B:110:0x01cd, code lost:
    
        return -1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:113:0x01d4, code lost:
    
        if (java.text.CollationElementIterator.secondaryOrder(r9) == 0) goto L117;
     */
    /* JADX WARN: Code restructure failed: missing block: B:115:0x01d9, code lost:
    
        if (r11 == false) goto L117;
     */
    /* JADX WARN: Code restructure failed: missing block: B:116:0x01dc, code lost:
    
        r7 = -1;
        r11 = false;
     */
    /* JADX WARN: Code restructure failed: missing block: B:117:0x01e1, code lost:
    
        r0 = r4.targetCursor.next();
        r9 = r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:118:0x01ec, code lost:
    
        if (r0 != (-1)) goto L175;
     */
    /* JADX WARN: Code restructure failed: missing block: B:120:0x01f0, code lost:
    
        if (r7 != 0) goto L133;
     */
    /* JADX WARN: Code restructure failed: missing block: B:122:0x01f8, code lost:
    
        if (getStrength() != 3) goto L133;
     */
    /* JADX WARN: Code restructure failed: missing block: B:123:0x01fb, code lost:
    
        r0 = getDecomposition();
     */
    /* JADX WARN: Code restructure failed: missing block: B:124:0x0204, code lost:
    
        if (r0 != 1) goto L126;
     */
    /* JADX WARN: Code restructure failed: missing block: B:125:0x0207, code lost:
    
        r16 = java.text.Normalizer.Form.NFD;
     */
    /* JADX WARN: Code restructure failed: missing block: B:127:0x0212, code lost:
    
        if (r0 != 2) goto L129;
     */
    /* JADX WARN: Code restructure failed: missing block: B:128:0x0215, code lost:
    
        r16 = java.text.Normalizer.Form.NFKD;
     */
    /* JADX WARN: Code restructure failed: missing block: B:130:0x0222, code lost:
    
        return r5.compareTo(r6);
     */
    /* JADX WARN: Code restructure failed: missing block: B:132:0x023a, code lost:
    
        return java.text.Normalizer.normalize(r5, r16).compareTo(java.text.Normalizer.normalize(r6, r16));
     */
    /* JADX WARN: Code restructure failed: missing block: B:134:0x023c, code lost:
    
        return r7;
     */
    /* JADX WARN: Code restructure failed: missing block: B:93:0x018f, code lost:
    
        if (r8 == (-1)) goto L106;
     */
    /* JADX WARN: Code restructure failed: missing block: B:95:0x0197, code lost:
    
        if (java.text.CollationElementIterator.primaryOrder(r8) == 0) goto L98;
     */
    /* JADX WARN: Code restructure failed: missing block: B:96:0x019a, code lost:
    
        return 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:99:0x01a1, code lost:
    
        if (java.text.CollationElementIterator.secondaryOrder(r8) == 0) goto L103;
     */
    @Override // java.text.Collator
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public synchronized int compare(java.lang.String r5, java.lang.String r6) {
        /*
            Method dump skipped, instructions count: 573
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: java.text.RuleBasedCollator.compare(java.lang.String, java.lang.String):int");
    }

    @Override // java.text.Collator
    public synchronized CollationKey getCollationKey(String str) {
        if (str == null) {
            return null;
        }
        if (this.primResult == null) {
            this.primResult = new StringBuffer();
            this.secResult = new StringBuffer();
            this.terResult = new StringBuffer();
        } else {
            this.primResult.setLength(0);
            this.secResult.setLength(0);
            this.terResult.setLength(0);
        }
        boolean z2 = getStrength() >= 1;
        boolean z3 = getStrength() >= 2;
        int length = 0;
        if (this.sourceCursor == null) {
            this.sourceCursor = getCollationElementIterator(str);
        } else {
            this.sourceCursor.setText(str);
        }
        while (true) {
            int next = this.sourceCursor.next();
            if (next == -1) {
                break;
            }
            short sSecondaryOrder = CollationElementIterator.secondaryOrder(next);
            short sTertiaryOrder = CollationElementIterator.tertiaryOrder(next);
            if (!CollationElementIterator.isIgnorable(next)) {
                this.primResult.append((char) (CollationElementIterator.primaryOrder(next) + 1));
                if (z2) {
                    if (this.tables.isFrenchSec() && length < this.secResult.length()) {
                        RBCollationTables.reverse(this.secResult, length, this.secResult.length());
                    }
                    this.secResult.append((char) (sSecondaryOrder + 1));
                    length = this.secResult.length();
                }
                if (z3) {
                    this.terResult.append((char) (sTertiaryOrder + 1));
                }
            } else {
                if (z2 && sSecondaryOrder != 0) {
                    this.secResult.append((char) (sSecondaryOrder + this.tables.getMaxSecOrder() + 1));
                }
                if (z3 && sTertiaryOrder != 0) {
                    this.terResult.append((char) (sTertiaryOrder + this.tables.getMaxTerOrder() + 1));
                }
            }
        }
        if (this.tables.isFrenchSec()) {
            if (length < this.secResult.length()) {
                RBCollationTables.reverse(this.secResult, length, this.secResult.length());
            }
            RBCollationTables.reverse(this.secResult, 0, this.secResult.length());
        }
        this.primResult.append((char) 0);
        this.secResult.append((char) 0);
        this.secResult.append(this.terResult.toString());
        this.primResult.append(this.secResult.toString());
        if (getStrength() == 3) {
            this.primResult.append((char) 0);
            int decomposition = getDecomposition();
            if (decomposition == 1) {
                this.primResult.append(Normalizer.normalize(str, Normalizer.Form.NFD));
            } else if (decomposition == 2) {
                this.primResult.append(Normalizer.normalize(str, Normalizer.Form.NFKD));
            } else {
                this.primResult.append(str);
            }
        }
        return new RuleBasedCollationKey(str, this.primResult.toString());
    }

    @Override // java.text.Collator
    public Object clone() {
        if (getClass() == RuleBasedCollator.class) {
            return new RuleBasedCollator(this);
        }
        RuleBasedCollator ruleBasedCollator = (RuleBasedCollator) super.clone();
        ruleBasedCollator.primResult = null;
        ruleBasedCollator.secResult = null;
        ruleBasedCollator.terResult = null;
        ruleBasedCollator.sourceCursor = null;
        ruleBasedCollator.targetCursor = null;
        return ruleBasedCollator;
    }

    @Override // java.text.Collator, java.util.Comparator
    public boolean equals(Object obj) {
        if (obj != null && super.equals(obj)) {
            return getRules().equals(((RuleBasedCollator) obj).getRules());
        }
        return false;
    }

    @Override // java.text.Collator
    public int hashCode() {
        return getRules().hashCode();
    }

    RBCollationTables getTables() {
        return this.tables;
    }
}
