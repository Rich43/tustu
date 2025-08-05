package com.sun.javafx.collections;

import com.sun.javafx.binding.ExpressionHelperBase;
import java.util.Arrays;
import javafx.beans.InvalidationListener;
import javafx.collections.ArrayChangeListener;
import javafx.collections.ObservableArray;

/* loaded from: jfxrt.jar:com/sun/javafx/collections/ArrayListenerHelper.class */
public abstract class ArrayListenerHelper<T extends ObservableArray<T>> extends ExpressionHelperBase {
    protected final T observable;

    protected abstract ArrayListenerHelper addListener(InvalidationListener invalidationListener);

    protected abstract ArrayListenerHelper removeListener(InvalidationListener invalidationListener);

    protected abstract ArrayListenerHelper addListener(ArrayChangeListener<T> arrayChangeListener);

    protected abstract ArrayListenerHelper removeListener(ArrayChangeListener<T> arrayChangeListener);

    protected abstract void fireValueChangedEvent(boolean z2, int i2, int i3);

    public static <T extends ObservableArray<T>> ArrayListenerHelper addListener(ArrayListenerHelper helper, T observable, InvalidationListener listener) {
        if (listener == null) {
            throw new NullPointerException();
        }
        return helper == null ? new SingleInvalidation(observable, listener) : helper.addListener(listener);
    }

    public static ArrayListenerHelper removeListener(ArrayListenerHelper helper, InvalidationListener listener) {
        if (listener == null) {
            throw new NullPointerException();
        }
        if (helper == null) {
            return null;
        }
        return helper.removeListener(listener);
    }

    public static <T extends ObservableArray<T>> ArrayListenerHelper addListener(ArrayListenerHelper helper, T observable, ArrayChangeListener listener) {
        if (listener == null) {
            throw new NullPointerException();
        }
        return helper == null ? new SingleChange(observable, listener) : helper.addListener(listener);
    }

    public static ArrayListenerHelper removeListener(ArrayListenerHelper helper, ArrayChangeListener listener) {
        if (listener == null) {
            throw new NullPointerException();
        }
        if (helper == null) {
            return null;
        }
        return helper.removeListener(listener);
    }

    public static void fireValueChangedEvent(ArrayListenerHelper helper, boolean sizeChanged, int from, int to) {
        if (helper != null) {
            if (from < to || sizeChanged) {
                helper.fireValueChangedEvent(sizeChanged, from, to);
            }
        }
    }

    public static boolean hasListeners(ArrayListenerHelper helper) {
        return helper != null;
    }

    public ArrayListenerHelper(T observable) {
        this.observable = observable;
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/collections/ArrayListenerHelper$SingleInvalidation.class */
    private static class SingleInvalidation<T extends ObservableArray<T>> extends ArrayListenerHelper<T> {
        private final InvalidationListener listener;

        private SingleInvalidation(T observable, InvalidationListener listener) {
            super(observable);
            this.listener = listener;
        }

        @Override // com.sun.javafx.collections.ArrayListenerHelper
        protected ArrayListenerHelper addListener(InvalidationListener listener) {
            return new Generic(this.observable, this.listener, listener);
        }

        @Override // com.sun.javafx.collections.ArrayListenerHelper
        protected ArrayListenerHelper removeListener(InvalidationListener listener) {
            if (listener.equals(this.listener)) {
                return null;
            }
            return this;
        }

        @Override // com.sun.javafx.collections.ArrayListenerHelper
        protected ArrayListenerHelper addListener(ArrayChangeListener listener) {
            return new Generic(this.observable, this.listener, listener);
        }

        @Override // com.sun.javafx.collections.ArrayListenerHelper
        protected ArrayListenerHelper removeListener(ArrayChangeListener listener) {
            return this;
        }

        @Override // com.sun.javafx.collections.ArrayListenerHelper
        protected void fireValueChangedEvent(boolean sizeChanged, int from, int to) {
            try {
                this.listener.invalidated(this.observable);
            } catch (Exception e2) {
                Thread.currentThread().getUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), e2);
            }
        }
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/collections/ArrayListenerHelper$SingleChange.class */
    private static class SingleChange<T extends ObservableArray<T>> extends ArrayListenerHelper<T> {
        private final ArrayChangeListener listener;

        private SingleChange(T observable, ArrayChangeListener listener) {
            super(observable);
            this.listener = listener;
        }

        @Override // com.sun.javafx.collections.ArrayListenerHelper
        protected ArrayListenerHelper addListener(InvalidationListener listener) {
            return new Generic(this.observable, listener, this.listener);
        }

        @Override // com.sun.javafx.collections.ArrayListenerHelper
        protected ArrayListenerHelper removeListener(InvalidationListener listener) {
            return this;
        }

