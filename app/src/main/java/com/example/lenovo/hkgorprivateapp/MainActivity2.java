package com.example.lenovo.hkgorprivateapp;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

public class MainActivity2 extends AppCompatActivity implements MediaPlayer.OnCompletionListener, View.OnClickListener {

    private GridView mGridView;
    private TextView noFolderFoundText;
    private ImageView lastPPView;


    private File[] allImageFiles;
    private File[] allMusicFiles;

    int item;
    int total = 12;
    int column = 4;
    GridLayout gridLayout;

    private MediaPlayer mediaPlayer;

    int lastPosition = -1;

    private ArrayList<DataModel> myList;
    private Uri uri;

    private View imageViewNext;
    private ImageView nextImage, bgImage;
    String currentFolderString = null;

    LinearLayout volumnControl;
    RelativeLayout parentlayout;
    Button volumnUp, volumnDown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        //  mGridView = (GridView) findViewById(R.id.gvMain);
        noFolderFoundText = (TextView) findViewById(R.id.tv_no_folder);
        bgImage = (ImageView) findViewById(R.id.bgImage);
        gridLayout = (GridLayout) findViewById(R.id.gridLayout);
        volumnControl = (LinearLayout) findViewById(R.id.volumnControl);
        parentlayout = (RelativeLayout) findViewById(R.id.parentlayout);

        volumnUp = (Button) findViewById(R.id.volumnUp);
        volumnDown = (Button) findViewById(R.id.volumnDown);

        volumnControl.bringToFront();
        parentlayout.setVisibility(View.GONE);
        final AudioManager audio = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        volumnDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                audio.adjustStreamVolume(AudioManager.STREAM_MUSIC,
                        AudioManager.ADJUST_LOWER, AudioManager.FLAG_SHOW_UI);

            }
        });

        volumnUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                audio.adjustStreamVolume(AudioManager.STREAM_MUSIC,
                        AudioManager.ADJUST_RAISE, AudioManager.FLAG_SHOW_UI);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        noFolderFoundText.setVisibility(View.GONE);
        myList = new ArrayList<>();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (ContextCompat.checkSelfPermission(MainActivity2.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                startReading();
            } else {
                askPermissions();
            }
        } else {
            startReading();
        }
    }

    private void askPermissions() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {

            } else {

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
            }
        }

    }


    private void startReading() {
        File currentFile = null;


        //  currentFolderString = Utils.getStorageDirectoriesCustom() + Constants.FILES_FOLDER_NAME;


        Map<String, File> externalLocations = Utils.getAllStorageLocations();
        File sdCard = externalLocations.get(Utils.SD_CARD);
        File externalSdCard = externalLocations.get(Utils.EXTERNAL_SD_CARD);


        if (externalSdCard != null) {
            currentFolderString = externalSdCard.getPath() + Constants.FILES_FOLDER_NAME;
        } else {
            currentFolderString = sdCard.getPath() + Constants.FILES_FOLDER_NAME;
        }

        currentFile = new File(currentFolderString);
        if (currentFile.exists()) {
            parentlayout.setVisibility(View.VISIBLE);
            File folder = new File(currentFolderString + Constants.FILES_FOLDER_NAME_PICTURES);
            File musicFolder = new File(currentFolderString + Constants.FILES_FOLDER_NAME_SOUNDS);
            if (folder.exists() && musicFolder.exists()) {

                allImageFiles = folder.listFiles();
                allMusicFiles = musicFolder.listFiles();
                if (allImageFiles.length != 0 && allMusicFiles.length != 0) {

                    File bg = new File(currentFolderString + Constants.FILES_FOLDER_NAME_BACKGROUND);

                    if (bg.exists()) {
                        File[] bgFiles = bg.listFiles();

                        if (bgFiles.length != 0) {
                            Bitmap bgImg = BitmapFactory.decodeFile(bgFiles[0] + "");
                            bgImage.setBackground(new BitmapDrawable(getResources(), bgImg));
                        }
                    }


                    int length = 0;
                    int lengthImages = allImageFiles.length;
                    int lengthSounds = allMusicFiles.length;

                    if (lengthImages <= lengthSounds) {
                        length = lengthImages;
                    } else {
                        length = lengthSounds;
                    }

                    if (length > 12) {
                        length = 12;
                    }


                    for (int i = 0; i < length; i++) {
                        DataModel dataModel = new DataModel();
                        dataModel.setMusicPath(allMusicFiles[i] + "");
                        Bitmap bmImg = BitmapFactory.decodeFile(allImageFiles[i] + "");
                        dataModel.setMyBitmap(bmImg);
                        myList.add(dataModel);
                    }
                    setupGridLayout(myList);

                } else {
                    noFolderFoundText.setVisibility(View.VISIBLE);
                }
            }
        } else {
            noFolderFoundText.setVisibility(View.VISIBLE);
        }

    }


    private void setupGridLayout(ArrayList<DataModel> myList) {

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        gridLayout.removeAllViews();

        int row = total / column;
        gridLayout.setColumnCount(column);
        gridLayout.setRowCount(row + 1);
        for (int i = 0, c = 0, r = 0; i < myList.size(); i++, c++) {
            if (c == column) {
                c = 0;
                r++;
            }

            GridLayout.LayoutParams param = new GridLayout.LayoutParams();
            View itemView = LayoutInflater.from(this).inflate(R.layout.item, null);

            int width = metrics.widthPixels;
            int height = metrics.heightPixels;


            switch (deviceType()) {
                case "mobile":
                    param.height = (int) (height / 4);
                    param.width = (int) (width / 4.2);
                    break;
                case "tablet":
                    param.height = (int) (height / 3.8);
                    param.width = (int) (width / 4);
                    break;
            }


            switch (getRotation(getApplicationContext())) {
                case "portrait":

                    param.setMargins(3, 3, 3, 3);

                    break;
                case "landscape":
                    // param.rightMargin = 3;
                    //  param.topMargin = 3;
                    param.setMargins(1, 1, 1, 1);
                    break;
            }


            param.setGravity(Gravity.CENTER);
            param.columnSpec = GridLayout.spec(c);
            param.rowSpec = GridLayout.spec(r);
            ImageView img = (ImageView) itemView.findViewById(R.id.image_view);
            img.setImageBitmap(myList.get(i).getMyBitmap());


            itemView.setId(i);
            itemView.setLayoutParams(param);


            gridLayout.addView(itemView);
            itemView.setOnClickListener(this);

        }
    }

    private void adjustGridView() {

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        //  mGridView.setAdapter(new GridViewAdapter(metrics, mGridView, this, myList, this));
        // mGridView.setNumColumns(4);

    }

