package javafx.scene.text;

import javafx.geometry.VPos;
import javafx.scene.shape.Shape;
import javafx.scene.shape.ShapeBuilder;
import javafx.scene.text.TextBuilder;
import javafx.util.Builder;

@Deprecated
/* loaded from: jfxrt.jar:javafx/scene/text/TextBuilder.class */
public class TextBuilder<B extends TextBuilder<B>> extends ShapeBuilder<B> implements Builder<Text> {
    private int __set;
    private TextBoundsType boundsType;
    private Font font;
    private FontSmoothingType fontSmoothingType;
    private boolean impl_caretBias;
    private int impl_caretPosition;
    private int impl_selectionEnd;
    private int impl_selectionStart;
    private boolean strikethrough;
    private String text;
    private TextAlignment textAlignment;
    private VPos textOrigin;
    private boolean underline;
    private double wrappingWidth;

    /* renamed from: x, reason: collision with root package name */
    private double f12745x;

    /* renamed from: y, reason: collision with root package name */
    private double f12746y;

    protected TextBuilder() {
    }

    public static TextBuilder<?> create() {
        return new TextBuilder<>();
    }

    private void __set(int i2) {
        this.__set |= 1 << i2;
    }

    public void applyTo(Text x2) {
        super.applyTo((Shape) x2);
        int set = this.__set;
        while (set != 0) {
            int i2 = Integer.numberOfTrailingZeros(set);
            set &= (1 << i2) ^ (-1);
            switch (i2) {
                case 0:
                    x2.setBoundsType(this.boundsType);
                    break;
                case 1:
                    x2.setFont(this.font);
                    break;
                case 2:
                    x2.setFontSmoothingType(this.fontSmoothingType);
                    break;
                case 3:
                    x2.setImpl_caretBias(this.impl_caretBias);
                    break;
                case 4:
                    x2.setImpl_caretPosition(this.impl_caretPosition);
                    break;
                case 6:
                    x2.setImpl_selectionEnd(this.impl_selectionEnd);
                    break;
                case 8:
                    x2.setImpl_selectionStart(this.impl_selectionStart);
                    break;
                case 9:
                    x2.setStrikethrough(this.strikethrough);
                    break;
                case 10:
                    x2.setText(this.text);
                    break;
                case 11:
                    x2.setTextAlignment(this.textAlignment);
                    break;
                case 12:
                    x2.setTextOrigin(this.textOrigin);
                    break;
                case 13:
                    x2.setUnderline(this.underline);
                    break;
                case 14:
                    x2.setWrappingWidth(this.wrappingWidth);
                    break;
                case 15:
                    x2.setX(this.f12745x);
                    break;
                case 16:
                    x2.setY(this.f12746y);
                    break;
            }
        }
    }

    public B boundsType(TextBoundsType x2) {
        this.boundsType = x2;
        __set(0);
        return this;
    }

    public B font(Font x2) {
        this.font = x2;
        __set(1);
        return this;
    }

    public B fontSmoothingType(FontSmoothingType x2) {
        this.fontSmoothingType = x2;
        __set(2);
        return this;
    }

    @Deprecated
    public B impl_caretBias(boolean x2) {
        this.impl_caretBias = x2;
        __set(3);
        return this;
    }

    @Deprecated
    public B impl_caretPosition(int x2) {
        this.impl_caretPosition = x2;
        __set(4);
        return this;
    }

    @Deprecated
    public B impl_selectionEnd(int x2) {
        this.impl_selectionEnd = x2;
        __set(6);
        return this;
    }

    @Deprecated
    public B impl_selectionStart(int x2) {
        this.impl_selectionStart = x2;
        __set(8);
        return this;
    }

    public B strikethrough(boolean x2) {
        this.strikethrough = x2;
        __set(9);
        return this;
    }

    public B text(String x2) {
        this.text = x2;
        __set(10);
        return this;
    }

    public B textAlignment(TextAlignment x2) {
        this.textAlignment = x2;
        __set(11);
        return this;
    }

    public B textOrigin(VPos x2) {
        this.textOrigin = x2;
        __set(12);
        return this;
    }

    public B underline(boolean x2) {
        this.underline = x2;
        __set(13);
        return this;
    }

    public B wrappingWidth(double x2) {
        this.wrappingWidth = x2;
        __set(14);
        return this;
    }

    public B x(double x2) {
        this.f12745x = x2;
        __set(15);
        return this;
    }

    public B y(double x2) {
        this.f12746y = x2;
        __set(16);
        return this;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // javafx.util.Builder
    /* renamed from: build */
    public Text build2() {
        Text x2 = new Text();
        applyTo(x2);
        return x2;
    }
}
