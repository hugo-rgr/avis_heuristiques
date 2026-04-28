# Architecture du projet — avis_heuristiques

> Plateforme de notation de jeux vidéo  
> Backend : Java 25 · Spring Boot 4.0.5 · Architecture hexagonale

---

## Table des matières

1. [Choix de l'architecture](#1-choix-de-larchitecture)
2. [Principes SOLID et DRY](#2-principes-solid-et-dry)
3. [Robustesse et évolutivité](#3-robustesse-et-évolutivité)
4. [Sécurité](#4-sécurité)
5. [Tests par couche](#5-tests-par-couche)
6. [Outils et techniques de modélisation](#6-outils-et-techniques-de-modélisation)
7. [Mise en application](#7-mise-en-application)

---

## 1. Choix de l'architecture

### 1.1 Architecture hexagonale (Ports & Adapters)

Le backend est structuré selon l'**architecture hexagonale**, proposée par Alistair Cockburn. Son principe fondateur est que le **domaine métier ne dépend d'aucune technologie**. C'est l'infrastructure (Spring, JPA, JWT) qui dépend du domaine, et jamais l'inverse.

L'application est organisée en trois cercles concentriques :

```
┌────────────────────────────────────────────────────────────┐
│                       INFRASTRUCTURE                       │
│                                                            │
│    Controllers REST              Adapters JPA / JWT        │
│    (adapters entrants)           (adapters sortants)       │
│           │                             ▲                  │
│           ▼                             │                  │
│    ┌──────────────────────────────────────────────┐        │
│    │               PORTS (interfaces)             │        │
│    │   Ports IN (UseCase)    Ports OUT (Port)     │        │
│    │           │                   ▲              │        │
│    │           ▼                   │              │        │
│    │    ┌────────────────────────────────┐        │        │
│    │    │         DOMAINE MÉTIER         │        │        │
│    │    │  Jeu · Avis · Joueur · Avatar  │        │        │
│    │    │  Services · Exceptions         │        │        │
│    │    └────────────────────────────────┘        │        │
│    └──────────────────────────────────────────────┘        │
└────────────────────────────────────────────────────────────┘
```

### 1.2 Structure des packages

```
fr.esgi.avis/
├── business/       ← Objets du domaine pur (Jeu, Avis, Joueur, Moderateur…)
├── port/
│   ├── in/         ← Ports entrants : contrats des use cases (interfaces)
│   └── out/        ← Ports sortants : contrats d'infrastructure (interfaces)
├── service/        ← Logique métier — implémente les ports entrants
├── dto/
│   ├── in/         ← Données reçues — validées par Bean Validation
│   └── out/        ← Données renvoyées au client
├── mapper/         ← Conversion domaine ↔ DTO (MapStruct, généré à la compilation)
├── controller/     ← Adapters entrants HTTP (Spring MVC)
├── infrastructure/
│   ├── entity/     ← Entités JPA (mapping base de données)
│   ├── repository/ ← Adapters sortants JPA — implémentent les ports out
│   └── security/   ← Filtre JWT, SecurityConfig
└── exception/      ← Hiérarchie d'exceptions métier typées
```

### 1.3 L'inversion de dépendances en pratique

Sans architecture hexagonale, les couches se couplent directement :

```java
// ❌ Sans inversion — le controller connaît le service concret
public class JeuController {
    private final JeuService jeuService; // classe concrète
}

// ❌ Le service connaît l'adapter JPA concret
public class JeuService {
    private final JeuAdapter jeuAdapter; // classe concrète
}
```

Le problème : changer l'implémentation de la persistence impose de modifier le service.

Dans ce projet, toutes les dépendances pointent vers des **interfaces** :

```java
// ✅ Le controller dépend d'une interface (port entrant)
public class JeuController {
    private final JeuUseCase jeuUseCase; // interface
}

// ✅ Le service dépend d'interfaces (ports sortants)
public class JeuService implements JeuUseCase {
    private final JeuPort jeuPort;             // interface
    private final GenrePort genrePort;         // interface
    private final EditeurPort editeurPort;     // interface
    private final ClassificationPort classificationPort; // interface
    private final PlateformePort plateformePort;         // interface
    private final AvisPort avisPort;           // interface
}

// ✅ L'adapter implémente le port imposé par le domaine
@Repository
public class JeuAdapter implements JeuPort {
    // JPA est confiné ici — le domaine n'en sait rien
}
```

Spring injecte à l'exécution les beans concrets (`jeuAdapter`, `genreAdapter`…) là où le code déclare des interfaces. Le domaine ne sait jamais quelle implémentation tourne derrière.

### 1.4 Bénéfices concrets

| Bénéfice | Concrètement dans ce projet |
|---|---|
| **Testabilité** | `JeuService` est testé avec des mocks Mockito purs, sans démarrer Spring ni une base de données |
| **Interchangeabilité** | Passer de H2 (dev) à PostgreSQL (prod) ne modifie aucun service — seul le profil Spring change |
| **Lisibilité** | Chaque couche a une responsabilité unique et un périmètre clair |
| **Évolutivité** | Ajouter un adapter GraphQL ou un consumer Kafka ne touche pas aux services métier |
| **Isolation** | Un bug dans la couche JPA n'affecte pas la logique métier — le domaine reste intact |

---

## 2. Principes SOLID et DRY

### S — Single Responsibility Principle

Chaque classe a une seule raison de changer :

- `JeuController` : recevoir les requêtes HTTP et déléguer — rien d'autre
- `JeuService` : orchestrer la logique de gestion des jeux — rien d'autre
- `JeuAdapter` : traduire entre le domaine et JPA — rien d'autre
- `JeuMapper` : convertir `JeuDtoIn` ↔ `Jeu` ↔ `JeuDtoOut` — rien d'autre
- `GlobalExceptionHandler` : centraliser la gestion des erreurs HTTP — rien d'autre

### O — Open/Closed Principle

Les services sont ouverts à l'extension, fermés à la modification.

Pour ajouter un nouveau mode de persistence (ex: MongoDB), on crée un nouvel adapter qui implémente `JeuPort` — aucune ligne du service n'est touchée.

```java
// Nouvel adapter MongoDB — sans modifier JeuService
@Repository
public class JeuMongoAdapter implements JeuPort {
    // implémentation MongoDB
}
```

### L — Liskov Substitution Principle

Tous les adapters (`JeuAdapter`, `GenreAdapter`…) sont interchangeables avec leurs ports respectifs. Spring peut injecter n'importe quelle implémentation de `JeuPort` dans `JeuService` sans changer son comportement observable — c'est précisément ce qui permet de passer de H2 à PostgreSQL.

### I — Interface Segregation Principle

Les ports sont finement découpés : `JeuPort`, `GenrePort`, `EditeurPort`, `AvisPort`, `TokenPort`… Chaque service ne déclare que les ports dont il a réellement besoin, sans interface fourre-tout.

```java
// JeuService ne dépend pas de JoueurPort — il n'en a pas besoin
// ModerateurService ne dépend pas de GenrePort — il n'en a pas besoin
```

### D — Dependency Inversion Principle

C'est le principe central de l'architecture hexagonale, déjà détaillé en section 1.3. Le domaine impose ses interfaces, l'infrastructure s'y conforme.

Le `JwtFilter` illustre également ce principe : il dépend de `TokenPort` (interface), pas de `JwtTokenAdapter` (implémentation) :

```java
@Component
public class JwtFilter extends OncePerRequestFilter {

    private final TokenPort tokenPort; // interface — pas JwtTokenAdapter

    public JwtFilter(TokenPort tokenPort) {
        this.tokenPort = tokenPort;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, ...) {
        String token = authHeader.substring(7);
        if (tokenPort.validateToken(token)) {
            String email = tokenPort.extractEmail(token);
            String role = tokenPort.extractRole(token);
            // construction du SecurityContext
        }
    }
}
```

### DRY — Don't Repeat Yourself

**Hiérarchie d'exceptions** — au lieu de répéter `throw new RuntimeException("X non trouvé")` dans chaque service, une classe centrale `ResourceNotFoundException` est réutilisée partout :

```java
// Un seul endroit pour le message et le code HTTP
public class ResourceNotFoundException extends AvisException {
    public ResourceNotFoundException(String type, Object id) {
        super(type + " non trouvé(e) avec l'identifiant : " + id, 404);
    }
}

// Utilisée partout de la même façon
.orElseThrow(() -> new ResourceNotFoundException("Jeu", id))
.orElseThrow(() -> new ResourceNotFoundException("Joueur", id))
.orElseThrow(() -> new ResourceNotFoundException("Avis", id))
```

**Déduplication de `ModerateurService`** — initialement, `ajouterJeu` dupliquait toute la logique de `JeuService`. Après refactoring, il délègue directement :

```java
@Override
public JeuDtoOut ajouterJeu(Long moderateurId, JeuDtoIn dto) {
    moderateurPort.findById(moderateurId)
            .orElseThrow(() -> new ResourceNotFoundException("Modérateur", moderateurId));
    return jeuUseCase.creerUnJeu(dto); // délègue — zéro duplication
}
```

**`UtilisateurEntity` (`@MappedSuperclass`)** — les champs communs `id`, `pseudo`, `email`, `motDePasse`, `version` sont définis une seule fois et hérités par `JoueurEntity` et `ModerateurEntity`.

---

## 3. Robustesse et évolutivité

### 3.1 Gestion globale des erreurs

Un `@RestControllerAdvice` unique intercepte toutes les exceptions et retourne un JSON normalisé. Le frontend reçoit toujours la même structure, jamais de stacktrace :

```java
@RestControllerAdvice
public class GlobalExceptionHandler {

    record ErrorResponse(int status, String error, String message, Instant timestamp) {}

    @ExceptionHandler(AvisException.class)
    public ResponseEntity<ErrorResponse> handleAvisException(AvisException ex) {
        // 404, 401, 409 selon le type d'exception
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex) {
        // 422 avec le détail des champs invalides
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(Exception ex) {
        // 500 sans détail technique exposé
    }
}
```

La hiérarchie d'exceptions permet d'ajouter de nouveaux cas sans modifier le handler :

```
AvisException (abstraite, httpStatus)
├── ResourceNotFoundException  → 404
├── DuplicateEmailException    → 409
├── BadCredentialsException    → 401
└── UserNotFoundException      → 401
```

### 3.2 Validation des entrées

Chaque DTO d'entrée est un `record` Java immuable annoté avec Bean Validation. Le code métier ne reçoit jamais de données invalides :

```java
public record JoueurDtoIn(
    @NotBlank(message = "Le pseudo est obligatoire")
    @Size(min = 3, max = 30, message = "Le pseudo doit contenir entre 3 et 30 caractères")
    String pseudo,

    @NotBlank @Email(message = "L'email n'est pas valide")
    String email,

    @NotBlank @Size(min = 8, message = "Le mot de passe doit contenir au moins 8 caractères")
    String motDePasse,

    @NotNull @Past(message = "La date de naissance doit être dans le passé")
    LocalDate dateDeNaissance
) implements Serializable {}
```

Les controllers déclenchent la validation avec `@Valid` :

```java
@PostMapping("/inscription")
public ResponseEntity<JoueurDtoOut> sInscrire(@Valid @RequestBody JoueurDtoIn dto) { ... }
```

### 3.3 Optimistic Locking

Toutes les entités JPA portent un champ `@Version`. En cas de modification concurrente, JPA lève automatiquement une `OptimisticLockException` — aucune corruption de données possible :

```java
@MappedSuperclass
public abstract class UtilisateurEntity {
    @Version
    private Long version; // couvre JoueurEntity et ModerateurEntity
}

@Entity
public class JeuEntity {
    @Version
    private Long version;
}
```

### 3.4 Pagination

Les endpoints à fort volume exposent des variantes paginées pour éviter de charger l'intégralité d'une table en mémoire :

```java
// Port — contrat
Page<Jeu> findAll(Pageable pageable);
Page<Jeu> findAllByGenreId(Long genreId, Pageable pageable);

// Controller — endpoint paginé
@GetMapping("/page")
public ResponseEntity<Page<JeuDtoOut>> recupererTousLesJeuxPagine(
        @PageableDefault(size = 20) Pageable pageable) {
    return ResponseEntity.ok(jeuUseCase.recupererTousLesJeux(pageable));
}
```

### 3.5 Profils Spring

Trois environnements isolés sans changer une ligne de code :

| Profil | Base | DDL | Logs SQL |
|---|---|---|---|
| `dev` | H2 in-memory | `create-drop` | verbeux |
| `test` | H2 in-memory isolée | `create-drop` | désactivés |
| `prod` | PostgreSQL | `validate` | désactivés |

---

## 4. Sécurité

### 4.1 Authentification sans état (stateless JWT)

Aucune session HTTP n'est créée. Chaque requête est authentifiée indépendamment via un token JWT signé HMAC-SHA256.

Le token contient trois claims :
- `sub` : l'email de l'utilisateur
- `role` : `JOUEUR` ou `MODERATEUR`
- `id` : l'identifiant en base (permet au frontend d'appeler `/api/joueurs/{id}` sans requête supplémentaire)

```java
@Override
public String generateToken(String email, String role, Long id) {
    return Jwts.builder()
            .subject(email)
            .claim("role", role)
            .claim("id", id)
            .issuedAt(new Date())
            .expiration(new Date(System.currentTimeMillis() + expiration))
            .signWith(Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8)))
            .compact();
}
```

Le secret est externalisé via variable d'environnement — jamais en dur dans le code :

```properties
jwt.secret=${JWT_SECRET:valeurParDefautPourDev}
jwt.expiration=${JWT_EXPIRATION:86400000}
```

### 4.2 Autorisation par couches

**Couche HTTP** — les règles globales sont définies dans `SecurityConfig` :

```java
.authorizeHttpRequests(auth -> auth
    .requestMatchers("/api/auth/**", "/api/joueurs/inscription").permitAll()
    .requestMatchers(GET, "/api/jeux/**", "/api/avis/**", "/api/genres/**",
                     "/api/editeurs/**", "/api/classifications/**",
                     "/api/plateformes/**").permitAll()
    .anyRequest().authenticated()
)
```

**Couche méthode** — `@EnableMethodSecurity` active le contrôle fin par rôle :

```java
// Seuls les modérateurs peuvent créer, modifier ou supprimer un jeu
@PostMapping
@PreAuthorize("hasRole('MODERATEUR')")
public ResponseEntity<JeuDtoOut> creerUnJeu(...) { ... }

// Seuls les joueurs peuvent rédiger un avis
@PostMapping
@PreAuthorize("hasRole('JOUEUR')")
public ResponseEntity<AvisDtoOut> redigerUnAvis(...) { ... }

// Tout le contrôleur modérateur est protégé
@RestController
@PreAuthorize("hasRole('MODERATEUR')")
public class ModerateurController { ... }
```

### 4.3 CORS

La configuration CORS est centralisée dans un bean `CorsConfigurationSource`. Les origines autorisées sont externalisées via variable d'environnement :

```java
@Bean
public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration config = new CorsConfiguration();
    config.setAllowedOriginPatterns(List.of("http://localhost:[*]", allowedOrigins));
    config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
    config.setAllowedHeaders(List.of("*"));
    config.setAllowCredentials(true);
    // ...
}
```

### 4.4 Mots de passe

Les mots de passe ne sont jamais stockés en clair. BCrypt avec facteur de coût par défaut est utilisé :

```java
@Bean
public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
}
```

---

## 5. Tests par couche

La stratégie de test suit la pyramide des tests : beaucoup de tests unitaires, moins de tests d'intégration, un seul test de démarrage complet.

### 5.1 Tests unitaires des services — `@ExtendWith(MockitoExtension.class)`

Les services sont testés en isolation totale. Toutes les dépendances sont mockées — Spring ne démarre pas, aucune base de données n'est sollicitée :

```java
@ExtendWith(MockitoExtension.class)
class JeuServiceTest {

    @Mock private JeuPort jeuPort;
    @Mock private GenrePort genrePort;
    @Mock private EditeurPort editeurPort;
    @Mock private ClassificationPort classificationPort;
    @Mock private PlateformePort plateformePort;
    @Mock private AvisPort avisPort;
    @Mock private JeuMapper jeuMapper;

    @InjectMocks
    private JeuService jeuService;

    @Test
    void creerUnJeu_shouldReturnJeuDtoOut() {
        when(genrePort.findById(1L)).thenReturn(Optional.of(genre));
        when(jeuPort.save(any())).thenReturn(jeu);
        when(avisPort.findNoteMoyenneByJeuId(any())).thenReturn(Optional.empty());
        // ...
    }
}
```

Services testés : `JeuService`, `AvisService`, `AvatarService`, `AuthService`, `JoueurService`, `ModerateurService`, `GenreService`, `EditeurService`, `ClassificationService`, `PlateformeService`.

### 5.2 Tests des controllers — `MockMvc standalone`

Les controllers sont testés via `MockMvc` sans démarrer le contexte Spring complet. Seule la couche HTTP est exercée — les use cases sont mockés :

```java
@ExtendWith(MockitoExtension.class)
class JeuControllerTest {

    private MockMvc mockMvc;

    @Mock private JeuUseCase jeuUseCase;

    @InjectMocks private JeuController jeuController;

    @Test
    void getJeux_shouldReturn200() throws Exception {
        when(jeuUseCase.recupererTousLesJeux()).thenReturn(List.of(jeuDtoOut));

        mockMvc.perform(get("/api/jeux"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$", hasSize(1)));
    }
}
```

Controllers testés : `JeuController`, `AvisController`, `JoueurController`, `ModerateurController`, `AvatarController`, `GenreController`, `EditeurController`, `ClassificationController`, `PlateformeController`, `AuthController`.

### 5.3 Tests des adapters JPA — `@DataJpaTest`

Les adapters sont testés avec une vraie base H2 in-memory, sans démarrer le contexte Spring Security ou les controllers :

```java
@DataJpaTest
@Import(JeuAdapter.class)
class JeuAdapterJpaTest {

    @Autowired private JeuAdapter jeuAdapter;
    @Autowired private GenreJpaRepository genreJpaRepository;

    @Test
    void saveAndFindById_withRelations() {
        GenreEntity genre = genreJpaRepository.save(entityGenre("Action"));

        Jeu jeu = new Jeu(null, "Assassin", "desc",
                new Genre(genre.getId(), null, List.of()), ...);

        Jeu saved = jeuAdapter.save(jeu);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getGenre().getId()).isEqualTo(genre.getId());
    }
}
```

Adapters testés : `JeuAdapter`, `AvisAdapter`, `AvatarAdapter`, `JoueurAdapter`, `ModerateurAdapter`, `GenreAdapter`, `EditeurAdapter`, `ClassificationAdapter`, `PlateformeAdapter`.

### 5.4 Tests des entités JPA — contraintes de base

Les contraintes `@Column(nullable = false)`, `@Column(unique = true)` et les relations sont vérifiées :

```java
@DataJpaTest
class JeuEntityJpaTest {
    @Test
    void save_shouldPersistWithAllRelations() { ... }
}
```

Entités testées : `JeuEntity`, `AvisEntity`, `AvatarEntity`, `JoueurEntity`, `ModerateurEntity`, `GenreEntity`, `EditeurEntity`, `ClassificationEntity`, `PlateformeEntity`.

### 5.5 Test de démarrage complet — `@SpringBootTest`

Un seul test charge l'intégralité du contexte Spring pour garantir qu'aucune erreur de configuration n'existe :

```java
@SpringBootTest
class AvisApplicationTests {
    @Test
    void contextLoads() { }
}
```

### 5.6 Configuration de test isolée

Un fichier `src/test/resources/application.properties` surcharge la configuration principale :
- Base H2 dédiée aux tests, différente de celle du développement
- `spring.sql.init.mode=never` — les données sont créées programmatiquement
- Secret JWT de test, sans dépendance à une variable d'environnement

---

## 6. Outils et techniques de modélisation

| Outil | Version | Rôle |
|---|---|---|
| **Java** | 25 | Langage — records, sealed classes, pattern matching |
| **Spring Boot** | 4.0.5 | Framework — IoC, MVC, Security, Data JPA, Actuator |
| **Spring Security** | (inclus SB) | Authentification JWT, autorisation par rôle |
| **JJWT** | 0.12.6 | Génération et validation des tokens JWT |
| **Bean Validation** | Jakarta | Validation déclarative des DTOs d'entrée |
| **MapStruct** | 1.6.x | Mapping domaine ↔ DTO généré à la compilation |
| **Lombok** | (inclus SB) | Réduction du boilerplate sur les POJO |
| **Spring Data JPA** | (inclus SB) | Persistence — repositories, pagination, requêtes nommées |
| **Hibernate** | (inclus SB) | ORM — mapping entités, optimistic locking `@Version` |
| **H2** | (inclus SB) | Base in-memory pour dev et tests |
| **PostgreSQL** | 16 | Base relationnelle pour la production |
| **Docker** | — | Conteneurisation — image multi-stage JDK→JRE Alpine |
| **Docker Compose** | — | Orchestration backend + PostgreSQL avec healthcheck |
| **JUnit 5** | (inclus SB) | Framework de test |
| **Mockito** | (inclus SB) | Mocks pour les tests unitaires |
| **AssertJ** | (inclus SB) | Assertions fluentes dans les tests |
| **SpringDoc OpenAPI** | 3.0.2 | Documentation API auto-générée `/swagger-ui.html` |
| **Spring Boot Actuator** | (inclus SB) | Healthcheck `/actuator/health` pour Docker |
| **PlantUML / Mermaid** | — | Diagrammes UML (état-transition, séquence, classes, use cases) |
| **IntelliJ IDEA** | — | Diagrammes de beans Spring (inversion de dépendances) |

---

## 7. Mise en application

### 7.1 Flux complet d'une requête

Voici le parcours d'une requête `POST /api/moderateurs/1/jeux` de bout en bout :

```
Client
  │
  │  POST /api/moderateurs/1/jeux  {nom, prix, genreId...}
  ▼
JwtFilter                          → vérifie le token, construit le SecurityContext
  │
  ▼
SecurityConfig                     → vérifie hasRole('MODERATEUR')
  │
  ▼
ModerateurController               → valide @Valid JeuDtoIn, appelle ajouterJeu()
  │
  ▼
ModerateurService                  → vérifie que le modérateur existe
  │
  ▼
JeuService.creerUnJeu()            → résout genre, éditeur, classification, plateformes
  │                                   mappe en domaine Jeu
  ▼
JeuAdapter.save()                  → persiste via JPA, retourne Jeu avec id
  │
  ▼
JeuService                         → calcule la note moyenne (null à la création)
  │                                   mappe Jeu → JeuDtoOut
  ▼
ModerateurController               → retourne 201 Created { id, nom, prix, genreNom... }
  │
  ▼
Client
```

### 7.2 Démarrage en développement

```bash
# Cloner et lancer le backend
cd backend
./mvnw spring-boot:run
# → H2 in-memory, données de dev chargées via data.sql
# → http://localhost:8080/actuator/health
# → http://localhost:8080/h2-console
# → http://localhost:8080/swagger-ui.html
```

### 7.3 Démarrage en production (Docker)

```bash
# Copier et remplir les variables d'environnement
cp .env.example .env

# Lancer PostgreSQL + Backend
docker compose up --build
# → PostgreSQL démarre avec healthcheck pg_isready
# → Backend attend que PostgreSQL soit prêt avant de démarrer
# → http://localhost:8080/actuator/health → {"status":"UP"}
```

### 7.4 Lancer les tests

```bash
cd backend
./mvnw test
# → 41 tests — unitaires, intégration JPA, controllers, démarrage
# → Base H2 isolée, données programmatiques, aucune dépendance externe
```

### 7.5 Résultats de couverture

| Couche | Stratégie | Annotations |
|---|---|---|
| Controllers | MockMvc standalone | `@ExtendWith(MockitoExtension.class)` |
| Services | Mocks purs | `@ExtendWith(MockitoExtension.class)` |
| Adapters JPA | Base H2 réelle | `@DataJpaTest` + `@Import` |
| Entités JPA | Contraintes DB | `@DataJpaTest` |
| Contexte Spring | Démarrage complet | `@SpringBootTest` |
