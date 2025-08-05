package com.sun.org.apache.xpath.internal.compiler;

import com.sun.org.apache.xalan.internal.res.XSLMessages;
import com.sun.org.apache.xml.internal.utils.ObjectVector;
import javax.xml.transform.TransformerException;

/* loaded from: rt.jar:com/sun/org/apache/xpath/internal/compiler/OpMap.class */
public class OpMap {
    protected String m_currentPattern;
    static final int MAXTOKENQUEUESIZE = 500;
    static final int BLOCKTOKENQUEUESIZE = 500;
    ObjectVector m_tokenQueue = new ObjectVector(500, 500);
    OpMapVector m_opMap = null;
    public static final int MAPINDEX_LENGTH = 1;

    public String toString() {
        return this.m_currentPattern;
    }

    public String getPatternString() {
        return this.m_currentPattern;
    }

    public ObjectVector getTokenQueue() {
        return this.m_tokenQueue;
    }

    public Object getToken(int pos) {
        return this.m_tokenQueue.elementAt(pos);
    }

    public int getTokenQueueSize() {
        return this.m_tokenQueue.size();
    }

    public OpMapVector getOpMap() {
        return this.m_opMap;
    }

    void shrink() {
        int n2 = this.m_opMap.elementAt(1);
        this.m_opMap.setToSize(n2 + 4);
        this.m_opMap.setElementAt(0, n2);
        this.m_opMap.setElementAt(0, n2 + 1);
        this.m_opMap.setElementAt(0, n2 + 2);
        int n3 = this.m_tokenQueue.size();
        this.m_tokenQueue.setToSize(n3 + 4);
        this.m_tokenQueue.setElementAt(null, n3);
        this.m_tokenQueue.setElementAt(null, n3 + 1);
        this.m_tokenQueue.setElementAt(null, n3 + 2);
    }

    public int getOp(int opPos) {
        return this.m_opMap.elementAt(opPos);
    }

    public void setOp(int opPos, int value) {
        this.m_opMap.setElementAt(value, opPos);
    }

    public int getNextOpPos(int opPos) {
        return opPos + this.m_opMap.elementAt(opPos + 1);
    }

    public int getNextStepPos(int opPos) {
        int newOpPos;
        int stepType = getOp(opPos);
        if (stepType >= 37 && stepType <= 53) {
            return getNextOpPos(opPos);
        }
        if (stepType >= 22 && stepType <= 25) {
            int nextOpPos = getNextOpPos(opPos);
            while (true) {
                newOpPos = nextOpPos;
                if (29 != getOp(newOpPos)) {
                    break;
                }
                nextOpPos = getNextOpPos(newOpPos);
            }
            int stepType2 = getOp(newOpPos);
            if (stepType2 < 37 || stepType2 > 53) {
                return -1;
            }
            return newOpPos;
        }
        throw new RuntimeException(XSLMessages.createXPATHMessage("ER_UNKNOWN_STEP", new Object[]{String.valueOf(stepType)}));
    }

    public static int getNextOpPos(int[] opMap, int opPos) {
        return opPos + opMap[opPos + 1];
    }

    public int getFirstPredicateOpPos(int opPos) throws TransformerException {
        int stepType = this.m_opMap.elementAt(opPos);
        if (stepType >= 37 && stepType <= 53) {
            return opPos + this.m_opMap.elementAt(opPos + 2);
        }
        if (stepType >= 22 && stepType <= 25) {
            return opPos + this.m_opMap.elementAt(opPos + 1);
        }
        if (-2 == stepType) {
            return -2;
        }
        error("ER_UNKNOWN_OPCODE", new Object[]{String.valueOf(stepType)});
        return -1;
    }

    public void error(String msg, Object[] args) throws TransformerException {
        String fmsg = XSLMessages.createXPATHMessage(msg, args);
        throw new TransformerException(fmsg);
    }

    public static int getFirstChildPos(int opPos) {
        return opPos + 2;
    }

    public int getArgLength(int opPos) {
        return this.m_opMap.elementAt(opPos + 1);
    }

    public int getArgLengthOfStep(int opPos) {
        return this.m_opMap.elementAt((opPos + 1) + 1) - 3;
    }

    public static int getFirstChildPosOfStep(int opPos) {
        return opPos + 3;
    }

    public int getStepTestType(int opPosOfStep) {
        return this.m_opMap.elementAt(opPosOfStep + 3);
    }

    public String getStepNS(int opPosOfStep) {
        int argLenOfStep = getArgLengthOfStep(opPosOfStep);
        if (argLenOfStep == 3) {
            int index = this.m_opMap.elementAt(opPosOfStep + 4);
            if (index >= 0) {
                return (String) this.m_tokenQueue.elementAt(index);
            }
            if (-3 == index) {
                return "*";
            }
            return null;
        }
        return null;
    }

    public String getStepLocalName(int opPosOfStep) {
        int index;
        int argLenOfStep = getArgLengthOfStep(opPosOfStep);
        switch (argLenOfStep) {
            case 0:
                index = -2;
                break;
            case 1:
                index = -3;
                break;
            case 2:
                index = this.m_opMap.elementAt(opPosOfStep + 4);
                break;
            case 3:
                index = this.m_opMap.elementAt(opPosOfStep + 5);
                break;
            default:
                index = -2;
                break;
        }
        if (index >= 0) {
            return this.m_tokenQueue.elementAt(index).toString();
        }
        if (-3 == index) {
            return "*";
        }
        return null;
    }
}
