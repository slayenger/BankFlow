# BankFlow

Это сервис для осуществления финансовых операций между пользователями,
а также управления своими банковскими аккаунтами.

## Команды:
```
git clone https://github.com/slayenger/BankFlow
cd BankFlow
./mvnw install
cd target
java -jar test_task-0.0.1-SNAPSHOT.jar
```


## Функциональные возможности

1. **Пользователи и банковские аккаунты:** Каждый пользователь имеет свой уникальный банковский аккаунт,
на котором изначально находится определенная сумма.
2. **Регистрация новых пользователей:** Существует API для регистрации новых пользователей с указанием логина, пароля,
начальной суммы на счете, телефона и электронной почты.
3. **Управление данными пользователя:** Пользователи могут добавлять/изменять свои номера телефонов и/или email,
а также удалять их (кроме последнего).
4. **Поиск и фильтрация пользователей:** Реализован API поиска с возможностью фильтрации и пагинации
по дате рождения, телефону, ФИО и email.
5. **Аутентификация и авторизация:** Доступ к API аутентифицирован с использованием JWT. Служебное API для 
создания новых клиентов доступно без аутентификации.
6. **Начисление процентов:** Раз в минуту баланс каждого клиента увеличивается на 5%,
но не более чем на 207% от начального депозита.
7. **Перевод средств:** Пользователи могут осуществлять перевод денег с одного счета на другой с соблюдением 
всех необходимых валидаций и потокобезопасности.

## Нефункциональные требования
1. **OpenAPI/Swagger:** Добавлено описание API с помощью OpenAPI/Swagger для удобной документации.

2. **Логирование:** Реализовано логирование для отслеживания действий и ошибок в приложении.

3. **Тестирование:** Написаны тесты для обеспечения покрытия функционала перевода денег и других важных аспектов приложения.

## Описание API

### AuthController
#### **Описание**:
Контроллер, отвечающий за аутентификацию пользователей.

#### Методы
1. **authenticateUser**
 - Метод: POST 
 - Путь: /auth
 - Описание: Позволяет пользователю аутентифицироваться в системе.
 - Параметры запроса: AuthenticateRequest (тело запроса)
Возвращаемый результат: ResponseEntity
 - Возможные коды ответа:
   - 200 OK: Успешная аутентификация
   - 400 Bad Request: Некорректные учетные данные

### ContactInfoController
#### **Описание:**
Контроллер, отвечающий за управление контактной информацией пользователей.

#### Методы

1. **addEmailToUser**
 - Метод: PATCH
 -  Путь: /api/contact_info/add-email
 -  Описание: Добавляет email пользователю.
 -  Параметры запроса: String email (тело запроса), CustomUserDetails (аутентифицированный пользователь)
 -  Возвращаемый результат: ResponseEntity
 -  Возможные коды ответа:
    - 200 OK: Email успешно добавлен
    - 400 Bad Request: Некорректные данные
2. **addPhoneNumber**
 - Метод: PATCH
 -  Путь: /api/contact_info/add-phone-number
 -  Описание: Добавляет номер телефона пользователю.
 -  Параметры запроса: String phoneNumber (тело запроса), CustomUserDetails (аутентифицированный пользователь)
 -  Возвращаемый результат: ResponseEntity
 -  Возможные коды ответа:
     - 200 OK: Номер телефона успешно добавлен
     - 400 Bad Request: Некорректные данные
3. **changeEmail**
- Метод: PATCH
-  Путь: /api/contact_info/change-email
-  Описание: Изменяет email пользователя.
-  Параметры запроса: ChangeEmailRequest (тело запроса), CustomUserDetails (аутентифицированный пользователь)
-  Возвращаемый результат: ResponseEntity
 - Возможные коды ответа:
   - 200 OK: Email успешно изменен
   - 400 Bad Request: Некорректные данные
4. **changePhoneNumber**
- Метод: PATCH
- Путь: /api/contact_info/change-phone-number
- Описание: Изменяет номер телефона пользователя.
- Параметры запроса: ChangeNumberRequest (тело запроса), CustomUserDetails (аутентифицированный пользователь)
- Возвращаемый результат: ResponseEntity
- Возможные коды ответа:
  - 200 OK: Номер телефона успешно изменен
  - 400 Bad Request: Некорректные данные
5. **removeEmail**
- Метод: DELETE
- Путь: /api/contact_info/remove-email
- Описание: Удаляет email пользователя.
- Параметры запроса: String email (тело запроса), CustomUserDetails (аутентифицированный пользователь)
- Возвращаемый результат: ResponseEntity
- Возможные коды ответа:
    - 200 OK: Email успешно удален
    - 400 Bad Request: Некорректные данные
