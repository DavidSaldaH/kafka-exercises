package streams.movies.model;

import java.io.Serializable;
import java.util.ArrayList;

public class Movie implements Serializable {

  private String tconst;
  private String titleType;
  private String primaryTitle;
  private String originalTitle;
  private Boolean isAdult;
  private String startYear;
  private String endYear;
  private String runtimeMinutes;
  private ArrayList<String> genres;

  public Movie() {}

  public Movie(String tconst, String titleType, String primaryTitle, String originalTitle, Boolean isAdult, String startYear, String endYear, String runtimeMinutes, ArrayList<String> genres) {
    this.tconst = tconst;
    this.titleType = titleType;
    this.primaryTitle = primaryTitle;
	this.originalTitle = originalTitle;
	this.isAdult = isAdult;
	this.startYear = startYear;
	this.endYear = endYear;
	this.runtimeMinutes = runtimeMinutes;
	this.genres = new ArrayList<String>();
  }

  public String getTconst() {
    return tconst;
  }

  public void setTconst(String tconst) {
    this.tconst = tconst;
  }

  public String getTitleType() {
    return titleType;
  }

  public void setTitleType(String titleType) {
    this.titleType = titleType;
  }

  public String getPrimaryTitle() {
    return primaryTitle;
  }

  public void setPrimaryTitle(String primaryTitle) {
    this.primaryTitle = primaryTitle;
  }
  
  public String getOriginalTitle() {
    return originalTitle;
  }

  public void setOriginalTitle(String originalTitle) {
    this.originalTitle = originalTitle;
  }
  
  public Boolean getIsAdult() {
    return isAdult;
  }

  public void setIsAdult(Boolean isAdult) {
    this.isAdult = isAdult;
  }
  
  public String getStartYearMovie() {
    return startYear;
  }

  public void setStartYearMovie(String startYear) {
    this.startYear = startYear;
  }
  
  public String getEndYearMovie() {
    return endYear;
  }

  public void setStartYear(String endYear) {
    this.endYear = endYear;
  }
  
  public String getRuntimeMinutes() {
    return runtimeMinutes;
  }

  public void setRuntimeMinutes(String runtimeMinutes) {
    this.runtimeMinutes = runtimeMinutes;
  }
  
   public ArrayList<String> getGenres() {
    return genres;
  }

  public void setGenres(ArrayList<String> genres) {
    this.genres = genres;
  }

//  @Override
//  public String toString() {
//    return "Movie{" +
//        "id=" + id +
//        ", title=" + title +
//        ", releaseYear=" + releaseYear +
//        '}';
//  }
}
