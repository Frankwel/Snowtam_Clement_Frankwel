package fr.fyt.snowtam_clement_frankwel.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import fr.fyt.snowtam_clement_frankwel.R;

/**
 * Created by frank on 14/11/2017.
 * link usefull: https://www.androidhive.info/2016/05/android-build-intro-slider-app/
 */

public class DecodingActivity extends AppCompatActivity {

    ViewPager viewPager;
    SectionsPagerAdapter mSectionsPagerAdapter;
    static List<String> codeList = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_decoding);

        //getting the list of code
        Gson gson = new Gson();
        Type type = new TypeToken<List<String>>(){}.getType();
        codeList = gson.fromJson((String)getIntent().getSerializableExtra("codeList"), type);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        viewPager.setAdapter(mSectionsPagerAdapter);
    }


    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_view_code, container, false);
            TextView tvCode = (TextView) rootView.findViewById(R.id.fvcTViewCode);
            tvCode.setText(DecodingActivity.codeList.get(getArguments().getInt(ARG_SECTION_NUMBER)));

            TextView tvIndicator = (TextView)rootView.findViewById(R.id.fd_tv_indicator);
            tvIndicator.setText(getArguments().getInt(ARG_SECTION_NUMBER)+1 + " / " + DecodingActivity.codeList.size());

            return rootView;
        }
    }


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position);
        }

        @Override
        public int getCount() {
            // return the number of code
            return DecodingActivity.codeList.size();
        }
    }
}
