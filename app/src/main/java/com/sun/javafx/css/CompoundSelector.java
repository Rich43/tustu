package com.sun.javafx.css;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import javafx.css.PseudoClass;
import javafx.css.Styleable;

/* loaded from: jfxrt.jar:com/sun/javafx/css/CompoundSelector.class */
public final class CompoundSelector extends Selector {
    private final List<SimpleSelector> selectors;
    private final List<Combinator> relationships;
    private int hash;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !CompoundSelector.class.desiredAssertionStatus();
    }

    public List<SimpleSelector> getSelectors() {
        return this.selectors;
    }

    public List<Combinator> getRelationships() {
        return this.relationships;
    }

    public CompoundSelector(List<SimpleSelector> selectors, List<Combinator> relationships) {
        this.hash = -1;
        this.selectors = selectors != null ? Collections.unmodifiableList(selectors) : Collections.EMPTY_LIST;
        this.relationships = relationships != null ? Collections.unmodifiableList(relationships) : Collections.EMPTY_LIST;
    }

    private CompoundSelector() {
        this(null, null);
    }

    @Override // com.sun.javafx.css.Selector
    Match createMatch() {
        PseudoClassState allPseudoClasses = new PseudoClassState();
        int idCount = 0;
        int styleClassCount = 0;
        int nMax = this.selectors.size();
        for (int n2 = 0; n2 < nMax; n2++) {
            Selector sel = this.selectors.get(n2);
            Match match = sel.createMatch();
            allPseudoClasses.addAll(match.pseudoClasses);
            idCount += match.idCount;
            styleClassCount += match.styleClassCount;
        }
        return new Match(this, allPseudoClasses, idCount, styleClassCount);
    }

    @Override // com.sun.javafx.css.Selector
    public boolean applies(Styleable styleable) {
        return applies(styleable, this.selectors.size() - 1, null, 0);
    }

    @Override // com.sun.javafx.css.Selector
    boolean applies(Styleable styleable, Set<PseudoClass>[] triggerStates, int depth) {
        if (!$assertionsDisabled && triggerStates != null && depth >= triggerStates.length) {
            throw new AssertionError();
        }
        if (triggerStates != null && triggerStates.length <= depth) {
            return false;
        }
        Set<PseudoClass>[] tempStates = triggerStates != null ? new PseudoClassState[triggerStates.length] : null;
        boolean applies = applies(styleable, this.selectors.size() - 1, tempStates, depth);
        if (applies && tempStates != null) {
            for (int n2 = 0; n2 < triggerStates.length; n2++) {
                Set<PseudoClass> pseudoClassOut = triggerStates[n2];
                Set<PseudoClass> pseudoClassIn = tempStates[n2];
                if (pseudoClassOut != null) {
                    pseudoClassOut.addAll(pseudoClassIn);
                } else {
                    triggerStates[n2] = pseudoClassIn;
                }
            }
        }
        return applies;
    }

    private boolean applies(Styleable styleable, int index, Set<PseudoClass>[] triggerStates, int depth) {
        if (index < 0 || !this.selectors.get(index).applies(styleable, triggerStates, depth)) {
            return false;
        }
        if (index == 0) {
            return true;
        }
        Combinator relationship = this.relationships.get(index - 1);
        if (relationship == Combinator.CHILD) {
            Styleable parent = styleable.getStyleableParent();
            if (parent == null) {
                return false;
            }
            return applies(parent, index - 1, triggerStates, depth + 1);
        }
        Styleable styleableParent = styleable.getStyleableParent();
        while (true) {
            Styleable parent2 = styleableParent;
            if (parent2 != null) {
                depth++;
                boolean answer = applies(parent2, index - 1, triggerStates, depth);
                if (answer) {
                    return true;
                }
                styleableParent = parent2.getStyleableParent();
            } else {
                return false;
            }
        }
    }

    @Override // com.sun.javafx.css.Selector
    public boolean stateMatches(Styleable styleable, Set<PseudoClass> states) {
        return stateMatches(styleable, states, this.selectors.size() - 1);
    }

    private boolean stateMatches(Styleable styleable, Set<PseudoClass> states, int index) {
        if (index < 0 || !this.selectors.get(index).stateMatches(styleable, states)) {
            return false;
        }
        if (index == 0) {
            return true;
        }
        Combinator relationship = this.relationships.get(index - 1);
        if (relationship == Combinator.CHILD) {
            Styleable parent = styleable.getStyleableParent();
            if (parent != null && this.selectors.get(index - 1).applies(parent)) {
                Set<PseudoClass> parentStates = parent.getPseudoClassStates();
                return stateMatches(parent, parentStates, index - 1);
            }
            return false;
        }
        Styleable styleableParent = styleable.getStyleableParent();
        while (true) {
            Styleable parent2 = styleableParent;
            if (parent2 != null) {
                if (this.selectors.get(index - 1).applies(parent2)) {
                    Set<PseudoClass> parentStates2 = parent2.getPseudoClassStates();
                    return stateMatches(parent2, parentStates2, index - 1);
                }
                styleableParent = parent2.getStyleableParent();
            } else {
                return false;
            }
        }
    }

    public int hashCode() {
        if (this.hash == -1) {
            int max = this.selectors.size();
            for (int i2 = 0; i2 < max; i2++) {
                this.hash = 31 * (this.hash + this.selectors.get(i2).hashCode());
            }
            int max2 = this.relationships.size();
            for (int i3 = 0; i3 < max2; i3++) {
                this.hash = 31 * (this.hash + this.relationships.get(i3).hashCode());
            }
        }
        return this.hash;
    }

    public boolean equals(Object obj) {
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        CompoundSelector other = (CompoundSelector) obj;
        if (other.selectors.size() != this.selectors.size()) {
            return false;
        }
        int max = this.selectors.size();
        for (int i2 = 0; i2 < max; i2++) {
            if (!other.selectors.get(i2).equals(this.selectors.get(i2))) {
                return false;
            }
        }
        if (other.relationships.size() != this.relationships.size()) {
            return false;
        }
        int max2 = this.relationships.size();
        for (int i3 = 0; i3 < max2; i3++) {
            if (!other.relationships.get(i3).equals(this.relationships.get(i3))) {
                return false;
            }
        }
        return true;
    }

    public String toString() {
        StringBuilder sbuf = new StringBuilder();
        sbuf.append((Object) this.selectors.get(0));
        for (int n2 = 1; n2 < this.selectors.size(); n2++) {
            sbuf.append((Object) this.relationships.get(n2 - 1));
            sbuf.append((Object) this.selectors.get(n2));
        }
        return sbuf.toString();
    }

    @Override // com.sun.javafx.css.Selector
    public final void writeBinary(DataOutputStream os, StringStore stringStore) throws IOException {
        super.writeBinary(os, stringStore);
        os.writeShort(this.selectors.size());
        for (int n2 = 0; n2 < this.selectors.size(); n2++) {
            this.selectors.get(n2).writeBinary(os, stringStore);
        }
        os.writeShort(this.relationships.size());
        for (int n3 = 0; n3 < this.relationships.size(); n3++) {
            os.writeByte(this.relationships.get(n3).ordinal());
        }
    }

    public static CompoundSelector readBinary(int bssVersion, DataInputStream is, String[] strings) throws IOException {
        int nSelectors = is.readShort();
        List<SimpleSelector> selectors = new ArrayList<>();
        for (int n2 = 0; n2 < nSelectors; n2++) {
            selectors.add((SimpleSelector) Selector.readBinary(bssVersion, is, strings));
        }
        int nRelationships = is.readShort();
        List<Combinator> relationships = new ArrayList<>();
        for (int n3 = 0; n3 < nRelationships; n3++) {
            int ordinal = is.readByte();
            if (ordinal == Combinator.CHILD.ordinal()) {
                relationships.add(Combinator.CHILD);
            } else if (ordinal == Combinator.DESCENDANT.ordinal()) {
                relationships.add(Combinator.DESCENDANT);
            } else {
                if (!$assertionsDisabled) {
                    throw new AssertionError((Object) ("error deserializing CompoundSelector: Combinator = " + ordinal));
                }
                relationships.add(Combinator.DESCENDANT);
            }
        }
        return new CompoundSelector(selectors, relationships);
    }
}
