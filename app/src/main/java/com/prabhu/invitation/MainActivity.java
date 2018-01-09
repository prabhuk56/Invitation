package com.prabhu.invitation;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.prabhu.invitation.Fragments.InviteFragment;
import com.prabhu.invitation.base.BaseActivity;

import java.util.Calendar;

import io.fabric.sdk.android.Fabric;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
public class MainActivity extends BaseActivity {

    private Toolbar toolbar;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private String geoLatitude;
    private String geoLongitude;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/great_vibes.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );

        //setContentView(R.layout.activity_main);
        setTitle("You're Specially Invited");
        setContentView(R.layout.activity_navigation);
        setNewFragment(new InviteFragment(), false);
        //initializing Toolbar
        toolbar=(Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        // Initializing NavigationView
        navigationView=(NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                        if (menuItem.isChecked()) {
                            menuItem.setChecked(false);
                        } else {
                            menuItem.setChecked(true);
                        }

                        //Closing drawer on item click
                        drawerLayout.closeDrawers();

                        //Check to see which item was being clicked and perform appropriate action
                        switch (menuItem.getItemId()) {
                            //Replacing the main content with InboxFragment Which is our Inbox View;
                            case R.id.invite_wedding:
                                Toast.makeText(getApplicationContext(),"Invitation loaded",Toast.LENGTH_SHORT).show();
                                setNewFragment(new InviteFragment(),false);
                                break;
                            case R.id.map_reception:
                                Toast.makeText(getApplicationContext(),"Loading Directions for Reception",Toast.LENGTH_LONG).show();
                                geoLatitude="13.0509334";
                                geoLongitude="80.2147134";
                                launchGoogleMaps(geoLatitude,geoLongitude);
                                break;
                            case R.id.map_wedding:
                                Toast.makeText(getApplicationContext(), "Loading Directions for Wedding", Toast.LENGTH_SHORT).show();
                                geoLatitude="9.4544263";
                                geoLongitude="77.5551582";
                                launchGoogleMaps(geoLatitude,geoLongitude);
                                break;
                            case R.id.id_save_date:
                                Toast.makeText(getApplicationContext(),"Save Date Launch",Toast.LENGTH_SHORT).show();
                                createReminder("Wedding","V.MuthuRaj weds K.Janani",2018,02,04,06,00,2018,02,04,10,00);
                                createReminder("Reception","V.MuthuRaj weds K.Janani",2018,02,9,18,00,2018,02,04,21,00);
                                break;
                            default:
                                setNewFragment(new InviteFragment(),false);
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

                    @Override public void onDrawerClosed(View drawerView) {
                        // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                        super.onDrawerClosed(drawerView);
                    }

                    @Override public void onDrawerOpened(View drawerView) {
                        // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank

                        super.onDrawerOpened(drawerView);
                    }
                };

        //Setting the actionbarToggle to drawer layout
        drawerLayout.setDrawerListener(actionBarDrawerToggle);

        //calling sync state is necessay or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();
    }
    @Override public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
       /* getMenuInflater().inflate(R.menu.activity_navigation_drawer, menu);
        return true;*/
       return false;
    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {
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
    public void launchGoogleMaps(String latitude,String longitude){
        // Creates an Intent that will load a map of San Francisco
        String geoURI="google.navigation:q="+geoLatitude+","+geoLongitude;
        Uri gmmIntentUri = Uri.parse(geoURI);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);


    }
    public void createReminder(String title,String description,int startYear, int startMonth, int startDay, int startHour, int startMinut, int endYear, int endMonth, int endDay, int endHour, int endMinuts){
        Calendar beginTime = Calendar.getInstance();
        beginTime.set(startYear, startMonth, startDay, startHour, startMinut);
        long startMillis = beginTime.getTimeInMillis();

        Calendar endTime = Calendar.getInstance();
        endTime.set(endYear, endMonth, endDay, endHour, endMinuts);
        long endMillis = endTime.getTimeInMillis();

        String eventUriString = "content://com.android.calendar/events";
        ContentValues eventValues = new ContentValues();

        eventValues.put(CalendarContract.Events.CALENDAR_ID, 1);
        eventValues.put(CalendarContract.Events.TITLE, title);
        eventValues.put(CalendarContract.Events.DESCRIPTION, description);
        eventValues.put(CalendarContract.Events.EVENT_TIMEZONE, "IST");
        eventValues.put(CalendarContract.Events.DTSTART, startMillis);
        eventValues.put(CalendarContract.Events.DTEND, endMillis);

        //eventValues.put(Events.RRULE, "FREQ=DAILY;COUNT=2;UNTIL="+endMillis);
        eventValues.put("eventStatus", 1);
        //eventValues.put("visibility", 3);
        //eventValues.put("transparency", 0);
        eventValues.put(CalendarContract.Events.HAS_ALARM, 1);

        Uri eventUri = getContentResolver().insert(Uri.parse(eventUriString), eventValues);
        long eventID = Long.parseLong(eventUri.getLastPathSegment());

        /***************** Event: Reminder(with alert) Adding reminder to event *******************/

        String reminderUriString = "content://com.android.calendar/reminders";

        ContentValues reminderValues = new ContentValues();
        reminderValues.put("event_id", eventID);
        reminderValues.put("minutes", 1);
        reminderValues.put("method", 1);

        Uri reminderUri = getContentResolver().insert(Uri.parse(reminderUriString), reminderValues);
    }
}
