package com.sun.xml.internal.ws.addressing;

import com.sun.istack.internal.Nullable;
import com.sun.xml.internal.ws.addressing.model.InvalidAddressingHeaderException;
import com.sun.xml.internal.ws.addressing.model.MissingAddressingHeaderException;
import com.sun.xml.internal.ws.api.SOAPVersion;
import com.sun.xml.internal.ws.api.WSBinding;
import com.sun.xml.internal.ws.api.addressing.AddressingVersion;
import com.sun.xml.internal.ws.api.message.AddressingUtils;
import com.sun.xml.internal.ws.api.message.Packet;
import com.sun.xml.internal.ws.api.model.JavaMethod;
import com.sun.xml.internal.ws.api.model.SEIModel;
import com.sun.xml.internal.ws.api.model.WSDLOperationMapping;
import com.sun.xml.internal.ws.api.model.wsdl.WSDLBoundOperation;
import com.sun.xml.internal.ws.api.model.wsdl.WSDLFault;
import com.sun.xml.internal.ws.api.model.wsdl.WSDLOperation;
import com.sun.xml.internal.ws.api.model.wsdl.WSDLOutput;
import com.sun.xml.internal.ws.api.model.wsdl.WSDLPort;
import com.sun.xml.internal.ws.model.CheckedExceptionImpl;
import com.sun.xml.internal.ws.model.JavaMethodImpl;
import javax.xml.namespace.QName;
import javax.xml.soap.Detail;
import javax.xml.soap.SOAPConstants;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFactory;
import javax.xml.soap.SOAPFault;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.WebServiceException;
import org.w3c.dom.Element;

/* loaded from: rt.jar:com/sun/xml/internal/ws/addressing/WsaTubeHelper.class */
public abstract class WsaTubeHelper {
    protected SEIModel seiModel;
    protected WSDLPort wsdlPort;
    protected WSBinding binding;
    protected final SOAPVersion soapVer;
    protected final AddressingVersion addVer;

    public abstract void getProblemActionDetail(String str, Element element);

    public abstract void getInvalidMapDetail(QName qName, Element element);

    public abstract void getMapRequiredDetail(QName qName, Element element);

    public WsaTubeHelper(WSBinding binding, SEIModel seiModel, WSDLPort wsdlPort) {
        this.binding = binding;
        this.wsdlPort = wsdlPort;
        this.seiModel = seiModel;
        this.soapVer = binding.getSOAPVersion();
        this.addVer = binding.getAddressingVersion();
    }

    public String getFaultAction(Packet requestPacket, Packet responsePacket) {
        WSDLOperationMapping wsdlOp;
        String action = null;
        if (this.seiModel != null) {
            action = getFaultActionFromSEIModel(requestPacket, responsePacket);
        }
        if (action != null) {
            return action;
        }
        String action2 = this.addVer.getDefaultFaultAction();
        if (this.wsdlPort != null && (wsdlOp = requestPacket.getWSDLOperationMapping()) != null) {
            WSDLBoundOperation wbo = wsdlOp.getWSDLBoundOperation();
            return getFaultAction(wbo, responsePacket);
        }
        return action2;
    }

    String getFaultActionFromSEIModel(Packet requestPacket, Packet responsePacket) {
        Detail detail;
        if (this.seiModel == null || this.wsdlPort == null) {
            return null;
        }
        try {
            SOAPMessage sm = responsePacket.getMessage().copy().readAsSOAPMessage();
            if (sm != null && sm.getSOAPBody() != null && sm.getSOAPBody().getFault() != null && (detail = sm.getSOAPBody().getFault().getDetail()) != null) {
                String ns = detail.getFirstChild().getNamespaceURI();
                String name = detail.getFirstChild().getLocalName();
                WSDLOperationMapping wsdlOp = requestPacket.getWSDLOperationMapping();
                JavaMethodImpl jm = wsdlOp != null ? (JavaMethodImpl) wsdlOp.getJavaMethod() : null;
                if (jm != null) {
                    for (CheckedExceptionImpl ce : jm.getCheckedExceptions()) {
                        if (ce.getDetailType().tagName.getLocalPart().equals(name) && ce.getDetailType().tagName.getNamespaceURI().equals(ns)) {
                            return ce.getFaultAction();
                        }
                    }
                }
                return null;
            }
            return null;
        } catch (SOAPException e2) {
            throw new WebServiceException(e2);
        }
    }

    String getFaultAction(@Nullable WSDLBoundOperation wbo, Packet responsePacket) {
        String action = AddressingUtils.getAction(responsePacket.getMessage().getHeaders(), this.addVer, this.soapVer);
        if (action != null) {
            return action;
        }
        String action2 = this.addVer.getDefaultFaultAction();
        if (wbo == null) {
            return action2;
        }
        try {
            SOAPMessage sm = responsePacket.getMessage().copy().readAsSOAPMessage();
            if (sm == null) {
                return action2;
            }
            if (sm.getSOAPBody() == null) {
                return action2;
            }
            if (sm.getSOAPBody().getFault() == null) {
                return action2;
            }
            Detail detail = sm.getSOAPBody().getFault().getDetail();
            if (detail == null) {
                return action2;
            }
            String ns = detail.getFirstChild().getNamespaceURI();
            String name = detail.getFirstChild().getLocalName();
            WSDLOperation o2 = wbo.getOperation();
            WSDLFault fault = o2.getFault(new QName(ns, name));
            if (fault == null) {
                return action2;
            }
            return fault.getAction();
        } catch (SOAPException e2) {
            throw new WebServiceException(e2);
        }
    }

