package com.sun.xml.internal.bind.v2.runtime.unmarshaller;

import com.sun.xml.internal.bind.v2.runtime.JaxBeanInfo;
import com.sun.xml.internal.bind.v2.runtime.unmarshaller.UnmarshallingContext;
import java.util.Collection;
import java.util.Collections;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.helpers.ValidationEventImpl;
import javax.xml.namespace.QName;
import org.xml.sax.SAXException;

/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/runtime/unmarshaller/Loader.class */
public abstract class Loader {
    protected boolean expectText;

    protected Loader(boolean expectText) {
        this.expectText = expectText;
    }

    protected Loader() {
    }

    public void startElement(UnmarshallingContext.State state, TagName ea) throws SAXException {
    }

    public void childElement(UnmarshallingContext.State state, TagName ea) throws SAXException {
        reportUnexpectedChildElement(ea, true);
        state.setLoader(Discarder.INSTANCE);
        state.setReceiver(null);
    }

    protected final void reportUnexpectedChildElement(TagName ea, boolean canRecover) throws SAXException {
        if (canRecover) {
            UnmarshallingContext context = UnmarshallingContext.getInstance();
            if (!context.parent.hasEventHandler() || !context.shouldErrorBeReported()) {
                return;
            }
        }
        if (ea.uri != ea.uri.intern() || ea.local != ea.local.intern()) {
            reportError(Messages.UNINTERNED_STRINGS.format(new Object[0]), canRecover);
        } else {
            reportError(Messages.UNEXPECTED_ELEMENT.format(ea.uri, ea.local, computeExpectedElements()), canRecover);
        }
    }

    public Collection<QName> getExpectedChildElements() {
        return Collections.emptyList();
    }

    public Collection<QName> getExpectedAttributes() {
        return Collections.emptyList();
    }

    public void text(UnmarshallingContext.State state, CharSequence text) throws SAXException {
        reportError(Messages.UNEXPECTED_TEXT.format(text.toString().replace('\r', ' ').replace('\n', ' ').replace('\t', ' ').trim()), true);
    }

    public final boolean expectText() {
        return this.expectText;
    }

    public void leaveElement(UnmarshallingContext.State state, TagName ea) throws SAXException {
    }

    private String computeExpectedElements() {
        StringBuilder r2 = new StringBuilder();
        for (QName n2 : getExpectedChildElements()) {
            if (r2.length() != 0) {
                r2.append(',');
            }
            r2.append("<{").append(n2.getNamespaceURI()).append('}').append(n2.getLocalPart()).append('>');
        }
        if (r2.length() == 0) {
            return "(none)";
        }
        return r2.toString();
    }

    protected final void fireBeforeUnmarshal(JaxBeanInfo beanInfo, Object child, UnmarshallingContext.State state) throws SAXException {
        if (beanInfo.lookForLifecycleMethods()) {
            UnmarshallingContext context = state.getContext();
            Unmarshaller.Listener listener = context.parent.getListener();
            if (beanInfo.hasBeforeUnmarshalMethod()) {
                beanInfo.invokeBeforeUnmarshalMethod(context.parent, child, state.getPrev().getTarget());
            }
            if (listener != null) {
                listener.beforeUnmarshal(child, state.getPrev().getTarget());
            }
        }
    }

    protected final void fireAfterUnmarshal(JaxBeanInfo beanInfo, Object child, UnmarshallingContext.State state) throws SAXException {
        if (beanInfo.lookForLifecycleMethods()) {
            UnmarshallingContext context = state.getContext();
            Unmarshaller.Listener listener = context.parent.getListener();
            if (beanInfo.hasAfterUnmarshalMethod()) {
                beanInfo.invokeAfterUnmarshalMethod(context.parent, child, state.getTarget());
            }
            if (listener != null) {
                listener.afterUnmarshal(child, state.getTarget());
            }
        }
    }

    protected static void handleGenericException(Exception e2) throws SAXException {
        handleGenericException(e2, false);
    }

    public static void handleGenericException(Exception e2, boolean canRecover) throws SAXException {
        reportError(e2.getMessage(), e2, canRecover);
    }

    public static void handleGenericError(Error e2) throws SAXException {
        reportError(e2.getMessage(), false);
    }

    protected static void reportError(String msg, boolean canRecover) throws SAXException {
        reportError(msg, null, canRecover);
    }

    public static void reportError(String msg, Exception nested, boolean canRecover) throws SAXException {
        UnmarshallingContext context = UnmarshallingContext.getInstance();
        context.handleEvent(new ValidationEventImpl(canRecover ? 1 : 2, msg, context.getLocator().getLocation(), nested), canRecover);
    }

    protected static void handleParseConversionException(UnmarshallingContext.State state, Exception e2) throws SAXException {
        state.getContext().handleError(e2);
    }
}
