package uet.jcia.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.MemberValuePair;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.NormalAnnotation;
import org.eclipse.jdt.core.dom.ParameterizedType;
import org.eclipse.jdt.core.dom.PrimitiveType;
import org.eclipse.jdt.core.dom.SimpleType;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import uet.jcia.entities.ColumnNode;
import uet.jcia.entities.MTORelationshipNode;
import uet.jcia.entities.OTMRelationshipNode;
import uet.jcia.entities.PrimaryKeyNode;
import uet.jcia.entities.TableNode;
import uet.jcia.entities.TreeNode;
import uet.jcia.utils.Mappers;

public class HASTVisitor extends ASTVisitor {
    
    private static final String SQL_TABLE = "Table";
    private static final String SQL_COLUMN = "Column";
    private static final String SQL_OTM = "One-to-Many";
    private static final String SQL_MTO = "Many-to-One";
    
    private static long tempId = 0L;
    
    private TableNode table;
    private List<TreeNode> children;
    
    /**
     * mapping between className and tableNode 
     */
    private Map<String, TableNode> cachedTables;
    
    public HASTVisitor() {
        cachedTables = new HashMap<>();
    }
    
    public long generateTempId() {
    	return tempId++;
    }
       
    public TableNode getTable() {
        return table;
    }
    
    public TableNode getTableNodeByClassName(String className) {
        return cachedTables.get(className);
    }
    
    @Override
    public boolean visit(TypeDeclaration node) {
        List modifiers = node.modifiers();
        String tableName = null;
        String catalog = null;
        String className = null;
        // check if class is a hibernate Entity
        boolean isEntity = false;
        boolean hasTableValues = false;
        List<MemberValuePair> tableValues = null;
        
        for (Object modifier : modifiers) {
            if (modifier instanceof Annotation) {
                Annotation ano = (Annotation) modifier;
                if (ano.getTypeName().toString().equals("Entity")) {
                    isEntity = true;
                } else if (ano.getTypeName().toString().equals("Table")
                        && ano instanceof NormalAnnotation) {
                    hasTableValues = true;
                    tableValues = ((NormalAnnotation) ano).values();
                }
            }
        }
        
        if (!isEntity) {
        	table = null;
            return false;
        }
        
        className = node.getName().getFullyQualifiedName();
        if (isEntity && !hasTableValues) {
            tableName = node.getName().getIdentifier();
        } else if (isEntity && hasTableValues){
            for (MemberValuePair val : tableValues) {
                if (val.getName().toString().equals("name")) {
                    tableName = val.getValue().toString().replace("\"", "");
                } else if (val.getName().toString().equals("catalog")) {
                    catalog = val.getValue().toString().replace("\"", "");
                }
            }
        }
        
        table = new TableNode();
        children = new ArrayList<>();
        table.setChilds(children);
        table.setTableName(tableName);
        table.setClassName(className);
        table.setCatalog(catalog);
        table.setTempId(generateTempId());
        cachedTables.put(className, table);
        return true;
    }

    @Override
    public boolean visit(MethodDeclaration node) {
    	if (table == null) return false;
        List modifiers = node.modifiers();
        // get type of element
        String elementType = getElementType(modifiers);
        Type returnType = node.getReturnType2();
        if (elementType.equals(SQL_COLUMN)) {
            children.add(parseColumn(modifiers, returnType));
        } else if (elementType.equals(SQL_OTM)) {
            children.add(parseOtm(modifiers, returnType));
        } else if (elementType.equals(SQL_MTO)) {
            children.add(parseMto(modifiers, returnType));
        } else {
            return false;
        }
        return true;
    }
    
