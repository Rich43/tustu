package com.sun.javafx.binding;

import java.util.Arrays;
import java.util.Map;
import javafx.beans.InvalidationListener;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableMapValue;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableMap;

/* loaded from: jfxrt.jar:com/sun/javafx/binding/MapExpressionHelper.class */
public abstract class MapExpressionHelper<K, V> extends ExpressionHelperBase {
    protected final ObservableMapValue<K, V> observable;

    protected abstract MapExpressionHelper<K, V> addListener(InvalidationListener invalidationListener);

    protected abstract MapExpressionHelper<K, V> removeListener(InvalidationListener invalidationListener);

    protected abstract MapExpressionHelper<K, V> addListener(ChangeListener<? super ObservableMap<K, V>> changeListener);

    protected abstract MapExpressionHelper<K, V> removeListener(ChangeListener<? super ObservableMap<K, V>> changeListener);

    protected abstract MapExpressionHelper<K, V> addListener(MapChangeListener<? super K, ? super V> mapChangeListener);

    protected abstract MapExpressionHelper<K, V> removeListener(MapChangeListener<? super K, ? super V> mapChangeListener);

    protected abstract void fireValueChangedEvent();

    protected abstract void fireValueChangedEvent(MapChangeListener.Change<? extends K, ? extends V> change);

    public static <K, V> MapExpressionHelper<K, V> addListener(MapExpressionHelper<K, V> helper, ObservableMapValue<K, V> observable, InvalidationListener listener) {
        if (observable == null || listener == null) {
            throw new NullPointerException();
        }
        observable.getValue2();
        return helper == null ? new SingleInvalidation(observable, listener) : helper.addListener(listener);
    }

    public static <K, V> MapExpressionHelper<K, V> removeListener(MapExpressionHelper<K, V> helper, InvalidationListener listener) {
        if (listener == null) {
            throw new NullPointerException();
        }
        if (helper == null) {
            return null;
        }
        return helper.removeListener(listener);
    }

    public static <K, V> MapExpressionHelper<K, V> addListener(MapExpressionHelper<K, V> helper, ObservableMapValue<K, V> observable, ChangeListener<? super ObservableMap<K, V>> listener) {
        if (observable == null || listener == null) {
            throw new NullPointerException();
        }
        return helper == null ? new SingleChange(observable, listener) : helper.addListener(listener);
    }

    public static <K, V> MapExpressionHelper<K, V> removeListener(MapExpressionHelper<K, V> helper, ChangeListener<? super ObservableMap<K, V>> listener) {
        if (listener == null) {
            throw new NullPointerException();
        }
        if (helper == null) {
            return null;
        }
        return helper.removeListener(listener);
    }

    public static <K, V> MapExpressionHelper<K, V> addListener(MapExpressionHelper<K, V> helper, ObservableMapValue<K, V> observable, MapChangeListener<? super K, ? super V> listener) {
        if (observable == null || listener == null) {
            throw new NullPointerException();
        }
        return helper == null ? new SingleMapChange(observable, listener) : helper.addListener(listener);
    }

    public static <K, V> MapExpressionHelper<K, V> removeListener(MapExpressionHelper<K, V> helper, MapChangeListener<? super K, ? super V> listener) {
        if (listener == null) {
            throw new NullPointerException();
        }
        if (helper == null) {
            return null;
        }
        return helper.removeListener(listener);
    }

    public static <K, V> void fireValueChangedEvent(MapExpressionHelper<K, V> helper) {
        if (helper != null) {
            helper.fireValueChangedEvent();
        }
    }

    public static <K, V> void fireValueChangedEvent(MapExpressionHelper<K, V> helper, MapChangeListener.Change<? extends K, ? extends V> change) {
        if (helper != null) {
            helper.fireValueChangedEvent(change);
        }
    }

