package com.sun.javafx.css;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javafx.css.PseudoClass;
import javafx.css.Styleable;
import javafx.geometry.NodeOrientation;
import javafx.scene.Node;

/* loaded from: jfxrt.jar:com/sun/javafx/css/SimpleSelector.class */
public final class SimpleSelector extends Selector {
    private final String name;
    private final StyleClassSet styleClassSet;
    private final String id;
    private final PseudoClassState pseudoClassState;
    private final boolean matchOnName;
    private final boolean matchOnId;
    private final boolean matchOnStyleClass;
    private final NodeOrientation nodeOrientation;

    public String getName() {
        return this.name;
    }

    public List<String> getStyleClasses() {
        List<String> names = new ArrayList<>();
        Iterator<StyleClass> iter = this.styleClassSet.iterator();
        while (iter.hasNext()) {
            names.add(iter.next().getStyleClassName());
        }
        return Collections.unmodifiableList(names);
    }

    Set<StyleClass> getStyleClassSet() {
        return this.styleClassSet;
    }

    public String getId() {
        return this.id;
    }

    Set<PseudoClass> getPseudoClassStates() {
        return this.pseudoClassState;
    }

    public List<String> getPseudoclasses() {
        List<String> names = new ArrayList<>();
        Iterator<PseudoClass> iter = this.pseudoClassState.iterator();
        while (iter.hasNext()) {
            names.add(iter.next().getPseudoClassName());
        }
        if (this.nodeOrientation == NodeOrientation.RIGHT_TO_LEFT) {
            names.add("dir(rtl)");
        } else if (this.nodeOrientation == NodeOrientation.LEFT_TO_RIGHT) {
            names.add("dir(ltr)");
        }
        return Collections.unmodifiableList(names);
    }

    NodeOrientation getNodeOrientation() {
        return this.nodeOrientation;
    }

    public SimpleSelector(String name, List<String> styleClasses, List<String> pseudoClasses, String id) {
        this.name = name == null ? "*" : name;
        this.matchOnName = (name == null || "".equals(name) || "*".equals(name)) ? false : true;
        this.styleClassSet = new StyleClassSet();
        int nMax = styleClasses != null ? styleClasses.size() : 0;
        for (int n2 = 0; n2 < nMax; n2++) {
            String styleClassName = styleClasses.get(n2);
            if (styleClassName != null && !styleClassName.isEmpty()) {
                StyleClass styleClass = StyleClassSet.getStyleClass(styleClassName);
                this.styleClassSet.add(styleClass);
            }
        }
        this.matchOnStyleClass = this.styleClassSet.size() > 0;
        this.pseudoClassState = new PseudoClassState();
        int nMax2 = pseudoClasses != null ? pseudoClasses.size() : 0;
        NodeOrientation dir = NodeOrientation.INHERIT;
        for (int n3 = 0; n3 < nMax2; n3++) {
            String pclass = pseudoClasses.get(n3);
            if (pclass != null && !pclass.isEmpty()) {
                if ("dir(".regionMatches(true, 0, pclass, 0, 4)) {
                    boolean rtl = "dir(rtl)".equalsIgnoreCase(pclass);
                    dir = rtl ? NodeOrientation.RIGHT_TO_LEFT : NodeOrientation.LEFT_TO_RIGHT;
                } else {
                    PseudoClass pseudoClass = PseudoClassState.getPseudoClass(pclass);
                    this.pseudoClassState.add(pseudoClass);
                }
            }
        }
        this.nodeOrientation = dir;
        this.id = id == null ? "" : id;
        this.matchOnId = (id == null || "".equals(id)) ? false : true;
    }

    @Override // com.sun.javafx.css.Selector
    Match createMatch() {
        int idCount = this.matchOnId ? 1 : 0;
        int styleClassCount = this.styleClassSet.size();
        return new Match(this, this.pseudoClassState, idCount, styleClassCount);
    }

    @Override // com.sun.javafx.css.Selector
    public boolean applies(Styleable styleable) {
        if (this.nodeOrientation != NodeOrientation.INHERIT && (styleable instanceof Node)) {
            Node node = (Node) styleable;
            NodeOrientation orientation = node.getNodeOrientation();
            if (orientation == NodeOrientation.INHERIT) {
                if (node.getEffectiveNodeOrientation() != this.nodeOrientation) {
                    return false;
                }
            } else if (orientation != this.nodeOrientation) {
                return false;
            }
        }
        if (this.matchOnId) {
            String otherId = styleable.getId();
            boolean idMatch = this.id.equals(otherId);
            if (!idMatch) {
                return false;
            }
        }
        if (this.matchOnName) {
            String otherName = styleable.getTypeSelector();
            boolean classMatch = this.name.equals(otherName);
            if (!classMatch) {
                return false;
            }
        }
        if (this.matchOnStyleClass) {
            StyleClassSet otherStyleClassSet = new StyleClassSet();
            List<String> styleClasses = styleable.getStyleClass();
            int nMax = styleClasses.size();
            for (int n2 = 0; n2 < nMax; n2++) {
                String styleClassName = styleClasses.get(n2);
                if (styleClassName != null && !styleClassName.isEmpty()) {
                    StyleClass styleClass = StyleClassSet.getStyleClass(styleClassName);
                    otherStyleClassSet.add(styleClass);
                }
            }
            boolean styleClassMatch = matchStyleClasses(otherStyleClassSet);
            return styleClassMatch;
        }
        return true;
    }

