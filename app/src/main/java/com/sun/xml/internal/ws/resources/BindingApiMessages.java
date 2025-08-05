package com.sun.xml.internal.ws.resources;

import com.sun.istack.internal.localization.Localizable;
import com.sun.istack.internal.localization.LocalizableMessageFactory;
import com.sun.istack.internal.localization.Localizer;

/* loaded from: rt.jar:com/sun/xml/internal/ws/resources/BindingApiMessages.class */
public final class BindingApiMessages {
    private static final LocalizableMessageFactory messageFactory = new LocalizableMessageFactory("com.sun.xml.internal.ws.resources.bindingApi");
    private static final Localizer localizer = new Localizer();

    public static Localizable localizableBINDING_API_NO_FAULT_MESSAGE_NAME() {
        return messageFactory.getMessage("binding.api.no.fault.message.name", new Object[0]);
    }

    public static String BINDING_API_NO_FAULT_MESSAGE_NAME() {
        return localizer.localize(localizableBINDING_API_NO_FAULT_MESSAGE_NAME());
    }
}
