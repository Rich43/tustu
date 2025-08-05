package com.sun.xml.internal.ws.policy.sourcemodel;

import com.sun.xml.internal.ws.policy.PolicyConstants;
import com.sun.xml.internal.ws.policy.PolicyException;
import com.sun.xml.internal.ws.policy.privateutil.LocalizationMessages;
import com.sun.xml.internal.ws.policy.privateutil.PolicyLogger;
import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;
import com.sun.xml.internal.ws.policy.sourcemodel.ModelNode;
import com.sun.xml.internal.ws.policy.sourcemodel.wspolicy.NamespaceVersion;
import com.sun.xml.internal.ws.policy.spi.PrefixMapper;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import javax.xml.namespace.QName;

/* loaded from: rt.jar:com/sun/xml/internal/ws/policy/sourcemodel/PolicySourceModel.class */
public class PolicySourceModel implements Cloneable {
    private static final PolicyLogger LOGGER = PolicyLogger.getLogger((Class<?>) PolicySourceModel.class);
    private static final Map<String, String> DEFAULT_NAMESPACE_TO_PREFIX = new HashMap();
    private final Map<String, String> namespaceToPrefix;
    private ModelNode rootNode;
    private final String policyId;
    private final String policyName;
    private final NamespaceVersion nsVersion;
    private final List<ModelNode> references;
    private boolean expanded;

    static {
        PrefixMapper[] prefixMappers = (PrefixMapper[]) PolicyUtils.ServiceProvider.load(PrefixMapper.class);
        if (prefixMappers != null) {
            for (PrefixMapper mapper : prefixMappers) {
                DEFAULT_NAMESPACE_TO_PREFIX.putAll(mapper.getPrefixMap());
            }
        }
        for (NamespaceVersion version : NamespaceVersion.values()) {
            DEFAULT_NAMESPACE_TO_PREFIX.put(version.toString(), version.getDefaultNamespacePrefix());
        }
        DEFAULT_NAMESPACE_TO_PREFIX.put(PolicyConstants.WSU_NAMESPACE_URI, PolicyConstants.WSU_NAMESPACE_PREFIX);
        DEFAULT_NAMESPACE_TO_PREFIX.put(PolicyConstants.SUN_POLICY_NAMESPACE_URI, PolicyConstants.SUN_POLICY_NAMESPACE_PREFIX);
    }

    public static PolicySourceModel createPolicySourceModel(NamespaceVersion nsVersion) {
        return new PolicySourceModel(nsVersion);
    }

    public static PolicySourceModel createPolicySourceModel(NamespaceVersion nsVersion, String policyId, String policyName) {
        return new PolicySourceModel(nsVersion, policyId, policyName);
    }

    private PolicySourceModel(NamespaceVersion nsVersion) {
        this(nsVersion, null, null);
    }

    private PolicySourceModel(NamespaceVersion nsVersion, String policyId, String policyName) {
        this(nsVersion, policyId, policyName, null);
    }

    protected PolicySourceModel(NamespaceVersion nsVersion, String policyId, String policyName, Collection<PrefixMapper> prefixMappers) {
        this.namespaceToPrefix = new HashMap(DEFAULT_NAMESPACE_TO_PREFIX);
        this.references = new LinkedList();
        this.expanded = false;
        this.rootNode = ModelNode.createRootPolicyNode(this);
        this.nsVersion = nsVersion;
        this.policyId = policyId;
        this.policyName = policyName;
        if (prefixMappers != null) {
            for (PrefixMapper prefixMapper : prefixMappers) {
                this.namespaceToPrefix.putAll(prefixMapper.getPrefixMap());
            }
        }
    }

    public ModelNode getRootNode() {
        return this.rootNode;
    }

    public String getPolicyName() {
        return this.policyName;
    }

    public String getPolicyId() {
        return this.policyId;
    }

    public NamespaceVersion getNamespaceVersion() {
        return this.nsVersion;
    }

