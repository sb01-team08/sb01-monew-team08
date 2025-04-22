package com.example.monewteam08.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.monewteam08.entity.Interest;
import com.example.monewteam08.exception.Interest.DuplicateInterestException;
import com.example.monewteam08.exception.Interest.InterestNotFoundException;
import com.example.monewteam08.repository.InterestRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

//@MockitoSettings(strictness = Strictness.LENIENT)
@ExtendWith(MockitoExtension.class)
public class InterestServiceImplTest {

  @Mock
  private InterestRepository interestRepository;

  @InjectMocks
  private InterestServiceImpl interestService;

  @Test
  @DisplayName("관심사를 정상적으로 등록할 수 있다.")
  void createInterestSuccess() {
    //given
    //관심사 이름과 키워드
    String name = "인공지능";
    List<String> keywords = List.of("AI", "머신러닝");

    Interest interest = new Interest(UUID.randomUUID(), name, keywords, 0);

    when(interestRepository.findAll()).thenReturn(List.of());
    when(interestRepository.save(any())).thenReturn(interest);

    //when
    //관심사 등록 메서드 호출
    Interest result = interestService.create(name, keywords);

    //then
    //리턴된 관심사 정보와 저장소 상태 검증
    assertThat(result.getName()).isEqualTo(name);
    assertThat(result.getKeywords()).containsExactly("AI", "머신러닝");
    assertThat(result.getSubscriberCount()).isEqualTo(0);
  }

  @Test
  @DisplayName("유사한 이름있으면 등록 실패")
  void createInterestFail() {
    //given
    //"인공지능", "인공 지능" 등
    String newName = "인공지능";
    List<String> keywords = List.of("AI", "머신러닝");

    List<Interest> existing = List.of(
        new Interest("인공 지능", List.of("AI")),
        new Interest("딥러닝", List.of("Deep learning"))
    );

    when(interestRepository.findAll()).thenReturn(existing);

    //when
    //중복 등록 시도
    assertThatThrownBy(() -> interestService.create(newName, keywords))
        .isInstanceOf(DuplicateInterestException.class);
    //then
    //예외 발생
  }

  @Test
  @DisplayName("관심사 키워드를 수정할 수 있다.")
  void updateInterestKeywordSuccess() {
    //given
    // 기존관심사
    UUID id = UUID.randomUUID();
    Interest existingInterest = new Interest(id, "인공지능", List.of("AI", "머신러닝"), 0);

    List<String> updatedKeywords = List.of("ChatGPT", "딥러닝");

    when(interestRepository.findById(id)).thenReturn(Optional.of(existingInterest));
    when(interestRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

    //when
    //키워드 수정 요청
    Interest result = interestService.updateKeywords(id, updatedKeywords);

    //then
    //키워드 바뀌었는지 검증
    assertThat(result.getKeywords()).containsExactly("ChatGPT", "딥러닝");
  }

  @Test
  @DisplayName("관심사를 삭제할 수 있다.")
  void deleteInterestSuccess() {
    //given
    //등록된 관심사
    UUID id = UUID.randomUUID();
    Interest interest = new Interest(id, "인공지능", List.of("AI", "머신러닝"), 0);

    when(interestRepository.findById(id)).thenReturn(Optional.of(interest));
    doNothing().when(interestRepository).delete(interest);

    //when
    //삭제 요청
    interestService.delete(id);

    //than
    //저장소에서 삭제되었는지 확인
    verify(interestRepository, times(1)).delete(interest);
  }

  @Test
  @DisplayName("존재하지 않는 관심사 삭제 시 예외 발생")
  void deleteInterestFailNotfound() {
    //given
    //존재하지 않는 관심사 삭제 요청
    UUID id = UUID.randomUUID();
    when(interestRepository.findById(id)).thenReturn(Optional.empty());

    // when & than
    // 삭제 요청 및 예외 발생
    assertThatThrownBy(() -> interestService.delete(id))
        .isInstanceOf(InterestNotFoundException.class);
  }

  @Test
  @DisplayName("조건 없이 전체 관심사 조회할 수 있다.")
  void readAllWithoutCondition() {
    //given
    //등록된 관심사
    Interest i1 = new Interest(UUID.randomUUID(), "인공지능", List.of("AI"), 0);
    Interest i2 = new Interest(UUID.randomUUID(), "환경보호", List.of("ESG"), 0);
    List<Interest> all = List.of(i1, i2);

    when(interestRepository.findAll()).thenReturn(all);
    //when
    // 조건없는 조회
    List<Interest> result = interestService.read(null, null);

    //than
    // 확인
    assertThat(result).containsExactly(i1, i2);
  }

  @Test
  @DisplayName("관심사나 키워드로 부분 검색할 수 있다.")
  void searchInterestByKeywordOrNameSuccess() {
    //given
    //여러 관심사
    Interest i1 = new Interest(UUID.randomUUID(), "인공지능", List.of("AI", "머신러닝"), 0);
    Interest i2 = new Interest(UUID.randomUUID(), "환경보호", List.of("ESG", "지속가능성"), 0);
    Interest i3 = new Interest(UUID.randomUUID(), "기후 변화", List.of("지구온난화", "환경"), 0);

    List<Interest> all = List.of(i1, i2, i3);
    when(interestRepository.findAll()).thenReturn(all);
    //when
    //검색어로 검색
    List<Interest> result = interestService.read("환경", "name");

    //then
    //매칭되는 괌심사 반환
    assertThat(result).containsExactlyInAnyOrder(i2, i3);
  }

  @Test
  @DisplayName("관심사를 구독할 수 있다.")
  void userSubscribeInterestSuccess() {
    //given
    //사용자와 관심사

    //when
    //구독 요청

    //than
    //구독 정보가 저장
  }
}
