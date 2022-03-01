# messages-rabbitmq
JMS Client samples for Messages for rabbit mq in IBM Cloud. 

IBM Cloud Messages for Rabbitmq: https://cloud.ibm.com/services/messages-for-rabbitmq/ 

Login to IBM Cloud. Under Catalog, search for "Messages for RabbitMQ". Create a service instance "Messages for RabbitMQ-as". 

Here is my Summary of my created Service instance:  

Region: Dallas  
Plan: Standard  
Service name: Messages for RabbitMQ-as  
Resource group: Default  
  
Service instance will be provisioned in few minutes. From the dashboard, navigate to Services - Messages for RabbitMQ-as. 

**RabbitMQ Admin Console**

The URL to launch RabbitMQ Admin console is found under Overview - Connections - HTTPS.

 ![Connection HTTPS ](images/RabbitMQ_AdminConsole_URL.png)


To Login to Admin console, you need a username and password. Set the password (minimum 10 characters) for admin user as shown in the below screen shot:

![Set Admin Password](images/Change_Admin_Password.png)

**Create new user in Rabbit MQ**

If you do not want to use admin user, then follow the steps for creating new user [here](https://cloud.ibm.com/docs/messages-for-rabbitmq?topic=messages-for-rabbitmq-connection-strings)  You can share this user with other applications. 

![New User](images/New_User.png)

Click Launch button in the Connections - HTTPS screen. In the Admin Console Launch screen, login with username/password as admin/new_password_set_above. 
  
![Admin Console](images/Admin_Console.png)

Now, let us find all RabbitMQ configuration. In the screen shot below, you can see that the SSL Protocol is enabled.  

![SSL enabled in Admin Console](images/AdminConsole_SSL.png)

So, we need to enable SSL protocol in connection factory. The code uses Ssl. 

```
factory.useSslProtocol();
```

In the Admin console, you can also find all created queues as shown below: 

![Queues in Admin Console](images/RabbitMQ_Queues.png)

You find the Java doc for Rabbit MQ [here](https://rabbitmq.github.io/rabbitmq-java-client/api/current/allclasses.html)  

Download all prerequisite jars for Rabbit MQ [here](https://jar-download.com/artifact-search/rabbitmq-jms). The files are downloaded under **/Users/malark/downloads** in my local mac system.    

I followed the examples from this website to code for [JMS Client](https://examples.javacodegeeks.com/enterprise-java/jms/jms-client-example/)

Open a terminal and set the CLASSPATH with downloaded prerequisite jars as shown below:

> (base) Malars-MacBook-Pro-2:rabbit-test malark$ export CLASSPATH=/Users/malark/downloads/slf4j-api-1.7.30.jar:/Users/malark/downloads/geronimo-jms_1.1_spec-1.1.1.jar:/Users/malark/downloads/rabbitmq-jms-2.2.0.jar:/Users/malark/downloads/amqp-client-5.10.0.jar

My java version is java 11.

> (base) Malars-MacBook-Pro-2:rabbit-test malark$ java -version  
java version "11.0.9" 2020-10-20 LTS  
Java(TM) SE Runtime Environment 18.9 (build 11.0.9+7-LTS)  
Java HotSpot(TM) 64-Bit Server VM 18.9 (build 11.0.9+7-LTS, mixed mode)  

You can learn more about RabbitMQ and the different messaging styles [here](https://www.rabbitmq.com/getstarted.html).

### Declare the connection properties in connection.properties 

We will first obtain a connection factory, which we will then use to create a connection. 

Modify the connection.properties file, and set the username and password from the Messages for RabbitMQ service - Connections - Service Credentials screen (I am using a different user than admin. You can also use admin user here). Set the Virtual host as "/". Set the hostname and port from Messages for RabbitMQ service - Connections - AMQPS screen as shown below.

![RabbitMQ AMQPS](images/RabbitMQ_AMQPS.png)

In Connection Factory, we use the connection properties by reading the properties file. Also, enable the SSL Protocol.    

Below is a sample connection.properties (value should be without quotes)   

```
username=admin
password=adminis...
hostname=.....databases.appdomain.cloud
port=32683
virtualhost=/
```

### JMS Client for Point to Point messaging style

The code **JMSProducer.java** creates a connection factory, create a new connection and session, create message producers which we will then use to send messages. It creates a queue called **jmsqueue**. It sends the message to the created queue.  	

Compile and Run the Program as shown below:

> Malars-MacBook-Pro-2:rabbit-test malark$ **javac JMSProducer.java**

> Malars-MacBook-Pro-2:rabbit-test malark$ ** java JMSProducer.java**  
SLF4J: Failed to load class "org.slf4j.impl.StaticLoggerBinder".  
SLF4J: Defaulting to no-operation (NOP) logger implementation  
SLF4J: See http://www.slf4j.org/codes.html#StaticLoggerBinder for further details.  
Created Connection Factory  
Created Connection  
Sending text 'Task0'  
Sending text 'Task1'  
Sending text 'Task2'  
Sending text 'Task3'  
Sending text 'Task4'  
Sending text 'Task5'    
Sending text 'Task6'  
Sending text 'Task7'  
Sending text 'Task8'  
Sending text 'Task9'  

The code **JMSAsyncReceiveQueueClient.java** creates a connection factory, create a new connection and session, create message consumers which we will then use to receive messages from the created queue **jmsqueue**. It consumes messages asynchronously. It uses a message listener in order to consume messages asynchronously.
In order to make sure the asynchronous consumer doesn’t run indefinitely, it calls countDown() on latch when the message received is ‘END’.

Compile and Run the Program as shown below:

> Malars-MacBook-Pro-2:rabbit-test malark$ **javac JMSAsyncReceiveQueueClient.java**  

> (base) Malars-MacBook-Pro-2:rabbit-test malark$ **java JMSAsyncReceiveQueueClient.java**  
SLF4J: Failed to load class "org.slf4j.impl.StaticLoggerBinder".  
SLF4J: Defaulting to no-operation (NOP) logger implementation   
SLF4J: See http://www.slf4j.org/codes.html#StaticLoggerBinder for further details.  
Created Connection Factory  
Created Connection  
Customer received Task0  
Customer received Task1  
Customer received Task2  
Customer received Task3  
Customer received Task4    
Customer received Task5  
Customer received Task6  
Customer received Task7  
Customer received Task8  
Customer received Task9    
Customer received END  


### JMS Client for Publish/Subscribe messaging style

Let’s now look into a client example that uses publish−and−subscribe message style. 

When a publisher sends a message, there may be more than one customer interested in such messages. Publisher broadcasts the message to JMS destination called ‘topic’. There may be more than one consumer subscribed to the topic. All the active clients subscribed to the topic will receive message and there is no need for the subscriber to poll for the messages. Every active subscriber receives its own copy of each message published to the topic. In this example, we will look into durable subscriber.

The createDurableSubscriber() method takes two parameters: a topic name, and a subscription name. A durable subscription’s uniqueness is defined by the client ID and the subscription name.

The code **JMSPublisherClient.java** creates a connection factory, create a new connection and session, create Topic which we will then use to publish messages. It creates a topic called **customerTopic**. It publishes messages to the created topic as shown below:  

> (base) Malars-MacBook-Pro-2:rabbit-test malark$ echo $CLASSPATH  
/Users/malark/downloads/slf4j-api-1.7.30.jar:/Users/malark/downloads/geronimo-jms_1.1_spec-1.1.1.jar:/Users/malark/downloads/rabbitmq-jms-2.2.0.jar:/Users/malark/downloads/amqp-client-5.10.0.jar  
> (base) Malars-MacBook-Pro-2:rabbit-test malark$ javac JMSPublisherClient.java    
(base) Malars-MacBook-Pro-2:rabbit-test malark$ java JMSPublisherClient.java  
SLF4J: Failed to load class "org.slf4j.impl.StaticLoggerBinder".   
SLF4J: Defaulting to no-operation (NOP) logger implementation   
SLF4J: See http://www.slf4j.org/codes.html#StaticLoggerBinder for further details.   
Created Connection Factory   
Created Connection   
Sending text 'Task'    

The code **JMSSubscriberClient.java** creates a connection factory, create a new connection and session. It also creates the subscribers Consumer1 and Consumer2 who are subscribed to the Publisher. They will receive a copy of the published message as shown below:  

> (base) Malars-MacBook-Pro-2:rabbit-test malark$ javac JMSSubscriberClient.java  
> (base) Malars-MacBook-Pro-2:rabbit-test malark$ java JMSSubscriberClient.java    
SLF4J: Failed to load class "org.slf4j.impl.StaticLoggerBinder".   
SLF4J: Defaulting to no-operation (NOP) logger implementation   
SLF4J: See http://www.slf4j.org/codes.html#StaticLoggerBinder for further details.   
Created Connection Factory   
Created Connection   
Consumer1 receives Task   
Consumer2 receives Task   

> **NOTE:** Run the program **JMSPublisherClient.java**  and then run the program  **JMSSubscriberClient.java**. Otherwise,  **JMSSubscriberClient.java** will indefinitely wait for messages from topic.

You can find detailed explanation with more examples [here](https://examples.javacodegeeks.com/enterprise-java/jms/jms-client-example/)