    public String getInputAction(Packet packet) {
        WSDLOperationMapping wsdlOp;
        String action = null;
        if (this.wsdlPort != null && (wsdlOp = packet.getWSDLOperationMapping()) != null) {
            WSDLBoundOperation wbo = wsdlOp.getWSDLBoundOperation();
            WSDLOperation op = wbo.getOperation();
            action = op.getInput().getAction();
        }
        return action;
    }

    public String getEffectiveInputAction(Packet packet) {
        String action;
        WSDLOperationMapping wsdlOp;
        if (packet.soapAction != null && !packet.soapAction.equals("")) {
            return packet.soapAction;
        }
        if (this.wsdlPort != null && (wsdlOp = packet.getWSDLOperationMapping()) != null) {
            WSDLBoundOperation wbo = wsdlOp.getWSDLBoundOperation();
            WSDLOperation op = wbo.getOperation();
            action = op.getInput().getAction();
        } else {
            action = packet.soapAction;
        }
        return action;
    }

    public boolean isInputActionDefault(Packet packet) {
        WSDLOperationMapping wsdlOp;
        if (this.wsdlPort == null || (wsdlOp = packet.getWSDLOperationMapping()) == null) {
            return false;
        }
        WSDLBoundOperation wbo = wsdlOp.getWSDLBoundOperation();
        WSDLOperation op = wbo.getOperation();
        return op.getInput().isDefaultAction();
    }

    public String getSOAPAction(Packet packet) {
        WSDLOperationMapping wsdlOp;
        if (packet == null || packet.getMessage() == null) {
            return "";
        }
        if (this.wsdlPort != null && (wsdlOp = packet.getWSDLOperationMapping()) != null) {
            WSDLBoundOperation op = wsdlOp.getWSDLBoundOperation();
            String action = op.getSOAPAction();
            return action;
        }
        return "";
    }

    public String getOutputAction(Packet packet) {
        JavaMethodImpl jm;
        WSDLOperationMapping wsdlOp = packet.getWSDLOperationMapping();
        if (wsdlOp != null) {
            JavaMethod javaMethod = wsdlOp.getJavaMethod();
            if (javaMethod != null && (jm = (JavaMethodImpl) javaMethod) != null && jm.getOutputAction() != null && !jm.getOutputAction().equals("")) {
                return jm.getOutputAction();
            }
            WSDLBoundOperation wbo = wsdlOp.getWSDLBoundOperation();
            if (wbo != null) {
                return getOutputAction(wbo);
            }
        }
        return null;
    }

    String getOutputAction(@Nullable WSDLBoundOperation wbo) {
        WSDLOutput op;
        String action = AddressingVersion.UNSET_OUTPUT_ACTION;
        if (wbo != null && (op = wbo.getOperation().getOutput()) != null) {
            action = op.getAction();
        }
        return action;
    }

    public SOAPFault createInvalidAddressingHeaderFault(InvalidAddressingHeaderException e2, AddressingVersion av2) {
        SOAPFault fault;
        QName name = e2.getProblemHeader();
        QName subsubcode = e2.getSubsubcode();
        QName subcode = av2.invalidMapTag;
        String faultstring = String.format(av2.getInvalidMapText(), name, subsubcode);
        try {
            if (this.soapVer == SOAPVersion.SOAP_12) {
                SOAPFactory factory = SOAPVersion.SOAP_12.getSOAPFactory();
                fault = factory.createFault();
                fault.setFaultCode(SOAPConstants.SOAP_SENDER_FAULT);
                fault.appendFaultSubcode(subcode);
                fault.appendFaultSubcode(subsubcode);
                getInvalidMapDetail(name, fault.addDetail());
            } else {
                SOAPFactory factory2 = SOAPVersion.SOAP_11.getSOAPFactory();
                fault = factory2.createFault();
                fault.setFaultCode(subsubcode);
            }
            fault.setFaultString(faultstring);
            return fault;
        } catch (SOAPException se) {
            throw new WebServiceException(se);
        }
    }

    public SOAPFault newMapRequiredFault(MissingAddressingHeaderException e2) {
        SOAPFault fault;
        QName subcode = this.addVer.mapRequiredTag;
        QName subsubcode = this.addVer.mapRequiredTag;
        String faultstring = this.addVer.getMapRequiredText();
        try {
            if (this.soapVer == SOAPVersion.SOAP_12) {
                SOAPFactory factory = SOAPVersion.SOAP_12.getSOAPFactory();
                fault = factory.createFault();
                fault.setFaultCode(SOAPConstants.SOAP_SENDER_FAULT);
                fault.appendFaultSubcode(subcode);
                fault.appendFaultSubcode(subsubcode);
                getMapRequiredDetail(e2.getMissingHeaderQName(), fault.addDetail());
            } else {
                SOAPFactory factory2 = SOAPVersion.SOAP_11.getSOAPFactory();
                fault = factory2.createFault();
                fault.setFaultCode(subsubcode);
            }
            fault.setFaultString(faultstring);
            return fault;
        } catch (SOAPException se) {
            throw new WebServiceException(se);
        }
    }
}
