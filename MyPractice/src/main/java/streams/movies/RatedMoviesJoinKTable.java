package streams.movies;

import streams.movies.model.Movie;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.*;
import streams.movies.MovieRatingJoiner;
import streams.movies.MovieTopology;
import streams.movies.model.RatedMovie;
import streams.movies.model.Rating;
import streams.movies.serializers.MovieCustomSerdes;

public class RatedMoviesJoinKTable {

  public static void main(String[] args) {

    org.ogomez.practica.movies.MovieTopology.createTopics();

    final StreamsBuilder builder = new StreamsBuilder();
    final streams.movies.MovieRatingJoiner movieRatingJoiner = new MovieRatingJoiner();

    KStream<String, Movie> movieStream = builder.stream("topic-movies",
        Consumed.with(Serdes.String(), MovieCustomSerdes.Movie()))
        .map((key, movie) ->
            new KeyValue<>(movie.getTconst(), movie));
    movieStream.to(streams.movies.MovieTopology.REKEYED_MOVIES_TOPIC,
        Produced.with(Serdes.String(), MovieCustomSerdes.Movie()));

    KTable<String, Movie> movies = builder.table(org.ogomez.practica.movies.MovieTopology.REKEYED_MOVIES_TOPIC,
        Consumed.with(Serdes.String(), MovieCustomSerdes.Movie()));

    KStream<String, Rating> ratings = builder.stream("topic-ratings",
        Consumed.with(Serdes.String(), MovieCustomSerdes.Rating()));

    KStream<String, RatedMovie> ratedMovies = ratings.join(movies, movieRatingJoiner);
    ratedMovies.print(Printed.toSysOut());
    ratedMovies.to(org.ogomez.practica.movies.MovieTopology.RATED_MOVIES_TOPIC,
        Produced.with(Serdes.String(), MovieCustomSerdes.RatedMovie()));

    Topology topology = builder.build();
    final KafkaStreams streams = new KafkaStreams(topology,
        MovieTopology.createStreamsConfigProperties("moviesJoin"));
    streams.cleanUp();

    streams.start();

    Runtime.getRuntime().addShutdownHook(new Thread(streams::close));
  }


}
