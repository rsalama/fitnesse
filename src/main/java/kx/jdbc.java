//2007.04.20 c.java sql.date/time/timestamp
//jar cf jdbc.jar *.class   url(jdbc:q:host:port) isql(new service resources jdbc.jar)
package kx;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.Date;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.DriverPropertyInfo;
import java.sql.NClob;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.RowId;
import java.sql.RowIdLifetime;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Struct;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Map;
import java.util.Properties;

public class jdbc implements Driver
{
    static int V = 2, v = 0;

    static void O(String s)
    {
        System.out.println(s);
    }

    public int getMajorVersion()
    {
        return V;
    }

    public int getMinorVersion()
    {
        return v;
    }

    public boolean jdbcCompliant()
    {
        return false;
    }

    public boolean acceptsURL(String s)
    {
        return s.startsWith("jdbc:q:");
    }

    public Connection connect(String s, Properties p) throws SQLException
    {
        return !acceptsURL(s) ? null : new co(s.substring(7), p != null ? p
                        .get("user") : p, p != null ? p.get("password") : p);
    }

    public DriverPropertyInfo[] getPropertyInfo(String s, Properties p)
                    throws SQLException
    {
        return new DriverPropertyInfo[0];
    }

    static
    {
        try
        {
            DriverManager.registerDriver(new jdbc());
        }
        catch (Exception e)
        {
            O(e.getMessage());
        }
    }
    static int[] SQLTYPE = { 0, 16, 0, 0, -2, 5, 4, -5, 7, 8, 0, 12, 0, 0, 91,
                    93, 0, 0, 0, 92 };
    static String[] TYPE = { "", "boolean", "", "", "byte", "short", "int",
                    "long", "real", "float", "char", "symbol", "", "month",
                    "date", "timestamp", "", "minute", "second", "time" };

    static int find(String[] x, String s)
    {
        int i = 0;
        for (; i < x.length && !s.equals(x[i]);)
            ++i;
        return i;
    }

    static int find(int[] x, int j)
    {
        int i = 0;
        for (; i < x.length && x[i] != j;)
            ++i;
        return i;
    }

    static void q(String s) throws SQLException
    {
        throw new SQLException(s);
    }

    static void q() throws SQLException
    {
        q("nyi");
    }

    static void q(Exception e) throws SQLException
    {
    	e.printStackTrace();
        throw new SQLException(e.getMessage());
    }

    public class co implements Connection
    {
        private K k;

        public co(String s, Object u, Object p) throws SQLException
        {
            int i = s.indexOf(":");
            try
            {
                k = new K(s.substring(0, i), Integer.parseInt(s
                                .substring(i + 1)), u == null ? "" : (String) u
                                + ":" + (String) p);
            }
            catch (Exception e)
            {
                q(e);
            }
        }

        public Object ex(String s, Object[] p) throws SQLException
        {
            try
            {
                return 0 < K.n(p) ? k.k(s, p) : k.k(".o.ex", s.toCharArray());
            }
            catch (Exception e)
            {
                q(e);
                return null;
            }
        }

        public rs qx(String s) throws SQLException
        {
            try
            {
                return new rs(null, k.k(s));
            }
            catch (Exception e)
            {
                q(e);
                return null;
            }
        }

        public rs qx(String s, Object x) throws SQLException
        {
            try
            {
                return new rs(null, k.k(s, x));
            }
            catch (Exception e)
            {
                q(e);
                return null;
            }
        }

        private boolean a = true;

        public void setAutoCommit(boolean b) throws SQLException
        {
            a = b;
        }

        public boolean getAutoCommit() throws SQLException
        {
            return a;
        }

        public void rollback() throws SQLException
        {
        }

        public void commit() throws SQLException
        {
        }

        public boolean isClosed() throws SQLException
        {
            return k == null;
        }

        public Statement createStatement() throws SQLException
        {
            return new st(this);
        }

        public DatabaseMetaData getMetaData() throws SQLException
        {
            return new dm(this);
        }

        public PreparedStatement prepareStatement(String s) throws SQLException
        {
            return new ps(this, s);
        }

        public CallableStatement prepareCall(String s) throws SQLException
        {
            return new cs(this, s);
        }

        public String nativeSQL(String s) throws SQLException
        {
            return s;
        }

        private boolean b;
        private int i = TRANSACTION_SERIALIZABLE,
                        h = rs.HOLD_CURSORS_OVER_COMMIT;

        public void setReadOnly(boolean x) throws SQLException
        {
            b = x;
        }

        public boolean isReadOnly() throws SQLException
        {
            return b;
        }

        public void setCatalog(String s) throws SQLException
        {
            q("cat");
        }

        public String getCatalog() throws SQLException
        {
            q("cat");
            return null;
        }

        public void setTransactionIsolation(int x) throws SQLException
        {
            i = x;
        }

        public int getTransactionIsolation() throws SQLException
        {
            return i;
        }

        public SQLWarning getWarnings() throws SQLException
        {
            return null;
        }

        public void clearWarnings() throws SQLException
        {
        }

        public void close() throws SQLException
        {
            try
            {
                k.close();
            }
            catch (IOException e)
            {
                q(e);
            }
        }

        public Statement createStatement(int resultSetType,
                        int resultSetConcurrency) throws SQLException
        {
            return new st(this);
        }

        public PreparedStatement prepareStatement(String s, int resultSetType,
                        int resultSetConcurrency) throws SQLException
        {
            return new ps(this, s);
        }

        public CallableStatement prepareCall(String s, int resultSetType,
                        int resultSetConcurrency) throws SQLException
        {
            return new cs(this, s);
        }

        public Map getTypeMap() throws SQLException
        {
            return null;
        }

        // 3
        public void setHoldability(int holdability) throws SQLException
        {
            h = holdability;
        }

        public int getHoldability() throws SQLException
        {
            return h;
        }

        public Savepoint setSavepoint() throws SQLException
        {
            q("sav");
            return null;
        }

        public Savepoint setSavepoint(String name) throws SQLException
        {
            q("sav");
            return null;
        }

        public void rollback(Savepoint savepoint) throws SQLException
        {
        }

        public void releaseSavepoint(Savepoint savepoint) throws SQLException
        {
        }

        public Statement createStatement(int resultSetType,
                        int resultSetConcurrency, int resultSetHoldability)
                        throws SQLException
        {
            return new st(this);
        }

        public PreparedStatement prepareStatement(String s, int resultSetType,
                        int resultSetConcurrency, int resultSetHoldability)
                        throws SQLException
        {
            return new ps(this, s);
        }

        public CallableStatement prepareCall(String s, int resultSetType,
                        int resultSetConcurrency, int resultSetHoldability)
                        throws SQLException
        {
            return new cs(this, s);
        }

        public PreparedStatement prepareStatement(String s,
                        int autoGeneratedKeys) throws SQLException
        {
            return new ps(this, s);
        }

        public PreparedStatement prepareStatement(String s, int[] columnIndexes)
                        throws SQLException
        {
            return new ps(this, s);
        }

        public PreparedStatement prepareStatement(String s, String[] columnNames)
                        throws SQLException
        {
            return new ps(this, s);
        }

