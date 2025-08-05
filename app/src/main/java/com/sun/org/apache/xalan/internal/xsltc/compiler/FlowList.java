package com.sun.org.apache.xalan.internal.xsltc.compiler;

import com.sun.org.apache.bcel.internal.generic.BranchHandle;
import com.sun.org.apache.bcel.internal.generic.InstructionHandle;
import com.sun.org.apache.bcel.internal.generic.InstructionList;
import java.util.Iterator;
import java.util.Vector;

/* loaded from: rt.jar:com/sun/org/apache/xalan/internal/xsltc/compiler/FlowList.class */
public final class FlowList {
    private Vector _elements;

    public FlowList() {
        this._elements = null;
    }

    public FlowList(InstructionHandle bh2) {
        this._elements = new Vector();
        this._elements.addElement(bh2);
    }

    public FlowList(FlowList list) {
        this._elements = list._elements;
    }

    public FlowList add(InstructionHandle bh2) {
        if (this._elements == null) {
            this._elements = new Vector();
        }
        this._elements.addElement(bh2);
        return this;
    }

    public FlowList append(FlowList right) {
        if (this._elements == null) {
            this._elements = right._elements;
        } else {
            Vector temp = right._elements;
            if (temp != null) {
                int n2 = temp.size();
                for (int i2 = 0; i2 < n2; i2++) {
                    this._elements.addElement(temp.elementAt(i2));
                }
            }
        }
        return this;
    }

    public void backPatch(InstructionHandle target) {
        if (this._elements != null) {
            int n2 = this._elements.size();
            for (int i2 = 0; i2 < n2; i2++) {
                BranchHandle bh2 = (BranchHandle) this._elements.elementAt(i2);
                bh2.setTarget(target);
            }
            this._elements.clear();
        }
    }

    public FlowList copyAndRedirect(InstructionList oldList, InstructionList newList) {
        FlowList result = new FlowList();
        if (this._elements == null) {
            return result;
        }
        int n2 = this._elements.size();
        Iterator oldIter = oldList.iterator();
        Iterator newIter = newList.iterator();
        while (oldIter.hasNext()) {
            InstructionHandle oldIh = (InstructionHandle) oldIter.next();
            InstructionHandle newIh = (InstructionHandle) newIter.next();
            for (int i2 = 0; i2 < n2; i2++) {
                if (this._elements.elementAt(i2) == oldIh) {
                    result.add(newIh);
                }
            }
        }
        return result;
    }
}
