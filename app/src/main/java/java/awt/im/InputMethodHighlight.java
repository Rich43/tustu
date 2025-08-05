package java.awt.im;

import java.awt.font.TextAttribute;
import java.util.Map;

/* loaded from: rt.jar:java/awt/im/InputMethodHighlight.class */
public class InputMethodHighlight {
    public static final int RAW_TEXT = 0;
    public static final int CONVERTED_TEXT = 1;
    public static final InputMethodHighlight UNSELECTED_RAW_TEXT_HIGHLIGHT = new InputMethodHighlight(false, 0);
    public static final InputMethodHighlight SELECTED_RAW_TEXT_HIGHLIGHT = new InputMethodHighlight(true, 0);
    public static final InputMethodHighlight UNSELECTED_CONVERTED_TEXT_HIGHLIGHT = new InputMethodHighlight(false, 1);
    public static final InputMethodHighlight SELECTED_CONVERTED_TEXT_HIGHLIGHT = new InputMethodHighlight(true, 1);
    private boolean selected;
    private int state;
    private int variation;
    private Map<TextAttribute, ?> style;

    public InputMethodHighlight(boolean z2, int i2) {
        this(z2, i2, 0, null);
    }

    public InputMethodHighlight(boolean z2, int i2, int i3) {
        this(z2, i2, i3, null);
    }

    public InputMethodHighlight(boolean z2, int i2, int i3, Map<TextAttribute, ?> map) {
        this.selected = z2;
        if (i2 != 0 && i2 != 1) {
            throw new IllegalArgumentException("unknown input method highlight state");
        }
        this.state = i2;
        this.variation = i3;
        this.style = map;
    }

    public boolean isSelected() {
        return this.selected;
    }

    public int getState() {
        return this.state;
    }

    public int getVariation() {
        return this.variation;
    }

    public Map<TextAttribute, ?> getStyle() {
        return this.style;
    }
}
