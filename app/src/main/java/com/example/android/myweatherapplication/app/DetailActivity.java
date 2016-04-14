package com.example.android.myweatherapplication.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class DetailActivity extends ActionBarActivity {

    /* method onCreate di panggil ketika DetailActivity mulai di panggil oleh apps, layout yang
     * di bentuk oleh DetailActivity adalah activity_detail dengan id container, namun di dalam
     * activity_detail isi nya akan diatur oleh inner class DetailFragment */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new DetailFragment())
                    .commit();
        }
    }

    /* method onCreateOptionsMenu digunakan untuk membuat menu pada layout activity_detail, dimana
     * menu yang di panggil adalah detail.xml yang ada pada package menu */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail, menu);
        return true;
    }

    /* method onCreateOptionsItemSelected digunakan untuk mendapatkan item yang diklik dari menu
     * yang ada pada activity_detail, jika menu yang di pilih adalah settings, maka akan
     * diarahkan kepada SettingsActivity */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /* DetailFragment merupakan inner class yang berfungsi mengatur isi dari activity_detail */
    public static class DetailFragment extends Fragment {

        /* hastag ketika menggunakan sharing intents */
        private static final String FORECAST_SHARE_HASHTAG = " #MyWeatherApplication";
        private String mForecastStr;

        public DetailFragment() {
            setHasOptionsMenu(true);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            /* ketika DetailFragment dijalankan, akan dimasukkan layout fragment_detail ke dalam
             * activity_detail */
            View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

            /* penggunaan Intent ditujukan untuk mendapatkan data text dari activity sebelumnya
             * dan nantinya data tersebut digunakan untuk di tampilkan pada textView yang
             * ada pada fragment_detail */
            Intent intent = getActivity().getIntent();
            if (intent != null && intent.hasExtra(Intent.EXTRA_TEXT)) {
                mForecastStr = intent.getStringExtra(Intent.EXTRA_TEXT);
                ((TextView) rootView.findViewById(R.id.detail_text))
                        .setText(mForecastStr);
            }

            return rootView;
        }

        @Override
        public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
            /* memasukkan menu yang ada pada detailfragment.xml pada package menu */
            inflater.inflate(R.menu.detailfragment, menu);

            /* mendapatkan menu share berdasarkan id */
            MenuItem menuItem = menu.findItem(R.id.action_share);

            /* membuat menu share dapat melakukan sharing data kepada aplikasi lain */
            ShareActionProvider mShareActionProvider =
                    (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);

            /* apabila menu share di klik, maka akan di tampilkan bisa di share kemana aja */
            if (mShareActionProvider != null ) {
                mShareActionProvider.setShareIntent(createShareForecastIntent());
            } else {
                Log.d("menu share", "Share Action Provider is null?");
            }
        }

        /* membuat Intent yang dapat digunakan untuk menyimpan text dan di share kepada apps
           lain */
        private Intent createShareForecastIntent() {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT,
                    mForecastStr + FORECAST_SHARE_HASHTAG);
            return shareIntent;
        }
    }
}
