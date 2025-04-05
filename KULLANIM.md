# Broker Uygulaması Dağıtım ve Kullanım Kılavuzu

Bu belge, Broker Uygulamasının farklı ortamlar için nasıl oluşturulacağını, dağıtılacağını ve çalıştırılacağını açıklar.

## Sistem Gereksinimleri

- Java 17 veya üzeri
- Maven 3.6.x veya üzeri
- Git

## Uygulamayı Oluşturma

1. Depoyu klonlayın:
   ```bash
   git clone https://github.com/kullaniciadi/brokerApp.git
   cd brokerApp
   ```

2. Uygulamayı derleyin:
   ```bash
   mvn clean package
   ```
   Bu işlem, `target` dizininde bir JAR dosyası oluşturacaktır: `broker-app-0.0.1-SNAPSHOT.jar`

## Uygulamayı Çalıştırma

### JAR Dosyasını Doğrudan Çalıştırma

```bash
java -jar target/broker-app-0.0.1-SNAPSHOT.jar
```

Varsayılan olarak, uygulama H2 bellek içi veritabanı kullanır ve 8080 portunda çalışır.

### Belirli Profille Çalıştırma

Uygulama, çeşitli ortamlar için farklı profilleri destekler:

```bash
java -jar target/broker-app-0.0.1-SNAPSHOT.jar --spring.profiles.active=dev
```

Mevcut profiller:
- `dev`: Geliştirme ortamı (varsayılan)
- `test`: Test ortamı
- `prod`: Üretim ortamı

## Ortam Yapılandırması

Uygulama, farklı ortamlar için farklı yapılandırma dosyaları kullanır:

- `application.properties`: Varsayılan yapılandırma
- `application-dev.properties`: Geliştirme ortamı
- `application-test.properties`: Test ortamı
- `application-prod.properties`: Üretim ortamı

Yapılandırmaları özelleştirmek için:

1. Yeni bir dosya oluşturun: `application-custom.properties`
2. Herhangi bir yapılandırma özelliğini geçersiz kılın
3. Uygulamayı özel profille çalıştırın:
   ```bash
   java -jar target/broker-app-0.0.1-SNAPSHOT.jar --spring.profiles.active=custom
   ```

## Veritabanı Yapılandırması

### H2 Veritabanı (Varsayılan)

Uygulama varsayılan olarak H2 bellek içi veritabanı kullanır. H2 konsolu http://localhost:8080/h2-console adresinden şu kimlik bilgileriyle erişilebilir:
- JDBC URL: `jdbc:h2:mem:testdb`
- Kullanıcı adı: `sa`
- Şifre: `` (boş)
-YAPILAN İŞLEMLERİ BURADAN GÖREBİLİRSİNİZ

### Harici Veritabanı Yapılandırması

Harici bir veritabanı kullanmak için, profile özgü uygulama özellikleri dosyanıza aşağıdaki özellikleri ekleyin:

```properties
spring.datasource.url=jdbc:mysql://veritabani-sunucunuz:3306/broker_db
spring.datasource.username=kullanici_adiniz
spring.datasource.password=sifreniz
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect
spring.jpa.hibernate.ddl-auto=update
```

## Güvenlik Yapılandırması

Varsayılan yönetici kimlik bilgileri:
- Kullanıcı adı: `admin`
- Şifre: `admin123`

Yönetici kimlik bilgilerini değiştirmek için aşağıdaki özellikleri değiştirin:

```properties
admin.username=ozel_admin
admin.password=ozel_sifre
```

## API Dokümantasyonu (Swagger UI)

Uygulama, kapsamlı API dokümantasyonunu Swagger UI aracılığıyla sağlar ve uygulama çalışırken http://localhost:8080/swagger-ui/ adresinden erişilebilir.

### Swagger UI Kullanımı

1. Uygulamayı başlatın
2. Bir web tarayıcısı açın ve http://localhost:8080/swagger-ui/ adresine gidin
3. Kontrollere göre düzenlenmiş tüm kullanılabilir API uç noktalarının bir listesini göreceksiniz
4. Ayrıntılarını görmek için herhangi bir uç noktaya tıklayın, bu ayrıntılar şunları içerir:
   - Gerekli parametreler
   - İstek gövdesi yapısı (POST ve PUT istekleri için)
   - Yanıt kodları ve gövde yapıları
   - Kimlik doğrulama gereksinimleri

### Swagger UI'da Kimlik Doğrulama

Kimlik doğrulama gerektiren uç noktalar için:
1. Sayfanın üst kısmındaki "Authorize" düğmesine tıklayın
2. JWT belirtecinizi şu formatta girin: `Bearer belirteciniz-buraya`
3. "Authorize" düğmesine tıklayın ve iletişim kutusunu kapatın
4. Artık güvenli uç noktaları doğrudan kullanıcı arayüzünden test edebilirsiniz

### Uç Noktaları Test Etme

1. Bir uç nokta seçin
2. Gerekli parametreleri veya istek gövdesini doldurun
3. "Execute" düğmesine tıklayın
4. Aşağıda sunucu yanıtını görüntüleyin

Swagger UI, Postman gibi ek araçlara ihtiyaç duymadan tüm kullanılabilir API uç noktalarını keşfetmek ve test etmek için uygun bir yol sağlar.

## İzleme ve Sağlık Kontrolleri

Sağlık kontrolü bitiş noktası: http://localhost:8080/actuator/health

## Günlük Kaydı

Uygulama günlükleri, günlük rotasyonlu `logs` dizininde saklanır. Günlük kaydı seviyesi, uygulama özelliklerinde ayarlanabilir:

```properties
logging.level.root=INFO
logging.level.com.brokerapp=DEBUG
```

## Sorun Giderme

### Yaygın Sorunlar

1. **Uygulama başlatılamıyor**
   - 8080 portunun zaten kullanımda olup olmadığını kontrol edin
   - Java sürümünü doğrulayın: `java -version`
   - Harici bir veritabanı kullanıyorsanız veritabanı bağlantısını doğrulayın

2. **Kimlik doğrulama sorunları**
   - JWT jetonlarının düzgün yapılandırıldığını doğrulayın
   - Gizli anahtarın örnekler arasında tutarlı olduğunu kontrol edin

3. **Veritabanı bağlantı sorunları**
   - Veritabanı kimlik bilgilerini doğrulayın
   - Veritabanı sunucusunun çalıştığından emin olun
   - Ağ bağlantısını kontrol edin

Ek yardım için lütfen support@brokerapp.com adresinden destek ile iletişime geçin 