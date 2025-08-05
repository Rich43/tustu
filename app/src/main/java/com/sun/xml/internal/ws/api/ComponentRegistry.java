package com.sun.xml.internal.ws.api;

import com.sun.istack.internal.NotNull;
import java.util.Set;

/* loaded from: rt.jar:com/sun/xml/internal/ws/api/ComponentRegistry.class */
public interface ComponentRegistry extends Component {
    @NotNull
    Set<Component> getComponents();
}
