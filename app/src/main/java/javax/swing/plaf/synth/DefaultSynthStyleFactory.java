package javax.swing.plaf.synth;

import java.awt.Font;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.PatternSyntaxException;
import javax.swing.JComponent;
import javax.swing.plaf.FontUIResource;
import sun.swing.BakedArrayList;
import sun.swing.plaf.synth.DefaultSynthStyle;
import sun.swing.plaf.synth.StyleAssociation;

/* loaded from: rt.jar:javax/swing/plaf/synth/DefaultSynthStyleFactory.class */
class DefaultSynthStyleFactory extends SynthStyleFactory {
    public static final int NAME = 0;
    public static final int REGION = 1;
    private SynthStyle _defaultStyle;
    private BakedArrayList _tmpList = new BakedArrayList(5);
    private List<StyleAssociation> _styles = new ArrayList();
    private Map<BakedArrayList, SynthStyle> _resolvedStyles = new HashMap();

    DefaultSynthStyleFactory() {
    }

    public synchronized void addStyle(DefaultSynthStyle defaultSynthStyle, String str, int i2) throws PatternSyntaxException {
        if (str == null) {
            str = ".*";
        }
        if (i2 == 0) {
            this._styles.add(StyleAssociation.createStyleAssociation(str, defaultSynthStyle, i2));
        } else if (i2 == 1) {
            this._styles.add(StyleAssociation.createStyleAssociation(str.toLowerCase(), defaultSynthStyle, i2));
        }
    }

    @Override // javax.swing.plaf.synth.SynthStyleFactory
    public synchronized SynthStyle getStyle(JComponent jComponent, Region region) {
        BakedArrayList bakedArrayList = this._tmpList;
        bakedArrayList.clear();
        getMatchingStyles(bakedArrayList, jComponent, region);
        if (bakedArrayList.size() == 0) {
            return getDefaultStyle();
        }
        bakedArrayList.cacheHashCode();
        SynthStyle cachedStyle = getCachedStyle(bakedArrayList);
        if (cachedStyle == null) {
            cachedStyle = mergeStyles(bakedArrayList);
            if (cachedStyle != null) {
                cacheStyle(bakedArrayList, cachedStyle);
            }
        }
        return cachedStyle;
    }

    private SynthStyle getDefaultStyle() {
        if (this._defaultStyle == null) {
            this._defaultStyle = new DefaultSynthStyle();
            ((DefaultSynthStyle) this._defaultStyle).setFont(new FontUIResource(Font.DIALOG, 0, 12));
        }
        return this._defaultStyle;
    }

    private void getMatchingStyles(List list, JComponent jComponent, Region region) {
        String str;
        String lowerCaseName = region.getLowerCaseName();
        String name = jComponent.getName();
        if (name == null) {
            name = "";
        }
        for (int size = this._styles.size() - 1; size >= 0; size--) {
            StyleAssociation styleAssociation = this._styles.get(size);
            if (styleAssociation.getID() == 0) {
                str = name;
            } else {
                str = lowerCaseName;
            }
            if (styleAssociation.matches(str) && list.indexOf(styleAssociation.getStyle()) == -1) {
                list.add(styleAssociation.getStyle());
            }
        }
    }

    private void cacheStyle(List list, SynthStyle synthStyle) {
        this._resolvedStyles.put(new BakedArrayList(list), synthStyle);
    }

    private SynthStyle getCachedStyle(List list) {
        if (list.size() == 0) {
            return null;
        }
        return this._resolvedStyles.get(list);
    }

    private SynthStyle mergeStyles(List list) {
        int size = list.size();
        if (size == 0) {
            return null;
        }
        if (size == 1) {
            return (SynthStyle) ((DefaultSynthStyle) list.get(0)).clone();
        }
        DefaultSynthStyle defaultSynthStyleAddTo = (DefaultSynthStyle) ((DefaultSynthStyle) list.get(size - 1)).clone();
        for (int i2 = size - 2; i2 >= 0; i2--) {
            defaultSynthStyleAddTo = ((DefaultSynthStyle) list.get(i2)).addTo(defaultSynthStyleAddTo);
        }
        return defaultSynthStyleAddTo;
    }
}
