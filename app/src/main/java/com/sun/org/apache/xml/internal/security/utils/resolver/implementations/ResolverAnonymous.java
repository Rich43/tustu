package com.sun.org.apache.xml.internal.security.utils.resolver.implementations;

import com.sun.org.apache.xml.internal.security.signature.XMLSignatureInput;
import com.sun.org.apache.xml.internal.security.utils.resolver.ResourceResolverContext;
import com.sun.org.apache.xml.internal.security.utils.resolver.ResourceResolverSpi;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Paths;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/security/utils/resolver/implementations/ResolverAnonymous.class */
public class ResolverAnonymous extends ResourceResolverSpi {
    private InputStream inStream;

    @Override // com.sun.org.apache.xml.internal.security.utils.resolver.ResourceResolverSpi
    public boolean engineIsThreadSafe() {
        return true;
    }

    public ResolverAnonymous(String str) throws IOException {
        this.inStream = Files.newInputStream(Paths.get(str, new String[0]), new OpenOption[0]);
    }

    public ResolverAnonymous(InputStream inputStream) {
        this.inStream = inputStream;
    }

    @Override // com.sun.org.apache.xml.internal.security.utils.resolver.ResourceResolverSpi
    public XMLSignatureInput engineResolveURI(ResourceResolverContext resourceResolverContext) {
        XMLSignatureInput xMLSignatureInput = new XMLSignatureInput(this.inStream);
        xMLSignatureInput.setSecureValidation(resourceResolverContext.secureValidation);
        return xMLSignatureInput;
    }

    @Override // com.sun.org.apache.xml.internal.security.utils.resolver.ResourceResolverSpi
    public boolean engineCanResolveURI(ResourceResolverContext resourceResolverContext) {
        if (resourceResolverContext.uriToResolve == null) {
            return true;
        }
        return false;
    }

    @Override // com.sun.org.apache.xml.internal.security.utils.resolver.ResourceResolverSpi
    public String[] engineGetPropertyKeys() {
        return new String[0];
    }
}
