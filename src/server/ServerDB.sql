use master
go
if DB_ID ('ServerDB') is not null
	drop database ServerDB
go
create database ServerDB
go
use ServerDB

create table UserAccount
(
	ID INT IDENTITY(1,1) NOT NULL,
    StringPrefix NVARCHAR(10) NOT NULL,
    IDandPrefix AS ISNULL(StringPrefix + CAST(ID AS NVARCHAR(10)), 'X') PERSISTED,

	Username nvarchar(20),
	AvatarImageBytes varbinary(max),
	UserPassword nvarchar(20),
	UserDOB Date,
	UserPhoneNumber nvarchar(12),
	UserEmail nvarchar(20),
	isAdmin bit,
	Unique (UserEmail)
)



ALTER TABLE dbo.UserAccount 
ADD CONSTRAINT PK_UserAccount PRIMARY KEY CLUSTERED (IDandPrefix)


create table Event
(
	ID INT IDENTITY(1,1) NOT NULL,
    StringPrefix NVARCHAR(10) NOT NULL,
    IDandPrefix AS ISNULL(StringPrefix + CAST(ID AS NVARCHAR(10)), 'X') PERSISTED,

	EventName nvarchar(20),
	EventDescription nvarchar(max),
	EventDate Date,
	EventGoal int,
	CurrentMoney int,
	EventRate float,
	isEnd bit,
	HostID nvarchar(20)
)

alter table Event
add
	constraint fk_Event_UserAccount
	foreign key (HostID)
	references UserAccount(IDandPrefix)



ALTER TABLE dbo.Event 
ADD CONSTRAINT PK_Event PRIMARY KEY CLUSTERED (IDandPrefix)



create table EventPhoto
(
	EventID int,
	OrderNumber int,
	ImageBytes varbinary(max)
	constraint pk_photo
	primary key (EventID, OrderNumber)
)


create table Donor
(
	DonorName nvarchar(50),
	DonorBankAccount nvarchar(10),
	DonorID nvarchar(20)
	constraint pk_Donor
	primary key (DonorID, DonorBankAccount)
)

create table Share
(
	SocialMediaID nvarchar(20),
	ShareContent nvarchar(max),
	ShareDate Date
)

create table Comment
(
	CommentID nvarchar(20),
	CommentContent nvarchar(max),
	CommentDate Date
	constraint pk_commentId
	primary key (CommentID)
)

alter table Donor
add
	constraint fk_Donor_UserAccount
	foreign key (DonorID)
	references UserAccount(IDandPrefix)

alter table Comment
add
	constraint fk_Comment_UserAccount
	foreign key (CommentID)
	references UserAccount(IDandPrefix)

create table Follow 
(
	UserID nvarchar(20) NOT NULL,
	EventID nvarchar(20) NOT NULL,
    FOREIGN KEY (UserID) REFERENCES UserAccount(IDandPrefix), 
    FOREIGN KEY (EventID) REFERENCES Event(IDandPrefix),
    UNIQUE (UserID, EventID)
)


INSERT INTO UserAccount(StringPrefix,Username,UserPassword,UserDOB,UserPhoneNumber,UserEmail,isAdmin)
	VALUES ('US',N'DUY','123',convert(datetime,'18-06-12 10:34:09 PM',5),'1213123','nt@asdasd.com',0);

INSERT INTO UserAccount(StringPrefix,Username,UserPassword,UserDOB,UserPhoneNumber,UserEmail,isAdmin)
	VALUES ('US',N'Kim','123',convert(datetime,'18-06-12 10:34:09 PM',5),'1213123','nt11@asdasd.com',0);
INSERT INTO UserAccount(StringPrefix,Username,UserPassword,UserDOB,UserPhoneNumber,UserEmail,isAdmin)
	VALUES ('US',N'Lam','123',convert(datetime,'18-06-12 10:34:09 PM',5),'1213123','n2t@asdasd.com',0);

INSERT INTO Event(StringPrefix,EventName,EventDescription,EventDate,EventGoal,CurrentMoney,EventRate,isEnd,HostID)
	Values ('E','Donation for MidSite','This event needed fund',convert(datetime,'18-06-12 10:34:09 PM',5),100000,50000,5.0,0,'US1')

INSERT INTO Event(StringPrefix,EventName,EventDescription,EventDate,EventGoal,CurrentMoney,EventRate,isEnd,HostID)
	Values ('E','Donation for Duy','This event needed fund',convert(datetime,'18-06-12 10:34:09 PM',5),200000,10000,5.0,0,'US2')

INSERT INTO Follow(UserID,EventID)
	VALUES ('US1','E1');

INSERT INTO Follow(UserID,EventID)
	VALUES ('US1','E2');

INSERT INTO Follow(UserID,EventID)
	VALUES ('US2','E1');

INSERT INTO Follow(UserID,EventID)
	VALUES ('US3','E1');




