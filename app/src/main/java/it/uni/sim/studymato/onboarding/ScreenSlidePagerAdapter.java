package it.uni.sim.studymato.onboarding;


import static it.uni.sim.studymato.onboarding.OnboardingActivity.NUM_PAGES;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class ScreenSlidePagerAdapter extends FragmentStateAdapter {
    public ScreenSlidePagerAdapter(FragmentActivity fa) {
        super(fa);
    }

    @Override
    public Fragment createFragment(int position) {
        return new OnboardingPageFragment();
    }

    @Override
    public int getItemCount() {
        return NUM_PAGES;
    }
}

