package com.vincent.gallerybackend.user.service;

import com.vincent.gallerybackend.artwork.model.Artwork;
import com.vincent.gallerybackend.artwork.model.ArtworkEntity;
import com.vincent.gallerybackend.artwork.model.ArtworkMapper;
import com.vincent.gallerybackend.artwork.repository.ArtworkRepository;
import com.vincent.gallerybackend.event.repository.EventRepository;
import com.vincent.gallerybackend.exceptions.AuthenticationFailedException;
import com.vincent.gallerybackend.exceptions.NotFoundException;
import com.vincent.gallerybackend.security.JwtUtils;
import com.vincent.gallerybackend.security.PasswordValidator;
import com.vincent.gallerybackend.user.model.*;
import com.vincent.gallerybackend.user.repository.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    @Value("${jwt.expiration-time}")
    private int expirationTime;

    private final UserRepository userRepository;
    private final ArtworkRepository artworkRepository;
    private final EventRepository eventRepository;

    private final UserMapper userMapper;
    private final ArtworkMapper artworkMapper;

    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    @Transactional(readOnly = true)
    @Override
    public User getUserByEmail(String email) {
        UserEntity userEntity = findByEmail(email);
        return userMapper.userDAOtoDTO(userEntity);
    }

    @Transactional(readOnly = true)
    @Override
    public User getUserByToken(String token) {
        String email = jwtUtils.extractUsername(token);
        UserEntity userEntity = findByEmail(email);
        return userMapper.userDAOtoDTO(userEntity);
    }

    public UserEntity findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("User not found"));
        // Try catch necessary?
    }

    @Transactional(readOnly = true)
    @Override
    public Set<Long> getFavoriteArtworkIdsByEmail(String email) {
        UserEntity userEntity = findByEmail(email);
        return userEntity.getFavoriteArtworkIds();
    }

    @Transactional(readOnly = true)
    @Override
    public Set<Long> getAcceptedEventIdsByEmail(String email) {
        UserEntity userEntity = findByEmail(email);
        return userEntity.getAcceptedEventIds();
    }

    @Transactional(readOnly = true)
    @Override
    public Set<Long> getNotificationIdsByEmail(String email) {
        UserEntity userEntity = findByEmail(email);
        return userEntity.getNotificationIds();
    }

    @Transactional(readOnly = true)
    @Override
    public List<Artwork> getFavoriteArtworksByEmail(String email) {
        UserEntity userEntity = findByEmail(email);
        Set<Long> artworkIds = userEntity.getFavoriteArtworkIds();
        List<ArtworkEntity> artworkEntities = artworkRepository.findAllById(artworkIds);
        return artworkMapper.artworksDAOtoDTO(artworkEntities);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Artwork> getRequestedArtworksByEmail(String email) {
        UserEntity userEntity = findByEmail(email);
        Set<Long> artworkIds = userEntity.getRequestedArtworkIds();
        List<ArtworkEntity> artworkEntities = artworkRepository.findAllById(artworkIds);
        return artworkMapper.artworksDAOtoDTO(artworkEntities);
    }

    @Transactional
    @Override
    public ResponseEntity<String> createUser(User user) {
        if (findByEmail(user.getEmail()) != null) {
            return ResponseEntity.badRequest().body("User already exists");
        }
        if (!PasswordValidator.isPasswordStrong(user.getPassword())) {
            return ResponseEntity.badRequest().body("Password is not strong enough");
        }
        UserEntity userEntity = mapUserAndSetDefaultValues(user);
        userRepository.save(userEntity);
        return ResponseEntity.ok("User registered successfully");
    }

    private UserEntity mapUserAndSetDefaultValues(User user) {
        UserEntity userEntity = userMapper.userDTOtoDAO(user);
        userEntity.setPassword(passwordEncoder.encode(user.getPassword()));
        userEntity.setLanguage(Language.DE);
        userEntity.setRole(Role.USER);
        return userEntity;
    }

    public AuthResponse loginUser(LoginRequest loginRequest, HttpServletResponse response) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String jwt = jwtUtils.generateToken(userDetails);
            createCookie(response, jwt);
            return createAuthResponse(loginRequest, jwt);
        } catch (AuthenticationException e) {
            throw new AuthenticationFailedException("Invalid email or password");
        }
    }

    private AuthResponse createAuthResponse(LoginRequest loginRequest, String jwt) {
        UserEntity userEntity = findByEmail(loginRequest.getEmail());
        User user = userMapper.userDAOtoDTO(userEntity);
        return new AuthResponse(user, jwt);
    }

    private void createCookie(HttpServletResponse response, String jwt) {
        Cookie jwtCookie = new Cookie("token", jwt);
        jwtCookie.setHttpOnly(true);
//        jwtCookie.setSecure(true);
        jwtCookie.setPath("/");
        jwtCookie.setMaxAge(expirationTime);
        response.addCookie(jwtCookie);
    }

    @Transactional
    @Override
    public UserEntity updateUser(User newUser) {
        UserEntity oldUser = userRepository.findById(newUser.getId()).orElseThrow(() -> new NotFoundException("User not found"));
        UserEntity updatedUserEntity = userMapper.updateUserDTOtoDAO(oldUser, newUser);
        return userRepository.save(updatedUserEntity);
    }

    @Transactional
    @Override
    public UserEntity updateFavoriteArtworksOfUser(String email, long artworkId) {
        UserEntity userEntity = findByEmail(email);
        checkIfArtworkExists(artworkId);
        Set<Long> favoriteArtworks = addOrDeleteIdInSet(userEntity.getFavoriteArtworkIds(), artworkId);
        userEntity.setFavoriteArtworkIds(favoriteArtworks);
        return userRepository.save(userEntity);
    }

    @Transactional
    @Override
    public UserEntity updateOneRequestedArtworkOfUser(String email, long artworkId) {
        UserEntity userEntity = findByEmail(email);
        checkIfArtworkExists(artworkId);
        Set<Long> requestedArtworks = addOrDeleteIdInSet(userEntity.getRequestedArtworkIds(), artworkId);
        userEntity.setRequestedArtworkIds(requestedArtworks);
        return userRepository.save(userEntity);
    }

    @Transactional
    @Override
    public UserEntity updateAcceptedEventsOfUser(String email, long eventId) {
        UserEntity userEntity = findByEmail(email);
        checkIfEventExists(eventId);
        Set<Long> acceptedEvents = addOrDeleteIdInSet(userEntity.getAcceptedEventIds(), eventId);
        userEntity.setAcceptedEventIds(acceptedEvents);
        return userRepository.save(userEntity);
    }

    private void checkIfEventExists(long eventId) {
        boolean eventExists = eventRepository.existsById(eventId);
        if (!eventExists) {
            throw new NotFoundException("Event not found");
        }
    }

    private void checkIfArtworkExists(long artworkId) {
        boolean artworkExists = artworkRepository.existsById(artworkId);
        if (!artworkExists) {
            throw new NotFoundException("Artwork not found");
        }
    }

    private Set<Long> addOrDeleteIdInSet(Set<Long> set, long id) {
        boolean exists = set.contains(id);
        if (exists) {
            set.remove(id);
        } else {
            set.add(id);
        }
        return set;
    }

}
