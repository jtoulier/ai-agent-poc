IF OBJECT_ID('credits.payments', 'U') IS NOT NULL
BEGIN
    DROP TABLE credits.payments;
END
GO

IF OBJECT_ID('credits.loans', 'U') IS NOT NULL
BEGIN
    DROP TABLE credits.loans;
END
GO

IF OBJECT_ID('credits.customers', 'U') IS NOT NULL
BEGIN
    DROP TABLE credits.customers;
END
GO

IF OBJECT_ID('credits.relationshipManagers', 'U') IS NOT NULL
BEGIN
    DROP TABLE credits.relationshipManagers;
END
GO

IF EXISTS (SELECT 1 FROM sys.schemas WHERE name = 'credits')
BEGIN
    DROP SCHEMA credits;
END
GO

CREATE SCHEMA credits;
GO

CREATE TABLE credits.relationshipManagers(
    relationshipManagerId   VARCHAR(16) NOT NULL,
    relationshipManagerName VARCHAR(64) NOT NULL,
    password                VARCHAR(256) NOT NULL,
    threadId                UNIQUEIDENTIFIER NULL,
    writtenAt               DATETIMEOFFSET(2) NOT NULL,

    CONSTRAINT pk_relman PRIMARY KEY (relationshipManagerId)
);
GO

CREATE TABLE credits.customers(
    customerId              VARCHAR(16) NOT NULL,
    customerName            VARCHAR(128) NOT NULL,
    customerTypeId          VARCHAR(16) NOT NULL CHECK (customerTypeId IN ('MICRO EMPRESA', 'PEQUEÑA EMPRESA', 'MEDIANA EMPRESA', 'GRAN EMPRESA')), -- MYPE=[MICRO, PEQUEÑA], PYME=[PEQUEÑA, MEDIANA]
    riskCategoryId          VARCHAR(2) NOT NULL CHECK (riskCategoryId IN ('NO', 'PP', 'DE', 'DU', 'PE')), -- NO=NORMAL, PP=POTENCIAL, DE=DEFICIENTE, DU=DUDOSO, PE=PÉRDIDA--
    lineOfCreditAmount      DECIMAL(18, 2) NOT NULL,
    relationshipManagerId   VARCHAR(16) NOT NULL,
    writtenAt               DATETIMEOFFSET(2) NOT NULL,

    CONSTRAINT pk_cus PRIMARY KEY (customerId),
    CONSTRAINT fk_relman_cus FOREIGN KEY (relationshipManagerId) REFERENCES credits.relationshipManagers(relationshipManagerId)
);
GO

CREATE TABLE credits.loans(
    loanId                  INT IDENTITY(1, 1) NOT NULL,
    customerId              VARCHAR(16) NOT NULL,
    currencyId              VARCHAR(3) NOT NULL CHECK (currencyId IN ('PEN', 'USD')),
    principalAmount         DECIMAL(18, 2) NOT NULL,
    interestRate            DECIMAL(5, 2) NOT NULL,
    loanDisbursementDate    DATE NOT NULL,
    numberOfMonthlyPayments TINYINT NOT NULL,
    loanStateId             VARCHAR(16) NOT NULL CHECK (loanStateId IN ('EN EVALUACION', 'VIGENTE', 'DESAPROBADO', 'CANCELADO')),
    writtenAt               DATETIMEOFFSET(2) NOT NULL,

    CONSTRAINT pk_loan PRIMARY KEY (loanId),
    CONSTRAINT fk_cus_loan FOREIGN KEY (customerId) REFERENCES credits.customers(customerId)
);
GO

CREATE TABLE credits.payments(
    loanId                  INT NOT NULL,
    paymentNumber           TINYINT NOT NULL,
    dueDate                 DATE NOT NULL,
    principalAmount         DECIMAL(18, 2) NOT NULL,
    interestAmount          DECIMAL(18, 2) NOT NULL,
    totalPaymentAmount      AS (principalAmount + interestAmount) PERSISTED,
    paymentStateId          VARCHAR(16) NOT NULL CHECK (paymentStateId IN ('PENDIENTE', 'PAGADO')),
    writtenAt               DATETIMEOFFSET(2) NOT NULL,

    CONSTRAINT pk_pay PRIMARY KEY (loanId, paymentNumber),
    CONSTRAINT fk_loan_pay FOREIGN KEY (loanId) REFERENCES credits.loans(loanId)
);
GO

