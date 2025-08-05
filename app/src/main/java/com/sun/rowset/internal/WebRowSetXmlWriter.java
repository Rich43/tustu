package com.sun.rowset.internal;

import com.sun.org.apache.xalan.internal.templates.Constants;
import com.sun.org.apache.xml.internal.serializer.SerializerConstants;
import com.sun.rowset.JdbcRowSetResourceBundle;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.io.Writer;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.MessageFormat;
import java.util.Map;
import java.util.Stack;
import javafx.fxml.FXMLLoader;
import javax.sql.RowSet;
import javax.sql.RowSetInternal;
import javax.sql.rowset.WebRowSet;
import javax.sql.rowset.spi.XmlWriter;
import sun.security.x509.PolicyMappingsExtension;

/* loaded from: rt.jar:com/sun/rowset/internal/WebRowSetXmlWriter.class */
public class WebRowSetXmlWriter implements XmlWriter, Serializable {
    private transient Writer writer;
    private Stack<String> stack;
    private JdbcRowSetResourceBundle resBundle;
    static final long serialVersionUID = 7163134986189677641L;

    public WebRowSetXmlWriter() {
        try {
            this.resBundle = JdbcRowSetResourceBundle.getJdbcRowSetResourceBundle();
        } catch (IOException e2) {
            throw new RuntimeException(e2);
        }
    }

    @Override // javax.sql.rowset.spi.XmlWriter
    public void writeXML(WebRowSet webRowSet, Writer writer) throws SQLException {
        this.stack = new Stack<>();
        this.writer = writer;
        writeRowSet(webRowSet);
    }

    public void writeXML(WebRowSet webRowSet, OutputStream outputStream) throws SQLException {
        this.stack = new Stack<>();
        this.writer = new OutputStreamWriter(outputStream);
        writeRowSet(webRowSet);
    }

    private void writeRowSet(WebRowSet webRowSet) throws SQLException {
        try {
            startHeader();
            writeProperties(webRowSet);
            writeMetaData(webRowSet);
            writeData(webRowSet);
            endHeader();
        } catch (IOException e2) {
            throw new SQLException(MessageFormat.format(this.resBundle.handleGetObject("wrsxmlwriter.ioex").toString(), e2.getMessage()));
        }
    }

    private void startHeader() throws IOException {
        setTag("webRowSet");
        this.writer.write("<?xml version=\"1.0\"?>\n");
        this.writer.write("<webRowSet xmlns=\"http://java.sun.com/xml/ns/jdbc\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n");
        this.writer.write("xsi:schemaLocation=\"http://java.sun.com/xml/ns/jdbc http://java.sun.com/xml/ns/jdbc/webrowset.xsd\">\n");
    }

    private void endHeader() throws IOException {
        endTag("webRowSet");
    }

