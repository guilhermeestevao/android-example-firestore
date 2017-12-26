package br.com.delxmobile.fidelidade.sync;

import android.content.Context;
import android.util.Log;

import java.util.Date;
import java.util.Map;

import br.com.delxmobile.fidelidade.db.DatabaseOpenHelper;
import br.com.delxmobile.fidelidade.db.mappers.Mapper;
import br.com.delxmobile.fidelidade.db.repositories.UserRepository;
import br.com.delxmobile.fidelidade.model.User;
import br.com.delxmobile.fidelidade.service.ServiceListener;
import br.com.delxmobile.fidelidade.service.firestore.UserService;
import br.com.delxmobile.fidelidade.service.mappers.user.UserToMap;
import br.com.delxmobile.fidelidade.util.UserPreference;

/**
 * Created by Guilherme on 22/12/2017.
 */

public class UserSync {

    private static UserSync instance;
    private Context context;
    private UserRepository repository;
    private UserService service;
    private Mapper<User, Map<String, Object>> userToMap;

    private UserSync(Context context){
        this.context = context;
        repository = new UserRepository(DatabaseOpenHelper.getInstance(context));
        service = UserService.getInstance(context);
        userToMap = new UserToMap();
    }

    public static UserSync getInstance(Context context){
        if(instance == null)
            instance = new UserSync(context);

        return instance;
    }

    public void save(User user){

        //Caso o usuario recebido como parametro ja exista na base local, não é mais preciso salvar na base remota (provavelmente só precse atualiza-lo)
        User found = repository.findByOid(user.oId);

        if(found == null){
            user.updatedAt = new Date().getTime();
            user.active = true;
            User add = repository.add(user);

            Map<String, Object> map = userToMap.map(add);

            service.save(map, new ServiceListener<Void>() {
                @Override
                public void onComplete(Void object) {

                }

                @Override
                public void onError(String cause) {

                }
            });

        }else{
            //Chama o update aqui
        }
    }

    public User getLoggedUser(){
        boolean isLogged = UserPreference.getInstance().isUserLogged(context);
        if(isLogged){
            User loggedUser = UserPreference.getInstance().getLoggedUser(context);
            loggedUser = repository.findByOid(loggedUser.oId);
            return loggedUser;
        }else{
            return null;
        }
    }

}
