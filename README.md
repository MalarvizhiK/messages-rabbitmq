# messages-rabbitmq
Java Client for Messages for rabbit mq in IBM Cloud

Login to IBM Cloud. Under Catalog, search for "Messages for RabbitMQ". Create a service instance "Messages for RabbitMQ-as". 

Here is my Summary of my created Service instance:  

Region: Dallas  
Plan: Standard  
Service name: Messages for RabbitMQ-rb  
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

Click Launch button in the Connections - HTTPS screen. In the Admin Console Launch screen, login with username/password as admin/<new password set above>. 
  
![Admin Console](images/Admin_Console.png)

Now, let us find all RabbitMQ configuration. In the screen shot below, you can see that the SSL Protocol is enabled.  

![SSL enabled in Admin Console](images/AdminConsole_SSL.png)

So, we need to enable SSL protocol in connection factory. 

In the Admin console, you can also find all created queues as shown below: 

![Queues in Admin Console](images/RabbitMQ_Queues.png)


Java doc for Rabbit MQ: https://rabbitmq.github.io/rabbitmq-java-client/api/current/allclasses.html

IBM Cloud Messages for Rabbitmq: https://cloud.ibm.com/services/messages-for-rabbitmq/ 




