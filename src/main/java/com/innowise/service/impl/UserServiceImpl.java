package com.innowise.service.impl;

import com.innowise.dao.model.CardModel;
import com.innowise.dao.repository.specification.UserSpecification;
import com.innowise.exception.CardLimitException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.innowise.dao.model.UserModel;
import com.innowise.dao.repository.UserRepository;
import com.innowise.exception.DuplicateEmailException;
import com.innowise.service.UserService;
import com.innowise.service.dto.UserDto;
import com.innowise.service.mapper.UserMapper;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;


    @Override
    @Transactional
    public UserDto createUser(UserDto userDto) {
        if (userRepository.existsByEmail(userDto.getEmail())) {
            throw new DuplicateEmailException(
                    "User with email " + userDto.getEmail() + " already exists"
            );
        }
        if (userDto.getCardDtos().size() > 5) {
            throw new CardLimitException("User cannot have more than 5 cards");
        }
        var userModel = userMapper.dtoToModel(userDto);
        if (userModel.getCards() != null && !userModel.getCards().isEmpty()) {
            for (CardModel card : userModel.getCards()) {
                card.setUser(userModel);
            }
        }
        UserModel savedUser = userRepository.save(userModel);
        return userMapper.modelToDto(savedUser);
    }

    @Override
    @Cacheable(value = "user", key = "#userId")
    public UserDto getUserById(UUID userId) {
        var userModel = userRepository.findUserWithCardsById(userId).orElseThrow(
                () -> new EntityNotFoundException("User not found with id " + userId)
        );
        return userMapper.modelToDto(userModel);
    }

    @Override
    @Transactional
    @CachePut(value = "user", key = "#userId")
    public UserDto updateUserById(UUID userId, UserDto userDto) {
        var userModel = userRepository.findById(userId).orElseThrow(
                () -> new EntityNotFoundException("User not found with id " + userId)
        );
        userMapper.updateFromDto(userDto, userModel);
        userModel.setUpdatedAt(LocalDateTime.now());
        return userMapper.modelToDto(userModel);
    }

    @Override
    @Transactional
    public boolean activateDeactivateUser(UUID userId) {
        var userModel = userRepository.findById(userId).orElseThrow(
                () -> new EntityNotFoundException("User not found with id " + userId)
        );
        if (userModel.isActive()) {
            userModel.setActive(false);
            log.info("User with id {} was Deactivated", userId);
        }
        else {
            userModel.setActive(true);
            log.info("User with id {} was Activated", userId);
        }
        return userModel.isActive();
    }

    @Override
    public Page<UserDto> getAllUsers(String searchTerm, Pageable pageable) {
        Specification<UserModel> spec = UserSpecification.filterByNameOrSurname(searchTerm);
        return userRepository.findAll(spec, pageable)
                .map(userMapper::modelToDto);
    }

    @Override
    @Transactional
    @CacheEvict(value = "user", key = "#userId")
    public void deleteById(UUID userId) {
        if (!userRepository.existsById(userId)) {
            throw new EntityNotFoundException("User not found with id " + userId);
        }
        userRepository.deleteById(userId);
    }
}
