package javafx.scene;

import com.sun.javafx.css.CalculatedValue;
import com.sun.javafx.css.CascadingStyle;
import com.sun.javafx.css.CssError;
import com.sun.javafx.css.ParsedValueImpl;
import com.sun.javafx.css.PseudoClassState;
import com.sun.javafx.css.Rule;
import com.sun.javafx.css.Selector;
import com.sun.javafx.css.Style;
import com.sun.javafx.css.StyleCache;
import com.sun.javafx.css.StyleCacheEntry;
import com.sun.javafx.css.StyleConverterImpl;
import com.sun.javafx.css.StyleManager;
import com.sun.javafx.css.StyleMap;
import com.sun.javafx.css.Stylesheet;
import com.sun.javafx.css.converters.FontConverter;
import com.sun.javafx.util.Logging;
import com.sun.javafx.util.Utils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.WritableValue;
import javafx.css.CssMetaData;
import javafx.css.FontCssMetaData;
import javafx.css.ParsedValue;
import javafx.css.PseudoClass;
import javafx.css.StyleConverter;
import javafx.css.StyleOrigin;
import javafx.css.Styleable;
import javafx.css.StyleableProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import org.icepdf.core.pobjects.graphics.Separation;
import org.icepdf.core.util.PdfOps;
import sun.util.logging.PlatformLogger;

/* loaded from: jfxrt.jar:javafx/scene/CssStyleHelper.class */
final class CssStyleHelper {
    private static final PlatformLogger LOGGER;
    private CacheContainer cacheContainer;
    private PseudoClassState triggerStates;
    private static final Set<PseudoClass> NULL_PSEUDO_CLASS_STATE;
    private static final CssMetaData dummyFontProperty;
    static final /* synthetic */ boolean $assertionsDisabled;
    private boolean resetInProgress = false;
    private boolean transitionStateInProgress = false;

    static {
        $assertionsDisabled = !CssStyleHelper.class.desiredAssertionStatus();
        LOGGER = Logging.getCSSLogger();
        NULL_PSEUDO_CLASS_STATE = null;
        dummyFontProperty = new FontCssMetaData<Node>("-fx-font", Font.getDefault()) { // from class: javafx.scene.CssStyleHelper.1
            @Override // javafx.css.CssMetaData
            public boolean isSettable(Node node) {
                return true;
            }

            @Override // javafx.css.CssMetaData
            public StyleableProperty<Font> getStyleableProperty(Node node) {
                return null;
            }
        };
    }

    private CssStyleHelper() {
        this.triggerStates = new PseudoClassState();
        this.triggerStates = new PseudoClassState();
    }

    static CssStyleHelper createStyleHelper(Node node) {
        int depth = 0;
        for (Styleable parent = node; parent != null; parent = parent.getStyleableParent()) {
            depth++;
        }
        PseudoClassState[] triggerStates = new PseudoClassState[depth];
        StyleMap styleMap = StyleManager.getInstance().findMatchingStyles(node, node.getSubScene(), triggerStates);
        if (canReuseStyleHelper(node, styleMap)) {
            if (node.styleHelper.cacheContainer != null && node.styleHelper.isUserSetFont(node)) {
                node.styleHelper.cacheContainer.fontSizeCache.clear();
            }
            node.styleHelper.cacheContainer.forceSlowpath = true;
            node.styleHelper.triggerStates.addAll(triggerStates[0]);
            updateParentTriggerStates(node, depth, triggerStates);
            return node.styleHelper;
        }
        if (styleMap == null || styleMap.isEmpty()) {
            boolean mightInherit = false;
            List<CssMetaData<? extends Styleable, ?>> props = node.getCssMetaData();
            int pMax = props != null ? props.size() : 0;
            int p2 = 0;
            while (true) {
                if (p2 >= pMax) {
                    break;
                }
                CssMetaData<? extends Styleable, ?> prop = props.get(p2);
                if (!prop.isInherits()) {
                    p2++;
                } else {
                    mightInherit = true;
                    break;
                }
            }
            if (!mightInherit) {
                if (node.styleHelper != null) {
                    node.styleHelper.resetToInitialValues(node);
                    return null;
                }
                return null;
            }
        }
        CssStyleHelper helper = new CssStyleHelper();
        helper.triggerStates.addAll(triggerStates[0]);
        updateParentTriggerStates(node, depth, triggerStates);
        helper.cacheContainer = new CacheContainer(node, styleMap, depth);
        if (node.styleHelper != null) {
            node.styleHelper.resetToInitialValues(node);
        }
        return helper;
    }

    private static void updateParentTriggerStates(Styleable styleable, int depth, PseudoClassState[] triggerStates) {
        Styleable styleableParent;
        Styleable parent = styleable.getStyleableParent();
        for (int n2 = 1; n2 < depth; n2++) {
            if (!(parent instanceof Node)) {
                styleableParent = parent.getStyleableParent();
            } else {
                Node parentNode = (Node) parent;
                PseudoClassState triggerState = triggerStates[n2];
                if (triggerState != null && triggerState.size() > 0) {
                    if (parentNode.styleHelper == null) {
                        parentNode.styleHelper = new CssStyleHelper();
                    }
                    parentNode.styleHelper.triggerStates.addAll(triggerState);
                }
                styleableParent = parent.getStyleableParent();
            }
            parent = styleableParent;
        }
    }

    private boolean isUserSetFont(Styleable node) {
        if (node == null) {
            return false;
        }
        CssMetaData<Styleable, Font> fontCssMetaData = this.cacheContainer != null ? this.cacheContainer.fontProp : null;
        if (fontCssMetaData != null) {
            StyleableProperty<Font> fontStyleableProperty = fontCssMetaData != null ? fontCssMetaData.getStyleableProperty(node) : null;
            if (fontStyleableProperty != null && fontStyleableProperty.getStyleOrigin() == StyleOrigin.USER) {
                return true;
            }
        }
        CssStyleHelper parentStyleHelper = null;
        Styleable styleableParent = node;
        do {
            styleableParent = styleableParent.getStyleableParent();
            if (styleableParent instanceof Node) {
                parentStyleHelper = ((Node) styleableParent).styleHelper;
            }
            if (parentStyleHelper != null) {
                break;
            }
        } while (styleableParent != null);
        if (parentStyleHelper != null) {
            return parentStyleHelper.isUserSetFont(styleableParent);
        }
        return false;
    }

    private static boolean isTrue(WritableValue<Boolean> booleanProperty) {
        return booleanProperty != null && booleanProperty.getValue().booleanValue();
    }

    private static void setTrue(WritableValue<Boolean> booleanProperty) {
        if (booleanProperty != null) {
            booleanProperty.setValue(true);
        }
    }

