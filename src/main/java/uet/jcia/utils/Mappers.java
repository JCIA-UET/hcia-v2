package uet.jcia.utils;

import java.util.HashMap;

public class Mappers {

    private static final HashMap<String, String> hbmToSql = new HashMap<>();
    private static final HashMap<String, String> sqlToHbm = new HashMap<>();
    
    static {
        
        // Hbm to MySQL
        hbmToSql.put("java.lang.Integer", "INTEGER");
        hbmToSql.put("int", "INTEGER");
        hbmToSql.put("java.lang.Long", "BIGINT");
        hbmToSql.put("long", "BIGINT");
        hbmToSql.put("java.lang.Short", "SMALLINT");
        hbmToSql.put("short", "SMALLINT");
        hbmToSql.put("java.lang.Float", "FLOAT");
        hbmToSql.put("float", "FLOAT");
        hbmToSql.put("java.lang.Double", "DOUBLE");
        hbmToSql.put("double", "DOUBLE");
        hbmToSql.put("java.math.BigDecimal", "NUMERIC");
        hbmToSql.put("java.lang.String", "VARCHAR");
        hbmToSql.put("string", "VARCHAR");
        hbmToSql.put("java.lang.Byte", "TINYINT");
        hbmToSql.put("byte", "TINYINT");
        hbmToSql.put("java.lang.Boolean", "BIT");
        hbmToSql.put("boolean", "BIT");
        hbmToSql.put("java.sql.Date", "DATE");
        hbmToSql.put("date", "DATE");
        hbmToSql.put("java.util.Date", "DATE");
        hbmToSql.put("java.sql.Time", "TIME");
        hbmToSql.put("java.sql.Timestamp", "TIMESTAMP");
        hbmToSql.put("java.util.Calendar", "TIMESTAMP");
        hbmToSql.put("byte[]", "VARBINARY()");
        hbmToSql.put("java.io.Serializable", "BLOB()");
        hbmToSql.put("java.sql.Clob", "CLOB()");
        hbmToSql.put("java.sql.Blob", "BLOB()");
        hbmToSql.put("java.lang.Class", "VARCHAR");
        hbmToSql.put("java.util.Locale", "VARCHAR");
        hbmToSql.put("java.util.TimeZone", "VARCHAR");
        hbmToSql.put("java.util.Currency", "VARCHAR");
        
        // MySQL to Hbm
        sqlToHbm.put("CHAR", "String");
        sqlToHbm.put("VARCHAR", "String");
        sqlToHbm.put("LONGVARCHAR", "String");
        sqlToHbm.put("NUMERIC", "java.math.BigDecimal");
        sqlToHbm.put("DECIMAL", "java.math.BigDecimal");
        sqlToHbm.put("BIT", "Boolean");
        sqlToHbm.put("TINYINT", "Byte");
        sqlToHbm.put("SMALLINT", "Short");
        sqlToHbm.put("INTEGER", "Integer");
        sqlToHbm.put("BIGINT", "Long");
        sqlToHbm.put("REAL", "Float");
        sqlToHbm.put("FLOAT", "Double");
        sqlToHbm.put("DOUBLE", "Double");
        sqlToHbm.put("BINARY", "byte[]");
        sqlToHbm.put("VARBINARY", "byte[]");
        sqlToHbm.put("LONGVARBINARY", "byte[]");
        sqlToHbm.put("DATE", "java.sql.Date");
        sqlToHbm.put("TIME", "java.sql.Time");
        sqlToHbm.put("TIMESTAMP", "java.sql.Timestamp");
        
    }
    
    public static String getHbmtosql(String hbmType) {
        return hbmToSql.get(hbmType);
    }
    
    public static String getSqltohbm(String sqlType) {
        return sqlToHbm.get(sqlType);
    }
}