        @Override
        public Array createArrayOf(String arg0, Object[] arg1)
                        throws SQLException
        {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public Blob createBlob() throws SQLException
        {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public Clob createClob() throws SQLException
        {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public NClob createNClob() throws SQLException
        {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public SQLXML createSQLXML() throws SQLException
        {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public Struct createStruct(String arg0, Object[] arg1)
                        throws SQLException
        {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public Properties getClientInfo() throws SQLException
        {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public String getClientInfo(String arg0) throws SQLException
        {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public boolean isValid(int arg0) throws SQLException
        {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public void setClientInfo(Properties arg0)
                        throws SQLClientInfoException
        {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void setClientInfo(String arg0, String arg1)
                        throws SQLClientInfoException
        {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void setTypeMap(Map<String, Class<?>> arg0) throws SQLException
        {
            // TODO Auto-generated method stub
            
        }

        @Override
        public boolean isWrapperFor(Class<?> arg0) throws SQLException
        {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public <T> T unwrap(Class<T> arg0) throws SQLException
        {
            // TODO Auto-generated method stub
            return null;
        }
    }

    public class st implements Statement
    {
        private co co;
        private Object r;
        private int R, T;
        protected Object[] p = {};

        public st(co x)
        {
            co = x;
        }

        public int executeUpdate(String s) throws SQLException
        {
            co.ex(s, p);
            return -1;
        }

        public ResultSet executeQuery(String s) throws SQLException
        {
        	if (s.startsWith("q)"))
        	{
        		return new rs(this, co.ex(s, p));
        	}
        	else
        	{
				throw new SQLException("Illegal query: " + s);
        	}
        }

        public boolean execute(String s) throws SQLException
        {
            return null != (r = co.ex(s, p));
        }

        public ResultSet getResultSet() throws SQLException
        {
            return new rs(this, r);
        }

        public int getUpdateCount()
        {
            return -1;
        }

        public int getMaxRows() throws SQLException
        {
            return R;
        }

        public void setMaxRows(int i) throws SQLException
        {
            R = i;
        }

        public int getQueryTimeout() throws SQLException
        {
            return T;
        }

        public void setQueryTimeout(int i) throws SQLException
        {
            T = i;
        }

        // truncate excess BINARY,VARBINARY,LONGVARBINARY,CHAR,VARCHAR,and
        // LONGVARCHAR fields
        public int getMaxFieldSize() throws SQLException
        {
            return 0;
        }

        public void setMaxFieldSize(int i) throws SQLException
        {
        }

        public void setEscapeProcessing(boolean b) throws SQLException
        {
        }

        public void cancel() throws SQLException
        {
        }

        public SQLWarning getWarnings() throws SQLException
        {
            return null;
        }

        public void clearWarnings() throws SQLException
        {
        }

        // positioned update? different statement?
        public void setCursorName(String name) throws SQLException
        {
            q("cur");
        }

        public boolean getMoreResults() throws SQLException
        {
            return false;
        }

        public void close() throws SQLException
        {
            co = null;
        }

        public void setFetchDirection(int direction) throws SQLException
        {
            q("fd");
        }

        public int getFetchDirection() throws SQLException
        {
            return 0;
        }

        public void setFetchSize(int rows) throws SQLException
        {
        }

        public int getFetchSize() throws SQLException
        {
            return 0;
        }

        public int getResultSetConcurrency() throws SQLException
        {
            return rs.CONCUR_READ_ONLY;
        }

        public int getResultSetType() throws SQLException
        {
            return rs.TYPE_SCROLL_INSENSITIVE;
        }

        public void addBatch(String sql) throws SQLException
        {
            q("bat");
        }

        public void clearBatch() throws SQLException
        {
        }

        public int[] executeBatch() throws SQLException
        {
            return new int[0];
        }

        public Connection getConnection() throws SQLException
        {
            return co;
        }

        // 3
        public boolean getMoreResults(int current) throws SQLException
        {
            return false;
        }

        public ResultSet getGeneratedKeys() throws SQLException
        {
            return null;
        }

        public int executeUpdate(String sql, int autoGeneratedKeys)
                        throws SQLException
        {
            q("a");
            return 0;
        }

        public int executeUpdate(String sql, int[] columnIndexes)
                        throws SQLException
        {
            q("a");
            return 0;
        }

        public int executeUpdate(String sql, String[] columnNames)
                        throws SQLException
        {
            q("a");
            return 0;
        }

        public boolean execute(String sql, int autoGeneratedKeys)
                        throws SQLException
        {
            q("a");
            return false;
        }

        public boolean execute(String sql, int[] columnIndexes)
                        throws SQLException
        {
            q("a");
            return false;
        }

        public boolean execute(String sql, String[] columnNames)
                        throws SQLException
        {
            q("a");
            return false;
        }

        public int getResultSetHoldability() throws SQLException
        {
            return rs.HOLD_CURSORS_OVER_COMMIT;
        }

        @Override
        public boolean isClosed() throws SQLException
        {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public boolean isPoolable() throws SQLException
        {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public void setPoolable(boolean arg0) throws SQLException
        {
            // TODO Auto-generated method stub
            
        }

        @Override
        public boolean isWrapperFor(Class<?> arg0) throws SQLException
        {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public <T> T unwrap(Class<T> arg0) throws SQLException
        {
            // TODO Auto-generated method stub
            return null;
        }
    }

    public class ps extends st implements PreparedStatement
    {
        private String s;

        public ps(co co, String x)
        {
            super(co);
            s = x;
        }

        public ResultSet executeQuery() throws SQLException
        {
            return executeQuery(s);
        }

        public int executeUpdate() throws SQLException
        {
            return executeUpdate(s);
        }

        public boolean execute() throws SQLException
        {
            return execute(s);
        }

        public void clearParameters() throws SQLException
        {
            for (int i = 0; i < K.n(p);)
                p[i++] = null;
        }

        public void setObject(int i, Object x) throws SQLException
        {
            int n = K.n(p);
            if (i > n)
            {
                Object[] r = new Object[i];
                System.arraycopy(p, 0, r, 0, n);
                p = r;
                for (; n < i;)
                    p[n++] = null;
            }
            p[i - 1] = x;
        }

        public void setObject(int i, Object x, int targetSqlType)
                        throws SQLException
        {
            setObject(i, x);
        }

        public void setObject(int i, Object x, int targetSqlType, int scale)
                        throws SQLException
        {
            setObject(i, x);
        }

        public void setNull(int i, int t) throws SQLException
        {
            setObject(i, K.NULL[find(SQLTYPE, t)]);
        }

        public void setBoolean(int i, boolean x) throws SQLException
        {
            setObject(i, new Boolean(x));
        }

        public void setByte(int i, byte x) throws SQLException
        {
            setObject(i, new Byte(x));
        }

        public void setShort(int i, short x) throws SQLException
        {
            setObject(i, new Short(x));
        }

        public void setInt(int i, int x) throws SQLException
        {
            setObject(i, new Integer(x));
        }

        public void setLong(int i, long x) throws SQLException
        {
            setObject(i, new Long(x));
        }

        public void setFloat(int i, float x) throws SQLException
        {
            setObject(i, new Float(x));
        }

        public void setDouble(int i, double x) throws SQLException
        {
            setObject(i, new Double(x));
        }

        public void setString(int i, String x) throws SQLException
        {
            setObject(i, x);
        }

        public void setDate(int i, Date x) throws SQLException
        {
            setObject(i, x);
        }

        public void setTime(int i, Time x) throws SQLException
        {
            setObject(i, x);
        }

        public void setTimestamp(int i, Timestamp x) throws SQLException
        {
            setObject(i, x);
        }

        public void setBytes(int i, byte x[]) throws SQLException
        {
            q();
        }

        public void setBigDecimal(int i, BigDecimal x) throws SQLException
        {
            q();
        }

        public void setAsciiStream(int i, InputStream x, int length)
                        throws SQLException
        {
            q();
        }

        public void setUnicodeStream(int i, InputStream x, int length)
                        throws SQLException
        {
            q();
        }

        public void setBinaryStream(int i, InputStream x, int length)
                        throws SQLException
        {
            q();
        }

        public void addBatch() throws SQLException
        {
        }

        public void setCharacterStream(int parameterIndex, Reader reader,
                        int length) throws SQLException
        {
            q();
        }

        public void setRef(int i, Ref x) throws SQLException
        {
            q();
        }

        public void setBlob(int i, Blob x) throws SQLException
        {
            q();
        }

        public void setClob(int i, Clob x) throws SQLException
        {
            q();
        }

        public void setArray(int i, Array x) throws SQLException
        {
            q();
        }

        public ResultSetMetaData getMetaData() throws SQLException
        {
            q("m");
            return null;
        }

        public void setDate(int parameterIndex, Date x, Calendar cal)
                        throws SQLException
        {
            q();
        }

        public void setTime(int parameterIndex, Time x, Calendar cal)
                        throws SQLException
        {
            q();
        }

        public void setTimestamp(int parameterIndex, Timestamp x, Calendar cal)
                        throws SQLException
        {
            q();
        }

        public void setNull(int paramIndex, int sqlType, String typeName)
                        throws SQLException
        {
            q();
        }

        // 3
        public void setURL(int parameterIndex, URL x) throws SQLException
        {
            q();
        }

        public ParameterMetaData getParameterMetaData() throws SQLException
        {
            q("m");
            return null;
        }

        @Override
        public void setAsciiStream(int arg0, InputStream arg1)
                        throws SQLException
        {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void setAsciiStream(int arg0, InputStream arg1, long arg2)
                        throws SQLException
        {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void setBinaryStream(int arg0, InputStream arg1)
                        throws SQLException
        {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void setBinaryStream(int arg0, InputStream arg1, long arg2)
                        throws SQLException
        {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void setBlob(int arg0, InputStream arg1) throws SQLException
        {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void setBlob(int arg0, InputStream arg1, long arg2)
                        throws SQLException
        {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void setCharacterStream(int arg0, Reader arg1)
                        throws SQLException
        {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void setCharacterStream(int arg0, Reader arg1, long arg2)
                        throws SQLException
        {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void setClob(int arg0, Reader arg1) throws SQLException
        {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void setClob(int arg0, Reader arg1, long arg2)
                        throws SQLException
        {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void setNCharacterStream(int arg0, Reader arg1)
                        throws SQLException
        {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void setNCharacterStream(int arg0, Reader arg1, long arg2)
                        throws SQLException
        {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void setNClob(int arg0, NClob arg1) throws SQLException
        {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void setNClob(int arg0, Reader arg1) throws SQLException
        {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void setNClob(int arg0, Reader arg1, long arg2)
                        throws SQLException
        {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void setNString(int arg0, String arg1) throws SQLException
        {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void setRowId(int arg0, RowId arg1) throws SQLException
        {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void setSQLXML(int arg0, SQLXML arg1) throws SQLException
        {
            // TODO Auto-generated method stub
            
        }
    }

    public class cs extends ps implements CallableStatement
    {
        public cs(co c, String s)
        {
            super(c, s);
        }

        public void registerOutParameter(int i, int sqlType)
                        throws SQLException
        {
        }

        public void registerOutParameter(int i, int sqlType, int scale)
                        throws SQLException
        {
        }

        public boolean wasNull() throws SQLException
        {
            return false;
        }

        public String getString(int i) throws SQLException
        {
            return null;
        }

        public boolean getBoolean(int i) throws SQLException
        {
            return false;
        }

        public byte getByte(int i) throws SQLException
        {
            return 0;
        }

        public short getShort(int i) throws SQLException
        {
            return 0;
        }

        public int getInt(int i) throws SQLException
        {
            return 0;
        }

        public long getLong(int i) throws SQLException
        {
            return 0;
        }

        public float getFloat(int i) throws SQLException
        {
            return (float) 0.0;
        }

        public double getDouble(int i) throws SQLException
        {
            return 0.0;
        }

        public BigDecimal getBigDecimal(int i, int scale) throws SQLException
        {
            return null;
        }

        public Date getDate(int i) throws SQLException
        {
            return null;
        }

        public Time getTime(int i) throws SQLException
        {
            return null;
        }

        public Timestamp getTimestamp(int i) throws SQLException
        {
            return null;
        }

        public byte[] getBytes(int i) throws SQLException
        {
            return null;
        }

        public Object getObject(int i) throws SQLException
        {
            return null;
        }

        public BigDecimal getBigDecimal(int parameterIndex) throws SQLException
        {
            q();
            return null;
        }

        public Blob getBlob(int i) throws SQLException
        {
            q();
            return null;
        }

        public Clob getClob(int i) throws SQLException
        {
            q();
            return null;
        }

        public Array getArray(int i) throws SQLException
        {
            q();
            return null;
        }

        public Date getDate(int parameterIndex, Calendar cal)
                        throws SQLException
        {
            q();
            return null;
        }

        public Time getTime(int parameterIndex, Calendar cal)
                        throws SQLException
        {
            q();
            return null;
        }

        public Timestamp getTimestamp(int parameterIndex, Calendar cal)
                        throws SQLException
        {
            q();
            return null;
        }

        public void registerOutParameter(int paramIndex, int sqlType,
                        String typeName) throws SQLException
        {
            q();
        }

        // 3
        public void registerOutParameter(String parameterName, int sqlType)
                        throws SQLException
        {
            q();
        }

        public void registerOutParameter(String parameterName, int sqlType,
                        int scale) throws SQLException
        {
            q();
        }

        public void registerOutParameter(String parameterName, int sqlType,
                        String typeName) throws SQLException
        {
            q();
        }

        public URL getURL(int parameterIndex) throws SQLException
        {
            q();
            return null;
        }

        public void setURL(String parameterName, URL val) throws SQLException
        {
            q();
        }

        public void setNull(String parameterName, int sqlType)
                        throws SQLException
        {
            q();
        }

        public void setBoolean(String parameterName, boolean x)
                        throws SQLException
        {
            q();
        }

        public void setByte(String parameterName, byte x) throws SQLException
        {
            q();
        }

        public void setShort(String parameterName, short x) throws SQLException
        {
            q();
        }

        public void setInt(String parameterName, int x) throws SQLException
        {
            q();
        }

        public void setLong(String parameterName, long x) throws SQLException
        {
            q();
        }

        public void setFloat(String parameterName, float x) throws SQLException
        {
            q();
        }

        public void setDouble(String parameterName, double x)
                        throws SQLException
        {
            q();
        }

        public void setBigDecimal(String parameterName, BigDecimal x)
                        throws SQLException
        {
            q();
        }

        public void setString(String parameterName, String x)
                        throws SQLException
        {
            q();
        }

        public void setBytes(String parameterName, byte[] x)
                        throws SQLException
        {
            q();
        }

        public void setDate(String parameterName, Date x) throws SQLException
        {
            q();
        }

        public void setTime(String parameterName, Time x) throws SQLException
        {
            q();
        }

        public void setTimestamp(String parameterName, Timestamp x)
                        throws SQLException
        {
            q();
        }

        public void setAsciiStream(String parameterName, InputStream x,
                        int length) throws SQLException
        {
            q();
        }

        public void setBinaryStream(String parameterName, InputStream x,
                        int length) throws SQLException
        {
            q();
        }

        public void setObject(String parameterName, Object x,
                        int targetSqlType, int scale) throws SQLException
        {
            q();
        }

        public void setObject(String parameterName, Object x, int targetSqlType)
                        throws SQLException
        {
            q();
        }

        public void setObject(String parameterName, Object x)
                        throws SQLException
        {
            q();
        }

        public void setCharacterStream(String parameterName, Reader reader,
                        int length) throws SQLException
        {
            q();
        }

        public void setDate(String parameterName, Date x, Calendar cal)
                        throws SQLException
        {
            q();
        }

        public void setTime(String parameterName, Time x, Calendar cal)
                        throws SQLException
        {
            q();
        }

        public void setTimestamp(String parameterName, Timestamp x, Calendar cal)
                        throws SQLException
        {
            q();
        }

        public void setNull(String parameterName, int sqlType, String typeName)
                        throws SQLException
        {
            q();
        }

        public String getString(String parameterName) throws SQLException
        {
            return null;
        }

        public boolean getBoolean(String parameterName) throws SQLException
        {
            return false;
        }

        public byte getByte(String parameterName) throws SQLException
        {
            return 0;
        }

        public short getShort(String parameterName) throws SQLException
        {
            return 0;
        }

        public int getInt(String parameterName) throws SQLException
        {
            return 0;
        }

        public long getLong(String parameterName) throws SQLException
        {
            return 0;
        }

        public float getFloat(String parameterName) throws SQLException
        {
            return 0;
        }

        public double getDouble(String parameterName) throws SQLException
        {
            return 0;
        }

        public byte[] getBytes(String parameterName) throws SQLException
        {
            return null;
        }

        public Date getDate(String parameterName) throws SQLException
        {
            return null;
        }

        public Time getTime(String parameterName) throws SQLException
        {
            return null;
        }

        public Timestamp getTimestamp(String parameterName) throws SQLException
        {
            return null;
        }

        public Object getObject(String parameterName) throws SQLException
        {
            return null;
        }

        public BigDecimal getBigDecimal(String parameterName)
                        throws SQLException
        {
            return null;
        }

        public Ref getRef(String parameterName) throws SQLException
        {
            return null;
        }

        public Blob getBlob(String parameterName) throws SQLException
        {
            return null;
        }

        public Clob getClob(String parameterName) throws SQLException
        {
            return null;
        }

        public Array getArray(String parameterName) throws SQLException
        {
            return null;
        }

        public Date getDate(String parameterName, Calendar cal)
                        throws SQLException
        {
            return null;
        }

        public Time getTime(String parameterName, Calendar cal)
                        throws SQLException
        {
            return null;
        }

        public Timestamp getTimestamp(String parameterName, Calendar cal)
                        throws SQLException
        {
            return null;
        }

        public URL getURL(String parameterName) throws SQLException
        {
            return null;
        }

        @Override
        public Reader getCharacterStream(int arg0) throws SQLException
        {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public Reader getCharacterStream(String arg0) throws SQLException
        {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public Reader getNCharacterStream(int arg0) throws SQLException
        {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public Reader getNCharacterStream(String arg0) throws SQLException
        {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public NClob getNClob(int arg0) throws SQLException
        {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public NClob getNClob(String arg0) throws SQLException
        {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public String getNString(int arg0) throws SQLException
        {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public String getNString(String arg0) throws SQLException
        {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public Object getObject(int arg0, Map<String, Class<?>> arg1)
                        throws SQLException
        {
            q();
            return null;
        }

        @Override
        public Object getObject(String arg0, Map<String, Class<?>> arg1)
                        throws SQLException
        {
            q();
            return null;
        }

        @Override
        public RowId getRowId(int arg0) throws SQLException
        {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public RowId getRowId(String arg0) throws SQLException
        {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public SQLXML getSQLXML(int arg0) throws SQLException
        {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public SQLXML getSQLXML(String arg0) throws SQLException
        {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public void setAsciiStream(String arg0, InputStream arg1)
                        throws SQLException
        {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void setAsciiStream(String arg0, InputStream arg1, long arg2)
                        throws SQLException
        {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void setBinaryStream(String arg0, InputStream arg1)
                        throws SQLException
        {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void setBinaryStream(String arg0, InputStream arg1, long arg2)
                        throws SQLException
        {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void setBlob(String arg0, Blob arg1) throws SQLException
        {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void setBlob(String arg0, InputStream arg1) throws SQLException
        {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void setBlob(String arg0, InputStream arg1, long arg2)
                        throws SQLException
        {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void setCharacterStream(String arg0, Reader arg1)
                        throws SQLException
        {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void setCharacterStream(String arg0, Reader arg1, long arg2)
                        throws SQLException
        {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void setClob(String arg0, Clob arg1) throws SQLException
        {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void setClob(String arg0, Reader arg1) throws SQLException
        {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void setClob(String arg0, Reader arg1, long arg2)
                        throws SQLException
        {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void setNCharacterStream(String arg0, Reader arg1)
                        throws SQLException
        {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void setNCharacterStream(String arg0, Reader arg1, long arg2)
                        throws SQLException
        {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void setNClob(String arg0, NClob arg1) throws SQLException
        {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void setNClob(String arg0, Reader arg1) throws SQLException
        {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void setNClob(String arg0, Reader arg1, long arg2)
                        throws SQLException
        {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void setNString(String arg0, String arg1) throws SQLException
        {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void setRowId(String arg0, RowId arg1) throws SQLException
        {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void setSQLXML(String arg0, SQLXML arg1) throws SQLException
        {
            // TODO Auto-generated method stub
            
        }

        @Override
        public Ref getRef(int arg0) throws SQLException
        {
            // TODO Auto-generated method stub
            return null;
        }
    }

    public class rs implements ResultSet
    {
        private st st;
        private String[] f;
        private Object o, d[];
        private int r, n;

        public rs(st s, Object x) throws SQLException
        {
            st = s;
            K.Flip a = K.td(x);
            f = a.x;
            d = a.y;
            n = K.n(d[0]);
            r = -1;
        }

        public ResultSetMetaData getMetaData() throws SQLException
        {
            return new rm(f, d);
        }

        public int findColumn(String s) throws SQLException
        {
            return 1 + find(f, s);
        }

        public boolean next() throws SQLException
        {
            return ++r < n;
        }

        public boolean wasNull() throws SQLException
        {
            return o == null;
        }

        public Object getObject(int i) throws SQLException
        {
            return o = K.at(d[i - 1], r);
        }

        public boolean getBoolean(int i) throws SQLException
        {
            return ((Boolean) getObject(i)).booleanValue();
        }

        public byte getByte(int i) throws SQLException
        {
            return ((Byte) getObject(i)).byteValue();
        }

        public short getShort(int i) throws SQLException
        {
            Object x = getObject(i);
            return x == null ? 0 : ((Short) x).shortValue();
        }

        public int getInt(int i) throws SQLException
        {
            Object x = getObject(i);
            return x == null ? 0 : ((Integer) x).intValue();
        }

        public long getLong(int i) throws SQLException
        {
            Object x = getObject(i);
            return x == null ? 0 : ((Long) x).longValue();
        }

        public float getFloat(int i) throws SQLException
        {
            Object x = getObject(i);
            return x == null ? 0 : ((Float) x).floatValue();
        }

        public double getDouble(int i) throws SQLException
        {
            Object x = getObject(i);
            return x == null ? 0 : ((Double) x).doubleValue();
        }

        public String getString(int i) throws SQLException
        {
            Object x = getObject(i);
            return x == null ? null : x.toString();
        }

        public Date getDate(int i) throws SQLException
        {
            return (Date) getObject(i);
        }

        public Time getTime(int i) throws SQLException
        {
            return (Time) getObject(i);
        }

        public Timestamp getTimestamp(int i) throws SQLException
        {
            return (Timestamp) getObject(i);
        }

        public byte[] getBytes(int i) throws SQLException
        {
            q();
            return null;
        }

        public BigDecimal getBigDecimal(int i, int scale) throws SQLException
        {
            q();
            return null;
        }

        public InputStream getAsciiStream(int i) throws SQLException
        {
            q();
            return null;
        }

        public InputStream getUnicodeStream(int i) throws SQLException
        {
            q();
            return null;
        }

        public InputStream getBinaryStream(int i) throws SQLException
        {
            q();
            return null;
        }

        public Object getObject(String s) throws SQLException
        {
            return getObject(findColumn(s));
        }

        public boolean getBoolean(String s) throws SQLException
        {
            return getBoolean(findColumn(s));
        }

        public byte getByte(String s) throws SQLException
        {
            return getByte(findColumn(s));
        }

        public short getShort(String s) throws SQLException
        {
            return getShort(findColumn(s));
        }

        public int getInt(String s) throws SQLException
        {
            return getInt(findColumn(s));
        }

        public long getLong(String s) throws SQLException
        {
            return getLong(findColumn(s));
        }

        public float getFloat(String s) throws SQLException
        {
            return getFloat(findColumn(s));
        }

        public double getDouble(String s) throws SQLException
        {
            return getDouble(findColumn(s));
        }

        public String getString(String s) throws SQLException
        {
            return getString(findColumn(s));
        }

        public Date getDate(String s) throws SQLException
        {
            return getDate(findColumn(s));
        }

        public Time getTime(String s) throws SQLException
        {
            return getTime(findColumn(s));
        }

        public Timestamp getTimestamp(String s) throws SQLException
        {
            return getTimestamp(findColumn(s));
        }

        public byte[] getBytes(String s) throws SQLException
        {
            return getBytes(findColumn(s));
        }

        public BigDecimal getBigDecimal(String s, int scale)
                        throws SQLException
        {
            return getBigDecimal(findColumn(s), scale);
        }

        public InputStream getAsciiStream(String s) throws SQLException
        {
            return getAsciiStream(findColumn(s));
        }

        public InputStream getUnicodeStream(String s) throws SQLException
        {
            return getUnicodeStream(findColumn(s));
        }

        public InputStream getBinaryStream(String s) throws SQLException
        {
            return getBinaryStream(findColumn(s));
        }

        public SQLWarning getWarnings() throws SQLException
        {
            return null;
        }

        public void clearWarnings() throws SQLException
        {
        }

        public String getCursorName() throws SQLException
        {
            q("cur");
            return "";
        }

        public void close() throws SQLException
        {
            d = null;
        }

        public Reader getCharacterStream(int columnIndex) throws SQLException
        {
            q();
            return null;
        }

        public Reader getCharacterStream(String columnName) throws SQLException
        {
            q();
            return null;
        }

        public BigDecimal getBigDecimal(int columnIndex) throws SQLException
        {
            q();
            return null;
        }

        public BigDecimal getBigDecimal(String columnName) throws SQLException
        {
            q();
            return null;
        }

        public boolean isBeforeFirst() throws SQLException
        {
            return r < 0;
        }

        public boolean isAfterLast() throws SQLException
        {
            return r >= n;
        }

        public boolean isFirst() throws SQLException
        {
            return r == 0;
        }

        public boolean isLast() throws SQLException
        {
            return r == n - 1;
        }

        public void beforeFirst() throws SQLException
        {
            r = -1;
        }

        public void afterLast() throws SQLException
        {
            r = n;
        }

        public boolean first() throws SQLException
        {
            r = 0;
            return n > 0;
        }

        public boolean last() throws SQLException
        {
            r = n - 1;
            return n > 0;
        }

        public int getRow() throws SQLException
        {
            return r;
        }

        public boolean absolute(int row) throws SQLException
        {
            r = row;
            return r < n;
        }

        public boolean relative(int rows) throws SQLException
        {
            r += rows;
            return r >= 0 && r < n;
        }

        public boolean previous() throws SQLException
        {
            --r;
            return r >= 0;
        }

        public void setFetchDirection(int direction) throws SQLException
        {
            q("fd");
        }

        public int getFetchDirection() throws SQLException
        {
            return FETCH_FORWARD;
        }

        public void setFetchSize(int rows) throws SQLException
        {
        }

        public int getFetchSize() throws SQLException
        {
            return 0;
        }

        public int getType() throws SQLException
        {
            return TYPE_SCROLL_SENSITIVE;
        }

        public int getConcurrency() throws SQLException
        {
            return CONCUR_READ_ONLY;
        }

        public boolean rowUpdated() throws SQLException
        {
            q();
            return false;
        }

        public boolean rowInserted() throws SQLException
        {
            q();
            return false;
        }

        public boolean rowDeleted() throws SQLException
        {
            q();
            return false;
        }

        public void updateNull(int columnIndex) throws SQLException
        {
            q();
        }

        public void updateBoolean(int columnIndex, boolean x)
                        throws SQLException
        {
            q();
        }

        public void updateByte(int columnIndex, byte x) throws SQLException
        {
            q();
        }

        public void updateShort(int columnIndex, short x) throws SQLException
        {
            q();
        }

        public void updateInt(int columnIndex, int x) throws SQLException
        {
            q();
        }

        public void updateLong(int columnIndex, long x) throws SQLException
        {
            q();
        }

        public void updateFloat(int columnIndex, float x) throws SQLException
        {
            q();
        }

        public void updateDouble(int columnIndex, double x) throws SQLException
        {
            q();
        }

        public void updateBigDecimal(int columnIndex, BigDecimal x)
                        throws SQLException
        {
            q();
        }

        public void updateString(int columnIndex, String x) throws SQLException
        {
            q();
        }

        public void updateBytes(int columnIndex, byte[] x) throws SQLException
        {
            q();
        }

        public void updateDate(int columnIndex, Date x) throws SQLException
        {
            q();
        }

        public void updateTime(int columnIndex, Time x) throws SQLException
        {
            q();
        }

        public void updateTimestamp(int columnIndex, Timestamp x)
                        throws SQLException
        {
            q();
        }

        public void updateAsciiStream(int columnIndex, InputStream x, int length)
                        throws SQLException
        {
            q();
        }

        public void updateBinaryStream(int columnIndex, InputStream x,
                        int length) throws SQLException
        {
            q();
        }

        public void updateCharacterStream(int columnIndex, Reader x, int length)
                        throws SQLException
        {
            q();
        }

        public void updateObject(int columnIndex, Object x, int scale)
                        throws SQLException
        {
            q();
        }

        public void updateObject(int columnIndex, Object x) throws SQLException
        {
            q();
        }

        public void updateNull(String columnName) throws SQLException
        {
            q();
        }

        public void updateBoolean(String columnName, boolean x)
                        throws SQLException
        {
            q();
        }

        public void updateByte(String columnName, byte x) throws SQLException
        {
            q();
        }

        public void updateShort(String columnName, short x) throws SQLException
        {
            q();
        }

        public void updateInt(String columnName, int x) throws SQLException
        {
            q();
        }

        public void updateLong(String columnName, long x) throws SQLException
        {
            q();
        }

        public void updateFloat(String columnName, float x) throws SQLException
        {
            q();
        }

        public void updateDouble(String columnName, double x)
                        throws SQLException
        {
            q();
        }

        public void updateBigDecimal(String columnName, BigDecimal x)
                        throws SQLException
        {
            q();
        }

        public void updateString(String columnName, String x)
                        throws SQLException
        {
            q();
        }

        public void updateBytes(String columnName, byte[] x)
                        throws SQLException
        {
            q();
        }

        public void updateDate(String columnName, Date x) throws SQLException
        {
            q();
        }

        public void updateTime(String columnName, Time x) throws SQLException
        {
            q();
        }

        public void updateTimestamp(String columnName, Timestamp x)
                        throws SQLException
        {
            q();
        }

        public void updateAsciiStream(String columnName, InputStream x,
                        int length) throws SQLException
        {
            q();
        }

        public void updateBinaryStream(String columnName, InputStream x,
                        int length) throws SQLException
        {
            q();
        }

        public void updateCharacterStream(String columnName, Reader reader,
                        int length) throws SQLException
        {
            q();
        }

        public void updateObject(String columnName, Object x, int scale)
                        throws SQLException
        {
            q();
        }

        public void updateObject(String columnName, Object x)
                        throws SQLException
        {
            q();
        }

        public void insertRow() throws SQLException
        {
            q();
        }

        public void updateRow() throws SQLException
        {
            q();
        }

        public void deleteRow() throws SQLException
        {
            q();
        }

        public void refreshRow() throws SQLException
        {
            q();
        }

        public void cancelRowUpdates() throws SQLException
        {
            q();
        }

        public void moveToInsertRow() throws SQLException
        {
            q();
        }

        public void moveToCurrentRow() throws SQLException
        {
            q();
        }

        public Statement getStatement() throws SQLException
        {
            return st;
        }

        public Ref getRef(int i) throws SQLException
        {
            q();
            return null;
        }

        public Blob getBlob(int i) throws SQLException
        {
            q();
            return null;
        }

        public Clob getClob(int i) throws SQLException
        {
            q();
            return null;
        }

        public Array getArray(int i) throws SQLException
        {
            q();
            return null;
        }

        public Ref getRef(String colName) throws SQLException
        {
            q();
            return null;
        }

        public Blob getBlob(String colName) throws SQLException
        {
            q();
            return null;
        }

        public Clob getClob(String colName) throws SQLException
        {
            q();
            return null;
        }

        public Array getArray(String colName) throws SQLException
        {
            q();
            return null;
        }

        public Date getDate(int columnIndex, Calendar cal) throws SQLException
        {
            q();
            return null;
        }

        public Date getDate(String columnName, Calendar cal)
                        throws SQLException
        {
            q();
            return null;
        }

        public Time getTime(int columnIndex, Calendar cal) throws SQLException
        {
            q();
            return null;
        }

        public Time getTime(String columnName, Calendar cal)
                        throws SQLException
        {
            q();
            return null;
        }

        public Timestamp getTimestamp(int columnIndex, Calendar cal)
                        throws SQLException
        {
            q();
            return null;
        }

        public Timestamp getTimestamp(String columnName, Calendar cal)
                        throws SQLException
        {
            q();
            return null;
        }

        // 3
        public URL getURL(int columnIndex) throws SQLException
        {
            q();
            return null;
        }

        public URL getURL(String columnName) throws SQLException
        {
            q();
            return null;
        }

        public void updateRef(int columnIndex, Ref x) throws SQLException
        {
            q();
        }

        public void updateRef(String columnName, Ref x) throws SQLException
        {
            q();
        }

        public void updateBlob(int columnIndex, Blob x) throws SQLException
        {
            q();
        }

        public void updateBlob(String columnName, Blob x) throws SQLException
        {
            q();
        }

        public void updateClob(int columnIndex, Clob x) throws SQLException
        {
            q();
        }

        public void updateClob(String columnName, Clob x) throws SQLException
        {
            q();
        }

        public void updateArray(int columnIndex, Array x) throws SQLException
        {
            q();
        }

        public void updateArray(String columnName, Array x) throws SQLException
        {
            q();
        }

        @Override
        public int getHoldability() throws SQLException
        {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public Reader getNCharacterStream(int arg0) throws SQLException
        {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public Reader getNCharacterStream(String arg0) throws SQLException
        {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public NClob getNClob(int arg0) throws SQLException
        {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public NClob getNClob(String arg0) throws SQLException
        {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public String getNString(int arg0) throws SQLException
        {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public String getNString(String arg0) throws SQLException
        {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public Object getObject(int arg0, Map<String, Class<?>> arg1)
                        throws SQLException
        {
            q();
            return null;
        }

        @Override
        public Object getObject(String arg0, Map<String, Class<?>> arg1)
                        throws SQLException
        {
            q();
            return null;
        }

        @Override
        public RowId getRowId(int arg0) throws SQLException
        {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public RowId getRowId(String arg0) throws SQLException
        {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public SQLXML getSQLXML(int arg0) throws SQLException
        {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public SQLXML getSQLXML(String arg0) throws SQLException
        {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public boolean isClosed() throws SQLException
        {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public void updateAsciiStream(int arg0, InputStream arg1)
                        throws SQLException
        {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void updateAsciiStream(String arg0, InputStream arg1)
                        throws SQLException
        {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void updateAsciiStream(int arg0, InputStream arg1, long arg2)
                        throws SQLException
        {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void updateAsciiStream(String arg0, InputStream arg1, long arg2)
                        throws SQLException
        {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void updateBinaryStream(int arg0, InputStream arg1)
                        throws SQLException
        {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void updateBinaryStream(String arg0, InputStream arg1)
                        throws SQLException
        {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void updateBinaryStream(int arg0, InputStream arg1, long arg2)
                        throws SQLException
        {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void updateBinaryStream(String arg0, InputStream arg1, long arg2)
                        throws SQLException
        {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void updateBlob(int arg0, InputStream arg1) throws SQLException
        {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void updateBlob(String arg0, InputStream arg1)
                        throws SQLException
        {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void updateBlob(int arg0, InputStream arg1, long arg2)
                        throws SQLException
        {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void updateBlob(String arg0, InputStream arg1, long arg2)
                        throws SQLException
        {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void updateCharacterStream(int arg0, Reader arg1)
                        throws SQLException
        {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void updateCharacterStream(String arg0, Reader arg1)
                        throws SQLException
        {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void updateCharacterStream(int arg0, Reader arg1, long arg2)
                        throws SQLException
        {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void updateCharacterStream(String arg0, Reader arg1, long arg2)
                        throws SQLException
        {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void updateClob(int arg0, Reader arg1) throws SQLException
        {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void updateClob(String arg0, Reader arg1) throws SQLException
        {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void updateClob(int arg0, Reader arg1, long arg2)
                        throws SQLException
        {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void updateClob(String arg0, Reader arg1, long arg2)
                        throws SQLException
        {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void updateNCharacterStream(int arg0, Reader arg1)
                        throws SQLException
        {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void updateNCharacterStream(String arg0, Reader arg1)
                        throws SQLException
        {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void updateNCharacterStream(int arg0, Reader arg1, long arg2)
                        throws SQLException
        {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void updateNCharacterStream(String arg0, Reader arg1, long arg2)
                        throws SQLException
        {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void updateNClob(int arg0, NClob arg1) throws SQLException
        {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void updateNClob(String arg0, NClob arg1) throws SQLException
        {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void updateNClob(int arg0, Reader arg1) throws SQLException
        {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void updateNClob(String arg0, Reader arg1) throws SQLException
        {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void updateNClob(int arg0, Reader arg1, long arg2)
                        throws SQLException
        {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void updateNClob(String arg0, Reader arg1, long arg2)
                        throws SQLException
        {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void updateNString(int arg0, String arg1) throws SQLException
        {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void updateNString(String arg0, String arg1) throws SQLException
        {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void updateRowId(int arg0, RowId arg1) throws SQLException
        {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void updateRowId(String arg0, RowId arg1) throws SQLException
        {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void updateSQLXML(int arg0, SQLXML arg1) throws SQLException
        {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void updateSQLXML(String arg0, SQLXML arg1) throws SQLException
        {
            // TODO Auto-generated method stub
            
        }

        @Override
        public boolean isWrapperFor(Class<?> iface) throws SQLException
        {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public <T> T unwrap(Class<T> iface) throws SQLException
        {
            // TODO Auto-generated method stub
            return null;
        }
    }

    public class rm implements ResultSetMetaData
    {
        private String[] f;
        private Object[] d;

        public rm(String[] x, Object[] y)
        {
            f = x;
            d = y;
        }

        public int getColumnCount() throws SQLException
        {
            return f.length;
        }

        public String getColumnName(int i) throws SQLException
        {
            return f[i - 1];
        }

        public String getColumnTypeName(int i) throws SQLException
        {
            return TYPE[K.t(d[i - 1])];
        }

        public int getColumnDisplaySize(int i) throws SQLException
        {
            return 11;
        }

        public int getScale(int i) throws SQLException
        {
            return 2;
        }

        public int isNullable(int i) throws SQLException
        {
            return 1;
        }

        public String getColumnLabel(int i) throws SQLException
        {
            return getColumnName(i);
        }

        public int getColumnType(int i) throws SQLException
        {
            return SQLTYPE[K.t(d[i - 1])];
        }

        public int getPrecision(int i) throws SQLException
        {
            return 11;
        } // SQLPREC[c.t(d[i-1])];}

        public boolean isSigned(int i) throws SQLException
        {
            return true;
        }

        public String getTableName(int i) throws SQLException
        {
            return "";
        }

        public String getSchemaName(int i) throws SQLException
        {
            return "";
        }

        public String getCatalogName(int i) throws SQLException
        {
            return "";
        }

        public boolean isReadOnly(int i) throws SQLException
        {
            return false;
        }

        public boolean isWritable(int i) throws SQLException
        {
            return false;
        }

        public boolean isDefinitelyWritable(int i) throws SQLException
        {
            return false;
        }

        public boolean isAutoIncrement(int i) throws SQLException
        {
            return false;
        }

        public boolean isCaseSensitive(int i) throws SQLException
        {
            return true;
        }

        public boolean isSearchable(int i) throws SQLException
        {
            return true;
        }

        public boolean isCurrency(int i) throws SQLException
        {
            return false;
        }

        public String getColumnClassName(int column) throws SQLException
        {
            q("col");
            return null;
        }

        @Override
        public boolean isWrapperFor(Class<?> iface) throws SQLException
        {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public <T> T unwrap(Class<T> iface) throws SQLException
        {
            // TODO Auto-generated method stub
            return null;
        }
    }

    public class dm implements DatabaseMetaData
    {
        private co co;

        public dm(co x)
        {
            co = x;
        }

        public ResultSet getCatalogs() throws SQLException
        {
            return co.qx("([]TABLE_CAT:`symbol$())");
        }

        public ResultSet getSchemas() throws SQLException
        {
            return co.qx("([]TABLE_SCHEM:`symbol$())");
        }

        public ResultSet getTableTypes() throws SQLException
        {
            return co.qx("([]TABLE_TYPE:`TABLE`VIEW)");
        }

        public ResultSet getTables(String a, String b, String t, String x[])
                        throws SQLException
        {
            return co
                            .qx(
                                            "raze{([]TABLE_CAT:`;TABLE_SCHEM:`;TABLE_NAME:system string`a`b x=`VIEW;TABLE_TYPE:x)}each",
                                            x);
        }

        public ResultSet getTypeInfo() throws SQLException
        {
            return co
                            .qx("`DATA_TYPE xasc([]TYPE_NAME:`boolean`byte`short`int`long`real`float`symbol`date`time`timestamp;DATA_TYPE:16 -2 5 4 -5 7 8 12 91 92 93;PRECISION:11;LITERAL_PREFIX:`;LITERAL_SUFFIX:`;CREATE_PARAMS:`;NULLABLE:1h;CASE_SENSITIVE:1b;SEARCHABLE:1h;UNSIGNED_ATTRIBUTE:0b;FIXED_PREC_SCALE:0b;AUTO_INCREMENT:0b;LOCAL_TYPE_NAME:`;MINIMUM_SCALE:0h;MAXIMUM_SCALE:0h;SQL_DATA_TYPE:0;SQL_DATETIME_SUB:0;NUM_PREC_RADIX:10)");
        }

        public ResultSet getColumns(String a, String b, String t, String c)
                        throws SQLException
        {
            if (t.startsWith("%"))
                t = "";
            return co
                            .qx("select TABLE_CAT:`,TABLE_SCHEM:`,TABLE_NAME:n,COLUMN_NAME:c,DATA_TYPE:0,TYPE_NAME:t,COLUMN_SIZE:2000000000,BUFFER_LENGTH:0,DECIMAL_DIGITS:16,NUM_PREC_RADIX:10,NULLABLE:1,REMARKS:`,COLUMN_DEF:`,SQL_DATA_TYPE:0,SQL_DATETIME_SUB:0,CHAR_OCTET_LENGTH:2000000000,ORDINAL_POSITION:1+til count n,NULLABLE:`YES from .Q.nct`"
                                            + t);
        }

        public ResultSet getPrimaryKeys(String a, String b, String t)
                        throws SQLException
        {
            q("pk");
            return co.qx("");
        } // "q)([]TABLE_CAT:'',TABLE_SCHEM:'',TABLE_NAME:'"+t+"',COLUMN_NAME:key "+t+",KEY_SEQ:1+asc count key "+t+",PK_NAME:'')");}

        public ResultSet getImportedKeys(String a, String b, String t)
                        throws SQLException
        {
            q("imp");
            return co.qx("");
        } // "q)select PKTABLE_CAT:'',PKTABLE_SCHEM:'',PKTABLE_NAME:x,PKCOLUMN_NAME:first each key each x,FKTABLE_CAT:'',FKTABLE_SCHEM:'',FKTABLE_NAME:'"+t+"',FKCOLUMN_NAME:y,KEY_SEQ:1,UPDATE_RULE:1,DELETE_RULE:0,FK_NAME:'',PK_NAME:'',DEFERRABILITY:0 from('x','y')vars fkey "+t);}

        public ResultSet getProcedures(String a, String b, String p)
                        throws SQLException
        {
            q("pr");
            return co.qx("");
        } // "q)([]PROCEDURE_CAT:'',PROCEDURE_SCHEM:'',PROCEDURE_NAME:varchar(),r0:0,r1:0,r2:0,REMARKS:'',PROCEDURE_TYPE:0)");}

        public ResultSet getExportedKeys(String a, String b, String t)
                        throws SQLException
        {
            q("exp");
            return null;
        }

        public ResultSet getCrossReference(String pa, String pb, String pt,
                        String fa, String fb, String ft) throws SQLException
        {
            q("cr");
            return null;
        }

        public ResultSet getIndexInfo(String a, String b, String t,
                        boolean unique, boolean approximate)
                        throws SQLException
        {
            q("ii");
            return null;
        }

        public ResultSet getProcedureColumns(String a, String b, String p,
                        String c) throws SQLException
        {
            q("pc");
            return null;
        }

        // PROCEDURE_CAT PROCEDURE_SCHEM PROCEDURE_NAME ...
        public ResultSet getColumnPrivileges(String a, String b, String table,
                        String columnNamePattern) throws SQLException
        {
            q("cp");
            return null;
        }

        // select TABLE_CAT TABLE_SCHEM TABLE_NAME COLUMN_NAME GRANTOR GRANTEE
        // PRIVILEGE IS_GRANTABLE ordered by COLUMN_NAME and PRIVILEGE.
        public ResultSet getTablePrivileges(String a, String b, String t)
                        throws SQLException
        {
            q("tp");
            return null;
        }

        // select TABLE_CAT TABLE_SCHEM TABLE_NAME GRANTOR GRANTEE PRIVILEGE
        // IS_GRANTABLE ordered by TABLE_SCHEM,TABLE_NAME,and PRIVILEGE.
        public ResultSet getBestRowIdentifier(String a, String b, String t,
                        int scope, boolean nullable) throws SQLException
        {
            q("br");
            return null;
        }

        // select SCOPE COLUMN_NAME DATA_TYPE TYPE_NAME COLUMN_SIZE
        // DECIMAL_DIGITS PSEUDO_COLUMN ordered by SCOPE
        public ResultSet getVersionColumns(String a, String b, String t)
                        throws SQLException
        {
            q("vc");
            return null;
        }

        // select SCOPE COLUMN_NAME DATA_TYPE TYPE_NAME COLUMN_SIZE
        // DECIMAL_DIGITS PSEUDO_COLUMN ordered by SCOPE
        public boolean allProceduresAreCallable() throws SQLException
        {
            return true;
        }

        public boolean allTablesAreSelectable() throws SQLException
        {
            return true;
        }

        public boolean dataDefinitionCausesTransactionCommit()
                        throws SQLException
        {
            return false;
        }

        public boolean dataDefinitionIgnoredInTransactions()
                        throws SQLException
        {
            return false;
        }

        public boolean doesMaxRowSizeIncludeBlobs() throws SQLException
        {
            return true;
        }

        public String getSchemaTerm() throws SQLException
        {
            return "schema";
        }

        public String getProcedureTerm() throws SQLException
        {
            return "procedure";
        }

        public String getCatalogTerm() throws SQLException
        {
            return "catalog";
        }

        public String getCatalogSeparator() throws SQLException
        {
            return ".";
        }

        public int getMaxBinaryLiteralLength() throws SQLException
        {
            return 0;
        }

        public int getMaxCharLiteralLength() throws SQLException
        {
            return 0;
        }

        public int getMaxColumnNameLength() throws SQLException
        {
            return 0;
        }

        public int getMaxColumnsInGroupBy() throws SQLException
        {
            return 0;
        }

        public int getMaxColumnsInIndex() throws SQLException
        {
            return 0;
        }

        public int getMaxColumnsInOrderBy() throws SQLException
        {
            return 0;
        }

        public int getMaxColumnsInSelect() throws SQLException
        {
            return 0;
        }

        public int getMaxColumnsInTable() throws SQLException
        {
            return 0;
        }

        public int getMaxConnections() throws SQLException
        {
            return 0;
        }

        public int getMaxCursorNameLength() throws SQLException
        {
            return 0;
        }

        public int getMaxIndexLength() throws SQLException
        {
            return 0;
        }

        public int getMaxSchemaNameLength() throws SQLException
        {
            return 0;
        }

        public int getMaxProcedureNameLength() throws SQLException
        {
            return 0;
        }

        public int getMaxCatalogNameLength() throws SQLException
        {
            return 0;
        }

        public int getMaxRowSize() throws SQLException
        {
            return 0;
        }

        public int getMaxStatementLength() throws SQLException
        {
            return 0;
        }

        public int getMaxStatements() throws SQLException
        {
            return 0;
        }

        public int getMaxTableNameLength() throws SQLException
        {
            return 0;
        }

        public int getMaxTablesInSelect() throws SQLException
        {
            return 0;
        }

        public int getMaxUserNameLength() throws SQLException
        {
            return 0;
        }

        public int getDefaultTransactionIsolation() throws SQLException
        {
            return co.TRANSACTION_SERIALIZABLE;
        }

        public String getSQLKeywords() throws SQLException
        {
            return "show,meta,load,save";
        }

        public String getNumericFunctions() throws SQLException
        {
            return "";
        }

        public String getStringFunctions() throws SQLException
        {
            return "";
        }

        public String getSystemFunctions() throws SQLException
        {
            return "";
        }

        public String getTimeDateFunctions() throws SQLException
        {
            return "";
        }

        public String getSearchStringEscape() throws SQLException
        {
            return "";
        }

        public String getExtraNameCharacters() throws SQLException
        {
            return "";
        }

        public String getIdentifierQuoteString() throws SQLException
        {
            return "";
        }

        public String getURL() throws SQLException
        {
            return null;
        }

        public String getUserName() throws SQLException
        {
            return "";
        }

        public String getDatabaseProductName() throws SQLException
        {
            return "kdb";
        }

        public String getDatabaseProductVersion() throws SQLException
        {
            return "2.0";
        }

        public String getDriverName() throws SQLException
        {
            return "jdbc";
        }

        public String getDriverVersion() throws SQLException
        {
            return V + "." + v;
        }

        public int getDriverMajorVersion()
        {
            return V;
        }

        public int getDriverMinorVersion()
        {
            return v;
        }

        public boolean isCatalogAtStart() throws SQLException
        {
            return true;
        }

        public boolean isReadOnly() throws SQLException
        {
            return false;
        }

        public boolean nullsAreSortedHigh() throws SQLException
        {
            return false;
        }

        public boolean nullsAreSortedLow() throws SQLException
        {
            return true;
        }

        public boolean nullsAreSortedAtStart() throws SQLException
        {
            return false;
        }

        public boolean nullsAreSortedAtEnd() throws SQLException
        {
            return false;
        }

        public boolean supportsMixedCaseIdentifiers() throws SQLException
        {
            return false;
        }

        public boolean storesUpperCaseIdentifiers() throws SQLException
        {
            return false;
        }

        public boolean storesLowerCaseIdentifiers() throws SQLException
        {
            return false;
        }

        public boolean storesMixedCaseIdentifiers() throws SQLException
        {
            return true;
        }

        public boolean supportsMixedCaseQuotedIdentifiers() throws SQLException
        {
            return true;
        }

        public boolean storesUpperCaseQuotedIdentifiers() throws SQLException
        {
            return false;
        }

        public boolean storesLowerCaseQuotedIdentifiers() throws SQLException
        {
            return false;
        }

        public boolean storesMixedCaseQuotedIdentifiers() throws SQLException
        {
            return true;
        }

        public boolean supportsAlterTableWithAddColumn() throws SQLException
        {
            return true;
        }

        public boolean supportsAlterTableWithDropColumn() throws SQLException
        {
            return true;
        }

        public boolean supportsTableCorrelationNames() throws SQLException
        {
            return true;
        }

        public boolean supportsDifferentTableCorrelationNames()
                        throws SQLException
        {
            return true;
        }

        public boolean supportsColumnAliasing() throws SQLException
        {
            return true;
        }

        public boolean nullPlusNonNullIsNull() throws SQLException
        {
            return true;
        }

        public boolean supportsExpressionsInOrderBy() throws SQLException
        {
            return true;
        }

        public boolean supportsOrderByUnrelated() throws SQLException
        {
            return false;
        }

        public boolean supportsGroupBy() throws SQLException
        {
            return true;
        }

        public boolean supportsGroupByUnrelated() throws SQLException
        {
            return false;
        }

        public boolean supportsGroupByBeyondSelect() throws SQLException
        {
            return false;
        }

        public boolean supportsLikeEscapeClause() throws SQLException
        {
            return false;
        }

        public boolean supportsMultipleResultSets() throws SQLException
        {
            return false;
        }

        public boolean supportsMultipleTransactions() throws SQLException
        {
            return false;
        }

        public boolean supportsNonNullableColumns() throws SQLException
        {
            return true;
        }

        public boolean supportsMinimumSQLGrammar() throws SQLException
        {
            return true;
        }

        public boolean supportsCoreSQLGrammar() throws SQLException
        {
            return true;
        }

        public boolean supportsExtendedSQLGrammar() throws SQLException
        {
            return false;
        }

        public boolean supportsANSI92EntryLevelSQL() throws SQLException
        {
            return true;
        }

        public boolean supportsANSI92IntermediateSQL() throws SQLException
        {
            return false;
        }

        public boolean supportsANSI92FullSQL() throws SQLException
        {
            return false;
        }

        public boolean supportsIntegrityEnhancementFacility()
                        throws SQLException
        {
            return false;
        }

        public boolean supportsOuterJoins() throws SQLException
        {
            return false;
        }

        public boolean supportsFullOuterJoins() throws SQLException
        {
            return false;
        }

        public boolean supportsLimitedOuterJoins() throws SQLException
        {
            return false;
        }

        public boolean supportsConvert() throws SQLException
        {
            return false;
        }

        public boolean supportsConvert(int fromType, int toType)
                        throws SQLException
        {
            return false;
        }

        public boolean supportsSchemasInDataManipulation() throws SQLException
        {
            return false;
        }

        public boolean supportsSchemasInProcedureCalls() throws SQLException
        {
            return false;
        }

        public boolean supportsSchemasInTableDefinitions() throws SQLException
        {
            return false;
        }

        public boolean supportsSchemasInIndexDefinitions() throws SQLException
        {
            return false;
        }

        public boolean supportsSchemasInPrivilegeDefinitions()
                        throws SQLException
        {
            return false;
        }

        public boolean supportsCatalogsInDataManipulation() throws SQLException
        {
            return false;
        }

        public boolean supportsCatalogsInProcedureCalls() throws SQLException
        {
            return false;
        }

        public boolean supportsCatalogsInTableDefinitions() throws SQLException
        {
            return false;
        }

        public boolean supportsCatalogsInIndexDefinitions() throws SQLException
        {
            return false;
        }

        public boolean supportsCatalogsInPrivilegeDefinitions()
                        throws SQLException
        {
            return false;
        }

        public boolean supportsSelectForUpdate() throws SQLException
        {
            return false;
        }

        public boolean supportsPositionedDelete() throws SQLException
        {
            return false;
        }

        public boolean supportsPositionedUpdate() throws SQLException
        {
            return false;
        }

        public boolean supportsOpenCursorsAcrossCommit() throws SQLException
        {
            return true;
        }

        public boolean supportsOpenCursorsAcrossRollback() throws SQLException
        {
            return true;
        }

        public boolean supportsOpenStatementsAcrossCommit() throws SQLException
        {
            return true;
        }

        public boolean supportsOpenStatementsAcrossRollback()
                        throws SQLException
        {
            return true;
        }

        public boolean supportsStoredProcedures() throws SQLException
        {
            return false;
        }

        public boolean supportsSubqueriesInComparisons() throws SQLException
        {
            return true;
        }

        public boolean supportsSubqueriesInExists() throws SQLException
        {
            return true;
        }

        public boolean supportsSubqueriesInIns() throws SQLException
        {
            return true;
        }

        public boolean supportsSubqueriesInQuantifieds() throws SQLException
        {
            return true;
        }

        public boolean supportsCorrelatedSubqueries() throws SQLException
        {
            return true;
        }

        public boolean supportsUnion() throws SQLException
        {
            return true;
        }

        public boolean supportsUnionAll() throws SQLException
        {
            return true;
        }

        public boolean supportsTransactions() throws SQLException
        {
            return true;
        }

        public boolean supportsTransactionIsolationLevel(int level)
                        throws SQLException
        {
            return true;
        }

        public boolean supportsDataDefinitionAndDataManipulationTransactions()
                        throws SQLException
        {
            return true;
        }

        public boolean supportsDataManipulationTransactionsOnly()
                        throws SQLException
        {
            return false;
        }

        public boolean usesLocalFiles() throws SQLException
        {
            return false;
        }

        public boolean usesLocalFilePerTable() throws SQLException
        {
            return false;
        }

        public boolean supportsResultSetType(int type) throws SQLException
        {
            return type != rs.TYPE_SCROLL_SENSITIVE;
        }

        public boolean supportsResultSetConcurrency(int type, int concurrency)
                        throws SQLException
        {
            return type == rs.CONCUR_READ_ONLY;
        }

        public boolean ownUpdatesAreVisible(int type) throws SQLException
        {
            return false;
        }

        public boolean ownDeletesAreVisible(int type) throws SQLException
        {
            return false;
        }

        public boolean ownInsertsAreVisible(int type) throws SQLException
        {
            return false;
        }

        public boolean othersUpdatesAreVisible(int type) throws SQLException
        {
            return false;
        }

        public boolean othersDeletesAreVisible(int type) throws SQLException
        {
            return false;
        }

        public boolean othersInsertsAreVisible(int type) throws SQLException
        {
            return false;
        }

        public boolean updatesAreDetected(int type) throws SQLException
        {
            return false;
        }

        public boolean deletesAreDetected(int type) throws SQLException
        {
            return false;
        }

        public boolean insertsAreDetected(int type) throws SQLException
        {
            return false;
        }

        public boolean supportsBatchUpdates() throws SQLException
        {
            return false;
        }

        public ResultSet getUDTs(String catalog, String schemaPattern,
                        String typeNamePattern, int[] types)
                        throws SQLException
        {
            return null;
        }

        public Connection getConnection() throws SQLException
        {
            return co;
        }

        // 3
        public boolean supportsSavepoints() throws SQLException
        {
            return false;
        }

        public boolean supportsNamedParameters() throws SQLException
        {
            return false;
        }

        public boolean supportsMultipleOpenResults() throws SQLException
        {
            return false;
        }

        public boolean supportsGetGeneratedKeys() throws SQLException
        {
            return false;
        }

        public ResultSet getSuperTypes(String catalog, String schemaPattern,
                        String typeNamePattern) throws SQLException
        {
            return null;
        }

        public ResultSet getSuperTables(String catalog, String schemaPattern,
                        String tableNamePattern) throws SQLException
        {
            return null;
        }

        public ResultSet getAttributes(String catalog, String schemaPattern,
                        String typeNamePattern, String attributeNamePattern)
                        throws SQLException
        {
            return null;
        }

        public boolean supportsResultSetHoldability(int holdability)
                        throws SQLException
        {
            return false;
        }

        public int getResultSetHoldability() throws SQLException
        {
            return 0;
        }

        public int getDatabaseMajorVersion() throws SQLException
        {
            return 0;
        }

        public int getDatabaseMinorVersion() throws SQLException
        {
            return 0;
        }

        public int getJDBCMajorVersion() throws SQLException
        {
            return 0;
        }

        public int getJDBCMinorVersion() throws SQLException
        {
            return 0;
        }

        public int getSQLStateType() throws SQLException
        {
            return 0;
        }

        public boolean locatorsUpdateCopy() throws SQLException
        {
            return false;
        }

        public boolean supportsStatementPooling() throws SQLException
        {
            return false;
        }

        @Override
        public boolean autoCommitFailureClosesAllResultSets()
                        throws SQLException
        {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public ResultSet getClientInfoProperties() throws SQLException
        {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public ResultSet getFunctionColumns(String arg0, String arg1,
                        String arg2, String arg3) throws SQLException
        {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public ResultSet getFunctions(String arg0, String arg1, String arg2)
                        throws SQLException
        {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public RowIdLifetime getRowIdLifetime() throws SQLException
        {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public ResultSet getSchemas(String arg0, String arg1)
                        throws SQLException
        {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public boolean supportsStoredFunctionsUsingCallSyntax()
                        throws SQLException
        {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public boolean isWrapperFor(Class<?> iface) throws SQLException
        {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public <T> T unwrap(Class<T> iface) throws SQLException
        {
            // TODO Auto-generated method stub
            return null;
        }
    }
}

/*
 * class ar implements Array{ public String getBaseTypeName()throws
 * SQLException{q();return null;} public int getBaseType()throws
 * SQLException{q();return 0;} public Object getArray()throws
 * SQLException{q();return null;} public Object getArray(Map map)throws
 * SQLException{q();return null;} public Object getArray(long index,int
 * count)throws SQLException{q();return null;} public Object getArray(long
 * index,int count,Map map)throws SQLException{q();return null;} public
 * ResultSet getResultSet()throws SQLException{q();return null;} public
 * ResultSet getResultSet(Map map)throws SQLException{q();return null;} public
 * ResultSet getResultSet(long index,int count)throws SQLException{q();return
 * null;} public ResultSet getResultSet(long index,int count,Map map)throws
 * SQLException{q();return null;}} class bl implements Blob{ public long
 * length()throws SQLException{q();return 0L;} public byte[]getBytes(long
 * pos,int length)throws SQLException{q();return null;} public InputStream
 * getBinaryStream()throws SQLException{q();return null;} public long
 * position(byte[]pattern,long start)throws SQLException{q();return 0L;} public
 * long position(Blob pattern,long start)throws SQLException{q();return 0L;}}
 * class cl implements Clob{ public long length()throws SQLException{q();return
 * 0L;} public String getSubString(long pos,int length)throws
 * SQLException{q();return null;} public Reader getCharacterStream()throws
 * SQLException{q();return null;} public InputStream getAsciiStream()throws
 * SQLException{q();return null;} public long position(String searchstr,long
 * start)throws SQLException{q();return 0L;} public long position(Clob
 * searchstr,long start)throws SQLException{q();return 0L;}} class re implements
 * Ref{public String getBaseTypeName()throws SQLException{q();return null;}} //
 * DriverPropertyInfo a=new DriverPropertyInfo("user",null),b=new
 * DriverPropertyInfo("password",null),r[]=new DriverPropertyInfo[2]; //
 * a.required=b.required=false;r[0]=a;r[1]=b;for(int
 * i=0;i<r.length;i++)r[i].value = p.getProperty(r[i].name);return r;} public
 * ResultSet getBestRowIdentifier(String a,String b,String t,int scope,boolean
 * nullable)throws SQLException {return co.qx("select
 * SCOPE:'1',COLUMN_NAME:name,
 * DATA_TYPE:([x:('int','float','varchar','date','time','timestamp','varbinary')]
 * y
 * :(4,8,12,91,92,93,-3,1111))[T].y,TYPE_NAME:T,COLUMN_SIZE:2000000000,BUFFER_LENGTH
 * :0,DECIMAL_DIGITS:16,PSEUDO_COLUMN:1 from meta " + t+" where name in key
 * "+t);}
 */

