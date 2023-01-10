# Vibecap backend : prototype

## architecture
<img src="/assets/image/architecture.jpeg" alt="architecture">

## test db

테스트 db는 [H2](https://github.com/h2database/h2database/releases/download/version-2.1.214/h2-2022-06-13.zip) 사용.

1. `~/Vibecap/test` db 파일 생성
2. `$ ./h2.sh` 실행

    * url : jdbc:h2:tcp://localhost/~/Vibecap/test
    * 사용자명 : sa
    * 비밀번호 : 0000