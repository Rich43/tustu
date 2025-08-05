package com.sun.javafx.css;

import java.util.Set;
import javafx.css.PseudoClass;
import javafx.css.StyleOrigin;

/* loaded from: jfxrt.jar:com/sun/javafx/css/CascadingStyle.class */
public class CascadingStyle implements Comparable<CascadingStyle> {
    private final Style style;
    private final Set<PseudoClass> pseudoClasses;
    private final int specificity;
    private final int ordinal;
    private final boolean skinProp;

    public Style getStyle() {
        return this.style;
    }

    public CascadingStyle(Style style, Set<PseudoClass> pseudoClasses, int specificity, int ordinal) {
        this.style = style;
        this.pseudoClasses = pseudoClasses;
        this.specificity = specificity;
        this.ordinal = ordinal;
        this.skinProp = "-fx-skin".equals(style.getDeclaration().getProperty());
    }

    public String getProperty() {
        return this.style.getDeclaration().getProperty();
    }

    public Selector getSelector() {
        return this.style.getSelector();
    }

    public Rule getRule() {
        return this.style.getDeclaration().getRule();
    }

    public StyleOrigin getOrigin() {
        return getRule().getOrigin();
    }

    public ParsedValueImpl getParsedValueImpl() {
        return this.style.getDeclaration().getParsedValueImpl();
    }

    public String toString() {
        return getProperty();
    }

    public boolean equals(Object obj) {
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        CascadingStyle other = (CascadingStyle) obj;
        String property = getProperty();
        String otherProperty = other.getProperty();
        if (property == null) {
            if (otherProperty != null) {
                return false;
            }
        } else if (!property.equals(otherProperty)) {
            return false;
        }
        if (this.pseudoClasses == null) {
            if (other.pseudoClasses != null) {
                return false;
            }
            return true;
        }
        if (!this.pseudoClasses.containsAll(other.pseudoClasses)) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        String property = getProperty();
        int hash = (47 * 7) + (property != null ? property.hashCode() : 0);
        return (47 * hash) + (this.pseudoClasses != null ? this.pseudoClasses.hashCode() : 0);
    }

    @Override // java.lang.Comparable
    public int compareTo(CascadingStyle other) {
        int c2;
        Declaration decl = this.style.getDeclaration();
        boolean important = decl != null ? decl.isImportant() : false;
        Rule rule = decl != null ? decl.getRule() : null;
        StyleOrigin source = rule != null ? rule.getOrigin() : null;
        Declaration otherDecl = other.style.getDeclaration();
        boolean otherImportant = otherDecl != null ? otherDecl.isImportant() : false;
        Rule otherRule = otherDecl != null ? otherDecl.getRule() : null;
        StyleOrigin otherSource = otherRule != null ? otherRule.getOrigin() : null;
        if (this.skinProp && !other.skinProp) {
            c2 = 1;
        } else if (important != otherImportant) {
            c2 = important ? -1 : 1;
        } else if (source != otherSource) {
            if (source == null) {
                c2 = -1;
            } else {
                c2 = otherSource == null ? 1 : otherSource.compareTo(source);
            }
        } else {
            c2 = other.specificity - this.specificity;
        }
        if (c2 == 0) {
            c2 = other.ordinal - this.ordinal;
        }
        return c2;
    }
}
