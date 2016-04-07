# Big Data
This project for home works

#Lesson 2. HDFS Java API
Implement tool for:
- upload local file to the HDFS.
- dounload from the HDFS to the local filesystem

Usage:

	hadoop jar hdfs.jar put source_file .
	hadoop jar hdfs.jar put source_file destination_file
	hadoop jar hdfs.jar put source_file /user/coudera/
	hadoop jar hdfs.jar put source_file /user/coudera/destination_file
	hadoop jar hdfs.jar put source_file hdfs://quickstart.cloudera:8020/user/cloudera/
	hadoop jar hdfs.jar put source_file hdfs://quickstart.cloudera:8020/user/cloudera/destination_file

	hadoop jar hdfs.jar get hdfs://localhost/users/coudera/source_file .
	hadoop jar hdfs.jar get hdfs://localhost/users/coudera/source_file destination_file
