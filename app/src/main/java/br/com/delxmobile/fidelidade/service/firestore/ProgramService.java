package br.com.delxmobile.fidelidade.service.firestore;

import android.content.Context;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.Map;
import br.com.delxmobile.fidelidade.db.tables.ProgramTable;
import br.com.delxmobile.fidelidade.service.ServiceListener;


/**
 * Created by Guilherme on 26/12/2017.
 */
public class ProgramService {

    private static final String COLLECTION_PROGRAM = ProgramTable.TABLE_NAME;
    private static ProgramService instance;
    private final FirebaseFirestore db;


    private ProgramService(Context context){
        db = FirebaseFirestore.getInstance();
    }

    public static ProgramService getInstance(Context context){
        if(instance == null)
            instance = new ProgramService(context);

        return instance;
    }


    public void save(Map<String, Object> map, final ServiceListener<String> listener){
        db.collection(COLLECTION_PROGRAM)
                .add(map)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        listener.onComplete(documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        listener.onError(e.getLocalizedMessage());
                    }
                });
    }

    public void update(String oid, final Map<String, Object> map, final ServiceListener<Void> listener){
        db.collection(COLLECTION_PROGRAM)
                .document(oid)
                .set(map)
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

    public void delete(String oid, final ServiceListener<Void> listener){
        db.collection(COLLECTION_PROGRAM)
                .document(oid)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        listener.onComplete(null);
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
