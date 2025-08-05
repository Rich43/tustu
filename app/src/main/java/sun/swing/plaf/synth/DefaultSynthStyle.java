package sun.swing.plaf.synth;

import java.awt.Color;
import java.awt.Font;
import java.awt.Insets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JComponent;
import javax.swing.UIDefaults;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.synth.ColorType;
import javax.swing.plaf.synth.Region;
import javax.swing.plaf.synth.SynthContext;
import javax.swing.plaf.synth.SynthGraphicsUtils;
import javax.swing.plaf.synth.SynthPainter;
import javax.swing.plaf.synth.SynthStyle;

/* loaded from: rt.jar:sun/swing/plaf/synth/DefaultSynthStyle.class */
public class DefaultSynthStyle extends SynthStyle implements Cloneable {
    private static final Object PENDING = new Object();
    private boolean opaque;
    private Insets insets;
    private StateInfo[] states;
    private Map data;
    private Font font;
    private SynthGraphicsUtils synthGraphics;
    private SynthPainter painter;

    public DefaultSynthStyle() {
    }

    public DefaultSynthStyle(DefaultSynthStyle defaultSynthStyle) {
        this.opaque = defaultSynthStyle.opaque;
        if (defaultSynthStyle.insets != null) {
            this.insets = new Insets(defaultSynthStyle.insets.top, defaultSynthStyle.insets.left, defaultSynthStyle.insets.bottom, defaultSynthStyle.insets.right);
        }
        if (defaultSynthStyle.states != null) {
            this.states = new StateInfo[defaultSynthStyle.states.length];
            for (int length = defaultSynthStyle.states.length - 1; length >= 0; length--) {
                this.states[length] = (StateInfo) defaultSynthStyle.states[length].clone();
            }
        }
        if (defaultSynthStyle.data != null) {
            this.data = new HashMap();
            this.data.putAll(defaultSynthStyle.data);
        }
        this.font = defaultSynthStyle.font;
        this.synthGraphics = defaultSynthStyle.synthGraphics;
        this.painter = defaultSynthStyle.painter;
    }

    public DefaultSynthStyle(Insets insets, boolean z2, StateInfo[] stateInfoArr, Map map) {
        this.insets = insets;
        this.opaque = z2;
        this.states = stateInfoArr;
        this.data = map;
    }

    @Override // javax.swing.plaf.synth.SynthStyle
    public Color getColor(SynthContext synthContext, ColorType colorType) {
        return getColor(synthContext.getComponent(), synthContext.getRegion(), synthContext.getComponentState(), colorType);
    }

    public Color getColor(JComponent jComponent, Region region, int i2, ColorType colorType) {
        if (!region.isSubregion() && i2 == 1) {
            if (colorType == ColorType.BACKGROUND) {
                return jComponent.getBackground();
            }
            if (colorType == ColorType.FOREGROUND) {
                return jComponent.getForeground();
            }
            if (colorType == ColorType.TEXT_FOREGROUND) {
                Color foreground = jComponent.getForeground();
                if (!(foreground instanceof UIResource)) {
                    return foreground;
                }
            }
        }
        Color colorForState = getColorForState(jComponent, region, i2, colorType);
        if (colorForState == null) {
            if (colorType == ColorType.BACKGROUND || colorType == ColorType.TEXT_BACKGROUND) {
                return jComponent.getBackground();
            }
            if (colorType == ColorType.FOREGROUND || colorType == ColorType.TEXT_FOREGROUND) {
                return jComponent.getForeground();
            }
        }
        return colorForState;
    }

    @Override // javax.swing.plaf.synth.SynthStyle
    protected Color getColorForState(SynthContext synthContext, ColorType colorType) {
        return getColorForState(synthContext.getComponent(), synthContext.getRegion(), synthContext.getComponentState(), colorType);
    }

    protected Color getColorForState(JComponent jComponent, Region region, int i2, ColorType colorType) {
        StateInfo stateInfo;
        Color color;
        StateInfo stateInfo2 = getStateInfo(i2);
        if (stateInfo2 != null && (color = stateInfo2.getColor(colorType)) != null) {
            return color;
        }
        if ((stateInfo2 == null || stateInfo2.getComponentState() != 0) && (stateInfo = getStateInfo(0)) != null) {
            return stateInfo.getColor(colorType);
        }
        return null;
    }

    public void setFont(Font font) {
        this.font = font;
    }

    @Override // javax.swing.plaf.synth.SynthStyle
    public Font getFont(SynthContext synthContext) {
        return getFont(synthContext.getComponent(), synthContext.getRegion(), synthContext.getComponentState());
    }

