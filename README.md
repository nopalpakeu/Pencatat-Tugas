# 📚 Pencatat Tugas (Task Planner & Academic Tracker)

Aplikasi pencatat tugas sekolah dan kuliah modern berbasis Android yang dibangun dengan **Jetpack Compose**, **Material 3**, dan **Room Database**. Dirancang untuk membantu siswa dan mahasiswa mengelola deadline tugas, mata pelajaran, serta memantau riwayat penyelesaian tugas secara efisien dan terorganisir.

---

## 🌟 Fitur Utama

- **📝 Manajemen Tugas Komprehensif**
  - Catat tugas baru dengan judul, deskripsi, mata pelajaran, jenis tugas (*PR, Proyek, Kuis, Makalah, Latihan*), prioritas (*Rendah, Sedang, Tinggi*), serta tanggal tenggat waktu (*deadline*).
  - Tandai tugas selesai hanya dengan satu kali ketuk (*checklist*).
  - Opsi penghapusan tugas dengan konfirmasi yang aman.

- **🔍 Filter & Pencarian Cerdas**
  - Filter tugas berdasarkan batas waktu: **Semua**, **Hari Ini**, **Mendatang**, atau **Terlambat (Overdue)**.
  - Filter dinamis berdasarkan kategori **Mata Pelajaran**.

- **🏫 Manajemen Mata Pelajaran (Subjects)**
  - Tambah mata pelajaran lengkap dengan nama guru/dosen dan pilihan palet warna identifikasi.
  - Dukungan relasi database terintegrasi (*foreign key with cascade delete*).

- **🏆 Riwayat Penyelesaian (History)**
  - Rekam jejak seluruh tugas yang telah diselesaikan untuk meninjau progres akademik.

- **🎨 UI Modern & Responsif**
  - Mengadopsi pedoman **Material You (Material 3)** dengan tata letak *Edge-to-Edge*.
  - Dilengkapi *Bottom Navigation Bar*, *Filter Chips*, *Extended Floating Action Button*, dan kartu tugas yang informatif.

---

## 🛠️ Tech Stack & Arsitektur

Aplikasi ini dibangun menggunakan arsitektur modern Android (**MVVM + Repository Pattern**) dan memanfaatkan teknologi terkini:

- **Bahasa:** [Kotlin](https://kotlinlang.org/)
- **UI Toolkit:** [Jetpack Compose](https://developer.android.com/jetpack/compose) dengan [Material Design 3](https://m3.material.io/)
- **Basis Data Lokal:** [Room Database](https://developer.android.com/training/data-storage/room) didukung oleh [KSP (Kotlin Symbol Processing)](https://kotlinlang.org/docs/ksp-overview.html)
- **Arsitektur:** MVVM (Model-View-ViewModel) + Repository Pattern
- **Asinkron & Reaktif:** Kotlin Coroutines & `StateFlow`
- **Dependency Injection / ViewModel:** AndroidX Lifecycle & Activity Compose

---

## 📂 Struktur Proyek

```text
app/src/main/java/com/pencatattugas/
├── data/
│   ├── local/
│   │   ├── dao/             # Data Access Objects (TaskDao, SubjectDao)
│   │   ├── entity/          # Entitas Room (TaskEntity, SubjectEntity)
│   │   ├── model/           # Relasi Room (TaskWithSubject)
│   │   └── AppDatabase.kt   # Inisialisasi Database Room
│   └── repository/          # Abstraksi data (AcademicRepository)
├── ui/
│   ├── components/          # Komponen UI Reusable (Dialog, TaskCard)
│   ├── screens/             # Halaman utama (HomeScreen, HistoryScreen, SubjectListScreen)
│   ├── theme/               # Konfigurasi Tema, Warna & Tipografi Material 3
│   └── viewmodel/           # Pengelola state UI (AcademicViewModel, TaskFilter)
├── MainActivity.kt          # Entry point aplikasi & Bottom Bar Navigation
└── MainApplication.kt       # Inisialisasi scope level aplikasi & database
```

---

## 🚀 Memulai (Getting Started)

### Prasyarat
- **Android Studio**: Android Studio Koala / Ladybug atau versi yang lebih baru disarankan.
- **JDK**: Java Development Kit versi 17.
- **Android SDK**: Min SDK 24 (Android 7.0 Nougat) & Target SDK 37.

### Langkah Instalasi

1. **Clone repositori ini:**
   ```bash
   git clone https://github.com/nopalpakeu/Pencatat-Tugas.git
   cd Pencatat-Tugas
   ```

2. **Buka di Android Studio:**
   - Buka Android Studio -> *File* -> *Open* -> Pilih folder repositori ini.
   - Tunggu proses *Gradle Sync* selesai.

3. **Jalankan Aplikasi:**
   - Hubungkan perangkat Android fisik (aktifkan *USB Debugging*) atau gunakan Android Emulator.
   - Klik tombol **Run** (`Shift + F10`) pada Android Studio.

---

## 📱 Tangkapan Layar (Screenshots)

| Beranda & Daftar Tugas | Tambah Tugas Baru | Kelola Mata Pelajaran | Riwayat Selesai |
|:---:|:---:|:---:|:---:|
| *(Tambahkan tangkapan layar di sini)* | *(Tambahkan tangkapan layar di sini)* | *(Tambahkan tangkapan layar di sini)* | *(Tambahkan tangkapan layar di sini)* |

> *Tips: Simpan gambar screenshot di direktori `screenshots/` di repositori Anda dan tautkan ke tabel di atas.*

---

## 🤝 Kontribusi

Kontribusi selalu terbuka! Jika Anda memiliki saran fitur baru, perbaikan bug, atau peningkatan desain UI/UX:

1. *Fork* repositori ini
2. Buat *branch* fitur baru (`git checkout -b feature/FiturKeren`)
3. *Commit* perubahan Anda (`git commit -m 'Menambahkan fitur keren'`)
4. *Push* ke *branch* tersebut (`git push origin feature/FiturKeren`)
5. Buat **Pull Request**

---

## 📄 Lisensi

Didistribusikan di bawah lisensi MIT. Lihat file `LICENSE` untuk informasi lebih lanjut.

---

**Dibuat dengan ❤️ oleh [nopalpakeu](https://github.com/nopalpakeu)**
