package com.sun.javafx.binding;

import java.util.Arrays;
import java.util.Iterator;
import javafx.beans.InvalidationListener;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableSetValue;
import javafx.collections.ObservableSet;
import javafx.collections.SetChangeListener;

/* loaded from: jfxrt.jar:com/sun/javafx/binding/SetExpressionHelper.class */
public abstract class SetExpressionHelper<E> extends ExpressionHelperBase {
    protected final ObservableSetValue<E> observable;

    protected abstract SetExpressionHelper<E> addListener(InvalidationListener invalidationListener);

    protected abstract SetExpressionHelper<E> removeListener(InvalidationListener invalidationListener);

    protected abstract SetExpressionHelper<E> addListener(ChangeListener<? super ObservableSet<E>> changeListener);

    protected abstract SetExpressionHelper<E> removeListener(ChangeListener<? super ObservableSet<E>> changeListener);

    protected abstract SetExpressionHelper<E> addListener(SetChangeListener<? super E> setChangeListener);

    protected abstract SetExpressionHelper<E> removeListener(SetChangeListener<? super E> setChangeListener);

    protected abstract void fireValueChangedEvent();

    protected abstract void fireValueChangedEvent(SetChangeListener.Change<? extends E> change);

    public static <E> SetExpressionHelper<E> addListener(SetExpressionHelper<E> helper, ObservableSetValue<E> observable, InvalidationListener listener) {
        if (observable == null || listener == null) {
            throw new NullPointerException();
        }
        observable.getValue2();
        return helper == null ? new SingleInvalidation(observable, listener) : helper.addListener(listener);
    }

    public static <E> SetExpressionHelper<E> removeListener(SetExpressionHelper<E> helper, InvalidationListener listener) {
        if (listener == null) {
            throw new NullPointerException();
        }
        if (helper == null) {
            return null;
        }
        return helper.removeListener(listener);
    }

    public static <E> SetExpressionHelper<E> addListener(SetExpressionHelper<E> helper, ObservableSetValue<E> observable, ChangeListener<? super ObservableSet<E>> listener) {
        if (observable == null || listener == null) {
            throw new NullPointerException();
        }
        return helper == null ? new SingleChange(observable, listener) : helper.addListener(listener);
    }

    public static <E> SetExpressionHelper<E> removeListener(SetExpressionHelper<E> helper, ChangeListener<? super ObservableSet<E>> listener) {
        if (listener == null) {
            throw new NullPointerException();
        }
        if (helper == null) {
            return null;
        }
        return helper.removeListener(listener);
    }

    public static <E> SetExpressionHelper<E> addListener(SetExpressionHelper<E> helper, ObservableSetValue<E> observable, SetChangeListener<? super E> listener) {
        if (observable == null || listener == null) {
            throw new NullPointerException();
        }
        return helper == null ? new SingleSetChange(observable, listener) : helper.addListener(listener);
    }

    public static <E> SetExpressionHelper<E> removeListener(SetExpressionHelper<E> helper, SetChangeListener<? super E> listener) {
        if (listener == null) {
            throw new NullPointerException();
        }
        if (helper == null) {
            return null;
        }
        return helper.removeListener(listener);
    }

    public static <E> void fireValueChangedEvent(SetExpressionHelper<E> helper) {
        if (helper != null) {
            helper.fireValueChangedEvent();
        }
    }

    public static <E> void fireValueChangedEvent(SetExpressionHelper<E> helper, SetChangeListener.Change<? extends E> change) {
        if (helper != null) {
            helper.fireValueChangedEvent(change);
        }
    }

