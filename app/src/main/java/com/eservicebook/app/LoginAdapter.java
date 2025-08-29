package com.eservicebook.app;

import android.content.Context;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import com.eservicebook.app.fragments.LoginTabFragment;
import com.eservicebook.app.fragments.SignupTabFragment;

public class LoginAdapter extends FragmentPagerAdapter {
    private final Context context;
    int totalTabs;

    public LoginAdapter(FragmentManager fm, Context context, int totalTabs) {
        super(fm);
        this.context = context;
        this.totalTabs = totalTabs;
    }

    @Override
    public int getCount() {
        return totalTabs;
    }

    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new LoginTabFragment(context);
            case 1:
                return new SignupTabFragment();
            default:
                return null;
        }
    }
}