/*
PROMPT
Carga Inicial de Datos en Azure SQL Database
Dadas las tablas creadas, se procede a la carga inicial de datos
Todas las fechas con zona horaria de Lima, Perú (UTC-5) y que sean relativas a hoy, deben calcularse en base a la fecha actual del sistema
Espero que me des los INSERTs necesarios para poblar las tablas con datos de prueba realistas en un solo archivo SQL. No quiero scripts para generar datos, solo los INSERTs. Dáme un archivo SQL para descargar ya expandido separado en secciones por tabla. No quiero que me des explicaciones, solo los INSERTs. Asegúrate que los datos cumplan con las siguientes reglas:
No quiero data parcial o de ejemplo para que yo la complete, quiero data completa que cumpla con todas las reglas.

Tabla "credits.relationshipManagers"
Poblar con 3 registros donde cada registro representa un gestor de relaciones con los siguientes atributos:
- relationshipManagerId: Un identificador único formado por la inicial del nombre y todas las letras del apellido. Todo en mayúsculas
- relationshipManagerName: un nombre y un apellido, generados aleatoriamente y de naturaleza latina. Asegúrate que no se generen relationshipManagerId duplicados. Todo en mayúsculas
- password: que sea siempre ABC123
- threadId: NULL
- writtenAt: la fecha y hora desde 2 años atrás hasta hoy

Tabla "credits.customers"
Poblar con 3 registros por cada ejecutivo de cuentas (relationshipManagerId) donde cada registro representa un cliente con los siguientes atributos:
- customerId: Un identificador único formado por la inicial del nombre y del apellido seguido de un número secuencial de 3 dígitos (ejemplo: JP001, JP002, etc.)
- customerName: nombres de empresas en mayúsculas con nombres de ríos y provincias de Perú, Papas medievales, de los siete diversos rubros más usados en el Perú. El tipo de sociedad debe ser uno de los siguientes: 'S.A.C.', 'S.A.', 'E.I.R.L.', 'S.R.L.', 'S.C.R.L.'
- customerTypeId: uno de los siguientes tipos: 'MICRO EMPRESA', 'PEQUEÑA EMPRESA', 'MEDIANA EMPRESA', 'GRAN EMPRESA'. Asegúrate que la distribución de tipos sea realista, con una mayoría de micro y pequeñas empresas pero usa todos los tipos. 'S.A.C.' para las empresas 'GRAN EMPRESA' y 'S.A.', 'S.R.L.', 'S.C.R.L.' para las empresas 'PEQUEÑA EMPRESA' y 'MEDIANA EMPRESA', y 'E.I.R.L.' para las 'MICRO EMPRESA'
- riskCategoryId: uno de los siguientes tipos: 'NO', 'PP', 'DE', 'DU', 'PE'. Asegúrate que la distribución de categorías de riesgo sea realista, con una mayoría en 'NO' y 'PP'. Asegúrate de repartir estos cinco valores entre los clientes
- lineOfCreditAmount: un monto decimal entre 50,000 y 50,000,000, con dos decimales donde los montos más bajos son para las MICRO y PEQUEÑA EMPRESA y los montos más altos para las MEDIANA y GRAN EMPRESA. Los dígitos de centenas, decenas y unidades deben ser ceros (ejemplo: 150,000.00)
- relationshipManagerId: un identificador de ejecutivo de cuentas existente
- writtenAt: la fecha y hora desde 2 años atrás hasta hoy

Tabla "credits.loans"
Poblar con 2 registros por cada cliente (customerId) donde cada registro representa un préstamo con los siguientes atributos:
- loanId: un identificador único autoincremental
- customerId: un identificador de cliente existente
- currencyId: uno de los siguientes tipos: 'PEN', 'USD'. Asegúrate que la mayoría de los préstamos estén en 'PEN' pero también usa 'USD'
- principalAmount: un monto decimal entre 5,000 y 5,000,000, con dos decimales donde los montos más bajos son para las MICRO y PEQUEÑA EMPRESA y los montos más altos para las MEDIANA y GRAN EMPRESA. Asegúrate que el monto de todos los préstamos VIGENTES de ese cliente no exceda la línea de crédito del cliente. Los dígitos de centenas, decenas y unidades deben ser ceros (ejemplo: 150,000.00)
- interestRate: un porcentaje decimal entre 5.00 y 15.00, con dos decimales. Las tasas más bajas son para las GRAN y MEDIANA EMPRESA y las tasas más altas para las MICRO y PEQUEÑA EMPRESA
- loanDisbursementDate: una fecha entre tres meses atrás y hoy
- numberOfMonthlyPayments: un número entero entre 2 y 6 y representa el número de pagos mensuales del préstamo
- loanStateId: uno de los siguientes tipos: 'EN EVALUACION', 'VIGENTE', 'DESAPROBADO', 'CANCELADO'. Asegúrate que la mayoría de los préstamos estén en 'VIGENTE' o 'EN EVALUACION'. Asegúrate que cada cliente tenga al menos un préstamo VIGENTE
- writtenAt: Si el estado es 'EN EVALUACION' o 'DESAPROBADO', la fecha y hora debe ser desde tres meses atrás hasta hoy. Si el estado es 'VIGENTE' debe ser la fecha y hora de vencimiento de la última cuota pagada. Si el estado es 'CANCELADO' debe ser la fecha y hora de la última cuota del préstamo
Dado que la tabla tiene una columna identity, asegúrate de usar SET IDENTITY_INSERT credits.loans ON; antes de los INSERTs y SET IDENTITY_INSERT credits.loans OFF; después de los INSERTs

Tabla "credits.payments"
Poblar con registros que representen el cronograma de pagos para cada préstamo (loanId) con los siguientes atributos:
- loanId: un identificador de préstamo existente
- paymentNumber: un número secuencial que comienza en 1 hasta el número de pagos mensuales del préstamo (numberOfMonthlyPayments)
- dueDate: la fecha de vencimiento del pago, que es un mes después de la fecha de desembolso del préstamo para el primer pago, dos meses después para el segundo pago, y así sucesivamente. Si cae feriado peruano o fin de semana, se debe mover al siguiente día hábil. No afecha la fecha de la siguiente cuota si la fecha de la cuota anterior se mueve
- principalAmount e interestAmount: el monto del pago de capital. Usa la fórmula de interés compuesto mensual para calcular un pago fijo mensual que cubra tanto el capital como los intereses durante el plazo del préstamo. Usa la fórmula: Payment = P * r / (1 - (1 + r)^-n), donde P es el monto del préstamo (principalAmount), r es la tasa de interés mensual (interestRate/100/12), y n es el número total de pagos mensuales (numberOfMonthlyPayments). El monto del pago de capital debe tener dos decimales. El último pago puede ajustarse para cubrir cualquier diferencia debido a redondeos en los pagos anteriores.
- paymentStateId: uno de los siguientes tipos: 'PENDIENTE', 'PAGADO'. Si la fecha de vencimiento es mayor a hoy, el estado debe ser 'PENDIENTE'. Si la fecha de vencimiento es menor o igual a hoy el estado debe ser 'PAGADO'. Si todas las cuotas del crédito están pagadas, el estado del crédito debe ser 'CANCELADO'.
- writtenAt: Si el estado es 'PENDIENTE', debe ser la fecha de loanDisbursementDate y una hora laboral aleatoria de ese día que es compartida por todas las cuotas pendientes de ese crédito. Si el estado es 'PAGADO', debe ser la fecha de vencimiento de esa cuota y una hora laboral aleatoria de ese día.
*/
-- INSERTS FOR credits.relationshipManagers
INSERT INTO credits.relationshipManagers (relationshipManagerId, relationshipManagerName, password, threadId, writtenAt) VALUES ('JPEREZ','JUAN PEREZ','ABC123', NULL, '2025-02-01 00:46:46 -05:00');
INSERT INTO credits.relationshipManagers (relationshipManagerId, relationshipManagerName, password, threadId, writtenAt) VALUES ('MGUTIERREZ','MARIA GUTIERREZ','ABC123', NULL, '2023-12-18 20:21:41 -05:00');
INSERT INTO credits.relationshipManagers (relationshipManagerId, relationshipManagerName, password, threadId, writtenAt) VALUES ('CLOPEZ','CARLOS LOPEZ','ABC123', NULL, '2023-10-12 19:14:03 -05:00');

