# NewsApiTest

## 設計模式
採`單一Activity` + 多個 Compose Screen 組成
使用 `MVVM` + `Hilt` 為專案主要結構

## 畫面
- MainActivity: 主要 Activity

- MainScreen 
主頁面下的 ComposeScreen
負責轉導至每一個 Screen

- HeadlineScreen: 頭條頁面
- SearchScreen: 新聞搜尋頁面
API 使用 `Retrofit` + `okhttp`
配合 `paging` 來取得分頁式的 News-api

- ConfigScreen 設定頁
使用 `preferenceDataStore` 作為簡易的存讀工具
存讀 主題顏色, 中英語系設定

- DetailScreen 新聞內頁
透過 sharedViewModel 將列表頁所選擇的文章傳至內頁顯示

- WebViewScreen 網頁
透過 sharedViewModel 將列表頁所選擇的文章 url 傳至 WebView 顯示

