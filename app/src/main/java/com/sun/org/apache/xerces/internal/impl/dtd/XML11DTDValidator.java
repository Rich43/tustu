package com.sun.org.apache.xerces.internal.impl.dtd;

import com.sun.org.apache.xerces.internal.xni.parser.XMLComponentManager;
import com.sun.org.apache.xerces.internal.xni.parser.XMLConfigurationException;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/impl/dtd/XML11DTDValidator.class */
public class XML11DTDValidator extends XMLDTDValidator {
    protected static final String DTD_VALIDATOR_PROPERTY = "http://apache.org/xml/properties/internal/validator/dtd";

    @Override // com.sun.org.apache.xerces.internal.impl.dtd.XMLDTDValidator, com.sun.org.apache.xerces.internal.xni.parser.XMLComponent
    public void reset(XMLComponentManager manager) throws XMLConfigurationException {
        XMLDTDValidator curr = (XMLDTDValidator) manager.getProperty(DTD_VALIDATOR_PROPERTY);
        if (curr != null && curr != this) {
            this.fGrammarBucket = curr.getGrammarBucket();
        }
        super.reset(manager);
    }

    @Override // com.sun.org.apache.xerces.internal.impl.dtd.XMLDTDValidator
    protected void init() {
        if (this.fValidation || this.fDynamicValidation) {
            super.init();
            try {
                this.fValID = this.fDatatypeValidatorFactory.getBuiltInDV("XML11ID");
                this.fValIDRef = this.fDatatypeValidatorFactory.getBuiltInDV("XML11IDREF");
                this.fValIDRefs = this.fDatatypeValidatorFactory.getBuiltInDV("XML11IDREFS");
                this.fValNMTOKEN = this.fDatatypeValidatorFactory.getBuiltInDV("XML11NMTOKEN");
                this.fValNMTOKENS = this.fDatatypeValidatorFactory.getBuiltInDV("XML11NMTOKENS");
            } catch (Exception e2) {
                e2.printStackTrace(System.err);
            }
        }
    }
}
