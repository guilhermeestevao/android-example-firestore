package br.com.delxmobile.fidelidade.view;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.Date;
import java.util.List;

import br.com.delxmobile.fidelidade.R;
import br.com.delxmobile.fidelidade.adapter.ProgramAdapter;
import br.com.delxmobile.fidelidade.model.Program;
import br.com.delxmobile.fidelidade.model.User;
import br.com.delxmobile.fidelidade.service.ServiceListener;
import br.com.delxmobile.fidelidade.sync.ProgramSync;
import br.com.delxmobile.fidelidade.sync.UserSync;
import br.com.delxmobile.fidelidade.util.UserPreference;
import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements PrograDialog.OnRefreshListener, GoogleApiClient.OnConnectionFailedListener {

    private List<Program> mPrograms;
    private ListView mListView;
    private ProgramAdapter mAdapter;
    private ProgramSync mSync;
    private CircleImageView mImage;
    private ImageView mLogout;
    private TextView mName;
    private TextView mEmail;
    private ProgressDialog dialog;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private GoogleApiClient mGoogleApiClient;
    private static final int RC_SIGN_IN = 9001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        configSignInGoogle();
        Toolbar toolbar = findViewById(R.id.toolbar);
        dialog = new ProgressDialog(this);
        mListView = findViewById(R.id.programs);
        mName = findViewById(R.id.name);
        mEmail = findViewById(R.id.email);
        mImage = findViewById(R.id.image);
        mLogout = findViewById(R.id.logout);
        setSupportActionBar(toolbar);
        mSync = ProgramSync.getInstance(this);
        loadData();
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                opemProgramDialog(new Program());
            }
        });

        mImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                singIn();
            }
        });

        mLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                long idProgram = mPrograms.get(position).id;
                Program program = mSync.getProgramById(idProgram);
                opemProgramDialog(program);
            }
        });
    }

    private void opemProgramDialog(Program program){
        PrograDialog dialog = PrograDialog.newInstance(program);
        dialog.setCancelable(false);
        dialog.show(getSupportFragmentManager(), "dialog");
    }

    private void configSignInGoogle() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                updateUI(user);
            }
        };
    }

    private void singIn(){
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void updateUI( FirebaseUser user){
        if(user != null) {
            mName.setText(user.getDisplayName());
            mEmail.setText(user.getEmail());
            Glide.with(getApplicationContext()).load(user.getPhotoUrl()).into(mImage);
            mLogout.setVisibility(View.VISIBLE);
            mImage.setOnClickListener(null);
        }else{
            configWihoutUser();
        }
    }

    private void loadData() {
        mPrograms =  mSync.getPrograms();
        mAdapter = new ProgramAdapter(this, mPrograms);
        mListView.setAdapter(mAdapter);
    }

    private void configWihoutUser() {
        mName.setText("Nome");
        mEmail.setText("Email");
        mImage.setImageResource(R.drawable.account_circle);
        mLogout.setVisibility(View.GONE);
        mImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                singIn();
            }
        });
        mLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            } else {
                configWihoutUser();
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete( Task<AuthResult> task) {

                        if (!task.isSuccessful()) {
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            dialog.setCancelable(false);
                            dialog.setTitle("Aguarde");
                            dialog.setMessage("Sincronizando dados");
                            dialog.show();

                            final FirebaseUser user = task.getResult().getUser();

                            User tabataUser = new User();
                            tabataUser.oId = user.getUid();
                            tabataUser.email = user.getEmail();
                            tabataUser.name = user.getDisplayName();
                            tabataUser.photo = user.getPhotoUrl().toString();
                            tabataUser.updatedAt = new Date().getTime();
                            UserPreference prefs = UserPreference.getInstance();
                            prefs.setLoggedUser(MainActivity.this, tabataUser);
                            prefs.setUserLogged(MainActivity.this, true);

                            UserSync.getInstance(MainActivity.this).save(tabataUser);

                            updateUI(user);
                            dialog.dismiss();
                        }
                    }
                });
    }


    private void signOut() {
        mAuth.signOut();

        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult( Status status) {
                        UserPreference prefs = UserPreference.getInstance();
                        prefs.setUserLogged(MainActivity.this, false);
                        configWihoutUser();
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_settings) {

            mSync.sync(new ServiceListener<Void>() {
                @Override
                public void onComplete(Void object) {
                    Toast.makeText(MainActivity.this, "Sincronização conluida", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onError(String cause) {

                }
            });
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void update() {
        loadData();
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
