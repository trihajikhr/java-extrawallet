# SDLC Design 

> Model SDLC: Prototype

Model prototyping adalah metode pengembangan perangkat lunak yang berfokus pada pembuatan prototipe awal dari sistem, kemudian melakukan perbaikan berdasarkan feedback pengguna hingga menghasilkan produk akhir yang sesuai kebutuhan.

Dalam metode ini, pengembang tidak langsung membangun sistem lengkap sejak awal.
Sebaliknya, ia membuat representasi awal (biasanya antarmuka/GUI), lalu melakukan iterasi perbaikan.

## 1 | Analisa kebutuhan

### 1.1 | Kebutuhan Fungsional

Kebutuhan fungsioanl adalah fungsi apa saja yang harus dapat dilakukan oleh aplikasi:

1. Aplikasi dapat digunakan oleh lebih dari satu akun. Aplikasi harus mampu untuk memyimpan data yang berda untuk setiap akun pengguna. Ini membuat user dapat memiliki lebih dari satu akun pada aplikasi, memungkinkan user untuk memisahkan data.
2. Aplikasi dapat menangani kebutuhan penggantian username, nama, dan password.
3. Aplikasi dapat menerima jenis mata uang, dan mengganti mata uang dengan mempertahankan value yang sesuai.
4. Aplikasi menyimpan setiap transaksi dengan beberapa kategori yang bisa dipilih oleh user, dan kategori tersebut sudah terbuat semisal 10 kategori diawal. Namun user masih dapat menambahkanya sendiri jika ingin kategori custom.
5. Aplikasi dapat menerima data yang dimasukan oleh user, dimana aplikasi menerima inputan nominal, dan mencatat tanggal transaksi.
6. Aplikasi dapat mencatat tanggal ketika saldo aplikasi bertambah atau berkurang.
7. Aplikasi dapat menampilkan data yang diminta pengguna, seperti melihat riwayat transaksi dalam 1 minggu, 2 minggu, 1 bulan, 6 bulan, dan 1 tahun.
8. Aplikasi dapat menampilkan statistik transaksi yang difilter berdasarkan waktu, dan kategori.
9. Aplikasi dapat menampilkan data yang bisa disort ascending dan descending, untuk menampilkan riwayat transaksi berdasarkan kategori
10. Aplikasi menyimpan semua data ke dalam SQlite, dan untuk data sensitif seperti password, harus ditangani dengan data hasil hashing
11. Aplikasi memiliki tampilan GUI.
12. Aplikasi menggunakan konsep OOP
13. Aplikasi mampu mengexport data kedalam CSV

### 1.2 | Kebutuhan Non-Fungsional

1. Usability 

    Antarmuka mudah digunakan, tombol dan layout harus rapi.

2.  Reliability

    Aplikasi harus stabil dam todal crash saat melakukan operasi CRUD. Penangannan error dan pemilihan algoritma harus tepat dan efisien.

3. Maintanability

    Struktur kode harus mengikuti kaidan dan konsep OOP, sehingga mudah dikembangkah lebih lanjut.

4. Portability 

    Aplikasi harus mampu untuk berjalan di sistem operasi yang berbeda, seperti Windows, Linux, dan MacOS

5. Security

    Aplikasi harus mampu menagani kesalahan input yang mungkin dilakukan oleh user, dengan melakukan validasi input

## 1.3 | Identifikasi Pengguna

Pengguna utama bisa berasal dari berbagai negara, beberapa bahasa harus digunakan. Pengguna tidak perlu memahami SQL,, interaksi hanya via GUI.

## 1.4 | Lingkungan dan Batasan

Aplikasi harus dibangun dengan menggunakan
- Java
- Java Swing / JavaFX
- SQLite database
- Konsep OOP

## 2 | Membuat prototype

Tampilan halaman utama:


<br/><br/><br/><br/>

## Identifikasi Entitas

1. User
2. Mata uang
3. Kategori
4. Pemasukan
5. Pengeluaran
6. Tabungan
7. Pengeluaran tabungan
8. Pemasukan tabungan

## Identifikasi Tipe data

SQLite memiliki beberapa perbedaan syntax dengan query SQL lain, sehingga akan digunakan penulisan tipe data yang sesuai dengan ketentuan SQLite yang ada.

### User

Aplikasi akan dirancang untuk bisa menangai lebih dari satu akun pengguna, yaitu dengan dibuat memiliki lebih dari akun. Setiap akun akan memiliki data keuangan yang berbeda-beda. Aplikasi personal finance modern kebanyakan mengikuti pendekatna ini.

