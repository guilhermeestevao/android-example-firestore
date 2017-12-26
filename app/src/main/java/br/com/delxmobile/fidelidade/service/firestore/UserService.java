package br.com.delxmobile.fidelidade.service.firestore;

import android.content.Context;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

import br.com.delxmobile.fidelidade.db.tables.ProgramTable;
import br.com.delxmobile.fidelidade.db.tables.UserTable;
import br.com.delxmobile.fidelidade.service.ServiceListener;

/**
 * Created by Guilherme on 26/12/2017.
 */
public class UserService {

    private static final String COLLECTION_USERS = UserTable.TABLE_NAME;
    private static UserService instance;
    private final FirebaseFirestore db;

    private UserService(Context context){
        db = FirebaseFirestore.getInstance();
    }

    public static UserService getInstance(Context context){
        if(instance == null)
            instance = new UserService(context);

        return instance;
    }

    public void save(Map<String, Object> mapUser, final ServiceListener<Void> listener){
        String uid = mapUser.remove("oid").toString();

        db.collection(COLLECTION_USERS)
                .document(uid)
                .set(mapUser)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        listener.onComplete(aVoid);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        listener.onError(e.getLocalizedMessage());
                    }
                });
    }


}
