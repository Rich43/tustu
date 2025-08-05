package com.sun.xml.internal.ws.server;

import com.sun.xml.internal.ws.api.SOAPVersion;
import com.sun.xml.internal.ws.api.WSBinding;
import com.sun.xml.internal.ws.api.message.Message;
import com.sun.xml.internal.ws.api.message.Packet;
import com.sun.xml.internal.ws.api.model.SEIModel;
import com.sun.xml.internal.ws.api.model.wsdl.WSDLPort;
import com.sun.xml.internal.ws.api.pipe.NextAction;
import com.sun.xml.internal.ws.api.pipe.Tube;
import com.sun.xml.internal.ws.api.pipe.TubeCloner;
import com.sun.xml.internal.ws.api.pipe.helper.AbstractTubeImpl;
import com.sun.xml.internal.ws.api.server.WSEndpoint;
import com.sun.xml.internal.ws.fault.SOAPFaultBuilder;
import com.sun.xml.internal.ws.model.CheckedExceptionImpl;
import com.sun.xml.internal.ws.util.pipe.AbstractSchemaValidationTube;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.transform.Source;
import javax.xml.validation.Schema;
import javax.xml.validation.Validator;
import javax.xml.ws.WebServiceException;
import org.xml.sax.SAXException;

/* loaded from: rt.jar:com/sun/xml/internal/ws/server/ServerSchemaValidationTube.class */
public class ServerSchemaValidationTube extends AbstractSchemaValidationTube {
    private static final Logger LOGGER = Logger.getLogger(ServerSchemaValidationTube.class.getName());
    private final Schema schema;
    private final Validator validator;
    private final boolean noValidation;
    private final SEIModel seiModel;
    private final WSDLPort wsdlPort;

    public ServerSchemaValidationTube(WSEndpoint endpoint, WSBinding binding, SEIModel seiModel, WSDLPort wsdlPort, Tube next) {
        super(binding, next);
        this.seiModel = seiModel;
        this.wsdlPort = wsdlPort;
        if (endpoint.getServiceDefinition() != null) {
            AbstractSchemaValidationTube.MetadataResolverImpl mdresolver = new AbstractSchemaValidationTube.MetadataResolverImpl(endpoint.getServiceDefinition());
            Source[] sources = getSchemaSources(endpoint.getServiceDefinition(), mdresolver);
            for (Source source : sources) {
                LOGGER.fine("Constructing service validation schema from = " + source.getSystemId());
            }
            if (sources.length != 0) {
                this.noValidation = false;
                this.sf.setResourceResolver(mdresolver);
                try {
                    this.schema = this.sf.newSchema(sources);
                    this.validator = this.schema.newValidator();
                    return;
                } catch (SAXException e2) {
                    throw new WebServiceException(e2);
                }
            }
        }
        this.noValidation = true;
        this.schema = null;
        this.validator = null;
    }

    @Override // com.sun.xml.internal.ws.util.pipe.AbstractSchemaValidationTube
    protected Validator getValidator() {
        return this.validator;
    }

    @Override // com.sun.xml.internal.ws.util.pipe.AbstractSchemaValidationTube
    protected boolean isNoValidation() {
        return this.noValidation;
    }

    @Override // com.sun.xml.internal.ws.api.pipe.helper.AbstractFilterTubeImpl, com.sun.xml.internal.ws.api.pipe.Tube
    public NextAction processRequest(Packet request) {
        if (isNoValidation() || !this.feature.isInbound() || !request.getMessage().hasPayload() || request.getMessage().isFault()) {
            return super.processRequest(request);
        }
        try {
            doProcess(request);
            return super.processRequest(request);
        } catch (SAXException se) {
            LOGGER.log(Level.WARNING, "Client Request doesn't pass Service's Schema Validation", (Throwable) se);
            SOAPVersion soapVersion = this.binding.getSOAPVersion();
            Message faultMsg = SOAPFaultBuilder.createSOAPFaultMessage(soapVersion, (CheckedExceptionImpl) null, se, soapVersion.faultCodeClient);
            return doReturnWith(request.createServerResponse(faultMsg, this.wsdlPort, this.seiModel, this.binding));
        }
    }

    @Override // com.sun.xml.internal.ws.api.pipe.helper.AbstractFilterTubeImpl, com.sun.xml.internal.ws.api.pipe.Tube
    public NextAction processResponse(Packet response) {
        if (isNoValidation() || !this.feature.isOutbound() || response.getMessage() == null || !response.getMessage().hasPayload() || response.getMessage().isFault()) {
            return super.processResponse(response);
        }
        try {
            doProcess(response);
            return super.processResponse(response);
        } catch (SAXException se) {
            throw new WebServiceException(se);
        }
    }

    protected ServerSchemaValidationTube(ServerSchemaValidationTube that, TubeCloner cloner) {
        super(that, cloner);
        this.schema = that.schema;
        this.validator = this.schema.newValidator();
        this.noValidation = that.noValidation;
        this.seiModel = that.seiModel;
        this.wsdlPort = that.wsdlPort;
    }

    @Override // com.sun.xml.internal.ws.api.pipe.helper.AbstractTubeImpl, com.sun.xml.internal.ws.api.pipe.Tube
    public AbstractTubeImpl copy(TubeCloner cloner) {
        return new ServerSchemaValidationTube(this, cloner);
    }
}
