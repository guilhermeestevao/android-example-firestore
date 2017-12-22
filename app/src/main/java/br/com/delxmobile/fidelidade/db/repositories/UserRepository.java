package br.com.delxmobile.fidelidade.db.repositories;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import br.com.delxmobile.fidelidade.db.mappers.Mapper;
import br.com.delxmobile.fidelidade.db.mappers.user.CursorToUser;
import br.com.delxmobile.fidelidade.db.mappers.user.UserToContentValues;
import br.com.delxmobile.fidelidade.db.sql.FindObjectById;
import br.com.delxmobile.fidelidade.db.sql.FindObjectByOid;
import br.com.delxmobile.fidelidade.db.sql.SqlSpecification;
import br.com.delxmobile.fidelidade.db.tables.ProgramTable;
import br.com.delxmobile.fidelidade.db.tables.UserTable;
import br.com.delxmobile.fidelidade.model.Program;
import br.com.delxmobile.fidelidade.model.User;

/**
 * Created by Guilherme on 20/12/2017.
 */
public class UserRepository implements Repository<User> {

    private final Mapper<Cursor, User> converterCursor;
    private final Mapper<User, ContentValues> converterContentValues;
    private final SQLiteOpenHelper openHelper;

    public UserRepository(SQLiteOpenHelper openHelper){
        this.openHelper = openHelper;
        converterCursor = new CursorToUser();
        converterContentValues = new UserToContentValues();
    }

    @Override
    public User add(User item) {
        final SQLiteDatabase database = openHelper.getWritableDatabase();
        long id;
        database.beginTransaction();
        try {
            final ContentValues contentValues = converterContentValues.map(item);
            id = database.insert(UserTable.TABLE_NAME, null, contentValues);
            item.id = id;
            database.setTransactionSuccessful();
            return item;
        } finally {
            database.endTransaction();
            database.close();
        }
    }

    @Override
    public void add(Iterable<User> items) {

    }

    @Override
    public void update(User item) {

    }

    @Override
    public void remove(User item) {

    }

    @Override
    public void remove(SqlSpecification specification) {

    }

    @Override
    public User findById(long id) {
        final SQLiteDatabase database = openHelper.getReadableDatabase();
        User user = null;

        try {
            final Cursor cursor = database.rawQuery(new FindObjectById(UserTable.TABLE_NAME, id).toSqlQuery(), new String[]{});
            if(cursor.moveToFirst())
                user = converterCursor.map(cursor);

            cursor.close();
            return user;
        } finally {
            database.close();
        }
    }

    @Override
    public User findByOid(String oid) {
        final SQLiteDatabase database = openHelper.getReadableDatabase();
        User user = null;

        try {
            final Cursor cursor = database.rawQuery(new FindObjectByOid(UserTable.TABLE_NAME, oid).toSqlQuery(), new String[]{});
            if(cursor.moveToFirst())
                user = converterCursor.map(cursor);

            cursor.close();
            return user;
        } finally {
            database.close();
        }
    }

    @Override
    public ArrayList<User> query(SqlSpecification specification) {
        return null;
    }
}
