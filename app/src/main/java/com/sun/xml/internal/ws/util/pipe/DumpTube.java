package com.sun.xml.internal.ws.util.pipe;

import com.sun.xml.internal.ws.api.message.Packet;
import com.sun.xml.internal.ws.api.pipe.NextAction;
import com.sun.xml.internal.ws.api.pipe.Tube;
import com.sun.xml.internal.ws.api.pipe.TubeCloner;
import com.sun.xml.internal.ws.api.pipe.helper.AbstractFilterTubeImpl;
import com.sun.xml.internal.ws.api.pipe.helper.AbstractTubeImpl;
import java.io.PrintStream;
import java.lang.reflect.Constructor;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import jdk.internal.dynalink.CallSiteDescriptor;

/* loaded from: rt.jar:com/sun/xml/internal/ws/util/pipe/DumpTube.class */
public class DumpTube extends AbstractFilterTubeImpl {
    private final String name;
    private final PrintStream out;
    private final XMLOutputFactory staxOut;
    private static boolean warnStaxUtils;

    public DumpTube(String name, PrintStream out, Tube next) {
        super(next);
        this.name = name;
        this.out = out;
        this.staxOut = XMLOutputFactory.newInstance();
    }

    protected DumpTube(DumpTube that, TubeCloner cloner) {
        super(that, cloner);
        this.name = that.name;
        this.out = that.out;
        this.staxOut = that.staxOut;
    }

    @Override // com.sun.xml.internal.ws.api.pipe.helper.AbstractFilterTubeImpl, com.sun.xml.internal.ws.api.pipe.Tube
    public NextAction processRequest(Packet request) {
        dump("request", request);
        return super.processRequest(request);
    }

    @Override // com.sun.xml.internal.ws.api.pipe.helper.AbstractFilterTubeImpl, com.sun.xml.internal.ws.api.pipe.Tube
    public NextAction processResponse(Packet response) {
        dump("response", response);
        return super.processResponse(response);
    }

    protected void dump(String header, Packet packet) {
        this.out.println("====[" + this.name + CallSiteDescriptor.TOKEN_DELIMITER + header + "]====");
        if (packet.getMessage() == null) {
            this.out.println("(none)");
        } else {
            try {
                XMLStreamWriter writer = createIndenter(this.staxOut.createXMLStreamWriter(new PrintStream(this.out) { // from class: com.sun.xml.internal.ws.util.pipe.DumpTube.1
                    @Override // java.io.PrintStream, java.io.FilterOutputStream, java.io.OutputStream, java.io.Closeable, java.lang.AutoCloseable
                    public void close() {
                    }
                }));
                packet.getMessage().copy().writeTo(writer);
                writer.close();
            } catch (XMLStreamException e2) {
                e2.printStackTrace(this.out);
            }
        }
        this.out.println("============");
    }

    private XMLStreamWriter createIndenter(XMLStreamWriter writer) {
        try {
            Class clazz = getClass().getClassLoader().loadClass("javanet.staxutils.IndentingXMLStreamWriter");
            Constructor c2 = clazz.getConstructor(XMLStreamWriter.class);
            writer = (XMLStreamWriter) c2.newInstance(writer);
        } catch (Exception e2) {
            if (!warnStaxUtils) {
                warnStaxUtils = true;
                this.out.println("WARNING: put stax-utils.jar to the classpath to indent the dump output");
            }
        }
        return writer;
    }

    @Override // com.sun.xml.internal.ws.api.pipe.helper.AbstractTubeImpl, com.sun.xml.internal.ws.api.pipe.Tube
    public AbstractTubeImpl copy(TubeCloner cloner) {
        return new DumpTube(this, cloner);
    }
}
