package br.com.delxmobile.fidelidade.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import br.com.delxmobile.fidelidade.R;
import br.com.delxmobile.fidelidade.model.Program;
import br.com.delxmobile.fidelidade.service.ServiceListener;
import br.com.delxmobile.fidelidade.sync.ProgramSync;

/**
 * Created by Guilherme on 21/12/2017.
 */
public class PrograDialog extends DialogFragment {

    private Program mItem;
    private ProgramSync sync;
    private OnRefreshListener mListener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mListener = (OnRefreshListener) activity;
    }

    public static PrograDialog newInstance(Program item) {
        Bundle args = new Bundle();
        args.putSerializable("program", item);
        PrograDialog fragment = new PrograDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null){
            mItem = (Program) getArguments().getSerializable("program");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater i = getActivity().getLayoutInflater();
        View v = i.inflate(R.layout.fragment_dialog_program,null);
        sync = ProgramSync.getInstance(getActivity());
        final EditText name = v.findViewById(R.id.name);
        final EditText description = v.findViewById(R.id.description);
        final EditText poinsts = v.findViewById(R.id.points);

        if(mItem != null && mItem.id != 0){
            name.setText(mItem.name);
            description.setText(mItem.description);
            poinsts.setText(String.valueOf(mItem.points));
        }

        AlertDialog.Builder dialog =  new  AlertDialog.Builder(getActivity())

                .setPositiveButton("Salvar",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                mItem.name = name.getText().toString();
                                mItem.points = Integer.parseInt(poinsts.getText().toString());
                                mItem.description = description.getText().toString();

                                if(mItem.id ==0) {
                                    sync.save(mItem, null);
                                }else {
                                    sync.update(mItem, null);
                                }

                                mListener.update();
                            }
                        }
                )
                .setNeutralButton("Fechar", null)
                .setNegativeButton(getString(R.string.delete),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                if(mItem.id != 0){
                                    sync.delete(mItem, null);
                                    mListener.update();
                                }
                            }
                        }
                );
        dialog.setView(v);
        return dialog.create();
    }

    public interface OnRefreshListener{
        void update();
    }

}