        @Override // com.sun.javafx.collections.ArrayListenerHelper
        protected ArrayListenerHelper addListener(ArrayChangeListener listener) {
            return new Generic(this.observable, this.listener, listener);
        }

        @Override // com.sun.javafx.collections.ArrayListenerHelper
        protected ArrayListenerHelper removeListener(ArrayChangeListener listener) {
            if (listener.equals(this.listener)) {
                return null;
            }
            return this;
        }

        @Override // com.sun.javafx.collections.ArrayListenerHelper
        protected void fireValueChangedEvent(boolean sizeChanged, int from, int to) {
            try {
                this.listener.onChanged(this.observable, sizeChanged, from, to);
            } catch (Exception e2) {
                Thread.currentThread().getUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), e2);
            }
        }
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/collections/ArrayListenerHelper$Generic.class */
    private static class Generic<T extends ObservableArray<T>> extends ArrayListenerHelper<T> {
        private InvalidationListener[] invalidationListeners;
        private ArrayChangeListener[] changeListeners;
        private int invalidationSize;
        private int changeSize;
        private boolean locked;

        private Generic(T observable, InvalidationListener listener0, InvalidationListener listener1) {
            super(observable);
            this.invalidationListeners = new InvalidationListener[]{listener0, listener1};
            this.invalidationSize = 2;
        }

        private Generic(T observable, ArrayChangeListener listener0, ArrayChangeListener listener1) {
            super(observable);
            this.changeListeners = new ArrayChangeListener[]{listener0, listener1};
            this.changeSize = 2;
        }

        private Generic(T observable, InvalidationListener invalidationListener, ArrayChangeListener changeListener) {
            super(observable);
            this.invalidationListeners = new InvalidationListener[]{invalidationListener};
            this.invalidationSize = 1;
            this.changeListeners = new ArrayChangeListener[]{changeListener};
            this.changeSize = 1;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.sun.javafx.collections.ArrayListenerHelper
        public Generic addListener(InvalidationListener listener) {
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

        @Override // com.sun.javafx.collections.ArrayListenerHelper
        protected ArrayListenerHelper removeListener(InvalidationListener listener) {
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
                            System.arraycopy(oldListeners, 0, this.invalidationListeners, 0, index + 1);
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

        @Override // com.sun.javafx.collections.ArrayListenerHelper
        protected ArrayListenerHelper addListener(ArrayChangeListener<T> listener) {
            if (this.changeListeners == null) {
                this.changeListeners = new ArrayChangeListener[]{listener};
                this.changeSize = 1;
            } else {
                int oldCapacity = this.changeListeners.length;
                if (this.locked) {
                    int newCapacity = this.changeSize < oldCapacity ? oldCapacity : ((oldCapacity * 3) / 2) + 1;
                    this.changeListeners = (ArrayChangeListener[]) Arrays.copyOf(this.changeListeners, newCapacity);
                } else if (this.changeSize == oldCapacity) {
                    this.changeSize = trim(this.changeSize, this.changeListeners);
                    if (this.changeSize == oldCapacity) {
                        int newCapacity2 = ((oldCapacity * 3) / 2) + 1;
                        this.changeListeners = (ArrayChangeListener[]) Arrays.copyOf(this.changeListeners, newCapacity2);
                    }
                }
                ArrayChangeListener[] arrayChangeListenerArr = this.changeListeners;
                int i2 = this.changeSize;
                this.changeSize = i2 + 1;
                arrayChangeListenerArr[i2] = listener;
            }
            return this;
        }

        @Override // com.sun.javafx.collections.ArrayListenerHelper
        protected ArrayListenerHelper removeListener(ArrayChangeListener<T> listener) {
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
                        ArrayChangeListener[] oldListeners = this.changeListeners;
                        if (this.locked) {
                            this.changeListeners = new ArrayChangeListener[this.changeListeners.length];
                            System.arraycopy(oldListeners, 0, this.changeListeners, 0, index + 1);
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

        @Override // com.sun.javafx.collections.ArrayListenerHelper
        protected void fireValueChangedEvent(boolean sizeChanged, int from, int to) {
            InvalidationListener[] curInvalidationList = this.invalidationListeners;
            int curInvalidationSize = this.invalidationSize;
            ArrayChangeListener[] curChangeList = this.changeListeners;
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
                for (int i3 = 0; i3 < curChangeSize; i3++) {
                    try {
                        curChangeList[i3].onChanged(this.observable, sizeChanged, from, to);
                    } catch (Exception e3) {
                        Thread.currentThread().getUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), e3);
                    }
                }
            } finally {
                this.locked = false;
            }
        }
    }
}
