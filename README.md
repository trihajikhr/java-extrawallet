# Project Managemet Keuangan
## 1 | Identifikasi Entitas

Halo dari readme! 😀

1. Pendapatan
2. Pengeluaran
3. Kategori
4. Tabungan
5. Pengeluaran tabungan
6. Pemasukan tabungan


## 2 | Identifikasi Atribut
### 2.1 | Pemasukan

| Atribut      | Lebar data | Tipe data | Keterangan            |
| ------------ | ---------- | --------- | --------------------- |
| id_pemasukan |            | int       | PK (*auto increment*) |
| tanggal      |            | datetime  |                       |
| sumber       | 100        | varchar   |                       |
| jumlah       |            | int       |                       |

### 2.2 | Pengeluaran

| Atribut        | Lebar data | Tipe data | Keterangan            |
| -------------- | ---------- | --------- | --------------------- |
| id_pengeluaran |            | int       | PK (*auto increment*) |
| tanggal        |            | datetime  |                       |
| id_kategori    | 100        | varchar   |                       |
| jumlah         |            | int       |                       |

### 2.3 | Kategori:

| Atribut       | Lebar data | Tipe data | Keterangan            |
| ------------- | ---------- | --------- | --------------------- |
| id_kategori   |            | int       | PK (*autu increment*) |
| nama_kategori | 50         | varchar   |                       |
### 2.4 | Tabungan:

| Atribut       | Lebar data | Tipe data | Keterangan            |
| ------------- | ---------- | --------- | --------------------- |
| id_tabungan   |            | int       | PK (*auto increment*) |
| tujuan        | 100        | varchar   |                       |
| jumlah        |            | int       |                       |
| target_jumlah |            | int       |                       |
| date_start    |            | date      |                       |
| date_end      |            | date      |                       |
Pengeluaran tabungan


| Atribut        | Lebar data | Tipe     | Keterangan                  |
| -------------- | ---------- | -------- | --------------------------- |
| id_tabungan    |            | int      | FK (tabungan . id_tabungan) |
| nominal_keluar |            | int      |                             |
| tanggal        |            | datetime |                             |
Pemasukan tabungan


| Atribut       | Lebar data | Tipe      | Keterangan                 |
| ------------- | ---------- | --------- | -------------------------- |
| id_tabungan   |            | int       | FK(tabungan . id_tabungan) |
| nominal_masuk |            | int       |                            |
| tanggal       |            | datetimee |                            |
