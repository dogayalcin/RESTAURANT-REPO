## Teknolojiler

- Java 17
- Spring Boot
- Maven

---

## Kurulum ve Calistirma

- PosrgreSQL ile veri tabani olusturulmali
- application.properties olusturulan veri tabanina gore guncellenmeli
- mvn clean install
  mvn spring-boot:run
  veya
  mvn clean
  mvn compile
  mvn test
  
---

## Proje Yapisi

- controller –> Endpoint'ler
- dto -> Request ve response modelleri (veri tasima)
- enums
- model -> Entity
- repository -> JPA repository
- service -> is mantigi

---

## Ornek API Kullanimi

Ornek Siparis Goruntuleme
GET http://localhost:8080/api/orders?userId=1

---

## Katmanli (Layered) Mimari

- Controller Katmani -> HTTP request
- Service Katmani -> Is mantigi
- Repository & Model Katmanlari -> Veritabani islemleri
