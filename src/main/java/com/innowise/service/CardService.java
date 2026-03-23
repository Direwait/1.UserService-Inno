package com.innowise.service;


import com.innowise.service.dto.CardDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface CardService {

    CardDto createCard(CardDto cardDto);

    CardDto getCardById(UUID cardId);

    CardDto updateCardById(UUID cardId, CardDto cardDto);

    boolean activateDeactivateCard(UUID cardId);

    List<CardDto> getCardsByUserIdWithUser(UUID userId);

    Page<CardDto> getAllCardsWithFilterNumber(String cardNumber, Pageable pageable);

    void deleteById(UUID cardId);
}
