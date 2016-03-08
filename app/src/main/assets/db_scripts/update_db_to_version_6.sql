
alter table vitrinis add email text;
alter table vitrinis add telephone text;

UPDATE db_version SET version=6 where _id=1;