-- INSERTS FOR credits.customers
INSERT INTO credits.customers (customerId, customerName, customerTypeId, riskCategoryId, lineOfCreditAmount, relationshipManagerId, writtenAt) VALUES ('JP001','EMPRESA MADRE DE DIOS LA LIBERTAD TRANSPORTE S.A.C.','GRAN EMPRESA','NO', 8000000.00, 'JPEREZ', '2024-02-24 19:36:08 -05:00');
INSERT INTO credits.customers (customerId, customerName, customerTypeId, riskCategoryId, lineOfCreditAmount, relationshipManagerId, writtenAt) VALUES ('LG001','EMPRESA CHANCHAMAYO ANCASH COMERCIO S.R.L.','MICRO EMPRESA','NO', 200000.00, 'JPEREZ', '2024-04-26 10:51:02 -05:00');
INSERT INTO credits.customers (customerId, customerName, customerTypeId, riskCategoryId, lineOfCreditAmount, relationshipManagerId, writtenAt) VALUES ('AM001','EMPRESA UCAYALI PIURA ALIMENTOS.S.A.','MICRO EMPRESA','PP', 100000.00, 'JPEREZ', '2024-04-26 04:53:50 -05:00');
INSERT INTO credits.customers (customerId, customerName, customerTypeId, riskCategoryId, lineOfCreditAmount, relationshipManagerId, writtenAt) VALUES ('AN001','EMPRESA AMAZONAS LAMBAYEQUE TRANSPORTE S.A.C.','MICRO EMPRESA','NO', 50000.00, 'MGUTIERREZ', '2023-12-04 09:54:28 -05:00');
INSERT INTO credits.customers (customerId, customerName, customerTypeId, riskCategoryId, lineOfCreditAmount, relationshipManagerId, writtenAt) VALUES ('LV001','EMPRESA RIMAC LA LIBERTAD CONSTRUCCION E.I.R.L.','MICRO EMPRESA','PE', 200000.00, 'MGUTIERREZ', '2024-04-15 20:01:34 -05:00');
INSERT INTO credits.customers (customerId, customerName, customerTypeId, riskCategoryId, lineOfCreditAmount, relationshipManagerId, writtenAt) VALUES ('SA001','EMPRESA TUMBES PIURA TEXTIL S.A.','PEQUEÑA EMPRESA','PP', 1000000.00, 'MGUTIERREZ', '2023-11-23 13:56:31 -05:00');
INSERT INTO credits.customers (customerId, customerName, customerTypeId, riskCategoryId, lineOfCreditAmount, relationshipManagerId, writtenAt) VALUES ('RM001','EMPRESA PISQU ICA SERVICIOS S.A.','MICRO EMPRESA','DU', 100000.00, 'CLOPEZ', '2024-12-14 19:37:26 -05:00');
INSERT INTO credits.customers (customerId, customerName, customerTypeId, riskCategoryId, lineOfCreditAmount, relationshipManagerId, writtenAt) VALUES ('MQ001','EMPRESA CHILLON LIMA AGROPECUARIO S.R.L.','MICRO EMPRESA','DE', 100000.00, 'CLOPEZ', '2023-11-24 08:25:48 -05:00');
INSERT INTO credits.customers (customerId, customerName, customerTypeId, riskCategoryId, lineOfCreditAmount, relationshipManagerId, writtenAt) VALUES ('RQ001','EMPRESA MARAÑÓN AREQUIPA COMERCIO S.C.R.L.','PEQUEÑA EMPRESA','NO', 1000000.00, 'CLOPEZ', '2025-01-29 03:01:00 -05:00');