    protected SetExpressionHelper(ObservableSetValue<E> observable) {
        this.observable = observable;
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/binding/SetExpressionHelper$SingleInvalidation.class */
    private static class SingleInvalidation<E> extends SetExpressionHelper<E> {
        private final InvalidationListener listener;

        private SingleInvalidation(ObservableSetValue<E> observable, InvalidationListener listener) {
            super(observable);
            this.listener = listener;
        }

        @Override // com.sun.javafx.binding.SetExpressionHelper
        protected SetExpressionHelper<E> addListener(InvalidationListener listener) {
            return new Generic(this.observable, this.listener, listener);
        }

        @Override // com.sun.javafx.binding.SetExpressionHelper
        protected SetExpressionHelper<E> removeListener(InvalidationListener listener) {
            if (listener.equals(this.listener)) {
                return null;
            }
            return this;
        }

        @Override // com.sun.javafx.binding.SetExpressionHelper
        protected SetExpressionHelper<E> addListener(ChangeListener<? super ObservableSet<E>> listener) {
            return new Generic(this.observable, this.listener, listener);
        }

        @Override // com.sun.javafx.binding.SetExpressionHelper
        protected SetExpressionHelper<E> removeListener(ChangeListener<? super ObservableSet<E>> listener) {
            return this;
        }

        @Override // com.sun.javafx.binding.SetExpressionHelper
        protected SetExpressionHelper<E> addListener(SetChangeListener<? super E> listener) {
            return new Generic(this.observable, this.listener, listener);
        }

        @Override // com.sun.javafx.binding.SetExpressionHelper
        protected SetExpressionHelper<E> removeListener(SetChangeListener<? super E> listener) {
            return this;
        }

        @Override // com.sun.javafx.binding.SetExpressionHelper
        protected void fireValueChangedEvent() {
            this.listener.invalidated(this.observable);
        }

        @Override // com.sun.javafx.binding.SetExpressionHelper
        protected void fireValueChangedEvent(SetChangeListener.Change<? extends E> change) {
            this.listener.invalidated(this.observable);
        }
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/binding/SetExpressionHelper$SingleChange.class */
    private static class SingleChange<E> extends SetExpressionHelper<E> {
        private final ChangeListener<? super ObservableSet<E>> listener;
        private ObservableSet<E> currentValue;

        private SingleChange(ObservableSetValue<E> observable, ChangeListener<? super ObservableSet<E>> listener) {
            super(observable);
            this.listener = listener;
            this.currentValue = observable.getValue2();
        }

        @Override // com.sun.javafx.binding.SetExpressionHelper
        protected SetExpressionHelper<E> addListener(InvalidationListener listener) {
            return new Generic(this.observable, listener, this.listener);
        }

        @Override // com.sun.javafx.binding.SetExpressionHelper
        protected SetExpressionHelper<E> removeListener(InvalidationListener listener) {
            return this;
        }

        @Override // com.sun.javafx.binding.SetExpressionHelper
        protected SetExpressionHelper<E> addListener(ChangeListener<? super ObservableSet<E>> listener) {
            return new Generic(this.observable, this.listener, listener);
        }

        @Override // com.sun.javafx.binding.SetExpressionHelper
        protected SetExpressionHelper<E> removeListener(ChangeListener<? super ObservableSet<E>> listener) {
            if (listener.equals(this.listener)) {
                return null;
            }
            return this;
        }

        @Override // com.sun.javafx.binding.SetExpressionHelper
        protected SetExpressionHelper<E> addListener(SetChangeListener<? super E> listener) {
            return new Generic(this.observable, this.listener, listener);
        }

        @Override // com.sun.javafx.binding.SetExpressionHelper
        protected SetExpressionHelper<E> removeListener(SetChangeListener<? super E> listener) {
            return this;
        }

        @Override // com.sun.javafx.binding.SetExpressionHelper
        protected void fireValueChangedEvent() {
            ObservableSet<E> oldValue = this.currentValue;
            this.currentValue = this.observable.getValue2();
            if (this.currentValue != oldValue) {
                this.listener.changed(this.observable, oldValue, this.currentValue);
            }
        }

        @Override // com.sun.javafx.binding.SetExpressionHelper
        protected void fireValueChangedEvent(SetChangeListener.Change<? extends E> change) {
            this.listener.changed(this.observable, this.currentValue, this.currentValue);
        }
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/binding/SetExpressionHelper$SingleSetChange.class */
    private static class SingleSetChange<E> extends SetExpressionHelper<E> {
        private final SetChangeListener<? super E> listener;
        private ObservableSet<E> currentValue;

        private SingleSetChange(ObservableSetValue<E> observable, SetChangeListener<? super E> listener) {
            super(observable);
            this.listener = listener;
            this.currentValue = observable.getValue2();
        }

        @Override // com.sun.javafx.binding.SetExpressionHelper
        protected SetExpressionHelper<E> addListener(InvalidationListener listener) {
            return new Generic(this.observable, listener, this.listener);
        }

        @Override // com.sun.javafx.binding.SetExpressionHelper
        protected SetExpressionHelper<E> removeListener(InvalidationListener listener) {
            return this;
        }

        @Override // com.sun.javafx.binding.SetExpressionHelper
        protected SetExpressionHelper<E> addListener(ChangeListener<? super ObservableSet<E>> listener) {
            return new Generic(this.observable, listener, this.listener);
        }

        @Override // com.sun.javafx.binding.SetExpressionHelper
        protected SetExpressionHelper<E> removeListener(ChangeListener<? super ObservableSet<E>> listener) {
            return this;
        }

        @Override // com.sun.javafx.binding.SetExpressionHelper
        protected SetExpressionHelper<E> addListener(SetChangeListener<? super E> listener) {
            return new Generic(this.observable, this.listener, listener);
        }

        @Override // com.sun.javafx.binding.SetExpressionHelper
        protected SetExpressionHelper<E> removeListener(SetChangeListener<? super E> listener) {
            if (listener.equals(this.listener)) {
                return null;
            }
            return this;
        }

        @Override // com.sun.javafx.binding.SetExpressionHelper
        protected void fireValueChangedEvent() {
            ObservableSet<E> oldValue = this.currentValue;
            this.currentValue = this.observable.getValue2();
            if (this.currentValue != oldValue) {
                SimpleChange<E> change = new SimpleChange<>(this.observable);
                if (this.currentValue == null) {
                    Iterator<E> it = oldValue.iterator();
                    while (it.hasNext()) {
                        this.listener.onChanged(change.setRemoved(it.next()));
                    }
                    return;
                }
                if (oldValue == null) {
                    Iterator<E> it2 = this.currentValue.iterator();
                    while (it2.hasNext()) {
                        this.listener.onChanged(change.setAdded(it2.next()));
                    }
                    return;
                }
                for (E element : oldValue) {
                    if (!this.currentValue.contains(element)) {
                        this.listener.onChanged(change.setRemoved(element));
                    }
                }
                for (E element2 : this.currentValue) {
                    if (!oldValue.contains(element2)) {
                        this.listener.onChanged(change.setAdded(element2));
                    }
                }
            }
        }

        @Override // com.sun.javafx.binding.SetExpressionHelper
        protected void fireValueChangedEvent(SetChangeListener.Change<? extends E> change) {
            this.listener.onChanged(new SimpleChange(this.observable, change));
        }
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/binding/SetExpressionHelper$Generic.class */
    private static class Generic<E> extends SetExpressionHelper<E> {
        private InvalidationListener[] invalidationListeners;
        private ChangeListener<? super ObservableSet<E>>[] changeListeners;
        private SetChangeListener<? super E>[] setChangeListeners;
        private int invalidationSize;
        private int changeSize;
        private int setChangeSize;
        private boolean locked;
        private ObservableSet<E> currentValue;

        private Generic(ObservableSetValue<E> observable, InvalidationListener listener0, InvalidationListener listener1) {
            super(observable);
            this.invalidationListeners = new InvalidationListener[]{listener0, listener1};
            this.invalidationSize = 2;
        }

        private Generic(ObservableSetValue<E> observable, ChangeListener<? super ObservableSet<E>> listener0, ChangeListener<? super ObservableSet<E>> listener1) {
            super(observable);
            this.changeListeners = new ChangeListener[]{listener0, listener1};
            this.changeSize = 2;
            this.currentValue = observable.getValue2();
        }

        private Generic(ObservableSetValue<E> observable, SetChangeListener<? super E> listener0, SetChangeListener<? super E> listener1) {
            super(observable);
            this.setChangeListeners = new SetChangeListener[]{listener0, listener1};
            this.setChangeSize = 2;
            this.currentValue = observable.getValue2();
        }

        private Generic(ObservableSetValue<E> observable, InvalidationListener invalidationListener, ChangeListener<? super ObservableSet<E>> changeListener) {
            super(observable);
            this.invalidationListeners = new InvalidationListener[]{invalidationListener};
            this.invalidationSize = 1;
            this.changeListeners = new ChangeListener[]{changeListener};
            this.changeSize = 1;
            this.currentValue = observable.getValue2();
        }

        private Generic(ObservableSetValue<E> observable, InvalidationListener invalidationListener, SetChangeListener<? super E> listChangeListener) {
            super(observable);
            this.invalidationListeners = new InvalidationListener[]{invalidationListener};
            this.invalidationSize = 1;
            this.setChangeListeners = new SetChangeListener[]{listChangeListener};
            this.setChangeSize = 1;
            this.currentValue = observable.getValue2();
        }

        private Generic(ObservableSetValue<E> observable, ChangeListener<? super ObservableSet<E>> changeListener, SetChangeListener<? super E> listChangeListener) {
            super(observable);
            this.changeListeners = new ChangeListener[]{changeListener};
            this.changeSize = 1;
            this.setChangeListeners = new SetChangeListener[]{listChangeListener};
            this.setChangeSize = 1;
            this.currentValue = observable.getValue2();
        }

        @Override // com.sun.javafx.binding.SetExpressionHelper
        protected SetExpressionHelper<E> addListener(InvalidationListener listener) {
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

        @Override // com.sun.javafx.binding.SetExpressionHelper
        protected SetExpressionHelper<E> removeListener(InvalidationListener listener) {
            if (this.invalidationListeners != null) {
                int index = 0;
                while (true) {
                    if (index >= this.invalidationSize) {
                        break;
                    }
                    if (!listener.equals(this.invalidationListeners[index])) {
                        index++;
                    } else if (this.invalidationSize == 1) {
                        if (this.changeSize == 1 && this.setChangeSize == 0) {
                            return new SingleChange(this.observable, this.changeListeners[0]);
                        }
                        if (this.changeSize == 0 && this.setChangeSize == 1) {
                            return new SingleSetChange(this.observable, this.setChangeListeners[0]);
                        }
                        this.invalidationListeners = null;
                        this.invalidationSize = 0;
                    } else {
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
                            InvalidationListener[] invalidationListenerArr = this.invalidationListeners;
                            int i2 = this.invalidationSize - 1;
                            this.invalidationSize = i2;
                            invalidationListenerArr[i2] = null;
                        }
                    }
                }
            }
            return this;
        }

        @Override // com.sun.javafx.binding.SetExpressionHelper
        protected SetExpressionHelper<E> addListener(ChangeListener<? super ObservableSet<E>> listener) {
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
                ChangeListener<? super ObservableSet<E>>[] changeListenerArr = this.changeListeners;
                int i2 = this.changeSize;
                this.changeSize = i2 + 1;
                changeListenerArr[i2] = listener;
            }
            if (this.changeSize == 1) {
                this.currentValue = this.observable.getValue2();
            }
            return this;
        }

        @Override // com.sun.javafx.binding.SetExpressionHelper
        protected SetExpressionHelper<E> removeListener(ChangeListener<? super ObservableSet<E>> listener) {
            if (this.changeListeners != null) {
                int index = 0;
                while (true) {
                    if (index >= this.changeSize) {
                        break;
                    }
                    if (!listener.equals(this.changeListeners[index])) {
                        index++;
                    } else if (this.changeSize == 1) {
                        if (this.invalidationSize == 1 && this.setChangeSize == 0) {
                            return new SingleInvalidation(this.observable, this.invalidationListeners[0]);
                        }
                        if (this.invalidationSize == 0 && this.setChangeSize == 1) {
                            return new SingleSetChange(this.observable, this.setChangeListeners[0]);
                        }
                        this.changeListeners = null;
                        this.changeSize = 0;
                    } else {
                        int numMoved = (this.changeSize - index) - 1;
                        ChangeListener<? super ObservableSet<E>>[] oldListeners = this.changeListeners;
                        if (this.locked) {
                            this.changeListeners = new ChangeListener[this.changeListeners.length];
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

        @Override // com.sun.javafx.binding.SetExpressionHelper
        protected SetExpressionHelper<E> addListener(SetChangeListener<? super E> listener) {
            if (this.setChangeListeners == null) {
                this.setChangeListeners = new SetChangeListener[]{listener};
                this.setChangeSize = 1;
            } else {
                int oldCapacity = this.setChangeListeners.length;
                if (this.locked) {
                    int newCapacity = this.setChangeSize < oldCapacity ? oldCapacity : ((oldCapacity * 3) / 2) + 1;
                    this.setChangeListeners = (SetChangeListener[]) Arrays.copyOf(this.setChangeListeners, newCapacity);
                } else if (this.setChangeSize == oldCapacity) {
                    this.setChangeSize = trim(this.setChangeSize, this.setChangeListeners);
                    if (this.setChangeSize == oldCapacity) {
                        int newCapacity2 = ((oldCapacity * 3) / 2) + 1;
                        this.setChangeListeners = (SetChangeListener[]) Arrays.copyOf(this.setChangeListeners, newCapacity2);
                    }
                }
                SetChangeListener<? super E>[] setChangeListenerArr = this.setChangeListeners;
                int i2 = this.setChangeSize;
                this.setChangeSize = i2 + 1;
                setChangeListenerArr[i2] = listener;
            }
            if (this.setChangeSize == 1) {
                this.currentValue = this.observable.getValue2();
            }
            return this;
        }

        @Override // com.sun.javafx.binding.SetExpressionHelper
        protected SetExpressionHelper<E> removeListener(SetChangeListener<? super E> listener) {
            if (this.setChangeListeners != null) {
                int index = 0;
                while (true) {
                    if (index >= this.setChangeSize) {
                        break;
                    }
                    if (!listener.equals(this.setChangeListeners[index])) {
                        index++;
                    } else if (this.setChangeSize == 1) {
                        if (this.invalidationSize == 1 && this.changeSize == 0) {
                            return new SingleInvalidation(this.observable, this.invalidationListeners[0]);
                        }
                        if (this.invalidationSize == 0 && this.changeSize == 1) {
                            return new SingleChange(this.observable, this.changeListeners[0]);
                        }
                        this.setChangeListeners = null;
                        this.setChangeSize = 0;
                    } else {
                        int numMoved = (this.setChangeSize - index) - 1;
                        SetChangeListener<? super E>[] oldListeners = this.setChangeListeners;
                        if (this.locked) {
                            this.setChangeListeners = new SetChangeListener[this.setChangeListeners.length];
                            System.arraycopy(oldListeners, 0, this.setChangeListeners, 0, index + 1);
                        }
                        if (numMoved > 0) {
                            System.arraycopy(oldListeners, index + 1, this.setChangeListeners, index, numMoved);
                        }
                        this.setChangeSize--;
                        if (!this.locked) {
                            this.setChangeListeners[this.setChangeSize] = null;
                        }
                    }
                }
            }
            return this;
        }

        @Override // com.sun.javafx.binding.SetExpressionHelper
        protected void fireValueChangedEvent() {
            if (this.changeSize == 0 && this.setChangeSize == 0) {
                notifyListeners(this.currentValue, null);
                return;
            }
            ObservableSet<E> oldValue = this.currentValue;
            this.currentValue = this.observable.getValue2();
            notifyListeners(oldValue, null);
        }

        @Override // com.sun.javafx.binding.SetExpressionHelper
        protected void fireValueChangedEvent(SetChangeListener.Change<? extends E> change) {
            SimpleChange<E> mappedChange = this.setChangeSize == 0 ? null : new SimpleChange<>(this.observable, change);
            notifyListeners(this.currentValue, mappedChange);
        }

        private void notifyListeners(ObservableSet<E> observableSet, SimpleChange<E> simpleChange) {
            InvalidationListener[] invalidationListenerArr = this.invalidationListeners;
            int i2 = this.invalidationSize;
            ChangeListener<? super ObservableSet<E>>[] changeListenerArr = this.changeListeners;
            int i3 = this.changeSize;
            SetChangeListener<? super E>[] setChangeListenerArr = this.setChangeListeners;
            int i4 = this.setChangeSize;
            try {
                this.locked = true;
                for (int i5 = 0; i5 < i2; i5++) {
                    invalidationListenerArr[i5].invalidated(this.observable);
                }
                if (this.currentValue != observableSet || simpleChange != null) {
                    for (int i6 = 0; i6 < i3; i6++) {
                        changeListenerArr[i6].changed(this.observable, observableSet, this.currentValue);
                    }
                    if (i4 > 0) {
                        if (simpleChange != null) {
                            for (int i7 = 0; i7 < i4; i7++) {
                                setChangeListenerArr[i7].onChanged(simpleChange);
                            }
                        } else {
                            SimpleChange simpleChange2 = new SimpleChange(this.observable);
                            if (this.currentValue == null) {
                                Iterator<E> it = observableSet.iterator();
                                while (it.hasNext()) {
                                    simpleChange2.setRemoved(it.next());
                                    for (int i8 = 0; i8 < i4; i8++) {
                                        setChangeListenerArr[i8].onChanged(simpleChange2);
                                    }
                                }
                            } else if (observableSet == null) {
                                Iterator<E> it2 = this.currentValue.iterator();
                                while (it2.hasNext()) {
                                    simpleChange2.setAdded(it2.next());
                                    for (int i9 = 0; i9 < i4; i9++) {
                                        setChangeListenerArr[i9].onChanged(simpleChange2);
                                    }
                                }
                            } else {
                                for (E e2 : observableSet) {
                                    if (!this.currentValue.contains(e2)) {
                                        simpleChange2.setRemoved(e2);
                                        for (int i10 = 0; i10 < i4; i10++) {
                                            setChangeListenerArr[i10].onChanged(simpleChange2);
                                        }
                                    }
                                }
                                for (E e3 : this.currentValue) {
                                    if (!observableSet.contains(e3)) {
                                        simpleChange2.setAdded(e3);
                                        for (int i11 = 0; i11 < i4; i11++) {
                                            setChangeListenerArr[i11].onChanged(simpleChange2);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            } finally {
                this.locked = false;
            }
        }
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/binding/SetExpressionHelper$SimpleChange.class */
    public static class SimpleChange<E> extends SetChangeListener.Change<E> {
        private E old;
        private E added;
        private boolean addOp;

        public SimpleChange(ObservableSet<E> set) {
            super(set);
        }

        public SimpleChange(ObservableSet<E> set, SetChangeListener.Change<? extends E> source) {
            super(set);
            this.old = source.getElementRemoved();
            this.added = source.getElementAdded();
            this.addOp = source.wasAdded();
        }

        public SimpleChange<E> setRemoved(E old) {
            this.old = old;
            this.added = null;
            this.addOp = false;
            return this;
        }

        public SimpleChange<E> setAdded(E added) {
            this.old = null;
            this.added = added;
            this.addOp = true;
            return this;
        }

        @Override // javafx.collections.SetChangeListener.Change
        public boolean wasAdded() {
            return this.addOp;
        }

        @Override // javafx.collections.SetChangeListener.Change
        public boolean wasRemoved() {
            return !this.addOp;
        }

        @Override // javafx.collections.SetChangeListener.Change
        public E getElementAdded() {
            return this.added;
        }

        @Override // javafx.collections.SetChangeListener.Change
        public E getElementRemoved() {
            return this.old;
        }

        public String toString() {
            return this.addOp ? "added " + ((Object) this.added) : "removed " + ((Object) this.old);
        }
    }
}
