package com.sun.org.apache.xpath.internal;

import com.sun.org.apache.xalan.internal.res.XSLMessages;
import com.sun.org.apache.xml.internal.utils.QName;
import com.sun.org.apache.xpath.internal.objects.XObject;
import javax.xml.transform.TransformerException;

/* loaded from: rt.jar:com/sun/org/apache/xpath/internal/VariableStack.class */
public class VariableStack implements Cloneable {
    public static final int CLEARLIMITATION = 1024;
    int _frameTop;
    private int _currentFrameBottom;
    int _linksTop;
    private static XObject[] m_nulls = new XObject[1024];
    XObject[] _stackFrames = new XObject[8192];
    int[] _links = new int[4096];

    public VariableStack() {
        reset();
    }

    public synchronized Object clone() throws CloneNotSupportedException {
        VariableStack vs = (VariableStack) super.clone();
        vs._stackFrames = (XObject[]) this._stackFrames.clone();
        vs._links = (int[]) this._links.clone();
        return vs;
    }

    public XObject elementAt(int i2) {
        return this._stackFrames[i2];
    }

    public int size() {
        return this._frameTop;
    }

    public void reset() {
        this._frameTop = 0;
        this._linksTop = 0;
        int[] iArr = this._links;
        int i2 = this._linksTop;
        this._linksTop = i2 + 1;
        iArr[i2] = 0;
        this._stackFrames = new XObject[this._stackFrames.length];
    }

    public void setStackFrame(int sf) {
        this._currentFrameBottom = sf;
    }

    public int getStackFrame() {
        return this._currentFrameBottom;
    }

    public int link(int size) {
        this._currentFrameBottom = this._frameTop;
        this._frameTop += size;
        if (this._frameTop >= this._stackFrames.length) {
            XObject[] newsf = new XObject[this._stackFrames.length + 4096 + size];
            System.arraycopy(this._stackFrames, 0, newsf, 0, this._stackFrames.length);
            this._stackFrames = newsf;
        }
        if (this._linksTop + 1 >= this._links.length) {
            int[] newlinks = new int[this._links.length + 2048];
            System.arraycopy(this._links, 0, newlinks, 0, this._links.length);
            this._links = newlinks;
        }
        int[] iArr = this._links;
        int i2 = this._linksTop;
        this._linksTop = i2 + 1;
        iArr[i2] = this._currentFrameBottom;
        return this._currentFrameBottom;
    }

    public void unlink() {
        int[] iArr = this._links;
        int i2 = this._linksTop - 1;
        this._linksTop = i2;
        this._frameTop = iArr[i2];
        this._currentFrameBottom = this._links[this._linksTop - 1];
    }

    public void unlink(int currentFrame) {
        int[] iArr = this._links;
        int i2 = this._linksTop - 1;
        this._linksTop = i2;
        this._frameTop = iArr[i2];
        this._currentFrameBottom = currentFrame;
    }

    public void setLocalVariable(int index, XObject val) {
        this._stackFrames[index + this._currentFrameBottom] = val;
    }

    public void setLocalVariable(int index, XObject val, int stackFrame) {
        this._stackFrames[index + stackFrame] = val;
    }

    public XObject getLocalVariable(XPathContext xctxt, int index) throws TransformerException {
        int index2 = index + this._currentFrameBottom;
        XObject val = this._stackFrames[index2];
        if (null == val) {
            throw new TransformerException(XSLMessages.createXPATHMessage("ER_VARIABLE_ACCESSED_BEFORE_BIND", null), xctxt.getSAXLocator());
        }
        if (val.getType() == 600) {
            XObject[] xObjectArr = this._stackFrames;
            XObject xObjectExecute = val.execute(xctxt);
            xObjectArr[index2] = xObjectExecute;
            return xObjectExecute;
        }
        return val;
    }

    public XObject getLocalVariable(int index, int frame) throws TransformerException {
        XObject val = this._stackFrames[index + frame];
        return val;
    }

    public XObject getLocalVariable(XPathContext xctxt, int index, boolean destructiveOK) throws TransformerException {
        int index2 = index + this._currentFrameBottom;
        XObject val = this._stackFrames[index2];
        if (null == val) {
            throw new TransformerException(XSLMessages.createXPATHMessage("ER_VARIABLE_ACCESSED_BEFORE_BIND", null), xctxt.getSAXLocator());
        }
        if (val.getType() != 600) {
            return destructiveOK ? val : val.getFresh();
        }
        XObject[] xObjectArr = this._stackFrames;
        XObject xObjectExecute = val.execute(xctxt);
        xObjectArr[index2] = xObjectExecute;
        return xObjectExecute;
    }

    public boolean isLocalSet(int index) throws TransformerException {
        return this._stackFrames[index + this._currentFrameBottom] != null;
    }

    public void clearLocalSlots(int start, int len) {
        System.arraycopy(m_nulls, 0, this._stackFrames, start + this._currentFrameBottom, len);
    }

    public void setGlobalVariable(int index, XObject val) {
        this._stackFrames[index] = val;
    }

    public XObject getGlobalVariable(XPathContext xctxt, int index) throws TransformerException {
        XObject val = this._stackFrames[index];
        if (val.getType() == 600) {
            XObject[] xObjectArr = this._stackFrames;
            XObject xObjectExecute = val.execute(xctxt);
            xObjectArr[index] = xObjectExecute;
            return xObjectExecute;
        }
        return val;
    }

    public XObject getGlobalVariable(XPathContext xctxt, int index, boolean destructiveOK) throws TransformerException {
        XObject val = this._stackFrames[index];
        if (val.getType() != 600) {
            return destructiveOK ? val : val.getFresh();
        }
        XObject[] xObjectArr = this._stackFrames;
        XObject xObjectExecute = val.execute(xctxt);
        xObjectArr[index] = xObjectExecute;
        return xObjectExecute;
    }

    public XObject getVariableOrParam(XPathContext xctxt, QName qname) throws TransformerException {
        throw new TransformerException(XSLMessages.createXPATHMessage("ER_VAR_NOT_RESOLVABLE", new Object[]{qname.toString()}));
    }
}
