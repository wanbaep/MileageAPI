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