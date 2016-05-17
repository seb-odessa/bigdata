set hive.execution.engine=mr;
SELECT * FROM users ORDER BY name;
SELECT COUNT(*), phone_number FROM phones GROUP BY phone_number;
SELECT phone_number, cnt FROM (SELECT phone_number, COUNT(*) AS cnt FROM phones GROUP BY phone_number) t2 WHERE t2.CNT > 1;
