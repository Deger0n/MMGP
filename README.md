# MMGP
/mmg - command to start

Этот плагин перенесёт мини-игры из игры Машинариум на ваш майнкрафт сервер.
Пока что это самая первая версия плагина, позже будут обновления и добавления различных режимов.

На данный момент я перенёс лишь мини-игру из ботанического сада, все уровни из игры есть в плагине, также я добавил 3 авторских уровня.

## [Glasshouse Control Box Puzzle](https://machinarium.fandom.com/wiki/Glasshouse_Control_Box_Puzzle)
### [gameplay in Machinarium](https://youtu.be/MPcC6KyuT4U)
![image](https://user-images.githubusercontent.com/90723848/234963611-31c3a9b1-de6b-4971-9b50-4cbe60635fb2.png)
### [gameplay in Minecraft](https://youtu.be/82qq16oVpa8)
![image](https://user-images.githubusercontent.com/90723848/234976123-855aa132-1806-4198-971a-7715e22a4316.png)

## 🔧 Настройка
При включении плагина, в папке сервера где он лежит, сощдаётся папка плагина. В ней вы можете найти файл config.yml, в котлром есть все необходимые настройки плагина.

Редактируя конфиг можно:
* Добавлять/изменять уровни;
-Для этого нужно найти секцию levels, найти нужный лвл и изменить его. Вы можете редактировать: расположение препятствий, название уровня, а также материал предмета, который будет отображаться в меню.

Если вы хотите добавить уровень, то нужно создать новую секцию в секции levels с теми же полями, что имею другие уровни. Название секции - порядковый номер уровня. После добавления уровня, добавьте слот в items_menu.level_slots, чтобы меню смогло отобразить новый уровень.

* Редактировать дизайн.
