INSERT INTO Status(
	id, name)
	VALUES
	(1, 'Available'),
	(2, 'Owned');

INSERT INTO user_role(
	id, name)
	VALUES
	(1, 'Manager'),
	(2, 'Employee'),
	(3, 'Customer');

INSERT INTO Registered_Users(
	id, name, passwd, rle)
	VALUES
	(1, 'crystal', 'pass', 1),
	(2, 'tyler', 'pass', 2),
	(3, 'ashley', 'pass', 3),
	(4, 'clark', 'pass', 3);

insert into Bikes
	values
	(1, 'Edge', 'Nischi', 'grey', 2),
	(2, 'BMX', 'Mongoose', 'black', 2),
	(3, 'Mountain', 'Schwinn', 'white', 2),
	(4, 'BMX', 'Nischi', 'purple', 1),
	(5, 'Mountain', 'Schwinn', 'black', 1),
	(6, 'BMX', 'Mongoose', 'white', 2);

insert into offers
	values
	(1, 120.11),
	(2, 105.40),
	(3, 152.50),
	(4, 73.74);

insert into offers_log
	values
	(1, 1, 4),
	(2, 2, 4),
	(3, 3, 5),
	(4, 1, 5);

insert into payments
	values
	(1, 240.10, 120),
	(2, 305.75, 200),
	(3, 102.25, 52.25);

insert into payments_log
	values
	(1, 1, 2),
	(2, 1, 1),
	(3, 3, 3);