    public Font getFont(JComponent jComponent, Region region, int i2) {
        if (!region.isSubregion() && i2 == 1) {
            return jComponent.getFont();
        }
        Font font = jComponent.getFont();
        if (font != null && !(font instanceof UIResource)) {
            return font;
        }
        return getFontForState(jComponent, region, i2);
    }

    protected Font getFontForState(JComponent jComponent, Region region, int i2) {
        StateInfo stateInfo;
        Font font;
        Font font2;
        if (jComponent == null) {
            return this.font;
        }
        StateInfo stateInfo2 = getStateInfo(i2);
        if (stateInfo2 != null && (font2 = stateInfo2.getFont()) != null) {
            return font2;
        }
        if ((stateInfo2 == null || stateInfo2.getComponentState() != 0) && (stateInfo = getStateInfo(0)) != null && (font = stateInfo.getFont()) != null) {
            return font;
        }
        return this.font;
    }

    @Override // javax.swing.plaf.synth.SynthStyle
    protected Font getFontForState(SynthContext synthContext) {
        return getFontForState(synthContext.getComponent(), synthContext.getRegion(), synthContext.getComponentState());
    }

    public void setGraphicsUtils(SynthGraphicsUtils synthGraphicsUtils) {
        this.synthGraphics = synthGraphicsUtils;
    }

    @Override // javax.swing.plaf.synth.SynthStyle
    public SynthGraphicsUtils getGraphicsUtils(SynthContext synthContext) {
        if (this.synthGraphics == null) {
            return super.getGraphicsUtils(synthContext);
        }
        return this.synthGraphics;
    }

    public void setInsets(Insets insets) {
        this.insets = insets;
    }

    @Override // javax.swing.plaf.synth.SynthStyle
    public Insets getInsets(SynthContext synthContext, Insets insets) {
        if (insets == null) {
            insets = new Insets(0, 0, 0, 0);
        }
        if (this.insets != null) {
            insets.left = this.insets.left;
            insets.right = this.insets.right;
            insets.top = this.insets.top;
            insets.bottom = this.insets.bottom;
        } else {
            insets.bottom = 0;
            insets.top = 0;
            insets.right = 0;
            insets.left = 0;
        }
        return insets;
    }

    public void setPainter(SynthPainter synthPainter) {
        this.painter = synthPainter;
    }

    @Override // javax.swing.plaf.synth.SynthStyle
    public SynthPainter getPainter(SynthContext synthContext) {
        return this.painter;
    }

    public void setOpaque(boolean z2) {
        this.opaque = z2;
    }

    @Override // javax.swing.plaf.synth.SynthStyle
    public boolean isOpaque(SynthContext synthContext) {
        return this.opaque;
    }

    public void setData(Map map) {
        this.data = map;
    }

    public Map getData() {
        return this.data;
    }

    @Override // javax.swing.plaf.synth.SynthStyle
    public Object get(SynthContext synthContext, Object obj) {
        StateInfo stateInfo = getStateInfo(synthContext.getComponentState());
        if (stateInfo != null && stateInfo.getData() != null && getKeyFromData(stateInfo.getData(), obj) != null) {
            return getKeyFromData(stateInfo.getData(), obj);
        }
        StateInfo stateInfo2 = getStateInfo(0);
        if (stateInfo2 != null && stateInfo2.getData() != null && getKeyFromData(stateInfo2.getData(), obj) != null) {
            return getKeyFromData(stateInfo2.getData(), obj);
        }
        if (getKeyFromData(this.data, obj) != null) {
            return getKeyFromData(this.data, obj);
        }
        return getDefaultValue(synthContext, obj);
    }

    private Object getKeyFromData(Map map, Object obj) {
        Object objCreateValue = null;
        if (map != null) {
            synchronized (map) {
                objCreateValue = map.get(obj);
            }
            while (objCreateValue == PENDING) {
                synchronized (map) {
                    try {
                        map.wait();
                    } catch (InterruptedException e2) {
                    }
                    objCreateValue = map.get(obj);
                }
            }
            if (objCreateValue instanceof UIDefaults.LazyValue) {
                synchronized (map) {
                    map.put(obj, PENDING);
                }
                objCreateValue = ((UIDefaults.LazyValue) objCreateValue).createValue(null);
                synchronized (map) {
                    map.put(obj, objCreateValue);
                    map.notifyAll();
                }
            }
        }
        return objCreateValue;
    }

    public Object getDefaultValue(SynthContext synthContext, Object obj) {
        return super.get(synthContext, obj);
    }

