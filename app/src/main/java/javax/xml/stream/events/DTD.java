package javax.xml.stream.events;

import java.util.List;

/* loaded from: rt.jar:javax/xml/stream/events/DTD.class */
public interface DTD extends XMLEvent {
    String getDocumentTypeDeclaration();

    Object getProcessedDTD();

    List getNotations();

    List getEntities();
}
