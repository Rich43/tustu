package com.sun.xml.internal.messaging.saaj.packaging.mime;

import com.sun.xml.internal.messaging.saaj.packaging.mime.internet.MimeBodyPart;
import javax.activation.DataSource;

/* loaded from: rt.jar:com/sun/xml/internal/messaging/saaj/packaging/mime/MultipartDataSource.class */
public interface MultipartDataSource extends DataSource {
    int getCount();

    MimeBodyPart getBodyPart(int i2) throws MessagingException;
}
