package com.sun.javafx.css;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/* loaded from: jfxrt.jar:com/sun/javafx/css/SelectorPartitioning.class */
public final class SelectorPartitioning {
    private final Map<PartitionKey, Partition> idMap = new HashMap();
    private final Map<PartitionKey, Partition> typeMap = new HashMap();
    private final Map<PartitionKey, Partition> styleClassMap = new HashMap();
    private int ordinal;
    private static final int ID_BIT = 4;
    private static final int TYPE_BIT = 2;
    private static final int STYLECLASS_BIT = 1;
    private static final PartitionKey WILDCARD;
    private static final Comparator<Selector> COMPARATOR;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !SelectorPartitioning.class.desiredAssertionStatus();
        WILDCARD = new PartitionKey("*");
        COMPARATOR = (o1, o2) -> {
            return o1.getOrdinal() - o2.getOrdinal();
        };
    }

    SelectorPartitioning() {
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/css/SelectorPartitioning$PartitionKey.class */
    private static final class PartitionKey<K> {
        private final K key;

        private PartitionKey(K key) {
            this.key = key;
        }

        public boolean equals(Object obj) {
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            PartitionKey<K> other = (PartitionKey) obj;
            if (this.key == other.key) {
                return true;
            }
            if (this.key == null || !this.key.equals(other.key)) {
                return false;
            }
            return true;
        }

        public int hashCode() {
            int hash = (71 * 7) + (this.key != null ? this.key.hashCode() : 0);
            return hash;
        }
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/css/SelectorPartitioning$Partition.class */
    private static final class Partition {
        private final PartitionKey key;
        private final Map<PartitionKey, Slot> slots;
        private List<Selector> selectors;

        private Partition(PartitionKey key) {
            this.key = key;
            this.slots = new HashMap();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void addSelector(Selector pair) {
            if (this.selectors == null) {
                this.selectors = new ArrayList();
            }
            this.selectors.add(pair);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public Slot partition(PartitionKey id, Map<PartitionKey, Partition> map) {
            Slot slot = this.slots.get(id);
            if (slot == null) {
                Partition partition = SelectorPartitioning.getPartition(id, map);
                slot = new Slot(partition);
                this.slots.put(id, slot);
            }
            return slot;
        }
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/css/SelectorPartitioning$Slot.class */
    private static final class Slot {
        private final Partition partition;
        private final Map<PartitionKey, Slot> referents;
        private List<Selector> selectors;

        private Slot(Partition partition) {
            this.partition = partition;
            this.referents = new HashMap();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void addSelector(Selector pair) {
            if (this.selectors == null) {
                this.selectors = new ArrayList();
            }
            this.selectors.add(pair);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public Slot partition(PartitionKey id, Map<PartitionKey, Partition> map) {
            Slot slot = this.referents.get(id);
            if (slot == null) {
                Partition p2 = SelectorPartitioning.getPartition(id, map);
                slot = new Slot(p2);
                this.referents.put(id, slot);
            }
            return slot;
        }
    }

    void reset() {
        this.idMap.clear();
        this.typeMap.clear();
        this.styleClassMap.clear();
        this.ordinal = 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static Partition getPartition(PartitionKey id, Map<PartitionKey, Partition> map) {
        Partition treeNode = map.get(id);
        if (treeNode == null) {
            treeNode = new Partition(id);
            map.put(id, treeNode);
        }
        return treeNode;
    }

    void partition(Selector selector) {
        SimpleSelector simpleSelector;
        if (selector instanceof CompoundSelector) {
            List<SimpleSelector> selectors = ((CompoundSelector) selector).getSelectors();
            int last = selectors.size() - 1;
            simpleSelector = selectors.get(last);
        } else {
            simpleSelector = (SimpleSelector) selector;
        }
        String selectorId = simpleSelector.getId();
        boolean hasId = (selectorId == null || selectorId.isEmpty()) ? false : true;
        PartitionKey idKey = hasId ? new PartitionKey(selectorId) : null;
        String selectorType = simpleSelector.getName();
        boolean hasType = (selectorType == null || selectorType.isEmpty()) ? false : true;
        PartitionKey typeKey = hasType ? new PartitionKey(selectorType) : null;
        Set<StyleClass> selectorStyleClass = simpleSelector.getStyleClassSet();
        boolean hasStyleClass = selectorStyleClass != null && selectorStyleClass.size() > 0;
        PartitionKey styleClassKey = hasStyleClass ? new PartitionKey(selectorStyleClass) : null;
        int c2 = (hasId ? 4 : 0) | (hasType ? 2 : 0) | (hasStyleClass ? 1 : 0);
        int i2 = this.ordinal;
        this.ordinal = i2 + 1;
        selector.setOrdinal(i2);
        switch (c2) {
            case 1:
            case 4:
            case 5:
            default:
                if (!$assertionsDisabled) {
                    throw new AssertionError();
                }
                return;
            case 2:
            case 3:
                Partition partition = getPartition(typeKey, this.typeMap);
                if ((c2 & 1) != 1) {
                    partition.addSelector(selector);
                    return;
                } else {
                    partition.partition(styleClassKey, this.styleClassMap).addSelector(selector);
                    return;
                }
            case 6:
            case 7:
                Slot slot = getPartition(idKey, this.idMap).partition(typeKey, this.typeMap);
                if ((c2 & 1) == 1) {
                    slot = slot.partition(styleClassKey, this.styleClassMap);
                }
                slot.addSelector(selector);
                return;
        }
    }

    List<Selector> match(String selectorId, String selectorType, Set<StyleClass> selectorStyleClass) {
        boolean hasId = (selectorId == null || selectorId.isEmpty()) ? false : true;
        PartitionKey idKey = hasId ? new PartitionKey(selectorId) : null;
        boolean hasType = (selectorType == null || selectorType.isEmpty()) ? false : true;
        PartitionKey typeKey = hasType ? new PartitionKey(selectorType) : null;
        boolean hasStyleClass = selectorStyleClass != null && selectorStyleClass.size() > 0;
        PartitionKey styleClassKey = hasStyleClass ? new PartitionKey(selectorStyleClass) : null;
        int c2 = (hasId ? 4 : 0) | (hasType ? 2 : 0) | (hasStyleClass ? 1 : 0);
        List<Selector> selectors = new ArrayList<>();
        while (c2 != 0) {
            switch (c2) {
                case 1:
                    c2--;
                    break;
                case 2:
                case 3:
                    PartitionKey typePK = typeKey;
                    do {
                        Partition partition = this.typeMap.get(typePK);
                        if (partition != null) {
                            if (partition.selectors != null) {
                                selectors.addAll(partition.selectors);
                            }
                            if ((c2 & 1) == 1) {
                                Set<StyleClass> key = (Set) styleClassKey.key;
                                for (Slot s2 : partition.slots.values()) {
                                    if (s2.selectors != null && !s2.selectors.isEmpty()) {
                                        Set<StyleClass> other = (Set) s2.partition.key.key;
                                        if (key.containsAll(other)) {
                                            selectors.addAll(s2.selectors);
                                        }
                                    }
                                }
                            }
                        }
                        typePK = !WILDCARD.equals(typePK) ? WILDCARD : null;
                    } while (typePK != null);
                    c2 -= 2;
                    break;
                case 4:
                case 5:
                    c2 -= 4;
                    break;
                case 6:
                case 7:
                    Partition partition2 = this.idMap.get(idKey);
                    if (partition2 != null) {
                        if (partition2.selectors != null) {
                            selectors.addAll(partition2.selectors);
                        }
                        PartitionKey typePK2 = typeKey;
                        do {
                            Slot slot = (Slot) partition2.slots.get(typePK2);
                            if (slot != null) {
                                if (slot.selectors != null) {
                                    selectors.addAll(slot.selectors);
                                }
                                if ((c2 & 1) == 1) {
                                    Set<StyleClass> key2 = (Set) styleClassKey.key;
                                    for (Slot s3 : slot.referents.values()) {
                                        if (s3.selectors != null && !s3.selectors.isEmpty()) {
                                            Set<StyleClass> other2 = (Set) s3.partition.key.key;
                                            if (key2.containsAll(other2)) {
                                                selectors.addAll(s3.selectors);
                                            }
                                        }
                                    }
                                }
                            }
                            typePK2 = !WILDCARD.equals(typePK2) ? WILDCARD : null;
                        } while (typePK2 != null);
                    }
                    c2 -= 4;
                    break;
                default:
                    if (!$assertionsDisabled) {
                        throw new AssertionError();
                    }
                    break;
            }
        }
        Collections.sort(selectors, COMPARATOR);
        return selectors;
    }
}
