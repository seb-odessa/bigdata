#Lesson 2. HDFS
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

#Lesson 3. MapReduce
Hadoop environment:

	$ cat jps_report 
	#!/bin/sh

	SLAVES=$HADOOP_CONF_DIR/slaves
	for host in $(hostname && cat ${SLAVES}); do
    		echo $host:
    		ssh $host jps
    		echo =======================
	done

	$ ./jps_report
	vm0:
	5112 Jps
	3987 NameNode
	4205 SecondaryNameNode
	4484 ResourceManager
	=======================
	vm1:
	1972 JobHistoryServer
	2151 Jps
	1449 DataNode
	1607 NodeManager
	=======================
	vm2:
	608 DataNode
	760 NodeManager
	1163 Jps
	=======================
	vm3:
	507 DataNode
	1062 Jps
	659 NodeManager
	=======================
	vm4:
	1389 Jps
	715 DataNode
	983 NodeManager
	=======================

Payment processing:

	$ hdfs dfs -put Lessons/bigdata-session-3/datasets/payments.log /user/seb
	$ hdfs dfs -ls /user/seb
	Found 1 items
	-rw-r--r--   3 seb supergroup        436 2016-04-09 12:28 /user/seb/payments.log
	$ hadoop jar jar/payments.jar /user/seb/payments.log out
	$ hdfs dfs -cat /user/seb/out/*
	{ "id":1, "total":106.72, "stores":["www.store1.com", "www.store3.com"] }
	{ "id":12, "total":185.66, "stores":["www.store2.com", "www.store4.com"] }
	{ "id":1123, "total":16.50, "stores":["www.store1.com"] }

# Links and tips

-Hadoop cluster in LXC:

	https://ofirm.wordpress.com/2014/01/05/creating-a-virtualized-fully-distributed-hadoop-cluster-using-linux-containers/
	





