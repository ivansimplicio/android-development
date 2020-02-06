package com.dev.meuaplicativo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    LivroPagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pagerAdapter = new LivroPagerAdapter(getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.viewPager);
        viewPager.setAdapter(pagerAdapter);
    }

    class LivroPagerAdapter extends FragmentPagerAdapter{
        LivrosListFragment listFragment;
        LivrosGridFragment gridFragment;

        public LivroPagerAdapter(FragmentManager fm){
            super(fm);
            listFragment = new LivrosListFragment();
            gridFragment = new LivrosGridFragment();
        }

        @Override
        public Fragment getItem(int position) {
            return (position == 0) ? listFragment : gridFragment;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return (position == 0) ? "Lista" : "Grid";
        }

        @Override
        public int getCount() {
            return 2;
        }
    }
}