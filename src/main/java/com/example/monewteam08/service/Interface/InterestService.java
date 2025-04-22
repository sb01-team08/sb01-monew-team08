package com.example.monewteam08.service.Interface;

import com.example.monewteam08.entity.Interest;
import java.util.List;
import java.util.UUID;

public interface InterestService {

  //관심사 생성
  public Interest create(String name, List<String> keywords);

  //관심사 조회
  public List<Interest> read(String query, String sortBy);

  //관심사 키워스 수정
  public Interest updateKeywords(UUID id, List<String> newKeywords);

  //관심사 삭제
  public void delete(UUID id);

}
