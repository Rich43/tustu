package com.sun.xml.internal.ws.api.server;

import com.sun.istack.internal.NotNull;
import com.sun.xml.internal.ws.api.Component;
import java.net.URI;

/* loaded from: rt.jar:com/sun/xml/internal/ws/api/server/BoundEndpoint.class */
public interface BoundEndpoint extends Component {
    @NotNull
    WSEndpoint getEndpoint();

    @NotNull
    URI getAddress();

    @NotNull
    URI getAddress(String str);
}
