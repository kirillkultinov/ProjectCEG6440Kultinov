package kultinov.kirill.finalprojectceg6440kultinov;

/**
 * Song class is used to represent a song object
 * Created by kirill on 12/11/17.
 */

public class Song {



    private String name;
    private String artist;
    private int id;
    private float rating;
    private boolean favorite;

    public Song(){
        this.name = "";
        this.artist = "";
        this.id = 0;
        this.rating = 0;
        this.favorite = false;
    }

    public Song(String name, String artist, int id){
        this.name = name;
        this.artist = artist;
        this.id = id;
        this.rating = 0;
        this.favorite = false;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getArtist() {
        return artist;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

}
