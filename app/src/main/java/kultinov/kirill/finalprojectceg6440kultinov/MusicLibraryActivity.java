package kultinov.kirill.finalprojectceg6440kultinov;

import android.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MusicLibraryActivity extends AppCompatActivity {

    TextView toolbarTitle;
    BottomNavigationView bottomNavigationView;
    FirebaseUser user;

    private BrowseFragment browseFragment;
    private YourLibraryFragment yourLibraryFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_library);
        toolbarTitle = (TextView) findViewById(R.id.toolbar_title);
        toolbarTitle.setText("Browse");

        browseFragment = new BrowseFragment();
        yourLibraryFragment = new YourLibraryFragment();

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);
        //bottomNavigationView.setSelectedItemId(R.id.navigation_browse);
        Menu menu = bottomNavigationView.getMenu();
        selectFragment(menu.getItem(0));

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        selectFragment(item);
                        return false;
                    }
                });


    }


    /**
     * Perform action when any item is selected.
     *
     * @param item Item that is selected.
     */
    protected void selectFragment(MenuItem item) {

        item.setChecked(true);

        switch (item.getItemId()) {
            case R.id.navigation_browse:
                // Action to perform when Home Menu item is selected.
                pushFragment(browseFragment);
                break;
            case R.id.navigation_your_lib:
                // Action to perform when Bag Menu item is selected.
                user = FirebaseAuth.getInstance().getCurrentUser();
                if(user == null){
                    Log.d("Authentication", "user is not logged in");
                    pushFragment(new LoginFragment());
                }else{
                    Log.d("Authentication", "user logged in");
                    Log.d("Authentication id", user.getUid());
                    pushFragment(yourLibraryFragment);
                }

                break;
        }
    }

    /**
     * Method to push any fragment into given id.
     *
     * @param fragment An instance of Fragment to show into the given id.
     */
    protected void pushFragment(Fragment fragment) {
        if (fragment == null)
            return;

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.songsLayout, fragment);
        transaction.commit();
    }



    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}
