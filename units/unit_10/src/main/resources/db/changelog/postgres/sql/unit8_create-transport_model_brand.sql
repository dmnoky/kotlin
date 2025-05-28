create table unit8.transport_brand
(
    id                  UUID not null primary key,
    version             int8 not null default 0,
    brand               varchar(255) not null,
    constraint brand_uk unique (brand)
);
CREATE INDEX index_brand ON unit8.transport_brand USING btree(brand ASC NULLS LAST); --часто будет юзаться в поиске
create table unit8.transport_model
(
    id                  UUID not null primary key,
    version             int8 not null default 0,
    model               varchar(255) not null,
    brand_id            UUID not null,
    foreign key (brand_id) references unit8.transport_brand (id),
    constraint model_uk unique (brand_id, model)
);
CREATE INDEX index_model ON unit8.transport_model USING btree(model ASC NULLS LAST); --часто будет юзаться в поиске
CREATE INDEX index_model_brand_id ON unit8.transport_model USING btree(brand_id ASC NULLS LAST); --внешний ключ