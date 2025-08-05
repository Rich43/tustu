package javax.xml.bind;

/* loaded from: rt.jar:javax/xml/bind/Validator.class */
public interface Validator {
    void setEventHandler(ValidationEventHandler validationEventHandler) throws JAXBException;

    ValidationEventHandler getEventHandler() throws JAXBException;

    boolean validate(Object obj) throws JAXBException;

    boolean validateRoot(Object obj) throws JAXBException;

    void setProperty(String str, Object obj) throws PropertyException;

    Object getProperty(String str) throws PropertyException;
}
