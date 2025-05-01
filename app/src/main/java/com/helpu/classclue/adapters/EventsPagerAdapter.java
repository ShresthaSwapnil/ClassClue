package com.helpu.classclue.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import com.helpu.classclue.events.EventListFragment;
import com.helpu.classclue.events.EventManager;

public class EventsPagerAdapter extends FragmentStateAdapter {

    public EventsPagerAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        // Return appropriate fragment based on position
        return EventListFragment.newInstance(position);
    }

    @Override
    public int getItemCount() {
        // Three tabs: To Do, In Progress, Completed
        return 3;
    }
}