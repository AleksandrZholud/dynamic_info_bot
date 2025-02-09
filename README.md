### Резюме по проекту с MongoDB

#### Структура базы данных:

**//docker run --name mongodb -d -p 27017:27017 mongo

1.  **Коллекция `messages`** (сообщения)

    -   **`_id`** (string): уникальный идентификатор сообщения.
    -   **`type`** (string): тип сообщения (например, текст, изображение и т. д.).
    -   **`text`** (string): текст сообщения.
    -   **`buttons`** (array): массив объектов, представляющих кнопки. Каждая кнопка содержит:
        -   **`button_text`** (string): текст кнопки.
        -   **`button_id`** (string): уникальный идентификатор кнопки.
        -   **`button_type`** (string): тип кнопки (например, переход на новое сообщение или URL).
    -   **`auto_delete`** (boolean): флаг, указывающий, должно ли сообщение автоматически удаляться при переходе (если кнопка ведет к новому сообщению).
2.  **Коллекция `users`** (пользователи)

    -   **`_id`** (string): уникальный идентификатор пользователя.
    -   **`chat_id`** (string): ID чата в Telegram, уникальный для каждого пользователя.
    -   **`history_ids`** (array): список `_id` сообщений, которые находятся в истории пользователя (ограничение до 30 записей).
    -   **`is_superuser`** (boolean): флаг, который определяет, является ли пользователь администратором бота.

#### Алгоритм работы с ботом (для пользователей и администратора):

1.  **Основные принципы работы с ботом:**

    -   Пользователь не вводит данные вручную, все взаимодействия происходят через кнопки.
    -   Для навигации по сообщениями доступны всегда кнопки "Начало" и "Назад", которые обеспечивают переходы между сообщениями.
    -   Когда пользователь нажимает кнопку с переходом на новое сообщение, старое сообщение удаляется (если флаг `auto_delete` установлен на `true`).
    -   Если кнопка ведет по URL, сообщение не удаляется.
2.  **Администратор (режим админа):**

    -   **Активирует режим добавления кнопок через команду `/admin`.**
    -   **Добавление кнопки к сообщению:**
        -   Администратор вводит текст кнопки и указывает, что она будет делать (например, переход на новое сообщение или переход по URL).
        -   Если кнопка ведет на новое сообщение, администратор сразу же задаёт текст для нового сообщения и добавляет другие кнопки, если нужно.
        -   Администратор может привязать кнопку к текущему сообщению, которое будет отображаться пользователю.
        -   **При завершении ввода кнопок администратор использует команду `/stop`, чтобы завершить процесс добавления кнопок.**
3.  **Наполнение базы через админ-функции:**

    -   **Добавление сообщения:**
        -   Администратор вводит текст нового сообщения, который будет отправлен пользователю.
        -   После создания сообщения администратор может добавить к нему кнопки с указанием их действий (переход на другое сообщение или URL).
        -   Каждое новое сообщение или кнопка сразу записывается в базу данных.
    -   **Добавление кнопок:**
        -   К каждому сообщению можно добавить одну или несколько кнопок, привязанных к текущему сообщению.
        -   Каждая кнопка будет иметь свой текст, идентификатор и действие (переход на новое сообщение или URL).
    -   **Завершение админ-режима:**
        -   После добавления всех кнопок администратор завершает настройку с помощью команды `/stop`.
        -   Система записывает в базу все изменения (новые сообщения и кнопки).
4.  **Работа с историей пользователя:**

    -   Каждый пользователь имеет ограниченную историю сообщений, которая не должна превышать 30 записей.
    -   Когда пользователь переходит на новое сообщение, оно добавляется в историю, и если история переполняется, старое сообщение удаляется.
    -   История позволяет администратору и пользователю возвращаться к предыдущим сообщениям, не создавая новые запросы.

#### Структура для наполнения базы данными:

Для того чтобы удобнее работать с наполнением базы через админ-функции бота, предлагаю следующую структуру для бота:

1.  **Администрирование сообщений и кнопок:**

    -   Администратор может ввести команду `/admin` для перехода в режим добавления сообщений и кнопок.
    -   После активации `/admin` бот ожидает текст сообщения, который нужно сохранить в базе.
    -   Когда сообщение создано, бот предлагает администратору добавить кнопки (с текстом кнопки и указанием действия --- переход на другое сообщение или URL).
    -   Кнопки добавляются поочередно, с возможностью указать переход на новое сообщение.
