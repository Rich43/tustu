package com.sun.javafx.fxml.expression;

import com.sun.javafx.fxml.BeanAdapter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javafx.beans.InvalidationListener;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.beans.value.ObservableValueBase;
import javafx.collections.ListChangeListener;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;

/* loaded from: jfxrt.jar:com/sun/javafx/fxml/expression/ExpressionValue.class */
public class ExpressionValue extends ObservableValueBase<Object> {
    private Object namespace;
    private Expression expression;
    private Class<?> type;
    private ArrayList<KeyPathMonitor> argumentMonitors;
    private int listenerCount = 0;

    /* loaded from: jfxrt.jar:com/sun/javafx/fxml/expression/ExpressionValue$KeyPathMonitor.class */
    private class KeyPathMonitor {
        private String key;
        private KeyPathMonitor next;
        private Object namespace = null;
        private ListChangeListener<Object> listChangeListener = new ListChangeListener<Object>() { // from class: com.sun.javafx.fxml.expression.ExpressionValue.KeyPathMonitor.1
            @Override // javafx.collections.ListChangeListener
            public void onChanged(ListChangeListener.Change<? extends Object> change) throws NumberFormatException {
                while (change.next()) {
                    int index = Integer.parseInt(KeyPathMonitor.this.key);
                    if (index >= change.getFrom() && index < change.getTo()) {
                        ExpressionValue.this.fireValueChangedEvent();
                        KeyPathMonitor.this.remonitor();
                    }
                }
            }
        };
        private MapChangeListener<String, Object> mapChangeListener = new MapChangeListener<String, Object>() { // from class: com.sun.javafx.fxml.expression.ExpressionValue.KeyPathMonitor.2
            @Override // javafx.collections.MapChangeListener
            public void onChanged(MapChangeListener.Change<? extends String, ? extends Object> change) {
                if (KeyPathMonitor.this.key.equals(change.getKey())) {
                    ExpressionValue.this.fireValueChangedEvent();
                    KeyPathMonitor.this.remonitor();
                }
            }
        };
        private ChangeListener<Object> propertyChangeListener = new ChangeListener<Object>() { // from class: com.sun.javafx.fxml.expression.ExpressionValue.KeyPathMonitor.3
            @Override // javafx.beans.value.ChangeListener
            public void changed(ObservableValue<? extends Object> observable, Object oldValue, Object newValue) {
                ExpressionValue.this.fireValueChangedEvent();
                KeyPathMonitor.this.remonitor();
            }
        };

        public KeyPathMonitor(Iterator<String> keyPathIterator) {
            this.key = keyPathIterator.next();
            if (keyPathIterator.hasNext()) {
                this.next = ExpressionValue.this.new KeyPathMonitor(keyPathIterator);
            } else {
                this.next = null;
            }
        }

        public void monitor(Object namespace) {
            Object value;
            if (namespace instanceof ObservableList) {
                ((ObservableList) namespace).addListener(this.listChangeListener);
            } else if (namespace instanceof ObservableMap) {
                ((ObservableMap) namespace).addListener(this.mapChangeListener);
            } else {
                BeanAdapter namespaceAdapter = new BeanAdapter(namespace);
                ObservableValue<Object> propertyModel = namespaceAdapter.getPropertyModel(this.key);
                if (propertyModel != null) {
                    propertyModel.addListener(this.propertyChangeListener);
                }
                namespace = namespaceAdapter;
            }
            this.namespace = namespace;
            if (this.next != null && (value = Expression.get(namespace, this.key)) != null) {
                this.next.monitor(value);
            }
        }

        public void unmonitor() {
            if (this.namespace instanceof ObservableList) {
                ((ObservableList) this.namespace).removeListener(this.listChangeListener);
            } else if (this.namespace instanceof ObservableMap) {
                ((ObservableMap) this.namespace).removeListener(this.mapChangeListener);
            } else if (this.namespace != null) {
                BeanAdapter namespaceAdapter = (BeanAdapter) this.namespace;
                ObservableValue<Object> propertyModel = namespaceAdapter.getPropertyModel(this.key);
                if (propertyModel != null) {
                    propertyModel.removeListener(this.propertyChangeListener);
                }
            }
            this.namespace = null;
            if (this.next != null) {
                this.next.unmonitor();
            }
        }

        public void remonitor() {
            if (this.next != null) {
                this.next.unmonitor();
                Object value = Expression.get(this.namespace, this.key);
                if (value != null) {
                    this.next.monitor(value);
                }
            }
        }
    }

    public ExpressionValue(Object namespace, Expression expression, Class<?> type) {
        if (namespace == null) {
            throw new NullPointerException();
        }
        if (expression == null) {
            throw new NullPointerException();
        }
        if (type == null) {
            throw new NullPointerException();
        }
        this.namespace = namespace;
        this.expression = expression;
        this.type = type;
        List<KeyPath> arguments = expression.getArguments();
        this.argumentMonitors = new ArrayList<>(arguments.size());
        for (KeyPath argument : arguments) {
            this.argumentMonitors.add(new KeyPathMonitor(argument.iterator()));
        }
    }

    @Override // javafx.beans.value.ObservableValue
    /* renamed from: getValue */
    public Object getValue2() {
        return BeanAdapter.coerce(this.expression.evaluate(this.namespace), this.type);
    }

    @Override // javafx.beans.value.ObservableValueBase, javafx.beans.Observable
    public void addListener(InvalidationListener listener) {
        if (this.listenerCount == 0) {
            monitorArguments();
        }
        super.addListener(listener);
        this.listenerCount++;
    }

    @Override // javafx.beans.value.ObservableValueBase, javafx.beans.Observable
    public void removeListener(InvalidationListener listener) {
        super.removeListener(listener);
        this.listenerCount--;
        if (this.listenerCount == 0) {
            unmonitorArguments();
        }
    }

    @Override // javafx.beans.value.ObservableValueBase, javafx.beans.value.ObservableValue
    public void addListener(ChangeListener<? super Object> listener) {
        if (this.listenerCount == 0) {
            monitorArguments();
        }
        super.addListener(listener);
        this.listenerCount++;
    }

    @Override // javafx.beans.value.ObservableValueBase, javafx.beans.value.ObservableValue
    public void removeListener(ChangeListener<? super Object> listener) {
        super.removeListener(listener);
        this.listenerCount--;
        if (this.listenerCount == 0) {
            unmonitorArguments();
        }
    }

    private void monitorArguments() {
        Iterator<KeyPathMonitor> it = this.argumentMonitors.iterator();
        while (it.hasNext()) {
            KeyPathMonitor argumentMonitor = it.next();
            argumentMonitor.monitor(this.namespace);
        }
    }

    private void unmonitorArguments() {
        Iterator<KeyPathMonitor> it = this.argumentMonitors.iterator();
        while (it.hasNext()) {
            KeyPathMonitor argumentMonitor = it.next();
            argumentMonitor.unmonitor();
        }
    }
}
