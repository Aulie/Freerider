--Used to verify an acceptance notification corresponds to an existing request notification.
CREATE OR REPLACE FUNCTION notifications_exist(sender_id character, recipient_id character, journey_serial integer, desired_type character)
  RETURNS boolean AS
	$BODY$
	SELECT EXISTS (
	SELECT 1 
	FROM notifications 
	WHERE 
	sender=$1 AND
	recipient=$2 AND 
	concerning_journey=$3 AND 
	type=$4::notification_type
	)
	$BODY$
LANGUAGE sql VOLATILE
COST 100;

ALTER FUNCTION notifications_exist(character, character, integer, character)
  OWNER TO social_hitchhiking;

--Used to peform searches.
CREATE OR REPLACE FUNCTION search_for_journeys(IN startpoint geography, IN endpoint geography, IN searcher character, IN starttime timestamp without time zone)
  RETURNS TABLE(starttime timestamp without time zone, journeyserial integer, visibility visibility, route geometry, routeserial integer, addresses character varying, maplocations geometry, name character varying, surname character varying, rating double precision, id character) AS
$BODY$
SELECT
journeys.starttime,
journeys.serial AS journeyserial,
journeys.visibility,
routes.route::geometry AS route,
routes.serial AS routeserial,
routes.addresses,
routes.maplocations::geometry,
users.name,
users.surname,
users.rating,
users.id
FROM
public.journeys INNER JOIN
public.routes ON journeys.route_used=routes.serial INNER JOIN
public.users ON routes.owner=users.id
WHERE 
((ST_Distance($1,routes.route) + ST_Distance($2,routes.route)) <= 5000) AND
journeys.hitchhiker IS NULL  AND
routes.owner <> $3 AND
(journeys.starttime - $4) <= interval'1d' AND
(journeys.starttime - $4) >= interval'-1d' AND
journeys.starttime >= (current_timestamp - interval'1h')
ORDER BY journeys.starttime
LIMIT 20
$BODY$
  LANGUAGE sql VOLATILE
  COST 100
  ROWS 20;
ALTER FUNCTION search_for_journeys(geography, geography, character, timestamp without time zone)
  OWNER TO social_hitchhiking;


--Call daily to age finished Journeys and remove no longer used ad-hoc routes.
CREATE OR REPLACE FUNCTION cleanup_journeys_and_routes()
  RETURNS void AS
$BODY$
DELETE FROM journeys 
WHERE starttime < (current_timestamp - interval'14d')
;
DELETE FROM routes
WHERE ad_hoc AND
NOT EXISTS (SELECT 1 FROM journeys WHERE route_used=routes.serial)
;
$BODY$
  LANGUAGE sql VOLATILE
  COST 100;
ALTER FUNCTION cleanup_journeys_and_routes()
  OWNER TO social_hitchhiking;
