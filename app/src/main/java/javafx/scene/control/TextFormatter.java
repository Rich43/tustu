package javafx.scene.control;

import com.sun.javafx.scene.control.FormatterAccessor;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;
import javafx.beans.NamedArg;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.util.StringConverter;
import javax.management.JMX;
import org.apache.commons.math3.geometry.VectorFormat;

/* loaded from: jfxrt.jar:javafx/scene/control/TextFormatter.class */
public class TextFormatter<V> {
    private final StringConverter<V> valueConverter;
    private final UnaryOperator<Change> filter;
    private Consumer<TextFormatter<?>> textUpdater;
    public static final StringConverter<String> IDENTITY_STRING_CONVERTER = new StringConverter<String>() { // from class: javafx.scene.control.TextFormatter.1
        @Override // javafx.util.StringConverter
        public String toString(String object) {
            return object == null ? "" : object;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // javafx.util.StringConverter
        public String fromString(String string) {
            return string;
        }
    };
    private final ObjectProperty<V> value;

    public TextFormatter(@NamedArg("filter") UnaryOperator<Change> filter) {
        this(null, null, filter);
    }

    public TextFormatter(@NamedArg("valueConverter") StringConverter<V> valueConverter, @NamedArg(JMX.DEFAULT_VALUE_FIELD) V defaultValue, @NamedArg("filter") UnaryOperator<Change> filter) {
        this.value = new ObjectPropertyBase<V>() { // from class: javafx.scene.control.TextFormatter.2
            @Override // javafx.beans.property.ReadOnlyProperty
            public Object getBean() {
                return TextFormatter.this;
            }

            @Override // javafx.beans.property.ReadOnlyProperty
            public String getName() {
                return "value";
            }

            @Override // javafx.beans.property.ObjectPropertyBase
            protected void invalidated() {
                if (TextFormatter.this.valueConverter != null || get() == null) {
                    TextFormatter.this.updateText();
                } else {
                    if (isBound()) {
                        unbind();
                    }
                    throw new IllegalStateException("Value changes are not supported when valueConverter is not set");
                }
            }
        };
        this.filter = filter;
        this.valueConverter = valueConverter;
        setValue(defaultValue);
    }

    public TextFormatter(@NamedArg("valueConverter") StringConverter<V> valueConverter, @NamedArg(JMX.DEFAULT_VALUE_FIELD) V defaultValue) {
        this(valueConverter, defaultValue, null);
    }

    public TextFormatter(@NamedArg("valueConverter") StringConverter<V> valueConverter) {
        this(valueConverter, null, null);
    }

    public final StringConverter<V> getValueConverter() {
        return this.valueConverter;
    }

    public final UnaryOperator<Change> getFilter() {
        return this.filter;
    }

    public final ObjectProperty<V> valueProperty() {
        return this.value;
    }

    public final void setValue(V value) {
        if (this.valueConverter == null && value != null) {
            throw new IllegalStateException("Value changes are not supported when valueConverter is not set");
        }
        this.value.set(value);
    }

    public final V getValue() {
        return this.value.get();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateText() {
        if (this.textUpdater != null) {
            this.textUpdater.accept(this);
        }
    }

    void bindToControl(Consumer<TextFormatter<?>> updater) {
        if (this.textUpdater != null) {
            throw new IllegalStateException("Formatter is already used in other control");
        }
        this.textUpdater = updater;
    }

    void unbindFromControl() {
        this.textUpdater = null;
    }

    void updateValue(String text) {
        if (!this.value.isBound()) {
            try {
                V v2 = this.valueConverter.fromString(text);
                setValue(v2);
            } catch (Exception e2) {
                updateText();
            }
        }
    }

    /* loaded from: jfxrt.jar:javafx/scene/control/TextFormatter$Change.class */
    public static final class Change implements Cloneable {
        private final FormatterAccessor accessor;
        private Control control;
        int start;
        int end;
        String text;
        int anchor;
        int caret;

        Change(Control control, FormatterAccessor accessor, int anchor, int caret) {
            this(control, accessor, caret, caret, "", anchor, caret);
        }

        Change(Control control, FormatterAccessor accessor, int start, int end, String text) {
            this(control, accessor, start, end, text, start + text.length(), start + text.length());
        }

        Change(Control control, FormatterAccessor accessor, int start, int end, String text, int anchor, int caret) {
            this.control = control;
            this.accessor = accessor;
            this.start = start;
            this.end = end;
            this.text = text;
            this.anchor = anchor;
            this.caret = caret;
        }

        public final Control getControl() {
            return this.control;
        }

        public final int getRangeStart() {
            return this.start;
        }

        public final int getRangeEnd() {
            return this.end;
        }

        public final void setRange(int start, int end) {
            int length = this.accessor.getTextLength();
            if (start < 0 || start > length || end < 0 || end > length) {
                throw new IndexOutOfBoundsException();
            }
            this.start = start;
            this.end = end;
        }

        public final int getCaretPosition() {
            return this.caret;
        }

        public final int getAnchor() {
            return this.anchor;
        }

        public final int getControlCaretPosition() {
            return this.accessor.getCaret();
        }

        public final int getControlAnchor() {
            return this.accessor.getAnchor();
        }

        public final void selectRange(int newAnchor, int newCaretPosition) {
            if (newAnchor < 0 || newAnchor > (this.accessor.getTextLength() - (this.end - this.start)) + this.text.length() || newCaretPosition < 0 || newCaretPosition > (this.accessor.getTextLength() - (this.end - this.start)) + this.text.length()) {
                throw new IndexOutOfBoundsException();
            }
            this.anchor = newAnchor;
            this.caret = newCaretPosition;
        }

        public final IndexRange getSelection() {
            return IndexRange.normalize(this.anchor, this.caret);
        }

        public final void setAnchor(int newAnchor) {
            if (newAnchor < 0 || newAnchor > (this.accessor.getTextLength() - (this.end - this.start)) + this.text.length()) {
                throw new IndexOutOfBoundsException();
            }
            this.anchor = newAnchor;
        }

        public final void setCaretPosition(int newCaretPosition) {
            if (newCaretPosition < 0 || newCaretPosition > (this.accessor.getTextLength() - (this.end - this.start)) + this.text.length()) {
                throw new IndexOutOfBoundsException();
            }
            this.caret = newCaretPosition;
        }

        public final String getText() {
            return this.text;
        }

        public final void setText(String value) {
            if (value == null) {
                throw new NullPointerException();
            }
            this.text = value;
        }

        public final String getControlText() {
            return this.accessor.getText(0, this.accessor.getTextLength());
        }

        public final String getControlNewText() {
            return this.accessor.getText(0, this.start) + this.text + this.accessor.getText(this.end, this.accessor.getTextLength());
        }

        public final boolean isAdded() {
            return !this.text.isEmpty();
        }

        public final boolean isDeleted() {
            return this.start != this.end;
        }

        public final boolean isReplaced() {
            return isAdded() && isDeleted();
        }

        public final boolean isContentChange() {
            return isAdded() || isDeleted();
        }

        public String toString() {
            StringBuilder builder = new StringBuilder("TextInputControl.Change [");
            if (isReplaced()) {
                builder.append(" replaced \"").append(this.accessor.getText(this.start, this.end)).append("\" with \"").append(this.text).append("\" at (").append(this.start).append(", ").append(this.end).append(")");
            } else if (isDeleted()) {
                builder.append(" deleted \"").append(this.accessor.getText(this.start, this.end)).append("\" at (").append(this.start).append(", ").append(this.end).append(")");
            } else if (isAdded()) {
                builder.append(" added \"").append(this.text).append("\" at ").append(this.start);
            }
            if (isAdded() || isDeleted()) {
                builder.append(VectorFormat.DEFAULT_SEPARATOR);
            } else {
                builder.append(" ");
            }
            builder.append("new selection (anchor, caret): [").append(this.anchor).append(", ").append(this.caret).append("]");
            builder.append(" ]");
            return builder.toString();
        }

        /* renamed from: clone, reason: merged with bridge method [inline-methods] */
        public Change m3813clone() {
            try {
                return (Change) super.clone();
            } catch (CloneNotSupportedException e2) {
                throw new RuntimeException(e2);
            }
        }
    }
}
