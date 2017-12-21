package br.com.delxmobile.fidelidade.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

import br.com.delxmobile.fidelidade.R;
import br.com.delxmobile.fidelidade.model.Program;

/**
 * Created by Guilherme on 21/12/2017.
 */

public class ProgramAdapter extends BaseAdapter {

    private List<Program> mList;
    private Context mContext;

    public ProgramAdapter(Context context, List<Program> list){
        mList = list;
        mContext = context;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View v = inflater.inflate(R.layout.item_program);


        return null;
    }


}
