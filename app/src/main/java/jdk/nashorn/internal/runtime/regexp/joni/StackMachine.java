package jdk.nashorn.internal.runtime.regexp.joni;

import java.lang.ref.WeakReference;
import jdk.nashorn.internal.runtime.regexp.joni.constants.StackType;

/* loaded from: nashorn.jar:jdk/nashorn/internal/runtime/regexp/joni/StackMachine.class */
abstract class StackMachine extends Matcher implements StackType {
    protected static final int INVALID_INDEX = -1;
    protected StackEntry[] stack;
    protected int stk;
    protected final int[] repeatStk;
    protected final int memStartStk;
    protected final int memEndStk;
    static final ThreadLocal<WeakReference<StackEntry[]>> stacks = new ThreadLocal<WeakReference<StackEntry[]>>() { // from class: jdk.nashorn.internal.runtime.regexp.joni.StackMachine.1
        /* JADX INFO: Access modifiers changed from: protected */
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.lang.ThreadLocal
        public WeakReference<StackEntry[]> initialValue() {
            return new WeakReference<>(StackMachine.allocateStack());
        }
    };

    protected StackMachine(Regex regex, char[] chars, int p2, int end) {
        super(regex, chars, p2, end);
        this.stack = regex.stackNeeded ? fetchStack() : null;
        int n2 = regex.numRepeat + (regex.numMem << 1);
        this.repeatStk = n2 > 0 ? new int[n2] : null;
        this.memStartStk = regex.numRepeat - 1;
        this.memEndStk = this.memStartStk + regex.numMem;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static StackEntry[] allocateStack() {
        StackEntry[] stack = new StackEntry[64];
        stack[0] = new StackEntry();
        return stack;
    }

    private void doubleStack() {
        StackEntry[] newStack = new StackEntry[this.stack.length << 1];
        System.arraycopy(this.stack, 0, newStack, 0, this.stack.length);
        this.stack = newStack;
    }

    private static StackEntry[] fetchStack() {
        WeakReference<StackEntry[]> ref = stacks.get();
        StackEntry[] stack = ref.get();
        if (stack == null) {
            StackEntry[] stackEntryArrAllocateStack = allocateStack();
            stack = stackEntryArrAllocateStack;
            WeakReference<StackEntry[]> ref2 = new WeakReference<>(stackEntryArrAllocateStack);
            stacks.set(ref2);
        }
        return stack;
    }

    protected final void init() {
        if (this.stack != null) {
            pushEnsured(1, this.regex.codeLength - 1);
        }
        if (this.repeatStk != null) {
            for (int i2 = 1; i2 <= this.regex.numMem; i2++) {
                int[] iArr = this.repeatStk;
                int i3 = i2 + this.memStartStk;
                this.repeatStk[i2 + this.memEndStk] = -1;
                iArr[i3] = -1;
            }
        }
    }

    protected final StackEntry ensure1() {
        if (this.stk >= this.stack.length) {
            doubleStack();
        }
        StackEntry e2 = this.stack[this.stk];
        if (e2 == null) {
            StackEntry[] stackEntryArr = this.stack;
            int i2 = this.stk;
            StackEntry stackEntry = new StackEntry();
            e2 = stackEntry;
            stackEntryArr[i2] = stackEntry;
        }
        return e2;
    }

    protected final void pushType(int type) {
        ensure1().type = type;
        this.stk++;
    }

    private void push(int type, int pat, int s2, int prev) {
        StackEntry e2 = ensure1();
        e2.type = type;
        e2.setStatePCode(pat);
        e2.setStatePStr(s2);
        e2.setStatePStrPrev(prev);
        this.stk++;
    }

    protected final void pushEnsured(int type, int pat) {
        StackEntry e2 = this.stack[this.stk];
        e2.type = type;
        e2.setStatePCode(pat);
        this.stk++;
    }

    protected final void pushAlt(int pat, int s2, int prev) {
        push(1, pat, s2, prev);
    }

    protected final void pushPos(int s2, int prev) {
        push(1280, -1, s2, prev);
    }

    protected final void pushPosNot(int pat, int s2, int prev) {
        push(3, pat, s2, prev);
    }

    protected final void pushStopBT() {
        pushType(1536);
    }

    protected final void pushLookBehindNot(int pat, int s2, int sprev) {
        push(2, pat, s2, sprev);
    }

    protected final void pushRepeat(int id, int pat) {
        StackEntry e2 = ensure1();
        e2.type = 1792;
        e2.setRepeatNum(id);
        e2.setRepeatPCode(pat);
        e2.setRepeatCount(0);
        this.stk++;
    }

    protected final void pushRepeatInc(int sindex) {
        StackEntry e2 = ensure1();
        e2.type = 768;
        e2.setSi(sindex);
        this.stk++;
    }

    protected final void pushMemStart(int mnum, int s2) {
        StackEntry e2 = ensure1();
        e2.type = 256;
        e2.setMemNum(mnum);
        e2.setMemPstr(s2);
        e2.setMemStart(this.repeatStk[this.memStartStk + mnum]);
        e2.setMemEnd(this.repeatStk[this.memEndStk + mnum]);
        this.repeatStk[this.memStartStk + mnum] = this.stk;
        this.repeatStk[this.memEndStk + mnum] = -1;
        this.stk++;
    }

    protected final void pushMemEnd(int mnum, int s2) {
        StackEntry e2 = ensure1();
        e2.type = StackType.MEM_END;
        e2.setMemNum(mnum);
        e2.setMemPstr(s2);
        e2.setMemStart(this.repeatStk[this.memStartStk + mnum]);
        e2.setMemEnd(this.repeatStk[this.memEndStk + mnum]);
        this.repeatStk[this.memEndStk + mnum] = this.stk;
        this.stk++;
    }

    protected final void pushMemEndMark(int mnum) {
        StackEntry e2 = ensure1();
        e2.type = StackType.MEM_END_MARK;
        e2.setMemNum(mnum);
        this.stk++;
    }

    protected final int getMemStart(int mnum) {
        int level = 0;
        int stkp = this.stk;
        while (stkp > 0) {
            stkp--;
            StackEntry e2 = this.stack[stkp];
            if ((e2.type & 32768) != 0 && e2.getMemNum() == mnum) {
                level++;
            } else if (e2.type == 256 && e2.getMemNum() == mnum) {
                if (level == 0) {
                    break;
                }
                level--;
            }
        }
        return stkp;
    }

    protected final void pushNullCheckStart(int cnum, int s2) {
        StackEntry e2 = ensure1();
        e2.type = StackType.NULL_CHECK_START;
        e2.setNullCheckNum(cnum);
        e2.setNullCheckPStr(s2);
        this.stk++;
    }

    protected final void pushNullCheckEnd(int cnum) {
        StackEntry e2 = ensure1();
        e2.type = StackType.NULL_CHECK_END;
        e2.setNullCheckNum(cnum);
        this.stk++;
    }

    protected final void popOne() {
        this.stk--;
    }

    protected final StackEntry pop() {
        switch (this.regex.stackPopLevel) {
            case 0:
                return popFree();
            case 1:
                return popMemStart();
            default:
                return popDefault();
        }
    }

    private StackEntry popFree() {
        StackEntry e2;
        do {
            StackEntry[] stackEntryArr = this.stack;
            int i2 = this.stk - 1;
            this.stk = i2;
            e2 = stackEntryArr[i2];
        } while ((e2.type & 255) == 0);
        return e2;
    }

    private StackEntry popMemStart() {
        while (true) {
            StackEntry[] stackEntryArr = this.stack;
            int i2 = this.stk - 1;
            this.stk = i2;
            StackEntry e2 = stackEntryArr[i2];
            if ((e2.type & 255) != 0) {
                return e2;
            }
            if (e2.type == 256) {
                this.repeatStk[this.memStartStk + e2.getMemNum()] = e2.getMemStart();
                this.repeatStk[this.memEndStk + e2.getMemNum()] = e2.getMemEnd();
            }
        }
    }

    private StackEntry popDefault() {
        while (true) {
            StackEntry[] stackEntryArr = this.stack;
            int i2 = this.stk - 1;
            this.stk = i2;
            StackEntry e2 = stackEntryArr[i2];
            if ((e2.type & 255) != 0) {
                return e2;
            }
            if (e2.type == 256) {
                this.repeatStk[this.memStartStk + e2.getMemNum()] = e2.getMemStart();
                this.repeatStk[this.memEndStk + e2.getMemNum()] = e2.getMemEnd();
            } else if (e2.type == 768) {
                this.stack[e2.getSi()].decreaseRepeatCount();
            } else if (e2.type == 33280) {
                this.repeatStk[this.memStartStk + e2.getMemNum()] = e2.getMemStart();
                this.repeatStk[this.memEndStk + e2.getMemNum()] = e2.getMemEnd();
            }
        }
    }

    protected final void popTilPosNot() {
        while (true) {
            this.stk--;
            StackEntry e2 = this.stack[this.stk];
            if (e2.type != 3) {
                if (e2.type == 256) {
                    this.repeatStk[this.memStartStk + e2.getMemNum()] = e2.getMemStart();
                    this.repeatStk[this.memEndStk + e2.getMemNum()] = e2.getMemStart();
                } else if (e2.type == 768) {
                    this.stack[e2.getSi()].decreaseRepeatCount();
                } else if (e2.type == 33280) {
                    this.repeatStk[this.memStartStk + e2.getMemNum()] = e2.getMemStart();
                    this.repeatStk[this.memEndStk + e2.getMemNum()] = e2.getMemStart();
                }
            } else {
                return;
            }
        }
    }

    protected final void popTilLookBehindNot() {
        while (true) {
            this.stk--;
            StackEntry e2 = this.stack[this.stk];
            if (e2.type != 2) {
                if (e2.type == 256) {
                    this.repeatStk[this.memStartStk + e2.getMemNum()] = e2.getMemStart();
                    this.repeatStk[this.memEndStk + e2.getMemNum()] = e2.getMemEnd();
                } else if (e2.type == 768) {
                    this.stack[e2.getSi()].decreaseRepeatCount();
                } else if (e2.type == 33280) {
                    this.repeatStk[this.memStartStk + e2.getMemNum()] = e2.getMemStart();
                    this.repeatStk[this.memEndStk + e2.getMemNum()] = e2.getMemEnd();
                }
            } else {
                return;
            }
        }
    }

    protected final int posEnd() {
        int k2 = this.stk;
        while (true) {
            k2--;
            StackEntry e2 = this.stack[k2];
            if ((e2.type & StackType.MASK_TO_VOID_TARGET) != 0) {
                e2.type = StackType.VOID;
            } else if (e2.type == 1280) {
                e2.type = StackType.VOID;
                return k2;
            }
        }
    }

    protected final void stopBtEnd() {
        int k2 = this.stk;
        while (true) {
            k2--;
            StackEntry e2 = this.stack[k2];
            if ((e2.type & StackType.MASK_TO_VOID_TARGET) != 0) {
                e2.type = StackType.VOID;
            } else if (e2.type == 1536) {
                e2.type = StackType.VOID;
                return;
            }
        }
    }

    protected final int nullCheck(int id, int s2) {
        StackEntry e2;
        int k2 = this.stk;
        while (true) {
            k2--;
            e2 = this.stack[k2];
            if (e2.type == 12288 && e2.getNullCheckNum() == id) {
                break;
            }
        }
        return e2.getNullCheckPStr() == s2 ? 1 : 0;
    }

    protected final int nullCheckMemSt(int id, int s2) {
        return -nullCheck(id, s2);
    }

    protected final int getRepeat(int id) {
        int level = 0;
        int k2 = this.stk;
        while (true) {
            k2--;
            StackEntry e2 = this.stack[k2];
            if (e2.type == 1792) {
                if (level == 0 && e2.getRepeatNum() == id) {
                    return k2;
                }
            } else if (e2.type == 2048) {
                level--;
            } else if (e2.type == 2304) {
                level++;
            }
        }
    }

    protected final int sreturn() {
        int level = 0;
        int k2 = this.stk;
        while (true) {
            k2--;
            StackEntry e2 = this.stack[k2];
            if (e2.type == 2048) {
                if (level == 0) {
                    return e2.getCallFrameRetAddr();
                }
                level--;
            } else if (e2.type == 2304) {
                level++;
            }
        }
    }
}
