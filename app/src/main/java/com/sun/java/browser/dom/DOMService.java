package com.sun.java.browser.dom;

/* loaded from: rt.jar:com/sun/java/browser/dom/DOMService.class */
public abstract class DOMService {
    public abstract Object invokeAndWait(DOMAction dOMAction) throws DOMAccessException;

    public abstract void invokeLater(DOMAction dOMAction);

    public static DOMService getService(Object obj) throws DOMUnsupportedException {
        try {
            return (DOMService) Class.forName("sun.plugin.dom.DOMService").newInstance();
        } catch (Throwable th) {
            throw new DOMUnsupportedException(th.toString());
        }
    }
}
