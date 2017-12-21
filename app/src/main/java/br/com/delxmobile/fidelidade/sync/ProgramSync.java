package br.com.delxmobile.fidelidade.sync;

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.List;

import br.com.delxmobile.fidelidade.db.DatabaseOpenHelper;
import br.com.delxmobile.fidelidade.db.repositories.ProgramRepository;
import br.com.delxmobile.fidelidade.db.sql.FindAllObjects;
import br.com.delxmobile.fidelidade.db.tables.ProgramTable;
import br.com.delxmobile.fidelidade.model.Program;

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

    public ProgramSync getInstance(Context context){
        if(instance == null)
            instance = new ProgramSync(context);

        return instance;
    }

    public void save(Program program){
        if(program.id == 0){
            repository.add(program);
        }
    }

    public void update(Program program){
        repository.update(program);
    }

    public void delete(Program program){
        repository.remove(program);
    }

    public List<Program> getPrograms(){
        return repository.query(new FindAllObjects(ProgramTable.TABLE_NAME, true));
    }


}
