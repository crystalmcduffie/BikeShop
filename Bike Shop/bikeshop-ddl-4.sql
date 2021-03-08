CREATE TABLE Status 
(
	id serial,
	name character varying(30) unique not null,

	Constraint Status_pkey primary key (id)
)

ALTER TABLE Status
    OWNER to postgres;

CREATE TABLE user_role
(
	id serial,
	name character varying(30) unique not null

	CONSTRAINT user_role_pkey PRIMARY key (id)
)

ALTER TABLE user_role
    OWNER to postgres;

CREATE TABLE Registered_Users
(
	id serial ,
	name character varying(30) unique,
    passwd character varying(30),
    rle integer,

    CONSTRAINT Registered_Users_pkey PRIMARY key (id),
	foreign key (rle) references user_role (id)
)

ALTER TABLE Registered_Users
    OWNER to postgres;

CREATE TABLE Bikes
(
    id serial,
    model character varying(30),
    brand character varying(30),
    color character varying(30),
    status_id integer,

    CONSTRAINT Bikes_pkey PRIMARY KEY (id),
    foreign key (status_id) references Status (id)
)

TABLESPACE pg_default;

ALTER TABLE public.Bikes
    OWNER to postgres;

create table Offers
(
	id serial,
	bid decimal,
	constraint offers_pkey primary key (id)
)

TABLESPACE pg_default;

ALTER TABLE public.Offers
	OWNER to postgres;

create table offers_log
(
	id serial,
	user_id integer,
	bike_id integer,
	foreign key (id) references offers (id),
	foreign key (user_id) references registered_users (id),
	foreign key (bike_id) references bikes (id)
)

TABLESPACE pg_default;

ALTER TABLE public.offers_log
    OWNER to postgres;

create table payments
(
	id serial,
	total decimal,
	paid decimal,
	constraint payments_pkey primary key (id)
)

TABLESPACE pg_default;

ALTER TABLE public.Payments
    OWNER to postgres;

create table payments_log
(
	id serial,
	user_id integer,
	bike_id integer,
	foreign key (id) references payments (id),
	foreign key (user_id) references registered_users (id),
	foreign key (bike_id) references bikes (id)
)

TABLESPACE pg_default;

ALTER TABLE public.payments_log
    OWNER to postgres;

