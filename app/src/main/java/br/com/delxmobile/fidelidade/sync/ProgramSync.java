package br.com.delxmobile.fidelidade.sync;

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.Date;
import java.util.List;

import br.com.delxmobile.fidelidade.db.DatabaseOpenHelper;
import br.com.delxmobile.fidelidade.db.repositories.ProgramRepository;
import br.com.delxmobile.fidelidade.db.sql.FindAllObjects;
import br.com.delxmobile.fidelidade.db.tables.ProgramTable;
import br.com.delxmobile.fidelidade.model.Program;
import br.com.delxmobile.fidelidade.model.User;

/**
 * Created by Guilherme on 21/12/2017.
 */

public class ProgramSync {

    private static ProgramSync instance;
    private Context context;
    private ProgramRepository repository;

    private ProgramSync(Context context){
        this.context = context;
        repository = new ProgramRepository(DatabaseOpenHelper.getInstance(context));
    }

    public static ProgramSync getInstance(Context context){
        if(instance == null)
            instance = new ProgramSync(context);

        return instance;
    }

    public void save(Program program){
        if(program.id == 0){
            User user = UserSync.getInstance(context).getLoggedUser();
            program.updatedAt = new Date().getTime();
            program.userId = user.id;
            program.userOid = user.oId;
            program.active = true;
            Program add = repository.add(program);
            Log.d("", "");
        }
    }

    public void update(Program program){
        program.updatedAt = new Date().getTime();
        repository.update(program);
    }

    public void delete(Program program){
        program.active = false;
        program.updatedAt = new Date().getTime();
        repository.update(program);
    }

    public List<Program> getPrograms(){
        return repository.query(new FindAllObjects(ProgramTable.TABLE_NAME, true));
    }


}
