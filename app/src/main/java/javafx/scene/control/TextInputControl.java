package javafx.scene.control;

import com.sun.javafx.binding.ExpressionHelper;
import com.sun.javafx.scene.control.FormatterAccessor;
import com.sun.javafx.util.Utils;
import com.sun.org.apache.xalan.internal.templates.Constants;
import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javafx.beans.DefaultProperty;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.binding.IntegerBinding;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableStringValue;
import javafx.beans.value.ObservableValue;
import javafx.css.CssMetaData;
import javafx.css.FontCssMetaData;
import javafx.css.PseudoClass;
import javafx.css.StyleOrigin;
import javafx.css.Styleable;
import javafx.css.StyleableObjectProperty;
import javafx.css.StyleableProperty;
import javafx.scene.AccessibleAction;
import javafx.scene.AccessibleAttribute;
import javafx.scene.control.TextFormatter;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.text.Font;
import javafx.util.StringConverter;
import javax.swing.JTree;

@DefaultProperty("text")
/* loaded from: jfxrt.jar:javafx/scene/control/TextInputControl.class */
public abstract class TextInputControl extends Control {
    private ObjectProperty<Font> font;
    private final Content content;
    private BreakIterator charIterator;
    private BreakIterator wordIterator;
    private FormatterAccessor accessor;
    private static final PseudoClass PSEUDO_CLASS_READONLY = PseudoClass.getPseudoClass("readonly");
    private StringProperty promptText = new SimpleStringProperty(this, "promptText", "") { // from class: javafx.scene.control.TextInputControl.4
        @Override // javafx.beans.property.StringPropertyBase
        protected void invalidated() {
            String txt = get();
            if (txt != null && txt.contains("\n")) {
                set(txt.replace("\n", ""));
            }
        }
    };
    private final ObjectProperty<TextFormatter<?>> textFormatter = new ObjectPropertyBase<TextFormatter<?>>() { // from class: javafx.scene.control.TextInputControl.5
        private TextFormatter<?> oldFormatter = null;

        @Override // javafx.beans.property.ReadOnlyProperty
        public Object getBean() {
            return TextInputControl.this;
        }

        @Override // javafx.beans.property.ReadOnlyProperty
        public String getName() {
            return "textFormatter";
        }

        @Override // javafx.beans.property.ObjectPropertyBase
        protected void invalidated() {
            TextFormatter<?> formatter = get();
            try {
                if (formatter != null) {
                    try {
                        formatter.bindToControl(f2 -> {
                            TextInputControl.this.updateText(f2);
                        });
                        if (!TextInputControl.this.isFocused()) {
                            TextInputControl.this.updateText(get());
                        }
                    } catch (IllegalStateException e2) {
                        if (isBound()) {
                            unbind();
                        }
                        set(null);
                        throw e2;
                    }
                }
                if (this.oldFormatter != null) {
                    this.oldFormatter.unbindFromControl();
                }
            } finally {
                this.oldFormatter = formatter;
            }
        }
    };
    private TextProperty text = new TextProperty();
    private ReadOnlyIntegerWrapper length = new ReadOnlyIntegerWrapper(this, "length");
    private BooleanProperty editable = new SimpleBooleanProperty(this, JTree.EDITABLE_PROPERTY, true) { // from class: javafx.scene.control.TextInputControl.6
        @Override // javafx.beans.property.BooleanPropertyBase
        protected void invalidated() {
            TextInputControl.this.pseudoClassStateChanged(TextInputControl.PSEUDO_CLASS_READONLY, !get());
        }
    };
    private ReadOnlyObjectWrapper<IndexRange> selection = new ReadOnlyObjectWrapper<>(this, "selection", new IndexRange(0, 0));
    private ReadOnlyStringWrapper selectedText = new ReadOnlyStringWrapper(this, "selectedText");
    private ReadOnlyIntegerWrapper anchor = new ReadOnlyIntegerWrapper(this, Constants.ELEMNAME_ANCHOR_STRING, 0);
    private ReadOnlyIntegerWrapper caretPosition = new ReadOnlyIntegerWrapper(this, "caretPosition", 0);
    private UndoRedoChange undoChangeHead = new UndoRedoChange();
    private UndoRedoChange undoChange = this.undoChangeHead;
    private boolean createNewUndoRecord = false;
    private final ReadOnlyBooleanWrapper undoable = new ReadOnlyBooleanWrapper(this, "undoable", false);
    private final ReadOnlyBooleanWrapper redoable = new ReadOnlyBooleanWrapper(this, "redoable", false);