    public Object clone() {
        try {
            DefaultSynthStyle defaultSynthStyle = (DefaultSynthStyle) super.clone();
            if (this.states != null) {
                defaultSynthStyle.states = new StateInfo[this.states.length];
                for (int length = this.states.length - 1; length >= 0; length--) {
                    defaultSynthStyle.states[length] = (StateInfo) this.states[length].clone();
                }
            }
            if (this.data != null) {
                defaultSynthStyle.data = new HashMap();
                defaultSynthStyle.data.putAll(this.data);
            }
            return defaultSynthStyle;
        } catch (CloneNotSupportedException e2) {
            return null;
        }
    }

    public DefaultSynthStyle addTo(DefaultSynthStyle defaultSynthStyle) {
        if (this.insets != null) {
            defaultSynthStyle.insets = this.insets;
        }
        if (this.font != null) {
            defaultSynthStyle.font = this.font;
        }
        if (this.painter != null) {
            defaultSynthStyle.painter = this.painter;
        }
        if (this.synthGraphics != null) {
            defaultSynthStyle.synthGraphics = this.synthGraphics;
        }
        defaultSynthStyle.opaque = this.opaque;
        if (this.states != null) {
            if (defaultSynthStyle.states == null) {
                defaultSynthStyle.states = new StateInfo[this.states.length];
                for (int length = this.states.length - 1; length >= 0; length--) {
                    if (this.states[length] != null) {
                        defaultSynthStyle.states[length] = (StateInfo) this.states[length].clone();
                    }
                }
            } else {
                int i2 = 0;
                int i3 = 0;
                int length2 = defaultSynthStyle.states.length;
                for (int length3 = this.states.length - 1; length3 >= 0; length3--) {
                    int componentState = this.states[length3].getComponentState();
                    boolean z2 = false;
                    int i4 = (length2 - 1) - i3;
                    while (true) {
                        if (i4 < 0) {
                            break;
                        }
                        if (componentState == defaultSynthStyle.states[i4].getComponentState()) {
                            defaultSynthStyle.states[i4] = this.states[length3].addTo(defaultSynthStyle.states[i4]);
                            StateInfo stateInfo = defaultSynthStyle.states[(length2 - 1) - i3];
                            defaultSynthStyle.states[(length2 - 1) - i3] = defaultSynthStyle.states[i4];
                            defaultSynthStyle.states[i4] = stateInfo;
                            i3++;
                            z2 = true;
                            break;
                        }
                        i4--;
                    }
                    if (!z2) {
                        i2++;
                    }
                }
                if (i2 != 0) {
                    StateInfo[] stateInfoArr = new StateInfo[i2 + length2];
                    int i5 = length2;
                    System.arraycopy(defaultSynthStyle.states, 0, stateInfoArr, 0, length2);
                    for (int length4 = this.states.length - 1; length4 >= 0; length4--) {
                        int componentState2 = this.states[length4].getComponentState();
                        boolean z3 = false;
                        int i6 = length2 - 1;
                        while (true) {
                            if (i6 < 0) {
                                break;
                            }
                            if (componentState2 == defaultSynthStyle.states[i6].getComponentState()) {
                                z3 = true;
                                break;
                            }
                            i6--;
                        }
                        if (!z3) {
                            int i7 = i5;
                            i5++;
                            stateInfoArr[i7] = (StateInfo) this.states[length4].clone();
                        }
                    }
                    defaultSynthStyle.states = stateInfoArr;
                }
            }
        }
        if (this.data != null) {
            if (defaultSynthStyle.data == null) {
                defaultSynthStyle.data = new HashMap();
            }
            defaultSynthStyle.data.putAll(this.data);
        }
        return defaultSynthStyle;
    }

    public void setStateInfo(StateInfo[] stateInfoArr) {
        this.states = stateInfoArr;
    }

    public StateInfo[] getStateInfo() {
        return this.states;
    }

