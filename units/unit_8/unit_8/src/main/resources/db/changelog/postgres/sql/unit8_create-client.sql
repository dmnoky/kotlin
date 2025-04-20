create table unit8.client
(
    id          UUID not null primary key,
    version     int8 not null default 0,
    delete_at   date, --soft-delete
    last_name   varchar(255) not null,
    first_name  varchar(255) not null,
    middle_name varchar(255),
    city        varchar(255)
);
CREATE INDEX index_client_delete_at ON unit8.client USING btree(delete_at ASC NULLS LAST); --часто будет юзаться в поиске