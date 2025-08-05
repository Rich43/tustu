package com.sun.xml.internal.ws.policy.sourcemodel;

import com.sun.xml.internal.ws.policy.privateutil.LocalizationMessages;
import com.sun.xml.internal.ws.policy.privateutil.PolicyLogger;
import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;
import java.net.URI;
import java.net.URISyntaxException;

/* loaded from: rt.jar:com/sun/xml/internal/ws/policy/sourcemodel/PolicyReferenceData.class */
final class PolicyReferenceData {
    private static final PolicyLogger LOGGER = PolicyLogger.getLogger((Class<?>) PolicyReferenceData.class);
    private static final URI DEFAULT_DIGEST_ALGORITHM_URI;
    private static final URISyntaxException CLASS_INITIALIZATION_EXCEPTION;
    private final URI referencedModelUri;
    private final String digest;
    private final URI digestAlgorithmUri;

    static {
        URI tempUri = null;
        try {
            tempUri = new URI("http://schemas.xmlsoap.org/ws/2004/09/policy/Sha1Exc");
            DEFAULT_DIGEST_ALGORITHM_URI = tempUri;
            CLASS_INITIALIZATION_EXCEPTION = null;
        } catch (URISyntaxException e2) {
            DEFAULT_DIGEST_ALGORITHM_URI = tempUri;
            CLASS_INITIALIZATION_EXCEPTION = e2;
        } catch (Throwable th) {
            DEFAULT_DIGEST_ALGORITHM_URI = tempUri;
            CLASS_INITIALIZATION_EXCEPTION = null;
            throw th;
        }
    }

    public PolicyReferenceData(URI referencedModelUri) {
        this.referencedModelUri = referencedModelUri;
        this.digest = null;
        this.digestAlgorithmUri = null;
    }

    public PolicyReferenceData(URI referencedModelUri, String expectedDigest, URI usedDigestAlgorithm) {
        if (CLASS_INITIALIZATION_EXCEPTION != null) {
            throw ((IllegalStateException) LOGGER.logSevereException(new IllegalStateException(LocalizationMessages.WSP_0015_UNABLE_TO_INSTANTIATE_DIGEST_ALG_URI_FIELD(), CLASS_INITIALIZATION_EXCEPTION)));
        }
        if (usedDigestAlgorithm != null && expectedDigest == null) {
            throw ((IllegalArgumentException) LOGGER.logSevereException(new IllegalArgumentException(LocalizationMessages.WSP_0072_DIGEST_MUST_NOT_BE_NULL_WHEN_ALG_DEFINED())));
        }
        this.referencedModelUri = referencedModelUri;
        if (expectedDigest == null) {
            this.digest = null;
            this.digestAlgorithmUri = null;
            return;
        }
        this.digest = expectedDigest;
        if (usedDigestAlgorithm == null) {
            this.digestAlgorithmUri = DEFAULT_DIGEST_ALGORITHM_URI;
        } else {
            this.digestAlgorithmUri = usedDigestAlgorithm;
        }
    }

    public URI getReferencedModelUri() {
        return this.referencedModelUri;
    }

    public String getDigest() {
        return this.digest;
    }

    public URI getDigestAlgorithmUri() {
        return this.digestAlgorithmUri;
    }

    public String toString() {
        return toString(0, new StringBuffer()).toString();
    }

    public StringBuffer toString(int indentLevel, StringBuffer buffer) {
        String indent = PolicyUtils.Text.createIndent(indentLevel);
        String innerIndent = PolicyUtils.Text.createIndent(indentLevel + 1);
        buffer.append(indent).append("reference data {").append(PolicyUtils.Text.NEW_LINE);
        buffer.append(innerIndent).append("referenced policy model URI = '").append((Object) this.referencedModelUri).append('\'').append(PolicyUtils.Text.NEW_LINE);
        if (this.digest == null) {
            buffer.append(innerIndent).append("no digest specified").append(PolicyUtils.Text.NEW_LINE);
        } else {
            buffer.append(innerIndent).append("digest algorith URI = '").append((Object) this.digestAlgorithmUri).append('\'').append(PolicyUtils.Text.NEW_LINE);
            buffer.append(innerIndent).append("digest = '").append(this.digest).append('\'').append(PolicyUtils.Text.NEW_LINE);
        }
        buffer.append(indent).append('}');
        return buffer;
    }
}
