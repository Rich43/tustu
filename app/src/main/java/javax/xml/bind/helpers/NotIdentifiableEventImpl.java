package javax.xml.bind.helpers;

import javax.xml.bind.NotIdentifiableEvent;
import javax.xml.bind.ValidationEventLocator;

/* loaded from: rt.jar:javax/xml/bind/helpers/NotIdentifiableEventImpl.class */
public class NotIdentifiableEventImpl extends ValidationEventImpl implements NotIdentifiableEvent {
    public NotIdentifiableEventImpl(int _severity, String _message, ValidationEventLocator _locator) {
        super(_severity, _message, _locator);
    }

    public NotIdentifiableEventImpl(int _severity, String _message, ValidationEventLocator _locator, Throwable _linkedException) {
        super(_severity, _message, _locator, _linkedException);
    }
}
