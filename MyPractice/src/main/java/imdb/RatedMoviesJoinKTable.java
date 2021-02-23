package imdb;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.*;
import imdb.model.movie.Movie;
import imdb.model.ratedmovie.RatedMovie;
import imdb.model.rating.Rating;
import imdb.serializers.MovieCustomSerdes;

public class RatedMoviesJoinKTable {

  public static void main(String[] args) {

    imdb.MovieTopology.createTopics();

    final StreamsBuilder builder = new StreamsBuilder();
    final imdb.MovieRatingJoiner movieRatingJoiner = new imdb.MovieRatingJoiner();

    KStream<String, Movie> movieStream = builder.stream(imdb.MovieTopology.MOVIES_TOPIC,
        Consumed.with(Serdes.String(), MovieCustomSerdes.Movie()))
        .map((key, movie) ->
            new KeyValue<>(movie.getPayload().getTconst(), movie));
    movieStream.to(imdb.MovieTopology.REKEYED_MOVIES_TOPIC,
        Produced.with(Serdes.String(), MovieCustomSerdes.Movie()));

    KTable<String, Movie> movies = builder.table(imdb.MovieTopology.REKEYED_MOVIES_TOPIC,
        Consumed.with(Serdes.String(), MovieCustomSerdes.Movie()));

    KStream<String, Rating> ratingsStream = builder.stream(imdb.MovieTopology.RATINGS_TOPIC,
        Consumed.with(Serdes.String(), MovieCustomSerdes.Rating()))
        .map((key, rating) ->
            new KeyValue<>(rating.getPayload().getTconst(), rating));

    ratingsStream.to(imdb.MovieTopology.REKEYED_RATING_TOPIC,
        Produced.with(Serdes.String(), MovieCustomSerdes.Rating()));

    KStream<String, Rating> ratings = builder.stream(imdb.MovieTopology.REKEYED_RATING_TOPIC,
        Consumed.with(Serdes.String(), MovieCustomSerdes.Rating()));

    KStream<String, RatedMovie> ratedMovies = ratings.join(movies, movieRatingJoiner);
    ratedMovies.print(Printed.toSysOut());

    ratedMovies.to(imdb.MovieTopology.RATED_MOVIES_TOPIC,
        Produced.with(Serdes.String(), MovieCustomSerdes.RatedMovie()));

    Topology topology = builder.build();
    final KafkaStreams streams = new KafkaStreams(topology,
        imdb.MovieTopology.createStreamsConfigProperties("imdb"));
    streams.cleanUp();

    streams.start();

    Runtime.getRuntime().addShutdownHook(new Thread(streams::close));
  }
}
