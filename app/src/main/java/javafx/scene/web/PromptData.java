package javafx.scene.web;

import javafx.beans.NamedArg;
import javax.management.JMX;

/* loaded from: jfxrt.jar:javafx/scene/web/PromptData.class */
public final class PromptData {
    private final String message;
    private final String defaultValue;

    public PromptData(@NamedArg("message") String message, @NamedArg(JMX.DEFAULT_VALUE_FIELD) String defaultValue) {
        this.message = message;
        this.defaultValue = defaultValue;
    }

    public final String getMessage() {
        return this.message;
    }

    public final String getDefaultValue() {
        return this.defaultValue;
    }
}
