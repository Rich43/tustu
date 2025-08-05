package com.sun.rowset.internal;

import com.sun.org.apache.xalan.internal.templates.Constants;
import com.sun.rowset.JdbcRowSetResourceBundle;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.Reader;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import javax.sql.RowSet;
import javax.sql.RowSetInternal;
import javax.sql.RowSetReader;
import javax.sql.rowset.CachedRowSet;

/* loaded from: rt.jar:com/sun/rowset/internal/CachedRowSetReader.class */
public class CachedRowSetReader implements RowSetReader, Serializable {
    private int writerCalls = 0;
    private boolean userCon = false;
    private int startPosition;
    private JdbcRowSetResourceBundle resBundle;
    static final long serialVersionUID = 5049738185801363801L;

    public CachedRowSetReader() {
        try {
            this.resBundle = JdbcRowSetResourceBundle.getJdbcRowSetResourceBundle();
        } catch (IOException e2) {
            throw new RuntimeException(e2);
        }
    }

    @Override // javax.sql.RowSetReader
    public void readData(RowSetInternal rowSetInternal) throws SQLException {
        Connection connection = null;
        try {
            try {
                CachedRowSet cachedRowSet = (CachedRowSet) rowSetInternal;
                if (cachedRowSet.getPageSize() == 0 && cachedRowSet.size() > 0) {
                    cachedRowSet.close();
                }
                this.writerCalls = 0;
                this.userCon = false;
                Connection connectionConnect = connect(rowSetInternal);
                if (connectionConnect == null || cachedRowSet.getCommand() == null) {
                    throw new SQLException(this.resBundle.handleGetObject("crsreader.connecterr").toString());
                }
                try {
                    connectionConnect.setTransactionIsolation(cachedRowSet.getTransactionIsolation());
                } catch (Exception e2) {
                }
                PreparedStatement preparedStatementPrepareStatement = connectionConnect.prepareStatement(cachedRowSet.getCommand());
                decodeParams(rowSetInternal.getParams(), preparedStatementPrepareStatement);
                try {
                    preparedStatementPrepareStatement.setMaxRows(cachedRowSet.getMaxRows());
                    preparedStatementPrepareStatement.setMaxFieldSize(cachedRowSet.getMaxFieldSize());
                    preparedStatementPrepareStatement.setEscapeProcessing(cachedRowSet.getEscapeProcessing());
                    preparedStatementPrepareStatement.setQueryTimeout(cachedRowSet.getQueryTimeout());
                    if (cachedRowSet.getCommand().toLowerCase().indexOf(Constants.ATTRNAME_SELECT) != -1) {
                        ResultSet resultSetExecuteQuery = preparedStatementPrepareStatement.executeQuery();
                        if (cachedRowSet.getPageSize() == 0) {
                            cachedRowSet.populate(resultSetExecuteQuery);
                        } else {
                            preparedStatementPrepareStatement = connectionConnect.prepareStatement(cachedRowSet.getCommand(), 1004, 1008);
                            decodeParams(rowSetInternal.getParams(), preparedStatementPrepareStatement);
                            try {
                                preparedStatementPrepareStatement.setMaxRows(cachedRowSet.getMaxRows());
                                preparedStatementPrepareStatement.setMaxFieldSize(cachedRowSet.getMaxFieldSize());
                                preparedStatementPrepareStatement.setEscapeProcessing(cachedRowSet.getEscapeProcessing());
                                preparedStatementPrepareStatement.setQueryTimeout(cachedRowSet.getQueryTimeout());
                                resultSetExecuteQuery = preparedStatementPrepareStatement.executeQuery();
                                cachedRowSet.populate(resultSetExecuteQuery, this.startPosition);
                            } catch (Exception e3) {
                                throw new SQLException(e3.getMessage());
                            }
                        }
                        resultSetExecuteQuery.close();
                    } else {
                        preparedStatementPrepareStatement.executeUpdate();
                    }
                    preparedStatementPrepareStatement.close();
                    try {
                        connectionConnect.commit();
                    } catch (SQLException e4) {
                    }
                    if (getCloseConnection()) {
                        connectionConnect.close();
                    }
                    if (connectionConnect != null) {
                        try {
                            if (getCloseConnection()) {
                                try {
                                    if (!connectionConnect.getAutoCommit()) {
                                        connectionConnect.rollback();
                                    }
                                } catch (Exception e5) {
                                }
                                connectionConnect.close();
                            }
                        } catch (SQLException e6) {
                        }
                    }
                } catch (Exception e7) {
                    throw new SQLException(e7.getMessage());
                }
            } catch (SQLException e8) {
                throw e8;
            }
        } catch (Throwable th) {
            if (0 != 0) {
                try {
                    if (getCloseConnection()) {
                        try {
                            if (!connection.getAutoCommit()) {
                                connection.rollback();
                            }
                        } catch (Exception e9) {
                        }
                        connection.close();
                    }
                } catch (SQLException e10) {
                    throw th;
                }
            }
            throw th;
        }
    }

