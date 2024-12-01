# Управление иерархией категорий (Telegram-бот)

Это Telegram-бот, который позволяет пользователям создавать, просматривать и удалять дерево категорий. Бот также поддерживает работу с Excel-документами для загрузки и скачивания дерева категорий.

## Используемые технологии

- **Java**
- **Spring Boot**
- **PostgreSQL**
- **Maven** (для сборки проекта)
- **Docker**

## Основные функциональные возможности

- **/viewTree** — отображает дерево категорий в структурированном виде.
- **/addElement <название элемента>** — добавляет новый элемент. Если не указан родитель, элемент становится корневым.
- **/addElement <родительский элемент> <дочерний элемент>** — добавляет дочерний элемент к указанному родителю. Если родительский элемент не найден, выводится соответствующее сообщение.
- **/removeElement <название элемента>** — удаляет указанный элемент и все его дочерние элементы. Если элемент не найден, выводится соответствующее сообщение.
- **/help** — отображает список доступных команд с их кратким описанием.
- **/download** — скачивает Excel-документ с деревом категорий.
- **/upload** — принимает Excel-документ с деревом категорий и сохраняет все элементы в базе данных.

## Запуск проекта

### 1. Сборка проекта

Для сборки проекта выполните команду:

```bash
./mvnw clean package -DskipTests
```

### 2. Запуск с помощью Docker Compose

```bash
docker-compose up --build
```
## Примечания

- Убедитесь, что у вас установлен Docker и Docker Compose.
- Для работы с PostgreSQL необходимо настроить подключение к базе данных в нужных полях.
- Для работы с Telegram-ботом необходимо указать имя бота и токен, которые можно получить у BotFather

