
alter table vitrinis add annotation text;

UPDATE db_version SET version=5 where _id=1;