    public boolean reset() throws SQLException {
        this.writerCalls++;
        return this.writerCalls == 1;
    }

    public Connection connect(RowSetInternal rowSetInternal) throws SQLException {
        if (rowSetInternal.getConnection() != null) {
            this.userCon = true;
            return rowSetInternal.getConnection();
        }
        if (((RowSet) rowSetInternal).getDataSourceName() != null) {
            try {
                DataSource dataSource = (DataSource) new InitialContext().lookup(((RowSet) rowSetInternal).getDataSourceName());
                if (((RowSet) rowSetInternal).getUsername() != null) {
                    return dataSource.getConnection(((RowSet) rowSetInternal).getUsername(), ((RowSet) rowSetInternal).getPassword());
                }
                return dataSource.getConnection();
            } catch (NamingException e2) {
                SQLException sQLException = new SQLException(this.resBundle.handleGetObject("crsreader.connect").toString());
                sQLException.initCause(e2);
                throw sQLException;
            }
        }
        if (((RowSet) rowSetInternal).getUrl() != null) {
            return DriverManager.getConnection(((RowSet) rowSetInternal).getUrl(), ((RowSet) rowSetInternal).getUsername(), ((RowSet) rowSetInternal).getPassword());
        }
        return null;
    }

    private void decodeParams(Object[] objArr, PreparedStatement preparedStatement) throws SQLException {
        for (int i2 = 0; i2 < objArr.length; i2++) {
            if (objArr[i2] instanceof Object[]) {
                Object[] objArr2 = (Object[]) objArr[i2];
                if (objArr2.length == 2) {
                    if (objArr2[0] == null) {
                        preparedStatement.setNull(i2 + 1, ((Integer) objArr2[1]).intValue());
                    } else if ((objArr2[0] instanceof Date) || (objArr2[0] instanceof Time) || (objArr2[0] instanceof Timestamp)) {
                        System.err.println(this.resBundle.handleGetObject("crsreader.datedetected").toString());
                        if (objArr2[1] instanceof Calendar) {
                            System.err.println(this.resBundle.handleGetObject("crsreader.caldetected").toString());
                            preparedStatement.setDate(i2 + 1, (Date) objArr2[0], (Calendar) objArr2[1]);
                        } else {
                            throw new SQLException(this.resBundle.handleGetObject("crsreader.paramtype").toString());
                        }
                    } else if (objArr2[0] instanceof Reader) {
                        preparedStatement.setCharacterStream(i2 + 1, (Reader) objArr2[0], ((Integer) objArr2[1]).intValue());
                    } else if (objArr2[1] instanceof Integer) {
                        preparedStatement.setObject(i2 + 1, objArr2[0], ((Integer) objArr2[1]).intValue());
                    }
                } else if (objArr2.length == 3) {
                    if (objArr2[0] == null) {
                        preparedStatement.setNull(i2 + 1, ((Integer) objArr2[1]).intValue(), (String) objArr2[2]);
                    } else {
                        if (objArr2[0] instanceof InputStream) {
                            switch (((Integer) objArr2[2]).intValue()) {
                                case 0:
                                    preparedStatement.setUnicodeStream(i2 + 1, (InputStream) objArr2[0], ((Integer) objArr2[1]).intValue());
                                    break;
                                case 1:
                                    preparedStatement.setBinaryStream(i2 + 1, (InputStream) objArr2[0], ((Integer) objArr2[1]).intValue());
                                    break;
                                case 2:
                                    preparedStatement.setAsciiStream(i2 + 1, (InputStream) objArr2[0], ((Integer) objArr2[1]).intValue());
                                    break;
                                default:
                                    throw new SQLException(this.resBundle.handleGetObject("crsreader.paramtype").toString());
                            }
                        }
                        if ((objArr2[1] instanceof Integer) && (objArr2[2] instanceof Integer)) {
                            preparedStatement.setObject(i2 + 1, objArr2[0], ((Integer) objArr2[1]).intValue(), ((Integer) objArr2[2]).intValue());
                        } else {
                            throw new SQLException(this.resBundle.handleGetObject("crsreader.paramtype").toString());
                        }
                    }
                } else {
                    preparedStatement.setObject(i2 + 1, objArr[i2]);
                }
            } else {
                preparedStatement.setObject(i2 + 1, objArr[i2]);
            }
        }
    }

    protected boolean getCloseConnection() {
        if (this.userCon) {
            return false;
        }
        return true;
    }

    public void setStartPosition(int i2) {
        this.startPosition = i2;
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
