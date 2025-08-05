package javafx.scene.control;

import javafx.collections.ObservableList;
import javafx.scene.control.ChoiceBoxBuilder;
import javafx.util.Builder;
import javafx.util.StringConverter;

@Deprecated
/* loaded from: jfxrt.jar:javafx/scene/control/ChoiceBoxBuilder.class */
public class ChoiceBoxBuilder<T, B extends ChoiceBoxBuilder<T, B>> extends ControlBuilder<B> implements Builder<ChoiceBox<T>> {
    private int __set;
    private StringConverter<T> converter;
    private ObservableList<T> items;
    private SingleSelectionModel<T> selectionModel;
    private T value;

    protected ChoiceBoxBuilder() {
    }

    public static <T> ChoiceBoxBuilder<T, ?> create() {
        return new ChoiceBoxBuilder<>();
    }

    public void applyTo(ChoiceBox<T> x2) {
        super.applyTo((Control) x2);
        int set = this.__set;
        if ((set & 1) != 0) {
            x2.setConverter(this.converter);
        }
        if ((set & 2) != 0) {
            x2.setItems(this.items);
        }
        if ((set & 4) != 0) {
            x2.setSelectionModel(this.selectionModel);
        }
        if ((set & 8) != 0) {
            x2.setValue(this.value);
        }
    }

    public B converter(StringConverter<T> x2) {
        this.converter = x2;
        this.__set |= 1;
        return this;
    }

    public B items(ObservableList<T> x2) {
        this.items = x2;
        this.__set |= 2;
        return this;
    }

    public B selectionModel(SingleSelectionModel<T> x2) {
        this.selectionModel = x2;
        this.__set |= 4;
        return this;
    }

    public B value(T x2) {
        this.value = x2;
        this.__set |= 8;
        return this;
    }

    @Override // javafx.util.Builder
    /* renamed from: build */
    public ChoiceBox<T> build2() {
        ChoiceBox<T> x2 = new ChoiceBox<>();
        applyTo((ChoiceBox) x2);
        return x2;
    }
}
