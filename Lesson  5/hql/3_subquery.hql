EXPLAIN SELECT phone_number, cnt FROM (SELECT phone_number, COUNT(*) AS cnt FROM phones GROUP BY phone_number) t2 WHERE t2.CNT > 1;
