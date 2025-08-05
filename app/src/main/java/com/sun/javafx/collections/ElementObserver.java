package com.sun.javafx.collections;

import java.util.IdentityHashMap;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.collections.ObservableListBase;
import javafx.util.Callback;

/* loaded from: jfxrt.jar:com/sun/javafx/collections/ElementObserver.class */
final class ElementObserver<E> {
    private Callback<E, Observable[]> extractor;
    private final Callback<E, InvalidationListener> listenerGenerator;
    private final ObservableListBase<E> list;
    private IdentityHashMap<E, ElementsMapElement> elementsMap = new IdentityHashMap<>();

    /* loaded from: jfxrt.jar:com/sun/javafx/collections/ElementObserver$ElementsMapElement.class */
    private static class ElementsMapElement {
        InvalidationListener listener;
        int counter = 1;

        public ElementsMapElement(InvalidationListener listener) {
            this.listener = listener;
        }

        public void increment() {
            this.counter++;
        }

        public int decrement() {
            int i2 = this.counter - 1;
            this.counter = i2;
            return i2;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public InvalidationListener getListener() {
            return this.listener;
        }
    }

    ElementObserver(Callback<E, Observable[]> extractor, Callback<E, InvalidationListener> listenerGenerator, ObservableListBase<E> list) {
        this.extractor = extractor;
        this.listenerGenerator = listenerGenerator;
        this.list = list;
    }

    void attachListener(E e2) {
        if (this.elementsMap != null && e2 != null) {
            if (this.elementsMap.containsKey(e2)) {
                this.elementsMap.get(e2).increment();
                return;
            }
            InvalidationListener listener = this.listenerGenerator.call(e2);
            for (Observable o2 : this.extractor.call(e2)) {
                o2.addListener(listener);
            }
            this.elementsMap.put(e2, new ElementsMapElement(listener));
        }
    }

    void detachListener(E e2) {
        if (this.elementsMap != null && e2 != null) {
            ElementsMapElement el = this.elementsMap.get(e2);
            for (Observable o2 : this.extractor.call(e2)) {
                o2.removeListener(el.getListener());
            }
            if (el.decrement() == 0) {
                this.elementsMap.remove(e2);
            }
        }
    }
}
