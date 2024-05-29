# 게임 체크리스트

----------

## [프로젝트 설명]



## [팀원]



## [프로젝트 구조]


### 서버


### 데이터베이스
- 테스트 : 로컬 MySql
- 메인 : AWS RDS MySql

### 구조

#### Auth
- Member의 Role을 활용해 접근 URL 제한
- Jwt와 Spring Security 사용
- 이메일 인증 사용(Redis)
- 로그아웃 시 해당 토큰 사용불가(Redis)

#### List 테이블
- 대량의 데이터 처리를 대비하여 DAILY, WEEKLY, MONTHLY 테이블로 나눔
- 단순 할일 체크 한번마다 관련 모든 리스트 확인 후 비율 표현하는 것은 비효율적이라 생각함 <br>하여 Achievement 테이블을 만들고 리스트에서 업데이트가 발생할 때마다 Achievement 업데이트
- 전체 조회 및 전체 삭제 처리 후 전체 insert 과정을 수행하도록 함 (list의 characterId에 index를 걸고 batch를 통해 실제 삭제로직 수행)
