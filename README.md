# Tabungan

Aplikasi pencatatan keuangan pribadi native Android yang sederhana, cepat, dan modern untuk mengelola pemasukan, pengeluaran, saldo, dompet, anggaran, dan statistik keuangan tanpa koneksi internet (offline-first).

---

## Karakter Visual & Desain

- **Bright Blue 3D Neobrutalism**: Menggabungkan warna biru cerah (`#1687FF`), putih, garis hitam tegas 2–3dp, dan bayangan solid (hard shadow 5–6dp tanpa blur).
- **Efek Taktil Fisik**: Tombol bergeser ke arah bayangan saat ditekan untuk memberikan sensasi tombol fisik nyata.
- **Ikon Vektor Open-Source**: Menggunakan ikon vektor berbasis stroke open-source yang konsisten tanpa emoji.
- **Light Mode Only**: Antarmuka konsisten dan bersih dalam mode terang.

---

## Fitur Utama

1. **Beranda & Total Saldo**
   - Kartu hero total saldo dari seluruh dompet aktif.
   - Ringkasan pemasukan dan pengeluaran bulanan.
   - Pantauan anggaran bulanan dengan progress bar interaktif.
   - Daftar riwayat transaksi terbaru.

2. **Pencatatan Transaksi (Pemasukan & Pengeluaran)**
   - Pilihan transaksi pemasukan dan pengeluaran.
   - Input nominal besar berformat mata uang Rupiah (`Rp`).
   - Pemilihan kategori, dompet, tanggal, dan catatan transaksi.
   - Validasi data transaksi.

3. **Detail & Riwayat Transaksi**
   - Rincian nominal, tipe transaksi, kategori, dompet, catatan, dan tanggal.
   - Edit transaksi dan hapus transaksi dengan dialog konfirmasi.

4. **Dompet Multi-Akun**
   - Mendukung jenis dompet Tunai, Bank, E-Wallet, dan Lainnya.
   - Saldo dompet dihitung otomatis secara konsisten: `Saldo Awal + Pemasukan - Pengeluaran`.
   - Tambah, edit, dan hapus dompet dengan perlindungan integritas data transaksi.

5. **Statistik Keuangan & Donut Chart**
   - Filter periode: Minggu, Bulan, dan Tahun.
   - Visualisasi persentase pengeluaran per kategori.
   - Total pemasukan, pengeluaran, serta selisih surplus atau defisit.
   - Data langsung dihitung dari Room Database.

6. **Target Anggaran Bulanan**
   - Menetapkan batas pengeluaran bulanan per kategori.
   - Progress bar penggunaan anggaran dengan indikator peringatan saat melebihi anggaran.

7. **Pencarian & Filter Transaksi**
   - Pencarian berdasarkan catatan, nama kategori, dompet, dan nominal.
   - Filter cepat berdasarkan rentang waktu dan tipe transaksi.

8. **Manajemen Kategori**
   - Kelola kategori kustom untuk pemasukan dan pengeluaran.
   - Pemilihan ikon open-source untuk setiap kategori.

9. **Android App Shortcuts (Long Press)**
   - Akses instan melalui tekan lama pada ikon aplikasi di Home Screen Android:
     - **Tambah Pengeluaran** (`tabungan://add-expense`) → membuka form dengan mode Pengeluaran terpilih
     - **Tambah Pemasukan** (`tabungan://add-income`) → membuka form dengan mode Pemasukan terpilih
     - **Lihat Statistik** (`tabungan://statistics`) → membuka layar Statistik
     - **Dompet** (`tabungan://wallet`) → membuka layar Dompet

10. **Home Screen Widgets (2x2 & 4x2)**
    - **Widget 2x2**: Menampilkan identitas Tabungan, saldo total terkini langsung dari Room database, dan tombol aksi cepat untuk mencatat transaksi.
    - **Widget 4x2**: Menampilkan saldo total, ringkasan pemasukan bulanan, pengeluaran bulanan, tombol pintasan `+ Pemasukan`, dan `- Pengeluaran`.
    - Desain Neobrutalism 3D dengan kartu putih, garis hitam tebal, bayangan solid, dan aksen biru cerah.
    - Otomatis sinkron dan terbarui setiap kali transaksi, dompet, atau data cadangan diperbarui.

