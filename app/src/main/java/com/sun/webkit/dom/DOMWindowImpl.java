package com.sun.webkit.dom;

import com.sun.webkit.Disposer;
import netscape.javascript.JSException;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.css.CSSStyleDeclaration;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.views.AbstractView;
import org.w3c.dom.views.DocumentView;

/* loaded from: jfxrt.jar:com/sun/webkit/dom/DOMWindowImpl.class */
public class DOMWindowImpl extends JSObject implements AbstractView, EventTarget {
    private static SelfDisposer[] hashTable = new SelfDisposer[64];
    private static int hashCount;

    /* JADX INFO: Access modifiers changed from: private */
    public static native void dispose(long j2);

    static native long getFrameElementImpl(long j2);

    static native boolean getOffscreenBufferingImpl(long j2);

    static native int getOuterHeightImpl(long j2);

    static native int getOuterWidthImpl(long j2);

    static native int getInnerHeightImpl(long j2);

    static native int getInnerWidthImpl(long j2);

    static native int getScreenXImpl(long j2);

    static native int getScreenYImpl(long j2);

    static native int getScreenLeftImpl(long j2);

    static native int getScreenTopImpl(long j2);

    static native int getScrollXImpl(long j2);

    static native int getScrollYImpl(long j2);

    static native int getPageXOffsetImpl(long j2);

    static native int getPageYOffsetImpl(long j2);

    static native boolean getClosedImpl(long j2);

    static native int getLengthImpl(long j2);

    static native String getNameImpl(long j2);

    static native void setNameImpl(long j2, String str);

    static native String getStatusImpl(long j2);

    static native void setStatusImpl(long j2, String str);

    static native String getDefaultStatusImpl(long j2);

    static native void setDefaultStatusImpl(long j2, String str);

    static native long getSelfImpl(long j2);

    static native long getWindowImpl(long j2);

    static native long getFramesImpl(long j2);

    static native long getOpenerImpl(long j2);

    static native long getParentImpl(long j2);

    static native long getTopImpl(long j2);

    static native long getDocumentExImpl(long j2);

    static native double getDevicePixelRatioImpl(long j2);

    static native long getOnanimationendImpl(long j2);

    static native void setOnanimationendImpl(long j2, long j3);

    static native long getOnanimationiterationImpl(long j2);

    static native void setOnanimationiterationImpl(long j2, long j3);

    static native long getOnanimationstartImpl(long j2);

    static native void setOnanimationstartImpl(long j2, long j3);

    static native long getOntransitionendImpl(long j2);

    static native void setOntransitionendImpl(long j2, long j3);

    static native long getOnwebkitanimationendImpl(long j2);

    static native void setOnwebkitanimationendImpl(long j2, long j3);

    static native long getOnwebkitanimationiterationImpl(long j2);

    static native void setOnwebkitanimationiterationImpl(long j2, long j3);

    static native long getOnwebkitanimationstartImpl(long j2);

    static native void setOnwebkitanimationstartImpl(long j2, long j3);

    static native long getOnwebkittransitionendImpl(long j2);

    static native void setOnwebkittransitionendImpl(long j2, long j3);

    static native long getOnabortImpl(long j2);

    static native void setOnabortImpl(long j2, long j3);

    static native long getOnblurImpl(long j2);

    static native void setOnblurImpl(long j2, long j3);

    static native long getOncanplayImpl(long j2);

    static native void setOncanplayImpl(long j2, long j3);

    static native long getOncanplaythroughImpl(long j2);

    static native void setOncanplaythroughImpl(long j2, long j3);

    static native long getOnchangeImpl(long j2);

    static native void setOnchangeImpl(long j2, long j3);

    static native long getOnclickImpl(long j2);

    static native void setOnclickImpl(long j2, long j3);

    static native long getOncontextmenuImpl(long j2);

    static native void setOncontextmenuImpl(long j2, long j3);

    static native long getOndblclickImpl(long j2);

    static native void setOndblclickImpl(long j2, long j3);

    static native long getOndragImpl(long j2);

    static native void setOndragImpl(long j2, long j3);

    static native long getOndragendImpl(long j2);

    static native void setOndragendImpl(long j2, long j3);

    static native long getOndragenterImpl(long j2);

    static native void setOndragenterImpl(long j2, long j3);

    static native long getOndragleaveImpl(long j2);

    static native void setOndragleaveImpl(long j2, long j3);

    static native long getOndragoverImpl(long j2);

    static native void setOndragoverImpl(long j2, long j3);

    static native long getOndragstartImpl(long j2);

    static native void setOndragstartImpl(long j2, long j3);

    static native long getOndropImpl(long j2);

    static native void setOndropImpl(long j2, long j3);

    static native long getOndurationchangeImpl(long j2);

    static native void setOndurationchangeImpl(long j2, long j3);

    static native long getOnemptiedImpl(long j2);

    static native void setOnemptiedImpl(long j2, long j3);

    static native long getOnendedImpl(long j2);

    static native void setOnendedImpl(long j2, long j3);

    static native long getOnerrorImpl(long j2);

    static native void setOnerrorImpl(long j2, long j3);

    static native long getOnfocusImpl(long j2);

    static native void setOnfocusImpl(long j2, long j3);

    static native long getOninputImpl(long j2);

    static native void setOninputImpl(long j2, long j3);

    static native long getOninvalidImpl(long j2);

    static native void setOninvalidImpl(long j2, long j3);

    static native long getOnkeydownImpl(long j2);

    static native void setOnkeydownImpl(long j2, long j3);

