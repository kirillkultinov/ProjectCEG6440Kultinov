package kultinov.kirill.finalprojectceg6440kultinov;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.ContentValues.TAG;

/**
 * BrowseFragment class is a fragment used to display and
 * handle all the actions associated with teh songs library
 */
public class BrowseFragment extends Fragment {

    protected RecyclerView mRecyclerView;
    protected RecyclerView.LayoutManager mLayoutManager;
    protected BrowseAdapter mAdapter;

    private Parcelable listState;

    private FirebaseUser user;
    private DatabaseReference mDatabase;
    private ImageView logOut;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("ONCREATE FRAGMENT", "CALLED");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState) {
        final View browseFragment = inflater.inflate(R.layout.songs_list_fragment, container, false);
        TextView toolBarTitle = (TextView) getActivity().findViewById(R.id.toolbar_title);
        toolBarTitle.setText("Browse");
        Log.d("ONCREATEVIEW FRAGMENT", "CALLED");
        //initialize logOut button
        logOut = (ImageView)getActivity().findViewById(R.id.toolbar_right_imgView);
        logOut.setImageResource(R.drawable.ic_exit_to_app_black_24dp);
        logOut.setVisibility(View.GONE);
        //save the state of the recyclerview
        if(savedInstanceState != null) {
            listState = savedInstanceState.getParcelable("ListState");
        }

        //initialize recycler view
        mRecyclerView = (RecyclerView) browseFragment.findViewById(R.id.recyclerSongsList);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        //get current user
        user = FirebaseAuth.getInstance().getCurrentUser();

        //update the data model if the user is logged in
        //set adapter otherwise without changing the data model
        if(user != null){
            updateModel();
        }else{
            setAdapter();
        }
        //retrieve the state ofhte recycler view
        if(savedInstanceState != null) {
            mRecyclerView.getLayoutManager().onRestoreInstanceState(listState);
        }

        return browseFragment;
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putParcelable("ListState", mRecyclerView.getLayoutManager().onSaveInstanceState());
    }

    /**
     * updateModel method is used for retrieving all values from database
     * and updating the local data model based on these values
     */
    public void updateModel(){
        //update data model based on saved favorite songs in the database
        Log.d("INSIDE UPDATEMODEL", "checking user");
        Log.d("UPDATING DATA MODEL", "user is not null");
        mDatabase = FirebaseDatabase.getInstance().getReference().child(user.getUid()).child("songs");

        //get all values from database
        mDatabase.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        //Get map of songs in datasnapshot
                        for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
                            Model.getSharedModel().setFavoriteAt(Integer.parseInt(childDataSnapshot.getKey()) - 1, true);
                            String rating = childDataSnapshot.child("rating").getValue().toString();
                            //update songs data in the local data model
                            Model.getSharedModel().setRatingAt(Integer.parseInt(childDataSnapshot.getKey()) - 1, Float.parseFloat(rating));
                        }
                        setAdapter();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });

    }

    /**
     * setAdapter method is used for setting an adapter for the recycler view
     */
    public void setAdapter(){
        mAdapter = new BrowseAdapter(getContext());
        mRecyclerView.setAdapter(mAdapter);
    }


}
