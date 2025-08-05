package com.sun.rowset.providers;

import com.sun.rowset.JdbcRowSetResourceBundle;
import java.io.IOException;
import java.sql.SQLException;
import javax.sql.RowSetReader;
import javax.sql.RowSetWriter;
import javax.sql.rowset.spi.SyncProvider;
import javax.sql.rowset.spi.SyncProviderException;
import javax.sql.rowset.spi.XmlReader;
import javax.sql.rowset.spi.XmlWriter;

/* loaded from: rt.jar:com/sun/rowset/providers/RIXMLProvider.class */
public final class RIXMLProvider extends SyncProvider {
    private String providerID;
    private String vendorName = "Oracle Corporation";
    private String versionNumber = "1.0";
    private JdbcRowSetResourceBundle resBundle;
    private XmlReader xmlReader;
    private XmlWriter xmlWriter;

    public RIXMLProvider() {
        this.providerID = "com.sun.rowset.providers.RIXMLProvider";
        this.providerID = getClass().getName();
        try {
            this.resBundle = JdbcRowSetResourceBundle.getJdbcRowSetResourceBundle();
        } catch (IOException e2) {
            throw new RuntimeException(e2);
        }
    }

    @Override // javax.sql.rowset.spi.SyncProvider
    public String getProviderID() {
        return this.providerID;
    }

    public void setXmlReader(XmlReader xmlReader) throws SQLException {
        this.xmlReader = xmlReader;
    }

    public void setXmlWriter(XmlWriter xmlWriter) throws SQLException {
        this.xmlWriter = xmlWriter;
    }

    public XmlReader getXmlReader() throws SQLException {
        return this.xmlReader;
    }

    public XmlWriter getXmlWriter() throws SQLException {
        return this.xmlWriter;
    }

    @Override // javax.sql.rowset.spi.SyncProvider
    public int getProviderGrade() {
        return 1;
    }

    @Override // javax.sql.rowset.spi.SyncProvider
    public int supportsUpdatableView() {
        return 6;
    }

    @Override // javax.sql.rowset.spi.SyncProvider
    public int getDataSourceLock() throws SyncProviderException {
        return 1;
    }

    @Override // javax.sql.rowset.spi.SyncProvider
    public void setDataSourceLock(int i2) throws SyncProviderException {
        throw new UnsupportedOperationException(this.resBundle.handleGetObject("rixml.unsupp").toString());
    }

    @Override // javax.sql.rowset.spi.SyncProvider
    public RowSetWriter getRowSetWriter() {
        return null;
    }

    @Override // javax.sql.rowset.spi.SyncProvider
    public RowSetReader getRowSetReader() {
        return null;
    }

    @Override // javax.sql.rowset.spi.SyncProvider
    public String getVersion() {
        return this.versionNumber;
    }

    @Override // javax.sql.rowset.spi.SyncProvider
    public String getVendor() {
        return this.vendorName;
    }
}