    private void writeProperties(WebRowSet webRowSet) throws IOException {
        beginSection("properties");
        try {
            propString("command", processSpecialCharacters(webRowSet.getCommand()));
            propInteger("concurrency", webRowSet.getConcurrency());
            propString("datasource", webRowSet.getDataSourceName());
            propBoolean("escape-processing", webRowSet.getEscapeProcessing());
            try {
                propInteger("fetch-direction", webRowSet.getFetchDirection());
            } catch (SQLException e2) {
            }
            propInteger("fetch-size", webRowSet.getFetchSize());
            propInteger("isolation-level", webRowSet.getTransactionIsolation());
            beginSection("key-columns");
            int[] keyColumns = webRowSet.getKeyColumns();
            for (int i2 = 0; keyColumns != null && i2 < keyColumns.length; i2++) {
                propInteger("column", keyColumns[i2]);
            }
            endSection("key-columns");
            beginSection(PolicyMappingsExtension.MAP);
            Map<String, Class<?>> typeMap = webRowSet.getTypeMap();
            if (typeMap != null) {
                for (Map.Entry<String, Class<?>> entry : typeMap.entrySet()) {
                    propString("type", entry.getKey());
                    propString(Constants.ATTRNAME_CLASS, entry.getValue().getName());
                }
            }
            endSection(PolicyMappingsExtension.MAP);
            propInteger("max-field-size", webRowSet.getMaxFieldSize());
            propInteger("max-rows", webRowSet.getMaxRows());
            propInteger("query-timeout", webRowSet.getQueryTimeout());
            propBoolean("read-only", webRowSet.isReadOnly());
            int type = webRowSet.getType();
            String str = "";
            if (type == 1003) {
                str = "ResultSet.TYPE_FORWARD_ONLY";
            } else if (type == 1004) {
                str = "ResultSet.TYPE_SCROLL_INSENSITIVE";
            } else if (type == 1005) {
                str = "ResultSet.TYPE_SCROLL_SENSITIVE";
            }
            propString("rowset-type", str);
            propBoolean("show-deleted", webRowSet.getShowDeleted());
            propString("table-name", webRowSet.getTableName());
            propString("url", webRowSet.getUrl());
            beginSection("sync-provider");
            propString("sync-provider-name", webRowSet.getSyncProvider().toString().substring(0, webRowSet.getSyncProvider().toString().indexOf("@")));
            propString("sync-provider-vendor", "Oracle Corporation");
            propString("sync-provider-version", "1.0");
            propInteger("sync-provider-grade", webRowSet.getSyncProvider().getProviderGrade());
            propInteger("data-source-lock", webRowSet.getSyncProvider().getDataSourceLock());
            endSection("sync-provider");
            endSection("properties");
        } catch (SQLException e3) {
            throw new IOException(MessageFormat.format(this.resBundle.handleGetObject("wrsxmlwriter.sqlex").toString(), e3.getMessage()));
        }
    }

    private void writeMetaData(WebRowSet webRowSet) throws IOException {
        beginSection("metadata");
        try {
            ResultSetMetaData metaData = webRowSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            propInteger("column-count", columnCount);
            for (int i2 = 1; i2 <= columnCount; i2++) {
                beginSection("column-definition");
                propInteger("column-index", i2);
                propBoolean("auto-increment", metaData.isAutoIncrement(i2));
                propBoolean("case-sensitive", metaData.isCaseSensitive(i2));
                propBoolean("currency", metaData.isCurrency(i2));
                propInteger("nullable", metaData.isNullable(i2));
                propBoolean("signed", metaData.isSigned(i2));
                propBoolean("searchable", metaData.isSearchable(i2));
                propInteger("column-display-size", metaData.getColumnDisplaySize(i2));
                propString("column-label", metaData.getColumnLabel(i2));
                propString("column-name", metaData.getColumnName(i2));
                propString("schema-name", metaData.getSchemaName(i2));
                propInteger("column-precision", metaData.getPrecision(i2));
                propInteger("column-scale", metaData.getScale(i2));
                propString("table-name", metaData.getTableName(i2));
                propString("catalog-name", metaData.getCatalogName(i2));
                propInteger("column-type", metaData.getColumnType(i2));
                propString("column-type-name", metaData.getColumnTypeName(i2));
                endSection("column-definition");
            }
            endSection("metadata");
        } catch (SQLException e2) {
            throw new IOException(MessageFormat.format(this.resBundle.handleGetObject("wrsxmlwriter.sqlex").toString(), e2.getMessage()));
        }
    }

