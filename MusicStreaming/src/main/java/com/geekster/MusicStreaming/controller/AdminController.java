package com.geekster.MusicStreaming.controller;

import com.geekster.MusicStreaming.model.NormalUser;
import com.geekster.MusicStreaming.model.Playlist;
import com.geekster.MusicStreaming.service.NormalUserService;
import com.geekster.MusicStreaming.service.PlaylistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
public class AdminController {
    @Autowired
    PlaylistService playlistService;
    @Autowired
    NormalUserService normalUserService;
    @GetMapping("NormalUser")
    public List<NormalUser> getAllNormalUser()
    {

        return normalUserService.getAllNormalUser();

    }

    @GetMapping("AllSong")
    public List<Playlist> getAllSong()
    {

        return playlistService.getAllSongs();

    }
    @PostMapping("AddSong")
    public String addSong(@RequestBody  Playlist playlist)
    {
        return playlistService.addSong(playlist);
    }


    @PostMapping("Song/{songId}")
    public String UpdateSong(@RequestBody Playlist playlist,@PathVariable Integer songId )
    {
        return playlistService.updateSong(playlist,songId) ;
    }

    @DeleteMapping("Song/{songId}")
    public String DeleteSong(@PathVariable Integer songId)
    {
        return playlistService.DeleteSong(songId);
    }



}