    private MTORelationshipNode parseMto(List modifiers, Type returnType) {
        MTORelationshipNode mto = new MTORelationshipNode();
        mto.setType(SQL_MTO);
        String referClassName = null;
        List<MemberValuePair> joinColumnValues = null;
        String fkColumnName = null;

        // get foreign key column name
        for (Object modifier : modifiers) {
            if (modifier instanceof Annotation) {
                Annotation anno = (Annotation) modifier;
                if (anno.getTypeName().toString().equals("JoinColumn")
                        && anno instanceof NormalAnnotation) {
                    joinColumnValues = ((NormalAnnotation)anno).values();
                }
            }
        }
        for (MemberValuePair pair : joinColumnValues) {
            if (pair.getName().toString().equals("name")) {
                fkColumnName = pair.getValue().toString().replace("\"", "");
            }
        }
        // refer class here
        if (returnType.isSimpleType()) {
            referClassName = ((SimpleType)returnType).getName().getFullyQualifiedName();
        }
        
        ColumnNode foreignKey = new ColumnNode();
        foreignKey.setForeignKey(true);
        foreignKey.setColumnName(fkColumnName);
        TableNode referTable = new TableNode();
        referTable.setClassName(referClassName);
        mto.setReferTable(referTable);
        mto.setForeignKey(foreignKey);
        mto.setTempId(generateTempId());
        return mto;
    }
    
    private OTMRelationshipNode parseOtm(List modifiers, Type returnType) {
        OTMRelationshipNode otm = new OTMRelationshipNode();
        otm.setType(SQL_OTM);
        String referClassName = null;
        // get type here
        if (returnType.isParameterizedType()) {
            ParameterizedType pReturnType = (ParameterizedType) returnType;
            Object argument = pReturnType.typeArguments().get(0);
            if (argument instanceof SimpleType) {
                referClassName = ((SimpleType)argument).getName().getFullyQualifiedName();
            }
        }
        TableNode referTable = new TableNode();
        referTable.setClassName(referClassName);
        otm.setReferTable(referTable);
        otm.setTempId(generateTempId());
        return otm;
    }
    
    private ColumnNode parseColumn(List modifiers, Type returnType) {
        List<MemberValuePair> columnValues = null;
        String columnName = null;
        String javaType = null;
        int length = 0;
        boolean isPK = false;
        boolean isUQ = false;
        boolean isNN = false;
        // get column name, length, pk, uq, nn here
        for (Object modifier : modifiers) {
            if (modifier instanceof Annotation) {
                Annotation anno = (Annotation) modifier;
                if (anno.getTypeName().toString().equals("Id")) {
                    isPK = true;
                } else if (anno.getTypeName().toString().equals("Column")
                        && anno instanceof NormalAnnotation) {
                    columnValues = ((NormalAnnotation)anno).values();
                }
            }
        }
        for (MemberValuePair pair : columnValues) {
            if (pair.getName().toString().equals("name")) {
                columnName = pair.getValue().toString().replace("\"", "");
            } else if (pair.getName().toString().equals("length")) { 
                length = Integer.parseInt(pair.getValue().toString());
            } else if ((pair.getName().toString().equals("unique")) 
                    && pair.getValue().toString().equals("true")) {
                isUQ = true;
            } else if ((pair.getName().toString().equals("nullable")) 
                    && pair.getValue().toString().equals("false")) {
                isNN = true;
            }
        }
        // get type here
        if (returnType.isPrimitiveType()) {
            javaType = ((PrimitiveType)returnType).getPrimitiveTypeCode().toString();
        } else if (returnType.isSimpleType()) {
            javaType = ((SimpleType)returnType).getName().getFullyQualifiedName();
        }
        ColumnNode column = null;
        if (isPK) {
            column = new PrimaryKeyNode();
        } else {
            column = new ColumnNode();
        }
        column.setColumnName(columnName);
        column.setDataType(Mappers.getHbmtoSql(javaType));
        column.setLength(length);
        column.setPrimaryKey(isPK);
        column.setUnique(isUQ);
        column.setNotNull(isNN);
        column.setTempId(generateTempId());
        return column;
    }
    
    /**
     * Table, Column, MTO, OTM
     * @return
     */
    private String getElementType(List modifiers) {
        for (Object modifier : modifiers) {
            if (modifier instanceof Annotation) {
                Annotation annotation = (Annotation) modifier;
                if (annotation.getTypeName().toString().equals("Entity")) {
                    return SQL_TABLE;
                } else if (annotation.getTypeName().toString().equals("Column")) {
                    return SQL_COLUMN;
                } else if (annotation.getTypeName().toString().equals("OneToMany")) {
                    return SQL_OTM;
                } else if (annotation.getTypeName().toString().equals("ManyToOne")) {
                    return SQL_MTO;
                }
            }
        }
        return "";
    }
    
}
