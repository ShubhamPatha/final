package com.geekster.MusicStreaming.controller;

import com.geekster.MusicStreaming.model.NormalUser;
import com.geekster.MusicStreaming.model.Playlist;
import com.geekster.MusicStreaming.model.dto.AuthenticationInputDto;
import com.geekster.MusicStreaming.model.dto.SignInInputDto;
import com.geekster.MusicStreaming.service.NormalUserService;
import com.geekster.MusicStreaming.service.PlaylistService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
public class NormalUserController {
    @Autowired
    NormalUserService normalUserService;

    @Autowired
    PlaylistService playlistService;

    //sign up
    @PostMapping("User/signup")
    public String NormalUserSignUp(@Valid @RequestBody NormalUser user)
    {
        return normalUserService.userSignUp(user);

    }



    //sign in
    @PostMapping("User/signIn")
    public String NormalUserSignIn(@RequestBody SignInInputDto signInInput)
    {
        return normalUserService.userSignIn(signInInput);

    }


    //sign out
    @DeleteMapping("User/signOut")
    public String NormalUserSignOut(@RequestBody AuthenticationInputDto authInfo)
    {
        return normalUserService.userSignOut(authInfo);

    }

    @GetMapping("Playlist/{nuserid}")
    public List<Playlist> getMyPlaylist(@PathVariable Integer nuserid)
    {

        return normalUserService.getMyPlaylist(nuserid);

    }
}
