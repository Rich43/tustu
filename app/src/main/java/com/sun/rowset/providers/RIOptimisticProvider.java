package com.sun.rowset.providers;

import com.sun.rowset.JdbcRowSetResourceBundle;
import com.sun.rowset.internal.CachedRowSetReader;
import com.sun.rowset.internal.CachedRowSetWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.sql.SQLException;
import javax.sql.RowSetReader;
import javax.sql.RowSetWriter;
import javax.sql.rowset.spi.SyncProvider;
import javax.sql.rowset.spi.SyncProviderException;

/* loaded from: rt.jar:com/sun/rowset/providers/RIOptimisticProvider.class */
public final class RIOptimisticProvider extends SyncProvider implements Serializable {
    private String providerID;
    private JdbcRowSetResourceBundle resBundle;
    static final long serialVersionUID = -3143367176751761936L;
    private String vendorName = "Oracle Corporation";
    private String versionNumber = "1.0";
    private CachedRowSetReader reader = new CachedRowSetReader();
    private CachedRowSetWriter writer = new CachedRowSetWriter();

    public RIOptimisticProvider() {
        this.providerID = "com.sun.rowset.providers.RIOptimisticProvider";
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

    @Override // javax.sql.rowset.spi.SyncProvider
    public RowSetWriter getRowSetWriter() {
        try {
            this.writer.setReader(this.reader);
        } catch (SQLException e2) {
        }
        return this.writer;
    }

    @Override // javax.sql.rowset.spi.SyncProvider
    public RowSetReader getRowSetReader() {
        return this.reader;
    }

    @Override // javax.sql.rowset.spi.SyncProvider
    public int getProviderGrade() {
        return 2;
    }

    @Override // javax.sql.rowset.spi.SyncProvider
    public void setDataSourceLock(int i2) throws SyncProviderException {
        if (i2 != 1) {
            throw new SyncProviderException(this.resBundle.handleGetObject("riop.locking").toString());
        }
    }

    @Override // javax.sql.rowset.spi.SyncProvider
    public int getDataSourceLock() throws SyncProviderException {
        return 1;
    }

    @Override // javax.sql.rowset.spi.SyncProvider
    public int supportsUpdatableView() {
        return 6;
    }

    @Override // javax.sql.rowset.spi.SyncProvider
    public String getVersion() {
        return this.versionNumber;
    }

    @Override // javax.sql.rowset.spi.SyncProvider
    public String getVendor() {
        return this.vendorName;
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        try {
            this.resBundle = JdbcRowSetResourceBundle.getJdbcRowSetResourceBundle();
        } catch (IOException e2) {
            throw new RuntimeException(e2);
        }
    }
}
