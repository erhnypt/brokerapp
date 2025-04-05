# Broker Uygulaması Kullanım Kılavuzu

Bu belge, müşteriler, varlıklar ve siparişlerin yönetimi için Broker Uygulaması API'sini nasıl kullanacağınıza dair ayrıntılı talimatlar sunar.

## Kimlik Doğrulama

### Giriş
```
POST /api/auth/login
```

**İstek Gövdesi:**
```json
{
  "username": "kullanici_adiniz",
  "password": "sifreniz"
}
```

**Yanıt:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "username": "kullanici_adiniz",
  "role": "USER"
}
```

Sonraki istekler için döndürülen belirteçi Authorization başlığında kullanın:
```
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

### `Varsayılan Yönetici Kimlik Bilgileri`
Sistem, varsayılan bir yönetici hesabıyla birlikte gelir:
```
Kullanıcı adı: admin
Şifre: admin123
```
Tüm uç noktalara erişmek için yönetici ayrıcalıklarıyla giriş yapmak için bu kimlik bilgilerini kullanabilirsiniz.

## Swagger UI Dokümantasyonu

Broker Uygulaması, API'yi kolayca keşfetmek ve test etmek için bir Swagger UI arayüzü sağlar. Swagger UI'ı kullanmak için:

1. Uygulamayı başlatın
2. Tarayıcınızda http://localhost:8080/swagger-ui/ adresine gidin
3. Arayüz, tüm mevcut API uç noktalarını denetici gruplarına göre gösterir
4. Ayrıntılarını görmek için herhangi bir uç noktaya tıklayın
5. Kimlik doğrulama gerektiren uç noktalar için:
   - Üstteki "Authorize" düğmesine tıklayın
   - JWT belirtecinizi `Bearer belirteciniz-buraya` şeklinde girin
   - Kimlik doğrulamayı uygulamak için "Authorize" düğmesine tıklayın
6. Uç noktaları doğrudan kullanıcı arayüzünden test etmek için:
   - Gerekli parametreleri girin
   - "Execute" düğmesine tıklayın
   - Yanıtı görüntüleyin

Swagger UI, her uç nokta için parametre gereksinimleri ve yanıt formatları dahil olmak üzere ayrıntılı belgelendirme sağladığından, API'yi keşfetmek ve test etmek için önerilen yöntemdir.

## Müşteri Yönetimi

### Tüm Müşterileri Listele (Sadece Yönetici)
```
GET /api/customers
```

### ID'ye Göre Müşteri Al
```
GET /api/customers/{id}
```
Not: Normal kullanıcılar yalnızca kendi müşteri kayıtlarına erişebilir.

### Yeni Müşteri Oluştur
```
POST /api/customers
```

**İstek Gövdesi:**
```json
{
  "name": "Müşteri Adı",
  "email": "musteri@ornek.com",
  "phone": "+901234567890"
}
```

### Müşteri Güncelle
```
PUT /api/customers/{id}
```

**İstek Gövdesi:**
```json
{
  "name": "Güncellenmiş Ad",
  "email": "guncellenmis@ornek.com",
  "phone": "+901234567890"
}
```

### Müşteri Sil
```
DELETE /api/customers/{id}
```

## Varlık Yönetimi

### Bir Müşteri İçin Varlıkları Listele
```
GET /api/assets/customer/{customerId}
```
Belirli bir müşteriye ait tüm varlıkları getirir.

### ID'ye Göre Varlık Al
```
GET /api/assets/{id}
```

### Varlık Oluştur
```
POST /api/assets
```

**İstek Gövdesi:**
```json
{
  "customerId": 1,
  "stockId": 1,
  "amount": 10.5
}
```

## Hisse Senedi Yönetimi (Sadece Yönetici)

### Tüm Hisse Senetlerini Listele
```
GET /api/stocks
```

### ID'ye Göre Hisse Senedi Al
```
GET /api/stocks/{id}
```

### İsme Göre Hisse Senedi Al
```
GET /api/stocks/name/{name}
```

### Yeni Hisse Senedi Oluştur
```
POST /api/stocks
```

**İstek Gövdesi:**
```json
{
  "name": "HISSETRY",
  "price": 100.50,
  "amount": 1000
}
```
Not: Hisse senedi adları Türk Lirası'nı belirten "TRY" ile bitmelidir.

### Hisse Senedi Güncelle
```
PUT /api/stocks/{id}
```

**İstek Gövdesi:**
```json
{
  "name": "HISSETRY",
  "price": 105.75,
  "amount": 1200
}
```

### Hisse Senedi Fiyatını Güncelle
```
PUT /api/stocks/{id}/price
```

**İstek Gövdesi:**
```json
{
  "price": 110.25
}
```

### Hisse Senedi Miktarını Artır
```
PUT /api/stocks/{id}/amount
```

**İstek Gövdesi:**
```json
{
  "amount": 500
}
```

### Hisse Senedi Sil
```
DELETE /api/stocks/{id}
```

## Sipariş Yönetimi

### Bir Müşteri İçin Siparişleri Listele
```
GET /api/orders/customer/{customerId}
```
Belirli bir müşteri tarafından oluşturulan tüm siparişleri getirir.

### ID'ye Göre Sipariş Al
```
GET /api/orders/{id}
```

### Yeni Alış Siparişi Oluştur
```
POST /api/orders/buy
```

**İstek Gövdesi:**
```json
{
  "customerId": 1,
  "stockId": 1,
  "price": 100.00,
  "amount": 5.0
}
```

### Yeni Satış Siparişi Oluştur
```
POST /api/orders/sell
```

**İstek Gövdesi:**
```json
{
  "customerId": 1,
  "stockId": 1,
  "price": 105.00,
  "amount": 3.0
}
```

### Sipariş İptal Et
```
DELETE /api/orders/{id}
```

## Hata İşleme

Tüm API uç noktaları standart HTTP durum kodlarını döndürür:
- 200: Başarılı
- 201: Oluşturuldu
- 400: Kötü İstek
- 401: Yetkisiz
- 403: Yasaklanmış
- 404: Bulunamadı
- 500: İç Sunucu Hatası

Hata yanıtları, neyin yanlış gittiği hakkında ayrıntılar içerir:
```json
{
  "timestamp": "2023-06-15T10:15:30.123Z",
  "status": 400,
  "error": "Bad Request",
  "message": "Doğrulama başarısız",
  "path": "/api/orders/buy"
}
```

## Sipariş İşleme Notları

1. Alış veya satış siparişleri oluşturulduğunda, sistem otomatik olarak bunları mevcut siparişlerle eşleştirmeye çalışır.
2. Eşleşen siparişler hemen yürütülür ve varlıklar buna göre güncellenir.
3. Kısmen eşleşen siparişler, eşleşmeyen kısım için aktif kalır.
4. Siparişler fiyat ve zaman önceliğine göre eşleştirilir.
5. Siparişlerinizin durumunu, siparişleri listele uç noktasını kullanarak kontrol edebilirsiniz.

Herhangi bir ek yardım için lütfen support@brokerapp.com adresinden iletişime geçin. 