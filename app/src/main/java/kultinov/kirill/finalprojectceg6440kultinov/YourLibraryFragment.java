package kultinov.kirill.finalprojectceg6440kultinov;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static android.content.ContentValues.TAG;


public class YourLibraryFragment extends Fragment {

    protected RecyclerView mRecyclerView;
    protected RecyclerView.LayoutManager mLayoutManager;
    protected YourLibraryAdapter mAdapter;

    private Parcelable listState;

    private FirebaseUser user;
    private DatabaseReference mDatabase;
    private ImageView logOut;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View yourLibraryFragment = inflater.inflate(R.layout.songs_list_fragment, container, false);
        TextView toolBarTitle = (TextView) getActivity().findViewById(R.id.toolbar_title);
        toolBarTitle.setText("Your Library");
        Log.d("parentof lib activity", getActivity().getClass().getSimpleName().toString());
        logOut = (ImageView)getActivity().findViewById(R.id.toolbar_right_imgView);
        logOut.setImageResource(R.drawable.ic_exit_to_app_black_24dp);
        logOut.setVisibility(View.VISIBLE);
        mRecyclerView = (RecyclerView) yourLibraryFragment.findViewById(R.id.recyclerSongsList);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        user = FirebaseAuth.getInstance().getCurrentUser();

        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
                alertDialog.setMessage("Do you want to log out?");
                alertDialog.setCancelable(true);

                alertDialog.setPositiveButton(
                        "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                logOutCleanUp();
                            }
                        });

                alertDialog.setNegativeButton(
                        "No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alert11 = alertDialog.create();
                alert11.show();

            }
        });

        updateModel();

        return yourLibraryFragment;
    }

    public void updateModel(){
        //update data model based on saved favorite songs in the database
        Log.d("INSIDE UPDATEMODEL", "checking user");
        Log.d("UPDATING DATA MODEL", "user is not null");
        mDatabase = FirebaseDatabase.getInstance().getReference().child(user.getUid()).child("songs");
        mDatabase.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        //Get map of users in datasnapshot
                        for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
                            Model.getSharedModel().setFavoriteAt(Integer.parseInt(childDataSnapshot.getKey()) - 1, true);
                            Log.v(TAG,"UPDATED MODEL FOR SONG ID: "+ childDataSnapshot.getKey());
                            Log.v(TAG,"rating of the song is: "+ childDataSnapshot.child("rating").getValue());
                            String rating = childDataSnapshot.child("rating").getValue().toString();
                            Model.getSharedModel().setRatingAt(Integer.parseInt(childDataSnapshot.getKey()) - 1, Float.parseFloat(rating));
                        }
                        setAdapter();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        //handle databaseError
                    }
                });

    }

    public void setAdapter(){
        mAdapter = new YourLibraryAdapter(getContext());
        Log.d("Adapter", "adapter is ready to be set");
        mRecyclerView.setAdapter(mAdapter);
        Log.d("Adapter", "adapter is set");
    }

    public void logOutCleanUp(){
        FirebaseAuth.getInstance().signOut();
        for(int i = 0; i < Model.getSharedModel().getNumberOfItems(); i++){
            Model.getSharedModel().setFavoriteAt(i, false);
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            LoginFragment fragment = new LoginFragment();
            transaction.replace(R.id.songsLayout, fragment);
            transaction.commit();
            logOut.setVisibility(View.GONE);
        }
    }

}
