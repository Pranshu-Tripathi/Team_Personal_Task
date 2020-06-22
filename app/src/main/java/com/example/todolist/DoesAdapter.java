package com.example.todolist;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class DoesAdapter extends RecyclerView.Adapter<DoesAdapter.MyViewHolder> {

    Context context;
    ArrayList<Mydoes> myDoes;

    public DoesAdapter (Context c,ArrayList<Mydoes> p){
        context = c;
        myDoes = p;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_does,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.titleDoes.setText(myDoes.get(position).getTitledoes());
        holder.descDoes.setText(myDoes.get(position).getDescdoes());
        holder.dateDoes.setText(myDoes.get(position).getDatedoes());
        holder.creationdoes.setText(myDoes.get(position).getCreationdoes());

        final String titleDoes = myDoes.get(position).getTitledoes();
        final String descDoes = myDoes.get(position).getDescdoes();
        final String dateDoes = myDoes.get(position).getDatedoes();
        final String keyDoes = myDoes.get(position).getKeydoes();

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent aa  = new Intent(context, EditTaskDesk.class);
                aa.putExtra("titledoes",titleDoes);
                aa.putExtra("descdoes",descDoes);
                aa.putExtra("datedoes",dateDoes);
                aa.putExtra("keydoes",keyDoes);
                context.startActivity(aa);
            }
        });
    }

    @Override
    public int getItemCount() {
        return myDoes.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        TextView titleDoes,descDoes,dateDoes,keyDoes,creationdoes;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            titleDoes = (TextView) itemView.findViewById(R.id.itemTitleText);
            descDoes = (TextView) itemView.findViewById(R.id.itemDesText);
            dateDoes = (TextView) itemView.findViewById(R.id.timeItemText);
            creationdoes = (TextView) itemView.findViewById(R.id.CreatetimeItemText);
        }
    }
}
