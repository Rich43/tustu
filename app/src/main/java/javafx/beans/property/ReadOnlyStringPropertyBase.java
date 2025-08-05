package javafx.beans.property;

import com.sun.javafx.binding.ExpressionHelper;
import javafx.beans.InvalidationListener;
import javafx.beans.value.ChangeListener;

/* loaded from: jfxrt.jar:javafx/beans/property/ReadOnlyStringPropertyBase.class */
public abstract class ReadOnlyStringPropertyBase extends ReadOnlyStringProperty {
    ExpressionHelper<String> helper;

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

    /* JADX INFO: Access modifiers changed from: protected */
    public void fireValueChangedEvent() {
        ExpressionHelper.fireValueChangedEvent(this.helper);
    }
}
