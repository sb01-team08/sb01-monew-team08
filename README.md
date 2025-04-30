# 📰 MONEW(모두의 뉴스): 맞춤형 뉴스 및 소셜 플랫폼 서비스

![Git](https://img.shields.io/badge/git-%23F05033.svg?style=for-the-badge&logo=git&logoColor=white)
![IntelliJ IDEA](https://img.shields.io/badge/IntelliJIDEA-000000.svg?style=for-the-badge&logo=intellij-idea&logoColor=white)
![GitHub](https://img.shields.io/badge/github-%23121011.svg?style=for-the-badge&logo=github&logoColor=white)
![GitHub Actions](https://img.shields.io/badge/github%20actions-%232671E5.svg?style=for-the-badge&logo=githubactions&logoColor=white)
![AWS](https://img.shields.io/badge/AWS-%23FF9900.svg?style=for-the-badge&logo=amazon-aws&logoColor=white)
![Amazon S3](https://img.shields.io/badge/Amazon%20S3-FF9900?style=for-the-badge&logo=amazons3&logoColor=white)
![Postgres](https://img.shields.io/badge/postgres-%23316192.svg?style=for-the-badge&logo=postgresql&logoColor=white)
![Spring](https://img.shields.io/badge/spring-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white)
![Swagger](https://img.shields.io/badge/-Swagger-%23Clojure?style=for-the-badge&logo=swagger&logoColor=white)

<br>

[![codecov - dev](https://codecov.io/gh/sb01-team08/sb01-monew-team08/branch/dev/graph/badge.svg?token=76DB0FHY5X)](https://codecov.io/gh/sb01-team08/sb01-monew-team08/branch/dev)

---

### 📝 협업 노션 링크

[협업 노션 링크](https://fortunate-tiger-b01.notion.site/SB01_8-1d797c15da0880bab32bfee993b16f60?pvs=4)

### 🌐 구현 링크

[구현 링크](http://52.79.248.10/)

## 프로젝트 소개

여러 뉴스 API(네이버, 조선, 한경 등)를 통합하여 사용자에게 최적화 된 뉴스를 제공하고, 댓글 기능을 통해 의견을 나눌 수 있는 소셜 뉴스 플랫폼

## 개발 기간

2025.04.16-

## 기술 스택

## 팀원

| 이름   | 담당                                               | Github                                      |
| ------ | -------------------------------------------------- | ------------------------------------------- |
| 한상은 | 팀장 / 인프라 / 사용자, 사용자 활동 내역 관리 기능 | [silvarge](https://github.com/silvarge)     |
| 김창우 | 테스트 / 댓글, 알림 관리 기능                      | [cwkim](https://github.com/qwertyuiop4m)    |
| 박태식 | 관심사 기능                                        | [martin](https://github.com/martinP-Ghub)   |
| 신은섭 | DB / 뉴스 기사 관리 기능                           | [Eunseob Shin](https://github.com/eunseobb) |

## 팀원 별 구현 기능 상세

### 한상은

### 김창우

### 박태식

### 신은섭

## 파일 구조

## 프로젝트 자료

```
sb01-monew-team08
├─ .coderabbit.yaml
├─ .dockerignore
├─ codecov.yml
├─ config
│  └─ checkstyle
│     └─ intellij-java-google-style.xml
├─ docker-compose.yml
├─ Dockerfile
├─ gradle
│  └─ wrapper
│     └─ gradle-wrapper.properties
├─ gradlew
├─ gradlew.bat
├─ HELP.md
├─ README.md
└─ src
   ├─ main
   │  ├─ java
   │  │  └─ com
   │  │     └─ example
   │  │        └─ monewteam08
   │  │           ├─ common
   │  │           │  ├─ CustomApiResponse.java
   │  │           │  └─ RegexPatternConstants.java
   │  │           ├─ config
   │  │           │  ├─ AuthenticatedUserInterceptor.java
   │  │           │  ├─ AwsS3Config.java
   │  │           │  ├─ BackupS3Properties.java
   │  │           │  ├─ JacConfig.java
   │  │           │  ├─ QuerydslConfig.java
   │  │           │  ├─ RequestTracingFilter.java
   │  │           │  ├─ RestTemplateConfig.java
   │  │           │  ├─ SwaggerConfig.java
   │  │           │  └─ WebConfig.java
   │  │           ├─ controller
   │  │           │  ├─ api
   │  │           │  │  ├─ ArticleControllerDocs.java
   │  │           │  │  ├─ CommentControllerDocs.java
   │  │           │  │  ├─ InterestControllerDocs.java
   │  │           │  │  ├─ NotificationControllerDocs.java
   │  │           │  │  ├─ UserActivityLogControllerDocs.java
   │  │           │  │  └─ UserControllerDocs.java
   │  │           │  ├─ ArticleController.java
   │  │           │  ├─ CommentController.java
   │  │           │  ├─ IndexViewController.java
   │  │           │  ├─ InterestController.java
   │  │           │  ├─ NotificationController.java
   │  │           │  ├─ UserActivityLogController.java
   │  │           │  └─ UserContoller.java
   │  │           ├─ dto
   │  │           │  ├─ request
   │  │           │  │  ├─ comment
   │  │           │  │  │  ├─ CommentRegisterRequest.java
   │  │           │  │  │  └─ CommentUpdateRequest.java
   │  │           │  │  ├─ Interest
   │  │           │  │  │  ├─ InterestRequest.java
   │  │           │  │  │  └─ InterestUpdateRequest.java
   │  │           │  │  └─ user
   │  │           │  │     ├─ UserLoginRequest.java
   │  │           │  │     ├─ UserRequest.java
   │  │           │  │     └─ UserUpdateRequest.java
   │  │           │  └─ response
   │  │           │     ├─ article
   │  │           │     │  ├─ ArticleDto.java
   │  │           │     │  ├─ ArticleInterestCount.java
   │  │           │     │  ├─ ArticleRestoreResultDto.java
   │  │           │     │  ├─ ArticleViewDto.java
   │  │           │     │  ├─ CursorPageResponseArticleDto.java
   │  │           │     │  ├─ FilteredArticleDto.java
   │  │           │     │  ├─ item
   │  │           │     │  │  └─ NaverNewsItem.java
   │  │           │     │  └─ NaverNewsResponse.java
   │  │           │     ├─ comment
   │  │           │     │  ├─ CommentDto.java
   │  │           │     │  ├─ CommentLikeDto.java
   │  │           │     │  └─ CursorPageResponseCommentDto.java
   │  │           │     ├─ interest
   │  │           │     │  ├─ InterestResponse.java
   │  │           │     │  ├─ InterestWithSubscriptionResponse.java
   │  │           │     │  ├─ PageResponse.java
   │  │           │     │  └─ UserActivitySubscriptionResponse.java
   │  │           │     ├─ notification
   │  │           │     │  ├─ CursorPageResponseNotificationDto.java
   │  │           │     │  └─ NotificationDto.java
   │  │           │     ├─ user
   │  │           │     │  └─ UserResponse.java
   │  │           │     └─ useractivitylog
   │  │           │        ├─ CommentLikeLogResponse.java
   │  │           │        ├─ CommentRecentLogResponse.java
   │  │           │        ├─ NewsViewLogResponse.java
   │  │           │        └─ UserActivityLogResponse.java
   │  │           ├─ entity
   │  │           │  ├─ Article.java
   │  │           │  ├─ ArticleView.java
   │  │           │  ├─ Comment.java
   │  │           │  ├─ CommentLike.java
   │  │           │  ├─ CommentLikeLog.java
   │  │           │  ├─ CommentRecentLog.java
   │  │           │  ├─ Interest.java
   │  │           │  ├─ NewsViewLog.java
   │  │           │  ├─ Notification.java
   │  │           │  ├─ ResourceType.java
   │  │           │  ├─ Subscription.java
   │  │           │  ├─ User.java
   │  │           │  └─ UserActivityLog.java
   │  │           ├─ event
   │  │           │  ├─ UserLoginEvent.java
   │  │           │  └─ UserLoginEventListener.java
   │  │           ├─ exception
   │  │           │  ├─ article
   │  │           │  │  ├─ ArticleExportFailedException.java
   │  │           │  │  ├─ ArticleFetchFailedException.java
   │  │           │  │  └─ ArticleNotFoundException.java
   │  │           │  ├─ comment
   │  │           │  │  ├─ CommentException.java
   │  │           │  │  ├─ CommentNotFoundException.java
   │  │           │  │  └─ UnauthorizedCommentAccessException.java
   │  │           │  ├─ ErrorCode.java
   │  │           │  ├─ ExceptionDto.java
   │  │           │  ├─ GlobalExceptionHandler.java
   │  │           │  ├─ Interest
   │  │           │  │  ├─ DuplicateInterestException.java
   │  │           │  │  ├─ InterestException.java
   │  │           │  │  └─ InterestNotFoundException.java
   │  │           │  ├─ MonewException.java
   │  │           │  ├─ Subscription
   │  │           │  │  ├─ AlreadySubscribedException.java
   │  │           │  │  ├─ SubscriptionException.java
   │  │           │  │  └─ SubscriptionNotFoundException.java
   │  │           │  ├─ user
   │  │           │  │  ├─ DeletedAccountException.java
   │  │           │  │  ├─ EmailAlreadyExistException.java
   │  │           │  │  ├─ InvalidUserIdRequestHeaderFormatException.java
   │  │           │  │  ├─ LoginFailedException.java
   │  │           │  │  ├─ MissingUserIdRequestHeaderException.java
   │  │           │  │  ├─ UserException.java
   │  │           │  │  └─ UserNotFoundException.java
   │  │           │  └─ useractivitylog
   │  │           │     ├─ UserActicityLogNotFoundException.java
   │  │           │     └─ UserActivityLogException.java
   │  │           ├─ mapper
   │  │           │  ├─ ArticleMapper.java
   │  │           │  ├─ ArticleViewMapper.java
   │  │           │  ├─ CommentLikeLogMapper.java
   │  │           │  ├─ CommentLikeMapper.java
   │  │           │  ├─ CommentMapper.java
   │  │           │  ├─ CommentRecentLogMapper.java
   │  │           │  ├─ InterestMapper.java
   │  │           │  ├─ NewsViewLogMapper.java
   │  │           │  ├─ NotificationMapper.java
   │  │           │  ├─ SubscriptionMapper.java
   │  │           │  ├─ UserActivityLogMapper.java
   │  │           │  └─ UserMapper.java
   │  │           ├─ MonewTeam08Application.java
   │  │           ├─ repository
   │  │           │  ├─ ArticleRepository.java
   │  │           │  ├─ ArticleRepositoryCustom.java
   │  │           │  ├─ ArticleRepositoryCustomImpl.java
   │  │           │  ├─ ArticleViewRepository.java
   │  │           │  ├─ CommentLikeLogRepository.java
   │  │           │  ├─ CommentLikeRepository.java
   │  │           │  ├─ CommentRecentLogRepository.java
   │  │           │  ├─ CommentRepository.java
   │  │           │  ├─ CommentRepositoryCustom.java
   │  │           │  ├─ CommentRepositoryImpl.java
   │  │           │  ├─ InterestRepository.java
   │  │           │  ├─ NewsViewLogRepository.java
   │  │           │  ├─ NotificationRepository.java
   │  │           │  ├─ NotificationRepositoryCustom.java
   │  │           │  ├─ NotificationRepositoryCustomImpl.java
   │  │           │  ├─ SubscriptionRepository.java
   │  │           │  ├─ UserActivityLogRepository.java
   │  │           │  └─ UserRepository.java
   │  │           ├─ scheduler
   │  │           │  ├─ ArticleScheduler.java
   │  │           │  └─ NotificationCleanupScheduler.java
   │  │           ├─ service
   │  │           │  ├─ impl
   │  │           │  │  ├─ ArticleBackupServiceImpl.java
   │  │           │  │  ├─ ArticleFetchServiceImpl.java
   │  │           │  │  ├─ ArticleServiceImpl.java
   │  │           │  │  ├─ ArticleViewServiceImpl.java
   │  │           │  │  ├─ CommentLikeLogServiceImpl.java
   │  │           │  │  ├─ CommentLikeServiceImpl.java
   │  │           │  │  ├─ CommentRecentLogServiceImpl.java
   │  │           │  │  ├─ CommentServiceImpl.java
   │  │           │  │  ├─ CsvServiceImpl.java
   │  │           │  │  ├─ InterestServiceImpl.java
   │  │           │  │  ├─ NewsViewLogServiceImpl.java
   │  │           │  │  ├─ NotificationServiceImpl.java
   │  │           │  │  ├─ S3ServiceImpl.java
   │  │           │  │  ├─ SubscriptionServiceImpl.java
   │  │           │  │  ├─ UserActivityLogServiceImpl.java
   │  │           │  │  └─ UserServiceImpl.java
   │  │           │  └─ Interface
   │  │           │     ├─ ArticleBackupService.java
   │  │           │     ├─ ArticleFetchService.java
   │  │           │     ├─ ArticleService.java
   │  │           │     ├─ ArticleViewService.java
   │  │           │     ├─ CommentLikeLogService.java
   │  │           │     ├─ CommentLikeService.java
   │  │           │     ├─ CommentRecentLogService.java
   │  │           │     ├─ CommentService.java
   │  │           │     ├─ CsvService.java
   │  │           │     ├─ InterestService.java
   │  │           │     ├─ NewsViewLogService.java
   │  │           │     ├─ NotificationService.java
   │  │           │     ├─ S3Service.java
   │  │           │     ├─ SubscriptionService.java
   │  │           │     ├─ UserActivityLogService.java
   │  │           │     └─ UserService.java
   │  │           └─ util
   │  │              └─ SimilarityUtil.java
   │  └─ resources
   │     ├─ logback-spring.xml
   │     └─ static
   │        ├─ assets
   │        │  ├─ index-D30UMZL2.css
   │        │  └─ index-DMbEghNi.js
   │        ├─ favicon.ico
   │        └─ index.html
   └─ test
      ├─ java
      │  └─ com
      │     └─ example
      │        └─ monewteam08
      │           ├─ controller
      │           │  ├─ ArticleControllerTest.java
      │           │  ├─ CommentControllerTest.java
      │           │  ├─ InterestControllerTest.java
      │           │  ├─ NotificationControllerTest.java
      │           │  └─ UserContollerTest.java
      │           ├─ entity
      │           │  └─ UserTest.java
      │           ├─ mapper
      │           │  ├─ InterestMapperTest.java
      │           │  └─ UserMapperTest.java
      │           ├─ MonewTeam08ApplicationTests.java
      │           ├─ repository
      │           │  ├─ ArticleRepositoryCustomTest.java
      │           │  ├─ ArticleRepositoryTest.java
      │           │  ├─ ArticleViewRepositoryTest.java
      │           │  ├─ CommentLikeLogRepositoryTest.java
      │           │  ├─ CommentLikeRepositoryTest.java
      │           │  ├─ CommentRecentLogRepositoryTest.java
      │           │  ├─ CommentRepositoryQuerydslTest.java
      │           │  ├─ CommentRepositoryTest.java
      │           │  ├─ NotificationRepositoryCustomImplTest.java
      │           │  ├─ NotificationRepositoryTest.java
      │           │  ├─ UserActivityLogRepositoryTest.java
      │           │  └─ UserRepositoryTest.java
      │           └─ service
      │              └─ impl
      │                 ├─ ArticleBackupServiceTest.java
      │                 ├─ ArticleFetchServiceTest.java
      │                 ├─ ArticleServiceTest.java
      │                 ├─ ArticleViewServiceImplTest.java
      │                 ├─ CommentLikeLogServiceImplTest.java
      │                 ├─ CommentLikeServiceImplTest.java
      │                 ├─ CommentRecentLogServiceImplTest.java
      │                 ├─ CommentServiceImplTest.java
      │                 ├─ CsvServiceTest.java
      │                 ├─ InterestServiceImplTest.java
      │                 ├─ NewsViewLogServiceImplTest.java
      │                 ├─ NotificationServiceImplTest.java
      │                 ├─ S3ServiceImplTest.java
      │                 ├─ SubscriptionServiceImplTest.java
      │                 ├─ UserActivityLogServiceImplTest.java
      │                 └─ UserServiceImplTest.java
      └─ resources
         ├─ application-test.yml
         ├─ application.yml
         └─ schema.sql

```
```
sb01-monew-team08
├─ .coderabbit.yaml
├─ .dockerignore
├─ codecov.yml
├─ config
│  └─ checkstyle
│     └─ intellij-java-google-style.xml
├─ docker-compose.yml
├─ Dockerfile
├─ gradle
│  └─ wrapper
│     └─ gradle-wrapper.properties
├─ gradlew
├─ gradlew.bat
├─ HELP.md
├─ README.md
└─ src
   ├─ main
   │  ├─ java
   │  │  └─ com
   │  │     └─ example
   │  │        └─ monewteam08
   │  │           ├─ common
   │  │           │  ├─ CustomApiResponse.java
   │  │           │  └─ RegexPatternConstants.java
   │  │           ├─ config
   │  │           │  ├─ AuthenticatedUserInterceptor.java
   │  │           │  ├─ AwsS3Config.java
   │  │           │  ├─ BackupS3Properties.java
   │  │           │  ├─ JacConfig.java
   │  │           │  ├─ QuerydslConfig.java
   │  │           │  ├─ RequestTracingFilter.java
   │  │           │  ├─ RestTemplateConfig.java
   │  │           │  ├─ SwaggerConfig.java
   │  │           │  └─ WebConfig.java
   │  │           ├─ controller
   │  │           │  ├─ api
   │  │           │  │  ├─ ArticleControllerDocs.java
   │  │           │  │  ├─ CommentControllerDocs.java
   │  │           │  │  ├─ InterestControllerDocs.java
   │  │           │  │  ├─ NotificationControllerDocs.java
   │  │           │  │  ├─ UserActivityLogControllerDocs.java
   │  │           │  │  └─ UserControllerDocs.java
   │  │           │  ├─ ArticleController.java
   │  │           │  ├─ CommentController.java
   │  │           │  ├─ IndexViewController.java
   │  │           │  ├─ InterestController.java
   │  │           │  ├─ NotificationController.java
   │  │           │  ├─ UserActivityLogController.java
   │  │           │  └─ UserContoller.java
   │  │           ├─ dto
   │  │           │  ├─ request
   │  │           │  │  ├─ comment
   │  │           │  │  │  ├─ CommentRegisterRequest.java
   │  │           │  │  │  └─ CommentUpdateRequest.java
   │  │           │  │  ├─ Interest
   │  │           │  │  │  ├─ InterestRequest.java
   │  │           │  │  │  └─ InterestUpdateRequest.java
   │  │           │  │  └─ user
   │  │           │  │     ├─ UserLoginRequest.java
   │  │           │  │     ├─ UserRequest.java
   │  │           │  │     └─ UserUpdateRequest.java
   │  │           │  └─ response
   │  │           │     ├─ article
   │  │           │     │  ├─ ArticleDto.java
   │  │           │     │  ├─ ArticleInterestCount.java
   │  │           │     │  ├─ ArticleRestoreResultDto.java
   │  │           │     │  ├─ ArticleViewDto.java
   │  │           │     │  ├─ CursorPageResponseArticleDto.java
   │  │           │     │  ├─ FilteredArticleDto.java
   │  │           │     │  ├─ item
   │  │           │     │  │  └─ NaverNewsItem.java
   │  │           │     │  └─ NaverNewsResponse.java
   │  │           │     ├─ comment
   │  │           │     │  ├─ CommentDto.java
   │  │           │     │  ├─ CommentLikeDto.java
   │  │           │     │  └─ CursorPageResponseCommentDto.java
   │  │           │     ├─ interest
   │  │           │     │  ├─ InterestResponse.java
   │  │           │     │  ├─ InterestWithSubscriptionResponse.java
   │  │           │     │  ├─ PageResponse.java
   │  │           │     │  └─ UserActivitySubscriptionResponse.java
   │  │           │     ├─ notification
   │  │           │     │  ├─ CursorPageResponseNotificationDto.java
   │  │           │     │  └─ NotificationDto.java
   │  │           │     ├─ user
   │  │           │     │  └─ UserResponse.java
   │  │           │     └─ useractivitylog
   │  │           │        ├─ CommentLikeLogResponse.java
   │  │           │        ├─ CommentRecentLogResponse.java
   │  │           │        ├─ NewsViewLogResponse.java
   │  │           │        └─ UserActivityLogResponse.java
   │  │           ├─ entity
   │  │           │  ├─ Article.java
   │  │           │  ├─ ArticleView.java
   │  │           │  ├─ Comment.java
   │  │           │  ├─ CommentLike.java
   │  │           │  ├─ CommentLikeLog.java
   │  │           │  ├─ CommentRecentLog.java
   │  │           │  ├─ Interest.java
   │  │           │  ├─ NewsViewLog.java
   │  │           │  ├─ Notification.java
   │  │           │  ├─ ResourceType.java
   │  │           │  ├─ Subscription.java
   │  │           │  ├─ User.java
   │  │           │  └─ UserActivityLog.java
   │  │           ├─ event
   │  │           │  ├─ UserLoginEvent.java
   │  │           │  └─ UserLoginEventListener.java
   │  │           ├─ exception
   │  │           │  ├─ article
   │  │           │  │  ├─ ArticleExportFailedException.java
   │  │           │  │  ├─ ArticleFetchFailedException.java
   │  │           │  │  └─ ArticleNotFoundException.java
   │  │           │  ├─ comment
   │  │           │  │  ├─ CommentException.java
   │  │           │  │  ├─ CommentNotFoundException.java
   │  │           │  │  └─ UnauthorizedCommentAccessException.java
   │  │           │  ├─ ErrorCode.java
   │  │           │  ├─ ExceptionDto.java
   │  │           │  ├─ GlobalExceptionHandler.java
   │  │           │  ├─ Interest
   │  │           │  │  ├─ DuplicateInterestException.java
   │  │           │  │  ├─ InterestException.java
   │  │           │  │  └─ InterestNotFoundException.java
   │  │           │  ├─ MonewException.java
   │  │           │  ├─ Subscription
   │  │           │  │  ├─ AlreadySubscribedException.java
   │  │           │  │  ├─ SubscriptionException.java
   │  │           │  │  └─ SubscriptionNotFoundException.java
   │  │           │  ├─ user
   │  │           │  │  ├─ DeletedAccountException.java
   │  │           │  │  ├─ EmailAlreadyExistException.java
   │  │           │  │  ├─ InvalidUserIdRequestHeaderFormatException.java
   │  │           │  │  ├─ LoginFailedException.java
   │  │           │  │  ├─ MissingUserIdRequestHeaderException.java
   │  │           │  │  ├─ UserException.java
   │  │           │  │  └─ UserNotFoundException.java
   │  │           │  └─ useractivitylog
   │  │           │     ├─ UserActicityLogNotFoundException.java
   │  │           │     └─ UserActivityLogException.java
   │  │           ├─ mapper
   │  │           │  ├─ ArticleMapper.java
   │  │           │  ├─ ArticleViewMapper.java
   │  │           │  ├─ CommentLikeLogMapper.java
   │  │           │  ├─ CommentLikeMapper.java
   │  │           │  ├─ CommentMapper.java
   │  │           │  ├─ CommentRecentLogMapper.java
   │  │           │  ├─ InterestMapper.java
   │  │           │  ├─ NewsViewLogMapper.java
   │  │           │  ├─ NotificationMapper.java
   │  │           │  ├─ SubscriptionMapper.java
   │  │           │  ├─ UserActivityLogMapper.java
   │  │           │  └─ UserMapper.java
   │  │           ├─ MonewTeam08Application.java
   │  │           ├─ repository
   │  │           │  ├─ ArticleRepository.java
   │  │           │  ├─ ArticleRepositoryCustom.java
   │  │           │  ├─ ArticleRepositoryCustomImpl.java
   │  │           │  ├─ ArticleViewRepository.java
   │  │           │  ├─ CommentLikeLogRepository.java
   │  │           │  ├─ CommentLikeRepository.java
   │  │           │  ├─ CommentRecentLogRepository.java
   │  │           │  ├─ CommentRepository.java
   │  │           │  ├─ CommentRepositoryCustom.java
   │  │           │  ├─ CommentRepositoryImpl.java
   │  │           │  ├─ InterestRepository.java
   │  │           │  ├─ NewsViewLogRepository.java
   │  │           │  ├─ NotificationRepository.java
   │  │           │  ├─ NotificationRepositoryCustom.java
   │  │           │  ├─ NotificationRepositoryCustomImpl.java
   │  │           │  ├─ SubscriptionRepository.java
   │  │           │  ├─ UserActivityLogRepository.java
   │  │           │  └─ UserRepository.java
   │  │           ├─ scheduler
   │  │           │  ├─ ArticleScheduler.java
   │  │           │  └─ NotificationCleanupScheduler.java
   │  │           ├─ service
   │  │           │  ├─ impl
   │  │           │  │  ├─ ArticleBackupServiceImpl.java
   │  │           │  │  ├─ ArticleFetchServiceImpl.java
   │  │           │  │  ├─ ArticleServiceImpl.java
   │  │           │  │  ├─ ArticleViewServiceImpl.java
   │  │           │  │  ├─ CommentLikeLogServiceImpl.java
   │  │           │  │  ├─ CommentLikeServiceImpl.java
   │  │           │  │  ├─ CommentRecentLogServiceImpl.java
   │  │           │  │  ├─ CommentServiceImpl.java
   │  │           │  │  ├─ CsvServiceImpl.java
   │  │           │  │  ├─ InterestServiceImpl.java
   │  │           │  │  ├─ NewsViewLogServiceImpl.java
   │  │           │  │  ├─ NotificationServiceImpl.java
   │  │           │  │  ├─ S3ServiceImpl.java
   │  │           │  │  ├─ SubscriptionServiceImpl.java
   │  │           │  │  ├─ UserActivityLogServiceImpl.java
   │  │           │  │  └─ UserServiceImpl.java
   │  │           │  └─ Interface
   │  │           │     ├─ ArticleBackupService.java
   │  │           │     ├─ ArticleFetchService.java
   │  │           │     ├─ ArticleService.java
   │  │           │     ├─ ArticleViewService.java
   │  │           │     ├─ CommentLikeLogService.java
   │  │           │     ├─ CommentLikeService.java
   │  │           │     ├─ CommentRecentLogService.java
   │  │           │     ├─ CommentService.java
   │  │           │     ├─ CsvService.java
   │  │           │     ├─ InterestService.java
   │  │           │     ├─ NewsViewLogService.java
   │  │           │     ├─ NotificationService.java
   │  │           │     ├─ S3Service.java
   │  │           │     ├─ SubscriptionService.java
   │  │           │     ├─ UserActivityLogService.java
   │  │           │     └─ UserService.java
   │  │           └─ util
   │  │              └─ SimilarityUtil.java
   │  └─ resources
   │     ├─ logback-spring.xml
   │     └─ static
   │        ├─ assets
   │        │  ├─ index-D30UMZL2.css
   │        │  └─ index-DMbEghNi.js
   │        ├─ favicon.ico
   │        └─ index.html
   └─ test
      ├─ java
      │  └─ com
      │     └─ example
      │        └─ monewteam08
      │           ├─ controller
      │           │  ├─ ArticleControllerTest.java
      │           │  ├─ CommentControllerTest.java
      │           │  ├─ InterestControllerTest.java
      │           │  ├─ NotificationControllerTest.java
      │           │  └─ UserContollerTest.java
      │           ├─ entity
      │           │  └─ UserTest.java
      │           ├─ mapper
      │           │  ├─ InterestMapperTest.java
      │           │  └─ UserMapperTest.java
      │           ├─ MonewTeam08ApplicationTests.java
      │           ├─ repository
      │           │  ├─ ArticleRepositoryCustomTest.java
      │           │  ├─ ArticleRepositoryTest.java
      │           │  ├─ ArticleViewRepositoryTest.java
      │           │  ├─ CommentLikeLogRepositoryTest.java
      │           │  ├─ CommentLikeRepositoryTest.java
      │           │  ├─ CommentRecentLogRepositoryTest.java
      │           │  ├─ CommentRepositoryQuerydslTest.java
      │           │  ├─ CommentRepositoryTest.java
      │           │  ├─ NotificationRepositoryCustomImplTest.java
      │           │  ├─ NotificationRepositoryTest.java
      │           │  ├─ UserActivityLogRepositoryTest.java
      │           │  └─ UserRepositoryTest.java
      │           └─ service
      │              └─ impl
      │                 ├─ ArticleBackupServiceTest.java
      │                 ├─ ArticleFetchServiceTest.java
      │                 ├─ ArticleServiceTest.java
      │                 ├─ ArticleViewServiceImplTest.java
      │                 ├─ CommentLikeLogServiceImplTest.java
      │                 ├─ CommentLikeServiceImplTest.java
      │                 ├─ CommentRecentLogServiceImplTest.java
      │                 ├─ CommentServiceImplTest.java
      │                 ├─ CsvServiceTest.java
      │                 ├─ InterestServiceImplTest.java
      │                 ├─ NewsViewLogServiceImplTest.java
      │                 ├─ NotificationServiceImplTest.java
      │                 ├─ S3ServiceImplTest.java
      │                 ├─ SubscriptionServiceImplTest.java
      │                 ├─ UserActivityLogServiceImplTest.java
      │                 └─ UserServiceImplTest.java
      └─ resources
         ├─ application-test.yml
         ├─ application.yml
         └─ schema.sql

```