package javax.sql.rowset.spi;

import javax.sql.RowSetReader;
import javax.sql.RowSetWriter;

/* compiled from: SyncFactory.java */
/* loaded from: rt.jar:javax/sql/rowset/spi/ProviderImpl.class */
class ProviderImpl extends SyncProvider {
    private String className = null;
    private String vendorName = null;
    private String ver = null;
    private int index;

    ProviderImpl() {
    }

    public void setClassname(String str) {
        this.className = str;
    }

    public String getClassname() {
        return this.className;
    }

    public void setVendor(String str) {
        this.vendorName = str;
    }

    @Override // javax.sql.rowset.spi.SyncProvider
    public String getVendor() {
        return this.vendorName;
    }

    public void setVersion(String str) {
        this.ver = str;
    }

    @Override // javax.sql.rowset.spi.SyncProvider
    public String getVersion() {
        return this.ver;
    }

    public void setIndex(int i2) {
        this.index = i2;
    }

    public int getIndex() {
        return this.index;
    }

    @Override // javax.sql.rowset.spi.SyncProvider
    public int getDataSourceLock() throws SyncProviderException {
        try {
            return SyncFactory.getInstance(this.className).getDataSourceLock();
        } catch (SyncFactoryException e2) {
            throw new SyncProviderException(e2.getMessage());
        }
    }

    @Override // javax.sql.rowset.spi.SyncProvider
    public int getProviderGrade() {
        int providerGrade = 0;
        try {
            providerGrade = SyncFactory.getInstance(this.className).getProviderGrade();
        } catch (SyncFactoryException e2) {
        }
        return providerGrade;
    }

    @Override // javax.sql.rowset.spi.SyncProvider
    public String getProviderID() {
        return this.className;
    }

    @Override // javax.sql.rowset.spi.SyncProvider
    public RowSetReader getRowSetReader() {
        RowSetReader rowSetReader = null;
        try {
            rowSetReader = SyncFactory.getInstance(this.className).getRowSetReader();
        } catch (SyncFactoryException e2) {
        }
        return rowSetReader;
    }

    @Override // javax.sql.rowset.spi.SyncProvider
    public RowSetWriter getRowSetWriter() {
        RowSetWriter rowSetWriter = null;
        try {
            rowSetWriter = SyncFactory.getInstance(this.className).getRowSetWriter();
        } catch (SyncFactoryException e2) {
        }
        return rowSetWriter;
    }

    @Override // javax.sql.rowset.spi.SyncProvider
    public void setDataSourceLock(int i2) throws SyncProviderException {
        try {
            SyncFactory.getInstance(this.className).setDataSourceLock(i2);
        } catch (SyncFactoryException e2) {
            throw new SyncProviderException(e2.getMessage());
        }
    }

    @Override // javax.sql.rowset.spi.SyncProvider
    public int supportsUpdatableView() {
        int iSupportsUpdatableView = 0;
        try {
            iSupportsUpdatableView = SyncFactory.getInstance(this.className).supportsUpdatableView();
        } catch (SyncFactoryException e2) {
        }
        return iSupportsUpdatableView;
    }
}
