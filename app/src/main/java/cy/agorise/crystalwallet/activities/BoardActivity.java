package cy.agorise.crystalwallet.activities;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cy.agorise.crystalwallet.R;
import cy.agorise.crystalwallet.fragments.BalanceFragment;
import cy.agorise.crystalwallet.fragments.ContactsFragment;
import cy.agorise.crystalwallet.fragments.SendTransactionFragment;
import cy.agorise.crystalwallet.fragments.TransactionsFragment;

/**
 * Created by Henry Varona on 7/10/2017.
 */

public class BoardActivity  extends AppCompatActivity {

    @BindView(R.id.pager)
    public ViewPager mPager;

    @BindView(R.id.btnGeneralSettings)
    public ImageButton btnGeneralSettings;

    @BindView(R.id.fabSend)
    public FloatingActionButton fabSend;

    @BindView(R.id.fabReceive)
    public FloatingActionButton fabReceive;

    @BindView(R.id.fabAddContact)
    public FloatingActionButton fabAddContact;

    public BoardPagerAdapter boardAdapter;

    /*
     * the id of the account of this crypto net balance. It will be loaded
     * when the element is bounded.
     */
    long cryptoNetAccountId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.board);
        ButterKnife.bind(this);

        //-1 represents a crypto net account not loaded yet
        this.cryptoNetAccountId = -1;

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        boardAdapter = new BoardPagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(boardAdapter);

        TabLayout tabLayout = findViewById(R.id.tabs);

        mPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mPager));

        fabSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendFromThisAccount();
            }
        });

        // Hide Add Contact fab, it won't hide until first page changed...
        // Convert 72dp to pixels (fab is 56dp in diameter + 16dp margin)
        final int fabDistanceToHide = (int) (72 * Resources.getSystem().getDisplayMetrics().density);;
        fabAddContact.animate().translationY(fabDistanceToHide)
                .setInterpolator(new LinearInterpolator()).start();

        // Hide and show respective fabs when convenient
        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {

                switch (position) {
                    case 0:
                        fabReceive.show();
                        fabSend.show();
                        fabAddContact.animate().translationY(fabDistanceToHide)
                                .setInterpolator(new LinearInterpolator()).start();
                        break;

                    case 1:
                        fabReceive.show();
                        fabSend.show();
                        fabAddContact.animate().translationY(fabDistanceToHide)
                                .setInterpolator(new LinearInterpolator()).start();
                        break;

                    default:
                        fabReceive.hide();
                        fabSend.hide();
                        fabAddContact.animate().translationY(0)
                                .setInterpolator(new LinearInterpolator()).start();
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public void sendFromThisAccount(){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment prev = getSupportFragmentManager().findFragmentByTag("SendDialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        // Create and show the dialog.
        SendTransactionFragment newFragment = SendTransactionFragment.newInstance(this.cryptoNetAccountId);
        newFragment.show(ft, "SendDialog");
    }

    @OnClick(R.id.btnGeneralSettings)
    public void onBtnGeneralSettingsClick(){
        Intent intent = new Intent(this, GeneralSettingsActivity.class);
        startActivity(intent);
    }

    private class BoardPagerAdapter extends FragmentStatePagerAdapter {
        public BoardPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    return new BalanceFragment();
                case 1:
                    return new TransactionsFragment();
                case 2:
                    return new ContactsFragment();
            }


            return null; //new OnConstructionFragment();
        }

        @Override
        public int getCount() {
            return 3;
        }
    }
}
