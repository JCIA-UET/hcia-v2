package uet.jcia.utils;

import java.util.HashMap;

public class SqlTypeMapper {

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
        hbmToSql.put("BigDecimal", "DOUBLE");
        hbmToSql.put("java.math.BigDecimal", "NUMERIC");
        hbmToSql.put("java.lang.String", "VARCHAR");
        hbmToSql.put("string", "VARCHAR");
        hbmToSql.put("String", "VARCHAR");
        hbmToSql.put("java.lang.Byte", "TINYINT");
        hbmToSql.put("byte", "TINYINT");
        hbmToSql.put("java.lang.Boolean", "BIT");
        hbmToSql.put("boolean", "BIT");
        hbmToSql.put("java.sql.Date", "DATE");
        hbmToSql.put("date", "DATE");
        hbmToSql.put("Date", "DATE");
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
        sqlToHbm.put("CHAR", "java.lang.String");
        sqlToHbm.put("VARCHAR", "java.lang.String");
        sqlToHbm.put("LONGVARCHAR", "java.lang.String");
        sqlToHbm.put("NUMERIC", "java.math.BigDecimal");
        sqlToHbm.put("DECIMAL", "java.math.BigDecimal");
        sqlToHbm.put("BIT", "java.lang.Boolean");
        sqlToHbm.put("TINYINT", "java.lang.Byte");
        sqlToHbm.put("SMALLINT", "java.lang.Short");
        sqlToHbm.put("INTEGER", "java.lang.Integer");
        sqlToHbm.put("BIGINT", "java.lang.Long");
        sqlToHbm.put("REAL", "java.lang.Float");
        sqlToHbm.put("FLOAT", "java.lang.Double");
        sqlToHbm.put("DOUBLE", "java.lang.Double");
        sqlToHbm.put("BINARY", "byte[]");
        sqlToHbm.put("VARBINARY", "byte[]");
        sqlToHbm.put("LONGVARBINARY", "byte[]");
        sqlToHbm.put("DATE", "java.sql.Date");
        sqlToHbm.put("TIME", "java.sql.Time");
        sqlToHbm.put("TIMESTAMP", "java.sql.Timestamp");
        
    }
    
    public static String getHbmtoSql(String hbmType) {
        return hbmToSql.get(hbmType);
    }
    
    public static String getSqltoHbm(String sqlType) {
        return sqlToHbm.get(sqlType);
    }
}
