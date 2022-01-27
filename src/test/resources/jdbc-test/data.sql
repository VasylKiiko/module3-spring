INSERT INTO User_table(user_name, email) VALUES
('Vasyl', 'f@mail.com'),
('Diana', 't@mail.com'),
('Vasyl', 'c@mail.com'),
('Oleg', 'b@mail.com'),
('Oleg', 'e@mail.com'),
('Vasyl', 'a@mail.com'),
('Vasyl', 'd@mail.com'),
('Vasyl', 'test8@mail.com'),
('Diana', 'test9@mail.com'),
('Sofia', 'sofia@gmail.com');

INSERT INTO event(title, date, ticketprice) VALUES
('Movie "Stars"', '2022-01-21', 10),
('Movie "Star Wars"', '2022-01-22', 20),
('Movie "Lord of the rings"', '2022-01-23', 30),
('Movie "Lords"', '2022-01-24', 40),
('Exhibition "Stars"', '2022-01-26', 50),
('Movie "Port 8080"', '2022-01-26', 60),
('Exhibition "Winter"', '2022-05-07', 70);

INSERT INTO ticket(category, place, event_id, user_id) VALUES
('STANDARD', 1, 1, 2),
('PREMIUM', 2, 1, 1),
('STANDARD', 3, 1, 5),
('PREMIUM', 1, 3, 2),
('PREMIUM', 2, 4, 1),
('STANDARD', 1, 4, 1),
('BAR', 1, 2, 3),
('PREMIUM', 5, 2, 4),
('STANDARD', 10, 1, 1);

INSERT INTO useraccount(id, password, amount, user_id) VALUES
( 1234, 'password', 1000.0, 1 ),
( 4321, 'pass', 200, 2);
