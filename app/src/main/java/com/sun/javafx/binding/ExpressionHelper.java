package com.sun.javafx.binding;

import java.util.Arrays;
import javafx.beans.InvalidationListener;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

/* loaded from: jfxrt.jar:com/sun/javafx/binding/ExpressionHelper.class */
public abstract class ExpressionHelper<T> extends ExpressionHelperBase {
    protected final ObservableValue<T> observable;

    protected abstract ExpressionHelper<T> addListener(InvalidationListener invalidationListener);

    protected abstract ExpressionHelper<T> removeListener(InvalidationListener invalidationListener);

    protected abstract ExpressionHelper<T> addListener(ChangeListener<? super T> changeListener);

    protected abstract ExpressionHelper<T> removeListener(ChangeListener<? super T> changeListener);

    protected abstract void fireValueChangedEvent();

    public static <T> ExpressionHelper<T> addListener(ExpressionHelper<T> helper, ObservableValue<T> observable, InvalidationListener listener) {
        if (observable == null || listener == null) {
            throw new NullPointerException();
        }
        observable.getValue2();
        return helper == null ? new SingleInvalidation(observable, listener) : helper.addListener(listener);
    }

    public static <T> ExpressionHelper<T> removeListener(ExpressionHelper<T> helper, InvalidationListener listener) {
        if (listener == null) {
            throw new NullPointerException();
        }
        if (helper == null) {
            return null;
        }
        return helper.removeListener(listener);
    }

    public static <T> ExpressionHelper<T> addListener(ExpressionHelper<T> helper, ObservableValue<T> observable, ChangeListener<? super T> listener) {
        if (observable == null || listener == null) {
            throw new NullPointerException();
        }
        return helper == null ? new SingleChange(observable, listener) : helper.addListener(listener);
    }

    public static <T> ExpressionHelper<T> removeListener(ExpressionHelper<T> helper, ChangeListener<? super T> listener) {
        if (listener == null) {
            throw new NullPointerException();
        }
        if (helper == null) {
            return null;
        }
        return helper.removeListener(listener);
    }

    public static <T> void fireValueChangedEvent(ExpressionHelper<T> helper) {
        if (helper != null) {
            helper.fireValueChangedEvent();
        }
    }

    private ExpressionHelper(ObservableValue<T> observable) {
        this.observable = observable;
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/binding/ExpressionHelper$SingleInvalidation.class */
    private static class SingleInvalidation<T> extends ExpressionHelper<T> {
        private final InvalidationListener listener;

        private SingleInvalidation(ObservableValue<T> expression, InvalidationListener listener) {
            super(expression);
            this.listener = listener;
        }

        @Override // com.sun.javafx.binding.ExpressionHelper
        protected ExpressionHelper<T> addListener(InvalidationListener listener) {
            return new Generic(this.observable, this.listener, listener);
        }

        @Override // com.sun.javafx.binding.ExpressionHelper
        protected ExpressionHelper<T> removeListener(InvalidationListener listener) {
            if (listener.equals(this.listener)) {
                return null;
            }
            return this;
        }

        @Override // com.sun.javafx.binding.ExpressionHelper
        protected ExpressionHelper<T> addListener(ChangeListener<? super T> listener) {
            return new Generic(this.observable, this.listener, listener);
        }

        @Override // com.sun.javafx.binding.ExpressionHelper
        protected ExpressionHelper<T> removeListener(ChangeListener<? super T> listener) {
            return this;
        }

        @Override // com.sun.javafx.binding.ExpressionHelper
        protected void fireValueChangedEvent() {
            try {
                this.listener.invalidated(this.observable);
            } catch (Exception e2) {
                Thread.currentThread().getUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), e2);
            }
        }
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/binding/ExpressionHelper$SingleChange.class */
    private static class SingleChange<T> extends ExpressionHelper<T> {
        private final ChangeListener<? super T> listener;
        private T currentValue;

        private SingleChange(ObservableValue<T> observable, ChangeListener<? super T> listener) {
            super(observable);
            this.listener = listener;
            this.currentValue = observable.getValue2();
        }

        @Override // com.sun.javafx.binding.ExpressionHelper
        protected ExpressionHelper<T> addListener(InvalidationListener listener) {
            return new Generic(this.observable, listener, this.listener);
        }

        @Override // com.sun.javafx.binding.ExpressionHelper
        protected ExpressionHelper<T> removeListener(InvalidationListener listener) {
            return this;
        }

