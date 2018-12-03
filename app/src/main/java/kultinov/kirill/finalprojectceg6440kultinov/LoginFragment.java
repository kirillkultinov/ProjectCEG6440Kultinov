package kultinov.kirill.finalprojectceg6440kultinov;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.security.GeneralSecurityException;
import java.util.concurrent.Executor;

import static android.content.ContentValues.TAG;

/**
 * LoginFragment is a fragment that is used for handling
 * displaying elements required for firebase authentication process
 * and authentication implementation
 */
public class LoginFragment extends Fragment {

    private TextInputLayout layoutLogin, layoutPwd;
    private EditText login, password;
    private Button signin, skip;

    private FirebaseAuth mAuth;

    ProgressDialog progressDialog;

    private FirebaseUser user;
    private DatabaseReference mDatabase;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //initialize the view and its elements
        final View loginFragment = inflater.inflate(R.layout.login_fragment, container, false);
        TextView toolBarTitle = (TextView) getActivity().findViewById(R.id.toolbar_title);
        toolBarTitle.setText("Sign In");
        login = (EditText)loginFragment.findViewById(R.id.userLogin);
        password = (EditText)loginFragment.findViewById(R.id.userPassword);
        signin = (Button)loginFragment.findViewById(R.id.btnLogin);
        skip = (Button)loginFragment.findViewById(R.id.btnLater);
        //hide skip button if the parent activity is MusicLibraryActivity
        if(getActivity().getClass().getSimpleName().toString().equals("MusicLibraryActivity")){
            skip.setVisibility(View.GONE);
        }
        //get instance of firebase authentication
        mAuth = FirebaseAuth.getInstance();
        //go to music library without logging in
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), MusicLibraryActivity.class);
                startActivity(i);
            }
        });

        //login attempt
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    login();
                } catch (GeneralSecurityException e){
                    e.printStackTrace();
                }
            }
        });
        return loginFragment;
    }

    /**
     * login method is used to handle authentication logic
     * @throws GeneralSecurityException
     */
    public void login() throws GeneralSecurityException {
        final String userName = login.getText().toString();
        final String userPassword = password.getText().toString();
        //do a basic check of entered login credentials
        if(!validateCredentials(userName, userPassword)){
            return;
        }
        //create a progress dialog while the authentication is in progress
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("Signing in");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
        progressDialog.show(); // Display Progress Dialog
        progressDialog.setCancelable(false);
        //create a thread for checking user credentials in the database
        new Thread(new Runnable() {
            public void run() {
                try {
                    fireBaseSignIn(userName, userPassword);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();


    }

    /**
     * fireBaseSignIn method is used to authenticate a user by
     * checking entered login credentials with the database
     * @param userName is a parameter that represents a user name as a string
     * @param userPassword is a parameter that represents a user password as a string
     */
    private void fireBaseSignIn(String userName, String userPassword){


        mAuth.signInWithEmailAndPassword(userName, userPassword)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            //open MusicLibrary activity if the parent activity is LoginActivity
                            // open your library fragment otherwise
                            if(!getActivity().getClass().getSimpleName().toString().equals("MusicLibraryActivity")) {
                                Log.d("LOGIN SUCCESSFUL", "starting music lib activity");
                                Intent i = new Intent(getActivity(), MusicLibraryActivity.class);
                                startActivity(i);
                            }else{
                                Log.d("LOGIN SUCCESSFUL", "opening your lib fragment");
                                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                                    YourLibraryFragment fragment = new YourLibraryFragment();
                                    transaction.replace(R.id.songsLayout, fragment);
                                    transaction.commit();

                            }
                            progressDialog.dismiss();

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.d("FIREBASEku", "onComplete: Failed=" + task.getException().getMessage());
                            Toast.makeText(getActivity(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    }
                });
    }

    /**
     * validateCredentials method is used to do a basic checking of entered credentials
     * @param userName is a parameter that represents user name as a string
     * @param userPassword is a parameter that represents user password as a string
     * @return boolean value that represents if the login credentials are valid or not
     */
    private boolean validateCredentials(String userName, String userPassword){

        if(userName.isEmpty()){
            Toast.makeText(getActivity(), "Enter userName",
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        if(userPassword.isEmpty() || userPassword.length() < 4){
            Toast.makeText(getActivity(), "Password is too short",
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }







}
