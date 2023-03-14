package com.ac.tuiasi.mongo_spring.services;

import com.ac.tuiasi.mongo_spring.exceptions.PlaylistNotFound;
import com.ac.tuiasi.mongo_spring.exceptions.SongNotFound;
import com.ac.tuiasi.mongo_spring.interfaces.IPlaylistService;
import com.ac.tuiasi.mongo_spring.model.Playlist;
import com.ac.tuiasi.mongo_spring.pojo.Artist;
import com.ac.tuiasi.mongo_spring.pojo.Song;
import com.ac.tuiasi.mongo_spring.repository.PlaylistRepository;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

@Service
public class PlaylistService implements IPlaylistService {
    final String base_url = "http://localhost:8080/api/songcollection/songs/";
    final String artist_url="http://localhost:8080/api/songcollection/artists/";
    @Autowired
    private PlaylistRepository repo;

    @Override
    public List<Playlist> listAll() {
        return repo.findAll();
    }

    @Override
    public Playlist get(String id) throws PlaylistNotFound {
        return repo.findById(id).orElseThrow(() -> new PlaylistNotFound(id));
    }

    @Override
    public Playlist save(Playlist playlist) {
        if (playlist.getPlaylistName() == null) {
            playlist.setPlaylistName("untitled");
        }
        return repo.save(playlist);
    }

    @Override
    public Playlist addSongs(Integer id, List<Song> songs) throws ChangeSetPersister.NotFoundException {
        Playlist playlist = repo.findById(Integer.toString(id)).orElseThrow(ChangeSetPersister.NotFoundException::new);
        for (Song s : songs) {
            playlist.getSongs().add(s);
        }
        repo.save(playlist);
        return playlist;
    }

    @Override
    public void delete(String id) {
        repo.deleteById(id);
    }

    @Override
    public Song getSongFromSql(Integer id) throws IOException, InterruptedException, SongNotFound {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(this.base_url + id.toString()))
                .build();
        HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 404) {
            throw new SongNotFound(id);
        }
        Gson g = new Gson();
        Song songReceived = g.fromJson(response.body(), Song.class);
        songReceived.setSelf(this.base_url + id.toString());
        return songReceived;
    }

    @Override
    public String getArtistNameFromSql(Integer id) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(this.artist_url + id.toString()))
                .build();
        HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 404) {
            return "unknown";
        }
        Gson g = new Gson();
        Artist artist = g.fromJson(response.body(), Artist.class);
        return artist.getName();
    }
}
