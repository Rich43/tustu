package com.sun.org.apache.xml.internal.utils;

import com.sun.org.apache.xml.internal.dtm.DTMManager;
import java.text.CollationElementIterator;
import java.text.Collator;
import java.text.RuleBasedCollator;
import java.util.Locale;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/utils/StringComparable.class */
public class StringComparable implements Comparable {
    public static final int UNKNOWN_CASE = -1;
    public static final int UPPER_CASE = 1;
    public static final int LOWER_CASE = 2;
    private String m_text;
    private Locale m_locale;
    private RuleBasedCollator m_collator;
    private String m_caseOrder;
    private int m_mask;

    public StringComparable(String text, Locale locale, Collator collator, String caseOrder) {
        this.m_mask = -1;
        this.m_text = text;
        this.m_locale = locale;
        this.m_collator = (RuleBasedCollator) collator;
        this.m_caseOrder = caseOrder;
        this.m_mask = getMask(this.m_collator.getStrength());
    }

    public static final Comparable getComparator(String text, Locale locale, Collator collator, String caseOrder) {
        if (caseOrder == null || caseOrder.length() == 0) {
            return ((RuleBasedCollator) collator).getCollationKey(text);
        }
        return new StringComparable(text, locale, collator, caseOrder);
    }

    public final String toString() {
        return this.m_text;
    }

    @Override // java.lang.Comparable
    public int compareTo(Object o2) {
        int comp;
        String pattern = ((StringComparable) o2).toString();
        if (this.m_text.equals(pattern)) {
            return 0;
        }
        int savedStrength = this.m_collator.getStrength();
        if (savedStrength == 0 || savedStrength == 1) {
            comp = this.m_collator.compare(this.m_text, pattern);
        } else {
            this.m_collator.setStrength(1);
            comp = this.m_collator.compare(this.m_text, pattern);
            this.m_collator.setStrength(savedStrength);
        }
        if (comp != 0) {
            return comp;
        }
        int comp2 = getCaseDiff(this.m_text, pattern);
        if (comp2 != 0) {
            return comp2;
        }
        return this.m_collator.compare(this.m_text, pattern);
    }

    private final int getCaseDiff(String text, String pattern) {
        int savedStrength = this.m_collator.getStrength();
        int savedDecomposition = this.m_collator.getDecomposition();
        this.m_collator.setStrength(2);
        this.m_collator.setDecomposition(1);
        int[] diff = getFirstCaseDiff(text, pattern, this.m_locale);
        this.m_collator.setStrength(savedStrength);
        this.m_collator.setDecomposition(savedDecomposition);
        if (diff != null) {
            if (this.m_caseOrder.equals(com.sun.org.apache.xalan.internal.templates.Constants.ATTRVAL_CASEORDER_UPPER)) {
                if (diff[0] == 1) {
                    return -1;
                }
                return 1;
            }
            if (diff[0] == 2) {
                return -1;
            }
            return 1;
        }
        return 0;
    }

    private final int[] getFirstCaseDiff(String text, String pattern, Locale locale) {
        int[] diff;
        CollationElementIterator targIter = this.m_collator.getCollationElementIterator(text);
        CollationElementIterator patIter = this.m_collator.getCollationElementIterator(pattern);
        int startTarg = -1;
        int endTarg = -1;
        int startPatt = -1;
        int endPatt = -1;
        int done = getElement(-1);
        int patternElement = 0;
        int targetElement = 0;
        boolean getPattern = true;
        boolean getTarget = true;
        while (true) {
            if (getPattern) {
                startPatt = patIter.getOffset();
                patternElement = getElement(patIter.next());
                endPatt = patIter.getOffset();
            }
            if (getTarget) {
                startTarg = targIter.getOffset();
                targetElement = getElement(targIter.next());
                endTarg = targIter.getOffset();
            }
            getPattern = true;
            getTarget = true;
            if (patternElement == done || targetElement == done) {
                return null;
            }
            if (targetElement == 0) {
                getPattern = false;
            } else if (patternElement == 0) {
                getTarget = false;
            } else if (targetElement != patternElement && startPatt < endPatt && startTarg < endTarg) {
                String subText = text.substring(startTarg, endTarg);
                String subPatt = pattern.substring(startPatt, endPatt);
                String subTextUp = subText.toUpperCase(locale);
                String subPattUp = subPatt.toUpperCase(locale);
                if (this.m_collator.compare(subTextUp, subPattUp) == 0) {
                    diff = new int[]{-1, -1};
                    if (this.m_collator.compare(subText, subTextUp) == 0) {
                        diff[0] = 1;
                    } else if (this.m_collator.compare(subText, subText.toLowerCase(locale)) == 0) {
                        diff[0] = 2;
                    }
                    if (this.m_collator.compare(subPatt, subPattUp) == 0) {
                        diff[1] = 1;
                    } else if (this.m_collator.compare(subPatt, subPatt.toLowerCase(locale)) == 0) {
                        diff[1] = 2;
                    }
                    if ((diff[0] == 1 && diff[1] == 2) || (diff[0] == 2 && diff[1] == 1)) {
                        break;
                    }
                } else {
                    continue;
                }
            }
        }
        return diff;
    }

    private static final int getMask(int strength) {
        switch (strength) {
            case 0:
                return DTMManager.IDENT_DTM_DEFAULT;
            case 1:
                return -256;
            default:
                return -1;
        }
    }

    private final int getElement(int maxStrengthElement) {
        return maxStrengthElement & this.m_mask;
    }
}
