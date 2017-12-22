package br.com.delxmobile.fidelidade.db.mappers.user;

import android.database.Cursor;
import br.com.delxmobile.fidelidade.db.mappers.Mapper;
import br.com.delxmobile.fidelidade.db.tables.ProgramTable;
import br.com.delxmobile.fidelidade.db.tables.UserTable;
import br.com.delxmobile.fidelidade.model.User;

/**
 * Created by Guilherme on 20/12/2017.
 */
public class CursorToUser implements Mapper<Cursor, User> {
    @Override
    public User map(Cursor cursor) {
        User user = new User();
        user.id = cursor.getLong(cursor.getColumnIndex(UserTable.Fields.ID));
        user.name = cursor.getString(cursor.getColumnIndex(UserTable.Fields.NAME));
        user.email = cursor.getString(cursor.getColumnIndex(UserTable.Fields.EMAIL));
        user.photo = cursor.getString(cursor.getColumnIndex(UserTable.Fields.PHOTO));
        user.active = cursor.getInt(cursor.getColumnIndex(UserTable.Fields.ACTIVE)) == 1 ? true : false;
        user.updatedAt = cursor.getLong(cursor.getColumnIndex(UserTable.Fields.UPDATED_AT));
        user.oId = cursor.getString(cursor.getColumnIndex(UserTable.Fields.OID));
        return user;
    }
}
