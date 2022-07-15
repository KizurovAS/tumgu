require: slotfilling/slotFilling.sc
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
                imageUrl: "https://clck.ru/sJ2bT",
                text: "махалка рукой"
                })
        
        # выводит приветствие и отправяет в стейт помощи
        random:
            a: Привет. 
            a: Здравствую.
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