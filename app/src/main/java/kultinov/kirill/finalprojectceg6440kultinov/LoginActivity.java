package kultinov.kirill.finalprojectceg6440kultinov;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.security.GeneralSecurityException;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * LoginActivity is a start up actvity of the app that is used to hold login fragment
 */
public class LoginActivity extends AppCompatActivity {

    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //set initial view of the activity
        FirebaseApp.initializeApp(this);
        user = FirebaseAuth.getInstance().getCurrentUser();
        /**
         * check if the user is logged in
         * if it is true then open up MusicLibrary activity to display songs library
         */
        if(user != null){
            //intent for opening MusicLibrary activity
            Intent i = new Intent(this, MusicLibraryActivity.class);
            startActivity(i);
        }else{
            if (savedInstanceState == null) {
                //display log in fragment if the user is not logged in
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                LoginFragment fragment = new LoginFragment();
                transaction.replace(R.id.login_fragment, fragment);
                transaction.commit();
            }
        }



    }


}
