package kultinov.kirill.finalprojectceg6440kultinov;

import android.util.Log;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

/**
 * Model class is used for creation and manipulating the data model
 * Created by kirill on 12/11/17.
 * this class is a modification of a data model class written by Erik Buck
 * in the recyclerview sample project
 */

public class Model {
    private static Model sModel = new Model();//instance of a shared model
    private ArrayList<Song> mDataSet;

    private String[] songs = {"song1", "song2", "song3", "song4", "song5", "song6", "song7", "song8", "song9", "song10", "song11", "song12"};
    private String[] artists = {"artist1", "artist2", "artist3", "artist4", "artist5", "artist6", "artist7", "artist8", "artist9", "artist10", "artist11", "artist12"};

    /**
     * getSharedModel method is used to get shared data model
     * @return Model object
     */
    public static Model getSharedModel(){
        return sModel;
    }

    /**
     * Model constructor is used to initiallize data set
     * by creating Songs objects and adding them to mDataSet ArrayList
     */
    public Model(){
        mDataSet = new ArrayList<>();
        for(int i = 0; i < 30; i++){
            Song song = new Song("song" + (i+1), "artist" + (i+1), i + 1);
            mDataSet.add(song);
        }
    }

    public int getNumberOfItems(){
        return mDataSet.size();
    }

    /**
     * getNumberOfFavoriteItems method is a getter that
     * gets number of favorite songs in the dataset
     * @return an integer number that represents number of favorite songs
     */
    public int getNumberOfFavoriteItems(){
        int count = 0;
        for(int i = 0; i < mDataSet.size(); i++){
            if(mDataSet.get(i).isFavorite()){
                count++;
            }
        }
        return count;
    }

    public void setRatingAt(int position, float rating){
        mDataSet.get(position).setRating(rating);
    }
    public void setFavoriteAt(int position, boolean value){
        mDataSet.get(position).setFavorite(value);
    }

    public String getSongTitleAt(int position){
        return mDataSet.get(position).getName();
    }
    public String getSongArtistAt(int position){
        return mDataSet.get(position).getArtist();
    }
    public float getRatingAt(int position){
        return mDataSet.get(position).getRating();
    }
    public boolean isFavoriteAt(int position){
        return mDataSet.get(position).isFavorite();
    }
    public int getIdAt(int position){ return mDataSet.get(position).getId(); }

    /**
     * getPositionById method is used to find a position of an element using its ID
     * @param id is an integer number that represents song's ID
     * @return a position of a song in the data set as an integer number
     */
    public int getPositionById(int id){
        int position = 0;
        for(int i = 0; i < mDataSet.size(); i++){
            if(id == mDataSet.get(i).getId()){
                position = i;
            }
        }
        Log.d(TAG, "position of element with id " + id + " is " + position);
        return position;
    }

    /**
     * getFavoritesIDs method is used to find all favorite elements in the data set and
     * return them as an arrayList
     * @return ArrayList containing IDs of songs that have been added to favorites
     */
    public ArrayList<Integer> getFavoritesIDs(){
        ArrayList<Integer> list = new ArrayList<>();
        for(int i = 0; i < mDataSet.size(); i++){
            if(mDataSet.get(i).isFavorite()){
                list.add(mDataSet.get(i).getId());
            }
        }
        Log.d("Favorite IDs are ", "" + list.toString());
        return list;
    }


}
