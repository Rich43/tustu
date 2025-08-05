package javafx.scene.control;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.LabeledBuilder;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

@Deprecated
/* loaded from: jfxrt.jar:javafx/scene/control/LabeledBuilder.class */
public abstract class LabeledBuilder<B extends LabeledBuilder<B>> extends ControlBuilder<B> {
    private int __set;
    private Pos alignment;
    private ContentDisplay contentDisplay;
    private String ellipsisString;
    private Font font;
    private Node graphic;
    private double graphicTextGap;
    private boolean mnemonicParsing;
    private String text;
    private TextAlignment textAlignment;
    private Paint textFill;
    private OverrunStyle textOverrun;
    private boolean underline;
    private boolean wrapText;

    protected LabeledBuilder() {
    }

    private void __set(int i2) {
        this.__set |= 1 << i2;
    }

    public void applyTo(Labeled x2) {
        super.applyTo((Control) x2);
        int set = this.__set;
        while (set != 0) {
            int i2 = Integer.numberOfTrailingZeros(set);
            set &= (1 << i2) ^ (-1);
            switch (i2) {
                case 0:
                    x2.setAlignment(this.alignment);
                    break;
                case 1:
                    x2.setContentDisplay(this.contentDisplay);
                    break;
                case 2:
                    x2.setEllipsisString(this.ellipsisString);
                    break;
                case 3:
                    x2.setFont(this.font);
                    break;
                case 4:
                    x2.setGraphic(this.graphic);
                    break;
                case 5:
                    x2.setGraphicTextGap(this.graphicTextGap);
                    break;
                case 6:
                    x2.setMnemonicParsing(this.mnemonicParsing);
                    break;
                case 7:
                    x2.setText(this.text);
                    break;
                case 8:
                    x2.setTextAlignment(this.textAlignment);
                    break;
                case 9:
                    x2.setTextFill(this.textFill);
                    break;
                case 10:
                    x2.setTextOverrun(this.textOverrun);
                    break;
                case 11:
                    x2.setUnderline(this.underline);
                    break;
                case 12:
                    x2.setWrapText(this.wrapText);
                    break;
            }
        }
    }

    public B alignment(Pos x2) {
        this.alignment = x2;
        __set(0);
        return this;
    }

    public B contentDisplay(ContentDisplay x2) {
        this.contentDisplay = x2;
        __set(1);
        return this;
    }

    public B ellipsisString(String x2) {
        this.ellipsisString = x2;
        __set(2);
        return this;
    }

    public B font(Font x2) {
        this.font = x2;
        __set(3);
        return this;
    }

    public B graphic(Node x2) {
        this.graphic = x2;
        __set(4);
        return this;
    }

    public B graphicTextGap(double x2) {
        this.graphicTextGap = x2;
        __set(5);
        return this;
    }

    public B mnemonicParsing(boolean x2) {
        this.mnemonicParsing = x2;
        __set(6);
        return this;
    }

    public B text(String x2) {
        this.text = x2;
        __set(7);
        return this;
    }

    public B textAlignment(TextAlignment x2) {
        this.textAlignment = x2;
        __set(8);
        return this;
    }

    public B textFill(Paint x2) {
        this.textFill = x2;
        __set(9);
        return this;
    }

    public B textOverrun(OverrunStyle x2) {
        this.textOverrun = x2;
        __set(10);
        return this;
    }

    public B underline(boolean x2) {
        this.underline = x2;
        __set(11);
        return this;
    }

    public B wrapText(boolean x2) {
        this.wrapText = x2;
        __set(12);
        return this;
    }
}
