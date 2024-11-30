# dépendance requis pour le début
- Spring boot Devtool
- spring web
- H2 database
- spring Data Jpa (spring-boot-starter-data-jpa)

# Projet Planning avec Github Issue(User story) Plan2


# API first Approach(avec swagger hub)
meilleur collaboration et communication
meilleur developpenet expérience
developpement rapide

# Créé des issue pour la gestion de chaque entité

# créeer une branch pour chaque entité
- git branch artifact-crud
- Wizard
- Massire

# DESIGN(User Story)(Design) Plan3
explication des relation entre les entité dans un
de diagramme de classe des entités

Artifact
id
name
description
url

Wizard
id name
name

## implementation de artifact et wizard 
un wizard (magicien) peut avoir 0 ou plusieurs artifact (Objet de magie)
artifact 0,n ----- o,1 wizard

- Diagramme de sequence
findById->:ArtifactController-->findById->:ArtifactService-->findByArtifactRepo

## Gestion D'exception
        @ExceptionHandler(ObjectNotFoundException.class)
        @ResponseStatus(HttpStatus.NOT_FOUND)
        Result handleObjectNofoundException(Exception ex, HttpServletRequest request) {
           return new Result(false, ex.getMessage(), StatusCode.NOT_FOUND);
        }
NB:
- @Transactional
- @RequestMapping("${api.endpoint.base-url}")
- Appres avoir crée le controller, on regarde lapi consruit dans swagger
- validation de données avec import jakarta.validation.constraints.NotEmpty; cest quoi ?
        <!-- https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-validation -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-validation</artifactId>
        <version>3.4.0</version>
    </dependency>
- c'est quoi un record ?
  public record ArtifactDto(
- c'est quoi un implements Converter<Artifact, ArtifactDto> 
- c'est quoi @Component

- commentaire Crud repository
 click sur JpaRepository
 puis click sur LiscrudReposittory
 puis click sur crudRepository
- OrElseThrow() c'est quoi , landa expression
- Explique pourquoi utiliser stream et nom pas directement stream ?
  this.artifactService.findAll()
   .stream()
   .map(artifactToArtifactDtoConverter::convert));
- c'est quoi implements CommandLineRunner, 
 quand sprind demarrae il exécute dabord ça.
- @Repository c'est quoi ?

# TestDriven Developpement(TDD))(Implementation et Testing) Plan4 (Ne demande pas de Bd)
### (Mocking)
    -   ctrl+entrer
    lorsquon éxécute dans un premier temps, il échoura naturellement, 
    puisque nous navons pas écrit de code dimplémentation, c'est l'étape rouge,
    le scénario de test échoue, nous voulons le rendre vert, donc écrire assez de code
    pour que le code réussisse, mais nous ne nous arretons pas la, nous refactorisons ensuite
    le codepour le rendre plus maintenable, et plusrobuste

### Service Test et Controller Test , ne demandent pas de Base de donnée,
### utilisation de h2 pendant le developpement
- apres avoior tester service et controller, connecter H2
- H2 , reinitialise la base de donnée a chaque fois que lon démarre l'application,
 h2 accelere notre developpement, augmente la productivité, pas de temps à perdre
 pour installer mysql pendant la phase de developpement(ne pas utiliser en production)

# API Integration test Avec Postman
- Pourquoi DTO,  Infinite recusion pourquoi ?,
  lorsque Jackson est la bibliotheque Json utilisé par Springboot, essaie
    de sérialiséer cer artefact dans une chaine JSon, il reste coincé dans 
    une boucle infinie, voici ce que ça veut dire, quand il sérialise artifact
  il a aucun problème mais lorsqu'il rencontre le propriétaire et essai de seriealisé 
  le propriétaire de cet artifact aucun probleme mais ensuite jackson va trouvers la
  liste artefact de ce propriétaire, il continuera a sérialisé les object contenant cet 
  object ainsi il reste dans une boucle infini.
    c'est ce qu'on appelle JSON infinite Recursion dans une relation One-to-Many
### DTO pourquoi ?
-   Controle des info que l'api renvoie
- On utilise Java record pour implementer DTO
  - Java record a été introduite avec l'intenetion d'etre utilisé comme un  moyen rapide
    de créer des class de support de données, il n'ya pas de logique métier, juste un 
    suport de données ou un container
- Au lieu de renvoyer directement les donnée recu depuis la base de données, on le 
    convvertu en dto puis on les renvoie
   

