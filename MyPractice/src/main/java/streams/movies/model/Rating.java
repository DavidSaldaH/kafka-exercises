package streams.movies.model;

import java.io.Serializable;

public class Rating implements Serializable {

  private String tconst;
  private float averageRating;
  private int numVotes;

  public Rating(String tconst, float averageRating, int numVotes) {
    this.tconst = tconst;
    this.averageRating = averageRating;
	this.numVotes = numVotes;
  }

  public Rating() {}

  public String getTconst() {
    return tconst;
  }

  public void setTconst(String tconst) {
    this.tconst = tconst;
  }

  public float getAverage() {
    return averageRating;
  }

  public void setAverage(float averageRating) {
    this.averageRating = averageRating;
  }
  
   public int getNumVotes() {
    return numVotes;
  }

  public void setNumVotes(int numVotes) {
    this.numVotes = numVotes;
  }

  /*@Override
  public String toString() {
    return "Ratings{" +
        "id='" + id +
        ", averageRating=" + averageRating + ", numVotes=" + numVotes +
        '}';
  }*/
}
