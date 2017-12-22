package br.com.delxmobile.fidelidade.db.tables;

/**
 * Created by Guilherme on 20/12/2017.
 */

public class UserTable {

    public static final String TABLE_NAME = "users";

    public static final class Fields{
        public static final String ID = "id";
        public static final String NAME = "name";
        public static final String EMAIL = "email";
        public static final String PHOTO = "photo";
        public static final String UPDATED_AT = "updated_at";
        public static final String OID = "oid";
        public static final String ACTIVE = "active";
    }
}
