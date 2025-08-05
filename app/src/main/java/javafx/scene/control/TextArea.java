package javafx.scene.control;

import com.sun.javafx.binding.ExpressionHelper;
import com.sun.javafx.collections.ListListenerHelper;
import com.sun.javafx.collections.NonIterableChange;
import com.sun.javafx.css.converters.SizeConverter;
import com.sun.javafx.scene.control.skin.TextAreaSkin;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import javafx.beans.InvalidationListener;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.css.CssMetaData;
import javafx.css.StyleConverter;
import javafx.css.Styleable;
import javafx.css.StyleableBooleanProperty;
import javafx.css.StyleableIntegerProperty;
import javafx.css.StyleableProperty;
import javafx.scene.AccessibleRole;
import javafx.scene.control.TextInputControl;

/* loaded from: jfxrt.jar:javafx/scene/control/TextArea.class */
public class TextArea extends TextInputControl {
    public static final int DEFAULT_PREF_COLUMN_COUNT = 40;
    public static final int DEFAULT_PREF_ROW_COUNT = 10;
    public static final int DEFAULT_PARAGRAPH_CAPACITY = 32;
    private BooleanProperty wrapText;
    private IntegerProperty prefColumnCount;
    private IntegerProperty prefRowCount;
    private DoubleProperty scrollTop;
    private DoubleProperty scrollLeft;

    /* loaded from: jfxrt.jar:javafx/scene/control/TextArea$TextAreaContent.class */
    private static final class TextAreaContent implements TextInputControl.Content {
        private ExpressionHelper<String> helper;
        private ArrayList<StringBuilder> paragraphs;
        private int contentLength;
        private ParagraphList paragraphList;
        private ListListenerHelper<CharSequence> listenerHelper;

        private TextAreaContent() {
            this.helper = null;
            this.paragraphs = new ArrayList<>();
            this.contentLength = 0;
            this.paragraphList = new ParagraphList();
            this.paragraphs.add(new StringBuilder(32));
            this.paragraphList.content = this;
        }

        @Override // javafx.scene.control.TextInputControl.Content
        public String get(int start, int end) {
            int length = end - start;
            StringBuilder textBuilder = new StringBuilder(length);
            int paragraphCount = this.paragraphs.size();
            int paragraphIndex = 0;
            int offset = start;
            while (paragraphIndex < paragraphCount) {
                StringBuilder paragraph = this.paragraphs.get(paragraphIndex);
                int count = paragraph.length() + 1;
                if (offset < count) {
                    break;
                }
                offset -= count;
                paragraphIndex++;
            }
            StringBuilder paragraph2 = this.paragraphs.get(paragraphIndex);
            for (int i2 = 0; i2 < length; i2++) {
                if (offset == paragraph2.length() && i2 < this.contentLength) {
                    textBuilder.append('\n');
                    paragraphIndex++;
                    paragraph2 = this.paragraphs.get(paragraphIndex);
                    offset = 0;
                } else {
                    int i3 = offset;
                    offset++;
                    textBuilder.append(paragraph2.charAt(i3));
                }
            }
            return textBuilder.toString();
        }