    /* loaded from: jfxrt.jar:javafx/scene/control/TextInputControl$Content.class */
    protected interface Content extends ObservableStringValue {
        String get(int i2, int i3);

        void insert(int i2, String str, boolean z2);

        void delete(int i2, int i3, boolean z2);

        int length();
    }

    protected TextInputControl(Content content) {
        this.content = content;
        content.addListener(observable -> {
            if (content.length() > 0) {
                this.text.textIsNull = false;
            }
            this.text.controlContentHasChanged();
        });
        this.length.bind(new IntegerBinding() { // from class: javafx.scene.control.TextInputControl.1
            {
                bind(TextInputControl.this.text);
            }

            @Override // javafx.beans.binding.IntegerBinding
            protected int computeValue() {
                String txt = TextInputControl.this.text.get();
                if (txt == null) {
                    return 0;
                }
                return txt.length();
            }
        });
        this.selectedText.bind(new StringBinding() { // from class: javafx.scene.control.TextInputControl.2
            {
                bind(TextInputControl.this.selection, TextInputControl.this.text);
            }

            /* JADX WARN: Multi-variable type inference failed */
            @Override // javafx.beans.binding.StringBinding
            protected String computeValue() {
                String txt = TextInputControl.this.text.get();
                IndexRange sel = (IndexRange) TextInputControl.this.selection.get();
                if (txt == null || sel == null) {
                    return "";
                }
                int start = sel.getStart();
                int end = sel.getEnd();
                int length = txt.length();
                if (end > start + length) {
                    end = length;
                }
                if (start > length - 1) {
                    end = 0;
                    start = 0;
                }
                return txt.substring(start, end);
            }
        });
        focusedProperty().addListener((ob, o2, n2) -> {
            if (n2.booleanValue()) {
                if (getTextFormatter() != null) {
                    updateText(getTextFormatter());
                    return;
                }
                return;
            }
            commitValue();
        });
        getStyleClass().add("text-input");
    }

    public final ObjectProperty<Font> fontProperty() {
        if (this.font == null) {
            this.font = new StyleableObjectProperty<Font>(Font.getDefault()) { // from class: javafx.scene.control.TextInputControl.3
                private boolean fontSetByCss = false;

                @Override // javafx.css.StyleableObjectProperty, javafx.css.StyleableProperty
                public void applyStyle(StyleOrigin newOrigin, Font value) {
                    try {
                        try {
                            this.fontSetByCss = true;
                            super.applyStyle(newOrigin, (StyleOrigin) value);
                            this.fontSetByCss = false;
                        } catch (Exception e2) {
                            throw e2;
                        }
                    } catch (Throwable th) {
                        this.fontSetByCss = false;
                        throw th;
                    }
                }

                @Override // javafx.css.StyleableObjectProperty, javafx.beans.property.ObjectPropertyBase, javafx.beans.value.WritableObjectValue
                public void set(Font value) {
                    Font oldValue = get();
                    if (value == null) {
                        if (oldValue == null) {
                            return;
                        }
                    } else if (value.equals(oldValue)) {
                        return;
                    }
                    super.set((AnonymousClass3) value);
                }

                @Override // javafx.beans.property.ObjectPropertyBase
                protected void invalidated() {
                    if (!this.fontSetByCss) {
                        TextInputControl.this.impl_reapplyCSS();
                    }
                }

                @Override // javafx.css.StyleableProperty
                public CssMetaData<TextInputControl, Font> getCssMetaData() {
                    return StyleableProperties.FONT;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return TextInputControl.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "font";
                }
            };
        }
        return this.font;
    }

    public final void setFont(Font value) {
        fontProperty().setValue(value);
    }

    public final Font getFont() {
        return this.font == null ? Font.getDefault() : this.font.getValue2();
    }

    public final StringProperty promptTextProperty() {
        return this.promptText;
    }

    public final String getPromptText() {
        return this.promptText.get();
    }

    public final void setPromptText(String value) {
        this.promptText.set(value);
    }

    public final ObjectProperty<TextFormatter<?>> textFormatterProperty() {
        return this.textFormatter;
    }

    public final TextFormatter<?> getTextFormatter() {
        return this.textFormatter.get();
    }

    public final void setTextFormatter(TextFormatter<?> value) {
        this.textFormatter.set(value);
    }

    protected final Content getContent() {
        return this.content;
    }

    public final String getText() {
        return this.text.get();
    }

    public final void setText(String value) {
        this.text.set(value);
    }

    public final StringProperty textProperty() {
        return this.text;
    }

    public final int getLength() {
        return this.length.get();
    }