    private static boolean canReuseStyleHelper(Node node, StyleMap styleMap) {
        if (node == null || node.styleHelper == null || styleMap == null) {
            return false;
        }
        StyleMap currentMap = node.styleHelper.getStyleMap(node);
        if (currentMap != styleMap) {
            return false;
        }
        if (node.styleHelper.cacheContainer == null) {
            return true;
        }
        CssStyleHelper parentHelper = null;
        Styleable parent = node.getStyleableParent();
        if (parent == null) {
            return true;
        }
        while (parent != null) {
            if (parent instanceof Node) {
                parentHelper = ((Node) parent).styleHelper;
                if (parentHelper != null) {
                    break;
                }
            }
            parent = parent.getStyleableParent();
        }
        if (parentHelper == null || parentHelper.cacheContainer == null) {
            return false;
        }
        int[] parentIds = parentHelper.cacheContainer.styleCacheKey.getStyleMapIds();
        int[] nodeIds = node.styleHelper.cacheContainer.styleCacheKey.getStyleMapIds();
        if (parentIds.length == nodeIds.length - 1) {
            boolean isSame = true;
            int i2 = 0;
            while (true) {
                if (i2 >= parentIds.length) {
                    break;
                }
                if (nodeIds[i2 + 1] == parentIds[i2]) {
                    i2++;
                } else {
                    isSame = false;
                    break;
                }
            }
            return isSame;
        }
        return false;
    }

    /* loaded from: jfxrt.jar:javafx/scene/CssStyleHelper$CacheContainer.class */
    private static final class CacheContainer {
        private final StyleCache.Key styleCacheKey;
        private final CssMetaData<Styleable, Font> fontProp;
        private final int smapId;
        private final Map<StyleCacheEntry.Key, CalculatedValue> fontSizeCache;
        private final Map<CssMetaData, CalculatedValue> cssSetProperties;
        private boolean forceSlowpath;

        /* JADX WARN: Multi-variable type inference failed */
        private CacheContainer(Node node, StyleMap styleMap, int depth) {
            this.forceSlowpath = false;
            int[] smapIds = new int[depth];
            int ctr = 0 + 1;
            int id = styleMap.getId();
            this.smapId = id;
            smapIds[0] = id;
            Styleable parent = node.getStyleableParent();
            for (int d2 = 1; d2 < depth; d2++) {
                if (parent instanceof Node) {
                    Node parentNode = (Node) parent;
                    CssStyleHelper helper = parentNode.styleHelper;
                    if (helper != null && helper.cacheContainer != null) {
                        int i2 = ctr;
                        ctr++;
                        smapIds[i2] = helper.cacheContainer.smapId;
                    }
                }
                parent = parent.getStyleableParent();
            }
            this.styleCacheKey = new StyleCache.Key(smapIds, ctr);
            CssMetaData cssMetaData = null;
            List<CssMetaData<? extends Styleable, ?>> props = node.getCssMetaData();
            int pMax = props != null ? props.size() : 0;
            int p2 = 0;
            while (true) {
                if (p2 >= pMax) {
                    break;
                }
                CssMetaData prop = props.get(p2);
                if (!"-fx-font".equals(prop.getProperty())) {
                    p2++;
                } else {
                    cssMetaData = prop;
                    break;
                }
            }
            this.fontProp = cssMetaData;
            this.fontSizeCache = new HashMap();
            this.cssSetProperties = new HashMap();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public StyleMap getStyleMap(Styleable styleable) {
            if (styleable != null) {
                SubScene subScene = styleable instanceof Node ? ((Node) styleable).getSubScene() : null;
                return StyleManager.getInstance().getStyleMap(styleable, subScene, this.smapId);
            }
            return StyleMap.EMPTY_MAP;
        }
    }

    private void resetToInitialValues(Styleable styleable) {
        if (this.cacheContainer == null || this.cacheContainer.cssSetProperties == null || this.cacheContainer.cssSetProperties.isEmpty()) {
            return;
        }
        this.resetInProgress = true;
        Set<Map.Entry<CssMetaData, CalculatedValue>> entrySet = new HashSet<>(this.cacheContainer.cssSetProperties.entrySet());
        this.cacheContainer.cssSetProperties.clear();
        for (Map.Entry<CssMetaData, CalculatedValue> resetValues : entrySet) {
            CssMetaData metaData = resetValues.getKey();
            StyleableProperty styleableProperty = metaData.getStyleableProperty(styleable);
            StyleOrigin styleOrigin = styleableProperty.getStyleOrigin();
            if (styleOrigin != null && styleOrigin != StyleOrigin.USER) {
                CalculatedValue calculatedValue = resetValues.getValue();
                styleableProperty.applyStyle(calculatedValue.getOrigin(), calculatedValue.getValue());
            }
        }
        this.resetInProgress = false;
    }

    private StyleMap getStyleMap(Styleable styleable) {
        if (this.cacheContainer == null || styleable == null) {
            return null;
        }
        return this.cacheContainer.getStyleMap(styleable);
    }

    boolean pseudoClassStateChanged(PseudoClass pseudoClass) {
        return this.triggerStates.contains(pseudoClass);
    }

