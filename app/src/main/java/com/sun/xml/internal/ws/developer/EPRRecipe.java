package com.sun.xml.internal.ws.developer;

import com.sun.istack.internal.NotNull;
import com.sun.xml.internal.ws.api.message.Header;
import java.util.ArrayList;
import java.util.List;
import javax.xml.transform.Source;

/* loaded from: rt.jar:com/sun/xml/internal/ws/developer/EPRRecipe.class */
public final class EPRRecipe {
    private final List<Header> referenceParameters = new ArrayList();
    private final List<Source> metadata = new ArrayList();

    @NotNull
    public List<Header> getReferenceParameters() {
        return this.referenceParameters;
    }

    @NotNull
    public List<Source> getMetadata() {
        return this.metadata;
    }

    public EPRRecipe addReferenceParameter(Header h2) {
        if (h2 == null) {
            throw new IllegalArgumentException();
        }
        this.referenceParameters.add(h2);
        return this;
    }

    public EPRRecipe addReferenceParameters(Header... headers) {
        for (Header h2 : headers) {
            addReferenceParameter(h2);
        }
        return this;
    }

    public EPRRecipe addReferenceParameters(Iterable<? extends Header> headers) {
        for (Header h2 : headers) {
            addReferenceParameter(h2);
        }
        return this;
    }

    public EPRRecipe addMetadata(Source source) {
        if (source == null) {
            throw new IllegalArgumentException();
        }
        this.metadata.add(source);
        return this;
    }

    public EPRRecipe addMetadata(Source... sources) {
        for (Source s2 : sources) {
            addMetadata(s2);
        }
        return this;
    }

    public EPRRecipe addMetadata(Iterable<? extends Source> sources) {
        for (Source s2 : sources) {
            addMetadata(s2);
        }
        return this;
    }
}
