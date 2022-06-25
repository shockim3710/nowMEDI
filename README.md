# 앱 ‘지금이약’과 메디슨 박스 - 건강해조
![image](https://user-images.githubusercontent.com/60650967/175776035-fc8db31d-47dd-42b1-8044-a13868ac35f4.png)
![image](https://user-images.githubusercontent.com/60650967/175776042-628d671e-2e9f-44a6-bfb6-c73a5eff7057.png)



## Description

> 캡스톤디자인II 팀프로젝트 및 졸업작품

> 2022.03.10 ~ 2022.06.09  



## Project Background and Objectives

![image](https://user-images.githubusercontent.com/60650967/175776065-137b3652-8b98-4de1-90f3-4cc8881ff5a2.png)
* 위의 그래프는 2011년 한국환자단체연합회에서 처방된 약을 모두 먹지 않은 이유를 조사한 것이다. 전체의 44%가 약 먹는 것을 잊어버려서 약이 남거나 복용 시기를 놓친 것을 확인할 수 있다. 이처럼 약을 장기간 먹다 보면 종종 약을 먹었는지 안 먹었는지 헷갈리거나, 깜빡하고 잊어버리는 경우가 생긴다.  


* 약을 통해 질병을 치료하는 만성질환자 또한 환자의 복약 순응도가 매우 중요하다. 하지만 서울대 간호대에서 1만3,590건 의약품 복용 건수를 분석한 결과, 만성질환을 앓고 있는 환자 중 30%가 약을 제때 먹지 않는다고 한다. 이러한 만성질환자는 나이별로 보았을 때 고연령층인 노인의 비율이 높았다.  


![image](https://user-images.githubusercontent.com/60650967/175776090-b4c21b90-dec4-4d4a-9fc6-9bb5bd032fea.png)
* 2017 보건복지부 노인 실태 조사 보고서에 따르면 노인이 만성질환을 1개, 2개, 3개 이상 보유하고 있는 비율이 각각 90%, 73%, 51%라고 한다. 노인이 복용하는 약물의 개수 또한 평균 4개로 많은 것을 확인할 수 있었다. 따라서 복용한 약의 개수도 많고, 그 약을 제때 섭취를 하는 것이 중요하지만 잘 잊어버린다는 것을 알 수 있었다.  


* 본 프로젝트는 일반 장기 복용자들이 복용을 깜빡하거나, 더 나아가 고연령층과 만성질환자들의 복용 망각을 방지하기 위해 애플리케이션과 메디슨 박스를 구현할 것이다. 약을 먹지 않으면 사용자나 보호자에게 알림을 보내고, 직관적으로 약의 복용 상태를 확인함으로써 복용 망각을 방지할 수 있도록 도움을 줄 것이다.

## Summary
* 애플리케이션은 사용자가 복용할 약의 타이머를 설정할 수 있다. 복용할 시간이 되면 알림이 뜨고, 사용자가 약을 먹지 않으면 5분 간격으로 3번의 재 알림을 받는다. 3번의 재 알림을 받은 후에도 사용자가 약을 먹지 않으면 지정한 보호자의 연락처로 문자를 전송하여 보호자가 직접 지도할 수 있도록 한다. 만약 사용자가 약을 먹었으면 먹은 시간과 날짜가 저장되어 확인할 수 있다.  


* 메디슨 박스에는 사용자가 복용할 약을 칸마다 보관할 수 있다. 보관된 칸은 애플리케이션으로 저장하였던 시간에 따라 돌아가며 사용자가 복용할 수 있도록 메디슨 박스 바깥에 튀어나오고, LED가 켜지게 된다. 만약 사용자가 약을 먹었으면 메디슨 박스에 버튼을 눌러 애플리케이션에 전송하고, LED는 꺼지게 된다.


## System Configuration Diagram
![image](https://user-images.githubusercontent.com/60650967/175776321-a44a8a53-c0bf-4d51-8d5d-d3b821a2ee85.png)
* 앱 ‘지금이약’은 안드로이드 앱 제작을 위한 공식 통합 개발 환경인 Android Studio로 제작하였다. 데이터베이스는 Android Studio의 로컬 DB인 SQLite를 사용하여 약 알람 정보 및 보호자 정보를 저장한다.  

* 메디슨 박스는 Autodesk Fusion 360로 도면을 설계하여 3D프린터로 구현한다. 구현한 박스에 단일 보드 마이크로컨트롤러인 Arduino를 장착하여 블루투스로 애플리케이션과 통신한다. 애플리케이션에 저장된 약 알람 정보를 수신하여 동작하고, 복용 여부를 애플리케이션에 송신한다.  


## System Spec
| * | 애플리케이션 |
|:------:| :- |
| 앱 이름 | 지금이약 (now MEDI) |
| 운영체제 | Android |
| 필요한 OS | Android 7.0 누가 (API Level 24) 이상 |
| 앱 권한 | - 다른 앱 위에 표시 </br>- 배터리 사용량 최적화 중지</br>- 블루투스 근처 기기 연결 및 위치 파악 </br>- SMS 메시지 전송 및 보기 |
| 화면 설계 | Figma |
| IDE | Android Studio |
| 프로그래밍 언어 | Java |
| 데이터베이스 | SQLite |


| * | 메디슨 박스 |
|:------:| :- |
| 모델설계 | Autodesk Fusion 360 |
| 모델 제작 | FDM 3D프린터 |
| 동작 보드 | Arduino |
| 핵심 부품 | - 블루투스 모듈 </br>- 스위치 </br>- RED LED </br>- 스텝모터 </br>- 1.5V AA 6칸 배터리 홀더 |
| 앱과의 통신 모듈 | HC-06 블루투스 2.0 모듈 |
| IDE | Arduino IDE |
| 프로그래밍 언어 | C/C++ |

## Use Case Diagram
* 유스 케이스 다이어그램으로 앱, 메디슨 박스 사용자인 액터와 시스템과의 상호작용을 나타내었다.
![캡스톤 유스캐이스 다이어그램 drawio](https://user-images.githubusercontent.com/60650967/175776699-36ea3157-22fd-4e14-91f4-653b417ea1e5.png)

## Sequence Diagram
* 시퀀스 다이어그램으로 시스템이 실행 시 생성되고 소멸하는 객체를 표기하고, 객체들 사이에 주고받는 메시지를 나타내었다.
![image](https://user-images.githubusercontent.com/60650967/175776893-fc4aa7ef-4f2a-4ab3-9801-81fceed1b280.png)



## ER diagram
* 총 4개의 개체로 복용 알람, 약, 복용 내역, 보호자로 이루어져 있다. 복용 알람은 약과 등록 관계를 맺고 있고, 복용 내역과는 기록 관계, 보호자와는 호출 관계를 맺고 있다.
![캡스톤ER다이어그램 drawio](https://user-images.githubusercontent.com/60650967/175776720-5b13cda4-8c77-43e5-b6ab-515d29358f54.png)



## About Project
<img src="https://img.shields.io/badge/Language-Java | C++-green?style=flat"/>  


* 메디슨 박스 연결부터 복용까지 구현 영상  
[![IMAGE ALT TEXT HERE](http://img.youtube.com/vi/D2fI3xB3ucE/0.jpg)](http://www.youtube.com/watch?v=D2fI3xB3ucE)  


* 미복용 시 구현 영상  
[![IMAGE ALT TEXT HERE](http://img.youtube.com/vi/ITHQxqA9Xog/0.jpg)](http://www.youtube.com/watch?v=ITHQxqA9Xog)  