    private Set<PseudoClass>[] getTransitionStates(Node node) {
        if (this.cacheContainer == null) {
            return null;
        }
        int depth = 0;
        Node parent = node;
        while (true) {
            Node parent2 = parent;
            if (parent2 == null) {
                break;
            }
            depth++;
            parent = parent2.getParent();
        }
        Set<PseudoClass>[] retainedStates = new PseudoClassState[depth];
        int count = 0;
        Node parent3 = node;
        while (true) {
            Node parent4 = parent3;
            if (parent4 != null) {
                CssStyleHelper helper = parent4 instanceof Node ? parent4.styleHelper : null;
                if (helper != null) {
                    Set<PseudoClass> pseudoClassState = parent4.pseudoClassStates;
                    retainedStates[count] = new PseudoClassState();
                    retainedStates[count].addAll(pseudoClassState);
                    retainedStates[count].retainAll(helper.triggerStates);
                    count++;
                }
                parent3 = parent4.getParent();
            } else {
                Set<PseudoClass>[] transitionStates = new PseudoClassState[count];
                System.arraycopy(retainedStates, 0, transitionStates, 0, count);
                return transitionStates;
            }
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:46:0x011b  */
    /* JADX WARN: Removed duplicated region for block: B:68:0x019d A[Catch: Exception -> 0x020c, TryCatch #0 {Exception -> 0x020c, blocks: (B:51:0x013c, B:54:0x015c, B:56:0x0164, B:59:0x016f, B:63:0x018b, B:68:0x019d, B:70:0x01a9, B:71:0x01df), top: B:79:0x013c }] */
    /* JADX WARN: Removed duplicated region for block: B:70:0x01a9 A[Catch: Exception -> 0x020c, TryCatch #0 {Exception -> 0x020c, blocks: (B:51:0x013c, B:54:0x015c, B:56:0x0164, B:59:0x016f, B:63:0x018b, B:68:0x019d, B:70:0x01a9, B:71:0x01df), top: B:79:0x013c }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    void recalculateRelativeSizeProperties(javafx.scene.Node r10, javafx.scene.text.Font r11) {
        /*
            Method dump skipped, instructions count: 596
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: javafx.scene.CssStyleHelper.recalculateRelativeSizeProperties(javafx.scene.Node, javafx.scene.text.Font):void");
    }

    /* JADX WARN: Removed duplicated region for block: B:105:0x02b4 A[Catch: Exception -> 0x0334, TryCatch #1 {Exception -> 0x0334, blocks: (B:73:0x01e9, B:83:0x0237, B:84:0x0240, B:86:0x025d, B:88:0x0263, B:89:0x026f, B:91:0x0273, B:93:0x027b, B:96:0x0286, B:100:0x02a2, B:105:0x02b4, B:107:0x02c0, B:108:0x02f6, B:110:0x0312, B:75:0x01f1, B:77:0x0209, B:79:0x021e), top: B:143:0x01e9 }] */
    /* JADX WARN: Removed duplicated region for block: B:107:0x02c0 A[Catch: Exception -> 0x0334, TryCatch #1 {Exception -> 0x0334, blocks: (B:73:0x01e9, B:83:0x0237, B:84:0x0240, B:86:0x025d, B:88:0x0263, B:89:0x026f, B:91:0x0273, B:93:0x027b, B:96:0x0286, B:100:0x02a2, B:105:0x02b4, B:107:0x02c0, B:108:0x02f6, B:110:0x0312, B:75:0x01f1, B:77:0x0209, B:79:0x021e), top: B:143:0x01e9 }] */
    /* JADX WARN: Removed duplicated region for block: B:110:0x0312 A[Catch: Exception -> 0x0334, TryCatch #1 {Exception -> 0x0334, blocks: (B:73:0x01e9, B:83:0x0237, B:84:0x0240, B:86:0x025d, B:88:0x0263, B:89:0x026f, B:91:0x0273, B:93:0x027b, B:96:0x0286, B:100:0x02a2, B:105:0x02b4, B:107:0x02c0, B:108:0x02f6, B:110:0x0312, B:75:0x01f1, B:77:0x0209, B:79:0x021e), top: B:143:0x01e9 }] */
    /* JADX WARN: Removed duplicated region for block: B:71:0x01e4 A[PHI: r26
  0x01e4: PHI (r26v2 'calculatedValue' com.sun.javafx.css.CalculatedValue) = 
  (r26v0 'calculatedValue' com.sun.javafx.css.CalculatedValue)
  (r26v1 'calculatedValue' com.sun.javafx.css.CalculatedValue)
  (r26v0 'calculatedValue' com.sun.javafx.css.CalculatedValue)
 binds: [B:64:0x01a9, B:66:0x01bf, B:61:0x01a1] A[DONT_GENERATE, DONT_INLINE]] */
    /* JADX WARN: Removed duplicated region for block: B:75:0x01f1 A[Catch: Exception -> 0x0334, TryCatch #1 {Exception -> 0x0334, blocks: (B:73:0x01e9, B:83:0x0237, B:84:0x0240, B:86:0x025d, B:88:0x0263, B:89:0x026f, B:91:0x0273, B:93:0x027b, B:96:0x0286, B:100:0x02a2, B:105:0x02b4, B:107:0x02c0, B:108:0x02f6, B:110:0x0312, B:75:0x01f1, B:77:0x0209, B:79:0x021e), top: B:143:0x01e9 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    void transitionToState(javafx.scene.Node r9) {
        /*
            Method dump skipped, instructions count: 1085
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: javafx.scene.CssStyleHelper.transitionToState(javafx.scene.Node):void");
    }

    private CascadingStyle getStyle(Styleable styleable, String property, StyleMap styleMap, Set<PseudoClass> states) {
        Map<String, List<CascadingStyle>> cascadingStyleMap;
        List<CascadingStyle> styles;
        if (styleMap == null || styleMap.isEmpty() || (cascadingStyleMap = styleMap.getCascadingStyles()) == null || cascadingStyleMap.isEmpty() || (styles = cascadingStyleMap.get(property)) == null || styles.isEmpty()) {
            return null;
        }
        CascadingStyle style = null;
        int max = styles == null ? 0 : styles.size();
        int i2 = 0;
        while (true) {
            if (i2 >= max) {
                break;
            }
            CascadingStyle s2 = styles.get(i2);
            Selector sel = s2 == null ? null : s2.getSelector();
            if (sel == null || !sel.stateMatches(styleable, states)) {
                i2++;
            } else {
                style = s2;
                break;
            }
        }
        return style;
    }

    private CalculatedValue lookup(Styleable styleable, CssMetaData cssMetaData, StyleMap styleMap, Set<PseudoClass> states, Styleable originatingStyleable, CalculatedValue cachedFont) {
        StyleableProperty styleableProperty;
        if (cssMetaData.getConverter() == FontConverter.getInstance()) {
            return lookupFont(styleable, cssMetaData.getProperty(), styleMap, cachedFont);
        }
        String property = cssMetaData.getProperty();
        CascadingStyle style = getStyle(styleable, property, styleMap, states);
        List<CssMetaData<? extends Styleable, ?>> subProperties = cssMetaData.getSubProperties();
        int numSubProperties = subProperties != null ? subProperties.size() : 0;
        if (style == null) {
            if (numSubProperties == 0) {
                return handleNoStyleFound(styleable, cssMetaData, styleMap, states, originatingStyleable, cachedFont);
            }
            Map<CssMetaData, Object> subs = null;
            StyleOrigin origin = null;
            boolean isRelative = false;
            for (int i2 = 0; i2 < numSubProperties; i2++) {
                CssMetaData subkey = subProperties.get(i2);
                CalculatedValue constituent = lookup(styleable, subkey, styleMap, states, originatingStyleable, cachedFont);
                if (constituent != CalculatedValue.SKIP) {
                    if (subs == null) {
                        subs = new HashMap<>();
                    }
                    subs.put(subkey, constituent.getValue());
                    if (origin == null || constituent.getOrigin() == null ? constituent.getOrigin() != null : origin.compareTo(constituent.getOrigin()) < 0) {
                        origin = constituent.getOrigin();
                    }
                    isRelative = isRelative || constituent.isRelative();
                }
            }
            if (subs == null || subs.isEmpty()) {
                return handleNoStyleFound(styleable, cssMetaData, styleMap, states, originatingStyleable, cachedFont);
            }
            try {
                StyleConverter keyType = cssMetaData.getConverter();
                if (keyType instanceof StyleConverterImpl) {
                    Object ret = ((StyleConverterImpl) keyType).convert(subs);
                    return new CalculatedValue(ret, origin, isRelative);
                }
                if ($assertionsDisabled) {
                    return CalculatedValue.SKIP;
                }
                throw new AssertionError();
            } catch (ClassCastException cce) {
                String msg = formatExceptionMessage(styleable, cssMetaData, null, cce);
                List<CssError> errors = StyleManager.getErrors();
                if (errors != null) {
                    CssError error = new CssError.PropertySetError(cssMetaData, styleable, msg);
                    errors.add(error);
                }
                if (LOGGER.isLoggable(PlatformLogger.Level.WARNING)) {
                    LOGGER.warning(msg);
                    LOGGER.fine("caught: ", cce);
                    LOGGER.fine("styleable = " + ((Object) cssMetaData));
                    LOGGER.fine("node = " + styleable.toString());
                }
                return CalculatedValue.SKIP;
            }
        }
        if (style.getOrigin() == StyleOrigin.USER_AGENT && (styleableProperty = cssMetaData.getStyleableProperty(originatingStyleable)) != null && styleableProperty.getStyleOrigin() == StyleOrigin.USER) {
            return CalculatedValue.SKIP;
        }
        ParsedValueImpl cssValue = style.getParsedValueImpl();
        if (cssValue != null && "inherit".equals(cssValue.getValue())) {
            style = getInheritedStyle(styleable, property);
            if (style == null) {
                return CalculatedValue.SKIP;
            }
        }
        return calculateValue(style, styleable, cssMetaData, styleMap, states, originatingStyleable, cachedFont);
    }

    private CalculatedValue handleNoStyleFound(Styleable styleable, CssMetaData cssMetaData, StyleMap styleMap, Set<PseudoClass> pseudoClassStates, Styleable originatingStyleable, CalculatedValue cachedFont) {
        if (cssMetaData.isInherits()) {
            StyleableProperty styleableProperty = cssMetaData.getStyleableProperty(styleable);
            StyleOrigin origin = styleableProperty != null ? styleableProperty.getStyleOrigin() : null;
            if (origin == StyleOrigin.USER) {
                return CalculatedValue.SKIP;
            }
            CascadingStyle style = getInheritedStyle(styleable, cssMetaData.getProperty());
            if (style == null) {
                return CalculatedValue.SKIP;
            }
            CalculatedValue cv = calculateValue(style, styleable, cssMetaData, styleMap, pseudoClassStates, originatingStyleable, cachedFont);
            return cv;
        }
        return CalculatedValue.SKIP;
    }

    private CascadingStyle getInheritedStyle(Styleable styleable, String property) {
        Styleable styleableParent = styleable != null ? styleable.getStyleableParent() : null;
        while (true) {
            Styleable parent = styleableParent;
            if (parent != null) {
                CssStyleHelper parentStyleHelper = parent instanceof Node ? ((Node) parent).styleHelper : null;
                if (parentStyleHelper != null) {
                    StyleMap parentStyleMap = parentStyleHelper.getStyleMap(parent);
                    Set<PseudoClass> transitionStates = ((Node) parent).pseudoClassStates;
                    CascadingStyle cascadingStyle = parentStyleHelper.getStyle(parent, property, parentStyleMap, transitionStates);
                    if (cascadingStyle != null) {
                        ParsedValueImpl cssValue = cascadingStyle.getParsedValueImpl();
                        if ("inherit".equals(cssValue.getValue())) {
                            return getInheritedStyle(parent, property);
                        }
                        return cascadingStyle;
                    }
                    return null;
                }
                styleableParent = parent.getStyleableParent();
            } else {
                return null;
            }
        }
    }

    private CascadingStyle resolveRef(Styleable styleable, String property, StyleMap styleMap, Set<PseudoClass> states) {
        CascadingStyle style = getStyle(styleable, property, styleMap, states);
        if (style != null) {
            return style;
        }
        if (states != null && states.size() > 0) {
            return resolveRef(styleable, property, styleMap, NULL_PSEUDO_CLASS_STATE);
        }
        Styleable styleableParent = styleable.getStyleableParent();
        CssStyleHelper parentStyleHelper = null;
        if (styleableParent != null && (styleableParent instanceof Node)) {
            parentStyleHelper = ((Node) styleableParent).styleHelper;
        }
        while (styleableParent != null && parentStyleHelper == null) {
            styleableParent = styleableParent.getStyleableParent();
            if (styleableParent != null && (styleableParent instanceof Node)) {
                parentStyleHelper = ((Node) styleableParent).styleHelper;
            }
        }
        if (styleableParent == null || parentStyleHelper == null) {
            return null;
        }
        StyleMap parentStyleMap = parentStyleHelper.getStyleMap(styleableParent);
        Set<PseudoClass> styleableParentPseudoClassStates = styleableParent instanceof Node ? ((Node) styleableParent).pseudoClassStates : styleable.getPseudoClassStates();
        return parentStyleHelper.resolveRef(styleableParent, property, parentStyleMap, styleableParentPseudoClassStates);
    }

    private ParsedValueImpl resolveLookups(Styleable styleable, ParsedValueImpl parsedValue, StyleMap styleMap, Set<PseudoClass> states, ObjectProperty<StyleOrigin> whence, Set<ParsedValue> resolves) {
        String sval;
        CascadingStyle resolved;
        if (parsedValue.isLookup()) {
            Object val = parsedValue.getValue();
            if ((val instanceof String) && (resolved = resolveRef(styleable, (sval = ((String) val).toLowerCase(Locale.ROOT)), styleMap, states)) != null) {
                if (resolves.contains(resolved.getParsedValueImpl())) {
                    if (LOGGER.isLoggable(PlatformLogger.Level.WARNING)) {
                        LOGGER.warning("Loop detected in " + resolved.getRule().toString() + " while resolving '" + sval + PdfOps.SINGLE_QUOTE_TOKEN);
                    }
                    throw new IllegalArgumentException("Loop detected in " + resolved.getRule().toString() + " while resolving '" + sval + PdfOps.SINGLE_QUOTE_TOKEN);
                }
                resolves.add(parsedValue);
                StyleOrigin wOrigin = whence.get();
                StyleOrigin rOrigin = resolved.getOrigin();
                if (rOrigin != null && (wOrigin == null || wOrigin.compareTo(rOrigin) < 0)) {
                    whence.set(rOrigin);
                }
                ParsedValueImpl pv = resolveLookups(styleable, resolved.getParsedValueImpl(), styleMap, states, whence, resolves);
                if (resolves != null) {
                    resolves.remove(parsedValue);
                }
                return pv;
            }
        }
        if (!parsedValue.isContainsLookups()) {
            return parsedValue;
        }
        Object val2 = parsedValue.getValue();
        if (val2 instanceof ParsedValueImpl[][]) {
            ParsedValueImpl[][] layers = (ParsedValueImpl[][]) val2;
            ParsedValueImpl[][] resolved2 = new ParsedValueImpl[layers.length][0];
            for (int l2 = 0; l2 < layers.length; l2++) {
                resolved2[l2] = new ParsedValueImpl[layers[l2].length];
                for (int ll = 0; ll < layers[l2].length; ll++) {
                    if (layers[l2][ll] != null) {
                        resolved2[l2][ll] = resolveLookups(styleable, layers[l2][ll], styleMap, states, whence, resolves);
                    }
                }
            }
            resolves.clear();
            return new ParsedValueImpl(resolved2, parsedValue.getConverter(), false);
        }
        if (val2 instanceof ParsedValueImpl[]) {
            ParsedValueImpl[] layer = (ParsedValueImpl[]) val2;
            ParsedValueImpl[] resolved3 = new ParsedValueImpl[layer.length];
            for (int l3 = 0; l3 < layer.length; l3++) {
                if (layer[l3] != null) {
                    resolved3[l3] = resolveLookups(styleable, layer[l3], styleMap, states, whence, resolves);
                }
            }
            resolves.clear();
            return new ParsedValueImpl(resolved3, parsedValue.getConverter(), false);
        }
        return parsedValue;
    }

    private String getUnresolvedLookup(ParsedValueImpl resolved) {
        String unresolvedLookup;
        String unresolvedLookup2;
        Object value = resolved.getValue();
        if (resolved.isLookup() && (value instanceof String)) {
            return (String) value;
        }
        if (!(value instanceof ParsedValueImpl[][])) {
            if (value instanceof ParsedValueImpl[]) {
                ParsedValueImpl[] layer = (ParsedValueImpl[]) value;
                for (int l2 = 0; l2 < layer.length; l2++) {
                    if (layer[l2] != null && (unresolvedLookup = getUnresolvedLookup(layer[l2])) != null) {
                        return unresolvedLookup;
                    }
                }
                return null;
            }
            return null;
        }
        ParsedValueImpl[][] layers = (ParsedValueImpl[][]) value;
        for (int l3 = 0; l3 < layers.length; l3++) {
            for (int ll = 0; ll < layers[l3].length; ll++) {
                if (layers[l3][ll] != null && (unresolvedLookup2 = getUnresolvedLookup(layers[l3][ll])) != null) {
                    return unresolvedLookup2;
                }
            }
        }
        return null;
    }

    private String formatUnresolvedLookupMessage(Styleable styleable, CssMetaData cssMetaData, Style style, ParsedValueImpl resolved, ClassCastException cce) {
        String missingLookup = (resolved == null || !resolved.isContainsLookups()) ? null : getUnresolvedLookup(resolved);
        StringBuilder sbuf = new StringBuilder();
        if (missingLookup != null) {
            sbuf.append("Could not resolve '").append(missingLookup).append(PdfOps.SINGLE_QUOTE_TOKEN).append(" while resolving lookups for '").append(cssMetaData.getProperty()).append(PdfOps.SINGLE_QUOTE_TOKEN);
        } else {
            sbuf.append("Caught '").append((Object) cce).append(PdfOps.SINGLE_QUOTE_TOKEN).append(" while converting value for '").append(cssMetaData.getProperty()).append(PdfOps.SINGLE_QUOTE_TOKEN);
        }
        Rule rule = style != null ? style.getDeclaration().getRule() : null;
        Stylesheet stylesheet = rule != null ? rule.getStylesheet() : null;
        String url = stylesheet != null ? stylesheet.getUrl() : null;
        if (url != null) {
            sbuf.append(" from rule '").append((Object) style.getSelector()).append("' in stylesheet ").append(url);
        } else if (stylesheet != null && StyleOrigin.INLINE == stylesheet.getOrigin()) {
            sbuf.append(" from inline style on ").append(styleable.toString());
        }
        return sbuf.toString();
    }

    private String formatExceptionMessage(Styleable styleable, CssMetaData cssMetaData, Style style, Exception e2) {
        StringBuilder sbuf = new StringBuilder();
        sbuf.append("Caught ").append(String.valueOf(e2));
        if (cssMetaData != null) {
            sbuf.append(PdfOps.SINGLE_QUOTE_TOKEN).append(" while calculating value for '").append(cssMetaData.getProperty()).append(PdfOps.SINGLE_QUOTE_TOKEN);
        }
        if (style != null) {
            Rule rule = style.getDeclaration().getRule();
            Stylesheet stylesheet = rule != null ? rule.getStylesheet() : null;
            String url = stylesheet != null ? stylesheet.getUrl() : null;
            if (url != null) {
                sbuf.append(" from rule '").append((Object) style.getSelector()).append("' in stylesheet ").append(url);
            } else if (styleable != null && stylesheet != null && StyleOrigin.INLINE == stylesheet.getOrigin()) {
                sbuf.append(" from inline style on ").append(styleable.toString());
            } else {
                sbuf.append(" from style '").append(String.valueOf(style)).append(PdfOps.SINGLE_QUOTE_TOKEN);
            }
        }
        return sbuf.toString();
    }

    /* JADX WARN: Multi-variable type inference failed */
    private CalculatedValue calculateValue(CascadingStyle style, Styleable styleable, CssMetaData cssMetaData, StyleMap styleMap, Set<PseudoClass> states, Styleable originatingStyleable, CalculatedValue fontFromCacheEntry) {
        Object val;
        Styleable styleableParent;
        ParsedValueImpl cssValue = style.getParsedValueImpl();
        if (cssValue != null && !FXMLLoader.NULL_KEYWORD.equals(cssValue.getValue()) && !Separation.COLORANT_NONE.equals(cssValue.getValue())) {
            ParsedValueImpl resolved = null;
            try {
                ObjectProperty<StyleOrigin> whence = new SimpleObjectProperty<>(style.getOrigin());
                resolved = resolveLookups(styleable, cssValue, styleMap, states, whence, new HashSet());
                String property = cssMetaData.getProperty();
                boolean isFontProperty = "-fx-font".equals(property) || "-fx-font-size".equals(property);
                boolean isRelative = ParsedValueImpl.containsFontRelativeSize(resolved, isFontProperty);
                Font fontForFontRelativeSizes = null;
                if (isRelative && isFontProperty && (fontFromCacheEntry == null || fontFromCacheEntry.isRelative())) {
                    Styleable parent = styleable;
                    CalculatedValue childsCachedFont = fontFromCacheEntry;
                    do {
                        CalculatedValue parentsCachedFont = getCachedFont(parent.getStyleableParent());
                        if (parentsCachedFont != null) {
                            if (parentsCachedFont.isRelative()) {
                                if (childsCachedFont == null || parentsCachedFont.equals(childsCachedFont)) {
                                    childsCachedFont = parentsCachedFont;
                                } else {
                                    fontForFontRelativeSizes = (Font) parentsCachedFont.getValue();
                                }
                            } else {
                                fontForFontRelativeSizes = (Font) parentsCachedFont.getValue();
                            }
                        }
                        if (fontForFontRelativeSizes != null) {
                            break;
                        }
                        styleableParent = parent.getStyleableParent();
                        parent = styleableParent;
                    } while (styleableParent != null);
                }
                if (fontForFontRelativeSizes == null) {
                    if (fontFromCacheEntry != null && (!fontFromCacheEntry.isRelative() || !isFontProperty)) {
                        fontForFontRelativeSizes = (Font) fontFromCacheEntry.getValue();
                    } else {
                        fontForFontRelativeSizes = Font.getDefault();
                    }
                }
                StyleConverter cssMetaDataConverter = cssMetaData.getConverter();
                if (cssMetaDataConverter == StyleConverter.getInsetsConverter()) {
                    if (resolved.getValue() instanceof ParsedValue) {
                        resolved = new ParsedValueImpl(new ParsedValue[]{(ParsedValue) resolved.getValue()}, null, false);
                    }
                    val = cssMetaDataConverter.convert(resolved, fontForFontRelativeSizes);
                } else if (resolved.getConverter() != null) {
                    val = resolved.convert(fontForFontRelativeSizes);
                } else {
                    val = cssMetaData.getConverter().convert(resolved, fontForFontRelativeSizes);
                }
                StyleOrigin origin = whence.get();
                return new CalculatedValue(val, origin, isRelative);
            } catch (ClassCastException cce) {
                String msg = formatUnresolvedLookupMessage(styleable, cssMetaData, style.getStyle(), resolved, cce);
                List<CssError> errors = StyleManager.getErrors();
                if (errors != null) {
                    CssError error = new CssError.PropertySetError(cssMetaData, styleable, msg);
                    errors.add(error);
                }
                if (LOGGER.isLoggable(PlatformLogger.Level.WARNING)) {
                    LOGGER.warning(msg);
                    LOGGER.fine("node = " + styleable.toString());
                    LOGGER.fine("cssMetaData = " + ((Object) cssMetaData));
                    LOGGER.fine("styles = " + ((Object) getMatchingStyles(styleable, cssMetaData)));
                }
                return CalculatedValue.SKIP;
            } catch (IllegalArgumentException iae) {
                String msg2 = formatExceptionMessage(styleable, cssMetaData, style.getStyle(), iae);
                List<CssError> errors2 = StyleManager.getErrors();
                if (errors2 != null) {
                    CssError error2 = new CssError.PropertySetError(cssMetaData, styleable, msg2);
                    errors2.add(error2);
                }
                if (LOGGER.isLoggable(PlatformLogger.Level.WARNING)) {
                    LOGGER.warning(msg2);
                    LOGGER.fine("caught: ", iae);
                    LOGGER.fine("styleable = " + ((Object) cssMetaData));
                    LOGGER.fine("node = " + styleable.toString());
                }
                return CalculatedValue.SKIP;
            } catch (NullPointerException npe) {
                String msg3 = formatExceptionMessage(styleable, cssMetaData, style.getStyle(), npe);
                List<CssError> errors3 = StyleManager.getErrors();
                if (errors3 != null) {
                    CssError error3 = new CssError.PropertySetError(cssMetaData, styleable, msg3);
                    errors3.add(error3);
                }
                if (LOGGER.isLoggable(PlatformLogger.Level.WARNING)) {
                    LOGGER.warning(msg3);
                    LOGGER.fine("caught: ", npe);
                    LOGGER.fine("styleable = " + ((Object) cssMetaData));
                    LOGGER.fine("node = " + styleable.toString());
                }
                return CalculatedValue.SKIP;
            }
        }
        return new CalculatedValue(null, style.getOrigin(), false);
    }

    private CalculatedValue getCachedFont(Styleable styleable) {
        if (!(styleable instanceof Node)) {
            return null;
        }
        CalculatedValue cachedFont = null;
        Node parent = (Node) styleable;
        CssStyleHelper parentHelper = parent.styleHelper;
        if (parentHelper == null || parentHelper.cacheContainer == null) {
            cachedFont = getCachedFont(parent.getStyleableParent());
        } else {
            CacheContainer parentCacheContainer = parentHelper.cacheContainer;
            if (parentCacheContainer != null && parentCacheContainer.fontSizeCache != null && !parentCacheContainer.fontSizeCache.isEmpty()) {
                Set<PseudoClass>[] transitionStates = parentHelper.getTransitionStates(parent);
                StyleCacheEntry.Key parentCacheEntryKey = new StyleCacheEntry.Key(transitionStates, Font.getDefault());
                cachedFont = (CalculatedValue) parentCacheContainer.fontSizeCache.get(parentCacheEntryKey);
            }
            if (cachedFont == null) {
                StyleMap smap = parentHelper.getStyleMap(parent);
                cachedFont = parentHelper.lookupFont(parent, "-fx-font", smap, null);
            }
        }
        if (cachedFont != CalculatedValue.SKIP) {
            return cachedFont;
        }
        return null;
    }

    FontPosture getFontPosture(Font font) {
        if (font == null) {
            return FontPosture.REGULAR;
        }
        String fontName = font.getName().toLowerCase(Locale.ROOT);
        if (fontName.contains("italic")) {
            return FontPosture.ITALIC;
        }
        return FontPosture.REGULAR;
    }

    FontWeight getFontWeight(Font font) {
        if (font == null) {
            return FontWeight.NORMAL;
        }
        String fontName = font.getName().toLowerCase(Locale.ROOT);
        if (fontName.contains("bold")) {
            if (!fontName.contains("extra") && !fontName.contains("ultra")) {
                if (!fontName.contains("semi") && !fontName.contains("demi")) {
                    return FontWeight.BOLD;
                }
                return FontWeight.SEMI_BOLD;
            }
            return FontWeight.EXTRA_BOLD;
        }
        if (fontName.contains("light")) {
            if (!fontName.contains("extra") && !fontName.contains("ultra")) {
                return FontWeight.LIGHT;
            }
            return FontWeight.EXTRA_LIGHT;
        }
        if (fontName.contains("black")) {
            return FontWeight.BLACK;
        }
        if (fontName.contains("heavy")) {
            return FontWeight.BLACK;
        }
        if (fontName.contains("medium")) {
            return FontWeight.MEDIUM;
        }
        return FontWeight.NORMAL;
    }

    String getFontFamily(Font font) {
        return font == null ? Font.getDefault().getFamily() : font.getFamily();
    }

    Font deriveFont(Font font, String fontFamily, FontWeight fontWeight, FontPosture fontPosture, double fontSize) {
        if (font != null && fontFamily == null) {
            fontFamily = getFontFamily(font);
        } else if (fontFamily != null) {
            fontFamily = Utils.stripQuotes(fontFamily);
        }
        if (font != null && fontWeight == null) {
            fontWeight = getFontWeight(font);
        }
        if (font != null && fontPosture == null) {
            fontPosture = getFontPosture(font);
        }
        if (font != null && fontSize <= 0.0d) {
            fontSize = font.getSize();
        }
        return Font.font(fontFamily, fontWeight, fontPosture, fontSize);
    }

    CalculatedValue lookupFont(Styleable styleable, String property, StyleMap styleMap, CalculatedValue cachedFont) {
        StyleOrigin origin = null;
        int distance = 0;
        boolean foundStyle = false;
        String family = null;
        double size = -1.0d;
        FontWeight weight = null;
        FontPosture posture = null;
        CalculatedValue cvFont = cachedFont;
        Set<PseudoClass> states = styleable instanceof Node ? ((Node) styleable).pseudoClassStates : styleable.getPseudoClassStates();
        if (this.cacheContainer.fontProp != null) {
            StyleableProperty<Font> styleableProp = this.cacheContainer.fontProp.getStyleableProperty(styleable);
            StyleOrigin fpOrigin = styleableProp.getStyleOrigin();
            Font font = styleableProp.getValue();
            if (font == null) {
                font = Font.getDefault();
            }
            if (fpOrigin == StyleOrigin.USER) {
                origin = fpOrigin;
                family = getFontFamily(font);
                size = font.getSize();
                weight = getFontWeight(font);
                posture = getFontPosture(font);
                cvFont = new CalculatedValue(font, fpOrigin, false);
            }
        }
        CalculatedValue parentCachedFont = getCachedFont(styleable.getStyleableParent());
        if (parentCachedFont == null) {
            parentCachedFont = new CalculatedValue(Font.getDefault(), null, false);
        }
        CascadingStyle fontShorthand = getStyle(styleable, property, styleMap, states);
        if (fontShorthand == null && origin != StyleOrigin.USER) {
            Styleable styleableParent = styleable != null ? styleable.getStyleableParent() : null;
            while (true) {
                Styleable parent = styleableParent;
                if (parent == null) {
                    break;
                }
                CssStyleHelper parentStyleHelper = parent instanceof Node ? ((Node) parent).styleHelper : null;
                if (parentStyleHelper != null) {
                    distance++;
                    StyleMap parentStyleMap = parentStyleHelper.getStyleMap(parent);
                    Set<PseudoClass> transitionStates = ((Node) parent).pseudoClassStates;
                    CascadingStyle cascadingStyle = parentStyleHelper.getStyle(parent, property, parentStyleMap, transitionStates);
                    if (cascadingStyle != null) {
                        ParsedValueImpl cssValue = cascadingStyle.getParsedValueImpl();
                        if (!"inherit".equals(cssValue.getValue())) {
                            fontShorthand = cascadingStyle;
                            break;
                        }
                    } else {
                        continue;
                    }
                }
                styleableParent = parent.getStyleableParent();
            }
        }
        if (fontShorthand != null && (origin == null || origin.compareTo(fontShorthand.getOrigin()) <= 0)) {
            CalculatedValue cv = calculateValue(fontShorthand, styleable, dummyFontProperty, styleMap, states, styleable, parentCachedFont);
            if (cv.getValue() instanceof Font) {
                origin = cv.getOrigin();
                Font font2 = (Font) cv.getValue();
                family = getFontFamily(font2);
                size = font2.getSize();
                weight = getFontWeight(font2);
                posture = getFontPosture(font2);
                cvFont = cv;
                foundStyle = true;
            }
        }
        CascadingStyle fontSize = getStyle(styleable, property.concat("-size"), styleMap, states);
        if (fontSize != null) {
            if (fontShorthand != null && fontShorthand.compareTo(fontSize) < 0) {
                fontSize = null;
            } else if (origin == StyleOrigin.USER && StyleOrigin.USER.compareTo(fontSize.getOrigin()) > 0) {
                fontSize = null;
            }
        } else if (origin != StyleOrigin.USER) {
            fontSize = lookupInheritedFontProperty(styleable, property.concat("-size"), styleMap, distance, fontShorthand);
        }
        if (fontSize != null) {
            CalculatedValue cv2 = calculateValue(fontSize, styleable, dummyFontProperty, styleMap, states, styleable, parentCachedFont);
            if (cv2.getValue() instanceof Double) {
                if (origin == null || origin.compareTo(fontSize.getOrigin()) <= 0) {
                    origin = cv2.getOrigin();
                }
                size = ((Double) cv2.getValue()).doubleValue();
                if (cvFont != null) {
                    boolean isRelative = cvFont.isRelative() || cv2.isRelative();
                    Font font3 = deriveFont((Font) cvFont.getValue(), family, weight, posture, size);
                    cvFont = new CalculatedValue(font3, origin, isRelative);
                } else {
                    boolean isRelative2 = cv2.isRelative();
                    Font font4 = deriveFont(Font.getDefault(), family, weight, posture, size);
                    cvFont = new CalculatedValue(font4, origin, isRelative2);
                }
                foundStyle = true;
            }
        }
        if (cachedFont == null) {
            return cvFont != null ? cvFont : CalculatedValue.SKIP;
        }
        CascadingStyle fontWeight = getStyle(styleable, property.concat("-weight"), styleMap, states);
        if (fontWeight != null) {
            if (fontShorthand != null && fontShorthand.compareTo(fontWeight) < 0) {
                fontWeight = null;
            }
        } else if (origin != StyleOrigin.USER) {
            fontWeight = lookupInheritedFontProperty(styleable, property.concat("-weight"), styleMap, distance, fontShorthand);
        }
        if (fontWeight != null) {
            CalculatedValue cv3 = calculateValue(fontWeight, styleable, dummyFontProperty, styleMap, states, styleable, null);
            if (cv3.getValue() instanceof FontWeight) {
                if (origin == null || origin.compareTo(fontWeight.getOrigin()) <= 0) {
                    origin = cv3.getOrigin();
                }
                weight = (FontWeight) cv3.getValue();
                foundStyle = true;
            }
        }
        CascadingStyle fontStyle = getStyle(styleable, property.concat("-style"), styleMap, states);
        if (fontStyle != null) {
            if (fontShorthand != null && fontShorthand.compareTo(fontStyle) < 0) {
                fontStyle = null;
            }
        } else if (origin != StyleOrigin.USER) {
            fontStyle = lookupInheritedFontProperty(styleable, property.concat("-style"), styleMap, distance, fontShorthand);
        }
        if (fontStyle != null) {
            CalculatedValue cv4 = calculateValue(fontStyle, styleable, dummyFontProperty, styleMap, states, styleable, null);
            if (cv4.getValue() instanceof FontPosture) {
                if (origin == null || origin.compareTo(fontStyle.getOrigin()) <= 0) {
                    origin = cv4.getOrigin();
                }
                posture = (FontPosture) cv4.getValue();
                foundStyle = true;
            }
        }
        CascadingStyle fontFamily = getStyle(styleable, property.concat("-family"), styleMap, states);
        if (fontFamily != null) {
            if (fontShorthand != null && fontShorthand.compareTo(fontFamily) < 0) {
                fontFamily = null;
            }
        } else if (origin != StyleOrigin.USER) {
            fontFamily = lookupInheritedFontProperty(styleable, property.concat("-family"), styleMap, distance, fontShorthand);
        }
        if (fontFamily != null) {
            CalculatedValue cv5 = calculateValue(fontFamily, styleable, dummyFontProperty, styleMap, states, styleable, null);
            if (cv5.getValue() instanceof String) {
                if (origin == null || origin.compareTo(fontFamily.getOrigin()) <= 0) {
                    origin = cv5.getOrigin();
                }
                family = (String) cv5.getValue();
                foundStyle = true;
            }
        }
        if (foundStyle) {
            Font font5 = cvFont != null ? (Font) cvFont.getValue() : Font.getDefault();
            Font derivedFont = deriveFont(font5, family, weight, posture, size);
            return new CalculatedValue(derivedFont, origin, false);
        }
        return CalculatedValue.SKIP;
    }

    private CascadingStyle lookupInheritedFontProperty(Styleable styleable, String property, StyleMap styleMap, int distance, CascadingStyle fontShorthand) {
        int nlooks = distance;
        for (Styleable parent = styleable != null ? styleable.getStyleableParent() : null; parent != null && nlooks > 0; parent = parent.getStyleableParent()) {
            CssStyleHelper parentStyleHelper = parent instanceof Node ? ((Node) parent).styleHelper : null;
            if (parentStyleHelper != null) {
                nlooks--;
                StyleMap parentStyleMap = parentStyleHelper.getStyleMap(parent);
                Set<PseudoClass> transitionStates = ((Node) parent).pseudoClassStates;
                CascadingStyle cascadingStyle = parentStyleHelper.getStyle(parent, property, parentStyleMap, transitionStates);
                if (cascadingStyle == null) {
                    continue;
                } else {
                    if (fontShorthand != null && nlooks == 0 && fontShorthand.compareTo(cascadingStyle) < 0) {
                        return null;
                    }
                    ParsedValueImpl cssValue = cascadingStyle.getParsedValueImpl();
                    if (!"inherit".equals(cssValue.getValue())) {
                        return cascadingStyle;
                    }
                }
            }
        }
        return null;
    }

    static List<Style> getMatchingStyles(Styleable styleable, CssMetaData styleableProperty) {
        if (!(styleable instanceof Node)) {
            return Collections.emptyList();
        }
        Node node = (Node) styleable;
        CssStyleHelper helper = node.styleHelper != null ? node.styleHelper : createStyleHelper(node);
        if (helper != null) {
            return helper.getMatchingStyles(node, styleableProperty, false);
        }
        return Collections.emptyList();
    }

    static Map<StyleableProperty<?>, List<Style>> getMatchingStyles(Map<StyleableProperty<?>, List<Style>> map, Node node) {
        CssStyleHelper helper = node.styleHelper != null ? node.styleHelper : createStyleHelper(node);
        if (helper != null) {
            if (map == null) {
                map = new HashMap();
            }
            for (CssMetaData metaData : node.getCssMetaData()) {
                List<Style> styleList = helper.getMatchingStyles(node, metaData, true);
                if (styleList != null && !styleList.isEmpty()) {
                    StyleableProperty prop = metaData.getStyleableProperty(node);
                    map.put(prop, styleList);
                }
            }
        }
        if (node instanceof Parent) {
            for (Node child : ((Parent) node).getChildren()) {
                map = getMatchingStyles(map, child);
            }
        }
        return map;
    }

    private List<Style> getMatchingStyles(Styleable node, CssMetaData styleableProperty, boolean matchState) {
        List<CascadingStyle> styleList = new ArrayList<>();
        getMatchingStyles(node, styleableProperty, styleList, matchState);
        List<CssMetaData<? extends Styleable, ?>> subProperties = styleableProperty.getSubProperties();
        if (subProperties != null) {
            int nMax = subProperties.size();
            for (int n2 = 0; n2 < nMax; n2++) {
                CssMetaData subProperty = subProperties.get(n2);
                getMatchingStyles(node, subProperty, styleList, matchState);
            }
        }
        Collections.sort(styleList);
        List<Style> matchingStyles = new ArrayList<>(styleList.size());
        int nMax2 = styleList.size();
        for (int n3 = 0; n3 < nMax2; n3++) {
            Style style = styleList.get(n3).getStyle();
            if (!matchingStyles.contains(style)) {
                matchingStyles.add(style);
            }
        }
        return matchingStyles;
    }

    private void getMatchingStyles(Styleable node, CssMetaData styleableProperty, List<CascadingStyle> styleList, boolean matchState) {
        if (node != null) {
            String property = styleableProperty.getProperty();
            Node _node = node instanceof Node ? (Node) node : null;
            StyleMap smap = getStyleMap(_node);
            if (smap == null) {
                return;
            }
            if (matchState) {
                CascadingStyle cascadingStyle = getStyle(node, styleableProperty.getProperty(), smap, _node.pseudoClassStates);
                if (cascadingStyle != null) {
                    styleList.add(cascadingStyle);
                    ParsedValueImpl parsedValue = cascadingStyle.getParsedValueImpl();
                    getMatchingLookupStyles(node, parsedValue, styleList, matchState);
                }
            } else {
                Map<String, List<CascadingStyle>> cascadingStyleMap = smap.getCascadingStyles();
                List<CascadingStyle> styles = cascadingStyleMap.get(property);
                if (styles != null) {
                    styleList.addAll(styles);
                    int nMax = styles.size();
                    for (int n2 = 0; n2 < nMax; n2++) {
                        CascadingStyle style = styles.get(n2);
                        ParsedValueImpl parsedValue2 = style.getParsedValueImpl();
                        getMatchingLookupStyles(node, parsedValue2, styleList, matchState);
                    }
                }
            }
            if (styleableProperty.isInherits()) {
                Styleable styleableParent = node.getStyleableParent();
                while (true) {
                    Styleable parent = styleableParent;
                    if (parent != null) {
                        CssStyleHelper parentHelper = parent instanceof Node ? ((Node) parent).styleHelper : null;
                        if (parentHelper != null) {
                            parentHelper.getMatchingStyles(parent, styleableProperty, styleList, matchState);
                        }
                        styleableParent = parent.getStyleableParent();
                    } else {
                        return;
                    }
                }
            }
        }
    }

    private void getMatchingLookupStyles(Styleable node, ParsedValueImpl parsedValue, List<CascadingStyle> styleList, boolean matchState) {
        Styleable styleableParent;
        StyleMap styleMap;
        if (parsedValue.isLookup()) {
            Object value = parsedValue.getValue();
            if (value instanceof String) {
                String property = (String) value;
                Styleable parent = node;
                do {
                    Node _parent = parent instanceof Node ? (Node) parent : null;
                    CssStyleHelper helper = _parent != null ? _parent.styleHelper : null;
                    if (helper != null && (styleMap = helper.getStyleMap(parent)) != null && !styleMap.isEmpty()) {
                        int start = styleList.size();
                        if (matchState) {
                            CascadingStyle cascadingStyle = helper.resolveRef(_parent, property, styleMap, _parent.pseudoClassStates);
                            if (cascadingStyle != null) {
                                styleList.add(cascadingStyle);
                            }
                        } else {
                            Map<String, List<CascadingStyle>> smap = styleMap.getCascadingStyles();
                            List<CascadingStyle> styles = smap.get(property);
                            if (styles != null) {
                                styleList.addAll(styles);
                            }
                        }
                        int end = styleList.size();
                        for (int index = start; index < end; index++) {
                            CascadingStyle style = styleList.get(index);
                            getMatchingLookupStyles(parent, style.getParsedValueImpl(), styleList, matchState);
                        }
                    }
                    styleableParent = parent.getStyleableParent();
                    parent = styleableParent;
                } while (styleableParent != null);
            }
        }
        if (!parsedValue.isContainsLookups()) {
            return;
        }
        Object val = parsedValue.getValue();
        if (!(val instanceof ParsedValueImpl[][])) {
            if (val instanceof ParsedValueImpl[]) {
                ParsedValueImpl[] layer = (ParsedValueImpl[]) val;
                for (int l2 = 0; l2 < layer.length; l2++) {
                    if (layer[l2] != null) {
                        getMatchingLookupStyles(node, layer[l2], styleList, matchState);
                    }
                }
                return;
            }
            return;
        }
        ParsedValueImpl[][] layers = (ParsedValueImpl[][]) val;
        for (int l3 = 0; l3 < layers.length; l3++) {
            for (int ll = 0; ll < layers[l3].length; ll++) {
                if (layers[l3][ll] != null) {
                    getMatchingLookupStyles(node, layers[l3][ll], styleList, matchState);
                }
            }
        }
    }
}
