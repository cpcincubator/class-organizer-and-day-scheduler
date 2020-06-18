package cpc.class_planner.sam.model.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.List;

import cpc.class_planner.sam.model.RoutineFragment;

public class RoutineViewAdapter extends FragmentStatePagerAdapter {

    String[] pageTitle = {"Saturday","Sunday","Monday","Tuesday","Wednesday","Thursday"};
    public RoutineViewAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        Fragment fragment = new RoutineFragment(pageTitle[position]);
        return fragment;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return pageTitle[position];
    }

    @Override
    public int getCount() {
        return pageTitle.length;
    }
}
