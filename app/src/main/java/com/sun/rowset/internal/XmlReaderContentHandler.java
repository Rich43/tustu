package com.sun.rowset.internal;

import com.sun.org.apache.xalan.internal.templates.Constants;
import com.sun.rowset.JdbcRowSetResourceBundle;
import com.sun.rowset.WebRowSetImpl;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;
import javafx.fxml.FXMLLoader;
import javax.sql.RowSet;
import javax.sql.RowSetMetaData;
import javax.sql.rowset.RowSetMetaDataImpl;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;
import sun.security.x509.PolicyMappingsExtension;

/* loaded from: rt.jar:com/sun/rowset/internal/XmlReaderContentHandler.class */
public class XmlReaderContentHandler extends DefaultHandler {
    private HashMap<String, Integer> propMap;
    private HashMap<String, Integer> colDefMap;
    private HashMap<String, Integer> dataMap;
    private HashMap<String, Class<?>> typeMap;
    private Vector<Object[]> updates;
    private Vector<String> keyCols;
    private String columnValue;
    private String propertyValue;
    private String metaDataValue;
    private int tag;
    private int state;
    private WebRowSetImpl rs;
    private boolean nullVal;
    private boolean emptyStringVal;
    private RowSetMetaData md;
    private int idx;
    private String lastval;
    private String Key_map;
    private String Value_map;
    private String tempStr;
    private String tempUpdate;
    private String tempCommand;
    private Object[] upd;
    private static final int CommandTag = 0;
    private static final int ConcurrencyTag = 1;
    private static final int DatasourceTag = 2;
    private static final int EscapeProcessingTag = 3;
    private static final int FetchDirectionTag = 4;
    private static final int FetchSizeTag = 5;
    private static final int IsolationLevelTag = 6;
    private static final int KeycolsTag = 7;
    private static final int MapTag = 8;
    private static final int MaxFieldSizeTag = 9;
    private static final int MaxRowsTag = 10;
    private static final int QueryTimeoutTag = 11;
    private static final int ReadOnlyTag = 12;
    private static final int RowsetTypeTag = 13;
    private static final int ShowDeletedTag = 14;
    private static final int TableNameTag = 15;
    private static final int UrlTag = 16;
    private static final int PropNullTag = 17;
    private static final int PropColumnTag = 18;
    private static final int PropTypeTag = 19;
    private static final int PropClassTag = 20;
    private static final int SyncProviderTag = 21;
    private static final int SyncProviderNameTag = 22;
    private static final int SyncProviderVendorTag = 23;
    private static final int SyncProviderVersionTag = 24;
    private static final int SyncProviderGradeTag = 25;
    private static final int DataSourceLock = 26;
    private static final int ColumnCountTag = 0;
    private static final int ColumnDefinitionTag = 1;
    private static final int ColumnIndexTag = 2;
    private static final int AutoIncrementTag = 3;
    private static final int CaseSensitiveTag = 4;
    private static final int CurrencyTag = 5;
    private static final int NullableTag = 6;
    private static final int SignedTag = 7;
    private static final int SearchableTag = 8;
    private static final int ColumnDisplaySizeTag = 9;
    private static final int ColumnLabelTag = 10;
    private static final int ColumnNameTag = 11;
    private static final int SchemaNameTag = 12;
    private static final int ColumnPrecisionTag = 13;
    private static final int ColumnScaleTag = 14;
    private static final int MetaTableNameTag = 15;
    private static final int CatalogNameTag = 16;
    private static final int ColumnTypeTag = 17;
    private static final int ColumnTypeNameTag = 18;
    private static final int MetaNullTag = 19;
    private static final int RowTag = 0;
    private static final int ColTag = 1;
    private static final int InsTag = 2;
    private static final int DelTag = 3;
    private static final int InsDelTag = 4;
    private static final int UpdTag = 5;
    private static final int NullTag = 6;
    private static final int EmptyStringTag = 7;
    private static final int INITIAL = 0;
    private static final int PROPERTIES = 1;
    private static final int METADATA = 2;
    private static final int DATA = 3;
    private JdbcRowSetResourceBundle resBundle;
    private String[] properties = {"command", "concurrency", "datasource", "escape-processing", "fetch-direction", "fetch-size", "isolation-level", "key-columns", PolicyMappingsExtension.MAP, "max-field-size", "max-rows", "query-timeout", "read-only", "rowset-type", "show-deleted", "table-name", "url", FXMLLoader.NULL_KEYWORD, "column", "type", Constants.ATTRNAME_CLASS, "sync-provider", "sync-provider-name", "sync-provider-vendor", "sync-provider-version", "sync-provider-grade", "data-source-lock"};
    private String[] colDef = {"column-count", "column-definition", "column-index", "auto-increment", "case-sensitive", "currency", "nullable", "signed", "searchable", "column-display-size", "column-label", "column-name", "schema-name", "column-precision", "column-scale", "table-name", "catalog-name", "column-type", "column-type-name", FXMLLoader.NULL_KEYWORD};
    private String[] data = {"currentRow", "columnValue", "insertRow", "deleteRow", "insdel", "updateRow", FXMLLoader.NULL_KEYWORD, "emptyString"};

