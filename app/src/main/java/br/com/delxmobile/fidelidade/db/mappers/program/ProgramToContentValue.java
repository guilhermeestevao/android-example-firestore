package br.com.delxmobile.fidelidade.db.mappers.program;

import android.content.ContentValues;
import br.com.delxmobile.fidelidade.db.mappers.Mapper;
import br.com.delxmobile.fidelidade.db.tables.ProgramTable;
import br.com.delxmobile.fidelidade.model.Program;

/**
 * Created by Guilherme on 20/12/2017.
 */
public class ProgramToContentValue implements Mapper<Program, ContentValues> {
    @Override
    public ContentValues map(Program program) {
        ContentValues values= new ContentValues();
        values.put(ProgramTable.Fields.NAME, program.name);
        values.put(ProgramTable.Fields.DESCRIPTION, program.description);
        values.put(ProgramTable.Fields.POINTS, program.points);
        values.put(ProgramTable.Fields.ACTIVE, program.active ? 1 : 0);
        values.put(ProgramTable.Fields.OID, program.oId);
        values.put(ProgramTable.Fields.UPDATED_AT, program.updatedAt);
        values.put(ProgramTable.Fields.USER_ID, program.userId);
        values.put(ProgramTable.Fields.USER_OID, program.userOid);
        return values;
    }
}