    public final ReadOnlyIntegerProperty lengthProperty() {
        return this.length.getReadOnlyProperty();
    }

    public final boolean isEditable() {
        return this.editable.getValue2().booleanValue();
    }

    public final void setEditable(boolean value) {
        this.editable.setValue(Boolean.valueOf(value));
    }

    public final BooleanProperty editableProperty() {
        return this.editable;
    }

    public final IndexRange getSelection() {
        return this.selection.getValue2();
    }

    public final ReadOnlyObjectProperty<IndexRange> selectionProperty() {
        return this.selection.getReadOnlyProperty();
    }

    public final String getSelectedText() {
        return this.selectedText.get();
    }

    public final ReadOnlyStringProperty selectedTextProperty() {
        return this.selectedText.getReadOnlyProperty();
    }

    public final int getAnchor() {
        return this.anchor.get();
    }

    public final ReadOnlyIntegerProperty anchorProperty() {
        return this.anchor.getReadOnlyProperty();
    }

    public final int getCaretPosition() {
        return this.caretPosition.get();
    }

    public final ReadOnlyIntegerProperty caretPositionProperty() {
        return this.caretPosition.getReadOnlyProperty();
    }

    public final boolean isUndoable() {
        return this.undoable.get();
    }

    public final ReadOnlyBooleanProperty undoableProperty() {
        return this.undoable.getReadOnlyProperty();
    }

    public final boolean isRedoable() {
        return this.redoable.get();
    }

    public final ReadOnlyBooleanProperty redoableProperty() {
        return this.redoable.getReadOnlyProperty();
    }

    public String getText(int start, int end) {
        if (start > end) {
            throw new IllegalArgumentException("The start must be <= the end");
        }
        if (start < 0 || end > getLength()) {
            throw new IndexOutOfBoundsException();
        }
        return getContent().get(start, end);
    }

    public void appendText(String text) {
        insertText(getLength(), text);
    }

    public void insertText(int index, String text) {
        replaceText(index, index, text);
    }

    public void deleteText(IndexRange range) {
        replaceText(range, "");
    }

    public void deleteText(int start, int end) {
        replaceText(start, end, "");
    }

