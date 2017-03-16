package com.example.nicolas.ig2i_tp1.tp2.activity.UI;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.nicolas.ig2i_tp1.R;
import com.example.nicolas.ig2i_tp1.tp2.model.Conversation;

import java.util.List;

/**
 * Created by Nicolas on 01/03/2017.
 */

public class ConversationAdapter extends ArrayAdapter<Conversation> {


    public ConversationAdapter(Context context, int resource, List<Conversation> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    private View getCustomView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = LayoutInflater.from(getContext());
        View row = inflater.inflate(R.layout.spinner_item_conversation, parent, false);
        TextView label = (TextView) row.findViewById(R.id.name);
        label.setText(getItem(position).getNom());

        return row;
    }
}