    public XmlReaderContentHandler(RowSet rowSet) {
        this.rs = (WebRowSetImpl) rowSet;
        initMaps();
        this.updates = new Vector<>();
        this.columnValue = "";
        this.propertyValue = "";
        this.metaDataValue = "";
        this.nullVal = false;
        this.idx = 0;
        this.tempStr = "";
        this.tempUpdate = "";
        this.tempCommand = "";
        try {
            this.resBundle = JdbcRowSetResourceBundle.getJdbcRowSetResourceBundle();
        } catch (IOException e2) {
            throw new RuntimeException(e2);
        }
    }

    private void initMaps() {
        this.propMap = new HashMap<>();
        int length = this.properties.length;
        for (int i2 = 0; i2 < length; i2++) {
            this.propMap.put(this.properties[i2], Integer.valueOf(i2));
        }
        this.colDefMap = new HashMap<>();
        int length2 = this.colDef.length;
        for (int i3 = 0; i3 < length2; i3++) {
            this.colDefMap.put(this.colDef[i3], Integer.valueOf(i3));
        }
        this.dataMap = new HashMap<>();
        int length3 = this.data.length;
        for (int i4 = 0; i4 < length3; i4++) {
            this.dataMap.put(this.data[i4], Integer.valueOf(i4));
        }
        this.typeMap = new HashMap<>();
    }

    @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
    public void startDocument() throws SAXException {
    }

    @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
    public void endDocument() throws SAXException {
    }