    Map<String, String> getNamespaceToPrefixMapping() throws PolicyException {
        Map<String, String> nsToPrefixMap = new HashMap<>();
        Collection<String> namespaces = getUsedNamespaces();
        for (String namespace : namespaces) {
            String prefix = getDefaultPrefix(namespace);
            if (prefix != null) {
                nsToPrefixMap.put(namespace, prefix);
            }
        }
        return nsToPrefixMap;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof PolicySourceModel)) {
            return false;
        }
        PolicySourceModel that = (PolicySourceModel) obj;
        boolean result = 1 != 0 && (this.policyId != null ? this.policyId.equals(that.policyId) : that.policyId == null);
        boolean result2 = result && (this.policyName != null ? this.policyName.equals(that.policyName) : that.policyName == null);
        boolean result3 = result2 && this.rootNode.equals(that.rootNode);
        return result3;
    }

    public int hashCode() {
        int result = (37 * 17) + (this.policyId == null ? 0 : this.policyId.hashCode());
        return (37 * ((37 * result) + (this.policyName == null ? 0 : this.policyName.hashCode()))) + this.rootNode.hashCode();
    }

    public String toString() {
        String innerIndent = PolicyUtils.Text.createIndent(1);
        StringBuffer buffer = new StringBuffer(60);
        buffer.append("Policy source model {").append(PolicyUtils.Text.NEW_LINE);
        buffer.append(innerIndent).append("policy id = '").append(this.policyId).append('\'').append(PolicyUtils.Text.NEW_LINE);
        buffer.append(innerIndent).append("policy name = '").append(this.policyName).append('\'').append(PolicyUtils.Text.NEW_LINE);
        this.rootNode.toString(1, buffer).append(PolicyUtils.Text.NEW_LINE).append('}');
        return buffer.toString();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* renamed from: clone, reason: merged with bridge method [inline-methods] */
    public PolicySourceModel m2553clone() throws CloneNotSupportedException {
        PolicySourceModel clone = (PolicySourceModel) super.clone();
        clone.rootNode = this.rootNode.m2541clone();
        try {
            clone.rootNode.setParentModel(clone);
            return clone;
        } catch (IllegalAccessException e2) {
            throw ((CloneNotSupportedException) LOGGER.logSevereException((PolicyLogger) new CloneNotSupportedException(LocalizationMessages.WSP_0013_UNABLE_TO_SET_PARENT_MODEL_ON_ROOT()), (Throwable) e2));
        }
    }

    public boolean containsPolicyReferences() {
        return !this.references.isEmpty();
    }

    private boolean isExpanded() {
        return this.references.isEmpty() || this.expanded;
    }

    public synchronized void expand(PolicySourceModelContext context) throws PolicyException {
        PolicySourceModel policySourceModelRetrieveModel;
        if (!isExpanded()) {
            for (ModelNode reference : this.references) {
                PolicyReferenceData refData = reference.getPolicyReferenceData();
                String digest = refData.getDigest();
                if (digest == null) {
                    policySourceModelRetrieveModel = context.retrieveModel(refData.getReferencedModelUri());
                } else {
                    policySourceModelRetrieveModel = context.retrieveModel(refData.getReferencedModelUri(), refData.getDigestAlgorithmUri(), digest);
                }
                PolicySourceModel referencedModel = policySourceModelRetrieveModel;
                reference.setReferencedModel(referencedModel);
            }
            this.expanded = true;
        }
    }

    void addNewPolicyReference(ModelNode node) {
        if (node.getType() != ModelNode.Type.POLICY_REFERENCE) {
            throw new IllegalArgumentException(LocalizationMessages.WSP_0042_POLICY_REFERENCE_NODE_EXPECTED_INSTEAD_OF(node.getType()));
        }
        this.references.add(node);
    }

    private Collection<String> getUsedNamespaces() throws PolicyException {
        Set<String> namespaces = new HashSet<>();
        namespaces.add(getNamespaceVersion().toString());
        if (this.policyId != null) {
            namespaces.add(PolicyConstants.WSU_NAMESPACE_URI);
        }
        Queue<ModelNode> nodesToBeProcessed = new LinkedList<>();
        nodesToBeProcessed.add(this.rootNode);
        while (true) {
            ModelNode processedNode = nodesToBeProcessed.poll();
            if (processedNode != null) {
                for (ModelNode child : processedNode.getChildren()) {
                    if (child.hasChildren() && !nodesToBeProcessed.offer(child)) {
                        throw ((PolicyException) LOGGER.logSevereException(new PolicyException(LocalizationMessages.WSP_0081_UNABLE_TO_INSERT_CHILD(nodesToBeProcessed, child))));
                    }
                    if (child.isDomainSpecific()) {
                        AssertionData nodeData = child.getNodeData();
                        namespaces.add(nodeData.getName().getNamespaceURI());
                        if (nodeData.isPrivateAttributeSet()) {
                            namespaces.add(PolicyConstants.SUN_POLICY_NAMESPACE_URI);
                        }
                        for (Map.Entry<QName, String> attribute : nodeData.getAttributesSet()) {
                            namespaces.add(attribute.getKey().getNamespaceURI());
                        }
                    }
                }
            } else {
                return namespaces;
            }
        }
    }

    private String getDefaultPrefix(String namespace) {
        return this.namespaceToPrefix.get(namespace);
    }
}
