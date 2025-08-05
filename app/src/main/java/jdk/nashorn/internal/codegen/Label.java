package jdk.nashorn.internal.codegen;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import jdk.nashorn.internal.codegen.types.Type;

/* loaded from: nashorn.jar:jdk/nashorn/internal/codegen/Label.class */
public final class Label implements Serializable {
    private static final long serialVersionUID = 1;
    private static int nextId;
    private final String name;
    private transient Stack stack;
    private transient jdk.internal.org.objectweb.asm.Label label;
    private final int id;
    private transient boolean reachable;
    private transient boolean breakTarget;
    private String str;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !Label.class.desiredAssertionStatus();
        nextId = 0;
    }

    /* loaded from: nashorn.jar:jdk/nashorn/internal/codegen/Label$Stack.class */
    static final class Stack implements Cloneable {
        static final int NON_LOAD = -1;
        int sp;
        int firstTemp;
        static final /* synthetic */ boolean $assertionsDisabled;
        Type[] data = new Type[8];
        int[] localLoads = new int[8];
        List<Type> localVariableTypes = new ArrayList(8);
        BitSet symbolBoundary = new BitSet();

        static {
            $assertionsDisabled = !Label.class.desiredAssertionStatus();
        }

        Stack() {
        }

        boolean isEmpty() {
            return this.sp == 0;
        }

        int size() {
            return this.sp;
        }

        void clear() {
            this.sp = 0;
        }

        void push(Type type) {
            if (this.data.length == this.sp) {
                Type[] newData = new Type[this.sp * 2];
                int[] newLocalLoad = new int[this.sp * 2];
                System.arraycopy(this.data, 0, newData, 0, this.sp);
                System.arraycopy(this.localLoads, 0, newLocalLoad, 0, this.sp);
                this.data = newData;
                this.localLoads = newLocalLoad;
            }
            this.data[this.sp] = type;
            this.localLoads[this.sp] = -1;
            this.sp++;
        }

        Type peek() {
            return peek(0);
        }

        Type peek(int n2) {
            int pos = (this.sp - 1) - n2;
            if (pos < 0) {
                return null;
            }
            return this.data[pos];
        }

        Type[] getTopTypes(int count) {
            Type[] topTypes = new Type[count];
            System.arraycopy(this.data, this.sp - count, topTypes, 0, count);
            return topTypes;
        }

        int[] getLocalLoads(int from, int to) {
            int count = to - from;
            int[] topLocalLoads = new int[count];
            System.arraycopy(this.localLoads, from, topLocalLoads, 0, count);
            return topLocalLoads;
        }

        int getUsedSlotsWithLiveTemporaries() {
            int afterSlot;
            int usedSlots = this.firstTemp;
            int i2 = this.sp;
            while (true) {
                int i3 = i2;
                i2--;
                if (i3 > 0) {
                    int slot = this.localLoads[i2];
                    if (slot != -1 && (afterSlot = slot + this.localVariableTypes.get(slot).getSlots()) > usedSlots) {
                        usedSlots = afterSlot;
                    }
                } else {
                    return usedSlots;
                }
            }
        }

        void joinFrom(Stack joinOrigin, boolean breakTarget) {
            if (!$assertionsDisabled && !isStackCompatible(joinOrigin)) {
                throw new AssertionError();
            }
            if (breakTarget) {
                this.firstTemp = Math.min(this.firstTemp, joinOrigin.firstTemp);
            } else if (!$assertionsDisabled && this.firstTemp != joinOrigin.firstTemp) {
                throw new AssertionError();
            }
            int[] otherLoads = joinOrigin.localLoads;
            int firstDeadTemp = this.firstTemp;
            for (int i2 = 0; i2 < this.sp; i2++) {
                int localLoad = this.localLoads[i2];
                if (localLoad != otherLoads[i2]) {
                    this.localLoads[i2] = -1;
                } else if (localLoad >= firstDeadTemp) {
                    firstDeadTemp = localLoad + this.localVariableTypes.get(localLoad).getSlots();
                }
            }
            undefineLocalVariables(firstDeadTemp, false);
            if (!$assertionsDisabled && !isVariablePartitioningEqual(joinOrigin, firstDeadTemp)) {
                throw new AssertionError();
            }
            mergeVariableTypes(joinOrigin, firstDeadTemp);
        }

        private void mergeVariableTypes(Stack joinOrigin, int toSlot) {
            ListIterator<Type> it1 = this.localVariableTypes.listIterator();
            Iterator<Type> it2 = joinOrigin.localVariableTypes.iterator();
            for (int i2 = 0; i2 < toSlot; i2++) {
                Type thisType = it1.next();
                Type otherType = it2.next();
                if (otherType == Type.UNKNOWN) {
                    it1.set(Type.UNKNOWN);
                } else if (thisType == otherType) {
                    continue;
                } else if (thisType.isObject() && otherType.isObject()) {
                    it1.set(Type.OBJECT);
                } else if (!$assertionsDisabled && thisType != Type.UNKNOWN) {
                    throw new AssertionError();
                }
            }
        }

        void joinFromTry(Stack joinOrigin) {
            this.firstTemp = Math.min(this.firstTemp, joinOrigin.firstTemp);
            if (!$assertionsDisabled && !isVariablePartitioningEqual(joinOrigin, this.firstTemp)) {
                throw new AssertionError();
            }
            mergeVariableTypes(joinOrigin, this.firstTemp);
        }

        private int getFirstDeadLocal(List<Type> types) {
            int i2 = types.size();
            ListIterator<Type> it = types.listIterator(i2);
            while (it.hasPrevious() && it.previous() == Type.UNKNOWN) {
                i2--;
            }
            while (!this.symbolBoundary.get(i2 - 1)) {
                i2++;
            }
            return i2;
        }

        private boolean isStackCompatible(Stack other) {
            if (this.sp != other.sp) {
                return false;
            }
            for (int i2 = 0; i2 < this.sp; i2++) {
                if (!this.data[i2].isEquivalentTo(other.data[i2])) {
                    return false;
                }
            }
            return true;
        }

        private boolean isVariablePartitioningEqual(Stack other, int toSlot) {
            BitSet diff = other.getSymbolBoundaryCopy();
            diff.xor(this.symbolBoundary);
            return diff.previousSetBit(toSlot - 1) == -1;
        }

        void markDeadLocalVariables(int fromSlot, int slotCount) {
            int localCount = this.localVariableTypes.size();
            if (fromSlot >= localCount) {
                return;
            }
            int toSlot = Math.min(fromSlot + slotCount, localCount);
            invalidateLocalLoadsOnStack(fromSlot, toSlot);
            for (int i2 = fromSlot; i2 < toSlot; i2++) {
                this.localVariableTypes.set(i2, Type.UNKNOWN);
            }
        }

        List<Type> getLocalVariableTypesCopy() {
            return (List) ((ArrayList) this.localVariableTypes).clone();
        }

        BitSet getSymbolBoundaryCopy() {
            return (BitSet) this.symbolBoundary.clone();
        }

        List<Type> getWidestLiveLocals(List<Type> lvarTypes) {
            List<Type> widestLiveLocals = new ArrayList<>(lvarTypes);
            boolean keepNextValue = true;
            int size = widestLiveLocals.size();
            int i2 = size - 1;
            while (true) {
                int i3 = i2;
                i2--;
                if (i3 > 0) {
                    if (this.symbolBoundary.get(i2)) {
                        keepNextValue = true;
                    }
                    Type t2 = widestLiveLocals.get(i2);
                    if (t2 != Type.UNKNOWN) {
                        if (keepNextValue) {
                            if (t2 != Type.SLOT_2) {
                                keepNextValue = false;
                            }
                        } else {
                            widestLiveLocals.set(i2, Type.UNKNOWN);
                        }
                    }
                } else {
                    widestLiveLocals.subList(Math.max(getFirstDeadLocal(widestLiveLocals), this.firstTemp), widestLiveLocals.size()).clear();
                    return widestLiveLocals;
                }
            }
        }

        String markSymbolBoundariesInLvarTypesDescriptor(String lvarDescriptor) {
            char[] chars = lvarDescriptor.toCharArray();
            int j2 = 0;
            for (int i2 = 0; i2 < chars.length; i2++) {
                char c2 = chars[i2];
                int nextj = j2 + CodeGeneratorLexicalContext.getTypeForSlotDescriptor(c2).getSlots();
                if (!this.symbolBoundary.get(nextj - 1)) {
                    chars[i2] = Character.toLowerCase(c2);
                }
                j2 = nextj;
            }
            return new String(chars);
        }

        Type pop() {
            if (!$assertionsDisabled && this.sp <= 0) {
                throw new AssertionError();
            }
            Type[] typeArr = this.data;
            int i2 = this.sp - 1;
            this.sp = i2;
            return typeArr[i2];
        }

        /* renamed from: clone, reason: merged with bridge method [inline-methods] */
        public Stack m4620clone() {
            try {
                Stack clone = (Stack) super.clone();
                clone.data = (Type[]) this.data.clone();
                clone.localLoads = (int[]) this.localLoads.clone();
                clone.symbolBoundary = getSymbolBoundaryCopy();
                clone.localVariableTypes = getLocalVariableTypesCopy();
                return clone;
            } catch (CloneNotSupportedException e2) {
                throw new AssertionError("", e2);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public Stack cloneWithEmptyStack() {
            Stack stack = m4620clone();
            stack.sp = 0;
            return stack;
        }

        int getTopLocalLoad() {
            return this.localLoads[this.sp - 1];
        }

        void markLocalLoad(int slot) {
            this.localLoads[this.sp - 1] = slot;
        }

        void onLocalStore(Type type, int slot, boolean onlySymbolLiveValue) {
            if (onlySymbolLiveValue) {
                int fromSlot = slot == 0 ? 0 : this.symbolBoundary.previousSetBit(slot - 1) + 1;
                int toSlot = this.symbolBoundary.nextSetBit(slot) + 1;
                for (int i2 = fromSlot; i2 < toSlot; i2++) {
                    this.localVariableTypes.set(i2, Type.UNKNOWN);
                }
                invalidateLocalLoadsOnStack(fromSlot, toSlot);
            } else {
                invalidateLocalLoadsOnStack(slot, slot + type.getSlots());
            }
            this.localVariableTypes.set(slot, type);
            if (type.isCategory2()) {
                this.localVariableTypes.set(slot + 1, Type.SLOT_2);
            }
        }

        private void invalidateLocalLoadsOnStack(int fromSlot, int toSlot) {
            for (int i2 = 0; i2 < this.sp; i2++) {
                int localLoad = this.localLoads[i2];
                if (localLoad >= fromSlot && localLoad < toSlot) {
                    this.localLoads[i2] = -1;
                }
            }
        }

        void defineBlockLocalVariable(int fromSlot, int toSlot) {
            defineLocalVariable(fromSlot, toSlot);
            if (!$assertionsDisabled && this.firstTemp >= toSlot) {
                throw new AssertionError();
            }
            this.firstTemp = toSlot;
        }

        int defineTemporaryLocalVariable(int width) {
            int fromSlot = getUsedSlotsWithLiveTemporaries();
            defineLocalVariable(fromSlot, fromSlot + width);
            return fromSlot;
        }

        void defineTemporaryLocalVariable(int fromSlot, int toSlot) {
            defineLocalVariable(fromSlot, toSlot);
        }

        private void defineLocalVariable(int fromSlot, int toSlot) {
            if (!$assertionsDisabled && hasLoadsOnStack(fromSlot, toSlot)) {
                throw new AssertionError();
            }
            if (!$assertionsDisabled && fromSlot >= toSlot) {
                throw new AssertionError();
            }
            this.symbolBoundary.clear(fromSlot, toSlot - 1);
            this.symbolBoundary.set(toSlot - 1);
            int lastExisting = Math.min(toSlot, this.localVariableTypes.size());
            for (int i2 = fromSlot; i2 < lastExisting; i2++) {
                this.localVariableTypes.set(i2, Type.UNKNOWN);
            }
            for (int i3 = lastExisting; i3 < toSlot; i3++) {
                this.localVariableTypes.add(i3, Type.UNKNOWN);
            }
        }

        void undefineLocalVariables(int fromSlot, boolean canTruncateSymbol) {
            int lvarCount = this.localVariableTypes.size();
            if (!$assertionsDisabled && lvarCount != this.symbolBoundary.length()) {
                throw new AssertionError();
            }
            if (!$assertionsDisabled && hasLoadsOnStack(fromSlot, lvarCount)) {
                throw new AssertionError();
            }
            if (canTruncateSymbol) {
                if (fromSlot > 0) {
                    this.symbolBoundary.set(fromSlot - 1);
                }
            } else if (!$assertionsDisabled && fromSlot != 0 && !this.symbolBoundary.get(fromSlot - 1)) {
                throw new AssertionError();
            }
            if (fromSlot < lvarCount) {
                this.symbolBoundary.clear(fromSlot, lvarCount);
                this.localVariableTypes.subList(fromSlot, lvarCount).clear();
            }
            this.firstTemp = Math.min(fromSlot, this.firstTemp);
            if (!$assertionsDisabled && this.symbolBoundary.length() != this.localVariableTypes.size()) {
                throw new AssertionError();
            }
            if (!$assertionsDisabled && this.symbolBoundary.length() != fromSlot) {
                throw new AssertionError();
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void markAsOptimisticCatchHandler(int liveLocalCount) {
            undefineLocalVariables(liveLocalCount, true);
            this.firstTemp = liveLocalCount;
            this.localVariableTypes.subList(this.firstTemp, this.localVariableTypes.size()).clear();
            if (!$assertionsDisabled && this.symbolBoundary.length() != this.firstTemp) {
                throw new AssertionError();
            }
            ListIterator<Type> it = this.localVariableTypes.listIterator();
            while (it.hasNext()) {
                Type type = it.next();
                if (type == Type.BOOLEAN) {
                    it.set(Type.INT);
                } else if (type.isObject() && type != Type.OBJECT) {
                    it.set(Type.OBJECT);
                }
            }
        }

        boolean hasLoadsOnStack(int fromSlot, int toSlot) {
            for (int i2 = 0; i2 < this.sp; i2++) {
                int load = this.localLoads[i2];
                if (load >= fromSlot && load < toSlot) {
                    return true;
                }
            }
            return false;
        }

        public String toString() {
            return "stack=" + Arrays.toString(Arrays.copyOf(this.data, this.sp)) + ", symbolBoundaries=" + String.valueOf(this.symbolBoundary) + ", firstTemp=" + this.firstTemp + ", localTypes=" + String.valueOf(this.localVariableTypes);
        }
    }

    public Label(String name) {
        this.name = name;
        int i2 = nextId;
        nextId = i2 + 1;
        this.id = i2;
    }

    public Label(Label label) {
        this.name = label.name;
        this.id = label.id;
    }

    jdk.internal.org.objectweb.asm.Label getLabel() {
        if (this.label == null) {
            this.label = new jdk.internal.org.objectweb.asm.Label();
        }
        return this.label;
    }

    Stack getStack() {
        return this.stack;
    }

    void joinFrom(Stack joinOrigin) {
        this.reachable = true;
        if (this.stack == null) {
            this.stack = joinOrigin.m4620clone();
        } else {
            this.stack.joinFrom(joinOrigin, this.breakTarget);
        }
    }

    void joinFromTry(Stack joinOrigin, boolean isOptimismHandler) {
        this.reachable = true;
        if (this.stack == null) {
            if (!isOptimismHandler) {
                this.stack = joinOrigin.cloneWithEmptyStack();
                this.stack.undefineLocalVariables(this.stack.firstTemp, false);
                return;
            }
            return;
        }
        if (!$assertionsDisabled && isOptimismHandler) {
            throw new AssertionError();
        }
        this.stack.joinFromTry(joinOrigin);
    }

    void markAsBreakTarget() {
        this.breakTarget = true;
    }

    boolean isBreakTarget() {
        return this.breakTarget;
    }

    void onCatch() {
        if (this.stack != null) {
            this.stack = this.stack.cloneWithEmptyStack();
        }
    }

    void markAsOptimisticCatchHandler(Stack currentStack, int liveLocalCount) {
        this.stack = currentStack.cloneWithEmptyStack();
        this.stack.markAsOptimisticCatchHandler(liveLocalCount);
    }

    void markAsOptimisticContinuationHandlerFor(Label afterConsumeStackLabel) {
        this.stack = afterConsumeStackLabel.stack.cloneWithEmptyStack();
    }

    boolean isReachable() {
        return this.reachable;
    }

    boolean isAfter(Label other) {
        return this.label.getOffset() > other.label.getOffset();
    }

    public String toString() {
        if (this.str == null) {
            this.str = this.name + '_' + this.id;
        }
        return this.str;
    }
}