    private void writeData(WebRowSet webRowSet) throws IOException {
        try {
            int columnCount = webRowSet.getMetaData().getColumnCount();
            beginSection("data");
            webRowSet.beforeFirst();
            webRowSet.setShowDeleted(true);
            while (webRowSet.next()) {
                if (webRowSet.rowDeleted() && webRowSet.rowInserted()) {
                    beginSection("modifyRow");
                } else if (webRowSet.rowDeleted()) {
                    beginSection("deleteRow");
                } else if (webRowSet.rowInserted()) {
                    beginSection("insertRow");
                } else {
                    beginSection("currentRow");
                }
                for (int i2 = 1; i2 <= columnCount; i2++) {
                    if (webRowSet.columnUpdated(i2)) {
                        ResultSet originalRow = webRowSet.getOriginalRow();
                        originalRow.next();
                        beginTag("columnValue");
                        writeValue(i2, (RowSet) originalRow);
                        endTag("columnValue");
                        beginTag("updateRow");
                        writeValue(i2, webRowSet);
                        endTag("updateRow");
                    } else {
                        beginTag("columnValue");
                        writeValue(i2, webRowSet);
                        endTag("columnValue");
                    }
                }
                endSection();
            }
            endSection("data");
        } catch (SQLException e2) {
            throw new IOException(MessageFormat.format(this.resBundle.handleGetObject("wrsxmlwriter.sqlex").toString(), e2.getMessage()));
        }
    }

    private void writeValue(int i2, RowSet rowSet) throws IOException {
        try {
            switch (rowSet.getMetaData().getColumnType(i2)) {
                case -7:
                case 16:
                    boolean z2 = rowSet.getBoolean(i2);
                    if (rowSet.wasNull()) {
                        writeNull();
                        break;
                    } else {
                        writeBoolean(z2);
                        break;
                    }
                case -6:
                case 5:
                    short s2 = rowSet.getShort(i2);
                    if (rowSet.wasNull()) {
                        writeNull();
                        break;
                    } else {
                        writeShort(s2);
                        break;
                    }
                case -5:
                    long j2 = rowSet.getLong(i2);
                    if (rowSet.wasNull()) {
                        writeNull();
                        break;
                    } else {
                        writeLong(j2);
                        break;
                    }
                case -4:
                case -3:
                case -2:
                    break;
                case -1:
                case 1:
                case 12:
                    writeStringData(rowSet.getString(i2));
                    break;
                case 2:
                case 3:
                    writeBigDecimal(rowSet.getBigDecimal(i2));
                    break;
                case 4:
                    int i3 = rowSet.getInt(i2);
                    if (rowSet.wasNull()) {
                        writeNull();
                        break;
                    } else {
                        writeInteger(i3);
                        break;
                    }
                case 6:
                case 7:
                    float f2 = rowSet.getFloat(i2);
                    if (rowSet.wasNull()) {
                        writeNull();
                        break;
                    } else {
                        writeFloat(f2);
                        break;
                    }
                case 8:
                    double d2 = rowSet.getDouble(i2);
                    if (rowSet.wasNull()) {
                        writeNull();
                        break;
                    } else {
                        writeDouble(d2);
                        break;
                    }
                case 91:
                    Date date = rowSet.getDate(i2);
                    if (rowSet.wasNull()) {
                        writeNull();
                        break;
                    } else {
                        writeLong(date.getTime());
                        break;
                    }
                case 92:
                    Time time = rowSet.getTime(i2);
                    if (rowSet.wasNull()) {
                        writeNull();
                        break;
                    } else {
                        writeLong(time.getTime());
                        break;
                    }
                case 93:
                    Timestamp timestamp = rowSet.getTimestamp(i2);
                    if (rowSet.wasNull()) {
                        writeNull();
                        break;
                    } else {
                        writeLong(timestamp.getTime());
                        break;
                    }
                default:
                    System.out.println(this.resBundle.handleGetObject("wsrxmlwriter.notproper").toString());
                    break;
            }
        } catch (SQLException e2) {
            throw new IOException(this.resBundle.handleGetObject("wrsxmlwriter.failedwrite").toString() + e2.getMessage());
        }
    }

    private void beginSection(String str) throws IOException {
        setTag(str);
        writeIndent(this.stack.size());
        this.writer.write("<" + str + ">\n");
    }

    private void endSection(String str) throws IOException {
        writeIndent(this.stack.size());
        String tag = getTag();
        if (tag.indexOf("webRowSet") != -1) {
            tag = "webRowSet";
        }
        if (str.equals(tag)) {
            this.writer.write("</" + tag + ">\n");
        }
        this.writer.flush();
    }

