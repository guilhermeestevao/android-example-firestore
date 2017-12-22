package br.com.delxmobile.fidelidade.db.mappers.user;

import android.content.ContentValues;
import br.com.delxmobile.fidelidade.db.mappers.Mapper;
import br.com.delxmobile.fidelidade.db.tables.UserTable;
import br.com.delxmobile.fidelidade.model.User;

/**
 * Created by Guilherme on 20/12/2017.
 */
public class UserToContentValues implements Mapper<User, ContentValues> {
    @Override
    public ContentValues map(User user) {
        ContentValues values = new ContentValues();
        values.put(UserTable.Fields.NAME, user.name);
        values.put(UserTable.Fields.EMAIL, user.email);
        values.put(UserTable.Fields.PHOTO, user.photo);
        values.put(UserTable.Fields.ACTIVE, user.active ? 1 : 0);
        values.put(UserTable.Fields.UPDATED_AT, user.updatedAt);
        values.put(UserTable.Fields.OID, user.oId);
        return values;
    }
}
