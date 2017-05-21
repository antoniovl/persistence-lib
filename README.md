# persistence-tools

Persistence-tools is a utility that allows the separation of the transaction demarcation from the business logic. 
This is a simplified version from the original library I wrote for my own projects (which has been use for quite some time in production). 
I took as a reference the Command Pattern depicted in [Professional Java JDK6th Edition](https://www.amazon.com/Professional-Java-JDK-Clay-Richardson/dp/0471777102/ref=sr_1_fkmr0_1?ie=UTF8&qid=1495309531&sr=8-1-fkmr0&keywords=Professional+Java+JDK+6th+edition)
but I ended up with a variant of it.

The project uses Gradle, and consists in 2 modules:
* **persistence-lib** : Contains base clases and constants.
* **persistence-jpa-impl** : Concrete implementation for JPA to be used outside a managed environment (i.e Tomcat with transaction-type=RESOURCE_LOCAL), but it works fine in CMT.

Modules for Pure Hibernate and plain JDBC are planned to be implemented.

# Usage
The main components are:
* Command: Contains a unit of work to be run within a transaction.
* CommandManager: Executes a Command, and requires a CommandExecutor to do so.
* CommandExecutor: The CommandManager uses this class to prepare and setup the Command to be executed. This class it's the ony one
that it´s implemented and depends on the project.

Persistence-lib consists in many abstract classe besides the SQLExecutor utility class. We need to focus on the concrete implementations.

## Using JPA
The main components are:
* JPACommand
* JPACommandManager
* A concrete implementation of CommandExecutor.
One of the main changes in this version of the tool is that we don´t require a concrete implementation of a command 
to exeucte transactional code, the command manager can take a Lambda as a parameter.

The CommandExecutor implementation it´s particular to each project, and it´s used to prepare and execute the command.
```java
public class CommandExecutorImpl implements CommandExecutor {
    @Override
    public <T> void executeCommand(Command<T> cmd) {
        ExecutorContext context = getContext();
        cmd.setTransactionType(TransactionTypesEnum.LOCAL);
        cmd.run(context);
    }

    protected JPAExecutorContext getContext() {
        EntityManager entityManager = JPATools.getEntityManager("my_persistence_unit");
        return new JPAExecutorContext(entityManager);
    }
}
```
This is how a basic CommandExecutor implementation will look like. JPAExecutorContext extends ExecutorContext and holds the current 
instance of EntityManager, and will talk in a moment about how we can get an EntityManager. The method `getContext()` creates 
an instance of JPAExecutorContext, which it's passed to the Command's `run()` method. Also, we are specifying that the 
transaction type is resource local. Valid transaction types are defined in `TransactionTypeEnum`:

* CONTAINER: We're running on a EBJ/CMT environment where the transaction demarcation it's done by someone else. In this case 
the Command's unit of work it's executed without creating any transaction.
* USER: The transaction it's controlled by a UserTransaction. Tipically when using BMT (Bean Managed Transaction) in Java EE.
* LOCAL: Our application runs in a Java SE environment.

We use the CommandManager to execute transactional code. Our DAOs can use an instance of it or can extend it:
```java
public class UserDao extends CommandManager {
   public UserDao(CommandExecutor executor) {
    super(executor);
   }
   
   public User createUser(User u) {
     return execute(executorContext -> {
       JPAExecutorContext ctx = JPAExecutorContext.instance(executorContext);
       EntityManager entityManager = ctx.getEntityManager();
       // ... do something with our entity
       entityManager.persist(u);
       return u;
     });
   }
}
```
```java
public class UserDao extends OtherSuperClass {
   private CommandManager commandManager;
   
   public UserDao(CommandExecutor executor) {
    commandManager = JPACommandManager(executor);
   }
   
   public User createUser(User u) {
     return commandManager.execute(executorContext -> {
       JPAExecutorContext ctx = JPAExecutorContext.instance(executorContext);
       EntityManager entityManager = ctx.getEntityManager();
       // ... do something with our entity
       entityManager.persist(u);
       return u;
     });
   }
}
```
If we're using CDI/Autowire in our Application, then we can manage to inject instances of CommandExecutor or CommandManager into our beans.

JPACommand checks if a transaction it's already active and executes the unit of work under it's scope. So it's safe do to something
like:
```java
public class UserDao extends OtherSuperClass {
   private CommandManager commandManager;
   
   public UserDao(CommandExecutor executor) {
    commandManager = JPACommandManager(executor);
   }
   
   public void persistSomething() {
     commandManager.execute(executorContext -> {
       // ... persist something in the DB
       // then call other method
       persistOther();
     }
   }
   public void persistOther() {
     return commandManager.execute(executorContext -> {
       // ... persist other thing in the DB
     });
   }
}
```
Also, having support for Container Managed Transactions will allow to use our code in Java EE environment just changing the transaction type to CONTAINER by the CommandExecutor.

### JPATools
JPATools provides several utility methods.
```java
getEntityManager(String persistenceUnit, Map overwrite)
getEntityManager(String persistenceUnit)
```
Arguments:
* persistenceUnit: String with the name of the persistenceUnit declared in persistence.xml We can use multiple persistence units
as long as we declare them in persistence.xml.
* overwrite: Map with properties for the EntityManager. 
The class holds internally an EntityManagerFactory for each persistence unit, and getting an EntityManager it's thread safe.

---
```java
closeEntityManagerFactories()
```
Closes the EntityManagerFactories that have been created.

---
```java
getCurrentEntity(Object entity, Class<T> klass, EntityManager entityManager)
```
Arguments:
* entity : Entity to be loaded from the database.
* klass : Type of the entity.
* entityManager : Current entityManager.
This method will load the provided entity from the database. It will inspect the annotated properties for @Id or @EmbeddedId and will take the values to invoke entityManager.find(). Example:
```java
// someAuthor won't be modified
Author author = JPATools.getCurrentEntity(someAuthor, Author.class, entityManager);
```

---
```java
Optional<T> getSingleResult(Query q)
Optional<T> getSingleResult(TypedQuery<T> q)
```
Arguments:
* Query : JPA Query
* TypedQuery : JPA TypedQuery
The default behavior for query.getSingleResult() is to throw an exception if no results are found. In this case we return an
Optional with either the result or empty.

(c) CopyRight - Antonio Varela
Released under the MIT Licence
