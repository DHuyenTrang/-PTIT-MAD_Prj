# BÁO CÁO BÀI TẬP LỚN: MOBILE MAP APPLICATION

## 1. Mô tả chi tiết Bài Tập Lớn (BTL)

### 1.1. Các chức năng cơ bản
Hệ thống là một ứng dụng bản đồ dẫn đường thông minh trên nền tảng Android, tích hợp các chức năng cốt lõi sau:
-   **Hiển thị bản đồ:** Sử dụng SDK (Mapbox hoặc Google Maps) để hiển thị bản đồ trực quan, hỗ trợ tương tác (zoom, pan, rotate).
-   **Định vị:** Xác định vị trí hiện tại của người dùng GPS thời gian thực.
-   **Tìm kiếm:** Tìm kiếm địa điểm, địa chỉ, POI (Points of Interest) với gợi ý thông minh.
-   **Dẫn đường (Navigation):** Tính toán lộ trình từ điểm A đến điểm B, điều hướng từng chặng (turn-by-turn navigation), dự đoán thời gian di chuyển.
-   **Quản lý người dùng:** Đăng ký, đăng nhập, lưu trữ lịch sử di chuyển, lưu địa chỉ yêu thích. (Nhà riêng, Văn phòng).

### 1.2. Các chức năng AI
Ứng dụng tích hợp trí tuệ nhân tạo để nâng cao trải nghiệm người dùng:
-   **Trợ lý ảo thông minh (AI Assistant):**
    -   Hỗ trợ ra lệnh bằng giọng nói (Voice Command) để tìm đường, tra cứu thông tin mà không cần thao tác tay.
    -   Tích hợp Text-to-Speech (TTS) để trơ lý phản hồi bằng giọng nói và Speech-to-Text (STT) để nhận diện lệnh.
-   **Gợi ý thông minh (Recommendation):** (Optional) Gợi ý địa điểm dựa trên thói quen hoặc lịch sử di chuyển của người dùng.

### 1.3. Các Actors của hệ thống
-   **Guest (Khách):** Người dùng chưa đăng nhập, chỉ có thể xem bản đồ, tìm kiếm cơ bản nhưng không lưu được lịch sử hay Favorite places.
-   **Authenticated User (Người dùng thành viên):** Người dùng đã đăng nhập, có đầy đủ quyền năng: dẫn đường, lưu địa điểm, xem lịch sử, sử dụng trợ lý ảo đầy đủ.
-   **System/Admin (nếu có):** Quản trị viên hệ thống (thường nằm ở Backend) để quản lý dữ liệu bản đồ, tài khoản người dùng (nếu phát triển module quản lý riêng).

### 1.4. Công nghệ sử dụng
-   **Mobile App (Android):**
    -   Ngôn ngữ: **Kotlin**.
    -   Kiến trúc: **MVVM** (Model-View-ViewModel) + **Clean Architecture**.
    -   Frameworks/Libs: Jetpack Components (Navigation, CameraX, Room, LiveData/Flow), Retrofit (Networking), Hilt/Koin (DI).
    -   **Android Auto:** Tích hợp để hiển thị và điều khiển trên màn hình ô tô.
-   **Backend:**
    -   Spring Boot (Java) hoặc Node.js (tùy thế mạnh thành viên BE).
    -   RESTful API.
-   **Database:**
    -   PostgreSQL (có hỗ trợ PostGIS cho dữ liệu không gian) hoặc MySQL.
-   **AI/ML:**
    -   Google ML Kit (On-device) hoặc tích hợp API Speech của Google/OpenAI.

### 1.5. API bên ngoài
-   **Map Service:** Mapbox SDK / Google Maps SDK (Maps, Navigation, Search).
-   **Voice/AI:** Google Speech-to-Text, Text-to-Speech API.
-   **Weather:** OpenWeatherMap API (nếu hiển thị thời tiết trên lộ trình).

---

## 2. Bản phân công công việc (Team 4 người)

### Phân công chi tiết:

#### **Thành viên 1: Đặng Huyển Trang**
*   **Vai trò:** Dựng khung dự án (Skeleton), handle các module khó, Android Auto.
*   **Nhiệm vụ:**
    -   Thiết lập kiến trúc dự án (MVVM, Clean Arch, DI).
    -   Tích hợp Map SDK (Mapbox/Google Maps): Hiển thị bản đồ, Camera control.
    -   Xử lý Module Navigation Logic: Tính toán lộ trình, vẽ đường đi.
    -   Tích hợp Android Auto: Đưa ứng dụng lên màn hình xe hơi.
    -   Xử lý Voice Command Flow (tích hợp STT/TTS cơ bản cho Assistant).
    -   Review code Android của các thành viên khác.

#### **Thành viên 2: Lê Tuần Phát**
*   **Vai trò:** Xây dựng Server, Database, và API cho mobile client.
*   **Nhiệm vụ:**
    -   Thiết kế Database Schema (Users, Saved Places, History).
    -   Build RESTful APIs:
        -   Auth (Login/Register/Token).
        -   User Profile & Settings.
        -   Sync dữ liệu (History, Favorites).
    -   Deploy Server (Heroku/Render/AWS/Localhost).
    -   Viết tài liệu API (Swagger/Postman) cho team Mobile.

#### **Thành viên 3: Phan Thanh Tân**
*   **Vai trò:** Implement các màn hình chức năng và logic nghiệp vụ vừa phải.
*   **Nhiệm vụ:**
    -   Code các màn hình UI chính: Dashboard, Search Bar, Settings.
    -   Xử lý chức năng User Profile (Hiển thị thông tin, Đổi mật khẩu).
    -   Làm màn hình Lịch sử di chuyển (History List) & Địa điểm yêu thích (Saved Places) (Call API từ BE).
    -   Tích hợp API Weather (nếu có) để hiển thị thời tiết.

#### **Thành viên 4: Nguyễn Thị Hương Nhài**
*   **Vai trò:** Thiết kế giao diện, làm các UI đơn giản và đảm bảo chất lượng.
*   **Nhiệm vụ:**
    -   **Design UI/UX:** Sử dụng **Figma** để thiết kế Prototype, Flow màn hình cho toàn bộ ứng dụng trước khi code.
    -   Code các màn hình cơ bản: Splash Screen, Login/Register Screen, Onboarding.
    -   Thực hiện Unit Test cơ bản (nếu kịp) hoặc Manual Test (Kiểm thử thủ công) toàn bộ app để log bug.
    -   Viết báo cáo, document hướng dẫn sử dụng (User Guide) cho đồ án.