        @Override // javafx.scene.control.TextInputControl.Content
        public void insert(int index, String text, boolean notifyListeners) {
            StringBuilder paragraph;
            if (index < 0 || index > this.contentLength) {
                throw new IndexOutOfBoundsException();
            }
            if (text == null) {
                throw new IllegalArgumentException();
            }
            String text2 = TextInputControl.filterInput(text, false, false);
            int length = text2.length();
            if (length > 0) {
                ArrayList<StringBuilder> lines = new ArrayList<>();
                StringBuilder line = new StringBuilder(32);
                for (int i2 = 0; i2 < length; i2++) {
                    char c2 = text2.charAt(i2);
                    if (c2 == '\n') {
                        lines.add(line);
                        line = new StringBuilder(32);
                    } else {
                        line.append(c2);
                    }
                }
                lines.add(line);
                int paragraphIndex = this.paragraphs.size();
                int offset = this.contentLength + 1;
                do {
                    paragraphIndex--;
                    paragraph = this.paragraphs.get(paragraphIndex);
                    offset -= paragraph.length() + 1;
                } while (index < offset);
                int start = index - offset;
                int n2 = lines.size();
                if (n2 == 1) {
                    paragraph.insert(start, (CharSequence) line);
                    fireParagraphListChangeEvent(paragraphIndex, paragraphIndex + 1, Collections.singletonList(paragraph));
                } else {
                    int end = paragraph.length();
                    CharSequence trailingText = paragraph.subSequence(start, end);
                    paragraph.delete(start, end);
                    StringBuilder first = lines.get(0);
                    paragraph.insert(start, (CharSequence) first);
                    line.append(trailingText);
                    fireParagraphListChangeEvent(paragraphIndex, paragraphIndex + 1, Collections.singletonList(paragraph));
                    this.paragraphs.addAll(paragraphIndex + 1, lines.subList(1, n2));
                    fireParagraphListChangeEvent(paragraphIndex + 1, paragraphIndex + n2, Collections.EMPTY_LIST);
                }
                this.contentLength += length;
                if (notifyListeners) {
                    ExpressionHelper.fireValueChangedEvent(this.helper);
                }
            }
        }

        @Override // javafx.scene.control.TextInputControl.Content
        public void delete(int start, int end, boolean notifyListeners) {
            StringBuilder paragraph;
            StringBuilder paragraph2;
            if (start > end) {
                throw new IllegalArgumentException();
            }
            if (start < 0 || end > this.contentLength) {
                throw new IndexOutOfBoundsException();
            }
            int length = end - start;
            if (length > 0) {
                int paragraphIndex = this.paragraphs.size();
                int offset = this.contentLength + 1;
                do {
                    paragraphIndex--;
                    paragraph = this.paragraphs.get(paragraphIndex);
                    offset -= paragraph.length() + 1;
                } while (end < offset);
                int paragraphIndex2 = paragraphIndex + 1;
                int offset2 = offset + paragraph.length() + 1;
                do {
                    paragraphIndex2--;
                    paragraph2 = this.paragraphs.get(paragraphIndex2);
                    offset2 -= paragraph2.length() + 1;
                } while (start < offset2);
                if (paragraphIndex2 == paragraphIndex) {
                    paragraph2.delete(start - offset2, end - offset2);
                    fireParagraphListChangeEvent(paragraphIndex2, paragraphIndex2 + 1, Collections.singletonList(paragraph2));
                } else {
                    CharSequence leadingSegment = paragraph2.subSequence(0, start - offset2);
                    int trailingSegmentLength = (start + length) - offset;
                    paragraph.delete(0, trailingSegmentLength);
                    fireParagraphListChangeEvent(paragraphIndex, paragraphIndex + 1, Collections.singletonList(paragraph));
                    if (paragraphIndex - paragraphIndex2 > 0) {
                        List<CharSequence> removed = new ArrayList<>(this.paragraphs.subList(paragraphIndex2, paragraphIndex));
                        this.paragraphs.subList(paragraphIndex2, paragraphIndex).clear();
                        fireParagraphListChangeEvent(paragraphIndex2, paragraphIndex2, removed);
                    }
                    paragraph.insert(0, leadingSegment);
                    fireParagraphListChangeEvent(paragraphIndex2, paragraphIndex2 + 1, Collections.singletonList(paragraph2));
                }
                this.contentLength -= length;
                if (notifyListeners) {
                    ExpressionHelper.fireValueChangedEvent(this.helper);
                }
            }
        }

