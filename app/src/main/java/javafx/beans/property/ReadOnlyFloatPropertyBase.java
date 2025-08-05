package javafx.beans.property;

import com.sun.javafx.binding.ExpressionHelper;
import javafx.beans.InvalidationListener;
import javafx.beans.binding.ObjectExpression;
import javafx.beans.value.ChangeListener;

/* loaded from: jfxrt.jar:javafx/beans/property/ReadOnlyFloatPropertyBase.class */
public abstract class ReadOnlyFloatPropertyBase extends ReadOnlyFloatProperty {
    ExpressionHelper<Number> helper;

    @Override // javafx.beans.property.ReadOnlyFloatProperty, javafx.beans.binding.FloatExpression
    public /* bridge */ /* synthetic */ ObjectExpression asObject() {
        return super.asObject();
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
    public void addListener(ChangeListener<? super Number> listener) {
        this.helper = ExpressionHelper.addListener(this.helper, this, listener);
    }

    @Override // javafx.beans.value.ObservableValue
    public void removeListener(ChangeListener<? super Number> listener) {
        this.helper = ExpressionHelper.removeListener(this.helper, listener);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void fireValueChangedEvent() {
        ExpressionHelper.fireValueChangedEvent(this.helper);
    }
}
