# Vibecap API server

[Vibecap](https://github.com/Vibecap/Vibecap_Android) 애플리케이션과 통신하는 서버

## 기능
* 회원가입, 로그인
* 커뮤니티 (게시글, 댓글/대댓글 작성, 좋아요, 알림)
* capture (사진, 사용자의 기분, 현재 상황과 관련된 플레이리스트 추천)

## 기술 스택
* application : `Spring Boot` `JPA`
* deployment : `AWS EC2` `AWS RDS` `Docker` `python`
* database : `MySQL`
* etc : `Google Cloud Vision` `YouTube Data`

## server architecture
<img src="/assets/image/server-architecture.png" alt="server-architecture diagram">

## contributors
* [Jieun Choi](https://github.com/cje172)
	* album, mypage 개발
	* Firebase storage 연결
* [SOO](https://github.com/LeeSuhyun58)
	* community (post, comment, like) 개발
* [Mingeun Park](https://github.com/mingeun2154)
	* member, notice 개발
	* 서버 구축, 배포
	* [demo web client](http://ec2-175-41-230-93.ap-northeast-1.compute.amazonaws.com:8080/wireframe/sample/demoClient.html) 개발

<img src="/assets/image/myteam.jpeg" alt="Vibecap 팀원">

## release

