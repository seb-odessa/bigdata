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

	$ hdfs dfsadmin -printTopology
	16/04/09 12:21:09 WARN util.NativeCodeLoader: Unable to load native-hadoop library for your platform... using builtin-java classes where applicable
	Rack: /default-rack
	   10.0.3.30:50010 (vm0)
	   10.0.3.31:50010 (vm1)
	   10.0.3.32:50010 (vm2)

	$ hdfs dfs -put Lessons/bigdata-session-3/datasets/payments.log /user/seb
	$ hdfs dfs -put Lessons/bigdata-session-3/datasets/facebook_combined.txt /user/seb
	$ hdfs dfs -ls /user/seb
	Found 2 items
	-rw-r--r--   3 seb supergroup     854362 2016-04-09 12:29 /user/seb/facebook_combined.txt
	-rw-r--r--   3 seb supergroup        436 2016-04-09 12:28 /user/seb/payments.log
	$ hdfs dfs -cat /user/seb/payments.log
	2014-07-02 20:52:39 1 12.01 www.store1.com
	2014-07-02 20:52:39 1123 1.75 www.store1.com
	2014-07-02 20:52:39 12 4.05 www.store2.com
	2014-07-02 20:52:39 1 7.87 www.store1.com
	2014-07-02 20:52:40 12 124.67 www.store2.com
	2014-07-02 20:52:40 1 9.14 www.store3.com
	2014-07-02 20:52:40 1123 14.75 www.store1.com
	2014-07-02 20:52:40 12 54.95 www.store2.com
	2014-07-02 20:52:40 1 77.70 www.store3.com
	2014-07-02 20:52:40 12 1.99 www.store4.com

    $ hadoop jar jar/payments.jar /user/seb/payments.log out
    $ hdfs dfs -cat /user/seb/out/*
    { "id":1, "total":106.72, "stores":["www.store1.com", "www.store3.com"] }
    { "id":12, "total":185.66, "stores":["www.store2.com", "www.store4.com"] }
    { "id":1123, "total":16.50, "stores":["www.store1.com"] }

# Links and tips

-Hadoop cluster in LXC:

	https://ofirm.wordpress.com/2014/01/05/creating-a-virtualized-fully-distributed-hadoop-cluster-using-linux-containers/
	





