package javafx.scene.input;

import java.io.Serializable;
import javafx.beans.NamedArg;

/* loaded from: jfxrt.jar:javafx/scene/input/InputMethodTextRun.class */
public class InputMethodTextRun implements Serializable {
    private final String text;
    private final InputMethodHighlight highlight;

    public InputMethodTextRun(@NamedArg("text") String text, @NamedArg("highlight") InputMethodHighlight highlight) {
        this.text = text;
        this.highlight = highlight;
    }

    public final String getText() {
        return this.text;
    }

    public final InputMethodHighlight getHighlight() {
        return this.highlight;
    }

    public String toString() {
        return "InputMethodTextRun text [" + getText() + "], highlight [" + ((Object) getHighlight()) + "]";
    }
}
