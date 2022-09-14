# 유아와 외국인을 위한 한국어 말하기 학습 앱 '가나다함께'

## 1. 개발 배경 및 목적
아동의 언어는 주로 또래 집단이나 부모님, 선생님과 같은 성인들과 대화하는 환경에서 시행착오를 바탕으로 발달하게 됩니다. 
하지만 코로나19 펜데믹 기간 중 사회적 거리두기와 마스크 착용 및 원격 수업의 영향으로 미취학 및 초등 저학년 아동이 
한글 해득에 어려움을 겪고 있습니다. 
시중에는 모바일 기반의 한글 교육 컨텐츠들이 이미 존재하나, 이들은 모두 유료 서비스이거나 올바른 답안을 지도할 보호자를 필요로 하는 형태입니다. 
이런 이유로 이들에게 도움이 될 학습 앱을 개발하게 되었습니다. <br>

‘가나다함께’ 는 AI를 기반으로 단어의 정확한 발음과 실제 음성을 비교하여 안내함으로써 보호자나 지도자 없이도 
한글의 발음을 습득할 수 있는 서비스를 제공하고자 합니다. 
부가적으로 한글 단어의 글자와 의미를 그림으로 배울 수 있습니다.

<br><br>

## 2. 개발 환경 및 개발 언어

### 서비스 아키텍처

Architecture 이미지

<br><br>



> <b>Android App 개발 환경 </b>  <br>

<br>

> <b>Server 개발 환경</b> <br>

<br>

> <b>Deeplearning 개발 환경</b> <br>

Colab Notebooks <br>
Tensorflow 2.8 <br>

<br><br>

### 딥러닝 모델 
> https://drive.google.com/drive/folders/146M8mq4q9ERBfql4MqaE2IC5bPoLiOv9 <br>

    ganada_A.h5 : 1단계 음성 분류 모델
    ganada_B.h5 : 2단계 음성 분류 모델
    ganada_C.h5 : 3단계 음성 분류 모델



### 음성 데이터
> 1 단계 : 아이, 화가, 뿌리 <br>
https://drive.google.com/drive/folders/17tfmA2f1XlZumJtv_SfuaKf7wMIXL9Ob <br>
    
> 2 단계 : 가수, 오리, 파도 <br>
https://drive.google.com/drive/folders/1QRby607veCuf5cc_6C3MvC_w5geRDqEy <br>
    
> 3 단계 : 부모, 까치, 의자 <br>
https://drive.google.com/drive/folders/1b7Rc7umnQEk2vQ4jzWbXZ7Lo-zcEUt5s <br>

<br><br>

## 3. 실행 가이드 & 프로젝트 주요 기능

### 앱 설치하기
아래 링크를 통해 apk 파일을 다운로드할 수 있습니다. 


> https://drive.google.com/file/d/1yIZ2XITRagGOwWTs6Jml8h4LkhWDFIlu/view?usp=sharing



다운로드 링크를 클릭하면 아래와 같이 경고가 뜨지만 [무시하고 다운로드] 버튼을 툴러 다운로드합니다. <br>
다운로드한 apk 파일을 실행시키면 설치가 시작됩니다. '설치' 링크를 눌러 설치를 진행합니다. <br>
설치가 완료되면 '열기' 링크를 클릭하여 실행합니다.

<img src="https://user-images.githubusercontent.com/63789657/189565466-9c2ada53-f0ae-4a82-80d3-b8e9f44fbe29.jpg" width="20%"> <img src="https://user-images.githubusercontent.com/63789657/189565474-dc22c336-ec57-43be-89a6-e1c901d606aa.jpg" width="20%">  <img src="https://user-images.githubusercontent.com/63789657/189565662-fb0d3ffd-1847-4d33-8afb-6ef6b5e419a4.jpg" width="20%">


### 앱 실행

'가나다함께' 앱을 실행하면 로고가 표시된 후 메인 화면이 뜹니다. <br>
메인 화면에서는 단계가 표시되며 아래로 스크롤링하여 이후 단계를 계속 볼 수 있습니다. <br>
단, 현재는 <받침이 없는 단어> 1 ~ 3 단계까지 실행 가능합니다. <br>


<img src="https://user-images.githubusercontent.com/63789657/189565944-a22f5500-10a8-4f03-bc2c-a7fa49ec0465.jpg" width="20%">  <img src="https://user-images.githubusercontent.com/63789657/189566072-942a9865-aa79-40a6-a5ff-8aa574d9de69.jpg" width="20%">   <img src="https://user-images.githubusercontent.com/63789657/189794766-d618a568-677c-46b2-9df7-5f38688fe9a9.jpg" width="20%">

숫자 1 ~3 중 하나를 눌러 문제를 풀어봅시다. <br>
<b>처음 문제 풀기</b> 페이지에 접근하면 두 가지를 <b>허용</b>해 주어야 합니다. <br>

<img src="https://user-images.githubusercontent.com/63789657/189617660-cac53cf8-455b-4413-bfcd-3af911effde0.jpg" width="20%">  <img src="https://user-images.githubusercontent.com/63789657/189617766-88318a44-a9b0-4b65-9a4a-d8a416e8c275.jpg" width="20%">  
<br>
                                                                                            
어린이들 또는 외국인을 위한 한글 배우기 앱으로, <b>문제 유형은 4가지</b>입니다. <br>
듣고 따라하기, 그림 보고 단어 고르기, 단어 보고 그림 고르기, 바르게 읽기 <br>
문제 유형이 랜덤하게 출제되며 5개의 문제를 풀고 결과를 확인합니다. <br>

<img src="https://user-images.githubusercontent.com/63789657/189794473-da72c279-0a45-47f1-8aec-84456d1c17bb.jpg" >
<br>

녹음 후 정답을 제출하면 정답 여부와 정확도를 안내합니다. <br>
그림 보고 단어 고르기, 단어 보고 알맞은 그림 고르기 등의 문제를 풉니다. <br><br>
<img src="https://user-images.githubusercontent.com/63789657/190048217-01fb3bcd-2b7c-4d7f-871c-b8992d57a3a1.jpg">
<br><br>


잘못된 정답을 고르면 안내를 하고, 다시 풀어보도록 안내합니다.  <br><br>
<img src="https://user-images.githubusercontent.com/63789657/189798934-d7ef0ac8-c780-44a6-9ced-27f138eb5567.jpg">
<br><br>

다섯 문제를 모두 풀면, 성취도 결과를 보여줍니다.<br><br>
<img src="https://user-images.githubusercontent.com/63789657/189799567-a0891f32-9ffe-48ae-a934-ee075fba5dd6.jpg">
<br><br>



---

## 4. 시연 및 기능 설명 영상
https://youtu.be/pm1B6-UV-SY 

<br><br>


## 5. 프로젝트 진행 관리
> 회의록, 작업 목록 관리 <br>
https://bitter-waterlily-190.notion.site/4972bf41c09d433692bbfc8a7cc29996

<br><br>

## 6. 팀구성원 
<img src="https://user-images.githubusercontent.com/63789657/190044449-92e0f7e4-714e-4a8f-b470-8e082801faf2.PNG" width="60%">



#### 김성은 Leader
> Android / Server / 소속 없음

#### 조보미 
> Deeplearning / 소속 없음

#### 김여진 
> Data / Design / 이화여자대학교 대학원 | 재학
