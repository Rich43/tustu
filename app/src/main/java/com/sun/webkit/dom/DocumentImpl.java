package com.sun.webkit.dom;

import org.w3c.dom.Attr;
import org.w3c.dom.CDATASection;
import org.w3c.dom.Comment;
import org.w3c.dom.DOMConfiguration;
import org.w3c.dom.DOMException;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.EntityReference;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.ProcessingInstruction;
import org.w3c.dom.Text;
import org.w3c.dom.css.CSSStyleDeclaration;
import org.w3c.dom.events.DocumentEvent;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.html.HTMLCollection;
import org.w3c.dom.html.HTMLElement;
import org.w3c.dom.html.HTMLHeadElement;
import org.w3c.dom.html.HTMLScriptElement;
import org.w3c.dom.ranges.Range;
import org.w3c.dom.stylesheets.StyleSheetList;
import org.w3c.dom.traversal.NodeFilter;
import org.w3c.dom.traversal.NodeIterator;
import org.w3c.dom.traversal.TreeWalker;
import org.w3c.dom.views.AbstractView;
import org.w3c.dom.views.DocumentView;
import org.w3c.dom.xpath.XPathEvaluator;
import org.w3c.dom.xpath.XPathExpression;
import org.w3c.dom.xpath.XPathNSResolver;
import org.w3c.dom.xpath.XPathResult;

/* loaded from: jfxrt.jar:com/sun/webkit/dom/DocumentImpl.class */
public class DocumentImpl extends NodeImpl implements Document, XPathEvaluator, DocumentView, DocumentEvent {
    static native boolean isHTMLDocumentImpl(long j2);

    static native long getDoctypeImpl(long j2);

    static native long getImplementationImpl(long j2);

    static native long getDocumentElementImpl(long j2);

    static native String getInputEncodingImpl(long j2);

    static native String getXmlEncodingImpl(long j2);

    static native String getXmlVersionImpl(long j2);

    static native void setXmlVersionImpl(long j2, String str);

    static native boolean getXmlStandaloneImpl(long j2);

    static native void setXmlStandaloneImpl(long j2, boolean z2);

    static native String getDocumentURIImpl(long j2);

    static native void setDocumentURIImpl(long j2, String str);

    static native long getDefaultViewImpl(long j2);

    static native long getStyleSheetsImpl(long j2);

    static native String getContentTypeImpl(long j2);

    static native String getTitleImpl(long j2);

    static native void setTitleImpl(long j2, String str);

    static native String getReferrerImpl(long j2);

    static native String getDomainImpl(long j2);

    static native String getURLImpl(long j2);

    static native String getCookieImpl(long j2);

    static native void setCookieImpl(long j2, String str);

    static native long getBodyImpl(long j2);

    static native void setBodyImpl(long j2, long j3);

    static native long getHeadImpl(long j2);

    static native long getImagesImpl(long j2);

    static native long getAppletsImpl(long j2);

    static native long getLinksImpl(long j2);

    static native long getFormsImpl(long j2);

    static native long getAnchorsImpl(long j2);

    static native String getLastModifiedImpl(long j2);

    static native String getCharsetImpl(long j2);

    static native String getDefaultCharsetImpl(long j2);

    static native String getReadyStateImpl(long j2);

    static native String getCharacterSetImpl(long j2);

    static native String getPreferredStylesheetSetImpl(long j2);

    static native String getSelectedStylesheetSetImpl(long j2);

    static native void setSelectedStylesheetSetImpl(long j2, String str);

    static native long getActiveElementImpl(long j2);

    static native String getCompatModeImpl(long j2);

    static native boolean getWebkitIsFullScreenImpl(long j2);

    static native boolean getWebkitFullScreenKeyboardInputAllowedImpl(long j2);

    static native long getWebkitCurrentFullScreenElementImpl(long j2);

    static native boolean getWebkitFullscreenEnabledImpl(long j2);

    static native long getWebkitFullscreenElementImpl(long j2);

    static native String getVisibilityStateImpl(long j2);

    static native boolean getHiddenImpl(long j2);

    static native long getCurrentScriptImpl(long j2);

    static native String getOriginImpl(long j2);

    static native long getScrollingElementImpl(long j2);

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

    static native long getOnselectionchangeImpl(long j2);

    static native void setOnselectionchangeImpl(long j2, long j3);

    static native long getOnreadystatechangeImpl(long j2);

    static native void setOnreadystatechangeImpl(long j2, long j3);

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

    static native long getChildrenImpl(long j2);

    static native long getFirstElementChildImpl(long j2);

