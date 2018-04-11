package com.tablayout.codelabs.githubapiapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.tablayout.codelabs.githubapiapp.core.Config;
import com.tablayout.codelabs.githubapiapp.fragment.FragmentProfile;
import com.tablayout.codelabs.githubapiapp.fragment.FragmentRepository;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class DetailUserActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.header_text) TextView header_text;
    @BindView(R.id.back) RelativeLayout back;

    @BindView(R.id.text_username) TextView text_username;
    @BindView(R.id.text_email) TextView text_email;
    @BindView(R.id.iv_user) CircleImageView iv_user;

    private String TAG = getClass().getSimpleName();
    private static Typeface typeface;

    private String username;
    private String member_url;
    private String avatar_url;

    private ViewPager viewPager;
    private TabLayout tabLayout;
    private ViewPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);
        typeface = Typeface.createFromAsset(this.getAssets(), "fonts/CenturyGothicRegular.ttf");
        ChangeFonts();
        setData();

        viewPager = findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        tabLayout = findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);

        back.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                finish();
                break;
        }

    }

    private void ChangeFonts(){
        header_text.setTypeface(typeface);
        text_email.setTypeface(typeface);
        text_username.setTypeface(typeface);
    }

    private void setData(){
        SharedPreferences sharedPreferences1 = DetailUserActivity.this.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        username      = sharedPreferences1.getString(Config.username, "null");
        member_url    = sharedPreferences1.getString(Config.member_url, "null");
        avatar_url    = sharedPreferences1.getString(Config.profil_image_url, "null");

        header_text.setText("DETAIL");
        text_username.setText(username);
        text_email.setText(member_url);
        Picasso.with(DetailUserActivity.this).load(avatar_url)
                .resize(110,80)
                .placeholder(R.drawable.ic_github)
                .error(R.drawable.ic_github)
                .into(iv_user);
    }

    private void setupViewPager(final ViewPager viewPager ) {

        adapter = new ViewPagerAdapter(getSupportFragmentManager());

        adapter.addFrag(new FragmentProfile(),"PROFILE");
        adapter.addFrag(new FragmentRepository(),"REPOSITORY");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {

            return mFragmentList.get(position);
        }



        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
            //notifyDataSetChanged();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }


    }
}