    @Override // com.sun.javafx.css.Selector
    boolean applies(Styleable styleable, Set<PseudoClass>[] pseudoClasses, int depth) {
        boolean applies = applies(styleable);
        if (applies && pseudoClasses != null && depth < pseudoClasses.length) {
            if (pseudoClasses[depth] == null) {
                pseudoClasses[depth] = new PseudoClassState();
            }
            pseudoClasses[depth].addAll(this.pseudoClassState);
        }
        return applies;
    }

    @Override // com.sun.javafx.css.Selector
    public boolean stateMatches(Styleable styleable, Set<PseudoClass> states) {
        if (states != null) {
            return states.containsAll(this.pseudoClassState);
        }
        return false;
    }

    private boolean matchStyleClasses(StyleClassSet otherStyleClasses) {
        return otherStyleClasses.containsAll(this.styleClassSet);
    }

    public boolean equals(Object obj) {
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        SimpleSelector other = (SimpleSelector) obj;
        if (this.name == null) {
            if (other.name != null) {
                return false;
            }
        } else if (!this.name.equals(other.name)) {
            return false;
        }
        if (this.id == null) {
            if (other.id != null) {
                return false;
            }
        } else if (!this.id.equals(other.id)) {
            return false;
        }
        if (!this.styleClassSet.equals(other.styleClassSet) || !this.pseudoClassState.equals(other.pseudoClassState)) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        int hash = 31 * (7 + this.name.hashCode());
        return 31 * ((this.id != null ? 31 * ((31 * ((31 * (hash + this.styleClassSet.hashCode())) + this.styleClassSet.hashCode())) + this.id.hashCode()) : 0) + this.pseudoClassState.hashCode());
    }

    public String toString() {
        StringBuilder sbuf = new StringBuilder();
        if (this.name == null || this.name.isEmpty()) {
            sbuf.append("*");
        } else {
            sbuf.append(this.name);
        }
        Iterator<StyleClass> iter1 = this.styleClassSet.iterator();
        while (iter1.hasNext()) {
            StyleClass styleClass = iter1.next();
            sbuf.append('.').append(styleClass.getStyleClassName());
        }
        if (this.id != null && !this.id.isEmpty()) {
            sbuf.append('#');
            sbuf.append(this.id);
        }
        Iterator<PseudoClass> iter2 = this.pseudoClassState.iterator();
        while (iter2.hasNext()) {
            PseudoClass pseudoClass = iter2.next();
            sbuf.append(':').append(pseudoClass.getPseudoClassName());
        }
        return sbuf.toString();
    }

    @Override // com.sun.javafx.css.Selector
    public final void writeBinary(DataOutputStream os, StringStore stringStore) throws IOException {
        super.writeBinary(os, stringStore);
        os.writeShort(stringStore.addString(this.name));
        os.writeShort(this.styleClassSet.size());
        Iterator<StyleClass> iter1 = this.styleClassSet.iterator();
        while (iter1.hasNext()) {
            StyleClass sc = iter1.next();
            os.writeShort(stringStore.addString(sc.getStyleClassName()));
        }
        os.writeShort(stringStore.addString(this.id));
        int pclassSize = this.pseudoClassState.size() + ((this.nodeOrientation == NodeOrientation.RIGHT_TO_LEFT || this.nodeOrientation == NodeOrientation.LEFT_TO_RIGHT) ? 1 : 0);
        os.writeShort(pclassSize);
        Iterator<PseudoClass> iter2 = this.pseudoClassState.iterator();
        while (iter2.hasNext()) {
            PseudoClass pc = iter2.next();
            os.writeShort(stringStore.addString(pc.getPseudoClassName()));
        }
        if (this.nodeOrientation == NodeOrientation.RIGHT_TO_LEFT) {
            os.writeShort(stringStore.addString("dir(rtl)"));
        } else if (this.nodeOrientation == NodeOrientation.LEFT_TO_RIGHT) {
            os.writeShort(stringStore.addString("dir(ltr)"));
        }
    }

    static SimpleSelector readBinary(int bssVersion, DataInputStream is, String[] strings) throws IOException {
        String name = strings[is.readShort()];
        int nStyleClasses = is.readShort();
        List<String> styleClasses = new ArrayList<>();
        for (int n2 = 0; n2 < nStyleClasses; n2++) {
            styleClasses.add(strings[is.readShort()]);
        }
        String id = strings[is.readShort()];
        int nPseudoclasses = is.readShort();
        List<String> pseudoclasses = new ArrayList<>();
        for (int n3 = 0; n3 < nPseudoclasses; n3++) {
            pseudoclasses.add(strings[is.readShort()]);
        }
        return new SimpleSelector(name, styleClasses, pseudoclasses, id);
    }
}