    static native long getOnkeypressImpl(long j2);

    static native void setOnkeypressImpl(long j2, long j3);

    static native long getOnkeyupImpl(long j2);

    static native void setOnkeyupImpl(long j2, long j3);

    static native long getOnloadImpl(long j2);

    static native void setOnloadImpl(long j2, long j3);

    static native long getOnloadeddataImpl(long j2);

    static native void setOnloadeddataImpl(long j2, long j3);

    static native long getOnloadedmetadataImpl(long j2);

    static native void setOnloadedmetadataImpl(long j2, long j3);

    static native long getOnloadstartImpl(long j2);

    static native void setOnloadstartImpl(long j2, long j3);

    static native long getOnmousedownImpl(long j2);

    static native void setOnmousedownImpl(long j2, long j3);

    static native long getOnmouseenterImpl(long j2);

    static native void setOnmouseenterImpl(long j2, long j3);

    static native long getOnmouseleaveImpl(long j2);

    static native void setOnmouseleaveImpl(long j2, long j3);

    static native long getOnmousemoveImpl(long j2);

    static native void setOnmousemoveImpl(long j2, long j3);

    static native long getOnmouseoutImpl(long j2);

    static native void setOnmouseoutImpl(long j2, long j3);

    static native long getOnmouseoverImpl(long j2);

    static native void setOnmouseoverImpl(long j2, long j3);

    static native long getOnmouseupImpl(long j2);

    static native void setOnmouseupImpl(long j2, long j3);

    static native long getOnmousewheelImpl(long j2);

    static native void setOnmousewheelImpl(long j2, long j3);

    static native long getOnpauseImpl(long j2);

    static native void setOnpauseImpl(long j2, long j3);

    static native long getOnplayImpl(long j2);

    static native void setOnplayImpl(long j2, long j3);

    static native long getOnplayingImpl(long j2);

    static native void setOnplayingImpl(long j2, long j3);

    static native long getOnprogressImpl(long j2);

    static native void setOnprogressImpl(long j2, long j3);

    static native long getOnratechangeImpl(long j2);

    static native void setOnratechangeImpl(long j2, long j3);

    static native long getOnresetImpl(long j2);

    static native void setOnresetImpl(long j2, long j3);

    static native long getOnresizeImpl(long j2);

    static native void setOnresizeImpl(long j2, long j3);

    static native long getOnscrollImpl(long j2);

    static native void setOnscrollImpl(long j2, long j3);

    static native long getOnseekedImpl(long j2);

    static native void setOnseekedImpl(long j2, long j3);

    static native long getOnseekingImpl(long j2);

    static native void setOnseekingImpl(long j2, long j3);

    static native long getOnselectImpl(long j2);

    static native void setOnselectImpl(long j2, long j3);

    static native long getOnstalledImpl(long j2);

    static native void setOnstalledImpl(long j2, long j3);

    static native long getOnsubmitImpl(long j2);

    static native void setOnsubmitImpl(long j2, long j3);

    static native long getOnsuspendImpl(long j2);

    static native void setOnsuspendImpl(long j2, long j3);

    static native long getOntimeupdateImpl(long j2);

    static native void setOntimeupdateImpl(long j2, long j3);

    static native long getOnvolumechangeImpl(long j2);

    static native void setOnvolumechangeImpl(long j2, long j3);

    static native long getOnwaitingImpl(long j2);

    static native void setOnwaitingImpl(long j2, long j3);

    static native long getOnsearchImpl(long j2);

    static native void setOnsearchImpl(long j2, long j3);

    static native long getOnwheelImpl(long j2);

    static native void setOnwheelImpl(long j2, long j3);

    static native long getOnbeforeunloadImpl(long j2);

    static native void setOnbeforeunloadImpl(long j2, long j3);

    static native long getOnhashchangeImpl(long j2);

    static native void setOnhashchangeImpl(long j2, long j3);

    static native long getOnmessageImpl(long j2);

    static native void setOnmessageImpl(long j2, long j3);

    static native long getOnofflineImpl(long j2);

    static native void setOnofflineImpl(long j2, long j3);

    static native long getOnonlineImpl(long j2);

    static native void setOnonlineImpl(long j2, long j3);

    static native long getOnpagehideImpl(long j2);

    static native void setOnpagehideImpl(long j2, long j3);

    static native long getOnpageshowImpl(long j2);

    static native void setOnpageshowImpl(long j2, long j3);

    static native long getOnpopstateImpl(long j2);

    static native void setOnpopstateImpl(long j2, long j3);

    static native long getOnstorageImpl(long j2);

    static native void setOnstorageImpl(long j2, long j3);

    static native long getOnunloadImpl(long j2);

    static native void setOnunloadImpl(long j2, long j3);

    static native long getSelectionImpl(long j2);

    static native void focusImpl(long j2);

    static native void blurImpl(long j2);

    static native void closeImpl(long j2);

    static native void printImpl(long j2);

    static native void stopImpl(long j2);

    static native void alertImpl(long j2, String str);

    static native boolean confirmImpl(long j2, String str);

    static native String promptImpl(long j2, String str, String str2);

    static native boolean findImpl(long j2, String str, boolean z2, boolean z3, boolean z4, boolean z5, boolean z6, boolean z7);

    static native void scrollByImpl(long j2, int i2, int i3);

    static native void scrollToImpl(long j2, int i2, int i3);

    static native void scrollImpl(long j2, int i2, int i3);

    static native void moveByImpl(long j2, float f2, float f3);

