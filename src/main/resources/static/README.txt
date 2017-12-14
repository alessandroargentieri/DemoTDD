
1. progettare il sistema

    LoginFilter                  (filtra le richieste tranne quella di login. verifica il JWT e salva i dati nella request)
        -> JwtUtils              (gestisce i Jwt)

    RestController               (punto di accesso dell'utente)
        |-> LoginService         (servizio per il Login)
        |    |-> JwtUtils        (gestisce i Jwt)
        |    |-> EncryptionUtils (gestisce il criptaggio e decriptaggio delle pwd)
        |    |-> UserDao         (repo per accesso al Database)
        |          -> User       (entità che mappa la tabella "users" nel H2 database)
        |                users: STRING EMAIL, STRING NAME, STRING PASSWORD
        |
        |-> ToDoService          (servizio di gestione dei ToDos)
              -> ToDoDao         (repo per l'accesso al database)
                   -> ToDo       (entita' che mappa la tabella "todos" nel H2 database)
                        todos: INT ID, STRING DESCRIPTION, STRING PRIORITY, DATE DATE.



    Test sulla struttura:

    //test che usano il file di configurazione di Spring che servono a costruire la struttura interna delle classi

    test che il loginFilter contenga un'istanza di JwtUtils
    test che il controller restituisca una ResponseEntity<JsonResponseBody> in tutti i suoi metodi (punti di accesso)
    test che il controller contenga il loginService e toDoService
    test che il loginService contenga un'istanza di JwtUtils, di UserDao e di EncryptionUtils.
    test che il toDoService contenga l'istanza di toDoDao
    test che l'Application contenga le istanze di toDoDao, userDao ed encryptionUtils per riempire il DB



    //LoginFilter test: (Unit Test del filtro)
    //https://stackoverflow.com/questions/11451917/how-do-i-unit-test-a-servlet-filter-with-junit

    l'utente chiede di accedere a signin.html (risorsa statica) e la richiesta passa
    l'utente chiede di accedere al path /login e la richiesta passa
    l'utente chiede di accedere al path /loginError e la richiesta passa
    l'utente chiede altro e viene analizzato il jwt che è null, quindi inoltra la stessa request a /loginError
    l'utente chiede altro e viene analizzato il jwt che è invalido: inoltra la stessa request a /loginError
    l'utente chiede altro e viene analizzato il jwt che è valido: salva i dati recuperati nella request e la richiesta passa


    // Restcontroller test

    l'utente invia email e password al controller per l'autenticazione e ottiene risposta affermativa e il JWT nell'header (mock Service)
    l'utente invia email e password al controller per l'autenticazione e ottiene risposta negativa perche' non trovato perche' l'email e' sbagliata (mock Service)
    l'utente invia email e password al controller per l'autenticazione e ottiene risposta negativa perche' non trovato perche' la pwd e' sbagliata (mock Service)

    il filtro fa il forward della request a /loginError perchè ha trovato errori di autenticazione

    l'utente chiede di vedere i suoi ToDos e li ottiene (mock toDoService)
    l'utente chiede di vedere i suoi ToDos ma qualcosa va storto e ottiene il messaggio di errore

    l'utente chiede di salvare un ToDos e ci riesce (mock toDoService)
    l'utente chiede di salvare un ToDos e non ci riesce perche' non e' valido per JSR-303 (mock toDoService) e da l'errore in output
    l'utente chiede di salvare un ToDos e non ci riesce perche' non e' valido per Spring Validator (mock toDoService)
    l'utente chiede si salvare un ToDos ma qualcosa va storto nel salvataggio e viene lanciata un'Eccezione

    l'utente chiede di cancellare un ToDos(mock toDoService)
    l'utente chiede di cancellare un ToDos(mock toDoService) ma qualcosa va storto



    IN QUESTA FASE E' COMPLETO LO STRATO PIU' ESTERNO DELL'APPLICAZIONE, OVVERO QUELLO CHE E' A DIRETTO CONTATTO CON
    LA RICHIESTA DELL'UTENTE.
    SONO COMPLETI IL FILTRO, IL CONTROLLER, LE INTERFACCE DEI SERVIZI.
    NON SONO COMPLETI I SERVIZI IMPLEMENTATI.

    ADESSO PASSIAMO ALLE CASISTICHE DI SECONDO LIVELLO CON CUI SI COMPLETANO I SERVIZI IMPLEMENTATI E I DAO

    // casistiche LoginServiceImpl

    il service riceve username e password ma l'utente non esiste, quindi restituisce un jwt nullo
    il service riceve username e password e l'utente esiste ma la password e' sbagliata, quindi restituisce un jwt nullo
    il service riceve username e password e l'utente esiste, quindi restituisce il jwt


    //casistiche ToDoServiceImpl

    al service viene chiesta la lista dei ToDos
    al service viene chiesto di salvare un ToDos
    al service viene chiesto di eliminare un ToDos

   1) create classes defined in the structure schema in project phase
   2) create unit test about the structure that must fail
   3) adjust tests @Autowiring one class into the other according to the schema

   ------------










BASE OF CONTROLLER BEFORE TESTS:
@org.springframework.web.bind.annotation.RestController
public class RestController {

    @RequestMapping("/login")
    public String login(){
        return null;
    }

    @RequestMapping("/showToDos")
    public String showToDos(){
        return null;
    }

    @RequestMapping("/newToDo")
    public String newToDo(ToDo toDo){
        return null;
    }

    @RequestMapping("/deleteToDo/{id}")
    public String deleteToDo(){
        return null;
    }
}






------------


@org.springframework.web.bind.annotation.RestController
public class RestController {

    @RequestMapping("/login")
    public ResponseEntity<JsonResponseBody> login(){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new JsonResponseBody(HttpStatus.BAD_REQUEST.value(), null));
    }

    @RequestMapping("/showToDos")
    public ResponseEntity<JsonResponseBody>  showToDos(){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new JsonResponseBody(HttpStatus.BAD_REQUEST.value(), null));
    }

    @RequestMapping("/newToDo")
    public ResponseEntity<JsonResponseBody>  newToDo(ToDo toDo){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new JsonResponseBody(HttpStatus.BAD_REQUEST.value(), null));
    }

    @RequestMapping("/deleteToDo/{id}")
    public ResponseEntity<JsonResponseBody> deleteToDo(){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new JsonResponseBody(HttpStatus.BAD_REQUEST.value(), null));
    }
}



------------------


@Test
    public void ReturnsResponseEntityOfJsonResponseBody(){

        assertThat(restController.login().getBody(), instanceOf(JsonResponseBody.class));
        assertThat(restController.showToDos().getBody(), instanceOf(JsonResponseBody.class));
        assertThat(restController.newToDo(new ToDo()).getBody(), instanceOf(JsonResponseBody.class));
        assertThat(restController.deleteToDo().getBody(), instanceOf(JsonResponseBody.class));
    }


-------------------