11. **Cadangkan & Pulihkan Data (Backup & Restore)**
    - Ekspor seluruh data keuangan ke berkas `tabungan_backup.json`.
    - Impor dan validasi struktur serta tipe data JSON.
    - Ringkasan data (jumlah dompet, kategori, transaksi, anggaran) sebelum pemulihan data dilakukan.

12. **Data Contoh (Sample Data)**
    - Opsi untuk mengisi data contoh untuk eksplorasi aplikasi atau membersihkan seluruh data dari menu Pengaturan.

---

## Teknologi

- **Bahasa**: Kotlin
- **UI Framework**: Jetpack Compose, Material 3
- **Database**: Room Database (SQLite) dengan Kotlin Symbol Processing (KSP)
- **Komponen Arsitektur**: ViewModel, StateFlow, Coroutines
- **Navigasi & Deep Link**: Navigation Compose
- **Widget & Shortcuts**: AppWidgetProvider, RemoteViews, ShortcutManager
- **Serialization**: Google Gson
- **Build Tool**: Gradle Kotlin DSL
- **Optimasi**: R8 Shrinking & Minification (`isMinifyEnabled = true`, `isShrinkResources = true`)

---

## Struktur Project

```text
Tabungan/
├── .github/
│   └── workflows/
│       └── android.yml
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/kyu/tabungan/
│   │   │   │   ├── TabunganApp.kt
│   │   │   │   ├── MainActivity.kt
│   │   │   │   ├── theme/
│   │   │   │   ├── components/
│   │   │   │   ├── data/
│   │   │   │   ├── navigation/
│   │   │   │   ├── ui/
│   │   │   │   ├── util/
│   │   │   │   └── widget/
│   │   │   │       ├── TabunganWidgetUpdater.kt
│   │   │   │       ├── TabunganWidgetSmallProvider.kt
│   │   │   │       └── TabunganWidgetLargeProvider.kt
│   │   │   ├── res/
│   │   │   │   ├── drawable/
│   │   │   │   ├── layout/
│   │   │   │   │   ├── widget_small.xml
│   │   │   │   │   └── widget_large.xml
│   │   │   │   ├── values/
│   │   │   │   └── xml/
│   │   │   │       ├── shortcuts.xml
│   │   │   │       ├── widget_info_small.xml
│   │   │   │       └── widget_info_large.xml
│   │   │   └── AndroidManifest.xml
│   │   └── test/
│   ├── build.gradle.kts
│   └── proguard-rules.pro
├── gradle/
│   └── wrapper/
│       ├── gradle-wrapper.jar
│       └── gradle-wrapper.properties
├── build.gradle.kts
├── settings.gradle.kts
├── gradle.properties
├── gradlew
├── gradlew.bat
├── .gitignore
├── LICENSE
└── README.md
```

---

## Cara Menjalankan

### Kebutuhan Sistem

- JDK 17
- Android SDK (API level 34)

### Menjalankan di Emulator / Device

```bash
git clone <URL_REPOSITORY>
cd tabungan
./gradlew installDebug
```

---

## Cara Build APK

### Build Debug APK

Untuk membuat berkas APK debug secara lokal:

```bash
./gradlew assembleDebug
```

Lokasi hasil berkas APK:
```text
app/build/outputs/apk/debug/app-debug.apk
```

### Build Release APK

```bash
./gradlew assembleRelease
```

Lokasi hasil berkas APK:
```text
app/build/outputs/apk/release/app-release-unsigned.apk
```

---

## GitHub Actions

Alur kerja CI/CD otomatis pada `.github/workflows/android.yml`:

1. Checkout repositori.
2. Memasang JDK 17 (Temurin).
3. Melakukan caching dependency Gradle.
4. Menjalankan unit test (`./gradlew testDebugUnitTest`).
5. Membangun debug APK (`./gradlew assembleDebug`).
6. Mengunggah berkas APK ke GitHub Actions Artifacts dengan nama `app-debug`.

---

## Lisensi

Proyek ini dilisensikan di bawah [MIT License](LICENSE).
Ikon vektor terinspirasi oleh Lucide Icons dan Tabler Icons (keduanya berlisensi MIT).
