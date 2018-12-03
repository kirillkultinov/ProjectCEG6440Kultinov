package kultinov.kirill.finalprojectceg6440kultinov;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * SongActivity is activity that is used to display song preview
 */
public class SongActivity extends AppCompatActivity {

    TextView songTitle, songArtist, songRating;
    private String title, artist, rating;

    private ImageView goBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song);
        //initialize UI components
        TextView toolBarTitle = (TextView) findViewById(R.id.toolbar_title);
        toolBarTitle.setText("Song preview");

        songTitle = (TextView)findViewById(R.id.txtSongTitle);
        songArtist = (TextView)findViewById(R.id.txtSongArtist);
        songRating = (TextView)findViewById(R.id.txtSongRating);

        //set up go back button
        goBack = (ImageView)findViewById(R.id.toolbar_left_imageView);
        goBack.setImageResource(R.drawable.ic_keyboard_arrow_left_black_24dp);
        goBack.setVisibility(View.VISIBLE);

        Intent i= getIntent();
        Bundle b = i.getExtras();


        //receive songs info from Intent's extras
        if(b!=null)
        {
            title =(String) b.get("title");
            artist =(String) b.get("artist");
            rating = String.valueOf(b.get("rating"));
        }
        //set values of the UI components
        songTitle.setText(title);
        songArtist.setText(artist);
        songRating.setText("Rating: " + rating + "/5.0");
        //goBack button listener
        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               onBackPressed();
            }
        });
    }
}
