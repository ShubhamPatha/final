package com.geekster.MusicStreaming.service;

import com.geekster.MusicStreaming.model.NormalUser;
import com.geekster.MusicStreaming.repo.NormalUserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NormalUserService {

@Autowired
    NormalUserRepo normalUserRepo;


    public  List<NormalUser> getAllNormalUser() {
        return normalUserRepo.findAll();

    }
}
