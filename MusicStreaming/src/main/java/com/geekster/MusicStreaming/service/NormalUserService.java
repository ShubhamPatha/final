package com.geekster.MusicStreaming.service;

import com.geekster.MusicStreaming.model.NormalUser;
import com.geekster.MusicStreaming.model.NormalUserAuthenticationToken;
import com.geekster.MusicStreaming.model.Playlist;
import com.geekster.MusicStreaming.model.dto.AuthenticationInputDto;
import com.geekster.MusicStreaming.model.dto.SignInInputDto;
import com.geekster.MusicStreaming.repo.NormalUserRepo;
import com.geekster.MusicStreaming.repo.PlaylistRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.util.List;
@Service
public class NormalUserService {

@Autowired
    NormalUserRepo normalUserRepo;

@Autowired
    NormalUserTokenService normalUserTokenService;




    public  List<NormalUser> getAllNormalUser() {
        return normalUserRepo.findAll();

    }

    public String userSignUp(NormalUser user) {


        String newEmail = user.getNemail();


        NormalUser existingUser = normalUserRepo.findFirstByNemail(newEmail);

        if(existingUser != null)
        {
            return "email already in use";
        }

        // passwords are encrypted before we store it in the table


        String signUpPassword = user.getNpassword();

        try {

            String encryptedPassword = PasswordEncryptor.encrypt(signUpPassword);

            user.setNpassword(encryptedPassword);

            normalUserRepo.save(user);
            
            return "User registered";

        } catch (NoSuchAlgorithmException e) {

            return "Internal Server issues while saving password, try again later!!!";
        }


    }

    public String userSignIn(SignInInputDto signInInput) {

        //check if the email is there in your tables
        //sign in only possible if this person ever signed up

        String email = signInInput.getEmail();
        NormalUser existingUser = normalUserRepo.findFirstByNemail(email);



        if(existingUser == null)
        {
            return "Not a valid email, Please sign up first !!!";
        }

        //password should be matched

        String password = signInInput.getPassword();

        try {
            String encryptedPassword = PasswordEncryptor.encrypt(password);


            if(existingUser.getNpassword().equals(encryptedPassword))
            {
                // return a token for this sign in
                NormalUserAuthenticationToken token = new NormalUserAuthenticationToken(existingUser);


                if(EmailService.sendEmail(email,"otp after login", token.getTokenValue())) {
                    normalUserTokenService.createToken(token);
                    
                    return "check email for otp/token!!!";
                }
                else {
                    return "error while generating token!!!";
                }

            }
            else {
                //password was wrong!!!
                return "Invalid Credentials!!!";
            }

        } catch (NoSuchAlgorithmException e) {

            return "Internal Server issues while saving password, try again later!!!";
        }


    }

    public String userSignOut(AuthenticationInputDto authInfo) {

        if(normalUserTokenService.authenticate(authInfo)) {
            String tokenValue = authInfo.getTokenValue();
            normalUserTokenService.deleteToken(tokenValue);
            
            return "Sign Out successful!!";
        }
        else {
            return "Un Authenticated access!!!";
        }
    }


    public List<Playlist> getMyPlaylist(Integer nuserid) {


        return normalUserRepo.getReferenceById(nuserid).getPlaylist();
    }
}
