package com.tablayout.codelabs.githubapiapp.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tablayout.codelabs.githubapiapp.R;
import com.tablayout.codelabs.githubapiapp.core.APIService;
import com.tablayout.codelabs.githubapiapp.core.Config;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentProfile extends Fragment implements View.OnClickListener {

    @BindView(R.id.text_1) TextView text_1;
    @BindView(R.id.text_2) TextView text_2;
    @BindView(R.id.text_following) TextView text_following;
    @BindView(R.id.text_followers) TextView text_followers;
    @BindView(R.id.text_name) TextView text_name;
    @BindView(R.id.text_company) TextView text_company;
    @BindView(R.id.text_location) TextView text_location;
    @BindView(R.id.text_email) TextView text_email;

    private String TAG = getClass().getSimpleName();
    private static Typeface typeface;

    String url = "";
    private String username;


    public FragmentProfile() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        ButterKnife.bind(this, rootView);
        typeface = Typeface.createFromAsset(getActivity().getAssets(), "fonts/CenturyGothicRegular.ttf");
        changeFonts();
        setData();


        return rootView;
    }

    @Override
    public void onClick(View v) {

    }

    private void changeFonts(){
        text_1.setTypeface(typeface);
        text_2.setTypeface(typeface);
        text_following.setTypeface(typeface);
        text_followers.setTypeface(typeface);
        text_name.setTypeface(typeface);
        text_company.setTypeface(typeface);
        text_location.setTypeface(typeface);
        text_email.setTypeface(typeface);
    }

    private void setData(){
        SharedPreferences sharedPreferences1 = getActivity().getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        username      = sharedPreferences1.getString(Config.username, "null");

        url = Config.SEARCH_USER_DETAIL+username;
        initializeRetrofit(url);
    }

    private void initializeRetrofit(String url) {
        Log.e(TAG, url);
        APIService apiService = Config.getClient().create(APIService.class);
        Call<ResponseBody> result = apiService.getResultAsJSON(url);
        result.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    JSONObject myObject = new JSONObject(response.body().string());
                    String name         = myObject.getString("name");
                    String company      = myObject.getString("company");
                    String loct         = myObject.getString("location");
                    String email        = myObject.getString("email");
                    String following    = myObject.getString("following");
                    String followers    = myObject.getString("followers");

//                    SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
//                    SharedPreferences.Editor editor = sharedPreferences.edit();
//                    editor.putString(Config.name, name);
//                    editor.putString(Config.company, company);
//                    editor.putString(Config.location, loct);
//                    editor.putString(Config.email, email);
//                    editor.apply();

                    text_following.setText(following);
                    text_followers.setText(followers);
                    text_name.setText(name);
                    text_company.setText(company);
                    text_location.setText(loct);
                    text_email.setText(email);

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



}
