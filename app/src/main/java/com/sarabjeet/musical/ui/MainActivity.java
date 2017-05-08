package com.sarabjeet.musical.ui;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.sarabjeet.musical.R;
import com.sarabjeet.musical.sync.LibrarySyncIntentService;
import com.sarabjeet.musical.sync.MusicPlayerService;
import com.squareup.picasso.Picasso;

import static com.sarabjeet.musical.utils.Constants.ACTION.ACTION_PAUSE;
import static com.sarabjeet.musical.utils.Constants.ACTION.ACTION_RESUME;
import static com.sarabjeet.musical.utils.Constants.PLAYER.PLAY;

public class MainActivity extends AppCompatActivity {
    final int READ_PERMISSION = 1;
    BroadcastReceiver receiver;
    ImageView playButtonSmall;
    Context mContext;
    String mediaPlayerStatus = "initial";
    ImageView albumArtMini;
    ImageView albumArtPlayer;

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;
    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private AdView mAdView;
    private TextView songTitle;
    private TextView songArtist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;

        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        songTitle = (TextView) findViewById(R.id.song_title_mini_player);
        songArtist = (TextView) findViewById(R.id.artist_title_mini_player);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        playButtonSmall = (ImageView) findViewById(R.id.icon_play_small);
        playButtonSmall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayerStatus.equals("playing")) {
                    Picasso.with(mContext)
                            .load(R.drawable.ic_play_arrow)
                            .into(playButtonSmall);
                    Intent intent = new Intent(mContext, MusicPlayerService.class);
                    intent.setAction(ACTION_PAUSE);
                    mContext.startService(intent);
                } else if (mediaPlayerStatus.equals("paused")) {
                    Picasso.with(mContext)
                            .load(R.drawable.ic_pause)
                            .into(playButtonSmall);
                    Intent intent = new Intent(mContext, MusicPlayerService.class);
                    intent.setAction(ACTION_RESUME);
                    mContext.startService(intent);
                }
            }
        });
        albumArtMini = (ImageView) findViewById(R.id.album_art_mini_player);
        albumArtPlayer = (ImageView) findViewById(R.id.album_art_player);
        ImageView nextButtonSmall = (ImageView) findViewById(R.id.icon_next_small);
        Picasso.with(this)
                .load(R.drawable.fallback_cover)
                .fit()
                .into(albumArtMini);
        Picasso.with(this)
                .load(R.drawable.fallback_cover)
                .into(albumArtPlayer);
        if (mediaPlayerStatus.equals("initial") || mediaPlayerStatus.equals("paused")) {
            Picasso.with(this)
                    .load(R.drawable.ic_play_arrow)
                    .into(playButtonSmall);
        } else {
            Picasso.with(this)
                    .load(R.drawable.ic_pause)
                    .into(playButtonSmall);
        }
        Picasso.with(this)
                .load(R.drawable.ic_skip_next)
                .into(nextButtonSmall);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String s = intent.getStringExtra("Player");
                String path = intent.getStringExtra("Path");
                if (s.equals("start")) {
                    mediaPlayerStatus = "playing";
                    Picasso.with(context)
                            .load(R.drawable.ic_pause)
                            .into(playButtonSmall);
                    MediaMetadataRetriever mmr = new MediaMetadataRetriever();
                    mmr.setDataSource(path);
                    byte[] data = mmr.getEmbeddedPicture();
                    if (data != null) {
                        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                        albumArtMini.setImageBitmap(bitmap);
                        albumArtPlayer.setImageBitmap(bitmap);

                    }
                    songTitle.setText(intent.getStringExtra("Title"));
                    songArtist.setText(intent.getStringExtra("Artist"));

                } else if (s.equals("pause")) {
                    mediaPlayerStatus = "paused";
                }
            }
        };

        checkPermission();
    }

    @Override
    protected void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(this).registerReceiver((receiver),
                new IntentFilter(PLAY)
        );
    }

    @Override
    protected void onStop() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
        super.onStop();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void checkPermission() {
        int permissionCheck = 0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);


            if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                        this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, READ_PERMISSION);
            } else {
                Intent intent = new Intent(this, LibrarySyncIntentService.class);
                startService(intent);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {

            case READ_PERMISSION:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    Intent intent = new Intent(this, LibrarySyncIntentService.class);
                    startService(intent);
                }
                break;

            default:
                Toast.makeText(this, "Permission to read storage denied. Please restart the app and grant permission", Toast.LENGTH_SHORT).show();
                break;
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
            switch (position) {
                case 0:
                    return new SongsFragment();
                case 1:
                    return new AlbumsFragment();
                case 2:
                    return new ArtistsFragment();
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "SONGS";
                case 1:
                    return "ALBUMS";
                case 2:
                    return "ARTISTS";
            }
            return null;
        }
    }
}