-- INSERTS FOR credits.loans
SET IDENTITY_INSERT credits.loans ON;
INSERT INTO credits.loans (loanId, customerId, currencyId, principalAmount, interestRate, loanDisbursementDate, numberOfMonthlyPayments, loanStateId, writtenAt) VALUES (1,'JP001','PEN', 1000000.00, 7.97, '2025-07-16', 3, 'VIGENTE', '2025-09-16 15:45:00 -05:00');
INSERT INTO credits.loans (loanId, customerId, currencyId, principalAmount, interestRate, loanDisbursementDate, numberOfMonthlyPayments, loanStateId, writtenAt) VALUES (2,'JP001','PEN', 1000000.00, 8.57, '2025-07-01', 6, 'VIGENTE', '2025-09-01 10:00:00 -05:00');
INSERT INTO credits.loans (loanId, customerId, currencyId, principalAmount, interestRate, loanDisbursementDate, numberOfMonthlyPayments, loanStateId, writtenAt) VALUES (3,'LG001','PEN', 5000.00, 10.47, '2025-07-15', 6, 'VIGENTE', '2025-08-15 13:00:00 -05:00');
INSERT INTO credits.loans (loanId, customerId, currencyId, principalAmount, interestRate, loanDisbursementDate, numberOfMonthlyPayments, loanStateId, writtenAt) VALUES (4,'LG001','PEN', 10000.00, 12.78, '2025-07-23', 6, 'EN EVALUACION', '2025-07-26 00:10:24 -05:00');
INSERT INTO credits.loans (loanId, customerId, currencyId, principalAmount, interestRate, loanDisbursementDate, numberOfMonthlyPayments, loanStateId, writtenAt) VALUES (5,'AM001','PEN', 100000.00, 12.56, '2025-07-12', 3, 'VIGENTE', '2025-08-12 09:15:00 -05:00');
INSERT INTO credits.loans (loanId, customerId, currencyId, principalAmount, interestRate, loanDisbursementDate, numberOfMonthlyPayments, loanStateId, writtenAt) VALUES (6,'AM001','PEN', 100000.00, 11.88, '2025-08-12', 4, 'EN EVALUACION', '2025-08-21 03:40:02 -05:00');
INSERT INTO credits.loans (loanId, customerId, currencyId, principalAmount, interestRate, loanDisbursementDate, numberOfMonthlyPayments, loanStateId, writtenAt) VALUES (7,'AN001','PEN', 50000.00, 10.18, '2025-07-14', 4, 'VIGENTE', '2025-08-14 10:00:00 -05:00');
INSERT INTO credits.loans (loanId, customerId, currencyId, principalAmount, interestRate, loanDisbursementDate, numberOfMonthlyPayments, loanStateId, writtenAt) VALUES (8,'AN001','PEN', 50000.00, 11.88, '2025-08-07', 3, 'EN EVALUACION', '2025-08-21 07:43:18 -05:00');
INSERT INTO credits.loans (loanId, customerId, currencyId, principalAmount, interestRate, loanDisbursementDate, numberOfMonthlyPayments, loanStateId, writtenAt) VALUES (9,'LV001','PEN', 200000.00, 11.37, '2025-07-09', 6, 'VIGENTE', '2025-09-09 16:30:00 -05:00');
INSERT INTO credits.loans (loanId, customerId, currencyId, principalAmount, interestRate, loanDisbursementDate, numberOfMonthlyPayments, loanStateId, writtenAt) VALUES (10,'LV001','PEN', 200000.00, 12.65, '2025-07-04', 2, 'EN EVALUACION', '2025-07-18 06:45:33 -05:00');
INSERT INTO credits.loans (loanId, customerId, currencyId, principalAmount, interestRate, loanDisbursementDate, numberOfMonthlyPayments, loanStateId, writtenAt) VALUES (11,'SA001','PEN', 1000000.00, 10.73, '2025-07-30', 6, 'VIGENTE', '2025-09-30 10:15:00 -05:00');
INSERT INTO credits.loans (loanId, customerId, currencyId, principalAmount, interestRate, loanDisbursementDate, numberOfMonthlyPayments, loanStateId, writtenAt) VALUES (12,'SA001','PEN', 500000.00, 11.08, '2025-08-23', 3, 'EN EVALUACION', '2025-08-28 01:39:17 -05:00');
INSERT INTO credits.loans (loanId, customerId, currencyId, principalAmount, interestRate, loanDisbursementDate, numberOfMonthlyPayments, loanStateId, writtenAt) VALUES (13,'RM001','PEN', 100000.00, 11.21, '2025-07-28', 6, 'VIGENTE', '2025-09-28 11:15:00 -05:00');
INSERT INTO credits.loans (loanId, customerId, currencyId, principalAmount, interestRate, loanDisbursementDate, numberOfMonthlyPayments, loanStateId, writtenAt) VALUES (14,'RM001','PEN', 50000.00, 12.78, '2025-08-09', 4, 'DESAPROBADO', '2025-08-30 03:49:02 -05:00');
INSERT INTO credits.loans (loanId, customerId, currencyId, principalAmount, interestRate, loanDisbursementDate, numberOfMonthlyPayments, loanStateId, writtenAt) VALUES (15,'MQ001','PEN', 100000.00, 12.59, '2025-07-11', 5, 'VIGENTE', '2025-09-11 11:00:00 -05:00');
INSERT INTO credits.loans (loanId, customerId, currencyId, principalAmount, interestRate, loanDisbursementDate, numberOfMonthlyPayments, loanStateId, writtenAt) VALUES (16,'MQ001','PEN', 100000.00, 12.02, '2025-08-07', 2, 'EN EVALUACION', '2025-08-16 16:07:17 -05:00');
INSERT INTO credits.loans (loanId, customerId, currencyId, principalAmount, interestRate, loanDisbursementDate, numberOfMonthlyPayments, loanStateId, writtenAt) VALUES (17,'RQ001','PEN', 1000000.00, 8.86, '2025-07-19', 4, 'VIGENTE', '2025-09-19 09:15:00 -05:00');
INSERT INTO credits.loans (loanId, customerId, currencyId, principalAmount, interestRate, loanDisbursementDate, numberOfMonthlyPayments, loanStateId, writtenAt) VALUES (18,'RQ001','PEN', 1000000.00, 8.86, '2025-08-22', 3, 'EN EVALUACION', '2025-09-02 06:54:36 -05:00');
SET IDENTITY_INSERT credits.loans ON;

