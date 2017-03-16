package com.example.nicolas.ig2i_tp1.tp2.activity.UI;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.nicolas.ig2i_tp1.R;
import com.example.nicolas.ig2i_tp1.tp2.model.Message;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nicolas on 13/03/2017.
 */

public class MessageRecyclerView extends RecyclerView.Adapter<MessageRecyclerView.MessageRVHolder> {

    public List<Message> messages = new ArrayList<>();

    @Override
    public MessageRVHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_element_message, parent, false);
        return new MessageRVHolder(v);
    }

    @Override
    public void onBindViewHolder(MessageRVHolder holder, int position) {
        Message item = messages.get(position);
        holder.nom.setText(item.getAuthor());
        holder.message.setText(item.getText());
        holder.nom.setTextColor(item.getColor());
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public class MessageRVHolder extends RecyclerView.ViewHolder {

        TextView nom;
        TextView message;

        public MessageRVHolder(View itemView) {
            super(itemView);
            nom = (TextView) itemView.findViewById(R.id.nom);
            message = (TextView) itemView.findViewById(R.id.message);
        }
    }
}
