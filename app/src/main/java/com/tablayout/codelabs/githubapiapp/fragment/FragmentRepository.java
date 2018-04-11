package com.tablayout.codelabs.githubapiapp.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.DimenRes;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;
import com.tablayout.codelabs.githubapiapp.MainActivity;
import com.tablayout.codelabs.githubapiapp.R;
import com.tablayout.codelabs.githubapiapp.adapter.AdapterRepository;
import com.tablayout.codelabs.githubapiapp.adapter.AdapterSearch;
import com.tablayout.codelabs.githubapiapp.core.APIService;
import com.tablayout.codelabs.githubapiapp.core.Config;
import com.tablayout.codelabs.githubapiapp.model.Repository;
import com.tablayout.codelabs.githubapiapp.model.User;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentRepository extends Fragment implements View.OnClickListener {

    @BindView(R.id.view_repository) RecyclerView view_repository;

    private String TAG = getClass().getSimpleName();
    private static Typeface typeface;

    private AdapterRepository mAdapterRepo;
    private ArrayList<Repository> mRepo = new ArrayList<>();

    String url = "";
    private String username;

    public FragmentRepository() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_repository, container, false);
        ButterKnife.bind(this, rootView);

        mAdapterRepo    = new AdapterRepository(getActivity());

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL,false);
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(getActivity(), R.dimen.item_offset0);
        view_repository.addItemDecoration(itemDecoration);
        view_repository.setLayoutManager(mLayoutManager);
        view_repository.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getActivity()).color(Color.parseColor("#f6f6f6")).sizeResId(R.dimen.item_offset).build());
        view_repository.setAdapter(mAdapterRepo);

        setData();


        return rootView;
    }

    @Override
    public void onClick(View v) {

    }

    private void setData(){
        SharedPreferences sharedPreferences1 = getActivity().getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        username      = sharedPreferences1.getString(Config.username, "null");

        url = Config.SEARCH_LIST_REPO+username+"/repos";
        initializeRetrofit(url);
    }

    private void initializeRetrofit(String url) {
        mRepo.clear();
        Log.e(TAG, url);
        APIService apiService = Config.getClient().create(APIService.class);
        Call<ResponseBody> result = apiService.getResultAsJSON(url);
        result.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
//                    Log.e(TAG+" REPO", response.body().string());
//                    JSONObject  json  = new JSONObject(response.body().string());
                    JSONArray myArray = new JSONArray(response.body().string());
                    for (int j = 0; j < myArray.length(); j++) {
                        JSONObject objectarr   = myArray.getJSONObject(j);
                        mRepo.add(new Repository(objectarr.getString("id"),
                                objectarr.getString("name"),
                                objectarr.getString("html_url")));
                    }
                    mAdapterRepo.addData(mRepo);
                    Log.e(TAG+"LIST", mRepo.size()+"");
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public class ItemOffsetDecoration extends RecyclerView.ItemDecoration {

        private int mItemOffset;

        public ItemOffsetDecoration(int itemOffset) {
            mItemOffset = itemOffset;
        }

        public ItemOffsetDecoration(@NonNull Context context, @DimenRes int itemOffsetId) {
            this(context.getResources().getDimensionPixelSize(itemOffsetId));
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                                   RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            outRect.set(mItemOffset, mItemOffset, mItemOffset, mItemOffset);
        }

    }
}
