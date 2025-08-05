package com.sun.javafx.css;

import com.sun.javafx.collections.TrackableObservableList;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.css.PseudoClass;
import javafx.css.StyleOrigin;
import javafx.scene.Node;

/* loaded from: jfxrt.jar:com/sun/javafx/css/Rule.class */
public final class Rule {
    private List<Selector> selectors;
    private List<Declaration> declarations;
    private Observables observables;
    private Stylesheet stylesheet;
    private byte[] serializedDecls;
    private final int bssVersion;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !Rule.class.desiredAssertionStatus();
    }

    public List<Selector> getUnobservedSelectorList() {
        if (this.selectors == null) {
            this.selectors = new ArrayList();
        }
        return this.selectors;
    }

    public List<Declaration> getUnobservedDeclarationList() {
        if (this.declarations == null) {
            try {
                if (this.serializedDecls != null) {
                    try {
                        ByteArrayInputStream bis = new ByteArrayInputStream(this.serializedDecls);
                        DataInputStream dis = new DataInputStream(bis);
                        int nDeclarations = dis.readShort();
                        this.declarations = new ArrayList(nDeclarations);
                        for (int i2 = 0; i2 < nDeclarations; i2++) {
                            Declaration decl = Declaration.readBinary(this.bssVersion, dis, this.stylesheet.getStringStore());
                            decl.rule = this;
                            if (this.stylesheet != null && this.stylesheet.getUrl() != null) {
                                String stylesheetUrl = this.stylesheet.getUrl();
                                decl.fixUrl(stylesheetUrl);
                            }
                            this.declarations.add(decl);
                        }
                        this.serializedDecls = null;
                    } catch (IOException ioe) {
                        this.declarations = new ArrayList();
                        if (!$assertionsDisabled) {
                            throw new AssertionError();
                        }
                        ioe.getMessage();
                        this.serializedDecls = null;
                    }
                }
            } catch (Throwable th) {
                this.serializedDecls = null;
                throw th;
            }
        }
        return this.declarations;
    }

    public final ObservableList<Declaration> getDeclarations() {
        if (this.observables == null) {
            this.observables = new Observables();
        }
        return this.observables.getDeclarations();
    }

    public final ObservableList<Selector> getSelectors() {
        if (this.observables == null) {
            this.observables = new Observables();
        }
        return this.observables.getSelectors();
    }

    public Stylesheet getStylesheet() {
        return this.stylesheet;
    }

    void setStylesheet(Stylesheet stylesheet) {
        this.stylesheet = stylesheet;
        if (stylesheet != null && stylesheet.getUrl() != null) {
            String stylesheetUrl = stylesheet.getUrl();
            int nDeclarations = this.declarations != null ? this.declarations.size() : 0;
            for (int d2 = 0; d2 < nDeclarations; d2++) {
                this.declarations.get(d2).fixUrl(stylesheetUrl);
            }
        }
    }

    public StyleOrigin getOrigin() {
        if (this.stylesheet != null) {
            return this.stylesheet.getOrigin();
        }
        return null;
    }

    public Rule(List<Selector> selectors, List<Declaration> declarations) {
        this.selectors = null;
        this.declarations = null;
        this.observables = null;
        this.selectors = selectors;
        this.declarations = declarations;
        this.serializedDecls = null;
        this.bssVersion = 5;
        int sMax = selectors != null ? selectors.size() : 0;
        for (int i2 = 0; i2 < sMax; i2++) {
            Selector sel = selectors.get(i2);
            sel.setRule(this);
        }
        int dMax = declarations != null ? declarations.size() : 0;
        for (int d2 = 0; d2 < dMax; d2++) {
            Declaration decl = declarations.get(d2);
            decl.rule = this;
        }
    }

    private Rule(List<Selector> selectors, byte[] buf, int bssVersion) {
        this.selectors = null;
        this.declarations = null;
        this.observables = null;
        this.selectors = selectors;
        this.declarations = null;
        this.serializedDecls = buf;
        this.bssVersion = bssVersion;
        int sMax = selectors != null ? selectors.size() : 0;
        for (int i2 = 0; i2 < sMax; i2++) {
            Selector sel = selectors.get(i2);
            sel.setRule(this);
        }
    }

    long applies(Node node, Set<PseudoClass>[] triggerStates) {
        long mask = 0;
        for (int i2 = 0; i2 < this.selectors.size(); i2++) {
            Selector sel = this.selectors.get(i2);
            if (sel.applies(node, triggerStates, 0)) {
                mask |= 1 << i2;
            }
        }
        return mask;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (this.selectors.size() > 0) {
            sb.append((Object) this.selectors.get(0));
        }
        for (int n2 = 1; n2 < this.selectors.size(); n2++) {
            sb.append(',');
            sb.append((Object) this.selectors.get(n2));
        }
        sb.append("{\n");
        int nDeclarations = this.declarations != null ? this.declarations.size() : 0;
        for (int n3 = 0; n3 < nDeclarations; n3++) {
            sb.append("\t");
            sb.append((Object) this.declarations.get(n3));
            sb.append("\n");
        }
        sb.append("}");
        return sb.toString();
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/css/Rule$Observables.class */
    private static final class Observables {
        private final Rule rule;
        private final ObservableList<Selector> selectorObservableList;
        private final ObservableList<Declaration> declarationObservableList;

        private Observables(Rule rule) {
            this.rule = rule;
            this.selectorObservableList = new TrackableObservableList<Selector>(rule.getUnobservedSelectorList()) { // from class: com.sun.javafx.css.Rule.Observables.1
                @Override // com.sun.javafx.collections.TrackableObservableList
                protected void onChanged(ListChangeListener.Change<Selector> c2) {
                    while (c2.next()) {
                        if (c2.wasAdded()) {
                            List<Selector> added = c2.getAddedSubList();
                            int max = added.size();
                            for (int i2 = 0; i2 < max; i2++) {
                                added.get(i2).setRule(Observables.this.rule);
                            }
                        }
                        if (c2.wasRemoved()) {
                            List<Selector> removed = c2.getAddedSubList();
                            int max2 = removed.size();
                            for (int i3 = 0; i3 < max2; i3++) {
                                Selector sel = removed.get(i3);
                                if (sel.getRule() == Observables.this.rule) {
                                    sel.setRule(null);
                                }
                            }
                        }
                    }
                }
            };
            this.declarationObservableList = new TrackableObservableList<Declaration>(rule.getUnobservedDeclarationList()) { // from class: com.sun.javafx.css.Rule.Observables.2
                @Override // com.sun.javafx.collections.TrackableObservableList
                protected void onChanged(ListChangeListener.Change<Declaration> c2) {
                    while (c2.next()) {
                        if (c2.wasAdded()) {
                            List<Declaration> added = c2.getAddedSubList();
                            int max = added.size();
                            for (int i2 = 0; i2 < max; i2++) {
                                Declaration decl = added.get(i2);
                                decl.rule = Observables.this.rule;
                                Stylesheet stylesheet = Observables.this.rule.stylesheet;
                                if (stylesheet != null && stylesheet.getUrl() != null) {
                                    String stylesheetUrl = stylesheet.getUrl();
                                    decl.fixUrl(stylesheetUrl);
                                }
                            }
                        }
                        if (c2.wasRemoved()) {
                            List<Declaration> removed = c2.getRemoved();
                            int max2 = removed.size();
                            for (int i3 = 0; i3 < max2; i3++) {
                                Declaration decl2 = removed.get(i3);
                                if (decl2.rule == Observables.this.rule) {
                                    decl2.rule = null;
                                }
                            }
                        }
                    }
                }
            };
        }

        /* JADX INFO: Access modifiers changed from: private */
        public ObservableList<Selector> getSelectors() {
            return this.selectorObservableList;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public ObservableList<Declaration> getDeclarations() {
            return this.declarationObservableList;
        }
    }

    final void writeBinary(DataOutputStream os, StringStore stringStore) throws IOException {
        int nSelectors = this.selectors != null ? this.selectors.size() : 0;
        os.writeShort(nSelectors);
        for (int i2 = 0; i2 < nSelectors; i2++) {
            Selector sel = this.selectors.get(i2);
            sel.writeBinary(os, stringStore);
        }
        List<Declaration> decls = getUnobservedDeclarationList();
        if (decls != null) {
            ByteArrayOutputStream bos = new ByteArrayOutputStream(5192);
            DataOutputStream dos = new DataOutputStream(bos);
            int nDeclarations = decls.size();
            dos.writeShort(nDeclarations);
            for (int i3 = 0; i3 < nDeclarations; i3++) {
                Declaration decl = this.declarations.get(i3);
                decl.writeBinary(dos, stringStore);
            }
            os.writeInt(bos.size());
            os.write(bos.toByteArray());
            return;
        }
        os.writeShort(0);
    }

    static Rule readBinary(int bssVersion, DataInputStream is, String[] strings) throws IOException {
        int nSelectors = is.readShort();
        List<Selector> selectors = new ArrayList<>(nSelectors);
        for (int i2 = 0; i2 < nSelectors; i2++) {
            Selector s2 = Selector.readBinary(bssVersion, is, strings);
            selectors.add(s2);
        }
        if (bssVersion < 4) {
            int nDeclarations = is.readShort();
            List<Declaration> declarations = new ArrayList<>(nDeclarations);
            for (int i3 = 0; i3 < nDeclarations; i3++) {
                Declaration d2 = Declaration.readBinary(bssVersion, is, strings);
                declarations.add(d2);
            }
            return new Rule(selectors, declarations);
        }
        int nBytes = is.readInt();
        byte[] buf = new byte[nBytes];
        if (nBytes > 0) {
            is.readFully(buf);
        }
        return new Rule(selectors, buf, bssVersion);
    }
}
