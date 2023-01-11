# Vibecap backend : prototype

## 애플리케이션 GUI

[figma 화면 설계서](https://www.figma.com/file/NycTb1CTdhqTgJfcRAdnEo/GUI-%ED%99%94%EB%A9%B4%EC%84%A4%EA%B3%84%EC%84%9C-%EC%9D%B4%EC%8B%9D?node-id=0%3A1&t=HdeaObyKOnZ7fXJy-0)

## architecture
<img src="/assets/image/architecture.jpeg" alt="architecture">

## test db

테스트 db : [H2](https://github.com/h2database/h2database/releases/download/version-2.1.214/h2-2022-06-13.zip) 사용.

1. `~/Vibecap/test` db 파일 생성
2. `$ ./h2.sh` 실행

    * url : jdbc:h2:tcp://localhost/~/Vibecap/test
    * 사용자명 : sa
    * 비밀번호 : 0000