    static native void moveToImpl(long j2, float f2, float f3);

    static native void resizeByImpl(long j2, float f2, float f3);

    static native void resizeToImpl(long j2, float f2, float f3);

    static native long getComputedStyleImpl(long j2, long j3, String str);

    static native void captureEventsImpl(long j2);

    static native void releaseEventsImpl(long j2);

    static native void addEventListenerImpl(long j2, String str, long j3, boolean z2);

    static native void removeEventListenerImpl(long j2, String str, long j3, boolean z2);

    static native boolean dispatchEventImpl(long j2, long j3);

    static native String atobImpl(long j2, String str);

    static native String btoaImpl(long j2, String str);

    static native void clearTimeoutImpl(long j2, int i2);

    static native void clearIntervalImpl(long j2, int i2);

    @Override // com.sun.webkit.dom.JSObject
    public /* bridge */ /* synthetic */ int hashCode() {
        return super.hashCode();
    }

    @Override // com.sun.webkit.dom.JSObject
    public /* bridge */ /* synthetic */ boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override // com.sun.webkit.dom.JSObject
    public /* bridge */ /* synthetic */ String toString() {
        return super.toString();
    }

    @Override // com.sun.webkit.dom.JSObject, netscape.javascript.JSObject
    public /* bridge */ /* synthetic */ Object call(String str, Object[] objArr) throws JSException {
        return super.call(str, objArr);
    }

    @Override // com.sun.webkit.dom.JSObject, netscape.javascript.JSObject
    public /* bridge */ /* synthetic */ void setSlot(int i2, Object obj) throws JSException {
        super.setSlot(i2, obj);
    }

    @Override // com.sun.webkit.dom.JSObject, netscape.javascript.JSObject
    public /* bridge */ /* synthetic */ Object getSlot(int i2) throws JSException {
        return super.getSlot(i2);
    }

    @Override // com.sun.webkit.dom.JSObject, netscape.javascript.JSObject
    public /* bridge */ /* synthetic */ void removeMember(String str) throws JSException {
        super.removeMember(str);
    }

    @Override // com.sun.webkit.dom.JSObject, netscape.javascript.JSObject
    public /* bridge */ /* synthetic */ void setMember(String str, Object obj) throws JSException {
        super.setMember(str, obj);
    }

    @Override // com.sun.webkit.dom.JSObject, netscape.javascript.JSObject
    public /* bridge */ /* synthetic */ Object getMember(String str) {
        return super.getMember(str);
    }

    @Override // com.sun.webkit.dom.JSObject, netscape.javascript.JSObject
    public /* bridge */ /* synthetic */ Object eval(String str) throws JSException {
        return super.eval(str);
    }

