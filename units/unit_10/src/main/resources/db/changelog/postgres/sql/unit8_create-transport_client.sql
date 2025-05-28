create table unit8.transport
(
    id                  UUID not null primary key,
    version             int8 not null default 0,
    gos_reg_num         varchar(12) not null,   --государственный регистрационный номер транспортного средства
    model_id            UUID not null,  --марка + модель
    foreign key (model_id) references unit8.transport_model (id),
    constraint transport_uk unique (gos_reg_num)
);
CREATE INDEX index_transport_gos_reg_num ON unit8.transport USING btree(gos_reg_num ASC NULLS LAST); --потенциально, должен часто юзаться в поиске
CREATE INDEX index_transport_model_id ON unit8.transport USING btree(model_id ASC NULLS LAST); --внешний ключ
-- start M:M client:transport
create table unit8.client_transport
(
    id                  UUID not null default gen_random_uuid() primary key,
    version             int8 not null default 0,
    client_id           UUID not null,
    transport_id        UUID not null,
    foreign key (client_id) references unit8.client (id) ON DELETE CASCADE,
    foreign key (transport_id) references unit8.transport (id) ON DELETE CASCADE,
    constraint client_transport_uk unique (client_id, transport_id)
);
CREATE INDEX index_client_transport ON unit8.client_transport USING btree(client_id, transport_id ASC NULLS LAST); --внешние ключи
-- end M:M client:transport