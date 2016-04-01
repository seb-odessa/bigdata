# Big Data
This project for home works

#hdfs 
Contains tool for 
- uploading local files to the (remote) HDFS. 
- dounloading frm the HDFS to the local filesystem

Usage:

	hadoop jar hdfs.jar put source_file .
	hadoop jar hdfs.jar put source_file destination_file
	hadoop jar hdfs.jar put source_file /user/coudera/
	hadoop jar hdfs.jar put source_file /user/coudera/destination_file
	hadoop jar hdfs.jar put source_file hdfs://quickstart.cloudera:8020/user/cloudera/
	hadoop jar hdfs.jar put source_file hdfs://quickstart.cloudera:8020/user/cloudera/destination_file

	hadoop jar hdfs.jar get hdfs://localhost/users/coudera/source_file .
	hadoop jar hdfs.jar get hdfs://localhost/users/coudera/source_file destination_file
