package br.com.delxmobile.fidelidade.db.sql;

/**
 * Created by Guilherme on 21/12/2017.
 */

public class FindObjectById implements SqlSpecification {

    private long mId;
    private String mTable;

    public FindObjectById(String table, long id){
        mId = id;
        mTable = table;
    }

    @Override
    public String toSqlQuery() {
        return String.format(
                "SELECT * FROM %1$s WHERE id = %2$s;",
                mTable, String.valueOf(mId)
        );
    }
}
