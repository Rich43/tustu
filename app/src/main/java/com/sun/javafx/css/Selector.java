package com.sun.javafx.css;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javafx.css.PseudoClass;
import javafx.css.Styleable;
import jdk.internal.dynalink.CallSiteDescriptor;

/* loaded from: jfxrt.jar:com/sun/javafx/css/Selector.class */
public abstract class Selector {
    private Rule rule;
    private int ordinal = -1;
    private static final int TYPE_SIMPLE = 1;
    private static final int TYPE_COMPOUND = 2;

    abstract Match createMatch();

    public abstract boolean applies(Styleable styleable);

    abstract boolean applies(Styleable styleable, Set<PseudoClass>[] setArr, int i2);

    public abstract boolean stateMatches(Styleable styleable, Set<PseudoClass> set);

    /* loaded from: jfxrt.jar:com/sun/javafx/css/Selector$UniversalSelector.class */
    private static class UniversalSelector {
        private static final Selector INSTANCE = new SimpleSelector("*", null, null, null);

        private UniversalSelector() {
        }
    }

    public static Selector getUniversalSelector() {
        return UniversalSelector.INSTANCE;
    }

    void setRule(Rule rule) {
        this.rule = rule;
    }

    Rule getRule() {
        return this.rule;
    }

    void setOrdinal(int ordinal) {
        this.ordinal = ordinal;
    }

    int getOrdinal() {
        return this.ordinal;
    }

    protected void writeBinary(DataOutputStream os, StringStore stringStore) throws IOException {
        if (this instanceof SimpleSelector) {
            os.writeByte(1);
        } else {
            os.writeByte(2);
        }
    }

    static Selector readBinary(int bssVersion, DataInputStream is, String[] strings) throws IOException {
        int type = is.readByte();
        if (type == 1) {
            return SimpleSelector.readBinary(bssVersion, is, strings);
        }
        return CompoundSelector.readBinary(bssVersion, is, strings);
    }

    public static Selector createSelector(String cssSelector) {
        if (cssSelector == null || cssSelector.length() == 0) {
            return null;
        }
        List<SimpleSelector> selectors = new ArrayList<>();
        List<Combinator> combinators = new ArrayList<>();
        List<String> parts = new ArrayList<>();
        int start = 0;
        int end = -1;
        char combinator = 0;
        for (int i2 = 0; i2 < cssSelector.length(); i2++) {
            char ch = cssSelector.charAt(i2);
            if (ch == ' ') {
                if (combinator == 0) {
                    combinator = ch;
                    end = i2;
                }
            } else if (ch == '>') {
                if (combinator == 0) {
                    end = i2;
                }
                combinator = ch;
            } else if (combinator != 0) {
                parts.add(cssSelector.substring(start, end));
                start = i2;
                combinators.add(combinator == ' ' ? Combinator.DESCENDANT : Combinator.CHILD);
                combinator = 0;
            }
        }
        parts.add(cssSelector.substring(start));
        for (int i3 = 0; i3 < parts.size(); i3++) {
            String part = parts.get(i3);
            if (part != null && !part.equals("")) {
                String[] pseudoClassParts = part.split(CallSiteDescriptor.TOKEN_DELIMITER);
                List<String> pseudoClasses = new ArrayList<>();
                for (int j2 = 1; j2 < pseudoClassParts.length; j2++) {
                    if (pseudoClassParts[j2] != null && !pseudoClassParts[j2].equals("")) {
                        pseudoClasses.add(pseudoClassParts[j2].trim());
                    }
                }
                String selector = pseudoClassParts[0].trim();
                String[] styleClassParts = selector.split("\\.");
                List<String> styleClasses = new ArrayList<>();
                for (int j3 = 1; j3 < styleClassParts.length; j3++) {
                    if (styleClassParts[j3] != null && !styleClassParts[j3].equals("")) {
                        styleClasses.add(styleClassParts[j3].trim());
                    }
                }
                String name = null;
                String id = null;
                if (!styleClassParts[0].equals("")) {
                    if (styleClassParts[0].charAt(0) == '#') {
                        id = styleClassParts[0].substring(1).trim();
                    } else {
                        name = styleClassParts[0].trim();
                    }
                }
                selectors.add(new SimpleSelector(name, styleClasses, pseudoClasses, id));
            }
        }
        if (selectors.size() == 1) {
            return selectors.get(0);
        }
        return new CompoundSelector(selectors, combinators);
    }
}
