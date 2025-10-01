# Entorno DEV

## Probar en vivo
```bash
mvn quarkus:dev
```

## Compilar y ejecutar
```bash
clear
mvn clean package -Dquarkus.profile=dev
java -jar ./target/quarkus-app/quarkus-run.jar 
```

# Entorno PROD

## Compilar y ejecutar
```bash
clear
mvn clean package
java -jar ./target/quarkus-app/quarkus-run.jar
```

# Swagger UI
http://localhost:8080/q/swagger-ui

# Open API
http://localhost:8080/q/openapi

# Endpoints
- Validar usuario y password
POST /relationship-managers/login
login
LoginRequest
{
  "relationshipManagerId": "RM001",
  "password": "secret123"
}
LoginResponse
{
  "relationshipManagerName": "Juan Pérez",
  "threadId": "550e8400-e29b-41d4-a716-446655440000"
}


- Obtener cartera de clientes dado un ejecutivo de cuentas
GET /relationship-managers/{relationshipManagerId}/customers
listCustomers
CustomersRetrievalResponse
{
  "customers": [
  - {
      "customerId": "C001",
      "customerName": "Empresa ABC SAC",
      "customerTypeId": "PEQUEÑA EMPRESA",
      "riskCategoryId": "NO",
      "lineOfCreditAmount": 500000.00,
      "relationshipManagerId": "RM001",
      "writtenAt": "2025-09-23T10:30:00-05:00"
    },
    {
      "customerId": "C002",
      "customerName": "Inversiones XYZ SRL",
      "customerTypeId": "MEDIANA EMPRESA",
      "riskCategoryId": "PP",
      "lineOfCreditAmount": 800000.00,
      "relationshipManagerId": "RM001",
      "writtenAt": "2025-09-20T15:45:00-05:00"
    }
  ]
}

- Obtener datos de un cliente
GET /customers/{customerId}
getCustomer
CustomerRetrievalResponse
{
  - "customer": {
        "customerId": "C001",
        "customerName": "Empresa ABC SAC",
        "customerTypeId": "PEQUEÑA EMPRESA",
        "riskCategoryId": "NO",
        "lineOfCreditAmount": 500000.00,
        "relationshipManagerId": "RM001",
        "writtenAt": "2025-09-23T10:30:00-05:00"
  }
}

- Obtener los préstamos de un cliente
GET /customers/{customerId}/loans
listLoans
LoansRetrievalResponse
{
    "loans": [
        {
            "loanId": 101,
            "currencyId": "PEN",
            "principalAmount": 100000.00,
            "interestRate": 12.50,
            "loanDisbursementDate": "2025-09-01",
            "numberOfMonthlyPayments": 24,
            "loanStateId": "VIGENTE",
            "writtenAt": "2025-09-01T09:00:00-05:00"
        },
        {
            "loanId": 102,
            "currencyId": "USD",
            "principalAmount": 50000.00,
            "interestRate": 10.00,
            "loanDisbursementDate": "2025-08-15",
            "numberOfMonthlyPayments": 12,
            "loanStateId": "EN EVALUACION",
            "writtenAt": "2025-08-15T11:15:00-05:00"
        }
    ]
}


- Obtener el detalle de un préstamo de un cliente (sin cronograma)
GET /loans/{loanId}
getLoan
LoanRetrievalResponse
{
  "loan": { 
    "loanId": 101,
    "currencyId": "PEN",
    "principalAmount": 100000.00,
    "interestRate": 12.50,
    "loanDisbursementDate": "2025-09-01",
    "numberOfMonthlyPayments": 24,
    "loanStateId": "VIGENTE",
    "writtenAt": "2025-09-01T09:00:00-05:00"
  }
}

- Obtener los pagos o cronograma de un préstamo
GET /loans/{loanId}/payments
listPayments
PaymentsRetrievalResponse
{
    "payments": [
        {
            "paymentNumber": 1,
            "dueDate": "2025-10-01",
            "principalAmount": 4000.00,
            "interestAmount": 1041.67,
            "totalPaymentAmount": 5041.67,
            "paymentStateId": "PENDIENTE",
            "writtenAt": "2025-09-01T09:00:00-05:00"
        },
        {
            "paymentNumber": 2,
            "dueDate": "2025-11-01",
            "principalAmount": 4000.00,
            "interestAmount": 1000.00,
            "totalPaymentAmount": 5000.00,
            "paymentStateId": "PENDIENTE",
            "writtenAt": "2025-09-01T09:00:00-05:00"
        }
    ]
}

- Crear un préstamo y su cronograma para un cliente
POST /customers/{customerId}/loans
createLoan
LoanCreationRequest
{
  "currencyId": "PEN",
  "principalAmount": 100000.00,
  "interestRate": 12.50,
  "numberOfMonthlyPayments": 24
}
204


- Pagar la cuota vigente de un préstamo el mismo día de su vencimiento
POST /loans/{loanId}/payments/{paymentNumber}/pay
payPayment
PaymentPaymentResponse
{
  "loanId": 201,
  "paymentNumber": 1,
  "dueDate": "2025-10-23",
  "principalAmount": 4166.67,
  "interestAmount": 1041.67,
  "totalPaymentAmount": 5208.34,
  "paymentStateId": "PAGADO",
  "paymentDate": "2025-10-23",
  "writtenAt": "2025-09-23T11:00:00-05:00"
}