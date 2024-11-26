package com.vincent.gallerybackend.user.service;

import com.vincent.gallerybackend.artwork.model.Artwork;
import com.vincent.gallerybackend.artwork.model.ArtworkEntity;
import com.vincent.gallerybackend.artwork.model.ArtworkMapper;
import com.vincent.gallerybackend.artwork.repository.ArtworkRepository;
import com.vincent.gallerybackend.artworkrequest.model.ArtworkAndArtworkRequest;
import com.vincent.gallerybackend.artworkrequest.model.ArtworkRequest;
import com.vincent.gallerybackend.artworkrequest.model.ArtworkRequestEntity;
import com.vincent.gallerybackend.artworkrequest.model.ArtworkRequestMapper;
import com.vincent.gallerybackend.artworkrequest.repository.ArtworkRequestRepository;
import com.vincent.gallerybackend.config.CookieUtils;
import com.vincent.gallerybackend.event.model.EventEntity;
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
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final ArtworkRepository artworkRepository;
    private final ArtworkRequestRepository artworkRequestRepository;
    private final EventRepository eventRepository;

    private final UserMapper userMapper;
    private final ArtworkMapper artworkMapper;
    private final ArtworkRequestMapper artworkRequestMapper;

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
    public List<ArtworkAndArtworkRequest> getArtworksAndArtworkRequestsOfUser(String email) {
        UserEntity userEntity = findByEmail(email);
        Set<Long> artworkRequestIds = userEntity.getArtworkRequestIds();
        List<ArtworkRequest> artworkRequests = getArtworkRequestsOfUser(artworkRequestIds);
        List<Artwork> artworks = getArtworksOfUser(artworkRequests);
        return getArtworksAndArtworkRequests(artworkRequests, artworks);
    }

    private List<ArtworkRequest> getArtworkRequestsOfUser(Set<Long> artworkRequestIds) {
        List<ArtworkRequestEntity> artworkRequestEntities = artworkRequestRepository.findAllById(artworkRequestIds);
        return artworkRequestMapper.artworkRequestsDAOtoDTO(artworkRequestEntities);
    }

    private List<Artwork> getArtworksOfUser(List<ArtworkRequest> artworkRequests) {
        Set<Long> usersArtworkIdsOfArtworkRequests =
                artworkRequests
                        .stream()
                        .map(ArtworkRequest::getArtworkId)
                        .collect(Collectors.toSet());
        List<ArtworkEntity> artworkEntities = artworkRepository.findAllById(usersArtworkIdsOfArtworkRequests);
        return artworkMapper.artworksDAOtoDTO(artworkEntities);
    }

    private List<ArtworkAndArtworkRequest> getArtworksAndArtworkRequests(List<ArtworkRequest> artworkRequests,
                                                                         List<Artwork> artworks) {
        Map<Long, Artwork> artworkMap = artworks.stream()
                .collect(Collectors.toMap(Artwork::getId, artwork -> artwork));
        return artworkRequests.stream()
                .map(request -> new ArtworkAndArtworkRequest(
                        artworkMap.get(request.getArtworkId()),
                        request
                ))
                .toList();
    }

    @Transactional(readOnly = true)
    @Override
    public List<ArtworkRequest> getArtworkRequestsByEmail(String email) {
        UserEntity userEntity = findByEmail(email);
        Set<Long> artworkRequestIds = userEntity.getArtworkRequestIds();
        return getArtworkRequestsOfUser(artworkRequestIds);
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

    public AuthResponse loginUser(LoginRequest loginRequest, HttpServletResponse response) {
        return generateTokenForUserAndSafeTokenInCookie(loginRequest, response);
    }


    private AuthResponse generateTokenForUserAndSafeTokenInCookie(AuthRequest authRequest, HttpServletResponse response) {
        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword()));
        } catch (AuthenticationException e) {
            throw new AuthenticationFailedException("Incorrect email or password");
        }
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String jwt = jwtUtils.generateToken(userDetails);
        cookieUtils.createJwtCookie(response, jwt);
        return createAuthResponse(authRequest.getEmail(), jwt);
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
    public User updateUser(User newUser) {
        UserEntity oldUser = userRepository.findById(newUser.getId())
                .orElseThrow(() -> new NotFoundException("User not found"));
        UserEntity updatedUserEntity = userMapper.updateUserDTOtoDAO(oldUser, newUser);
        userRepository.save(updatedUserEntity);
        return userMapper.userDAOtoDTO(updatedUserEntity);
    }

    @Transactional
    @Override
    public User updateLanguageOfUser(String email, Language language) {
        UserEntity user = findByEmail(email);
        user.setLanguage(language);
        userRepository.save(user);
        return userMapper.userDAOtoDTO(user);
    }

    @Transactional
    @Override
    public User updateFavoriteArtworksOfUser(String email, long artworkId) {
        UserEntity userEntity = findByEmail(email);
        checkIfArtworkExists(artworkId);
        Set<Long> favoriteArtworks = addOrDeleteIdInSet(userEntity.getFavoriteArtworkIds(), artworkId);
        userEntity.setFavoriteArtworkIds(favoriteArtworks);
        userRepository.save(userEntity);
        return userMapper.userDAOtoDTO(userEntity);
    }

    @Transactional
    @Override
    public User updateAcceptedEventsOfUser(String email, long eventId) {
        UserEntity userEntity = findByEmail(email);
        updateUserInEvent(eventId, userEntity.getId());
        updateEventInUser(eventId, userEntity);
        return userMapper.userDAOtoDTO(userEntity);
    }

    private void updateUserInEvent(long eventId, UUID userId) {
        EventEntity event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event not found"));
        Set<UUID> confirmedUsers = addOrDeleteIdInSet(event.getConfirmedUserIds(), userId);
        event.setConfirmedUserIds(confirmedUsers);
        eventRepository.save(event);
    }

    private void updateEventInUser(long eventId, UserEntity userEntity) {
        Set<Long> acceptedEvents = addOrDeleteIdInSet(userEntity.getAcceptedEventIds(), eventId);
        userEntity.setAcceptedEventIds(acceptedEvents);
        userRepository.save(userEntity);
    }

    private void checkIfArtworkExists(long artworkId) {
        boolean artworkExists = artworkRepository.existsById(artworkId);
        if (!artworkExists) {
            throw new NotFoundException("Artwork not found");
        }
    }

    private <T> Set<T> addOrDeleteIdInSet(Set<T> set, T id) {
        boolean exists = set.contains(id);
        if (exists) {
            set.remove(id);
        } else {
            set.add(id);
        }
        return set;
    }

}
