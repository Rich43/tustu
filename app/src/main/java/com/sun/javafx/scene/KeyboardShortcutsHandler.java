package com.sun.javafx.scene;

import com.sun.javafx.PlatformUtil;
import com.sun.javafx.collections.ObservableListWrapper;
import com.sun.javafx.collections.ObservableMapWrapper;
import com.sun.javafx.event.BasicEventDispatcher;
import com.sun.javafx.scene.traversal.Direction;
import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.event.Event;
import javafx.scene.Node;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.Mnemonic;

/* loaded from: jfxrt.jar:com/sun/javafx/scene/KeyboardShortcutsHandler.class */
public final class KeyboardShortcutsHandler extends BasicEventDispatcher {
    private ObservableMap<KeyCombination, Runnable> accelerators;
    private CopyOnWriteMap<KeyCombination, Runnable> acceleratorsBackingMap;
    private ObservableMap<KeyCombination, ObservableList<Mnemonic>> mnemonics;
    private boolean mnemonicsDisplayEnabled = false;

    public void addMnemonic(Mnemonic m2) {
        ObservableList<Mnemonic> mnemonicsList = getMnemonics().get(m2.getKeyCombination());
        if (mnemonicsList == null) {
            mnemonicsList = new ObservableListWrapper(new ArrayList());
            getMnemonics().put(m2.getKeyCombination(), mnemonicsList);
        }
        boolean foundMnemonic = false;
        for (int i2 = 0; i2 < mnemonicsList.size(); i2++) {
            if (mnemonicsList.get(i2) == m2) {
                foundMnemonic = true;
            }
        }
        if (!foundMnemonic) {
            mnemonicsList.add(m2);
        }
    }

    public void removeMnemonic(Mnemonic m2) {
        ObservableList<Mnemonic> mnemonicsList = getMnemonics().get(m2.getKeyCombination());
        if (mnemonicsList != null) {
            for (int i2 = 0; i2 < mnemonicsList.size(); i2++) {
                if (mnemonicsList.get(i2).getNode() == m2.getNode()) {
                    mnemonicsList.remove(i2);
                }
            }
        }
    }

    public ObservableMap<KeyCombination, ObservableList<Mnemonic>> getMnemonics() {
        if (this.mnemonics == null) {
            this.mnemonics = new ObservableMapWrapper(new HashMap());
        }
        return this.mnemonics;
    }

    public ObservableMap<KeyCombination, Runnable> getAccelerators() {
        if (this.accelerators == null) {
            this.acceleratorsBackingMap = new CopyOnWriteMap<>();
            this.accelerators = new ObservableMapWrapper(this.acceleratorsBackingMap);
        }
        return this.accelerators;
    }

    private void traverse(Event event, Node node, Direction dir) {
        if (node.impl_traverse(dir)) {
            event.consume();
        }
    }

    public void processTraversal(Event event) {
        if ((event instanceof KeyEvent) && event.getEventType() == KeyEvent.KEY_PRESSED && !((KeyEvent) event).isMetaDown() && !((KeyEvent) event).isControlDown() && !((KeyEvent) event).isAltDown()) {
            Object obj = event.getTarget();
            if (obj instanceof Node) {
                switch (((KeyEvent) event).getCode()) {
                    case TAB:
                        if (((KeyEvent) event).isShiftDown()) {
                            traverse(event, (Node) obj, Direction.PREVIOUS);
                            break;
                        } else {
                            traverse(event, (Node) obj, Direction.NEXT);
                            break;
                        }
                    case UP:
                        traverse(event, (Node) obj, Direction.UP);
                        break;
                    case DOWN:
                        traverse(event, (Node) obj, Direction.DOWN);
                        break;
                    case LEFT:
                        traverse(event, (Node) obj, Direction.LEFT);
                        break;
                    case RIGHT:
                        traverse(event, (Node) obj, Direction.RIGHT);
                        break;
                }
            }
        }
    }

