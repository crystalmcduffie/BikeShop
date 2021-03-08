CREATE TABLE public."Registered_Users"
(
    "User_Id" integer NOT NULL,
    "Username" character varying(20) COLLATE pg_catalog."default",
    "Password" character varying(20) COLLATE pg_catalog."default",
    "Role" integer,
    CONSTRAINT "Registered_Users_pkey" PRIMARY KEY ("User_Id")
)

TABLESPACE pg_default;

ALTER TABLE public."Registered_Users"
    OWNER to postgres;

INSERT INTO public."Registered_Users"(
	"User_Id", "Username", "Password", "Role")
	VALUES(1, 'crystal', 'pass', 0),
	(2, 'tyler', 'pass', 1),
	(3, 'ashley', 'pass', 2),
	(4, 'clark', 'pass', 2);

-- Table: public.Bikes

CREATE TABLE public."Bikes"
(
    "Bike_Model" integer NOT NULL,
    "Brand" character varying COLLATE pg_catalog."default",
    "Color" character varying COLLATE pg_catalog."default",
    "User_Id" integer,
    "Weekly_Payment" double precision,
    CONSTRAINT "Bikes_pkey" PRIMARY KEY ("Bike_Model")
)

TABLESPACE pg_default;

ALTER TABLE public."Bikes"
    OWNER to postgres;

INSERT INTO public."Bikes"(
	"Bike_Model", "Brand", "Color", "User_Id", "Weekly_Payment")
	VALUES(0000, 'Mongoose', 'purple', 1, 20.25),
	(0001, 'Nishiki', 'blue', 2, 10.25),
	(0010, 'Schwinn', 'black', 0, 0),
	(0011, 'Nicec', 'grey', 0, 0),
	(0100, 'Mongoose', 'red', 3, 30.75),
	(0101, 'Nishiki', 'black', 0, 0),
	(0110, 'Schwinn', 'white', 0, 0);

CREATE TABLE public."Offers"
(
	"User_Id" integer NOT NULL,
	"Bike_Model" integer NOT NULL,
	"Bid" double precision
)

TABLESPACE pg_default;

ALTER TABLE public."Offers"
	OWNER to postgres;

INSERT INTO public."Offers"(
	"User_Id", "Bike_Model", "Bid")
	VALUES (3, 0010, 100.50),
	(4, 0010, 70.10);

CREATE TABLE public."Payments"
(
	"User_Id" integer NOT NULL,
	"Bike_Model" integer NOT NULL,
	"Remaining" double precision,
	"Paid" double precision,
	"Total_Cost" double precision,
	CONSTRAINT "Payments_pkey" PRIMARY KEY ("Bike_Model")
)

TABLESPACE pg_default;

ALTER TABLE public."Payments"
    OWNER to postgres;

INSERT INTO public."Payments"(
	"User_Id", "Bike_Model", "Remaining", "Paid", "Total_Cost")
	VALUES (1, 0000, 81, 162, 243),
	(2, 0001, 0, 123, 123),
	(3, 0100, 246, 123, 369);