| Atribut       | Tipe data | Keterangan                    |
|---------------|-----------|-------------------------------|
| id_user       | int       | PK (autoincrement, not null)) |
| email         | varchar   | unique                        |
| nama          | varchar   |                               |
| tanggal regis | datetime  |                               |
| mata uang     | id        | FK                            |
| saldo         | int       |                               |

### Mata Uang

Ketika user pertama kali masuk dan membuat akun, user akan diminta untuk memberi tahu aplikasi jenis mata uang apa yang dia gunakan. Namun jika ditengah-tengah pemakaian yser ingin berganti mata uang, maka aplikasi harus bisa menghandle hal tersebut. 

Kasus ini mungkin untuk terjadi, jika semisal user berpindah negara, dan ingin mempertahankan value mata uang yang dimilikinya di aplikasi.

| Atribut         | Tipe data | Keterangan |
|-----------------|-----------|------------|
| id_matauang     | int       | PK         |
| Kode iso        | varchar   | unique     |
| nama_matauang   | varchar   |            |
| simbol matauang | varchar   |            |
| presisi_desimal | int       |            |
| nama_singkat    | varchar   |            |

### Kategori

Kategori akan menyimpan pengelompokan pemasukan dan pengeluaran yang dikelompokan berdasarkan kategori. User bisa memantau total pengeluaran dan pemasukan berdasarkan kategori tertentu, dengan menyediakan opsi yang menyediakan tampilakan riwayat transaksi berdasar kategori.


| Atribut       | Tipe data | Keterangan               |
|---------------|-----------|--------------------------|
| id_kategori   | int       | PK (*autu increment*)    |
| jenis         | enum      | (pemasukan, pengeluaran) |
| nama_kategori | varchar   |                          |

### Pemasukan

Pemasukan mencaatat uang masuk yang diterima oleh sistem, dan kemudian akan menambah total saldo yang ada. Aplikasi harus bisa menyimpan log pemasukan, mulai dari tanggal, value, dan jenis pemasukan yang diinputkan oleh user.

| Atribut      | Tipe data | Keterangan            |
|--------------|-----------|-----------------------|
| id_pemasukan | int       | PK (*auto increment*) |
| tanggal      | datetime  |                       |
| id_kategori  | int       | FK                    |
| jumlah       | int       |                       |

### Pengeluaran

Pengeluaran mencatat uang yang keluar, atau dispent oleh user. Fungsinya mendukung pencatatan riwayat transaksi, yang tidak berbeda jauh dengan pencatatn pemasukan.

| Atribut        | Tipe data  | Keterangan            |
| -------------- |------------|-----------------------|
| id_pengeluaran | int        | PK (*auto increment*) |
| tanggal        | datetime   |                       |
| id_kategori    | varchar    | FK                    |
| jumlah         | int        |                       |

### Tabungan:

Beberapa aplikasi menawarkan opsi untuk membuat pengelompokan, dimana user membuat rencana finansial untuk kepentingan tertentu, dan mengharuskan menambung uangnya. Aplikasi akan dirancang untuk menyediakan opsi tersebut, sehingga pencatatn transaksi akan dipisahkan dengan pencatatn transaksi utama, namun dengan beberapa fitur yang lebih disederhanakan.

Ini artinya, tabungan akan memiliki entitas pemasukan dan pengeluaranya sendri, namun dengan kategori, user, dan mata uang utama.

| Atribut       | Tipe data   | Keterangan            |
| ------------- |-------------| --------------------- |
| id_tabungan   | int         | PK (*auto increment*) |
| tujuan        | varchar     |                       |
| jumlah        | int         |                       |
| target_jumlah | int         |                       |
| date_start    | date        |                       |
| date_end      |        date |                       |

- Pengeluaran tabungan

| Atribut        | Tipe     | Keterangan                  |
| -------------- |----------| --------------------------- |
| id_tabungan    | int      | FK (tabungan . id_tabungan) |
| nominal_keluar |    int   |                             |
| tanggal        | datetime |                             |

- Pemasukan tabungan

| Atribut       | Tipe            | Keterangan                 |
| ------------- |-----------------| -------------------------- |
| id_tabungan   | int             | FK(tabungan . id_tabungan) |
| nominal_masuk | int             |                            |
| tanggal       |       datetimee |                            |
