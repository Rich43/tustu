package com.efianalytics.ecudef;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "findEcuDefForQueryString", propOrder = {"queryString", "uid"})
/* loaded from: efiaServicesClient.jar:com/efianalytics/ecudef/FindEcuDefForQueryString.class */
public class FindEcuDefForQueryString {
    protected String queryString;
    protected String uid;

    public String getQueryString() {
        return this.queryString;
    }

    public void setQueryString(String value) {
        this.queryString = value;
    }

    public String getUid() {
        return this.uid;
    }

    public void setUid(String value) {
        this.uid = value;
    }
}
