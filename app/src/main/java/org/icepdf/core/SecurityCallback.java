package org.icepdf.core;

import org.icepdf.core.pobjects.Document;

/* loaded from: icepdf-core.jar:org/icepdf/core/SecurityCallback.class */
public interface SecurityCallback {
    String requestPassword(Document document);
}