    public void replaceText(IndexRange range, String text) {
        int start = range.getStart();
        int end = start + range.getLength();
        replaceText(start, end, text);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public void replaceText(int start, int end, String text) {
        if (start > end) {
            throw new IllegalArgumentException();
        }
        if (text == null) {
            throw new NullPointerException();
        }
        if (start < 0 || end > getLength()) {
            throw new IndexOutOfBoundsException();
        }
        if (!this.text.isBound()) {
            int oldLength = getLength();
            TextFormatter<?> formatter = getTextFormatter();
            TextFormatter.Change change = new TextFormatter.Change(this, getFormatterAccessor(), start, end, text);
            if (formatter != null && formatter.getFilter() != null) {
                change = (TextFormatter.Change) formatter.getFilter().apply(change);
                if (change == null) {
                    return;
                }
            }
            updateContent(change, oldLength == 0);
        }
    }

    private void updateContent(TextFormatter.Change change, boolean forceNewUndoRecord) {
        boolean nonEmptySelection = getSelection().getLength() > 0;
        String oldText = getText(change.start, change.end);
        int adjustmentAmount = replaceText(change.start, change.end, change.text, change.getAnchor(), change.getCaretPosition());
        int endOfUndoChange = this.undoChange == this.undoChangeHead ? -1 : this.undoChange.start + this.undoChange.newText.length();
        String newText = getText(change.start, (change.start + change.text.length()) - adjustmentAmount);
        if (this.createNewUndoRecord || nonEmptySelection || endOfUndoChange == -1 || forceNewUndoRecord || ((endOfUndoChange != change.start && endOfUndoChange != change.end) || change.start - change.end > 1)) {
            this.undoChange = this.undoChange.add(change.start, oldText, newText);
        } else if (change.start != change.end && change.text.isEmpty()) {
            if (this.undoChange.newText.length() > 0) {
                this.undoChange.newText = this.undoChange.newText.substring(0, change.start - this.undoChange.start);
                if (this.undoChange.newText.isEmpty()) {
                    this.undoChange = this.undoChange.discard();
                }
            } else if (change.start == endOfUndoChange) {
                StringBuilder sb = new StringBuilder();
                UndoRedoChange undoRedoChange = this.undoChange;
                undoRedoChange.oldText = sb.append(undoRedoChange.oldText).append(oldText).toString();
            } else {
                this.undoChange.oldText = oldText + this.undoChange.oldText;
                this.undoChange.start--;
            }
        } else {
            StringBuilder sb2 = new StringBuilder();
            UndoRedoChange undoRedoChange2 = this.undoChange;
            undoRedoChange2.newText = sb2.append(undoRedoChange2.newText).append(newText).toString();
        }
        updateUndoRedoState();
    }

    public void cut() {
        copy();
        IndexRange selection = getSelection();
        deleteText(selection.getStart(), selection.getEnd());
    }

    public void copy() {
        String selectedText = getSelectedText();
        if (selectedText.length() > 0) {
            ClipboardContent content = new ClipboardContent();
            content.putString(selectedText);
            Clipboard.getSystemClipboard().setContent(content);
        }
    }

    public void paste() {
        String text;
        Clipboard clipboard = Clipboard.getSystemClipboard();
        if (clipboard.hasString() && (text = clipboard.getString()) != null) {
            this.createNewUndoRecord = true;
            try {
                replaceSelection(text);
            } finally {
                this.createNewUndoRecord = false;
            }
        }
    }

    public void selectBackward() {
        if (getCaretPosition() > 0 && getLength() > 0) {
            if (this.charIterator == null) {
                this.charIterator = BreakIterator.getCharacterInstance();
            }
            this.charIterator.setText(getText());
            selectRange(getAnchor(), this.charIterator.preceding(getCaretPosition()));
        }
    }

    public void selectForward() {
        int textLength = getLength();
        if (textLength > 0 && getCaretPosition() < textLength) {
            if (this.charIterator == null) {
                this.charIterator = BreakIterator.getCharacterInstance();
            }
            this.charIterator.setText(getText());
            selectRange(getAnchor(), this.charIterator.following(getCaretPosition()));
        }
    }

    public void previousWord() {
        previousWord(false);
    }

    public void nextWord() {
        nextWord(false);
    }

    public void endOfNextWord() {
        endOfNextWord(false);
    }

    public void selectPreviousWord() {
        previousWord(true);
    }

    public void selectNextWord() {
        nextWord(true);
    }

    public void selectEndOfNextWord() {
        endOfNextWord(true);
    }

    private void previousWord(boolean select) {
        int pos;
        int textLength = getLength();
        String text = getText();
        if (textLength <= 0) {
            return;
        }
        if (this.wordIterator == null) {
            this.wordIterator = BreakIterator.getWordInstance();
        }
        this.wordIterator.setText(text);
        int iPreceding = this.wordIterator.preceding(Utils.clamp(0, getCaretPosition(), textLength));
        while (true) {
            pos = iPreceding;
            if (pos == -1 || Character.isLetterOrDigit(text.charAt(Utils.clamp(0, pos, textLength - 1)))) {
                break;
            } else {
                iPreceding = this.wordIterator.preceding(Utils.clamp(0, pos, textLength));
            }
        }
        selectRange(select ? getAnchor() : pos, pos);
    }

    private void nextWord(boolean select) {
        int textLength = getLength();
        String text = getText();
        if (textLength <= 0) {
            return;
        }
        if (this.wordIterator == null) {
            this.wordIterator = BreakIterator.getWordInstance();
        }
        this.wordIterator.setText(text);
        int last = this.wordIterator.following(Utils.clamp(0, getCaretPosition(), textLength - 1));
        int next = this.wordIterator.next();
        while (true) {
            int current = next;
            if (current != -1) {
                for (int p2 = last; p2 <= current; p2++) {
                    char ch = text.charAt(Utils.clamp(0, p2, textLength - 1));
                    if (ch != ' ' && ch != '\t') {
                        if (select) {
                            selectRange(getAnchor(), p2);
                            return;
                        } else {
                            selectRange(p2, p2);
                            return;
                        }
                    }
                }
                last = current;
                next = this.wordIterator.next();
            } else if (select) {
                selectRange(getAnchor(), textLength);
                return;
            } else {
                end();
                return;
            }
        }
    }

    private void endOfNextWord(boolean select) {
        int textLength = getLength();
        String text = getText();
        if (textLength <= 0) {
            return;
        }
        if (this.wordIterator == null) {
            this.wordIterator = BreakIterator.getWordInstance();
        }
        this.wordIterator.setText(text);
        int last = this.wordIterator.following(Utils.clamp(0, getCaretPosition(), textLength));
        int next = this.wordIterator.next();
        while (true) {
            int current = next;
            if (current != -1) {
                for (int p2 = last; p2 <= current; p2++) {
                    if (!Character.isLetterOrDigit(text.charAt(Utils.clamp(0, p2, textLength - 1)))) {
                        if (select) {
                            selectRange(getAnchor(), p2);
                            return;
                        } else {
                            selectRange(p2, p2);
                            return;
                        }
                    }
                }
                last = current;
                next = this.wordIterator.next();
            } else if (select) {
                selectRange(getAnchor(), textLength);
                return;
            } else {
                end();
                return;
            }
        }
    }

    public void selectAll() {
        selectRange(0, getLength());
    }

    public void home() {
        selectRange(0, 0);
    }

    public void end() {
        int textLength = getLength();
        if (textLength > 0) {
            selectRange(textLength, textLength);
        }
    }

    public void selectHome() {
        selectRange(getAnchor(), 0);
    }

    public void selectEnd() {
        int textLength = getLength();
        if (textLength > 0) {
            selectRange(getAnchor(), textLength);
        }
    }

    public boolean deletePreviousChar() {
        boolean failed = true;
        if (isEditable() && !isDisabled()) {
            String text = getText();
            int dot = getCaretPosition();
            int mark = getAnchor();
            if (dot != mark) {
                replaceSelection("");
                failed = false;
            } else if (dot > 0) {
                int p2 = Character.offsetByCodePoints(text, dot, -1);
                deleteText(p2, dot);
                failed = false;
            }
        }
        return !failed;
    }

    public boolean deleteNextChar() {
        boolean failed = true;
        if (isEditable() && !isDisabled()) {
            int textLength = getLength();
            String text = getText();
            int dot = getCaretPosition();
            int mark = getAnchor();
            if (dot != mark) {
                replaceSelection("");
                failed = false;
            } else if (textLength > 0 && dot < textLength) {
                if (this.charIterator == null) {
                    this.charIterator = BreakIterator.getCharacterInstance();
                }
                this.charIterator.setText(text);
                int p2 = this.charIterator.following(dot);
                deleteText(dot, p2);
                failed = false;
            }
        }
        return !failed;
    }

    public void forward() {
        int textLength = getLength();
        int dot = getCaretPosition();
        int mark = getAnchor();
        if (dot != mark) {
            int pos = Math.max(dot, mark);
            selectRange(pos, pos);
        } else if (dot < textLength && textLength > 0) {
            if (this.charIterator == null) {
                this.charIterator = BreakIterator.getCharacterInstance();
            }
            this.charIterator.setText(getText());
            int pos2 = this.charIterator.following(dot);
            selectRange(pos2, pos2);
        }
        deselect();
    }

    public void backward() {
        int textLength = getLength();
        int dot = getCaretPosition();
        int mark = getAnchor();
        if (dot != mark) {
            int pos = Math.min(dot, mark);
            selectRange(pos, pos);
        } else if (dot > 0 && textLength > 0) {
            if (this.charIterator == null) {
                this.charIterator = BreakIterator.getCharacterInstance();
            }
            this.charIterator.setText(getText());
            int pos2 = this.charIterator.preceding(dot);
            selectRange(pos2, pos2);
        }
        deselect();
    }

    public void positionCaret(int pos) {
        int p2 = Utils.clamp(0, pos, getLength());
        selectRange(p2, p2);
    }

    public void selectPositionCaret(int pos) {
        selectRange(getAnchor(), Utils.clamp(0, pos, getLength()));
    }

    /* JADX WARN: Multi-variable type inference failed */
    public void selectRange(int anchor, int caretPosition) {
        int caretPosition2 = Utils.clamp(0, caretPosition, getLength());
        TextFormatter.Change change = new TextFormatter.Change(this, getFormatterAccessor(), Utils.clamp(0, anchor, getLength()), caretPosition2);
        TextFormatter<?> formatter = getTextFormatter();
        if (formatter != null && formatter.getFilter() != null) {
            change = (TextFormatter.Change) formatter.getFilter().apply(change);
            if (change == null) {
                return;
            }
        }
        updateContent(change, false);
    }

    private void doSelectRange(int anchor, int caretPosition) {
        this.caretPosition.set(Utils.clamp(0, caretPosition, getLength()));
        this.anchor.set(Utils.clamp(0, anchor, getLength()));
        this.selection.set(IndexRange.normalize(getAnchor(), getCaretPosition()));
        notifyAccessibleAttributeChanged(AccessibleAttribute.SELECTION_START);
    }

    public void extendSelection(int pos) {
        int p2 = Utils.clamp(0, pos, getLength());
        int dot = getCaretPosition();
        int mark = getAnchor();
        int start = Math.min(dot, mark);
        int end = Math.max(dot, mark);
        if (p2 < start) {
            selectRange(end, p2);
        } else {
            selectRange(start, p2);
        }
    }

    public void clear() {
        deselect();
        if (!this.text.isBound()) {
            setText("");
        }
    }

    public void deselect() {
        selectRange(getCaretPosition(), getCaretPosition());
    }

    public void replaceSelection(String replacement) {
        replaceText(getSelection(), replacement);
    }

    public final void undo() {
        if (isUndoable()) {
            int start = this.undoChange.start;
            String newText = this.undoChange.newText;
            String oldText = this.undoChange.oldText;
            if (newText != null) {
                getContent().delete(start, start + newText.length(), oldText.isEmpty());
            }
            if (oldText != null) {
                getContent().insert(start, oldText, true);
                doSelectRange(start, start + oldText.length());
            } else {
                doSelectRange(start, start + newText.length());
            }
            this.undoChange = this.undoChange.prev;
        }
        updateUndoRedoState();
    }

    public final void redo() {
        if (isRedoable()) {
            this.undoChange = this.undoChange.next;
            int start = this.undoChange.start;
            String newText = this.undoChange.newText;
            String oldText = this.undoChange.oldText;
            if (oldText != null) {
                getContent().delete(start, start + oldText.length(), newText.isEmpty());
            }
            if (newText != null) {
                getContent().insert(start, newText, true);
                doSelectRange(start + newText.length(), start + newText.length());
            } else {
                doSelectRange(start, start);
            }
        }
        updateUndoRedoState();
    }

    void textUpdated() {
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void resetUndoRedoState() {
        this.undoChange = this.undoChangeHead;
        this.undoChange.next = null;
        updateUndoRedoState();
    }

    private void updateUndoRedoState() {
        this.undoable.set(this.undoChange != this.undoChangeHead);
        this.redoable.set(this.undoChange.next != null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Multi-variable type inference failed */
    public boolean filterAndSet(String value) {
        TextFormatter<?> formatter = getTextFormatter();
        int length = this.content.length();
        if (formatter != null && formatter.getFilter() != null && !this.text.isBound()) {
            TextFormatter.Change change = (TextFormatter.Change) formatter.getFilter().apply(new TextFormatter.Change(this, getFormatterAccessor(), 0, length, value, 0, 0));
            if (change == null) {
                return false;
            }
            replaceText(change.start, change.end, change.text, change.getAnchor(), change.getCaretPosition());
            return true;
        }
        replaceText(0, length, value, 0, 0);
        return true;
    }

    private int replaceText(int start, int end, String value, int anchor, int caretPosition) {
        int length = getLength();
        int adjustmentAmount = 0;
        if (end != start) {
            getContent().delete(start, end, value.isEmpty());
            length -= end - start;
        }
        if (value != null) {
            getContent().insert(start, value, true);
            adjustmentAmount = value.length() - (getLength() - length);
            anchor -= adjustmentAmount;
            caretPosition -= adjustmentAmount;
        }
        doSelectRange(anchor, caretPosition);
        return adjustmentAmount;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public <T> void updateText(TextFormatter<T> formatter) {
        T value = formatter.getValue();
        StringConverter<T> converter = formatter.getValueConverter();
        if (converter != null) {
            String text = converter.toString(value);
            replaceText(0, getLength(), text, text.length(), text.length());
        }
    }

    public final void commitValue() {
        if (getTextFormatter() != null) {
            getTextFormatter().updateValue(getText());
        }
    }

    public final void cancelEdit() {
        if (getTextFormatter() != null) {
            updateText(getTextFormatter());
        }
    }

    private FormatterAccessor getFormatterAccessor() {
        if (this.accessor == null) {
            this.accessor = new TextInputControlFromatterAccessor();
        }
        return this.accessor;
    }

    static String filterInput(String txt, boolean stripNewlines, boolean stripTabs) {
        if (containsInvalidCharacters(txt, stripNewlines, stripTabs)) {
            StringBuilder s2 = new StringBuilder(txt.length());
            for (int i2 = 0; i2 < txt.length(); i2++) {
                char c2 = txt.charAt(i2);
                if (!isInvalidCharacter(c2, stripNewlines, stripTabs)) {
                    s2.append(c2);
                }
            }
            txt = s2.toString();
        }
        return txt;
    }

    static boolean containsInvalidCharacters(String txt, boolean newlineIllegal, boolean tabIllegal) {
        for (int i2 = 0; i2 < txt.length(); i2++) {
            char c2 = txt.charAt(i2);
            if (isInvalidCharacter(c2, newlineIllegal, tabIllegal)) {
                return true;
            }
        }
        return false;
    }

    private static boolean isInvalidCharacter(char c2, boolean newlineIllegal, boolean tabIllegal) {
        if (c2 == 127) {
            return true;
        }
        return c2 == '\n' ? newlineIllegal : c2 == '\t' ? tabIllegal : c2 < ' ';
    }

    /* loaded from: jfxrt.jar:javafx/scene/control/TextInputControl$TextProperty.class */
    private class TextProperty extends StringProperty {
        private ObservableValue<? extends String> observable;
        private InvalidationListener listener;
        private ExpressionHelper<String> helper;
        private boolean textIsNull;

        private TextProperty() {
            this.observable = null;
            this.listener = null;
            this.helper = null;
            this.textIsNull = false;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // javafx.beans.value.ObservableObjectValue
        public String get() {
            if (this.textIsNull) {
                return null;
            }
            return TextInputControl.this.content.get();
        }

        @Override // javafx.beans.value.WritableObjectValue
        public void set(String value) {
            if (isBound()) {
                throw new RuntimeException("A bound value cannot be set.");
            }
            doSet(value);
            markInvalid();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void controlContentHasChanged() {
            markInvalid();
            TextInputControl.this.notifyAccessibleAttributeChanged(AccessibleAttribute.TEXT);
        }

        @Override // javafx.beans.property.Property
        public void bind(ObservableValue<? extends String> observable) {
            if (observable == null) {
                throw new NullPointerException("Cannot bind to null");
            }
            if (!observable.equals(this.observable)) {
                unbind();
                this.observable = observable;
                if (this.listener == null) {
                    this.listener = new Listener();
                }
                this.observable.addListener(this.listener);
                markInvalid();
                doSet(observable.getValue2());
            }
        }

        @Override // javafx.beans.property.Property
        public void unbind() {
            if (this.observable != null) {
                doSet(this.observable.getValue2());
                this.observable.removeListener(this.listener);
                this.observable = null;
            }
        }

        @Override // javafx.beans.property.Property
        public boolean isBound() {
            return this.observable != null;
        }

        @Override // javafx.beans.Observable
        public void addListener(InvalidationListener listener) {
            this.helper = ExpressionHelper.addListener(this.helper, this, listener);
        }

        @Override // javafx.beans.Observable
        public void removeListener(InvalidationListener listener) {
            this.helper = ExpressionHelper.removeListener(this.helper, listener);
        }

        @Override // javafx.beans.value.ObservableValue
        public void addListener(ChangeListener<? super String> listener) {
            this.helper = ExpressionHelper.addListener(this.helper, this, listener);
        }

        @Override // javafx.beans.value.ObservableValue
        public void removeListener(ChangeListener<? super String> listener) {
            this.helper = ExpressionHelper.removeListener(this.helper, listener);
        }

        @Override // javafx.beans.property.ReadOnlyProperty
        public Object getBean() {
            return TextInputControl.this;
        }

        @Override // javafx.beans.property.ReadOnlyProperty
        public String getName() {
            return "text";
        }

        private void fireValueChangedEvent() {
            ExpressionHelper.fireValueChangedEvent(this.helper);
        }

        private void markInvalid() {
            fireValueChangedEvent();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void doSet(String value) {
            this.textIsNull = value == null;
            if (value == null) {
                value = "";
            }
            if (TextInputControl.this.filterAndSet(value)) {
                if (TextInputControl.this.getTextFormatter() != null) {
                    TextInputControl.this.getTextFormatter().updateValue(TextInputControl.this.getText());
                }
                TextInputControl.this.textUpdated();
                TextInputControl.this.resetUndoRedoState();
            }
        }

        /* loaded from: jfxrt.jar:javafx/scene/control/TextInputControl$TextProperty$Listener.class */
        private class Listener implements InvalidationListener {
            private Listener() {
            }

            @Override // javafx.beans.InvalidationListener
            public void invalidated(Observable valueModel) {
                TextProperty.this.doSet((String) TextProperty.this.observable.getValue2());
            }
        }
    }

    /* loaded from: jfxrt.jar:javafx/scene/control/TextInputControl$UndoRedoChange.class */
    static class UndoRedoChange {
        int start;
        String oldText;
        String newText;
        UndoRedoChange prev;
        UndoRedoChange next;

        UndoRedoChange() {
        }

        public UndoRedoChange add(int start, String oldText, String newText) {
            UndoRedoChange c2 = new UndoRedoChange();
            c2.start = start;
            c2.oldText = oldText;
            c2.newText = newText;
            c2.prev = this;
            this.next = c2;
            return c2;
        }

        public UndoRedoChange discard() {
            this.prev.next = this.next;
            return this.prev;
        }

        void debugPrint() {
            System.out.print("[");
            for (UndoRedoChange c2 = this; c2 != null; c2 = c2.next) {
                System.out.print(c2.toString());
                if (c2.next != null) {
                    System.out.print(", ");
                }
            }
            System.out.println("]");
        }

        public String toString() {
            if (this.oldText == null && this.newText == null) {
                return "head";
            }
            if (this.oldText.isEmpty() && !this.newText.isEmpty()) {
                return "added '" + this.newText + "' at index " + this.start;
            }
            if (!this.oldText.isEmpty() && !this.newText.isEmpty()) {
                return "replaced '" + this.oldText + "' with '" + this.newText + "' at index " + this.start;
            }
            return "deleted '" + this.oldText + "' at index " + this.start;
        }
    }

    /* loaded from: jfxrt.jar:javafx/scene/control/TextInputControl$StyleableProperties.class */
    private static class StyleableProperties {
        private static final FontCssMetaData<TextInputControl> FONT = new FontCssMetaData<TextInputControl>("-fx-font", Font.getDefault()) { // from class: javafx.scene.control.TextInputControl.StyleableProperties.1
            @Override // javafx.css.CssMetaData
            public boolean isSettable(TextInputControl n2) {
                return n2.font == null || !n2.font.isBound();
            }

            @Override // javafx.css.CssMetaData
            public StyleableProperty<Font> getStyleableProperty(TextInputControl n2) {
                return (StyleableProperty) n2.fontProperty();
            }
        };
        private static final List<CssMetaData<? extends Styleable, ?>> STYLEABLES;

        private StyleableProperties() {
        }

        static {
            List<CssMetaData<? extends Styleable, ?>> styleables = new ArrayList<>(Control.getClassCssMetaData());
            styleables.add(FONT);
            STYLEABLES = Collections.unmodifiableList(styleables);
        }
    }

    public static List<CssMetaData<? extends Styleable, ?>> getClassCssMetaData() {
        return StyleableProperties.STYLEABLES;
    }

    @Override // javafx.scene.control.Control
    public List<CssMetaData<? extends Styleable, ?>> getControlCssMetaData() {
        return getClassCssMetaData();
    }

    @Override // javafx.scene.control.Control, javafx.scene.Parent, javafx.scene.Node
    public Object queryAccessibleAttribute(AccessibleAttribute attribute, Object... parameters) {
        switch (attribute) {
            case TEXT:
                String accText = getAccessibleText();
                if (accText != null && !accText.isEmpty()) {
                    return accText;
                }
                String text = getText();
                if (text == null || text.isEmpty()) {
                    text = getPromptText();
                }
                return text;
            case EDITABLE:
                return Boolean.valueOf(isEditable());
            case SELECTION_START:
                return Integer.valueOf(getSelection().getStart());
            case SELECTION_END:
                return Integer.valueOf(getSelection().getEnd());
            case CARET_OFFSET:
                return Integer.valueOf(getCaretPosition());
            case FONT:
                return getFont();
            default:
                return super.queryAccessibleAttribute(attribute, parameters);
        }
    }

    @Override // javafx.scene.control.Control, javafx.scene.Node
    public void executeAccessibleAction(AccessibleAction action, Object... parameters) {
        switch (action) {
            case SET_TEXT:
                String value = (String) parameters[0];
                if (value != null) {
                    setText(value);
                    break;
                }
                break;
            case SET_TEXT_SELECTION:
                break;
            default:
                super.executeAccessibleAction(action, parameters);
                return;
        }
        Integer start = (Integer) parameters[0];
        Integer end = (Integer) parameters[1];
        if (start != null && end != null) {
            selectRange(start.intValue(), end.intValue());
        }
    }

    /* loaded from: jfxrt.jar:javafx/scene/control/TextInputControl$TextInputControlFromatterAccessor.class */
    private class TextInputControlFromatterAccessor implements FormatterAccessor {
        private TextInputControlFromatterAccessor() {
        }

        @Override // com.sun.javafx.scene.control.FormatterAccessor
        public int getTextLength() {
            return TextInputControl.this.getLength();
        }

        @Override // com.sun.javafx.scene.control.FormatterAccessor
        public String getText(int begin, int end) {
            return TextInputControl.this.getText(begin, end);
        }

        @Override // com.sun.javafx.scene.control.FormatterAccessor
        public int getCaret() {
            return TextInputControl.this.getCaretPosition();
        }

        @Override // com.sun.javafx.scene.control.FormatterAccessor
        public int getAnchor() {
            return TextInputControl.this.getAnchor();
        }
    }
}
