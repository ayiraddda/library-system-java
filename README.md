# library-system-java
Console based library system
# Library System — Java

Консольная система управления библиотекой: книги, читатели, выдача и возврат.  
Язык: **Java (Core)** | Интерфейс: **CLI** | Данные: **in-memory (List + HashMap)**

---

## Структура проекта

```
src/
├── model/
│   ├── Book.java           # Книга (id, title, author, year, available)
│   ├── Reader.java         # Читатель (id, name, phone)
│   └── BorrowRecord.java   # Запись о выдаче книги
├── service/
│   └── LibraryService.java # Вся бизнес-логика и аналитика
└── main/
    └── Main.java           # Консольное меню (точка входа)
```

---

## Как запустить

```bash
cd src
javac model/Book.java model/Reader.java model/BorrowRecord.java service/LibraryService.java main/Main.java
java main.Main
```

### Через IntelliJ IDEA
1. File → Open → папка `library-system`
2. Правой кнопкой на `src` → Mark Directory as → Sources Root
3. Открыть `main/Main.java` → Run

---

## Возможности

| Пункт меню | Описание |
|---|---|
| 1. Add book | Добавить книгу в библиотеку |
| 2. Add reader | Зарегистрировать читателя |
| 3. Borrow book | Выдать книгу читателю |
| 4. Return book | Принять возврат книги |
| 5. Reader's books | Книги на руках у читателя |
| 6. Search book | Поиск по названию или автору |
| 7. All available books | Все доступные книги |
| 8. Analytics | Аналитика (см. ниже) |
| 0. Exit | Выход |

### Аналитика (пункт 8)

| Пункт | Описание |
|---|---|
| 1. Readers with books | Читатели у которых сейчас есть книги |
| 2. Top-3 readers | Топ-3 читателя по числу взятых книг за всё время |

---

## Валидация

- Нельзя выдать книгу если `available = false`
- При возврате `available` устанавливается в `true`, фиксируется `returnDate`
- Нельзя вернуть книгу если нет активной записи о выдаче
- Несуществующий ID книги или читателя — сообщение, не падает
- Дата выдачи и возврата фиксируется автоматически через `LocalDate.now()`
