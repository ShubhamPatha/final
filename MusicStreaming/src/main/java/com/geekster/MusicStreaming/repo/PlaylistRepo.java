package com.geekster.MusicStreaming.repo;

import com.geekster.MusicStreaming.model.NormalUser;
import com.geekster.MusicStreaming.model.Playlist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlaylistRepo extends JpaRepository<Playlist,Integer> {
    Playlist findFirstBysongId(Integer songId);
}
