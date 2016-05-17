##Lesson 2. HDFS
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

##Lesson 3. MapReduce
Hadoop environment:
```
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
```
Payments processing:
```
	$ hdfs dfs -put Lessons/bigdata-session-3/datasets/payments.log /user/seb
	$ hdfs dfs -ls /user/seb
	Found 1 items
	-rw-r--r--   3 seb supergroup        436 2016-04-09 12:28 /user/seb/payments.log
	$ hadoop jar jar/payments.jar /user/seb/payments.log out
	...
	$ hdfs dfs -cat /user/seb/out/*
	{ "id":1, "total":106.72, "stores":["www.store1.com", "www.store3.com"] }
	{ "id":12, "total":185.66, "stores":["www.store2.com", "www.store4.com"] }
	{ "id":1123, "total":16.50, "stores":["www.store1.com"] }
```
##Lesson 4. KeyValue storage (Voldemort)
Hadoop environment:

## Links and tips
- [Hadoop cluster in LXC](https://ofirm.wordpress.com/2014/01/05/creating-a-virtualized-fully-distributed-hadoop-cluster-using-linux-containers/)
- [Building Voldemort read-only stores with Hadoop](http://blog.intelligencecomputing.io/cloud/487/repostbuilding-voldemort-read-only-stores-with-hadoop)



##Lesson 5. Apache Hive
- Create several tables on HIVE
- Create several queries with GROUP BYs, ORDER BYs, JOINs, Subqueries
- Visualize execution plan with MapReduce
- Configure HIVE to work on TEZ
- Visualize execution plan with TEZ
- Compare execution plans. Write conclusions

Apache Hive Installation:

https://cwiki.apache.org/confluence/display/Hive/GettingStarted#GettingStarted-InstallingHivefromaStableRelease

Apache Tez installation:

https://github.com/apache/incubator-tez/blob/branch-0.2.0/INSTALL.txt

#### Several tables in Hive
```
	$ cat hql/create_tables.hql
```
```
	DROP TABLE IF EXISTS users  PURGE;
	DROP TABLE IF EXISTS phones PURGE;
	DROP TABLE IF EXISTS rooms  PURGE;
	CREATE TABLE users  (id int, name string);
	CREATE TABLE phones (id int, user_id int, phone_number int);
	CREATE TABLE rooms  (id int, phone_id int, room_number int);
	SHOW TABLES;
	LOAD DATA INPATH '/tmp/users'  OVERWRITE INTO TABLE users;
	LOAD DATA INPATH '/tmp/phones' OVERWRITE INTO TABLE phones;
	LOAD DATA INPATH '/tmp/rooms'  OVERWRITE INTO TABLE rooms;
	SELECT * FROM users;
	SELECT * FROM phones;
	SELECT * FROM rooms;
```
```
	$ hive -f hql/create_tables.hql
```
```
	...
	OK
	1	Foo
	2	Bar
	3	Baz
	4	Qux
	Time taken: 0.189 seconds, Fetched: 4 row(s)
	OK
	1	2	200
	2	4	200
	3	3	202
	4	1	200
	Time taken: 0.074 seconds, Fetched: 4 row(s)
	OK
	1	4	30
	2	1	32
	3	3	35
	4	2	50
	Time taken: 0.065 seconds, Fetched: 4 row(s)
```
#### Query with GROUP BYs, ORDER BYs, JOINs, Subqueries
```
	SELECT 
	    users.id, 
	    users.name, 
	    phones.phone_number, 
	    rooms.room_number,
	    oc.cnt
	FROM 
	    users 
	    JOIN phones ON users.id = phones.user_id 
	    JOIN rooms ON phones.id = rooms.phone_id
	    JOIN (SELECT phone_number, COUNT(*) AS cnt FROM phones GROUP BY phone_number) oc ON oc.phone_number = phones.phone_number
	ORDER BY 
	    users.name;
```
##### Query execution time with Apache Tez:
```
	OK
	2	Bar	200	32	3
	3	Baz	202	35	1
	1	Foo	200	30	3
	4	Qux	200	50	3
	Time taken: 13.742 seconds, Fetched: 4 row(s)
```
##### Query execution time with Map/Reduce:
```
	OK
	2	Bar	200	32	3
	3	Baz	202	35	1
	1	Foo	200	30	3
	4	Qux	200	50	3
	Time taken: 62.586 seconds, Fetched: 4 row(s)
```