6. **removePhoneNumber**
- Метод: DELETE
- Путь: /api/contact_info/remove-phone-number
- Описание: Удаляет номер телефона пользователя.
- Параметры запроса: String number (тело запроса), CustomUserDetails (аутентифицированный пользователь)
- Возвращаемый результат: ResponseEntity
- Возможные коды ответа:
  - 200 OK: Номер телефона успешно удален
  - 400 Bad Request: Некорректные данные

### Search API
Search API
**Поиск по почте**
- URL: /api/search/by-email
- Метод: GET
- Параметры запроса:
  - email: Строка - адрес электронной почты для поиска
- Возвращаемые значения:
  - 200 OK: Успешный запрос. Возвращает информацию о пользователе с указанным email.
  - 400 Bad Request: В случае, если пользователь с указанным email не найден.

**Поиск по номеру телефона:**
- URL: /api/search/by-phone-number
- Метод: GET
- Параметры запроса:
  - phoneNumber: Строка - номер телефона для поиска
- Возвращаемые значения:
  - 200 OK: Успешный запрос. Возвращает информацию о пользователе с указанным номером телефона.
  - 400 Bad Request: В случае, если пользователь с указанным номером телефона не найден.

**Поиск по дате рождения:**
- URL: /api/search/by-date-of-birth
- Метод: GET
- Параметры запроса:
  - page: Целое число - номер страницы (по умолчанию 0)
  - size: Целое число - количество записей на странице (по умолчанию 10)
  - dateOfBirth: Строка - дата рождения для поиска в формате "dd.MM.yyyy"
- Возвращаемые значения:
   - 200 OK: Успешный запрос. Возвращает информацию о пользователях с указанной датой рождения.
   - 400 Bad Request: В случае, если произошла ошибка парсинга даты или пользователи с указанной датой рождения не найдены.

**Поиск по ФИО:**

- URL: /api/search/by-full-name
- Метод: GET
- Параметры запроса:
  - page: Целое число - номер страницы (по умолчанию 0)
  - size: Целое число - количество записей на странице (по умолчанию 10)
  - fullName: Строка - полное имя для поиска
- Возвращаемые значения:
  - 200 OK: Успешный запрос. Возвращает информацию о пользователях с указанным полным именем.
  - 400 Bad Request: В случае, если пользователи с указанным полным именем не найдены.

## Transaction API
**Отправка денег по ID получателя**
- URL: /api/transaction/send-money
- Метод: POST
- Тело запроса:
  - receiverId: UUID - идентификатор получателя
  - amount: BigDecimal - сумма для отправки
- Возвращаемые значения:
   - 201 Created: Успешный запрос. Операция успешно завершена.
   - 400 Bad Request: В случае, если произошла ошибка или недостаточно средств на счету отправителя.

**Отправка денег по номеру телефона получателя**
- URL: /api/transaction/send-money-by-number
- Метод: POST
- Тело запроса:
   - phoneNumber: Строка - номер телефона получателя
   - amount: BigDecimal - сумма для отправки
- Возвращаемые значения:
   - 201 Created: Успешный запрос. Операция успешно завершена.
   - 400 Bad Request: В случае, если произошла ошибка или недостаточно средств на счету отправителя.

### User API
**Создание нового пользователя**
- URL: /api/user/create
- Метод: POST
- Тело запроса: Объект типа RegistrationRequest, содержащий данные нового пользователя
- Возвращаемые значения:
  - 201 Created: Успешный запрос. Пользователь успешно создан.
  - 400 Bad Request: В случае, если произошла ошибка при создании пользователя, например, если указанные данные уже существуют.

**Установка полного имени пользователя**
- URL: /api/user/set-full-name
- Метод: PATCH
- Параметры запроса:
   - fullName: Строка - полное имя пользователя
- Возвращаемые значения:
   - 200 OK: Успешный запрос. Полное имя пользователя успешно установлено.
   - 400 Bad Request: В случае, если произошла ошибка при установке полного имени пользователя, например, если пользователь уже установил свое имя.

**Установка даты рождения пользователя**
- URL: /api/user/set-date-of-birth
- Метод: PATCH
- Тело запроса: 
  - Строка - дата рождения пользователя
- Возвращаемые значения:
   - 200 OK: Успешный запрос. Дата рождения пользователя успешно установлена.
   - 400 Bad Request: В случае, если произошла ошибка при установке даты рождения пользователя, например, если дата имеет неверный формат или пользователь уже установил свою дату рождения.
