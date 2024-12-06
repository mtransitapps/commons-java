- get route trips headsigns (directions) for route ID 300


```sql
SELECT route_ids.route_id, trip.direction_id, trip.trip_headsign
FROM trip
JOIN route_ids ON trip.route_id_int IN (SELECT route_id_int FROM route WHERE route_id IN ("300"))
GROUP BY route_ids.route_id, trip.direction_id
```