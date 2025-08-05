package com.sun.xml.internal.ws.api;

import com.sun.istack.internal.NotNull;

/* loaded from: rt.jar:com/sun/xml/internal/ws/api/ComponentEx.class */
public interface ComponentEx extends Component {
    @NotNull
    <S> Iterable<S> getIterableSPI(@NotNull Class<S> cls);
}