    protected MapExpressionHelper(ObservableMapValue<K, V> observable) {
        this.observable = observable;
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/binding/MapExpressionHelper$SingleInvalidation.class */
    private static class SingleInvalidation<K, V> extends MapExpressionHelper<K, V> {
        private final InvalidationListener listener;

        private SingleInvalidation(ObservableMapValue<K, V> observable, InvalidationListener listener) {
            super(observable);
            this.listener = listener;
        }

        @Override // com.sun.javafx.binding.MapExpressionHelper
        protected MapExpressionHelper<K, V> addListener(InvalidationListener listener) {
            return new Generic(this.observable, this.listener, listener);
        }

        @Override // com.sun.javafx.binding.MapExpressionHelper
        protected MapExpressionHelper<K, V> removeListener(InvalidationListener listener) {
            if (listener.equals(this.listener)) {
                return null;
            }
            return this;
        }

        @Override // com.sun.javafx.binding.MapExpressionHelper
        protected MapExpressionHelper<K, V> addListener(ChangeListener<? super ObservableMap<K, V>> listener) {
            return new Generic(this.observable, this.listener, listener);
        }

        @Override // com.sun.javafx.binding.MapExpressionHelper
        protected MapExpressionHelper<K, V> removeListener(ChangeListener<? super ObservableMap<K, V>> listener) {
            return this;
        }

        @Override // com.sun.javafx.binding.MapExpressionHelper
        protected MapExpressionHelper<K, V> addListener(MapChangeListener<? super K, ? super V> listener) {
            return new Generic(this.observable, this.listener, listener);
        }

        @Override // com.sun.javafx.binding.MapExpressionHelper
        protected MapExpressionHelper<K, V> removeListener(MapChangeListener<? super K, ? super V> listener) {
            return this;
        }

        @Override // com.sun.javafx.binding.MapExpressionHelper
        protected void fireValueChangedEvent() {
            this.listener.invalidated(this.observable);
        }

        @Override // com.sun.javafx.binding.MapExpressionHelper
        protected void fireValueChangedEvent(MapChangeListener.Change<? extends K, ? extends V> change) {
            this.listener.invalidated(this.observable);
        }
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/binding/MapExpressionHelper$SingleChange.class */
    private static class SingleChange<K, V> extends MapExpressionHelper<K, V> {
        private final ChangeListener<? super ObservableMap<K, V>> listener;
        private ObservableMap<K, V> currentValue;

        private SingleChange(ObservableMapValue<K, V> observable, ChangeListener<? super ObservableMap<K, V>> listener) {
            super(observable);
            this.listener = listener;
            this.currentValue = observable.getValue2();
        }

        @Override // com.sun.javafx.binding.MapExpressionHelper
        protected MapExpressionHelper<K, V> addListener(InvalidationListener listener) {
            return new Generic(this.observable, listener, this.listener);
        }

        @Override // com.sun.javafx.binding.MapExpressionHelper
        protected MapExpressionHelper<K, V> removeListener(InvalidationListener listener) {
            return this;
        }

        @Override // com.sun.javafx.binding.MapExpressionHelper
        protected MapExpressionHelper<K, V> addListener(ChangeListener<? super ObservableMap<K, V>> listener) {
            return new Generic(this.observable, this.listener, listener);
        }

        @Override // com.sun.javafx.binding.MapExpressionHelper
        protected MapExpressionHelper<K, V> removeListener(ChangeListener<? super ObservableMap<K, V>> listener) {
            if (listener.equals(this.listener)) {
                return null;
            }
            return this;
        }

        @Override // com.sun.javafx.binding.MapExpressionHelper
        protected MapExpressionHelper<K, V> addListener(MapChangeListener<? super K, ? super V> listener) {
            return new Generic(this.observable, this.listener, listener);
        }

        @Override // com.sun.javafx.binding.MapExpressionHelper
        protected MapExpressionHelper<K, V> removeListener(MapChangeListener<? super K, ? super V> listener) {
            return this;
        }

        @Override // com.sun.javafx.binding.MapExpressionHelper
        protected void fireValueChangedEvent() {
            ObservableMap<K, V> oldValue = this.currentValue;
            this.currentValue = this.observable.getValue2();
            if (this.currentValue != oldValue) {
                this.listener.changed(this.observable, oldValue, this.currentValue);
            }
        }

        @Override // com.sun.javafx.binding.MapExpressionHelper
        protected void fireValueChangedEvent(MapChangeListener.Change<? extends K, ? extends V> change) {
            this.listener.changed(this.observable, this.currentValue, this.currentValue);
        }
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/binding/MapExpressionHelper$SingleMapChange.class */
    private static class SingleMapChange<K, V> extends MapExpressionHelper<K, V> {
        private final MapChangeListener<? super K, ? super V> listener;
        private ObservableMap<K, V> currentValue;

        private SingleMapChange(ObservableMapValue<K, V> observable, MapChangeListener<? super K, ? super V> listener) {
            super(observable);
            this.listener = listener;
            this.currentValue = observable.getValue2();
        }

        @Override // com.sun.javafx.binding.MapExpressionHelper
        protected MapExpressionHelper<K, V> addListener(InvalidationListener listener) {
            return new Generic(this.observable, listener, this.listener);
        }

        @Override // com.sun.javafx.binding.MapExpressionHelper
        protected MapExpressionHelper<K, V> removeListener(InvalidationListener listener) {
            return this;
        }

        @Override // com.sun.javafx.binding.MapExpressionHelper
        protected MapExpressionHelper<K, V> addListener(ChangeListener<? super ObservableMap<K, V>> listener) {
            return new Generic(this.observable, listener, this.listener);
        }

        @Override // com.sun.javafx.binding.MapExpressionHelper
        protected MapExpressionHelper<K, V> removeListener(ChangeListener<? super ObservableMap<K, V>> listener) {
            return this;
        }

        @Override // com.sun.javafx.binding.MapExpressionHelper
        protected MapExpressionHelper<K, V> addListener(MapChangeListener<? super K, ? super V> listener) {
            return new Generic(this.observable, this.listener, listener);
        }

        @Override // com.sun.javafx.binding.MapExpressionHelper
        protected MapExpressionHelper<K, V> removeListener(MapChangeListener<? super K, ? super V> listener) {
            if (listener.equals(this.listener)) {
                return null;
            }
            return this;
        }

        @Override // com.sun.javafx.binding.MapExpressionHelper
        protected void fireValueChangedEvent() {
            ObservableMap<K, V> oldValue = this.currentValue;
            this.currentValue = this.observable.getValue2();
            if (this.currentValue != oldValue) {
                SimpleChange<K, V> change = new SimpleChange<>(this.observable);
                if (this.currentValue == null) {
                    for (Map.Entry<K, V> element : oldValue.entrySet()) {
                        this.listener.onChanged(change.setRemoved(element.getKey(), element.getValue()));
                    }
                    return;
                }
                if (oldValue == null) {
                    for (Map.Entry<K, V> element2 : this.currentValue.entrySet()) {
                        this.listener.onChanged(change.setAdded(element2.getKey(), element2.getValue()));
                    }
                    return;
                }
                for (Map.Entry<K, V> element3 : oldValue.entrySet()) {
                    K key = element3.getKey();
                    V oldEntry = element3.getValue();
                    if (this.currentValue.containsKey(key)) {
                        V newEntry = this.currentValue.get(key);
                        if (oldEntry == null) {
                            if (newEntry != null) {
                                this.listener.onChanged(change.setPut(key, oldEntry, newEntry));
                            }
                        } else if (!newEntry.equals(oldEntry)) {
                            this.listener.onChanged(change.setPut(key, oldEntry, newEntry));
                        }
                    } else {
                        this.listener.onChanged(change.setRemoved(key, oldEntry));
                    }
                }
                for (Map.Entry<K, V> element4 : this.currentValue.entrySet()) {
                    K key2 = element4.getKey();
                    if (!oldValue.containsKey(key2)) {
                        this.listener.onChanged(change.setAdded(key2, element4.getValue()));
                    }
                }
            }
        }

        @Override // com.sun.javafx.binding.MapExpressionHelper
        protected void fireValueChangedEvent(MapChangeListener.Change<? extends K, ? extends V> change) {
            this.listener.onChanged(new SimpleChange(this.observable, change));
        }
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/binding/MapExpressionHelper$Generic.class */
    private static class Generic<K, V> extends MapExpressionHelper<K, V> {
        private InvalidationListener[] invalidationListeners;
        private ChangeListener<? super ObservableMap<K, V>>[] changeListeners;
        private MapChangeListener<? super K, ? super V>[] mapChangeListeners;
        private int invalidationSize;
        private int changeSize;
        private int mapChangeSize;
        private boolean locked;
        private ObservableMap<K, V> currentValue;

        private Generic(ObservableMapValue<K, V> observable, InvalidationListener listener0, InvalidationListener listener1) {
            super(observable);
            this.invalidationListeners = new InvalidationListener[]{listener0, listener1};
            this.invalidationSize = 2;
        }

        private Generic(ObservableMapValue<K, V> observable, ChangeListener<? super ObservableMap<K, V>> listener0, ChangeListener<? super ObservableMap<K, V>> listener1) {
            super(observable);
            this.changeListeners = new ChangeListener[]{listener0, listener1};
            this.changeSize = 2;
            this.currentValue = observable.getValue2();
        }

        private Generic(ObservableMapValue<K, V> observable, MapChangeListener<? super K, ? super V> listener0, MapChangeListener<? super K, ? super V> listener1) {
            super(observable);
            this.mapChangeListeners = new MapChangeListener[]{listener0, listener1};
            this.mapChangeSize = 2;
            this.currentValue = observable.getValue2();
        }

        private Generic(ObservableMapValue<K, V> observable, InvalidationListener invalidationListener, ChangeListener<? super ObservableMap<K, V>> changeListener) {
            super(observable);
            this.invalidationListeners = new InvalidationListener[]{invalidationListener};
            this.invalidationSize = 1;
            this.changeListeners = new ChangeListener[]{changeListener};
            this.changeSize = 1;
            this.currentValue = observable.getValue2();
        }

        private Generic(ObservableMapValue<K, V> observable, InvalidationListener invalidationListener, MapChangeListener<? super K, ? super V> listChangeListener) {
            super(observable);
            this.invalidationListeners = new InvalidationListener[]{invalidationListener};
            this.invalidationSize = 1;
            this.mapChangeListeners = new MapChangeListener[]{listChangeListener};
            this.mapChangeSize = 1;
            this.currentValue = observable.getValue2();
        }

        private Generic(ObservableMapValue<K, V> observable, ChangeListener<? super ObservableMap<K, V>> changeListener, MapChangeListener<? super K, ? super V> listChangeListener) {
            super(observable);
            this.changeListeners = new ChangeListener[]{changeListener};
            this.changeSize = 1;
            this.mapChangeListeners = new MapChangeListener[]{listChangeListener};
            this.mapChangeSize = 1;
            this.currentValue = observable.getValue2();
        }

        @Override // com.sun.javafx.binding.MapExpressionHelper
        protected MapExpressionHelper<K, V> addListener(InvalidationListener listener) {
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

        @Override // com.sun.javafx.binding.MapExpressionHelper
        protected MapExpressionHelper<K, V> removeListener(InvalidationListener listener) {
            if (this.invalidationListeners != null) {
                int index = 0;
                while (true) {
                    if (index >= this.invalidationSize) {
                        break;
                    }
                    if (!listener.equals(this.invalidationListeners[index])) {
                        index++;
                    } else if (this.invalidationSize == 1) {
                        if (this.changeSize == 1 && this.mapChangeSize == 0) {
                            return new SingleChange(this.observable, this.changeListeners[0]);
                        }
                        if (this.changeSize == 0 && this.mapChangeSize == 1) {
                            return new SingleMapChange(this.observable, this.mapChangeListeners[0]);
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

        @Override // com.sun.javafx.binding.MapExpressionHelper
        protected MapExpressionHelper<K, V> addListener(ChangeListener<? super ObservableMap<K, V>> listener) {
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
                ChangeListener<? super ObservableMap<K, V>>[] changeListenerArr = this.changeListeners;
                int i2 = this.changeSize;
                this.changeSize = i2 + 1;
                changeListenerArr[i2] = listener;
            }
            if (this.changeSize == 1) {
                this.currentValue = this.observable.getValue2();
            }
            return this;
        }

        @Override // com.sun.javafx.binding.MapExpressionHelper
        protected MapExpressionHelper<K, V> removeListener(ChangeListener<? super ObservableMap<K, V>> listener) {
            if (this.changeListeners != null) {
                int index = 0;
                while (true) {
                    if (index >= this.changeSize) {
                        break;
                    }
                    if (!listener.equals(this.changeListeners[index])) {
                        index++;
                    } else if (this.changeSize == 1) {
                        if (this.invalidationSize == 1 && this.mapChangeSize == 0) {
                            return new SingleInvalidation(this.observable, this.invalidationListeners[0]);
                        }
                        if (this.invalidationSize == 0 && this.mapChangeSize == 1) {
                            return new SingleMapChange(this.observable, this.mapChangeListeners[0]);
                        }
                        this.changeListeners = null;
                        this.changeSize = 0;
                    } else {
                        int numMoved = (this.changeSize - index) - 1;
                        ChangeListener<? super ObservableMap<K, V>>[] oldListeners = this.changeListeners;
                        if (this.locked) {
                            this.changeListeners = new ChangeListener[this.changeListeners.length];
                            System.arraycopy(oldListeners, 0, this.changeListeners, 0, index + 1);
                        }
                        if (numMoved > 0) {
                            System.arraycopy(oldListeners, index + 1, this.changeListeners, index, numMoved);
                        }
                        this.changeSize--;
                        if (!this.locked) {
                            ChangeListener<? super ObservableMap<K, V>>[] changeListenerArr = this.changeListeners;
                            int i2 = this.changeSize - 1;
                            this.changeSize = i2;
                            changeListenerArr[i2] = null;
                        }
                    }
                }
            }
            return this;
        }

        @Override // com.sun.javafx.binding.MapExpressionHelper
        protected MapExpressionHelper<K, V> addListener(MapChangeListener<? super K, ? super V> listener) {
            if (this.mapChangeListeners == null) {
                this.mapChangeListeners = new MapChangeListener[]{listener};
                this.mapChangeSize = 1;
            } else {
                int oldCapacity = this.mapChangeListeners.length;
                if (this.locked) {
                    int newCapacity = this.mapChangeSize < oldCapacity ? oldCapacity : ((oldCapacity * 3) / 2) + 1;
                    this.mapChangeListeners = (MapChangeListener[]) Arrays.copyOf(this.mapChangeListeners, newCapacity);
                } else if (this.mapChangeSize == oldCapacity) {
                    this.mapChangeSize = trim(this.mapChangeSize, this.mapChangeListeners);
                    if (this.mapChangeSize == oldCapacity) {
                        int newCapacity2 = ((oldCapacity * 3) / 2) + 1;
                        this.mapChangeListeners = (MapChangeListener[]) Arrays.copyOf(this.mapChangeListeners, newCapacity2);
                    }
                }
                MapChangeListener<? super K, ? super V>[] mapChangeListenerArr = this.mapChangeListeners;
                int i2 = this.mapChangeSize;
                this.mapChangeSize = i2 + 1;
                mapChangeListenerArr[i2] = listener;
            }
            if (this.mapChangeSize == 1) {
                this.currentValue = this.observable.getValue2();
            }
            return this;
        }

        @Override // com.sun.javafx.binding.MapExpressionHelper
        protected MapExpressionHelper<K, V> removeListener(MapChangeListener<? super K, ? super V> listener) {
            if (this.mapChangeListeners != null) {
                int index = 0;
                while (true) {
                    if (index >= this.mapChangeSize) {
                        break;
                    }
                    if (!listener.equals(this.mapChangeListeners[index])) {
                        index++;
                    } else if (this.mapChangeSize == 1) {
                        if (this.invalidationSize == 1 && this.changeSize == 0) {
                            return new SingleInvalidation(this.observable, this.invalidationListeners[0]);
                        }
                        if (this.invalidationSize == 0 && this.changeSize == 1) {
                            return new SingleChange(this.observable, this.changeListeners[0]);
                        }
                        this.mapChangeListeners = null;
                        this.mapChangeSize = 0;
                    } else {
                        int numMoved = (this.mapChangeSize - index) - 1;
                        MapChangeListener<? super K, ? super V>[] oldListeners = this.mapChangeListeners;
                        if (this.locked) {
                            this.mapChangeListeners = new MapChangeListener[this.mapChangeListeners.length];
                            System.arraycopy(oldListeners, 0, this.mapChangeListeners, 0, index + 1);
                        }
                        if (numMoved > 0) {
                            System.arraycopy(oldListeners, index + 1, this.mapChangeListeners, index, numMoved);
                        }
                        this.mapChangeSize--;
                        if (!this.locked) {
                            MapChangeListener<? super K, ? super V>[] mapChangeListenerArr = this.mapChangeListeners;
                            int i2 = this.mapChangeSize - 1;
                            this.mapChangeSize = i2;
                            mapChangeListenerArr[i2] = null;
                        }
                    }
                }
            }
            return this;
        }

        @Override // com.sun.javafx.binding.MapExpressionHelper
        protected void fireValueChangedEvent() {
            if (this.changeSize == 0 && this.mapChangeSize == 0) {
                notifyListeners(this.currentValue, null);
                return;
            }
            ObservableMap<K, V> oldValue = this.currentValue;
            this.currentValue = this.observable.getValue2();
            notifyListeners(oldValue, null);
        }

        @Override // com.sun.javafx.binding.MapExpressionHelper
        protected void fireValueChangedEvent(MapChangeListener.Change<? extends K, ? extends V> change) {
            SimpleChange<K, V> mappedChange = this.mapChangeSize == 0 ? null : new SimpleChange<>(this.observable, change);
            notifyListeners(this.currentValue, mappedChange);
        }

        /* JADX WARN: Removed duplicated region for block: B:61:0x01df A[Catch: all -> 0x0288, LOOP:8: B:59:0x01d8->B:61:0x01df, LOOP_END, TryCatch #0 {all -> 0x0288, blocks: (B:3:0x0023, B:6:0x0032, B:7:0x0045, B:14:0x005b, B:22:0x0087, B:24:0x009b, B:26:0x00ae, B:27:0x00bb, B:29:0x00c5, B:32:0x00ee, B:37:0x0109, B:38:0x0119, B:40:0x0123, B:43:0x014c, B:46:0x0163, B:47:0x0170, B:49:0x017a, B:51:0x01a6, B:58:0x01ca, B:61:0x01df, B:56:0x01c0, B:63:0x01f3, B:66:0x0206, B:68:0x021a, B:69:0x022a, B:71:0x0234, B:73:0x0254, B:76:0x026c), top: B:85:0x0023 }] */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        private void notifyListeners(javafx.collections.ObservableMap<K, V> r6, com.sun.javafx.binding.MapExpressionHelper.SimpleChange<K, V> r7) {
            /*
                Method dump skipped, instructions count: 659
                To view this dump change 'Code comments level' option to 'DEBUG'
            */
            throw new UnsupportedOperationException("Method not decompiled: com.sun.javafx.binding.MapExpressionHelper.Generic.notifyListeners(javafx.collections.ObservableMap, com.sun.javafx.binding.MapExpressionHelper$SimpleChange):void");
        }
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/binding/MapExpressionHelper$SimpleChange.class */
    public static class SimpleChange<K, V> extends MapChangeListener.Change<K, V> {
        private K key;
        private V old;
        private V added;
        private boolean removeOp;
        private boolean addOp;

        public SimpleChange(ObservableMap<K, V> set) {
            super(set);
        }

        public SimpleChange(ObservableMap<K, V> set, MapChangeListener.Change<? extends K, ? extends V> source) {
            super(set);
            this.key = source.getKey();
            this.old = source.getValueRemoved();
            this.added = source.getValueAdded();
            this.addOp = source.wasAdded();
            this.removeOp = source.wasRemoved();
        }

        public SimpleChange<K, V> setRemoved(K key, V old) {
            this.key = key;
            this.old = old;
            this.added = null;
            this.addOp = false;
            this.removeOp = true;
            return this;
        }

        public SimpleChange<K, V> setAdded(K key, V added) {
            this.key = key;
            this.old = null;
            this.added = added;
            this.addOp = true;
            this.removeOp = false;
            return this;
        }

        public SimpleChange<K, V> setPut(K key, V old, V added) {
            this.key = key;
            this.old = old;
            this.added = added;
            this.addOp = true;
            this.removeOp = true;
            return this;
        }

        @Override // javafx.collections.MapChangeListener.Change
        public boolean wasAdded() {
            return this.addOp;
        }

        @Override // javafx.collections.MapChangeListener.Change
        public boolean wasRemoved() {
            return this.removeOp;
        }

        @Override // javafx.collections.MapChangeListener.Change
        public K getKey() {
            return this.key;
        }

        @Override // javafx.collections.MapChangeListener.Change
        public V getValueAdded() {
            return this.added;
        }

        @Override // javafx.collections.MapChangeListener.Change
        public V getValueRemoved() {
            return this.old;
        }

        public String toString() {
            StringBuilder builder = new StringBuilder();
            if (this.addOp) {
                if (this.removeOp) {
                    builder.append("replaced ").append((Object) this.old).append("by ").append((Object) this.added);
                } else {
                    builder.append("added ").append((Object) this.added);
                }
            } else {
                builder.append("removed ").append((Object) this.old);
            }
            builder.append(" at key ").append((Object) this.key);
            return builder.toString();
        }
    }
}
