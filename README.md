# 📚 Book Shelf

An Android app to store, manage, and read your personal PDF books, built using Kotlin, MVVM architecture, and Room database.

---

## ✨ Features

- 📥 Save PDF books with:
    - Title
    - Author
    - Version
    - Publication Year
    - Cover Image
- 📑 Read PDF files using swipeable pages (ViewPager2)
- 🔖 Remembers the last-read page for every book
- 🌙 Supports dark mode (inverted colors for PDFs)
- 🔍 Search books by Title, Author, Version, or Year
- ✏️ Edit or update book details
- 🗑️ Delete books with confirmation
- ⚙️ Clean architecture with MVVM + Room


---

## 🧑‍💻 Tech Stack

- **Kotlin**
- **Room Database**
- **LiveData & ViewModel (MVVM)**
- **PdfRenderer & ViewPager2**
- **PhotoView (Zoom support)**

---

## 🚀 Getting Started

### Requirements
- Android Studio Hedgehog or newer
- Minimum SDK: 21
- Target SDK: 34

### Build Setup

1. Clone this repo:
   ```bash
   git clone https://github.com/yourusername/pdf-book-saver.git
   
   ```

2. Open the project in Android Studio.

3. Run the app.

--- 

##  Folder Structure

project-root:
data:
- Book.kt
- BookDao.kt
- BookDatabase.kt
repository:
- BookRepository.kt
viewmodel:
- BookViewModel.kt
ui:
- MainActivity.kt
- AddBookActivity.kt
- PdfViewerActivity.kt
adapter:
- BookAdapter.kt
- PdfPagerAdapter.kt
layout:
- activity_main.xml
- activity_add_book.xml
- activity_pdf_viewer.xml
- item_book.xml
- item_pdf_page.xml
drawable:
- ic_launcher_foreground.png
- ic_launcher_background.png
manifest:
- AndroidManifest.xml
build:
- build.gradle (Module)
- build.gradle (Project)


---

## 🙏 Acknowledgments

- [PdfRenderer](https://developer.android.com/reference/android/webkit/PdfRenderer)
- [ViewPager2](https://developer.android.com/reference/androidx/viewpager2/widget/ViewPager2)
- [PhotoView](https://github.com/chrisbanes/PhotoView)
- [Room Database](https://developer.android.com/reference/androidx/room/Room)
- [LiveData & ViewModel (MVVM)](https://developer.android.com/topic/libraries/architecture/livedata)
- [Android Studio](https://developer.android.com/studio)

---

## 🌟 Let's Build Something Great Together!

[![GitHub Profile](https://img.shields.io/badge/GitHub-Profile-1DA1F2?style=for-the-badge&logo=github&logoColor=white)](https://github.com/musfiqur552608)
[![LinkedIn Profile](https://img.shields.io/badge/LinkedIn-Profile-0A66C2?style=for-the-badge&logo=linkedin&logoColor=white)](https://www.linkedin.com/in/musfiqur55/)

---

[![GitHub Stars](https://img.shields.io/github/stars/yourusername/pdf-book-saver?style=social)](https://github.com/musfiqur552608/pdf-book-saver)
[![GitHub Forks](https://img.shields.io/github/forks/yourusername/pdf-book-saver?style=social)](https://github.com/musfiqur552608/pdf-book-saver/forks)
[![GitHub Issues](https://img.shields.io/github/issues/yourusername/pdf-book-saver?style=social)](https://github.com/musfiqur552608/pdf-book-saver/issues)    

---

