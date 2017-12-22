package br.com.delxmobile.fidelidade.db.mappers.program;

import android.database.Cursor;

import br.com.delxmobile.fidelidade.db.mappers.Mapper;
import br.com.delxmobile.fidelidade.db.tables.ProgramTable;
import br.com.delxmobile.fidelidade.db.tables.UserTable;
import br.com.delxmobile.fidelidade.model.Program;

/**
 * Created by Guilherme on 20/12/2017.
 */
public class CursorToProgram implements Mapper<Cursor, Program> {
    @Override
    public Program map(Cursor cursor) {
        Program program = new Program();
        program.id = cursor.getLong(cursor.getColumnIndex(ProgramTable.Fields.ID));
        program.name = cursor.getString(cursor.getColumnIndex(ProgramTable.Fields.NAME));
        program.description = cursor.getString(cursor.getColumnIndex(ProgramTable.Fields.DESCRIPTION));
        program.points = cursor.getLong(cursor.getColumnIndex(ProgramTable.Fields.POINTS));
        program.oId = cursor.getString(cursor.getColumnIndex(ProgramTable.Fields.OID));
        program.active = cursor.getInt(cursor.getColumnIndex(ProgramTable.Fields.ACTIVE)) == 1 ? true : false;
        program.updatedAt = cursor.getLong(cursor.getColumnIndex(ProgramTable.Fields.UPDATED_AT));
        program.userId = cursor.getLong(cursor.getColumnIndex(ProgramTable.Fields.USER_ID));
        program.userOid = cursor.getString(cursor.getColumnIndex(ProgramTable.Fields.USER_OID));
        return program;
    }
}
