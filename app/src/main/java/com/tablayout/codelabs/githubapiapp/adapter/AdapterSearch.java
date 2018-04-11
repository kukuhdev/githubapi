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
import com.tablayout.codelabs.githubapiapp.model.User;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by indra on 3/15/2016.
 */
public class AdapterSearch extends RecyclerView.Adapter<AdapterSearch.ViewHolder> {
    private List<User> mDataset = new ArrayList<>();
    private Context mContext;
    private Activity activity;

    public AdapterSearch(Activity activity) {
        this.activity = activity;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout rel_detail;
        CircleImageView iv_icon;
        TextView txt_name;
        public ViewHolder(View v) {
            super(v);
            rel_detail  = v.findViewById(R.id.rel_detail);
            iv_icon     = v.findViewById(R.id.iv_icon);
            txt_name    = v.findViewById(R.id.txt_name);
        }
    }

    public void addData(List<User> myDataset) {
        mDataset.clear();
        mDataset.addAll(myDataset);
        notifyDataSetChanged();
    }

    // Create new views (invoked by the layout manager)
    @Override
    public AdapterSearch.ViewHolder onCreateViewHolder(ViewGroup parent,int viewType) {
        // create a new view
        mContext = parent.getContext();
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_search, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Log.e("POSITION", position+"");
        Log.e("LOGIN", mDataset.get(position).getLogin());
        Log.e("LOGIN URL", mDataset.get(position).getUrl());
        Typeface bold = Typeface.createFromAsset(mContext.getAssets(), "fonts/CenturyGothicBold.ttf");
        holder.txt_name.setTypeface(bold);
        holder.txt_name.setText(mDataset.get(position).getLogin());
        Picasso.with(mContext).load(mDataset.get(position).getAvatarUrl())
                .resize(110,80)
                .placeholder(R.drawable.ic_github)
                .error(R.drawable.ic_github)
                .into(holder.iv_icon);

        holder.rel_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences = activity.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(Config.username, mDataset.get(position).getLogin());
                editor.putString(Config.member_url, mDataset.get(position).getUrl());
                editor.putString(Config.profil_image_url, mDataset.get(position).getAvatarUrl());
                editor.apply();

                Intent intent = new Intent(activity, DetailUserActivity.class);
                activity.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
