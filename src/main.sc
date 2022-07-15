require: slotfilling/slotFilling.sc
  module = sys.zb-common

require: localPatterns.sc

theme: /

    state: Welcome
        q!: $regex</start>
        q!: *start
        q!: $hi
        random:
            a: Привет. 
            a: Здравствую.
        go!: /SuggestHelp   

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

    state: AskPhone || modal=true
        a: Для продолжения нужен номер телефона.
        buttons:
            "Отмена"