package com.sun.xml.internal.ws.policy.sourcemodel;

import com.sun.xml.internal.txw2.TXW;
import com.sun.xml.internal.txw2.TypedXmlWriter;
import com.sun.xml.internal.txw2.output.StaxSerializer;
import com.sun.xml.internal.ws.policy.PolicyConstants;
import com.sun.xml.internal.ws.policy.PolicyException;
import com.sun.xml.internal.ws.policy.privateutil.LocalizationMessages;
import com.sun.xml.internal.ws.policy.privateutil.PolicyLogger;
import com.sun.xml.internal.ws.policy.sourcemodel.wspolicy.NamespaceVersion;
import com.sun.xml.internal.ws.policy.sourcemodel.wspolicy.XmlToken;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamWriter;

/* loaded from: rt.jar:com/sun/xml/internal/ws/policy/sourcemodel/XmlPolicyModelMarshaller.class */
public final class XmlPolicyModelMarshaller extends PolicyModelMarshaller {
    private static final PolicyLogger LOGGER = PolicyLogger.getLogger((Class<?>) XmlPolicyModelMarshaller.class);
    private final boolean marshallInvisible;

    XmlPolicyModelMarshaller(boolean marshallInvisible) {
        this.marshallInvisible = marshallInvisible;
    }

    @Override // com.sun.xml.internal.ws.policy.sourcemodel.PolicyModelMarshaller
    public void marshal(PolicySourceModel model, Object storage) throws PolicyException {
        if (storage instanceof StaxSerializer) {
            marshal(model, (StaxSerializer) storage);
        } else if (storage instanceof TypedXmlWriter) {
            marshal(model, (TypedXmlWriter) storage);
        } else {
            if (storage instanceof XMLStreamWriter) {
                marshal(model, (XMLStreamWriter) storage);
                return;
            }
            throw ((PolicyException) LOGGER.logSevereException(new PolicyException(LocalizationMessages.WSP_0022_STORAGE_TYPE_NOT_SUPPORTED(storage.getClass().getName()))));
        }
    }

    @Override // com.sun.xml.internal.ws.policy.sourcemodel.PolicyModelMarshaller
    public void marshal(Collection<PolicySourceModel> models, Object storage) throws PolicyException {
        for (PolicySourceModel model : models) {
            marshal(model, storage);
        }
    }

    private void marshal(PolicySourceModel model, StaxSerializer writer) throws PolicyException {
        TypedXmlWriter policy = TXW.create(model.getNamespaceVersion().asQName(XmlToken.Policy), TypedXmlWriter.class, writer);
        marshalDefaultPrefixes(model, policy);
        marshalPolicyAttributes(model, policy);
        marshal(model.getNamespaceVersion(), model.getRootNode(), policy);
        policy.commit();
    }

    private void marshal(PolicySourceModel model, TypedXmlWriter writer) throws PolicyException {
        TypedXmlWriter policy = writer._element(model.getNamespaceVersion().asQName(XmlToken.Policy), (Class<TypedXmlWriter>) TypedXmlWriter.class);
        marshalDefaultPrefixes(model, policy);
        marshalPolicyAttributes(model, policy);
        marshal(model.getNamespaceVersion(), model.getRootNode(), policy);
    }

    private void marshal(PolicySourceModel model, XMLStreamWriter writer) throws PolicyException {
        StaxSerializer serializer = new StaxSerializer(writer);
        TypedXmlWriter policy = TXW.create(model.getNamespaceVersion().asQName(XmlToken.Policy), TypedXmlWriter.class, serializer);
        marshalDefaultPrefixes(model, policy);
        marshalPolicyAttributes(model, policy);
        marshal(model.getNamespaceVersion(), model.getRootNode(), policy);
        policy.commit();
        serializer.flush();
    }

    private static void marshalPolicyAttributes(PolicySourceModel model, TypedXmlWriter writer) {
        String policyId = model.getPolicyId();
        if (policyId != null) {
            writer._attribute(PolicyConstants.WSU_ID, policyId);
        }
        String policyName = model.getPolicyName();
        if (policyName != null) {
            writer._attribute(model.getNamespaceVersion().asQName(XmlToken.Name), policyName);
        }
    }

    private void marshal(NamespaceVersion nsVersion, ModelNode rootNode, TypedXmlWriter writer) {
        TypedXmlWriter child;
        Iterator<ModelNode> it = rootNode.iterator();
        while (it.hasNext()) {
            ModelNode node = it.next();
            AssertionData data = node.getNodeData();
            if (this.marshallInvisible || data == null || !data.isPrivateAttributeSet()) {
                if (data == null) {
                    child = writer._element(nsVersion.asQName(node.getType().getXmlToken()), (Class<TypedXmlWriter>) TypedXmlWriter.class);
                } else {
                    child = writer._element(data.getName(), (Class<TypedXmlWriter>) TypedXmlWriter.class);
                    String value = data.getValue();
                    if (value != null) {
                        child._pcdata(value);
                    }
                    if (data.isOptionalAttributeSet()) {
                        child._attribute(nsVersion.asQName(XmlToken.Optional), Boolean.TRUE);
                    }
                    if (data.isIgnorableAttributeSet()) {
                        child._attribute(nsVersion.asQName(XmlToken.Ignorable), Boolean.TRUE);
                    }
                    for (Map.Entry<QName, String> entry : data.getAttributesSet()) {
                        child._attribute(entry.getKey(), entry.getValue());
                    }
                }
                marshal(nsVersion, node, child);
            }
        }
    }

    private void marshalDefaultPrefixes(PolicySourceModel model, TypedXmlWriter writer) throws PolicyException {
        Map<String, String> nsMap = model.getNamespaceToPrefixMapping();
        if (!this.marshallInvisible && nsMap.containsKey(PolicyConstants.SUN_POLICY_NAMESPACE_URI)) {
            nsMap.remove(PolicyConstants.SUN_POLICY_NAMESPACE_URI);
        }
        for (Map.Entry<String, String> nsMappingEntry : nsMap.entrySet()) {
            writer._namespace(nsMappingEntry.getKey(), nsMappingEntry.getValue());
        }
    }
}
