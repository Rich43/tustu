package com.sun.xml.internal.bind.v2.schemagen;

import java.io.IOException;
import java.util.logging.Logger;
import javax.xml.bind.SchemaOutputResolver;
import javax.xml.transform.Result;

/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/schemagen/FoolProofResolver.class */
final class FoolProofResolver extends SchemaOutputResolver {
    private static final Logger logger;
    private final SchemaOutputResolver resolver;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !FoolProofResolver.class.desiredAssertionStatus();
        logger = com.sun.xml.internal.bind.Util.getClassLogger();
    }

    public FoolProofResolver(SchemaOutputResolver resolver) {
        if (!$assertionsDisabled && resolver == null) {
            throw new AssertionError();
        }
        this.resolver = resolver;
    }

    @Override // javax.xml.bind.SchemaOutputResolver
    public Result createOutput(String namespaceUri, String suggestedFileName) throws IOException {
        logger.entering(getClass().getName(), "createOutput", new Object[]{namespaceUri, suggestedFileName});
        Result r2 = this.resolver.createOutput(namespaceUri, suggestedFileName);
        if (r2 != null) {
            String sysId = r2.getSystemId();
            logger.finer("system ID = " + sysId);
            if (sysId == null) {
                throw new AssertionError((Object) "system ID cannot be null");
            }
        }
        logger.exiting(getClass().getName(), "createOutput", r2);
        return r2;
    }
}
