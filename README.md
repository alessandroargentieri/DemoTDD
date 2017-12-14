# DemoTDD
A Test Driven Development example: a web app with Spring Boot and H2 database developed using TDD

1. Analizzare il dominio di applicazione e il problema da risolvere.
2. Progettare bene la struttura dei componenti e le relazioni tra le stesse.
3. Creare le entità basilari (POJO) e le Utility che non vanno testate (Encryption, Reflection ecc.)
4. Fare i test sulla struttura: quali componenti devono essere iniettati/utilizzati in quali altri componenti, quali gli output
5. Fare i test dagli strati piu' esterni ai piu' interni:
    a. prima il Filtro
    b. poi il Controller
    c. poi i ServiziImplementati
    d. poi i Dao implementati.
    
    TENERE PRESENTE CHE:  
      A. quando si testa uno strato si mockano tutti gli strati inferiori
      B. quando si testa uno strato bisogna:
            a. scrivere il test
            b. eliminare gli errori di compilazione modificando/integrando la dipendenza da mockare in modo da eliminare l'errore e poter fare il run del test
            c. ora che si puo' fare il run fare il run e farlo fallire
            d. modificare il SUT (system under test) per far passare il test
            e. scrivere il test successivo fino a che il componente sia stato completamente definito
            f. fare eventuali refactor e ritestare tutta la classe di test del componente
      C. quando si completano i test di uno strato (componente), si completa automaticamente la scrittura del componente stesso e di TUTTE LE INTERFACCE delle dipendenze da ESSO UTILIZZATE.
      D. terminata la creazione di tutti i casi di test di un componente passare al componente dello stesso strato successivo o allo strato successivo (CLASSI IMPLEMENTATE)
      E. i Dao di Spring che usano JpaRepository o CrudRepository non vanno testati perchè garantiti da Spring.
      F. Non vanno testati i POJO.
