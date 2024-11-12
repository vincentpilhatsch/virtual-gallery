package com.vincent.gallerybackend.user.service;

import com.vincent.gallerybackend.artwork.model.Artwork;
import com.vincent.gallerybackend.artwork.model.ArtworkEntity;
import com.vincent.gallerybackend.artwork.model.ArtworkMapper;
import com.vincent.gallerybackend.artwork.repository.ArtworkRepository;
import com.vincent.gallerybackend.config.CookieUtils;
import com.vincent.gallerybackend.event.repository.EventRepository;
import com.vincent.gallerybackend.exceptions.AuthenticationFailedException;
import com.vincent.gallerybackend.exceptions.NotFoundException;
import com.vincent.gallerybackend.security.JwtUtils;
import com.vincent.gallerybackend.security.PasswordValidator;
import com.vincent.gallerybackend.user.model.*;
import com.vincent.gallerybackend.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
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
    private final UserRepository userRepository;
    private final ArtworkRepository artworkRepository;
    private final EventRepository eventRepository;

    private final UserMapper userMapper;
    private final ArtworkMapper artworkMapper;

    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    private final JwtUtils jwtUtils;
    private final CookieUtils cookieUtils;

    @Transactional
    @Override
    public User getCurrentUser(Authentication authentication) {
        String email = ((UserDetails) authentication.getPrincipal()).getUsername();
        UserEntity userEntity = findByEmail(email);
        return userMapper.userDAOtoDTO(userEntity);
    }

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
    public AuthResponse registerUser(RegisterRequest registerRequest, HttpServletResponse response) {
        if (userRepository.findByEmail(registerRequest.getEmail()).isPresent()) {
            throw new AuthenticationFailedException("User already exists");
        }
        if (!PasswordValidator.isPasswordStrong(registerRequest.getPassword())) {
            throw new AuthenticationFailedException("Password is not strong enough");
        }
        setAttributesAndSaveUser(registerRequest);
        return generateTokenForUserAndSafeTokenInCookie(registerRequest, response);
    }

    private void setAttributesAndSaveUser(RegisterRequest registerRequest) {
        UserEntity userEntity = new UserEntity();
        userEntity.setFirstName(registerRequest.getFirstName());
        userEntity.setLastName(registerRequest.getLastName());
        userEntity.setEmail(registerRequest.getEmail());
        userEntity.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        userEntity.setLanguage(Language.DE);
        userEntity.setRole(Role.USER);
        userRepository.save(userEntity);
    }

    private AuthResponse generateTokenForUserAndSafeTokenInCookie(AuthRequest authRequest, HttpServletResponse response) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword()));
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String jwt = jwtUtils.generateToken(userDetails);
        cookieUtils.createJwtCookie(response, jwt);
        return createAuthResponse(authRequest.getEmail(), jwt);
    }

    public AuthResponse loginUser(LoginRequest loginRequest, HttpServletResponse response) {
        try {
            return generateTokenForUserAndSafeTokenInCookie(loginRequest, response);
        } catch (AuthenticationException e) {
            throw new AuthenticationFailedException("Invalid email or password");
        }
    }


    private AuthResponse createAuthResponse(String email, String jwt) {
        UserEntity userEntity = findByEmail(email);
        User user = userMapper.userDAOtoDTO(userEntity);    
        return new AuthResponse(user, jwt);
    }

    @Override
    public void logoutUser(HttpServletResponse response) {
        cookieUtils.deleteJwtCookie(response);
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
    public UserEntity updateLanguageOfUser(String email, Language language) {
        UserEntity user = findByEmail(email);
        user.setLanguage(language);
        return userRepository.save(user);
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
