-- start client
create table unit7.client
(
    id          UUID not null,
    version     int8 not null default 0,
    last_name   varchar(255) not null,
    first_name  varchar(255) not null,
    middle_name varchar(255),
    city        varchar(255),
    primary key (id)
);
-- end client
-- start transport
create table unit7.transport_brand
(
    id                  UUID not null,
    version             int8 not null default 0,
    brand               varchar(255) not null,
    primary key (id),
    constraint brand_uk unique (brand)
);
CREATE INDEX index_brand ON unit7.transport_brand USING btree(brand ASC NULLS LAST); --часто будет юзаться в поиске
create table unit7.transport_model
(
    id                  UUID not null,
    version             int8 not null default 0,
    model               varchar(255) not null,
    primary key (id),
    constraint model_uk unique (model)
);
CREATE INDEX index_model ON unit7.transport_model USING btree(model ASC NULLS LAST); --часто будет юзаться в поиске
create table unit7.transport_model_brand
(
    id                  UUID not null,
    version             int8 not null default 0,
    model_id            UUID not null,
    brand_id            UUID not null,
    primary key (id),
    foreign key (brand_id) references unit7.transport_brand (id) ON DELETE CASCADE,
    foreign key (model_id) references unit7.transport_model (id) ON DELETE CASCADE,
    constraint model_brand_uk unique (model_id, brand_id)
);
CREATE INDEX index_model_brand ON unit7.transport_model_brand USING btree(brand_id, model_id ASC NULLS LAST); --внешние ключи
create table unit7.transport
(
    id                  UUID not null,
    version             int8 not null default 0,
    gos_reg_num         varchar(12) not null,   --государственный регистрационный номер транспортного средства
    model_id            UUID not null,  --марка + модель
    primary key (id),
    foreign key (model_id) references unit7.transport_model (id)
    constraint transport_uk unique (gos_reg_num)
);
CREATE INDEX index_transport_gos_reg_num ON unit7.transport USING btree(gos_reg_num ASC NULLS LAST); --потенциально, должен часто юзаться в поиске
CREATE INDEX index_transport_model_id ON unit7.transport USING btree(model_id ASC NULLS LAST); --внешний ключ
-- start M:M client:transport
create table unit7.client_transport
(
    id                  UUID not null,
    version             int8 not null default 0,
    client_id           UUID not null,
    transport_id        UUID not null,
    primary key (id),
    foreign key (client_id) references unit7.client (id) ON DELETE CASCADE,
    foreign key (transport_id) references unit7.transport (id) ON DELETE CASCADE,
    constraint client_transport_uk unique (client_id, transport_id)
);
CREATE INDEX index_client_transport ON unit7.client_transport USING btree(client_id, transport_id ASC NULLS LAST); --внешние ключи
-- end M:M client:transport