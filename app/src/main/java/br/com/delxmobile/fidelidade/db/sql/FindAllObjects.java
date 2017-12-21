package br.com.delxmobile.fidelidade.db.sql;

/**
 * Created by Guilherme on 21/12/2017.
 */
public class FindAllObjects implements SqlSpecification {

    private String mTable;
    private boolean mActive;

    public FindAllObjects(String table, boolean actives){
        mTable = table;
        mActive = actives;
    }

    @Override
    public String toSqlQuery() {

        if(mActive){
            return String.format(
                    "SELECT * FROM %1$s WHERE active = 1 ORDER BY id; ",
                    mTable);
        }else{
            return String.format(
                    "SELECT * FROM %1$s ORDER BY id;",
                    mTable);
        }

    }
}
