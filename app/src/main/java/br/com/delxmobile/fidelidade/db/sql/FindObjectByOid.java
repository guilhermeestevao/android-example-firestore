package br.com.delxmobile.fidelidade.db.sql;

/**
 * Created by Guilherme on 21/12/2017.
 */
public class FindObjectByOid implements SqlSpecification {

    private String mTable;
    private String mOid;

    public FindObjectByOid(String table, String oid){
        mOid = oid;
        mTable = table;
    }

    @Override
    public String toSqlQuery() {
        return String.format(
                "SELECT * FROM %1$s WHERE oid = \"%2$s\";",
                mTable, mOid
        );
    }
}
