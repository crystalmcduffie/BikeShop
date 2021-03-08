CREATE TABLE Registered_Users
(
	urname character varying(30),
    passwd character varying(30),
    rle integer,

    CONSTRAINT Registered_Users_pkey PRIMARY key (urname)
)

ALTER TABLE Registered_Users
    OWNER to postgres;

INSERT INTO Registered_Users(
	urname, passwd, rle)
	VALUES('available', 'pass', 3),
	('crystal', 'pass', 0),
	('tyler', 'pass', 1),
	('ashley', 'pass', 2),
	('clark', 'pass', 2);

CREATE TABLE Bikes
(
    bikeid serial,
    urname character varying(30),
    brand character varying(30),
    color character varying(30),
    wkpay decimal,
    CONSTRAINT Bikes_pkey PRIMARY KEY (bikeid),
    foreign key (urname) references registered_users (urname)
)

TABLESPACE pg_default;

ALTER TABLE public.Bikes
    OWNER to postgres;

insert into Bikes
	values
	(default, 'crystal', 'Mongoose', 'purple', 20.25),
	(default, 'tyler', 'Nishiki', 'blue', 10.25),
	(default, 'available', 'Schwinn', 'black', 0),
	(default, 'available', 'Nicec', 'grey', 0),
	(default, 'ashley', 'Mongoose', 'red', 30.75),
	(default, 'available', 'Nishiki', 'black', 0),
	(default, 'available', 'Schwinn', 'white', 0),
	(default, 'available', 'Nicec', 'brown', 0);

create table Offers
(
	bid decimal,
	urname character varying(30),
	bikeid integer,
	foreign key (urname) references registered_users (urname),
	foreign key (bikeid) references bikes (bikeid),
	constraint offers_pkey primary key (urname, bikeid)
)

TABLESPACE pg_default;

ALTER TABLE public.Offers
	OWNER to postgres;

insert into offers
	values
	(100.50, 'ashley', 12),
	(70.10, 'clark', 12);

create table payments
(
	bikeid integer unique,
	urname character varying(30),
	total decimal,
	paid decimal,
	remaining decimal,
	foreign key (bikeid) references bikes (bikeid),
	foreign key (urname) references registered_users (urname),
	constraint payments_pkey primary key (bikeid, urname)
)

TABLESPACE pg_default;

ALTER TABLE public.Payments
    OWNER to postgres;

insert into payments
	values
	(9, 'crystal', 243, 162, 81),
	(10, 'tyler', 123, 123, 0),
	(13, 'ashley', 369, 123, 246);