        @Override // javafx.scene.control.TextInputControl.Content
        public int length() {
            return this.contentLength;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // javafx.beans.value.ObservableObjectValue
        public String get() {
            return get(0, length());
        }

        @Override // javafx.beans.value.ObservableValue
        public void addListener(ChangeListener<? super String> changeListener) {
            this.helper = ExpressionHelper.addListener(this.helper, this, changeListener);
        }

        @Override // javafx.beans.value.ObservableValue
        public void removeListener(ChangeListener<? super String> changeListener) {
            this.helper = ExpressionHelper.removeListener(this.helper, changeListener);
        }

        @Override // javafx.beans.value.ObservableValue
        /* renamed from: getValue */
        public String getValue2() {
            return get();
        }

        @Override // javafx.beans.Observable
        public void addListener(InvalidationListener listener) {
            this.helper = ExpressionHelper.addListener(this.helper, this, listener);
        }

        @Override // javafx.beans.Observable
        public void removeListener(InvalidationListener listener) {
            this.helper = ExpressionHelper.removeListener(this.helper, listener);
        }

        private void fireParagraphListChangeEvent(int from, int to, List<CharSequence> removed) {
            ParagraphListChange change = new ParagraphListChange(this.paragraphList, from, to, removed);
            ListListenerHelper.fireValueChangedEvent(this.listenerHelper, change);
        }
    }

    /* loaded from: jfxrt.jar:javafx/scene/control/TextArea$ParagraphList.class */
    private static final class ParagraphList extends AbstractList<CharSequence> implements ObservableList<CharSequence> {
        private TextAreaContent content;

        private ParagraphList() {
        }

