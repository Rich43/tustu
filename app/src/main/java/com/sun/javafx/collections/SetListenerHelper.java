package com.sun.javafx.collections;

import com.sun.javafx.binding.ExpressionHelperBase;
import java.util.Arrays;
import javafx.beans.InvalidationListener;
import javafx.collections.SetChangeListener;

/* loaded from: jfxrt.jar:com/sun/javafx/collections/SetListenerHelper.class */
public abstract class SetListenerHelper<E> extends ExpressionHelperBase {
    protected abstract SetListenerHelper<E> addListener(InvalidationListener invalidationListener);

    protected abstract SetListenerHelper<E> removeListener(InvalidationListener invalidationListener);

    protected abstract SetListenerHelper<E> addListener(SetChangeListener<? super E> setChangeListener);

    protected abstract SetListenerHelper<E> removeListener(SetChangeListener<? super E> setChangeListener);

    protected abstract void fireValueChangedEvent(SetChangeListener.Change<? extends E> change);

    public static <E> SetListenerHelper<E> addListener(SetListenerHelper<E> helper, InvalidationListener listener) {
        if (listener == null) {
            throw new NullPointerException();
        }
        return helper == null ? new SingleInvalidation(listener) : helper.addListener(listener);
    }

    public static <E> SetListenerHelper<E> removeListener(SetListenerHelper<E> helper, InvalidationListener listener) {
        if (listener == null) {
            throw new NullPointerException();
        }
        if (helper == null) {
            return null;
        }
        return helper.removeListener(listener);
    }

    public static <E> SetListenerHelper<E> addListener(SetListenerHelper<E> helper, SetChangeListener<? super E> listener) {
        if (listener == null) {
            throw new NullPointerException();
        }
        return helper == null ? new SingleChange(listener) : helper.addListener(listener);
    }

    public static <E> SetListenerHelper<E> removeListener(SetListenerHelper<E> helper, SetChangeListener<? super E> listener) {
        if (listener == null) {
            throw new NullPointerException();
        }
        if (helper == null) {
            return null;
        }
        return helper.removeListener(listener);
    }

    public static <E> void fireValueChangedEvent(SetListenerHelper<E> helper, SetChangeListener.Change<? extends E> change) {
        if (helper != null) {
            helper.fireValueChangedEvent(change);
        }
    }

    public static <E> boolean hasListeners(SetListenerHelper<E> helper) {
        return helper != null;
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/collections/SetListenerHelper$SingleInvalidation.class */
    private static class SingleInvalidation<E> extends SetListenerHelper<E> {
        private final InvalidationListener listener;

        private SingleInvalidation(InvalidationListener listener) {
            this.listener = listener;
        }

        @Override // com.sun.javafx.collections.SetListenerHelper
        protected SetListenerHelper<E> addListener(InvalidationListener listener) {
            return new Generic(this.listener, listener);
        }

        @Override // com.sun.javafx.collections.SetListenerHelper
        protected SetListenerHelper<E> removeListener(InvalidationListener listener) {
            if (listener.equals(this.listener)) {
                return null;
            }
            return this;
        }

        @Override // com.sun.javafx.collections.SetListenerHelper
        protected SetListenerHelper<E> addListener(SetChangeListener<? super E> listener) {
            return new Generic(this.listener, listener);
        }

        @Override // com.sun.javafx.collections.SetListenerHelper
        protected SetListenerHelper<E> removeListener(SetChangeListener<? super E> listener) {
            return this;
        }

        @Override // com.sun.javafx.collections.SetListenerHelper
        protected void fireValueChangedEvent(SetChangeListener.Change<? extends E> change) {
            try {
                this.listener.invalidated(change.getSet());
            } catch (Exception e2) {
                Thread.currentThread().getUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), e2);
            }
        }
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/collections/SetListenerHelper$SingleChange.class */
    private static class SingleChange<E> extends SetListenerHelper<E> {
        private final SetChangeListener<? super E> listener;

        private SingleChange(SetChangeListener<? super E> listener) {
            this.listener = listener;
        }

        @Override // com.sun.javafx.collections.SetListenerHelper
        protected SetListenerHelper<E> addListener(InvalidationListener listener) {
            return new Generic(listener, this.listener);
        }

        @Override // com.sun.javafx.collections.SetListenerHelper
        protected SetListenerHelper<E> removeListener(InvalidationListener listener) {
            return this;
        }

        @Override // com.sun.javafx.collections.SetListenerHelper
        protected SetListenerHelper<E> addListener(SetChangeListener<? super E> listener) {
            return new Generic(this.listener, listener);
        }

        @Override // com.sun.javafx.collections.SetListenerHelper
        protected SetListenerHelper<E> removeListener(SetChangeListener<? super E> listener) {
            if (listener.equals(this.listener)) {
                return null;
            }
            return this;
        }

