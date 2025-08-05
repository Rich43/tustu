package javafx.scene.control;

import javafx.scene.Node;
import javafx.scene.control.TooltipBuilder;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

@Deprecated
/* loaded from: jfxrt.jar:javafx/scene/control/TooltipBuilder.class */
public class TooltipBuilder<B extends TooltipBuilder<B>> extends PopupControlBuilder<B> {
    private int __set;
    private ContentDisplay contentDisplay;
    private Font font;
    private Node graphic;
    private double graphicTextGap;
    private String text;
    private TextAlignment textAlignment;
    private OverrunStyle textOverrun;
    private boolean wrapText;

    protected TooltipBuilder() {
    }

    public static TooltipBuilder<?> create() {
        return new TooltipBuilder<>();
    }

    private void __set(int i2) {
        this.__set |= 1 << i2;
    }

    public void applyTo(Tooltip x2) {
        super.applyTo((PopupControl) x2);
        int set = this.__set;
        while (set != 0) {
            int i2 = Integer.numberOfTrailingZeros(set);
            set &= (1 << i2) ^ (-1);
            switch (i2) {
                case 0:
                    x2.setContentDisplay(this.contentDisplay);
                    break;
                case 1:
                    x2.setFont(this.font);
                    break;
                case 2:
                    x2.setGraphic(this.graphic);
                    break;
                case 3:
                    x2.setGraphicTextGap(this.graphicTextGap);
                    break;
                case 4:
                    x2.setText(this.text);
                    break;
                case 5:
                    x2.setTextAlignment(this.textAlignment);
                    break;
                case 6:
                    x2.setTextOverrun(this.textOverrun);
                    break;
                case 7:
                    x2.setWrapText(this.wrapText);
                    break;
            }
        }
    }

    public B contentDisplay(ContentDisplay x2) {
        this.contentDisplay = x2;
        __set(0);
        return this;
    }

    public B font(Font x2) {
        this.font = x2;
        __set(1);
        return this;
    }

    public B graphic(Node x2) {
        this.graphic = x2;
        __set(2);
        return this;
    }

    public B graphicTextGap(double x2) {
        this.graphicTextGap = x2;
        __set(3);
        return this;
    }

    public B text(String x2) {
        this.text = x2;
        __set(4);
        return this;
    }

    public B textAlignment(TextAlignment x2) {
        this.textAlignment = x2;
        __set(5);
        return this;
    }

    public B textOverrun(OverrunStyle x2) {
        this.textOverrun = x2;
        __set(6);
        return this;
    }

    public B wrapText(boolean x2) {
        this.wrapText = x2;
        __set(7);
        return this;
    }

    @Override // javafx.scene.control.PopupControlBuilder, javafx.util.Builder
    /* renamed from: build */
    public Tooltip build2() {
        Tooltip x2 = new Tooltip();
        applyTo(x2);
        return x2;
    }
}
