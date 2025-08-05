package com.sun.webkit.dom;

import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;
import org.w3c.dom.TypeInfo;
import org.w3c.dom.css.CSSStyleDeclaration;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.html.HTMLCollection;

/* loaded from: jfxrt.jar:com/sun/webkit/dom/ElementImpl.class */
public class ElementImpl extends NodeImpl implements Element {
    public static final int ALLOW_KEYBOARD_INPUT = 1;

    static native boolean isHTMLElementImpl(long j2);

    static native String getTagNameImpl(long j2);

    static native long getAttributesImpl(long j2);

    static native long getStyleImpl(long j2);

    static native String getIdImpl(long j2);

    static native void setIdImpl(long j2, String str);

    static native double getOffsetLeftImpl(long j2);

    static native double getOffsetTopImpl(long j2);

    static native double getOffsetWidthImpl(long j2);

    static native double getOffsetHeightImpl(long j2);

    static native double getClientLeftImpl(long j2);

    static native double getClientTopImpl(long j2);

    static native double getClientWidthImpl(long j2);

    static native double getClientHeightImpl(long j2);

    static native int getScrollLeftImpl(long j2);

    static native void setScrollLeftImpl(long j2, int i2);

    static native int getScrollTopImpl(long j2);

    static native void setScrollTopImpl(long j2, int i2);

    static native int getScrollWidthImpl(long j2);

    static native int getScrollHeightImpl(long j2);

    static native long getOffsetParentImpl(long j2);

    static native String getInnerHTMLImpl(long j2);

    static native void setInnerHTMLImpl(long j2, String str);

    static native String getOuterHTMLImpl(long j2);

    static native void setOuterHTMLImpl(long j2, String str);

    static native String getClassNameImpl(long j2);

    static native void setClassNameImpl(long j2, String str);

    static native long getOnbeforecopyImpl(long j2);

    static native void setOnbeforecopyImpl(long j2, long j3);

    static native long getOnbeforecutImpl(long j2);

    static native void setOnbeforecutImpl(long j2, long j3);

    static native long getOnbeforepasteImpl(long j2);

    static native void setOnbeforepasteImpl(long j2, long j3);

    static native long getOncopyImpl(long j2);

    static native void setOncopyImpl(long j2, long j3);

    static native long getOncutImpl(long j2);

    static native void setOncutImpl(long j2, long j3);

    static native long getOnpasteImpl(long j2);

    static native void setOnpasteImpl(long j2, long j3);

    static native long getOnselectstartImpl(long j2);

    static native void setOnselectstartImpl(long j2, long j3);

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

    static native long getOnfocusinImpl(long j2);

    static native void setOnfocusinImpl(long j2, long j3);

    static native long getOnfocusoutImpl(long j2);

    static native void setOnfocusoutImpl(long j2, long j3);

    static native long getOnbeforeloadImpl(long j2);

    static native void setOnbeforeloadImpl(long j2, long j3);

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

    static native long getPreviousElementSiblingImpl(long j2);

    static native long getNextElementSiblingImpl(long j2);

    static native long getChildrenImpl(long j2);

    static native long getFirstElementChildImpl(long j2);

    static native long getLastElementChildImpl(long j2);

    static native int getChildElementCountImpl(long j2);

    static native String getAttributeImpl(long j2, String str);

    static native void setAttributeImpl(long j2, String str, String str2);

    static native void removeAttributeImpl(long j2, String str);

    static native long getAttributeNodeImpl(long j2, String str);

    static native long setAttributeNodeImpl(long j2, long j3);

    static native long removeAttributeNodeImpl(long j2, long j3);

    static native long getElementsByTagNameImpl(long j2, String str);

    static native boolean hasAttributesImpl(long j2);

    static native String getAttributeNSImpl(long j2, String str, String str2);

    static native void setAttributeNSImpl(long j2, String str, String str2, String str3);

    static native void removeAttributeNSImpl(long j2, String str, String str2);

    static native long getElementsByTagNameNSImpl(long j2, String str, String str2);

    static native long getAttributeNodeNSImpl(long j2, String str, String str2);

    static native long setAttributeNodeNSImpl(long j2, long j3);

    static native boolean hasAttributeImpl(long j2, String str);

