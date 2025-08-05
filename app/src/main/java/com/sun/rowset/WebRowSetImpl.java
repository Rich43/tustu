package com.sun.rowset;

import com.sun.rowset.internal.WebRowSetXmlReader;
import com.sun.rowset.internal.WebRowSetXmlWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Hashtable;
import javax.sql.rowset.WebRowSet;
import javax.sql.rowset.spi.SyncFactory;
import javax.sql.rowset.spi.SyncProvider;

/* loaded from: rt.jar:com/sun/rowset/WebRowSetImpl.class */
public class WebRowSetImpl extends CachedRowSetImpl implements WebRowSet {
    private WebRowSetXmlReader xmlReader;
    private WebRowSetXmlWriter xmlWriter;
    private int curPosBfrWrite;
    private SyncProvider provider;
    static final long serialVersionUID = -8771775154092422943L;

    public WebRowSetImpl() throws SQLException {
        this.xmlReader = new WebRowSetXmlReader();
        this.xmlWriter = new WebRowSetXmlWriter();
    }

    public WebRowSetImpl(Hashtable hashtable) throws SQLException {
        try {
            this.resBundle = JdbcRowSetResourceBundle.getJdbcRowSetResourceBundle();
            if (hashtable == null) {
                throw new SQLException(this.resBundle.handleGetObject("webrowsetimpl.nullhash").toString());
            }
            this.provider = SyncFactory.getInstance((String) hashtable.get(SyncFactory.ROWSET_SYNC_PROVIDER));
        } catch (IOException e2) {
            throw new RuntimeException(e2);
        }
    }

    public void writeXml(ResultSet resultSet, Writer writer) throws SQLException {
        populate(resultSet);
        this.curPosBfrWrite = getRow();
        writeXml(writer);
    }

    public void writeXml(Writer writer) throws SQLException {
        if (this.xmlWriter != null) {
            this.curPosBfrWrite = getRow();
            this.xmlWriter.writeXML(this, writer);
            return;
        }
        throw new SQLException(this.resBundle.handleGetObject("webrowsetimpl.invalidwr").toString());
    }

    public void readXml(Reader reader) throws SQLException {
        try {
            if (reader != null) {
                this.xmlReader.readXML(this, reader);
                if (this.curPosBfrWrite == 0) {
                    beforeFirst();
                } else {
                    absolute(this.curPosBfrWrite);
                }
                return;
            }
            throw new SQLException(this.resBundle.handleGetObject("webrowsetimpl.invalidrd").toString());
        } catch (Exception e2) {
            throw new SQLException(e2.getMessage());
        }
    }

    public void readXml(InputStream inputStream) throws SQLException, IOException {
        if (inputStream != null) {
            this.xmlReader.readXML(this, inputStream);
            if (this.curPosBfrWrite == 0) {
                beforeFirst();
                return;
            } else {
                absolute(this.curPosBfrWrite);
                return;
            }
        }
        throw new SQLException(this.resBundle.handleGetObject("webrowsetimpl.invalidrd").toString());
    }

    public void writeXml(OutputStream outputStream) throws SQLException, IOException {
        if (this.xmlWriter != null) {
            this.curPosBfrWrite = getRow();
            this.xmlWriter.writeXML(this, outputStream);
            return;
        }
        throw new SQLException(this.resBundle.handleGetObject("webrowsetimpl.invalidwr").toString());
    }

    public void writeXml(ResultSet resultSet, OutputStream outputStream) throws SQLException, IOException {
        populate(resultSet);
        this.curPosBfrWrite = getRow();
        writeXml(outputStream);
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
