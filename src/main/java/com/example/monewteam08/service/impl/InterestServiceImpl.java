package com.example.monewteam08.service.impl;

import com.example.monewteam08.entity.Interest;
import com.example.monewteam08.exception.Interest.DuplicateInterestException;
import com.example.monewteam08.exception.Interest.InterestNotFoundException;
import com.example.monewteam08.repository.InterestRepository;
import com.example.monewteam08.service.Interface.InterestService;
import com.example.monewteam08.util.SimilarityUtil;
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

  @Override
  public Interest create(String name, List<String> keywords) {
    List<Interest> existing = interestRepository.findAll();

    boolean hasSimilar = existing.stream()
        .anyMatch(i -> SimilarityUtil.isSimilar(i.getName(), name));

    if (hasSimilar) {
      throw new DuplicateInterestException(name);
    }

    return interestRepository.save(new Interest(name, keywords));
  }

  @Override
  public List<Interest> read(String keyword, String sortBy) {
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
    
    return stream.sorted(comparator).collect(Collectors.toList());
  }

  @Override
  public Interest updateKeywords(UUID id, List<String> newKeywords) {
    Interest interest = interestRepository.findById(id)
        .orElseThrow(() -> new InterestNotFoundException(id.toString()));

    interest.updateKeywords(newKeywords);
    return interestRepository.save(interest);

  }

  @Override
  public void delete(UUID id) {
    Interest interest = interestRepository.findById(id)
        .orElseThrow(() -> new InterestNotFoundException(id.toString()));
    interestRepository.delete(interest);
  }
}
