package javafx.beans.binding;

import com.sun.javafx.binding.BindingHelperObserver;
import com.sun.javafx.binding.ExpressionHelper;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableNumberValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/* loaded from: jfxrt.jar:javafx/beans/binding/DoubleBinding.class */
public abstract class DoubleBinding extends DoubleExpression implements NumberBinding {
    private double value;
    private boolean valid;
    private BindingHelperObserver observer;
    private ExpressionHelper<Number> helper = null;

    protected abstract double computeValue();

    @Override // javafx.beans.binding.DoubleExpression, javafx.beans.value.ObservableValue
    /* renamed from: getValue */
    public /* bridge */ /* synthetic */ Number getValue2() {
        return super.getValue2();
    }

    @Override // javafx.beans.binding.DoubleExpression, javafx.beans.binding.NumberExpressionBase, javafx.beans.binding.NumberExpression
    public /* bridge */ /* synthetic */ NumberBinding divide(ObservableNumberValue observableNumberValue) {
        return super.divide(observableNumberValue);
    }

    @Override // javafx.beans.binding.DoubleExpression, javafx.beans.binding.NumberExpressionBase, javafx.beans.binding.NumberExpression
    public /* bridge */ /* synthetic */ NumberBinding multiply(ObservableNumberValue observableNumberValue) {
        return super.multiply(observableNumberValue);
    }

    @Override // javafx.beans.binding.DoubleExpression, javafx.beans.binding.NumberExpressionBase, javafx.beans.binding.NumberExpression
    public /* bridge */ /* synthetic */ NumberBinding subtract(ObservableNumberValue observableNumberValue) {
        return super.subtract(observableNumberValue);
    }

    @Override // javafx.beans.binding.DoubleExpression, javafx.beans.binding.NumberExpressionBase, javafx.beans.binding.NumberExpression
    public /* bridge */ /* synthetic */ NumberBinding add(ObservableNumberValue observableNumberValue) {
        return super.add(observableNumberValue);
    }

    @Override // javafx.beans.binding.DoubleExpression, javafx.beans.binding.NumberExpression
    public /* bridge */ /* synthetic */ NumberBinding divide(int i2) {
        return super.divide(i2);
    }

    @Override // javafx.beans.binding.DoubleExpression, javafx.beans.binding.NumberExpression
    public /* bridge */ /* synthetic */ NumberBinding divide(long j2) {
        return super.divide(j2);
    }

    @Override // javafx.beans.binding.DoubleExpression, javafx.beans.binding.NumberExpression
    public /* bridge */ /* synthetic */ NumberBinding divide(float f2) {
        return super.divide(f2);
    }

    @Override // javafx.beans.binding.DoubleExpression, javafx.beans.binding.NumberExpression
    public /* bridge */ /* synthetic */ NumberBinding divide(double d2) {
        return super.divide(d2);
    }

    @Override // javafx.beans.binding.DoubleExpression, javafx.beans.binding.NumberExpression
    public /* bridge */ /* synthetic */ NumberBinding multiply(int i2) {
        return super.multiply(i2);
    }

    @Override // javafx.beans.binding.DoubleExpression, javafx.beans.binding.NumberExpression
    public /* bridge */ /* synthetic */ NumberBinding multiply(long j2) {
        return super.multiply(j2);
    }

    @Override // javafx.beans.binding.DoubleExpression, javafx.beans.binding.NumberExpression
    public /* bridge */ /* synthetic */ NumberBinding multiply(float f2) {
        return super.multiply(f2);
    }

    @Override // javafx.beans.binding.DoubleExpression, javafx.beans.binding.NumberExpression
    public /* bridge */ /* synthetic */ NumberBinding multiply(double d2) {
        return super.multiply(d2);
    }

    @Override // javafx.beans.binding.DoubleExpression, javafx.beans.binding.NumberExpression
    public /* bridge */ /* synthetic */ NumberBinding subtract(int i2) {
        return super.subtract(i2);
    }

    @Override // javafx.beans.binding.DoubleExpression, javafx.beans.binding.NumberExpression
    public /* bridge */ /* synthetic */ NumberBinding subtract(long j2) {
        return super.subtract(j2);
    }

    @Override // javafx.beans.binding.DoubleExpression, javafx.beans.binding.NumberExpression
    public /* bridge */ /* synthetic */ NumberBinding subtract(float f2) {
        return super.subtract(f2);
    }

    @Override // javafx.beans.binding.DoubleExpression, javafx.beans.binding.NumberExpression
    public /* bridge */ /* synthetic */ NumberBinding subtract(double d2) {
        return super.subtract(d2);
    }

    @Override // javafx.beans.binding.DoubleExpression, javafx.beans.binding.NumberExpression
    public /* bridge */ /* synthetic */ NumberBinding add(int i2) {
        return super.add(i2);
    }

    @Override // javafx.beans.binding.DoubleExpression, javafx.beans.binding.NumberExpression
    public /* bridge */ /* synthetic */ NumberBinding add(long j2) {
        return super.add(j2);
    }

    @Override // javafx.beans.binding.DoubleExpression, javafx.beans.binding.NumberExpression
    public /* bridge */ /* synthetic */ NumberBinding add(float f2) {
        return super.add(f2);
    }

    @Override // javafx.beans.binding.DoubleExpression, javafx.beans.binding.NumberExpression
    public /* bridge */ /* synthetic */ NumberBinding add(double d2) {
        return super.add(d2);
    }

    @Override // javafx.beans.binding.DoubleExpression, javafx.beans.binding.NumberExpression
    public /* bridge */ /* synthetic */ NumberBinding negate() {
        return super.negate();
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

    protected final void bind(Observable... dependencies) {
        if (dependencies != null && dependencies.length > 0) {
            if (this.observer == null) {
                this.observer = new BindingHelperObserver(this);
            }
            for (Observable dep : dependencies) {
                dep.addListener(this.observer);
            }
        }
    }

    protected final void unbind(Observable... dependencies) {
        if (this.observer != null) {
            for (Observable dep : dependencies) {
                dep.removeListener(this.observer);
            }
            this.observer = null;
        }
    }

    public void dispose() {
    }

    public ObservableList<?> getDependencies() {
        return FXCollections.emptyObservableList();
    }

    @Override // javafx.beans.value.ObservableDoubleValue
    public final double get() {
        if (!this.valid) {
            this.value = computeValue();
            this.valid = true;
        }
        return this.value;
    }

    protected void onInvalidating() {
    }

    @Override // javafx.beans.binding.Binding
    public final void invalidate() {
        if (this.valid) {
            this.valid = false;
            onInvalidating();
            ExpressionHelper.fireValueChangedEvent(this.helper);
        }
    }

    @Override // javafx.beans.binding.Binding
    public final boolean isValid() {
        return this.valid;
    }

    public String toString() {
        return this.valid ? "DoubleBinding [value: " + get() + "]" : "DoubleBinding [invalid]";
    }
}