    @Override // com.sun.javafx.event.BasicEventDispatcher
    public Event dispatchBubblingEvent(Event event) {
        if (event.getEventType() == KeyEvent.KEY_PRESSED) {
            if (PlatformUtil.isMac()) {
                if (((KeyEvent) event).isMetaDown()) {
                    processMnemonics((KeyEvent) event);
                }
            } else if (((KeyEvent) event).isAltDown() || isMnemonicsDisplayEnabled()) {
                processMnemonics((KeyEvent) event);
            }
            if (!event.isConsumed()) {
                processAccelerators((KeyEvent) event);
            }
            if (!event.isConsumed()) {
                processTraversal(event);
            }
        }
        if (!PlatformUtil.isMac()) {
            if (event.getEventType() == KeyEvent.KEY_PRESSED && ((KeyEvent) event).isAltDown() && !event.isConsumed()) {
                if (!isMnemonicsDisplayEnabled()) {
                    setMnemonicsDisplayEnabled(true);
                } else if (PlatformUtil.isWindows()) {
                    setMnemonicsDisplayEnabled(!isMnemonicsDisplayEnabled());
                }
            }
            if (event.getEventType() == KeyEvent.KEY_RELEASED && !((KeyEvent) event).isAltDown() && !PlatformUtil.isWindows()) {
                setMnemonicsDisplayEnabled(false);
            }
        }
        return event;
    }

