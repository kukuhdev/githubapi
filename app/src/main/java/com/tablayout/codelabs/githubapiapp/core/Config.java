package com.tablayout.codelabs.githubapiapp.core;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Config {

    public static final String BASE_API_URL             = "https://api.github.com/";
    public static final String SEARCH_USER_URL          = BASE_API_URL+"search/users?q=";
    public static final String SEARCH_LIST_REPO         = BASE_API_URL+"users/";
    public static final String SEARCH_USER_DETAIL       = BASE_API_URL+"users/";


    public static final String SHARED_PREF_NAME         = "bamms";
    public static final String username                 = "username";
    public static final String member_url               = "member_url";
    public static final String profil_image_url         = "profil_image_url";

    public static final String name                     = "name";
    public static final String company                  = "company";
    public static final String location                 = "location";
    public static final String email                    = "email";

    private static Retrofit retrofit = null;


    public static Retrofit getClient() {
        if (retrofit==null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_API_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