    private void endSection() throws IOException {
        writeIndent(this.stack.size());
        this.writer.write("</" + getTag() + ">\n");
        this.writer.flush();
    }

    private void beginTag(String str) throws IOException {
        setTag(str);
        writeIndent(this.stack.size());
        this.writer.write("<" + str + ">");
    }

    private void endTag(String str) throws IOException {
        String tag = getTag();
        if (str.equals(tag)) {
            this.writer.write("</" + tag + ">\n");
        }
        this.writer.flush();
    }

    private void emptyTag(String str) throws IOException {
        this.writer.write("<" + str + "/>");
    }

    private void setTag(String str) {
        this.stack.push(str);
    }

    private String getTag() {
        return this.stack.pop();
    }

    private void writeNull() throws IOException {
        emptyTag(FXMLLoader.NULL_KEYWORD);
    }

    private void writeStringData(String str) throws IOException {
        if (str == null) {
            writeNull();
        } else if (str.equals("")) {
            writeEmptyString();
        } else {
            this.writer.write(processSpecialCharacters(str));
        }
    }

    private void writeString(String str) throws IOException {
        if (str != null) {
            this.writer.write(str);
        } else {
            writeNull();
        }
    }

    private void writeShort(short s2) throws IOException {
        this.writer.write(Short.toString(s2));
    }

    private void writeLong(long j2) throws IOException {
        this.writer.write(Long.toString(j2));
    }

    private void writeInteger(int i2) throws IOException {
        this.writer.write(Integer.toString(i2));
    }

    private void writeBoolean(boolean z2) throws IOException {
        this.writer.write(Boolean.valueOf(z2).toString());
    }

    private void writeFloat(float f2) throws IOException {
        this.writer.write(Float.toString(f2));
    }

    private void writeDouble(double d2) throws IOException {
        this.writer.write(Double.toString(d2));
    }

    private void writeBigDecimal(BigDecimal bigDecimal) throws IOException {
        if (bigDecimal != null) {
            this.writer.write(bigDecimal.toString());
        } else {
            emptyTag(FXMLLoader.NULL_KEYWORD);
        }
    }

    private void writeIndent(int i2) throws IOException {
        for (int i3 = 1; i3 < i2; i3++) {
            this.writer.write(sun.security.pkcs11.wrapper.Constants.INDENT);
        }
    }

    private void propString(String str, String str2) throws IOException {
        beginTag(str);
        writeString(str2);
        endTag(str);
    }

    private void propInteger(String str, int i2) throws IOException {
        beginTag(str);
        writeInteger(i2);
        endTag(str);
    }

    private void propBoolean(String str, boolean z2) throws IOException {
        beginTag(str);
        writeBoolean(z2);
        endTag(str);
    }

    private void writeEmptyString() throws IOException {
        emptyTag("emptyString");
    }

    @Override // javax.sql.RowSetWriter
    public boolean writeData(RowSetInternal rowSetInternal) {
        return false;
    }

    private String processSpecialCharacters(String str) {
        String strConcat;
        if (str == null) {
            return null;
        }
        char[] charArray = str.toCharArray();
        String str2 = "";
        for (int i2 = 0; i2 < charArray.length; i2++) {
            if (charArray[i2] == '&') {
                strConcat = str2.concat(SerializerConstants.ENTITY_AMP);
            } else if (charArray[i2] == '<') {
                strConcat = str2.concat(SerializerConstants.ENTITY_LT);
            } else if (charArray[i2] == '>') {
                strConcat = str2.concat(SerializerConstants.ENTITY_GT);
            } else if (charArray[i2] == '\'') {
                strConcat = str2.concat("&apos;");
            } else if (charArray[i2] == '\"') {
                strConcat = str2.concat(SerializerConstants.ENTITY_QUOT);
            } else {
                strConcat = str2.concat(String.valueOf(charArray[i2]));
            }
            str2 = strConcat;
        }
        return str2;
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