        @Override // com.sun.javafx.binding.ExpressionHelper
        protected ExpressionHelper<T> addListener(ChangeListener<? super T> listener) {
            return new Generic(this.observable, this.listener, listener);
        }

        @Override // com.sun.javafx.binding.ExpressionHelper
        protected ExpressionHelper<T> removeListener(ChangeListener<? super T> listener) {
            if (listener.equals(this.listener)) {
                return null;
            }
            return this;
        }

        @Override // com.sun.javafx.binding.ExpressionHelper
        protected void fireValueChangedEvent() {
            T t2 = this.currentValue;
            this.currentValue = this.observable.getValue2();
            if (this.currentValue == null ? t2 != null : !this.currentValue.equals(t2)) {
                try {
                    this.listener.changed(this.observable, t2, this.currentValue);
                } catch (Exception e2) {
                    Thread.currentThread().getUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), e2);
                }
            }
        }
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/binding/ExpressionHelper$Generic.class */
    private static class Generic<T> extends ExpressionHelper<T> {
        private InvalidationListener[] invalidationListeners;
        private ChangeListener<? super T>[] changeListeners;
        private int invalidationSize;
        private int changeSize;
        private boolean locked;
        private T currentValue;

        private Generic(ObservableValue<T> observable, InvalidationListener listener0, InvalidationListener listener1) {
            super(observable);
            this.invalidationListeners = new InvalidationListener[]{listener0, listener1};
            this.invalidationSize = 2;
        }

        private Generic(ObservableValue<T> observable, ChangeListener<? super T> listener0, ChangeListener<? super T> listener1) {
            super(observable);
            this.changeListeners = new ChangeListener[]{listener0, listener1};
            this.changeSize = 2;
            this.currentValue = observable.getValue2();
        }

        private Generic(ObservableValue<T> observable, InvalidationListener invalidationListener, ChangeListener<? super T> changeListener) {
            super(observable);
            this.invalidationListeners = new InvalidationListener[]{invalidationListener};
            this.invalidationSize = 1;
            this.changeListeners = new ChangeListener[]{changeListener};
            this.changeSize = 1;
            this.currentValue = observable.getValue2();
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.sun.javafx.binding.ExpressionHelper
        public Generic<T> addListener(InvalidationListener listener) {
            if (this.invalidationListeners == null) {
                this.invalidationListeners = new InvalidationListener[]{listener};
                this.invalidationSize = 1;
            } else {
                int oldCapacity = this.invalidationListeners.length;
                if (this.locked) {
                    int newCapacity = this.invalidationSize < oldCapacity ? oldCapacity : ((oldCapacity * 3) / 2) + 1;
                    this.invalidationListeners = (InvalidationListener[]) Arrays.copyOf(this.invalidationListeners, newCapacity);
                } else if (this.invalidationSize == oldCapacity) {
                    this.invalidationSize = trim(this.invalidationSize, this.invalidationListeners);
                    if (this.invalidationSize == oldCapacity) {
                        int newCapacity2 = ((oldCapacity * 3) / 2) + 1;
                        this.invalidationListeners = (InvalidationListener[]) Arrays.copyOf(this.invalidationListeners, newCapacity2);
                    }
                }
                InvalidationListener[] invalidationListenerArr = this.invalidationListeners;
                int i2 = this.invalidationSize;
                this.invalidationSize = i2 + 1;
                invalidationListenerArr[i2] = listener;
            }
            return this;
        }

        @Override // com.sun.javafx.binding.ExpressionHelper
        protected ExpressionHelper<T> removeListener(InvalidationListener listener) {
            if (this.invalidationListeners != null) {
                int index = 0;
                while (true) {
                    if (index >= this.invalidationSize) {
                        break;
                    }
                    if (!listener.equals(this.invalidationListeners[index])) {
                        index++;
                    } else if (this.invalidationSize == 1) {
                        if (this.changeSize == 1) {
                            return new SingleChange(this.observable, this.changeListeners[0]);
                        }
                        this.invalidationListeners = null;
                        this.invalidationSize = 0;
                    } else {
                        if (this.invalidationSize == 2 && this.changeSize == 0) {
                            return new SingleInvalidation(this.observable, this.invalidationListeners[1 - index]);
                        }
                        int numMoved = (this.invalidationSize - index) - 1;
                        InvalidationListener[] oldListeners = this.invalidationListeners;
                        if (this.locked) {
                            this.invalidationListeners = new InvalidationListener[this.invalidationListeners.length];
                            System.arraycopy(oldListeners, 0, this.invalidationListeners, 0, index);
                        }
                        if (numMoved > 0) {
                            System.arraycopy(oldListeners, index + 1, this.invalidationListeners, index, numMoved);
                        }
                        this.invalidationSize--;
                        if (!this.locked) {
                            this.invalidationListeners[this.invalidationSize] = null;
                        }
                    }
                }
            }
            return this;
        }

        @Override // com.sun.javafx.binding.ExpressionHelper
        protected ExpressionHelper<T> addListener(ChangeListener<? super T> listener) {
            if (this.changeListeners == null) {
                this.changeListeners = new ChangeListener[]{listener};
                this.changeSize = 1;
            } else {
                int oldCapacity = this.changeListeners.length;
                if (this.locked) {
                    int newCapacity = this.changeSize < oldCapacity ? oldCapacity : ((oldCapacity * 3) / 2) + 1;
                    this.changeListeners = (ChangeListener[]) Arrays.copyOf(this.changeListeners, newCapacity);
                } else if (this.changeSize == oldCapacity) {
                    this.changeSize = trim(this.changeSize, this.changeListeners);
                    if (this.changeSize == oldCapacity) {
                        int newCapacity2 = ((oldCapacity * 3) / 2) + 1;
                        this.changeListeners = (ChangeListener[]) Arrays.copyOf(this.changeListeners, newCapacity2);
                    }
                }
                ChangeListener<? super T>[] changeListenerArr = this.changeListeners;
                int i2 = this.changeSize;
                this.changeSize = i2 + 1;
                changeListenerArr[i2] = listener;
            }
            if (this.changeSize == 1) {
                this.currentValue = this.observable.getValue2();
            }
            return this;
        }

        @Override // com.sun.javafx.binding.ExpressionHelper
        protected ExpressionHelper<T> removeListener(ChangeListener<? super T> listener) {
            if (this.changeListeners != null) {
                int index = 0;
                while (true) {
                    if (index >= this.changeSize) {
                        break;
                    }
                    if (!listener.equals(this.changeListeners[index])) {
                        index++;
                    } else if (this.changeSize == 1) {
                        if (this.invalidationSize == 1) {
                            return new SingleInvalidation(this.observable, this.invalidationListeners[0]);
                        }
                        this.changeListeners = null;
                        this.changeSize = 0;
                    } else {
                        if (this.changeSize == 2 && this.invalidationSize == 0) {
                            return new SingleChange(this.observable, this.changeListeners[1 - index]);
                        }
                        int numMoved = (this.changeSize - index) - 1;
                        ChangeListener<? super T>[] oldListeners = this.changeListeners;
                        if (this.locked) {
                            this.changeListeners = new ChangeListener[this.changeListeners.length];
                            System.arraycopy(oldListeners, 0, this.changeListeners, 0, index);
                        }
                        if (numMoved > 0) {
                            System.arraycopy(oldListeners, index + 1, this.changeListeners, index, numMoved);
                        }
                        this.changeSize--;
                        if (!this.locked) {
                            this.changeListeners[this.changeSize] = null;
                        }
                    }
                }
            }
            return this;
        }

        @Override // com.sun.javafx.binding.ExpressionHelper
        protected void fireValueChangedEvent() {
            InvalidationListener[] curInvalidationList = this.invalidationListeners;
            int curInvalidationSize = this.invalidationSize;
            ChangeListener<? super T>[] curChangeList = this.changeListeners;
            int curChangeSize = this.changeSize;
            try {
                this.locked = true;
                for (int i2 = 0; i2 < curInvalidationSize; i2++) {
                    try {
                        curInvalidationList[i2].invalidated(this.observable);
                    } catch (Exception e2) {
                        Thread.currentThread().getUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), e2);
                    }
                }
                if (curChangeSize > 0) {
                    T oldValue = this.currentValue;
                    this.currentValue = this.observable.getValue2();
                    boolean changed = this.currentValue == null ? oldValue != null : !this.currentValue.equals(oldValue);
                    if (changed) {
                        for (int i3 = 0; i3 < curChangeSize; i3++) {
                            try {
                                curChangeList[i3].changed(this.observable, oldValue, this.currentValue);
                            } catch (Exception e3) {
                                Thread.currentThread().getUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), e3);
                            }
                        }
                    }
                }
            } finally {
                this.locked = false;
            }
        }
    }
}
