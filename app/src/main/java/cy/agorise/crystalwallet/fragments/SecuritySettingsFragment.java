package cy.agorise.crystalwallet.fragments;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import cy.agorise.crystalwallet.R;
import cy.agorise.crystalwallet.application.CrystalSecurityMonitor;
import cy.agorise.crystalwallet.models.GeneralSetting;
import cy.agorise.crystalwallet.util.ChildViewPager;

/**
 * Created by xd on 1/17/18.
 * In this fragment the user should be able to select its preferred security option.
 */

public class SecuritySettingsFragment extends Fragment {

    public SecuritySettingsFragment() {
        // Required empty public constructor
    }

    public static SecuritySettingsFragment newInstance() {
        SecuritySettingsFragment fragment = new SecuritySettingsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @BindView(R.id.pager)
    public ChildViewPager mPager;

    public SecurityPagerAdapter securityPagerAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_security_settings, container, false);
        ButterKnife.bind(this, v);

        securityPagerAdapter = new SecurityPagerAdapter(getChildFragmentManager());
        mPager.setAdapter(securityPagerAdapter);

        switch(CrystalSecurityMonitor.getInstance(null).actualSecurity()) {
            case GeneralSetting.SETTING_PASSWORD:
                mPager.setCurrentItem(1);
                break;
            case GeneralSetting.SETTING_PATTERN:
                mPager.setCurrentItem(2);
                break;
            default:
                mPager.setCurrentItem(0);
        }
        mPager.setSwipeLocked(true);

        TabLayout tabLayout = v.findViewById(R.id.tabs);

        mPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mPager));

        return v;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            CrystalSecurityMonitor.getInstance(null).callPasswordRequest(this.getActivity());
        }
    }

    private class SecurityPagerAdapter extends FragmentPagerAdapter {
        SecurityPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    return new NoneSecurityFragment();
                case 1:
                    return new PinSecurityFragment();
                case 2:
                    return new PatternSecurityFragment();
            }

            return null; //new OnConstructionFragment();
        }

        @Override
        public int getCount() {
            return 3;
        }
    }
}
