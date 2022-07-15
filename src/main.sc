require: slotfilling/slotFilling.sc
  module = sys.zb-common
  
require: phoneNumber/phoneNumber.sc
  module = sys.zb-common

require: localPatterns.sc

theme: /

    state: Welcome
        q!: $regex</start>
        q!: *start
        q!: $hi
        # выводим картинку, для этого включаем тег js
        script:
        # если респонс реплайс отсутствует, то создаем новый массив
            $response.replies = $response.replies || [];
        # добавляем 
            $response.replies.push({
                type: "image",
                imageUrl: "https://tsvetyzhizni.ru/wp-content/uploads/2011/11/C%D0%BC%D0%B0%D0%B9%D0%BB%D0%B8%D0%BA_%D0%BF%D1%80%D0%B8%D0%B2%D0%B5%D1%82%D1%81%D1%82%D0%B2%D0%B8%D1%8F.jpg",
                //text: "махалка рукой"
                });
        
        # выводит приветствие и отправяет в стейт помощи
        random:
            a: Привет. 
            a: Здравствую.
        a: Меня зовут {{$injector.botName}}
        go!: /SuggestHelp   

# переходим в другой стейт при этом сохраняем контекст
    state: CatchAll || noContext = true
        event!: noMatch
        a: Я не понял. Перефразируйте.
    
    state: SuggestHelp
        q: отмена || fromState = /AskPhone
        a: Я помогу Вам купить билет на самолет. ОК?
        buttons:
            "Да" -> /SuggestHelp/Accepted
            "Нет"
            
        state: Accepted
            q: * (да/давай*/хорошо) *
            a: Отлично!
            if: $client.phone
                go!: /Confirm
            else:
                go!: /AskPhone
            buttons:
                "В начало" -> /Welcome
        
        state: Rejected
            q: * (нет/не) *
            a: Боюсь, что на этом мои полномочия как бы все)

    state: AskPhone
        # || modal=true
        a: Для продолжения нужен номер телефона.
        buttons:
            "Отмена"
        # будем ловить номер телефона
        state: GetPhone
            q: * $mobilePhoneNumber *
            # распарсим второй строкой и запилим переменную в темп первой строкой, смотреть лог в http://json.parser.online.fr/
            script:
                $temp.phone = $parseTree._mobilePhoneNumber
                # распарсим лог js
                # log("@@@@@@ " + toPrettyString($parseTree));
            go!: /Confirm
        
        # если номерр введен не корректно
        state: localCatchAll
            event: noMatch
            a: Не похоже на номер телефона
            go!: ..
            
    state: Confirm
        script:
        # проверяем нет ли сохраненного номера телефона
            $temp.phone = $temp.phone || $client.phone;
        a: Ваш номер - {{$temp.phone}}, верно?
        # после этого запроса темп с номером телефона будет уничтожаться
        script:
            $session.probablyPhone=$temp.phone;
        buttons:
            "Да"
            "Нет"
            
        state: Agree
            q: (да/верно)
            script:
                $client.phone=$session.probablyPhone;
                delete $session.probablyPhone
            a: Отлично
                
        state: Disagree
            q: (нет/неверно)
            go!: /AskPhone