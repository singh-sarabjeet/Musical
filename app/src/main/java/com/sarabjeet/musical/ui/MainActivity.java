package com.sarabjeet.musical.ui;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.IBinder;
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

import static com.sarabjeet.musical.utils.Constants.ACTION.ACTION_NEXT;
import static com.sarabjeet.musical.utils.Constants.ACTION.ACTION_PAUSE;
import static com.sarabjeet.musical.utils.Constants.ACTION.ACTION_RESUME;
import static com.sarabjeet.musical.utils.Constants.PLAYER.PLAY;

public class MainActivity extends AppCompatActivity {
    final int READ_PERMISSION = 1;
    BroadcastReceiver receiver;
    ImageView playButtonSmall;
    Context mContext;
    ImageView albumArtMini;
    ImageView albumArtPlayer;
    MusicPlayerService musicPlayerService;
    MediaPlayer mediaPlayer = null;
    boolean mServiceBound = false;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private AdView mAdView;
    private TextView songTitle;
    private TextView songArtist;
    private Bitmap bitmap;
    private ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mServiceBound = false;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicPlayerService.MusicBinder musicBinder = (MusicPlayerService.MusicBinder) service;
            musicPlayerService = musicBinder.getService();
            mServiceBound = true;
        }
    };

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

            /**
             * If the player is already playing, the icon would be that of pause and on click it should be changed
             * to play button icon. The reverse condition holds for pause state
             */
            @Override
            public void onClick(View v) {
                if (mediaPlayer.isPlaying()) {
                    Picasso.with(mContext)
                            .load(R.drawable.ic_play_arrow)
                            .into(playButtonSmall);
                    Intent intent = new Intent(mContext, MusicPlayerService.class);
                    intent.setAction(ACTION_PAUSE);
                    mContext.startService(intent);
                } else {
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

        nextButtonSmall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, MusicPlayerService.class);
                intent.setAction(ACTION_NEXT);
                mContext.startService(intent);
            }
        });

        Picasso.with(this)
                .load(R.drawable.fallback_cover)
                .fit()
                .into(albumArtMini);
        Picasso.with(this)
                .load(R.drawable.fallback_cover)
                .into(albumArtPlayer);
        Picasso.with(this)
                .load(R.drawable.ic_play_arrow)
                .into(playButtonSmall);
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
                String path = intent.getStringExtra("Path");

                if (mServiceBound) {
                    mediaPlayer = musicPlayerService.getMediaPlayer();
                    if (mediaPlayer.isPlaying()) {
                        Picasso.with(context)
                                .load(R.drawable.ic_pause)
                                .into(playButtonSmall);
                        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
                        mmr.setDataSource(path);
                        byte[] data = mmr.getEmbeddedPicture();
                        if (data != null) {
                            bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                            albumArtMini.setImageBitmap(bitmap);
                            albumArtPlayer.setImageBitmap(bitmap);

                        }
                        songTitle.setText(intent.getStringExtra("Title"));
                        songArtist.setText(intent.getStringExtra("Artist"));
                    }

                    mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            Picasso.with(mContext)
                                    .load(R.drawable.ic_play_arrow)
                                    .into(playButtonSmall);
                        }
                    });

                }
            }
        };

        checkPermission();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(this, MusicPlayerService.class);
        intent.setAction("SERVICE_START");
        startService(intent);
        bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
        LocalBroadcastManager.getInstance(this).registerReceiver((receiver),
                new IntentFilter(PLAY));
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mServiceBound) {
            mediaPlayer = musicPlayerService.getMediaPlayer();
            if (mediaPlayer.isPlaying()) {
                Picasso.with(this)
                        .load(R.drawable.ic_pause)
                        .into(playButtonSmall);
                byte[] data = musicPlayerService.getData();
                if (data != null) {
                    bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                    albumArtMini.setImageBitmap(bitmap);
                    albumArtPlayer.setImageBitmap(bitmap);
                }
                songTitle.setText(musicPlayerService.getTitle());
                songArtist.setText(musicPlayerService.getArtist());
            } else {
                Picasso.with(mContext)
                        .load(R.drawable.ic_play_arrow)
                        .into(playButtonSmall);
            }

        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mServiceBound) {
            unbindService(mServiceConnection);
            mServiceBound = false;
        }
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
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
