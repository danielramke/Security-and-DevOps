create table if not exists item (
    id long not null auto_increment,
    name varchar,
    description varchar,
    price decimal,
    primary key (id)
);