-- INSERTS FOR credits.payments
INSERT INTO credits.payments (loanId, paymentNumber, dueDate, principalAmount, interestAmount, paymentStateId, writtenAt) VALUES (1, 1, '2025-08-16', 31000.00, 6648.44, 'PAGADO', '2025-08-16 13:25:00 -05:00');
INSERT INTO credits.payments (loanId, paymentNumber, dueDate, principalAmount, interestAmount, paymentStateId, writtenAt) VALUES (1, 2, '2025-09-16', 32500.00, 4757.53, 'PAGADO', '2025-09-16 15:45:00 -05:00');
INSERT INTO credits.payments (loanId, paymentNumber, dueDate, principalAmount, interestAmount, paymentStateId, writtenAt) VALUES (1, 3, '2025-10-16', 36500.00, 2067.12, 'PENDIENTE', '2025-07-16 11:15:00 -05:00');
INSERT INTO credits.payments (loanId, paymentNumber, dueDate, principalAmount, interestAmount, paymentStateId, writtenAt) VALUES (2, 1, '2025-08-01', 15616.00, 7142.14, 'PAGADO', '2025-08-01 12:00:00 -05:00');
INSERT INTO credits.payments (loanId, paymentNumber, dueDate, principalAmount, interestAmount, paymentStateId, writtenAt) VALUES (2, 2, '2025-09-01', 16094.00, 6420.68, 'PAGADO', '2025-09-01 10:00:00 -05:00');
INSERT INTO credits.payments (loanId, paymentNumber, dueDate, principalAmount, interestAmount, paymentStateId, writtenAt) VALUES (2, 3, '2025-10-01', 16604.00, 5774.24, 'PENDIENTE', '2025-07-01 13:45:00 -05:00');
INSERT INTO credits.payments (loanId, paymentNumber, dueDate, principalAmount, interestAmount, paymentStateId, writtenAt) VALUES (2, 4, '2025-11-01', 17199.00, 5078.33, 'PENDIENTE', '2025-07-01 10:45:00 -05:00');
INSERT INTO credits.payments (loanId, paymentNumber, dueDate, principalAmount, interestAmount, paymentStateId, writtenAt) VALUES (2, 5, '2025-12-01', 17829.00, 4339.33, 'PENDIENTE', '2025-07-01 13:15:00 -05:00');
INSERT INTO credits.payments (loanId, paymentNumber, dueDate, principalAmount, interestAmount, paymentStateId, writtenAt) VALUES (2, 6, '2026-01-01', 192358.00, 3220.82, 'PENDIENTE', '2025-07-01 09:45:00 -05:00');
INSERT INTO credits.payments (loanId, paymentNumber, dueDate, principalAmount, interestAmount, paymentStateId, writtenAt) VALUES (3, 1, '2025-08-15', 774.00, 52.35, 'PAGADO', '2025-08-15 13:00:00 -05:00');
INSERT INTO credits.payments (loanId, paymentNumber, dueDate, principalAmount, interestAmount, paymentStateId, writtenAt) VALUES (3, 2, '2025-09-15', 800.00, 25.00, 'PAGADO', '2025-09-15 09:15:00 -05:00');
INSERT INTO credits.payments (loanId, paymentNumber, dueDate, principalAmount, interestAmount, paymentStateId, writtenAt) VALUES (3, 3, '2025-10-15', 823.00, 1.23, 'PENDIENTE', '2025-07-15 11:15:00 -05:00');
INSERT INTO credits.payments (loanId, paymentNumber, dueDate, principalAmount, interestAmount, paymentStateId, writtenAt) VALUES (3, 4, '2025-11-17', 850.00, 0.00, 'PENDIENTE', '2025-07-15 14:15:00 -05:00');
INSERT INTO credits.payments (loanId, paymentNumber, dueDate, principalAmount, interestAmount, paymentStateId, writtenAt) VALUES (3, 5, '2025-12-15', 875.00, 0.00, 'PENDIENTE', '2025-07-15 09:00:00 -05:00');
INSERT INTO credits.payments (loanId, paymentNumber, dueDate, principalAmount, interestAmount, paymentStateId, writtenAt) VALUES (3, 6, '2026-01-15', 926.00, 0.00, 'PENDIENTE', '2025-07-15 09:45:00 -05:00');
INSERT INTO credits.payments (loanId, paymentNumber, dueDate, principalAmount, interestAmount, paymentStateId, writtenAt) VALUES (4, 1, '2025-08-23', 1804.00, 150.00, 'PAGADO', '2025-08-23 09:30:00 -05:00');
INSERT INTO credits.payments (loanId, paymentNumber, dueDate, principalAmount, interestAmount, paymentStateId, writtenAt) VALUES (4, 2, '2025-09-23', 1848.00, 120.00, 'PAGADO', '2025-09-23 10:25:00 -05:00');
INSERT INTO credits.payments (loanId, paymentNumber, dueDate, principalAmount, interestAmount, paymentStateId, writtenAt) VALUES (4, 3, '2025-10-23', 1880.00, 90.00, 'PENDIENTE', '2025-07-23 16:20:00 -05:00');
INSERT INTO credits.payments (loanId, paymentNumber, dueDate, principalAmount, interestAmount, paymentStateId, writtenAt) VALUES (4, 4, '2025-11-24', 1896.00, 60.00, 'PENDIENTE', '2025-07-23 09:25:00 -05:00');
INSERT INTO credits.payments (loanId, paymentNumber, dueDate, principalAmount, interestAmount, paymentStateId, writtenAt) VALUES (5, 1, '2025-08-12', 33200.00, 10500.00, 'PAGADO', '2025-08-12 09:15:00 -05:00');
INSERT INTO credits.payments (loanId, paymentNumber, dueDate, principalAmount, interestAmount, paymentStateId, writtenAt) VALUES (5, 2, '2025-09-12', 34000.00, 8200.00, 'PAGADO', '2025-09-12 11:30:00 -05:00');
INSERT INTO credits.payments (loanId, paymentNumber, dueDate, principalAmount, interestAmount, paymentStateId, writtenAt) VALUES (5, 3, '2025-10-13', 32800.00, 5200.00, 'PENDIENTE', '2025-07-12 10:30:00 -05:00');
INSERT INTO credits.payments (loanId, paymentNumber, dueDate, principalAmount, interestAmount, paymentStateId, writtenAt) VALUES (6, 1, '2025-09-12', 25250.00, 9916.67, 'PAGADO', '2025-09-12 03:05:00 -05:00');
INSERT INTO credits.payments (loanId, paymentNumber, dueDate, principalAmount, interestAmount, paymentStateId, writtenAt) VALUES (6, 2, '2025-10-12', 25750.00, 9375.00, 'PENDIENTE', '2025-08-12 12:30:00 -05:00');
INSERT INTO credits.payments (loanId, paymentNumber, dueDate, principalAmount, interestAmount, paymentStateId, writtenAt) VALUES (6, 3, '2025-11-12', 26250.00, 8800.00, 'PENDIENTE', '2025-08-12 09:50:00 -05:00');
INSERT INTO credits.payments (loanId, paymentNumber, dueDate, principalAmount, interestAmount, paymentStateId, writtenAt) VALUES (6, 4, '2025-12-12', 27250.00, 8200.00, 'PENDIENTE', '2025-08-12 03:40:02 -05:00');
INSERT INTO credits.payments (loanId, paymentNumber, dueDate, principalAmount, interestAmount, paymentStateId, writtenAt) VALUES (7, 1, '2025-08-14', 12400.00, 426.67, 'PAGADO', '2025-08-14 10:00:00 -05:00');
INSERT INTO credits.payments (loanId, paymentNumber, dueDate, principalAmount, interestAmount, paymentStateId, writtenAt) VALUES (7, 2, '2025-09-15', 12600.00, 267.00, 'PAGADO', '2025-09-15 09:40:00 -05:00');
INSERT INTO credits.payments (loanId, paymentNumber, dueDate, principalAmount, interestAmount, paymentStateId, writtenAt) VALUES (7, 3, '2025-10-14', 12750.00, 90.00, 'PENDIENTE', '2025-07-14 09:15:00 -05:00');
INSERT INTO credits.payments (loanId, paymentNumber, dueDate, principalAmount, interestAmount, paymentStateId, writtenAt) VALUES (7, 4, '2025-11-14', 12350.00, 50.00, 'PENDIENTE', '2025-07-14 16:00:00 -05:00');
INSERT INTO credits.payments (loanId, paymentNumber, dueDate, principalAmount, interestAmount, paymentStateId, writtenAt) VALUES (8, 1, '2025-09-08', 16300.00, 495.00, 'PAGADO', '2025-09-08 09:45:00 -05:00');
INSERT INTO credits.payments (loanId, paymentNumber, dueDate, principalAmount, interestAmount, paymentStateId, writtenAt) VALUES (8, 2, '2025-10-08', 16800.00, 330.00, 'PENDIENTE', '2025-08-07 14:10:00 -05:00');
INSERT INTO credits.payments (loanId, paymentNumber, dueDate, principalAmount, interestAmount, paymentStateId, writtenAt) VALUES (8, 3, '2025-11-08', 16900.00, 120.00, 'PENDIENTE', '2025-08-07 16:25:00 -05:00');
INSERT INTO credits.payments (loanId, paymentNumber, dueDate, principalAmount, interestAmount, paymentStateId, writtenAt) VALUES (9, 1, '2025-08-09', 30250.00, 1888.33, 'PAGADO', '2025-08-09 16:30:00 -05:00');
INSERT INTO credits.payments (loanId, paymentNumber, dueDate, principalAmount, interestAmount, paymentStateId, writtenAt) VALUES (9, 2, '2025-09-09', 30750.00, 1485.00, 'PAGADO', '2025-09-09 09:20:00 -05:00');
INSERT INTO credits.payments (loanId, paymentNumber, dueDate, principalAmount, interestAmount, paymentStateId, writtenAt) VALUES (9, 3, '2025-10-09', 31250.00, 1066.67, 'PENDIENTE', '2025-07-09 09:10:00 -05:00');
INSERT INTO credits.payments (loanId, paymentNumber, dueDate, principalAmount, interestAmount, paymentStateId, writtenAt) VALUES (9, 4, '2025-11-10', 31750.00, 622.50, 'PENDIENTE', '2025-07-09 09:15:00 -05:00');
INSERT INTO credits.payments (loanId, paymentNumber, dueDate, principalAmount, interestAmount, paymentStateId, writtenAt) VALUES (9, 5, '2025-12-09', 32250.00, 166.67, 'PENDIENTE', '2025-07-09 09:30:00 -05:00');
INSERT INTO credits.payments (loanId, paymentNumber, dueDate, principalAmount, interestAmount, paymentStateId, writtenAt) VALUES (9, 6, '2026-01-09', 36250.00, 0.00, 'PENDIENTE', '2025-07-09 11:05:00 -05:00');
INSERT INTO credits.payments (loanId, paymentNumber, dueDate, principalAmount, interestAmount, paymentStateId, writtenAt) VALUES (10, 1, '2025-08-04', 98500.00, 4474.44, 'PAGADO', '2025-08-04 09:10:00 -05:00');
INSERT INTO credits.payments (loanId, paymentNumber, dueDate, principalAmount, interestAmount, paymentStateId, writtenAt) VALUES (10, 2, '2025-09-04', 101500.00, 3357.78, 'PAGADO', '2025-09-04 10:20:00 -05:00');
INSERT INTO credits.payments (loanId, paymentNumber, dueDate, principalAmount, interestAmount, paymentStateId, writtenAt) VALUES (11, 1, '2025-08-30', 15300.00, 8934.00, 'PAGADO', '2025-08-30 09:00:00 -05:00');
INSERT INTO credits.payments (loanId, paymentNumber, dueDate, principalAmount, interestAmount, paymentStateId, writtenAt) VALUES (11, 2, '2025-09-30', 15500.00, 8500.00, 'PAGADO', '2025-09-30 10:15:00 -05:00');
INSERT INTO credits.payments (loanId, paymentNumber, dueDate, principalAmount, interestAmount, paymentStateId, writtenAt) VALUES (11, 3, '2025-10-30', 15700.00, 8000.00, 'PENDIENTE', '2025-07-30 09:45:00 -05:00');
INSERT INTO credits.payments (loanId, paymentNumber, dueDate, principalAmount, interestAmount, paymentStateId, writtenAt) VALUES (11, 4, '2025-11-30', 15900.00, 7400.00, 'PENDIENTE', '2025-07-30 12:00:00 -05:00');
INSERT INTO credits.payments (loanId, paymentNumber, dueDate, principalAmount, interestAmount, paymentStateId, writtenAt) VALUES (11, 5, '2025-12-30', 16100.00, 6800.00, 'PENDIENTE', '2025-07-30 14:15:00 -05:00');
INSERT INTO credits.payments (loanId, paymentNumber, dueDate, principalAmount, interestAmount, paymentStateId, writtenAt) VALUES (11, 6, '2026-01-30', 16600.00, 6200.00, 'PENDIENTE', '2025-07-30 09:35:00 -05:00');
INSERT INTO credits.payments (loanId, paymentNumber, dueDate, principalAmount, interestAmount, paymentStateId, writtenAt) VALUES (12, 1, '2025-09-23', 164000.00, 10833.33, 'PAGADO', '2025-09-23 09:20:00 -05:00');
INSERT INTO credits.payments (loanId, paymentNumber, dueDate, principalAmount, interestAmount, paymentStateId, writtenAt) VALUES (12, 2, '2025-10-23', 166000.00, 8325.00, 'PENDIENTE', '2025-08-23 09:05:00 -05:00');
INSERT INTO credits.payments (loanId, paymentNumber, dueDate, principalAmount, interestAmount, paymentStateId, writtenAt) VALUES (12, 3, '2025-11-24', 170000.00, 5500.00, 'PENDIENTE', '2025-08-23 01:39:17 -05:00');
INSERT INTO credits.payments (loanId, paymentNumber, dueDate, principalAmount, interestAmount, paymentStateId, writtenAt) VALUES (13, 1, '2025-08-28', 14500.00, 1025.00, 'PAGADO', '2025-08-28 11:15:00 -05:00');
INSERT INTO credits.payments (loanId, paymentNumber, dueDate, principalAmount, interestAmount, paymentStateId, writtenAt) VALUES (13, 2, '2025-09-28', 14700.00, 800.00, 'PAGADO', '2025-09-28 11:15:00 -05:00');
INSERT INTO credits.payments (loanId, paymentNumber, dueDate, principalAmount, interestAmount, paymentStateId, writtenAt) VALUES (13, 3, '2025-10-28', 14900.00, 500.00, 'PENDIENTE', '2025-07-28 09:55:00 -05:00');
INSERT INTO credits.payments (loanId, paymentNumber, dueDate, principalAmount, interestAmount, paymentStateId, writtenAt) VALUES (13, 4, '2025-11-28', 15300.00, 200.00, 'PENDIENTE', '2025-07-28 14:40:00 -05:00');
INSERT INTO credits.payments (loanId, paymentNumber, dueDate, principalAmount, interestAmount, paymentStateId, writtenAt) VALUES (13, 5, '2025-12-28', 15500.00, 50.00, 'PENDIENTE', '2025-07-28 09:20:00 -05:00');
INSERT INTO credits.payments (loanId, paymentNumber, dueDate, principalAmount, interestAmount, paymentStateId, writtenAt) VALUES (13, 6, '2026-01-28', 16800.00, 0.00, 'PENDIENTE', '2025-07-28 09:40:00 -05:00');
INSERT INTO credits.payments (loanId, paymentNumber, dueDate, principalAmount, interestAmount, paymentStateId, writtenAt) VALUES (14, 1, '2025-09-09', 12250.00, 675.00, 'PAGADO', '2025-09-09 06:40:00 -05:00');
INSERT INTO credits.payments (loanId, paymentNumber, dueDate, principalAmount, interestAmount, paymentStateId, writtenAt) VALUES (14, 2, '2025-10-09', 12350.00, 450.00, 'PENDIENTE', '2025-08-09 14:50:00 -05:00');
INSERT INTO credits.payments (loanId, paymentNumber, dueDate, principalAmount, interestAmount, paymentStateId, writtenAt) VALUES (14, 3, '2025-11-10', 12500.00, 200.00, 'PENDIENTE', '2025-08-09 09:05:00 -05:00');
INSERT INTO credits.payments (loanId, paymentNumber, dueDate, principalAmount, interestAmount, paymentStateId, writtenAt) VALUES (14, 4, '2025-12-09', 12700.00, 100.00, 'PENDIENTE', '2025-08-09 03:49:02 -05:00');
INSERT INTO credits.payments (loanId, paymentNumber, dueDate, principalAmount, interestAmount, paymentStateId, writtenAt) VALUES (15, 1, '2025-08-11', 19600.00, 1023.33, 'PAGADO', '2025-08-11 11:00:00 -05:00');
INSERT INTO credits.payments (loanId, paymentNumber, dueDate, principalAmount, interestAmount, paymentStateId, writtenAt) VALUES (15, 2, '2025-09-11', 19800.00, 600.00, 'PAGADO', '2025-09-11 11:00:00 -05:00');
INSERT INTO credits.payments (loanId, paymentNumber, dueDate, principalAmount, interestAmount, paymentStateId, writtenAt) VALUES (15, 3, '2025-10-11', 20000.00, 250.00, 'PENDIENTE', '2025-07-11 09:25:00 -05:00');
INSERT INTO credits.payments (loanId, paymentNumber, dueDate, principalAmount, interestAmount, paymentStateId, writtenAt) VALUES (15, 4, '2025-11-11', 20200.00, 120.00, 'PENDIENTE', '2025-07-11 12:20:00 -05:00');
INSERT INTO credits.payments (loanId, paymentNumber, dueDate, principalAmount, interestAmount, paymentStateId, writtenAt) VALUES (15, 5, '2025-12-11', 20600.00, 50.00, 'PENDIENTE', '2025-07-11 14:40:00 -05:00');
INSERT INTO credits.payments (loanId, paymentNumber, dueDate, principalAmount, interestAmount, paymentStateId, writtenAt) VALUES (16, 1, '2025-09-08', 49350.00, 401.67, 'PAGADO', '2025-09-08 15:45:00 -05:00');
INSERT INTO credits.payments (loanId, paymentNumber, dueDate, principalAmount, interestAmount, paymentStateId, writtenAt) VALUES (16, 2, '2025-10-08', 50650.00, 200.00, 'PENDIENTE', '2025-08-07 12:30:00 -05:00');
INSERT INTO credits.payments (loanId, paymentNumber, dueDate, principalAmount, interestAmount, paymentStateId, writtenAt) VALUES (17, 1, '2025-08-19', 23800.00, 741.67, 'PAGADO', '2025-08-19 09:15:00 -05:00');
INSERT INTO credits.payments (loanId, paymentNumber, dueDate, principalAmount, interestAmount, paymentStateId, writtenAt) VALUES (17, 2, '2025-09-19', 24100.00, 428.33, 'PAGADO', '2025-09-19 09:15:00 -05:00');
INSERT INTO credits.payments (loanId, paymentNumber, dueDate, principalAmount, interestAmount, paymentStateId, writtenAt) VALUES (17, 3, '2025-10-19', 24400.00, 110.00, 'PENDIENTE', '2025-07-19 09:05:00 -05:00');
INSERT INTO credits.payments (loanId, paymentNumber, dueDate, principalAmount, interestAmount, paymentStateId, writtenAt) VALUES (17, 4, '2025-11-19', 25100.00, 50.00, 'PENDIENTE', '2025-07-19 13:20:00 -05:00');
INSERT INTO credits.payments (loanId, paymentNumber, dueDate, principalAmount, interestAmount, paymentStateId, writtenAt) VALUES (18, 1, '2025-09-22', 330000.00, 26600.00, 'PAGADO', '2025-09-22 06:54:36 -05:00');
INSERT INTO credits.payments (loanId, paymentNumber, dueDate, principalAmount, interestAmount, paymentStateId, writtenAt) VALUES (18, 2, '2025-10-22', 332000.00, 15000.00, 'PENDIENTE', '2025-08-22 09:10:00 -05:00');
INSERT INTO credits.payments (loanId, paymentNumber, dueDate, principalAmount, interestAmount, paymentStateId, writtenAt) VALUES (18, 3, '2025-11-22', 338000.00, 8000.00, 'PENDIENTE', '2025-09-02 06:54:36 -05:00');