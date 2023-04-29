## Nexign bootcamp 2023 (dev)
**Автор**: Репенко Аннемария

-----------

#### Система биллинга реализованная в виде 4 микросервисов и одного общего сервиса, который предоставляет jpa сущности.

### Использованные технологии:
- Java 17
- Spring Boot
- Maven
- Tomcat
- PostgreSQL
- ActiveMQ
- Spring Security
- Spring Cache
- Spring AOP

### Подробнее о каждом сервисе:

[Микросервис CDR](CDR-service/README.md)  
[Микросервис BRT](BRT-service/README.md)  
[Микросервис HRS](HRS-service/README.md)  
[Микросервис CRM](CRM-service/README.md)

### Миграции и данные для бд:

[Данные клиентов и тарифов](db/data)    
[Миграции](db/migrations)

### Подробнее об архитектуре базы данных:  [тык](JPA-entities/README.md)        

-------
Запуск сервисов:    
    
    CRM -> CDR -> HRS -> BRT

Для работы Active MQ необходимо запустить [docker-compose](docker-compose.yml)

### Тесты
[CDR Generator tests](CDR-service/README.md)