    static native long getLastElementChildImpl(long j2);

    static native int getChildElementCountImpl(long j2);

    static native long createElementImpl(long j2, String str);

    static native long createDocumentFragmentImpl(long j2);

    static native long createTextNodeImpl(long j2, String str);

    static native long createCommentImpl(long j2, String str);

    static native long createCDATASectionImpl(long j2, String str);

    static native long createProcessingInstructionImpl(long j2, String str, String str2);

    static native long createAttributeImpl(long j2, String str);

    static native long createEntityReferenceImpl(long j2, String str);

    static native long getElementsByTagNameImpl(long j2, String str);

    static native long importNodeImpl(long j2, long j3, boolean z2);

    static native long createElementNSImpl(long j2, String str, String str2);

    static native long createAttributeNSImpl(long j2, String str, String str2);

    static native long getElementsByTagNameNSImpl(long j2, String str, String str2);

    static native long adoptNodeImpl(long j2, long j3);

    static native long createEventImpl(long j2, String str);

    static native long createRangeImpl(long j2);

    static native long createNodeIteratorImpl(long j2, long j3, int i2, long j4, boolean z2);

    static native long createTreeWalkerImpl(long j2, long j3, int i2, long j4, boolean z2);

    static native long getOverrideStyleImpl(long j2, long j3, String str);

    static native long createExpressionImpl(long j2, String str, long j3);

    static native long createNSResolverImpl(long j2, long j3);

    static native long evaluateImpl(long j2, String str, long j3, long j4, short s2, long j5);

    static native boolean execCommandImpl(long j2, String str, boolean z2, String str2);

    static native boolean queryCommandEnabledImpl(long j2, String str);

    static native boolean queryCommandIndetermImpl(long j2, String str);

    static native boolean queryCommandStateImpl(long j2, String str);

    static native boolean queryCommandSupportedImpl(long j2, String str);

    static native String queryCommandValueImpl(long j2, String str);

    static native long getElementsByNameImpl(long j2, String str);

    static native long elementFromPointImpl(long j2, int i2, int i3);

    static native long caretRangeFromPointImpl(long j2, int i2, int i3);

    static native long createCSSStyleDeclarationImpl(long j2);

    static native long getElementsByClassNameImpl(long j2, String str);

    static native boolean hasFocusImpl(long j2);

    static native void webkitCancelFullScreenImpl(long j2);

    static native void webkitExitFullscreenImpl(long j2);

    static native long getElementByIdImpl(long j2, String str);

    static native long querySelectorImpl(long j2, String str);

    static native long querySelectorAllImpl(long j2, String str);

    DocumentImpl(long peer) {
        super(peer);
    }

    static Document getImpl(long peer) {
        return (Document) create(peer);
    }

    @Override // org.w3c.dom.xpath.XPathEvaluator
    public Object evaluate(String expression, Node contextNode, XPathNSResolver resolver, short type, Object result) throws DOMException {
        return evaluate(expression, contextNode, resolver, type, (XPathResult) result);
    }

    @Override // org.w3c.dom.Document
    public DocumentType getDoctype() {
        return DocumentTypeImpl.getImpl(getDoctypeImpl(getPeer()));
    }

    @Override // org.w3c.dom.Document
    public DOMImplementation getImplementation() {
        return DOMImplementationImpl.getImpl(getImplementationImpl(getPeer()));
    }

    @Override // org.w3c.dom.Document
    public Element getDocumentElement() {
        return ElementImpl.getImpl(getDocumentElementImpl(getPeer()));
    }

    @Override // org.w3c.dom.Document
    public String getInputEncoding() {
        return getInputEncodingImpl(getPeer());
    }

    @Override // org.w3c.dom.Document
    public String getXmlEncoding() {
        return getXmlEncodingImpl(getPeer());
    }

    @Override // org.w3c.dom.Document
    public String getXmlVersion() {
        return getXmlVersionImpl(getPeer());
    }

    @Override // org.w3c.dom.Document
    public void setXmlVersion(String value) throws DOMException {
        setXmlVersionImpl(getPeer(), value);
    }

    @Override // org.w3c.dom.Document
    public boolean getXmlStandalone() {
        return getXmlStandaloneImpl(getPeer());
    }

    @Override // org.w3c.dom.Document
    public void setXmlStandalone(boolean value) throws DOMException {
        setXmlStandaloneImpl(getPeer(), value);
    }

    @Override // org.w3c.dom.Document
    public String getDocumentURI() {
        return getDocumentURIImpl(getPeer());
    }

