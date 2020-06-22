package com.example.todolist;

import android.content.Context;
import android.content.Intent;
import android.text.style.BulletSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class BusinessDoesAdapter extends RecyclerView.Adapter<BusinessDoesAdapter.BusinessViewHolder> {

    Context businessContext;
    ArrayList<BusinessDoes> businessDoes;

    public BusinessDoesAdapter(Context context,ArrayList<BusinessDoes> p){
        businessContext = context;
        businessDoes = p;
    }

    @NonNull
    @Override
    public BusinessViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new BusinessViewHolder(LayoutInflater.from(businessContext).inflate(R.layout.business_item_does,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull BusinessViewHolder holder, int position) {
        holder.businesstitledoes.setText(businessDoes.get(position).getBusinesstitledoes());
        holder.businessdescriptiondoes.setText(businessDoes.get(position).getBusinessdescriptiondoes());
        holder.businesscreationdoes.setText(businessDoes.get(position).getBusinesscreationdoes());
        holder.businessdeadlinedoes.setText(businessDoes.get(position).getBusinessdeadlinedoes());
        holder.businessassignedto.setText(businessDoes.get(position).getBusinessassignedto());
        holder.businessassignedtochange.setText(businessDoes.get(position).getBusinessassignedtochange());
        holder.businessstatus.setText(businessDoes.get(position).getBusinessstatus());
        holder.businessstatuschange.setText(businessDoes.get(position).getBusinessstatuschange());

        final String teamname = businessDoes.get(position).getTeamname();
        final String keyvalue = businessDoes.get(position).getBusinesskeyvalue();
        final String title = businessDoes.get(position).getBusinesstitledoes();
        final String desc = businessDoes.get(position).getBusinessdescriptiondoes();
        final String assigner = businessDoes.get(position).getBusinessassignedtochange();
        final String stat = businessDoes.get(position).getBusinessstatuschange();
        final String dead = businessDoes.get(position).getBusinessdeadlinedoes();

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent a = new Intent(businessContext,BusinessEditTaskActivity.class);
                a.putExtra("TeamName",teamname);
                a.putExtra("businesskeyvalue",keyvalue);
                a.putExtra("title", title);
                a.putExtra("desc",desc);
                a.putExtra("assigner",assigner);
                a.putExtra("stat",stat);
                a.putExtra("dead",dead);
                businessContext.startActivity(a);
            }
        });


    }

    @Override
    public int getItemCount() {
        return businessDoes.size();
    }

    class BusinessViewHolder extends RecyclerView.ViewHolder{

        TextView businesstitledoes,businessdescriptiondoes,businesscreationdoes,businessdeadlinedoes,businessassignedto,businessstatus,businessassignedtochange,businessstatuschange;

        public BusinessViewHolder(@NonNull View itemView) {
            super(itemView);

            businesstitledoes = itemView.findViewById(R.id.BusinessItemTitleText);
            businessdescriptiondoes = itemView.findViewById(R.id.BusinessItemDesText);
            businesscreationdoes = itemView.findViewById(R.id.BusinessCreateTimeItemText);
            businessdeadlinedoes = itemView.findViewById(R.id.BusinessTimeItemText);
            businessassignedto = itemView.findViewById(R.id.BusinessTaskAssigner);
            businessassignedtochange = itemView.findViewById(R.id.BusinessTaskAssigned);
            businessstatus = itemView.findViewById(R.id.BusinessTaskStatus);
            businessstatuschange = itemView.findViewById(R.id.BusinessTaskStatusAss);

        }
    }
}
