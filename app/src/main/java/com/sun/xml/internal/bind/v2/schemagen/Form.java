package com.sun.xml.internal.bind.v2.schemagen;

import com.sun.org.apache.xerces.internal.impl.xs.SchemaSymbols;
import com.sun.xml.internal.bind.v2.schemagen.xmlschema.LocalAttribute;
import com.sun.xml.internal.bind.v2.schemagen.xmlschema.LocalElement;
import com.sun.xml.internal.bind.v2.schemagen.xmlschema.Schema;
import com.sun.xml.internal.txw2.TypedXmlWriter;
import javax.xml.bind.annotation.XmlNsForm;
import javax.xml.namespace.QName;

/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/schemagen/Form.class */
enum Form {
    QUALIFIED(XmlNsForm.QUALIFIED, true) { // from class: com.sun.xml.internal.bind.v2.schemagen.Form.1
        @Override // com.sun.xml.internal.bind.v2.schemagen.Form
        void declare(String attName, Schema schema) {
            schema._attribute(attName, SchemaSymbols.ATTVAL_QUALIFIED);
        }
    },
    UNQUALIFIED(XmlNsForm.UNQUALIFIED, false) { // from class: com.sun.xml.internal.bind.v2.schemagen.Form.2
        @Override // com.sun.xml.internal.bind.v2.schemagen.Form
        void declare(String attName, Schema schema) {
            schema._attribute(attName, SchemaSymbols.ATTVAL_UNQUALIFIED);
        }
    },
    UNSET(XmlNsForm.UNSET, false) { // from class: com.sun.xml.internal.bind.v2.schemagen.Form.3
        @Override // com.sun.xml.internal.bind.v2.schemagen.Form
        void declare(String attName, Schema schema) {
        }
    };

    private final XmlNsForm xnf;
    public final boolean isEffectivelyQualified;

    abstract void declare(String str, Schema schema);

    Form(XmlNsForm xnf, boolean effectivelyQualified) {
        this.xnf = xnf;
        this.isEffectivelyQualified = effectivelyQualified;
    }

    public void writeForm(LocalElement e2, QName tagName) {
        _writeForm(e2, tagName);
    }

    public void writeForm(LocalAttribute a2, QName tagName) {
        _writeForm(a2, tagName);
    }

    private void _writeForm(TypedXmlWriter e2, QName tagName) {
        boolean qualified = tagName.getNamespaceURI().length() > 0;
        if (qualified && this != QUALIFIED) {
            e2._attribute("form", SchemaSymbols.ATTVAL_QUALIFIED);
        } else if (!qualified && this == QUALIFIED) {
            e2._attribute("form", SchemaSymbols.ATTVAL_UNQUALIFIED);
        }
    }

    public static Form get(XmlNsForm xnf) {
        for (Form v2 : values()) {
            if (v2.xnf == xnf) {
                return v2;
            }
        }
        throw new IllegalArgumentException();
    }
}
