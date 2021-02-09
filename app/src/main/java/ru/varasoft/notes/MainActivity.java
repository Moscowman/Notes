package ru.varasoft.notes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;

import ru.varasoft.notes.data.Publisher;
import ru.varasoft.notes.ui.Navigation;
import ru.varasoft.notes.ui.NoteFragment;
import ru.varasoft.notes.ui.NotesListFragment;
import ru.varasoft.notes.ui.SettingsFragment;

public class MainActivity extends AppCompatActivity {

    NotesListFragment notesListFragment;

    private Navigation navigation;
    private Publisher publisher = new Publisher();

    String NOTES_LIST_FRAGMENT = "NotesListFragment";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        navigation = new Navigation(getSupportFragmentManager());
        Toolbar toolbar = initToolbar();
        if (savedInstanceState == null) {
            notesListFragment = NotesListFragment.newInstance();
            getNavigation().addFragment(notesListFragment, false);
        }
        initDrawer(toolbar);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        fragmentTransaction.commit();
    }

    private Toolbar initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        return toolbar;
    }

    private void initDrawer(Toolbar toolbar) {
        final DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (navigateFragment(id)) {
                    drawer.closeDrawer(GravityCompat.START);
                    return true;
                }
                return false;
            }
        });
    }

    private boolean navigateFragment(int id) {
        switch (id) {
            case R.id.action_notes_list:
                getNavigation().addFragment(notesListFragment, false);
                return true;
            case R.id.action_settings:
                getNavigation().addFragment(new SettingsFragment(), true);
                return true;
        }
        return false;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public Navigation getNavigation() {
        return navigation;
    }

    public Publisher getPublisher() {
        return publisher;
    }
}