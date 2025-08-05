package javax.sql.rowset;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.sql.ResultSet;
import java.sql.SQLException;

/* loaded from: rt.jar:javax/sql/rowset/WebRowSet.class */
public interface WebRowSet extends CachedRowSet {
    public static final String PUBLIC_XML_SCHEMA = "--//Oracle Corporation//XSD Schema//EN";
    public static final String SCHEMA_SYSTEM_ID = "http://java.sun.com/xml/ns/jdbc/webrowset.xsd";

    void readXml(Reader reader) throws SQLException;

    void readXml(InputStream inputStream) throws SQLException, IOException;

    void writeXml(ResultSet resultSet, Writer writer) throws SQLException;

    void writeXml(ResultSet resultSet, OutputStream outputStream) throws SQLException, IOException;

    void writeXml(Writer writer) throws SQLException;

    void writeXml(OutputStream outputStream) throws SQLException, IOException;
}
