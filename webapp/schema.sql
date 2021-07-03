CREATE TABLE membership (
    seq bigint not null auto_increment,
    membershipId varchar(255),
    userId varchar(255),
    membershipName varchar(255),
    startDate datetime,
    membershipStatus varchar(255),
    point varchar(255)
) engine=InnoDB;