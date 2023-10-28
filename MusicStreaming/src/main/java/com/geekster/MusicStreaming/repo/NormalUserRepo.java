package com.geekster.MusicStreaming.repo;

import com.geekster.MusicStreaming.model.NormalUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NormalUserRepo extends JpaRepository<NormalUser,Integer> {

    NormalUser findFirstByNemail(String newEmail);
}
