package br.com.delxmobile.fidelidade.service.firestore;

import android.content.Context;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;
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

    public void getProgram(String oid, final ServiceListener<DocumentSnapshot> listener){
        DocumentReference docRef = db.collection(COLLECTION_PROGRAM).document(oid);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    listener.onComplete(document);
                } else {
                    listener.onError(task.getException().getLocalizedMessage());
                }
            }
        });
    }

    public void getPrograms(String userOid, final ServiceListener<List<DocumentSnapshot>> listener){
        db.collection(COLLECTION_PROGRAM)
                .whereEqualTo("user_oid", userOid)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            QuerySnapshot result = task.getResult();
                            listener.onComplete(result.getDocuments());
                        } else {
                            listener.onError(task.getException().getLocalizedMessage());
                        }
                    }
                });
    }
}
