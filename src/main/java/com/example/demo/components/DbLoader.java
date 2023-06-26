package com.example.demo.components;

import com.example.demo.dao.*;
import com.example.demo.data.*;
import com.example.demo.services.Documentation;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.io.StringReader;
import java.net.URL;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.util.regex.Pattern;

@Component
public class DbLoader implements CommandLineRunner {
    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private DirectorRepository directorRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private Documentation documentation;

    private void loadBooks(String firstName, String lastName) throws Exception {
        String query = String.format("""
                    SELECT DISTINCT ?author ?bookLabel ?bookDescription ?authorLabel ?genreLabel ?release WHERE {
                        SERVICE wikibase:label { bd:serviceParam wikibase:language "fr". }
                        {
                            SELECT DISTINCT ?book ?author ?genre ?release WHERE {
                                 ?book wdt:P50 ?author.
                                 ?book wdt:P136 ?genre.
                                 ?book wdt:P577 ?release.
                                 ?author wdt:P735 ?gn; wdt:P734 ?fn.
                                 ?gn rdfs:label "%s"@fr.
                                 ?fn rdfs:label "%s"@fr.
                            } LIMIT 100
                        }
                    }
                """, firstName, lastName);
        HttpClient client = HttpClient.newBuilder().followRedirects(HttpClient.Redirect.NORMAL).build();
        URL url = new URL("https://query.wikidata.org/sparql?query=" + URLEncoder.encode(query, "UTF-8"));
        HttpRequest request = HttpRequest.newBuilder().GET().uri(url.toURI()).header("Accept", "application/json").build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        JsonObject res = JsonParser.parseReader(new StringReader(response.body())).getAsJsonObject().getAsJsonObject("results");
        JsonArray bindings = res.getAsJsonArray("bindings");
        Pattern p = Pattern.compile("Q\\d+");

        for (int i = 0; i < bindings.size(); i++) {
            JsonObject obj = (JsonObject) bindings.get(i);
            String title = obj.getAsJsonObject("bookLabel").getAsJsonPrimitive("value").getAsString();
            if (p.matcher(title).matches()) continue;
            JsonObject d = obj.getAsJsonObject("bookDescription");
            String description = "No description";
            if (d != null)
                description = d.getAsJsonPrimitive("value").getAsString();
            String genre = obj.getAsJsonObject("genreLabel").getAsJsonPrimitive("value").getAsString();
            String release = obj.getAsJsonObject("release").getAsJsonPrimitive("value").getAsString();

            Genre g = genreRepository.findByName(genre);
            if (g == null) g = genreRepository.save(new Genre(-1, genre));

            Author author = authorRepository.findByFirstNameAndLastName(firstName, lastName);
            if (author == null) author = authorRepository.save(new Author(-1, firstName, lastName));

            int year = Integer.parseInt(release.substring(0, 4));
            int month = Integer.parseInt(release.substring(5, 7));
            int day = Integer.parseInt(release.substring(8, 10));

            Book b = bookRepository.findByName(title);
            System.out.println(b);
            System.out.println(author);
            if (b == null) bookRepository.save(new Book(-1, title, LocalDate.of(year, month, day), description, g, author));
        }
    }

    private void loadMovies(String firstName, String lastName) throws Exception {
        String query = String.format("""
                    SELECT DISTINCT ?movieLabel ?movieDescription ?directorLabel ?genreLabel ?release ?duration WHERE {
                        SERVICE wikibase:label { bd:serviceParam wikibase:language "fr". }
                        {
                            SELECT DISTINCT ?movie ?director ?genre ?release ?duration WHERE {
                                ?movie wdt:P57 ?director; wdt:P136 ?genre; wdt:P577 ?release.
                                ?director wdt:P735 ?gn; wdt:P734 ?fn.
                                ?gn rdfs:label "%s"@en.
                                ?fn rdfs:label "%s"@en.
                                ?movie wdt:P2047 ?duration.
                            } LIMIT 500
                        }
                    }
                """, firstName, lastName);
        HttpClient client = HttpClient.newBuilder().followRedirects(HttpClient.Redirect.NORMAL).build();
        URL url = new URL("https://query.wikidata.org/sparql?query=" + URLEncoder.encode(query, "UTF-8"));
        HttpRequest request = HttpRequest.newBuilder().GET().uri(url.toURI()).header("Accept", "application/json").build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        JsonObject res = JsonParser.parseReader(new StringReader(response.body())).getAsJsonObject().getAsJsonObject("results");
        JsonArray bindings = res.getAsJsonArray("bindings");
        Pattern p = Pattern.compile("Q\\d+");

        for (int i = 0; i < bindings.size(); i++) {
            JsonObject obj = (JsonObject) bindings.get(i);
            String title = obj.getAsJsonObject("movieLabel").getAsJsonPrimitive("value").getAsString();
            if (p.matcher(title).matches()) continue;
            JsonObject d = obj.getAsJsonObject("movieDescription");
            String description = "No description";
            if (d != null)
                description = d.getAsJsonPrimitive("value").getAsString();
            String genre = obj.getAsJsonObject("genreLabel").getAsJsonPrimitive("value").getAsString();
            String release = obj.getAsJsonObject("release").getAsJsonPrimitive("value").getAsString();
            int duration = obj.getAsJsonObject("duration").getAsJsonPrimitive("value").getAsInt();

            Genre g = genreRepository.findByName(genre);
            if (g == null) g = genreRepository.save(new Genre(-1, genre));

            Director director = directorRepository.findByFirstNameAndLastName(firstName, lastName);
            if (director == null) director = directorRepository.save(new Director(-1, firstName, lastName));

            int year = Integer.parseInt(release.substring(0, 4));
            int month = Integer.parseInt(release.substring(5, 7));
            int day = Integer.parseInt(release.substring(8, 10));

            Movie m = movieRepository.findByName(title);
            if (m == null) movieRepository.save(new Movie(-1, title, LocalDate.of(year, month, day), description, duration, g, director));
        }
    }

    @Override
    public void run(String... args) throws Exception {
        /*
        loadMovies("Quentin", "Tarantino");
        loadMovies("Steven", "Spielberg");
        loadMovies("Wes", "Anderson");
        loadMovies("George", "Lucas");
        loadMovies("David", "Fincher");

        loadBooks("Victor", "Hugo");
        loadBooks("Marcel", "Proust");
        loadBooks("Jules", "Verne");
        loadBooks("Charles", "Baudelaire");
        loadBooks("Alexandre", "Dumas");
         */
    }
}
