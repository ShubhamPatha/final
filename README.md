# MusicAPI

## FRAMEWORK AND LANGUAGE USED
* JAVA 17
* MAVEN
* SPRINGBOOT 3.1.1
<!-- Headings -->   
## DATA FLOW



  ### CONFIGURATION

  #### Admin Controller
  ```
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

```
  #### NormalUser Controller
  ```
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

```
 ### MODEL
 #### AuthenticationInputDto
  ```
package com.geekster.MusicStreaming.model.dto;

import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationInputDto {

    @Email
    private String email;

    private String tokenValue;
}
```
#### SignInInputDto
  ```
package com.geekster.MusicStreaming.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignInInputDto {

    @Email
    private String email;

    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@#$!%])[A-Za-z\\d@#$!%]{8,}$\n")
    private String password;
}
```
#### Admin
  ```
package com.geekster.MusicStreaming.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Admin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer adminId;
    private String adminName;

    @Pattern(regexp = ".*@music\\.admin\\.in$")
    private String adminEmail;
    private String adminPassword;

}
```
#### NormalUser
  ```
package com.geekster.MusicStreaming.model;


import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class,scope = NormalUser.class,property = "nuserid")
public class NormalUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer nuserid;
    private String nname;
    @Email
    private String nemail;
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@#$!%])[A-Za-z\\d@#$!%]{8,}$\n")
    private  String npassword;


    @OneToMany(mappedBy = "normalUser")
    List<Playlist> playlist;

}

```
#### NormalUserAuthenticationToken
  ```
package com.geekster.MusicStreaming.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "NUAuthentication")
public class NormalUserAuthenticationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer tokenId;
    private String tokenValue;
    private LocalDateTime tokenCreationTime;

    //each token should be linked with a patient


    public NormalUserAuthenticationToken(NormalUser normalUser) {
        this.normalUser=normalUser;
        this.tokenCreationTime = LocalDateTime.now();
        this.tokenValue = UUID.randomUUID().toString();
    }

    @OneToOne
    @JoinColumn(name = "fk_normal_user_id")
   NormalUser normalUser;
}

```
#### Playlist
  ```
package com.geekster.MusicStreaming.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class,scope = Playlist.class,property = "songId")
public class Playlist {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer songId;
    private String songName;

    @ManyToOne()
    @JoinColumn(name = "fk_normaluser_id")
    NormalUser normalUser;



}

```

### REPO
#### NormalUserRepo
  ```
package com.geekster.MusicStreaming.repo;

import com.geekster.MusicStreaming.model.NormalUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NormalUserRepo extends JpaRepository<NormalUser,Integer> {

    NormalUser findFirstByNemail(String newEmail);
}

```
#### NTokenRepo
  ```
package com.geekster.MusicStreaming.repo;

import com.geekster.MusicStreaming.model.NormalUserAuthenticationToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NTokenRepo  extends JpaRepository<NormalUserAuthenticationToken,Integer> {
    NormalUserAuthenticationToken findFirstByTokenValue(String tokenValue);
}
```
#### PlaylistRepo
  ```
package com.geekster.MusicStreaming.repo;

import com.geekster.MusicStreaming.model.NormalUser;
import com.geekster.MusicStreaming.model.Playlist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlaylistRepo extends JpaRepository<Playlist,Integer> {
    Playlist findFirstBysongId(Integer songId);
}

```
### SERVICE
 #### EmailService
  ```
package com.geekster.MusicStreaming.service;




import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class EmailService {

    private static final String EMAIL_USERNAME = "shubhampathak1350@gmail.com";
    private static final String EMAIL_PASSWORD = "ntaj mqhb ejlu jkox";

    public static boolean sendEmail(String toEmail, String subject, String body){
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(EMAIL_USERNAME, EMAIL_PASSWORD);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(EMAIL_USERNAME));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject(subject);
            message.setText(body);
            Transport.send(message);
            System.out.println("OTP sent successfully to " + toEmail);
            return true;
        } catch (MessagingException e) {
            e.printStackTrace();
            return false;
        }
    }
}
```
#### NormalUserService
  ```
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

```
#### NormalUserTokenService
  ```
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

```
#### PasswordEncryptor
  ```
package com.geekster.MusicStreaming.service;

import jakarta.xml.bind.DatatypeConverter;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static jakarta.xml.bind.DatatypeConverter.printHexBinary;

public class PasswordEncryptor {

    public static String encrypt(String unHashedPassword) throws NoSuchAlgorithmException {
        MessageDigest md5=MessageDigest.getInstance("MD5");

        md5.update(unHashedPassword.getBytes());
        byte[]digested=md5.digest();
        return DatatypeConverter.printHexBinary(digested);

    }
}
```
#### PlaylistService
  ```
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

```
### MAIN
  ``` 
package com.geekster.MusicStreaming;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MusicStreamingApplication {

	public static void main(String[] args) {
		SpringApplication.run(MusicStreamingApplication.class, args);
	}

}
```


 ### POM
  ```
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
	<modelVersion>4.0.0</modelVersion>
	<parent>
		<groupId>org.springframework.boot</groupId>
		<artifactId>spring-boot-starter-parent</artifactId>
		<version>3.1.5</version>
		<relativePath/> <!-- lookup parent from repository -->
	</parent>
	<groupId>com.geekster</groupId>
	<artifactId>MusicStreaming</artifactId>
	<version>0.0.1-SNAPSHOT</version>
	<name>MusicStreaming</name>
	<description>Demo project for Spring Boot</description>
	<properties>
		<java.version>17</java.version>
	</properties>
	<dependencies>
		<dependency>
			<groupId>com.sun.mail</groupId>
			<artifactId>javax.mail</artifactId>
			<version>1.6.2</version>
		</dependency>
		<dependency>
			<groupId>org.springdoc</groupId>
			<artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
			<version>2.1.0</version>
		</dependency>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-data-jpa</artifactId>
		</dependency>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-validation</artifactId>
		</dependency>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-web</artifactId>
		</dependency>

		<dependency>
			<groupId>com.mysql</groupId>
			<artifactId>mysql-connector-j</artifactId>
			<scope>runtime</scope>
		</dependency>
		<dependency>
			<groupId>org.projectlombok</groupId>
			<artifactId>lombok</artifactId>
			<optional>true</optional>
		</dependency>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-test</artifactId>
			<scope>test</scope>
		</dependency>
	</dependencies>

	<build>
		<plugins>
			<plugin>
				<groupId>org.springframework.boot</groupId>
				<artifactId>spring-boot-maven-plugin</artifactId>
				<configuration>
					<excludes>
						<exclude>
							<groupId>org.projectlombok</groupId>
							<artifactId>lombok</artifactId>
						</exclude>
					</excludes>
				</configuration>
			</plugin>
		</plugins>
	</build>

</project>
```
## DATA STRUCTURE USED
* LIST 
* STRING
* LOCAL DATE
* INTEGER
* USER

# PROJECT SUMMARY

## Music Streaming System Has been created with the following attribute

* UserId
* username
* Song Id
* Song name
* admin 

### Endpoint to be provided 
* User / Signup 
* User /signin
* PLaylist/{nuserid}
* user /signout
* update song
*  add song
*  get all song
*  get alll user









<!-- Headings -->   
# Author
SHUBHAM PATHAK
 <!-- UL -->
* Twitter <!-- Links -->
[@ShubhamPathak]( https://twitter.com/Shubham15022000)
* Github  <!-- Links -->
[@ShubhamPathak]( https://github.com/ShubhamPatha)
<!-- Headings -->   
