package javax.swing.plaf.nimbus;

import java.awt.Color;
import java.awt.Font;
import java.awt.Insets;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.Painter;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.nimbus.State;
import javax.swing.plaf.synth.ColorType;
import javax.swing.plaf.synth.SynthContext;
import javax.swing.plaf.synth.SynthPainter;
import javax.swing.plaf.synth.SynthStyle;

/* loaded from: rt.jar:javax/swing/plaf/nimbus/NimbusStyle.class */
public final class NimbusStyle extends SynthStyle {
    public static final String LARGE_KEY = "large";
    public static final String SMALL_KEY = "small";
    public static final String MINI_KEY = "mini";
    public static final double LARGE_SCALE = 1.15d;
    public static final double SMALL_SCALE = 0.857d;
    public static final double MINI_SCALE = 0.714d;
    private static final Object NULL;
    private static final Color DEFAULT_COLOR;
    private static final Comparator<RuntimeState> STATE_COMPARATOR;
    private String prefix;
    private SynthPainter painter;
    private Values values;
    private CacheKey tmpKey = new CacheKey("", 0);
    private WeakReference<JComponent> component;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !NimbusStyle.class.desiredAssertionStatus();
        NULL = (char) 0;
        DEFAULT_COLOR = new ColorUIResource(Color.BLACK);
        STATE_COMPARATOR = new Comparator<RuntimeState>() { // from class: javax.swing.plaf.nimbus.NimbusStyle.1
            @Override // java.util.Comparator
            public int compare(RuntimeState runtimeState, RuntimeState runtimeState2) {
                return runtimeState.state - runtimeState2.state;
            }
        };
    }

    NimbusStyle(String str, JComponent jComponent) {
        if (jComponent != null) {
            this.component = new WeakReference<>(jComponent);
        }
        this.prefix = str;
        this.painter = new SynthPainterImpl(this);
    }

    @Override // javax.swing.plaf.synth.SynthStyle
    public void installDefaults(SynthContext synthContext) {
        validate();
        super.installDefaults(synthContext);
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void validate() {
        String strSubstring;
        if (this.values != null) {
            return;
        }
        this.values = new Values();
        Map<String, Object> defaultsForPrefix = ((NimbusLookAndFeel) UIManager.getLookAndFeel()).getDefaultsForPrefix(this.prefix);
        Map<String, Object> map = defaultsForPrefix;
        if (this.component != null) {
            Object clientProperty = this.component.get().getClientProperty("Nimbus.Overrides");
            map = defaultsForPrefix;
            if (clientProperty instanceof UIDefaults) {
                Object clientProperty2 = this.component.get().getClientProperty("Nimbus.Overrides.InheritDefaults");
                boolean zBooleanValue = clientProperty2 instanceof Boolean ? ((Boolean) clientProperty2).booleanValue() : true;
                UIDefaults uIDefaults = (UIDefaults) clientProperty;
                TreeMap treeMap = new TreeMap();
                for (Object obj : uIDefaults.keySet()) {
                    if (obj instanceof String) {
                        String str = (String) obj;
                        if (str.startsWith(this.prefix)) {
                            treeMap.put(str, uIDefaults.get(str));
                        }
                    }
                }
                if (zBooleanValue) {
                    defaultsForPrefix.putAll(treeMap);
                    map = defaultsForPrefix;
                } else {
                    map = treeMap;
                }
            }
        }
        ArrayList arrayList = new ArrayList();
        HashMap map2 = new HashMap();
        ArrayList arrayList2 = new ArrayList();
        String str2 = (String) map.get(this.prefix + ".States");
        if (str2 != null) {
            String[] strArrSplit = str2.split(",");
            for (int i2 = 0; i2 < strArrSplit.length; i2++) {
                strArrSplit[i2] = strArrSplit[i2].trim();
                if (!State.isStandardStateName(strArrSplit[i2])) {
                    State state = (State) map.get(this.prefix + "." + strArrSplit[i2]);
                    if (state != null) {
                        arrayList.add(state);
                    }
                } else {
                    arrayList.add(State.getStandardState(strArrSplit[i2]));
                }
            }
            if (arrayList.size() > 0) {
                this.values.stateTypes = (State[]) arrayList.toArray(new State[arrayList.size()]);
            }
            int i3 = 1;
            Iterator<E> it = arrayList.iterator();
            while (it.hasNext()) {
                map2.put(((State) it.next()).getName(), Integer.valueOf(i3));
                i3 <<= 1;
            }
        } else {
            arrayList.add(State.Enabled);
            arrayList.add(State.MouseOver);
            arrayList.add(State.Pressed);
            arrayList.add(State.Disabled);
            arrayList.add(State.Focused);
            arrayList.add(State.Selected);
            arrayList.add(State.Default);
            map2.put("Enabled", 1);
            map2.put("MouseOver", 2);
            map2.put("Pressed", 4);
            map2.put("Disabled", 8);
            map2.put("Focused", 256);
            map2.put("Selected", 512);
            map2.put(Action.DEFAULT, 1024);
        }
        for (String str3 : map.keySet()) {
            String strSubstring2 = str3.substring(this.prefix.length());
            if (strSubstring2.indexOf(34) == -1 && strSubstring2.indexOf(58) == -1) {
                String strSubstring3 = strSubstring2.substring(1);
                String strSubstring4 = null;
                int iIndexOf = strSubstring3.indexOf(93);
                if (iIndexOf < 0) {
                    strSubstring = strSubstring3;
                } else {
                    strSubstring4 = strSubstring3.substring(0, iIndexOf);
                    strSubstring = strSubstring3.substring(iIndexOf + 2);
                }
                if (strSubstring4 == null) {
                    if ("contentMargins".equals(strSubstring)) {
                        this.values.contentMargins = (Insets) map.get(str3);
                    } else if (!"States".equals(strSubstring)) {
                        this.values.defaults.put(strSubstring, map.get(str3));
                    }
                } else {
                    boolean z2 = false;
                    int iIntValue = 0;
                    String[] strArrSplit2 = strSubstring4.split("\\+");
                    int length = strArrSplit2.length;
                    int i4 = 0;
                    while (true) {
                        if (i4 >= length) {
                            break;
                        }
                        String str4 = strArrSplit2[i4];
                        if (map2.containsKey(str4)) {
                            iIntValue |= ((Integer) map2.get(str4)).intValue();
                            i4++;
                        } else {
                            z2 = true;
                            break;
                        }
                    }
                    if (!z2) {
                        RuntimeState runtimeState = null;
                        Iterator<E> it2 = arrayList2.iterator();
                        while (true) {
                            if (!it2.hasNext()) {
                                break;
                            }
                            RuntimeState runtimeState2 = (RuntimeState) it2.next();
                            if (runtimeState2.state == iIntValue) {
                                runtimeState = runtimeState2;
                                break;
                            }
                        }
                        if (runtimeState == null) {
                            runtimeState = new RuntimeState(iIntValue, strSubstring4);
                            arrayList2.add(runtimeState);
                        }
                        if ("backgroundPainter".equals(strSubstring)) {
                            runtimeState.backgroundPainter = getPainter(map, str3);
                        } else if ("foregroundPainter".equals(strSubstring)) {
                            runtimeState.foregroundPainter = getPainter(map, str3);
                        } else if ("borderPainter".equals(strSubstring)) {
                            runtimeState.borderPainter = getPainter(map, str3);
                        } else {
                            runtimeState.defaults.put(strSubstring, map.get(str3));
                        }
                    }
                }
            }
        }
        Collections.sort(arrayList2, STATE_COMPARATOR);
        this.values.states = (RuntimeState[]) arrayList2.toArray(new RuntimeState[arrayList2.size()]);
    }

    private Painter getPainter(Map<String, Object> map, String str) {
        Object objCreateValue = map.get(str);
        if (objCreateValue instanceof UIDefaults.LazyValue) {
            objCreateValue = ((UIDefaults.LazyValue) objCreateValue).createValue(UIManager.getDefaults());
        }
        if (objCreateValue instanceof Painter) {
            return (Painter) objCreateValue;
        }
        return null;
    }

    @Override // javax.swing.plaf.synth.SynthStyle
    public Insets getInsets(SynthContext synthContext, Insets insets) {
        if (insets == null) {
            insets = new Insets(0, 0, 0, 0);
        }
        Values values = getValues(synthContext);
        if (values.contentMargins == null) {
            insets.right = 0;
            insets.left = 0;
            insets.top = 0;
            insets.bottom = 0;
            return insets;
        }
        insets.bottom = values.contentMargins.bottom;
        insets.top = values.contentMargins.top;
        insets.left = values.contentMargins.left;
        insets.right = values.contentMargins.right;
        String str = (String) synthContext.getComponent().getClientProperty("JComponent.sizeVariant");
        if (str != null) {
            if (LARGE_KEY.equals(str)) {
                insets.bottom = (int) (r0.bottom * 1.15d);
                insets.top = (int) (r0.top * 1.15d);
                insets.left = (int) (r0.left * 1.15d);
                insets.right = (int) (r0.right * 1.15d);
            } else if (SMALL_KEY.equals(str)) {
                insets.bottom = (int) (r0.bottom * 0.857d);
                insets.top = (int) (r0.top * 0.857d);
                insets.left = (int) (r0.left * 0.857d);
                insets.right = (int) (r0.right * 0.857d);
            } else if (MINI_KEY.equals(str)) {
                insets.bottom = (int) (r0.bottom * 0.714d);
                insets.top = (int) (r0.top * 0.714d);
                insets.left = (int) (r0.left * 0.714d);
                insets.right = (int) (r0.right * 0.714d);
            }
        }
        return insets;
    }

    @Override // javax.swing.plaf.synth.SynthStyle
    protected Color getColorForState(SynthContext synthContext, ColorType colorType) {
        String string;
        if (colorType == ColorType.BACKGROUND) {
            string = "background";
        } else if (colorType == ColorType.FOREGROUND) {
            string = "textForeground";
        } else if (colorType == ColorType.TEXT_BACKGROUND) {
            string = "textBackground";
        } else if (colorType == ColorType.TEXT_FOREGROUND) {
            string = "textForeground";
        } else if (colorType == ColorType.FOCUS) {
            string = "focus";
        } else if (colorType != null) {
            string = colorType.toString();
        } else {
            return DEFAULT_COLOR;
        }
        Color color = (Color) get(synthContext, string);
        if (color == null) {
            color = DEFAULT_COLOR;
        }
        return color;
    }

    @Override // javax.swing.plaf.synth.SynthStyle
    protected Font getFontForState(SynthContext synthContext) {
        Font fontDeriveFont = (Font) get(synthContext, "font");
        if (fontDeriveFont == null) {
            fontDeriveFont = UIManager.getFont("defaultFont");
        }
        String str = (String) synthContext.getComponent().getClientProperty("JComponent.sizeVariant");
        if (str != null) {
            if (LARGE_KEY.equals(str)) {
                fontDeriveFont = fontDeriveFont.deriveFont(Math.round(fontDeriveFont.getSize2D() * 1.15d));
            } else if (SMALL_KEY.equals(str)) {
                fontDeriveFont = fontDeriveFont.deriveFont(Math.round(fontDeriveFont.getSize2D() * 0.857d));
            } else if (MINI_KEY.equals(str)) {
                fontDeriveFont = fontDeriveFont.deriveFont(Math.round(fontDeriveFont.getSize2D() * 0.714d));
            }
        }
        return fontDeriveFont;
    }

    @Override // javax.swing.plaf.synth.SynthStyle
    public SynthPainter getPainter(SynthContext synthContext) {
        return this.painter;
    }

    @Override // javax.swing.plaf.synth.SynthStyle
    public boolean isOpaque(SynthContext synthContext) {
        if ("Table.cellRenderer".equals(synthContext.getComponent().getName())) {
            return true;
        }
        Boolean bool = (Boolean) get(synthContext, "opaque");
        if (bool == null) {
            return false;
        }
        return bool.booleanValue();
    }

    @Override // javax.swing.plaf.synth.SynthStyle
    public Object get(SynthContext synthContext, Object obj) {
        RuntimeState nextState;
        Values values = getValues(synthContext);
        String string = obj.toString();
        String strSubstring = string.substring(string.indexOf(".") + 1);
        int extendedState = getExtendedState(synthContext, values);
        this.tmpKey.init(strSubstring, extendedState);
        Object obj2 = values.cache.get(this.tmpKey);
        if (!(obj2 != null)) {
            int[] iArr = {-1};
            while (obj2 == null && (nextState = getNextState(values.states, iArr, extendedState)) != null) {
                obj2 = nextState.defaults.get(strSubstring);
            }
            if (obj2 == null && values.defaults != null) {
                obj2 = values.defaults.get(strSubstring);
            }
            if (obj2 == null) {
                obj2 = UIManager.get(string);
            }
            if (obj2 == null && strSubstring.equals("focusInputMap")) {
                obj2 = super.get(synthContext, string);
            }
            values.cache.put(new CacheKey(strSubstring, extendedState), obj2 == null ? NULL : obj2);
        }
        if (obj2 == NULL) {
            return null;
        }
        return obj2;
    }

    public Painter getBackgroundPainter(SynthContext synthContext) {
        Values values = getValues(synthContext);
        int extendedState = getExtendedState(synthContext, values);
        this.tmpKey.init("backgroundPainter$$instance", extendedState);
        Painter painter = (Painter) values.cache.get(this.tmpKey);
        if (painter != null) {
            return painter;
        }
        int[] iArr = {-1};
        while (true) {
            RuntimeState nextState = getNextState(values.states, iArr, extendedState);
            if (nextState == null) {
                break;
            }
            if (nextState.backgroundPainter != null) {
                painter = nextState.backgroundPainter;
                break;
            }
        }
        if (painter == null) {
            painter = (Painter) get(synthContext, "backgroundPainter");
        }
        if (painter != null) {
            values.cache.put(new CacheKey("backgroundPainter$$instance", extendedState), painter);
        }
        return painter;
    }

    public Painter getForegroundPainter(SynthContext synthContext) {
        Values values = getValues(synthContext);
        int extendedState = getExtendedState(synthContext, values);
        this.tmpKey.init("foregroundPainter$$instance", extendedState);
        Painter painter = (Painter) values.cache.get(this.tmpKey);
        if (painter != null) {
            return painter;
        }
        int[] iArr = {-1};
        while (true) {
            RuntimeState nextState = getNextState(values.states, iArr, extendedState);
            if (nextState == null) {
                break;
            }
            if (nextState.foregroundPainter != null) {
                painter = nextState.foregroundPainter;
                break;
            }
        }
        if (painter == null) {
            painter = (Painter) get(synthContext, "foregroundPainter");
        }
        if (painter != null) {
            values.cache.put(new CacheKey("foregroundPainter$$instance", extendedState), painter);
        }
        return painter;
    }

    public Painter getBorderPainter(SynthContext synthContext) {
        Values values = getValues(synthContext);
        int extendedState = getExtendedState(synthContext, values);
        this.tmpKey.init("borderPainter$$instance", extendedState);
        Painter painter = (Painter) values.cache.get(this.tmpKey);
        if (painter != null) {
            return painter;
        }
        int[] iArr = {-1};
        while (true) {
            RuntimeState nextState = getNextState(values.states, iArr, extendedState);
            if (nextState == null) {
                break;
            }
            if (nextState.borderPainter != null) {
                painter = nextState.borderPainter;
                break;
            }
        }
        if (painter == null) {
            painter = (Painter) get(synthContext, "borderPainter");
        }
        if (painter != null) {
            values.cache.put(new CacheKey("borderPainter$$instance", extendedState), painter);
        }
        return painter;
    }

    private Values getValues(SynthContext synthContext) {
        validate();
        return this.values;
    }

    private boolean contains(String[] strArr, String str) {
        if (!$assertionsDisabled && str == null) {
            throw new AssertionError();
        }
        for (String str2 : strArr) {
            if (str.equals(str2)) {
                return true;
            }
        }
        return false;
    }

    private int getExtendedState(SynthContext synthContext, Values values) {
        JComponent component = synthContext.getComponent();
        int state = 0;
        int i2 = 1;
        Object clientProperty = component.getClientProperty("Nimbus.State");
        if (clientProperty != null) {
            String[] strArrSplit = clientProperty.toString().split("\\+");
            if (values.stateTypes == null) {
                for (String str : strArrSplit) {
                    State.StandardState standardState = State.getStandardState(str);
                    if (standardState != null) {
                        state |= standardState.getState();
                    }
                }
            } else {
                for (State state2 : values.stateTypes) {
                    if (contains(strArrSplit, state2.getName())) {
                        state |= i2;
                    }
                    i2 <<= 1;
                }
            }
        } else {
            if (values.stateTypes == null) {
                return synthContext.getComponentState();
            }
            int componentState = synthContext.getComponentState();
            for (State state3 : values.stateTypes) {
                if (state3.isInState(component, componentState)) {
                    state |= i2;
                }
                i2 <<= 1;
            }
        }
        return state;
    }

    private RuntimeState getNextState(RuntimeState[] runtimeStateArr, int[] iArr, int i2) {
        if (runtimeStateArr != null && runtimeStateArr.length > 0) {
            int i3 = 0;
            int i4 = -1;
            int i5 = -1;
            if (i2 == 0) {
                for (int length = runtimeStateArr.length - 1; length >= 0; length--) {
                    if (runtimeStateArr[length].state == 0) {
                        iArr[0] = length;
                        return runtimeStateArr[length];
                    }
                }
                iArr[0] = -1;
                return null;
            }
            for (int length2 = ((iArr == null || iArr[0] == -1) ? runtimeStateArr.length : iArr[0]) - 1; length2 >= 0; length2--) {
                int i6 = runtimeStateArr[length2].state;
                if (i6 == 0) {
                    if (i5 == -1) {
                        i5 = length2;
                    }
                } else if ((i2 & i6) == i6) {
                    int i7 = i6 - (((-1431655766) & i6) >>> 1);
                    int i8 = (i7 & 858993459) + ((i7 >>> 2) & 858993459);
                    int i9 = (i8 + (i8 >>> 4)) & 252645135;
                    int i10 = i9 + (i9 >>> 8);
                    int i11 = (i10 + (i10 >>> 16)) & 255;
                    if (i11 > i3) {
                        i4 = length2;
                        i3 = i11;
                    }
                }
            }
            if (i4 != -1) {
                iArr[0] = i4;
                return runtimeStateArr[i4];
            }
            if (i5 != -1) {
                iArr[0] = i5;
                return runtimeStateArr[i5];
            }
        }
        iArr[0] = -1;
        return null;
    }

    /* loaded from: rt.jar:javax/swing/plaf/nimbus/NimbusStyle$RuntimeState.class */
    private final class RuntimeState implements Cloneable {
        int state;
        Painter backgroundPainter;
        Painter foregroundPainter;
        Painter borderPainter;
        String stateName;
        UIDefaults defaults;

        private RuntimeState(int i2, String str) {
            this.defaults = new UIDefaults(10, 0.7f);
            this.state = i2;
            this.stateName = str;
        }

        public String toString() {
            return this.stateName;
        }

        /* renamed from: clone, reason: merged with bridge method [inline-methods] */
        public RuntimeState m4345clone() {
            RuntimeState runtimeState = NimbusStyle.this.new RuntimeState(this.state, this.stateName);
            runtimeState.backgroundPainter = this.backgroundPainter;
            runtimeState.foregroundPainter = this.foregroundPainter;
            runtimeState.borderPainter = this.borderPainter;
            runtimeState.defaults.putAll(this.defaults);
            return runtimeState;
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/nimbus/NimbusStyle$Values.class */
    private static final class Values {
        State[] stateTypes;
        RuntimeState[] states;
        Insets contentMargins;
        UIDefaults defaults;
        Map<CacheKey, Object> cache;

        private Values() {
            this.stateTypes = null;
            this.states = null;
            this.defaults = new UIDefaults(10, 0.7f);
            this.cache = new HashMap();
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/nimbus/NimbusStyle$CacheKey.class */
    private static final class CacheKey {
        private String key;
        private int xstate;

        CacheKey(Object obj, int i2) {
            init(obj, i2);
        }

        void init(Object obj, int i2) {
            this.key = obj.toString();
            this.xstate = i2;
        }

        public boolean equals(Object obj) {
            CacheKey cacheKey = (CacheKey) obj;
            return obj != null && this.xstate == cacheKey.xstate && this.key.equals(cacheKey.key);
        }

        public int hashCode() {
            return (29 * ((29 * 3) + this.key.hashCode())) + this.xstate;
        }
    }
}
