package com.sun.xml.internal.ws.addressing;

import com.sun.xml.internal.ws.api.WSBinding;
import com.sun.xml.internal.ws.api.model.SEIModel;
import com.sun.xml.internal.ws.api.model.wsdl.WSDLPort;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.namespace.QName;
import javax.xml.ws.WebServiceException;
import org.w3c.dom.Element;

/* loaded from: rt.jar:com/sun/xml/internal/ws/addressing/WsaTubeHelperImpl.class */
public class WsaTubeHelperImpl extends WsaTubeHelper {
    static final JAXBContext jc;

    static {
        try {
            jc = JAXBContext.newInstance(ProblemAction.class, ProblemHeaderQName.class);
        } catch (JAXBException e2) {
            throw new WebServiceException(e2);
        }
    }

    public WsaTubeHelperImpl(WSDLPort wsdlPort, SEIModel seiModel, WSBinding binding) {
        super(binding, seiModel, wsdlPort);
    }

    private Marshaller createMarshaller() throws JAXBException {
        Marshaller marshaller = jc.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
        return marshaller;
    }

    @Override // com.sun.xml.internal.ws.addressing.WsaTubeHelper
    public final void getProblemActionDetail(String action, Element element) {
        ProblemAction pa = new ProblemAction(action);
        try {
            createMarshaller().marshal(pa, element);
        } catch (JAXBException e2) {
            throw new WebServiceException(e2);
        }
    }

    @Override // com.sun.xml.internal.ws.addressing.WsaTubeHelper
    public final void getInvalidMapDetail(QName name, Element element) {
        ProblemHeaderQName phq = new ProblemHeaderQName(name);
        try {
            createMarshaller().marshal(phq, element);
        } catch (JAXBException e2) {
            throw new WebServiceException(e2);
        }
    }

    @Override // com.sun.xml.internal.ws.addressing.WsaTubeHelper
    public final void getMapRequiredDetail(QName name, Element element) {
        getInvalidMapDetail(name, element);
    }
}
