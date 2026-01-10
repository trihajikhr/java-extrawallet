# Sytnax untuk persiapan rilis versi

1. Pastikan tidak ada error pada program, dan branch `main` sudah menerima semua commit. Bersihkan branch dengan hapus branch yang sudah di merge commit. 
2. Perbarui versi aplikasi, dengan cara masuk ke `pom.xml`, dan perbarui semantik versioning aplikasi yang akan dirilis:

    ```xml
    <version>v1.0.0</version>
    ```
    
    Dirubah menjadi, misal:
    
    ```xml
    <version>v1.5.0</version>
    ```

> [!WARNING]
> Pemberian versining dilakukan di Maven `pom.xml` file, namun settingan lain , seperti pemberian nama aplikasi, icons, dll, dilakukan melalui 'JPackage'. Pembahasanya ada dibawah!
    
Maven bertugas membangun artifact aplikasi (JAR) dan mengelola dependency secara lintas platform, sedangkan **jpackage digunakan khusus untuk membuat installer OS (EXE/MSI/DMG)** dengan konfigurasi yang sifatnya platform-spesifik.

Karena itu, properti seperti nama aplikasi, vendor, icon, dan shortcut **tidak didefinisikan di `pom.xml`**, melainkan melalui perintah atau script `jpackage` saat proses distribusi.

2. Rubah project Java keseluruhan menjadi `JAR` file dengan cara berikut:

    ```bash
    mvn clean install
    ```
   Maka akan dibuat file `JAR` di folder `target/app.jar`.

3. Konfigurasi Maven yang digunakan di `pom.xml` akan membuat 2 file JAR, satu untuk developer, dimana library tidak dibundle, dan satunya lagi adalah JAR _standalone_. JAR _standalone_ inilah yang akan kita gunakan untuk konversi ke `EXE`. File ini ditandai dengan ukuran filenya yang lebih besar daripada satunya.

4. Test apakah JAR tersebut dapat berjalan, gunakan perintah berikut:

    ```bash
    java -jar target/extrawallet-v1.0.0.jar
    ```

5. Jika aplikasi berjalan, langkah selanjutnya adalah mengkonversi ke EXE dengan menggunakan JPackage berikut.
    Bagian `--name` berisi nama aplikasi, beri tanda petik ganda `""` jika ingin menyematkan spasi diantara nama aplikasi. Lalu pada bagian `--app-version` berikan semver versi aplikasi. Cukup gunakan angka, tidak perlu menambahkan `v` didepanya, karena JPackage hanya menerima angka, lagipula format resmi versioning (SemVer): MAJOR.MINOR.PATCH. Penggunaan versioning misal dengan `v1.0.0` dilakukan dengan `git tag` 

    ```bash
    jpackage 
    --name "Extra Wallet" 
    --app-version 1.0.0
    --vendor Extra-Inc 
    --type exe 
    --input target 
    --dest target 
    --main-jar extrawallet-v1.0.0.jar 
    --main-class app.Main 
    --module-path "C:\Program Files\Java\javafx-jmods-25.0.1;C:\Program Files\Java\jdk-25.0.1\jmods" 
    --add-modules javafx.base,javafx.graphics,javafx.controls,javafx.fxml
    --icon "src/main/resources/app-icon/favicon.ico" 
    --win-shortcut 
    --win-menu
    ```
   
    Versi yang dicoba untuk tidak error lagi:

    ```bash
        jpackage 
        --name "Extra Wallet" 
        --app-version 1.0.0
        --vendor Extra-Inc 
        --type exe 
        --input target 
        --dest target 
        --main-jar extrawallet-v1.0.0.jar 
        --main-class app.Main 
        --module-path "C:\Program Files\Java\javafx-jmods-25.0.1" 
        --add-modules javafx.base,javafx.graphics,javafx.controls,javafx.fxml
        --icon "src/main/resources/app-icon/favicon.ico" 
        --win-shortcut 
        --win-menu
    ```
    
    Atau berikut yang siap tempel di terminal (lakukan perubahan pada `--app-version` sesuai versi aplikasi):
    
    ```bash
    jpackage --name "Extra Wallet" --app-version 1.0.0 --vendor Extra-Inc --type exe --input target  --dest target --main-jar extrawallet-v1.0.0.jar --main-class app.Main --module-path "C:\Program Files\Java\javafx-jmods-25.0.1;C:\Program Files\Java\jdk-25.0.1\jmods" --add-modules javafx.base,javafx.graphics,javafx.controls,javafx.fxml --icon "src/main/resources/app-icon/favicon.ico" --win-shortcut --win-menu --java-options "-verbose:class" "-Dapp.mode=prod"
    ```
   
    Versi kedua yang jika pertama error:
    
    ```bash
    jpackage --name "Extra Wallet" --app-version 1.0.0 --vendor Extra-Inc --type exe --input target --dest target --main-jar extrawallet-v1.0.0.jar --main-class app.Main --module-path "C:\Program Files\Java\javafx-jmods-25.0.1" --add-modules javafx.base,javafx.graphics,javafx.controls,javafx.fxml --icon "src/main/resources/app-icon/favicon.ico" --win-shortcut --win-menu
    ```
   
buat runtims:

```bash
jlink --module-path "C:\Program Files\Java\jdk-25.0.1\jmods;C:\Program Files\Java\javafx-jmods-25.0.1" --add-modules java.base,javafx.controls,javafx.fxml,javafx.graphics --output runtime
```

Lalu package

```bash
jpackage --name "Extra Wallet" --app-version 1.0.0 --vendor Extra-Inc  --type exe --input target --dest target --main-jar extrawallet-v1.0.0.jar --main-class app.Main --runtime-image runtime --icon "src/main/resources/app-icon/favicon.ico" --win-shortcut --win-menu
```