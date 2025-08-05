package com.sun.javafx.css;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* loaded from: jfxrt.jar:com/sun/javafx/css/StyleMap.class */
public final class StyleMap {
    public static final StyleMap EMPTY_MAP;
    private static final Comparator<CascadingStyle> cascadingStyleComparator;
    private final int id;
    private List<Selector> selectors;
    private Map<String, List<CascadingStyle>> cascadingStyles;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !StyleMap.class.desiredAssertionStatus();
        EMPTY_MAP = new StyleMap(-1, Collections.emptyList());
        cascadingStyleComparator = (o1, o2) -> {
            String thisProperty = o1.getProperty();
            String otherProperty = o2.getProperty();
            int c2 = thisProperty.compareTo(otherProperty);
            return c2 != 0 ? c2 : o1.compareTo(o2);
        };
    }

    StyleMap(int id, List<Selector> selectors) {
        this.id = id;
        this.selectors = selectors;
    }

    public int getId() {
        return this.id;
    }

    public boolean isEmpty() {
        if (this.selectors != null) {
            return this.selectors.isEmpty();
        }
        if (this.cascadingStyles != null) {
            return this.cascadingStyles.isEmpty();
        }
        return true;
    }

    public Map<String, List<CascadingStyle>> getCascadingStyles() {
        if (this.cascadingStyles == null) {
            if (this.selectors == null || this.selectors.isEmpty()) {
                this.cascadingStyles = Collections.emptyMap();
                return this.cascadingStyles;
            }
            List<CascadingStyle> cascadingStyleList = new ArrayList<>();
            int ordinal = 0;
            int iMax = this.selectors.size();
            for (int i2 = 0; i2 < iMax; i2++) {
                Selector selector = this.selectors.get(i2);
                Match match = selector.createMatch();
                Rule rule = selector.getRule();
                int dmax = rule.getDeclarations().size();
                for (int d2 = 0; d2 < dmax; d2++) {
                    Declaration decl = rule.getDeclarations().get(d2);
                    int i3 = ordinal;
                    ordinal++;
                    CascadingStyle s2 = new CascadingStyle(new Style(match.selector, decl), match.pseudoClasses, match.specificity, i3);
                    cascadingStyleList.add(s2);
                }
            }
            if (cascadingStyleList.isEmpty()) {
                this.cascadingStyles = Collections.emptyMap();
                return this.cascadingStyles;
            }
            Collections.sort(cascadingStyleList, cascadingStyleComparator);
            int nCascadingStyles = cascadingStyleList.size();
            this.cascadingStyles = new HashMap(nCascadingStyles);
            CascadingStyle cascadingStyle = cascadingStyleList.get(0);
            String property = cascadingStyle.getProperty();
            int fromIndex = 0;
            while (fromIndex < nCascadingStyles) {
                List<CascadingStyle> value = this.cascadingStyles.get(property);
                if (value == null) {
                    int toIndex = fromIndex;
                    String currentProperty = property;
                    do {
                        toIndex++;
                        if (toIndex >= nCascadingStyles) {
                            break;
                        }
                        CascadingStyle cascadingStyle2 = cascadingStyleList.get(toIndex);
                        property = cascadingStyle2.getProperty();
                    } while (property.equals(currentProperty));
                    this.cascadingStyles.put(currentProperty, cascadingStyleList.subList(fromIndex, toIndex));
                    fromIndex = toIndex;
                } else if (!$assertionsDisabled) {
                    throw new AssertionError();
                }
            }
            this.selectors.clear();
            this.selectors = null;
        }
        return this.cascadingStyles;
    }
}
