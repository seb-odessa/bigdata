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
- [Hadoop cluster in LXC](https://ofirm.wordpress.com/2014/01/05/creating-a-virtualized-fully-distributed-hadoop-cluster-using-linux-containers/)
- [Building Voldemort read-only stores with Hadoop](http://blog.intelligencecomputing.io/cloud/487/repostbuilding-voldemort-read-only-stores-with-hadoop)
- [Fix securite (kerberos) issue](https://github.com/voldemort/voldemort/issues/278)

Script for preparation the dataset for Voldemort (collect facebook friends to avro file):
```
#!/bin/sh

RUN=../tools/exec
HDFS_ROOT=/user/$(whoami)
DATASET=$HDFS_ROOT/dataset
INPUT=facebook_combined.txt
OUTPUT=facebook-network.avro
RESULTS=$HDFS_ROOT/tmp

WD="$(pwd)"
cd ../
JARS=$(pwd)/jars
cd "$WD"


$RUN hdfs dfs -test -d $HDFS_ROOT || $RUN hdfs dfs -mkdir $HDFS_ROOT || exit
$RUN hdfs dfs -test -d $DATASET || $RUN hdfs dfs -mkdir $DATASET || exit
$RUN hdfs dfs -test -d $RESULTS && $RUN hdfs dfs -rm -r $RESULTS
$RUN hdfs dfs -test -f $DATASET/$INPUT || $RUN hdfs dfs -put dataset/$INPUT $DATASET || exit
$RUN hdfs dfs -test -f $DATASET/$OUTPUT && $RUN hdfs dfs -rm dataset/$OUTPUT  || exit

export LIBJARS=$JARS/avro-1.7.4.jar,$JARS/avro-mapred-1.7.4-hadoop2.jar
export HADOOP_CLASSPATH=$(echo ${LIBJARS} | sed s/,/:/g)
ant jar
$RUN yarn jar jar/facebook.jar avro $DATASET/$INPUT $RESULTS -libjars $LIBJARS
$RUN hdfs dfs -mv $RESULTS/part-r-00000.avro $DATASET/$OUTPUT
$RUN hdfs dfs -rm -r $RESULTS/
```

Deploy config and run Voldemort cluster:
```
#!/bin/sh
RUN=../tools/exec
for id in 0 1 2; do
    host=vm${id}
    echo ${host}:
    $RUN ssh hduser@$host "mkdir -p ~/workspace"
    $RUN rsync -vr --delete voldemort/facebook_friends hduser@$host:~/workspace/
    $RUN ssh hduser@$host "nohup ~/workspace/voldemort/bin/voldemort-server.sh ~/workspace/facebook_friends/${id} > ~/workspace/${id}.log &"
done
$RUN rsync -v voldemort/read-only-bnp.cfg hduser@$host:~/workspace/
```
Loading data to the Voldemort cluster:

```
hduser@vm0:~/workspace$ ./voldemort/bin/run-bnp.sh read-only-bnp.cfg
    ...
16/05/29 11:27:33 INFO mr.HadoopStoreBuilder: Data size = 439463, replication factor = 2, numNodes = 3, numPartitions = 6, chunk size = 1073741824
16/05/29 11:27:33 INFO mr.HadoopStoreBuilder: Number of chunks: 1, number of reducers: 6, save keys: true, reducerPerBucket: true, buildPrimaryReplicasOnly: true
16/05/29 11:27:33 INFO mr.HadoopStoreBuilder: Building store...
    ...
BnP run script finished!
```
Check loaded data:
```
hduser@vm0:~/workspace$ ./voldemort/bin/voldemort-shell.sh facebook_friends tcp://vm0:6666/
    ...
Established connection to facebook_friends via [tcp://vm0:6666/]
> get "1"
version() ts:1463936934020: 48 73 88 92 119 126 133 194 54 236 280 53 299 315 322 346
> get "2"
version() ts:1463936936238: 20 115 116 149 226 312 326 333 343
> get "7"
version() ts:1463936938115: 291 22 31 38 65 87 103 129 136 168 213 246 304 308 315 322 339 340 347
```

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

The Apache Hive is much more effective when used over Apache Tez instead of regular Map/Reduce.

##Lesson 7. Column Oriented Data Store

- Create Cassandra cluster at least with 2 nodes(e.g. using Docker)
- Integrate Cassandra with Lucene index
- Provide possibility to search by wildcards
- Acceptance criteria:
- Dockerfile or something that expose how to create the cluster
- CQL: Initial schema and insert requests
- Examples of requests with wildcards
