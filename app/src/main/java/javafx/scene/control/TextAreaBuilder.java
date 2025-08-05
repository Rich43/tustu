package javafx.scene.control;

import java.util.Arrays;
import java.util.Collection;
import javafx.scene.control.TextAreaBuilder;
import javafx.util.Builder;

@Deprecated
/* loaded from: jfxrt.jar:javafx/scene/control/TextAreaBuilder.class */
public class TextAreaBuilder<B extends TextAreaBuilder<B>> extends TextInputControlBuilder<B> implements Builder<TextArea> {
    private int __set;
    private Collection<? extends CharSequence> paragraphs;
    private int prefColumnCount;
    private int prefRowCount;
    private String promptText;
    private double scrollLeft;
    private double scrollTop;
    private boolean wrapText;

    protected TextAreaBuilder() {
    }

    public static TextAreaBuilder<?> create() {
        return new TextAreaBuilder<>();
    }

    public void applyTo(TextArea x2) {
        super.applyTo((TextInputControl) x2);
        int set = this.__set;
        if ((set & 1) != 0) {
            x2.getParagraphs().addAll(this.paragraphs);
        }
        if ((set & 2) != 0) {
            x2.setPrefColumnCount(this.prefColumnCount);
        }
        if ((set & 4) != 0) {
            x2.setPrefRowCount(this.prefRowCount);
        }
        if ((set & 8) != 0) {
            x2.setPromptText(this.promptText);
        }
        if ((set & 16) != 0) {
            x2.setScrollLeft(this.scrollLeft);
        }
        if ((set & 32) != 0) {
            x2.setScrollTop(this.scrollTop);
        }
        if ((set & 64) != 0) {
            x2.setWrapText(this.wrapText);
        }
    }

    public B paragraphs(Collection<? extends CharSequence> x2) {
        this.paragraphs = x2;
        this.__set |= 1;
        return this;
    }

    public B paragraphs(CharSequence... charSequenceArr) {
        return (B) paragraphs(Arrays.asList(charSequenceArr));
    }

    public B prefColumnCount(int x2) {
        this.prefColumnCount = x2;
        this.__set |= 2;
        return this;
    }

    public B prefRowCount(int x2) {
        this.prefRowCount = x2;
        this.__set |= 4;
        return this;
    }

    @Override // javafx.scene.control.TextInputControlBuilder
    public B promptText(String x2) {
        this.promptText = x2;
        this.__set |= 8;
        return this;
    }

    public B scrollLeft(double x2) {
        this.scrollLeft = x2;
        this.__set |= 16;
        return this;
    }

    public B scrollTop(double x2) {
        this.scrollTop = x2;
        this.__set |= 32;
        return this;
    }

    public B wrapText(boolean x2) {
        this.wrapText = x2;
        this.__set |= 64;
        return this;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // javafx.util.Builder
    /* renamed from: build */
    public TextArea build2() {
        TextArea x2 = new TextArea();
        applyTo(x2);
        return x2;
    }
}
