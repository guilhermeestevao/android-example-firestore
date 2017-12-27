package br.com.delxmobile.fidelidade.service.mappers.program;

import com.google.firebase.firestore.DocumentSnapshot;

import br.com.delxmobile.fidelidade.db.mappers.Mapper;
import br.com.delxmobile.fidelidade.model.Program;

/**
 * Created by guilherme on 26/12/17.
 */

public class DocumentToProgram implements Mapper<DocumentSnapshot, Program> {
    @Override
    public Program map(DocumentSnapshot document) {
        Program program = new Program();
        program.description = document.getString("description");
        program.name = document.getString("name");
        program.points = document.getLong("points");
        program.oId = document.getId();
        program.userOid = document.getString("user_oid");
        program.updatedAt = document.getLong("updated_at");
        return program;
    }
}
