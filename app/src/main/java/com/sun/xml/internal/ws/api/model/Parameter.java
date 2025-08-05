package com.sun.xml.internal.ws.api.model;

import com.sun.xml.internal.bind.api.Bridge;
import javax.jws.WebParam;
import javax.xml.namespace.QName;

/* loaded from: rt.jar:com/sun/xml/internal/ws/api/model/Parameter.class */
public interface Parameter {
    SEIModel getOwner();

    JavaMethod getParent();

    QName getName();

    Bridge getBridge();

    WebParam.Mode getMode();

    int getIndex();

    boolean isWrapperStyle();

    boolean isReturnValue();

    ParameterBinding getBinding();

    ParameterBinding getInBinding();

    ParameterBinding getOutBinding();

    boolean isIN();

    boolean isOUT();

    boolean isINOUT();

    boolean isResponse();

    Object getHolderValue(Object obj);

    String getPartName();
}
