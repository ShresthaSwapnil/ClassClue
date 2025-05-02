package com.helpu.classclue.events;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.helpu.classclue.R;
import com.helpu.classclue.adapters.EventsPagerAdapter;

public class EventsFragment extends Fragment {

    private ViewPager2 viewPager;
    private TabLayout tabLayout;
    private Button btnDownload;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_events, container, false);

        // Initialize ViewPager and TabLayout
        viewPager = view.findViewById(R.id.viewPager);
        tabLayout = view.findViewById(R.id.tabLayout);
        btnDownload = view.findViewById(R.id.ivDownload);

        // Set up ViewPager with adapter
        EventsPagerAdapter pagerAdapter = new EventsPagerAdapter(this);
        viewPager.setAdapter(pagerAdapter);

        // Connect TabLayout with ViewPager
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            switch (position) {
                case 0: tab.setText("To Do"); break;
                case 1: tab.setText("In Progress"); break;
                case 2: tab.setText("Completed"); break;
            }
        }).attach();

        btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Disable the button to prevent multiple clicks
                btnDownload.setEnabled(false);

                // Post a delayed action using Handler
                new Handler(Looper.getMainLooper()).postDelayed(() -> {
                    // Show a toast message
                    Toast.makeText(getContext(), "Download Complete", Toast.LENGTH_SHORT).show();
                    // Re-enable the button
                    btnDownload.setEnabled(true);
                }, 1000); // 1000 milliseconds = 1 second
            }
        });

        return view;
    }
}