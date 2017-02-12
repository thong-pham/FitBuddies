package com.example.juliethjaramillo.fitbuddies;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class home extends AppCompatActivity {
    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = true;

    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

    /**
     * Some older devices needs a small delay between UI widget updates
     * and a change of the status and navigation bar.
     */
    private static final int UI_ANIMATION_DELAY = 300;
    private final Handler mHideHandler = new Handler();
    private View mContentView;
    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            // Delayed removal of status and navigation bar

            // Note that some of these constants are new as of API 16 (Jelly Bean)
            // and API 19 (KitKat). It is safe to use them, as they are inlined
            // at compile-time and do nothing on earlier devices.
            mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    };
    private View mControlsView;
    private final Runnable mShowPart2Runnable = new Runnable() {
        @Override
        public void run() {
            // Delayed display of UI elements
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.show();
            }
            mControlsView.setVisibility(View.VISIBLE);
        }
    };
    private boolean mVisible;
    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {

        }
    };
    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */
    private final View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.home_menu);

        mVisible = true;
       // mControlsView = findViewById(R.id.fullscreen_content_controls);
    //    mContentView = findViewById(R.id.fullscreen_content);


        // Set up the user interaction to manually show or hide the system UI.
      //  mContentView.setOnClickListener(new View.OnClickListener() {
          //  @Override
          //  public void onClick(View view) {
                toggle();
        user userinfo= new user();

           // }
     //   });

        // Upon interacting with UI controls, delay any scheduled hide()
        // operations to prevent the jarring behavior of controls going away
        // while interacting with the UI.
      //  findViewById(R.id.dummy_button).setOnTouchListener(mDelayHideTouchListener);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        getDetails(1);
        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
    }

    private void getDetails(int id) {
        setMyPicture(id);

        AsyncTask<Integer, Void, user> userDetailTask = new AsyncTask<Integer, Void, user>() {
            @Override
            protected user doInBackground(Integer... params) {
                try {
                    return user.get(params[0]);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(user user) {
                gotUserDetails(user);
            }
        };

        userDetailTask.execute(id);

        AsyncTask<Integer, Void, Points> userPointsTask = new AsyncTask<Integer, Void, Points>() {
            @Override
            protected Points doInBackground(Integer... params) {
                try {
                    return Points.get(params[0]);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Points points) {
                gotOwnPoints(points);
            }
        };

        userPointsTask.execute(id);

        AsyncTask<Integer, Void, BuddyInformation> userBuddyTask = new AsyncTask<Integer, Void, BuddyInformation>() {
            int currentUser;

            @Override
            protected BuddyInformation doInBackground(Integer... params) {
                try {
                    currentUser = params[0];
                    return BuddyInformation.get(currentUser);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(BuddyInformation buddyInformation) {
                gotBuddyInformation(currentUser,buddyInformation);
            }
        };

        userBuddyTask.execute(id);

    }

    private void gotBuddyInformation(int currentUser, BuddyInformation buddyInformation) {
        int buddy = (currentUser == buddyInformation.id1)?buddyInformation.id2:buddyInformation.id1;

        setBuddyPicture(buddy);

        AsyncTask<Integer, Void, user> buddyDetailTask = new AsyncTask<Integer, Void, user>() {
            @Override
            protected user doInBackground(Integer... params) {
                try {
                    return user.get(params[0]);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(user user) {
                gotBuddyDetails(user);
            }
        };

        buddyDetailTask.execute(buddy);

        AsyncTask<Integer, Void, Points> buddyPointsTask = new AsyncTask<Integer, Void, Points>() {
            @Override
            protected Points doInBackground(Integer... params) {
                try {
                    return Points.get(params[0]);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Points points) {
                gotBuddyPoints(points);
            }
        };

        buddyPointsTask.execute(buddy);

    }

    private void gotOwnPoints(Points points) {
        TextView point = (TextView) findViewById(R.id.pu);
        point.setText(Integer.toString(points.points));
    }

    private void gotBuddyPoints(Points points) {
        TextView point = (TextView) findViewById(R.id.pb);
        point.setText(Integer.toString(points.points));
    }


    private void gotUserDetails(user user) {
        if(user == null){
            Toast.makeText(this, "Excuse our mess, our server is apparently out for a coffee break.", Toast.LENGTH_SHORT).show();
        }

        TextView name = (TextView) findViewById(R.id.nameu);
        name.setText("" + user.firstname+ " " + user.lastname + "");

        TextView age = (TextView) findViewById(R.id.au);
        age.setText(Long.toString(((System.currentTimeMillis() / 1000L) - user.dob)/(24*365*60*60)));

    }

    private void gotBuddyDetails(user user) {
        if(user == null){
            Toast.makeText(this, "Excuse our mess, our server is apparently out for a coffee break.", Toast.LENGTH_SHORT).show();
        }

        TextView name = (TextView) findViewById(R.id.nameb);
        name.setText("" + user.firstname+ " " + user.lastname + "");

        TextView age = (TextView) findViewById(R.id.agebud);
        age.setText(Long.toString(((System.currentTimeMillis() / 1000L) - user.dob)/(24*365*60*60)));

    }

    private void setMyPicture(int id) {
        new DownloadImageTask((ImageButton) findViewById(R.id.imageButton1))
                .execute(config.baseUrl + "/getPicture/" + id + ".jpg/");
    }

    private void setBuddyPicture(int id) {
        new DownloadImageTask((ImageButton) findViewById(R.id.imageButton2))
                .execute(config.baseUrl + "/getPicture/" + id + ".jpg/");
    }


    private void toggle() {

    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageButton bmImage;

        public DownloadImageTask(ImageButton bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }

}
