package com.sun.xml.internal.ws.client;

import com.sun.xml.internal.ws.api.WSBinding;
import com.sun.xml.internal.ws.api.message.Packet;
import com.sun.xml.internal.ws.api.model.wsdl.WSDLPort;
import com.sun.xml.internal.ws.api.pipe.NextAction;
import com.sun.xml.internal.ws.api.pipe.Tube;
import com.sun.xml.internal.ws.api.pipe.TubeCloner;
import com.sun.xml.internal.ws.api.pipe.helper.AbstractTubeImpl;
import com.sun.xml.internal.ws.api.server.SDDocument;
import com.sun.xml.internal.ws.util.MetadataUtil;
import com.sun.xml.internal.ws.util.pipe.AbstractSchemaValidationTube;
import java.util.Map;
import java.util.logging.Logger;
import javax.xml.transform.Source;
import javax.xml.validation.Schema;
import javax.xml.validation.Validator;
import javax.xml.ws.WebServiceException;
import org.xml.sax.SAXException;

/* loaded from: rt.jar:com/sun/xml/internal/ws/client/ClientSchemaValidationTube.class */
public class ClientSchemaValidationTube extends AbstractSchemaValidationTube {
    private static final Logger LOGGER = Logger.getLogger(ClientSchemaValidationTube.class.getName());
    private final Schema schema;
    private final Validator validator;
    private final boolean noValidation;
    private final WSDLPort port;

    public ClientSchemaValidationTube(WSBinding binding, WSDLPort port, Tube next) {
        super(binding, next);
        this.port = port;
        if (port != null) {
            String primaryWsdl = port.getOwner().getParent().getLocation().getSystemId();
            Map<String, SDDocument> docs = MetadataUtil.getMetadataClosure(primaryWsdl, new AbstractSchemaValidationTube.MetadataResolverImpl(), true);
            AbstractSchemaValidationTube.MetadataResolverImpl mdresolver = new AbstractSchemaValidationTube.MetadataResolverImpl(docs.values());
            Source[] sources = getSchemaSources(docs.values(), mdresolver);
            for (Source source : sources) {
                LOGGER.fine("Constructing client validation schema from = " + source.getSystemId());
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

    protected ClientSchemaValidationTube(ClientSchemaValidationTube that, TubeCloner cloner) {
        super(that, cloner);
        this.port = that.port;
        this.schema = that.schema;
        this.validator = this.schema.newValidator();
        this.noValidation = that.noValidation;
    }

    @Override // com.sun.xml.internal.ws.api.pipe.helper.AbstractTubeImpl, com.sun.xml.internal.ws.api.pipe.Tube
    public AbstractTubeImpl copy(TubeCloner cloner) {
        return new ClientSchemaValidationTube(this, cloner);
    }

    @Override // com.sun.xml.internal.ws.api.pipe.helper.AbstractFilterTubeImpl, com.sun.xml.internal.ws.api.pipe.Tube
    public NextAction processRequest(Packet request) {
        if (isNoValidation() || !this.feature.isOutbound() || !request.getMessage().hasPayload() || request.getMessage().isFault()) {
            return super.processRequest(request);
        }
        try {
            doProcess(request);
            return super.processRequest(request);
        } catch (SAXException se) {
            throw new WebServiceException(se);
        }
    }

    @Override // com.sun.xml.internal.ws.api.pipe.helper.AbstractFilterTubeImpl, com.sun.xml.internal.ws.api.pipe.Tube
    public NextAction processResponse(Packet response) {
        if (isNoValidation() || !this.feature.isInbound() || response.getMessage() == null || !response.getMessage().hasPayload() || response.getMessage().isFault()) {
            return super.processResponse(response);
        }
        try {
            doProcess(response);
            return super.processResponse(response);
        } catch (SAXException se) {
            throw new WebServiceException(se);
        }
    }
}