    @Override // org.w3c.dom.Document
    public void setDocumentURI(String value) {
        setDocumentURIImpl(getPeer(), value);
    }

    @Override // org.w3c.dom.views.DocumentView
    public AbstractView getDefaultView() {
        return DOMWindowImpl.getImpl(getDefaultViewImpl(getPeer()));
    }

    public StyleSheetList getStyleSheets() {
        return StyleSheetListImpl.getImpl(getStyleSheetsImpl(getPeer()));
    }

    public String getContentType() {
        return getContentTypeImpl(getPeer());
    }

    public String getTitle() {
        return getTitleImpl(getPeer());
    }

    public void setTitle(String value) {
        setTitleImpl(getPeer(), value);
    }

    public String getReferrer() {
        return getReferrerImpl(getPeer());
    }

    public String getDomain() {
        return getDomainImpl(getPeer());
    }

    public String getURL() {
        return getURLImpl(getPeer());
    }

    public String getCookie() throws DOMException {
        return getCookieImpl(getPeer());
    }

    public void setCookie(String value) throws DOMException {
        setCookieImpl(getPeer(), value);
    }

    public HTMLElement getBody() {
        return HTMLElementImpl.getImpl(getBodyImpl(getPeer()));
    }

    public void setBody(HTMLElement value) throws DOMException {
        setBodyImpl(getPeer(), HTMLElementImpl.getPeer(value));
    }

    public HTMLHeadElement getHead() {
        return HTMLHeadElementImpl.getImpl(getHeadImpl(getPeer()));
    }

    public HTMLCollection getImages() {
        return HTMLCollectionImpl.getImpl(getImagesImpl(getPeer()));
    }

    public HTMLCollection getApplets() {
        return HTMLCollectionImpl.getImpl(getAppletsImpl(getPeer()));
    }

    public HTMLCollection getLinks() {
        return HTMLCollectionImpl.getImpl(getLinksImpl(getPeer()));
    }

    public HTMLCollection getForms() {
        return HTMLCollectionImpl.getImpl(getFormsImpl(getPeer()));
    }

    public HTMLCollection getAnchors() {
        return HTMLCollectionImpl.getImpl(getAnchorsImpl(getPeer()));
    }

    public String getLastModified() {
        return getLastModifiedImpl(getPeer());
    }

    public String getCharset() {
        return getCharsetImpl(getPeer());
    }

    public String getDefaultCharset() {
        return getDefaultCharsetImpl(getPeer());
    }

    public String getReadyState() {
        return getReadyStateImpl(getPeer());
    }

    public String getCharacterSet() {
        return getCharacterSetImpl(getPeer());
    }

    public String getPreferredStylesheetSet() {
        return getPreferredStylesheetSetImpl(getPeer());
    }

    public String getSelectedStylesheetSet() {
        return getSelectedStylesheetSetImpl(getPeer());
    }

    public void setSelectedStylesheetSet(String value) {
        setSelectedStylesheetSetImpl(getPeer(), value);
    }

    public Element getActiveElement() {
        return ElementImpl.getImpl(getActiveElementImpl(getPeer()));
    }

    public String getCompatMode() {
        return getCompatModeImpl(getPeer());
    }

    public boolean getWebkitIsFullScreen() {
        return getWebkitIsFullScreenImpl(getPeer());
    }

    public boolean getWebkitFullScreenKeyboardInputAllowed() {
        return getWebkitFullScreenKeyboardInputAllowedImpl(getPeer());
    }

    public Element getWebkitCurrentFullScreenElement() {
        return ElementImpl.getImpl(getWebkitCurrentFullScreenElementImpl(getPeer()));
    }

    public boolean getWebkitFullscreenEnabled() {
        return getWebkitFullscreenEnabledImpl(getPeer());
    }

    public Element getWebkitFullscreenElement() {
        return ElementImpl.getImpl(getWebkitFullscreenElementImpl(getPeer()));
    }

    public String getVisibilityState() {
        return getVisibilityStateImpl(getPeer());
    }

    public boolean getHidden() {
        return getHiddenImpl(getPeer());
    }

    public HTMLScriptElement getCurrentScript() {
        return HTMLScriptElementImpl.getImpl(getCurrentScriptImpl(getPeer()));
    }

    public String getOrigin() {
        return getOriginImpl(getPeer());
    }

