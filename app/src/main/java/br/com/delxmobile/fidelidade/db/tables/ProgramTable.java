package br.com.delxmobile.fidelidade.db.tables;

/**
 * Created by Guilherme on 20/12/2017.
 */

public class ProgramTable {

    public static final String TABLE_NAME = "programs";

    public static final class Fields{
        public static final String ID = "id";
        public static final String NAME = "name";
        public static final String DESCRIPTION = "description";
        public static final String POINTS = "points";
        public static final String OID = "oid";
        public static final String UPDATED_AT = "updated_at";
        public static final String USER_ID = "user_id";
        public static final String USER_OID = "user_oid";
    }
}
