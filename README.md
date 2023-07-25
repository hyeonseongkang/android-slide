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


## 3-4. 슬라이드 목록 표시
- 23/07/19(수) - 23:25 완성

## 주요내용
- ItemTouchHelper
  - Slide 목록을 드래그해서 순서를 바꾸기 위해 ItemTouchHelper 구현하여 Adapter에 적용

## 결과화면
![화면-기록-2023-07-19-오후-11 28 58](https://github.com/hyeonseongkang/OpenSource_Lecture_project/assets/68272971/2a099306-4572-4d0a-8c33-18814e666aef)


## 3-5. 사진 슬라이드 추가하기
- 23/07/24(월) - 10:43 완성

## 구현 내용
- 이미지 슬라이드 추가하기
- slide 목록 아이템 이동

## 고민 사항
- adapter bindingAdapter: 이미지 슬라이드 경우 drawable/ic_gallery, 정사각형 슬라이드 경우 해당 색깔 표시하기 위해 adapter_slide_view_item.xml binding, 문제는 정사각형일 때 View의 backgroundColor를 바꿔야 하는데 일반적인 binding으로 불가능하여 @BindingAdapter 이용하여 색깔 변경함
- slide view long click popup menu: 슬라이드 view를 길게 누르면 popup menu 나와야 함 -> PopupMenu class 사용
- adapter item 이동: popup menue에서 수행해야 하는 동작 클릭하면 adapter 안에서 position 바꾼 뒤 notifyItemMoved(fromPosition, toPosition)
  notifyItemRangeChanged(0, slideViewList.size) 함수 사용하여 이동 처리

## 개선할 점
- 코드가 아직 깔끔하지 못한 거 같음, 리뷰 받은 내용 중심으로 코드 작성하기

## 4-1. 슬라이드 불러오기
- 23/07/25(화) - 17:45 완성

## 구현 내용
- 서버에서 Slides 데이터 가져오기

## 고민 사항
* 서버에서 Slides 데이터 가져올 때 사용할 Library 선택 -> Retrofit 사용
  이유
1. interface 작성하면 그 안에 구현 내용을 retrofit에서 작성해줌,
2. JSON 데이터 객체 변환 : retrofit은 기본적으로 Gson 컨버터를 제공, Gson을 추가로 설정하거나 코드를 작성하지 않아도 자동으로 JSON 데이터를 객체로 변환해주기 때문에 JSON 객체를 편하게 사용할 수 있음


* Image Url -> Bitmap 변환
  retrofit을 통해서 JSON 데이터를 받아온 뒤, type이 Image라면 url이 존재하고 해당 url의 image를 다운로드 받아 byteArray로 관리해야 함.
  처음에는 중첩으로 retrofit 사용하여 image까지 받아왔음 -> 코드 복잡해짐.
  utils에 HttpURLConnection을 사용하여 image다운 받아서 Bitmap으로 반환하는 함수 만들어서 사용

## 개선할 점
MainActivity에 DataBinding 적용하기