    static native boolean hasAttributeNSImpl(long j2, String str, String str2);

    static native void focusImpl(long j2);

    static native void blurImpl(long j2);

    static native void scrollIntoViewImpl(long j2, boolean z2);

    static native void scrollIntoViewIfNeededImpl(long j2, boolean z2);

    static native void scrollByLinesImpl(long j2, int i2);

    static native void scrollByPagesImpl(long j2, int i2);

    static native long getElementsByClassNameImpl(long j2, String str);

    static native boolean matchesImpl(long j2, String str);

    static native long closestImpl(long j2, String str);

    static native boolean webkitMatchesSelectorImpl(long j2, String str);

    static native void webkitRequestFullScreenImpl(long j2, short s2);

    static native void webkitRequestFullscreenImpl(long j2);

    static native void removeImpl(long j2);

    static native long querySelectorImpl(long j2, String str);

    static native long querySelectorAllImpl(long j2, String str);

    ElementImpl(long peer) {
        super(peer);
    }

    static Element getImpl(long peer) {
        return (Element) create(peer);
    }

    @Override // org.w3c.dom.Element
    public String getTagName() {
        return getTagNameImpl(getPeer());
    }

    @Override // com.sun.webkit.dom.NodeImpl, org.w3c.dom.Node
    public NamedNodeMap getAttributes() {
        return NamedNodeMapImpl.getImpl(getAttributesImpl(getPeer()));
    }

    public CSSStyleDeclaration getStyle() {
        return CSSStyleDeclarationImpl.getImpl(getStyleImpl(getPeer()));
    }

    public String getId() {
        return getIdImpl(getPeer());
    }

    public void setId(String value) {
        setIdImpl(getPeer(), value);
    }

    public double getOffsetLeft() {
        return getOffsetLeftImpl(getPeer());
    }

    public double getOffsetTop() {
        return getOffsetTopImpl(getPeer());
    }

    public double getOffsetWidth() {
        return getOffsetWidthImpl(getPeer());
    }

    public double getOffsetHeight() {
        return getOffsetHeightImpl(getPeer());
    }

    public double getClientLeft() {
        return getClientLeftImpl(getPeer());
    }

    public double getClientTop() {
        return getClientTopImpl(getPeer());
    }

    public double getClientWidth() {
        return getClientWidthImpl(getPeer());
    }

    public double getClientHeight() {
        return getClientHeightImpl(getPeer());
    }

    public int getScrollLeft() {
        return getScrollLeftImpl(getPeer());
    }

    public void setScrollLeft(int value) {
        setScrollLeftImpl(getPeer(), value);
    }

    public int getScrollTop() {
        return getScrollTopImpl(getPeer());
    }

    public void setScrollTop(int value) {
        setScrollTopImpl(getPeer(), value);
    }

    public int getScrollWidth() {
        return getScrollWidthImpl(getPeer());
    }

    public int getScrollHeight() {
        return getScrollHeightImpl(getPeer());
    }

    public Element getOffsetParent() {
        return getImpl(getOffsetParentImpl(getPeer()));
    }

    public String getInnerHTML() {
        return getInnerHTMLImpl(getPeer());
    }

    public void setInnerHTML(String value) throws DOMException {
        setInnerHTMLImpl(getPeer(), value);
    }

    public String getOuterHTML() {
        return getOuterHTMLImpl(getPeer());
    }

    public void setOuterHTML(String value) throws DOMException {
        setOuterHTMLImpl(getPeer(), value);
    }

    public String getClassName() {
        return getClassNameImpl(getPeer());
    }

    public void setClassName(String value) {
        setClassNameImpl(getPeer(), value);
    }

    public EventListener getOnbeforecopy() {
        return EventListenerImpl.getImpl(getOnbeforecopyImpl(getPeer()));
    }

    public void setOnbeforecopy(EventListener value) {
        setOnbeforecopyImpl(getPeer(), EventListenerImpl.getPeer(value));
    }

    public EventListener getOnbeforecut() {
        return EventListenerImpl.getImpl(getOnbeforecutImpl(getPeer()));
    }

    public void setOnbeforecut(EventListener value) {
        setOnbeforecutImpl(getPeer(), EventListenerImpl.getPeer(value));
    }

