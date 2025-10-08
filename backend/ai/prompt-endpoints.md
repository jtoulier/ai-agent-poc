# Generación de proyecto
Quiero que generes un proyecto de API REST en Java Quarkus CNF completo, respetando las mejores prácticas de la industria y las siguientes consideraciones:

Dados:
- El pom.xml adjunto. Respeta dicho archivo, no modifiques absolutamente nada. El proyecto va a ser editado en IntelliJ IDEA Community Edition correctamente configurado para dicho POM
- El esquema de base de datos adjunto schema.sql
- La estructura de carpetas
```code
  └───main
  ├───docker
  ├───java
  │   └───com
  │       └───springonly
  │           └───backend
  │               ├───mapper
  │               ├───model
  │               │   ├───dto
  │               │   ├───entity
  │               │   ├───request
  │               │   └───response
  │               ├───repository
  │               ├───resource
  │               └───service
  └───resources
  ├───banner
  ├───openapi
  └───sql
```
- El flujo de información va así
  - resource -> service -> repository
  - repository -> service -> resource
- En todos los endpoints viene un header llamado X-RelationshipManager-Id que representa al usuario loggeado, por el momento solo recíbelo, no hagas nada más
- El resource recibe clases Request y responde clases Response
- El service recibe y responde clases DTO
- El repository recibe DTOs y los transforma a Entity, asimismo lee de la base de datos Entity y retorna DTOs
- En mapper usa MapStruct
- En model, los DTO son agnósticos, los Entity se mapean a la estructura de la base de datos, los Request se usan solo como entrada en los resource y finalmente los Response se usan solo como salida de los resource
- En los repository usa la interfaz PanacheRepositoryBase cuando el Id no sea Long. También tienen la lógica de filtrar o hacer el "SELECT" cuando no sea trivial la consulta a la base de datos 
- En los service va la lógica de negocio, si hubiese, como por ejemplo poner fechas como hoy de ser el caso en caso de las actualizaciones. Los service son clases, no interfaces.
- En los resource te defino en un cuadro las características de cada endpoint e implementa la lógica respetando las responsabilidades definidas para cada capa
- No hagas getter ni setters. Utiliza Lombok con las anotaciones @Data, @AllArgsConstructor y @NoArgsConstructor. Mi IDE ya está configurado
- No uses nada de reactividad, solo programación imperativa
- No inventes cosas que no estés seguro, todo debe estar fundamentado
- Los nombres de las variables deben ser como el de la clase por ejemplo evita "CustomerMapper mapper;" en su lugar emplea "CustomerMapper customerMapper;"
- Los resultados dámelos archivo por archivo
- En openapi dáme el Open API en formato json y válido para Azure AI Foundry. Usa el ejemplo que te doy


# Package: resource
## RelationshipManagerResource.java
|verb|endpoint| operation                              | description |
|-|-|----------------------------------------|-|
|POST|/api/relationship-managers/login| loginRelationshipManager               |Validar las credenciales de un Ejecutivo de Cuenta y retornar su nombre y threadId de conversación con el Agente AI|
|PATCH|/api/relationship-managers/{relationshipManagerId}| updateRelationshipManager              |Actualizar los datos de un Ejecutivo de Cuenta, es particular el threadId de la conversación con el Agente AI|
|GET|/api/relationship-managers/{relationshipManagerId}| getRelationshipManagerById             |Obtener los datos de un Ejecutivo de Cuenta, excepto el password|
|GET|/api/relationship-managers/{relationshipManagerId}/customers| listCustomersByRelationshipManagerById |Obtener la relación de clientes de un Ejecutivo de Cuenta|

## CustomerResource.java
|verb|endpoint|operation|description |
|-|-|-|-|
|POST|/api/customers|createCustomer|Crear un cliente|
|PATCH|/api/customers/{customerId}|updateCustomer|Actualizar los datos de un cliente|
|GET|/api/customers/{customerId}|getCustomerById|Obtener los detalles de un cliente|
|GET|/api/customers/{customerId}/loans|listLoansByCustomerId|Obtener la relación de préstamos o créditos de un cliente|

## LoanResource.java 
|verb|endpoint|operation|description |
|-|-|-|-|
|POST|/api/loans|createLoan|Crear un crédito o préstamo, sin las cuotas|
|PATCH|/api/loans/{loanId}|updateLoan|Actualizar un crédito o préstamo|
|GET|/api/loans/{loanId}|getLoanById|Obtener el detalle de un préstamo o crédito en particular, sin las cuotas|
|POST|/api/loans/{loanId}/payments/{paymentNumber}|createPayment|Crear una cuota en un crédito dado|
|PATCH|/api/loans/{loanId}/payments/{paymentNumber}|updatePayment|Actualizar los datos de una cuota|
|GET|/api/loans/{loanId}/payments|listPaymentsByLoanId|Obtener la relación de cuotas de un crédito en particular|
|GET|/api/loans/{loanId}/payments//{paymentNumber}|getPaymentById|Obtener el detalle de una cuota en particular|

