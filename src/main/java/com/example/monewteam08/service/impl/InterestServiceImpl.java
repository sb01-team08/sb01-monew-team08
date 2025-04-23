package com.example.monewteam08.service.impl;

import com.example.monewteam08.dto.request.Interest.InterestRequest;
import com.example.monewteam08.dto.request.Interest.InterestUpdateRequest;
import com.example.monewteam08.dto.response.interest.InterestResponse;
import com.example.monewteam08.dto.response.interest.InterestWithSubscriptionResponse;
import com.example.monewteam08.dto.response.interest.PageResponse;
import com.example.monewteam08.entity.Interest;
import com.example.monewteam08.exception.Interest.DuplicateInterestException;
import com.example.monewteam08.exception.Interest.InterestNotFoundException;
import com.example.monewteam08.mapper.InterestMapper;
import com.example.monewteam08.repository.InterestRepository;
import com.example.monewteam08.repository.SubscriptionRepository;
import com.example.monewteam08.service.Interface.InterestService;
import com.example.monewteam08.util.SimilarityUtil;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InterestServiceImpl implements InterestService {

  private final InterestRepository interestRepository;
  private final SubscriptionRepository subscriptionRepository;
  private final InterestMapper interestMapper;

  private void validateDuplicate(String name) {
    List<Interest> existing = interestRepository.findAll();

    boolean hasSimilar = existing.stream()
        .anyMatch(i -> SimilarityUtil.isSimilar(i.getName(), name));

    if (hasSimilar) {
      throw new DuplicateInterestException(name);
    }
  }

  @Override
  public InterestResponse create(InterestRequest request) {
    validateDuplicate(request.name());
    Interest saved = interestRepository.save(interestMapper.toEntity(request));
    return interestMapper.toResponse(saved);
  }

  @Override
  public List<InterestResponse> read(String keyword, String sortBy) {
    List<Interest> all = interestRepository.findAll();

    Stream<Interest> stream = all.stream();

    if (keyword != null && !keyword.isBlank()) {
      stream = stream.filter(i ->
          i.getName().contains(keyword) ||
              i.getKeywords().stream().anyMatch(k -> k.contains(keyword))
      );
    }

    Comparator<Interest> comparator = "subscriberCount".equals(sortBy)
        ? Comparator.comparingInt(Interest::getSubscriberCount).reversed()
        : Comparator.comparing(Interest::getSubscriberCount);

    return stream.sorted(comparator)
        .map(interestMapper::toResponse)
        .collect(Collectors.toList());
  }

  @Override
  public PageResponse<InterestWithSubscriptionResponse> read(String keyword, String orderBy,
      String direction, UUID cursor, LocalDateTime after, int limit, UUID userId) {
    List<Interest> all = interestRepository.findAll();

    Stream<Interest> stream = all.stream();
    if (keyword != null && !keyword.isBlank()) {
      stream = stream.filter(i ->
          i.getName().contains(keyword) ||
              i.getKeywords().stream().anyMatch(k -> k.contains(keyword))
      );
    }

    Comparator<Interest> comparator = switch (orderBy) {
      case "subscriberCount" -> Comparator.comparingInt(Interest::getSubscriberCount);
      case "name" -> Comparator.comparing(Interest::getName);
      default -> Comparator.comparing(Interest::getCreatedAt);
    };

    if ("DESC".equalsIgnoreCase(direction)) {
      comparator = comparator.reversed();
    }

    stream = stream.sorted(comparator);

    List<Interest> filtered = stream
        .dropWhile(i -> cursor != null && i.getId().equals(cursor))
        .limit(limit + 1)
        .toList();

    boolean hasNext = filtered.size() > limit;
    List<Interest> pageContent = hasNext ? filtered.subList(0, limit) : filtered;

    List<InterestWithSubscriptionResponse> content = pageContent.stream()
        .map(i -> new InterestWithSubscriptionResponse(
            i.getId(),
            i.getName(),
            i.getKeywords(),
            i.getSubscriberCount(),
            subscriptionRepository.findByUserIdAndInterestId(userId, i.getId()).isPresent()
        ))
        .toList();

    UUID nextCursor = hasNext ? pageContent.get(pageContent.size() - 1).getId() : null;
    LocalDateTime nextAfter =
        hasNext ? pageContent.get(pageContent.size() - 1).getCreatedAt() : null;

    return new PageResponse<>(content, nextCursor, nextAfter, limit, all.size(), hasNext);
  }

  @Override
  public InterestResponse updateKeywords(UUID id, InterestUpdateRequest request) {
    Interest interest = interestRepository.findById(id)
        .orElseThrow(() -> new InterestNotFoundException(id.toString()));
    interest.updateKeywords(request.keywords());
    Interest saved = interestRepository.save(interest);
    return interestMapper.toResponse(saved);

  }

  @Override
  public InterestResponse delete(UUID id) {
    Interest interest = interestRepository.findById(id)
        .orElseThrow(() -> new InterestNotFoundException(id.toString()));
    interestRepository.delete(interest);
    return interestMapper.toResponse(interest);
  }
}