2.  **Добавление данных в коллекции:**

    -   При создании нового сообщения с кнопками все данные о сообщении, кнопках и их действиях записываются в коллекцию `messages`.
    -   При добавлении нового пользователя или изменения информации о существующем пользователе (например, создание новой истории), обновляется коллекция `users`.
3.  **Автоматическая запись в базу данных:**

    -   Все изменения (новые сообщения, добавление кнопок, изменение истории пользователя) записываются в базу данных сразу после завершения их ввода.
    -   Если кнопка ведет на новое сообщение, система создает новое сообщение, привязывает его к кнопке и записывает это в базу данных.

---

# ТЗ:

**Техническое задание для разработки Telegram-бота с использованием MongoDB**

### 1\. **Цели и задачи:**

-   Разработать Telegram-бота, который будет управлять сообщениями и кнопками, связанными с ними, с возможностью администрирования.
-   Бот должен хранить информацию в базе данных MongoDB, включая сообщения, кнопки и историю взаимодействий с пользователями.
-   Все данные должны храниться в базе, и администратор должен иметь возможность изменять структуру данных через командный интерфейс.

### 2\. **Структура базы данных (MongoDB):**

-   **Коллекция `messages`**: хранит сообщения с кнопками и их действиями.
    -   **`_id`** (string) --- уникальный идентификатор сообщения.
    -   **`type`** (string) --- тип сообщения (например, текстовое, изображение и т. д.).
    -   **`text`** (string) --- текст сообщения.
    -   **`buttons`** (array) --- список кнопок с текстами и действиями (переход на новое сообщение или URL).
    -   **`auto_delete`** (boolean) --- флаг для автоматического удаления сообщения, если оно ведет к новому.
-   **Коллекция `users`**: хранит информацию о пользователях.
    -   **`_id`** (string) --- уникальный идентификатор пользователя.
    -   **`chat_id`** (string) --- ID чата Telegram.
    -   **`history_ids`** (array) --- список `_id` сообщений в истории пользователя (максимум 30).
    -   **`is_superuser`** (boolean) --- флаг для администраторов бота.

### 3\. **Функциональные требования:**

#### Для пользователей:

-   **Навигация через кнопки**: всегда доступны кнопки "Начало" и "Назад".
-   **Переход на новое сообщение**: при нажатии на кнопку, если это переход на новое сообщение, старое сообщение удаляется (если `auto_delete` установлено на true).
-   **Переход по URL**: если кнопка ведет на URL, старое сообщение не удаляется.

#### Для администратора:

-   **Команды администратора**:
    -   `/admin` --- активирует режим добавления кнопок к сообщениям.
    -   **Добавление сообщений**: администратор вводит текст сообщения.
    -   **Добавление кнопок**: к сообщению добавляются кнопки с текстом и указанием действия (переход на новое сообщение или URL).
    -   **Завершение добавления**: команда `/stop` завершает добавление сообщений и кнопок.
-   **Запись данных**: все добавления и изменения записываются в базу данных MongoDB сразу после ввода.

### 4\. **История пользователей**:

-   История сообщений сохраняется для каждого пользователя, ограничение на 30 записей.
-   Когда пользователь переходит на новое сообщение, оно добавляется в историю, и если история переполняется, удаляется старое сообщение.

### 5\. **Как будет наполняться база данных через админ-функции**:

-   **Режим добавления сообщений и кнопок**:
    -   После активации `/admin` бот ожидает от администратора ввод текста нового сообщения.
    -   Далее, после создания сообщения, администратор может добавить кнопки. Для каждой кнопки необходимо указать:
        -   Текст кнопки.
        -   Действие кнопки (переход на новое сообщение или URL).
    -   Все кнопки привязываются к текущему сообщению.
    -   После завершения процесса администратор завершает команду `/stop`, и все данные записываются в базу.

* * * * *

### 6\. **Миграции и структуры данных:**

-   Структура данных будет изменяться редко. В случае необходимости изменений в структуре, коллекции MongoDB обеспечивают гибкость (например, добавление новых полей в документы).
-   Миграции не требуются, так как MongoDB позволяет динамически обновлять схему без необходимости использования сложных миграционных скриптов.

* * * * *

### 7\. **Технические детали**:

-   Используем MongoDB для хранения данных, так как она хорошо справляется с высокими требованиями к чтению и масштабированию.
-   Работа с базой данных будет происходить через MongoDB драйвер для Java (или аналогичный для вашего стека).

* * * * *

### Заключение:

Мне нужно создать Telegram-бота с управлением сообщениями и кнопками через MongoDB. Администратор должен иметь возможность добавлять сообщения и кнопки, а также управлять историей пользователей. Все данные должны записываться в MongoDB.



