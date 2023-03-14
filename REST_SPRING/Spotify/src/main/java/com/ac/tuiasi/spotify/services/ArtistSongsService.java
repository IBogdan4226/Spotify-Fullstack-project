package com.ac.tuiasi.spotify.services;

import com.ac.tuiasi.spotify.enums.Type;
import com.ac.tuiasi.spotify.interfaces.IArtistSongsService;
import com.ac.tuiasi.spotify.interfaces.ISongService;
import com.ac.tuiasi.spotify.model.ArtistSongs;
import com.ac.tuiasi.spotify.model.Song;
import com.ac.tuiasi.spotify.repository.ArtistSongsRepository;
import com.ac.tuiasi.spotify.utils.SongModelCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class ArtistSongsService implements IArtistSongsService {
    @Autowired
    private ArtistSongsRepository repo;
    @Autowired
    private ISongService songService;
    @Autowired
    private SongModelCreator songModel;

    @Override
    public List<Song> getAllFromArtist(Integer artistUUID, Type type, PageRequest page) {

        List<ArtistSongs> allEntries = repo.findByUuid(artistUUID, page);
        List<Song> songs = new ArrayList<Song>();
        if (allEntries.isEmpty()) {
            return songs;
        }
        for (ArtistSongs entry : allEntries) {
            Song currentSong = songService.get(entry.getId());
            Song model = songModel.toModel(currentSong);
            if (type == null) {
                songs.add(model);
            } else if (type == currentSong.getType()) {
                songs.add(model);
            }

        }
        return songs;
    }

    @Override
    public ArtistSongs getArtistSong(Integer artistUUID, Integer songID) {
        return repo.findByUuidAndId(artistUUID, songID);
    }

    @Override
    public ArtistSongs save(ArtistSongs artistSongs) {
        return repo.save(artistSongs);
    }

    @Override
    public void delete(Integer artistUUID, Integer songID) {
        repo.deleteByUuidAndId(artistUUID, songID);
    }

}
