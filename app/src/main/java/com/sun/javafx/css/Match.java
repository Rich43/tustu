package com.sun.javafx.css;

import javafx.geometry.NodeOrientation;

/* loaded from: jfxrt.jar:com/sun/javafx/css/Match.class */
final class Match implements Comparable<Match> {
    final Selector selector;
    final PseudoClassState pseudoClasses;
    final int idCount;
    final int styleClassCount;
    final int specificity;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !Match.class.desiredAssertionStatus();
    }

    Match(Selector selector, PseudoClassState pseudoClasses, int idCount, int styleClassCount) {
        if (!$assertionsDisabled && selector == null) {
            throw new AssertionError();
        }
        this.selector = selector;
        this.idCount = idCount;
        this.styleClassCount = styleClassCount;
        this.pseudoClasses = pseudoClasses;
        int nPseudoClasses = pseudoClasses != null ? pseudoClasses.size() : 0;
        if (selector instanceof SimpleSelector) {
            SimpleSelector simple = (SimpleSelector) selector;
            if (simple.getNodeOrientation() != NodeOrientation.INHERIT) {
                nPseudoClasses++;
            }
        }
        this.specificity = (idCount << 8) | (styleClassCount << 4) | nPseudoClasses;
    }

    @Override // java.lang.Comparable
    public int compareTo(Match o2) {
        return this.specificity - o2.specificity;
    }
}
