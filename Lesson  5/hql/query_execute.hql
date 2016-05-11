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


