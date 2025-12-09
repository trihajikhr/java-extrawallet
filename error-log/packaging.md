# Error Log

Ada error ketika menjalankan `mvn clean install`. Beberapa error log dan solusi:

## Masalah pada Dependency
Pastikan 2 plugin jupiter ada, jika tidak maka akan ada error:

```bash
[ERROR] COMPILATION ERROR : 
[INFO] -------------------------------------------------------------
[ERROR] /D:/Github-Grinder/Java/java-extrawallet/src/test/java/app/AppTest.java:[3,29] package org.junit.jupiter.api does not exist
[ERROR] /D:/Github-Grinder/Java/java-extrawallet/src/test/java/app/AppTest.java:[5,36] package org.junit.jupiter.api does not exist
```
Solusinya adalah dengan menambahkan 2 plugin Jupiter, dengan dependency berikut ditambahkan pada `pom.xml`:

```xml
<!-- JANGAN DIHAPUS, UNTUK PACKAGING! -->
<dependency>
    <groupId>org.junit.jupiter</groupId>
    <artifactId>junit-jupiter-engine</artifactId>
    <version>5.7.1</version>
    <scope>test</scope>
</dependency>
<dependency>
    <groupId>org.junit.jupiter</groupId>
    <artifactId>junit-jupiter-params</artifactId>
    <version>5.7.1</version>
    <scope>test</scope>
</dependency>
```

## JAR tidak bisa dibuka
Ada masalah dimana aksi `mvn clean install` menghasilkan format `jar` yang tidak bisa dibuka. Tapi anehnya masih bisa dibuka dengan `java -jar target/<nama-file-jar>.jar`, walaupun menghasilkan error berikut:

```bash
WARNING: Unsupported JavaFX configuration: classes were loaded from 'unnamed module @29047afe'
WARNING: A restricted method in java.lang.System has been called
WARNING: java.lang.System::load has been called by com.sun.glass.utils.NativeLibLoader in an unnamed module (file:/D:/Github-Grinder/Java/java-extrawallet/target/java-extrawallet-v0.0.1.jar)
WARNING: Use --enable-native-access=ALL-UNNAMED to avoid a warning for callers in this module
WARNING: Restricted methods will be blocked in a future release unless native access is enabled
```
   
Solusinya adalah dengan menjalankan file jar tersebut melalui terminal dengan:

```bash
java --enable-native-access=ALL-UNNAMED -jar target/java-extrawallet-v0.0.1.jar
```

atau
```bash
java -jar target/java-extrawallet-v0.0.1.jar
```

Solusi yang paling baik (saat ini) adalah dengan melakukan format langsung ke exe, ini karena aku mendapatkan salah satu informasi bahwa:

> Itu sebabnya di dunia profesional, JavaFX app tidak pernah didistribusikan sebagai file JAR standalone. Selalu EXE/MSI, DMG, atau ZIP dengan launcher script.

Jadi, ketika dilakukan format dari JAR ke EXE, bisa menambahkan opsi berikut pada pproses packaging:

```bash
jpackage ^
  --name ExtraWallet ^
  --input target ^
  --main-jar extrawallet-v0.0.1.jar ^
  --main-class app.Main ^
  --type exe ^
  --icon icon.ico ^
  --java-options "--enable-native-access=ALL-UNNAMED" 
```

Yaitu pada bagian `--java-options "--enable-native-access=ALL-UNNAMED"`.

### Perbaikan

Pada package `app`, aku harus membuat dua class yaitu `App` dan `Main`, dimana keedua class ini harus saling mengoper supaya ketika dijadikan exe berhasil dibuka. Aku tidak tahu kenapa harus seperti ini, aku hanya mengetahuainya lewat stackOverflow.