    @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
    public void startElement(String str, String str2, String str3, Attributes attributes) throws SAXException {
        int iIntValue;
        switch (getState()) {
            case 1:
                this.tempCommand = "";
                int iIntValue2 = this.propMap.get(str2).intValue();
                if (iIntValue2 == 17) {
                    setNullValue(true);
                    break;
                } else {
                    setTag(iIntValue2);
                    break;
                }
            case 2:
                int iIntValue3 = this.colDefMap.get(str2).intValue();
                if (iIntValue3 == 19) {
                    setNullValue(true);
                    break;
                } else {
                    setTag(iIntValue3);
                    break;
                }
            case 3:
                this.tempStr = "";
                this.tempUpdate = "";
                if (this.dataMap.get(str2) != null) {
                    if (this.dataMap.get(str2).intValue() != 7) {
                        iIntValue = this.dataMap.get(str2).intValue();
                    } else {
                        iIntValue = 7;
                    }
                } else {
                    iIntValue = 6;
                }
                if (iIntValue == 6) {
                    setNullValue(true);
                    break;
                } else if (iIntValue == 7) {
                    setEmptyStringValue(true);
                    break;
                } else {
                    setTag(iIntValue);
                    if (iIntValue == 0 || iIntValue == 3 || iIntValue == 2) {
                        this.idx = 0;
                        try {
                            this.rs.moveToInsertRow();
                            break;
                        } catch (SQLException e2) {
                            return;
                        }
                    }
                }
                break;
            default:
                setState(str2);
                break;
        }
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:23:0x00ff A[Catch: SQLException -> 0x0117, TryCatch #9 {SQLException -> 0x0117, blocks: (B:7:0x0036, B:8:0x0049, B:9:0x006c, B:11:0x0073, B:12:0x0081, B:14:0x0089, B:15:0x00a3, B:16:0x00af, B:20:0x00ea, B:21:0x00f8, B:23:0x00ff, B:24:0x010c, B:18:0x00c7, B:19:0x00e9), top: B:108:0x0036, inners: #4 }] */
    /* JADX WARN: Removed duplicated region for block: B:24:0x010c A[Catch: SQLException -> 0x0117, TryCatch #9 {SQLException -> 0x0117, blocks: (B:7:0x0036, B:8:0x0049, B:9:0x006c, B:11:0x0073, B:12:0x0081, B:14:0x0089, B:15:0x00a3, B:16:0x00af, B:20:0x00ea, B:21:0x00f8, B:23:0x00ff, B:24:0x010c, B:18:0x00c7, B:19:0x00e9), top: B:108:0x0036, inners: #4 }] */
    @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void endElement(java.lang.String r9, java.lang.String r10, java.lang.String r11) throws org.xml.sax.SAXException {
        /*
            Method dump skipped, instructions count: 1018
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sun.rowset.internal.XmlReaderContentHandler.endElement(java.lang.String, java.lang.String, java.lang.String):void");
    }

    private void applyUpdates() throws SAXException {
        if (this.updates.size() > 0) {
            try {
                Iterator<Object[]> it = this.updates.iterator();
                while (it.hasNext()) {
                    Object[] next = it.next();
                    this.idx = ((Integer) next[0]).intValue();
                    if (!this.lastval.equals(next[1])) {
                        insertValue((String) next[1]);
                    }
                }
                this.rs.updateRow();
                this.updates.removeAllElements();
            } catch (SQLException e2) {
                throw new SAXException(MessageFormat.format(this.resBundle.handleGetObject("xmlrch.errupdrow").toString(), e2.getMessage()));
            }
        }
    }

    @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
    public void characters(char[] cArr, int i2, int i3) throws SAXException {
        try {
            switch (getState()) {
                case 1:
                    this.propertyValue = new String(cArr, i2, i3);
                    this.tempCommand = this.tempCommand.concat(this.propertyValue);
                    this.propertyValue = this.tempCommand;
                    if (this.tag == 19) {
                        this.Key_map = this.propertyValue;
                        break;
                    } else if (this.tag == 20) {
                        this.Value_map = this.propertyValue;
                        break;
                    }
                    break;
                case 2:
                    if (this.tag != -1) {
                        this.metaDataValue = new String(cArr, i2, i3);
                        break;
                    } else {
                        break;
                    }
                case 3:
                    setDataValue(cArr, i2, i3);
                    break;
            }
        } catch (SQLException e2) {
            throw new SAXException(this.resBundle.handleGetObject("xmlrch.chars").toString() + e2.getMessage());
        }
    }

    private void setState(String str) throws SAXException {
        if (str.equals("webRowSet")) {
            this.state = 0;
            return;
        }
        if (str.equals("properties")) {
            if (this.state != 1) {
                this.state = 1;
                return;
            } else {
                this.state = 0;
                return;
            }
        }
        if (str.equals("metadata")) {
            if (this.state != 2) {
                this.state = 2;
                return;
            } else {
                this.state = 0;
                return;
            }
        }
        if (str.equals("data")) {
            if (this.state != 3) {
                this.state = 3;
            } else {
                this.state = 0;
            }
        }
    }

    private int getState() {
        return this.state;
    }

    private void setTag(int i2) {
        this.tag = i2;
    }

    private int getTag() {
        return this.tag;
    }

    private void setNullValue(boolean z2) {
        this.nullVal = z2;
    }

    private boolean getNullValue() {
        return this.nullVal;
    }

    private void setEmptyStringValue(boolean z2) {
        this.emptyStringVal = z2;
    }

    private boolean getEmptyStringValue() {
        return this.emptyStringVal;
    }

    private String getStringValue(String str) {
        return str;
    }

    private int getIntegerValue(String str) {
        return Integer.parseInt(str);
    }

    private boolean getBooleanValue(String str) {
        return Boolean.valueOf(str).booleanValue();
    }

    private BigDecimal getBigDecimalValue(String str) {
        return new BigDecimal(str);
    }

    private byte getByteValue(String str) {
        return Byte.parseByte(str);
    }

    private short getShortValue(String str) {
        return Short.parseShort(str);
    }

    private long getLongValue(String str) {
        return Long.parseLong(str);
    }

    private float getFloatValue(String str) {
        return Float.parseFloat(str);
    }

    private double getDoubleValue(String str) {
        return Double.parseDouble(str);
    }

    private byte[] getBinaryValue(String str) {
        return str.getBytes();
    }

    private Date getDateValue(String str) {
        return new Date(getLongValue(str));
    }

    private Time getTimeValue(String str) {
        return new Time(getLongValue(str));
    }

    private Timestamp getTimestampValue(String str) {
        return new Timestamp(getLongValue(str));
    }

    private void setPropertyValue(String str) throws SQLException {
        boolean nullValue = getNullValue();
        switch (getTag()) {
            case 0:
                if (!nullValue) {
                    this.rs.setCommand(str);
                    return;
                }
                return;
            case 1:
                if (nullValue) {
                    throw new SQLException(this.resBundle.handleGetObject("xmlrch.badvalue").toString());
                }
                this.rs.setConcurrency(getIntegerValue(str));
                return;
            case 2:
                if (nullValue) {
                    this.rs.setDataSourceName(null);
                    return;
                } else {
                    this.rs.setDataSourceName(str);
                    return;
                }
            case 3:
                if (nullValue) {
                    throw new SQLException(this.resBundle.handleGetObject("xmlrch.badvalue").toString());
                }
                this.rs.setEscapeProcessing(getBooleanValue(str));
                return;
            case 4:
                if (nullValue) {
                    throw new SQLException(this.resBundle.handleGetObject("xmlrch.badvalue").toString());
                }
                this.rs.setFetchDirection(getIntegerValue(str));
                return;
            case 5:
                if (nullValue) {
                    throw new SQLException(this.resBundle.handleGetObject("xmlrch.badvalue").toString());
                }
                this.rs.setFetchSize(getIntegerValue(str));
                return;
            case 6:
                if (nullValue) {
                    throw new SQLException(this.resBundle.handleGetObject("xmlrch.badvalue").toString());
                }
                this.rs.setTransactionIsolation(getIntegerValue(str));
                return;
            case 7:
            case 8:
            case 17:
            case 19:
            case 20:
            case 21:
            case 23:
            case 24:
            case 25:
            case 26:
            default:
                return;
            case 9:
                if (nullValue) {
                    throw new SQLException(this.resBundle.handleGetObject("xmlrch.badvalue").toString());
                }
                this.rs.setMaxFieldSize(getIntegerValue(str));
                return;
            case 10:
                if (nullValue) {
                    throw new SQLException(this.resBundle.handleGetObject("xmlrch.badvalue").toString());
                }
                this.rs.setMaxRows(getIntegerValue(str));
                return;
            case 11:
                if (nullValue) {
                    throw new SQLException(this.resBundle.handleGetObject("xmlrch.badvalue").toString());
                }
                this.rs.setQueryTimeout(getIntegerValue(str));
                return;
            case 12:
                if (nullValue) {
                    throw new SQLException(this.resBundle.handleGetObject("xmlrch.badvalue").toString());
                }
                this.rs.setReadOnly(getBooleanValue(str));
                return;
            case 13:
                if (nullValue) {
                    throw new SQLException(this.resBundle.handleGetObject("xmlrch.badvalue").toString());
                }
                String stringValue = getStringValue(str);
                int i2 = 0;
                if (stringValue.trim().equals("ResultSet.TYPE_SCROLL_INSENSITIVE")) {
                    i2 = 1004;
                } else if (stringValue.trim().equals("ResultSet.TYPE_SCROLL_SENSITIVE")) {
                    i2 = 1005;
                } else if (stringValue.trim().equals("ResultSet.TYPE_FORWARD_ONLY")) {
                    i2 = 1003;
                }
                this.rs.setType(i2);
                return;
            case 14:
                if (nullValue) {
                    throw new SQLException(this.resBundle.handleGetObject("xmlrch.badvalue").toString());
                }
                this.rs.setShowDeleted(getBooleanValue(str));
                return;
            case 15:
                if (!nullValue) {
                    this.rs.setTableName(str);
                    return;
                }
                return;
            case 16:
                if (nullValue) {
                    this.rs.setUrl(null);
                    return;
                } else {
                    this.rs.setUrl(str);
                    return;
                }
            case 18:
                if (this.keyCols == null) {
                    this.keyCols = new Vector<>();
                }
                this.keyCols.add(str);
                return;
            case 22:
                if (nullValue) {
                    this.rs.setSyncProvider(null);
                    return;
                } else {
                    this.rs.setSyncProvider(str.substring(0, str.indexOf("@") + 1));
                    return;
                }
        }
    }

    private void setMetaDataValue(String str) throws SQLException {
        boolean nullValue = getNullValue();
        switch (getTag()) {
            case 0:
                this.md = new RowSetMetaDataImpl();
                this.idx = 0;
                if (nullValue) {
                    throw new SQLException(this.resBundle.handleGetObject("xmlrch.badvalue1").toString());
                }
                this.md.setColumnCount(getIntegerValue(str));
                return;
            case 1:
            default:
                return;
            case 2:
                this.idx++;
                return;
            case 3:
                if (nullValue) {
                    throw new SQLException(this.resBundle.handleGetObject("xmlrch.badvalue1").toString());
                }
                this.md.setAutoIncrement(this.idx, getBooleanValue(str));
                return;
            case 4:
                if (nullValue) {
                    throw new SQLException(this.resBundle.handleGetObject("xmlrch.badvalue1").toString());
                }
                this.md.setCaseSensitive(this.idx, getBooleanValue(str));
                return;
            case 5:
                if (nullValue) {
                    throw new SQLException(this.resBundle.handleGetObject("xmlrch.badvalue1").toString());
                }
                this.md.setCurrency(this.idx, getBooleanValue(str));
                return;
            case 6:
                if (nullValue) {
                    throw new SQLException(this.resBundle.handleGetObject("xmlrch.badvalue1").toString());
                }
                this.md.setNullable(this.idx, getIntegerValue(str));
                return;
            case 7:
                if (nullValue) {
                    throw new SQLException(this.resBundle.handleGetObject("xmlrch.badvalue1").toString());
                }
                this.md.setSigned(this.idx, getBooleanValue(str));
                return;
            case 8:
                if (nullValue) {
                    throw new SQLException(this.resBundle.handleGetObject("xmlrch.badvalue1").toString());
                }
                this.md.setSearchable(this.idx, getBooleanValue(str));
                return;
            case 9:
                if (nullValue) {
                    throw new SQLException(this.resBundle.handleGetObject("xmlrch.badvalue1").toString());
                }
                this.md.setColumnDisplaySize(this.idx, getIntegerValue(str));
                return;
            case 10:
                if (nullValue) {
                    this.md.setColumnLabel(this.idx, null);
                    return;
                } else {
                    this.md.setColumnLabel(this.idx, str);
                    return;
                }
            case 11:
                if (nullValue) {
                    this.md.setColumnName(this.idx, null);
                    return;
                } else {
                    this.md.setColumnName(this.idx, str);
                    return;
                }
            case 12:
                if (nullValue) {
                    this.md.setSchemaName(this.idx, null);
                    return;
                } else {
                    this.md.setSchemaName(this.idx, str);
                    return;
                }
            case 13:
                if (nullValue) {
                    throw new SQLException(this.resBundle.handleGetObject("xmlrch.badvalue1").toString());
                }
                this.md.setPrecision(this.idx, getIntegerValue(str));
                return;
            case 14:
                if (nullValue) {
                    throw new SQLException(this.resBundle.handleGetObject("xmlrch.badvalue1").toString());
                }
                this.md.setScale(this.idx, getIntegerValue(str));
                return;
            case 15:
                if (nullValue) {
                    this.md.setTableName(this.idx, null);
                    return;
                } else {
                    this.md.setTableName(this.idx, str);
                    return;
                }
            case 16:
                if (nullValue) {
                    this.md.setCatalogName(this.idx, null);
                    return;
                } else {
                    this.md.setCatalogName(this.idx, str);
                    return;
                }
            case 17:
                if (nullValue) {
                    throw new SQLException(this.resBundle.handleGetObject("xmlrch.badvalue1").toString());
                }
                this.md.setColumnType(this.idx, getIntegerValue(str));
                return;
            case 18:
                if (nullValue) {
                    this.md.setColumnTypeName(this.idx, null);
                    return;
                } else {
                    this.md.setColumnTypeName(this.idx, str);
                    return;
                }
        }
    }

    private void setDataValue(char[] cArr, int i2, int i3) throws SQLException {
        switch (getTag()) {
            case 1:
                this.columnValue = new String(cArr, i2, i3);
                this.tempStr = this.tempStr.concat(this.columnValue);
                break;
            case 5:
                this.upd = new Object[2];
                this.tempUpdate = this.tempUpdate.concat(new String(cArr, i2, i3));
                this.upd[0] = Integer.valueOf(this.idx);
                this.upd[1] = this.tempUpdate;
                this.lastval = (String) this.upd[1];
                break;
        }
    }

    private void insertValue(String str) throws SQLException {
        if (getNullValue()) {
            this.rs.updateNull(this.idx);
        }
        switch (this.rs.getMetaData().getColumnType(this.idx)) {
            case -7:
                this.rs.updateBoolean(this.idx, getBooleanValue(str));
                break;
            case -6:
            case 5:
                this.rs.updateShort(this.idx, getShortValue(str));
                break;
            case -5:
                this.rs.updateLong(this.idx, getLongValue(str));
                break;
            case -4:
            case -3:
            case -2:
                this.rs.updateBytes(this.idx, getBinaryValue(str));
                break;
            case -1:
            case 1:
            case 12:
                this.rs.updateString(this.idx, getStringValue(str));
                break;
            case 2:
            case 3:
                this.rs.updateObject(this.idx, getBigDecimalValue(str));
                break;
            case 4:
                this.rs.updateInt(this.idx, getIntegerValue(str));
                break;
            case 6:
            case 7:
                this.rs.updateFloat(this.idx, getFloatValue(str));
                break;
            case 8:
                this.rs.updateDouble(this.idx, getDoubleValue(str));
                break;
            case 16:
                this.rs.updateBoolean(this.idx, getBooleanValue(str));
                break;
            case 91:
                this.rs.updateDate(this.idx, getDateValue(str));
                break;
            case 92:
                this.rs.updateTime(this.idx, getTimeValue(str));
                break;
            case 93:
                this.rs.updateTimestamp(this.idx, getTimestampValue(str));
                break;
        }
    }

    @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ErrorHandler
    public void error(SAXParseException sAXParseException) throws SAXParseException {
        throw sAXParseException;
    }

    @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ErrorHandler
    public void warning(SAXParseException sAXParseException) throws SAXParseException {
        System.out.println(MessageFormat.format(this.resBundle.handleGetObject("xmlrch.warning").toString(), sAXParseException.getMessage(), Integer.valueOf(sAXParseException.getLineNumber()), sAXParseException.getSystemId()));
    }

    @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.DTDHandler
    public void notationDecl(String str, String str2, String str3) {
    }

    @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.DTDHandler
    public void unparsedEntityDecl(String str, String str2, String str3, String str4) {
    }

    private Row getPresentRow(WebRowSetImpl webRowSetImpl) throws SQLException {
        return null;
    }
}
