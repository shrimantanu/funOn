package com.funon.com.funon.Adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.funon.com.funon.R;
import com.funon.com.funon.SessionManager;
import com.funon.com.funon.fragments.search;


import java.util.ArrayList;

public class RecentHistoryAdapter extends RecyclerView.Adapter<RecentHistoryAdapter.RecyclerViewHolder> {
    search parent;
    ArrayList<String> ModelList;
    SessionManager manager;

    public RecentHistoryAdapter(search parent, ArrayList<String> ModelList){
        this.parent=parent;
        this.ModelList=ModelList;
        manager=new SessionManager();

    }

    @NonNull
    @Override
    public RecentHistoryAdapter.RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.history_items, parent, false);
        // SearchChemistAdapter.ViewHolder viewHolder = new SearchChemistAdapter.ViewHolder(v);
        return new RecyclerViewHolder(view);
    }

    public void filterlist(  ArrayList<String> ModelList){
        this.ModelList.clear();
        this.ModelList.addAll(ModelList);
        notifyDataSetChanged();
        notifyItemRangeChanged(0,this.ModelList.size());
    }

    @Override
    public void onBindViewHolder(@NonNull RecentHistoryAdapter.RecyclerViewHolder holder, final int position) {
holder.query.setText(ModelList.get(position));
        Log.e("On Bind","M"+ModelList.get(position));

holder.query.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        parent.addtoRecent=false;

        parent.searchView.setQuery(ModelList.get(position),false);
    }
});
holder.clear.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        ModelList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position,ModelList.size());
        if(ModelList.size()==0) {
         Log.e("SizeZER0","@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");

            parent.recent_searches.setPadding(0, 0, 0, 60);

        }
        manager.setPrefs(parent.getActivity(),"recent_searches","");
        for (int k=ModelList.size()-1;k>=0;k--){
            manager.setPrefs(parent.getActivity(), "recent_searches",  ModelList.get(k)+"@" + manager.getPrefs(parent.getActivity(), "recent_searches") );
        }
    }
});


    }

    @Override
    public int getItemCount() {
        return ModelList.size();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder{

        public TextView query;
        public ImageView clear;





        public RecyclerViewHolder(View itemView) {
            super(itemView);
            query = (TextView)itemView.findViewById(R.id.query);
            clear=(ImageView) itemView.findViewById(R.id.clear);



        }
    }
}
