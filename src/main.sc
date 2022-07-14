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
    
        
    state: CatchAll
        event!: noMatch
        a: Я не понял. Перефразируйте.
    
    state: SuggestHelp
         a: Я помогу Вам купить билет на самолет. ОК?
         
        state: Accepted
           q: * (да/давай*/хорошо) *
            a: Отлично!
        
        state: Rejected
            q: * (нет/не) *
            a: Боюсь, что на этом мои полномочия как бы все)

    # state: Hello
    #     intent!: /привет
    #     a: Привет привет

    # state: Bye
    #     intent!: /пока
    #     a: Пока пока

    # state: NoMatch
    #     event!: noMatch
    #     a: Я не понял. Вы сказали: {{$request.query}}

    # state: Match
    #     event!: match
    #     a: {{$context.intent.answer}}