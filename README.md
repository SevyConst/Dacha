На данный момент основное назначение программы - уведомлять об отключении/восстановлении электричества на даче.

##### Проект состоит из двух частей:

* Программа [`Client`](https://github.com/SevyConst/Dacha/tree/master/Client), установленная на даче на raspberry pi
* Программа [`Server`](https://github.com/SevyConst/Dacha/tree/master/Server), установленая на облаке.

Клиент периодически пингует сервер. Если пинг прерывается - значит отключили электричество. Если пинг возобновился - посылается уведомление, что электричество восстановлено.

Уведомления присылаются через Telegram.

Проект имеет некую ценность, то есть такие уведомления мне действительно нужны. Но также данный проект делается, чтобы я лучше научился программировать на джаве: разобрался с сокетами, xml, логами, особенностями исключений в джаве, и т. д.

##### Дальнейшие планы:
 
* подключить малинку к умной розетке, чтобы розетка по команде от rasperry pi могла перегрузить роутер, если на малинке нет ответа на пинг от облака.
* подключить к rasperry pi симкарту, чтобы было запасное интернет соединение, в случае отключения электричества
* добавить уведомления с помощью facebook messenger