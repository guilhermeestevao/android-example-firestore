package br.com.delxmobile.fidelidade.view;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.List;

import br.com.delxmobile.fidelidade.R;
import br.com.delxmobile.fidelidade.adapter.ProgramAdapter;
import br.com.delxmobile.fidelidade.model.Program;
import br.com.delxmobile.fidelidade.sync.ProgramSync;

public class MainActivity extends AppCompatActivity implements PrograDialog.OnRefreshListener {

    private ListView mListView;
    private ProgramAdapter mAdapter;
    private ProgramSync mSync;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        mListView = findViewById(R.id.programs);
        setSupportActionBar(toolbar);
        mSync = ProgramSync.getInstance(this);
        loadData();
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PrograDialog dialog = PrograDialog.newInstance(new Program());
                dialog.show(getSupportFragmentManager(), "dialog");
            }
        });
    }

    private void loadData() {
        List<Program> programs =  mSync.getPrograms();
        mAdapter = new ProgramAdapter(this, programs);
        mListView.setAdapter(mAdapter);
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void update() {
        loadData();
        mAdapter.notifyDataSetChanged();
    }
}
