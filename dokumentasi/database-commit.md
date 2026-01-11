# Transaksi Database di JDBC

*(setAutoCommit, commit, rollback)*

## 1. Latar Belakang

Secara default, JDBC menggunakan **auto-commit mode**, di mana setiap perintah SQL (`INSERT`, `UPDATE`, `DELETE`) akan **langsung disimpan permanen** ke database setelah dieksekusi.

Mode ini **tidak aman** untuk operasi yang:

* saling bergantung
* membutuhkan konsistensi antara database dan data lokal
* tidak boleh menghasilkan data setengah jadi

Untuk itu, JDBC menyediakan **mekanisme transaction**.

---

## 2. Auto-Commit Mode

### 2.1 Auto-Commit = `true` (default)

```java
koneksi.setAutoCommit(true);
```

Karakteristik:

* Setiap SQL statement langsung disimpan
* Tidak bisa dibatalkan
* Cocok hanya untuk operasi sederhana dan independen

Contoh:

```java
INSERT INTO account ...
// langsung tersimpan, tidak bisa rollback
```

---

### 2.2 Auto-Commit = `false` (Transaction Mode)

```java
koneksi.setAutoCommit(false);
```

Karakteristik:

* Perubahan **belum permanen**
* Menunggu keputusan eksplisit:

    * `commit()` → simpan
    * `rollback()` → batalkan
* Semua query setelah ini berada dalam **satu transaction**

---

## 3. `commit()`

```java
koneksi.commit();
```

Fungsi:

* Menyimpan **seluruh perubahan** sejak transaction dimulai
* Mengakhiri transaction
* Perubahan menjadi **permanen**

Digunakan ketika:

* Semua query berhasil
* Data valid
* Konsistensi terjaga

---

## 4. `rollback()`

```java
koneksi.rollback();
```

Fungsi:

* Membatalkan **seluruh perubahan** sejak transaction dimulai
* Database kembali ke kondisi awal
* Tidak ada data setengah jadi

Digunakan ketika:

* Terjadi error
* Validasi gagal
* Generated key tidak ditemukan
* Konsistensi tidak terjamin

---

## 5. Alur Transaksi yang Benar

### 5.1 Pola Umum

```java
try {
    koneksi.setAutoCommit(false);

    // operasi database
    INSERT / UPDATE / DELETE

    koneksi.commit(); // sukses
} catch (Exception e) {
    koneksi.rollback(); // gagal → batalkan semua
} finally {
    koneksi.setAutoCommit(true); // kembalikan ke default
}
```

---

## 6. Contoh Kasus Nyata

### Tanpa Transaksi (Berbahaya)

```java
INSERT account
INSERT log_transaksi  // gagal
```

Hasil:

* `account` masuk
* `log_transaksi` tidak
* Database tidak konsisten

---

### Dengan Transaksi (Aman)

```java
setAutoCommit(false)

INSERT account
INSERT log_transaksi

commit()
```

Jika salah satu gagal:

```java
rollback()
```

Hasil:

* Tidak ada data setengah jadi
* Konsistensi terjaga

---

## 7. Prinsip Desain yang Wajib Diingat

1. **Database dan data lokal harus selalu konsisten**
2. **Insert sukses tapi ID tidak tersedia = transaction gagal**
3. Jangan pernah:

    * insert data
    * lalu membiarkannya tanpa bisa direferensikan
4. Transaksi **bukan opsional** untuk operasi penting

---

## 8. Catatan Khusus SQLite

* SQLite **mendukung transaction sepenuhnya**
* `setAutoCommit(false)` aman dan ringan
* Sangat dianjurkan bahkan untuk single insert yang krusial

---

## 9. Ringkasan Singkat

| Method                 | Fungsi                  |
| ---------------------- | ----------------------- |
| `setAutoCommit(false)` | Memulai transaction       |
| `commit()`             | Menyimpan perubahan     |
| `rollback()`           | Membatalkan perubahan   |
| `setAutoCommit(true)`  | Kembali ke mode default |

---

## 10. Kesimpulan

Menggunakan transaction berarti:

* Tidak ada data yatim
* Tidak ada state setengah jadi
* Aplikasi lebih stabil
* Bug lebih sedikit
* Masa depan lebih damai

> **Jika data itu penting, transaction bukan pilihan — tapi kewajiban.**