package com.helpu.classclue.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import com.helpu.classclue.events.EventListFragment;

public class EventsPagerAdapter extends FragmentStateAdapter {

    public EventsPagerAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        // Return different fragments based on position
        return EventListFragment.newInstance(position);
    }

    @Override
    public int getItemCount() {
        return 3; // Number of tabs
    }
}