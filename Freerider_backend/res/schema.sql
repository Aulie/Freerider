-- This file was created for group 4's project for IT2901 in the spring of 2012.
-- It is an SQL script laying out the schema of the project's relational database.
-- It assumes that PostGIS has already been installed in the database using the appropriate installation scripts,
-- and that the postgres user social_hitchhiking has been created.

CREATE TABLE users
(
  name character varying(30),
  surname character varying(30),
  ip inet,
  rating double precision,
  id character(15) NOT NULL,
  access_token character varying(100),
  CONSTRAINT user_primary_key PRIMARY KEY (id )
)
WITH (
  OIDS=FALSE
);
ALTER TABLE users
  OWNER TO social_hitchhiking;




CREATE SEQUENCE ad_hoc_routes
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 14
  CACHE 1;
ALTER TABLE ad_hoc_routes
  OWNER TO social_hitchhiking;


CREATE TABLE routes
(
  name character varying(40) NOT NULL,
  route geography(LineString,4326),
  owner character(15) NOT NULL,
  serial serial NOT NULL,
  maplocations geography(LineString,4326),
  addresses character varying(500),
  ad_hoc boolean NOT NULL DEFAULT false,
  CONSTRAINT route_pk PRIMARY KEY (serial ),
  CONSTRAINT route_owner_fk FOREIGN KEY (owner)
      REFERENCES users (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT owner_name_unique UNIQUE (name , owner )
)
WITH (
  OIDS=FALSE
);
ALTER TABLE routes
  OWNER TO social_hitchhiking;

CREATE INDEX route_data_idx
  ON routes
  USING gist
  (route );



CREATE TYPE visibility AS ENUM('FRIENDS','FRIENDS_OF_FRIENDS','PUBLIC');  
  
CREATE TABLE journeys
(
  route_used integer,
  serial serial NOT NULL,
  starttime timestamp without time zone,
  hitchhiker character(15),
  visibility visibility NOT NULL DEFAULT 'FRIENDS'::visibility,
  CONSTRAINT journey_pk PRIMARY KEY (serial ),
  CONSTRAINT hiker_fk FOREIGN KEY (hitchhiker)
      REFERENCES users (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE SET NULL,
  CONSTRAINT route_used_fk FOREIGN KEY (route_used)
      REFERENCES routes (serial) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
  CONSTRAINT duplicate_journeys UNIQUE (route_used , starttime )
)
WITH (
  OIDS=FALSE
);
ALTER TABLE journeys
  OWNER TO social_hitchhiking;

CREATE TYPE notification_type AS Enum('HITCHHIKER_REQUEST','REQUEST_ACCEPT','REQUEST_REJECT','HITCHHIKER_CANCEL','DRIVER_CANCEL','HITCHHIKER_ACCEPTS_DRIVER_CANCEL');  

 CREATE TABLE notifications
(
  serial serial,
  time_sent timestamp with time zone NOT NULL DEFAULT now(),
  recipient character(15) NOT NULL,
  sender character(15) NOT NULL,
  concerning_journey integer NOT NULL,
  type notification_type NOT NULL,
  CONSTRAINT notification_pk PRIMARY KEY (serial ),
  CONSTRAINT notification_jrn_fk FOREIGN KEY (concerning_journey)
      REFERENCES journeys (serial) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT notification_rcpt_fk FOREIGN KEY (recipient)
      REFERENCES users (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT notification_sndr_fk FOREIGN KEY (sender)
      REFERENCES users (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT no_duplicate_notifications UNIQUE (recipient , sender , concerning_journey , type ),
  CONSTRAINT no_self_notifications CHECK (recipient <> sender)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE notifications
  OWNER TO social_hitchhiking;
