ALTER TABLE city
ADD COLUMN coord_x DOUBLE PRECISION,
ADD COLUMN coord_y DOUBLE PRECISION;

ALTER TABLE forecast
ALTER COLUMN temperature TYPE DOUBLE PRECISION,
ADD COLUMN wind_speed DOUBLE PRECISION;