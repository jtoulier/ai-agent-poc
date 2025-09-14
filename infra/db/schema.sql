IF OBJECT_ID('dbo.credits', 'U') IS NOT NULL
BEGIN
    DROP TABLE dbo.credits;
END
GO

IF OBJECT_ID('dbo.orders', 'U') IS NOT NULL
BEGIN
    DROP TABLE dbo.orders;
END
GO

CREATE TABLE [dbo].[orders](
    [orderId] [int] IDENTITY(1,1) NOT NULL,
    [businessId] [varchar](16) NULL,
    [businessName] [varchar](256) NULL,
    [orderState] [varchar](32) NULL,
    [writtenAt] [datetime2](2) NULL,
    [author] [varchar](32) NULL,

    CONSTRAINT pk_ord PRIMARY KEY (orderId)
) ON [PRIMARY]
GO

CREATE TABLE [dbo].[credits](
    [orderId] [int] NOT NULL,
    [amount] [numeric](15, 2) NULL,
    [interestRate] [numeric](5, 2) NULL,
    [dueDate] [date] NULL,
    [writtenAt] [datetime2](2) NULL,
    [author] [varchar](32) NULL,

    CONSTRAINT pk_cre PRIMARY KEY (orderId),
    CONSTRAINT fk_ord_cre FOREIGN KEY (orderId) REFERENCES dbo.orders(orderId)
) ON [PRIMARY]
GO

