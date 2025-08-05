package com.sun.javafx.css;

/* loaded from: jfxrt.jar:com/sun/javafx/css/Style.class */
public final class Style {
    private final Selector selector;
    private final Declaration declaration;

    public Selector getSelector() {
        return this.selector;
    }

    public Declaration getDeclaration() {
        return this.declaration;
    }

    public Style(Selector selector, Declaration declaration) {
        this.selector = selector;
        this.declaration = declaration;
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Style other = (Style) obj;
        if (this.selector != other.selector && (this.selector == null || !this.selector.equals(other.selector))) {
            return false;
        }
        if (this.declaration == other.declaration) {
            return true;
        }
        if (this.declaration == null || !this.declaration.equals(other.declaration)) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        int hash = (83 * 3) + (this.selector != null ? this.selector.hashCode() : 0);
        return (83 * hash) + (this.declaration != null ? this.declaration.hashCode() : 0);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder().append(String.valueOf(this.selector)).append(" { ").append(String.valueOf(this.declaration)).append(" } ");
        return sb.toString();
    }
}
