# 유통산업용 재고 관리 PDA Application(입고, 출고, 재고현황파악)
물류 센터(Hub 등)에서 사용하는 입고, 출고, 관리용 App을 제작

## 보고서
[PDA_final_report.pdf](https://github.com/user-attachments/files/21072447/PDA_final_report.pdf)

## 시연 영상
[유튜브링크](https://www.youtube.com/watch?v=2N3npNVdcOY&feature=youtu.be)

## 개발 기간
2024.05.01 ~ 2024.06.14

## 개발 목적
- [🚗 PDA란?](https://ko.wikipedia.org/wiki/%EA%B0%9C%EC%9D%B8_%EC%A0%95%EB%B3%B4_%EB%8B%A8%EB%A7%90%EA%B8%B0)   
CJ, 쿠팡 등 다양한 물류 센터에서 공통적으로 수행되는 입고 및 출고 과정을 더욱 효율적으로 관리하기 위해, 근무자와 관리자에게 PDA Application을 제공하고자 합니다. 이 애플리케이션은 실시간 데이터 처리와 정확한 정보 제공을 통해 작업의 신속성과 정확성을 높이며, 전체 물류 프로세스의 최적화를 목표로 합니다. 이를 통해 작업자는 보다 체계적으로 업무를 수행할 수 있고, 관리자는 실시간으로 작업 현황을 모니터링하고, 효율적인 자원 배분 및 문제 발생 시 신속한 대응이 가능해집니다. 궁극적으로, 이 Application은 물류 센터의 운영 효율성을 극대화하고, 고객에게 보다 나은 서비스를 제공하는 데 기여할 것을 기대 합니다.

## 전제조건
이번 개발에서는 물류라는 커다란 과정 중 입고와 출고 과정만을 다루기 때문에 몇 가지 상황을 가정하여 제작되었습니다.

- 모든 상품의 데이터는 DB에 존재하고 있으며, 바코드를 스캔할 때 상품 ID를 통해 상품명을 가져오도록 가정합니다.
- 입고되는 상품은 실제 물류 과정에서 누락되거나 분실되지 않는다고 가정합니다.
- 출고되는 상품 리스트는 이미 만들어져 있으며, 이 리스트를 가져오는 것만을 가정합니다.

## 🛠 앱 기능   

1. [ 로그인 기능 ](#)

2. [ 근무자 기능 ](#)
    - [입고 모드](#)   
    - [출고 모드](#)   
    - [오류 보고](#)   

3. [ 관리자 기능](#)
    - [입고 승인](#)   
    - [재고 확인 및 수정](#)   
    - [오류 보고서 확인](#)   

## 기능 소개

## 로그인 기능

## 근무자 기능

## 관리자 기능
