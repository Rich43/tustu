package javax.xml.bind.annotation;

import javax.xml.bind.ValidationEventHandler;
import javax.xml.transform.Result;
import javax.xml.transform.Source;

/* loaded from: rt.jar:javax/xml/bind/annotation/DomHandler.class */
public interface DomHandler<ElementT, ResultT extends Result> {
    ResultT createUnmarshaller(ValidationEventHandler validationEventHandler);

    ElementT getElement(ResultT resultt);

    Source marshal(ElementT elementt, ValidationEventHandler validationEventHandler);
}
