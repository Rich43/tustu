package javax.sql.rowset.spi;

import java.io.Reader;
import java.sql.SQLException;
import javax.sql.RowSetReader;
import javax.sql.rowset.WebRowSet;

/* loaded from: rt.jar:javax/sql/rowset/spi/XmlReader.class */
public interface XmlReader extends RowSetReader {
    void readXML(WebRowSet webRowSet, Reader reader) throws SQLException;
}
