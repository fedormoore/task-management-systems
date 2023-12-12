# Task Management System

Система обеспечивает создание, редактирование, удаление и просмотр задач. К задачам можно писать комментарии.

## Запуск приложения

```bash
./mvnw.cmd clean complete package
```

Приложение работает на порту 8080.

http://localhost:8080/api/v1/**

Настроен swagger. Описание к методам контроллера.

### Есть возможность зарегистрировать нового пользователя.

Для этого необходимо отправить JSON запрос на адрес http://localhost:8080/api/v1/auth/signUp

Пример запроса:

```json
{
  "email": "fedormoore@gmail.com",
  "password": "fedormoore@gmail.com",
  "userName": "fedormoore"
}
```

### В системе заведен пользователь. С его помощью можно пройти аутентификацию.

Для этого необходимо отправить JSON запрос на адрес http://localhost:8080/api/v1/auth/signIn

Пример запроса:

```json
{
  "email": "fedormoore@gmail.com",
  "password": "fedormoore@gmail.com"
}
```