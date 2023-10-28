package com.geekster.MusicStreaming.service;

import com.geekster.MusicStreaming.model.Playlist;
import com.geekster.MusicStreaming.repo.PlaylistRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlaylistService {
    @Autowired
    PlaylistRepo playlistRepo;
    public String addSong(Playlist playlist) {
  playlistRepo.save(playlist);
  return "Added";
    }

    public List<Playlist> getAllSongs() {
        return playlistRepo.findAll();
    }

    public String updateSong(Playlist playlist, Integer songId) {

        Playlist existingPlaylist=playlistRepo.findFirstBysongId(songId);
        if(existingPlaylist == null)
        {
            return "Song Not found";
        }
        else
        {
            playlistRepo.delete(existingPlaylist);
            playlist.setSongId(songId);
            playlistRepo.save(playlist);
            return "Records updated";

        }
    }

    public String DeleteSong(Integer songId) {

        Playlist existingPlaylist=playlistRepo.findFirstBysongId(songId);
        if(existingPlaylist == null)
        {
            return "Song Not found";
        }
        else
        {
            playlistRepo.delete(existingPlaylist);

            return "Records Deleted";

        }
    }
}
