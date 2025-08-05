package javax.sql.rowset.spi;

import java.io.Writer;
import java.sql.SQLException;
import javax.sql.RowSetWriter;
import javax.sql.rowset.WebRowSet;

/* loaded from: rt.jar:javax/sql/rowset/spi/XmlWriter.class */
public interface XmlWriter extends RowSetWriter {
    void writeXML(WebRowSet webRowSet, Writer writer) throws SQLException;
}
