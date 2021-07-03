CREATE TABLE membership (
     seq int,
     membershipId varchar(255),
     userId varchar(255),
     membershipName varchar(255),
     startDate datetime,
     membershipStatus varchar(255),
     point varchar(255)
);
--
-- INSERT INTO membership(seq, membershipId, userId, membershipName, startDate, membershipStatus, point)
-- VALUES ('1', 'spc', 'test1', 'sinshegae', NOW(), 'Y', '1000');
-- INSERT INTO membership(membership_Id, user_Id, membership_Name, membership_Status, point) VALUES ('spc', 'test1', 'happypoint', 'Y', 1000);
-- INSERT INTO membership(membership_Id, user_Id, membership_Name, membership_Status, point) VALUES ('shinsegae', 'test1', 'shinsegaepoint', 'Y', 3500);
-- INSERT INTO membership(membership_Id, user_Id, membership_Name, membership_Status, point) VALUES ('cj', 'test1', 'cjone', 'Y', 3500);


-- INSERT INTO membership(membership_Id, user_Id, membership_Name, membership_Status, point) VALUES
('spc', 'test1', 'happypoint', 'Y', 1000),
('shinsegae', 'test1', 'shinsegaepoint', 'Y', 3500),
('cj', 'test1', 'cjone', 'Y', 3500);