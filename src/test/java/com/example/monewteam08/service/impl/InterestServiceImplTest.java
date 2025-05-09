package com.example.monewteam08.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
import com.example.monewteam08.service.Interface.SubscriptionMLogService;
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

  @Mock
  private SubscriptionRepository subscriptionRepository;

  @Mock
  private InterestMapper interestMapper;

  @Mock
  private SubscriptionMLogService subscriptionMLogService;

  @InjectMocks
  private InterestServiceImpl interestService;

  @Test
  @DisplayName("관심사를 정상적으로 등록할 수 있다.")
  void createInterestSuccess() {
    //given
    //관심사 이름과 키워드
    InterestRequest request = new InterestRequest("인공지능", List.of("AI", "머신러닝"));

    Interest entity = new Interest(UUID.randomUUID(), request.name(), request.keywords(), 0);
    InterestResponse response = new InterestResponse(entity.getId(), entity.getName(),
        entity.getKeywords(), entity.getSubscriberCount());

    when(interestMapper.toEntity(request)).thenReturn(entity);
    when(interestRepository.findAll()).thenReturn(List.of());
    when(interestRepository.save(entity)).thenReturn(entity);
    when(interestMapper.toResponse(any())).thenReturn(response);

    //when
    //관심사 등록 메서드 호출
    InterestResponse result = interestService.create(request);

    //then
    //리턴된 관심사 정보와 저장소 상태 검증
    assertThat(result.name()).isEqualTo(request.name());
    assertThat(result.keywords()).containsExactly("AI", "머신러닝");
    assertThat(result.subscriberCount()).isEqualTo(0);
  }

  @Test
  @DisplayName("유사한 이름있으면 등록 실패")
  void createInterestFail() {
    //given
    //"인공지능", "인공 지능" 등
    InterestRequest request = new InterestRequest("인공지능", List.of("AI", "머신러닝"));

    List<Interest> existing = List.of(
        new Interest("인공 지능", List.of("AI")),
        new Interest("딥러닝", List.of("Deep learning"))
    );

    when(interestRepository.findAll()).thenReturn(existing);
//    when(interestMapper.toEntity(request)).thenCallRealMethod();

    //when
    //중복 등록 시도
    assertThatThrownBy(() -> interestService.create(request))
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
    Interest existing = new Interest(id, "인공지능", List.of("AI", "머신러닝"), 0);

    InterestUpdateRequest request = new InterestUpdateRequest(List.of("ChatGPT", "딥러닝"));
    Interest updated = new Interest(id, "인공지능", request.keywords(), 0);
    InterestResponse response = new InterestResponse(id, "인공지능", request.keywords(), 0);

    when(interestRepository.findById(id)).thenReturn(Optional.of(existing));
    when(interestRepository.save(existing)).thenReturn(updated);
    when(interestMapper.toResponse(updated)).thenReturn(response);

    //when
    //키워드 수정 요청
    InterestResponse result = interestService.updateKeywords(id, request);

    //then
    //키워드 바뀌었는지 검증
    assertThat(result.keywords()).containsExactly("ChatGPT", "딥러닝");
  }

  @Test
  @DisplayName("관심사를 삭제할 수 있다.")
  void deleteInterestSuccess() {
    //given
    //등록된 관심사
    UUID id = UUID.randomUUID();
    Interest entity = new Interest(id, "인공지능", List.of("AI", "머신러닝"), 0);
    InterestResponse response = new InterestResponse(id, "인공지능", List.of("AI", "머신러닝"), 0);

    when(interestRepository.findById(id)).thenReturn(Optional.of(entity));
    doNothing().when(interestRepository).delete(entity);
    when(interestMapper.toResponse(any())).thenReturn(response);

    //when
    //삭제 요청
    InterestResponse result = interestService.delete(id);

    //than
    //저장소에서 삭제되었는지 확인
    assertThat(result.id()).isEqualTo(id);
    assertThat(result.name()).isEqualTo("인공지능");
    verify(interestRepository).delete(entity);
    verify(subscriptionMLogService).removeSubscriptionLog(id);
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

    InterestResponse r1 = new InterestResponse(i1.getId(), i1.getName(), i1.getKeywords(),
        i1.getSubscriberCount());
    InterestResponse r2 = new InterestResponse(i2.getId(), i2.getName(), i2.getKeywords(),
        i2.getSubscriberCount());

    when(interestRepository.findAll()).thenReturn(all);
    when(interestMapper.toResponse(i1)).thenReturn(r1);
    when(interestMapper.toResponse(i2)).thenReturn(r2);

    //when
    // 조건없는 조회
    List<InterestResponse> result = interestService.read(null, null);

    //than
    // 확인
    assertThat(result).containsExactly(r1, r2);
  }

  @Test
  @DisplayName("검색어와 정렬 조건에 따라 필터링 및 페이지된 관심사 목록을 변환한다.")
  void readWithPaginationFilterSort() {
    //given
    UUID userId = UUID.randomUUID();

    Interest i1 = new Interest(UUID.randomUUID(), "기후", List.of("온난화"), 20);
    Interest i2 = new Interest(UUID.randomUUID(), "기후위기", List.of("온난화", "지구온도상승"), 30);

    when(interestRepository.findAll()).thenReturn(List.of(i1, i2));
    when(subscriptionRepository.findByUserIdAndInterestId(eq(userId), any())).thenReturn(
        Optional.empty());

    //when
    PageResponse<InterestWithSubscriptionResponse> result = interestService.read(
        "온난화", "subscriberCount", "DESC", null, null, 2, userId
    );

    //then
    assertThat(result.content()).hasSize(2);
    assertThat(result.content().get(0).subscriberCount()).isEqualTo(30);
    assertThat(result.hasNext()).isFalse();
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

    InterestResponse r2 = new InterestResponse(i2.getId(), i2.getName(), i2.getKeywords(), 0);
    InterestResponse r3 = new InterestResponse(i3.getId(), i3.getName(), i3.getKeywords(), 0);

    when(interestRepository.findAll()).thenReturn(all);
    when(interestMapper.toResponse(i2)).thenReturn(r2);
    when(interestMapper.toResponse(i3)).thenReturn(r3);

    //when
    //검색어로 검색
    List<InterestResponse> result = interestService.read("환경", "name");

    //then
    //매칭되는 괌심사 반환
    assertThat(result).containsExactlyInAnyOrder(r2, r3);
  }
}