        @Override // com.sun.javafx.collections.SetListenerHelper
        protected void fireValueChangedEvent(SetChangeListener.Change<? extends E> change) {
            try {
                this.listener.onChanged(change);
            } catch (Exception e2) {
                Thread.currentThread().getUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), e2);
            }
        }
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/collections/SetListenerHelper$Generic.class */
    private static class Generic<E> extends SetListenerHelper<E> {
        private InvalidationListener[] invalidationListeners;
        private SetChangeListener<? super E>[] changeListeners;
        private int invalidationSize;
        private int changeSize;
        private boolean locked;

        private Generic(InvalidationListener listener0, InvalidationListener listener1) {
            this.invalidationListeners = new InvalidationListener[]{listener0, listener1};
            this.invalidationSize = 2;
        }

        private Generic(SetChangeListener<? super E> listener0, SetChangeListener<? super E> listener1) {
            this.changeListeners = new SetChangeListener[]{listener0, listener1};
            this.changeSize = 2;
        }

        private Generic(InvalidationListener invalidationListener, SetChangeListener<? super E> changeListener) {
            this.invalidationListeners = new InvalidationListener[]{invalidationListener};
            this.invalidationSize = 1;
            this.changeListeners = new SetChangeListener[]{changeListener};
            this.changeSize = 1;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.sun.javafx.collections.SetListenerHelper
        public Generic<E> addListener(InvalidationListener listener) {
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

        @Override // com.sun.javafx.collections.SetListenerHelper
        protected SetListenerHelper<E> removeListener(InvalidationListener listener) {
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
                            return new SingleChange(this.changeListeners[0]);
                        }
                        this.invalidationListeners = null;
                        this.invalidationSize = 0;
                    } else {
                        if (this.invalidationSize == 2 && this.changeSize == 0) {
                            return new SingleInvalidation(this.invalidationListeners[1 - index]);
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

        @Override // com.sun.javafx.collections.SetListenerHelper
        protected SetListenerHelper<E> addListener(SetChangeListener<? super E> listener) {
            if (this.changeListeners == null) {
                this.changeListeners = new SetChangeListener[]{listener};
                this.changeSize = 1;
            } else {
                int oldCapacity = this.changeListeners.length;
                if (this.locked) {
                    int newCapacity = this.changeSize < oldCapacity ? oldCapacity : ((oldCapacity * 3) / 2) + 1;
                    this.changeListeners = (SetChangeListener[]) Arrays.copyOf(this.changeListeners, newCapacity);
                } else if (this.changeSize == oldCapacity) {
                    this.changeSize = trim(this.changeSize, this.changeListeners);
                    if (this.changeSize == oldCapacity) {
                        int newCapacity2 = ((oldCapacity * 3) / 2) + 1;
                        this.changeListeners = (SetChangeListener[]) Arrays.copyOf(this.changeListeners, newCapacity2);
                    }
                }
                SetChangeListener<? super E>[] setChangeListenerArr = this.changeListeners;
                int i2 = this.changeSize;
                this.changeSize = i2 + 1;
                setChangeListenerArr[i2] = listener;
            }
            return this;
        }

        @Override // com.sun.javafx.collections.SetListenerHelper
        protected SetListenerHelper<E> removeListener(SetChangeListener<? super E> listener) {
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
                            return new SingleInvalidation(this.invalidationListeners[0]);
                        }
                        this.changeListeners = null;
                        this.changeSize = 0;
                    } else {
                        if (this.changeSize == 2 && this.invalidationSize == 0) {
                            return new SingleChange(this.changeListeners[1 - index]);
                        }
                        int numMoved = (this.changeSize - index) - 1;
                        SetChangeListener<? super E>[] oldListeners = this.changeListeners;
                        if (this.locked) {
                            this.changeListeners = new SetChangeListener[this.changeListeners.length];
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

        @Override // com.sun.javafx.collections.SetListenerHelper
        protected void fireValueChangedEvent(SetChangeListener.Change<? extends E> change) {
            InvalidationListener[] curInvalidationList = this.invalidationListeners;
            int curInvalidationSize = this.invalidationSize;
            SetChangeListener<? super E>[] curChangeList = this.changeListeners;
            int curChangeSize = this.changeSize;
            try {
                this.locked = true;
                for (int i2 = 0; i2 < curInvalidationSize; i2++) {
                    try {
                        curInvalidationList[i2].invalidated(change.getSet());
                    } catch (Exception e2) {
                        Thread.currentThread().getUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), e2);
                    }
                }
                for (int i3 = 0; i3 < curChangeSize; i3++) {
                    try {
                        curChangeList[i3].onChanged(change);
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
