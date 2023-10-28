package com.geekster.MusicStreaming.service;

import com.geekster.MusicStreaming.model.NormalUserAuthenticationToken;
import com.geekster.MusicStreaming.model.dto.AuthenticationInputDto;
import com.geekster.MusicStreaming.repo.NTokenRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NormalUserTokenService {

    @Autowired
    NTokenRepo nTokenRepo;


    public void createToken(NormalUserAuthenticationToken token) {

        nTokenRepo.save(token);

    }

    public void deleteToken(String tokenValue) {

        NormalUserAuthenticationToken token = nTokenRepo.findFirstByTokenValue(tokenValue);
        nTokenRepo.delete(token);


    }

    public boolean authenticate(AuthenticationInputDto authInfo) {

        String email = authInfo.getEmail();
        String tokenValue = authInfo.getTokenValue();

        //find thr actual token -> get the connected patient -> get its email-> verify with passed email

        //return ipTokenRepo.findFirstByTokenValue(tokenValue).getPatient().getPatientEmail().equals(email);

        NormalUserAuthenticationToken token = nTokenRepo.findFirstByTokenValue(tokenValue);
        if (token != null) {

            return token.getNormalUser().getNemail().equals(email);

        } else {
            return false;
        }


    }
}