        @Override // java.util.AbstractList, java.util.List
        public CharSequence get(int index) {
            return (CharSequence) this.content.paragraphs.get(index);
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public boolean addAll(Collection<? extends CharSequence> paragraphs) {
            throw new UnsupportedOperationException();
        }

        @Override // javafx.collections.ObservableList
        public boolean addAll(CharSequence... paragraphs) {
            throw new UnsupportedOperationException();
        }

        @Override // javafx.collections.ObservableList
        public boolean setAll(Collection<? extends CharSequence> paragraphs) {
            throw new UnsupportedOperationException();
        }

        @Override // javafx.collections.ObservableList
        public boolean setAll(CharSequence... paragraphs) {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public int size() {
            return this.content.paragraphs.size();
        }

        @Override // javafx.collections.ObservableList
        public void addListener(ListChangeListener<? super CharSequence> listener) {
            this.content.listenerHelper = ListListenerHelper.addListener(this.content.listenerHelper, listener);
        }

        @Override // javafx.collections.ObservableList
        public void removeListener(ListChangeListener<? super CharSequence> listener) {
            this.content.listenerHelper = ListListenerHelper.removeListener(this.content.listenerHelper, listener);
        }

        @Override // javafx.collections.ObservableList
        public boolean removeAll(CharSequence... elements) {
            throw new UnsupportedOperationException();
        }

        @Override // javafx.collections.ObservableList
        public boolean retainAll(CharSequence... elements) {
            throw new UnsupportedOperationException();
        }

        @Override // javafx.collections.ObservableList
        public void remove(int from, int to) {
            throw new UnsupportedOperationException();
        }

        @Override // javafx.beans.Observable
        public void addListener(InvalidationListener listener) {
            this.content.listenerHelper = ListListenerHelper.addListener(this.content.listenerHelper, listener);
        }

        @Override // javafx.beans.Observable
        public void removeListener(InvalidationListener listener) {
            this.content.listenerHelper = ListListenerHelper.removeListener(this.content.listenerHelper, listener);
        }
    }

    /* loaded from: jfxrt.jar:javafx/scene/control/TextArea$ParagraphListChange.class */
    private static final class ParagraphListChange extends NonIterableChange<CharSequence> {
        private List<CharSequence> removed;

        protected ParagraphListChange(ObservableList<CharSequence> list, int from, int to, List<CharSequence> removed) {
            super(from, to, list);
            this.removed = removed;
        }

        @Override // javafx.collections.ListChangeListener.Change
        public List<CharSequence> getRemoved() {
            return this.removed;
        }

        @Override // com.sun.javafx.collections.NonIterableChange, javafx.collections.ListChangeListener.Change
        protected int[] getPermutation() {
            return new int[0];
        }
    }

    public TextArea() {
        this("");
    }

    public TextArea(String text) {
        super(new TextAreaContent());
        this.wrapText = new StyleableBooleanProperty(false) { // from class: javafx.scene.control.TextArea.1
            @Override // javafx.beans.property.ReadOnlyProperty
            public Object getBean() {
                return TextArea.this;
            }

            @Override // javafx.beans.property.ReadOnlyProperty
            public String getName() {
                return "wrapText";
            }

            @Override // javafx.css.StyleableProperty
            public CssMetaData<? extends Styleable, Boolean> getCssMetaData() {
                return StyleableProperties.WRAP_TEXT;
            }
        };
        this.prefColumnCount = new StyleableIntegerProperty(40) { // from class: javafx.scene.control.TextArea.2
            private int oldValue = get();

            @Override // javafx.beans.property.IntegerPropertyBase
            protected void invalidated() {
                int value = get();
                if (value < 0) {
                    if (isBound()) {
                        unbind();
                    }
                    set(this.oldValue);
                    throw new IllegalArgumentException("value cannot be negative.");
                }
                this.oldValue = value;
            }

            @Override // javafx.css.StyleableProperty
            public CssMetaData<? extends Styleable, Number> getCssMetaData() {
                return StyleableProperties.PREF_COLUMN_COUNT;
            }

            @Override // javafx.beans.property.ReadOnlyProperty
            public Object getBean() {
                return TextArea.this;
            }

            @Override // javafx.beans.property.ReadOnlyProperty
            public String getName() {
                return "prefColumnCount";
            }
        };
        this.prefRowCount = new StyleableIntegerProperty(10) { // from class: javafx.scene.control.TextArea.3
            private int oldValue = get();

            @Override // javafx.beans.property.IntegerPropertyBase
            protected void invalidated() {
                int value = get();
                if (value < 0) {
                    if (isBound()) {
                        unbind();
                    }
                    set(this.oldValue);
                    throw new IllegalArgumentException("value cannot be negative.");
                }
                this.oldValue = value;
            }

            @Override // javafx.css.StyleableProperty
            public CssMetaData<? extends Styleable, Number> getCssMetaData() {
                return StyleableProperties.PREF_ROW_COUNT;
            }

            @Override // javafx.beans.property.ReadOnlyProperty
            public Object getBean() {
                return TextArea.this;
            }

            @Override // javafx.beans.property.ReadOnlyProperty
            public String getName() {
                return "prefRowCount";
            }
        };
        this.scrollTop = new SimpleDoubleProperty(this, "scrollTop", 0.0d);
        this.scrollLeft = new SimpleDoubleProperty(this, "scrollLeft", 0.0d);
        getStyleClass().add("text-area");
        setAccessibleRole(AccessibleRole.TEXT_AREA);
        setText(text);
    }

    @Override // javafx.scene.control.TextInputControl
    final void textUpdated() {
        setScrollTop(0.0d);
        setScrollLeft(0.0d);
    }

    public ObservableList<CharSequence> getParagraphs() {
        return ((TextAreaContent) getContent()).paragraphList;
    }

    public final BooleanProperty wrapTextProperty() {
        return this.wrapText;
    }

    public final boolean isWrapText() {
        return this.wrapText.getValue2().booleanValue();
    }

    public final void setWrapText(boolean value) {
        this.wrapText.setValue(Boolean.valueOf(value));
    }

    public final IntegerProperty prefColumnCountProperty() {
        return this.prefColumnCount;
    }

    public final int getPrefColumnCount() {
        return this.prefColumnCount.getValue2().intValue();
    }

    public final void setPrefColumnCount(int value) {
        this.prefColumnCount.setValue((Number) Integer.valueOf(value));
    }

    public final IntegerProperty prefRowCountProperty() {
        return this.prefRowCount;
    }

    public final int getPrefRowCount() {
        return this.prefRowCount.getValue2().intValue();
    }

    public final void setPrefRowCount(int value) {
        this.prefRowCount.setValue((Number) Integer.valueOf(value));
    }

    public final DoubleProperty scrollTopProperty() {
        return this.scrollTop;
    }

    public final double getScrollTop() {
        return this.scrollTop.getValue2().doubleValue();
    }

    public final void setScrollTop(double value) {
        this.scrollTop.setValue((Number) Double.valueOf(value));
    }

    public final DoubleProperty scrollLeftProperty() {
        return this.scrollLeft;
    }

    public final double getScrollLeft() {
        return this.scrollLeft.getValue2().doubleValue();
    }

    public final void setScrollLeft(double value) {
        this.scrollLeft.setValue((Number) Double.valueOf(value));
    }

    @Override // javafx.scene.control.Control
    protected Skin<?> createDefaultSkin() {
        return new TextAreaSkin(this);
    }

    /* loaded from: jfxrt.jar:javafx/scene/control/TextArea$StyleableProperties.class */
    private static class StyleableProperties {
        private static final CssMetaData<TextArea, Number> PREF_COLUMN_COUNT = new CssMetaData<TextArea, Number>("-fx-pref-column-count", SizeConverter.getInstance(), 40) { // from class: javafx.scene.control.TextArea.StyleableProperties.1
            @Override // javafx.css.CssMetaData
            public boolean isSettable(TextArea n2) {
                return !n2.prefColumnCount.isBound();
            }

            @Override // javafx.css.CssMetaData
            public StyleableProperty<Number> getStyleableProperty(TextArea n2) {
                return (StyleableProperty) n2.prefColumnCountProperty();
            }
        };
        private static final CssMetaData<TextArea, Number> PREF_ROW_COUNT = new CssMetaData<TextArea, Number>("-fx-pref-row-count", SizeConverter.getInstance(), 10) { // from class: javafx.scene.control.TextArea.StyleableProperties.2
            @Override // javafx.css.CssMetaData
            public boolean isSettable(TextArea n2) {
                return !n2.prefRowCount.isBound();
            }

            @Override // javafx.css.CssMetaData
            public StyleableProperty<Number> getStyleableProperty(TextArea n2) {
                return (StyleableProperty) n2.prefRowCountProperty();
            }
        };
        private static final CssMetaData<TextArea, Boolean> WRAP_TEXT = new CssMetaData<TextArea, Boolean>("-fx-wrap-text", StyleConverter.getBooleanConverter(), false) { // from class: javafx.scene.control.TextArea.StyleableProperties.3
            @Override // javafx.css.CssMetaData
            public boolean isSettable(TextArea n2) {
                return !n2.wrapText.isBound();
            }

            @Override // javafx.css.CssMetaData
            public StyleableProperty<Boolean> getStyleableProperty(TextArea n2) {
                return (StyleableProperty) n2.wrapTextProperty();
            }
        };
        private static final List<CssMetaData<? extends Styleable, ?>> STYLEABLES;

        private StyleableProperties() {
        }

        static {
            List<CssMetaData<? extends Styleable, ?>> styleables = new ArrayList<>(TextInputControl.getClassCssMetaData());
            styleables.add(PREF_COLUMN_COUNT);
            styleables.add(PREF_ROW_COUNT);
            styleables.add(WRAP_TEXT);
            STYLEABLES = Collections.unmodifiableList(styleables);
        }
    }

    public static List<CssMetaData<? extends Styleable, ?>> getClassCssMetaData() {
        return StyleableProperties.STYLEABLES;
    }

    @Override // javafx.scene.control.TextInputControl, javafx.scene.control.Control
    public List<CssMetaData<? extends Styleable, ?>> getControlCssMetaData() {
        return getClassCssMetaData();
    }
}
