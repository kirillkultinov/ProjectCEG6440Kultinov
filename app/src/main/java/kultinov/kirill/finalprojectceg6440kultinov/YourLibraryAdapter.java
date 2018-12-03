package kultinov.kirill.finalprojectceg6440kultinov;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import static android.content.ContentValues.TAG;

/**
 * YourLibraryAdapter is a class that contains impementation of a recyclerview
 * created in YourLibraryFragment
 * Created by kirill on 12/12/17.
 */

public class YourLibraryAdapter extends RecyclerView.Adapter<YourLibraryAdapter.ViewHolder> {

    Context context;
    FirebaseUser user;
    DatabaseReference mDatabase;

    /**
     * YourLibraryAdapter constructor is used to initialize global  variables of the class
     * @param context
     */
    public YourLibraryAdapter(Context context){
        this.context = context;
        user = FirebaseAuth.getInstance().getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    /**
     * ViewHolder class used to prepare elements of the recycler view
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView mTextViewSong;
        private final TextView mTextViewArtist;
        private final ImageView rate;
        private final ImageView add;


        /**
         * ViewHolder constructor is used to initialize the view of each row of the ViewHolder
         * @param v is a parent view of the ViewHolder
         */
        public ViewHolder(View v) {
            super(v);
            // Click listener for the ViewHolder view
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //get position of a favorite song in the data set
                    final int pos = Model.getSharedModel().getPositionById(Model.getSharedModel().getFavoritesIDs().get(getAdapterPosition()));
                    //switch to a song preview activity
                    Intent i = new Intent(context, SongActivity.class);
                    i.putExtra("title", Model.getSharedModel().getSongTitleAt(pos));
                    i.putExtra("artist", Model.getSharedModel().getSongArtistAt(pos));
                    i.putExtra("rating", Model.getSharedModel().getRatingAt(pos));
                    context.startActivity(i);
                }
            });
            mTextViewSong = (TextView) v.findViewById(R.id.txtSong);
            mTextViewArtist = (TextView) v.findViewById(R.id.txtArtist);
            rate = (ImageView)v.findViewById(R.id.rate);
            add = (ImageView)v.findViewById(R.id.add);
        }

        /**
         * public getters used to return view elements of the given row
         */

        public TextView getTextViewSong() {
            return mTextViewSong;
        }
        public TextView getTextViewArtist() {
            return mTextViewArtist;
        }
        public ImageView getRatingButton() {
            return rate;
        }
        public ImageView getAddButton() {
            return add;
        }
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view.
        Log.v(TAG,"ON CREATE VIEW HOLDER ");
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.recycler_view_row, viewGroup, false);
        return new ViewHolder(v);
    }


    /**
     * onBindViewHolder method is used to set the view of each row
     * Essentially, this is a controller that maps values from a data model to
     * each row in the recycler view
     * @param viewHolder
     * @param position
     */
    @Override
    public void onBindViewHolder(YourLibraryAdapter.ViewHolder viewHolder, final int position) {
            //get position of a favorite song in the data set
            final int pos = Model.getSharedModel().getPositionById(Model.getSharedModel().getFavoritesIDs().get(position));
            // Get element from your dataset at this position and replace the contents of the view
            // with that element
            viewHolder.getTextViewSong().setText(
                    Model.getSharedModel().getSongTitleAt(pos));
            viewHolder.getTextViewArtist().setText(
                    Model.getSharedModel().getSongArtistAt(pos));

            viewHolder.getAddButton().setImageResource(R.drawable.ic_check_black_24dp);

            viewHolder.getRatingButton().setImageResource(R.drawable.ic_star_black_24dp);

            //rating button on click listener
            viewHolder.getRatingButton().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view) {
                    //initialize and show custom rate alert dialog for rating a song
                    final Dialog alertDialog = new Dialog(context);
                    alertDialog.setTitle("Rate Song");
                    alertDialog.setContentView(R.layout.rating_alert);
                    final RatingBar rb = (RatingBar) alertDialog.findViewById(R.id.ratingBar);
                    rb.setRating(Model.getSharedModel().getRatingAt(pos));
                    rb.setStepSize(0.5f);
                    rb.setNumStars(5);
                    Button btnRate = (Button) alertDialog.findViewById(R.id.btnRate);
                    btnRate.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //update data model based on set rating for a particular song
                            Model.getSharedModel().setRatingAt(pos, rb.getRating());
                            //update rating value in the database if the song is marked as a favorite
                            mDatabase.child(user.getUid()).child("songs").child(String.valueOf(Model.getSharedModel().getIdAt(pos)))
                                    .child("rating").setValue(Model.getSharedModel().getRatingAt(pos));
                            alertDialog.dismiss();
                        }
                    });

                    alertDialog.show();
                }
            });

            //delete song from favorites button on click listener
            viewHolder.getAddButton().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view) {
                    //delete action
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                    alertDialog.setMessage("Do you want to delete this song from favorites");
                    alertDialog.setCancelable(true);

                    alertDialog.setPositiveButton(
                            "Yes",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    mDatabase.child(user.getUid()).child("songs").child(String.valueOf(pos + 1)).removeValue();
                                    Model.getSharedModel().setFavoriteAt(pos, false);
                                    notifyDataSetChanged();
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


    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        Log.d(TAG, "dataset Length" + Model.getSharedModel().getNumberOfFavoriteItems());
        return Model.getSharedModel().getNumberOfFavoriteItems();
    }

}
