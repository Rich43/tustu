package com.sun.org.apache.xml.internal.dtm.ref;

import com.sun.org.apache.xml.internal.res.XMLMessages;
import java.util.BitSet;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/dtm/ref/CoroutineManager.class */
public class CoroutineManager {
    static final int m_unreasonableId = 1024;
    static final int NOBODY = -1;
    static final int ANYBODY = -1;
    BitSet m_activeIDs = new BitSet();
    Object m_yield = null;
    int m_nextCoroutine = -1;

    public synchronized int co_joinCoroutineSet(int coroutineID) {
        if (coroutineID >= 0) {
            if (coroutineID >= 1024 || this.m_activeIDs.get(coroutineID)) {
                return -1;
            }
        } else {
            coroutineID = 0;
            while (coroutineID < 1024 && this.m_activeIDs.get(coroutineID)) {
                coroutineID++;
            }
            if (coroutineID >= 1024) {
                return -1;
            }
        }
        this.m_activeIDs.set(coroutineID);
        return coroutineID;
    }

    public synchronized Object co_entry_pause(int thisCoroutine) throws NoSuchMethodException {
        if (!this.m_activeIDs.get(thisCoroutine)) {
            throw new NoSuchMethodException();
        }
        while (this.m_nextCoroutine != thisCoroutine) {
            try {
                wait();
            } catch (InterruptedException e2) {
            }
        }
        return this.m_yield;
    }

    public synchronized Object co_resume(Object arg_object, int thisCoroutine, int toCoroutine) throws NoSuchMethodException {
        if (!this.m_activeIDs.get(toCoroutine)) {
            throw new NoSuchMethodException(XMLMessages.createXMLMessage("ER_COROUTINE_NOT_AVAIL", new Object[]{Integer.toString(toCoroutine)}));
        }
        this.m_yield = arg_object;
        this.m_nextCoroutine = toCoroutine;
        notify();
        while (true) {
            if (this.m_nextCoroutine == thisCoroutine && this.m_nextCoroutine != -1 && this.m_nextCoroutine != -1) {
                break;
            }
            try {
                wait();
            } catch (InterruptedException e2) {
            }
        }
        if (this.m_nextCoroutine == -1) {
            co_exit(thisCoroutine);
            throw new NoSuchMethodException(XMLMessages.createXMLMessage("ER_COROUTINE_CO_EXIT", null));
        }
        return this.m_yield;
    }

    public synchronized void co_exit(int thisCoroutine) {
        this.m_activeIDs.clear(thisCoroutine);
        this.m_nextCoroutine = -1;
        notify();
    }

    public synchronized void co_exit_to(Object arg_object, int thisCoroutine, int toCoroutine) throws NoSuchMethodException {
        if (!this.m_activeIDs.get(toCoroutine)) {
            throw new NoSuchMethodException(XMLMessages.createXMLMessage("ER_COROUTINE_NOT_AVAIL", new Object[]{Integer.toString(toCoroutine)}));
        }
        this.m_yield = arg_object;
        this.m_nextCoroutine = toCoroutine;
        this.m_activeIDs.clear(thisCoroutine);
        notify();
    }
}
