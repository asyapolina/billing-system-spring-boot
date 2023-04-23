## Customer Relationship Management

--------

Сервис через который происходит взаимодействие с пользователем.

--------
### Реализованна авторизация с помощью Spring Security, JWT token.

При авторизации по юзернейму и паролю пользователю присылается токен, который нужно вставить в поле Authorization -> Bearer token. После чего делать другие запросы.


Есть два пользователя для теста (менеджер и клиент с номером телефона).
Клиент может получить данные только о своих звонках, но пополнить счет он может любому номеру телефона.

-------
### Реализованные эндпоинты:

Endpoint:

    localhost:8083/abonent/pay

Method:

    PATCH

Request body:

    {
        "phoneNumber": "79013402041",
        "money": 100.00
    }

Response body:

    {
        "id": 38,
        "phoneNumber": "79000223103",
        "money": 320.00
    }
-----

Endpoint:

    localhost:8083/abonent/report/{numberPhone}

Method:

    GET

Response body:

    {
        "id": 26,
        "phoneNumber": "79340343130",
        "tariffId": "06",
        "payload": [
           {
               "id": 157,
               "callType": "01",
               "startTime": "2010-01-02T16:53:04",
               "endTime": "2010-01-02T18:31:12",
               "duration": "01:38:08",
              "cost": 99.00
           },
           {
               "id": 158,
               "callType": "01",
               "startTime": "2010-01-10T16:32:25",
               "endTime": "2010-01-10T18:22:27",
               "duration": "01:50:02",
               "cost": 111.00
           },
        ],
        "totalCost": 210.00,
        "monetaryUnit": "rubles"
    }

------

Endpoint:

    localhost:8083/manager/changeTariff

Method:

    PATCH

Request body:

    {
       "phoneNumber": "79340343130",
       "tariffId": "11"
    }

Response body:

    {
       "id": 487,
       "phoneNumber": "79340343130",
       "tariffId": "11"
    }

------

Endpoint:

    localhost:8083/manager/abonent

Method:

    POST

Request body:
    
    {
        "phoneNumber": "79340343130",
        "tariffId": "03",
        "balance": 500
    }

Response body:

    {
        "phoneNumber": "79340343130",
        "tariffId": "03",
        "balance": 500
    }

-----

Endpoint:

    localhost:8083/manager/billing

Method:

    PATCH

Request body:

    {
        "action": "run"
    }

Response body:

    {
        "numbers": [
            {
                "phoneNumber": "79200336533",
                "balance": -165.50
            },
            {
                "phoneNumber": "79242231400",
                "balance": -31.50
            },
            {
                "phoneNumber": "79112400032",
                "balance": 513.00
            },
            {
                "phoneNumber": "79200309453",
                "balance": -403.00
            },
            {
                "phoneNumber": "79313022142",
                "balance": 291.00
            }
        ]
    }