    public Element getScrollingElement() {
        return ElementImpl.getImpl(getScrollingElementImpl(getPeer()));
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

    public EventListener getOnselectionchange() {
        return EventListenerImpl.getImpl(getOnselectionchangeImpl(getPeer()));
    }

    public void setOnselectionchange(EventListener value) {
        setOnselectionchangeImpl(getPeer(), EventListenerImpl.getPeer(value));
    }

    public EventListener getOnreadystatechange() {
        return EventListenerImpl.getImpl(getOnreadystatechangeImpl(getPeer()));
    }

    public void setOnreadystatechange(EventListener value) {
        setOnreadystatechangeImpl(getPeer(), EventListenerImpl.getPeer(value));
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

    public HTMLCollection getChildren() {
        return HTMLCollectionImpl.getImpl(getChildrenImpl(getPeer()));
    }

    public Element getFirstElementChild() {
        return ElementImpl.getImpl(getFirstElementChildImpl(getPeer()));
    }

    public Element getLastElementChild() {
        return ElementImpl.getImpl(getLastElementChildImpl(getPeer()));
    }

    public int getChildElementCount() {
        return getChildElementCountImpl(getPeer());
    }

    @Override // org.w3c.dom.Document
    public Element createElement(String tagName) throws DOMException {
        return ElementImpl.getImpl(createElementImpl(getPeer(), tagName));
    }

    @Override // org.w3c.dom.Document
    public DocumentFragment createDocumentFragment() {
        return DocumentFragmentImpl.getImpl(createDocumentFragmentImpl(getPeer()));
    }

    @Override // org.w3c.dom.Document
    public Text createTextNode(String data) {
        return TextImpl.getImpl(createTextNodeImpl(getPeer(), data));
    }

    @Override // org.w3c.dom.Document
    public Comment createComment(String data) {
        return CommentImpl.getImpl(createCommentImpl(getPeer(), data));
    }

    @Override // org.w3c.dom.Document
    public CDATASection createCDATASection(String data) throws DOMException {
        return CDATASectionImpl.getImpl(createCDATASectionImpl(getPeer(), data));
    }

    @Override // org.w3c.dom.Document
    public ProcessingInstruction createProcessingInstruction(String target, String data) throws DOMException {
        return (ProcessingInstruction) ProcessingInstructionImpl.getImpl(createProcessingInstructionImpl(getPeer(), target, data));
    }

    @Override // org.w3c.dom.Document
    public Attr createAttribute(String name) throws DOMException {
        return AttrImpl.getImpl(createAttributeImpl(getPeer(), name));
    }

    @Override // org.w3c.dom.Document
    public EntityReference createEntityReference(String name) throws DOMException {
        return EntityReferenceImpl.getImpl(createEntityReferenceImpl(getPeer(), name));
    }

    @Override // org.w3c.dom.Document
    public NodeList getElementsByTagName(String tagname) {
        return NodeListImpl.getImpl(getElementsByTagNameImpl(getPeer(), tagname));
    }

    @Override // org.w3c.dom.Document
    public Node importNode(Node importedNode, boolean deep) throws DOMException {
        return NodeImpl.getImpl(importNodeImpl(getPeer(), NodeImpl.getPeer(importedNode), deep));
    }

    @Override // org.w3c.dom.Document
    public Element createElementNS(String namespaceURI, String qualifiedName) throws DOMException {
        return ElementImpl.getImpl(createElementNSImpl(getPeer(), namespaceURI, qualifiedName));
    }

    @Override // org.w3c.dom.Document
    public Attr createAttributeNS(String namespaceURI, String qualifiedName) throws DOMException {
        return AttrImpl.getImpl(createAttributeNSImpl(getPeer(), namespaceURI, qualifiedName));
    }

    @Override // org.w3c.dom.Document
    public NodeList getElementsByTagNameNS(String namespaceURI, String localName) {
        return NodeListImpl.getImpl(getElementsByTagNameNSImpl(getPeer(), namespaceURI, localName));
    }

    @Override // org.w3c.dom.Document
    public Node adoptNode(Node source) throws DOMException {
        return NodeImpl.getImpl(adoptNodeImpl(getPeer(), NodeImpl.getPeer(source)));
    }

    @Override // org.w3c.dom.events.DocumentEvent
    public Event createEvent(String eventType) throws DOMException {
        return EventImpl.getImpl(createEventImpl(getPeer(), eventType));
    }

    public Range createRange() {
        return RangeImpl.getImpl(createRangeImpl(getPeer()));
    }

    public NodeIterator createNodeIterator(Node root, int whatToShow, NodeFilter filter, boolean expandEntityReferences) throws DOMException {
        return NodeIteratorImpl.getImpl(createNodeIteratorImpl(getPeer(), NodeImpl.getPeer(root), whatToShow, NodeFilterImpl.getPeer(filter), expandEntityReferences));
    }

    public TreeWalker createTreeWalker(Node root, int whatToShow, NodeFilter filter, boolean expandEntityReferences) throws DOMException {
        return TreeWalkerImpl.getImpl(createTreeWalkerImpl(getPeer(), NodeImpl.getPeer(root), whatToShow, NodeFilterImpl.getPeer(filter), expandEntityReferences));
    }

    public CSSStyleDeclaration getOverrideStyle(Element element, String pseudoElement) {
        return CSSStyleDeclarationImpl.getImpl(getOverrideStyleImpl(getPeer(), ElementImpl.getPeer(element), pseudoElement));
    }

    @Override // org.w3c.dom.xpath.XPathEvaluator
    public XPathExpression createExpression(String expression, XPathNSResolver resolver) throws DOMException {
        return XPathExpressionImpl.getImpl(createExpressionImpl(getPeer(), expression, XPathNSResolverImpl.getPeer(resolver)));
    }

    @Override // org.w3c.dom.xpath.XPathEvaluator
    public XPathNSResolver createNSResolver(Node nodeResolver) {
        return XPathNSResolverImpl.getImpl(createNSResolverImpl(getPeer(), NodeImpl.getPeer(nodeResolver)));
    }

    public XPathResult evaluate(String expression, Node contextNode, XPathNSResolver resolver, short type, XPathResult inResult) throws DOMException {
        return XPathResultImpl.getImpl(evaluateImpl(getPeer(), expression, NodeImpl.getPeer(contextNode), XPathNSResolverImpl.getPeer(resolver), type, XPathResultImpl.getPeer(inResult)));
    }

    public boolean execCommand(String command, boolean userInterface, String value) {
        return execCommandImpl(getPeer(), command, userInterface, value);
    }

    public boolean queryCommandEnabled(String command) {
        return queryCommandEnabledImpl(getPeer(), command);
    }

    public boolean queryCommandIndeterm(String command) {
        return queryCommandIndetermImpl(getPeer(), command);
    }

    public boolean queryCommandState(String command) {
        return queryCommandStateImpl(getPeer(), command);
    }

    public boolean queryCommandSupported(String command) {
        return queryCommandSupportedImpl(getPeer(), command);
    }

    public String queryCommandValue(String command) {
        return queryCommandValueImpl(getPeer(), command);
    }

    public NodeList getElementsByName(String elementName) {
        return NodeListImpl.getImpl(getElementsByNameImpl(getPeer(), elementName));
    }

    public Element elementFromPoint(int x2, int y2) {
        return ElementImpl.getImpl(elementFromPointImpl(getPeer(), x2, y2));
    }

    public Range caretRangeFromPoint(int x2, int y2) {
        return RangeImpl.getImpl(caretRangeFromPointImpl(getPeer(), x2, y2));
    }

    public CSSStyleDeclaration createCSSStyleDeclaration() {
        return CSSStyleDeclarationImpl.getImpl(createCSSStyleDeclarationImpl(getPeer()));
    }

    public HTMLCollection getElementsByClassName(String classNames) {
        return HTMLCollectionImpl.getImpl(getElementsByClassNameImpl(getPeer(), classNames));
    }

    public boolean hasFocus() {
        return hasFocusImpl(getPeer());
    }

    public void webkitCancelFullScreen() {
        webkitCancelFullScreenImpl(getPeer());
    }

    public void webkitExitFullscreen() {
        webkitExitFullscreenImpl(getPeer());
    }

    @Override // org.w3c.dom.Document
    public Element getElementById(String elementId) {
        return ElementImpl.getImpl(getElementByIdImpl(getPeer(), elementId));
    }

    public Element querySelector(String selectors) throws DOMException {
        return ElementImpl.getImpl(querySelectorImpl(getPeer(), selectors));
    }

    public NodeList querySelectorAll(String selectors) throws DOMException {
        return NodeListImpl.getImpl(querySelectorAllImpl(getPeer(), selectors));
    }

    @Override // org.w3c.dom.Document
    public boolean getStrictErrorChecking() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override // org.w3c.dom.Document
    public void setStrictErrorChecking(boolean strictErrorChecking) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override // org.w3c.dom.Document
    public Node renameNode(Node n2, String namespaceURI, String qualifiedName) throws DOMException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override // org.w3c.dom.Document
    public DOMConfiguration getDomConfig() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override // org.w3c.dom.Document
    public void normalizeDocument() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
