package com.tablayout.codelabs.githubapiapp.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.tablayout.codelabs.githubapiapp.DetailUserActivity;
import com.tablayout.codelabs.githubapiapp.R;
import com.tablayout.codelabs.githubapiapp.core.Config;
import com.tablayout.codelabs.githubapiapp.model.Repository;
import com.tablayout.codelabs.githubapiapp.model.User;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by indra on 3/15/2016.
 */
public class AdapterRepository extends RecyclerView.Adapter<AdapterRepository.ViewHolder> {
    private List<Repository> mDataset = new ArrayList<>();
    private Context mContext;
    private Activity activity;

    public AdapterRepository(Activity activity) {
        this.activity = activity;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout rel_detail;
        TextView txt_title;
        public ViewHolder(View v) {
            super(v);
            rel_detail  = v.findViewById(R.id.rel_detail);
            txt_title   = v.findViewById(R.id.txt_title);
        }
    }

    public void addData(List<Repository> myDataset) {
        mDataset.clear();
        mDataset.addAll(myDataset);
        notifyDataSetChanged();
    }

    // Create new views (invoked by the layout manager)
    @Override
    public AdapterRepository.ViewHolder onCreateViewHolder(ViewGroup parent,int viewType) {
        // create a new view
        mContext = parent.getContext();
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_repository, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Log.e("POSITION", position+"");
        Log.e("LOGIN", mDataset.get(position).getName());
        Log.e("LOGIN URL", mDataset.get(position).getUrl());
        Typeface bold = Typeface.createFromAsset(mContext.getAssets(), "fonts/CenturyGothicBold.ttf");
        holder.txt_title.setTypeface(bold);
        holder.txt_title.setText(mDataset.get(position).getName());

        holder.rel_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
