package streams.movies;

import org.apache.kafka.streams.kstream.ValueJoiner;
import streams.movies.model.Movie;
import streams.movies.model.RatedMovie;
import streams.movies.model.Rating;

public class MovieRatingJoiner implements ValueJoiner<Rating, Movie, RatedMovie> {

  @Override
  public RatedMovie apply(Rating rating, Movie movie) {
    return new RatedMovie(movie.getId(), movie.getTitle(), movie.getReleaseYear(),
        rating.getRating());
  }
}
