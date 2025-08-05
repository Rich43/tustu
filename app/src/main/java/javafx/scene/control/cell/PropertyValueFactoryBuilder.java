package javafx.scene.control.cell;

import javafx.scene.control.cell.PropertyValueFactoryBuilder;
import javafx.util.Builder;

@Deprecated
/* loaded from: jfxrt.jar:javafx/scene/control/cell/PropertyValueFactoryBuilder.class */
public class PropertyValueFactoryBuilder<S, T, B extends PropertyValueFactoryBuilder<S, T, B>> implements Builder<PropertyValueFactory<S, T>> {
    private String property;

    protected PropertyValueFactoryBuilder() {
    }

    public static <S, T> PropertyValueFactoryBuilder<S, T, ?> create() {
        return new PropertyValueFactoryBuilder<>();
    }

    public B property(String x2) {
        this.property = x2;
        return this;
    }

    @Override // javafx.util.Builder
    /* renamed from: build */
    public PropertyValueFactory<S, T> build2() {
        PropertyValueFactory<S, T> x2 = new PropertyValueFactory<>(this.property);
        return x2;
    }
}
