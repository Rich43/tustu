package javafx.collections;

import com.sun.javafx.collections.ChangeHelper;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import javafx.collections.ListChangeListener;

/* loaded from: jfxrt.jar:javafx/collections/ListChangeBuilder.class */
final class ListChangeBuilder<E> {
    private static final int[] EMPTY_PERM;
    private final ObservableListBase<E> list;
    private int changeLock;
    private List<SubChange<E>> addRemoveChanges;
    private List<SubChange<E>> updateChanges;
    private SubChange<E> permutationChange;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !ListChangeBuilder.class.desiredAssertionStatus();
        EMPTY_PERM = new int[0];
    }

    private void checkAddRemoveList() {
        if (this.addRemoveChanges == null) {
            this.addRemoveChanges = new ArrayList();
        }
    }

    private void checkState() {
        if (this.changeLock == 0) {
            throw new IllegalStateException("beginChange was not called on this builder");
        }
    }

    private int findSubChange(int idx, List<SubChange<E>> list) {
        int from = 0;
        int to = list.size() - 1;
        while (from <= to) {
            int changeIdx = (from + to) / 2;
            SubChange<E> change = list.get(changeIdx);
            if (idx >= change.to) {
                from = changeIdx + 1;
            } else if (idx < change.from) {
                to = changeIdx - 1;
            } else {
                return changeIdx;
            }
        }
        return from ^ (-1);
    }

    private void insertUpdate(int pos) {
        int idx = findSubChange(pos, this.updateChanges);
        if (idx < 0) {
            int idx2 = idx ^ (-1);
            if (idx2 > 0) {
                SubChange<E> change = this.updateChanges.get(idx2 - 1);
                if (change.to == pos) {
                    change.to = pos + 1;
                    return;
                }
            }
            if (idx2 < this.updateChanges.size()) {
                SubChange<E> change2 = this.updateChanges.get(idx2);
                if (change2.from == pos + 1) {
                    change2.from = pos;
                    return;
                }
            }
            this.updateChanges.add(idx2, new SubChange<>(pos, pos + 1, null, EMPTY_PERM, true));
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:14:0x008c  */
    /* JADX WARN: Removed duplicated region for block: B:9:0x0041  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private void insertRemoved(int r11, E r12) {
        /*
            Method dump skipped, instructions count: 319
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: javafx.collections.ListChangeBuilder.insertRemoved(int, java.lang.Object):void");
    }

    /* JADX WARN: Removed duplicated region for block: B:9:0x0040  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private void insertAdd(int r11, int r12) {
        /*
            r10 = this;
            r0 = r10
            r1 = r11
            r2 = r10
            java.util.List<javafx.collections.ListChangeBuilder$SubChange<E>> r2 = r2.addRemoveChanges
            int r0 = r0.findSubChange(r1, r2)
            r13 = r0
            r0 = r12
            r1 = r11
            int r0 = r0 - r1
            r14 = r0
            r0 = r13
            if (r0 >= 0) goto L61
            r0 = r13
            r1 = -1
            r0 = r0 ^ r1
            r13 = r0
            r0 = r13
            if (r0 <= 0) goto L40
            r0 = r10
            java.util.List<javafx.collections.ListChangeBuilder$SubChange<E>> r0 = r0.addRemoveChanges
            r1 = r13
            r2 = 1
            int r1 = r1 - r2
            java.lang.Object r0 = r0.get(r1)
            javafx.collections.ListChangeBuilder$SubChange r0 = (javafx.collections.ListChangeBuilder.SubChange) r0
            r1 = r0
            r15 = r1
            int r0 = r0.to
            r1 = r11
            if (r0 != r1) goto L40
            r0 = r15
            r1 = r12
            r0.to = r1
            int r13 = r13 + (-1)
            goto L7c
        L40:
            r0 = r10
            java.util.List<javafx.collections.ListChangeBuilder$SubChange<E>> r0 = r0.addRemoveChanges
            r1 = r13
            javafx.collections.ListChangeBuilder$SubChange r2 = new javafx.collections.ListChangeBuilder$SubChange
            r3 = r2
            r4 = r11
            r5 = r12
            java.util.ArrayList r6 = new java.util.ArrayList
            r7 = r6
            r7.<init>()
            int[] r7 = javafx.collections.ListChangeBuilder.EMPTY_PERM
            r8 = 0
            r3.<init>(r4, r5, r6, r7, r8)
            r0.add(r1, r2)
            goto L7c
        L61:
            r0 = r10
            java.util.List<javafx.collections.ListChangeBuilder$SubChange<E>> r0 = r0.addRemoveChanges
            r1 = r13
            java.lang.Object r0 = r0.get(r1)
            javafx.collections.ListChangeBuilder$SubChange r0 = (javafx.collections.ListChangeBuilder.SubChange) r0
            r15 = r0
            r0 = r15
            r1 = r0
            int r1 = r1.to
            r2 = r14
            int r1 = r1 + r2
            r0.to = r1
        L7c:
            r0 = r13
            r1 = 1
            int r0 = r0 + r1
            r15 = r0
        L81:
            r0 = r15
            r1 = r10
            java.util.List<javafx.collections.ListChangeBuilder$SubChange<E>> r1 = r1.addRemoveChanges
            int r1 = r1.size()
            if (r0 >= r1) goto Lbd
            r0 = r10
            java.util.List<javafx.collections.ListChangeBuilder$SubChange<E>> r0 = r0.addRemoveChanges
            r1 = r15
            java.lang.Object r0 = r0.get(r1)
            javafx.collections.ListChangeBuilder$SubChange r0 = (javafx.collections.ListChangeBuilder.SubChange) r0
            r16 = r0
            r0 = r16
            r1 = r0
            int r1 = r1.from
            r2 = r14
            int r1 = r1 + r2
            r0.from = r1
            r0 = r16
            r1 = r0
            int r1 = r1.to
            r2 = r14
            int r1 = r1 + r2
            r0.to = r1
            int r15 = r15 + 1
            goto L81
        Lbd:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: javafx.collections.ListChangeBuilder.insertAdd(int, int):void");
    }

    private int compress(List<SubChange<E>> list) {
        int removed = 0;
        SubChange<E> prev = list.get(0);
        int sz = list.size();
        for (int i2 = 1; i2 < sz; i2++) {
            SubChange<E> cur = list.get(i2);
            if (prev.to == cur.from) {
                prev.to = cur.to;
                if (prev.removed != null || cur.removed != null) {
                    if (prev.removed == null) {
                        prev.removed = new ArrayList();
                    }
                    prev.removed.addAll(cur.removed);
                }
                list.set(i2, null);
                removed++;
            } else {
                prev = cur;
            }
        }
        return removed;
    }

    /* loaded from: jfxrt.jar:javafx/collections/ListChangeBuilder$SubChange.class */
    private static class SubChange<E> {
        int from;
        int to;
        List<E> removed;
        int[] perm;
        boolean updated;

        public SubChange(int from, int to, List<E> removed, int[] perm, boolean updated) {
            this.from = from;
            this.to = to;
            this.removed = removed;
            this.perm = perm;
            this.updated = updated;
        }
    }

    ListChangeBuilder(ObservableListBase<E> list) {
        this.list = list;
    }

    public void nextRemove(int idx, E removed) {
        checkState();
        checkAddRemoveList();
        SubChange<E> last = this.addRemoveChanges.isEmpty() ? null : this.addRemoveChanges.get(this.addRemoveChanges.size() - 1);
        if (last != null && last.to == idx) {
            last.removed.add(removed);
        } else if (last != null && last.from == idx + 1) {
            last.from--;
            last.to--;
            last.removed.add(0, removed);
        } else {
            insertRemoved(idx, removed);
        }
        if (this.updateChanges != null && !this.updateChanges.isEmpty()) {
            int uPos = findSubChange(idx, this.updateChanges);
            if (uPos < 0) {
                uPos ^= -1;
            } else {
                SubChange<E> change = this.updateChanges.get(uPos);
                if (change.from == change.to - 1) {
                    this.updateChanges.remove(uPos);
                } else {
                    change.to--;
                    uPos++;
                }
            }
            for (int i2 = uPos; i2 < this.updateChanges.size(); i2++) {
                this.updateChanges.get(i2).from--;
                this.updateChanges.get(i2).to--;
            }
        }
    }

    public void nextRemove(int idx, List<? extends E> removed) {
        checkState();
        for (int i2 = 0; i2 < removed.size(); i2++) {
            nextRemove(idx, (int) removed.get(i2));
        }
    }

    public void nextAdd(int from, int to) {
        int uPos;
        checkState();
        checkAddRemoveList();
        SubChange<E> last = this.addRemoveChanges.isEmpty() ? null : this.addRemoveChanges.get(this.addRemoveChanges.size() - 1);
        int numberOfAdded = to - from;
        if (last != null && last.to == from) {
            last.to = to;
        } else if (last != null && from >= last.from && from < last.to) {
            last.to += numberOfAdded;
        } else {
            insertAdd(from, to);
        }
        if (this.updateChanges != null && !this.updateChanges.isEmpty()) {
            int uPos2 = findSubChange(from, this.updateChanges);
            if (uPos2 < 0) {
                uPos = uPos2 ^ (-1);
            } else {
                SubChange<E> change = this.updateChanges.get(uPos2);
                this.updateChanges.add(uPos2 + 1, new SubChange<>(to, (change.to + to) - from, null, EMPTY_PERM, true));
                change.to = from;
                uPos = uPos2 + 2;
            }
            for (int i2 = uPos; i2 < this.updateChanges.size(); i2++) {
                this.updateChanges.get(i2).from += numberOfAdded;
                this.updateChanges.get(i2).to += numberOfAdded;
            }
        }
    }

    public void nextPermutation(int from, int to, int[] perm) {
        checkState();
        int prePermFrom = from;
        int prePermTo = to;
        int[] prePerm = perm;
        if (this.addRemoveChanges != null && !this.addRemoveChanges.isEmpty()) {
            int[] mapToOriginal = new int[this.list.size()];
            Set<Integer> removed = new TreeSet<>();
            int last = 0;
            int offset = 0;
            int sz = this.addRemoveChanges.size();
            for (int i2 = 0; i2 < sz; i2++) {
                SubChange<E> change = this.addRemoveChanges.get(i2);
                int j2 = last;
                while (j2 < change.from) {
                    mapToOriginal[(j2 < from || j2 >= to) ? j2 : perm[j2 - from]] = j2 + offset;
                    j2++;
                }
                int j3 = change.from;
                while (j3 < change.to) {
                    mapToOriginal[(j3 < from || j3 >= to) ? j3 : perm[j3 - from]] = -1;
                    j3++;
                }
                last = change.to;
                int removedSize = change.removed != null ? change.removed.size() : 0;
                int upTo = change.from + offset + removedSize;
                for (int j4 = change.from + offset; j4 < upTo; j4++) {
                    removed.add(Integer.valueOf(j4));
                }
                offset += removedSize - (change.to - change.from);
            }
            int i3 = last;
            while (i3 < mapToOriginal.length) {
                mapToOriginal[(i3 < from || i3 >= to) ? i3 : perm[i3 - from]] = i3 + offset;
                i3++;
            }
            int[] newPerm = new int[this.list.size() + offset];
            int mapPtr = 0;
            for (int i4 = 0; i4 < newPerm.length; i4++) {
                if (removed.contains(Integer.valueOf(i4))) {
                    newPerm[i4] = i4;
                } else {
                    while (mapToOriginal[mapPtr] == -1) {
                        mapPtr++;
                    }
                    int i5 = mapPtr;
                    mapPtr++;
                    newPerm[mapToOriginal[i5]] = i4;
                }
            }
            prePermFrom = 0;
            prePermTo = newPerm.length;
            prePerm = newPerm;
        }
        if (this.permutationChange != null) {
            if (prePermFrom == this.permutationChange.from && prePermTo == this.permutationChange.to) {
                for (int i6 = 0; i6 < prePerm.length; i6++) {
                    this.permutationChange.perm[i6] = prePerm[this.permutationChange.perm[i6] - prePermFrom];
                }
            } else {
                int newTo = Math.max(this.permutationChange.to, prePermTo);
                int newFrom = Math.min(this.permutationChange.from, prePermFrom);
                int[] newPerm2 = new int[newTo - newFrom];
                for (int i7 = newFrom; i7 < newTo; i7++) {
                    if (i7 < this.permutationChange.from || i7 >= this.permutationChange.to) {
                        newPerm2[i7 - newFrom] = prePerm[i7 - prePermFrom];
                    } else {
                        int p2 = this.permutationChange.perm[i7 - this.permutationChange.from];
                        if (p2 < prePermFrom || p2 >= prePermTo) {
                            newPerm2[i7 - newFrom] = p2;
                        } else {
                            newPerm2[i7 - newFrom] = prePerm[p2 - prePermFrom];
                        }
                    }
                }
                this.permutationChange.from = newFrom;
                this.permutationChange.to = newTo;
                this.permutationChange.perm = newPerm2;
            }
        } else {
            this.permutationChange = new SubChange<>(prePermFrom, prePermTo, null, prePerm, false);
        }
        if (this.addRemoveChanges != null && !this.addRemoveChanges.isEmpty()) {
            Set<Integer> newAdded = new TreeSet<>();
            Map<Integer, List<E>> newRemoved = new HashMap<>();
            int sz2 = this.addRemoveChanges.size();
            for (int i8 = 0; i8 < sz2; i8++) {
                SubChange<E> change2 = this.addRemoveChanges.get(i8);
                for (int cIndex = change2.from; cIndex < change2.to; cIndex++) {
                    if (cIndex < from || cIndex >= to) {
                        newAdded.add(Integer.valueOf(cIndex));
                    } else {
                        newAdded.add(Integer.valueOf(perm[cIndex - from]));
                    }
                }
                if (change2.removed != null) {
                    if (change2.from < from || change2.from >= to) {
                        newRemoved.put(Integer.valueOf(change2.from), change2.removed);
                    } else {
                        newRemoved.put(Integer.valueOf(perm[change2.from - from]), change2.removed);
                    }
                }
            }
            this.addRemoveChanges.clear();
            SubChange<E> lastChange = null;
            for (Integer i9 : newAdded) {
                if (lastChange == null || lastChange.to != i9.intValue()) {
                    lastChange = new SubChange<>(i9.intValue(), i9.intValue() + 1, null, EMPTY_PERM, false);
                    this.addRemoveChanges.add(lastChange);
                } else {
                    lastChange.to = i9.intValue() + 1;
                }
                List<E> removed2 = newRemoved.remove(i9);
                if (removed2 != null) {
                    if (lastChange.removed != null) {
                        lastChange.removed.addAll(removed2);
                    } else {
                        lastChange.removed = removed2;
                    }
                }
            }
            for (Map.Entry<Integer, List<E>> e2 : newRemoved.entrySet()) {
                Integer at2 = e2.getKey();
                int idx = findSubChange(at2.intValue(), this.addRemoveChanges);
                if (!$assertionsDisabled && idx >= 0) {
                    throw new AssertionError();
                }
                this.addRemoveChanges.add(idx ^ (-1), new SubChange<>(at2.intValue(), at2.intValue(), e2.getValue(), new int[0], false));
            }
        }
        if (this.updateChanges != null && !this.updateChanges.isEmpty()) {
            Set<Integer> newUpdated = new TreeSet<>();
            int sz3 = this.updateChanges.size();
            for (int i10 = 0; i10 < sz3; i10++) {
                SubChange<E> change3 = this.updateChanges.get(i10);
                for (int cIndex2 = change3.from; cIndex2 < change3.to; cIndex2++) {
                    if (cIndex2 < from || cIndex2 >= to) {
                        newUpdated.add(Integer.valueOf(cIndex2));
                    } else {
                        newUpdated.add(Integer.valueOf(perm[cIndex2 - from]));
                    }
                }
            }
            this.updateChanges.clear();
            SubChange<E> lastUpdateChange = null;
            for (Integer i11 : newUpdated) {
                if (lastUpdateChange == null || lastUpdateChange.to != i11.intValue()) {
                    lastUpdateChange = new SubChange<>(i11.intValue(), i11.intValue() + 1, null, EMPTY_PERM, true);
                    this.updateChanges.add(lastUpdateChange);
                } else {
                    lastUpdateChange.to = i11.intValue() + 1;
                }
            }
        }
    }

    public void nextReplace(int from, int to, List<? extends E> removed) {
        nextRemove(from, (List) removed);
        nextAdd(from, to);
    }

    public void nextSet(int idx, E old) {
        nextRemove(idx, (int) old);
        nextAdd(idx, idx + 1);
    }

    public void nextUpdate(int idx) {
        checkState();
        if (this.updateChanges == null) {
            this.updateChanges = new ArrayList();
        }
        SubChange<E> last = this.updateChanges.isEmpty() ? null : this.updateChanges.get(this.updateChanges.size() - 1);
        if (last != null && last.to == idx) {
            last.to = idx + 1;
        } else {
            insertUpdate(idx);
        }
    }

    private void commit() {
        boolean addRemoveNotEmpty = (this.addRemoveChanges == null || this.addRemoveChanges.isEmpty()) ? false : true;
        boolean updateNotEmpty = (this.updateChanges == null || this.updateChanges.isEmpty()) ? false : true;
        if (this.changeLock == 0) {
            if (addRemoveNotEmpty || updateNotEmpty || this.permutationChange != null) {
                int totalSize = (this.updateChanges != null ? this.updateChanges.size() : 0) + (this.addRemoveChanges != null ? this.addRemoveChanges.size() : 0) + (this.permutationChange != null ? 1 : 0);
                if (totalSize == 1) {
                    if (addRemoveNotEmpty) {
                        this.list.fireChange(new SingleChange(finalizeSubChange(this.addRemoveChanges.get(0)), this.list));
                        this.addRemoveChanges.clear();
                        return;
                    } else if (updateNotEmpty) {
                        this.list.fireChange(new SingleChange(finalizeSubChange(this.updateChanges.get(0)), this.list));
                        this.updateChanges.clear();
                        return;
                    } else {
                        this.list.fireChange(new SingleChange(finalizeSubChange(this.permutationChange), this.list));
                        this.permutationChange = null;
                        return;
                    }
                }
                if (updateNotEmpty) {
                    int removed = compress(this.updateChanges);
                    totalSize -= removed;
                }
                if (addRemoveNotEmpty) {
                    int removed2 = compress(this.addRemoveChanges);
                    totalSize -= removed2;
                }
                SubChange<E>[] array = new SubChange[totalSize];
                int ptr = 0;
                if (this.permutationChange != null) {
                    ptr = 0 + 1;
                    array[0] = this.permutationChange;
                }
                if (addRemoveNotEmpty) {
                    int sz = this.addRemoveChanges.size();
                    for (int i2 = 0; i2 < sz; i2++) {
                        SubChange<E> change = this.addRemoveChanges.get(i2);
                        if (change != null) {
                            int i3 = ptr;
                            ptr++;
                            array[i3] = change;
                        }
                    }
                }
                if (updateNotEmpty) {
                    int sz2 = this.updateChanges.size();
                    for (int i4 = 0; i4 < sz2; i4++) {
                        SubChange<E> change2 = this.updateChanges.get(i4);
                        if (change2 != null) {
                            int i5 = ptr;
                            ptr++;
                            array[i5] = change2;
                        }
                    }
                }
                this.list.fireChange(new IterableChange(finalizeSubChangeArray(array), this.list));
                if (this.addRemoveChanges != null) {
                    this.addRemoveChanges.clear();
                }
                if (this.updateChanges != null) {
                    this.updateChanges.clear();
                }
                this.permutationChange = null;
            }
        }
    }

    public void beginChange() {
        this.changeLock++;
    }

    public void endChange() {
        if (this.changeLock <= 0) {
            throw new IllegalStateException("Called endChange before beginChange");
        }
        this.changeLock--;
        commit();
    }

    private static <E> SubChange<E>[] finalizeSubChangeArray(SubChange<E>[] changes) {
        for (SubChange<E> c2 : changes) {
            finalizeSubChange(c2);
        }
        return changes;
    }

    private static <E> SubChange<E> finalizeSubChange(SubChange<E> c2) {
        if (c2.perm == null) {
            c2.perm = EMPTY_PERM;
        }
        if (c2.removed == null) {
            c2.removed = Collections.emptyList();
        } else {
            c2.removed = Collections.unmodifiableList(c2.removed);
        }
        return c2;
    }

    /* loaded from: jfxrt.jar:javafx/collections/ListChangeBuilder$SingleChange.class */
    private static class SingleChange<E> extends ListChangeListener.Change<E> {
        private final SubChange<E> change;
        private boolean onChange;

        public SingleChange(SubChange<E> change, ObservableListBase<E> list) {
            super(list);
            this.change = change;
        }

        @Override // javafx.collections.ListChangeListener.Change
        public boolean next() {
            if (this.onChange) {
                return false;
            }
            this.onChange = true;
            return true;
        }

        @Override // javafx.collections.ListChangeListener.Change
        public void reset() {
            this.onChange = false;
        }

        @Override // javafx.collections.ListChangeListener.Change
        public int getFrom() {
            checkState();
            return this.change.from;
        }

        @Override // javafx.collections.ListChangeListener.Change
        public int getTo() {
            checkState();
            return this.change.to;
        }

        @Override // javafx.collections.ListChangeListener.Change
        public List<E> getRemoved() {
            checkState();
            return this.change.removed;
        }

        @Override // javafx.collections.ListChangeListener.Change
        protected int[] getPermutation() {
            checkState();
            return this.change.perm;
        }

        @Override // javafx.collections.ListChangeListener.Change
        public boolean wasUpdated() {
            checkState();
            return this.change.updated;
        }

        private void checkState() {
            if (!this.onChange) {
                throw new IllegalStateException("Invalid Change state: next() must be called before inspecting the Change.");
            }
        }

        public String toString() {
            String ret;
            if (this.change.perm.length != 0) {
                ret = ChangeHelper.permChangeToString(this.change.perm);
            } else if (this.change.updated) {
                ret = ChangeHelper.updateChangeToString(this.change.from, this.change.to);
            } else {
                ret = ChangeHelper.addRemoveChangeToString(this.change.from, this.change.to, getList(), this.change.removed);
            }
            return "{ " + ret + " }";
        }
    }

    /* loaded from: jfxrt.jar:javafx/collections/ListChangeBuilder$IterableChange.class */
    private static class IterableChange<E> extends ListChangeListener.Change<E> {
        private SubChange[] changes;
        private int cursor;

        private IterableChange(SubChange[] changes, ObservableList<E> list) {
            super(list);
            this.cursor = -1;
            this.changes = changes;
        }

        @Override // javafx.collections.ListChangeListener.Change
        public boolean next() {
            if (this.cursor + 1 < this.changes.length) {
                this.cursor++;
                return true;
            }
            return false;
        }

        @Override // javafx.collections.ListChangeListener.Change
        public void reset() {
            this.cursor = -1;
        }

        @Override // javafx.collections.ListChangeListener.Change
        public int getFrom() {
            checkState();
            return this.changes[this.cursor].from;
        }

        @Override // javafx.collections.ListChangeListener.Change
        public int getTo() {
            checkState();
            return this.changes[this.cursor].to;
        }

        @Override // javafx.collections.ListChangeListener.Change
        public List<E> getRemoved() {
            checkState();
            return this.changes[this.cursor].removed;
        }

        @Override // javafx.collections.ListChangeListener.Change
        protected int[] getPermutation() {
            checkState();
            return this.changes[this.cursor].perm;
        }

        @Override // javafx.collections.ListChangeListener.Change
        public boolean wasUpdated() {
            checkState();
            return this.changes[this.cursor].updated;
        }

        private void checkState() {
            if (this.cursor == -1) {
                throw new IllegalStateException("Invalid Change state: next() must be called before inspecting the Change.");
            }
        }

        public String toString() {
            StringBuilder b2 = new StringBuilder();
            b2.append("{ ");
            for (int c2 = 0; c2 < this.changes.length; c2++) {
                if (this.changes[c2].perm.length != 0) {
                    b2.append(ChangeHelper.permChangeToString(this.changes[c2].perm));
                } else if (this.changes[c2].updated) {
                    b2.append(ChangeHelper.updateChangeToString(this.changes[c2].from, this.changes[c2].to));
                } else {
                    b2.append(ChangeHelper.addRemoveChangeToString(this.changes[c2].from, this.changes[c2].to, getList(), this.changes[c2].removed));
                }
                if (c2 != this.changes.length - 1) {
                    b2.append(", ");
                }
            }
            b2.append(" }");
            return b2.toString();
        }
    }
}
