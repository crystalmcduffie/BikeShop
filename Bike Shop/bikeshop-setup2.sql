CREATE TABLE Registered_Users
(
	urname character varying(30),
    passwd character varying(30),
    urid serial,
    rle integer,

    CONSTRAINT Registered_Users_pkey PRIMARY key (urid)
)

ALTER TABLE Registered_Users
    OWNER to postgres;

INSERT INTO Registered_Users(
	urid, rle, urname, passwd)
	VALUES(defualt, 0, 'crystal', 'pass'),
	(default, 1, 'tyler', 'pass'),
	(default, 2, 'ashley', 'pass'),
	(default, 2, 'clark', 'pass');

-- Table: public.Bikes

CREATE TABLE Bikes
(
    bikeid serial,
    urid integer,
    brand character varying(30),
    color character varying(30),
    wkpay decimal,
    CONSTRAINT Bikes_pkey PRIMARY KEY (bikeid),
    foreign key (urid) references registered_users (urid)
)

TABLESPACE pg_default;

ALTER TABLE public.Bikes
    OWNER to postgres;

insert into Bikes
	values
	(default, 1, 'Mongoose', 'purple', 20.25),
	(default, 2, 'Nishiki', 'blue', 10.25),
	(default, 0, 'Schwinn', 'black', 0),
	(default, 0, 'Nicec', 'grey', 0),
	(default, 3, 'Mongoose', 'red', 30.75),
	(default, 0, 'Nishiki', 'black', 0),
	(default, 0, 'Schwinn', 'white', 0),
	(default, 0, 'Nicec', 'brown', 0);

create table Offers
(
	bid decimal,
	urid integer,
	bikeid integer,
	foreign key (urid) references registered_users (urid),
	foreign key (bikeid) references bikes (bikeid),
	constraint offers_pkey primary key (urid, bikeid)
)

TABLESPACE pg_default;

ALTER TABLE public.Offers
	OWNER to postgres;

insert into offers
	values
	(100.50, 3, 9),
	(70.10, 4, 9);

create table payments
(
	bikeid integer unique,
	urid integer,
	total decimal,
	paid decimal,
	remaining decimal,
	foreign key (bikeid) references bikes (bikeid),
	foreign key (urid) references registered_users (urid),
	constraint payments_pkey primary key (bikeid, urid)
)

TABLESPACE pg_default;

ALTER TABLE public.Payments
    OWNER to postgres;

insert into payments
	values
	(1, 1, 243, 162, 81),
	(8, 2, 123, 123, 0),
	(11, 3, 369, 123, 246);