    public StateInfo getStateInfo(int i2) {
        if (this.states != null) {
            int i3 = 0;
            int i4 = -1;
            int i5 = -1;
            if (i2 == 0) {
                for (int length = this.states.length - 1; length >= 0; length--) {
                    if (this.states[length].getComponentState() == 0) {
                        return this.states[length];
                    }
                }
                return null;
            }
            for (int length2 = this.states.length - 1; length2 >= 0; length2--) {
                int componentState = this.states[length2].getComponentState();
                if (componentState == 0) {
                    if (i5 == -1) {
                        i5 = length2;
                    }
                } else if ((i2 & componentState) == componentState) {
                    int i6 = componentState - (((-1431655766) & componentState) >>> 1);
                    int i7 = (i6 & 858993459) + ((i6 >>> 2) & 858993459);
                    int i8 = (i7 + (i7 >>> 4)) & 252645135;
                    int i9 = i8 + (i8 >>> 8);
                    int i10 = (i9 + (i9 >>> 16)) & 255;
                    if (i10 > i3) {
                        i4 = length2;
                        i3 = i10;
                    }
                }
            }
            if (i4 != -1) {
                return this.states[i4];
            }
            if (i5 != -1) {
                return this.states[i5];
            }
            return null;
        }
        return null;
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(super.toString()).append(',');
        stringBuffer.append("data=").append((Object) this.data).append(',');
        stringBuffer.append("font=").append((Object) this.font).append(',');
        stringBuffer.append("insets=").append((Object) this.insets).append(',');
        stringBuffer.append("synthGraphics=").append((Object) this.synthGraphics).append(',');
        stringBuffer.append("painter=").append((Object) this.painter).append(',');
        StateInfo[] stateInfo = getStateInfo();
        if (stateInfo != null) {
            stringBuffer.append("states[");
            for (StateInfo stateInfo2 : stateInfo) {
                stringBuffer.append(stateInfo2.toString()).append(',');
            }
            stringBuffer.append(']').append(',');
        }
        stringBuffer.deleteCharAt(stringBuffer.length() - 1);
        return stringBuffer.toString();
    }

    /* loaded from: rt.jar:sun/swing/plaf/synth/DefaultSynthStyle$StateInfo.class */
    public static class StateInfo {
        private Map data;
        private Font font;
        private Color[] colors;
        private int state;

        public StateInfo() {
        }

        public StateInfo(int i2, Font font, Color[] colorArr) {
            this.state = i2;
            this.font = font;
            this.colors = colorArr;
        }

        public StateInfo(StateInfo stateInfo) {
            this.state = stateInfo.state;
            this.font = stateInfo.font;
            if (stateInfo.data != null) {
                if (this.data == null) {
                    this.data = new HashMap();
                }
                this.data.putAll(stateInfo.data);
            }
            if (stateInfo.colors != null) {
                this.colors = new Color[stateInfo.colors.length];
                System.arraycopy(stateInfo.colors, 0, this.colors, 0, stateInfo.colors.length);
            }
        }

        public Map getData() {
            return this.data;
        }

        public void setData(Map map) {
            this.data = map;
        }

        public void setFont(Font font) {
            this.font = font;
        }

        public Font getFont() {
            return this.font;
        }

        public void setColors(Color[] colorArr) {
            this.colors = colorArr;
        }

        public Color[] getColors() {
            return this.colors;
        }

        public Color getColor(ColorType colorType) {
            int id;
            if (this.colors != null && (id = colorType.getID()) < this.colors.length) {
                return this.colors[id];
            }
            return null;
        }

        public StateInfo addTo(StateInfo stateInfo) {
            if (this.font != null) {
                stateInfo.font = this.font;
            }
            if (this.data != null) {
                if (stateInfo.data == null) {
                    stateInfo.data = new HashMap();
                }
                stateInfo.data.putAll(this.data);
            }
            if (this.colors != null) {
                if (stateInfo.colors == null) {
                    stateInfo.colors = new Color[this.colors.length];
                    System.arraycopy(this.colors, 0, stateInfo.colors, 0, this.colors.length);
                } else {
                    if (stateInfo.colors.length < this.colors.length) {
                        Color[] colorArr = stateInfo.colors;
                        stateInfo.colors = new Color[this.colors.length];
                        System.arraycopy(colorArr, 0, stateInfo.colors, 0, colorArr.length);
                    }
                    for (int length = this.colors.length - 1; length >= 0; length--) {
                        if (this.colors[length] != null) {
                            stateInfo.colors[length] = this.colors[length];
                        }
                    }
                }
            }
            return stateInfo;
        }

        public void setComponentState(int i2) {
            this.state = i2;
        }

        public int getComponentState() {
            return this.state;
        }

        private int getMatchCount(int i2) {
            int i3 = i2 & this.state;
            int i4 = i3 - (((-1431655766) & i3) >>> 1);
            int i5 = (i4 & 858993459) + ((i4 >>> 2) & 858993459);
            int i6 = (i5 + (i5 >>> 4)) & 252645135;
            int i7 = i6 + (i6 >>> 8);
            return (i7 + (i7 >>> 16)) & 255;
        }

        public Object clone() {
            return new StateInfo(this);
        }

        public String toString() {
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append(super.toString()).append(',');
            stringBuffer.append("state=").append(Integer.toString(this.state)).append(',');
            stringBuffer.append("font=").append((Object) this.font).append(',');
            if (this.colors != null) {
                stringBuffer.append("colors=").append((Object) Arrays.asList(this.colors)).append(',');
            }
            return stringBuffer.toString();
        }
    }
}
