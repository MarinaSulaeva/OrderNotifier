# *Микросервисы по отправке заказов*

## созданы два приложения:
- Первое приложение — order-service. 
- Второе приложение — order-status-service 

## Order-service
Эндпоинт принимает заказ и отправляет в Kafka событие OrderEvent. Событие отправляется в топик order-topic.
Ещё это приложение имеет KafkaListener, который будет слушать события по топику order-status-topic. 
Этот слушатель выводит в консоль информацию о событии в следующем формате: 
log.info("Received message: {}", message); 
log.info("Key: {}; Partition: {}; Topic: {}, Timestamp: {}", key, partition, topic, timestamp);

## Order-status-service:
Приложение состоит из KafkaListener, который слушает топик order-topic. 
Когда в слушатель приходит событие, происходит отправка другого события в топик order-status-service. 
В поле status записывается статус заказа в зависимости от количества повторов, в поле date — текущая дата.
