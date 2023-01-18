-- Table: public.cluster

-- DROP TABLE IF EXISTS public.cluster;

CREATE TABLE IF NOT EXISTS public.cluster
(
    id double precision NOT NULL,
    service character varying(255) COLLATE pg_catalog."default",
    cluster_name character varying(255) COLLATE pg_catalog."default",
    CONSTRAINT cluster_pkey PRIMARY KEY (id)
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.cluster
    OWNER to ocloud_cost_dev1;
	

-- Table: public.cluster
INSERT INTO public.cluster(
	id, cluster_name, service)
	VALUES (1, 'Vm1', 'kuberbetes'),
    (2, 'Kube-Test-', 'kuberbetes'),
    (3, 'dpg-dpg-1-dpg', 'Rancher');


-- Table: public.image

-- DROP TABLE IF EXISTS public.image;

CREATE TABLE IF NOT EXISTS public.image
(
    id double precision NOT NULL,
    image_ref character varying(255) COLLATE pg_catalog."default",
    service character varying(255) COLLATE pg_catalog."default",
    CONSTRAINT image_pkey PRIMARY KEY (id)
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.image
    OWNER to ocloud_cost_dev1;

	
-- Table: public.image
INSERT INTO public.image(
	id, image_ref, service)
	VALUES (1, '5ebcca42-c40a-4c93-8a7f-3c58798d4194', 'kubernetes'),
    (2,'2fb7cde2-a17d-4bb7-bac7-9c22d62c7595', 'kuberbetes'),
    (3, '1f0bc61e-68b1-40a9-96d1-d8469ec5ffaa', 'kuberbetes');


-- Table: public.price

-- DROP TABLE IF EXISTS public.price;

CREATE TABLE IF NOT EXISTS public.price
(
    id bigint NOT NULL,
    flavor_name character varying(255) COLLATE pg_catalog."default",
    hourly_rate double precision,
    CONSTRAINT listino_pkey PRIMARY KEY (id)
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.price
    OWNER to ocloud_cost_dev1;


-- Table: public.price
INSERT INTO public.price(
	id, flavor_name, hourly_rate)
	VALUES (1, 'e3.standard.x1', 0.011),
    (2, 'e3.standard.x2', 0.019),
    (3, 'e3.standard.x3', 0.034),
    (4, 'e3.standard.x4', 0.062),
    (5, 'e3.standard.x5', 0.0111),
    (6, 'e3.standard.x6', 0.0199),
    (7, 'e3.standard.x7', 0.0359),
    (8, 'e3.standard.x8', 0.0646),
    (9, 'e3.standard.x9', 1.162);
	
-- Table: public.usage_hour

-- DROP TABLE IF EXISTS public.usage_hour;

CREATE TABLE IF NOT EXISTS public.usage_hour
(
    id bigint NOT NULL,
    cluster_name character varying(255) COLLATE pg_catalog."default",
    cost numeric(38,2),
    resource_total integer,
    time_slot timestamp(6) without time zone,
    CONSTRAINT usage_hour_pkey PRIMARY KEY (id),
    CONSTRAINT ukmc8xiyki6rn4afo0ftf0rtsfa UNIQUE (cluster_name, time_slot)
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.usage_hour
    OWNER to ocloud_cost_dev1;


-- Table: public.service

-- DROP TABLE IF EXISTS public.service;

CREATE TABLE IF NOT EXISTS public.service
(
    id double precision NOT NULL,
    type character varying(255) COLLATE pg_catalog."default",
    CONSTRAINT service_pkey PRIMARY KEY (id)
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.service
    OWNER to ocloud_cost_dev1;