/*******************************************************************************
Joseph Gonzales
BankProject 2
*******************************************************************************/
DROP USER bankuser CASCADE;
/*******************************************************************************
   Create database
********************************************************************************/
CREATE USER bankuser
IDENTIFIED BY J#db$pgW
DEFAULT TABLESPACE users
TEMPORARY TABLESPACE temp
QUOTA 10M ON users;

GRANT connect to bankuser;
GRANT resource to bankuser;
GRANT create session TO bankuser;
GRANT create table TO bankuser;
GRANT create view TO bankuser;



conn bankuser/J#db$pgW;


/*******************************************************************************
   Create Tables
********************************************************************************/
CREATE TABLE BankUser
(
    UserId NUMBER NOT NULL,
    UserName VARCHAR2(160) NOT NULL,
    UserEmail VARCHAR2(160) NOT NULL,
    UserPassword VARCHAR2 NOT NULL,
    Status number,
    CONSTRAINT PK_User PRIMARY KEY (UserId)
);

CREATE TABLE BankAccount
(
    AccountId NUMBER NOT NULL,
    UserId number not null,
    balance number(8,2) not null,
    AccountType VARCHAR2(32) NOT NULL,
    CONSTRAINT PK_Account PRIMARY KEY (AccountId)
);

ALTER TABLE BankAccount ADD CONSTRAINT FK_UserAccountId
    FOREIGN KEY (UserId) REFERENCES BankUser (UserId);

/*******************************************************************************
Create default user for testing
*********************************************************************!w3d1d1tR3dd1t!**********/
INSERT INTO BankUser (UserId, UserName, UserEmail, UserPassword, Status) VALUES (1, 'DEFAULT', 'DEFAULT@chiru.no', '4c51ff40a043ceed97a72d6408147399ebabf82a335f855164dd3b95e8139529', 2);
INSERT INTO BankAccount (AccountId, UserId, balance, AccountType) VALUES (1, 1, 0.00, 'checking');

commit;

savepoint before_procedures;
create or replace procedure get_user_accounts(
    s out SYS_REFCURSOR
)
is
begin
    open s for
    SELECT AccountID, Balance, AccountType
    from BankAccount; 
end;
/
var s  refcursor;
select GET_USER_ACCOUNTS(:s) from BankAccount;


create sequence userID_seq start with 500 increment by 1 cache 100;
create or replace trigger id_increment
before insert on BankUser
for each row
begin
    :new.UserID := userID_seq.nextval;
end;
/
commit;

create sequence acctID_seq start with 400 increment by 1 cache 100;
create or replace trigger account_id_increment
before insert on BankAccount
for each row
begin
    if :new.AccountID is null then
        select acctID_seq.nextval into :new.AccountID from dual;
    end if;
end;
/
commit;

exit;