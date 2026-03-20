package com.innowise.service.impl;

import com.innowise.dao.repository.specification.CardSpecification;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import com.innowise.dao.model.CardModel;
import com.innowise.dao.repository.CardRepository;
import com.innowise.exception.CardLimitException;
import com.innowise.service.CardService;
import com.innowise.service.dto.CardDto;
import com.innowise.service.mapper.CardMapper;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class CardServiceImpl implements CardService {
    private final CardRepository cardRepository;
    private final CardMapper cardMapper;


    @Override
    @Transactional
    public CardDto createCard(CardDto cardDto) {
        int cardsCount = cardRepository.countByUserId(cardDto.getUserId());

        if (cardsCount >= 5) {
            throw new CardLimitException("User cannot have more than 5 cards");
        }
        var cardModel = cardMapper.dtoToModel(cardDto);
        var save = cardRepository.save(cardModel);
        return cardMapper.modelToDto(save);
    }

    @Override
    @Cacheable(value = "card", key = "#cardId")
    public CardDto getCardById(UUID cardId) {
        var cardModel = cardRepository.findById(cardId).orElseThrow(
                () -> new EntityNotFoundException("Card not found with id " + cardId)
        );
        return cardMapper.modelToDto(cardModel);
    }

    @Override
    @CachePut(value = "card", key = "#cardId")
    @Transactional
    public CardDto updateCardById(UUID cardId, CardDto cardDto) {
        var cardModel = cardRepository.findById(cardId).orElseThrow(
                () -> new EntityNotFoundException("Card not found with id " + cardId)
        );
        cardMapper.updateFromDto(cardDto, cardModel);
        cardModel.setUpdatedAt(LocalDateTime.now());
        return cardMapper.modelToDto(cardModel);
    }

    @Override
    @Transactional
    public boolean activateDeactivateCard(UUID cardId) {
        var cardModel = cardRepository.findById(cardId).orElseThrow(
                () -> new EntityNotFoundException("Card not found with id " + cardId)
        );
        if (cardModel.isActive()) {
            cardModel.setActive(false);
            log.info("Card with id {} was Deactivated", cardId);
        }
        else {
            cardModel.setActive(true);
            log.info("Card with id {} was Activated", cardId);
        }
        return cardModel.isActive();
    }

    @Cacheable(value = "cards", key = "#userId")
    @Override
    public List<CardDto> getCardsByUserIdWithUser(UUID userId) {
        return cardRepository.findAllCardsByUserIdWithUser(userId).stream()
                .map(cardMapper::modelToDto)
                .toList();
    }

    @Override
    public Page<CardDto> getAllCardsWithFilterNumber(String cardNumber, Pageable pageable) {
        Specification<CardModel> spec = CardSpecification.filterByNumber(cardNumber);

        return cardRepository.findAll(spec, pageable)
                .map(cardMapper::modelToDto);
    }

    @Override
    @CacheEvict(value = "card", key = "#cardId")
    @Transactional
    public void deleteById(UUID cardId) {
        if (!cardRepository.existsById(cardId)) {
            throw new EntityNotFoundException("Card not found with id " + cardId);
        }
        cardRepository.deleteById(cardId);
    }
}
