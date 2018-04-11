package com.tablayout.codelabs.githubapiapp;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Movie;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.DimenRes;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.security.ProviderInstaller;
import com.tablayout.codelabs.githubapiapp.adapter.AdapterSearch;
import com.tablayout.codelabs.githubapiapp.core.APIService;
import com.tablayout.codelabs.githubapiapp.core.Config;
import com.tablayout.codelabs.githubapiapp.model.User;
import com.wang.avi.AVLoadingIndicatorView;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import org.json.JSONArray;
import org.json.JSONObject;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    @BindView(R.id.edit_search) EditText edit_search;
    @BindView(R.id.txt_cancel) TextView txt_cancel;
    @BindView(R.id.txt_search) TextView txt_search;

    @BindView(R.id.view_seacrh) RecyclerView view_seacrh;

    @BindView(R.id.lin_loading) LinearLayout lin_loading;
    @BindView(R.id.avi) AVLoadingIndicatorView avi;

    private String TAG = getClass().getSimpleName();
    private static Typeface typeface;

    private AdapterSearch mAdapterSearch;
    private ArrayList<User> mSearch = new ArrayList<>();

    String url = "";

    String keyword="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        typeface = Typeface.createFromAsset(this.getAssets(), "fonts/CenturyGothicRegular.ttf");
        changeFonts();

        try {
            ProviderInstaller.installIfNeeded(getApplicationContext());
            SSLContext sslContext = SSLContext.getInstance("TLSv1.2");
            sslContext.init(null, null, null);
            SSLEngine engine = sslContext.createSSLEngine();
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }

        edit_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.equals("0")) {
                    txt_cancel.setVisibility(View.VISIBLE);
                    txt_search.setVisibility(View.GONE);
                } else {
                    txt_cancel.setVisibility(View.GONE);
                    txt_search.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mAdapterSearch    = new AdapterSearch(this);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL,false);
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(this, R.dimen.item_offset0);
        view_seacrh.addItemDecoration(itemDecoration);
        view_seacrh.setLayoutManager(mLayoutManager);
        view_seacrh.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this).color(Color.parseColor("#f6f6f6")).sizeResId(R.dimen.item_offset).build());
        view_seacrh.setAdapter(mAdapterSearch);

        txt_search.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.txt_cancel:
                break;
            case R.id.txt_search:
                keyword = edit_search.getText().toString();
                url = Config.SEARCH_USER_URL+keyword;
                initializeRetrofit(url);
                break;
        }
    }

    private void changeFonts(){
        edit_search.setTypeface(typeface);
        txt_cancel.setTypeface(typeface);
        txt_search.setTypeface(typeface);
        edit_search.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
    }

    private void initializeRetrofit(String url) {
        lin_loading.setVisibility(View.VISIBLE);
        avi.show();
        mSearch.clear();
        Log.e(TAG, url);
        APIService apiService = Config.getClient().create(APIService.class);
        Call<ResponseBody> result = apiService.getResultAsJSON(url);
        result.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    JSONObject myObject = new JSONObject(response.body().string());
                    JSONArray items     = myObject.getJSONArray("items");
                    for (int i = 0; i < items.length(); i++) {
                        JSONObject object   = items.getJSONObject(i);
                        mSearch.add(new User(object.getString("login"),
                                Integer.parseInt(object.getString("id")),
                                object.getString("avatar_url"),object.getString("url")));
                    }
                    mAdapterSearch.addData(mSearch);
                    lin_loading.setVisibility(View.GONE);
                    avi.hide();
                    Log.e(TAG+"LIST", mSearch.size()+"");
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