    public EventListener getOnbeforepaste() {
        return EventListenerImpl.getImpl(getOnbeforepasteImpl(getPeer()));
    }

    public void setOnbeforepaste(EventListener value) {
        setOnbeforepasteImpl(getPeer(), EventListenerImpl.getPeer(value));
    }

    public EventListener getOncopy() {
        return EventListenerImpl.getImpl(getOncopyImpl(getPeer()));
    }

    public void setOncopy(EventListener value) {
        setOncopyImpl(getPeer(), EventListenerImpl.getPeer(value));
    }

    public EventListener getOncut() {
        return EventListenerImpl.getImpl(getOncutImpl(getPeer()));
    }

    public void setOncut(EventListener value) {
        setOncutImpl(getPeer(), EventListenerImpl.getPeer(value));
    }

    public EventListener getOnpaste() {
        return EventListenerImpl.getImpl(getOnpasteImpl(getPeer()));
    }

    public void setOnpaste(EventListener value) {
        setOnpasteImpl(getPeer(), EventListenerImpl.getPeer(value));
    }

    public EventListener getOnselectstart() {
        return EventListenerImpl.getImpl(getOnselectstartImpl(getPeer()));
    }

    public void setOnselectstart(EventListener value) {
        setOnselectstartImpl(getPeer(), EventListenerImpl.getPeer(value));
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

    public EventListener getOnfocusin() {
        return EventListenerImpl.getImpl(getOnfocusinImpl(getPeer()));
    }

    public void setOnfocusin(EventListener value) {
        setOnfocusinImpl(getPeer(), EventListenerImpl.getPeer(value));
    }

    public EventListener getOnfocusout() {
        return EventListenerImpl.getImpl(getOnfocusoutImpl(getPeer()));
    }

    public void setOnfocusout(EventListener value) {
        setOnfocusoutImpl(getPeer(), EventListenerImpl.getPeer(value));
    }

    public EventListener getOnbeforeload() {
        return EventListenerImpl.getImpl(getOnbeforeloadImpl(getPeer()));
    }

    public void setOnbeforeload(EventListener value) {
        setOnbeforeloadImpl(getPeer(), EventListenerImpl.getPeer(value));
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

    public Element getPreviousElementSibling() {
        return getImpl(getPreviousElementSiblingImpl(getPeer()));
    }

    public Element getNextElementSibling() {
        return getImpl(getNextElementSiblingImpl(getPeer()));
    }

    public HTMLCollection getChildren() {
        return HTMLCollectionImpl.getImpl(getChildrenImpl(getPeer()));
    }

    public Element getFirstElementChild() {
        return getImpl(getFirstElementChildImpl(getPeer()));
    }

    public Element getLastElementChild() {
        return getImpl(getLastElementChildImpl(getPeer()));
    }

    public int getChildElementCount() {
        return getChildElementCountImpl(getPeer());
    }

    @Override // org.w3c.dom.Element
    public String getAttribute(String name) {
        return getAttributeImpl(getPeer(), name);
    }

    @Override // org.w3c.dom.Element
    public void setAttribute(String name, String value) throws DOMException {
        setAttributeImpl(getPeer(), name, value);
    }

    @Override // org.w3c.dom.Element
    public void removeAttribute(String name) {
        removeAttributeImpl(getPeer(), name);
    }

    @Override // org.w3c.dom.Element
    public Attr getAttributeNode(String name) {
        return AttrImpl.getImpl(getAttributeNodeImpl(getPeer(), name));
    }

    @Override // org.w3c.dom.Element
    public Attr setAttributeNode(Attr newAttr) throws DOMException {
        return AttrImpl.getImpl(setAttributeNodeImpl(getPeer(), AttrImpl.getPeer(newAttr)));
    }

    @Override // org.w3c.dom.Element
    public Attr removeAttributeNode(Attr oldAttr) throws DOMException {
        return AttrImpl.getImpl(removeAttributeNodeImpl(getPeer(), AttrImpl.getPeer(oldAttr)));
    }

    @Override // org.w3c.dom.Element
    public NodeList getElementsByTagName(String name) {
        return NodeListImpl.getImpl(getElementsByTagNameImpl(getPeer(), name));
    }

    @Override // com.sun.webkit.dom.NodeImpl, org.w3c.dom.Node
    public boolean hasAttributes() {
        return hasAttributesImpl(getPeer());
    }

    @Override // org.w3c.dom.Element
    public String getAttributeNS(String namespaceURI, String localName) {
        return getAttributeNSImpl(getPeer(), namespaceURI, localName);
    }

    @Override // org.w3c.dom.Element
    public void setAttributeNS(String namespaceURI, String qualifiedName, String value) throws DOMException {
        setAttributeNSImpl(getPeer(), namespaceURI, qualifiedName, value);
    }

    @Override // org.w3c.dom.Element
    public void removeAttributeNS(String namespaceURI, String localName) {
        removeAttributeNSImpl(getPeer(), namespaceURI, localName);
    }

    @Override // org.w3c.dom.Element
    public NodeList getElementsByTagNameNS(String namespaceURI, String localName) {
        return NodeListImpl.getImpl(getElementsByTagNameNSImpl(getPeer(), namespaceURI, localName));
    }

    @Override // org.w3c.dom.Element
    public Attr getAttributeNodeNS(String namespaceURI, String localName) {
        return AttrImpl.getImpl(getAttributeNodeNSImpl(getPeer(), namespaceURI, localName));
    }

    @Override // org.w3c.dom.Element
    public Attr setAttributeNodeNS(Attr newAttr) throws DOMException {
        return AttrImpl.getImpl(setAttributeNodeNSImpl(getPeer(), AttrImpl.getPeer(newAttr)));
    }

    @Override // org.w3c.dom.Element
    public boolean hasAttribute(String name) {
        return hasAttributeImpl(getPeer(), name);
    }

    @Override // org.w3c.dom.Element
    public boolean hasAttributeNS(String namespaceURI, String localName) {
        return hasAttributeNSImpl(getPeer(), namespaceURI, localName);
    }

    public void focus() {
        focusImpl(getPeer());
    }

    public void blur() {
        blurImpl(getPeer());
    }

    public void scrollIntoView(boolean alignWithTop) {
        scrollIntoViewImpl(getPeer(), alignWithTop);
    }

    public void scrollIntoViewIfNeeded(boolean centerIfNeeded) {
        scrollIntoViewIfNeededImpl(getPeer(), centerIfNeeded);
    }

    public void scrollByLines(int lines) {
        scrollByLinesImpl(getPeer(), lines);
    }

    public void scrollByPages(int pages) {
        scrollByPagesImpl(getPeer(), pages);
    }

    public HTMLCollection getElementsByClassName(String name) {
        return HTMLCollectionImpl.getImpl(getElementsByClassNameImpl(getPeer(), name));
    }

    public boolean matches(String selectors) throws DOMException {
        return matchesImpl(getPeer(), selectors);
    }

    public Element closest(String selectors) throws DOMException {
        return getImpl(closestImpl(getPeer(), selectors));
    }

    public boolean webkitMatchesSelector(String selectors) throws DOMException {
        return webkitMatchesSelectorImpl(getPeer(), selectors);
    }

    public void webkitRequestFullScreen(short flags) {
        webkitRequestFullScreenImpl(getPeer(), flags);
    }

    public void webkitRequestFullscreen() {
        webkitRequestFullscreenImpl(getPeer());
    }

    public void remove() throws DOMException {
        removeImpl(getPeer());
    }

    public Element querySelector(String selectors) throws DOMException {
        return getImpl(querySelectorImpl(getPeer(), selectors));
    }

    public NodeList querySelectorAll(String selectors) throws DOMException {
        return NodeListImpl.getImpl(querySelectorAllImpl(getPeer(), selectors));
    }

    @Override // org.w3c.dom.Element
    public void setIdAttribute(String name, boolean isId) throws DOMException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override // org.w3c.dom.Element
    public void setIdAttributeNode(Attr idAttr, boolean isId) throws DOMException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override // org.w3c.dom.Element
    public TypeInfo getSchemaTypeInfo() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override // org.w3c.dom.Element
    public void setIdAttributeNS(String namespaceURI, String localName, boolean isId) throws DOMException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
