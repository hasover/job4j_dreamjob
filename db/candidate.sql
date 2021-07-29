CREATE TABLE candidate (
    id SERIAL PRIMARY KEY,
    name TEXT,
    city_id int references city(id)
);