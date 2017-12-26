package br.com.delxmobile.fidelidade.db.repositories;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.util.ArrayList;

import br.com.delxmobile.fidelidade.db.mappers.Mapper;
import br.com.delxmobile.fidelidade.db.mappers.program.CursorToProgram;
import br.com.delxmobile.fidelidade.db.mappers.program.ProgramToContentValue;
import br.com.delxmobile.fidelidade.db.sql.FindObjectById;
import br.com.delxmobile.fidelidade.db.sql.FindObjectByOid;
import br.com.delxmobile.fidelidade.db.sql.SqlSpecification;
import br.com.delxmobile.fidelidade.db.tables.ProgramTable;
import br.com.delxmobile.fidelidade.db.tables.UserTable;
import br.com.delxmobile.fidelidade.model.Program;

/**
 * Created by Guilherme on 21/12/2017.
 */

public class ProgramRepository implements Repository<Program> {

    private final SQLiteOpenHelper openHelper;
    private final Mapper<Program, ContentValues> converterContentValues;
    private final Mapper<Cursor, Program> converterCursor;

    public ProgramRepository(SQLiteOpenHelper openHelper){
        this.openHelper = openHelper;
        converterCursor = new CursorToProgram();
        converterContentValues = new ProgramToContentValue();
    }

    @Override
    public Program add(Program item) {
        final SQLiteDatabase database = openHelper.getWritableDatabase();
        long id;
        database.beginTransaction();
        try {
            final ContentValues contentValues = converterContentValues.map(item);
            id = database.insert(ProgramTable.TABLE_NAME, null, contentValues);
            item.id = id;
            database.setTransactionSuccessful();
            return item;
        } finally {
            database.endTransaction();
            database.close();
        }
    }

    @Override
    public void add(Iterable<Program> items) {

    }

    @Override
    public void update(Program item) {
        final SQLiteDatabase database = openHelper.getWritableDatabase();
        database.beginTransaction();
        try {
            String whereClause = ProgramTable.Fields.ID + "=?";
            String[] whereArgs = new String[] { String.valueOf(item.id) };
            final ContentValues contentValues = converterContentValues.map(item);
            database.update(ProgramTable.TABLE_NAME, contentValues, whereClause, whereArgs);
            database.setTransactionSuccessful();
        } finally {
            database.endTransaction();
            database.close();
        }
    }

    @Override
    public void remove(Program item) {
        final SQLiteDatabase database = openHelper.getWritableDatabase();
        database.beginTransaction();
        try {
            String where = ProgramTable.Fields.ID + "=?";
            String[] whereArgs = new String[] { String.valueOf(item.id)};
            database.delete(ProgramTable.TABLE_NAME, where, whereArgs);
            database.setTransactionSuccessful();
        } finally {
            database.endTransaction();
            database.close();
        }
    }

    @Override
    public void remove(SqlSpecification specification) {

    }

    @Override
    public Program findById(long id) {
        final SQLiteDatabase database = openHelper.getReadableDatabase();
        Program program = null;

        try {
            final Cursor cursor = database.rawQuery(new FindObjectById(ProgramTable.TABLE_NAME, id).toSqlQuery(), new String[]{});
            if(cursor.moveToFirst())
                program =converterCursor.map(cursor);

            cursor.close();
            return program;
        } finally {
            database.close();
        }
    }

    @Override
    public Program findByOid(String oid) {
        final SQLiteDatabase database = openHelper.getReadableDatabase();
        Program program = null;

        try {
            final Cursor cursor = database.rawQuery(new FindObjectByOid(ProgramTable.TABLE_NAME, oid).toSqlQuery(), new String[]{});
            if(cursor.moveToFirst())
                program =converterCursor.map(cursor);

            cursor.close();
            return program;
        } finally {
            database.close();
        }
    }

    @Override
    public ArrayList<Program> query(SqlSpecification specification) {
        final SQLiteDatabase database = openHelper.getReadableDatabase();
        final ArrayList<Program> items = new ArrayList<>();
        try {
            final Cursor cursor = database.rawQuery(specification.toSqlQuery(), new String[]{});
            for (int i = 0, size = cursor.getCount(); i < size; i++) {
                cursor.moveToPosition(i);
                items.add(converterCursor.map(cursor));
            }
            cursor.close();
            return items;
        } finally {
            database.close();
        }
    }
}