//    @Override
//    public void gdItemClickListener(final View v, final int position) {
//        switch (v.getId()) {
//            case R.id.card_view:
//
//
//
//                break;
//            default:
//                break;
//        }
//    }


    private void initPlayer(final int position, ImageView playImage) {


        if (mediaPlayer == null) {
            startPlayer(position, playImage);

        } else {

            if (position == lastPosition) {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                    mediaPlayer.release();
                    mediaPlayer = null;
                    playImage.setVisibility(View.GONE);
                } else {
                    mediaPlayer.start();
                    playImage.setVisibility(View.VISIBLE);
                }
            } else {
                lastPPView.setVisibility(View.GONE);
                mediaPlayer.pause();
                mediaPlayer.release();
                mediaPlayer = null;
                playImage.setVisibility(View.GONE);
                startPlayer(position, playImage);
            }


        }


    }


    @Override
    protected void onPause() {
        super.onPause();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }


    private void startPlayer(int position, ImageView playImage) {
        lastPosition = position;
        lastPPView = playImage;
        try {
            uri = Uri.parse(myList.get(position).getMusicPath() + "");
            mediaPlayer = MediaPlayer.create(MainActivity2.this, uri);
            mediaPlayer.setOnCompletionListener(MainActivity2.this);
            mediaPlayer.start();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        playImage.setVisibility(View.VISIBLE);
        myList.get(position).setChecked(playImage.isSelected());

    }


    private int getRandomTrack() {
        return new Random().nextInt(myList.size());

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 0: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    startReading();

                }
                return;
            }

        }
    }


    @Override
    public void onCompletion(MediaPlayer mp) {
        int randomTrackNumber = getRandomTrack();

        imageViewNext = gridLayout.getChildAt(randomTrackNumber);
        View viewPrevious = gridLayout.getChildAt(lastPosition);
        //lastPosition = randomTrackNumber;
        nextImage = (ImageView) imageViewNext.findViewById(R.id.iv_play_pause);
        ImageView previusImage = (ImageView) viewPrevious.findViewById(R.id.iv_play_pause);


        nextImage.setVisibility(View.VISIBLE);
        previusImage.setVisibility(View.GONE);

        startPlayer(randomTrackNumber, nextImage);

    }


    @Override
    public void onClick(View v) {
        ImageView playImage = (ImageView) v.findViewById(R.id.iv_play_pause);

        initPlayer(v.getId(), playImage);
    }


    public String getRotation(Context context) {
        final int rotation = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getOrientation();
        switch (rotation) {
            case Surface.ROTATION_0:
                return "portrait";
            case Surface.ROTATION_90:
                return "landscape";
            case Surface.ROTATION_180:
                //reverse
                return "portrait";
            default:
                //reverse
                return "landscape";
        }
    }

    private String deviceType() {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        float yInches = metrics.heightPixels / metrics.ydpi;
        float xInches = metrics.widthPixels / metrics.xdpi;
        double diagonalInches = Math.sqrt(xInches * xInches + yInches * yInches);
        if (diagonalInches >= 6.5) {
            return "tablet";// 6.5inch device or bigger
        } else {
            return "mobile";
        }

    }
}
