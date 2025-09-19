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

Deseo que la conexión entre el Agente y el API REST se haga mediante el protocolo MCP, de modo que, concéntrate en ayudarme solo con el componente MCP Server.

Dame las instrucciones paso a paso para construir un MCP Server en Python de modo que exponga dicho endpoint. Hazlo lo más simpe posible y que me sirva para luego agregarle más endpoints. La idea es exponer el endpoint tal como está en el backend original, no quiero agregar lógica adicional por ahora.

Indícame paso a paso cómo puedo probar localmente dicho MCP Server. No tengo docker instalado en mi laptop personal. Dame un archivo a la vez, una vez que termine con ese archivo y te haga las preguntas o correcciones del caso, te pediré el siguiente archivo a implementar por lo tanto debes recordar este pedido como las correcciones que hayas hecho mientras interactuamos para desarrollar esta solución.

También dame el archivo Dockerfile para preparar una imagen que desplegaré luego en Azure Container Apps.
