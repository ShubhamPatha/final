package com.geekster.MusicStreaming.repo;

import com.geekster.MusicStreaming.model.NormalUserAuthenticationToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NTokenRepo  extends JpaRepository<NormalUserAuthenticationToken,Integer> {
    NormalUserAuthenticationToken findFirstByTokenValue(String tokenValue);
}