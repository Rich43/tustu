package com.sun.xml.internal.ws.policy.sourcemodel;

import com.sun.xml.internal.ws.policy.PolicyConstants;
import com.sun.xml.internal.ws.policy.PolicyException;
import com.sun.xml.internal.ws.policy.privateutil.LocalizationMessages;
import com.sun.xml.internal.ws.policy.privateutil.PolicyLogger;
import com.sun.xml.internal.ws.policy.sourcemodel.ModelNode;
import com.sun.xml.internal.ws.policy.sourcemodel.wspolicy.NamespaceVersion;
import com.sun.xml.internal.ws.policy.sourcemodel.wspolicy.XmlToken;
import java.io.Reader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;

/* loaded from: rt.jar:com/sun/xml/internal/ws/policy/sourcemodel/XmlPolicyModelUnmarshaller.class */
public class XmlPolicyModelUnmarshaller extends PolicyModelUnmarshaller {
    private static final PolicyLogger LOGGER = PolicyLogger.getLogger((Class<?>) XmlPolicyModelUnmarshaller.class);

    protected XmlPolicyModelUnmarshaller() {
    }

    /* JADX WARN: Code restructure failed: missing block: B:21:0x00f5, code lost:
    
        return r9;
     */
    @Override // com.sun.xml.internal.ws.policy.sourcemodel.PolicyModelUnmarshaller
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public com.sun.xml.internal.ws.policy.sourcemodel.PolicySourceModel unmarshalModel(java.lang.Object r7) throws java.lang.UnsupportedOperationException, java.lang.IllegalArgumentException, com.sun.xml.internal.ws.policy.PolicyException {
        /*
            r6 = this;
            r0 = r6
            r1 = r7
            javax.xml.stream.XMLEventReader r0 = r0.createXMLEventReader(r1)
            r8 = r0
            r0 = 0
            r9 = r0
        L8:
            r0 = r8
            boolean r0 = r0.hasNext()
            if (r0 == 0) goto Lf4
            r0 = r8
            javax.xml.stream.events.XMLEvent r0 = r0.peek()     // Catch: javax.xml.stream.XMLStreamException -> Ldc
            r10 = r0
            r0 = r10
            int r0 = r0.getEventType()     // Catch: javax.xml.stream.XMLStreamException -> Ldc
            switch(r0) {
                case 1: goto L70;
                case 2: goto Lc5;
                case 3: goto Lc5;
                case 4: goto L56;
                case 5: goto L4c;
                case 6: goto Lc5;
                case 7: goto L4c;
                default: goto Lc5;
            }     // Catch: javax.xml.stream.XMLStreamException -> Ldc
        L4c:
            r0 = r8
            javax.xml.stream.events.XMLEvent r0 = r0.nextEvent()     // Catch: javax.xml.stream.XMLStreamException -> Ldc
            goto Ld9
        L56:
            r0 = r6
            com.sun.xml.internal.ws.policy.sourcemodel.ModelNode$Type r1 = com.sun.xml.internal.ws.policy.sourcemodel.ModelNode.Type.POLICY     // Catch: javax.xml.stream.XMLStreamException -> Ldc
            r2 = r10
            javax.xml.stream.events.Characters r2 = r2.asCharacters()     // Catch: javax.xml.stream.XMLStreamException -> Ldc
            r3 = 0
            java.lang.StringBuilder r0 = r0.processCharacters(r1, r2, r3)     // Catch: javax.xml.stream.XMLStreamException -> Ldc
            r0 = r8
            javax.xml.stream.events.XMLEvent r0 = r0.nextEvent()     // Catch: javax.xml.stream.XMLStreamException -> Ldc
            goto Ld9
        L70:
            r0 = r10
            javax.xml.stream.events.StartElement r0 = r0.asStartElement()     // Catch: javax.xml.stream.XMLStreamException -> Ldc
            javax.xml.namespace.QName r0 = r0.getName()     // Catch: javax.xml.stream.XMLStreamException -> Ldc
            com.sun.xml.internal.ws.policy.sourcemodel.wspolicy.XmlToken r0 = com.sun.xml.internal.ws.policy.sourcemodel.wspolicy.NamespaceVersion.resolveAsToken(r0)     // Catch: javax.xml.stream.XMLStreamException -> Ldc
            com.sun.xml.internal.ws.policy.sourcemodel.wspolicy.XmlToken r1 = com.sun.xml.internal.ws.policy.sourcemodel.wspolicy.XmlToken.Policy     // Catch: javax.xml.stream.XMLStreamException -> Ldc
            if (r0 != r1) goto Lb1
            r0 = r8
            javax.xml.stream.events.XMLEvent r0 = r0.nextEvent()     // Catch: javax.xml.stream.XMLStreamException -> Ldc
            javax.xml.stream.events.StartElement r0 = r0.asStartElement()     // Catch: javax.xml.stream.XMLStreamException -> Ldc
            r11 = r0
            r0 = r6
            r1 = r11
            com.sun.xml.internal.ws.policy.sourcemodel.PolicySourceModel r0 = r0.initializeNewModel(r1)     // Catch: javax.xml.stream.XMLStreamException -> Ldc
            r9 = r0
            r0 = r6
            r1 = r9
            com.sun.xml.internal.ws.policy.sourcemodel.wspolicy.NamespaceVersion r1 = r1.getNamespaceVersion()     // Catch: javax.xml.stream.XMLStreamException -> Ldc
            r2 = r9
            com.sun.xml.internal.ws.policy.sourcemodel.ModelNode r2 = r2.getRootNode()     // Catch: javax.xml.stream.XMLStreamException -> Ldc
            r3 = r11
            javax.xml.namespace.QName r3 = r3.getName()     // Catch: javax.xml.stream.XMLStreamException -> Ldc
            r4 = r8
            java.lang.String r0 = r0.unmarshalNodeContent(r1, r2, r3, r4)     // Catch: javax.xml.stream.XMLStreamException -> Ldc
            goto Lf4
        Lb1:
            com.sun.xml.internal.ws.policy.privateutil.PolicyLogger r0 = com.sun.xml.internal.ws.policy.sourcemodel.XmlPolicyModelUnmarshaller.LOGGER     // Catch: javax.xml.stream.XMLStreamException -> Ldc
            com.sun.xml.internal.ws.policy.PolicyException r1 = new com.sun.xml.internal.ws.policy.PolicyException     // Catch: javax.xml.stream.XMLStreamException -> Ldc
            r2 = r1
            java.lang.String r3 = com.sun.xml.internal.ws.policy.privateutil.LocalizationMessages.WSP_0048_POLICY_ELEMENT_EXPECTED_FIRST()     // Catch: javax.xml.stream.XMLStreamException -> Ldc
            r2.<init>(r3)     // Catch: javax.xml.stream.XMLStreamException -> Ldc
            java.lang.Throwable r0 = r0.logSevereException(r1)     // Catch: javax.xml.stream.XMLStreamException -> Ldc
            com.sun.xml.internal.ws.policy.PolicyException r0 = (com.sun.xml.internal.ws.policy.PolicyException) r0     // Catch: javax.xml.stream.XMLStreamException -> Ldc
            throw r0     // Catch: javax.xml.stream.XMLStreamException -> Ldc
        Lc5:
            com.sun.xml.internal.ws.policy.privateutil.PolicyLogger r0 = com.sun.xml.internal.ws.policy.sourcemodel.XmlPolicyModelUnmarshaller.LOGGER     // Catch: javax.xml.stream.XMLStreamException -> Ldc
            com.sun.xml.internal.ws.policy.PolicyException r1 = new com.sun.xml.internal.ws.policy.PolicyException     // Catch: javax.xml.stream.XMLStreamException -> Ldc
            r2 = r1
            java.lang.String r3 = com.sun.xml.internal.ws.policy.privateutil.LocalizationMessages.WSP_0048_POLICY_ELEMENT_EXPECTED_FIRST()     // Catch: javax.xml.stream.XMLStreamException -> Ldc
            r2.<init>(r3)     // Catch: javax.xml.stream.XMLStreamException -> Ldc
            java.lang.Throwable r0 = r0.logSevereException(r1)     // Catch: javax.xml.stream.XMLStreamException -> Ldc
            com.sun.xml.internal.ws.policy.PolicyException r0 = (com.sun.xml.internal.ws.policy.PolicyException) r0     // Catch: javax.xml.stream.XMLStreamException -> Ldc
            throw r0     // Catch: javax.xml.stream.XMLStreamException -> Ldc
        Ld9:
            goto L8
        Ldc:
            r10 = move-exception
            com.sun.xml.internal.ws.policy.privateutil.PolicyLogger r0 = com.sun.xml.internal.ws.policy.sourcemodel.XmlPolicyModelUnmarshaller.LOGGER
            com.sun.xml.internal.ws.policy.PolicyException r1 = new com.sun.xml.internal.ws.policy.PolicyException
            r2 = r1
            java.lang.String r3 = com.sun.xml.internal.ws.policy.privateutil.LocalizationMessages.WSP_0068_FAILED_TO_UNMARSHALL_POLICY_EXPRESSION()
            r4 = r10
            r2.<init>(r3, r4)
            java.lang.Throwable r0 = r0.logSevereException(r1)
            com.sun.xml.internal.ws.policy.PolicyException r0 = (com.sun.xml.internal.ws.policy.PolicyException) r0
            throw r0
        Lf4:
            r0 = r9
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sun.xml.internal.ws.policy.sourcemodel.XmlPolicyModelUnmarshaller.unmarshalModel(java.lang.Object):com.sun.xml.internal.ws.policy.sourcemodel.PolicySourceModel");
    }

    protected PolicySourceModel createSourceModel(NamespaceVersion nsVersion, String id, String name) {
        return PolicySourceModel.createPolicySourceModel(nsVersion, id, name);
    }

    private PolicySourceModel initializeNewModel(StartElement element) throws XMLStreamException, PolicyException {
        NamespaceVersion nsVersion = NamespaceVersion.resolveVersion(element.getName().getNamespaceURI());
        Attribute policyName = getAttributeByName(element, nsVersion.asQName(XmlToken.Name));
        Attribute xmlId = getAttributeByName(element, PolicyConstants.XML_ID);
        Attribute policyId = getAttributeByName(element, PolicyConstants.WSU_ID);
        if (policyId == null) {
            policyId = xmlId;
        } else if (xmlId != null) {
            throw ((PolicyException) LOGGER.logSevereException(new PolicyException(LocalizationMessages.WSP_0058_MULTIPLE_POLICY_IDS_NOT_ALLOWED())));
        }
        PolicySourceModel model = createSourceModel(nsVersion, policyId == null ? null : policyId.getValue(), policyName == null ? null : policyName.getValue());
        return model;
    }

    private ModelNode addNewChildNode(NamespaceVersion nsVersion, ModelNode parentNode, StartElement childElement) throws UnsupportedOperationException, PolicyException {
        ModelNode childNode;
        PolicyReferenceData refData;
        QName childElementName = childElement.getName();
        if (parentNode.getType() == ModelNode.Type.ASSERTION_PARAMETER_NODE) {
            childNode = parentNode.createChildAssertionParameterNode();
        } else {
            XmlToken token = NamespaceVersion.resolveAsToken(childElementName);
            switch (token) {
                case Policy:
                    childNode = parentNode.createChildPolicyNode();
                    break;
                case All:
                    childNode = parentNode.createChildAllNode();
                    break;
                case ExactlyOne:
                    childNode = parentNode.createChildExactlyOneNode();
                    break;
                case PolicyReference:
                    Attribute uri = getAttributeByName(childElement, nsVersion.asQName(XmlToken.Uri));
                    if (uri == null) {
                        throw ((PolicyException) LOGGER.logSevereException(new PolicyException(LocalizationMessages.WSP_0040_POLICY_REFERENCE_URI_ATTR_NOT_FOUND())));
                    }
                    try {
                        URI reference = new URI(uri.getValue());
                        Attribute digest = getAttributeByName(childElement, nsVersion.asQName(XmlToken.Digest));
                        if (digest == null) {
                            refData = new PolicyReferenceData(reference);
                        } else {
                            Attribute digestAlgorithm = getAttributeByName(childElement, nsVersion.asQName(XmlToken.DigestAlgorithm));
                            URI algorithmRef = null;
                            if (digestAlgorithm != null) {
                                algorithmRef = new URI(digestAlgorithm.getValue());
                            }
                            refData = new PolicyReferenceData(reference, digest.getValue(), algorithmRef);
                        }
                        childNode = parentNode.createChildPolicyReferenceNode(refData);
                        break;
                    } catch (URISyntaxException e2) {
                        throw ((PolicyException) LOGGER.logSevereException(new PolicyException(LocalizationMessages.WSP_0012_UNABLE_TO_UNMARSHALL_POLICY_MALFORMED_URI(), e2)));
                    }
                default:
                    if (parentNode.isDomainSpecific()) {
                        childNode = parentNode.createChildAssertionParameterNode();
                        break;
                    } else {
                        childNode = parentNode.createChildAssertionNode();
                        break;
                    }
            }
        }
        return childNode;
    }

    private void parseAssertionData(NamespaceVersion nsVersion, String value, ModelNode childNode, StartElement childElement) throws IllegalArgumentException, PolicyException {
        Map<QName, String> attributeMap = new HashMap<>();
        boolean optional = false;
        boolean ignorable = false;
        Iterator iterator = childElement.getAttributes();
        while (iterator.hasNext()) {
            Attribute nextAttribute = (Attribute) iterator.next();
            QName name = nextAttribute.getName();
            if (attributeMap.containsKey(name)) {
                throw ((PolicyException) LOGGER.logSevereException(new PolicyException(LocalizationMessages.WSP_0059_MULTIPLE_ATTRS_WITH_SAME_NAME_DETECTED_FOR_ASSERTION(nextAttribute.getName(), childElement.getName()))));
            }
            if (nsVersion.asQName(XmlToken.Optional).equals(name)) {
                optional = parseBooleanValue(nextAttribute.getValue());
            } else if (nsVersion.asQName(XmlToken.Ignorable).equals(name)) {
                ignorable = parseBooleanValue(nextAttribute.getValue());
            } else {
                attributeMap.put(name, nextAttribute.getValue());
            }
        }
        AssertionData nodeData = new AssertionData(childElement.getName(), value, attributeMap, childNode.getType(), optional, ignorable);
        if (nodeData.containsAttribute(PolicyConstants.VISIBILITY_ATTRIBUTE)) {
            String visibilityValue = nodeData.getAttributeValue(PolicyConstants.VISIBILITY_ATTRIBUTE);
            if (!PolicyConstants.VISIBILITY_VALUE_PRIVATE.equals(visibilityValue)) {
                throw ((PolicyException) LOGGER.logSevereException(new PolicyException(LocalizationMessages.WSP_0004_UNEXPECTED_VISIBILITY_ATTR_VALUE(visibilityValue))));
            }
        }
        childNode.setOrReplaceNodeData(nodeData);
    }

    private Attribute getAttributeByName(StartElement element, QName attributeName) {
        Attribute attribute = element.getAttributeByName(attributeName);
        if (attribute == null) {
            String localAttributeName = attributeName.getLocalPart();
            Iterator iterator = element.getAttributes();
            while (true) {
                if (!iterator.hasNext()) {
                    break;
                }
                Attribute nextAttribute = (Attribute) iterator.next();
                QName aName = nextAttribute.getName();
                boolean attributeFoundByWorkaround = aName.equals(attributeName) || (aName.getLocalPart().equals(localAttributeName) && (aName.getPrefix() == null || "".equals(aName.getPrefix())));
                if (attributeFoundByWorkaround) {
                    attribute = nextAttribute;
                    break;
                }
            }
        }
        return attribute;
    }

    /* JADX WARN: Code restructure failed: missing block: B:20:0x00d4, code lost:
    
        if (r11 != null) goto L22;
     */
    /* JADX WARN: Code restructure failed: missing block: B:21:0x00d7, code lost:
    
        return null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:23:0x00e3, code lost:
    
        return r11.toString().trim();
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private java.lang.String unmarshalNodeContent(com.sun.xml.internal.ws.policy.sourcemodel.wspolicy.NamespaceVersion r7, com.sun.xml.internal.ws.policy.sourcemodel.ModelNode r8, javax.xml.namespace.QName r9, javax.xml.stream.XMLEventReader r10) throws java.lang.UnsupportedOperationException, java.lang.IllegalArgumentException, com.sun.xml.internal.ws.policy.PolicyException {
        /*
            r6 = this;
            r0 = 0
            r11 = r0
        L3:
            r0 = r10
            boolean r0 = r0.hasNext()
            if (r0 == 0) goto Ld2
            r0 = r10
            javax.xml.stream.events.XMLEvent r0 = r0.nextEvent()     // Catch: javax.xml.stream.XMLStreamException -> Lba
            r12 = r0
            r0 = r12
            int r0 = r0.getEventType()     // Catch: javax.xml.stream.XMLStreamException -> Lba
            switch(r0) {
                case 1: goto L68;
                case 2: goto L59;
                case 3: goto La3;
                case 4: goto L43;
                case 5: goto L40;
                default: goto La3;
            }     // Catch: javax.xml.stream.XMLStreamException -> Lba
        L40:
            goto Lb7
        L43:
            r0 = r6
            r1 = r8
            com.sun.xml.internal.ws.policy.sourcemodel.ModelNode$Type r1 = r1.getType()     // Catch: javax.xml.stream.XMLStreamException -> Lba
            r2 = r12
            javax.xml.stream.events.Characters r2 = r2.asCharacters()     // Catch: javax.xml.stream.XMLStreamException -> Lba
            r3 = r11
            java.lang.StringBuilder r0 = r0.processCharacters(r1, r2, r3)     // Catch: javax.xml.stream.XMLStreamException -> Lba
            r11 = r0
            goto Lb7
        L59:
            r0 = r6
            r1 = r9
            r2 = r12
            javax.xml.stream.events.EndElement r2 = r2.asEndElement()     // Catch: javax.xml.stream.XMLStreamException -> Lba
            r0.checkEndTagName(r1, r2)     // Catch: javax.xml.stream.XMLStreamException -> Lba
            goto Ld2
        L68:
            r0 = r12
            javax.xml.stream.events.StartElement r0 = r0.asStartElement()     // Catch: javax.xml.stream.XMLStreamException -> Lba
            r13 = r0
            r0 = r6
            r1 = r7
            r2 = r8
            r3 = r13
            com.sun.xml.internal.ws.policy.sourcemodel.ModelNode r0 = r0.addNewChildNode(r1, r2, r3)     // Catch: javax.xml.stream.XMLStreamException -> Lba
            r14 = r0
            r0 = r6
            r1 = r7
            r2 = r14
            r3 = r13
            javax.xml.namespace.QName r3 = r3.getName()     // Catch: javax.xml.stream.XMLStreamException -> Lba
            r4 = r10
            java.lang.String r0 = r0.unmarshalNodeContent(r1, r2, r3, r4)     // Catch: javax.xml.stream.XMLStreamException -> Lba
            r15 = r0
            r0 = r14
            boolean r0 = r0.isDomainSpecific()     // Catch: javax.xml.stream.XMLStreamException -> Lba
            if (r0 == 0) goto Lb7
            r0 = r6
            r1 = r7
            r2 = r15
            r3 = r14
            r4 = r13
            r0.parseAssertionData(r1, r2, r3, r4)     // Catch: javax.xml.stream.XMLStreamException -> Lba
            goto Lb7
        La3:
            com.sun.xml.internal.ws.policy.privateutil.PolicyLogger r0 = com.sun.xml.internal.ws.policy.sourcemodel.XmlPolicyModelUnmarshaller.LOGGER     // Catch: javax.xml.stream.XMLStreamException -> Lba
            com.sun.xml.internal.ws.policy.PolicyException r1 = new com.sun.xml.internal.ws.policy.PolicyException     // Catch: javax.xml.stream.XMLStreamException -> Lba
            r2 = r1
            java.lang.String r3 = com.sun.xml.internal.ws.policy.privateutil.LocalizationMessages.WSP_0011_UNABLE_TO_UNMARSHALL_POLICY_XML_ELEM_EXPECTED()     // Catch: javax.xml.stream.XMLStreamException -> Lba
            r2.<init>(r3)     // Catch: javax.xml.stream.XMLStreamException -> Lba
            java.lang.Throwable r0 = r0.logSevereException(r1)     // Catch: javax.xml.stream.XMLStreamException -> Lba
            com.sun.xml.internal.ws.policy.PolicyException r0 = (com.sun.xml.internal.ws.policy.PolicyException) r0     // Catch: javax.xml.stream.XMLStreamException -> Lba
            throw r0     // Catch: javax.xml.stream.XMLStreamException -> Lba
        Lb7:
            goto L3
        Lba:
            r12 = move-exception
            com.sun.xml.internal.ws.policy.privateutil.PolicyLogger r0 = com.sun.xml.internal.ws.policy.sourcemodel.XmlPolicyModelUnmarshaller.LOGGER
            com.sun.xml.internal.ws.policy.PolicyException r1 = new com.sun.xml.internal.ws.policy.PolicyException
            r2 = r1
            java.lang.String r3 = com.sun.xml.internal.ws.policy.privateutil.LocalizationMessages.WSP_0068_FAILED_TO_UNMARSHALL_POLICY_EXPRESSION()
            r4 = r12
            r2.<init>(r3, r4)
            java.lang.Throwable r0 = r0.logSevereException(r1)
            com.sun.xml.internal.ws.policy.PolicyException r0 = (com.sun.xml.internal.ws.policy.PolicyException) r0
            throw r0
        Ld2:
            r0 = r11
            if (r0 != 0) goto Ldb
            r0 = 0
            goto Le3
        Ldb:
            r0 = r11
            java.lang.String r0 = r0.toString()
            java.lang.String r0 = r0.trim()
        Le3:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sun.xml.internal.ws.policy.sourcemodel.XmlPolicyModelUnmarshaller.unmarshalNodeContent(com.sun.xml.internal.ws.policy.sourcemodel.wspolicy.NamespaceVersion, com.sun.xml.internal.ws.policy.sourcemodel.ModelNode, javax.xml.namespace.QName, javax.xml.stream.XMLEventReader):java.lang.String");
    }

    private XMLEventReader createXMLEventReader(Object storage) throws PolicyException {
        if (storage instanceof XMLEventReader) {
            return (XMLEventReader) storage;
        }
        if (!(storage instanceof Reader)) {
            throw ((PolicyException) LOGGER.logSevereException(new PolicyException(LocalizationMessages.WSP_0022_STORAGE_TYPE_NOT_SUPPORTED(storage.getClass().getName()))));
        }
        try {
            return XMLInputFactory.newInstance().createXMLEventReader((Reader) storage);
        } catch (XMLStreamException e2) {
            throw ((PolicyException) LOGGER.logSevereException(new PolicyException(LocalizationMessages.WSP_0014_UNABLE_TO_INSTANTIATE_READER_FOR_STORAGE(), e2)));
        }
    }

    private void checkEndTagName(QName expected, EndElement element) throws PolicyException {
        QName actual = element.getName();
        if (!expected.equals(actual)) {
            throw ((PolicyException) LOGGER.logSevereException(new PolicyException(LocalizationMessages.WSP_0003_UNMARSHALLING_FAILED_END_TAG_DOES_NOT_MATCH(expected, actual))));
        }
    }

    private StringBuilder processCharacters(ModelNode.Type currentNodeType, Characters characters, StringBuilder currentValueBuffer) throws PolicyException {
        if (characters.isWhiteSpace()) {
            return currentValueBuffer;
        }
        StringBuilder buffer = currentValueBuffer == null ? new StringBuilder() : currentValueBuffer;
        String data = characters.getData();
        if (currentNodeType == ModelNode.Type.ASSERTION || currentNodeType == ModelNode.Type.ASSERTION_PARAMETER_NODE) {
            return buffer.append(data);
        }
        throw ((PolicyException) LOGGER.logSevereException(new PolicyException(LocalizationMessages.WSP_0009_UNEXPECTED_CDATA_ON_SOURCE_MODEL_NODE(currentNodeType, data))));
    }

    private boolean parseBooleanValue(String value) throws PolicyException {
        if ("true".equals(value) || "1".equals(value)) {
            return true;
        }
        if ("false".equals(value) || "0".equals(value)) {
            return false;
        }
        throw ((PolicyException) LOGGER.logSevereException(new PolicyException(LocalizationMessages.WSP_0095_INVALID_BOOLEAN_VALUE(value))));
    }
}