    static /* synthetic */ int access$310() {
        int i2 = hashCount;
        hashCount = i2 - 1;
        return i2;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static int hashPeer(long peer) {
        return ((int) ((peer ^ (-1)) ^ (peer >> 7))) & (hashTable.length - 1);
    }

    /* JADX WARN: Multi-variable type inference failed */
    private static AbstractView getCachedImpl(long peer) {
        if (peer == 0) {
            return null;
        }
        int hash = hashPeer(peer);
        SelfDisposer head = hashTable[hash];
        SelfDisposer prev = null;
        SelfDisposer selfDisposer = head;
        while (true) {
            SelfDisposer disposer = selfDisposer;
            if (disposer == null) {
                break;
            }
            SelfDisposer next = disposer.next;
            if (disposer.peer == peer) {
                DOMWindowImpl node = (DOMWindowImpl) disposer.get();
                if (node != null) {
                    dispose(peer);
                    return node;
                }
                if (prev != null) {
                    prev.next = next;
                } else {
                    hashTable[hash] = next;
                }
            } else {
                prev = disposer;
                selfDisposer = next;
            }
        }
        DOMWindowImpl node2 = (DOMWindowImpl) createInterface(peer);
        SelfDisposer disposer2 = new SelfDisposer(node2, peer);
        Disposer.addRecord(disposer2);
        disposer2.next = head;
        hashTable[hash] = disposer2;
        if (3 * hashCount >= 2 * hashTable.length) {
            rehash();
        }
        hashCount++;
        return node2;
    }

    static int test_getHashCount() {
        return hashCount;
    }

    private static void rehash() {
        SelfDisposer[] oldTable = hashTable;
        int oldLength = oldTable.length;
        SelfDisposer[] newTable = new SelfDisposer[2 * oldLength];
        hashTable = newTable;
        int i2 = oldLength;
        while (true) {
            i2--;
            if (i2 >= 0) {
                SelfDisposer selfDisposer = oldTable[i2];
                while (true) {
                    SelfDisposer disposer = selfDisposer;
                    if (disposer != null) {
                        SelfDisposer next = disposer.next;
                        int hash = hashPeer(disposer.peer);
                        disposer.next = newTable[hash];
                        newTable[hash] = disposer;
                        selfDisposer = next;
                    }
                }
            } else {
                return;
            }
        }
    }

    /* loaded from: jfxrt.jar:com/sun/webkit/dom/DOMWindowImpl$SelfDisposer.class */
    private static final class SelfDisposer extends Disposer.WeakDisposerRecord {
        private final long peer;
        SelfDisposer next;

        SelfDisposer(Object referent, long _peer) {
            super(referent);
            this.peer = _peer;
        }

        @Override // com.sun.webkit.Disposer.WeakDisposerRecord, com.sun.webkit.DisposerRecord
        public void dispose() {
            int hash = DOMWindowImpl.hashPeer(this.peer);
            SelfDisposer head = DOMWindowImpl.hashTable[hash];
            SelfDisposer prev = null;
            SelfDisposer selfDisposer = head;
            while (true) {
                SelfDisposer disposer = selfDisposer;
                if (disposer == null) {
                    break;
                }
                SelfDisposer next = disposer.next;
                if (disposer.peer == this.peer) {
                    disposer.clear();
                    if (prev == null) {
                        DOMWindowImpl.hashTable[hash] = next;
                    } else {
                        prev.next = next;
                    }
                    DOMWindowImpl.access$310();
                } else {
                    prev = disposer;
                    selfDisposer = next;
                }
            }
            DOMWindowImpl.dispose(this.peer);
        }
    }

    DOMWindowImpl(long peer) {
        super(peer, 2);
    }

    static AbstractView createInterface(long peer) {
        if (peer == 0) {
            return null;
        }
        return new DOMWindowImpl(peer);
    }

    static AbstractView create(long peer) {
        return getCachedImpl(peer);
    }

    static long getPeer(AbstractView arg) {
        if (arg == null) {
            return 0L;
        }
        return ((DOMWindowImpl) arg).getPeer();
    }

    static AbstractView getImpl(long peer) {
        return create(peer);
    }

    public Element getFrameElement() {
        return ElementImpl.getImpl(getFrameElementImpl(getPeer()));
    }

    public boolean getOffscreenBuffering() {
        return getOffscreenBufferingImpl(getPeer());
    }

    public int getOuterHeight() {
        return getOuterHeightImpl(getPeer());
    }

    public int getOuterWidth() {
        return getOuterWidthImpl(getPeer());
    }

    public int getInnerHeight() {
        return getInnerHeightImpl(getPeer());
    }

    public int getInnerWidth() {
        return getInnerWidthImpl(getPeer());
    }

    public int getScreenX() {
        return getScreenXImpl(getPeer());
    }

    public int getScreenY() {
        return getScreenYImpl(getPeer());
    }

    public int getScreenLeft() {
        return getScreenLeftImpl(getPeer());
    }

    public int getScreenTop() {
        return getScreenTopImpl(getPeer());
    }

    public int getScrollX() {
        return getScrollXImpl(getPeer());
    }

    public int getScrollY() {
        return getScrollYImpl(getPeer());
    }

    public int getPageXOffset() {
        return getPageXOffsetImpl(getPeer());
    }

    public int getPageYOffset() {
        return getPageYOffsetImpl(getPeer());
    }

    public boolean getClosed() {
        return getClosedImpl(getPeer());
    }

    public int getLength() {
        return getLengthImpl(getPeer());
    }

    public String getName() {
        return getNameImpl(getPeer());
    }

    public void setName(String value) {
        setNameImpl(getPeer(), value);
    }

    public String getStatus() {
        return getStatusImpl(getPeer());
    }

    public void setStatus(String value) {
        setStatusImpl(getPeer(), value);
    }

    public String getDefaultStatus() {
        return getDefaultStatusImpl(getPeer());
    }

    public void setDefaultStatus(String value) {
        setDefaultStatusImpl(getPeer(), value);
    }

    public AbstractView getSelf() {
        return getImpl(getSelfImpl(getPeer()));
    }

    public AbstractView getWindow() {
        return getImpl(getWindowImpl(getPeer()));
    }

    public AbstractView getFrames() {
        return getImpl(getFramesImpl(getPeer()));
    }

    public AbstractView getOpener() {
        return getImpl(getOpenerImpl(getPeer()));
    }

    public AbstractView getParent() {
        return getImpl(getParentImpl(getPeer()));
    }

    public AbstractView getTop() {
        return getImpl(getTopImpl(getPeer()));
    }

    public Document getDocumentEx() {
        return DocumentImpl.getImpl(getDocumentExImpl(getPeer()));
    }

    public double getDevicePixelRatio() {
        return getDevicePixelRatioImpl(getPeer());
    }

    public EventListener getOnanimationend() {
        return EventListenerImpl.getImpl(getOnanimationendImpl(getPeer()));
    }

    public void setOnanimationend(EventListener value) {
        setOnanimationendImpl(getPeer(), EventListenerImpl.getPeer(value));
    }

    public EventListener getOnanimationiteration() {
        return EventListenerImpl.getImpl(getOnanimationiterationImpl(getPeer()));
    }

    public void setOnanimationiteration(EventListener value) {
        setOnanimationiterationImpl(getPeer(), EventListenerImpl.getPeer(value));
    }

    public EventListener getOnanimationstart() {
        return EventListenerImpl.getImpl(getOnanimationstartImpl(getPeer()));
    }

    public void setOnanimationstart(EventListener value) {
        setOnanimationstartImpl(getPeer(), EventListenerImpl.getPeer(value));
    }

    public EventListener getOntransitionend() {
        return EventListenerImpl.getImpl(getOntransitionendImpl(getPeer()));
    }

    public void setOntransitionend(EventListener value) {
        setOntransitionendImpl(getPeer(), EventListenerImpl.getPeer(value));
    }

    public EventListener getOnwebkitanimationend() {
        return EventListenerImpl.getImpl(getOnwebkitanimationendImpl(getPeer()));
    }

    public void setOnwebkitanimationend(EventListener value) {
        setOnwebkitanimationendImpl(getPeer(), EventListenerImpl.getPeer(value));
    }

    public EventListener getOnwebkitanimationiteration() {
        return EventListenerImpl.getImpl(getOnwebkitanimationiterationImpl(getPeer()));
    }

    public void setOnwebkitanimationiteration(EventListener value) {
        setOnwebkitanimationiterationImpl(getPeer(), EventListenerImpl.getPeer(value));
    }

    public EventListener getOnwebkitanimationstart() {
        return EventListenerImpl.getImpl(getOnwebkitanimationstartImpl(getPeer()));
    }

    public void setOnwebkitanimationstart(EventListener value) {
        setOnwebkitanimationstartImpl(getPeer(), EventListenerImpl.getPeer(value));
    }

    public EventListener getOnwebkittransitionend() {
        return EventListenerImpl.getImpl(getOnwebkittransitionendImpl(getPeer()));
    }

    public void setOnwebkittransitionend(EventListener value) {
        setOnwebkittransitionendImpl(getPeer(), EventListenerImpl.getPeer(value));
    }

    public EventListener getOnabort() {
        return EventListenerImpl.getImpl(getOnabortImpl(getPeer()));
    }

    public void setOnabort(EventListener value) {
        setOnabortImpl(getPeer(), EventListenerImpl.getPeer(value));
    }

    public EventListener getOnblur() {
        return EventListenerImpl.getImpl(getOnblurImpl(getPeer()));
    }

    public void setOnblur(EventListener value) {
        setOnblurImpl(getPeer(), EventListenerImpl.getPeer(value));
    }

    public EventListener getOncanplay() {
        return EventListenerImpl.getImpl(getOncanplayImpl(getPeer()));
    }

    public void setOncanplay(EventListener value) {
        setOncanplayImpl(getPeer(), EventListenerImpl.getPeer(value));
    }

    public EventListener getOncanplaythrough() {
        return EventListenerImpl.getImpl(getOncanplaythroughImpl(getPeer()));
    }

    public void setOncanplaythrough(EventListener value) {
        setOncanplaythroughImpl(getPeer(), EventListenerImpl.getPeer(value));
    }

    public EventListener getOnchange() {
        return EventListenerImpl.getImpl(getOnchangeImpl(getPeer()));
    }

    public void setOnchange(EventListener value) {
        setOnchangeImpl(getPeer(), EventListenerImpl.getPeer(value));
    }

    public EventListener getOnclick() {
        return EventListenerImpl.getImpl(getOnclickImpl(getPeer()));
    }

    public void setOnclick(EventListener value) {
        setOnclickImpl(getPeer(), EventListenerImpl.getPeer(value));
    }

    public EventListener getOncontextmenu() {
        return EventListenerImpl.getImpl(getOncontextmenuImpl(getPeer()));
    }

    public void setOncontextmenu(EventListener value) {
        setOncontextmenuImpl(getPeer(), EventListenerImpl.getPeer(value));
    }

    public EventListener getOndblclick() {
        return EventListenerImpl.getImpl(getOndblclickImpl(getPeer()));
    }

    public void setOndblclick(EventListener value) {
        setOndblclickImpl(getPeer(), EventListenerImpl.getPeer(value));
    }

    public EventListener getOndrag() {
        return EventListenerImpl.getImpl(getOndragImpl(getPeer()));
    }

    public void setOndrag(EventListener value) {
        setOndragImpl(getPeer(), EventListenerImpl.getPeer(value));
    }

    public EventListener getOndragend() {
        return EventListenerImpl.getImpl(getOndragendImpl(getPeer()));
    }

    public void setOndragend(EventListener value) {
        setOndragendImpl(getPeer(), EventListenerImpl.getPeer(value));
    }

    public EventListener getOndragenter() {
        return EventListenerImpl.getImpl(getOndragenterImpl(getPeer()));
    }

    public void setOndragenter(EventListener value) {
        setOndragenterImpl(getPeer(), EventListenerImpl.getPeer(value));
    }

    public EventListener getOndragleave() {
        return EventListenerImpl.getImpl(getOndragleaveImpl(getPeer()));
    }

    public void setOndragleave(EventListener value) {
        setOndragleaveImpl(getPeer(), EventListenerImpl.getPeer(value));
    }

    public EventListener getOndragover() {
        return EventListenerImpl.getImpl(getOndragoverImpl(getPeer()));
    }

    public void setOndragover(EventListener value) {
        setOndragoverImpl(getPeer(), EventListenerImpl.getPeer(value));
    }

    public EventListener getOndragstart() {
        return EventListenerImpl.getImpl(getOndragstartImpl(getPeer()));
    }

    public void setOndragstart(EventListener value) {
        setOndragstartImpl(getPeer(), EventListenerImpl.getPeer(value));
    }

    public EventListener getOndrop() {
        return EventListenerImpl.getImpl(getOndropImpl(getPeer()));
    }

    public void setOndrop(EventListener value) {
        setOndropImpl(getPeer(), EventListenerImpl.getPeer(value));
    }

    public EventListener getOndurationchange() {
        return EventListenerImpl.getImpl(getOndurationchangeImpl(getPeer()));
    }

    public void setOndurationchange(EventListener value) {
        setOndurationchangeImpl(getPeer(), EventListenerImpl.getPeer(value));
    }

    public EventListener getOnemptied() {
        return EventListenerImpl.getImpl(getOnemptiedImpl(getPeer()));
    }

    public void setOnemptied(EventListener value) {
        setOnemptiedImpl(getPeer(), EventListenerImpl.getPeer(value));
    }

    public EventListener getOnended() {
        return EventListenerImpl.getImpl(getOnendedImpl(getPeer()));
    }

    public void setOnended(EventListener value) {
        setOnendedImpl(getPeer(), EventListenerImpl.getPeer(value));
    }

    public EventListener getOnerror() {
        return EventListenerImpl.getImpl(getOnerrorImpl(getPeer()));
    }

    public void setOnerror(EventListener value) {
        setOnerrorImpl(getPeer(), EventListenerImpl.getPeer(value));
    }

    public EventListener getOnfocus() {
        return EventListenerImpl.getImpl(getOnfocusImpl(getPeer()));
    }

    public void setOnfocus(EventListener value) {
        setOnfocusImpl(getPeer(), EventListenerImpl.getPeer(value));
    }

    public EventListener getOninput() {
        return EventListenerImpl.getImpl(getOninputImpl(getPeer()));
    }

    public void setOninput(EventListener value) {
        setOninputImpl(getPeer(), EventListenerImpl.getPeer(value));
    }

    public EventListener getOninvalid() {
        return EventListenerImpl.getImpl(getOninvalidImpl(getPeer()));
    }

    public void setOninvalid(EventListener value) {
        setOninvalidImpl(getPeer(), EventListenerImpl.getPeer(value));
    }

    public EventListener getOnkeydown() {
        return EventListenerImpl.getImpl(getOnkeydownImpl(getPeer()));
    }

    public void setOnkeydown(EventListener value) {
        setOnkeydownImpl(getPeer(), EventListenerImpl.getPeer(value));
    }

    public EventListener getOnkeypress() {
        return EventListenerImpl.getImpl(getOnkeypressImpl(getPeer()));
    }

    public void setOnkeypress(EventListener value) {
        setOnkeypressImpl(getPeer(), EventListenerImpl.getPeer(value));
    }

    public EventListener getOnkeyup() {
        return EventListenerImpl.getImpl(getOnkeyupImpl(getPeer()));
    }

    public void setOnkeyup(EventListener value) {
        setOnkeyupImpl(getPeer(), EventListenerImpl.getPeer(value));
    }

    public EventListener getOnload() {
        return EventListenerImpl.getImpl(getOnloadImpl(getPeer()));
    }

    public void setOnload(EventListener value) {
        setOnloadImpl(getPeer(), EventListenerImpl.getPeer(value));
    }

    public EventListener getOnloadeddata() {
        return EventListenerImpl.getImpl(getOnloadeddataImpl(getPeer()));
    }

    public void setOnloadeddata(EventListener value) {
        setOnloadeddataImpl(getPeer(), EventListenerImpl.getPeer(value));
    }

    public EventListener getOnloadedmetadata() {
        return EventListenerImpl.getImpl(getOnloadedmetadataImpl(getPeer()));
    }

    public void setOnloadedmetadata(EventListener value) {
        setOnloadedmetadataImpl(getPeer(), EventListenerImpl.getPeer(value));
    }

    public EventListener getOnloadstart() {
        return EventListenerImpl.getImpl(getOnloadstartImpl(getPeer()));
    }

    public void setOnloadstart(EventListener value) {
        setOnloadstartImpl(getPeer(), EventListenerImpl.getPeer(value));
    }

    public EventListener getOnmousedown() {
        return EventListenerImpl.getImpl(getOnmousedownImpl(getPeer()));
    }

    public void setOnmousedown(EventListener value) {
        setOnmousedownImpl(getPeer(), EventListenerImpl.getPeer(value));
    }

    public EventListener getOnmouseenter() {
        return EventListenerImpl.getImpl(getOnmouseenterImpl(getPeer()));
    }

    public void setOnmouseenter(EventListener value) {
        setOnmouseenterImpl(getPeer(), EventListenerImpl.getPeer(value));
    }

    public EventListener getOnmouseleave() {
        return EventListenerImpl.getImpl(getOnmouseleaveImpl(getPeer()));
    }

    public void setOnmouseleave(EventListener value) {
        setOnmouseleaveImpl(getPeer(), EventListenerImpl.getPeer(value));
    }

    public EventListener getOnmousemove() {
        return EventListenerImpl.getImpl(getOnmousemoveImpl(getPeer()));
    }

    public void setOnmousemove(EventListener value) {
        setOnmousemoveImpl(getPeer(), EventListenerImpl.getPeer(value));
    }

    public EventListener getOnmouseout() {
        return EventListenerImpl.getImpl(getOnmouseoutImpl(getPeer()));
    }

    public void setOnmouseout(EventListener value) {
        setOnmouseoutImpl(getPeer(), EventListenerImpl.getPeer(value));
    }

    public EventListener getOnmouseover() {
        return EventListenerImpl.getImpl(getOnmouseoverImpl(getPeer()));
    }

    public void setOnmouseover(EventListener value) {
        setOnmouseoverImpl(getPeer(), EventListenerImpl.getPeer(value));
    }

    public EventListener getOnmouseup() {
        return EventListenerImpl.getImpl(getOnmouseupImpl(getPeer()));
    }

    public void setOnmouseup(EventListener value) {
        setOnmouseupImpl(getPeer(), EventListenerImpl.getPeer(value));
    }

    public EventListener getOnmousewheel() {
        return EventListenerImpl.getImpl(getOnmousewheelImpl(getPeer()));
    }

    public void setOnmousewheel(EventListener value) {
        setOnmousewheelImpl(getPeer(), EventListenerImpl.getPeer(value));
    }

    public EventListener getOnpause() {
        return EventListenerImpl.getImpl(getOnpauseImpl(getPeer()));
    }

    public void setOnpause(EventListener value) {
        setOnpauseImpl(getPeer(), EventListenerImpl.getPeer(value));
    }

    public EventListener getOnplay() {
        return EventListenerImpl.getImpl(getOnplayImpl(getPeer()));
    }

    public void setOnplay(EventListener value) {
        setOnplayImpl(getPeer(), EventListenerImpl.getPeer(value));
    }

    public EventListener getOnplaying() {
        return EventListenerImpl.getImpl(getOnplayingImpl(getPeer()));
    }

    public void setOnplaying(EventListener value) {
        setOnplayingImpl(getPeer(), EventListenerImpl.getPeer(value));
    }

    public EventListener getOnprogress() {
        return EventListenerImpl.getImpl(getOnprogressImpl(getPeer()));
    }

    public void setOnprogress(EventListener value) {
        setOnprogressImpl(getPeer(), EventListenerImpl.getPeer(value));
    }

    public EventListener getOnratechange() {
        return EventListenerImpl.getImpl(getOnratechangeImpl(getPeer()));
    }

    public void setOnratechange(EventListener value) {
        setOnratechangeImpl(getPeer(), EventListenerImpl.getPeer(value));
    }

    public EventListener getOnreset() {
        return EventListenerImpl.getImpl(getOnresetImpl(getPeer()));
    }

    public void setOnreset(EventListener value) {
        setOnresetImpl(getPeer(), EventListenerImpl.getPeer(value));
    }

    public EventListener getOnresize() {
        return EventListenerImpl.getImpl(getOnresizeImpl(getPeer()));
    }

    public void setOnresize(EventListener value) {
        setOnresizeImpl(getPeer(), EventListenerImpl.getPeer(value));
    }

    public EventListener getOnscroll() {
        return EventListenerImpl.getImpl(getOnscrollImpl(getPeer()));
    }

    public void setOnscroll(EventListener value) {
        setOnscrollImpl(getPeer(), EventListenerImpl.getPeer(value));
    }

    public EventListener getOnseeked() {
        return EventListenerImpl.getImpl(getOnseekedImpl(getPeer()));
    }

    public void setOnseeked(EventListener value) {
        setOnseekedImpl(getPeer(), EventListenerImpl.getPeer(value));
    }

    public EventListener getOnseeking() {
        return EventListenerImpl.getImpl(getOnseekingImpl(getPeer()));
    }

    public void setOnseeking(EventListener value) {
        setOnseekingImpl(getPeer(), EventListenerImpl.getPeer(value));
    }

    public EventListener getOnselect() {
        return EventListenerImpl.getImpl(getOnselectImpl(getPeer()));
    }

    public void setOnselect(EventListener value) {
        setOnselectImpl(getPeer(), EventListenerImpl.getPeer(value));
    }

    public EventListener getOnstalled() {
        return EventListenerImpl.getImpl(getOnstalledImpl(getPeer()));
    }

    public void setOnstalled(EventListener value) {
        setOnstalledImpl(getPeer(), EventListenerImpl.getPeer(value));
    }

    public EventListener getOnsubmit() {
        return EventListenerImpl.getImpl(getOnsubmitImpl(getPeer()));
    }

    public void setOnsubmit(EventListener value) {
        setOnsubmitImpl(getPeer(), EventListenerImpl.getPeer(value));
    }

    public EventListener getOnsuspend() {
        return EventListenerImpl.getImpl(getOnsuspendImpl(getPeer()));
    }

    public void setOnsuspend(EventListener value) {
        setOnsuspendImpl(getPeer(), EventListenerImpl.getPeer(value));
    }

    public EventListener getOntimeupdate() {
        return EventListenerImpl.getImpl(getOntimeupdateImpl(getPeer()));
    }

    public void setOntimeupdate(EventListener value) {
        setOntimeupdateImpl(getPeer(), EventListenerImpl.getPeer(value));
    }

    public EventListener getOnvolumechange() {
        return EventListenerImpl.getImpl(getOnvolumechangeImpl(getPeer()));
    }

    public void setOnvolumechange(EventListener value) {
        setOnvolumechangeImpl(getPeer(), EventListenerImpl.getPeer(value));
    }

    public EventListener getOnwaiting() {
        return EventListenerImpl.getImpl(getOnwaitingImpl(getPeer()));
    }

    public void setOnwaiting(EventListener value) {
        setOnwaitingImpl(getPeer(), EventListenerImpl.getPeer(value));
    }

    public EventListener getOnsearch() {
        return EventListenerImpl.getImpl(getOnsearchImpl(getPeer()));
    }

    public void setOnsearch(EventListener value) {
        setOnsearchImpl(getPeer(), EventListenerImpl.getPeer(value));
    }

    public EventListener getOnwheel() {
        return EventListenerImpl.getImpl(getOnwheelImpl(getPeer()));
    }

    public void setOnwheel(EventListener value) {
        setOnwheelImpl(getPeer(), EventListenerImpl.getPeer(value));
    }

    public EventListener getOnbeforeunload() {
        return EventListenerImpl.getImpl(getOnbeforeunloadImpl(getPeer()));
    }

    public void setOnbeforeunload(EventListener value) {
        setOnbeforeunloadImpl(getPeer(), EventListenerImpl.getPeer(value));
    }

    public EventListener getOnhashchange() {
        return EventListenerImpl.getImpl(getOnhashchangeImpl(getPeer()));
    }

    public void setOnhashchange(EventListener value) {
        setOnhashchangeImpl(getPeer(), EventListenerImpl.getPeer(value));
    }

    public EventListener getOnmessage() {
        return EventListenerImpl.getImpl(getOnmessageImpl(getPeer()));
    }

    public void setOnmessage(EventListener value) {
        setOnmessageImpl(getPeer(), EventListenerImpl.getPeer(value));
    }

    public EventListener getOnoffline() {
        return EventListenerImpl.getImpl(getOnofflineImpl(getPeer()));
    }

    public void setOnoffline(EventListener value) {
        setOnofflineImpl(getPeer(), EventListenerImpl.getPeer(value));
    }

    public EventListener getOnonline() {
        return EventListenerImpl.getImpl(getOnonlineImpl(getPeer()));
    }

    public void setOnonline(EventListener value) {
        setOnonlineImpl(getPeer(), EventListenerImpl.getPeer(value));
    }

    public EventListener getOnpagehide() {
        return EventListenerImpl.getImpl(getOnpagehideImpl(getPeer()));
    }

    public void setOnpagehide(EventListener value) {
        setOnpagehideImpl(getPeer(), EventListenerImpl.getPeer(value));
    }

    public EventListener getOnpageshow() {
        return EventListenerImpl.getImpl(getOnpageshowImpl(getPeer()));
    }

    public void setOnpageshow(EventListener value) {
        setOnpageshowImpl(getPeer(), EventListenerImpl.getPeer(value));
    }

    public EventListener getOnpopstate() {
        return EventListenerImpl.getImpl(getOnpopstateImpl(getPeer()));
    }

    public void setOnpopstate(EventListener value) {
        setOnpopstateImpl(getPeer(), EventListenerImpl.getPeer(value));
    }

    public EventListener getOnstorage() {
        return EventListenerImpl.getImpl(getOnstorageImpl(getPeer()));
    }

    public void setOnstorage(EventListener value) {
        setOnstorageImpl(getPeer(), EventListenerImpl.getPeer(value));
    }

    public EventListener getOnunload() {
        return EventListenerImpl.getImpl(getOnunloadImpl(getPeer()));
    }

    public void setOnunload(EventListener value) {
        setOnunloadImpl(getPeer(), EventListenerImpl.getPeer(value));
    }

    public DOMSelectionImpl getSelection() {
        return DOMSelectionImpl.getImpl(getSelectionImpl(getPeer()));
    }

    public void focus() {
        focusImpl(getPeer());
    }

    public void blur() {
        blurImpl(getPeer());
    }

    public void close() {
        closeImpl(getPeer());
    }

    public void print() {
        printImpl(getPeer());
    }

    public void stop() {
        stopImpl(getPeer());
    }

    public void alert(String message) {
        alertImpl(getPeer(), message);
    }

    public boolean confirm(String message) {
        return confirmImpl(getPeer(), message);
    }

    public String prompt(String message, String defaultValue) {
        return promptImpl(getPeer(), message, defaultValue);
    }

    public boolean find(String string, boolean caseSensitive, boolean backwards, boolean wrap, boolean wholeWord, boolean searchInFrames, boolean showDialog) {
        return findImpl(getPeer(), string, caseSensitive, backwards, wrap, wholeWord, searchInFrames, showDialog);
    }

    public void scrollBy(int x2, int y2) {
        scrollByImpl(getPeer(), x2, y2);
    }

    public void scrollTo(int x2, int y2) {
        scrollToImpl(getPeer(), x2, y2);
    }

    public void scroll(int x2, int y2) {
        scrollImpl(getPeer(), x2, y2);
    }

    public void moveBy(float x2, float y2) {
        moveByImpl(getPeer(), x2, y2);
    }

    public void moveTo(float x2, float y2) {
        moveToImpl(getPeer(), x2, y2);
    }

    public void resizeBy(float x2, float y2) {
        resizeByImpl(getPeer(), x2, y2);
    }

    public void resizeTo(float width, float height) {
        resizeToImpl(getPeer(), width, height);
    }

    public CSSStyleDeclaration getComputedStyle(Element element, String pseudoElement) {
        return CSSStyleDeclarationImpl.getImpl(getComputedStyleImpl(getPeer(), ElementImpl.getPeer(element), pseudoElement));
    }

    public void captureEvents() {
        captureEventsImpl(getPeer());
    }

    public void releaseEvents() {
        releaseEventsImpl(getPeer());
    }

    @Override // org.w3c.dom.events.EventTarget
    public void addEventListener(String type, EventListener listener, boolean useCapture) {
        addEventListenerImpl(getPeer(), type, EventListenerImpl.getPeer(listener), useCapture);
    }

    @Override // org.w3c.dom.events.EventTarget
    public void removeEventListener(String type, EventListener listener, boolean useCapture) {
        removeEventListenerImpl(getPeer(), type, EventListenerImpl.getPeer(listener), useCapture);
    }

    @Override // org.w3c.dom.events.EventTarget
    public boolean dispatchEvent(Event event) throws DOMException {
        return dispatchEventImpl(getPeer(), EventImpl.getPeer(event));
    }

    public String atob(String string) throws DOMException {
        return atobImpl(getPeer(), string);
    }

    public String btoa(String string) throws DOMException {
        return btoaImpl(getPeer(), string);
    }

    public void clearTimeout(int handle) {
        clearTimeoutImpl(getPeer(), handle);
    }

    public void clearInterval(int handle) {
        clearIntervalImpl(getPeer(), handle);
    }

    @Override // org.w3c.dom.views.AbstractView
    public DocumentView getDocument() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
