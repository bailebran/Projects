package edu.ranken.bbailey.finalproject.ui.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import edu.ranken.bbailey.finalproject.ui.fragment.CompletedListFragment;
import edu.ranken.bbailey.finalproject.ui.fragment.HuntingListFragment;
import edu.ranken.bbailey.finalproject.ui.fragment.PokedexFragment;

public class MyPagerAdapter extends FragmentPagerAdapter {

    private FragmentManager mFragmentManager;

    public MyPagerAdapter(FragmentManager fm) {
        super(fm);
        mFragmentManager = fm;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0: return new PokedexFragment();
            case 1: return new HuntingListFragment();
            case 2: return new CompletedListFragment();
            default: throw new IndexOutOfBoundsException("Position is invalid");
        }
    }
}
