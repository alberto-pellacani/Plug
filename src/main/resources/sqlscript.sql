CREATE TABLE number_md (

    tenant   int not null,
    id          varchar(15) not null,
    start_at    date not null,
    end_at      date,
    format      varchar(15),
    next_idx    int not null,
    offset_idx  int not null default(1),
    last_number varchar(15),
    created_at  timestamp,
    updated_at  timestamp,

    constraint number_md_pk primary key (tenant, id, start_at)

);

CREATE TABLE user_md (

    id     int not null ,
    name   varchar(150) not null unique,
    pwd    varchar(32) not null,
    description varchar(150),

    created_at timestamp,
    updated_at timestamp,

    constraint user_md_pk primary key (id)


);

create index user_md_name on user_md (name);


CREATE TABLE tenant_md (
    tenant_id   int not null,
    name        varchar(150),

    constraint tenant_md_pk primary key (tenant_id)

);


CREATE TABLE user_tenant (
    user_id     int not null,
    tenant_id   int not null,

    constraint user_tenant_pk primary key (user_id, tenant_id)

);