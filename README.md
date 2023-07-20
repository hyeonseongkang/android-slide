# android-slide

## 3-1. 슬라이드 앱 프로젝트
- 23/07/17(월) - 15:50 완성

## 주요내용
- Factory Pattern
  SlideSquareView class를 만들고 생성자는 private으로 하여 외부에서 생성하지 못하도록 한 뒤 내부에 companion object Factory을 만들고 Factory안에서 fun createRandomSlideSquareView(): SlideSquareView 함수를 통해서 SlideSquareView를 생성하고 return함

## 결과화면
| 생성 |
|:-:|
|<img src="https://github.com/softeerbootcamp-2nd/android-slide/assets/68272971/5449965e-0020-4bf9-a103-ac7fb4dd6af1" height=100px>|

## 3-2. 속성 변경 동작 & 3-3. Unidirectional Data Flow(UDF)
- 23/07/19(수) - 14:07 완성

## 주요내용
- CustomView
  : SlideSquareView를 표현하기 위해 CustomView 사용

- LiveData
  : SlideSquareView를 생성하고 배경색, 투명도 등을 MutableLiveData에 저장하고, MainActivity에서는 Observer를 통해서 데이터를 관찰 후 변경사항 감지되면 UI에 표시함

- Test Code
  * SlideSquareView 생성
  * SlideSquareView 반환
  * Alpha 값 0 이하 변경 X
  * Alpha 값 10 이상 변경 X
  * SlideSquareView 전체 갯수 반환 
  * 랜덤 배경 색상

## 결과화면
| 선택 전 | 선택 후 |
|:-:|:-:|
|<img src="https://github.com/softeerbootcamp-2nd/android-slide/assets/68272971/71e98c64-b339-4129-a795-f2d53eb85a64" height=500px>|<img src="https://github.com/softeerbootcamp-2nd/android-slide/assets/68272971/652cd4e9-9e60-420e-8531-defcad1ef06f" height=500px>|
| 투명도 변경 |  배경색 변경 |
|<img src="https://github.com/softeerbootcamp-2nd/android-slide/assets/68272971/a86684c6-b8fb-40a7-a9fe-8a8878db65eb" height=500px>| <img src="https://github.com/softeerbootcamp-2nd/android-slide/assets/68272971/41aa5e6e-2f11-4077-8c21-967d7b202d1a" height=500px>|


