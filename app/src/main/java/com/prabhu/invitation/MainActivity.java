package com.prabhu.invitation;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.prabhu.invitation.Fragments.InviteFragment;
import com.prabhu.invitation.base.BaseActivity;

import java.util.Calendar;
import java.util.TimeZone;

import io.fabric.sdk.android.Fabric;

import static android.Manifest.permission.READ_CALENDAR;
import static android.Manifest.permission.WRITE_CALENDAR;

public class MainActivity extends BaseActivity {

    private Toolbar toolbar;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private String geoLatitude;
    private String geoLongitude;
    public static final int PERMISSION_REQUEST_CODE=200;
    // [START declare_analytics]
    private FirebaseAnalytics mFirebaseAnalytics;
    // [END declare_analytics]
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        // [START shared_app_measurement]
        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        // [END shared_app_measurement]
        //setContentView(R.layout.activity_main);
        setTitle("You're Specially Invited");
        setContentView(R.layout.activity_navigation);
        setNewFragment(new InviteFragment(), false);
        //initializing Toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        // Initializing NavigationView
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                        if (menuItem.isChecked()) {
                            menuItem.setChecked(true);

                        } else {
                            menuItem.setChecked(false);
                        }

                        //Closing drawer on item click
                        drawerLayout.closeDrawers();

                        //Check to see which item was being clicked and perform appropriate action
                        switch (menuItem.getItemId()) {
                            //Replacing the main content with InboxFragment Which is our Inbox View;
                            case R.id.invite_wedding:
                                setNewFragment(new InviteFragment(), false);
                                mFirebaseAnalytics.setUserProperty("invite_read", "Invite loaded");
                                break;
                            case R.id.map_reception:
                                Snackbar.make(navigationView, "Loading Directions for Reception", Snackbar.LENGTH_LONG).show();
                                geoLatitude = "13.0509334";
                                geoLongitude = "80.2147134";
                                launchGoogleMaps(geoLatitude, geoLongitude);
                                mFirebaseAnalytics.setUserProperty("map_reception", "Location for Reception");
                                break;
                            case R.id.map_wedding:
                                Snackbar.make(navigationView, "Loading Directions for Wedding", Snackbar.LENGTH_LONG).show();
                                geoLatitude = "9.4544263";
                                geoLongitude = "77.5551582";
                                launchGoogleMaps(geoLatitude, geoLongitude);
                                mFirebaseAnalytics.setUserProperty("map_reception", "Location for Wedding");
                                break;
                            case R.id.id_save_date:
                                if (!checkPermission()){
                                    requestPermission();
                                }
                                else{
                                    eventCreation();
                                }
                                mFirebaseAnalytics.setUserProperty("save_date", "Date Saved");
                                break;
                            default:
                                setNewFragment(new InviteFragment(), false);
                                break;

                        }

                        return false;
                    }
                }
        );
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle actionBarDrawerToggle =
                new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.openDrawer,
                        R.string.closeDrawer) {

                    @Override
                    public void onDrawerClosed(View drawerView) {
                        // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                        invalidateOptionsMenu();
                        super.onDrawerClosed(drawerView);

                    }

                    @Override
                    public void onDrawerOpened(View drawerView) {
                        // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank
                        invalidateOptionsMenu();
                        super.onDrawerOpened(drawerView);

                    }
                };

        //Setting the actionbarToggle to drawer layout
        drawerLayout.setDrawerListener(actionBarDrawerToggle);

        //calling sync state is necessay or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
       /* getMenuInflater().inflate(R.menu.activity_navigation_drawer, menu);
        return true;*/
        return false;
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

    public void launchGoogleMaps(String latitude, String longitude) {
        // Creates an Intent that will load a map of San Francisco
        String geoURI = "google.navigation:q=" + geoLatitude + "," + geoLongitude;
        Uri gmmIntentUri = Uri.parse(geoURI);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);


    }
    private void createReminder(String title, String description, int startYear, int startMonth, int startDay, int startHour, int startMinut, int endYear, int endMonth, int endDay, int endHour, int endMinuts,String eventLocation) {
        try{
            long calID = 3;
            long startMillis = 0;
            long endMillis = 0;
            TimeZone timeZone = TimeZone.getDefault();
            Calendar beginTime = Calendar.getInstance();
            beginTime.set(startYear, startMonth, startDay, startHour, startMinut);
            startMillis = beginTime.getTimeInMillis();
            Calendar endTime = Calendar.getInstance();
            endTime.set(endYear, endMonth, endDay, endHour, endMinuts);
            endMillis = endTime.getTimeInMillis();

            ContentResolver contentResolver = getContentResolver();
            ContentValues contentValues = new ContentValues();
            contentValues.put(CalendarContract.Events.DTSTART, startMillis);
            contentValues.put(CalendarContract.Events.DTEND, endMillis);
            contentValues.put(CalendarContract.Events.TITLE, title);
            contentValues.put(CalendarContract.Events.DESCRIPTION, description);
            contentValues.put(CalendarContract.Events.CALENDAR_ID, calID);
            contentValues.put(CalendarContract.Events.EVENT_TIMEZONE, timeZone.getID());
            contentValues.put(CalendarContract.Events.EVENT_LOCATION,eventLocation);
            if (ActivityCompat.checkSelfPermission(this, WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                Toast.makeText(getApplicationContext(),"Calendar permission rejected",Toast.LENGTH_LONG);
                return;
            }
            Uri uri = contentResolver.insert(CalendarContract.Events.CONTENT_URI, contentValues);
            // get the event ID that is the last element in the Uri
            long eventID = Long.parseLong(uri.getLastPathSegment());

        }
        catch (Exception e){
            Crashlytics.log(e.toString());
        }

    }




    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), READ_CALENDAR);
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_CALENDAR);

        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {

        ActivityCompat.requestPermissions(this, new String[]{READ_CALENDAR, WRITE_CALENDAR}, PERMISSION_REQUEST_CODE);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0) {

                    boolean writeCalendar = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean readCalendar = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                    if (writeCalendar && readCalendar) {
                        Snackbar.make(navigationView, "Permission Granted,  Dates will be saved in your calendar!!", Snackbar.LENGTH_LONG).show();
                        eventCreation();
                    }

                    else {

                        Snackbar.make(navigationView, "Permission Denied, Dates will not be saved in your calendar!!", Snackbar.LENGTH_LONG).show();

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (shouldShowRequestPermissionRationale(WRITE_CALENDAR)) {
                                showMessageOKCancel("You need to allow access to both the permissions",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                    requestPermissions(new String[]{WRITE_CALENDAR, READ_CALENDAR},
                                                            PERMISSION_REQUEST_CODE);
                                                }
                                            }
                                        });
                                return;
                            }
                        }

                    }
                }


                break;
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(MainActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    private void eventCreation(){
        String wedLocation=getString(R.string.wedding_hall) + getString(R.string.wedding_addr);
        String recepLocation=getString(R.string.reception_hall) + getString(R.string.reception_addr) +getString(R.string.reception_city);
        createReminder("Wedding Ceremony", "V.MuthuRaj Weds K.Janani", 2018, 1, 4, 06, 00, 2018, 1, 4, 10, 00,wedLocation);
        createReminder("Wedding Reception", "V.MuthuRaj Weds K.Janani", 2018, 1, 9, 18, 00, 2018, 1, 9, 21, 00,recepLocation);
    }

}
