Tengo:
- un agente en Azure AI Foundry que usa gpt-4o

- un API REST Backend expuesto en https://backend.springonly.com

- este API REST expone un endpoint para crear solicitudes en POST https://backend.springonly.com/orders que recibe el siguiente request:

{
    "order": {
        "businessId": "IDNEGOCIO",
        "businessName": "NOMBRE DEL NEGOCIO"
    },
    "credit": {
        "amount": 55600.30,
        "interestRate": 10.5,
        "dueDate": 2022-03-10
    },
    "author": "NOMBRE DEL AUTOR"
}

en caso de éxito el endpoint retorna un estado 201 y este response:
{
    "orderId": 101
}

en caso contrario el endpoint retorna un estado 400
 
Quiero que el Agente de Azure AI Foundry invoque esta API REST para crear solicitudes cuando reciba un prompt que le pida esto

Deseo que la conexión entre el Agente y el API REST se haga mediante el protocolo OpenAI Plugin, de modo que, concéntrate en ayudarme solo con esta configuración