    private void processMnemonics(KeyEvent event) {
        if (this.mnemonics != null) {
            ObservableList<Mnemonic> mnemonicsList = null;
            Iterator<Map.Entry<KeyCombination, ObservableList<Mnemonic>>> it = this.mnemonics.entrySet().iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                Map.Entry<KeyCombination, ObservableList<Mnemonic>> mnemonic = it.next();
                if (!isMnemonicsDisplayEnabled()) {
                    if (mnemonic.getKey().match(event)) {
                        mnemonicsList = mnemonic.getValue();
                        break;
                    }
                } else {
                    KeyEvent fakeEvent = new KeyEvent(null, event.getTarget(), KeyEvent.KEY_PRESSED, event.getCharacter(), event.getText(), event.getCode(), event.isShiftDown(), event.isControlDown(), true, event.isMetaDown());
                    if (mnemonic.getKey().match(fakeEvent)) {
                        mnemonicsList = mnemonic.getValue();
                        break;
                    }
                }
            }
            if (mnemonicsList != null) {
                boolean multipleNodes = false;
                Node firstNode = null;
                Mnemonic firstMnemonics = null;
                int focusedIndex = -1;
                int nextFocusable = -1;
                for (int i2 = 0; i2 < mnemonicsList.size(); i2++) {
                    if (mnemonicsList.get(i2) instanceof Mnemonic) {
                        Node currentNode = mnemonicsList.get(i2).getNode();
                        if (firstMnemonics == null && currentNode.impl_isTreeVisible() && !currentNode.isDisabled()) {
                            firstMnemonics = mnemonicsList.get(i2);
                        }
                        if (currentNode.impl_isTreeVisible() && currentNode.isFocusTraversable() && !currentNode.isDisabled()) {
                            if (firstNode == null) {
                                firstNode = currentNode;
                            } else {
                                multipleNodes = true;
                                if (focusedIndex != -1 && nextFocusable == -1) {
                                    nextFocusable = i2;
                                }
                            }
                        }
                        if (currentNode.isFocused()) {
                            focusedIndex = i2;
                        }
                    }
                }
                if (firstNode != null) {
                    if ((!multipleNodes) || focusedIndex == -1 || focusedIndex >= mnemonicsList.size()) {
                        firstNode.requestFocus();
                        event.consume();
                    } else {
                        if (nextFocusable != -1) {
                            mnemonicsList.get(nextFocusable).getNode().requestFocus();
                        } else {
                            firstNode.requestFocus();
                        }
                        event.consume();
                    }
                }
                if (!multipleNodes && firstMnemonics != null) {
                    firstMnemonics.fire();
                }
            }
        }
    }

    private void processAccelerators(KeyEvent event) {
        Runnable acceleratorRunnable;
        if (this.acceleratorsBackingMap != null) {
            this.acceleratorsBackingMap.lock();
            try {
                for (Map.Entry<KeyCombination, Runnable> accelerator : ((CopyOnWriteMap) this.acceleratorsBackingMap).backingMap.entrySet()) {
                    if (accelerator.getKey().match(event) && (acceleratorRunnable = accelerator.getValue()) != null) {
                        acceleratorRunnable.run();
                        event.consume();
                    }
                }
            } finally {
                this.acceleratorsBackingMap.unlock();
            }
        }
    }

    private void processMnemonicsKeyDisplay() {
        if (this.mnemonics != null) {
            for (Map.Entry<KeyCombination, ObservableList<Mnemonic>> mnemonic : this.mnemonics.entrySet()) {
                ObservableList<Mnemonic> mnemonicsList = mnemonic.getValue();
                if (mnemonicsList != null) {
                    for (int i2 = 0; i2 < mnemonicsList.size(); i2++) {
                        Node currentNode = mnemonicsList.get(i2).getNode();
                        currentNode.impl_setShowMnemonics(this.mnemonicsDisplayEnabled);
                    }
                }
            }
        }
    }

    public boolean isMnemonicsDisplayEnabled() {
        return this.mnemonicsDisplayEnabled;
    }

    public void setMnemonicsDisplayEnabled(boolean b2) {
        if (b2 != this.mnemonicsDisplayEnabled) {
            this.mnemonicsDisplayEnabled = b2;
            processMnemonicsKeyDisplay();
        }
    }

    public void clearNodeMnemonics(Node node) {
        if (this.mnemonics != null) {
            for (ObservableList<Mnemonic> list : this.mnemonics.values()) {
                Iterator<Mnemonic> it = list.iterator();
                while (it.hasNext()) {
                    Mnemonic m2 = it.next();
                    if (m2.getNode() == node) {
                        it.remove();
                    }
                }
            }
        }
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/scene/KeyboardShortcutsHandler$CopyOnWriteMap.class */
    private static class CopyOnWriteMap<K, V> extends AbstractMap<K, V> {
        private Map<K, V> backingMap;
        private boolean lock;

        private CopyOnWriteMap() {
            this.backingMap = new HashMap();
        }

        public void lock() {
            this.lock = true;
        }

        public void unlock() {
            this.lock = false;
        }

        @Override // java.util.AbstractMap, java.util.Map
        public V put(K key, V value) {
            if (this.lock) {
                this.backingMap = new HashMap(this.backingMap);
                this.lock = false;
            }
            return this.backingMap.put(key, value);
        }

        @Override // java.util.AbstractMap, java.util.Map
        public Set<Map.Entry<K, V>> entrySet() {
            return new AbstractSet<Map.Entry<K, V>>() { // from class: com.sun.javafx.scene.KeyboardShortcutsHandler.CopyOnWriteMap.1
                @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.List
                public Iterator<Map.Entry<K, V>> iterator() {
                    return new Iterator<Map.Entry<K, V>>() { // from class: com.sun.javafx.scene.KeyboardShortcutsHandler.CopyOnWriteMap.1.1
                        private Iterator<Map.Entry<K, V>> backingIt;
                        private Map<K, V> backingMapAtCreation;
                        private Map.Entry<K, V> lastNext = null;

                        {
                            this.backingIt = CopyOnWriteMap.this.backingMap.entrySet().iterator();
                            this.backingMapAtCreation = CopyOnWriteMap.this.backingMap;
                        }

                        @Override // java.util.Iterator
                        public boolean hasNext() {
                            checkCoMod();
                            return this.backingIt.hasNext();
                        }

                        private void checkCoMod() {
                            if (CopyOnWriteMap.this.backingMap != this.backingMapAtCreation) {
                                throw new ConcurrentModificationException();
                            }
                        }

                        @Override // java.util.Iterator
                        public Map.Entry<K, V> next() {
                            checkCoMod();
                            Map.Entry<K, V> next = this.backingIt.next();
                            this.lastNext = next;
                            return next;
                        }

                        @Override // java.util.Iterator
                        public void remove() {
                            checkCoMod();
                            if (this.lastNext != null) {
                                if (CopyOnWriteMap.this.lock) {
                                    CopyOnWriteMap.this.backingMap = new HashMap(CopyOnWriteMap.this.backingMap);
                                    this.backingIt = CopyOnWriteMap.this.backingMap.entrySet().iterator();
                                    while (!this.lastNext.equals(this.backingIt.next())) {
                                    }
                                    CopyOnWriteMap.this.lock = false;
                                }
                                this.backingIt.remove();
                                this.lastNext = null;
                                return;
                            }
                            throw new IllegalStateException();
                        }
                    };
                }

                @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
                public int size() {
                    return CopyOnWriteMap.this.backingMap.size();
                }
            };
        }
    }
}
