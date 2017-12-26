package br.com.delxmobile.fidelidade.sync;

import android.app.Service;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.util.Date;
import java.util.List;
import java.util.Map;

import br.com.delxmobile.fidelidade.db.DatabaseOpenHelper;
import br.com.delxmobile.fidelidade.db.mappers.Mapper;
import br.com.delxmobile.fidelidade.db.repositories.ProgramRepository;
import br.com.delxmobile.fidelidade.db.sql.FindAllObjects;
import br.com.delxmobile.fidelidade.db.tables.ProgramTable;
import br.com.delxmobile.fidelidade.model.Program;
import br.com.delxmobile.fidelidade.model.User;
import br.com.delxmobile.fidelidade.service.ServiceListener;
import br.com.delxmobile.fidelidade.service.firestore.ProgramService;
import br.com.delxmobile.fidelidade.service.firestore.UserService;
import br.com.delxmobile.fidelidade.service.mappers.program.ProgramToMap;
import br.com.delxmobile.fidelidade.util.ConnectionUtil;
import br.com.delxmobile.fidelidade.util.UserPreference;

/**
 * Created by Guilherme on 21/12/2017.
 */

public class ProgramSync {

    private static ProgramSync instance;
    private Context context;
    private ProgramRepository repository;
    private Mapper<Program, Map<String, Object>> programToMap;
    private ProgramService service;

    private ProgramSync(Context context){
        this.context = context;
        repository = new ProgramRepository(DatabaseOpenHelper.getInstance(context));
        programToMap = new ProgramToMap();
        service = ProgramService.getInstance(context);
    }

    public static ProgramSync getInstance(Context context){
        if(instance == null)
            instance = new ProgramSync(context);

        return instance;
    }

    public void save(Program program, ServiceListener<Void> listener) {

        //Salva localmente
        if (program.id == 0)
            program = saveLocal(program);

        //Salva remotamente
        boolean isConnected = ConnectionUtil.isConnected(context);
        if (isConnected && program.oId == null){
            saveRemote(program, listener);
        }else {
            if (listener != null)
                listener.onComplete(null);
        }
    }


    public void update(Program program){
        upateLocal(program);

        //Tenta salvar remoto
        Map<String, Object> map = programToMap.map(program);
        String oid = program.oId;
        if(oid != null) {

            service.update(oid, map, new ServiceListener<Void>() {
                @Override
                public void onComplete(Void object) {

                }

                @Override
                public void onError(String cause) {

                }
            });
        }

    }

    public Program getProgramById(long id){
        return repository.findById(id);
    }

    public void delete(final Program program){
        deleteLocal(program, true);

        String oid = program.oId;


        if(oid != null) {

            service.delete(oid, new ServiceListener<Void>() {
                @Override
                public void onComplete(Void object) {
                    deleteLocal(program, false);
                }

                @Override
                public void onError(String cause) {

                }
            });
        }

    }


    public List<Program> getPrograms(){
        return repository.query(new FindAllObjects(ProgramTable.TABLE_NAME, true));
    }

    private Program saveLocal(Program program){
        User user = UserSync.getInstance(context).getLoggedUser();
        program.updatedAt = new Date().getTime();
        program.userId = user.id;
        program.userOid = user.oId;
        program.active = true;
        return repository.add(program);
    }

    private void saveRemote(final Program add, final ServiceListener<Void> listener){
        Map<String, Object> map = programToMap.map(add);
        service.save(map, new ServiceListener<String>() {
            @Override
            public void onComplete(String object) {
                add.oId = object;
                repository.update(add);
                if(listener != null)
                    listener.onComplete(null);
            }

            @Override
            public void onError(String cause) {
                if(listener != null)
                    listener.onError(cause);
            }
        });
    }

    private void upateLocal(Program program){
        program.updatedAt = new Date().getTime();
        repository.update(program);

    }

    private void deleteLocal(Program program, boolean disable){
        if(disable) {
            program.active = false;
            program.updatedAt = new Date().getTime();
            repository.update(program);
        }else{
            repository.remove(program);
        }
    }

    private void saveAll(List<Program> allPrograms, final ServiceListener<Void> listener){
        int initialPosition = allPrograms.size()-1;
        saveAll(allPrograms, initialPosition, listener);
    }

    private void saveAll(final List<Program> allPrograms, final int position, final ServiceListener<Void> listener){
        if(position < 0){
            listener.onComplete(null);
        }else{
            Program program = allPrograms.get(position);

            save(program, new ServiceListener<Void>() {
                @Override
                public void onComplete(Void object) {
                    next();
                }

                @Override
                public void onError(String cause) {
                    next();
                }

                private void next(){
                    int next = position - 1;
                    saveAll(allPrograms, next, listener);
                }
            });
        }
    }

    private void syncUp(final ServiceListener<Void> listener){
        List<Program> programs = repository.query(new FindAllObjects(ProgramTable.TABLE_NAME, false));
        saveAll(programs, new ServiceListener<Void>() {
            @Override
            public void onComplete(Void object) {
                listener.onComplete(null);
            }

            @Override
            public void onError(String cause) {
                listener.onError(cause);
            }
        });
    }

    public void sync(final ServiceListener<Void> listener){

        syncUp(listener);

    }


}
